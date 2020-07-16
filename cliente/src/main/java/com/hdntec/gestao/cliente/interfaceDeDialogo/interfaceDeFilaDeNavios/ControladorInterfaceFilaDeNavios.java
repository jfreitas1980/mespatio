package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.hdntec.gestao.cliente.interfaceDeDialogo.ControladorInterfaceInicial;
import com.hdntec.gestao.cliente.interfaceDeDialogo.IControladorInterfaceInicial;
import com.hdntec.gestao.cliente.interfaceDeDialogo.ModoDeOperacaoEnum;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.InterfaceDadosEdicao;
import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.cliente.planoDeEmpilhamentoERecuperacaoCliente.atualizacao.edicao.ControladorAtualizacaoEdicao;
import com.hdntec.gestao.domain.navios.entity.status.Carga;
import com.hdntec.gestao.domain.navios.entity.status.Cliente;
import com.hdntec.gestao.domain.navios.entity.status.FilaDeNavios;
import com.hdntec.gestao.domain.navios.entity.status.Navio;
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.enums.TipoAtividadeEnum;
import com.hdntec.gestao.domain.vo.atividades.AtividadeAtracarDesAtracarNavioVO;
import com.hdntec.gestao.exceptions.AtividadeException;
import com.hdntec.gestao.exceptions.BlendagemInvalidaException;
import com.hdntec.gestao.exceptions.CampanhaIncompativelException;
import com.hdntec.gestao.exceptions.CargaSelecionadaException;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.ExcessoDeMaterialParaEmbarqueException;
import com.hdntec.gestao.exceptions.ModoDeEdicaoException;
import com.hdntec.gestao.exceptions.ObjetoPesquisadoNaoEncontradoException;
import com.hdntec.gestao.exceptions.OperacaoCanceladaPeloUsuarioException;
import com.hdntec.gestao.exceptions.ProdutoIncompativelException;
import com.hdntec.gestao.integracao.controladores.ControladorModelo;
import com.hdntec.gestao.integracao.controladores.IControladorModelo;
import com.hdntec.gestao.services.controladores.impl.ControladorFilaDeNavios;


/**
 * Controlador das operações do subsistema de interface gráfica de fila de navios.
 * 
 * @author andre
 * 
 */
public class ControladorInterfaceFilaDeNavios implements IControladorInterfaceFilaDeNavios
{    
	public ControladorInterfaceFilaDeNavios()
   {
      this.controladorAtualizacaoEdicao = new ControladorAtualizacaoEdicao();
   }

   private ControladorAtualizacaoEdicao controladorAtualizacaoEdicao;

   /** a lista de interfaces gráficas de carregadoras de navio */
   private List<InterfaceCarregadoraDeNavios> listaInterfacesCarregadoraDeNavio;

   /** a interface gráfica de fila de navios */
   private InterfaceFilaDeNavios interfaceFilaDeNavios;

   /** a lista de interfaces gráficas de carga */
   private List<InterfaceCarga> listaInterfacesCarga;

   /** a lista de interfaces gráficas de navios */
   private List<InterfaceNavio> listaInterfacesNavios;

   /** a lista de interfaces gráficas de berços */
   private List<InterfaceBerco> listaInterfacesBerco;

   /** a lista de interfaces gráficas de píeres */
   private List<InterfacePier> listaInterfacesPier;

   /** acesso às operações do subsistema de interface principal */
   private IControladorInterfaceInicial interfaceInicial;

   /** Interface utilizada para o modo de edição manual das informações dos itens do DSP*/
   private InterfaceDadosEdicao interfaceDadosEdicao;

   /** lista das mensagens exibidas pela interface */
   private List<String> listaMensagens;

   @Override
   public void selecionaCarga(Carga cargaSelecionada) throws BlendagemInvalidaException, CampanhaIncompativelException, ProdutoIncompativelException, CargaSelecionadaException, ExcessoDeMaterialParaEmbarqueException
   {
      interfaceInicial.verificaBlendagemCarga(cargaSelecionada);
   }

   @Override
   public void deselecionaCarga(Carga cargaSelecionada) throws BlendagemInvalidaException, ProdutoIncompativelException
   {
      interfaceInicial.verificaRemocaoBlendagemCarga(cargaSelecionada);
   }

