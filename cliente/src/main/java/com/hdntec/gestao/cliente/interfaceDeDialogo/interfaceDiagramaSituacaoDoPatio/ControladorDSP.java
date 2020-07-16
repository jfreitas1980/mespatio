package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.hdntec.gestao.cliente.interfaceDeDialogo.IControladorInterfaceInicial;
import com.hdntec.gestao.cliente.interfaceDeDialogo.InterfaceInicial;
import com.hdntec.gestao.cliente.interfaceDeDialogo.ModoDeOperacaoEnum;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios.InterfacePier;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.InterfaceEditarCampanha.ACAO;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.comparadores.ComparadorInterfaceBaliza;
import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.cliente.planoDeEmpilhamentoERecuperacaoCliente.atualizacao.edicao.ControladorAtualizacaoEdicao;
import com.hdntec.gestao.domain.navios.entity.status.Carga;
import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.Campanha;
import com.hdntec.gestao.domain.planta.entity.status.Correia;
import com.hdntec.gestao.domain.planta.entity.status.Filtragem;
import com.hdntec.gestao.domain.planta.entity.status.Manutencao;
import com.hdntec.gestao.domain.planta.entity.status.MaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Patio;
import com.hdntec.gestao.domain.planta.entity.status.Pilha;
import com.hdntec.gestao.domain.planta.entity.status.Usina;
import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;
import com.hdntec.gestao.domain.produto.entity.TipoItemDeControle;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.domain.produto.enums.TipoDeProdutoEnum;
import com.hdntec.gestao.exceptions.BalizasSelecionadasException;
import com.hdntec.gestao.exceptions.BlendagemInvalidaException;
import com.hdntec.gestao.exceptions.CampanhaIncompativelException;
import com.hdntec.gestao.exceptions.ConsolidacaoNaoNecessariaException;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.ExcessoDeMaterialParaEmbarqueException;
import com.hdntec.gestao.exceptions.ModoDeEdicaoException;
import com.hdntec.gestao.exceptions.OperacaoCanceladaPeloUsuarioException;
import com.hdntec.gestao.exceptions.PilhaPelletFeedNaoEncontradaException;
import com.hdntec.gestao.exceptions.ProdutoIncompativelException;
import com.hdntec.gestao.exceptions.ValidacaoCampoException;
import com.hdntec.gestao.login.entity.Permission;
import com.hdntec.gestao.util.PropertiesUtil;

/**
 * Controla as operações do subsistema de interface gráfica DSP
 * 
 * @author andre
 * 
 */
public class ControladorDSP implements IControladorDSP
{

   public ControladorDSP()
   {
      this.controladorAtualizacaoEdicao = new ControladorAtualizacaoEdicao();
      this.selecionaMenuBaliza = Boolean.FALSE;
   }
   private ControladorAtualizacaoEdicao controladorAtualizacaoEdicao;

   /** a lista de interfaces gráficas de {@link Patio} */
   private List<InterfacePatio> listaDePatios;

   /** a lista de interfaces gráficas de {@link Correia} */
   private List<InterfaceCorreia> listaDeCorreias;

   /** a lista de interfaces gráficas de {@link Usina} */
   private List<InterfaceUsina> listaDeUsinas;

   /** a lista de interfaces gráficas de {@link MaquinaDoPatio}*/
   private List<InterfaceMaquinaDoPatio> listaDeMaquinas;

   /** a lista de interfaces gráficas de {@link Baliza} */
   private List<InterfaceBaliza> listaDeBalizas;

   /** a interfaces gráfica DSP */
   private InterfaceDSP interfaceDSP;

   /** acesso às funcionalidades do subsistema de interface gráfica principal */
   private IControladorInterfaceInicial interfaceInicial;

   /** a lista de interfaces gráficas de {@link Campanha} */
   private List<InterfaceCampanha> listaInterfaceCampanha;

   /** Interface utilizada para o modo de edição manual das informações dos itens do DSP*/
   private InterfaceDadosEdicao interfaceDadosEdicao;
   
   private Boolean selecionaMenuBaliza;
   
	/**variavel para inserir o log de blendagem de produtos diferentes para deterninados tipos de usuarios */
   private Logger logger = Logger.getLogger("blendagem");
   
   @Override
   public void selecionaCampanhaDeUsina(Campanha campanhaSelecionada, Double quantidade, TipoDeProdutoEnum tipoDeProdutoSelecionado) throws BlendagemInvalidaException, CampanhaIncompativelException, ExcessoDeMaterialParaEmbarqueException, ProdutoIncompativelException
   {
      interfaceInicial.verificaBlendagemCampanhaDeUsina(campanhaSelecionada, quantidade, tipoDeProdutoSelecionado);
   }

   @Override
   public void deselecionaCampanhaDeUsina(Campanha campanhaSelecionada) throws BlendagemInvalidaException, ProdutoIncompativelException
   {
      interfaceInicial.verificaBlendagemRemocaoCampanhaDeUsina(campanhaSelecionada);
   }

   @Override
   public void selecionaBaliza(Baliza balizaSelecionada) throws BlendagemInvalidaException, CampanhaIncompativelException, ProdutoIncompativelException, ExcessoDeMaterialParaEmbarqueException
   {
      interfaceInicial.verificaBlendagemBaliza(balizaSelecionada);
   }

