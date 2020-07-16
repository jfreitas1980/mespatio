
package com.hdntec.gestao.services.controladores.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.numeros.DSSStockyardFuncoesNumeros;

import com.hdntec.gestao.domain.blendagem.Blendagem;
import com.hdntec.gestao.domain.blendagem.ControladorCalculoQualidade;
import com.hdntec.gestao.domain.navios.entity.MetaCarga;
import com.hdntec.gestao.domain.navios.entity.status.Carga;
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.entity.LugarEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.enums.SentidoEmpilhamentoRecuperacaoEnum;
import com.hdntec.gestao.domain.plano.enums.TipoAtividadeEnum;
import com.hdntec.gestao.domain.plano.enums.TipoMaquinaEnum;
import com.hdntec.gestao.domain.planta.comparadores.ComparadorBalizas;
import com.hdntec.gestao.domain.planta.entity.MetaBaliza;
import com.hdntec.gestao.domain.planta.entity.MetaCorreia;
import com.hdntec.gestao.domain.planta.entity.MetaFiltragem;
import com.hdntec.gestao.domain.planta.entity.MetaMaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.MetaUsina;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.Campanha;
import com.hdntec.gestao.domain.planta.entity.status.Correia;
import com.hdntec.gestao.domain.planta.entity.status.MaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Pilha;
import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;
import com.hdntec.gestao.domain.produto.entity.Amostra;
import com.hdntec.gestao.domain.produto.entity.Produto;
import com.hdntec.gestao.domain.produto.entity.Qualidade;
import com.hdntec.gestao.domain.produto.entity.Rastreabilidade;
import com.hdntec.gestao.domain.vo.atividades.MovimentacaoVO;
import com.hdntec.gestao.exceptions.AtividadeException;
import com.hdntec.gestao.services.controladores.IControladorExecutarAtividadeMovimentacaoPilha;

/**
 * <P><B>Description :</B><BR>
 * General ControladorExecutarAtividadeMovimentacaoPilha
 * </P>
 * <P>
 * <B>
 * SAs : <BR>
 * 8148
 * </B>
 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
 * @since 28/10/2010
 * @version $Revision: 1.1 $
 */
public class ControladorExecutarAtividadeMovimentacaoPilha implements IControladorExecutarAtividadeMovimentacaoPilha {

    private static ControladorExecutarAtividadeMovimentacaoPilha instance = null;

    /**
     * Construtor privado.
     */
    public ControladorExecutarAtividadeMovimentacaoPilha() {
    }

    /**
     * Retorna a instancia singleton da fAbrica.
     * 
     * @return TransLogicDAOFactory
     */
    public static ControladorExecutarAtividadeMovimentacaoPilha getInstance() {
        if (instance == null) {
            instance = new ControladorExecutarAtividadeMovimentacaoPilha();
        }
        return instance;
    }

