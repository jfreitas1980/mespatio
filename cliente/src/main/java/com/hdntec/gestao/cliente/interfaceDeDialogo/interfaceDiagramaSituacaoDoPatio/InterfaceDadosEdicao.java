package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios.ControladorInterfaceFilaDeNavios;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios.InterfaceCarga;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios.InterfaceEdicaoCarga;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios.InterfaceEditaNavioNaFilaDeNavios;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios.InterfaceInserirNavioFilaDeNavios;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios.InterfaceNavio;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios.InterfacePier;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.InterfaceEditarCampanha.ACAO;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.comparadores.ComparadorInterfaceBaliza;
import com.hdntec.gestao.cliente.planoDeEmpilhamentoERecuperacaoCliente.atualizacao.edicao.AtualizacaoEdicaoMaquinaDoPatio;
import com.hdntec.gestao.domain.navios.entity.MetaCarga;
import com.hdntec.gestao.domain.navios.entity.status.Carga;
import com.hdntec.gestao.domain.navios.entity.status.FilaDeNavios;
import com.hdntec.gestao.domain.navios.entity.status.Navio;
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.enums.TipoAtividadeEnum;
import com.hdntec.gestao.domain.plano.enums.TipoMaquinaEnum;
import com.hdntec.gestao.domain.planta.dao.PlantaDAO;
import com.hdntec.gestao.domain.planta.entity.MetaBaliza;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.Campanha;
import com.hdntec.gestao.domain.planta.entity.status.Filtragem;
import com.hdntec.gestao.domain.planta.entity.status.MaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Usina;
import com.hdntec.gestao.domain.produto.entity.TipoItemDeControle;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.domain.produto.enums.TipoDeProdutoEnum;
import com.hdntec.gestao.domain.vo.atividades.AtividadeAtracarDesAtracarNavioVO;
import com.hdntec.gestao.exceptions.AtividadeException;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.OperacaoCanceladaPeloUsuarioException;
import com.hdntec.gestao.exceptions.PilhaPelletFeedNaoEncontradaException;
import com.hdntec.gestao.exceptions.ValidacaoCampoException;
import com.hdntec.gestao.util.PropertiesUtil;

/**
 * Interface utilizada para editar os itens do DSP manualmente atrav�s do modo de edi 
 * 
 * @author andre
 *
 */
public class InterfaceDadosEdicao
{

   /** acesso ao controldaor do DSP*/
   ControladorDSP controladorDSP;

   /** acesso ao controladorInterfaceFilaDeNavios */
   ControladorInterfaceFilaDeNavios controladorInterfaceFilaDeNavios;

   public ControladorDSP getControladorDSP()
   {
      return controladorDSP;
   }

   public void setControladorDSP(ControladorDSP controladorDSP)
   {
      this.controladorDSP = controladorDSP;
   }

   public ControladorInterfaceFilaDeNavios getControladorInterfaceFilaDeNavios()
   {
      return controladorInterfaceFilaDeNavios;
   }

   public void setControladorInterfaceFilaDeNavios(ControladorInterfaceFilaDeNavios controladorInterfaceFilaDeNavios)
   {
      this.controladorInterfaceFilaDeNavios = controladorInterfaceFilaDeNavios;
   }

   /**
    *
    * Solicita os dados e preenche na correia editada
    * @param correiaEditada a correia a ser editada
    */
   public void solicitaDadosEdicaoCorreia(ActionEvent evt, InterfaceCorreia interfaceCorreia) throws OperacaoCanceladaPeloUsuarioException
   {

      final InterfaceEditarCorreia interfaceEditarCorreia = new InterfaceEditarCorreia(interfaceCorreia)
      {

         @Override
         public void fecharJanela()
         {
            controladorDSP.getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida().setStatusEdicao(Boolean.FALSE);
            this.setOperacaoCanceladaPeloUsuario(Boolean.TRUE);
         }
      };

      JDialog dialogInformacoesCorreia = new JDialog(interfaceCorreia.getInterfaceDSP().getInterfaceInicial(), true);
      dialogInformacoesCorreia.addWindowListener(new java.awt.event.WindowAdapter()
      {

         @Override
         public void windowClosing(java.awt.event.WindowEvent evt)
         {
            controladorDSP.getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida().setStatusEdicao(Boolean.FALSE);
            interfaceEditarCorreia.setOperacaoCanceladaPeloUsuario(Boolean.TRUE);
         }
      });

      dialogInformacoesCorreia.setLayout(new BorderLayout());
      dialogInformacoesCorreia.setTitle(PropertiesUtil.getMessage("mensagem.correia.informacoes"));
      dialogInformacoesCorreia.getContentPane().add(interfaceEditarCorreia);
      dialogInformacoesCorreia.pack();
      dialogInformacoesCorreia.setLocationRelativeTo(null);
      dialogInformacoesCorreia.setVisible(true);

      if (interfaceEditarCorreia.getOperacaoCanceladaPeloUsuario()) {
         dialogInformacoesCorreia.setVisible(false);
         controladorDSP.getInterfaceInicial().getInterfaceComandos().cancelarModoOperacao();
         throw new OperacaoCanceladaPeloUsuarioException(PropertiesUtil.buscarPropriedade("exception.operacao.cancelada.pelo.usuario"));
      }

      //edita os dados da correia editada na interface
      interfaceCorreia.getCorreiaVisualizada().setEstado(interfaceEditarCorreia.getEstado());
      interfaceCorreia.getCorreiaVisualizada().setTaxaDeOperacao(interfaceEditarCorreia.getTaxaDeOperacao());

      dialogInformacoesCorreia.setVisible(false);
      controladorDSP.getInterfaceInicial().getInterfaceComandos().finalizarEdicoes();
      controladorDSP.getInterfaceInicial().getInterfaceInicial().montaInterfaceDSP();
   }

   /**
    * Solicita a edi  dos dados das balizas selecionadas no modo de edi o o sistema e preenche nas balizas editadas
    * @param listaDeBalizas a lista de balizas a ser editada
    */
   public void solicitaDadosEdicaoBalizas(List<InterfaceBaliza> listaDeBalizas) throws OperacaoCanceladaPeloUsuarioException, ErroSistemicoException
   {

      InterfaceEdicaoDadosBaliza interfaceDadosEdicaoBaliza = new InterfaceEdicaoDadosBaliza(controladorDSP.getInterfaceDSP().getInterfaceInicial(), true, listaDeBalizas, controladorDSP);
      interfaceDadosEdicaoBaliza.setLocationRelativeTo(null);
      interfaceDadosEdicaoBaliza.setVisible(Boolean.TRUE);

      // A operacao de edicao da baliza foi cancelada pelo usu�rio
      if (interfaceDadosEdicaoBaliza.getOperacaoCanceladaPeloUsuario()) {
         controladorDSP.getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida().setStatusEdicao(Boolean.FALSE);
         controladorDSP.getInterfaceInicial().getInterfaceComandos().cancelarModoOperacao();
         throw new OperacaoCanceladaPeloUsuarioException(PropertiesUtil.buscarPropriedade("exception.operacao.cancelada.pelo.usuario"));
      }
   }

