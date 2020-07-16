package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.hdntec.gestao.cliente.interfaceDeDialogo.ModoDeOperacaoEnum;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.estruturasAuxiliares.ProdutoMovimentacao;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.estruturasAuxiliares.TotalMovimentacao;
import com.hdntec.gestao.cliente.interfaceDeDialogo.representacaoGrafica.RepresentacaoGrafica;
import com.hdntec.gestao.cliente.interfaceProgramacao.InterfaceInicializacao;
import com.hdntec.gestao.cliente.interfaceProgramacao.InterfaceSelecionavelParaAtividade;
import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.cliente.util.numeros.DSSStockyardFuncoesNumeros;
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.entity.LugarEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.enums.TipoMaquinaEnum;
import com.hdntec.gestao.domain.planta.entity.status.Correia;
import com.hdntec.gestao.domain.planta.entity.status.MaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Patio;
import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.domain.produto.enums.TipoDeProdutoEnum;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.EstruturaEmManutencaoException;
import com.hdntec.gestao.exceptions.InverterPosicaoMaquinaException;
import com.hdntec.gestao.exceptions.MaquinaEstaIndisponivelException;
import com.hdntec.gestao.exceptions.OperacaoCanceladaPeloUsuarioException;
import com.hdntec.gestao.exceptions.PilhaPelletFeedNaoEncontradaException;
import com.hdntec.gestao.exceptions.SelecaoObjetoModoEmpilhamentoException;
import com.hdntec.gestao.exceptions.SelecaoObjetoModoRecuperacaoException;
import com.hdntec.gestao.exceptions.ValidacaoCampoException;
import com.hdntec.gestao.util.PropertiesUtil;

/**
 * Interface grafica que representa uma {@link MaquinaDoPatio}.
 * @author andre
 *
 */
public class InterfaceMaquinaDoPatio extends RepresentacaoGrafica implements InterfaceInicializacao, InterfaceSelecionavelParaAtividade
{

   /**
    * Serialização
    */
   private static final long serialVersionUID = 3389841209591258075L;

   /** a {@link MaquinaDoPatio} visualizada nesta interface */
   private MaquinaDoPatio maquinaDoPatioVisualizada;

   /** a interface gráfica que representa a posicao onde a máquina se encontra */
   private InterfaceBaliza posicao;

   /** a interface gráfica que representa o {@link Patio} no qual esta máquina se encontra */
   private InterfacePatio patio;

   /** a interface gráfica da {@link Correia} a qual esta máquina pertence */
   private InterfaceCorreia correia;

   /** acesso às operações do subsistema de interface gráfica DSP */
   private ControladorDSP controladorDSP;

   /**flag que indica se a máquina está ou não selecionada */
   private Boolean maquinaSelecionada;

   /** a interface das mensagens exibidas pela interface baliza */
   private InterfaceMensagem interfaceMensagem;

   /** o comprimento da imagem da máquina*/
   private int comprimentoMaquina;

   /** a largura da imagem da máquina*/
   private int larguraMaquina;

   /** o complemento da altura ou o tamanho da borda da imagem */
   private int complementoAlturaTamanhoBorda;

   /** flag auxiliar para evitar que o evento de seleção se repita */
   private boolean selecaoDeMaquinaAtivado = true;

   /** Constante referente ao tipo "iniciar" da atualizacao de empilhamento */
   public static final int ATUALIZACAO_EMPILHAMENTO_TIPO_INICAR = 1;

   /** Constante referente ao tipo "mudar baliza" da atualizacao de empilhamento */
   public static final int ATUALIZACAO_EMPILHAMENTO_TIPO_MUDAR_BALIZA = 2;

   /** Constante referente ao tipo "parar" da atualizacao de empilhamento */
   public static final int ATUALIZACAO_EMPILHAMENTO_TIPO_PARAR = 3;

   /** o tipo de atualizacao de empilhamento executado */
   private Integer tipoAtualizacaoEmpilhamentoExcutado;

   //menus
   private JMenuItem mnuEditaMaquina;
   private JMenuItem mnuInverterMaquina;
   private JMenuItem mnuAtualizacaoMaquinaEmpilhamento;
   private JMenuItem mnuMovimentarMaquina;
   private JMenuItem mnuListaManutencoes;
   private JMenuItem menuFinalizarEmpilhamento;
   private JMenuItem mnuAtualizacaoRecuperacao;
   private JMenuItem mnuMovimentarEmergencia;


   @Override
   public void defineDimensoesFixas()
   {
      if (maquinaDoPatioVisualizada.getTipoDaMaquina() == TipoMaquinaEnum.RECUPERADORA)
      {
         comprimentoMaquina = Integer.parseInt(PropertiesUtil.buscarPropriedade("dimensao.recuperadora.comprimento").trim());
         larguraMaquina = Integer.parseInt(PropertiesUtil.buscarPropriedade("dimensao.recuperadora.largura").trim());
         complementoAlturaTamanhoBorda = Integer.parseInt(PropertiesUtil.buscarPropriedade("dimensao.recuperadora.tamanho.borda").trim());
      }
      else if (maquinaDoPatioVisualizada.getTipoDaMaquina() == TipoMaquinaEnum.PA_CARREGADEIRA)
      {
         comprimentoMaquina = Integer.parseInt(PropertiesUtil.buscarPropriedade("dimensao.paCarregadeira.comprimento").trim());
         larguraMaquina = Integer.parseInt(PropertiesUtil.buscarPropriedade("dimensao.paCarregadeira.largura").trim());
         complementoAlturaTamanhoBorda = Integer.parseInt(PropertiesUtil.buscarPropriedade("dimensao.paCarregadeira.altura.complemento").trim());
      }
      else if (maquinaDoPatioVisualizada.getTipoDaMaquina() == TipoMaquinaEnum.EMPILHADEIRA)
      {
         comprimentoMaquina = Integer.parseInt(PropertiesUtil.buscarPropriedade("dimensao.empilhadeira.comprimento").trim());
         larguraMaquina = Integer.parseInt(PropertiesUtil.buscarPropriedade("dimensao.empilhadeira.largura").trim());
         complementoAlturaTamanhoBorda = Integer.parseInt(PropertiesUtil.buscarPropriedade("dimensao.empilhadeira.tamanho.borda").trim());
      }
      else
      {
         comprimentoMaquina = Integer.parseInt(PropertiesUtil.buscarPropriedade("dimensao.empilhadeirarecuperadora.comprimento").trim());
         larguraMaquina = Integer.parseInt(PropertiesUtil.buscarPropriedade("dimensao.empilhadeirarecuperadora.largura").trim());
         complementoAlturaTamanhoBorda = Integer.parseInt(PropertiesUtil.buscarPropriedade("dimensao.empilhadeirarecuperadora.tamanho.borda").trim());
      }

   }

   @Override
   public void inicializaInterface()
   {
      this.setToolTipText(maquinaDoPatioVisualizada.getNomeMaquina());
      setSelecionada(Boolean.FALSE);
      if (maquinaDoPatioVisualizada.getTipoDaMaquina() != TipoMaquinaEnum.PA_CARREGADEIRA) //se a maquina nao for uma pa-carregadeira, precisamos definir se ela esta virada para cima ou para baixo
      {
         defineImagemMaquina();
      }
      else
      {
         desenhaPaCarregadeira();
      }
      
      defineDimensoesFixas();
      calculaPosicaoMaquina();
      defineEventosParaMaquina();
      criaPopMenuMaquinaDoPatio();
      desabilitaMenusPermissaoUsuario();
      StringBuffer toolTip = new StringBuffer();
      toolTip.append("<html>");
      toolTip.append(PropertiesUtil.getMessage("label.maquina")).append(maquinaDoPatioVisualizada.getNomeMaquina()).append("<br>");
      toolTip.append(PropertiesUtil.getMessage("label.estado")).append(maquinaDoPatioVisualizada.getEstado()).append("<br>");
      toolTip.append(PropertiesUtil.getMessage("label.taxa.de.operacao")).append(DSSStockyardFuncoesNumeros.getQtdeFormatada(maquinaDoPatioVisualizada.getTaxaDeOperacaoNominal(), 2)).append(PropertiesUtil.getMessage("label.tonelada.hora")).append("<br>");
      toolTip.append(PropertiesUtil.getMessage("label.lanca.giratoria")).append((maquinaDoPatioVisualizada.getGiraLanca() ? PropertiesUtil.getMessage("mensagem.condicional.sim") : PropertiesUtil.getMessage("mensagem.condicional.nao"))).append("<br>");
      toolTip.append(PropertiesUtil.getMessage("label.velocidade.deslocamento")).append(maquinaDoPatioVisualizada.getVelocidadeDeslocamento()).append(PropertiesUtil.getMessage("label.baliza.hora"));
       
      Atividade empilhamento = maquinaDoPatioVisualizada.getMetaMaquina().existeEmpilhamentoPendente();
      
      if (empilhamento != null) {
          toolTip.append("<br>").append(PropertiesUtil.getMessage("label.atividade")).append(empilhamento.getTipoAtividade().toString());
      }
      Atividade recuperacao = maquinaDoPatioVisualizada.getMetaMaquina().existeRecuperacaoPendente();
      if (recuperacao != null) {
          toolTip.append("<br>").append(PropertiesUtil.getMessage("label.atividade")).append(recuperacao.getTipoAtividade().toString());
      }
      Atividade movimentarPsm = maquinaDoPatioVisualizada.getMetaMaquina().existeMovimentacaoPSMPendente();
      if (movimentarPsm != null) {
          toolTip.append("<br>").append(PropertiesUtil.getMessage("label.atividade")).append(movimentarPsm.getTipoAtividade().toString());
      }
      Atividade movimentarEmergencia = maquinaDoPatioVisualizada.getMetaMaquina().existeMovimentacaoEmergenciaPendente();
      if (movimentarEmergencia != null) {
          toolTip.append("<br>").append(PropertiesUtil.getMessage("label.atividade")).append(movimentarEmergencia.getTipoAtividade().toString());
      }
      
      this.setToolTipText(toolTip.toString());

      this.setOpaque(false);
    
   }