    /* (non-Javadoc)
     * @see com.hdntec.gestao.services.controladores.IControladorExecutarAtividadeMovimentacaoPilha#movimentar(com.hdntec.gestao.domain.vo.atividades.MovimentacaoVO)
     */
    public Atividade movimentar(MovimentacaoVO movimentacaoVO) throws AtividadeException {
        Pilha pilhaAtualOrigem = null;
        Pilha pilhaAtualDestino = null;
        // TODO Auto-generated method stub
        List<Baliza> balizasOrigem = new ArrayList<Baliza>();
        List<Baliza> balizasDestino = new ArrayList<Baliza>();
        List<MaquinaDoPatio> maquinas = new ArrayList<MaquinaDoPatio>();
        List<Pilha> pilhasEditadas = new ArrayList<Pilha>();
        List<Correia> correias = new ArrayList<Correia>();
        Boolean finalizarAtividade = Boolean.FALSE;
        Atividade atividade = new Atividade();
        atividade.setTipoAtividade(movimentacaoVO.getTipoAtividade());
        LugarEmpilhamentoRecuperacao lugarEmpRecOrigem = new LugarEmpilhamentoRecuperacao();
        LugarEmpilhamentoRecuperacao lugarEmpRecDestino = new LugarEmpilhamentoRecuperacao();
        Carga novoStatusCarga = null;

        try {
            
            /***
             * Valida se existem itens sob manutenção/interdição
             * 
             */
            validarManutencaoInterdicao(movimentacaoVO);
                        
            /* verifica se deve finalizar a operacao */
            if (movimentacaoVO.getDataFim() != null) {
                
            	  MetaBaliza metaBalizaOperacao = movimentacaoVO.getListaBalizas().get(0);
                  
                  Baliza balizaOperacao = metaBalizaOperacao.retornaStatusHorario(movimentacaoVO.getDataFim());
                  
                  int idxGerado = metaBalizaOperacao.getListaStatus().indexOf(balizaOperacao);
                  
                  balizaOperacao = metaBalizaOperacao.getListaStatus().get(idxGerado-1);
                
            	  pilhaAtualOrigem =balizaOperacao.retornaStatusHorario(movimentacaoVO.getDataInicio()); 
            		//movimentacaoVO.getListaBalizas().get(0).retornaStatusHorario(movimentacaoVO.getDataFim())
                                //.retornaStatusHorario(movimentacaoVO.getDataFim());
                
            	
            	if (movimentacaoVO.getListaBalizasDestino() != null && movimentacaoVO.getListaBalizasDestino().size() > 0) {
                    pilhaAtualDestino = movimentacaoVO.getListaBalizasDestino().get(0).retornaStatusHorario(
                                    movimentacaoVO.getDataFim()).retornaStatusHorario(movimentacaoVO.getDataFim());
                }
                
                
                finalizarAtividade = Boolean.TRUE;
            } else {
                pilhaAtualOrigem = movimentacaoVO.getListaBalizas().get(0).retornaStatusHorario(
                                movimentacaoVO.getDataInicio()).retornaStatusHorario(movimentacaoVO.getDataInicio());

                if (movimentacaoVO.getListaBalizasDestino() != null && movimentacaoVO.getListaBalizasDestino().size() > 0) {
                    pilhaAtualDestino = movimentacaoVO.getListaBalizasDestino().get(0).retornaStatusHorario(
                                    movimentacaoVO.getDataInicio()).retornaStatusHorario(movimentacaoVO.getDataInicio());
                }
            }

            if (finalizarAtividade) {
                atividade.setDtInicio(movimentacaoVO.getDataInicio());
                atividade.setDtFim(movimentacaoVO.getDataFim());
                atividade.setDtInsert(new Date(System.currentTimeMillis()));
                atividade.setIdUser(1L);
                atividade.setFinalizada(true);
                lugarEmpRecOrigem.setDtFim(movimentacaoVO.getDataFim());
                lugarEmpRecOrigem.setExecutado(Boolean.TRUE);
                lugarEmpRecDestino.setDtFim(movimentacaoVO.getDataFim());
                lugarEmpRecDestino.setExecutado(Boolean.TRUE);
            } else {
                atividade.setDtInicio(movimentacaoVO.getDataInicio());
                atividade.setDtInsert(new Date(System.currentTimeMillis()));
                atividade.setIdUser(1L);
            }
            atividade.setAtividadeAnterior(movimentacaoVO.getAtividadeAnterior());

            /**
             1- aplica atividade nas maquinas
            */
            List<SentidoEmpilhamentoRecuperacaoEnum> sentidos = new ArrayList<SentidoEmpilhamentoRecuperacaoEnum>();
            sentidos.add(movimentacaoVO.getSentidoRecuperacao());
            sentidos.add(movimentacaoVO.getSentidoEmpilhamento());

            // aplica atividade nas balizas e retorna  a lista de novos estados
            balizasOrigem = MetaBaliza.gerarAtividadeMovimentacaoRecuperacaoBalizas(movimentacaoVO);

            if (movimentacaoVO.getListaBalizasDestino() != null && movimentacaoVO.getListaBalizasDestino().size() > 0) {
                balizasDestino = MetaBaliza.gerarAtividadeMovimentacaoEmpilhamentoBalizas(movimentacaoVO);

            } else if (movimentacaoVO.getMetaCarga() != null) {
                novoStatusCarga = MetaCarga.gerarAtividadeCarga(movimentacaoVO);

            }

            List<LugarEmpilhamentoRecuperacao> lugarAnterior = new ArrayList<LugarEmpilhamentoRecuperacao>();
            MetaMaquinaDoPatio.gerarAtividadeMaquina(movimentacaoVO, lugarAnterior, balizasOrigem, balizasDestino, maquinas,
                            correias, atividade, finalizarAtividade);

            if (finalizarAtividade) {

                for (MaquinaDoPatio novaStatusMaquina : maquinas) {

                    if (novaStatusMaquina.getMetaMaquina().getTipoDaMaquina().equals(TipoMaquinaEnum.EMPILHADEIRA)
                                    || novaStatusMaquina.getMetaMaquina().getTipoDaMaquina().equals(
                                                    TipoMaquinaEnum.EMPILHADEIRA_RECUPERADORA)) {

                        Atividade atv = novaStatusMaquina.getMetaMaquina().existeEmpilhamentoPendente();

                        if (atv != null) {
                            novaStatusMaquina.setEstado(EstadoMaquinaEnum.OPERACAO);
                            novaStatusMaquina.setAtividade(atv);
                        }
                    }
                }
            }

            if (movimentacaoVO.getDataFim() == null) {
                // cria a pilha destino
                // cria a pilha destino

                Pilha novaPilhaOrigem = Pilha.criaPilha(movimentacaoVO.getNomePilha(), balizasOrigem, movimentacaoVO
                                .getCliente(), movimentacaoVO.getDataInicio().getTime(), pilhaAtualOrigem);
                //if (pilhaAtualOrigem != null)

                   // novaPilhaOrigem.addBaliza(pilhaAtualOrigem.getListaDeBalizas());

                if (balizasDestino != null && balizasDestino.size() > 0) {
                    Pilha novaPilhaDestino = Pilha
                                    .criaPilha(movimentacaoVO.getNomePilhaDestino(), balizasDestino, movimentacaoVO
                                                    .getCliente(), movimentacaoVO.getDataInicio().getTime(),
                                                    pilhaAtualDestino);
                    if (pilhaAtualDestino != null) {                        
                    	                    	
                    	novaPilhaDestino.addBaliza(pilhaAtualDestino.getListaDeBalizas());
                    	
                    	Pilha.decompoePilhaEditada(novaPilhaDestino, movimentacaoVO.getDataInicio());
                    	
                    }	
                }

            }

            /**
             * 2 - efetua a movimentacao do material 
             */


            if (movimentacaoVO.getTipoAtividade().equals(TipoAtividadeEnum.RETORNO_PELLET_FEED)) {
            	pilhasEditadas.addAll(realizarRetornoPellet(movimentacaoVO, balizasOrigem, pilhaAtualOrigem));
            } else {	
            	pilhasEditadas.addAll(realizarMovimentacaoPilhaPilha(movimentacaoVO, balizasOrigem, balizasDestino, pilhaAtualOrigem,
    					pilhaAtualDestino));
            	
            	pilhasEditadas.addAll(realizarMovimentacaoPilhaCarga(movimentacaoVO, balizasOrigem, novoStatusCarga, pilhaAtualOrigem,
                        pilhaAtualDestino));
            	
            }
            	
            /**
             *  ATUALIZACAO DE STATUS OCIOSO DA CORREIA 
             * 
             */
            if (movimentacaoVO.getDataFim() != null) {
                for (Correia correia : correias) {
                    correia.setEstado(MetaCorreia.atualizaStatusOcioso(EstadoMaquinaEnum.OCIOSA,
                                    movimentacaoVO.getDataFim(), correia.getMetaCorreia()));
                }

            } else {
                for (Correia correia : correias) {
                    correia.setEstado(MetaCorreia.atualizaStatusOcioso(EstadoMaquinaEnum.OCIOSA, movimentacaoVO
                                    .getDataInicio(), correia.getMetaCorreia()));
                }
            }

            /**
             * 3 - cria os lugares de empilhamento e recuperacao
             */
            lugarEmpRecOrigem.setDtFim(movimentacaoVO.getDataFim());
            lugarEmpRecOrigem.setOrdem(0);
            lugarEmpRecOrigem.setIdUser(1L);
            lugarEmpRecOrigem.setDtInicio(movimentacaoVO.getDataInicio());
            lugarEmpRecOrigem.setQuantidade(movimentacaoVO.getQuantidadeMovimentacao());
            lugarEmpRecOrigem.addMaquinaDoPatio(maquinas.get(0));
            lugarEmpRecOrigem.addBaliza(balizasOrigem);
            lugarEmpRecOrigem.setTipoProduto(movimentacaoVO.getTipoProduto());
            lugarEmpRecOrigem.setNomeDoLugarEmpRec(movimentacaoVO.getNomePilha());
            lugarEmpRecOrigem.addPilhaEditada(pilhasEditadas);
            atividade.addLugarEmpilhamento(lugarEmpRecOrigem);
            if ((balizasDestino != null && balizasDestino.size() > 0) || novoStatusCarga != null) {

                lugarEmpRecDestino.setDtFim(movimentacaoVO.getDataFim());
                lugarEmpRecDestino.setIdUser(1L);
                lugarEmpRecDestino.setDtInicio(movimentacaoVO.getDataInicio());
                if (maquinas.size() > 1) {
                    lugarEmpRecDestino.addBaliza(balizasDestino);
                    lugarEmpRecDestino.setNomeDoLugarEmpRec(movimentacaoVO.getNomePilhaDestino());
                    lugarEmpRecDestino.addMaquinaDoPatio(maquinas.get(1));
                    lugarEmpRecDestino.addCorreia(correias);
                } else if (novoStatusCarga != null) {
                    lugarEmpRecDestino.setNomeDoLugarEmpRec(movimentacaoVO.getTxtNumeroPorao());
                    lugarEmpRecDestino.setNomePorao(movimentacaoVO.getTxtNumeroPorao());
                    lugarEmpRecDestino.addCarga(novoStatusCarga);

                }
                lugarEmpRecDestino.setQuantidade(movimentacaoVO.getQuantidadeMovimentacao());
                lugarEmpRecDestino.setTipoProduto(movimentacaoVO.getTipoProduto());
                lugarEmpRecDestino.setOrdem(1);
                atividade.addLugarEmpilhamento(lugarEmpRecDestino);
            }
        }catch (AtividadeException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return atividade;

    }

	private void retornarPelletFeed(MovimentacaoVO movimentacaoVO,
			Pilha pilhaAtualOrigem, List<Baliza> balizasOrigem)
			throws AtividadeException {
		List<Baliza> listaBalizasSemProduto = new ArrayList<Baliza>();
		retiraMaterialDaPilhaOrigem(movimentacaoVO, balizasOrigem, null, null, movimentacaoVO.getQuantidadeMovimentacao(), pilhaAtualOrigem, listaBalizasSemProduto);
	}

    /**
     * validarManutencaoInterdicao
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 25/01/2011
     * @see
     * @param 
     * @return void
     */
    private void validarManutencaoInterdicao(MovimentacaoVO movimentacaoVO) throws AtividadeException {
        Boolean interditada = Boolean.FALSE;
        StringBuffer strInterditadas = new StringBuffer();
        Date dataInterdicao = movimentacaoVO.getDataInicio();
        if (movimentacaoVO.getDataFim() != null ) {
            dataInterdicao = movimentacaoVO.getDataFim(); 
        }   
        
        for (MetaBaliza novaBaliza : movimentacaoVO.getListaBalizasDestino()) {            
            if (novaBaliza.balizaInterditado(dataInterdicao)) {
                interditada = Boolean.TRUE;
                strInterditadas.append(novaBaliza.getNomeBaliza()).append("\n");                       
            }
        }
        
        // Maquina 
        for (MetaMaquinaDoPatio novaBaliza : movimentacaoVO.getListaMaquinas()) {            
            if (novaBaliza.maquinaInterditado(dataInterdicao)) {
                interditada = Boolean.TRUE;
                strInterditadas.append(novaBaliza.getNomeMaquina()).append("\n");                       
            }
        }

        // Correias Máquinas 
        for (MetaMaquinaDoPatio novaBaliza : movimentacaoVO.getListaMaquinas()) {            
            if (!novaBaliza.getTipoDaMaquina().equals(TipoMaquinaEnum.PA_CARREGADEIRA)) {
                if (novaBaliza.getMetaCorreia().correiaInterditado(dataInterdicao)) {
                    interditada = Boolean.TRUE;
                    strInterditadas.append(novaBaliza.getMetaCorreia().getNomeCorreia()).append("\n");                       
                }
            }    
        }
        
        
        //Correias Usinas  
        for (MetaUsina novaBaliza : movimentacaoVO.getListaUsinas()) {                           
                if (novaBaliza.getMetaCorreia().correiaInterditado(dataInterdicao)) {
                    interditada = Boolean.TRUE;
                    strInterditadas.append(novaBaliza.getMetaCorreia().getNomeCorreia()).append("\n");                       
                }
         }
        

        //Correias Filtragens  
        for (MetaFiltragem novaBaliza : movimentacaoVO.getListaFiltragens()) {                           
                if (novaBaliza.getMetaCorreia().correiaInterditado(dataInterdicao)) {
                    interditada = Boolean.TRUE;
                    strInterditadas.append(novaBaliza.getMetaCorreia().getNomeCorreia()).append("\n");                       
                }
         }

        
        
        if (interditada) {
            throw new AtividadeException("Existem itens sob interdição/manutenção! \n" + strInterditadas.toString() );
        }
    }

    /**
    * realizarMovimentacaoPilhaPilha
    * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
    * @since 28/10/2010
    * @see
    * @param 
    * @return void
    */
    private List<Pilha> realizarMovimentacaoPilhaPilha(MovimentacaoVO movimentacaoVO, List<Baliza> balizasOrigem,
                    List<Baliza> balizasDestino, Pilha pilhaAtualOrigem, Pilha pilhaAtualDestino) throws AtividadeException {

    	List<Pilha> pilhasEditadas = new ArrayList<Pilha>();
    	if (movimentacaoVO.getDataFim() != null && balizasDestino != null && balizasDestino.size() > 0 ) {

            Produto produtoTransporte = calcularQuantidadeMovimentada(movimentacaoVO, balizasOrigem);

            // quantidade de produto empilhado em cada baliza destino
            double quantidadeProdutoPorBaliza = (movimentacaoVO.getQuantidadeMovimentacao() / balizasDestino.size());

            //TODO atualizar qualidade e fazer blendagem

            for (Baliza balizaDestino : balizasDestino) {
                Produto produtoNovo = Produto.criaProduto(movimentacaoVO.getTipoProduto(), movimentacaoVO.getDataFim()
                                .getTime());
                produtoNovo.setQuantidade(quantidadeProdutoPorBaliza);

                Qualidade novaQualidade = new Qualidade();
                novaQualidade.setListaDeAmostras(new ArrayList<Amostra>());
                novaQualidade.setListaDeItensDeControle(Blendagem
                                .criaListaDeItensDeControleComValoresVazios(produtoTransporte.getQualidade()
                                                .getListaDeItensDeControle()));
                novaQualidade.setEhReal(Boolean.FALSE);

                produtoNovo.setQualidade(novaQualidade);
                novaQualidade.setProduto(produtoNovo);

                if (balizaDestino.getProduto() != null) {
                    Blendagem blend = Blendagem.getInstance();
                    blend.setMapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagem(null);
                    blend.setProdutoResultante(null);
                    blend.setListaBalizasBlendadas(null);
                    blend.setCargaSelecionada(null);
                    blend.setListaDeCampanhas(null);
                    blend.setListaDeProdutosSelecionados(null);

                    Produto produtobaliza = balizaDestino.getProduto().copiarStatus();
                    blend.atualizaOMapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagemAposInsercaoDoProduto(produtobaliza,
                                    produtobaliza.getQuantidade());
                    blend.atualizaOMapaDeQuantidadesDeCadaTipoDeProdutoNaBlendagemAposInsercaoDoProduto(produtoNovo,
                                    quantidadeProdutoPorBaliza);
                    produtoNovo = blend.calculaProdutoResultanteDaInsercaoDeProdutoNaBlendagem(produtobaliza, produtoNovo,
                                    quantidadeProdutoPorBaliza);
                    produtoNovo.addRastreabilidade(produtobaliza.getListaDeRastreabilidades());
                } else {
                    produtoNovo.setQuantidade(quantidadeProdutoPorBaliza);
                }
                balizaDestino.setProduto(produtoNovo);
            }
            List<Baliza> listaBalizasSemProduto = new ArrayList<Baliza>();
            pilhasEditadas.addAll(retiraMaterialDaPilhaOrigem(movimentacaoVO, balizasOrigem, balizasDestino, null, movimentacaoVO
                            .getQuantidadeMovimentacao(), pilhaAtualOrigem,listaBalizasSemProduto));

            // ordena a lista de balizas
            Collections.sort(balizasDestino, new ComparadorBalizas());
            // novaPilhaDestino.setListaDeBalizas(listaBalizasDestino);

            // cria a pilha destino
            Pilha novaPilhaDestino = Pilha.criaPilha(movimentacaoVO.getNomePilhaDestino(), balizasDestino, movimentacaoVO
                            .getCliente(), movimentacaoVO.getDataFim().getTime(), pilhaAtualDestino);


            
            // Movimentacao para a mesma pilha comendo quantidade, não removia a baliza sem produto 
            List<Baliza> listaBalizasPilha = pilhaAtualDestino.getListaDeBalizas();
            for (Baliza b : listaBalizasSemProduto) {
            	removeBaliza(b, listaBalizasPilha);
            }
                        
            
            novaPilhaDestino.addBaliza(listaBalizasPilha);
            
            Pilha.decompoePilhaEditada(novaPilhaDestino, movimentacaoVO.getDataFim());          
            novaPilhaDestino.setHorarioFimFormacao(movimentacaoVO.getDataFim());
            pilhasEditadas.add(novaPilhaDestino);
            pilhasEditadas.add(pilhaAtualOrigem);
            pilhasEditadas.add(pilhaAtualDestino);
        }
		return pilhasEditadas;
    }

    /**
    * realizarMovimentacaoPilhaCarga
    * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
    * @since 28/10/2010
    * @see
    * @param 
    * @return void
    */
    private  List<Pilha>  realizarMovimentacaoPilhaCarga(MovimentacaoVO movimentacaoVO, List<Baliza> balizasOrigem,
                    Carga cargaDestino, Pilha pilhaAtualOrigem, Pilha pilhaAtualDestino) throws AtividadeException {
        List<Pilha> pilhasEditadas = new ArrayList<Pilha>();
        if (movimentacaoVO.getDataFim() != null && cargaDestino != null) {

            Produto produtoTransporte = calcularQuantidadeMovimentada(movimentacaoVO, balizasOrigem);

            //TODO atualizar qualidade e fazer blendagem

            Produto produtoNovo = Produto
                            .criaProduto(movimentacaoVO.getTipoProduto(), movimentacaoVO.getDataFim().getTime());
            produtoNovo.setQuantidade(movimentacaoVO.getQuantidadeMovimentacao());

            Qualidade novaQualidade = new Qualidade();
            novaQualidade.setListaDeAmostras(new ArrayList<Amostra>());
            novaQualidade.setListaDeItensDeControle(Blendagem.criaListaDeItensDeControleComValoresVazios(produtoTransporte
                            .getQualidade().getListaDeItensDeControle()));
            novaQualidade.setEhReal(Boolean.FALSE);

            produtoNovo.setQualidade(novaQualidade);
            novaQualidade.setProduto(produtoNovo);
            ControladorCalculoQualidade controladorCQ = new ControladorCalculoQualidade();

            // atualiza o produto da carga
            Blendagem blendagemResultante;
            try {
                Produto produtoCarga = cargaDestino.getProduto();
                blendagemResultante = controladorCQ.atenderCarga(cargaDestino);
                blendagemResultante.inserirProdutoNaBlendagem(produtoNovo, movimentacaoVO.getQuantidadeMovimentacao());
                cargaDestino.setProduto(blendagemResultante.getProdutoResultante());
                cargaDestino.getProduto().addRastreabilidade(produtoCarga.getListaDeRastreabilidades());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            List<Baliza> listaBalizasSemProduto = new ArrayList<Baliza>();
            pilhasEditadas.addAll(retiraMaterialDaPilhaOrigem(movimentacaoVO, balizasOrigem, null, cargaDestino, movimentacaoVO
                            .getQuantidadeMovimentacao(), pilhaAtualOrigem, listaBalizasSemProduto));
        }
		return pilhasEditadas;

    }

    /**
     * calcularQuantidadeMovimentada
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 28/10/2010
     * @see
     * @param 
     * @return Produto
     */
    private Produto calcularQuantidadeMovimentada(MovimentacaoVO movimentacaoVO, List<Baliza> balizasOrigem) {
        // pega o produto da primeira baliza da pilha origem
        Produto produtoTransporte = balizasOrigem.get(0).getProduto();
        double totalOrigem = 0D;
        for (Baliza baliza : balizasOrigem) {
            totalOrigem += baliza.getProduto().getQuantidade();
        }

        // duracao da atividade
        double horasDuracaoAtividade = ((((movimentacaoVO.getDataFim().getTime() - movimentacaoVO.getDataInicio().getTime()) / 1000)) / 60.0) / 60.0;

        // quantidade maxima que a maquina pode ter movimentado de acordo com sua taxa de operacao
        MaquinaDoPatio maquinaOrigem = movimentacaoVO.getListaMaquinas().get(0).retornaStatusHorario(
                        movimentacaoVO.getDataFim());
        double capacidadeMaquinaTempo = maquinaOrigem.quantidadePorTempo(horasDuracaoAtividade);

        if (totalOrigem < capacidadeMaquinaTempo) {
            movimentacaoVO.setQuantidadeMovimentacao(totalOrigem);
        } else {
            movimentacaoVO.setQuantidadeMovimentacao(capacidadeMaquinaTempo);
        }
        return produtoTransporte;
    }

    /**
     * retiraMaterialDaPilhaOrigem
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 20/07/2010
     * @see
     * @param 
     * @return void
     * @throws AtividadeException 
     */
    private  List<Pilha>  retiraMaterialDaPilhaOrigem(MovimentacaoVO vo, List<Baliza> listaBalizasOrigem,
                    List<Baliza> listaBalizasDestino, Carga cargaDestino, double quantidadeMovimentada,
                    Pilha pilhaAtualOrigem,List<Baliza> listaBalizasSemProduto) throws AtividadeException {
        List<Rastreabilidade> rastreabilidade = new ArrayList<Rastreabilidade>();
        Map<MetaBaliza, Double> mapaQuantidadeRetiradaDeBaliza = new HashMap<MetaBaliza, Double>();
        List<Pilha> pilhasDeletadas = new ArrayList<Pilha>();        
        int sizeBalizas = listaBalizasOrigem.size();
        // 2) PELLET FEED / SINTER FEED      
        // calcula a quantidade que deve ser retirada de cada baliza
        double quantidadeRetirarPorBaliza = quantidadeMovimentada / sizeBalizas;
        quantidadeRetirarPorBaliza = DSSStockyardFuncoesNumeros.arredondarDoubleParaCimaAPartirDeNumCasasDecimais(quantidadeRetirarPorBaliza,2);
        // lista com as balizas que ainda tem produto. Essa lista eh utilizada como auxiliar para verificar se existe alguma
        // baliza que nao contem a quantidade que deve ser subtraida. Neste caso distribui a retirada pelas balizas restantes
        List<Baliza> listaBalizasComProduto = new ArrayList<Baliza>();
     // lista com as balizas que ainda tem produto. Essa lista eh utilizada como auxiliar para verificar se existe alguma
        // baliza que nao contem a quantidade que deve ser subtraida. Neste caso distribui a retirada pelas balizas restantes
        List<Baliza> listaBalizasPilha = new ArrayList<Baliza>();
        
        listaBalizasComProduto.addAll(listaBalizasOrigem);
        
        listaBalizasPilha.addAll(pilhaAtualOrigem.getListaDeBalizas());
                
        boolean distribuiuRetirada = false;
        //Format twoDForm = new DecimalFormat("#.##");		
        while (!distribuiuRetirada) {
            distribuiuRetirada = true;
            for (Baliza bal : listaBalizasComProduto) {
                if (bal.getProduto() != null) {
                    // verifica se pode retirar toda a quantidade desta baliza
                    Double saldoDaPilha = bal.getProduto().getQuantidade() - quantidadeRetirarPorBaliza;                                        
                    saldoDaPilha = DSSStockyardFuncoesNumeros.arredondarDoubleParaCimaAPartirDeNumCasasDecimais(saldoDaPilha,1);
                    if (saldoDaPilha  <= 0) {
                        double quantidadeRecuperadaBaliza = bal.getProduto().getQuantidade();
                        mapaQuantidadeRetiradaDeBaliza.put(bal.getMetaBaliza(), new Double(quantidadeRecuperadaBaliza));
                        if (getBaliza(bal, listaBalizasDestino) < 0) {
                        	bal.setProduto(null);
                        	bal.setHorarioInicioFormacao(null);
                        	bal.setHorarioFimFormacao(null);                        
                        	listaBalizasSemProduto.add(bal);
                        }	                        
                    	listaBalizasComProduto.remove(bal);
                        distribuiuRetirada = false;
                        quantidadeRetirarPorBaliza += Math.abs(saldoDaPilha / listaBalizasComProduto.size());
                        break;
                    }
                } else {
                    throw new AtividadeException("Baliza origem da movimentação sem produto !");
                }
            }
        }

        // distribui as retiradas pelas pilhas restantes
        for (Baliza bal : listaBalizasComProduto) {
            if (bal.getProduto() != null && bal.getProduto().getQuantidade().doubleValue() <= 0D) {

                bal.setProduto(null);

            } else if (bal.getProduto() != null && bal.getProduto().getQuantidade().doubleValue() > 0D) {
                mapaQuantidadeRetiradaDeBaliza.put(bal.getMetaBaliza(), quantidadeRetirarPorBaliza);
                double quantidadeResultanteBaliza = Math.max(0d, bal.getProduto().getQuantidade()
                                - quantidadeRetirarPorBaliza);
                bal.getProduto().setQuantidade(quantidadeResultanteBaliza);
                if (bal.getProduto().getQuantidade().doubleValue() <= 0D) {
                    bal.setProduto(null);
                }
            }
        }

        for (Baliza baliza : listaBalizasSemProduto) {
            Rastreabilidade item = acrescentaRastreabilidade(vo.getDataInicio(), vo.getDataFim(), baliza, null,
                            mapaQuantidadeRetiradaDeBaliza.get(baliza.getMetaBaliza()), vo.getTxtNumeroPorao(), vo);
            rastreabilidade.add(item);
        }

        for (Baliza baliza : listaBalizasComProduto) {
            Rastreabilidade item = acrescentaRastreabilidade(vo.getDataInicio(), vo.getDataFim(), baliza, null,
                            mapaQuantidadeRetiradaDeBaliza.get(baliza.getMetaBaliza()), vo.getTxtNumeroPorao(), vo);
            rastreabilidade.add(item);
        }

        List<Baliza> novaOrigem = new ArrayList<Baliza>();
        List<Baliza> novaOrigemFinal = new ArrayList<Baliza>();
        for (Baliza b : listaBalizasPilha) {
        	int idx = getBaliza(b, listaBalizasSemProduto);
        	int idx1 = getBaliza(b, listaBalizasComProduto);
        	if (idx >= 0 || idx1 >= 0) {
    			System.out.println(" removendo baliza " + b.getNomeBaliza());
    		} else {
    			novaOrigem.add(b);
    		}
        }
        
        
        if (novaOrigem.size() > 0 ) {
        	for(Baliza b : novaOrigem) {
        		System.out.println(" gerando status das balizas que não foram alteradas " + b.getNomeBaliza());
        		novaOrigemFinal.add(b.getMetaBaliza().clonarStatus(vo.getDataFim()));        		
        	}
        	listaBalizasOrigem.addAll(novaOrigemFinal);            
        }
        
        novaOrigemFinal.addAll(listaBalizasComProduto);
        //novaOrigem.addAll(listaBalizasPilha);
        
        
        	
        
        
        if (novaOrigemFinal.size() > 0)  {
              Pilha novaPilhaOrigem = Pilha.criaPilha(vo.getNomePilha(), novaOrigemFinal, vo.getCliente(), vo.getDataFim()
                 .getTime(), pilhaAtualOrigem);
	
              
             Pilha.decompoePilhaEditada(novaPilhaOrigem, vo.getDataFim());
        }          
        
        if (listaBalizasDestino != null) {
            for (Baliza balizaDestino : listaBalizasDestino) {

                balizaDestino.getProduto().addRastreabilidade(rastreabilidade);
                long chave = 1l;
                for (Rastreabilidade rast : balizaDestino.getProduto().getListaDeRastreabilidades()) {
                    rast.setNumeroRastreabilidade(chave);
                    chave++;
                }
            }
        } else if (cargaDestino != null) {
            cargaDestino.getProduto().addRastreabilidade(rastreabilidade);
            long chave = 1l;
            for (Rastreabilidade rast : cargaDestino.getProduto().getListaDeRastreabilidades()) {
                rast.setNumeroRastreabilidade(chave);
                chave++;
            }
        }
		return pilhasDeletadas;
    }

    /* Acrescenta os registros de rastreabilidade no produto */
    /**
     * acrescentaRastreabilidade
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 20/07/2010
     * @see
     * @param 
     * @return Rastreabilidade
     */
    private Rastreabilidade acrescentaRastreabilidade(Date horaInicio, Date horaTermino, Baliza balizaOrigem,
                    Campanha campanha, double quantidadeEmpilhada, String nomePorao, MovimentacaoVO movimentacaoVO) {
        // para as atividades pontuais nao eh necessario incluir a
        // rastreabilidade
        if (horaInicio.getTime() == horaTermino.getTime()) {
            return null;
        }
        Rastreabilidade rastreabilidade = new Rastreabilidade();
        rastreabilidade.setHorarioInicioEntradaDeMaterial(horaInicio);
        rastreabilidade.setHorarioFimEntradaDeMaterial(horaTermino);
        rastreabilidade.setDtInicio(horaInicio);
        rastreabilidade.setDtFim(horaTermino);
        if (campanha != null) {
            rastreabilidade.setNomeUsina(campanha.getMetaUsina().getNomeUsina());
            rastreabilidade.setTaxaDeOperacaoUsina(campanha.getMetaUsina().retornaStatusHorario(horaInicio)
                            .getTaxaDeOperacao());
            rastreabilidade.setDataInicioUsina(campanha.getDataInicial());
            rastreabilidade.setDataFimUsina(campanha.getDataFinal());
            rastreabilidade.setNomeCampanhaUsina(campanha.getNomeCampanha());
            rastreabilidade.setTipoProdutoUsina(campanha.getTipoProduto());
            rastreabilidade.setTipoPelletUsina(campanha.getTipoPellet());
            rastreabilidade.setTipoScreeningUsina(campanha.getTipoScreening());
            rastreabilidade.setTipoScreeningUsina(campanha.getTipoScreening());
        }
        rastreabilidade.setTipoAtividade(movimentacaoVO.getTipoAtividade());
        rastreabilidade.setQuantidade(quantidadeEmpilhada);
        if (balizaOrigem != null) {
            rastreabilidade.setNomeBaliza(balizaOrigem.getNomeBaliza());
            rastreabilidade.setNumeroBaliza(balizaOrigem.getNumero());
            rastreabilidade.setLarguraBaliza(balizaOrigem.getLargura());
            rastreabilidade.setDtHoraIniFormBaliza(balizaOrigem.getHorarioInicioFormacao());
            rastreabilidade.setDtHoraFimFormBaliza(balizaOrigem.getHorarioFimFormacao());
            rastreabilidade.setCapacMaxBaliza(balizaOrigem.getCapacidadeMaxima());
            rastreabilidade.setNomePatioBaliza(balizaOrigem.getPatio().getNomePatio());
            rastreabilidade.setNumPatioBaliza(balizaOrigem.getPatio().getNumero());
            rastreabilidade.setNomePilhaBaliza(movimentacaoVO.getNomePilha());
        }
        rastreabilidade.setNomePorao(nomePorao);
        return rastreabilidade;
    }
    
    public void removeBaliza(Baliza baliza, List<Baliza> itens) {

		
		int idx = getBaliza(baliza, itens);
		if (idx >= 0)
			itens.remove(idx);

	}

	private int getBaliza(Baliza baliza, List<Baliza> itens) {
		int idx = -1;
		if (itens != null) {
			for (int i = 0; i < itens.size(); i++) {
				if (itens.get(i).getMetaBaliza()
						.equals(baliza.getMetaBaliza())) {
					idx = i;
					break;
				}
			}
		}	
		return idx;
	}
	
	
    /**
	    * realizarMovimentacaoPilhaPilha
	    * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
	    * @since 28/10/2010
	    * @see
	    * @param 
	    * @return void
	    */
	    private List<Pilha> realizarRetornoPellet(MovimentacaoVO movimentacaoVO, List<Baliza> balizasOrigem, Pilha pilhaAtualOrigem) throws AtividadeException {

	    	List<Pilha> pilhasEditadas = new ArrayList<Pilha>();
	    	if (movimentacaoVO.getDataFim() != null) {
	            
	            List<Baliza> listaBalizasSemProduto = new ArrayList<Baliza>();
	            pilhasEditadas.addAll(retiraMaterialDaPilhaOrigem(movimentacaoVO, balizasOrigem, null, null, movimentacaoVO
	                            .getQuantidadeMovimentacao(), pilhaAtualOrigem,listaBalizasSemProduto));

	            // ordena a lista de balizas	            
	            pilhasEditadas.add(pilhaAtualOrigem);
	        }
			return pilhasEditadas;
	    }

	    

}
