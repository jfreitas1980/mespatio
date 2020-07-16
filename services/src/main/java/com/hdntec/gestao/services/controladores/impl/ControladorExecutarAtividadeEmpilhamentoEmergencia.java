package com.hdntec.gestao.services.controladores.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.entity.LugarEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.enums.TipoAtividadeEnum;
import com.hdntec.gestao.domain.plano.enums.TipoMaquinaEnum;
import com.hdntec.gestao.domain.planta.entity.MetaBaliza;
import com.hdntec.gestao.domain.planta.entity.MetaCorreia;
import com.hdntec.gestao.domain.planta.entity.MetaFiltragem;
import com.hdntec.gestao.domain.planta.entity.MetaMaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.MetaPatio;
import com.hdntec.gestao.domain.planta.entity.MetaUsina;
import com.hdntec.gestao.domain.planta.entity.status.AtividadeCampanha;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.Campanha;
import com.hdntec.gestao.domain.planta.entity.status.Correia;
import com.hdntec.gestao.domain.planta.entity.status.Pilha;
import com.hdntec.gestao.domain.planta.entity.status.Usina;
import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;
import com.hdntec.gestao.domain.produto.entity.Rastreabilidade;
import com.hdntec.gestao.domain.vo.atividades.EmpilhamentoEmergenciaVO;
import com.hdntec.gestao.exceptions.AtividadeException;


public class ControladorExecutarAtividadeEmpilhamentoEmergencia
{
	private static ControladorExecutarAtividadeEmpilhamentoEmergencia instance = null;

	/**
	 * Construtor privado.
	 */
	public ControladorExecutarAtividadeEmpilhamentoEmergencia() {
	}