   @Override
   public void deselecionaBaliza(Baliza balizaDeselecionada) throws BlendagemInvalidaException, CampanhaIncompativelException, ProdutoIncompativelException
   {
      interfaceInicial.verificaRemocaoBlendagemBaliza(balizaDeselecionada);
   }

   @Override
   public void deselecionaBalizas()
   {
      List<InterfaceBaliza> listaAuxiliarParaIterar = new ArrayList<InterfaceBaliza>();
      for (InterfaceBaliza interfaceBaliza : this.getListaDeBalizas())
      {
         listaAuxiliarParaIterar.add(interfaceBaliza);
      }

      for (InterfaceBaliza interfaceBaliza : listaAuxiliarParaIterar)
      {
         interfaceBaliza.deselecionar();
      }
      this.listaDeBalizas = null;
   }

   @Override
   public void ativarMensagem(InterfaceMensagem interfaceMensagem)
   {
      interfaceInicial.ativarMensagem(interfaceMensagem);
   }

   @Override
   public void mostrarSituacaoDePatio(SituacaoPatio situacaoDePatio)
   {
      throw new UnsupportedOperationException();
   }

   @Override
   public List<Campanha> buscarCampanhasSelecionadas()
   {
      return interfaceInicial.buscarCampanhasSelecionadas();
   }

   @Override
   public List<Baliza> buscarBalizasSelecionadas()
   {
      return interfaceInicial.buscarBalizasSelecionadas();
   }

   @Override
   public ModoDeOperacaoEnum obterModoOperacao()
   {
      return interfaceInicial.obterModoOperacao();
   }

   @Override
   public InterfaceDSP getInterfaceDSP()
   {
      return interfaceDSP;
   }

   public void setInterfaceDSP(InterfaceDSP interfaceDSP)
   {
      this.interfaceDSP = interfaceDSP;
   }

   public IControladorInterfaceInicial getInterfaceInicial()
   {
      return interfaceInicial;
   }

   public void setInterfaceInicial(IControladorInterfaceInicial interfaceInicial)
   {
      this.interfaceInicial = interfaceInicial;
   }

   @Override
   public List<InterfaceBaliza> getListaDeBalizas()
   {
      if (listaDeBalizas == null)
      {
         this.listaDeBalizas = new ArrayList<InterfaceBaliza>();
      }
      return listaDeBalizas;
   }

   public void setListaDeBalizas(List<InterfaceBaliza> listaDeBalizas)
   {
      this.listaDeBalizas = listaDeBalizas;
   }

   @Override
   public List<InterfaceCorreia> getListaDeCorreias()
   {
      return interfaceDSP.getListaCorreias();
   }

   public void setListaDeCorreias(List<InterfaceCorreia> listaDeCorreias)
   {
      this.listaDeCorreias = listaDeCorreias;
   }

   @Override
   public List<InterfacePatio> getListaDePatios()
   {
      return interfaceDSP.getListaDePatios();
   }

   public void setListaDePatios(List<InterfacePatio> listaDePatios)
   {
      this.listaDePatios = listaDePatios;
   }

   @Override
   public List<InterfaceUsina> getListaDeUsinas()
   {
      return interfaceDSP.getListaUsinas();
   }

   public void setListaDeUsinas(List<InterfaceUsina> listaDeUsinas)
   {
      this.listaDeUsinas = listaDeUsinas;
   }

   public List<InterfaceCampanha> getListaInterfaceCampanha()
   {
      return listaInterfaceCampanha;
   }

   public void setListaInterfaceCampanha(List<InterfaceCampanha> listaInterfaceCampanha)
   {
      this.listaInterfaceCampanha = listaInterfaceCampanha;
   }

   public InterfaceDadosEdicao getInterfaceDadosEdicao()
   {
      return interfaceDadosEdicao;
   }

   public void setInterfaceDadosEdicao(InterfaceDadosEdicao interfaceDadosEdicao)
   {
      this.interfaceDadosEdicao = interfaceDadosEdicao;
   }

   /**
    * @return o modo de operação atual do sistema
    */
   public ModoDeOperacaoEnum verificaModoDeOperacao()
   {
      throw new UnsupportedOperationException();
   }

   /**
    *
    * * Solicita a edição de dados de um grupo de balizas
    *
    * @param listaDeBalizasEditadas a lista de balizas editadas
    */
   public void editaDadosDeBalizas() throws OperacaoCanceladaPeloUsuarioException, ErroSistemicoException
   {
      interfaceDadosEdicao.setControladorDSP(this);
      interfaceDadosEdicao.solicitaDadosEdicaoBalizas(this.listaDeBalizas);
   }

   public ControladorAtualizacaoEdicao getControladorAtualizacaoEdicao()
   {
      return controladorAtualizacaoEdicao;
   }

   public void setControladorAtualizacaoEdicao(ControladorAtualizacaoEdicao controladorAtualizacaoEdicao)
   {
      this.controladorAtualizacaoEdicao = controladorAtualizacaoEdicao;
   }

  
   
   /**
    * Solicita a edição da campanha da usina selecionada
    * @param campanhaEditada a Campanha a ser editada
    */
   public void editaCampanhaProducao(InterfaceUsina interfaceUsina, List<TipoProduto> listaTiposProduto, List<TipoItemDeControle> listaTiposItemDeControle,ACAO acao,Campanha campanha) throws OperacaoCanceladaPeloUsuarioException
   {
      interfaceDadosEdicao.setControladorDSP(this);
      interfaceDadosEdicao.editarCampanha(interfaceUsina, listaTiposProduto, listaTiposItemDeControle,acao,campanha);
   }