   /** Cria as opcoes de popMenu para uma maquina do patio */
   private void criaPopMenuMaquinaDoPatio()
   {
      JPopupMenu popMnuMaquina = new JPopupMenu();

      if (maquinaDoPatioVisualizada.getEstado().equals(EstadoMaquinaEnum.OCIOSA) || maquinaDoPatioVisualizada.getAtividade() == null)
      {
         mnuEditaMaquina = new JMenuItem();
         mnuEditaMaquina.setText(PropertiesUtil.getMessage("menu.titulo.edita.maquina"));
         mnuEditaMaquina.setToolTipText(PropertiesUtil.getMessage("tooltip.edita.informacao.maquina.patio"));
         
         // habilita a edicao apenas quando é a ultima situacao do patio
         if (controladorDSP != null && !controladorDSP.getInterfaceInicial().estaNaUltimaSituacao())
        	 mnuEditaMaquina.setEnabled(false);
         else
        	 mnuEditaMaquina.setEnabled(true);
         
         mnuEditaMaquina.addActionListener(new java.awt.event.ActionListener()
         {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
               editarMaquinaDoPatioActionPerformed(evt);
            }
         });
         popMnuMaquina.add(mnuEditaMaquina);
      }

      if (maquinaDoPatioVisualizada.getGiraLanca() && maquinaDoPatioVisualizada.getEstado().equals(EstadoMaquinaEnum.OCIOSA))
      {
//         JMenuItem
         mnuInverterMaquina = new JMenuItem();
         mnuInverterMaquina.setText(PropertiesUtil.getMessage("menu.titulo.Inverter.maquina"));
         mnuInverterMaquina.setToolTipText(PropertiesUtil.getMessage("tootip.inverter.posicao.maquina.patio"));
         
         // habilita a edicao apenas quando Ã© a ultima situacao do patio
         if (controladorDSP != null && !controladorDSP.getInterfaceInicial().estaNaUltimaSituacao())
        	 mnuInverterMaquina.setEnabled(false);
         else
        	 mnuInverterMaquina.setEnabled(maquinaDoPatioVisualizada.getGiraLanca());
         
         mnuInverterMaquina.addActionListener(new java.awt.event.ActionListener()
         {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
               inverterMaquinaDoPatioActionPerformed(evt);
            }
         });

         popMnuMaquina.add(mnuInverterMaquina);
      }

      if (maquinaDoPatioVisualizada.getTipoDaMaquina().equals(TipoMaquinaEnum.EMPILHADEIRA) || maquinaDoPatioVisualizada.getTipoDaMaquina().equals(TipoMaquinaEnum.EMPILHADEIRA_RECUPERADORA))
      {
          if (exibeMenuAtualizacaoEmpilhamento()) {
              // cria um Item de Manu para atualizacao de maquina
//              JMenuItem
              mnuAtualizacaoMaquinaEmpilhamento = new JMenuItem();
              mnuAtualizacaoMaquinaEmpilhamento.setText(PropertiesUtil.getMessage("menu.Atualizacao.empilhamento"));
              mnuAtualizacaoMaquinaEmpilhamento.setToolTipText(PropertiesUtil.getMessage("tooltip.executa.atualizacao.informacao.maquina.empilhamento"));
              mnuAtualizacaoMaquinaEmpilhamento.addActionListener(new java.awt.event.ActionListener() {

                  @Override
                  public void actionPerformed(java.awt.event.ActionEvent evt) {
                      atualizarEmpilhamentoActionPerformed(evt);
                  }
              });

              // adiciona o menu de atualizacao dentro do popmenu principal da maquina
              popMnuMaquina.add(mnuAtualizacaoMaquinaEmpilhamento);
          }
      }

      // adiciona o popMenu na maquina
      if (maquinaDoPatioVisualizada.getTipoDaMaquina().equals(TipoMaquinaEnum.PA_CARREGADEIRA))
      {
    	  // verifica se a maquina ja esta realizando outras atividades
          Atividade movimentarPsm = maquinaDoPatioVisualizada.getMetaMaquina().existeMovimentacaoPSMPendente();
          Atividade movimentarEmergencia = maquinaDoPatioVisualizada.getMetaMaquina().existeMovimentacaoEmergenciaPendente();
          Atividade movimentarPellet = maquinaDoPatioVisualizada.getMetaMaquina().existeMovimentacaoPelletPendente();
          
          boolean estaNaUltimaSituacao = true;
          if (controladorDSP != null && !controladorDSP.getInterfaceInicial().estaNaUltimaSituacao())
        	  estaNaUltimaSituacao = false;
          
//         JMenuItem
         mnuMovimentarMaquina = new JMenuItem();
         mnuMovimentarMaquina.setText(PropertiesUtil.getMessage("menu.movimentar.pellet.screening"));
         mnuMovimentarMaquina.setToolTipText(PropertiesUtil.getMessage("tooltip.movimentacao.pellet.screenig"));
         if (movimentarPsm != null || movimentarEmergencia != null || !estaNaUltimaSituacao) 
        	 mnuMovimentarMaquina.setEnabled(false);
         mnuMovimentarMaquina.addActionListener(new java.awt.event.ActionListener()
         {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
               movimentarPelletScrenning(evt, TipoDeProdutoEnum.PELLET_SCREENING);
            }
         });

         popMnuMaquina.add(mnuMovimentarMaquina);

         mnuMovimentarMaquina = new JMenuItem();
         mnuMovimentarMaquina.setText(PropertiesUtil.getMessage("menu.movimentar.psm"));
         mnuMovimentarMaquina.setToolTipText(PropertiesUtil.getMessage("tooltip.movimentacao.psm"));
         if (movimentarPellet != null || movimentarEmergencia != null || !estaNaUltimaSituacao)
        	 mnuMovimentarMaquina.setEnabled(false);
         mnuMovimentarMaquina.addActionListener(new java.awt.event.ActionListener()
         {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                movimentarPSM(evt, TipoDeProdutoEnum.PELLET_PSM);

            }
         });

         popMnuMaquina.add(mnuMovimentarMaquina);

         mnuMovimentarMaquina = new JMenuItem();
         mnuMovimentarMaquina.setText(PropertiesUtil.getMessage("menu.retornar.pellet.feed"));
         mnuMovimentarMaquina.setToolTipText(PropertiesUtil.getMessage("tooltip.retornar.pellet.feed"));
         if (movimentarPsm != null || movimentarEmergencia != null || movimentarPellet != null || !estaNaUltimaSituacao)
        	 mnuMovimentarMaquina.setEnabled(false);
         mnuMovimentarMaquina.addActionListener(new java.awt.event.ActionListener()
         {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
               retornarPelletFeed(evt);
            }
         });
         popMnuMaquina.add(mnuMovimentarMaquina);