   /**
    * Solicita a edi o   m�quina para atualiza o d empilhamento
    */
   public void solicitaAtualizacaoEmpilhamento(MaquinaDoPatio maquinaDoPatioEditada, InterfaceMaquinaDoPatio interfaceMaquinaDoPatio, boolean finalizaAtividade) throws OperacaoCanceladaPeloUsuarioException, ErroSistemicoException
   {

      InterfaceAtualizacaoMaquinaEmpilhamento interfaceAtualizacaoMaquinaEmpilhamento = new InterfaceAtualizacaoMaquinaEmpilhamento(controladorDSP.getInterfaceDSP().getInterfaceInicial(), true, controladorDSP, maquinaDoPatioEditada, interfaceMaquinaDoPatio, finalizaAtividade);
      interfaceAtualizacaoMaquinaEmpilhamento.setLocationRelativeTo(null);
      interfaceAtualizacaoMaquinaEmpilhamento.setVisible(Boolean.TRUE);

      controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();

      // A operacao de atualizacao do empilhamento
      if (interfaceAtualizacaoMaquinaEmpilhamento.getOperacaoCanceladaPeloUsuario()) {
         controladorDSP.getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida().setStatusEdicao(Boolean.FALSE);
         controladorDSP.getInterfaceInicial().getInterfaceComandos().cancelarModoOperacao();
         throw new OperacaoCanceladaPeloUsuarioException(PropertiesUtil.buscarPropriedade("exception.operacao.cancelada.pelo.usuario"));
      }

      controladorDSP.getInterfaceInicial().getInterfaceInicial().atualizarDSP();

   }

   /**
    * Verifica se a lista de balizas a ser edita � contigua
    * @param listaDeBalizas: a lista de balizas a ser editada
    * @return verdadeiro caso a lista de balizas seja contigua
    */
   public boolean verificaListaBalizaContiguas(List<InterfaceBaliza> listaDeBalizas)
   {
      boolean flag = false;
      if (listaDeBalizas == null) {
         return false;
      }
      Collections.sort(listaDeBalizas, new ComparadorInterfaceBaliza());
      if (listaDeBalizas.size() == 1) {
         return flag = true;
      }
      for (int i = 0; i < listaDeBalizas.size() - 1; i++) {
         if ((listaDeBalizas.get(i + 1).getBalizaVisualizada().getNumero() == (listaDeBalizas.get(i).getBalizaVisualizada().getNumero() + 1)) && (listaDeBalizas.get(i + 1).getBalizaVisualizada().getPatio().getIdPatio().equals(listaDeBalizas.get(i).getBalizaVisualizada().getPatio().getIdPatio()))) {
            flag = true;
         }
         else {
            return false;
         }
      }

      return flag;
   }

   /**
    * Edita uma campanha a partir das informa es i eridas pelo uus�rio
    */
   public void editarCampanha(InterfaceUsina interfaceUsina, List<TipoProduto> listaTiposProduto, List<TipoItemDeControle> listaTiposItemDeControle,ACAO acao,Campanha campanha) throws OperacaoCanceladaPeloUsuarioException
   {

      final InterfaceEditarCampanha interfaceEditarCampanha = new InterfaceEditarCampanha(interfaceUsina, listaTiposProduto, listaTiposItemDeControle,acao,campanha)
      {

         @Override
         public void fecharJanela()
         {
            controladorDSP.getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida().setStatusEdicao(Boolean.FALSE);
            this.setOperacaoCanceladaPeloUsuario(Boolean.TRUE);
         }
      };

      JDialog dialogInformacoesUsina = new JDialog(interfaceUsina.getInterfaceDSP().getInterfaceInicial(), true);
      dialogInformacoesUsina.addWindowListener(new java.awt.event.WindowAdapter()
      {

         @Override
         public void windowClosing(java.awt.event.WindowEvent evt)
         {
            controladorDSP.getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida().setStatusEdicao(Boolean.FALSE);
            interfaceEditarCampanha.setOperacaoCanceladaPeloUsuario(Boolean.TRUE);
         }
      });

      dialogInformacoesUsina.setLayout(new BorderLayout());
      dialogInformacoesUsina.setTitle(PropertiesUtil.getMessage("mensagem.campanha.info"));
      dialogInformacoesUsina.getContentPane().add(interfaceEditarCampanha);
      dialogInformacoesUsina.pack();
      dialogInformacoesUsina.setLocationRelativeTo(null);
      dialogInformacoesUsina.setVisible(true);

      if (interfaceEditarCampanha.getOperacaoCanceladaPeloUsuario()) {
         dialogInformacoesUsina.setVisible(false);
         controladorDSP.getInterfaceInicial().getInterfaceComandos().cancelarModoOperacao();
         throw new OperacaoCanceladaPeloUsuarioException(PropertiesUtil.buscarPropriedade("exception.operacao.cancelada.pelo.usuario"));
      }

      controladorDSP.getInterfaceInicial().getInterfaceInicial().montaInterfaceDSP();
//      controladorDSP.getInterfaceInicial().getInterfaceInicial().atualizarDSP();
//      controladorDSP.getInterfaceInicial().getInterfaceInicial().montaPatio();
      controladorDSP.getInterfaceInicial().getPlanejamento().ativarNecessidadeDeConsolidacao();
      controladorDSP.getInterfaceInicial().desativarModoEdicao();
   }

   /**
    * Solicita a edi o dos ados da Orientacao de Embarque de uma Carga do Navio selecionado
    * @param interfaceNavio
    * @throws br.com.cflex.supervision.stockYard.cliente.exception.OperacaoCanceladaPeloUsuarioException
    */
   public void solicitaInsercaoNavioNaFila(InterfaceNavio interfaceNavio) throws OperacaoCanceladaPeloUsuarioException
   {
      final InterfaceInserirNavioFilaDeNavios interfaceInserirNavioFilaDeNavios = new InterfaceInserirNavioFilaDeNavios(interfaceNavio, controladorInterfaceFilaDeNavios)
      {

         @Override
         public void fecharJanela()
         {
            FilaDeNavios filaDeNavios = controladorInterfaceFilaDeNavios.getInterfaceFilaDeNavios().getFilaDeNaviosVisualizada();            
            this.setOperacaoCanceladaPeloUsuario(Boolean.TRUE);
         }
      };

      final JDialog dialogInfomacaoEdicaoNavio = new JDialog(controladorInterfaceFilaDeNavios.getInterfaceFilaDeNavios().getInterfaceInicial(), true);
      dialogInfomacaoEdicaoNavio.addWindowListener(new java.awt.event.WindowAdapter()
      {

         @Override
         public void windowClosing(java.awt.event.WindowEvent evt)
         {
            FilaDeNavios filaDeNavios = controladorInterfaceFilaDeNavios.getInterfaceFilaDeNavios().getFilaDeNaviosVisualizada();            
            interfaceInserirNavioFilaDeNavios.setOperacaoCanceladaPeloUsuario(Boolean.TRUE);
         }
      });

      dialogInfomacaoEdicaoNavio.setLayout(new BorderLayout());
      dialogInfomacaoEdicaoNavio.setTitle(PropertiesUtil.getMessage("mensagem.navio.inserir"));
      dialogInfomacaoEdicaoNavio.getContentPane().add(interfaceInserirNavioFilaDeNavios);
      dialogInfomacaoEdicaoNavio.pack();
      dialogInfomacaoEdicaoNavio.setLocationRelativeTo(null);
      dialogInfomacaoEdicaoNavio.setVisible(true);

      if (interfaceInserirNavioFilaDeNavios.getOperacaoCanceladaPeloUsuario()) {
         interfaceInserirNavioFilaDeNavios.setVisible(false);
         controladorDSP.getInterfaceInicial().getInterfaceComandos().cancelarModoOperacao();
         throw new OperacaoCanceladaPeloUsuarioException(PropertiesUtil.buscarPropriedade("exception.operacao.cancelada.pelo.usuario"));
      }

      Navio navio = insereNavioNaFila(interfaceInserirNavioFilaDeNavios);


      dialogInfomacaoEdicaoNavio.setVisible(false);
      controladorDSP.getInterfaceInicial().getPlanejamento().ativarNecessidadeDeConsolidacao();
      controladorInterfaceFilaDeNavios.getInterfaceFilaDeNavios().setFilaDeNaviosEditada(Boolean.TRUE);
      controladorDSP.getInterfaceInicial().getPlanejamento().ativarNecessidadeDeConsolidacao();
      //Darley. O metodo atualizarDSP n�o est� atualizando o novo navio inserido 
//      controladorDSP.getInterfaceInicial().getInterfaceInicial().atualizarDSP();
      controladorDSP.getInterfaceInicial().getInterfaceInicial().montaInterfaceFilaNavios(true);
      
      //controladorDSP.getInterfaceInicial().getInterfaceInicial().montaPatio();
   }


