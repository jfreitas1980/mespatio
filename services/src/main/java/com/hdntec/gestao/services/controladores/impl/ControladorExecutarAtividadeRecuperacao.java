package com.hdntec.gestao.services.controladores.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hdntec.gestao.domain.navios.entity.MetaCarga;
import com.hdntec.gestao.domain.navios.entity.status.Carga;
import com.hdntec.gestao.domain.navios.entity.status.Navio;
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
import com.hdntec.gestao.domain.planta.entity.status.Correia;
import com.hdntec.gestao.domain.planta.entity.status.Filtragem;
import com.hdntec.gestao.domain.planta.entity.status.MaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Pilha;
import com.hdntec.gestao.domain.planta.entity.status.Usina;
import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;
import com.hdntec.gestao.domain.vo.atividades.AtualizarRecuperacaoVO;
import com.hdntec.gestao.exceptions.AtividadeException;
import com.hdntec.gestao.services.controladores.IControladorExecutarAtividadeRecuperacao;

/**
 * <P><B>Description :</B><BR>
 * General ControladorExecutarAtividade
 * </P>
 * <P>
 * <B>
 * SAs : <BR>
 * 8148
 * </B>
 * @author <a href="mailto:paulo@cflex.com.br">paulo</a>
 * @since 10/06/2010
 * @version $Revision: 1.1 $
 */
public class ControladorExecutarAtividadeRecuperacao implements IControladorExecutarAtividadeRecuperacao 
{

	private static ControladorExecutarAtividadeRecuperacao instance = null; 
	
	
	
	   /**
	    * Construtor privado.
	    */
	   public ControladorExecutarAtividadeRecuperacao()
	   {
	   }

	   /**
	    * Retorna a instancia singleton da fábrica.
	    * @return TransLogicDAOFactory
	    */
	   public static ControladorExecutarAtividadeRecuperacao getInstance()
	   {
	      if (instance == null)
	      {
	         instance = new ControladorExecutarAtividadeRecuperacao();
	      }
	      return instance;
	   }
	