   @Override
   public void ativarMensagem(InterfaceMensagem interfaceMensagem)
   {
      interfaceInicial.ativarMensagem(interfaceMensagem);
   }

   @Override
   public ModoDeOperacaoEnum obterModoOperacao()
   {
      return interfaceInicial.obterModoOperacao();
   }

   public InterfaceCarga obterInterfaceDaCarga(Carga carga)
   {
      for (InterfacePier interfacePier : getInterfaceFilaDeNavios().getListaDePiers())
      {
         for (InterfaceBerco interfaceBerco : interfacePier.getListaDeBercos())
         {
            if (interfaceBerco.getNavioAtendido() != null)
            {
               for (InterfaceCarga interfaceCarga : interfaceBerco.getNavioAtendido().getListaDecarga())
               {
                  if (interfaceCarga.getCargaVisualizada().getIdCarga().equals(carga.getIdCarga()))
                  {
                     return interfaceCarga;
                  }
               }
            }
         }
      }

      return null;
   }

   public InterfaceFilaDeNavios getInterfaceFilaDeNavios()
   {
      return interfaceFilaDeNavios;
   }

   public void setInterfaceFilaDeNavios(InterfaceFilaDeNavios interfaceFilaDeNavios)
   {
      this.interfaceFilaDeNavios = interfaceFilaDeNavios;
   }

   public IControladorInterfaceInicial getInterfaceInicial()
   {
      return interfaceInicial;
   }

   public void setInterfaceInicial(IControladorInterfaceInicial interfaceInicial)
   {
      this.interfaceInicial = interfaceInicial;
   }

   public List<InterfaceBerco> getListaInterfacesBerco()
   {
      return listaInterfacesBerco;
   }

   public void setListaInterfacesBerco(List<InterfaceBerco> listaInterfacesBerco)
   {
      this.listaInterfacesBerco = listaInterfacesBerco;
   }

   public List<InterfaceCarga> getListaInterfacesCarga()
   {
      return listaInterfacesCarga;
   }

   public void setListaInterfacesCarga(List<InterfaceCarga> listaInterfacesCarga)
   {
      this.listaInterfacesCarga = listaInterfacesCarga;
   }

   public List<InterfaceCarregadoraDeNavios> getListaInterfacesCarregadoraDeNavio()
   {
      return listaInterfacesCarregadoraDeNavio;
   }

   public void setListaInterfacesCarregadoraDeNavio(List<InterfaceCarregadoraDeNavios> listaInterfacesCarregadoraDeNavio)
   {
      this.listaInterfacesCarregadoraDeNavio = listaInterfacesCarregadoraDeNavio;
   }

   public List<InterfaceNavio> getListaInterfacesNavios()
   {
      if (listaInterfacesNavios == null)
      {
         listaInterfacesNavios = new ArrayList<InterfaceNavio>();
      }
      return listaInterfacesNavios;
   }

   public void setListaInterfacesNavios(List<InterfaceNavio> listaInterfacesNavios)
   {
      this.listaInterfacesNavios = listaInterfacesNavios;
   }

   public List<InterfacePier> getListaInterfacesPier()
   {
      return listaInterfacesPier;
   }

   public void setListaInterfacesPier(List<InterfacePier> listaInterfacesPier)
   {
      this.listaInterfacesPier = listaInterfacesPier;
   }

   /**
    * solicita a edição dos dados do navio selecionado
    * @param navio  InterfaceNavio que esta sendo editado
    * @throws OperacaoCanceladaPeloUsuarioException
    */
   public void editarNavio(InterfaceNavio navio, InterfaceEditaNavioNaFilaDeNavios interfaceEditaNavioNaFilaDeNavios, Integer modoOperacao, String identificador) throws ErroSistemicoException
   {
      interfaceDadosEdicao.setControladorInterfaceFilaDeNavios(this);
      interfaceDadosEdicao.setControladorDSP(interfaceInicial.getInterfaceInicial().getInterfaceDSP().getControladorDSP());
      interfaceDadosEdicao.solicitaDadosEdicaoNavio(navio, interfaceEditaNavioNaFilaDeNavios, modoOperacao, identificador);
   }