   /**
    * Solicita a edi o dos ados da Orientacao de Embarque de uma Carga do Navio selecionado
    * @param interfaceNavio
    * @throws br.com.cflex.supervision.stockYard.cliente.exception.OperacaoCanceladaPeloUsuarioException
 * @throws ErroSistemicoException 
    */
   public void solicitaAtualizacaoNavioNaFila(InterfaceNavio interfaceNavio) throws OperacaoCanceladaPeloUsuarioException, ErroSistemicoException
   {
      
          Date dataSituacao = controladorDSP.getInterfaceInicial().getPlanejamento().getControladorDePlano().obterSituacaoPatioFinal().getDtInicio();
          controladorDSP.getInterfaceInicial().executarIntegracaoSistemaNavioCRM(interfaceNavio.getNavioVisualizado().getMetaNavio(), dataSituacao);
          controladorDSP.getInterfaceInicial().getPlanejamento().ativarNecessidadeDeConsolidacao();          
   }

   
   
   /**
    * Solicita a edi o dos  dos da Carga do Navio selecionado
    * @param navioEditado a InterfaceNavio que esta sendo editado
    */
   public void solicitaDadosEdicaoNavio(InterfaceNavio navioEditado, InterfaceEditaNavioNaFilaDeNavios interfaceEditaNavioNaFilaDeNavios, Integer modoOperacao, String identificador) throws ErroSistemicoException
   {
      //instancia para verificar onde o navio esta sendo editado (No berco ou na Fila de navios
      final InterfaceEdicaoCarga interfaceEdicaoCarga = new InterfaceEdicaoCarga(navioEditado, interfaceEditaNavioNaFilaDeNavios, modoOperacao, identificador)
      {

         @Override
         public void fecharJanela()
         {
            this.setOperacaoCanceladaPeloUsuario(Boolean.TRUE);
         }
      };

      final JDialog dialogInfomacaoEdicaoCarga = new JDialog(controladorInterfaceFilaDeNavios.getInterfaceFilaDeNavios().getInterfaceInicial(), true);
      dialogInfomacaoEdicaoCarga.addWindowListener(new java.awt.event.WindowAdapter()
      {

         @Override
         public void windowClosing(java.awt.event.WindowEvent evt)
         {
            interfaceEdicaoCarga.setOperacaoCanceladaPeloUsuario(Boolean.TRUE);
         }
      });

      dialogInfomacaoEdicaoCarga.setLayout(new BorderLayout());
      dialogInfomacaoEdicaoCarga.setTitle(PropertiesUtil.getMessage("mensagem.edicao.dados.carga"));
      dialogInfomacaoEdicaoCarga.getContentPane().add(interfaceEdicaoCarga);
      dialogInfomacaoEdicaoCarga.pack();
      dialogInfomacaoEdicaoCarga.setLocationRelativeTo(null);
      dialogInfomacaoEdicaoCarga.setVisible(true);

      if (interfaceEdicaoCarga.getOperacaoCanceladaPeloUsuario()) {
         interfaceEdicaoCarga.setVisible(false);
      }

      dialogInfomacaoEdicaoCarga.setVisible(false);
      if (interfaceEdicaoCarga.getOperacaoSelecionada() == InterfaceEdicaoCarga.MODO_EXCLUIR) {          
          for (Carga carga : interfaceEditaNavioNaFilaDeNavios.getListaCargas()) {
             MetaCarga metaCarga = carga.getMetaCarga();
             InterfaceCarga interfaceCarga = interfaceEdicaoCarga.getEdicaoDadosCarga().getInterfaceCarga();
             if (interfaceCarga != null && metaCarga.equals(interfaceCarga.getCargaVisualizada().getMetaCarga())) {
                 interfaceEditaNavioNaFilaDeNavios.getInterfaceNavio().getNavioVisualizado().getMetaNavio().getListaMetaCargas().remove(metaCarga);
                 break;
             } 
         }
          
/*         
         
         
         interfaceEditaNavioNaFilaDeNavios.getListaCargasExcluidas().add(interfaceEdicaoCarga.getEdicaoDadosCarga().getInterfaceCarga().getCargaVisualizada());
         interfaceEditaNavioNaFilaDeNavios.getListaInterfaceCargasExcluidas().add(interfaceEdicaoCarga.getEdicaoDadosCarga().getInterfaceCarga());*/
      }
      else if (interfaceEdicaoCarga.getOperacaoSelecionada() == InterfaceEdicaoCarga.MODO_INSERIR) {
         interfaceEditaNavioNaFilaDeNavios.getListaCargas().add(interfaceEdicaoCarga.getEdicaoDadosCarga().getInterfaceCarga().getCargaVisualizada());
         interfaceEditaNavioNaFilaDeNavios.getListaInterfaceCargasIncluidas().add(interfaceEdicaoCarga.getEdicaoDadosCarga().getInterfaceCarga());
      }

      interfaceEditaNavioNaFilaDeNavios.getEdicaoDadosCarga().atualizaDadosTela();
      interfaceEditaNavioNaFilaDeNavios.getEditaOrientacaoEmbarque().atualizaDadosCampos();
      interfaceEditaNavioNaFilaDeNavios.atualizaCampos();
      controladorDSP.getInterfaceInicial().getInterfaceInicial().montaInterfaceDSP();
   }

   /**
    * insere uma nova carga no navio
    * @param navio
    * @param InterfaceEditaNavioNaFilaDeNavios
    */
   public void inserirCarga(InterfaceNavio navio, InterfaceEditaNavioNaFilaDeNavios interfaceEditaNavioNaFilaDeNavios)
   {
      for (Carga carga : interfaceEditaNavioNaFilaDeNavios.getListaCargas()) {
         //if (carga.getIdCarga() == null) {
            for (InterfaceCarga interfaceCarga : interfaceEditaNavioNaFilaDeNavios.getListaInterfaceCargasIncluidas()) {
               if (interfaceCarga.getCargaVisualizada().getIdentificadorCarga().equals(carga.getIdentificadorCarga())) {
                  interfaceCarga.getCargaVisualizada().setCargaEditada(Boolean.TRUE);
                  navio.getListaDecarga().add(interfaceCarga);
                  // inclui a nova carga
                  navio.getNavioVisualizado().incluiNovaCarga(carga, controladorDSP.getInterfaceInicial().getInterfaceInicial().getSituacaoPatioExibida().getDtInicio());
                  interfaceCarga.repaint();
               }
            }
         //}
      }
   }

   /**
    * Exclui a Carga selecionada da interface
    * @param navio
    * @param InterfaceEditaNavioNaFilaDeNavios
    */
   public void excluirCarga(InterfaceNavio navio, InterfaceEditaNavioNaFilaDeNavios interfaceEditaNavioNaFilaDeNavios)
   {
      for (Carga carga : interfaceEditaNavioNaFilaDeNavios.getListaCargasExcluidas()) {
         for (InterfaceCarga interfaceCarga : interfaceEditaNavioNaFilaDeNavios.getListaInterfaceCargasExcluidas()) {
            if (interfaceCarga.getCargaVisualizada().getIdentificadorCarga().equals(carga.getIdentificadorCarga())) {
               navio.getNavioVisualizado().getListaDeCargasDoNavio(navio.getHoraSituacao()).remove(carga);
               navio.getListaDecarga().remove(interfaceCarga);
               //carga.setNavio(null);
            }
         }
      }
   }