   public void verificaListaBaliza(boolean consideraLimitesPilha) throws BalizasSelecionadasException
   {
      interfaceDadosEdicao.setControladorDSP(this);

      // ordena a lista de balizas inicial e final selecionada
      Collections.sort(this.listaDeBalizas, new ComparadorInterfaceBaliza());

      // valida a selecao de balizas somente se foram selecionadas mais de uma para edicao
      if (listaDeBalizas.size() > 1)
      {
         // verifica se foram selecionadas as balizas 2 balizas apenas. A baliza inicial e a baliza final
         if ((listaDeBalizas.size() % 2) == 0 && listaDeBalizas.size() == 2)
         {
            InterfaceBaliza interfaceBalizaInicial = new LinkedList<InterfaceBaliza>(getListaDeBalizas()).getFirst();
            InterfaceBaliza interfaceBalizaFinal = new LinkedList<InterfaceBaliza>(getListaDeBalizas()).getLast();
            
            listaDeBalizas = new ArrayList<InterfaceBaliza>();
            
            if (consideraLimitesPilha)
            {
            	interfaceBalizaInicial = retornaPrimeiraBalizaPilha(interfaceBalizaInicial);
            	interfaceBalizaFinal = retornaUltimaBalizaPilha(interfaceBalizaFinal);
            }
            
            // caso as balizas selecionadas não forem do mesmo pátio
            if (!interfaceBalizaInicial.getBalizaVisualizada().getPatio().getNumero().equals(interfaceBalizaFinal.getBalizaVisualizada().getPatio().getNumero()))
            {
               throw new BalizasSelecionadasException(PropertiesUtil.getMessage("mensagem.balizas.mesmo.patio"));
            }

            for (InterfacePatio interfacePatio : this.getInterfaceDSP().getListaDePatios())
            {
               if (interfacePatio.getPatioVisualizado().getNumero().equals(interfaceBalizaInicial.getBalizaVisualizada().getPatio().getNumero()))
               {
                  // seleciona as balizas entre a inicial e final
                  for (InterfaceBaliza interfaceBaliza : interfacePatio.getListaDeBalizas())
                  {
                     // adiciona a baliza na lista somente se o numero for maior que o numero da baliza inicial e
                     // menor que o numero da baliza final
                     if (interfaceBaliza.getBalizaVisualizada().getNumero() >= interfaceBalizaInicial.getBalizaVisualizada().getNumero() && interfaceBaliza.getBalizaVisualizada().getNumero() <= interfaceBalizaFinal.getBalizaVisualizada().getNumero())
                     {
                        // adicionando a baliza na lista
                        listaDeBalizas.add(interfaceBaliza);
                     }
                  }
               }
            }
         }
         else
         {
            throw new BalizasSelecionadasException(PropertiesUtil.getMessage("mensagem.balizas.patio.incompativel"));
         }
      }
   }
   
   // retorna a interfacebaliza da primeira baliza da pilha que contem a baliza passada como parametro
   private InterfaceBaliza retornaPrimeiraBalizaPilha(InterfaceBaliza interfaceBalizaInicial)
   {
	   Date dataAtual = interfaceDSP.getInterfaceInicial().getSituacaoPatioExibida().getDtInicio();
       for (InterfacePatio interfacePatio : this.getInterfaceDSP().getListaDePatios())
       {
          if (interfacePatio.getPatioVisualizado().getNumero().equals(interfaceBalizaInicial.getBalizaVisualizada().getPatio().getNumero()))
          {
        	  Pilha pilhaBalizaInicial = interfaceBalizaInicial.getBalizaVisualizada().retornaStatusHorario(dataAtual);
              for (InterfaceBaliza interfaceBaliza : interfacePatio.getListaDeBalizas())
              {
            	  Pilha pilhaAtual = interfaceBaliza.getBalizaVisualizada().retornaStatusHorario(dataAtual);
            	  if (pilhaAtual != null && pilhaBalizaInicial != null && pilhaBalizaInicial.equals(pilhaAtual))
            		  return interfaceBaliza;
              }
          }
       }
       
       return null;
   }
   
   // retorna a interfacebaliza da ultima baliza da pilha que contem a baliza passada como parametro
   private InterfaceBaliza retornaUltimaBalizaPilha(InterfaceBaliza interfaceBalizaInicial)
   {
	   Date dataAtual = interfaceDSP.getInterfaceInicial().getSituacaoPatioExibida().getDtInicio();
       for (InterfacePatio interfacePatio : this.getInterfaceDSP().getListaDePatios())
       {
          if (interfacePatio.getPatioVisualizado().getNumero().equals(interfaceBalizaInicial.getBalizaVisualizada().getPatio().getNumero()))
          {
        	  Pilha pilhaBalizaInicial = interfaceBalizaInicial.getBalizaVisualizada().retornaStatusHorario(dataAtual);
        	  int sizeLista = interfacePatio.getListaDeBalizas().size();
        	  for (int i = sizeLista-1; i >= 0; i--)
        	  {
        		  InterfaceBaliza interfaceBaliza = interfacePatio.getListaDeBalizas().get(i);
        		  Pilha pilhaAtual = interfaceBaliza.getBalizaVisualizada().retornaStatusHorario(dataAtual);
            	  if (pilhaAtual != null && pilhaBalizaInicial != null && pilhaBalizaInicial.equals(pilhaAtual))
            		  return interfaceBaliza;
              }
          }
       }
       
       return null;
   }
   