   /**
    * solicita a edição dos dados do navio selecionado
    * @param interfaceNavio
    * @throws br.com.cflex.supervision.stockYard.cliente.exception.OperacaoCanceladaPeloUsuarioException
    */
   public void inserirNavioFilaDeNavios(InterfaceNavio interfaceNavio) throws OperacaoCanceladaPeloUsuarioException
   {
      interfaceDadosEdicao.setControladorInterfaceFilaDeNavios(this);
      interfaceDadosEdicao.setControladorDSP(interfaceInicial.getInterfaceInicial().getInterfaceDSP().getControladorDSP());
      interfaceDadosEdicao.solicitaInsercaoNavioNaFila(interfaceNavio);

   }

   /**
    * solicita a edição dos dados do navio selecionado
    * @param interfaceNavio
    * @throws br.com.cflex.supervision.stockYard.cliente.exception.OperacaoCanceladaPeloUsuarioException
 * @throws ErroSistemicoException 
    */
   public void atualizarNavioFilaDeNavios(InterfaceNavio interfaceNavio) throws OperacaoCanceladaPeloUsuarioException, ErroSistemicoException
   {
      interfaceDadosEdicao.setControladorInterfaceFilaDeNavios(this);
      interfaceDadosEdicao.setControladorDSP(interfaceInicial.getInterfaceInicial().getInterfaceDSP().getControladorDSP());
      interfaceDadosEdicao.solicitaAtualizacaoNavioNaFila(interfaceNavio);

   }

   
   
   /**
    * edita a Fila De Navios existente no plano visualizado
    * @param filaDeNaviosASerEditada a fila de Navios a ser editada
    */
   public void editarFilaDeNavios(InterfaceNavio interfaceNavio, Integer modoOperacao) throws OperacaoCanceladaPeloUsuarioException
   {
      interfaceDadosEdicao.setControladorInterfaceFilaDeNavios(this);
      interfaceDadosEdicao.setControladorDSP(interfaceInicial.getInterfaceInicial().getInterfaceDSP().getControladorDSP());
      interfaceDadosEdicao.solicitaDadosEdicaoFilaDeNavios(interfaceNavio, modoOperacao);
   }