   /**
    * solicita que a Fila de Navios do plano seja editada
    * @param interfaceNavio
    * @throws br.com.cflex.supervision.stockYard.cliente.exception.OperacaoCanceladaPeloUsuarioException
    */
   public void solicitaDadosEdicaoFilaDeNavios(InterfaceNavio interfaceNavio, Integer modoOperacao) throws OperacaoCanceladaPeloUsuarioException
   {

      final InterfaceEditaNavioNaFilaDeNavios interfaceEditaNavioNaFilaDeNavios = new InterfaceEditaNavioNaFilaDeNavios(interfaceNavio, controladorInterfaceFilaDeNavios, modoOperacao)
      {

         @Override
         public void fecharJanela()
         {
            FilaDeNavios filaDeNavios = controladorInterfaceFilaDeNavios.getInterfaceFilaDeNavios().getFilaDeNaviosVisualizada();
            if (filaDeNavios != null) {
              controladorDSP.getInterfaceInicial().getInterfaceInicial().atualizarDSP(); 
//                filaDeNavios.setStatusEdicao(Boolean.FALSE);
            }
            this.setOperacaoCanceladaPeloUsuario(Boolean.TRUE);
         }
      };

      final JDialog dialogInfomacaoEdicaoNavio = new JDialog(controladorInterfaceFilaDeNavios.getInterfaceFilaDeNavios().getInterfaceInicial(), true);
      dialogInfomacaoEdicaoNavio.addWindowListener(new java.awt.event.WindowAdapter()
      {

         @Override
         public void windowClosing(java.awt.event.WindowEvent evt)
         {
            //FilaDeNavios filaDeNavios = controladorInterfaceFilaDeNavios.getInterfaceFilaDeNavios().getFilaDeNaviosVisualizada();
             controladorDSP.getInterfaceInicial().getInterfaceInicial().atualizarDSP();
             interfaceEditaNavioNaFilaDeNavios.setOperacaoCanceladaPeloUsuario(Boolean.TRUE);
         }
      });

      dialogInfomacaoEdicaoNavio.setLayout(new BorderLayout());
      dialogInfomacaoEdicaoNavio.setTitle(PropertiesUtil.getMessage("mensagem.navio.edicao"));
      dialogInfomacaoEdicaoNavio.getContentPane().add(interfaceEditaNavioNaFilaDeNavios);
      dialogInfomacaoEdicaoNavio.pack();
      dialogInfomacaoEdicaoNavio.setLocationRelativeTo(null);
      dialogInfomacaoEdicaoNavio.setVisible(true);

      if (interfaceEditaNavioNaFilaDeNavios.getOperacaoCanceladaPeloUsuario()) {
         interfaceEditaNavioNaFilaDeNavios.setVisible(false);
         controladorDSP.getInterfaceInicial().getInterfaceComandos().cancelarModoOperacao();
         throw new OperacaoCanceladaPeloUsuarioException(PropertiesUtil.buscarPropriedade("exception.operacao.cancelada.pelo.usuario"));
      }

//        AtualizacaoEdicaoNavioNaFilaDeNavios atualizacao = new AtualizacaoEdicaoNavioNaFilaDeNavios(interfaceNavio, interfaceNavio.getNavioVisualizado().getDwt(),
//                interfaceNavio.getNavioVisualizado().getNomeNavio(), interfaceNavio.getNavioVisualizado().getStatusEmbarque(),
//                interfaceNavio.getNavioVisualizado().getEta(), interfaceNavio.getNavioVisualizado().getDiaDeChegada(),
//                interfaceNavio.getNavioVisualizado().getDiaDeSaida(), interfaceNavio.getNavioVisualizado().getDataEmbarque(),
//                interfaceNavio.getNavioVisualizado().getCliente(), interfaceNavio.getNavioVisualizado().getBercoDeAtracacao());
//
//        controladorInterfaceFilaDeNavios.getControladorAtualizacaoEdicao().adicionarEdicao(atualizacao);

      if (interfaceEditaNavioNaFilaDeNavios.getOperacaoSelecionada() != InterfaceEditaNavioNaFilaDeNavios.MODO_EDITAR_ATRACADO) {
         editaNavioNaFila(interfaceEditaNavioNaFilaDeNavios, interfaceNavio);
      }
      inserirCarga(interfaceNavio, interfaceEditaNavioNaFilaDeNavios);
      excluirCarga(interfaceNavio, interfaceEditaNavioNaFilaDeNavios);

      
      interfaceNavio.updateUI();
      interfaceNavio.repaint();

      dialogInfomacaoEdicaoNavio.setVisible(false);

      controladorInterfaceFilaDeNavios.getInterfaceFilaDeNavios().setFilaDeNaviosEditada(Boolean.TRUE);
      controladorDSP.getInterfaceInicial().getPlanejamento().ativarNecessidadeDeConsolidacao();
      controladorDSP.getInterfaceInicial().getInterfaceInicial().montaInterfaceDSP();
      //Darley Nao est� atualizando a interface grafica da fila de navios com o metodo atualizarDSP
//      controladorDSP.getInterfaceInicial().getInterfaceInicial().atualizarDSP();      
      //controladorDSP.getInterfaceInicial().getInterfaceInicial().montaPatio();
   }

   /**
    * insere um navio na Fila de Navios
    * @param interfaceEditaNavioNaFilaDeNavios
    */
   public Navio insereNavioNaFila(InterfaceInserirNavioFilaDeNavios interfaceInserirNavioFilaDeNavios) throws OperacaoCanceladaPeloUsuarioException
   {
	  //Date horaInsercao = controladorDSP.getInterfaceInicial().getInterfaceInicial().getSituacaoPatioExibida().getDtInicio();
      /**
       * TODO DEFINIR QUAL SERÁ O HORARIO- ACREDITO Q DEVA SER O ETA ..E RESTRINGIR ATRACAR ANTES 
       * DO ETA 
       * 
       */
	  String str_date="01/01/2011";
      DateFormat formatter ;          
      formatter = new SimpleDateFormat("MM/dd/yyyy");
      Date horaInsercao = null;
      try {
    	  horaInsercao = (Date)formatter.parse(str_date);         
     } catch (ParseException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
     } 
	  
	  
	  //Atividade.verificaAtualizaDataAtividade(horaInsercao, controladorDSP.getInterfaceInicial().getInterfaceInicial().getSituacaoPatioExibida().getDtInicio());
	  Navio navio = interfaceInserirNavioFilaDeNavios.getEditarInserirDadosNavioNaFilaDeNavios().criaNavio(horaInsercao);
      InterfaceNavio interfaceNavio = new InterfaceNavio(horaInsercao);
      interfaceNavio.setBercoDeAtracacao(null);
      interfaceNavio.setComponenteInterfaceFilaNavios(null);
      interfaceNavio.setNavioVisualizado(navio);
      interfaceNavio.setControladorInterfaceFilaDeNavios(controladorInterfaceFilaDeNavios);
      
     // controladorInterfaceFilaDeNavios.inserirNavioFilaDeNavios(interfaceNavio);
      controladorInterfaceFilaDeNavios.getInterfaceFilaDeNavios().getFilaDeNaviosVisualizada().getListaDeNaviosNaFila().add(navio);
      return navio;
   }