   /**
    * Verifica se existe alguma maquina operando sobre o grupo de balizas selecionados para uma edicao
    * @return
    */
   public boolean verificaMaquinaOperandoGrupoBaliza(){
	   boolean result = false;
	   if(this.getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida().getPlanta().getListaMaquinasDoPatio(this.getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida().getDtInicio()) != null ){
		   InterfaceBaliza iBalizaInicial = new LinkedList<InterfaceBaliza>(getListaDeBalizas()).getFirst();
           InterfaceBaliza iBalizaFinal = new LinkedList<InterfaceBaliza>(getListaDeBalizas()).getLast();
           for(MaquinaDoPatio maquina : this.getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida().getPlanta().getListaMaquinasDoPatio(this.getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida().getDtInicio())){
        	   if(maquina.getEstado().equals(EstadoMaquinaEnum.OPERACAO)
        			   && maquina.getPosicao().getPatio().getNumero().equals(iBalizaInicial.getBalizaVisualizada().getPatio().getNumero())
        			   && (maquina.getPosicao().getNumero() >= iBalizaInicial.getBalizaVisualizada().getNumero()
        					   && maquina.getPosicao().getNumero() <= iBalizaFinal.getBalizaVisualizada().getNumero()))
        	   {
        		   result = true;
        		   break;
        	   }
           }
	   }
	   
	   return result;
   }

   public Boolean getSelecionaMenuBaliza() {
		return selecionaMenuBaliza;
	}

	public void setSelecionaMenuBaliza(Boolean selecionaMenuBaliza) {
		this.selecionaMenuBaliza = selecionaMenuBaliza;
	}
	
   public List<MaquinaDoPatio> getListaDeMaquinasSelecionadas()
   {
      List<MaquinaDoPatio> maquinasSelecionadas = new ArrayList<MaquinaDoPatio>();
      // percorre todas as correias do patio e suas respectivas maquinas
      for (InterfaceCorreia interfaceCorreia : interfaceDSP.getListaCorreias())
      {
         for (InterfaceMaquinaDoPatio interfaceMaquinaDoPatio : interfaceCorreia.getListaDeMaquinas())
         {
            if (interfaceMaquinaDoPatio.isSelecionada())
            {
               maquinasSelecionadas.add(interfaceMaquinaDoPatio.getMaquinaVisualizada());
            }
         }
      }
      return (maquinasSelecionadas.isEmpty()) ? null : maquinasSelecionadas;
   }

   /**
    * Solicita a edição de dados da máquina selecionada através do modo de edição do sistema
    * @param maquinaDoPatioVisualizada a maquina a ser editada
    * @param interfaceMaquinaDoPatio a interface gráfica representando a máquina a ser editada
    * @throws ValidacaoCampoException 
    * @throws OperacaoCanceladaPeloUsuarioException 
    */
   public void editaDadosDeMaquina(MaquinaDoPatio maquinaDoPatioVisualizada, InterfaceMaquinaDoPatio interfaceMaquinaDoPatio) throws OperacaoCanceladaPeloUsuarioException, ValidacaoCampoException
   {
      interfaceDadosEdicao.setControladorDSP(this);
      interfaceDadosEdicao.solicitaDadosEdicaoMaquina(maquinaDoPatioVisualizada, interfaceMaquinaDoPatio);
   }

   /**
    * Atualizacao de empilhamento com maquina
    * @param maquinaDoPatioVisualizada a maquina que esta empilhando
    * @throws ValidacaoCampoException
    * @throws OperacaoCanceladaPeloUsuarioException
    */
   @Override
   public void atualizaEmpilhamentoMaquina(MaquinaDoPatio maquinaDoPatioVisualizada, InterfaceMaquinaDoPatio interfaceMaquinaDoPatio, 
		   boolean finalizaAtualizao) throws OperacaoCanceladaPeloUsuarioException, ErroSistemicoException
   {
      interfaceDadosEdicao.setControladorDSP(this);
      interfaceDadosEdicao.solicitaAtualizacaoEmpilhamento(maquinaDoPatioVisualizada, interfaceMaquinaDoPatio, finalizaAtualizao);
   }

   /**
    * Atualizacao de recuperacao com maquina
    * @param maquinaDoPatioVisualizada a maquina que esta recuperando
    * @throws ValidacaoCampoException
    * @throws OperacaoCanceladaPeloUsuarioException
    */
   @Override
   public void atualizaRecuperacaoMaquina(MaquinaDoPatio maquinaDoPatioVisualizada, InterfaceMaquinaDoPatio interfaceMaquinaDoPatio) throws OperacaoCanceladaPeloUsuarioException, ErroSistemicoException
   {
      interfaceDadosEdicao.setControladorDSP(this);
      interfaceDadosEdicao.solicitaAtualizacaoRecuperacao(maquinaDoPatioVisualizada, interfaceMaquinaDoPatio);
   }

   /**
    * @return the listaDeMaquinas
    */
   public List<InterfaceMaquinaDoPatio> getListaDeMaquinas()
   {
      return listaDeMaquinas;
   }

   /**
    * @param listaDeMaquinas the listaDeMaquinas to set
    */
   public void setListaDeMaquinas(List<InterfaceMaquinaDoPatio> listaDeMaquinas)
   {
      this.listaDeMaquinas = listaDeMaquinas;
   }