	/**
	 * Retorna a instancia singleton da fAbrica.
	 * 
	 * @return TransLogicDAOFactory
	 */
	public static ControladorExecutarAtividadeEmpilhamentoEmergencia getInstance() {
		if (instance == null) {
			instance = new ControladorExecutarAtividadeEmpilhamentoEmergencia();
		}
		return instance;
	}

    
    public Atividade empilharEmergencia(EmpilhamentoEmergenciaVO empilhamentoEmergenciaVO) throws AtividadeException
   {
      // TODO Auto-generated method stub
      HashMap<MetaPatio, List<Baliza>> mapaBalizasDestinoPorPatio = new HashMap<MetaPatio, List<Baliza>>();
      List<Correia> correias = new ArrayList<Correia>();
      List<AtividadeCampanha> atividadesCampanhas = new ArrayList<AtividadeCampanha>();
      List<Usina> usinas = new ArrayList<Usina>();
      Date dataAtividade = null;
      Boolean finalizarAtividade = Boolean.FALSE;
      Atividade atividade = new Atividade();
      atividade.setTipoAtividade(TipoAtividadeEnum.ATUALIZACAO_PILHA_DE_EMERGENCIA);
      Set<MetaPatio> listaPatios;
      LugarEmpilhamentoRecuperacao lugarEmpRecDestino = new LugarEmpilhamentoRecuperacao();
      List<Pilha> listaNovaPilhaDestino = null;
      double quantidadeEmpilhada = 0;

      try
      {
          /***
           * Valida se existem itens sob manutenção/interdição
           * 
           */
          validarManutencaoInterdicao(empilhamentoEmergenciaVO);
          
          /* verifica se deve finalizar a operacao */
         if (empilhamentoEmergenciaVO.getDataFim() != null)
         {
             //pilhaAtualOrigem = movimentacaoVO.getListaBalizas().get(0).retornaStatusHorario(movimentacaoVO.getDataFim()).retornaStatusHorario(movimentacaoVO.getDataFim());
             //pilhaAtualDestino = movimentacaoVO.getListaBalizasDestino().get(0).retornaStatusHorario(movimentacaoVO.getDataFim()).retornaStatusHorario(movimentacaoVO.getDataFim());
             finalizarAtividade = Boolean.TRUE;
             dataAtividade = empilhamentoEmergenciaVO.getDataFim();
         } //else {
           //  pilhaAtualOrigem = movimentacaoVO.getListaBalizas().get(0).retornaStatusHorario(movimentacaoVO.getDataInicio()).retornaStatusHorario(movimentacaoVO.getDataInicio());
           //  pilhaAtualDestino = movimentacaoVO.getListaBalizasDestino().get(0).retornaStatusHorario(movimentacaoVO.getDataInicio()).retornaStatusHorario(movimentacaoVO.getDataInicio());
         
         if (finalizarAtividade)
         {
            atividade.setDtInicio(empilhamentoEmergenciaVO.getDataInicio());
            atividade.setDtFim(empilhamentoEmergenciaVO.getDataFim());
            atividade.setDtInsert(new Date(System.currentTimeMillis()));
            atividade.setIdUser(1L);
            atividade.setAtividadeAnterior(empilhamentoEmergenciaVO.getAtividadeAnterior());
            atividade.setFinalizada(true);                        
            lugarEmpRecDestino.setDtFim(empilhamentoEmergenciaVO.getDataFim());
            lugarEmpRecDestino.setExecutado(Boolean.TRUE);
            quantidadeEmpilhada = calculaQuantidadePilhaEmergencia(empilhamentoEmergenciaVO, atividade.getDtInicio(), atividade.getDtFim());
         } else
         {
            atividade.setDtInicio(empilhamentoEmergenciaVO.getDataInicio());
            atividade.setDtInsert(new Date(System.currentTimeMillis()));
            atividade.setIdUser(1L);
            dataAtividade = empilhamentoEmergenciaVO.getDataInicio();
         }
         
 		/***
 		 * Gera atividade de campanha
 		 */
 		atividadesCampanhas = AtividadeCampanha.gerarAtividadesCampanha(empilhamentoEmergenciaVO);

 		usinas = MetaUsina.gerarAtividadeUsina(empilhamentoEmergenciaVO, atividade, finalizarAtividade,correias);
         
         /**
          1- aplica atividade nas balizas
         */
         // aplica atividade nas balizas e retorna  a lista de novos estados
         mapaBalizasDestinoPorPatio = MetaBaliza.gerarAtividadeEmpilhamentoEmergencia(empilhamentoEmergenciaVO);
         ArrayList<Baliza> listaTotalNovasBalizas = new ArrayList<Baliza>();
         /** cria as novas pilhas */
        // if (empilhamentoEmergenciaVO.getDataFim() != null) 
         //{
        	 listaPatios = mapaBalizasDestinoPorPatio.keySet();
        	 for (MetaPatio mp : listaPatios)
        	 {
        		 listaTotalNovasBalizas.addAll(mapaBalizasDestinoPorPatio.get(mp));
        		 
        		 Pilha pilhaAtual =  listaTotalNovasBalizas.get(0).getMetaBaliza().retornaStatusHorario(empilhamentoEmergenciaVO.getDataInicio()).retornaStatusHorario(empilhamentoEmergenciaVO.getDataInicio());
        		 
        		 listaNovaPilhaDestino = Pilha.criaPilhasDescontinuas(mapaBalizasDestinoPorPatio.get(mp), empilhamentoEmergenciaVO.getCliente(), 
        		                 dataAtividade, null, empilhamentoEmergenciaVO.getNomePilha(),pilhaAtual);
        	
        	     if (finalizarAtividade) {
        	         for (Pilha p : listaNovaPilhaDestino) {        	              
        	             //p.setHorarioFimFormacao(empilhamentoEmergenciaVO.getDataFim()); 
        	        	 p.setHorarioFimFormacao(empilhamentoEmergenciaVO.getDataFim());
        	              
        	         }
        	     }
        		 
        	 }
         //}
         
         /**
          * 2 - efetua o empilhamento do material 
          */
         realizarEmpilhamentoEmergencia(listaTotalNovasBalizas, quantidadeEmpilhada,empilhamentoEmergenciaVO);

         
         /**
          *  ATUALIZACAO DE STATUS OCIOSO DA CORREIA 
          * 
          */
         if (empilhamentoEmergenciaVO.getDataFim() != null) {              
             for (Correia correia : correias) {
                 correia.setEstado(MetaCorreia.atualizaStatusOcioso(EstadoMaquinaEnum.OCIOSA,empilhamentoEmergenciaVO.getDataFim(),correia.getMetaCorreia()));
             }  
             
         } else {                         
             for (Correia correia : correias) {
                 correia.setEstado(MetaCorreia.atualizaStatusOcioso(EstadoMaquinaEnum.OCIOSA,empilhamentoEmergenciaVO.getDataInicio(),correia.getMetaCorreia()));
             }               
         }

         
         /**
          * 3 - cria o lugar de empilhamento e recuperacao
          */
         lugarEmpRecDestino.setOrdem(0);
         lugarEmpRecDestino.setDtFim(empilhamentoEmergenciaVO.getDataFim());
         lugarEmpRecDestino.setIdUser(1L);
         lugarEmpRecDestino.setDtInicio(empilhamentoEmergenciaVO.getDataInicio());
         lugarEmpRecDestino.addBaliza(listaTotalNovasBalizas);
         lugarEmpRecDestino.addCorreia(correias);
         lugarEmpRecDestino.setQuantidade(quantidadeEmpilhada);
         lugarEmpRecDestino.setTipoProduto(empilhamentoEmergenciaVO.getTipoProduto());
         lugarEmpRecDestino.setNomeDoLugarEmpRec(empilhamentoEmergenciaVO.getNomePilha());
         lugarEmpRecDestino.addAtividadeCampanha(atividadesCampanhas);
         lugarEmpRecDestino.setTaxaDeOperacaoNaPilha(empilhamentoEmergenciaVO.getTaxaOperacao());
         lugarEmpRecDestino.setListaUsinas(usinas);
         atividade.addLugarEmpilhamento(lugarEmpRecDestino);
                  
      }catch (AtividadeException e) {
          throw e;
      } catch (Exception e)
      {
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
    private void validarManutencaoInterdicao(EmpilhamentoEmergenciaVO empilhamentoEmergenciaVO) throws AtividadeException {
        Boolean interditada = Boolean.FALSE;
          StringBuffer strInterditadas = new StringBuffer();
          Date dataInterdicao = empilhamentoEmergenciaVO.getDataInicio();
          if (empilhamentoEmergenciaVO.getDataFim() != null ) {
              dataInterdicao = empilhamentoEmergenciaVO.getDataFim(); 
          }   
          // Balizas
          Set<MetaPatio> listaPatios = empilhamentoEmergenciaVO.getMapaBalizasPorPatio().keySet();
          for (MetaPatio metaPatio : listaPatios)
          {
              List<MetaBaliza> listaBalizasPatio = empilhamentoEmergenciaVO.getMapaBalizasPorPatio().get(metaPatio);
              for (MetaBaliza novaBaliza : listaBalizasPatio) {            
                  if (novaBaliza.balizaInterditado(dataInterdicao)) {
                      interditada = Boolean.TRUE;
                      strInterditadas.append(novaBaliza.getNomeBaliza()).append("\n");                       
                  }
              }                            
          }
          
          // Maquina 
          for (MetaMaquinaDoPatio novaBaliza : empilhamentoEmergenciaVO.getListaMaquinas()) {            
              if (novaBaliza.maquinaInterditado(dataInterdicao)) {
                  interditada = Boolean.TRUE;
                  strInterditadas.append(novaBaliza.getNomeMaquina()).append("\n");                       
              }
          }

          
          // Correias das Máquinas 
          for (MetaMaquinaDoPatio novaBaliza : empilhamentoEmergenciaVO.getListaMaquinas()) {            
              if (!novaBaliza.getTipoDaMaquina().equals(TipoMaquinaEnum.PA_CARREGADEIRA)) {
                  if (novaBaliza.getMetaCorreia().correiaInterditado(dataInterdicao)) {
                      interditada = Boolean.TRUE;
                      strInterditadas.append(novaBaliza.getNomeMaquina()).append("\n");                       
                  }
              }    
          }

        
          //Correias Usinas  
          for (MetaUsina novaBaliza : empilhamentoEmergenciaVO.getListaUsinas()) {                           
                  if (novaBaliza.getMetaCorreia().correiaInterditado(dataInterdicao)) {
                      interditada = Boolean.TRUE;
                      strInterditadas.append(novaBaliza.getMetaCorreia().getNomeCorreia()).append("\n");                       
                  }
           }
          

          //Correias Filtragens  
          for (MetaFiltragem novaBaliza : empilhamentoEmergenciaVO.getListaFiltragens()) {                           
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
     * Metodo que calcula a quantidade de produtos que sera empilhado nas balizas
     * de emergencia
     * @param lugarEmpRecAtual
     * @param dataInico
     * @param dataFinal
     * @return
     */
    private double calculaQuantidadePilhaEmergencia(EmpilhamentoEmergenciaVO empilhamentoEmergenciaVO, Date dataInico, Date dataFinal)
    {
       double quantidadePilhaEmergencia = 0d;

       // calcula a duracao da atividade em horas
       double horasDuracaoAtividade = ((((dataFinal.getTime() - dataInico.getTime()) / 1000)) / 60.0) / 60.0;

       // calcula a quantidade multiplicando-se a taxa de operacao da usina pela duracao em horas da atividade
       quantidadePilhaEmergencia = (empilhamentoEmergenciaVO.getTaxaOperacao() / 24) * horasDuracaoAtividade;

       return quantidadePilhaEmergencia;
    }
   
   private void realizarEmpilhamentoEmergencia(List<Baliza> listaBalizasEmergencia, double quantidadeEmpilhada,EmpilhamentoEmergenciaVO empilhamentoEmergenciaVO) 
   {
	   double quantidadeEmpilhadaPorBaliza = quantidadeEmpilhada / listaBalizasEmergencia.size();
       for (Baliza baliza : listaBalizasEmergencia)
       {
    	   baliza.getProduto().setQuantidade(baliza.getProduto().getQuantidade() + quantidadeEmpilhadaPorBaliza);
    	   if (empilhamentoEmergenciaVO.getDataFim() != null) {
    		  Rastreabilidade rastreabilidade = acrescentaRastreabilidade(empilhamentoEmergenciaVO.getDataInicio(), empilhamentoEmergenciaVO.getDataFim(), null,empilhamentoEmergenciaVO.getListaUsinas().get(0).getCampanhaAtual(empilhamentoEmergenciaVO.getDataFim()), quantidadeEmpilhadaPorBaliza, TipoAtividadeEnum.ATUALIZACAO_PILHA_DE_EMERGENCIA);
    		   if (baliza.getProduto().getListaDeRastreabilidades() == null)
    	         {
    			   baliza.getProduto().addRastreabilidade(new ArrayList<Rastreabilidade>());
    	         }
    	         baliza.getProduto().addRastreabilidade(rastreabilidade);   
    	   }      
       }
   }
   
    
    /* Acrescenta os registros de rastreabilidade no produto */
    private Rastreabilidade acrescentaRastreabilidade(Date horaInicio,
            Date horaTermino, Baliza balizaOrigem, Campanha campanha,
            double quantidadeEmpilhada, TipoAtividadeEnum tipoAtividade) {
       
        Rastreabilidade rastreabilidade = new Rastreabilidade();
        rastreabilidade.setHorarioInicioEntradaDeMaterial(horaInicio);
        rastreabilidade.setHorarioFimEntradaDeMaterial(horaTermino);        
        rastreabilidade.setNumeroRastreabilidade(1L);
        rastreabilidade.setDtInicio(horaInicio);
        rastreabilidade.setDtFim(horaTermino);
        rastreabilidade.setDtInsert(new Date(System.currentTimeMillis()));
        if (campanha != null) {
            rastreabilidade.setNomeUsina(campanha.getMetaUsina().getNomeUsina());
            rastreabilidade.setTaxaDeOperacaoUsina(campanha.getMetaUsina().retornaStatusHorario(horaInicio).getTaxaDeOperacao());
            rastreabilidade.setDataInicioUsina(campanha.getDataInicial());
            rastreabilidade.setDataFimUsina(campanha.getDataFinal());
            rastreabilidade.setNomeCampanhaUsina(campanha.getNomeCampanha());
            rastreabilidade.setTipoProdutoUsina(campanha.getTipoProduto());
            rastreabilidade.setTipoPelletUsina(campanha.getTipoPellet());
            rastreabilidade.setTipoScreeningUsina(campanha.getTipoScreening());
            rastreabilidade.setTipoScreeningUsina(campanha.getTipoScreening());         
        }
        rastreabilidade
                .setTipoAtividade(tipoAtividade);
        rastreabilidade.setQuantidade(quantidadeEmpilhada);
      
        return rastreabilidade;
    }

    
    
}