   /**
    * solicita a edi o de um avio selecionado na fila de navios
    * @param interfaceEditaNavioNaFilaDeNavios
    * @param interfaceNavio
    */
   public void editaNavioNaFila(InterfaceEditaNavioNaFilaDeNavios interfaceEditaNavioNaFilaDeNavios, InterfaceNavio interfaceNavio)
   {
      interfaceNavio.getNavioVisualizado().setNomeNavio(interfaceEditaNavioNaFilaDeNavios.getEditaDadosNavioNaFilaDeNavios().getNomeNavio());
      interfaceNavio.getNavioVisualizado().setDwt(interfaceEditaNavioNaFilaDeNavios.getEditaDadosNavioNaFilaDeNavios().getDwt());
      interfaceNavio.getNavioVisualizado().setEta(interfaceEditaNavioNaFilaDeNavios.getEditaDadosNavioNaFilaDeNavios().getEta());
      interfaceNavio.getNavioVisualizado().setDiaDeChegada(interfaceEditaNavioNaFilaDeNavios.getEditaDadosNavioNaFilaDeNavios().getDataChegada());
      interfaceNavio.getNavioVisualizado().setDiaDeSaida(interfaceEditaNavioNaFilaDeNavios.getEditaDadosNavioNaFilaDeNavios().getDataSaida());
      interfaceNavio.getNavioVisualizado().setDataEmbarque(interfaceEditaNavioNaFilaDeNavios.getEditaDadosNavioNaFilaDeNavios().getDataEmbarque());
      //interfaceNavio.getNavioVisualizado().setStatusEmbarque(interfaceEditaNavioNaFilaDeNavios.getEditaDadosNavioNaFilaDeNavios().getStatus());

      //remove da lista de navios do cliente o ojeto modificado
      //interfaceNavio.getNavioVisualizado().getCliente().getListaDeNaviosDoCliente().remove(interfaceNavio.getNavioVisualizado());
      //RetornaObjetosDaInterfaceFilaDeNavios roifn = new RetornaObjetosDaInterfaceFilaDeNavios();
      //Cliente cliente = roifn.buscaClinteEquivalenteNaListaNavios(controladorInterfaceFilaDeNavios.getInterfaceFilaDeNavios().getFilaDeNaviosVisualizada().getListaDeNaviosNaFila(), interfaceEditaNavioNaFilaDeNavios.getEditaDadosNavioNaFilaDeNavios().getCliente());
      //adiciona o novo cliente editado para o objeto navio
      //if (interfaceNavio.getNavioVisualizado().getCliente() == null && cliente != null) {
         //interfaceNavio.getNavioVisualizado().setCliente(cliente);
      //}
      //adiciona na listaDeNavios deste novo Cliente o Navio
      //interfaceNavio.getNavioVisualizado().getCliente().getListaDeNaviosDoCliente().add(interfaceNavio.getNavioVisualizado());
   }

   /**
    * exclui o navio selecionado da fila
    * @param navioASerExcluido
    */
   public void excluirNavioDaFila(Navio navioASerExcluido) throws ErroSistemicoException
   {
      //navioASerExcluido.getCliente().getListaDeNaviosDoCliente().remove(navioASerExcluido);
      //Darley retirando chamada remota
//      IControladorModelo controladorModelo = InterfaceInicial.lookUpModelo();
      //IControladorModelo controladorModelo = new ControladorModelo();
      //controladorModelo.removerNavio(navioASerExcluido);
      //controladorInterfaceFilaDeNavios.getInterfaceFilaDeNavios().getFilaDeNaviosVisualizada().getListaDeNaviosNaFila().remove(navioASerExcluido);
      //controladorModelo = null;
      
      //Darley Nao est� atualizando a interface grafica da fila de navios com o metodo atualizarDSP
       try {
           AtividadeAtracarDesAtracarNavioVO movimentacaoVO = new AtividadeAtracarDesAtracarNavioVO();
           Date dataAtividade = controladorDSP.getInterfaceInicial().getPlanejamento().getControladorDePlano().obterSituacaoPatioFinal().getDtInicio();
           movimentacaoVO.setTipoAtividade(TipoAtividadeEnum.NAVIO_EXCLUIDO);
           movimentacaoVO.setDataInicio(Atividade.verificaAtualizaDataAtividade(dataAtividade, controladorDSP.getInterfaceInicial().getPlanejamento().getControladorDePlano().obterSituacaoPatioFinal().getDtInicio()));           
           movimentacaoVO.setMetaBercoDestino(null);  
           movimentacaoVO.setMetaNavio(navioASerExcluido.getMetaNavio());
           this.getControladorInterfaceFilaDeNavios().getInterfaceInicial().verificaMovimentacaoNavio(movimentacaoVO);
       
       } catch (AtividadeException e) {
          throw new ErroSistemicoException(e);           
       }
              
       controladorDSP.getInterfaceInicial().getInterfaceInicial().atualizarDSP();
   }

   public void solicitaDadosEdicaoMaquina(MaquinaDoPatio maquinaDoPatioEditada, InterfaceMaquinaDoPatio interfaceMaquinaDoPatio) throws OperacaoCanceladaPeloUsuarioException, ValidacaoCampoException
   {
      final InterfaceEditaDadosMaquina interfaceEditaDadosMaquina = new InterfaceEditaDadosMaquina(interfaceMaquinaDoPatio, maquinaDoPatioEditada.getTaxaDeOperacaoNominal(), maquinaDoPatioEditada.getEstado(), maquinaDoPatioEditada.getPosicao(), maquinaDoPatioEditada.getVelocidadeDeslocamento())
      {

         @Override
         public void fecharJanela()
         {
            this.setOperacaoCanceladaPeloUsuario(Boolean.TRUE);
         }
      };

      JDialog dialogInfomacaoEdicaoMaquina = new JDialog(controladorDSP.getInterfaceDSP().getInterfaceInicial(), true);
      dialogInfomacaoEdicaoMaquina.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
      dialogInfomacaoEdicaoMaquina.addWindowListener(new java.awt.event.WindowAdapter()
      {

         @Override
         public void windowClosing(java.awt.event.WindowEvent evt)
         {
            interfaceEditaDadosMaquina.setOperacaoCanceladaPeloUsuario(Boolean.TRUE);
         }
      });

      interfaceEditaDadosMaquina.setControladorDSP(interfaceMaquinaDoPatio.getControladorDSP());

      dialogInfomacaoEdicaoMaquina.setLayout(new BorderLayout());
      dialogInfomacaoEdicaoMaquina.setTitle(PropertiesUtil.getMessage("mensagem.edicao.dados.maquina"));
      dialogInfomacaoEdicaoMaquina.getContentPane().add(interfaceEditaDadosMaquina);
      dialogInfomacaoEdicaoMaquina.pack();
      dialogInfomacaoEdicaoMaquina.setLocationRelativeTo(null);
      dialogInfomacaoEdicaoMaquina.setVisible(true);

      if (interfaceEditaDadosMaquina.getOperacaoCanceladaPeloUsuario()) {
         interfaceEditaDadosMaquina.setVisible(false);
         controladorDSP.getInterfaceInicial().getInterfaceComandos().cancelarModoOperacao();
         throw new OperacaoCanceladaPeloUsuarioException(PropertiesUtil.buscarPropriedade("exception.operacao.cancelada.pelo.usuario"));
      } else {
         AtualizacaoEdicaoMaquinaDoPatio atualizacao = new AtualizacaoEdicaoMaquinaDoPatio(interfaceMaquinaDoPatio, interfaceEditaDadosMaquina);
    	  atualizacao.execute();
      }	  
      	  controladorDSP.getInterfaceInicial().getInterfaceInicial().atualizarDSP();
      	  controladorDSP.getInterfaceInicial().exibirUltimaSituacaoPatio();
    	  dialogInfomacaoEdicaoMaquina.setVisible(false);
    	  interfaceEditaDadosMaquina.setVisible(false);
    	  
    	  
      
   }