   @Override
   public List<MaquinaDoPatio> getListaMaquinasSelecionadas()
   {
      List<MaquinaDoPatio> maquinasSelecionadas = new ArrayList<MaquinaDoPatio>();
      // percorre todas as correias do patio e suas respectivas recuperadoras
      for (InterfaceCorreia interfaceCorreia : interfaceDSP.getListaCorreias())
      {
         for (InterfaceMaquinaDoPatio interfaceMaquina : interfaceCorreia.getListaDeMaquinas())
         {
            if (interfaceMaquina != null && interfaceMaquina.isSelecionada())
            {
               maquinasSelecionadas.add(interfaceMaquina.getMaquinaVisualizada());
            }
         }
      }
      // percorre todos os patios e suas respectivas pas-carregadeiras
      for (InterfacePatio interfacePatio : interfaceDSP.getListaDePatios())
      {
         for (InterfaceMaquinaDoPatio interfaceMaquinaDoPatio : interfacePatio.getListaDeMaquinas())
         {
            if (interfaceMaquinaDoPatio.isSelecionada())
            {
               maquinasSelecionadas.add(interfaceMaquinaDoPatio.getMaquinaDoPatioVisualizada());
            }
         }
      }
      return (maquinasSelecionadas.isEmpty()) ? null : maquinasSelecionadas;
   }

   @Override
   public void verificarSelecaoDeBalizasParaEdicao() throws ModoDeEdicaoException
   {
      List<String> paramMensagens = new ArrayList<String>();

      // Verifica se foi selecionada pelo menos 1 baliza para edição
      if (this.getListaDeBalizas().isEmpty())
      {
         paramMensagens.add("baliza(s)");
         paramMensagens.add("edição");
         throw new ModoDeEdicaoException("exception.validacao.selecao.objetos.operacao", paramMensagens);
      }
      else
      {
         this.getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida().setStatusEdicao(Boolean.TRUE);
      }
   }

   @Override
   public void verificarModoDeEdicao() throws ModoDeEdicaoException
   {
      List<String> paramMensagens = new ArrayList<String>();
      /*if (this.getInterfaceDSP().getInterfaceInicial().getControladorInterfaceFilaNavios().getInterfaceFilaDeNavios().getFilaDeNaviosVisualizada() != null && this.getInterfaceDSP().getInterfaceInicial().getControladorInterfaceFilaNavios().getInterfaceFilaDeNavios().getFilaDeNaviosVisualizada().isStatusEdicao())
      {
         paramMensagens.add("Situação de pátio");
         paramMensagens.add("Fila de navios");
         throw new ModoDeEdicaoException("exception.ModoDeEdicao.Nao.Salvo", paramMensagens);
      }
      else
      {*/
         this.getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida().setStatusEdicao(Boolean.TRUE);
      //}
   }

   @Override
   public List<Usina> getUsinasSelecionadas()
   {
      List<Usina> usinasSelecionadas = new ArrayList<Usina>();

      for (InterfaceUsina interfaceUsinaAnalisada : getListaDeUsinas())
      {
         if (interfaceUsinaAnalisada.isSelecionada())
         {
            usinasSelecionadas.add(interfaceUsinaAnalisada.getUsinaVisualizada());

         }
      }
      return usinasSelecionadas;
   }

   @Override
   public TipoProduto getTipoDeProdutoSelecionado()
   {
      return interfaceInicial.getTipoDeProdutoSelecionado();
   }

   /**
    * 
    * @param coordenadasDeInsercaoDaAnotacao
    * @param interfacePatio
    */
   public void inserirAnotacao(Dimension coordenadasDeInsercaoDaAnotacao, InterfacePatio interfacePatio)
   {
      //TODO SA9675
      /*
       * 1 - Apresentar uma janela para inserção das informações contidas na anotação - fazer no NetBeans (lembre-se que o cara pode cancelar a operação) 
       * 2 - criar e chamar um método neste controlador para verificar se pode criar a anotaçÃo
       * 3 - Cria a entidade anotação
       * 4 - Inseri-la na lista de anotações dos pátios
       * 5 - criar a InterfaceAnotacao
       * 6 - Inseri-la na lista de InterfaceAnotacao da InterfacePatio correspondente
       * 7 - Atualiza a interface
       */

      throw new UnsupportedOperationException();
   }

   /**
    * Atualizacao de recuperacao com usina
    * @param usinaVisualizada - a usina que esta recuperando
    * @throws ValidacaoCampoException
    * @throws OperacaoCanceladaPeloUsuarioException
    */
   @Override
   public void atualizaRecuperacaoUsina(Usina usinaVisualizada, InterfaceUsina interfaceUsina) throws OperacaoCanceladaPeloUsuarioException, ErroSistemicoException
   {
      interfaceDadosEdicao.setControladorDSP(this);
      interfaceDadosEdicao.solicitaAtualizacaoRecuperacaoUsina(usinaVisualizada, interfaceUsina);
   }

   /**
    * Atualizacao de recuperacao com filtragem
    * @param usinaVisualizada - a usina que esta recuperando
    * @throws ValidacaoCampoException
    * @throws OperacaoCanceladaPeloUsuarioException
    */
   @Override
   public void atualizaRecuperacaoFiltragem(Filtragem filtragemSelecionada,Usina usinaVisualizada,InterfaceFiltragem interfaceFiltragem) throws OperacaoCanceladaPeloUsuarioException, ErroSistemicoException
   {
      interfaceDadosEdicao.setControladorDSP(this);
      interfaceDadosEdicao.solicitaAtualizacaoRecuperacaoFiltragem(filtragemSelecionada, usinaVisualizada, null,interfaceFiltragem);
   }

   
   
