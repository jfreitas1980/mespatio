
package com.hdntec.gestao.cliente.relatorio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import br.com.cflex.samarco.supervision.stockyard.relatorio.GeradorPDF;
import br.com.cflex.samarco.supervision.stockyard.relatorio.RelatorioPDF;
import br.com.cflex.samarco.supervision.stockyard.relatorio.controles.CabecalhoRelatorio;
import br.com.cflex.samarco.supervision.stockyard.relatorio.controles.Coluna;
import br.com.cflex.samarco.supervision.stockyard.relatorio.enums.CalculoColunaEnum;
import br.com.cflex.samarco.supervision.stockyard.relatorio.exception.ErroGeracaoRelatorioPDF;

import com.hdntec.gestao.cliente.util.numeros.DSSStockyardFuncoesNumeros;
import com.hdntec.gestao.domain.plano.comparadores.ComparadorAtividadePorTipo;
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.entity.AtividadeRelatorio;
import com.hdntec.gestao.domain.plano.entity.LugarEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
import com.hdntec.gestao.domain.plano.enums.TipoAtividadeEnum;
import com.hdntec.gestao.domain.planta.comparadores.ComparadorBalizas;
import com.hdntec.gestao.domain.planta.entity.MetaFiltragem;
import com.hdntec.gestao.domain.planta.entity.status.AtividadeCampanha;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.Filtragem;
import com.hdntec.gestao.domain.planta.entity.status.Usina;
import com.hdntec.gestao.domain.relatorio.Relatorio;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.integracao.controladores.ControladorModelo;
import com.hdntec.gestao.integracao.controladores.IControladorModelo;
import com.hdntec.gestao.util.PropertiesUtil;
import com.hdntec.gestao.util.datahora.DSSStockyardTimeUtil;

/**
 * <P><B>Description :</B><BR>
 * General ControladorRelDeslocamentoMaquina
 * </P>
 * <P>
 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
 * @since 10/08/2009
 * @version $Revision: 1.1 $
 */
public class ControladorRelDeslocamentoMaquina {
    private final String SERVIDOR_RELATORIOS;

    /**
     * 
     * @param controladorModelo
     */
    public ControladorRelDeslocamentoMaquina() {
        this.SERVIDOR_RELATORIOS = PropertiesUtil.buscarPropriedade("pasta.servidor.relatorios").trim();
    }