   private void verificarDadosEdicaoMaquina(MaquinaDoPatio maquinaDoPatioEditada, InterfaceMaquinaDoPatio interfaceMaquinaDoPatio, InterfaceEditaDadosMaquina interfaceEditaDadosMaquina) throws ValidacaoCampoException
   {
      if (maquinaDoPatioEditada.getTipoDaMaquina() == TipoMaquinaEnum.PA_CARREGADEIRA) {
         //atribui o novo patio editado
         for (InterfacePatio ipatio : controladorDSP.getInterfaceDSP().getListaDePatios()) {
            if (ipatio.equals(interfaceEditaDadosMaquina.getIpatio())) {
               maquinaDoPatioEditada.setPatio(ipatio.getPatioVisualizado());
               interfaceMaquinaDoPatio.setPatio(ipatio);

               //atualiza a lista de maquinas do novo patio
               ipatio.getListaDeMaquinas().add(interfaceMaquinaDoPatio);
               ipatio.getPatioVisualizado().getListaDeMaquinasDoPatio(controladorDSP.getInterfaceInicial().getInterfaceInicial().getSituacaoPatioExibida().getDtInicio()).add(maquinaDoPatioEditada);
               break;
            }
         }
         for (InterfacePatio ipatio : controladorDSP.getInterfaceDSP().getListaDePatios()) {
            if (ipatio.equals(interfaceEditaDadosMaquina.getIpatioDaMaquina())) {
               ipatio.getListaDeMaquinas().remove(interfaceMaquinaDoPatio);
               ipatio.getPatioVisualizado().getListaDeMaquinasDoPatio(controladorDSP.getInterfaceInicial().getInterfaceInicial().getSituacaoPatioExibida().getDtInicio()).remove(maquinaDoPatioEditada);
               break;
            }
         }

         // atribui a nova posicao
         for (MetaBaliza metaBaliza : maquinaDoPatioEditada.getPatio().getMetaPatio().getListaDeMetaBalizas()) {
            if (metaBaliza.getNumero().equals(interfaceEditaDadosMaquina.getPosicaoNroBaliza())) {
               maquinaDoPatioEditada.setPosicao(metaBaliza.retornaStatusHorario(controladorDSP.getInterfaceInicial().getInterfaceInicial().getSituacaoPatioExibida().getDtInicio()));
               break;
            }
         }
         for (InterfaceBaliza interfaceBaliza : interfaceEditaDadosMaquina.getIpatio().getListaDeBalizas()) {
            if (interfaceBaliza.getBalizaVisualizada().getNumero().equals(interfaceMaquinaDoPatio.getMaquinaDoPatioVisualizada().getPosicao().getNumero())) {
               interfaceMaquinaDoPatio.setPosicao(interfaceBaliza);
               break;
            }
         }
         // verifica taxa de operacao
         maquinaDoPatioEditada.setTaxaDeOperacaoNominal(interfaceEditaDadosMaquina.getTaxaOperacao());
         // atualiza o Estado
//         maquinaDoPatioEditada.setEstado(interfaceEditaDadosMaquina.getEstado());
         //velocidade de deslocamento
         maquinaDoPatioEditada.setVelocidadeDeslocamento(interfaceEditaDadosMaquina.getVelocidadeDeslocamento());
      }
      else {
         if (interfaceMaquinaDoPatio.getMaquinaDoPatioVisualizada().getBracoNoPatioInferior()) {
            for (Baliza baliza : maquinaDoPatioEditada.getCorreia().getPatioInferior().getListaDeBalizas(controladorDSP.getInterfaceInicial().getInterfaceInicial().getSituacaoPatioExibida().getDtInicio())) {
               if (baliza.getNumero().equals(interfaceEditaDadosMaquina.getPosicaoNroBaliza())) {
                  maquinaDoPatioEditada.setPosicao(baliza);
                  break;
               }
            }
         }
         else {
            for (Baliza baliza : maquinaDoPatioEditada.getCorreia().getPatioSuperior().getListaDeBalizas(controladorDSP.getInterfaceInicial().getInterfaceInicial().getSituacaoPatioExibida().getDtInicio())) {
               if (baliza.getNumero().equals(interfaceEditaDadosMaquina.getPosicaoNroBaliza())) {
                  maquinaDoPatioEditada.setPosicao(baliza);
                  break;
               }
            }
         }
         for (InterfaceBaliza interfaceBaliza : interfaceMaquinaDoPatio.getPosicao().getInterfacePatio().getListaDeBalizas()) {
            if (interfaceBaliza.getBalizaVisualizada().getNumero().equals(maquinaDoPatioEditada.getPosicao().getNumero())) {
               interfaceMaquinaDoPatio.setPosicao(interfaceBaliza);
               interfaceMaquinaDoPatio.getPosicao().setBalizaVisualizada(maquinaDoPatioEditada.getPosicao());
               break;
            }
         }

         // seta a taxa de operacao da maquina
         maquinaDoPatioEditada.setTaxaDeOperacaoNominal(interfaceEditaDadosMaquina.getTaxaOperacao());

         //velocidade de deslocamento balizas por hora
         maquinaDoPatioEditada.setVelocidadeDeslocamento(interfaceEditaDadosMaquina.getVelocidadeDeslocamento());
         controladorDSP.getInterfaceInicial().getInterfaceInicial().montaInterfaceDSP();
      }
   }

   /**
    * Solicita a edi o da m�q na para atualiza o do empi amento
    */
   public void solicitaAtualizacaoRecuperacao(MaquinaDoPatio maquinaDoPatioEditada, InterfaceMaquinaDoPatio interfaceMaquinaDoPatio) throws OperacaoCanceladaPeloUsuarioException, ErroSistemicoException
   {

      InterfaceAtualizacaoRecuperacao interfaceAtualizacaoRecuperacao = new InterfaceAtualizacaoRecuperacao(controladorDSP.getInterfaceDSP().getInterfaceInicial(), true, controladorDSP, maquinaDoPatioEditada, interfaceMaquinaDoPatio);
      interfaceAtualizacaoRecuperacao.setLocationRelativeTo(null);
      interfaceAtualizacaoRecuperacao.setVisible(Boolean.TRUE);

      controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();

      // A operacao de atualizacao da Recuperacao
      if (interfaceAtualizacaoRecuperacao.getOperacaoCanceladaPeloUsuario()) {
         controladorDSP.getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida().setStatusEdicao(Boolean.FALSE);
         throw new OperacaoCanceladaPeloUsuarioException(PropertiesUtil.buscarPropriedade("exception.operacao.cancelada.pelo.usuario"));
      }

      controladorDSP.getInterfaceInicial().getInterfaceInicial().atualizarDSP();
      //controladorDSP.getInterfaceInicial().getInterfaceInicial().montaPatio();
   }

   /**
    * Solicita a edi o da m�qui  para atualiza o do empilh ento
    */
   public void solicitaAtualizacaoRecuperacaoUsina(Usina usinaEditada, InterfaceUsina interfaceUsina) throws OperacaoCanceladaPeloUsuarioException, ErroSistemicoException
   {

      InterfaceAtualizacaoUsinaRecuperacao interfaceAtualizacaoUsinaRecuperacao = new InterfaceAtualizacaoUsinaRecuperacao(controladorDSP.getInterfaceDSP().getInterfaceInicial(), true, controladorDSP, usinaEditada,null, interfaceUsina,null);
      interfaceAtualizacaoUsinaRecuperacao.setLocationRelativeTo(null);
      interfaceAtualizacaoUsinaRecuperacao.setVisible(Boolean.TRUE);

      controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();

      // A operacao de atualizacao da Recuperacao
      if (interfaceAtualizacaoUsinaRecuperacao.getOperacaoCanceladaPeloUsuario()) {
         controladorDSP.getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida().setStatusEdicao(Boolean.FALSE);
         throw new OperacaoCanceladaPeloUsuarioException(PropertiesUtil.buscarPropriedade("exception.operacao.cancelada.pelo.usuario"));
      }

      controladorDSP.getInterfaceInicial().getInterfaceInicial().atualizarDSP();
      //controladorDSP.getInterfaceInicial().getInterfaceInicial().montaPatio();
   }

