package com.hdntec.gestao.services.controladores.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.entity.LugarEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.enums.TipoAtividadeEnum;
import com.hdntec.gestao.domain.plano.enums.TipoMaquinaEnum;
import com.hdntec.gestao.domain.planta.entity.MetaBaliza;
import com.hdntec.gestao.domain.planta.entity.MetaCorreia;
import com.hdntec.gestao.domain.planta.entity.MetaFiltragem;
import com.hdntec.gestao.domain.planta.entity.MetaMaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.MetaUsina;
import com.hdntec.gestao.domain.planta.entity.status.AtividadeCampanha;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.Campanha;
import com.hdntec.gestao.domain.planta.entity.status.Correia;
import com.hdntec.gestao.domain.planta.entity.status.Filtragem;
import com.hdntec.gestao.domain.planta.entity.status.MaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Pilha;
import com.hdntec.gestao.domain.planta.entity.status.Usina;
import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;
import com.hdntec.gestao.domain.produto.entity.Produto;
import com.hdntec.gestao.domain.vo.atividades.AtualizarEmpilhamentoVO;
import com.hdntec.gestao.exceptions.AtividadeException;
import com.hdntec.gestao.services.controladores.IControladorExecutarAtividadeEmpilhamento;


public class ControladorExecutarAtividadeEmpilhamento implements
		IControladorExecutarAtividadeEmpilhamento {

	private static ControladorExecutarAtividadeEmpilhamento instance = null;

	/**
	 * Construtor privado.
	 */
	public ControladorExecutarAtividadeEmpilhamento() {
	}

	/**
	 * Retorna a instancia singleton da fAbrica.
	 * 
	 * @return TransLogicDAOFactory
	 */
	public static ControladorExecutarAtividadeEmpilhamento getInstance() {
		if (instance == null) {
			instance = new ControladorExecutarAtividadeEmpilhamento();
		}
		return instance;
	}

	/* (non-Javadoc)
	 * @see com.hdntec.gestao.services.controladores.IControladorExecutarAtividadeEmpilhamento#empilhar(com.hdntec.gestao.domain.vo.atividades.AtualizarEmpilhamentoVO, java.util.List)
	 */
	public Atividade empilhar(AtualizarEmpilhamentoVO empilhamentoVO,
			List<LugarEmpilhamentoRecuperacao> lugaresAnteriores)
			throws AtividadeException {
		// TODO Auto-generated method stub

		List<AtividadeCampanha> atividadesCampanhas = new ArrayList<AtividadeCampanha>();
		List<Usina> usinas = new ArrayList<Usina>();
		List<Filtragem> filtragens = new ArrayList<Filtragem>();
		List<Baliza> balizasDestino = new ArrayList<Baliza>();
		List<MaquinaDoPatio> maquinasDestino = new ArrayList<MaquinaDoPatio>();
		List<Correia> correiasDestino = new ArrayList<Correia>();		
		Atividade atividade = null;
		// Atividade atividade = null;
		// objetos utilizados para comparaááes
		Baliza balizaAtual = null;
		Pilha novaPilha = null;
		Boolean finalizarAtividade = Boolean.FALSE;
		LugarEmpilhamentoRecuperacao lugarEmpRecAtual = null;
		Pilha pilhaAtualOrigem =null;		
		try {

		    /***
             * Valida se existem itens sob manutenção/interdição
             * 
             */
            validarManutencaoInterdicao(empilhamentoVO);
		    
		    /***
			 * Verifica e está finalizando atividade
			 * 
			 */
			lugarEmpRecAtual = new LugarEmpilhamentoRecuperacao();
			if (empilhamentoVO.getDataFim() != null) {
				finalizarAtividade = Boolean.TRUE;
			}

			atividade = new Atividade();
			atividade
					.setTipoAtividade(TipoAtividadeEnum.ATUALIZACAO_EMPILHAMENTO);

			if (finalizarAtividade) {
				atividade.setFinalizada(Boolean.TRUE);
				atividade.setDtInicio(empilhamentoVO.getDataInicio());
				atividade.setDtFim(empilhamentoVO.getDataFim());
				atividade.setDtInsert(new Date(System.currentTimeMillis()));
				atividade.setIdUser(1L);
				lugarEmpRecAtual.setDtFim(empilhamentoVO.getDataFim());
				lugarEmpRecAtual.setExecutado(Boolean.TRUE);
			} else {
				atividade.setFinalizada(Boolean.FALSE);
				atividade.setDtInicio(empilhamentoVO.getDataInicio());
				atividade.setDtInsert(new Date(System.currentTimeMillis()));
				atividade.setIdUser(1L);
			}
			
            /* verifica se deve finalizar a operacao */
            if (empilhamentoVO.getDataFim() != null)
            {
                pilhaAtualOrigem = empilhamentoVO.getListaBalizas().get(0).retornaStatusHorario(empilhamentoVO.getDataFim()).retornaStatusHorario(empilhamentoVO.getDataFim());
            } else {
                pilhaAtualOrigem = empilhamentoVO.getListaBalizas().get(0).retornaStatusHorario(empilhamentoVO.getDataInicio()).retornaStatusHorario(empilhamentoVO.getDataInicio());
            }
            
			/***
			 * Gera atividade de campanha
			 */
			atividadesCampanhas = AtividadeCampanha
					.gerarAtividadesMultiCampanha(empilhamentoVO);
            // em empilhamento não gera atividade para correia pela usina ou pela filtragem, só pela maquina  
			usinas = MetaUsina.gerarAtividadeUsina(empilhamentoVO, atividade,
					finalizarAtividade,null);

			filtragens = MetaFiltragem.gerarAtividadeFiltragem(empilhamentoVO, atividade,
		                    finalizarAtividade,null);
			
		  	
			List<Campanha> campanhas = new ArrayList<Campanha>();
	        for (AtividadeCampanha atvCamp : atividadesCampanhas) {
	            campanhas.add(atvCamp.getCampanha());
	        }    
	        empilhamentoVO.setCampanhas(campanhas);
			
			// aplica atividade nas balizas e retorna a lista de novos estados
			balizasDestino = MetaBaliza
					.gerarAtividadeEmpilharBalizas(empilhamentoVO);
			// define a baliza atual como sendo a primeira da lista
			balizaAtual = balizasDestino.get(0);

			/**
			 * 
			 * 1 - cria a pilha com as balizas da atividade - cenário apenas em
			 * atualizaááo de empilhamento
			 */
			novaPilha = Pilha.criaPilha(empilhamentoVO.getNomePilha(),
					balizasDestino, empilhamentoVO.getCliente(), empilhamentoVO
							.getDataInicio().getTime(),pilhaAtualOrigem);
			
			
			
			/**
			 * 2- aplica atividade nas maquinas
			 */
			MetaMaquinaDoPatio.gerarAtividadeMaquina(empilhamentoVO,
					lugaresAnteriores, null,balizasDestino, maquinasDestino,
					correiasDestino, atividade, finalizarAtividade);
            
			if (finalizarAtividade) {
    			for (MaquinaDoPatio novaStatusMaquina : maquinasDestino) {
    			   
    			    
    			    Atividade atv1 = null;
    			        atv1 =    novaStatusMaquina.getMetaMaquina().existeMovimentacaoPSMPendente();    			    
        			    if (atv1 == null) {
        			        atv1 = novaStatusMaquina.getMetaMaquina().existeMovimentacaoPelletPendente();
        			        if (atv1 == null) {
                                atv1 = novaStatusMaquina.getMetaMaquina().existeMovimentacaoEmergenciaPendente();
        			        }    			    
        			    }
    			        
    			    if ( (novaStatusMaquina.getMetaMaquina().getTipoDaMaquina().equals(TipoMaquinaEnum.EMPILHADEIRA)  
    			        || novaStatusMaquina.getMetaMaquina().getTipoDaMaquina().equals(TipoMaquinaEnum.EMPILHADEIRA_RECUPERADORA)) && atv1 != null ) {
    			        novaStatusMaquina.setEstado(EstadoMaquinaEnum.OPERACAO);
                        novaStatusMaquina.setAtividade(atv1);
                        novaStatusMaquina.getCorreia().setEstado(EstadoMaquinaEnum.OPERACAO);
            
                        
    			    }
			    }
			
			}	
			
			
			
			/**
			 * 
			 * 4 - FINALIZAR ATIVIDADE ANTERIOR caso exista
			 */

			finalizarAtividadesAnteriores(empilhamentoVO, lugaresAnteriores,
					usinas, filtragens, finalizarAtividade, balizaAtual, novaPilha,
					atividade,lugarEmpRecAtual,pilhaAtualOrigem);

	         if (pilhaAtualOrigem != null) {
	        	    lugarEmpRecAtual.addPilhaEditada(pilhaAtualOrigem);
	                novaPilha.addBaliza(pilhaAtualOrigem.getListaDeBalizas());
	            }    
			
			if (empilhamentoVO.getDataFim() != null) { 
				Pilha.decompoePilhaEditada(novaPilha, empilhamentoVO.getDataFim());
                novaPilha.setHorarioFimFormacao(empilhamentoVO.getDataFim());
                for (Correia correia : correiasDestino) {
                    correia.setEstado(MetaCorreia.atualizaStatusOcioso(EstadoMaquinaEnum.OCIOSA,empilhamentoVO.getDataFim(),correia.getMetaCorreia()));
                }  
                
			} else {                				
			    for (Correia correia : correiasDestino) {
	                correia.setEstado(MetaCorreia.atualizaStatusOcioso(EstadoMaquinaEnum.OCIOSA,empilhamentoVO.getDataInicio(),correia.getMetaCorreia()));
	            }  	            
            }
			
			
			
			/**
			 * 
			 * 5 - cria o lugar de empilhament
			 */
			lugarEmpRecAtual.setOrdem(0);
			lugarEmpRecAtual.setDtInsert(new Date(System.currentTimeMillis()));
			lugarEmpRecAtual.setIdUser(1L);
			lugarEmpRecAtual.setDtInicio(empilhamentoVO.getDataInicio());
			lugarEmpRecAtual.setQuantidade(0D);
			lugarEmpRecAtual.setTipoProduto(empilhamentoVO.getTipoProduto());
			lugarEmpRecAtual.addPilhaEditada(novaPilha);
			//lugarEmpRecAtual.add
			// seta os objetos no lugar de empilhamento
			lugarEmpRecAtual.addMaquinaDoPatio(maquinasDestino);
			lugarEmpRecAtual.addBaliza(balizasDestino);
			lugarEmpRecAtual.addAtividadeCampanha(atividadesCampanhas);
			lugarEmpRecAtual.addCorreia(correiasDestino);
			lugarEmpRecAtual.addUsina(usinas);
			lugarEmpRecAtual.addFiltragem(filtragens);
			lugarEmpRecAtual.setNomeDoLugarEmpRec(empilhamentoVO.getNomePilha());
			atividade.addLugarEmpilhamento(lugarEmpRecAtual);

		}catch (AtividadeException e) {
		    throw e;
	    } catch (Exception e) {
			e.printStackTrace();
		} 
		return atividade;

	}

    /**
     * validarManutencaoInterdicao
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 25/01/2011
     * @see
     * @param 
     * @return void
     */
    private void validarManutencaoInterdicao(AtualizarEmpilhamentoVO empilhamentoVO) throws AtividadeException {
        Boolean interditada = Boolean.FALSE;
        StringBuffer strInterditadas = new StringBuffer();
        Date dataInterdicao = empilhamentoVO.getDataInicio();
        if (empilhamentoVO.getDataFim() != null ) {
            dataInterdicao = empilhamentoVO.getDataFim(); 
        }   
        
        // Baliza
        for (MetaBaliza novaBaliza : empilhamentoVO.getListaBalizas()) {            
            if (novaBaliza.balizaInterditado(dataInterdicao)) {
                interditada = Boolean.TRUE;
                strInterditadas.append(novaBaliza.getNomeBaliza()).append("\n");                       
            }
        }
        
        // Maquina 
        for (MetaMaquinaDoPatio novaBaliza : empilhamentoVO.getListaMaquinas()) {            
            if (novaBaliza.maquinaInterditado(dataInterdicao)) {
                interditada = Boolean.TRUE;
                strInterditadas.append(novaBaliza.getNomeMaquina()).append("\n");                       
            }
        }

        // Correias  Maquinas
        for (MetaMaquinaDoPatio novaBaliza : empilhamentoVO.getListaMaquinas()) {            
            if (!novaBaliza.getTipoDaMaquina().equals(TipoMaquinaEnum.PA_CARREGADEIRA)) {
                if (novaBaliza.getMetaCorreia().correiaInterditado(dataInterdicao)) {
                    interditada = Boolean.TRUE;
                    strInterditadas.append(novaBaliza.getMetaCorreia().getNomeCorreia()).append("\n");                       
                }
            }
        }
         //Correias Usinas  
        for (MetaUsina novaBaliza : empilhamentoVO.getListaUsinas()) {                           
                if (novaBaliza.getMetaCorreia().correiaInterditado(dataInterdicao)) {
                    interditada = Boolean.TRUE;
                    strInterditadas.append(novaBaliza.getMetaCorreia().getNomeCorreia()).append("\n");                       
                }
         }
        

        //Correias Filtragens  
        for (MetaFiltragem novaBaliza : empilhamentoVO.getListaFiltragens()) {                           
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
	 * finalizarAtividadesAnteriores
	 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
	 * @since 06/07/2010
	 * @see
	 * @param 
	 * @return void
	 */
	private void finalizarAtividadesAnteriores(
			AtualizarEmpilhamentoVO empilhamentoVO,
			List<LugarEmpilhamentoRecuperacao> lugaresAnteriores,
			List<Usina> usinas,List<Filtragem> filtragens, Boolean finalizarAtividade, Baliza balizaAtual,
			Pilha novaPilha, Atividade atividadeAtual,LugarEmpilhamentoRecuperacao lugaresAtual,Pilha pilhaAtualOrigem) {
		List<Baliza> ociosas = new ArrayList<Baliza>();
		Atividade atividadeAnterior = null;
		for (LugarEmpilhamentoRecuperacao lugarAnterior : lugaresAnteriores) {
			atividadeAnterior = lugarAnterior.getAtividade();
			atividadeAtual.setAtividadeAnterior(atividadeAnterior);
			atividadeAtual.setFinalizada(Boolean.TRUE);
			List<Baliza> balizasAnteriores = lugarAnterior.getListaDeBalizas();
			atividadeAnterior.setDtFim(empilhamentoVO.getDataInicio());
			lugarAnterior.setDtFim(empilhamentoVO.getDataInicio());
			lugarAnterior.setExecutado(Boolean.TRUE);

			Pilha pilhaAtual = balizasAnteriores.get(0).retornaStatusHorario(
					empilhamentoVO.getDataInicio());
            if (novaPilha != null && pilhaAtual != null) { 
                novaPilha.setHorarioInicioFormacao(pilhaAtual.getHorarioInicioFormacao());
		    }
			for (Baliza b : balizasAnteriores) {
				Baliza ociosa = null;
				if (!b.getEstado().equals(EstadoMaquinaEnum.OCIOSA)) {
    				b.setHorarioFimFormacao(empilhamentoVO.getDataInicio());
    				b.setDtFim(empilhamentoVO.getDataInicio());
    				// se mudou a baliza gera estado ocioso
    				if (b != null
    						&& b.getNumero().intValue() != balizaAtual.getNumero()
    								.intValue()) {
    					// gera estado da baliza ocioso
    					ociosa = b.getMetaBaliza().clonarStatus(
    							empilhamentoVO.getDataInicio());
    					ociosa.setEstado(EstadoMaquinaEnum.OCIOSA);
    					ociosas.add(ociosa);
    				}
    				if (ociosa != null) {
    					novaPilha.addBaliza(ociosa);
    				} else if (ociosa == null
    						&& b != null
    						&& b.getNumero().intValue() != balizaAtual.getNumero()
    								.intValue()) {
    					novaPilha.addBaliza(b);
    				} else if (ociosa == null
    						&& b != null
    						&& b.getNumero().intValue() == balizaAtual.getNumero()
    								.intValue()) {
    					/***
    					 * TODO BLENDAR PRODUTO DA BALIZA ATUAL COM O PRODUTO QUE
    					 * ESTá SENDO EMPILHADO
    					 * 
    					 */
    					Produto produtoAtual = b.getProduto();
    					if (produtoAtual != null
    							&& produtoAtual.getQuantidade() > 0) {
    						Produto produtoBaliza = produtoAtual.copiarStatus();
    						produtoBaliza.setTipoProduto(empilhamentoVO
    								.getTipoProduto());
    						balizaAtual.setProduto(produtoBaliza);
					}
				}
				/**
				 * TODO VALIDAR SE VAI FUNCIONAR CORRETAMENTE, TESTAR COM BALIZA
				 * Q APONTA PARA DUAS PILHAS CASO CONTRáRIO PODEMOS GERAR UM
				 * ESTADO NOVO PARA CADA UMA DAS BALIZAS DA PILHA ANTIGA PORáM
				 * MUITOS DADOS !!
				 */
			   }
			}
			lugarAnterior.addBaliza(ociosas);
			novaPilha.addBaliza(pilhaAtual.getListaDeBalizas());
			lugaresAtual.addBaliza(ociosas);
			//Pilha.decompoePilhaEditada(novaPilha, empilhamentoVO.getDataInicio());
			
            usinas = MetaUsina.finalizarAtividadeUsina(empilhamentoVO,
                            finalizarAtividade, atividadeAnterior,null);
            lugaresAtual.addUsina(usinas);
            
            filtragens = MetaFiltragem.finalizarAtividadeFiltragem(empilhamentoVO, finalizarAtividade, atividadeAnterior);
            
            lugaresAtual.addFiltragem(filtragens);
            
			if (finalizarAtividade) {
				novaPilha.setFim(empilhamentoVO.getDataInicio().getTime());
			}

			
		}

	}
}