//            JMenuItem
            mnuMovimentarEmergencia = new JMenuItem();
            mnuMovimentarEmergencia.setText(PropertiesUtil.getMessage("menu.movimentar.pilha.emergencia"));
            mnuMovimentarEmergencia.setToolTipText(PropertiesUtil.getMessage("tooltip.movimentacao.pilha.emergencia"));
            if (movimentarPellet != null || movimentarPsm != null || !estaNaUltimaSituacao)
                mnuMovimentarEmergencia.setEnabled(false);
            mnuMovimentarEmergencia.addActionListener(new java.awt.event.ActionListener()
            {
               @Override
               public void actionPerformed(java.awt.event.ActionEvent evt)
               {
                  movimentarPilhaEmergencia(evt);
               }
            });

           popMnuMaquina.add(mnuMovimentarEmergencia);
      }

     if (!maquinaDoPatioVisualizada.getTipoDaMaquina().equals(TipoMaquinaEnum.PA_CARREGADEIRA)) {
//         JMenuItem
         mnuListaManutencoes = new JMenuItem();
         mnuListaManutencoes.setText(PropertiesUtil.getMessage("mensagem.manutencao.popup"));
         mnuListaManutencoes.setToolTipText(PropertiesUtil.getMessage("mensagem.manutencao.popup"));
         mnuListaManutencoes.addActionListener(new java.awt.event.ActionListener() {

             @Override
             public void actionPerformed(
                     java.awt.event.ActionEvent evt) {
                 apresentaListaManutencoes(evt);
             }
         });

         popMnuMaquina.add(mnuListaManutencoes);
     }

      if (maquinaDoPatioVisualizada.getTipoDaMaquina().equals(TipoMaquinaEnum.EMPILHADEIRA) || maquinaDoPatioVisualizada.getTipoDaMaquina().equals(TipoMaquinaEnum.EMPILHADEIRA_RECUPERADORA))
      {
          if(exibeMenuFinalizarEmpilhamento()){
//              JMenuItem
              menuFinalizarEmpilhamento = new JMenuItem();
              menuFinalizarEmpilhamento.setText(PropertiesUtil.getMessage("menu.finalizar.empilhamento"));
              menuFinalizarEmpilhamento.setToolTipText("tooltip.finaliza.atualizacao.empilhamento");
              menuFinalizarEmpilhamento.addActionListener(new java.awt.event.ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        finalizaAtulizacaoEmpilhamento(evt);
                    }
                });
            popMnuMaquina.add(menuFinalizarEmpilhamento);
          }
      }


      if (maquinaDoPatioVisualizada.getTipoDaMaquina().equals(TipoMaquinaEnum.RECUPERADORA) || maquinaDoPatioVisualizada.getTipoDaMaquina().equals(TipoMaquinaEnum.EMPILHADEIRA_RECUPERADORA))
      {
          if (exibeMenuAtualizacaoRecuperacao()) {
              // cria um JMenu para atualizacao de maquina
//              JMenuItem
              mnuAtualizacaoRecuperacao = new JMenuItem();
              mnuAtualizacaoRecuperacao.setText(PropertiesUtil.getMessage("menu.atualizacao.recuperacao"));
              mnuAtualizacaoRecuperacao.setToolTipText(PropertiesUtil.getMessage("tooltip.executa.atualizacao.informacoes.maquina.recuperacao"));
              mnuAtualizacaoRecuperacao.addActionListener(new java.awt.event.ActionListener() {

                  @Override
                  public void actionPerformed(java.awt.event.ActionEvent evt) {
                      atualizarRecuperacaoActionPerformed(evt);
                  }
              });

              // adiciona o menu de atualizacao dentro do popmenu principal da maquina
              popMnuMaquina.add(mnuAtualizacaoRecuperacao);
          }
      }

      this.setComponentPopupMenu(popMnuMaquina);
      
   }

   private void defineEventosParaMaquina()
   {
      this.addMouseListener(new MouseAdapter()
      {

         @Override
         public void mouseClicked(MouseEvent evt)
         {
            interfaceMaquinaDoPatioMouseClicked(evt);
         }
      });
   }

   private void movimentarPelletScrenning(ActionEvent evt, TipoDeProdutoEnum tipoProduto)
   {
      InterfaceMaquinaDoPatio maquinaClicada = obterMaquinaClicada(evt);
      if (maquinaClicada.getMaquinaDoPatioVisualizada().equals(getMaquinaDoPatioVisualizada()))
      {
         try
         {
            selecionar(true);
            controladorDSP.movimentarPilhaPellet(maquinaDoPatioVisualizada, this, tipoProduto);
            controladorDSP.getInterfaceInicial().exibirUltimaSituacaoPatio();
            deselecionar();
         }
         catch (OperacaoCanceladaPeloUsuarioException ex)
         {
          deselecionar();
          controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
         }
         catch (ErroSistemicoException erroSis)
         {
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(erroSis.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
         }
      }
   }

   private void movimentarPSM(ActionEvent evt, TipoDeProdutoEnum tipoProduto)
   {
      InterfaceMaquinaDoPatio maquinaClicada = obterMaquinaClicada(evt);
      if (maquinaClicada.getMaquinaDoPatioVisualizada().equals(getMaquinaDoPatioVisualizada()))
      {
         try
         {
            selecionar(true);
            controladorDSP.movimentarPilhaPSM(maquinaDoPatioVisualizada, this, tipoProduto);
            controladorDSP.getInterfaceInicial().exibirUltimaSituacaoPatio();
            deselecionar();
         }
         catch (OperacaoCanceladaPeloUsuarioException ex)
         {
          deselecionar();
          controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
         }
         catch (ErroSistemicoException erroSis)
         {
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(erroSis.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
         }
      }
   }

   
   private InterfaceMaquinaDoPatio obterMaquinaClicada(ActionEvent evt)
   {
      JMenuItem mnuItem = (JMenuItem) evt.getSource();
      JPopupMenu popupMenu = (JPopupMenu) mnuItem.getParent();
      if (popupMenu.getInvoker() instanceof JMenu)
      {
         JMenu menuAtualizacao = (JMenu) popupMenu.getInvoker();
         popupMenu = (JPopupMenu) menuAtualizacao.getParent();
      }
      return (InterfaceMaquinaDoPatio) popupMenu.getInvoker();
   }

   /** Executa o evento do clique do mouse da maquina do patio */
   private void interfaceMaquinaDoPatioMouseClicked(MouseEvent evt)
   {
      selecaoDeMaquinaAtivado = true;
      try
      {
         // Não interessa qual a máquina, não é possível seleciona-la caso o modo de operação seja de consulta
         if (controladorDSP.obterModoOperacao().equals(ModoDeOperacaoEnum.OPERACAONULA))
         {
            //throw new SelecaoObjetoModoNuloException();
         }
         else
         {//verifica se a maquina selecionada e uma Pa Carregadeira
            if (maquinaDoPatioVisualizada.getTipoDaMaquina() == TipoMaquinaEnum.PA_CARREGADEIRA)
            {
               selecionaDeselecionaPaCarregadeira();
               selecaoDeMaquinaAtivado = false;
            }
            else{//caso contrario, procede com as validacoes para selecionar uma maquina
                 //Primeiramente, encontramos o pátio onde a máquina está e os pátios que fazem fronteira com a Correia dela ...
                 Patio patioLocalizacaoMaquina = posicao.getBalizaVisualizada().getPatio();
                 Patio patioInferiorCorreia = correia.getCorreiaVisualizada().getPatioInferior();
                 Patio patioSuperiorCorreia = correia.getCorreiaVisualizada().getPatioSuperior();
                 if (maquinaDoPatioVisualizada.getTipoDaMaquina() == TipoMaquinaEnum.RECUPERADORA) {
                     selecionaDeselecionaRecuperadora(patioLocalizacaoMaquina, patioInferiorCorreia, patioSuperiorCorreia);
                     selecaoDeMaquinaAtivado = false;
                 } else if (maquinaDoPatioVisualizada.getTipoDaMaquina() == TipoMaquinaEnum.EMPILHADEIRA) {
                     selecionaDeselecionaEmpilhadeira(patioLocalizacaoMaquina, patioInferiorCorreia, patioSuperiorCorreia);
                     selecaoDeMaquinaAtivado = false;
                 } else {
                     selecionaDeselecionaEmpilhadeiraRecuperadora(patioLocalizacaoMaquina, patioInferiorCorreia, patioSuperiorCorreia);
                     selecaoDeMaquinaAtivado = false;
                 }
             }
         }
      }
     /* catch (SelecaoObjetoModoNuloException e)
      {
          controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
         interfaceMensagem = new InterfaceMensagem();
         interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
         interfaceMensagem.setDescricaoMensagem(e.getMessage());
         controladorDSP.ativarMensagem(interfaceMensagem);
      }*/
      catch (SelecaoObjetoModoEmpilhamentoException e)
      {
          controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
         interfaceMensagem = new InterfaceMensagem();
         interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
         interfaceMensagem.setDescricaoMensagem(e.getMessage());
         controladorDSP.ativarMensagem(interfaceMensagem);
      }
      catch (SelecaoObjetoModoRecuperacaoException e)
      {
          controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
         interfaceMensagem = new InterfaceMensagem();
         interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
         interfaceMensagem.setDescricaoMensagem(e.getMessage());
         controladorDSP.ativarMensagem(interfaceMensagem);
      }
      catch (OperacaoCanceladaPeloUsuarioException e)
      {
          controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
         interfaceMensagem = new InterfaceMensagem();
         interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
         interfaceMensagem.setDescricaoMensagem(e.getMessage());
         controladorDSP.ativarMensagem(interfaceMensagem);
      }
      catch (ValidacaoCampoException e)
      {
          controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
         interfaceMensagem = new InterfaceMensagem();
         interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
         interfaceMensagem.setDescricaoMensagem(e.getMessage());
         controladorDSP.ativarMensagem(interfaceMensagem);
      }

   }

   /** Executa o evento de acao da acao do popmenu de edicao da maquina */
   private void editarMaquinaDoPatioActionPerformed(ActionEvent evt)
   {
      InterfaceMaquinaDoPatio maquinaClicada = obterMaquinaClicada(evt);
      if (maquinaClicada.getMaquinaDoPatioVisualizada().equals(getMaquinaDoPatioVisualizada()))
      {
         try
         {
            selecaoDeMaquinaAtivado = true;
            if (maquinaSelecionada)
            {
               deselecionar();
            }
            else
            {
               if (selecaoDeMaquinaAtivado != false)
               {
                  selecionar(false);
                  controladorDSP.editaDadosDeMaquina(maquinaDoPatioVisualizada, this);
                  deselecionar();
               }
            }
            repaint();
            controladorDSP.getInterfaceDSP().getInterfaceInicial().repaint();
         }
         catch (OperacaoCanceladaPeloUsuarioException ex)
         {
             controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
         }
         catch (ValidacaoCampoException ex)
         {
             controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
         }
      }
   }

   /** Atualizacao do empilhamento da maquina */
   private void atualizarEmpilhamentoActionPerformed(ActionEvent evt)
   {
      InterfaceMaquinaDoPatio maquinaClicada = obterMaquinaClicada(evt);
      if (maquinaClicada.getMaquinaDoPatioVisualizada().equals(getMaquinaDoPatioVisualizada()))
      {
         try
         {
            selecaoDeMaquinaAtivado = true;
            if (maquinaSelecionada)
            {
               deselecionar();
            }
            else
            {
               if (selecaoDeMaquinaAtivado != false)
               {
                   
                  selecionar(true);
                  /*
                  //TODO verificar a regra nesse caso quando é um empilhamento e a maquina possui atividade mas
                  //já é uma atividade concluida.
                  
                  if (maquinaDoPatioVisualizada.getAtividade() != null && 
                          !TipoAtividadeEnum.ATUALIZACAO_EMPILHAMENTO.equals(
                                  maquinaDoPatioVisualizada.getAtividade().getTipoAtividade()) ) {
                      maquinaDoPatioVisualizada.setAtividade(null);
                  }
                  */
                  
                  controladorDSP.atualizaEmpilhamentoMaquina(maquinaDoPatioVisualizada, this, false);
                   controladorDSP.getInterfaceInicial().exibirUltimaSituacaoPatio();
                  deselecionar();                
               }
            }
//            repaint();
//            controladorDSP.getInterfaceDSP().getInterfaceInicial().repaint();
         }
         catch (OperacaoCanceladaPeloUsuarioException ex)
         {
             controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
         }
         catch (ErroSistemicoException erroSis)
         {
             controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(erroSis.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
         }
      }
   }

   /**
    * Metodo auxiliar que inverte a posicao da maquina. Execucao será feita a partir do clique no popMenu
    */
   private void inverterMaquinaDoPatioActionPerformed(ActionEvent evt)
   {
      try
      {
         InterfaceMaquinaDoPatio maquinaClicada = obterMaquinaClicada(evt);
         if (maquinaClicada.getMaquinaDoPatioVisualizada().equals(getMaquinaDoPatioVisualizada()))
         {
        if (!maquinaDoPatioVisualizada.getGiraLanca())
            {
               throw new InverterPosicaoMaquinaException(PropertiesUtil.buscarPropriedade("exception.inversao.maquina.nao.gira.lanca"));
            }
            else
            {
                if(maquinaDoPatioVisualizada.getEstado().equals(EstadoMaquinaEnum.MANUTENCAO))
                {
                    throw new InverterPosicaoMaquinaException(PropertiesUtil.getMessage("aviso.maquina.em.manutencao"));
                }
                else if (maquinaDoPatioVisualizada.getTipoDaMaquina() != TipoMaquinaEnum.PA_CARREGADEIRA)
                {
                    Patio patioInferiorCorreia = correia.getCorreiaVisualizada().getPatioInferior();
                    Patio patioSuperiorCorreia = correia.getCorreiaVisualizada().getPatioSuperior();
                    Patio novoPatioMaquina = null;
                    //Baliza novaPosicaoMaquina = null;
                    if (maquinaClicada.getMaquinaVisualizada().getBracoNoPatioInferior())
                    {
                        if (patioSuperiorCorreia == null)
                        {
                            throw new InverterPosicaoMaquinaException(PropertiesUtil.buscarPropriedade("exception.inversao.maquina.para.patio.superior"));
                        }
                        // se o braco da maquina for do lado direito, novo patio será o superior
                        novoPatioMaquina = patioSuperiorCorreia;
                        //setamos a máquina para o pátio superior
                        maquinaClicada.getMaquinaVisualizada().setBracoNoPatioInferior(Boolean.FALSE);
                    }else{
                        if (patioInferiorCorreia == null)
                        {
                            throw new InverterPosicaoMaquinaException(PropertiesUtil.buscarPropriedade("exception.inversao.maquina.para.patio.inferior"));
                        }
                        // se o braco da maquina for do lado esquerdo, novo patio será o inferior
                        novoPatioMaquina = patioInferiorCorreia;
                        //setamos a máquina para o pátio inferior
                        maquinaClicada.getMaquinaVisualizada().setBracoNoPatioInferior(Boolean.TRUE);

                    }

                    if (maquinaDoPatioVisualizada.getTipoDaMaquina() == TipoMaquinaEnum.RECUPERADORA)
                    {
                        if (maquinaSelecionada)
                        {
                            desenhaEmpilhadoraRetomadoraSelecionada(novoPatioMaquina, patioInferiorCorreia, patioSuperiorCorreia);
                        }
                        else
                        {
                            desenhaEmpilhadoraERetomadora(novoPatioMaquina, patioInferiorCorreia, patioSuperiorCorreia);
                        }
                    }
                    else if (maquinaDoPatioVisualizada.getTipoDaMaquina() == TipoMaquinaEnum.EMPILHADEIRA)
                    {
                        if (maquinaSelecionada)
                        {
                            desenhaEmpilhadeiraSelecionada(novoPatioMaquina, patioInferiorCorreia, patioSuperiorCorreia);
                        }
                        else
                        {
                            desenhaEmpinhadeira(novoPatioMaquina, patioInferiorCorreia, patioSuperiorCorreia);
                        }
                    }
                    else
                    {
                        if (maquinaSelecionada)
                        {
                            desenhaEmpilhadoraRetomadoraSelecionada(novoPatioMaquina, patioInferiorCorreia, patioSuperiorCorreia);
                        }
                        else
                        {
                            desenhaEmpilhadoraERetomadora(novoPatioMaquina, patioInferiorCorreia, patioSuperiorCorreia);
                        }
                    }

                    // busca a interface patio referente ao novo patio
                    for (InterfacePatio interfacePatio : controladorDSP.getInterfaceDSP().getListaDePatios())
                    {
                        if (interfacePatio.getPatioVisualizado().getMetaPatio().equals(novoPatioMaquina.getMetaPatio()))
                        {
                            // busca a interface baliza do novo patio
                            for (InterfaceBaliza interfaceBaliza : interfacePatio.getListaDeBalizas())
                            {
                                if (interfaceBaliza.getBalizaVisualizada().getNumero().equals(posicao.getBalizaVisualizada().getNumero()))
                                {
                                    posicao = interfaceBaliza;
                                    maquinaDoPatioVisualizada.setPosicao(interfaceBaliza.getBalizaVisualizada());
                                    patio = interfaceBaliza.getInterfacePatio();
                                    maquinaDoPatioVisualizada.setPatio(interfaceBaliza.getBalizaVisualizada().getPatio());
                                    break;
                                }
                            }
                        }
                    }
                    calculaPosicaoMaquina();
                    this.repaint();
                }
            }
         }
      }
      catch (InverterPosicaoMaquinaException e)
      {
          controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
         interfaceMensagem = new InterfaceMensagem();
         interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
         interfaceMensagem.setDescricaoMensagem(e.getMessage());
         controladorDSP.ativarMensagem(interfaceMensagem);
      }
   }

   private void selecionaDeselecionaEmpilhadeiraRecuperadora(Patio patioLocalizacaoMaquina, Patio patioInferiorCorreia, Patio patioSuperiorCorreia) throws OperacaoCanceladaPeloUsuarioException, ValidacaoCampoException
   {
      if (maquinaSelecionada)
      {
         deselecionar();
         desenhaEmpilhadoraERetomadora(patioLocalizacaoMaquina, patioInferiorCorreia, patioSuperiorCorreia);
      }
      else
      {
         selecionar(false);
         desenhaEmpilhadoraRetomadoraSelecionada(patioLocalizacaoMaquina, patioInferiorCorreia, patioSuperiorCorreia);
      }
      this.repaint();
      controladorDSP.getInterfaceDSP().getInterfaceInicial().repaint();
   }

   private void selecionaDeselecionaEmpilhadeira(Patio patioLocalizacaoMaquina, Patio patioInferiorCorreia, Patio patioSuperiorCorreia) throws SelecaoObjetoModoRecuperacaoException, OperacaoCanceladaPeloUsuarioException, ValidacaoCampoException
   {
      if (controladorDSP.obterModoOperacao().equals(ModoDeOperacaoEnum.RECUPERAR))
      {
         throw new SelecaoObjetoModoRecuperacaoException(PropertiesUtil.buscarPropriedade("exception.selecao.empilhadeira.modo.recuperacao"));
      }
      if (maquinaSelecionada)
      {
         deselecionar();
         desenhaEmpinhadeira(patioLocalizacaoMaquina, patioInferiorCorreia, patioSuperiorCorreia);
      }
      else
      {
         selecionar(false);
         desenhaEmpilhadeiraSelecionada(patioLocalizacaoMaquina, patioInferiorCorreia, patioSuperiorCorreia);
      }
      this.repaint();
      controladorDSP.getInterfaceDSP().getInterfaceInicial().repaint();
   }

   private void selecionaDeselecionaRecuperadora(Patio patioLocalizacaoMaquina, Patio patioInferiorCorreia, Patio patioSuperiorCorreia) throws SelecaoObjetoModoEmpilhamentoException, OperacaoCanceladaPeloUsuarioException, ValidacaoCampoException
   {
      if (controladorDSP.obterModoOperacao().equals(ModoDeOperacaoEnum.EMPILHAR))
      {
         throw new SelecaoObjetoModoEmpilhamentoException(PropertiesUtil.buscarPropriedade("exception.selecao.recuperadora.modo.empilhar"));
      }
      if (maquinaSelecionada)
      {
         deselecionar();
         this.maquinaSelecionada = Boolean.FALSE;
         desenhaEmpilhadoraERetomadora(patioLocalizacaoMaquina, patioInferiorCorreia, patioSuperiorCorreia);
      }
      else
      {
         selecionar(false);
         desenhaEmpilhadoraRetomadoraSelecionada(patioLocalizacaoMaquina, patioInferiorCorreia, patioSuperiorCorreia);
      }
      this.repaint();
      controladorDSP.getInterfaceDSP().getInterfaceInicial().repaint();
   }

   private void selecionaDeselecionaPaCarregadeira() throws OperacaoCanceladaPeloUsuarioException, ValidacaoCampoException
   {
      if (maquinaSelecionada)
      {
         deselecionar();
         desenhaPaCarregadeira();
      }
      else
      {
         selecionar(false);
         desenhaPaCarregadeiraSelecionada();
      }
       this.repaint();
      controladorDSP.getInterfaceDSP().getInterfaceInicial().repaint();
   }

   /**
    * Define a posição que a máquina deve estar
    */
   private void calculaPosicaoMaquina()
   {
      int eixoX = 0, eixoY = 0, largura = 0, altura = 0;
      altura = comprimentoMaquina;
      largura = larguraMaquina;
      //ajuste na coordenada X, para que o desenho da maquina fique com a ponta da lança apontando para a baliza na qual se encontra localizada
      eixoX = posicao.getX() - (largura / 2) + posicao.getWidth();

      //Caso a máquina seja uma pá-carregadeira, não precisa definir se estará virada para cima ou para baixo. Caso contrário, deve-se definir isto.

      if (maquinaDoPatioVisualizada.getTipoDaMaquina() == TipoMaquinaEnum.PA_CARREGADEIRA)
      {
         eixoY = ((posicao.getHeight() + complementoAlturaTamanhoBorda));
      }
      else
      {
         if (this.maquinaDoPatioVisualizada.getBracoNoPatioInferior())
         {
             
            eixoY = correia.getY() - complementoAlturaTamanhoBorda;
            eixoX = posicao.getX() - (largura / 2);
         }
         else
         {
            eixoY = correia.getY() - comprimentoMaquina + correia.getComprimentoCorreiaPatio() + complementoAlturaTamanhoBorda;
         }
      }
      this.setDimensaoImagem(eixoX, eixoY, largura, altura);
      this.setBounds(eixoX, eixoY, largura, altura);
   }

   /**
    * Todas as máquinas de pátio, excetuando-se a pá carregadeira, podem estar viradas para um dos pátios que fazem fronteira com sua correia.
    * Este método procura definir a imagem da máquinas, dependendo do lado para o qual ela esteja virada.
    */
   private void defineImagemMaquina()
   {
      //Primeiramente, encontramos o pátio onde a máquina está e os pátios que fazem fronteira com a Correia dela ...
      Patio patioLocalizacaoMaquina = posicao.getBalizaVisualizada().getPatio();
      Patio patioInferiorCorreia = correia.getCorreiaVisualizada().getPatioInferior();
      Patio patioSuperiorCorreia = correia.getCorreiaVisualizada().getPatioSuperior();

      //... depois, para cada tipo de máquina, baseado no pátio para o qual a máquina está virada, definimos sua imagem.

      if (maquinaDoPatioVisualizada.getTipoDaMaquina() == TipoMaquinaEnum.RECUPERADORA)
      {
         desenhaEmpilhadoraERetomadora(patioLocalizacaoMaquina, patioInferiorCorreia, patioSuperiorCorreia);
      }
      else if (maquinaDoPatioVisualizada.getTipoDaMaquina() == TipoMaquinaEnum.EMPILHADEIRA)
      {
         desenhaEmpinhadeira(patioLocalizacaoMaquina, patioInferiorCorreia, patioSuperiorCorreia);
      }
      else if (maquinaDoPatioVisualizada.getTipoDaMaquina() == TipoMaquinaEnum.EMPILHADEIRA_RECUPERADORA)
      {
         desenhaEmpilhadoraERetomadora(patioLocalizacaoMaquina, patioInferiorCorreia, patioSuperiorCorreia);
      }

   }

   /**
    * desenha as empilhadeiraRecuperadora e as Recuperadoras, pois serão representadas pelo mesmo desenho sendo diferenciadas pelo tooltip
    * e o seu tipo de maquina
    * @param patioLocalizacaoMaquina
    * @param patioInferiorCorreia
    * @param patioSuperiorCorreia
    */
   private void desenhaEmpilhadoraERetomadora(Patio patioLocalizacaoMaquina, Patio patioInferiorCorreia, Patio patioSuperiorCorreia)
   {
       //verifica se a maquina esta posicionada no patio superior ou inferior
       if (patioInferiorCorreia != null && patioInferiorCorreia.equals(patioLocalizacaoMaquina)) {
           this.maquinaDoPatioVisualizada.setBracoNoPatioInferior(Boolean.TRUE);
           //verifica o status da maquina
           if(maquinaDoPatioVisualizada.getEstado().equals(EstadoMaquinaEnum.MANUTENCAO))
           {
               this.setImagemDSP("recuperadora_manutencao-a-baixo.png");
           } else if (this.maquinaDoPatioVisualizada.getMetaMaquina().maquinaInterditado(this.correia.getHoraSituacao())){
               this.setImagemDSP("recuperadora_manutencao-a-baixo.png");        
           }else if(maquinaDoPatioVisualizada.getEstado().equals(EstadoMaquinaEnum.OPERACAO))
           {
               this.setImagemDSP("recuperadora_operacao-a-baixo.png");
           }else{// se entrar neste condicial o estado deve estar em OCIOSA
               this.setImagemDSP("recuperadora_ociosa-a-baixo.png");
           }
       }
       else if (patioSuperiorCorreia != null && patioSuperiorCorreia.equals(patioLocalizacaoMaquina))
       {//neste condicional a maquina esta setada para o patio superior
           this.maquinaDoPatioVisualizada.setBracoNoPatioInferior(Boolean.FALSE);
           //verifica o status da maquina
           if(maquinaDoPatioVisualizada.getEstado().equals(EstadoMaquinaEnum.MANUTENCAO))
           {
               this.setImagemDSP("recuperadora_manutencao-a.png");
           } else if (this.maquinaDoPatioVisualizada.getMetaMaquina().maquinaInterditado(this.correia.getHoraSituacao())){
               this.setImagemDSP("recuperadora_manutencao-a.png");
           }else if(maquinaDoPatioVisualizada.getEstado().equals(EstadoMaquinaEnum.OPERACAO))
           {
               this.setImagemDSP("recuperadora_operacao-a.png");
           }else{// se entrar neste condicial o estado deve estar em OCIOSA
               this.setImagemDSP("recuperadora_ociosa-a.png");
           }
       }
   }

   /**
    * desenha as intefaces de maquina EmpilhadeiraRecuperadora e Recuperadoras que estão selecionadas
    * @param patioLocalizacaoMaquina
    * @param patioInferiorCorreia
    * @param patioSuperiorCorreia
    */
   private void desenhaEmpilhadoraRetomadoraSelecionada(Patio patioLocalizacaoMaquina, Patio patioInferiorCorreia, Patio patioSuperiorCorreia)
   {
       //verifica se a maquina esta posicionada no patio superior ou inferior
       if (patioInferiorCorreia != null && patioInferiorCorreia.equals(patioLocalizacaoMaquina)) {
           this.maquinaDoPatioVisualizada.setBracoNoPatioInferior(Boolean.TRUE);
           //verifica o status da maquina
           if(maquinaDoPatioVisualizada.getEstado().equals(EstadoMaquinaEnum.MANUTENCAO))
           {
               this.setImagemDSP("recuperadora_manutencao-b-baixo.png");
           } else if (this.maquinaDoPatioVisualizada.getMetaMaquina().maquinaInterditado(this.correia.getHoraSituacao())){
               this.setImagemDSP("recuperadora_manutencao-b-baixo.png");
           }else if(maquinaDoPatioVisualizada.getEstado().equals(EstadoMaquinaEnum.OPERACAO))
           {
               this.setImagemDSP("recuperadora_operacao-b-baixo.png");
           }else{// se entrar neste condicial o estado deve estar em OCIOSA
               this.setImagemDSP("recuperadora_ociosa-b-baixo.png");
           }
       }
       else if (patioSuperiorCorreia != null && patioSuperiorCorreia.equals(patioLocalizacaoMaquina))
       {//neste condicional a maquina esta setada para o patio superior
           this.maquinaDoPatioVisualizada.setBracoNoPatioInferior(Boolean.FALSE);
           //verifica o status da maquina
           if(maquinaDoPatioVisualizada.getEstado().equals(EstadoMaquinaEnum.MANUTENCAO))
           {
               this.setImagemDSP("recuperadora_manutencao-b.png");
           } else if (this.maquinaDoPatioVisualizada.getMetaMaquina().maquinaInterditado(this.correia.getHoraSituacao())){
               this.setImagemDSP("recuperadora_manutencao-b.png");
           }else if(maquinaDoPatioVisualizada.getEstado().equals(EstadoMaquinaEnum.OPERACAO))
           {
               this.setImagemDSP("recuperadora_operacao-b.png");
           }else{// se entrar neste condicial o estado deve estar em OCIOSA
               this.setImagemDSP("recuperadora_ociosa-b.png");
           }
       }
   }

   /**
    * desenha as empilhadeiras
    * @param patioLocalizacaoMaquina
    * @param patioInferiorCorreia
    * @param patioSuperiorCorreia
    */
   private void desenhaEmpinhadeira(Patio patioLocalizacaoMaquina, Patio patioInferiorCorreia, Patio patioSuperiorCorreia)
   {
        //verifica se a maquina esta posicionada no patio superior ou inferior
       if (patioInferiorCorreia != null && patioInferiorCorreia.equals(patioLocalizacaoMaquina)) {
           this.maquinaDoPatioVisualizada.setBracoNoPatioInferior(Boolean.TRUE);
           //verifica o status da maquina
           if(maquinaDoPatioVisualizada.getEstado().equals(EstadoMaquinaEnum.MANUTENCAO))
           {
               this.setImagemDSP("empilhadeira_manutecao-a-baixo.png");
           } else if (this.maquinaDoPatioVisualizada.getMetaMaquina().maquinaInterditado(this.correia.getHoraSituacao())){
               this.setImagemDSP("empilhadeira_manutecao-a-baixo.png");
           }else if(maquinaDoPatioVisualizada.getEstado().equals(EstadoMaquinaEnum.OPERACAO))
           {
               this.setImagemDSP("empilhadeira_operacao-a-baixo.png");
           }else{// se entrar neste condicial o estado deve estar em OCIOSA
               this.setImagemDSP("empilhadeira_ociosa-a-baixo.png");
           }
       }
       else if (patioSuperiorCorreia != null && patioSuperiorCorreia.equals(patioLocalizacaoMaquina))
       {//neste condicional a maquina esta setada para o patio superior
           this.maquinaDoPatioVisualizada.setBracoNoPatioInferior(Boolean.FALSE);
           //verifica o status da maquina
           if(maquinaDoPatioVisualizada.getEstado().equals(EstadoMaquinaEnum.MANUTENCAO))
           {
               this.setImagemDSP("empilhadeira_manutecao-a.png");
           } else if (this.maquinaDoPatioVisualizada.getMetaMaquina().maquinaInterditado(this.correia.getHoraSituacao())){
               this.setImagemDSP("empilhadeira_manutecao-a.png");
           }else if(maquinaDoPatioVisualizada.getEstado().equals(EstadoMaquinaEnum.OPERACAO))
           {
               this.setImagemDSP("empilhadeira_operacao-a.png");
           }else{// se entrar neste condicial o estado deve estar em OCIOSA
               this.setImagemDSP("empilhadeira_ociosa-a.png");
           }
       }
   }

   /**
    * desenha as empilhadeiras em modo de seleção
    * @param patioLocalizacaoMaquina
    * @param patioInferiorCorreia
    * @param patioSuperiorCorreia
    */
   private void desenhaEmpilhadeiraSelecionada(Patio patioLocalizacaoMaquina, Patio patioInferiorCorreia, Patio patioSuperiorCorreia)
   {
        //verifica se a maquina esta posicionada no patio superior ou inferior
       if (patioInferiorCorreia != null && patioInferiorCorreia.equals(patioLocalizacaoMaquina)) {
           this.maquinaDoPatioVisualizada.setBracoNoPatioInferior(Boolean.TRUE);
           //verifica o status da maquina
           if(maquinaDoPatioVisualizada.getEstado().equals(EstadoMaquinaEnum.MANUTENCAO))
           {
               this.setImagemDSP("empilhadeira_manutecao-b-baixo.png");
           } else if (this.maquinaDoPatioVisualizada.getMetaMaquina().maquinaInterditado(this.correia.getHoraSituacao())){
               this.setImagemDSP("empilhadeira_manutecao-b-baixo.png");
           }else if(maquinaDoPatioVisualizada.getEstado().equals(EstadoMaquinaEnum.OPERACAO))
           {
               this.setImagemDSP("empilhadeira_operacao-b-baixo.png");
           }else{// se entrar neste condicial o estado deve estar em OCIOSA
               this.setImagemDSP("empilhadeira_ociosa-b-baixo.png");
           }
       }
       else{//neste condicional a maquina esta setada para o patio superior
           this.maquinaDoPatioVisualizada.setBracoNoPatioInferior(Boolean.FALSE);
           //verifica o status da maquina
           if(maquinaDoPatioVisualizada.getEstado().equals(EstadoMaquinaEnum.MANUTENCAO))
           {
               this.setImagemDSP("empilhadeira_manutecao-b.png");
           } else if (this.maquinaDoPatioVisualizada.getMetaMaquina().maquinaInterditado(this.correia.getHoraSituacao())){
               this.setImagemDSP("empilhadeira_manutecao-b.png");   
           }else if(maquinaDoPatioVisualizada.getEstado().equals(EstadoMaquinaEnum.OPERACAO))
           {
               this.setImagemDSP("empilhadeira_operacao-b.png");
           }else{// se entrar neste condicial o estado deve estar em OCIOSA
               this.setImagemDSP("empilhadeira_ociosa-b.png");
           }
       }
   }

   /**
    * desenha as pa carregadeiras
    */
   private void desenhaPaCarregadeira()
   {
       //verifica o status da maquina
       if (maquinaDoPatioVisualizada.getEstado().equals(EstadoMaquinaEnum.MANUTENCAO)) {
           this.setImagemDSP("pa_carreg_manutencao-a.png");
       } else if (maquinaDoPatioVisualizada.getEstado().equals(EstadoMaquinaEnum.OPERACAO)) {
           this.setImagemDSP("pa_carreg_operacao-a.png");
       } else {// se entrar neste condicial o estado deve estar em OCIOSA
           this.setImagemDSP("pa_carreg_ociosa-a.png");
       }
   }

   /**
    * desenha as pa carregadeiras selecionadas
    */
   private void desenhaPaCarregadeiraSelecionada()
   {
       //verifica o status da maquina
       if (maquinaDoPatioVisualizada.getEstado().equals(EstadoMaquinaEnum.MANUTENCAO)) {
           this.setImagemDSP("pa_carreg_manutencao-b.png");
       } else if (maquinaDoPatioVisualizada.getEstado().equals(EstadoMaquinaEnum.OPERACAO)) {
           this.setImagemDSP("pa_carreg_operacao-b.png");
       } else {// se entrar neste condicial o estado deve estar em OCIOSA
           this.setImagemDSP("pa_carreg_ociosa-b.png");
       }
   }

   @Override
   public void deselecionar()
   {
      this.maquinaSelecionada = Boolean.FALSE;
   }

   @Override
   public Boolean isSelecionada()
   {
      return maquinaSelecionada;
   }

   public void selecionar(Boolean atividadeAtualizacao)
   {
      try
      {
         if ((patio != null && patio.getPatioVisualizado().getEstado().equals(EstadoMaquinaEnum.OPERACAO)) || (correia != null && !correia.getCorreiaVisualizada().getEstado().equals(EstadoMaquinaEnum.MANUTENCAO)))
         {
            if (maquinaDoPatioVisualizada.getEstado().equals(EstadoMaquinaEnum.OCIOSA) || atividadeAtualizacao)
            {
               setSelecionada(Boolean.TRUE);
            }
            else if (maquinaDoPatioVisualizada.getEstado().equals(EstadoMaquinaEnum.OPERACAO))
            {
               throw new MaquinaEstaIndisponivelException(PropertiesUtil.getMessage("aviso.maquina.em.operacao"));
            }
            else
            {
               throw new MaquinaEstaIndisponivelException(PropertiesUtil.getMessage("aviso.maquina.em.manutencao"));
            }
         }
         else
         {
            throw new EstruturaEmManutencaoException();
         }
      }
      catch (MaquinaEstaIndisponivelException e)
      {
          controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
         interfaceMensagem = new InterfaceMensagem();
         interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
         interfaceMensagem.setDescricaoMensagem(e.getMessage());
         controladorDSP.ativarMensagem(interfaceMensagem);
      }
      catch (EstruturaEmManutencaoException e)
      {
          controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
         interfaceMensagem = new InterfaceMensagem();
         interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
         interfaceMensagem.setDescricaoMensagem(e.getMessage());
         controladorDSP.ativarMensagem(interfaceMensagem);
      }

   }

   @Override
   public void setSelecionada(Boolean selecionada)
   {
      this.maquinaSelecionada = selecionada;

   }

   public MaquinaDoPatio getMaquinaVisualizada()
   {
      return maquinaDoPatioVisualizada;
   }

   /**
    * @return the posicao
    */
   public InterfaceBaliza getPosicao()
   {
      return posicao;
   }

   /**
    * @param posicao the posicao to set
    */
   public void setPosicao(InterfaceBaliza posicao)
   {
      this.posicao = posicao;
   }

   /**
    * @return the controladorDSP
    */
   public ControladorDSP getControladorDSP()
   {
      return controladorDSP;
   }

   /**
    * @param controladorDSP the controladorDSP to set
    */
   public void setControladorDSP(ControladorDSP controladorDSP)
   {
      this.controladorDSP = controladorDSP;
   }

   /**
    * @return the maquinaDoPatioVisualizada
    */
   public MaquinaDoPatio getMaquinaDoPatioVisualizada()
   {
      return maquinaDoPatioVisualizada;
   }

   /**
    * @param maquinaDoPatioVisualizada the maquinaDoPatioVisualizada to set
    */
   public void setMaquinaDoPatioVisualizada(MaquinaDoPatio maquinaDoPatioVisualizada)
   {
      this.maquinaDoPatioVisualizada = maquinaDoPatioVisualizada;
   }

   /**
    * @return the patio
    */
   public InterfacePatio getPatio()
   {
      return patio;
   }

   /**
    * @param patio the patio to set
    */
   public void setPatio(InterfacePatio patio)
   {
      this.patio = patio;
   }

   /**
    * @return the correia
    */
   public InterfaceCorreia getCorreia()
   {
      return correia;
   }

   /**
    * @param correia the correia to set
    */
   public void setCorreia(InterfaceCorreia correia)
   {
      this.correia = correia;
   }

   /**
    * @return the maquinaSelecionada
    */
   public Boolean getMaquinaSelecionada()
   {
      return maquinaSelecionada;
   }

   /**
    * @param maquinaSelecionada the maquinaSelecionada to set
    */
   public void setMaquinaSelecionada(Boolean maquinaSelecionada)
   {
      this.maquinaSelecionada = maquinaSelecionada;
   }

   /**
    * @return the interfaceMensagem
    */
   public InterfaceMensagem getInterfaceMensagem()
   {
      return interfaceMensagem;
   }

   /**
    * @param interfaceMensagem the interfaceMensagem to set
    */
   public void setInterfaceMensagem(InterfaceMensagem interfaceMensagem)
   {
      this.interfaceMensagem = interfaceMensagem;
   }

   /**
    * @return the comprimentoMaquina
    */
   public int getComprimentoMaquina()
   {
      return comprimentoMaquina;
   }

   /**
    * @param comprimentoMaquina the comprimentoMaquina to set
    */
   public void setComprimentoMaquina(int comprimentoMaquina)
   {
      this.comprimentoMaquina = comprimentoMaquina;
   }

   /**
    * @return the larguraMaquina
    */
   public int getLarguraMaquina()
   {
      return larguraMaquina;
   }

   /**
    * @param larguraMaquina the larguraMaquina to set
    */
   public void setLarguraMaquina(int larguraMaquina)
   {
      this.larguraMaquina = larguraMaquina;
   }

   /**
    * @return the complementoAlturaTamanhoBorda
    */
   public int getComplementoAlturaTamanhoBorda()
   {
      return complementoAlturaTamanhoBorda;
   }

   /**
    * @param complementoAlturaTamanhoBorda the complementoAlturaTamanhoBorda to set
    */
   public void setComplementoAlturaTamanhoBorda(int complementoAlturaTamanhoBorda)
   {
      this.complementoAlturaTamanhoBorda = complementoAlturaTamanhoBorda;
   }

   @Override
   public String toString()
   {
      return this.getMaquinaVisualizada().getNomeMaquina();
   }

   /**
    * Metodo que desabilita os menus existentes neste objeto em caso de processamento
    */
   @Override
   public void desabilitarMenus()
   {
      if (this.getComponentPopupMenu() != null)
      {
         for (int i = 0; i < this.getComponentPopupMenu().getComponentCount(); i++)
         {
            this.getComponentPopupMenu().getComponent(i).setEnabled(false);
         }
      }
   }

   /**
    * Metodo que habilita os menus existentes neste objeto em caso de finalizacao processamento
    */
   @Override
   public void habilitarMenus()
   {
      if (this.getComponentPopupMenu() != null)
      {
         for (int i = 0; i < this.getComponentPopupMenu().getComponentCount(); i++)
         {
            this.getComponentPopupMenu().getComponent(i).setEnabled(true);
         }
      }
   }

   public Integer getTipoAtualizacaoEmpilhamentoExcutado()
   {
      return tipoAtualizacaoEmpilhamentoExcutado;
   }

   public void setTipoAtualizacaoEmpilhamentoExcutado(Integer tipoAtualizacaoEmpilhamentoExcutado)
   {
      this.tipoAtualizacaoEmpilhamentoExcutado = tipoAtualizacaoEmpilhamentoExcutado;
   }

  
   /**
    * Define o lugar de destino da movimentacao onde sera empilhado o PSM/Pellet
    * Screening.
    *
    * @param totalMovimentacao
    * @return lugarEmpilhamento
    */
   private LugarEmpilhamentoRecuperacao defineLugarEmpilhamento(TotalMovimentacao totalMovimentacao, TipoDeProdutoEnum tipoDeProduto)
   {
      LugarEmpilhamentoRecuperacao lugarEmpilhamento = new LugarEmpilhamentoRecuperacao();
      
      //lugarEmpilhamento.setListaDeBalizas(new ArrayList<Baliza>());
      /*Baliza baliza = new Baliza();
      baliza.geraClone(totalMovimentacao.getBalizaEmpilhamento(), clone);*/
      
      lugarEmpilhamento.addPilhaVirtual(totalMovimentacao.getBalizaEmpilhamento());
      
      
      lugarEmpilhamento.addMaquinaDoPatio(this.getMaquinaVisualizada());
      lugarEmpilhamento.setSentido(totalMovimentacao.getSentido());
      lugarEmpilhamento.setNomeDoLugarEmpRec(totalMovimentacao.getNomePilha());
      //lugarEmpilhamento.setTaxaDeOperacaoNaPilha(0.0d);
      lugarEmpilhamento.setExecutado(Boolean.FALSE);
      lugarEmpilhamento.setQuantidade(0.0d);

      List<TipoProduto> listaTipoProduto = new ArrayList<TipoProduto>();
      try
      {
         listaTipoProduto = controladorDSP.getInterfaceInicial().getInterfaceInicial().getListaTiposProduto();
      }
      catch (ErroSistemicoException ex)
      {
         Logger.getLogger(InterfaceMaquinaDoPatio.class.getName()).log(Level.SEVERE, null, ex);
      }
      for (TipoProduto tipoProduto : listaTipoProduto)
      {
         if (tipoProduto.getTipoDeProduto().equals(tipoDeProduto))
         {
           // lugarEmpilhamento.setTipoDeProduto(tipoProduto);
         }
      }



      return lugarEmpilhamento;
   }

   /**
    * Define o lugar de origem do produto que sera movimentado.
    *
    * @param totalMovimentacao
    * @return listaLugaresRecuperacao
    */
   private List<LugarEmpilhamentoRecuperacao> defineLugarRecuperacao(TotalMovimentacao totalMovimentacao)
   {
      List<LugarEmpilhamentoRecuperacao> listaLugaresRecuperacao = new ArrayList<LugarEmpilhamentoRecuperacao>();

      for (ProdutoMovimentacao produtoMovimentacao : totalMovimentacao.getListaProdutoMovimentacao())
      {
         LugarEmpilhamentoRecuperacao lugarRecuperacao = new LugarEmpilhamentoRecuperacao();
         lugarRecuperacao.addMaquinaDoPatio(this.getMaquinaVisualizada());
         //lugarRecuperacao.setListaDeBalizas(new ArrayList<Baliza>());
         lugarRecuperacao.addPilhaVirtual(produtoMovimentacao.getBalizaSelecionada());
         lugarRecuperacao.setQuantidade(produtoMovimentacao.getQuantidadeMovimentacao());
        // lugarRecuperacao.setTaxaDeOperacaoNaPilha(0.0d);
         //lugarRecuperacao.setNomeDoLugarEmpRec(produtoMovimentacao.getBalizaSelecionada().getPilha().getNomePilha());
         lugarRecuperacao.setExecutado(Boolean.FALSE);
         lugarRecuperacao.setSentido(totalMovimentacao.getSentido());

         listaLugaresRecuperacao.add(lugarRecuperacao);
      }

      return listaLugaresRecuperacao;
   }

   /**
    * apresenta mensagens de aviso ao usuario
    */
   private void ativaMensagem(String mensagem)
   {
       controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
      interfaceMensagem = new InterfaceMensagem();
      interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_PROCESSAMENTO);
      interfaceMensagem.setProcessamentoAtividado(Boolean.TRUE);
      interfaceMensagem.setDescricaoMensagem(mensagem);
      controladorDSP.ativarMensagem(interfaceMensagem);
   }
   

   /** Atualizacao da recuperacao da maquina */
   private void atualizarRecuperacaoActionPerformed(ActionEvent evt)
   {
      InterfaceMaquinaDoPatio maquinaClicada = obterMaquinaClicada(evt);
      if (maquinaClicada.getMaquinaDoPatioVisualizada().equals(getMaquinaDoPatioVisualizada()))
      {
         try
         {
            selecaoDeMaquinaAtivado = true;
            if (maquinaSelecionada)
            {
               deselecionar();
            }
            else
            {
               if (selecaoDeMaquinaAtivado != false)
               {
                  selecionar(true);
                  controladorDSP.atualizaRecuperacaoMaquina(maquinaDoPatioVisualizada, this);
                  controladorDSP.getInterfaceInicial().exibirUltimaSituacaoPatio();
                  deselecionar();                  
               }
            }
            repaint();
            controladorDSP.getInterfaceDSP().getInterfaceInicial().repaint();            
         }
         catch (OperacaoCanceladaPeloUsuarioException ex)
         {
             deselecionar();
             controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
         }
         catch (ErroSistemicoException erroSis)
         {
             controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(erroSis.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
         }
      }
   }

   @Override
   public void selecionar()
   {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   private boolean exibeMenuAtualizacaoRecuperacao()
   {
       boolean exibeMenu = Boolean.TRUE;
       if(maquinaDoPatioVisualizada.getMetaMaquina().existeEmpilhamentoPendente() != null ) {
               exibeMenu = Boolean.FALSE;       
       }else{
               exibeMenu = Boolean.TRUE;
       }
       return exibeMenu;
   }
   private boolean exibeMenuAtualizacaoEmpilhamento()
   {
       boolean exibeMenu = Boolean.TRUE;       
           if(maquinaDoPatioVisualizada.getTipoDaMaquina().equals(TipoMaquinaEnum.EMPILHADEIRA_RECUPERADORA) 
             && (maquinaDoPatioVisualizada.getMetaMaquina().existeRecuperacaoPendente() != null) ){
               exibeMenu = Boolean.FALSE;           
           }else{
               exibeMenu = Boolean.TRUE;
           }
       return exibeMenu;
   }

   public void deselecionarMaquina(){
       Patio patioLocalizacaoMaquina = posicao.getBalizaVisualizada().getPatio();
        try {
            if (maquinaDoPatioVisualizada.getTipoDaMaquina().equals(TipoMaquinaEnum.EMPILHADEIRA)) {
                Patio patioInferiorCorreia = correia.getCorreiaVisualizada().getPatioInferior();
                Patio patioSuperiorCorreia = correia.getCorreiaVisualizada().getPatioSuperior();
                selecionaDeselecionaEmpilhadeira(patioLocalizacaoMaquina, patioInferiorCorreia, patioSuperiorCorreia);
            }
            else if(maquinaDoPatioVisualizada.getTipoDaMaquina().equals(TipoMaquinaEnum.RECUPERADORA)){
                Patio patioInferiorCorreia = correia.getCorreiaVisualizada().getPatioInferior();
                Patio patioSuperiorCorreia = correia.getCorreiaVisualizada().getPatioSuperior();
                selecionaDeselecionaRecuperadora(patioLocalizacaoMaquina, patioInferiorCorreia, patioSuperiorCorreia);
            }
            else if(maquinaDoPatioVisualizada.getTipoDaMaquina().equals(TipoMaquinaEnum.EMPILHADEIRA_RECUPERADORA)){
                Patio patioInferiorCorreia = correia.getCorreiaVisualizada().getPatioInferior();
                Patio patioSuperiorCorreia = correia.getCorreiaVisualizada().getPatioSuperior();
                selecionaDeselecionaEmpilhadeiraRecuperadora(patioLocalizacaoMaquina, patioInferiorCorreia, patioSuperiorCorreia);
            }
            else if(maquinaDoPatioVisualizada.getTipoDaMaquina().equals(TipoMaquinaEnum.PA_CARREGADEIRA)){
                selecionaDeselecionaPaCarregadeira();
            }

        } catch (SelecaoObjetoModoEmpilhamentoException e) {
            controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(e.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
        } catch (SelecaoObjetoModoRecuperacaoException e) {
            controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(e.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
        } catch (OperacaoCanceladaPeloUsuarioException e) {
            controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
            interfaceMensagem.setDescricaoMensagem(e.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
        } catch (ValidacaoCampoException e) {
            controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(e.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
        }

    }

   private boolean exibeMenuFinalizarEmpilhamento()
   {
       boolean exibeMenu = Boolean.FALSE;
       if(maquinaDoPatioVisualizada.getMetaMaquina().existeEmpilhamentoPendente() != null)
       {
           exibeMenu = Boolean.TRUE;
       }
       return exibeMenu;
   }

   private void finalizaAtulizacaoEmpilhamento(ActionEvent evt)
   {
        InterfaceMaquinaDoPatio maquinaClicada = obterMaquinaClicada(evt);
        if (maquinaClicada.getMaquinaDoPatioVisualizada().equals(getMaquinaDoPatioVisualizada())) {
            try {
                selecaoDeMaquinaAtivado = true;
                if (maquinaSelecionada) {
                    deselecionar();
                } else {
                    if (selecaoDeMaquinaAtivado != false) {
                        selecionar(true);
                        controladorDSP.atualizaEmpilhamentoMaquina(maquinaDoPatioVisualizada, this, true);
                        controladorDSP.getInterfaceInicial().exibirUltimaSituacaoPatio();
                        deselecionar();
                    }
                }
                repaint();
                controladorDSP.getInterfaceDSP().getInterfaceInicial().repaint();
            } catch (OperacaoCanceladaPeloUsuarioException ex) {
                controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
                interfaceMensagem = new InterfaceMensagem();
                interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
                interfaceMensagem.setDescricaoMensagem(ex.getMessage());
                controladorDSP.ativarMensagem(interfaceMensagem);
            } catch (ErroSistemicoException erroSis) {
                controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
                interfaceMensagem = new InterfaceMensagem();
                interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
                interfaceMensagem.setDescricaoMensagem(erroSis.getMessage());
                controladorDSP.ativarMensagem(interfaceMensagem);
            }
        }
    }

   private void retornarPelletFeed(ActionEvent evt)
   {
      InterfaceMaquinaDoPatio maquinaClicada = obterMaquinaClicada(evt);
      if (maquinaClicada.getMaquinaDoPatioVisualizada().equals(getMaquinaDoPatioVisualizada()))
      {
         try
         {
            controladorDSP.retornarPelletFeed(maquinaClicada);
            controladorDSP.getInterfaceInicial().exibirUltimaSituacaoPatio();
         }
         catch (PilhaPelletFeedNaoEncontradaException pex)
         {
             controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            interfaceMensagem.setDescricaoMensagem(pex.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
         }
         catch (OperacaoCanceladaPeloUsuarioException ex)
         {
             controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
         }
         catch (ErroSistemicoException erroSis)
         {
             controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(erroSis.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
         }
      }
   }

    /**
     * Metodo que apresenta a interface de lista de manutencoes da maquina
     * visualizada.
     *
     * @param evt
     */
    @SuppressWarnings("serial")
    private void apresentaListaManutencoes(ActionEvent evt) {
        try {
            final InterfaceManutencao interfaceManutencao = new InterfaceManutencao( this.controladorDSP, this) {

                @Override
                public void fecharJanela() {
                    controladorDSP.getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida().setStatusEdicao(
                            Boolean.FALSE);
                    this.setOperacaoCanceladaPeloUsuario(Boolean.TRUE);
                }
            };

            JDialog dialogTratamentoPSM = new JDialog(controladorDSP.getInterfaceDSP().getInterfaceInicial(), true);
            dialogTratamentoPSM.addWindowListener(new java.awt.event.WindowAdapter() {

                @Override
                public void windowClosing(java.awt.event.WindowEvent evt) {
                    controladorDSP.getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida().setStatusEdicao(
                            Boolean.FALSE);
                    interfaceManutencao.setOperacaoCanceladaPeloUsuario(Boolean.TRUE);
                }
            });

            dialogTratamentoPSM.setLayout(new BorderLayout());
            dialogTratamentoPSM.setTitle(PropertiesUtil.getMessage("mensagem.manutencao.popup"));
            dialogTratamentoPSM.getContentPane().add(interfaceManutencao);
            dialogTratamentoPSM.setSize(740, 400);
            dialogTratamentoPSM.setLocationRelativeTo(null);
            dialogTratamentoPSM.setVisible(true);

            // A operacao de edicao da lista de manutencoes foi cancelada pelo
            // usuário
            if (interfaceManutencao.getOperacaoCanceladaPeloUsuario()) {
                dialogTratamentoPSM.setVisible(false);
                controladorDSP.getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida().setStatusEdicao(
                        Boolean.FALSE);
                controladorDSP.getInterfaceInicial().getInterfaceComandos().cancelarModoOperacao();
                throw new OperacaoCanceladaPeloUsuarioException(
                        PropertiesUtil.buscarPropriedade("exception.operacao.cancelada.pelo.usuario"));
            }


            
          //atualiza o estado da maquina caso esteja algum intervalo de manutenção em execução
            controladorDSP.validaDataAtividadePeriodoManutencao(controladorDSP.getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida().getDtInicio(), getMaquinaDoPatioVisualizada());
            controladorDSP.getInterfaceInicial().getInterfaceInicial().atualizarDSP();


        } catch (OperacaoCanceladaPeloUsuarioException ex) {
            controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
        }
        
}

   /** Atividade de movimentacao de pilha de emergencia */
   private void movimentarPilhaEmergencia(ActionEvent evt)
   {
      InterfaceMaquinaDoPatio maquinaClicada = obterMaquinaClicada(evt);
      if (maquinaClicada.getMaquinaDoPatioVisualizada().equals(getMaquinaDoPatioVisualizada()))
      {
         try
         {
            selecionar(true);
            controladorDSP.movimentarPilhaEmergencia(maquinaDoPatioVisualizada, this);
            controladorDSP.getInterfaceInicial().exibirUltimaSituacaoPatio();
            deselecionar();
         }
         catch (OperacaoCanceladaPeloUsuarioException ex)
         {
             deselecionar();
             controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
         }
         catch (ErroSistemicoException erroSis)
         {
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(erroSis.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
         }
      }
   }

   private void desabilitaMenusPermissaoUsuario()
   {       
       if (controladorDSP != null) {
           if(controladorDSP.getInterfaceInicial().verificaPermissaoAtualizacaoProducao())
           {
               if (this.getComponentPopupMenu() != null) {
                   for (int i = 0; i < this.getComponentPopupMenu().getComponentCount(); i++) {
                       this.getComponentPopupMenu().getComponent(i).setEnabled(false);
                   }
               }
           }
       }    
   }


}