   /**
    * Solicita a edi o da m�qui  para atualiza o do empilh ento
    */
   public void solicitaAtualizacaoRecuperacaoFiltragem(Filtragem filtragem,Usina usinaEditada, InterfaceUsina interfaceUsina,InterfaceFiltragem interfaceFiltragem) throws OperacaoCanceladaPeloUsuarioException, ErroSistemicoException
   {

      InterfaceAtualizacaoUsinaRecuperacao interfaceAtualizacaoUsinaRecuperacao = new InterfaceAtualizacaoUsinaRecuperacao(controladorDSP.getInterfaceDSP().getInterfaceInicial(), true, controladorDSP, null,filtragem, interfaceUsina,interfaceFiltragem);
      interfaceAtualizacaoUsinaRecuperacao.setLocationRelativeTo(null);
      interfaceAtualizacaoUsinaRecuperacao.setVisible(Boolean.TRUE);

      controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();

      // A operacao de atualizacao da Recuperacao
      if (interfaceAtualizacaoUsinaRecuperacao.getOperacaoCanceladaPeloUsuario()) {
         controladorDSP.getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida().setStatusEdicao(Boolean.FALSE);
         throw new OperacaoCanceladaPeloUsuarioException(PropertiesUtil.buscarPropriedade("exception.operacao.cancelada.pelo.usuario"));
      }

      controladorDSP.getInterfaceInicial().getInterfaceInicial().atualizarDSP();
      //controladorDSP.getInterfaceInicial().getInterfaceInicial().montaPatio();
   }
   
   
   /**
    * remove objeto anotacao
    * @param interfaceAnotacao
    */
   public void removerAnotacao(InterfaceAnotacao interfaceAnotacao)
   {
      JLabel pergunta = new JLabel(PropertiesUtil.getMessage("mensagem.option.pane.confirma.exclusao.anotacao"));
      pergunta.setFont(new Font("Tahoma", Font.PLAIN, 11));
      int confirm = JOptionPane.showOptionDialog(
              controladorDSP.getInterfaceDSP().getInterfaceInicial(),
              pergunta,
              PropertiesUtil.getMessage("mensagem.option.pane.alerta"),
              JOptionPane.YES_OPTION,
              JOptionPane.INFORMATION_MESSAGE,
              null,
              null,
              null);

      if (confirm == JOptionPane.YES_OPTION) {
         //seta o patio da anotacao para null
//            interfaceAnotacao.getAnotacaoVisualizada().setPatio(null);
         //remove a Anotacao da lista de anotacoes da Plantas
         controladorDSP.getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida().getPlanta().getListaDeAnotacoes().remove(interfaceAnotacao.getAnotacaoVisualizada());
         PlantaDAO dao = new PlantaDAO();
         dao.salvar(controladorDSP.getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida().getPlanta());
         controladorDSP.getInterfaceDSP().getInterfaceInicial().getInterfaceComandos().finalizarEdicoes();
      }
   }

   /**
    *
    * @param interfacePatio
    */
   public void editarInserirAnotacao(InterfaceAnotacao interfaceAnotacao, int posicaoX, int posicaoY) throws OperacaoCanceladaPeloUsuarioException
   {
      //TODO BRUNO SA9675
      final InterfaceEditarInserirAnotacao interfaceEditarInserirAnotacao;
      if (interfaceAnotacao == null) {
         interfaceEditarInserirAnotacao = new InterfaceEditarInserirAnotacao(controladorDSP, posicaoX, posicaoY)
         {

            @Override
            public void fecharJanela()
            {
               controladorDSP.getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida().setStatusEdicao(Boolean.FALSE);
               this.setOperacaoCanceladaPeloUsuario(Boolean.TRUE);
            }
         };
      }
      else {
         interfaceEditarInserirAnotacao = new InterfaceEditarInserirAnotacao(interfaceAnotacao)
         {

            @Override
            public void fecharJanela()
            {
               controladorDSP.getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida().setStatusEdicao(Boolean.FALSE);
               this.setOperacaoCanceladaPeloUsuario(Boolean.TRUE);
            }
         };
      }

      final JDialog dialogInfomacaoEdicaoAnotacao = new JDialog(controladorDSP.getInterfaceDSP().getInterfaceInicial(), true);
      dialogInfomacaoEdicaoAnotacao.addWindowListener(new java.awt.event.WindowAdapter()
      {

         @Override
         public void windowClosing(java.awt.event.WindowEvent evt)
         {
            controladorDSP.getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida().setStatusEdicao(Boolean.FALSE);
            interfaceEditarInserirAnotacao.setOperacaoCanceladaPeloUsuario(Boolean.TRUE);
         }
      });

      dialogInfomacaoEdicaoAnotacao.setLayout(new BorderLayout());
      dialogInfomacaoEdicaoAnotacao.setTitle(PropertiesUtil.getMessage("titulo.mensagem.edicao.dados.anotacao"));
      dialogInfomacaoEdicaoAnotacao.getContentPane().add(interfaceEditarInserirAnotacao);
      dialogInfomacaoEdicaoAnotacao.pack();
      dialogInfomacaoEdicaoAnotacao.setLocationRelativeTo(null);
      dialogInfomacaoEdicaoAnotacao.setVisible(true);

      //verifica se a operacao n?o foi cancelada pelo usuario
      if (interfaceEditarInserirAnotacao.getOperacaoCanceladaPeloUsuario()) {
         interfaceEditarInserirAnotacao.setVisible(false);
         controladorDSP.getInterfaceInicial().getInterfaceComandos().cancelarModoOperacao();
         throw new OperacaoCanceladaPeloUsuarioException(PropertiesUtil.buscarPropriedade("exception.operacao.cancelada.pelo.usuario"));
      }
   }

   /**
    * Solicita o retorno de pellet feed do patio para filtragem
    */
   public void solicitarRetornoPelletFeed(InterfaceMaquinaDoPatio interfaceMaquinaDoPatio) throws OperacaoCanceladaPeloUsuarioException, ErroSistemicoException, PilhaPelletFeedNaoEncontradaException
   {

      InterfaceAtualizacaoRetornoPelletFeed interfaceAtualizacaoRetornoPelletFeed = new InterfaceAtualizacaoRetornoPelletFeed(controladorDSP.getInterfaceDSP().getInterfaceInicial(), true, controladorDSP, interfaceMaquinaDoPatio);
      interfaceAtualizacaoRetornoPelletFeed.setLocationRelativeTo(null);
      interfaceAtualizacaoRetornoPelletFeed.setVisible(Boolean.TRUE);

      controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();

      // A operacao de atualizacao da Recuperacao
      if (interfaceAtualizacaoRetornoPelletFeed.getOperacaoCanceladaPeloUsuario()) {
         controladorDSP.getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida().setStatusEdicao(Boolean.FALSE);
         throw new OperacaoCanceladaPeloUsuarioException(PropertiesUtil.buscarPropriedade("exception.operacao.cancelada.pelo.usuario"));
      }

      controladorDSP.getInterfaceInicial().getInterfaceInicial().atualizarDSP();
      //controladorDSP.getInterfaceInicial().getInterfaceInicial().montaPatio();
   }