    /**
     * 
     * popularRelatorio
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 07/08/2009
     * @see
     * @param spAtracados
     * @param situacaoPatioList
     * @param relatorio
     * @return Returns the void.
     */
    public void popularRelatorio(Relatorio relatorio) throws IOException, ErroSistemicoException {

        try {

            // Darley removendo chamada remota
            // IControladorModelo controladorModelo =
            // InterfaceInicial.lookUpModelo();
            IControladorModelo controladorModelo = new ControladorModelo();
            List<SituacaoPatio> situacaoPatioList = controladorModelo.buscarSituacaoPatioRelAtividade(relatorio
                            .getHorarioInicioRelatorio(), relatorio.getHorarioFimRelatorio(), true);

            // Carregar as usinas encontradas
            List<Usina> listaUsinas = new ArrayList<Usina>();
            for (SituacaoPatio situacaoPatio : situacaoPatioList) {
                for (Usina usina : situacaoPatio.getPlanta().getListaUsinas(situacaoPatio.getDtInicio())) {
                    if (!contemUsinaNaLista(listaUsinas, usina.getNomeUsina())) {
                        listaUsinas.add(usina);
                    }
                }
            }
            List<Atividade> listaAtividade = filtrarAtividades(situacaoPatioList);
            if (listaAtividade.size() > 0) {
                gerarRelatorio(relatorio, listaAtividade, listaUsinas);
            } else {
                throw new ErroSistemicoException(PropertiesUtil.getMessage("relatorio.configurar.padrao.emptyreport"));
            }
            controladorModelo = null;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
    * 
    * @param listaSituacaoPatio
    * @return
    */
    private List<Atividade> filtrarAtividades(List<SituacaoPatio> listaSituacaoPatio) {
        //guarda a posicao da lista de situacoes que jah foram filtradas. 
        List<Integer> situacoesFiltradas = new ArrayList<Integer>();
        //Lista que retorna as atividades encontradas nas situacoes do patio
        List<Atividade> lista = new ArrayList<Atividade>();
        int pos = 0;
        Date horaInicio = null;
        Date horaTermino = null;

        Map<Atividade, Atividade> mapaAtividades = new HashMap<Atividade, Atividade>();
        for (SituacaoPatio situacaoPatio : listaSituacaoPatio) {
            if (situacaoPatio.getAtividade() != null) {
                Atividade atividadePai = situacaoPatio.getAtividade();
                Atividade atividadeAnterior = atividadePai.getAtividadeAnterior();
                mapaAtividades.put(atividadePai, atividadeAnterior);
            }
        }

        for (SituacaoPatio situacaoPatio : listaSituacaoPatio) {
            if (situacaoPatio.getAtividade() != null) {
                Atividade atividadePai = situacaoPatio.getAtividade();
                //Atividade atividade = atividadePai.getAtividadeAnterior();

                if (atividadePai.getTipoAtividade() == TipoAtividadeEnum.ATUALIZACAO_EMPILHAMENTO) {

                    horaInicio = atividadePai.getDtInicio();
                    // a hora de termino da atividade eh o ultimo elemento da lista de datas relevantes
                    horaTermino = atividadePai.getDtFim();
                    {
                        if (atividadePai.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaAtividadeCampanhas() != null && !atividadePai.getListaDeAtividadesCampanha().isEmpty()) {
                            Atividade atividadeRel = recuperarAtividadeDaLista(lista,
                                            TipoAtividadeEnum.ATUALIZACAO_EMPILHAMENTO);
                            atividadeRel.addAtividadeRelatorio(getRelatorioAtualizacaoEmpilhamento(situacaoPatio));
                       }
                    }
                } else if (atividadePai.getTipoAtividade() == TipoAtividadeEnum.ATUALIZACAO_RECUPERACAO) {

                    if (!atividadePai.getFinalizada() && atividadePai.getFinalizada() != null && mapaAtividades.containsValue(atividadePai)) {
                        System.out.println("descartar !");
                    } else {
                        horaInicio = atividadePai.getDtInicio();
                        // a hora de termino da atividade eh o ultimo elemento da lista de datas relevantes
                        horaTermino = atividadePai.getDtFim();
                        Atividade atividadeRel = recuperarAtividadeDaLista(lista, TipoAtividadeEnum.ATUALIZACAO_RECUPERACAO);
                        AtividadeRelatorio rel = getRelatorioAtualizacaoRecuperacao(situacaoPatio, pos, situacoesFiltradas);
                        atividadeRel.addAtividadeRelatorio(rel);
                    }
                } else if (atividadePai.getTipoAtividade().equals(TipoAtividadeEnum.ATUALIZACAO_PILHA_DE_EMERGENCIA)) {
                    if (!atividadePai.getFinalizada() && atividadePai.getFinalizada() != null && mapaAtividades.containsValue(atividadePai)) {
                        System.out.println("descartar !");
                    } else {
                        Atividade atividadeRel = recuperarAtividadeDaLista(lista, TipoAtividadeEnum.ATUALIZACAO_PILHA_DE_EMERGENCIA);
                        AtividadeRelatorio relat = getRelatorioPilhaEmergencia(situacaoPatio.getAtividade());                        
                        atividadeRel.addAtividadeRelatorio(relat);                        
                    }
                } else if (atividadePai.getTipoAtividade().equals(TipoAtividadeEnum.MOVIMENTAR_PILHA_EMERGENCIA)) {
                    //TODO
                    if (!atividadePai.getFinalizada() && atividadePai.getFinalizada() != null && mapaAtividades.containsValue(atividadePai)) {
                        System.out.println("descartar !");
                    } else {

                        Atividade atividadeRel = recuperarAtividadeDaLista(lista, TipoAtividadeEnum.MOVIMENTAR_PILHA_EMERGENCIA);
                        atividadeRel.addAtividadeRelatorio(getRelatorioMovimentarPilha(situacaoPatio
                                        .getAtividade()));
                    }
                } else if (atividadePai.getTipoAtividade().equals(TipoAtividadeEnum.MOVIMENTAR_PILHA_PSM)) {
                    if (!atividadePai.getFinalizada() && atividadePai.getFinalizada() != null && mapaAtividades.containsValue(atividadePai)) {
                        System.out.println("descartar !");
                    } else {
                        Atividade atividadeRel = recuperarAtividadeDaLista(lista, TipoAtividadeEnum.MOVIMENTAR_PILHA_PSM);
                        atividadeRel.addAtividadeRelatorio(getRelatorioMovimentarPilha(situacaoPatio.getAtividade()));
                    }
                } else if (atividadePai.getTipoAtividade().equals(TipoAtividadeEnum.RETORNO_PELLET_FEED)) {
                    if (!atividadePai.getFinalizada() && atividadePai.getFinalizada() != null && mapaAtividades.containsValue(atividadePai)) {
                        System.out.println("descartar !");
                    } else {
                        Atividade atividadeRel = recuperarAtividadeDaLista(lista, TipoAtividadeEnum.RETORNO_PELLET_FEED);
                        atividadeRel.addAtividadeRelatorio(getRelatorioRetornoPelletFeed(situacaoPatio.getAtividade()));
                    }
                } else if (atividadePai.getTipoAtividade().equals(TipoAtividadeEnum.MOVIMENTAR_PILHA_PELLET_FEED)) {
                    if (!atividadePai.getFinalizada() && atividadePai.getFinalizada() != null && mapaAtividades.containsValue(atividadePai)) {
                        System.out.println("descartar !");
                    } else {                   
                        Atividade atividadeRel = recuperarAtividadeDaLista(lista, situacaoPatio.getAtividade()
                                        .getTipoAtividade());
                        atividadeRel.addAtividadeRelatorio(getRelatorioMovimentarPilha(situacaoPatio.getAtividade()));
                    }
                } else if (atividadePai.getTipoAtividade().equals(TipoAtividadeEnum.TRATAMENTO_PSM)) {
                    if (!atividadePai.getFinalizada() && atividadePai.getFinalizada() != null && mapaAtividades.containsValue(atividadePai)) {
                        System.out.println("descartar !");
                    } else {
                        Atividade atividadeRel = recuperarAtividadeDaLista(lista, situacaoPatio.getAtividade()
                                        .getTipoAtividade());
                        atividadeRel.addAtividadeRelatorio(getRelatorioTratamentoPSM(situacaoPatio));
                    }
                }
                pos++;
            }
        }
        return lista;
    }

    /**
     * 
     * @param listaSituacaoPatio
     * @return
     */
    /*
    private List<Atividade> filtrarAtividades(List<SituacaoPatio> listaSituacaoPatio) {
    //guarda a posicao da lista de situacoes que jah foram filtradas. 
    List<Integer> situacoesFiltradas = new ArrayList<Integer>();
    //Lista que retorna as atividades encontradas nas situacoes do patio
    List<Atividade> lista = new ArrayList<Atividade>();
    int pos = 0;		
    Map<Atividade,Atividade> mapaAtividades = new HashMap<Atividade, Atividade>();
    for (SituacaoPatio situacaoPatio : listaSituacaoPatio) {
         if (situacaoPatio.getAtividade() != null) {
             Atividade atividadePai = situacaoPatio.getAtividade();              
             Atividade atividadeAnterior = atividadePai.getAtividadeAnterior();                
             mapaAtividades.put(atividadePai, atividadeAnterior);
         }
    }    
    List<Atividade> listaAtividades = new ArrayList<Atividade>();
    Set s = mapaAtividades.keySet();  
     for(Iterator it = s.iterator(); it.hasNext(); ) {  
         Atividade atividadePai = (Atividade) it.next();
         Atividade atividadeAnterior = mapaAtividades.get(atividadePai);  
     
         if (atividadeAnterior == null && mapaAtividades.containsValue(atividadePai)) {
             listaAtividades.add(atividadePai);   
         } else (atividadeAnterior != null && mapaAtividadesFinalizadas.contains(atividadeAnterior)) {
             
         }
         
         if (atividade.getTipoAtividade().equals(TipoAtividadeEnum.ATUALIZACAO_RECUPERACAO)) {
             Atividade atividadeRel = recuperarAtividadeDaLista(lista,TipoAtividadeEnum.ATUALIZACAO_RECUPERACAO);
             AtividadeRelatorio rel = getRelatorioAtualizacaoRecuperacao1(atividade,pos,situacoesFiltradas);
                            if (rel != null) {
                                atividadeRel.addAtividadeRelatorio(rel);
                            }
                            
        } else if (atividade.getTipoAtividade().equals(TipoAtividadeEnum.ATUALIZACAO_EMPILHAMENTO)) {
                      //if (atividade.getListaDeAtividadesCampanha().isEmpty()) {
                          Atividade atividadeRel = recuperarAtividadeDaLista(lista,TipoAtividadeEnum.ATUALIZACAO_EMPILHAMENTO);
                          atividadeRel.addAtividadeRelatorio(getRelatorioAtualizacaoEmpilhamento(atividade));
                      //}
         }
    }
     
    
    
    for (SituacaoPatio situacaoPatio : listaSituacaoPatio) {
    	if (situacaoPatio.getAtividade() != null) {
             Atividade atividadePai = situacaoPatio.getAtividade();	            
             Atividade atividade = atividadePai.getAtividadeAnterior();
             
        
             
             if (atividadePai.getFinalizada() && atividade != null && 
                      atividade.getTipoAtividade() == TipoAtividadeEnum.ATUALIZACAO_EMPILHAMENTO)
             {

    			
                   // a hora de inicio da atividade eh o primeiro elemento da lista de datas relevantes
                   horaInicio = atividade.getDtInicio();
                   // a hora de termino da atividade eh o ultimo elemento da lista de datas relevantes
                   horaTermino = atividade.getDtFim();
                   {					
    					if (atividade.getListaDeAtividadesCampanha().isEmpty()) {
    						Atividade atividadeRel = recuperarAtividadeDaLista(lista,TipoAtividadeEnum.ATUALIZACAO_EMPILHAMENTO);
    						atividadeRel.addAtividadeRelatorio(getRelatorioAtualizacaoEmpilhamento(situacaoPatio));
    					}
                   }		
    		}
    		
    		}
    		/*else if (TipoAtividadeEnum.MOVIMENTACAO.equals(situacaoPatio.getAtividade().getTipoAtividade())) {
    			Atividade atividadeRel = recuperarAtividadeDaLista(lista,TipoAtividadeEnum.MOVIMENTACAO);
    			AtividadeRelatorio rel = getRelatorioMovimentacao(situacaoPatio);
    			atividadeRel.addAtividadeRelatorio(rel);
    			Produto produto = new Produto();
    			produto.setTipoProduto(rel.getTipoProduto());
    			Carga carga = new Carga();
    			carga.setProduto(produto);
    			atividadeRel.addCarga(carga);
    		}
    		else if (TipoAtividadeEnum.MOVIMENTAR_PILHA_PSM.equals(situacaoPatio.getAtividade().getTipoAtividade())) {
    			Atividade atividadeRel = recuperarAtividadeDaLista(lista,TipoAtividadeEnum.MOVIMENTAR_PILHA_PSM);
    			atividadeRel.addAtividadeRelatorio(getRelatorioTratamentoPSM(situacaoPatio));
    		}
    		else if (TipoAtividadeEnum.RETORNO_PELLET_FEED.equals(situacaoPatio.getAtividade().getTipoAtividade())) {
    			Atividade atividadeRel = recuperarAtividadeDaLista(lista,TipoAtividadeEnum.RETORNO_PELLET_FEED);
    			atividadeRel.addAtividadeRelatorio(getRelatorioRetornoPelletFeed(situacaoPatio.getAtividade()));
    		}
    		else if (TipoAtividadeEnum.PILHA_DE_EMERGENCIA.equals(situacaoPatio.getAtividade().getTipoAtividade())) {
    			//TODO
    			Atividade atividadeRel = recuperarAtividadeDaLista(lista,situacaoPatio.getAtividade().getTipoAtividade());
    			AtividadeRelatorio relat = getRelatorioPilhaEmergencia(situacaoPatio.getAtividade());
    			if (relat != null) {
    				atividadeRel.addAtividadeRelatorio(relat);
    			}
    		}
    		else if (TipoAtividadeEnum.MOVIMENTAR_PILHA_EMERGENCIA.equals(situacaoPatio.getAtividade().getTipoAtividade())) {
    			//TODO
    			Atividade atividadeRel = recuperarAtividadeDaLista(lista,situacaoPatio.getAtividade().getTipoAtividade());
    			atividadeRel.addAtividadeRelatorio(getRelatorioMovimentarPilhaEmergencia(situacaoPatio.getAtividade()));
    		}
    		else if (TipoAtividadeEnum.TRANSPORTAR_PILHA_EMERGENCIA.equals(situacaoPatio.getAtividade().getTipoAtividade())) {
    			//TODO
    			Atividade atividadeRel = recuperarAtividadeDaLista(lista,situacaoPatio.getAtividade().getTipoAtividade());
    			atividadeRel.addAtividadeRelatorio(getRelatorioTransportarPilhaEmergencia(situacaoPatio.getAtividade()));
    		}
    	//}
    	//pos++;
    //}
    return lista;
    }
    */
    /**
     * Metodo que verifica de acordo com o tipo de atividade informado se ja existe na lista de atividade
     * @param lista
     * @param tipo
     * @return
     */
    private Atividade recuperarAtividadeDaLista(List<Atividade> lista, TipoAtividadeEnum tipo) {
        Collections.sort(lista, new ComparadorAtividadePorTipo());
        Atividade atividadeRel = contemTipoAtividade(lista, tipo);
        if (atividadeRel == null) {
            Atividade atividade = new Atividade();
            atividade.setTipoAtividade(tipo);
            lista.add(atividade);
            atividadeRel = atividade;
        }
        return atividadeRel;
    }

    /**
     * 
     * @param atividadeRel
     * @param situacaoPatio
     */
    private AtividadeRelatorio getRelatorioAtualizacaoEmpilhamento(SituacaoPatio situacao) {
        Atividade atividade = situacao.getAtividade();
        LugarEmpilhamentoRecuperacao lugarEmp = atividade.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0);
        Baliza baliza = lugarEmp.getListaDeBalizas().get(0);
        AtividadeRelatorio relatorio = new AtividadeRelatorio();
        relatorio.setNomePatio(baliza.getPatio().getNomePatio());
        relatorio.setNomeBalizaOrigem(baliza.getNomeBaliza());
        //Collections.sort(situacaoPatio.getAtividade().getDatas());
        String dataInicio = DSSStockyardTimeUtil.formatarData(atividade.getDtInicio(), PropertiesUtil
                        .buscarPropriedade("formato.campo.datahora"));
        if (atividade.getDtFim() != null) {
            String dataFim = DSSStockyardTimeUtil.formatarData(atividade.getDtFim(), PropertiesUtil
                            .buscarPropriedade("formato.campo.datahora"));
            relatorio.setDataFim(dataFim);
        }
        relatorio.setDataInicio(dataInicio);
        relatorio.setNomeMaquina(lugarEmp.getMaquinaDoPatio().getNomeMaquina());
        relatorio.setQtdBalizaOrigem(lugarEmp.getQuantidade());
        List<Usina> usinas = atividade.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaUsinas();
        List<Filtragem> filtragens = atividade.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaFiltragens();
        if (usinas != null) {
            for (Usina usina : usinas) {
                relatorio.addUsina(usina);
            }
        } 
        if (filtragens != null) {
            for (Filtragem filtragem : filtragens) {
                relatorio.addFiltragem(filtragem);
            }            
        }        
        return relatorio;
    }

    /**
     * 
     * @param situacaoPatio
     * @return
     */
    private AtividadeRelatorio getRelatorioAtualizacaoRecuperacao(SituacaoPatio situacao, int pos,
                    List<Integer> situacoesFiltradas) {

        AtividadeRelatorio relatorio = null;
        String strBaliza = "-";
        String nomePatio = "";
        String nomeMaquina = "";
        Double quantidade = null;
        Atividade atividade = situacao.getAtividade();
        LugarEmpilhamentoRecuperacao lugarEmp = null;
        int posSituacao = -1;
        if (atividade.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaAtividadeCampanhas() != null &&  !atividade.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaAtividadeCampanhas()
                        .isEmpty()) {                
            quantidade = atividade.getListaDeAtividadesCampanha().get(0).getQuantidade();
        } else {
            lugarEmp = atividade.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0);
            Baliza balizaIni = lugarEmp.getListaDeBalizas().get(0);
            Baliza balizaFim = lugarEmp.getListaDeBalizas().get(lugarEmp.getListaDeBalizas().size() - 1);
            strBaliza = balizaIni.getNomeBaliza() + " - " + balizaFim.getNomeBaliza();
            nomePatio = balizaIni.getPatio().getNomePatio();
            quantidade = lugarEmp.getQuantidade();
            nomeMaquina = lugarEmp.getMaquinaDoPatio().getNomeMaquina();
        }
        if (atividade != null) {
            situacoesFiltradas.add(posSituacao);
            relatorio = new AtividadeRelatorio();
            relatorio.setNomePatio(nomePatio);
            relatorio.setNomeBalizaOrigem(strBaliza);
            String dataInicio = DSSStockyardTimeUtil.formatarData(atividade.getDtInicio(), PropertiesUtil
                            .buscarPropriedade("formato.campo.datahora"));
            String dataFim = DSSStockyardTimeUtil.formatarData(atividade.getDtFim(), PropertiesUtil
                            .buscarPropriedade("formato.campo.datahora"));
            relatorio.setDataInicio(dataInicio);
            relatorio.setDataFim(dataFim);
            relatorio.setNomeMaquina(nomeMaquina);
            relatorio.setQtdBalizaOrigem(quantidade);
            relatorio.setNomeNavio(atividade.getCarga().getNavio(atividade.getDtFim()).getNomeNavio());
            relatorio.setStrCarga(atividade.getCarga().getIdentificadorCarga());
            relatorio
                            .setStrPorao(atividade.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0)
                                            .getNomePorao());

            List<Usina> usinas = atividade.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaUsinas();
            List<Filtragem> filtragens = atividade.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaFiltragens();
            if (usinas != null) {
                for (Usina usina : usinas) {
                    relatorio.addUsina(usina);
                }
            } 
            if (filtragens != null) {
                for (Filtragem filtragem : filtragens) {
                    relatorio.addFiltragem(filtragem);
                }            
            }

        }
        return relatorio;
    }

        /**
     * 
     * @return
     */
    private AtividadeRelatorio getRelatorioRetornoPelletFeed(Atividade atividade) {

    	LugarEmpilhamentoRecuperacao lugarEmpOrigem = atividade.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0);
        AtividadeRelatorio relatorio = null;

        Collections.sort(lugarEmpOrigem.getListaDeBalizas(), new ComparadorBalizas());        
        Baliza balizaIni = lugarEmpOrigem.getListaDeBalizas().get(0);
        Baliza balizaFim = lugarEmpOrigem.getListaDeBalizas().get(lugarEmpOrigem.getListaDeBalizas().size() - 1);

        relatorio = new AtividadeRelatorio();
        relatorio.setNomePatio(balizaIni.getPatio().getNomePatio());
        relatorio.setNomeBalizaOrigem(balizaIni.getNomeBaliza() + " - " + balizaFim.getNomeBaliza());
        String dataInicio = DSSStockyardTimeUtil.formatarData(atividade.getDtInicio(), PropertiesUtil
                        .buscarPropriedade("formato.campo.datahora"));
        String dataFim = DSSStockyardTimeUtil.formatarData(atividade.getDtFim(), PropertiesUtil
                        .buscarPropriedade("formato.campo.datahora"));
        relatorio.setDataInicio(dataInicio);
        relatorio.setDataFim(dataFim);
        relatorio.setNomeMaquina(lugarEmpOrigem.getListaMaquinaDoPatio().get(0).getMetaMaquina().getNomeMaquina());        
        relatorio.setQtdBalizaOrigem(lugarEmpOrigem.getQuantidade());
        return relatorio;
    }

    /**
     * 
     * @param situacaoPatio
     * @return
     */
    private AtividadeRelatorio getRelatorioTratamentoPSM(SituacaoPatio situacaoPatio) {
        AtividadeRelatorio relatorio = new AtividadeRelatorio();
        double porcentagemPelota = 0;
        double porcentagemPellet = 0;
        double porcentagemLixo = 0;
        /*for (LugarEmpilhamentoRecuperacao lugar : situacaoPatio.getAtividade().getListaDeLugaresDeEmpilhamentoRecuperacao()) {
        	
        	if (TipoDeProdutoEnum.PELOTA_PSM.equals(lugar.getTipoDeProduto().getTipoDeProduto())) {
        		relatorio.setNomeBalizaPelota(lugar.getListaDeBalizas().get(0).getNomeBaliza());
        		porcentagemPelota = lugar.getTipoDeProduto().getPorcentagemResultadoPSM();
        	}
        	else if (TipoDeProdutoEnum.PELLET_PSM.equals(lugar.getTipoDeProduto().getTipoDeProduto())) {
        		relatorio.setNomeBalizaPellet(lugar.getListaDeBalizas().get(0).getNomeBaliza());
        		porcentagemPellet = lugar.getTipoDeProduto().getPorcentagemResultadoPSM();
        	}
        	else if (TipoDeProdutoEnum.LIXO.equals(lugar.getTipoDeProduto().getTipoDeProduto())) {
        		relatorio.setNomeBalizaDestino(lugar.getListaDeBalizas().get(0).getNomeBaliza());
        		porcentagemLixo = lugar.getTipoDeProduto().getPorcentagemResultadoPSM();
        	}
        	else {
        		relatorio.setNomeBalizaOrigem(lugar.getListaDeBalizas().get(0).getNomeBaliza());
        		relatorio.setQtdBalizaOrigem(lugar.getQuantidade());
        	}
        }*/
        double quantidade = relatorio.getQtdBalizaOrigem();
        relatorio.setQtdBalizaDestino(quantidade * porcentagemLixo);
        relatorio.setQtdBalizaPellet(quantidade * porcentagemPellet);
        relatorio.setQtdBalizaPelota(quantidade * porcentagemPelota);
        return relatorio;
    }

    /**
     * 
     * @param situacaoPatio
     * @return
     */
    private AtividadeRelatorio getRelatorioPilhaEmergencia(Atividade atividade) {
        boolean recuperar = true;
        LugarEmpilhamentoRecuperacao lugarEmp = atividade.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0);

        AtividadeRelatorio relatorio = null;

        Collections.sort(lugarEmp.getListaDeBalizas(), new ComparadorBalizas());
        int index = lugarEmp.getListaDeBalizas().size() - 1;
        Baliza balizaIni = lugarEmp.getListaDeBalizas().get(0);
        Baliza balizaFim = lugarEmp.getListaDeBalizas().get(index);

        relatorio = new AtividadeRelatorio();
        relatorio.setNomePatio(balizaIni.getPatio().getNomePatio());
        relatorio.setNomeBalizaOrigem(balizaIni.getNomeBaliza() + " - " + balizaFim.getNomeBaliza());
        String dataInicio = DSSStockyardTimeUtil.formatarData(atividade.getDtInicio(), PropertiesUtil
                        .buscarPropriedade("formato.campo.datahora"));
        String dataFim = DSSStockyardTimeUtil.formatarData(atividade.getDtFim(), PropertiesUtil
                        .buscarPropriedade("formato.campo.datahora"));
        relatorio.setDataInicio(dataInicio);
        relatorio.setDataFim(dataFim);
        relatorio.setNomeMaquina("");
        relatorio.setQtdBalizaOrigem(lugarEmp.getQuantidade());
        for (AtividadeCampanha campanha : atividade.getListaDeAtividadesCampanha()) {
            relatorio.addUsina(campanha.getCampanha().getMetaUsina().retornaStatusHorario(atividade.getDtInicio()));
        }

        return relatorio;
    }

    /**
     * getRelatorioMovimentarPilha
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 11/08/2010
     * @see
     * @param 
     * @return AtividadeRelatorio
     */
    private AtividadeRelatorio getRelatorioMovimentarPilha(Atividade atividade) {        
        
    	//atividade.ordernar();
    	
    	LugarEmpilhamentoRecuperacao lugarEmpOrigem = atividade.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0);
        LugarEmpilhamentoRecuperacao lugarEmpDestino = atividade.getListaDeLugaresDeEmpilhamentoRecuperacao().get(1);
        AtividadeRelatorio relatorio = null;

        Collections.sort(lugarEmpOrigem.getListaDeBalizas(), new ComparadorBalizas());        
        Baliza balizaIni = lugarEmpOrigem.getListaDeBalizas().get(0);
        Baliza balizaFim = lugarEmpOrigem.getListaDeBalizas().get(lugarEmpOrigem.getListaDeBalizas().size() - 1);

        relatorio = new AtividadeRelatorio();
        relatorio.setNomePatio(balizaIni.getPatio().getNomePatio());
        relatorio.setNomeBalizaOrigem(balizaIni.getNomeBaliza() + " - " + balizaFim.getNomeBaliza());
        String dataInicio = DSSStockyardTimeUtil.formatarData(atividade.getDtInicio(), PropertiesUtil
                        .buscarPropriedade("formato.campo.datahora"));
        String dataFim = DSSStockyardTimeUtil.formatarData(atividade.getDtFim(), PropertiesUtil
                        .buscarPropriedade("formato.campo.datahora"));
        relatorio.setDataInicio(dataInicio);
        relatorio.setDataFim(dataFim);
        relatorio.setNomeMaquina(lugarEmpOrigem.getListaMaquinaDoPatio().get(0).getMetaMaquina().getNomeMaquina());        
        
        if (lugarEmpDestino.getListaMaquinaDoPatio() != null &&  lugarEmpDestino.getListaMaquinaDoPatio().size() > 0) {
            Baliza balizaIniDest = lugarEmpDestino.getListaDeBalizas().get(0);
            Baliza balizaFimDest = lugarEmpDestino.getListaDeBalizas().get(lugarEmpDestino.getListaDeBalizas().size() - 1);
            relatorio.setNomePatioDestino(balizaIni.getPatio().getNomePatio());
            relatorio.setNomeBalizaDestino(balizaIniDest.getNomeBaliza() + " - " + balizaFimDest.getNomeBaliza());
            relatorio.setNomeMaquinaDestino(lugarEmpDestino.getListaMaquinaDoPatio().get(0).getMetaMaquina().getNomeMaquina());                    
        }  else if (lugarEmpDestino.getListaCargas().size() > 0) {            
            relatorio.setNomeNavio(lugarEmpDestino.getListaCargas().get(0).getNavio(atividade.getDtFim()).getNomeNavio());
            relatorio.setStrCarga(lugarEmpDestino.getListaCargas().get(0).getIdentificadorCarga());
            relatorio
                            .setStrPorao(lugarEmpDestino.getNomePorao());
        }            
        relatorio.setQtdBalizaOrigem(lugarEmpDestino.getQuantidade());
        relatorio.setTipoProduto(lugarEmpDestino.getTipoProduto());
        return relatorio;
    }

    /**
     * 
     * @param lista
     * @param tipoAtividade
     * @return
     */
    private Atividade contemTipoAtividade(List<Atividade> lista, TipoAtividadeEnum tipoAtividade) {
        Atividade result = null;
        for (Atividade atividade : lista) {
            if (tipoAtividade.equals(atividade.getTipoAtividade())) {
                result = atividade;
                break;
            }
        }
        return result;
    }

    /**
     * 
     * @param listaUsinas
     * @param nomeUsina
     * @return
     */
    private boolean contemUsinaNaLista(List<Usina> listaUsinas, String nomeUsina) {
        boolean contem = false;
        for (Usina usina : listaUsinas) {
            if (usina.getNomeUsina().equals(nomeUsina)) {
                contem = true;
                break;
            }
        }
        return contem;
    }

    /**
     * 
     * @param listaUsinas
     * @param nomeUsina
     * @return
     */
    private boolean contemFiltragemNaLista(List<Filtragem> listaUsinas, String nomeUsina) {
        boolean contem = false;
        if (listaUsinas == null)
            return contem;
        for (Filtragem usina : listaUsinas) {
            if (usina.getMetaFiltragem().getNomeFiltragem().equals(nomeUsina)) {
                contem = true;
                break;
            }
        }
        return contem;
    }
    
    
    /**
     * 
     * gerarRelatorio
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 07/08/2009
     * @see
     * @param relatorio
     * @param navioMap
     * @throws IOException
     * @throws ErroSistemicoException
     * @return Returns the void.
     */
    private void gerarRelatorio(Relatorio relatorio, List<Atividade> listaAtividade, List<Usina> listaUsinas)
                    throws IOException, ErroSistemicoException {

        GeradorPDF pdf = new GeradorPDF(SERVIDOR_RELATORIOS, relatorio.getNomeRelatorio());
        ImageIcon logo = new ImageIcon(getClass().getResource("/images/logo_samarco.png"));
        String titulo = PropertiesUtil.getMessage("relatorio.cabecalho.title.deslocamentoMaquina");
        String subTitulo = PropertiesUtil.getMessage("relatorio.cabecalho.subtitle.empilhamentoRecuperacao");
        String msgTipoAtividade = PropertiesUtil.getMessage("relatorio.deslocamento.tipoAtividade");
        String msgPeriodo = PropertiesUtil.getMessage("relatorio.deslocamento.periodo");
        String msgEntre = PropertiesUtil.getMessage("relatorio.deslocamento.entrePeriodo");

        String dataHoraInicio = DSSStockyardTimeUtil.formatarData(relatorio.getHorarioInicioRelatorio(), PropertiesUtil
                        .buscarPropriedade("formato.campo.datahora"));
        String dataHoraFim = DSSStockyardTimeUtil.formatarData(relatorio.getHorarioFimRelatorio(), PropertiesUtil
                        .buscarPropriedade("formato.campo.datahora"));

        RelatorioPDF relatorioPDF = null;
        boolean primeiraPagina = true;
        for (Atividade atividade : listaAtividade) {

            relatorioPDF = new RelatorioPDF(titulo, subTitulo, logo.getImage());
            relatorioPDF.setGeraNovaPagina(primeiraPagina);

            pdf.addRelatorio(relatorioPDF);

            //Montando o cabecalho do Relatorio
            CabecalhoRelatorio cabecalho = relatorioPDF.getCabecalhoRelatorio();
            if (primeiraPagina) {
                cabecalho.addColuna(msgPeriodo + ": " + dataHoraInicio + " " + msgEntre + " " + dataHoraFim);
                cabecalho.addColuna("");
            }
            primeiraPagina = false;

            cabecalho.setQtdColunas(1);
            cabecalho.addColuna("");

            Map<Integer, List<Object>> dados = new HashMap<Integer, List<Object>>();

            if (TipoAtividadeEnum.ATUALIZACAO_EMPILHAMENTO.equals(atividade.getTipoAtividade())) {
                cabecalho.addColuna(msgTipoAtividade + ": " + atividade.getTipoAtividade().toString());
                adicionarColunasEmpilhamento(relatorioPDF, listaUsinas);
                dados = popularDadosEmpilhamento(atividade, listaUsinas);
            } else if (TipoAtividadeEnum.ATUALIZACAO_RECUPERACAO.equals(atividade.getTipoAtividade())) {
                cabecalho.addColuna(msgTipoAtividade + ": " + atividade.getTipoAtividade().toString());
                adicionarColunasRecuperacao(relatorioPDF, listaUsinas);
                dados = popularDadosRecuperacao(atividade, listaUsinas);
            } else if (TipoAtividadeEnum.MOVIMENTAR_PILHA_PSM.equals(atividade.getTipoAtividade())
                            || TipoAtividadeEnum.MOVIMENTAR_PILHA_PELLET_FEED.equals(atividade.getTipoAtividade()) 
                            || TipoAtividadeEnum.MOVIMENTAR_PILHA_EMERGENCIA.equals(atividade.getTipoAtividade())) {
                cabecalho.setQtdColunas(2);
                cabecalho.addColuna("");
                String msgProduto = PropertiesUtil.getMessage("relatorio.deslocamento.produto");
                cabecalho.addColuna(msgTipoAtividade + ": " + atividade.getTipoAtividade().toString());
               // cabecalho.addColuna(msgProduto + ": " +  atividade.getListaDeLugaresDeEmpilhamentoRecuperacao().get(1).getTipoProduto());                    
                adicionarColunasMovimentacao(relatorioPDF, listaUsinas);
                dados = popularDadosMovimentacao(atividade);
            } else if (TipoAtividadeEnum.TRATAMENTO_PSM.equals(atividade.getTipoAtividade())) {
                cabecalho.addColuna(msgTipoAtividade + ": " + atividade.getTipoAtividade().toString());
                adicionarColunasTratamentoPSM(relatorioPDF, listaUsinas);
                dados = popularDadosTratamentoPSM(atividade);
            } else if (TipoAtividadeEnum.RETORNO_PELLET_FEED.equals(atividade.getTipoAtividade())) {
                cabecalho.addColuna(msgTipoAtividade + ": " + atividade.getTipoAtividade().toString());
                adicionarColunasRetornoPelletFeed(relatorioPDF);
                dados = popularDadosRetornoPelletFeed(atividade);
            } else if (TipoAtividadeEnum.ATUALIZACAO_PILHA_DE_EMERGENCIA.equals(atividade.getTipoAtividade())) {    
                cabecalho.addColuna(msgTipoAtividade + ": " + atividade.getTipoAtividade().toString());
                adicionarColunasPilhaEmergencia(relatorioPDF, listaUsinas);
                dados = popularDadosPilhaEmergencia(atividade, listaUsinas);
           
            }    

            relatorioPDF.setDadosCorpoRelatorio(dados);

        }

        //Gerando o relatorio
        try {
            pdf.gerarPDF();
        } catch (ErroGeracaoRelatorioPDF e) {
            throw new ErroSistemicoException(e);
        }
    }

    /**
     * 
     * adicionarColunasEmpilhamento
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 11/08/2009
     * @see
     * @param relatorio
     * @param listaUsinas
     * @return Returns the void.
     */
    private void adicionarColunasEmpilhamento(RelatorioPDF relatorio, List<Usina> listaUsinas) {
        final float tamanhoColunaUsina = 5;
        float totalColunas = 6.00f;        
        int ordem = 1;
        relatorio.addColuna(new Coluna(ordem++, "Grupo", 0, CalculoColunaEnum.NAO_CALCULA, true, 1, null, false));
        List<MetaFiltragem> itensFiltro = new ArrayList<MetaFiltragem>();
        
        for (Usina usina : listaUsinas) {
                        
            MetaFiltragem filtragem = usina.getMetaUsina().getFiltragemOrigem();
        
            if (filtragem != null && !itensFiltro.contains(filtragem) ) {                
                itensFiltro.add(filtragem);
            }
        }
        
        float totalTamanhoUsinas = (listaUsinas.size() + itensFiltro.size())* tamanhoColunaUsina;
        
        for (Usina usina : listaUsinas) {
            relatorio.addColuna(new Coluna(ordem++, usina.getNomeUsina(), tamanhoColunaUsina, CalculoColunaEnum.NAO_CALCULA,
                            false, 0, null, false));            
        }
        
        
        for (MetaFiltragem filtragem : itensFiltro) {
            relatorio.addColuna(new Coluna(ordem++, filtragem.getNomeFiltragem(), tamanhoColunaUsina, CalculoColunaEnum.NAO_CALCULA,
                            false, 0, null, false));            
        }
        
        
        float tamanhoColunas = (100.00f - totalTamanhoUsinas) / totalColunas;
        tamanhoColunas = (float) Math.floor(tamanhoColunas);
        
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.patio"), tamanhoColunas,
                        CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.baliza"), tamanhoColunas,
                        CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.inicio"), tamanhoColunas,
                        CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.fim"), tamanhoColunas,
                        CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.maquina"), tamanhoColunas,
                        CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.quantidade"),
                        tamanhoColunas, CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        relatorio.ajustarTamanhoColuna(ordem - 1);
    }

    /**
     * 
     * popularDadosEmpilhamento
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 12/08/2009
     * @see
     * @param situacaoPatio
     * @param listaUsinas
     * @return
     * @return Returns the Map<Integer,List<Object>>.
     */
    private Map<Integer, List<Object>> popularDadosEmpilhamento(Atividade atividade, List<Usina> listaUsinas) {
        Integer registros = 1;
        Map<Integer, List<Object>> result = new HashMap<Integer, List<Object>>();
        List<MetaFiltragem> itensFiltro = new ArrayList<MetaFiltragem>();
        for (AtividadeRelatorio atividadeRelatorio : atividade.getListaDeAtividadesRelatorio()) {

            List<Object> dadosList = new ArrayList<Object>();
            dadosList.add("grupo1");
            for (Usina usina : listaUsinas) {
                if (contemUsinaNaLista(atividadeRelatorio.getListaUsinas(), usina.getNomeUsina())) {
                    dadosList.add("X");
                } else {
                    dadosList.add("");
                }

                MetaFiltragem filtragem = usina.getMetaUsina().getFiltragemOrigem();
                
                if (filtragem != null && !itensFiltro.contains(filtragem) ) {                
                    itensFiltro.add(filtragem);
                }
                
            }
            
            for (MetaFiltragem usina : itensFiltro) {
                if (contemFiltragemNaLista(atividadeRelatorio.getListaFiltragens(), usina.getNomeFiltragem())) {
                    dadosList.add("X");
                } else {
                    dadosList.add("");
                }
            }
            
            String qtdBaliza = DSSStockyardFuncoesNumeros.getValorFormatado(atividadeRelatorio.getQtdBalizaOrigem(), 2);
            dadosList.add(atividadeRelatorio.getNomePatio());
            dadosList.add(atividadeRelatorio.getNomeBalizaOrigem());
            dadosList.add(atividadeRelatorio.getDataInicio());
            if (atividadeRelatorio.getDataFim() != null) {
                dadosList.add(atividadeRelatorio.getDataFim());
            } else {
                dadosList.add(null);
            }
            dadosList.add(atividadeRelatorio.getNomeMaquina());
            dadosList.add(qtdBaliza);
            result.put(registros++, dadosList);
        }
        return result;
    }

    /**
     * 
     * adicionarColunasRecuperacao
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 11/08/2009
     * @see
     * @param relatorio
     * @param listaUsinas
     * @return Returns the void.
     */
    private void adicionarColunasRecuperacao(RelatorioPDF relatorio, List<Usina> listaUsinas) {
        final float tamanhoColunaUsina = 5;
        float totalColunas = 9.00f;        
        int ordem = 1;
        relatorio.addColuna(new Coluna(ordem++, "Grupo", 0, CalculoColunaEnum.NAO_CALCULA, true, 1, null, false));
        List<MetaFiltragem> itensFiltro = new ArrayList<MetaFiltragem>();
        
        for (Usina usina : listaUsinas) {
                        
            MetaFiltragem filtragem = usina.getMetaUsina().getFiltragemOrigem();
        
            if (filtragem != null && !itensFiltro.contains(filtragem) ) {                
                itensFiltro.add(filtragem);
            }
        }
        
        float totalTamanhoUsinas = (listaUsinas.size() + itensFiltro.size())* tamanhoColunaUsina;
        
        
        for (Usina usina : listaUsinas) {
            relatorio.addColuna(new Coluna(ordem++, usina.getNomeUsina(), tamanhoColunaUsina, CalculoColunaEnum.NAO_CALCULA,
                            false, 0, null, false));            
        }
        
        
        
        for (MetaFiltragem filtragem : itensFiltro) {
            relatorio.addColuna(new Coluna(ordem++, filtragem.getNomeFiltragem(), tamanhoColunaUsina, CalculoColunaEnum.NAO_CALCULA,
                            false, 0, null, false));            
        }
        
           
        float tamanhoColunas = (100.00f - totalTamanhoUsinas) / totalColunas;
        tamanhoColunas = (float) Math.floor(tamanhoColunas);
        
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.patio"), tamanhoColunas,
                        CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.baliza"), tamanhoColunas,
                        CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.inicio"), tamanhoColunas,
                        CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.fim"), tamanhoColunas,
                        CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.navio"), tamanhoColunas,
                        CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.carga"), tamanhoColunas,
                        CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.porao"), tamanhoColunas,
                        CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.maquina"), tamanhoColunas,
                        CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.quantidade"),
                        tamanhoColunas, CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        relatorio.ajustarTamanhoColuna(ordem - 1);
    }

    /**
     * 
     * popularDadosRecuperacao
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 12/08/2009
     * @see
     * @param situacaoPatio
     * @param listaUsinas
     * @return
     * @return Returns the Map<Integer,List<Object>>.
     */
    private Map<Integer, List<Object>> popularDadosRecuperacao(Atividade atividade, List<Usina> listaUsinas) {
        Integer registros = 1;
        Map<Integer, List<Object>> result = new HashMap<Integer, List<Object>>();


            List<MetaFiltragem> itensFiltro = new ArrayList<MetaFiltragem>();
            for (AtividadeRelatorio atividadeRelatorio : atividade.getListaDeAtividadesRelatorio()) {

                List<Object> dadosList = new ArrayList<Object>();
                dadosList.add("grupo1");
                for (Usina usina : listaUsinas) {
                    if (contemUsinaNaLista(atividadeRelatorio.getListaUsinas(), usina.getNomeUsina())) {
                        dadosList.add("X");
                    } else {
                        dadosList.add("");
                    }

                    MetaFiltragem filtragem = usina.getMetaUsina().getFiltragemOrigem();
                    
                    if (filtragem != null && !itensFiltro.contains(filtragem) ) {                
                        itensFiltro.add(filtragem);
                    }
                    
                }
                
                for (MetaFiltragem usina : itensFiltro) {
                    if (contemFiltragemNaLista(atividadeRelatorio.getListaFiltragens(), usina.getNomeFiltragem())) {
                        dadosList.add("X");
                    } else {
                        dadosList.add("");
                    }
                }

            String qtdBaliza = DSSStockyardFuncoesNumeros.getValorFormatado(atividadeRelatorio.getQtdBalizaOrigem(), 2);
            dadosList.add(atividadeRelatorio.getNomePatio());
            dadosList.add(atividadeRelatorio.getNomeBalizaOrigem());
            dadosList.add(atividadeRelatorio.getDataInicio());
            dadosList.add(atividadeRelatorio.getDataFim());
            dadosList.add(atividadeRelatorio.getNomeNavio());
            dadosList.add(atividadeRelatorio.getStrCarga());
            dadosList.add(atividadeRelatorio.getStrPorao());
            dadosList.add(atividadeRelatorio.getNomeMaquina());
            dadosList.add(qtdBaliza);
            result.put(registros++, dadosList);
        }
        return result;
    }

    
    /**
     * 
     * adicionarColunasRecuperacao
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 11/08/2009
     * @see
     * @param relatorio
     * @param listaUsinas
     * @return Returns the void.
     */
    private void adicionarColunasMovimentacao(RelatorioPDF relatorio, List<Usina> listaUsinas) {
        float totalColunas = 12.00f;        
        int ordem = 1;
        relatorio.addColuna(new Coluna(ordem++, "Grupo", 0, CalculoColunaEnum.NAO_CALCULA, true, 1, null, false));
           
        float tamanhoColunas = (100.00f / totalColunas);
        
        tamanhoColunas = (float) Math.floor(tamanhoColunas);
        
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.patio"), tamanhoColunas,
                        CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.baliza"), tamanhoColunas,
                        CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.maquina"), tamanhoColunas,
                        CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        
        
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.inicio"), tamanhoColunas,
                        CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.fim"), tamanhoColunas,
                        CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        

        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.patio"), tamanhoColunas,
                        CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.baliza"), tamanhoColunas,
                        CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.maquina"), tamanhoColunas,
                        CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.navio"), tamanhoColunas,
                        CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.carga"), tamanhoColunas,
                        CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.porao"), tamanhoColunas,
                        CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.quantidade"),
                        tamanhoColunas, CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        relatorio.ajustarTamanhoColuna(ordem - 1);
    }

    /**
     * 
     * adicionarColunasTratamentoPSM
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 12/08/2009
     * @see
     * @param relatorio
     * @param listaUsinas
     * @return Returns the void.
     */
    private void adicionarColunasTratamentoPSM(RelatorioPDF relatorio, List<Usina> listaUsinas) {
        int ordem = 1;
        relatorio.addColuna(new Coluna(ordem++, "Grupo", 0, CalculoColunaEnum.NAO_CALCULA, true, 1, null, false));
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.balizaOrigem"), 10,
                        CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.qtdOrigem"), 10,
                        CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.balizaLixo"), 10,
                        CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.qtdLixo"), 10,
                        CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.balizaPellet"), 10,
                        CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.qtdPellet"), 10,
                        CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.balizaPelota"), 10,
                        CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.qtdPelota"), 10,
                        CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
    }

    /**
     * 
     * popularDadosRelatorio
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 12/08/2009
     * @see
     * @param situacaoPatio
     * @param listaUsinas
     * @return
     * @return Returns the Map<Integer,List<Object>>.
     */
    private Map<Integer, List<Object>> popularDadosTratamentoPSM(Atividade atividade) {
        Integer registros = 1;
        Map<Integer, List<Object>> result = new HashMap<Integer, List<Object>>();

        for (AtividadeRelatorio atividadeRelatorio : atividade.getListaDeAtividadesRelatorio()) {

            String qtdBalizaOrigem = DSSStockyardFuncoesNumeros
                            .getValorFormatado(atividadeRelatorio.getQtdBalizaOrigem(), 2);
            String qtdBalizaDestino = DSSStockyardFuncoesNumeros.getValorFormatado(atividadeRelatorio.getQtdBalizaDestino(),
                            2);
            String qtdBalizaPellet = DSSStockyardFuncoesNumeros
                            .getValorFormatado(atividadeRelatorio.getQtdBalizaPellet(), 2);
            String qtdBalizaPelota = DSSStockyardFuncoesNumeros
                            .getValorFormatado(atividadeRelatorio.getQtdBalizaPelota(), 2);

            List<Object> dadosList = new ArrayList<Object>();
            dadosList.add("grupo1");
            dadosList.add(atividadeRelatorio.getNomeBalizaOrigem());
            dadosList.add(qtdBalizaOrigem);
            dadosList.add(atividadeRelatorio.getNomeBalizaDestino());
            dadosList.add(qtdBalizaDestino);
            dadosList.add(atividadeRelatorio.getNomeBalizaPellet());
            dadosList.add(qtdBalizaPellet);
            dadosList.add(atividadeRelatorio.getNomeBalizaPelota());
            dadosList.add(qtdBalizaPelota);

            result.put(registros++, dadosList);
        }
        return result;
    }

    /**
     * 
     * adicionarColunasRetornoPelletFeed
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 19/08/2009
     * @see
     * @param relatorio
     * @return Returns the void.
     */
    private void adicionarColunasRetornoPelletFeed(RelatorioPDF relatorio) {

        int ordem = 1;
        relatorio.addColuna(new Coluna(ordem++, "Grupo", 0, CalculoColunaEnum.NAO_CALCULA, true, 1, null, false));
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.patio"), 20,
                        CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.baliza"), 20,
                        CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.inicio"), 10,
                        CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.fim"), 10,
                        CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.maquina"), 20,
                        CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.quantidade"), 20,
                        CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        relatorio.ajustarTamanhoColuna(ordem - 1);
    }

    /**
     * 
     * popularDadosRetornoPelletFeed
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 19/08/2009
     * @see
     * @param situacaoPatio
     * @return
     * @return Returns the Map<Integer,List<Object>>.
     */
    private Map<Integer, List<Object>> popularDadosRetornoPelletFeed(Atividade atividade) {
        Integer registros = 1;
        Map<Integer, List<Object>> result = new HashMap<Integer, List<Object>>();

        for (AtividadeRelatorio atividadeRelatorio : atividade.getListaDeAtividadesRelatorio()) {

            List<Object> dadosList = new ArrayList<Object>();
            dadosList.add("grupo1");
            String qtdBaliza = DSSStockyardFuncoesNumeros.getValorFormatado(atividadeRelatorio.getQtdBalizaOrigem(), 2);
            dadosList.add(atividadeRelatorio.getNomePatio());
            dadosList.add(atividadeRelatorio.getNomeBalizaOrigem());
            dadosList.add(atividadeRelatorio.getDataInicio());
            dadosList.add(atividadeRelatorio.getDataFim());
            dadosList.add(atividadeRelatorio.getNomeMaquina());
            dadosList.add(qtdBaliza);
            result.put(registros++, dadosList);
        }
        return result;
    }

    /**
     * 
     * adicionarColunasPilhaEmergencia
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 08/09/2009
     * @see
     * @param relatorio
     * @param listaUsinas
     * @return Returns the void.
     */
    private void adicionarColunasPilhaEmergencia(RelatorioPDF relatorio, List<Usina> listaUsinas) {
        final float tamanhoColunaUsina = 5;
        float totalColunas = 5.00f;
        int ordem = 1;
        relatorio.addColuna(new Coluna(ordem++, "Grupo", 0, CalculoColunaEnum.NAO_CALCULA, true, 1, null, false));
        float totalTamanhoUsinas = listaUsinas.size() * tamanhoColunaUsina;
        
        for (Usina usina : listaUsinas) {
            relatorio.addColuna(new Coluna(ordem++, usina.getNomeUsina(), tamanhoColunaUsina, CalculoColunaEnum.NAO_CALCULA,
                            false, 0, null, false));                                    
        }
        float tamanhoColunas = (100.00f - totalTamanhoUsinas) / totalColunas;
        tamanhoColunas = (float) Math.floor(tamanhoColunas);
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.patio"), tamanhoColunas,
                        CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.baliza"), tamanhoColunas,
                        CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.inicio"), tamanhoColunas,
                        CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.fim"), tamanhoColunas,
                        CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.deslocamento.quantidade"),
                        tamanhoColunas, CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
        relatorio.ajustarTamanhoColuna(ordem - 1);
    }

    /**
     * 
     * popularDadosPilhaEmergencia
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 08/09/2009
     * @see
     * @param atividade
     * @param listaUsinas
     * @return
     * @return Returns the Map<Integer,List<Object>>.
     */
    private Map<Integer, List<Object>> popularDadosPilhaEmergencia(Atividade atividade, List<Usina> listaUsinas) {
        Integer registros = 1;
        Map<Integer, List<Object>> result = new HashMap<Integer, List<Object>>();

        for (AtividadeRelatorio atividadeRelatorio : atividade.getListaDeAtividadesRelatorio()) {

            List<Object> dadosList = new ArrayList<Object>();
            dadosList.add("grupo1");
            for (Usina usina : listaUsinas) {
                if (contemUsinaNaLista(atividadeRelatorio.getListaUsinas(), usina.getNomeUsina())) {
                    dadosList.add("X");
                } else {
                    dadosList.add("");
                }
            }
            String qtdBaliza = DSSStockyardFuncoesNumeros.getValorFormatado(atividadeRelatorio.getQtdBalizaOrigem(), 2);
            dadosList.add(atividadeRelatorio.getNomePatio());
            dadosList.add(atividadeRelatorio.getNomeBalizaOrigem());
            dadosList.add(atividadeRelatorio.getDataInicio());
            dadosList.add(atividadeRelatorio.getDataFim());            
            dadosList.add(qtdBaliza);
            result.put(registros++, dadosList);
        }
        return result;
    }


    /**
     * 
     * popularDadosPilhaEmergencia
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 08/09/2009
     * @see
     * @param atividade
     * @param listaUsinas
     * @return
     * @return Returns the Map<Integer,List<Object>>.
     */
    private Map<Integer, List<Object>> popularDadosMovimentacao(Atividade atividade) {
        Integer registros = 1;
        Map<Integer, List<Object>> result = new HashMap<Integer, List<Object>>();

        for (AtividadeRelatorio atividadeRelatorio : atividade.getListaDeAtividadesRelatorio()) {

            List<Object> dadosList = new ArrayList<Object>();
            dadosList.add("grupo1");            
            String qtdBaliza = DSSStockyardFuncoesNumeros.getValorFormatado(atividadeRelatorio.getQtdBalizaOrigem(), 2);
            dadosList.add(atividadeRelatorio.getNomePatio());
            dadosList.add(atividadeRelatorio.getNomeBalizaOrigem());
            dadosList.add(atividadeRelatorio.getNomeMaquina());            
            dadosList.add(atividadeRelatorio.getDataInicio());
            dadosList.add(atividadeRelatorio.getDataFim());            
            dadosList.add(atividadeRelatorio.getNomePatioDestino());
            dadosList.add(atividadeRelatorio.getNomeBalizaDestino());            
            dadosList.add(atividadeRelatorio.getNomeMaquinaDestino());
            dadosList.add(atividadeRelatorio.getNomeNavio());
            dadosList.add(atividadeRelatorio.getStrCarga());
            dadosList.add(atividadeRelatorio.getStrPorao());
            dadosList.add(qtdBaliza);            
            result.put(registros++, dadosList);
        }
        return result;
    }
}