   /**
    *  Verifica se a permissao do usuario, ao realizar um blend de produtos diferentes, necessita gerar um log no arquivo
    * @param listaTipoProduto
    * @param carga
    */
   public void verificaGerarLogBlend(List<String> listaTipoProduto, Carga carga)
   {
       for(Permission permission : InterfaceInicial.getUsuarioLogado().getProfile().getPermissions()){
           if(permission.getName().trim().equalsIgnoreCase(PropertiesUtil.buscarPropriedade("nivel.acesso.gerar.log.para.blend.produto.diferente").trim())){
               if(listaTipoProduto.size() > 1)
               {//se a lista possuir apenas um elemento, significa que não foi gerado blend de produtos diferentes
                   geraLogDoBlend(listaTipoProduto, carga);
               }
           }
       }
   }

   private void geraLogDoBlend(List<String> listaTipoProduto, Carga carga)
   {
       StringBuilder mensagem = new StringBuilder();
       String espacoVirgula = " , ";
       String espacoBranco = " ";
       mensagem.append(PropertiesUtil.getMessage("log.mensagem.usuario") );
       mensagem.append(espacoBranco);
       mensagem.append(InterfaceInicial.getUsuarioLogado().getName());
       mensagem.append(espacoBranco);
       mensagem.append(PropertiesUtil.getMessage("log.mensagem.blendou.produto"));
       mensagem.append(espacoBranco);
       for(String tipoProduto : listaTipoProduto){
           mensagem.append(tipoProduto.toString());
           mensagem.append(espacoVirgula);
       }
       mensagem.append(PropertiesUtil.getMessage("log.mensagem.para.carga.tipo.produto"));
       mensagem.append(espacoBranco);
       mensagem.append(carga.getIdentificadorCarga());
       mensagem.append(espacoBranco);
       if (carga.getProduto() != null) {
           mensagem.append(PropertiesUtil.getMessage("log.mensagem.que.possui.tipo.produto"));
           mensagem.append(espacoBranco);
           mensagem.append(carga.getProduto().getTipoProduto().toString());
           mensagem.append(espacoBranco);
       }
       mensagem.append(PropertiesUtil.getMessage("log.mensagem.do.navio"));
       mensagem.append(espacoBranco);
       mensagem.append(carga.getNavio(carga.getDtInicio()).getNomeNavio());

       //grava o log da mensagem montada acima
       logger.info(mensagem);
   }
   
    

   /**
    * verifica se uma data de uma atividade não será menor ou igual que a data da ultima situação de patio do Plano do usuario
    * @param data
    * @return <b>true</b> e a data for valida, ou seja, maior que a data da ultima situação <br> <b>false</b> caso contrario
    */
   public boolean validaDataMenorOuIgualUltimaSituacaoPatio(Date data)
   {
       boolean ehValido = Boolean.TRUE;
       if(data.before(this.getInterfaceInicial().getPlanejamento().getControladorDePlano().obterSituacaoPatioFinal().getDtInicio())
             || data.equals(this.getInterfaceInicial().getPlanejamento().getControladorDePlano().obterSituacaoPatioFinal().getDtInicio())){
           ehValido = Boolean.FALSE;
       }

       return ehValido;
   }
   /**
    * verifica se uma data de uma atividade não será menor que a data da ultima situação de patio do Plano do usuario
    * @param data
    * @return
    */
   public boolean validaDAtaMenorUltimaSituacaoPatio(Date data){
       boolean ehValido = Boolean.TRUE;
       if(data.before(this.getInterfaceInicial().getPlanejamento().getControladorDePlano().obterSituacaoPatioFinal().getDtInicio()) ){
           ehValido = Boolean.FALSE;
       }

       return ehValido;
   }

   /**
    *  Verifica se a permissao do usuario, ao realizar um blend de produtos diferentes durante Atualizacao, necessita gerar um log no arquivo
    * @param listaTipoProduto
    * @param carga
    */
   public void verificaEGerarLogBlendNaAtualizacao(List<String> listaTipoProduto, Carga carga)
   {
        for (Permission permission : InterfaceInicial.getUsuarioLogado().getProfile().getPermissions()) {
            if (permission.getName().trim().equalsIgnoreCase(PropertiesUtil.buscarPropriedade("nivel.acesso.gerar.log.para.blend.produto.diferente").trim())) {
                //se a lista possuir apenas um elemento, significa que não foi gerado blend de produtos diferentes
                geraLogDoBlend(listaTipoProduto, carga);
            }
        }
    }

   /**
    * Metodo que verifica se o usuario logado no sistema tem permissГЈo para excluir uma anotacao
    * @return
    */
   public boolean verificaPermissaoParaExcluirAnotacao(){
      boolean permissao = false;
      for (Permission permission : InterfaceInicial.getUsuarioLogado().getProfile().getPermissions()) {
            if ((permission.getName().trim().equalsIgnoreCase(PropertiesUtil.buscarPropriedade("nivel.acesso.apagar.anotacao").trim() ))) {
                permissao = true;
                break;
            }
        }
      return permissao;
   }