   /**
    * Solicita a edi o da m�quina ara atualiza o do empilham to
    */
   public void solicitaAtualizacaoPilhaEmergencia(Usina usinaEditada, InterfaceUsina interfaceUsina) throws OperacaoCanceladaPeloUsuarioException, ErroSistemicoException
   {

      InterfaceAtualizacaoPilhaEmergencia interfaceAtualizacaoPilhaEmergencia = new InterfaceAtualizacaoPilhaEmergencia(controladorDSP.getInterfaceDSP().getInterfaceInicial(), true, controladorDSP, usinaEditada, interfaceUsina);
      interfaceAtualizacaoPilhaEmergencia.setLocationRelativeTo(null);
      interfaceAtualizacaoPilhaEmergencia.setVisible(Boolean.TRUE);

      controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();

      // A operacao de atualizacao da Recuperacao
      if (interfaceAtualizacaoPilhaEmergencia.getOperacaoCanceladaPeloUsuario()) {
         controladorDSP.getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida().setStatusEdicao(Boolean.FALSE);
         throw new OperacaoCanceladaPeloUsuarioException(PropertiesUtil.buscarPropriedade("exception.operacao.cancelada.pelo.usuario"));
      }
      controladorDSP.getInterfaceInicial().getInterfaceInicial().atualizarDSP();
      //controladorDSP.getInterfaceInicial().getInterfaceInicial().montaPatio();
   }

   
   public void solicitaMovimentacaoPilhaEmergencia(MaquinaDoPatio maquinaDoPatio, InterfaceMaquinaDoPatio interfaceMaquina) throws OperacaoCanceladaPeloUsuarioException, ErroSistemicoException
   {
       InterfaceMovimentacao interfaceMovimentacao = new InterfaceMovimentacao(controladorDSP.getInterfaceDSP().getInterfaceInicial(), true, maquinaDoPatio, interfaceMaquina, controladorDSP, null,TipoAtividadeEnum.MOVIMENTAR_PILHA_EMERGENCIA,controladorDSP.getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida().getDtInicio());
       interfaceMovimentacao.setLocationRelativeTo(null);
       interfaceMovimentacao.setVisible(Boolean.TRUE);

       // A operacao de atualizacao da Recuperacao
       if (interfaceMovimentacao.getOperacaoCanceladaPeloUsuario()) {
          controladorDSP.getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida().setStatusEdicao(Boolean.FALSE);
          throw new OperacaoCanceladaPeloUsuarioException(PropertiesUtil.buscarPropriedade("exception.operacao.cancelada.pelo.usuario"));
       }

       controladorDSP.getInterfaceInicial().getInterfaceInicial().atualizarDSP();

   }

   public void solicitaMovimentacaoPilhaPellet(MaquinaDoPatio maquinaDoPatio, InterfaceMaquinaDoPatio interfaceMaquina, TipoDeProdutoEnum tipoProduto) throws OperacaoCanceladaPeloUsuarioException, ErroSistemicoException
   {
      InterfaceMovimentacao interfaceMovimentacao = new InterfaceMovimentacao(controladorDSP.getInterfaceDSP().getInterfaceInicial(), true, maquinaDoPatio, interfaceMaquina, controladorDSP, tipoProduto,TipoAtividadeEnum.MOVIMENTAR_PILHA_PELLET_FEED,controladorDSP.getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida().getDtInicio());
      interfaceMovimentacao.setLocationRelativeTo(null);
      interfaceMovimentacao.setVisible(Boolean.TRUE);

      // A operacao de atualizacao da Recuperacao
      if (interfaceMovimentacao.getOperacaoCanceladaPeloUsuario()) {
         controladorDSP.getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida().setStatusEdicao(Boolean.FALSE);
         throw new OperacaoCanceladaPeloUsuarioException(PropertiesUtil.buscarPropriedade("exception.operacao.cancelada.pelo.usuario"));
      }

      controladorDSP.getInterfaceInicial().getInterfaceInicial().atualizarDSP();
      //controladorDSP.getInterfaceInicial().getInterfaceInicial().montaPatio();
   }

   public void solicitaMovimentacaoPilhaPSM(MaquinaDoPatio maquinaDoPatio, InterfaceMaquinaDoPatio interfaceMaquina, TipoDeProdutoEnum tipoProduto) throws OperacaoCanceladaPeloUsuarioException, ErroSistemicoException
   {
      InterfaceMovimentacao interfaceMovimentacao = new InterfaceMovimentacao(controladorDSP.getInterfaceDSP().getInterfaceInicial(), true, maquinaDoPatio, interfaceMaquina, controladorDSP, tipoProduto,TipoAtividadeEnum.MOVIMENTAR_PILHA_PSM,controladorDSP.getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida().getDtInicio());
      interfaceMovimentacao.setLocationRelativeTo(null);
      interfaceMovimentacao.setVisible(Boolean.TRUE);

      // A operacao de atualizacao da Recuperacao
      if (interfaceMovimentacao.getOperacaoCanceladaPeloUsuario()) {
         controladorDSP.getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida().setStatusEdicao(Boolean.FALSE);
         throw new OperacaoCanceladaPeloUsuarioException(PropertiesUtil.buscarPropriedade("exception.operacao.cancelada.pelo.usuario"));
      }

      controladorDSP.getInterfaceInicial().getInterfaceInicial().atualizarDSP();
      //controladorDSP.getInterfaceInicial().getInterfaceInicial().montaPatio();
   }

   
   
   public void solicitaUnificarPilhas(List<InterfaceBaliza> listaBalizasSelecionadas) throws OperacaoCanceladaPeloUsuarioException, ErroSistemicoException
   {

      InterfaceUnificacaoDePilha interfaceUnificarPilha = new InterfaceUnificacaoDePilha(controladorDSP.getInterfaceDSP().getInterfaceInicial(), true, listaBalizasSelecionadas, controladorDSP);
      interfaceUnificarPilha.setLocationRelativeTo(null);
      interfaceUnificarPilha.setVisible(Boolean.TRUE);

      // A operacao de atualizacao da Recuperacao
      if (interfaceUnificarPilha.getOperacaoCanceladaPeloUsuario()) {
         controladorDSP.getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida().setStatusEdicao(Boolean.FALSE);
         throw new OperacaoCanceladaPeloUsuarioException(PropertiesUtil.buscarPropriedade("exception.operacao.cancelada.pelo.usuario"));
      }

      controladorDSP.getInterfaceInicial().getInterfaceInicial().montaInterfaceDSP();
//      controladorDSP.getInterfaceInicial().getInterfaceInicial().atualizarDSP();

   }


   public void solicitarInterdicaoPier(InterfacePier interfacePier) throws OperacaoCanceladaPeloUsuarioException {
    // TODO Auto-generated method stub
       InterfaceInterdicoes interfaceInterdicaoPier = new InterfaceInterdicoes(controladorDSP.getInterfaceInicial().getInterfaceInicial(), true, controladorDSP, interfacePier);
       interfaceInterdicaoPier.setLocationRelativeTo(null);
       interfaceInterdicaoPier.setVisible(true);

       // A operacao de atualizacao da Recuperacao
       if (interfaceInterdicaoPier.getOperacaoCanceladaPeloUsuario()) {
          throw new OperacaoCanceladaPeloUsuarioException(PropertiesUtil.buscarPropriedade("exception.operacao.cancelada.pelo.usuario"));
       }
       
       controladorDSP.getInterfaceInicial().getPlanejamento().ativarNecessidadeDeConsolidacao();
       controladorDSP.getInterfaceInicial().getInterfaceInicial().atualizarDSP();
   }

   public void solicitarInterdicaoBaliza(InterfacePatio interfacePatio) throws OperacaoCanceladaPeloUsuarioException {
       InterfaceInterdicoesBaliza interfaceInterdicaoPier = new InterfaceInterdicoesBaliza(controladorDSP.getInterfaceInicial().getInterfaceInicial(), true, controladorDSP, interfacePatio);
       interfaceInterdicaoPier.setLocationRelativeTo(null);
       interfaceInterdicaoPier.setVisible(true);

       // A operacao de atualizacao da Recuperacao
       if (interfaceInterdicaoPier.getOperacaoCanceladaPeloUsuario()) {
          throw new OperacaoCanceladaPeloUsuarioException(PropertiesUtil.buscarPropriedade("exception.operacao.cancelada.pelo.usuario"));
       }
       
       controladorDSP.getInterfaceInicial().getPlanejamento().ativarNecessidadeDeConsolidacao();
       controladorDSP.getInterfaceInicial().getInterfaceInicial().atualizarDSP();
    
   }
}