   /**
    * edita a Fila De Navios existente no plano visualizado
    * @param filaDeNaviosASerEditada a fila de Navios a ser editada
    */
   public void excluirNavioFilaDeNavios(InterfaceNavio interfaceNavio) throws OperacaoCanceladaPeloUsuarioException
   {
      interfaceDadosEdicao.setControladorInterfaceFilaDeNavios(this);
      interfaceDadosEdicao.setControladorDSP(interfaceInicial.getInterfaceInicial().getInterfaceDSP().getControladorDSP());
      try
      {
         interfaceDadosEdicao.excluirNavioDaFila(interfaceNavio.getNavioVisualizado());
      } catch (ErroSistemicoException ex)
      {
         Logger.getLogger(ControladorInterfaceFilaDeNavios.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   public InterfaceDadosEdicao getInterfaceDadosEdicao()
   {
      return interfaceDadosEdicao;
   }

   public void setInterfaceDadosEdicao(InterfaceDadosEdicao interfaceDadosEdicao)
   {
      this.interfaceDadosEdicao = interfaceDadosEdicao;
   }

   public ControladorAtualizacaoEdicao getControladorAtualizacaoEdicao()
   {
      return controladorAtualizacaoEdicao;
   }

   @Override
   public void buscarFilaDeNaviosCadastrada() throws ObjetoPesquisadoNaoEncontradoException, ErroSistemicoException
   {
      listaMensagens = new ArrayList<String>();
      listaMensagens.add("fila de navios");
      //Darley Retirando a chamada remota
//      IControladorModelo controladorModelo = interfaceFilaDeNavios.getInterfaceInicial().lookUpModelo();
      IControladorModelo controladorModelo = new ControladorModelo();
      FilaDeNavios filaCadastrada =
    	  controladorModelo.buscarFilaDeNavios(new FilaDeNavios());
      controladorModelo = null;
  /*    if (filaCadastrada == null || filaCadastrada.getIdFilaDeNavios() == null)
      {
         throw new ObjetoPesquisadoNaoEncontradoException("exception.validacao.objeto.pesquisado.nao.encontrado", listaMensagens);
      }*/
      interfaceFilaDeNavios.setFilaDeNaviosVisualizada(filaCadastrada);
   }

   @Override
   public void verificarModoDeEdicao() throws ModoDeEdicaoException
   {
      List<String> paramMensagens = new ArrayList<String>();
      if (this.getInterfaceFilaDeNavios().getInterfaceInicial().getInterfaceDSP().getControladorDSP().getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida().isStatusEdicao())
      {
         paramMensagens.add("Fila de Navios");
         paramMensagens.add("Situação de Patio");
         throw new ModoDeEdicaoException("exception.ModoDeEdicao.Nao.Salvo", paramMensagens);
      } else
      {
        /* FilaDeNavios filaDeNavios = this.getInterfaceFilaDeNavios().getFilaDeNaviosVisualizada();
         if (filaDeNavios != null)
         {
            filaDeNavios.setStatusEdicao(Boolean.TRUE);
         }*/
      }
   }

   @Override
   public Atividade criaAtividadeDesatracarNavio(Navio navio, Date date)
   {
      Atividade atividadeDesatracacao = new Atividade();
    //  atividadeDesatracacao.setNavio(navio);
      atividadeDesatracacao.setTipoAtividade(TipoAtividadeEnum.SAIDA_DE_NAVIO);
      //data da atividade
      atividadeDesatracacao.setDtInicio(date);

      return atividadeDesatracacao;
   }

   @Override
   public Atividade verificaDesatracacaoDoNavioNoBerco(ControladorInterfaceInicial controlador)
   {
	   /*TODO 11079
	    *  rECUPERANDO A ULTIMA SIRTUCAO DE PATIO 
	    * */
	   Atividade atividade = null;


/*	   boolean continuarAtracacao = true;
	   //verifica se o pier esta sob interdicao
	   if (existePierSobInterdicao(controlador)) {
		   JLabel pergunta = new JLabel("Pier sob interdição! Deseja continuar?");//PropertiesUtil.getMessage("mensagem.option.pane.confirma.consolidacao.plano"));
		   pergunta.setFont(new Font("Tahoma", Font.PLAIN, 11));
		   int confirm = JOptionPane.showOptionDialog(
				   this.interfaceInicial.getInterfaceInicial(),
				   pergunta,
				   PropertiesUtil.getMessage("mensagem.option.pane.tipo.atencao") ,
				   JOptionPane.YES_OPTION,
				   JOptionPane.INFORMATION_MESSAGE,
				   null,
				   null,
				   null);

		   if (confirm != JOptionPane.YES_OPTION) {
			   continuarAtracacao = false;
		   }
	   }
	   if (continuarAtracacao) {*/


		   //for (Pier pier : controlador.getPlanejamento().getControladorDePlano().obterSituacaoPatioInicial().getPlanta().getListaPiers())
		   //      {
		   //List<Berco> listaBerco = controlador.getInterfaceInicial().getSituacaoPatioExibida().getPlanta().getListaBercos();
		   //for (Berco berco : listaBerco)
		   //{//percorrendo os bercos existentes para verificar se o navio atracado esta com a data de saida menor que a data atual
			  // if (berco.getNavioAtracado() != null)
			   //{//para um navio desatracar seu status deve ser de "E" (embarcado) e sua data de saida menor igual a da ultima situação de patio realizada
				  /* if(berco.getNavioAtracado().getStatusEmbarque().equalsIgnoreCase(PropertiesUtil.getMessage("label.abreviacao.embarcado"))
						   && berco.getNavioAtracado().getDiaDeSaida().before(controlador.getInterfaceInicial().getSituacaoPatioExibida().getDtInicio()) )
				   {
					   Date data = DSSStockyardTimeUtil.obterDataHoraUltimaSituacaoParaAtividades(getInterfaceInicial().getInterfaceInicial().getSituacaoPatioExibida().getDtInicio());
					   atividade = criaAtividadeDesatracarNavio(berco.getNavioAtracado(), data);
					   break;
				   }*/
			   //}
			   //         }
		   //}
	   //}

	   return atividade;
   }
   
/*   private boolean existePierSobInterdicao(ControladorInterfaceInicial controlador) {
	   boolean status = false;
	   try {   
		 //Recupera a situacao de patio do momento da atracacao
		   SituacaoPatio situacaoPatio = controlador.getInterfaceInicial().getSituacaoPatioExibida();
		   Date data = situacaoPatio.getDtInicio();
		   SimpleDateFormat sdf = new SimpleDateFormat();
		   sdf.applyPattern(PropertiesUtil.buscarPropriedade("format.atualizacao.timestamp"));
		   String str = sdf.format(data);
		   Date dataSituacao = sdf.parse(str);

		   if (situacaoPatio != null) {

			   for (Pier pier : situacaoPatio.getPlanta().getListaPiers()) {

				   if (pier.getListaInterdicao() != null || !pier.getListaInterdicao().isEmpty()) {


					   for(Interdicao interdicao : pier.getListaInterdicao()) {
						   //Retirando os milisegundos da data
						   str = sdf.format(interdicao.getDataInicial());
						   Date dataIni = sdf.parse(str);

						   str = sdf.format(interdicao.getDataFinal());
						   Date dataFim = sdf.parse(str); 


						   if (dataIni.compareTo(dataSituacao)<=0 &&
								   dataFim.compareTo(dataSituacao)>=0) {
							   status = true;
						   }
					   }
				   }
			   }
		   }
	   } catch (ParseException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
	   }
	   return status;
   }
*/
   /**
    * Verifica se existe algum navio na fila com status em "A" (atracado) significando que este navio deve sair da fila e ser
    * setado no respectivo berco de atracacao
    * @param filaNavio
    * @return
    */
   @Override
   public Navio verificaStatusAtracadoDoNavio(FilaDeNavios filaNavio)
   {
      Navio navioAtualizado = null;

      return navioAtualizado;
   }

   @Override
   public Atividade criaAtividadeAtracarNavio(AtividadeAtracarDesAtracarNavioVO movimentacaoVO,Date data) throws AtividadeException
   {
	  
	   movimentacaoVO.setDataInicio(Atividade.verificaAtualizaDataAtividade(movimentacaoVO.getDataInicio(),data));
       
	   Atividade atividade = ControladorFilaDeNavios.getInstance().movimentarNavio(movimentacaoVO);
      return atividade;
   }

   @Override
   public Navio verificarNavioExistenteNaFila(FilaDeNavios filaNavios, Navio navio)
   {
      Navio navioEncontrado = null;
      for (Navio navioFila : filaNavios.getListaDeNaviosNaFila())
      {

         if (navioFila.getNomeNavio().equals(navio.getNomeNavio())
                 && navioFila.getEta().equals(navio.getEta())
                 && navioFila.getDataEmbarque().equals(navio.getDataEmbarque()))
         {
            navioEncontrado = navioFila;
            break;
         }
      }
      return navioEncontrado;
   }

   @Override
   public Carga verificaCargaExistenteNoNavio(Navio navio, Carga carga)
   {
      Carga cargaEncontrada = null;
      /*for (Carga cargaNavio : navio.getListaDeCargasDoNavio())
      {
         if (cargaNavio.getIdentificadorCarga().equals(carga.getIdentificadorCarga())
                 && cargaNavio.getOrientacaoDeEmbarque().getQuantidadeNecessaria().equals(carga.getOrientacaoDeEmbarque().getQuantidadeNecessaria())
                 && cargaNavio.getProduto().getTipoProduto().equals(carga.getProduto().getTipoProduto()))
         {
            cargaEncontrada = carga;
            break;
         }
      }*/
      return cargaEncontrada;
   }

   @Override
   public List<Cliente> buscarClientes() throws ErroSistemicoException
   {
	   IControladorModelo controladorModelo = new ControladorModelo();
	   List<Cliente> listaClientesCadastrados = controladorModelo.buscaClientes();	    
	   return listaClientesCadastrados;
   }

   
}