   /**
    *  Edita uma anotaГ§ГЈo de um pГЎtio
    * @param interfaceAnotacao
    */
   public void editarAnotacao(InterfaceAnotacao interfaceAnotacao) throws OperacaoCanceladaPeloUsuarioException
   {
      //TODO SA9675
      /*
       * 1 - Apresentar uma janela para ediГ§ГЈo das informaГ§Гµes contidas nesta anotaГ§ГЈo(lembre-se que o cara pode cancelar a operaГ§ГЈo)
       * 2 - criar e chamar um mГ©todo neste controlador para verificar se pode editar a anotaГ§Гѓo
       * 3 - Atualiza a interface
       */

       // 1)...
       interfaceDadosEdicao.setControladorDSP(this);
       interfaceDadosEdicao.editarInserirAnotacao(interfaceAnotacao, 0, 0);

   }
   /**
    * Exclui uma anotaГ§ГЈo de um pГЎtio
    * @param interfaceAnotacao
    */
   public void excluirAnotacao(InterfaceAnotacao interfaceAnotacao)
   {
    //TODO SA9675 BRUNO
      /*
       * 1 - criar e chamar um mГ©todo neste controlador para verificar se pode excluir a anotaГ§Гѓo
       * 2 - remover InterfaceAnotacao da InterfacePatio correspondente
       * 3 - remover a anotaГ§ГЈo da lista de anotaГ§Гµes do pГЎtio
       * 4 - Atualiza a interface
       */
       // (1)
      if(verificaPermissaoParaExcluirAnotacao()){
          //(2) (3)
          interfaceDadosEdicao.setControladorDSP(this);
          //(4)
          interfaceDadosEdicao.removerAnotacao(interfaceAnotacao);
    
      }
      
   }

    /**
    *
    * @param coordenadasDeInsercaoDaAnotacao
    * @param interfacePatio
    */
   public void inserirAnotacao(int eixoX, int eixoY,InterfaceAnotacao interfaceAnotacao) throws OperacaoCanceladaPeloUsuarioException
   {
      //TODO SA9675
      /*
       * 1 - Apresentar uma janela para inserГ§ГЈo das informaГ§Гµes contidas na anotaГ§ГЈo - fazer no NetBeans (lembre-se que o cara pode cancelar a operaГ§ГЈo)
       * 2 - criar e chamar um mГ©todo neste controlador para verificar se pode criar a anotaГ§ГЈo
       * 3 - Cria a entidade anotaГ§ГЈo
       * 4 - Inseri-la na lista de anotaГ§Гµes dos pГЎtios
       * 5 - criar a InterfaceAnotacao
       * 6 - Inseri-la na lista de InterfaceAnotacao da InterfacePatio correspondente
       * 7 - Atualiza a interface
       */

       // (1)
       interfaceDadosEdicao.setControladorDSP(this);
       interfaceDadosEdicao.editarInserirAnotacao(interfaceAnotacao,eixoX, eixoY);
   }

   @Override
   public void retornarPelletFeed(InterfaceMaquinaDoPatio interfaceMaquinaDoPatio) throws OperacaoCanceladaPeloUsuarioException, ErroSistemicoException, PilhaPelletFeedNaoEncontradaException
   {
      interfaceDadosEdicao.setControladorDSP(this);
      interfaceDadosEdicao.solicitarRetornoPelletFeed(interfaceMaquinaDoPatio);
   }

    @Override
    public void selecionaBaliza(List<InterfaceBaliza> listaBalizaSelecionada) throws BlendagemInvalidaException, CampanhaIncompativelException, ProdutoIncompativelException, ExcessoDeMaterialParaEmbarqueException {
        interfaceInicial.verificaBlendagemBaliza(listaBalizaSelecionada);
    }

    @Override
    public void deselecionaBaliza(List<InterfaceBaliza> listtaBalizaDeselecionada) throws BlendagemInvalidaException, CampanhaIncompativelException, ProdutoIncompativelException {
        interfaceInicial.verificaRemocaoBlendagemBaliza(listtaBalizaDeselecionada);
    }

   /**
    * Atualizacao de recuperacao com usina
    * @param usinaVisualizada - a usina que esta recuperando
    * @throws ValidacaoCampoException
    * @throws OperacaoCanceladaPeloUsuarioException
    */
   @Override
   public void atualizaPilhaEmergencia(Usina usinaVisualizada, InterfaceUsina interfaceUsina) throws OperacaoCanceladaPeloUsuarioException, ErroSistemicoException
   {
      interfaceDadosEdicao.setControladorDSP(this);
      interfaceDadosEdicao.solicitaAtualizacaoPilhaEmergencia(usinaVisualizada, interfaceUsina);
   }

   
   @Override
   public void movimentarPilhaEmergencia(MaquinaDoPatio maquinaSelecionada, InterfaceMaquinaDoPatio interfaceMaquina)  throws OperacaoCanceladaPeloUsuarioException, ErroSistemicoException
   {
      interfaceDadosEdicao.setControladorDSP(this);
      interfaceDadosEdicao.solicitaMovimentacaoPilhaEmergencia(maquinaSelecionada, interfaceMaquina);
   }
   
   @Override
   public void movimentarPilhaPellet(MaquinaDoPatio maquinaSelecionada, InterfaceMaquinaDoPatio interfaceMaquina, TipoDeProdutoEnum tipoProduto)  throws OperacaoCanceladaPeloUsuarioException, ErroSistemicoException
   {
      interfaceDadosEdicao.setControladorDSP(this);
      interfaceDadosEdicao.solicitaMovimentacaoPilhaPellet(maquinaSelecionada, interfaceMaquina, tipoProduto);
   }