	public Atividade recuperar(AtualizarRecuperacaoVO recuperacaoVO,List<LugarEmpilhamentoRecuperacao> lugaresAnteriores) throws AtividadeException {
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
        Pilha novaPilha = null;
        Boolean finalizarAtividade = Boolean.FALSE;
        LugarEmpilhamentoRecuperacao lugarEmpRecAtual = null;
        Pilha pilhaAtual = null;        
        try {
            /***
             * Valida se existem itens sob manutenção/interdição
             * 
             */
           // validarManutencaoInterdicao(recuperacaoVO);
            
            /***
             * Verifica e está finalizando atividade
             * 
             */

            if (recuperacaoVO.getDataFim() != null) {
                finalizarAtividade = Boolean.TRUE;
            }
       
            
            lugarEmpRecAtual = new LugarEmpilhamentoRecuperacao();
            atividade = new Atividade();
            atividade.setTipoAtividade(TipoAtividadeEnum.ATUALIZACAO_RECUPERACAO);
            if (finalizarAtividade) {
            	atividade.setFinalizada(Boolean.TRUE);
                atividade.setDtInicio(recuperacaoVO.getDataInicio());
                atividade.setDtFim(recuperacaoVO.getDataFim());
                atividade.setDtInsert(new Date(System.currentTimeMillis()));
                atividade.setIdUser(1L);
                
                lugarEmpRecAtual.setDtFim(recuperacaoVO.getDataFim());
                lugarEmpRecAtual.setExecutado(Boolean.TRUE);
            } else {                
                atividade.setDtInicio(recuperacaoVO.getDataInicio());
                atividade.setDtInsert(new Date(System.currentTimeMillis()));
                atividade.setIdUser(1L);                
            }             
            atividade.setAtividadeAnterior(recuperacaoVO.getAtividadeAnterior());    
            
            /**
                 * TODO IMPLEMENTAR NA USINA 
                 *  recuperacao na baliza
                 */
                if (recuperacaoVO.getListaBalizas() != null && recuperacaoVO.getListaBalizas().size() > 0 ) {
                   
                	MetaBaliza metaBalizaMeste = recuperacaoVO.getListaBalizas().get(0);                		
                	Baliza balizaAnterior = metaBalizaMeste.retornaStatusHorario(recuperacaoVO.getDataInicio());
                       
                   // int idxGerado = metaBalizaMeste.getListaStatus().indexOf(balizaAnterior);
                    
                   // balizaAnterior = metaBalizaMeste.getListaStatus().get(idxGerado-1);
                  
                    pilhaAtual =  balizaAnterior.retornaStatusHorario(recuperacaoVO.getDataInicio());
                                        
                    // aplica atividade nas balizas e retorna  a lista de novos estados
                    balizasDestino = MetaBaliza.gerarAtividadeRecuperarBalizas(recuperacaoVO);
                    
                    for (Baliza b : balizasDestino) {
                    	b.setRetirandoMaterial(1L);
                    }
                    
                    /**
                     * 
                     *  1 - cria a pilha com as balizas da atividade - cenário apenas em atualizaááo de empilhamento 
                     */
                    //novaPilha = Pilha.criaPilha(balizasDestino, recuperacaoVO.getDataInicio().getTime(),pilhaAtual);                    
                    novaPilha = Pilha.criaPilha(recuperacaoVO.getNomePilha(), balizasDestino, recuperacaoVO.getCliente(),recuperacaoVO.getDataInicio().getTime(),pilhaAtual);
            
                    if (finalizarAtividade) {
                    	pilhaAtual.setDtFim(recuperacaoVO.getDataFim());
                    	novaPilha.setDtInicio(recuperacaoVO.getDataFim());
                    } 
                    
                    
                    MetaMaquinaDoPatio.gerarAtividadeMaquina(recuperacaoVO, lugaresAnteriores, null, balizasDestino,   maquinasDestino,
                            correiasDestino, atividade, finalizarAtividade);
                
                    
                    	for (Baliza b : pilhaAtual.getListaDeBalizas()) {
                    		if (getBaliza(b, balizasDestino) < 0) {
                    			Baliza novaBaliza = null;
                    			if (finalizarAtividade) {
                    				novaBaliza = b.getMetaBaliza().clonarStatus(recuperacaoVO.getDataFim());
                    			} else{
                    				novaBaliza = b.getMetaBaliza().clonarStatus(recuperacaoVO.getDataInicio());	
                    			}                  
                    			novaBaliza.setRetirandoMaterial(0L);
                    			novaPilha.addBaliza(novaBaliza);	
                    			balizasDestino.add(novaBaliza);
                    		}	
                    	}	
                    //}
                    
                	
                    //novaPilha.addBaliza(pilhaAtual.getListaDeBalizas());
                    
                    
                    // define atividade da maquina destino atualizando dados da correia
                    
                } else {
                	/***
                     * Gera atividade de campanha 
                     */
                    atividadesCampanhas = AtividadeCampanha.gerarAtividadesCampanha(recuperacaoVO);
                
                	
                	usinas = MetaUsina.gerarAtividadeUsina(recuperacaoVO, atividade, finalizarAtividade,correiasDestino);
                	
                	filtragens = MetaFiltragem.gerarAtividadeFiltragem(recuperacaoVO, atividade,
                                    finalizarAtividade,correiasDestino);
                	
                    	
                }
               
                
                Carga novoStatusCarga = MetaCarga.gerarAtividadeCarga(recuperacaoVO);
                                
                
                /**
                 *  ATUALIZACAO DE STATUS OCIOSO DA CORREIA 
                 * 
                 */
                if (recuperacaoVO.getDataFim() != null) {              
                    for (Correia correia : correiasDestino) {
                        correia.setEstado(MetaCorreia.atualizaStatusOcioso(EstadoMaquinaEnum.OCIOSA,recuperacaoVO.getDataFim(),correia.getMetaCorreia()));
                    }  
                    
                } else {                         
                    for (Correia correia : correiasDestino) {
                        correia.setEstado(MetaCorreia.atualizaStatusOcioso(EstadoMaquinaEnum.OCIOSA,recuperacaoVO.getDataInicio(),correia.getMetaCorreia()));
                    }               
                }
                
                /**
                   * 
                   *  5 - cria o lugar de empilhamento 
                   */
                  lugarEmpRecAtual.setOrdem(0);  
                  lugarEmpRecAtual.setDtInsert(new Date(System.currentTimeMillis()));
                  lugarEmpRecAtual.setIdUser(1L);
                  lugarEmpRecAtual.setDtInicio(recuperacaoVO.getDataInicio());
                  lugarEmpRecAtual.setTipoProduto(recuperacaoVO.getTipoProduto());
                  lugarEmpRecAtual.setQuantidade(0D);
                  // seta os objetos no lugar de empilhamento 
                  lugarEmpRecAtual.setNomePorao(recuperacaoVO.getNomePorao());
                  lugarEmpRecAtual.addMaquinaDoPatio(maquinasDestino);
                  lugarEmpRecAtual.addBaliza(balizasDestino);            
                  lugarEmpRecAtual.addAtividadeCampanha(atividadesCampanhas);
                  lugarEmpRecAtual.addCorreia(correiasDestino);                  
                  lugarEmpRecAtual.addCarga(novoStatusCarga);
                  lugarEmpRecAtual.addUsina(usinas);
                  lugarEmpRecAtual.addPilhaEditada(novaPilha);
                  lugarEmpRecAtual.addPilhaEditada(pilhaAtual);
                  lugarEmpRecAtual.addFiltragem(filtragens);
                  lugarEmpRecAtual.setNomeDoLugarEmpRec(recuperacaoVO.getNomePilha());
                  lugarEmpRecAtual.setSentido(recuperacaoVO.getSentidoRecuperacao());
                  
                  
                  atividade.addLugarEmpilhamento(lugarEmpRecAtual);
                  
                 
      /*  }catch (AtividadeException e) {
            throw e;*/
        } catch (Exception e) {
             throw new AtividadeException(e);  
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
    private void validarManutencaoInterdicao(AtualizarRecuperacaoVO recuperacaoVO) throws AtividadeException {
        Boolean interditada = Boolean.FALSE;
        StringBuffer strInterditadas = new StringBuffer();
        Date dataInterdicao = recuperacaoVO.getDataInicio();
        if (recuperacaoVO.getDataFim() != null ) {
            dataInterdicao = recuperacaoVO.getDataFim(); 
        }   
        // 
        Navio navio = recuperacaoVO.getMetaCarga().getMetaNavio().retornaStatusHorario(dataInterdicao);
        if (navio.getBercoDeAtracacao().getMetaBerco().getMetaPier().pierInterditado(dataInterdicao)) {
            interditada = Boolean.TRUE;
            strInterditadas.append(navio.getBercoDeAtracacao().getMetaBerco().getMetaPier().getNomePier()).append("\n");                       
        }
        
        // Baliza em interdição pode recuperar                  
            
        // Maquina 
            for (MetaMaquinaDoPatio novaBaliza : recuperacaoVO.getListaMaquinas()) {            
                if (novaBaliza.maquinaInterditado(dataInterdicao)) {
                    interditada = Boolean.TRUE;
                    strInterditadas.append(novaBaliza.getNomeMaquina()).append("\n");                       
                }
            }

            // Correias Máquinas 
            for (MetaMaquinaDoPatio novaBaliza : recuperacaoVO.getListaMaquinas()) {            
                if (!novaBaliza.getTipoDaMaquina().equals(TipoMaquinaEnum.PA_CARREGADEIRA)) {
                    if (novaBaliza.getMetaCorreia().correiaInterditado(dataInterdicao)) {
                        interditada = Boolean.TRUE;
                        strInterditadas.append(novaBaliza.getMetaCorreia().getNomeCorreia()).append("\n");                       
                    }
                }    
            }
            
            
            //Correias Usinas  
            for (MetaUsina novaBaliza : recuperacaoVO.getListaUsinas()) {                           
                    if (novaBaliza.getMetaCorreia().correiaInterditado(dataInterdicao)) {
                        interditada = Boolean.TRUE;
                        strInterditadas.append(novaBaliza.getMetaCorreia().getNomeCorreia()).append("\n");                       
                    }
             }
            

            //Correias Filtragens  
            for (MetaFiltragem novaBaliza : recuperacaoVO.getListaFiltragens()) {                           
                    if (novaBaliza.getMetaCorreia().correiaInterditado(dataInterdicao)) {
                        interditada = Boolean.TRUE;
                        strInterditadas.append(novaBaliza.getMetaCorreia().getNomeCorreia()).append("\n");                       
                    }
             }

            if (interditada) {
                throw new AtividadeException("Existem itens sob interdição/manutenção! \n" + strInterditadas.toString() );
            }
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
	
}   