   @Override
   public void movimentarPilhaPSM(MaquinaDoPatio maquinaSelecionada, InterfaceMaquinaDoPatio interfaceMaquina, TipoDeProdutoEnum tipoProduto)  throws OperacaoCanceladaPeloUsuarioException, ErroSistemicoException
   {
      interfaceDadosEdicao.setControladorDSP(this);
      interfaceDadosEdicao.solicitaMovimentacaoPilhaPSM(maquinaSelecionada, interfaceMaquina, tipoProduto);
   }

   
   @Override
   public void unificarPilhas() throws OperacaoCanceladaPeloUsuarioException, ErroSistemicoException
   {
      interfaceDadosEdicao.setControladorDSP(this);
      interfaceDadosEdicao.solicitaUnificarPilhas(this.listaDeBalizas);
   }

   @Override
   public void interditaPier(InterfacePier interfacePier) throws OperacaoCanceladaPeloUsuarioException, ErroSistemicoException
   {
      interfaceDadosEdicao.setControladorDSP(this);
      interfaceDadosEdicao.solicitarInterdicaoPier(interfacePier);
   }

   @Override
   public void interditarBaliza(InterfacePatio interfacePatio) throws OperacaoCanceladaPeloUsuarioException, ErroSistemicoException
   {
      interfaceDadosEdicao.setControladorDSP(this);
      interfaceDadosEdicao.solicitarInterdicaoBaliza(interfacePatio);
   }
   
   /**
    * Antes de integrar com os sistemas externos verifica se o sistema esta consolidado, se este não estiver retirna TRUE,
    * caso ja estaja consolidado retorna FALSE
    * @return
    */
    public boolean verificaNecessidadeConsolidarParaIntegrar() {
        try {
            getInterfaceInicial().getPlanejamento().verificarPossibilidadeDeConsolidacao();
            return true;
        } catch (ConsolidacaoNaoNecessariaException ex) {
            return false;
        }
    }

    public boolean verificaNecessidadeIntegracaoAntesRecuperacao() {
        return getInterfaceInicial().getInterfaceComandos().verificaNecessidadeDeIntegrarComMES();
    }
    
    /**
     * Atualiza o estado da maquina de acordo com sua lista de manutencoes, verificando se uma manutencao ja comecou ou acabou
     * @param dataAtividade
     * @param maquina
     */
    public void validaDataAtividadePeriodoManutencao(Date dataAtividade, MaquinaDoPatio maquina)
    {
    	/*if(maquina.getListaManutencao() != null && !maquina.getListaManutencao().isEmpty()){
    		for(Manutencao manutencao : maquina.getListaManutencao()){
    			//valida se a maquina se encontra num intervalo de manutencao
    			if((dataAtividade.after(manutencao.getDataInicial()) || dataAtividade.equals(manutencao.getDataInicial()) ) 
    					&& (dataAtividade.before(manutencao.getDataFinal()) || dataAtividade.equals(manutencao.getDataFinal())) ){
    				maquina.setEstado(EstadoMaquinaEnum.MANUTENCAO);
    				//se a maquina não esta em intervalo de manutencao e seu estado dor de manutencao significa que a manutencao desta maquina ja acabou
    			}else if(maquina.getEstado().equals(EstadoMaquinaEnum.MANUTENCAO)){
    				maquina.setEstado(EstadoMaquinaEnum.OCIOSA);
    			}

    		}
    	}*/
    }
    /**
     * Atualiza o estado das maquinas para quando entram ou saem da manutenção
     * @param data
     * @param listaDeMaquinas
     */
    public void atualizaEstadoMaquinas(Date data, List<MaquinaDoPatio> listaDeMaquinas){
    	for(MaquinaDoPatio maquina : listaDeMaquinas){
    		validaDataAtividadePeriodoManutencao(data, maquina);
    	}
    }
    
    /**
     * Atualiza o estado das correias para quando entram ou saem da manutenção
     * @param data
     * @param listaDeCorreias
     */
    public void atualizaEstadoCorreias(Date data, List<Correia> listaDeCorreias){
    	for(Correia correia : listaDeCorreias){
    		validaDataAtividadeManutencaoCorreia(data, correia);
    	}
    }
    
    /**
     * Atualiza o estado da correia de acordo com sua lista de manutencoes, verificando se uma manutencao ja comecou ou acabou
     * @param data
     * @param correia
     */
    public void validaDataAtividadeManutencaoCorreia(Date data, Correia correia){
    	if(correia.getMetaCorreia().getListaManutencao() != null && !correia.getMetaCorreia().getListaManutencao().isEmpty()){
    		for(Manutencao manutencao : correia.getMetaCorreia().getListaManutencao()){
    			//valida se a correia se encontra num intervalo de manutencao
    			if((data.after(manutencao.getDataInicial()) || data.equals(manutencao.getDataInicial()) ) 
    					&& (data.before(manutencao.getDataFinal()) || data.equals(manutencao.getDataFinal())) ){
    				correia.setEstado(EstadoMaquinaEnum.MANUTENCAO);
    				//se a correia não esta em intervalo de manutencao e seu estado dor de manutencao significa que a manutencao desta correia ja acabou
    			}else if(correia.getEstado().equals(EstadoMaquinaEnum.MANUTENCAO)){
    				correia.setEstado(EstadoMaquinaEnum.OCIOSA);
    			}
    		}
    	}
    }

}
