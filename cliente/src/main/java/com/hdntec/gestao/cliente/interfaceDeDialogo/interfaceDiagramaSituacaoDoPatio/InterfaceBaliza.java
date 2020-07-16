package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;

import com.hdntec.gestao.cliente.interfaceDeDialogo.ModoDeOperacaoEnum;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.estruturasAuxiliares.DadosTratamentoPSM;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.estruturasAuxiliares.ProdutoMovimentacao;
import com.hdntec.gestao.cliente.interfaceProgramacao.InterfaceInicializacao;
import com.hdntec.gestao.cliente.interfaceProgramacao.InterfaceSelecionavelParaAtividade;
import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.cliente.util.numeros.DSSStockyardFuncoesNumeros;
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.entity.LugarEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.enums.TipoAtividadeEnum;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.domain.produto.enums.TipoDeProdutoEnum;
import com.hdntec.gestao.exceptions.BalizaInvalidaException;
import com.hdntec.gestao.exceptions.BalizasSelecionadasException;
import com.hdntec.gestao.exceptions.BlendagemInvalidaException;
import com.hdntec.gestao.exceptions.CampanhaIncompativelException;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.EstruturaEmManutencaoException;
import com.hdntec.gestao.exceptions.ExcessoDeMaterialParaEmbarqueException;
import com.hdntec.gestao.exceptions.ModoDeEdicaoException;
import com.hdntec.gestao.exceptions.OperacaoCanceladaPeloUsuarioException;
import com.hdntec.gestao.exceptions.ProdutoIncompativelException;
import com.hdntec.gestao.exceptions.RastreabilidadeException;
import com.hdntec.gestao.exceptions.ValidacaoCampoException;
import com.hdntec.gestao.util.PropertiesUtil;

/**
 * Interface grafica que representa a {@link Baliza}.
 *
 * @author andre
 *
 */
public class InterfaceBaliza extends JLabel implements InterfaceSelecionavelParaAtividade, InterfaceInicializacao
{

   /** serial gerado */
   private static final long serialVersionUID = -960791369433904670L;

   /** a baliza visualizada */
   private Baliza balizaVisualizada;

   /** a interface grÃ¡fica do pÃ¡tio ao qual essa baliza pertence */
   private InterfacePatio interfacePatio;

   /** acesso Ã s operaÃ§Ãµes do subsistema de interface grÃ¡fica DSP */
   private ControladorDSP controladorDSP;

   /** flag que indica se a baliza estao ou nao selecionada */
   private Boolean balizaSelecionada;

   /** a interface grÃ¡fica da pilha que esta sobre esta baliza */
   private InterfacePilha interfacePilha;

   /** a interface das mensagens exibidas pela interface baliza */
   private InterfaceMensagem interfaceMensagem;

   private int alturaPilha;

   private int complementoTamanhoBaliza;

   private int alturaBalizaPilha;

   private Boolean selecionadaMenu;

   /**controla a ação de seleção da baliza com arraste do mouse */
   private ControladorEventoMouseBaliza controladorEventoMouseBaliza;
   //menus
   private JMenuItem mnuEditaBaliza;
   private JMenuItem mnuEditaNomePilha;
   private JMenuItem mnuDeselecionaPilha;
   private JMenuItem mnuSelecionaPilha;
   private JMenuItem mnuTransportarEmergencia;
   private JMenuItem mnuExibirRastreabilidade;
   private JMenuItem mnuTratarPSM;
   private JMenuItem mnuInterdicaoDeBaliza;
   private JMenuItem mnuUnificarPilha;


   public InterfaceBaliza()
   {
      super();
      balizaSelecionada = Boolean.FALSE;
      selecionadaMenu = Boolean.FALSE;

   }

   @Override
   public void inicializaInterface()
   {
      definePropriedadesInterfaceBaliza();
      criarPopMenuBaliza();
      defineDimensoesFixas();
      calculaPosicaoBaliza();
      defineEventosParaBaliza();
      desabilitaMenusPermissaoUsuario();
   }

   @Override
   public void defineDimensoesFixas()
   {
      alturaBalizaPilha = Integer.parseInt(PropertiesUtil.buscarPropriedade("dimensao.altura.baliza.pilha").trim());
      alturaPilha = Integer.parseInt(PropertiesUtil.buscarPropriedade("dimensao.pilha.comprimento").trim());
      complementoTamanhoBaliza = Integer.parseInt(PropertiesUtil.buscarPropriedade("dimensao.baliza.altura.complemento.tamanho").trim());
   }

   private void criarPopMenuBaliza()
   {
      JPopupMenu popMnuParaBaliza = new JPopupMenu();

//      JMenuItem
      mnuEditaBaliza = new JMenuItem();
      mnuEditaBaliza.setText(PropertiesUtil.getMessage("menu.edita.grupo.baliza"));
      
      // habilita a edicao apenas quando é a ultima situacao do patio
      if (!controladorDSP.getInterfaceInicial().estaNaUltimaSituacao())
    	  mnuEditaBaliza.setEnabled(false);
      else
    	  mnuEditaBaliza.setEnabled(true);
      
      mnuEditaBaliza.addActionListener(new ActionListener()
      {

         @Override
         public void actionPerformed(ActionEvent evt)
         {
            controladorDSP.setSelecionaMenuBaliza(Boolean.TRUE);
            editaBalizaMenu(evt);
         }
      });
      popMnuParaBaliza.add(mnuEditaBaliza);

      if (this.balizaVisualizada.retornaStatusHorario(interfacePatio.getHoraSituacao()) != null)
      {
//         JMenuItem
         mnuEditaNomePilha = new JMenuItem();
         mnuEditaNomePilha.setText(PropertiesUtil.getMessage("menu.edita.nome.pilha"));
         
         // habilita a edicao apenas quando é a ultima situacao do patio
         if (!controladorDSP.getInterfaceInicial().estaNaUltimaSituacao())
        	 mnuEditaNomePilha.setEnabled(false);
         else
        	 mnuEditaNomePilha.setEnabled(true);
         
         mnuEditaNomePilha.addActionListener(new ActionListener()
         {

            @Override
            public void actionPerformed(ActionEvent evt)
            {
               controladorDSP.setSelecionaMenuBaliza(Boolean.TRUE);
               editaNomePilha(evt);
            }
         });
         popMnuParaBaliza.add(mnuEditaNomePilha);

      }

      if (this.balizaVisualizada.retornaStatusHorario(interfacePatio.getHoraSituacao()) != null && this.balizaVisualizada.getProduto() != null )
      {
//    	  JMenuItem
         mnuSelecionaPilha = new JMenuItem();
         mnuSelecionaPilha.setText(PropertiesUtil.getMessage("menu.selecionar.pilha"));
         mnuSelecionaPilha.addActionListener(new java.awt.event.ActionListener()
         {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
               if (interfacePilha != null)
               {
                  controladorDSP.setSelecionaMenuBaliza(Boolean.TRUE);
                  interfacePilha.selecionarPilhaActionPerformed(evt);
               }
            }
         });
         popMnuParaBaliza.add(mnuSelecionaPilha);


//         JMenuItem
         mnuDeselecionaPilha = new JMenuItem();
         mnuDeselecionaPilha.setText(PropertiesUtil.getMessage("menu.deselecionar.pilha"));
         mnuDeselecionaPilha.addActionListener(new java.awt.event.ActionListener()
         {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
               if (interfacePilha != null)
               {
                  controladorDSP.setSelecionaMenuBaliza(Boolean.TRUE);
                  interfacePilha.deselecionarPilhaActionPerformed(evt);
               }
            }
         });
         popMnuParaBaliza.add(mnuDeselecionaPilha);
      }

      // verifica se a emergencia de origem tem a possibilidade de efetuar o transporte de emergencia
    /*  if (this.balizaVisualizada.getPilha() != null ) 
    	  *//** TODO TRATAMENTO DE PILHA DE EMERGENCIA EDIÇAO DE BALIZA
        
      //&&
            //this.balizaVisualizada.getPilha().getEmergenciaOrigem() != null &&
            //this.balizaVisualizada.getPilha().getEmergenciaOrigem().getEmergenciaTransporte() != null) *//*
      { 
//         JMenuItem
          mnuTransportarEmergencia = new JMenuItem();
         mnuTransportarEmergencia.setText(PropertiesUtil.getMessage("menu.transportar.pilha.emergencia"));
         mnuTransportarEmergencia.addActionListener(new java.awt.event.ActionListener()
         {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
               if (interfacePilha != null)
               {
                  controladorDSP.setSelecionaMenuBaliza(Boolean.TRUE);
                  transportarPilhaEmergencia(evt);
               }
            }
         });
         popMnuParaBaliza.add(mnuTransportarEmergencia);
      }
*/
      if (this.balizaVisualizada.getProduto() != null)
      {
//         JMenuItem
          mnuExibirRastreabilidade = new JMenuItem();
         mnuExibirRastreabilidade.setText(PropertiesUtil.getMessage("menu.exibir.rastreabilidade"));
         mnuExibirRastreabilidade.addActionListener(new java.awt.event.ActionListener()
         {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
               controladorDSP.setSelecionaMenuBaliza(Boolean.TRUE);
               exibirRastreabilidadeBalizaActionPerformed(evt);
            }
         });
         popMnuParaBaliza.add(mnuExibirRastreabilidade);
      }

     /*
      * TODO VOLTAR TRATAMENTO DE PSM CASO USUARIO SOLICITE 
      *  
      *  if (this.balizaVisualizada.getTipoBaliza().equals(EnumTipoBaliza.PSM))
      {
//         JMenuItem
          mnuTratarPSM = new JMenuItem();
         mnuTratarPSM.setText(PropertiesUtil.getMessage("menu.tratar.psm"));
         mnuTratarPSM.addActionListener(new java.awt.event.ActionListener()
         {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
               controladorDSP.setSelecionaMenuBaliza(Boolean.TRUE);
               tratarPSMBalizaActionPerformed(evt);
            }
         });
         popMnuParaBaliza.add(mnuTratarPSM);
      }*/

      // menu de Interdicao de baliza
//      JMenuItem
      mnuInterdicaoDeBaliza = new JMenuItem();
      mnuInterdicaoDeBaliza.setText(PropertiesUtil.getMessage("mensagem.menu.baliza.interdicao"));
      mnuInterdicaoDeBaliza.addActionListener(new ActionListener()
      {

         @Override
         public void actionPerformed(ActionEvent event)
         {
            interditarBalizaActionPerformed(event);
         }
      });
      popMnuParaBaliza.add(mnuInterdicaoDeBaliza);

      //menu de unificacao de pilha
      mnuUnificarPilha = new JMenuItem();
      mnuUnificarPilha.setText(PropertiesUtil.getMessage("menu.unificar.pilha"));
      // habilita a unificacao apenas quando é a ultima situacao do patio
      if (!controladorDSP.getInterfaceInicial().estaNaUltimaSituacao())
    	  mnuUnificarPilha.setEnabled(false);
      else
    	  mnuUnificarPilha.setEnabled(true);
      mnuUnificarPilha.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent evt) {
            //metodo de INSERIR anotacao - InterfacePAtio
            mnuUnificarPilhaActionPerformed(evt);
         }
      });
      popMnuParaBaliza.add(mnuUnificarPilha);
      
      this.setComponentPopupMenu(popMnuParaBaliza);
   }

   private void definePropriedadesInterfaceBaliza()
   {
      // defina as propriedades da fonte do desenho da baliza
      this.setFont(new Font("Dialog", 0, 7));
      this.setText("-|");

      if (this.balizaVisualizada.getMetaBaliza().balizaInterditado(interfacePatio.getHoraSituacao()))
      {
         this.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 0, 0)));
      }

      this.setVerticalAlignment(SwingConstants.TOP);

      String numeroBaliza;
      if (balizaVisualizada.getNumero() != null)
      {
         numeroBaliza = balizaVisualizada.getNumero().toString();
      }
      else
      {
         numeroBaliza = "";
      }

      String capacidadeMaximaBaliza;
      if (balizaVisualizada.getCapacidadeMaxima() != null)
      {
         capacidadeMaximaBaliza = balizaVisualizada.getCapacidadeMaxima().toString();
      }
      else
      {
         capacidadeMaximaBaliza = "";
      }

      String quantidadeNaBaliza;
      if (balizaVisualizada.getProduto() != null)
      {
         quantidadeNaBaliza = DSSStockyardFuncoesNumeros.getQtdeFormatada(balizaVisualizada.getProduto().getQuantidade(), 2);
      }
      else
      {
         quantidadeNaBaliza = "0";
      }

      String horarioInicioFormacao;
      if (balizaVisualizada.getHorarioInicioFormacao() != null)
      {
         horarioInicioFormacao = balizaVisualizada.getHorarioInicioFormacao().toString();
      }
      else
      {
         horarioInicioFormacao = "";
      }

      String horarioFimFormacao;
      if (balizaVisualizada.getHorarioFimFormacao() != null)
      {
         horarioFimFormacao = balizaVisualizada.getHorarioFimFormacao().toString();
      }
      else
      {
         horarioFimFormacao = "";
      }

      String nomeDaPilha;
      if (balizaVisualizada.retornaStatusHorario(interfacePatio.getHoraSituacao()) != null && balizaVisualizada.getProduto() != null)
      {
         nomeDaPilha = balizaVisualizada.retornaStatusHorario(interfacePatio.getHoraSituacao()).getNomePilha();
      }
      else
      {
         nomeDaPilha = "";
      }

      String estadoDaBaliza;
      if (balizaVisualizada.getEstado() != null)
      {
         estadoDaBaliza = balizaVisualizada.getEstado().toString();
      }
      else
      {
         estadoDaBaliza = "";
      }

      String tipoDaBaliza;
      if (balizaVisualizada.getTipoBaliza() != null)
      {
         tipoDaBaliza = balizaVisualizada.getTipoBaliza().toString();
      }
      else
      {
         tipoDaBaliza = "";
      }

      List<String> listaParam = new ArrayList<String>();
      listaParam.add(numeroBaliza);
      listaParam.add(quantidadeNaBaliza);
      listaParam.add(capacidadeMaximaBaliza);
      listaParam.add(horarioInicioFormacao);
      listaParam.add(horarioFimFormacao);
      listaParam.add(nomeDaPilha);
      listaParam.add(estadoDaBaliza);
      listaParam.add(tipoDaBaliza);
      this.setToolTipText(PropertiesUtil.getMessage("texto.tooltip.baliza", listaParam));
      //fazendo os tooltips do programa aparecem instantaneamente e durarem infinitamente enquanto o cursor estiver em cima do objeto
   }

   private void calculaPosicaoBaliza()
   {
      Rectangle rectParent = interfacePatio.getBounds();

      int eixoX = 0, eixoY = 0, altura = 0, largura = 0;

      eixoY = (int) ((rectParent.getHeight() / 2) - alturaBalizaPilha);
      altura = (alturaPilha + complementoTamanhoBaliza);
      largura = (int) (rectParent.getWidth() / interfacePatio.getListaDeBalizas().size());
      eixoX = (((interfacePatio.getComponentCount() - 1) * largura));

      this.setBounds(eixoX, eixoY, largura, altura);

   }

   private void defineEventosParaBaliza()
   {
      this.addMouseListener(new MouseAdapter()
      {

         @Override
         public void mousePressed(MouseEvent evt)
         {
            controladorEventoMouseBaliza.setMousePressedFoiAtivado(Boolean.TRUE);
            selecionaDeselecionaBaliza(evt);
         }

         @Override
         public void mouseEntered(MouseEvent evt)
         {
            /** verifica se o botÃ£o1 foi precionado e compara se o macara de modificacao do evento Ã© equivalente ao
             * botao1, do contrario significa que o botÃ£o1 nÃ£o esta precionado.
             * Maiores informacoes @see http://java.sun.com/javase/6/docs/api/java/awt/event/InputEvent.html#BUTTON1_MASK   */
            if (controladorEventoMouseBaliza.isMousePressedFoiAtivado() && evt.getModifiersEx() == InputEvent.BUTTON1_DOWN_MASK)
            {
               if (!controladorDSP.getSelecionaMenuBaliza())
               {
                  selecionaDeselecionaBaliza();
               }

            }
         }

         @Override
         public void mouseReleased(MouseEvent evt)
         {
            //desabilita variavel de verificaÃ§Ã£o para FALSE quando o evento de released Ã© chamado (soltar o botÃ£o do mouse)
            controladorEventoMouseBaliza.setMousePressedFoiAtivado(Boolean.FALSE);
         }
      });
   }

   private InterfaceBaliza obterBalizaClicada(java.awt.event.ActionEvent evt)
   {
      JMenuItem mnuItem = (JMenuItem) evt.getSource();
      JPopupMenu popupMenu = (JPopupMenu) mnuItem.getParent();
      return (InterfaceBaliza) popupMenu.getInvoker();
   }
   
   private void editaBalizaMenu(ActionEvent evt)
   {

      InterfaceBaliza balizaClicada = obterBalizaClicada(evt);
      if (balizaClicada.getBalizaVisualizada().equals(balizaVisualizada))
      {
         try
         {

            // verifica se exite pelo menos 1 baliza selecionada para edicao e se o modo edicao da fila de navios foi salvo
            controladorDSP.verificarModoDeEdicao();
            controladorDSP.verificarSelecaoDeBalizasParaEdicao();
            controladorDSP.getInterfaceInicial().ativaModoEdicao();

            controladorDSP.verificaListaBaliza(false);
            if(controladorDSP.verificaMaquinaOperandoGrupoBaliza()){
            	 JLabel pergunta = new JLabel(PropertiesUtil.getMessage("aviso.existe.maquina.operando.sobre.balizas.deseja.continuar"));
                 pergunta.setFont(new Font("Verdana", Font.PLAIN, 11));
                 int confirm = JOptionPane.showOptionDialog(
                		 controladorDSP.getInterfaceDSP().getInterfaceInicial(),
                         pergunta,
                         PropertiesUtil.getMessage("mensagem.option.pane.tipo.atencao") ,
                         JOptionPane.YES_OPTION,
                         JOptionPane.INFORMATION_MESSAGE,
                         null,
                         null,
                         null);

                 if (confirm == JOptionPane.YES_OPTION) {
                	 controladorDSP.editaDadosDeBalizas();
                 }
            }else{
            	controladorDSP.editaDadosDeBalizas();
            }            
            
         }
         catch (ModoDeEdicaoException mdex)
         {
        	 controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(mdex.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
         }
         catch (OperacaoCanceladaPeloUsuarioException operCancelEx)
         {
        	 controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
            interfaceMensagem.setDescricaoMensagem(operCancelEx.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
         }
         catch (BalizasSelecionadasException balizaSelEx)
         {
        	 controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            controladorDSP.getInterfaceInicial().desativarModoEdicao();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            interfaceMensagem.setDescricaoMensagem(balizaSelEx.getMessage());
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

   private void selecionaDeselecionaBaliza(MouseEvent evt)
   {
      if (evt.getButton() == MouseEvent.BUTTON1)
      {
         if (balizaSelecionada)
         {
            deselecionar();
         }
         else
         {
            selecionar();
         }
      }
   }

   /**
    * metodo que verifica se a baliza ja se encontra selecionada, em caso positivo "deseleciona" a baliza, caso contrario "seleciona"
    */
   public void selecionaDeselecionaBaliza()
   {
      if (balizaSelecionada)
      {
         deselecionar();
      }
      else
      {
         selecionar();
      }
   }

   @Override
   public void selecionar()
   {
      // sÃ³ seleciona uma baliza caso ela nÃ£o esteja selecionada!
      if (!balizaSelecionada)
      {
         try
         {
            if (interfacePatio.getPatioVisualizado().getEstado().equals(EstadoMaquinaEnum.OPERACAO))
            {
               if (balizaVisualizada.getEstado().equals(EstadoMaquinaEnum.OCIOSA))
               {
                  // o modo de operaÃ§Ã£o nula deve ser considerado como um modo de ediÃ§Ã£o
                  if (controladorDSP.getInterfaceDSP().getInterfaceInicial().getModoDeOperacao().equals(ModoDeOperacaoEnum.OPERACAONULA))
                  {
                     if (balizaVisualizada.getProduto() == null)
                     {
                        controladorDSP.getListaDeBalizas().add(this);
                        defineImagemDeSelecao();
                        balizaSelecionada = Boolean.TRUE;
                     }
                     else if (balizaVisualizada.getProduto() != null && balizaVisualizada.retornaStatusHorario(interfacePatio.getHoraSituacao()) != null)
                     {
                    	 
                    	controladorDSP.selecionaBaliza(balizaVisualizada);
                    	controladorDSP.getListaDeBalizas().add(this);

                        defineImagemDeSelecao();
                        balizaSelecionada = Boolean.TRUE;
                        verificaSeAPilhaDestaBalizaEstaCompletaESeleciona();// Verifica se as outras balizas desta pilha jÃ¡ estavam selecionadas. Se jÃ¡ estavam, marca a pilha como selecionada!
                     }
                  }
                  else if (controladorDSP.getInterfaceDSP().getInterfaceInicial().getModoDeOperacao().equals(ModoDeOperacaoEnum.RECUPERAR))
                  {
                     if (balizaVisualizada.getProduto() != null && balizaVisualizada.retornaStatusHorario(interfacePatio.getHoraSituacao()) != null)
                     {//verifica se a baliza selecionada para recuperacao possui produto e seu objeto pilha diferente de null
                        if (balizaVisualizada.getEstado() != EstadoMaquinaEnum.OPERACAO)
                        {//verifica se a baliza não esta em operacao em outra atividade
                           controladorDSP.selecionaBaliza(balizaVisualizada);
                           controladorDSP.getListaDeBalizas().add(this);

                           defineImagemDeSelecao();
                           balizaSelecionada = Boolean.TRUE;
                           verificaSeAPilhaDestaBalizaEstaCompletaESeleciona();// Verifica se as outras balizas desta pilha jÃ¡ estavam selecionadas. Se jÃ¡ estavam, marca a pilha como selecionada!
                        }
                        else
                        {
                           throw new EstruturaEmManutencaoException(PropertiesUtil.getMessage("mensagem.baliza.manutencao.ou.operacao"));
                        }
                     }
                     else
                     {
                        throw new BalizaInvalidaException(PropertiesUtil.getMessage("mensagem.baliza.vazia.recuperacao"));
                     }
                  }
                  else if (controladorDSP.getInterfaceDSP().getInterfaceInicial().getModoDeOperacao().equals(ModoDeOperacaoEnum.EMPILHAR))
                  {
                     if (balizaVisualizada.getProduto() == null)
                     {
                        controladorDSP.getListaDeBalizas().add(this);

                        defineImagemDeSelecao();
                        balizaSelecionada = Boolean.TRUE;
                     }
                     else if (balizaVisualizada.getProduto().getQuantidade() < balizaVisualizada.getCapacidadeMaxima())
                     {
                        controladorDSP.selecionaBaliza(balizaVisualizada);
                        controladorDSP.getListaDeBalizas().add(this);

                        defineImagemDeSelecao();
                        balizaSelecionada = Boolean.TRUE;
                        verificaSeAPilhaDestaBalizaEstaCompletaESeleciona();// [SA8753] - Verifica se as outras balizas desta pilha jÃ¡ estavam selecionadas. Se jÃ¡ estavam, marca a pilha como selecionada!
                     }
                     else
                     {
                        throw new BalizaInvalidaException(PropertiesUtil.getMessage("mensagem.baliza.completamente.preenchida"));
                     }
                  }
               }
               else
               {
                  throw new EstruturaEmManutencaoException(PropertiesUtil.getMessage("mensagem.baliza.manutencao.ou.operacao"));
               }
            }
            else
            {
               throw new EstruturaEmManutencaoException();
            }

            //this.repaint();
            controladorDSP.getInterfaceDSP().getInterfaceInicial().repaint();

         }
         catch (BlendagemInvalidaException e)
         {
        	 controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(e.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
         }
         catch (CampanhaIncompativelException ca)
         {
        	 controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(ca.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
         }
         catch (ProdutoIncompativelException po)
         {
        	 controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(po.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
         }
         catch (BalizaInvalidaException bi)
         {
        	 controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(bi.getMessage());
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
         catch (ExcessoDeMaterialParaEmbarqueException e)
         {
        	 controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(e.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
         }
      }
   }

   @Override
   public void deselecionar()
   {
      //sÃ³ se deseleciona caso a baliza esteja selecionada!
      if (balizaSelecionada)
      {
         try
         {
            controladorDSP.deselecionaBaliza(balizaVisualizada);
            this.setIcon(null);
            balizaSelecionada = Boolean.FALSE;
            controladorDSP.getListaDeBalizas().remove(this);
            deselecionarPilhaDestaBaliza(); //se a pilha desta baliza estiver selecionada, deselecionar!
            }
         catch (BlendagemInvalidaException e)
         {
        	 controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(e.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
         }
         catch (ProdutoIncompativelException po)
         {
        	 controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(po.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
         }
         catch (CampanhaIncompativelException ca)
         {
        	 controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(ca.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
         }
      }
   }

   /**
    * Seleciona a pilha que contÃ©m esta baliza caso o resto dela esteja selecionado
    */
   public void verificaSeAPilhaDestaBalizaEstaCompletaESeleciona()
   {
      //[SA8753] - Ao selecionar a Ãºltima baliza que estava faltando para a pilha ficar toda selecionada, a pilha deve ser marcada como selecionada!
      if (interfacePilha != null)
      {
         boolean oRestoDaPilhaEstahSelecionado = true;
         List<InterfaceBaliza> listaDeBalizasDaPilhaDestaBaliza = interfacePilha.getListaDeBalizas();

         for (Iterator<InterfaceBaliza> it = listaDeBalizasDaPilhaDestaBaliza.iterator(); it.hasNext() && oRestoDaPilhaEstahSelecionado;)
         {
            InterfaceBaliza outraBalizaDestaPilha = it.next();
            //Testa todas as outras balizas da pilha a qual esta baliza pertence.
            if (this != outraBalizaDestaPilha)
            {
               //Caso qualquer uma dessas balizas esteja deselecionada, significa que o resto da pilha NÃƒO estÃ¡ selecionado
               if (!outraBalizaDestaPilha.isSelecionada())
               {
                  oRestoDaPilhaEstahSelecionado = false;
               }
            }
         }

         //se o resto da pilha esta selecionado, marcamos a pilha como selecionada!
         if (oRestoDaPilhaEstahSelecionado)
         {
            Boolean pilhaSelecionada = interfacePilha.getPilhaSelecionada();
            if (!pilhaSelecionada)
            {
               interfacePilha.setPilhaSelecionada(Boolean.TRUE);
            }
         }
      }
   }

   /**
    * Deselecionar uma eventual pilha que esteja selecionada e contendo esta baliza
    */
   public void deselecionarPilhaDestaBaliza()
   {
      //Ao deselecionar uma Ãºnica baliza, a pilha jÃ¡ deixa de estar selecionada!
      if (interfacePilha != null)
      {
         Boolean pilhaSelecionada = interfacePilha.getPilhaSelecionada();
         if (pilhaSelecionada)
         {
            interfacePilha.setPilhaSelecionada(Boolean.FALSE);
         }
      }
   }

   @Override
   public Boolean isSelecionada()
   {
      return balizaSelecionada;
   }

   @Override
   public void setSelecionada(Boolean selecionada)
   {
      this.balizaSelecionada = selecionada;
   }

   public Baliza getBalizaVisualizada()
   {
      return balizaVisualizada;
   }

   public void setBalizaVisualizada(Baliza balizaVisualizada)
   {
      this.balizaVisualizada = balizaVisualizada;
   }

   public ControladorDSP getControladorDSP()
   {
      return controladorDSP;
   }

   public void setControladorDSP(ControladorDSP controladorDSP)
   {
      this.controladorDSP = controladorDSP;
   }

   public InterfacePatio getInterfacePatio()
   {
      return interfacePatio;
   }

   public void setInterfacePatio(InterfacePatio interfacePatio)
   {
      this.interfacePatio = interfacePatio;
   }

   public InterfacePilha getInterfacePilha()
   {
      return interfacePilha;
   }

   public void setInterfacePilha(InterfacePilha interfacePilha)
   {
      this.interfacePilha = interfacePilha;
   }

   public ControladorEventoMouseBaliza getControladorEventoMouseBaliza()
   {
      return controladorEventoMouseBaliza;
   }

   public void setControladorEventoMouseBaliza(ControladorEventoMouseBaliza controladorEventoMouseBaliza)
   {
      this.controladorEventoMouseBaliza = controladorEventoMouseBaliza;
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

   /**
    * Metodo acionado no momento que clicamos no menu exibir rastreabilidade de uma
    * interface baliza
    * @param evt
    */
   private void exibirRastreabilidadeBalizaActionPerformed(ActionEvent evt)
   {
      try
      {
         if (balizaVisualizada.getProduto() != null && balizaVisualizada.getProduto().getListaDeRastreabilidades() != null && !balizaVisualizada.getProduto().getListaDeRastreabilidades().isEmpty())
         {
            InterfaceRastreabilidade interfaceRastreabilidade = new InterfaceRastreabilidade(controladorDSP.getInterfaceInicial().getInterfaceInicial(), true, balizaVisualizada.getProduto().getListaDeRastreabilidades());
            interfaceRastreabilidade.setControladorInterfaceInicial(controladorDSP.getInterfaceInicial());
            interfaceRastreabilidade.setLocationRelativeTo(null);
            interfaceRastreabilidade.setVisible(true);
         }
         else
         {
            throw new RastreabilidadeException(PropertiesUtil.getMessage("mensagem.produto.rastreabilidade.nao.definida"));
         }
      }
      catch (RastreabilidadeException rastEx)
      {
    	  controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
         interfaceMensagem = new InterfaceMensagem();
         interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
         interfaceMensagem.setDescricaoMensagem(rastEx.getMessage());
         controladorDSP.ativarMensagem(interfaceMensagem);
      }
      catch (ErroSistemicoException errSis)
      {
    	  controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
         interfaceMensagem = new InterfaceMensagem();
         interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
         interfaceMensagem.setDescricaoMensagem(errSis.getMessage());
         controladorDSP.ativarMensagem(interfaceMensagem);
      }
   }

   private void defineImagemDeSelecao()
   {
      this.setIcon(new javax.swing.ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens") + "/selecionar_pilha.png"));
   }

   @Override
   public String toString()
   {
      return balizaVisualizada.getNomeBaliza();
   }

   /**
    * Metodo acionado no momento que clicamos no menu exibir rastreabilidade de uma
    * interface baliza
    * @param evt
    */
   private void tratarPSMBalizaActionPerformed(ActionEvent evt)
   {
      // Recupera as balizas que possuem PSM no patio visualizado
     List<Baliza> listBalizasPSM = controladorDSP.getInterfaceInicial().getBalizasProdutoPatio(balizaVisualizada.getPatio(), TipoDeProdutoEnum.PR,interfacePatio.getHoraSituacao());

     // Recupera todas as balizas do patio
     List<Baliza> listaBalizaDoPatio = balizaVisualizada.getPatio().getListaDeBalizas(interfacePatio.getHoraSituacao());
     List<Baliza> listaBalizas = new ArrayList<Baliza>();
     for (Baliza baliza : listaBalizaDoPatio)
     {
        if (baliza.getProduto() != null)
        {
           if (baliza.getProduto().getTipoProduto().getTipoDeProduto().equals(TipoDeProdutoEnum.PELLET_PSM) ||
                 baliza.getProduto().getTipoProduto().getTipoDeProduto().equals(TipoDeProdutoEnum.PELOTA_PSM))
           {
              listaBalizas.add(baliza);
           }
        }
        else
        {
           listaBalizas.add(baliza);
        }
     }

     if (!listBalizasPSM.isEmpty())
     {
/*        	 *//**
    	  * 
    	  * TODO InterfaceTratamentoPSM REVER TRATAMENTO PSM MAQUINAS
    	  *//*
    	 //final InterfaceTratamentoPSM interfaceTratamentoPSM = new InterfaceTratamentoPSM(controladorDSP, listBalizasPSM, listaBalizas, new ArrayList<MaquinaDoPatio>());
    	 final InterfaceTratamentoPSM interfaceTratamentoPSM = new InterfaceTratamentoPSM(controladorDSP, listBalizasPSM, listaBalizas, balizaVisualizada.getPatio().getListaDeMaquinasDoPatio())
        {

           *//**
    		 * 
    		 *//*
    		private static final long serialVersionUID = 1L;

    	@Override
           public void fecharJanela()
           {
              controladorDSP.getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida().setStatusEdicao(Boolean.FALSE);
              this.setOperacaoCanceladaPeloUsuario(Boolean.TRUE);
           }
        };

        JDialog dialogTratamentoPSM = new JDialog(controladorDSP.getInterfaceDSP().getInterfaceInicial(), true);
        dialogTratamentoPSM.addWindowListener(new java.awt.event.WindowAdapter()
        {

           @Override
           public void windowClosing(java.awt.event.WindowEvent evt)
           {
              controladorDSP.getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida().setStatusEdicao(Boolean.FALSE);
              interfaceTratamentoPSM.setOperacaoCanceladaPeloUsuario(Boolean.TRUE);
           }
        });

        dialogTratamentoPSM.setLayout(new BorderLayout());
        dialogTratamentoPSM.setTitle("Informações para tratamento de PSM");
        dialogTratamentoPSM.getContentPane().add(interfaceTratamentoPSM);
        dialogTratamentoPSM.pack();
        dialogTratamentoPSM.setLocationRelativeTo(null);
        dialogTratamentoPSM.setVisible(true);

        // A operacao de edicao da baliza foi cancelada pelo usuÃ¡rio
        if (interfaceTratamentoPSM.getOperacaoCanceladaPeloUsuario())
        {
           dialogTratamentoPSM.setVisible(false);
           controladorDSP.getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida().setStatusEdicao(Boolean.FALSE);
           controladorDSP.getInterfaceInicial().getInterfaceComandos().cancelarModoOperacao();
           throw new OperacaoCanceladaPeloUsuarioException(PropertiesUtil.buscarPropriedade("exception.operacao.cancelada.pelo.usuario"));
        }

        new Thread("Thread tratar PSM baliza")
        {
           @Override
           public void run()
           {
              try
              {

                 controladorDSP.getInterfaceInicial().setModoOperacao(ModoDeOperacaoEnum.ATUALIZACAO);
                 controladorDSP.getInterfaceInicial().getInterfaceComandos().desabilitarTodasFuncoes();
                 ativaMensagem(PropertiesUtil.getMessage("mensagem.processamento.tratamento.psm"));

                 DadosTratamentoPSM dadosTratamento = interfaceTratamentoPSM.getDadosTratamentoPSM();

                 Atividade atividadeMovimentaPSM = criarAtividade(dadosTratamento);

                 controladorDSP.getInterfaceInicial().tratamentoPSM(atividadeMovimentaPSM);

                 controladorDSP.getInterfaceInicial().setModoOperacao(ModoDeOperacaoEnum.OPERACAONULA);
                 controladorDSP.getInterfaceInicial().getInterfaceInicial().desativarMensagem();
                 controladorDSP.getInterfaceInicial().getInterfaceComandos().habilitarTodasFuncoes();

                 controladorDSP.getInterfaceInicial().getInterfaceInicial().atualizarDSP();
              }
              catch (AtividadeException ex)
              {
            	  controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
                 interfaceMensagem = new InterfaceMensagem();
                 interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
                 interfaceMensagem.setDescricaoMensagem(ex.getMessage());
                 controladorDSP.ativarMensagem(interfaceMensagem);
              }
           }
        }.start();
*/
     }
     else
     {
    	 controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
        interfaceMensagem = new InterfaceMensagem();
        interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
        interfaceMensagem.setDescricaoMensagem(PropertiesUtil.getMessage("mensagem.patio.nao.possui.psm"));
        controladorDSP.ativarMensagem(interfaceMensagem);
     }
   }

   /**
    * Cria a atividade de tratamento de PSM.
    *
    * @param totalMovimentacao
    * @return atividade
    */
   private Atividade criarAtividade(DadosTratamentoPSM dadosTratamentoPSM)
   {
      Atividade atividade = new Atividade();
      
      atividade.addLugarEmpilhamento(this.defineBalizasPellet(dadosTratamentoPSM));
      atividade.addLugarEmpilhamento(this.defineBalizasPelotas(dadosTratamentoPSM));
      atividade.addLugarEmpilhamento(this.defineBalizasLixo(dadosTratamentoPSM));
      
      atividade.addLugarEmpilhamento(this.defineBalizasTratadas(dadosTratamentoPSM));

      atividade.setTipoAtividade(TipoAtividadeEnum.TRATAMENTO_PSM);

      return atividade;
   }

   /**
    * Define o lugar de destino do pellet screening resultante do tratamento do
    * PSM.
    *
    * @param totalMovimentacao
    * @return lugarEmpilhamento
    */
   private LugarEmpilhamentoRecuperacao defineBalizasPellet(DadosTratamentoPSM dadosTratamentoPSM)
   {
      LugarEmpilhamentoRecuperacao lugarEmpilhamento = new LugarEmpilhamentoRecuperacao();

      //lugarEmpilhamento.setListaDeBalizas(new ArrayList<Baliza>());
      //lugarEmpilhamento.getListaDeBalizas().add(dadosTratamentoPSM.getBalizaInicialPellet());
      lugarEmpilhamento.addPilhaVirtual(dadosTratamentoPSM.getBalizaInicialPellet());
      lugarEmpilhamento.setSentido(dadosTratamentoPSM.getSentidoPellet());
      lugarEmpilhamento.setNomeDoLugarEmpRec(dadosTratamentoPSM.getNomePilhaPellet());
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
         if (tipoProduto.getTipoDeProduto().equals(TipoDeProdutoEnum.PELLET_PSM))
         {
            //lugarEmpilhamento.setTipoDeProduto(tipoProduto);
         }
      }

      return lugarEmpilhamento;
   }

   /**
    * Define o lugar de destino das Pelotas resultantes do tratamento do PSM.
    *
    * @param dadosTratamentoPSM
    * @return lugarEmpilhamento
    */
   private LugarEmpilhamentoRecuperacao defineBalizasPelotas(DadosTratamentoPSM dadosTratamentoPSM)
   {
      LugarEmpilhamentoRecuperacao lugarEmpilhamento = new LugarEmpilhamentoRecuperacao();

      //lugarEmpilhamento.setListaDeBalizas(new ArrayList<Baliza>());
      lugarEmpilhamento.addPilhaVirtual(dadosTratamentoPSM.getBalizaInicialProduto());
      //lugarEmpilhamento.getListaDeBalizas().add(dadosTratamentoPSM.getBalizaInicialProduto());
      lugarEmpilhamento.setSentido(dadosTratamentoPSM.getSentidoProduto());
      lugarEmpilhamento.setNomeDoLugarEmpRec(dadosTratamentoPSM.getNomePilhaProduto());
     // lugarEmpilhamento.setTaxaDeOperacaoNaPilha(0.0d);
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
         if (tipoProduto.getTipoDeProduto().equals(TipoDeProdutoEnum.PELOTA_PSM))
         {
            //lugarEmpilhamento.setTipoDeProduto(tipoProduto);
         }
      }

      return lugarEmpilhamento;
   }

   /**
    * Define o lugar de destino das Pelotas resultantes do tratamento do PSM.
    *
    * @param dadosTratamentoPSM
    * @return lugarEmpilhamento
    */
   private LugarEmpilhamentoRecuperacao defineBalizasLixo(DadosTratamentoPSM dadosTratamentoPSM)
   {
      LugarEmpilhamentoRecuperacao lugarEmpilhamento = new LugarEmpilhamentoRecuperacao();

      ///lugarEmpilhamento.setListaDeBalizas(new ArrayList<Baliza>());
      //lugarEmpilhamento.getListaDeBalizas().add(dadosTratamentoPSM.getBalizaInicialLixo());
      lugarEmpilhamento.addPilhaVirtual(dadosTratamentoPSM.getBalizaInicialProduto());
      lugarEmpilhamento.setSentido(dadosTratamentoPSM.getSentidoLixo());
      lugarEmpilhamento.setNomeDoLugarEmpRec(dadosTratamentoPSM.getNomePilhaLixo());
     // lugarEmpilhamento.setTaxaDeOperacaoNaPilha(0.0d);
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
         if (tipoProduto.getTipoDeProduto().equals(TipoDeProdutoEnum.LIXO))
         {
            //lugarEmpilhamento.setTipoDeProduto(tipoProduto);
         }
      }

      return lugarEmpilhamento;
   }

   /**
    * Define o lugar onde o produto esta sendo tratado.
    *
    * @param totalMovimentacao
    * @return listaLugaresRecuperacao
    */
   private List<LugarEmpilhamentoRecuperacao> defineBalizasTratadas(DadosTratamentoPSM dadosTratamentoPSM)
   {
      List<LugarEmpilhamentoRecuperacao> listaLugaresRecuperacao = new ArrayList<LugarEmpilhamentoRecuperacao>();

      for (ProdutoMovimentacao produtoMovimentacao : dadosTratamentoPSM.getListaProdutoTratado())
      {
         LugarEmpilhamentoRecuperacao lugarRecuperacao = new LugarEmpilhamentoRecuperacao();
         //lugarRecuperacao.setListaDeBalizas(new ArrayList<Baliza>());
        // lugarRecuperacao.setNomeDoLugarEmpRec(produtoMovimentacao.getBalizaSelecionada().getPilha().getNomePilha());
         lugarRecuperacao.addPilhaVirtual(produtoMovimentacao.getBalizaSelecionada());
         //lugarRecuperacao.getListaDeBalizas().add(produtoMovimentacao.getBalizaSelecionada());
         //lugarRecuperacao.setTipoDeProduto(produtoMovimentacao.getBalizaSelecionada().getProduto().getTipoProduto());
         lugarRecuperacao.setQuantidade(produtoMovimentacao.getQuantidadeMovimentacao());
         //lugarRecuperacao.setTaxaDeOperacaoNaPilha(0.0d);
         lugarRecuperacao.setExecutado(Boolean.FALSE);

         listaLugaresRecuperacao.add(lugarRecuperacao);
      }

      return listaLugaresRecuperacao;
   }

   /**
    * Metodo ativado quando clicado sobre o menu de interdicao de baliza
    *
    * @param event
    */
   private void interditarBalizaActionPerformed(ActionEvent evt)
   {
      try
      {
         controladorDSP.interditarBaliza(this.getInterfacePatio());
      }
      catch (OperacaoCanceladaPeloUsuarioException ex)
      {
    	  controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
         interfaceMensagem = new InterfaceMensagem();
         interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
         interfaceMensagem.setDescricaoMensagem(ex.getMessage());
         controladorDSP.ativarMensagem(interfaceMensagem);
      }
      catch (ErroSistemicoException ex)
      {
    	  controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
         interfaceMensagem = new InterfaceMensagem();
         interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
         interfaceMensagem.setDescricaoMensagem(ex.getMessage());
         controladorDSP.ativarMensagem(interfaceMensagem);
      }
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

   private void editaNomePilha(ActionEvent evt)
   {
      boolean controle = Boolean.FALSE;
      while (!controle)
      {
         try
         {
            InterfaceBaliza iBaliza = obterBalizaClicada(evt);
            if (iBaliza.getBalizaVisualizada().retornaStatusHorario(interfacePatio.getHoraSituacao()) != null)
            {
               String nomePilha = JOptionPane.showInputDialog(null,
                     PropertiesUtil.getMessage("mensagem.option.pane.informe.nome.pilha"),
                     PropertiesUtil.getMessage("mensagem.option.pane.title.edicao"),
                     JOptionPane.QUESTION_MESSAGE);
               if (nomePilha == null)
               {
                  controle = Boolean.TRUE;
               }
               else if (nomePilha.trim().isEmpty())
               {
                  throw new ValidacaoCampoException(PropertiesUtil.getMessage("aviso.nome.pilha.vazio"));
               }
               else
               {
                  this.balizaVisualizada.retornaStatusHorario(interfacePatio.getHoraSituacao()).setNomePilha(nomePilha);
                  controladorDSP.getInterfaceInicial().getInterfaceComandos().finalizarEdicoes();
                  controle = Boolean.TRUE;
               }
            }

         }
         catch (ValidacaoCampoException ex)
         {
        	 controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
         }
      }
   }

   private void transportarPilhaEmergencia(ActionEvent evt)
   {
      InterfaceBaliza iBaliza = obterBalizaClicada(evt);
      /*if (iBaliza.getBalizaVisualizada().getPilha() != null)
      {
         try
         {
            controladorDSP.transportarPilhaEmergencia(this.interfacePilha);
            controladorDSP.getInterfaceInicial().exibirUltimaSituacaoPatio();
         }
         catch (OperacaoCanceladaPeloUsuarioException ex)
         {
        	 controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
         }
         catch (ErroSistemicoException ex)
         {
        	 controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
         }
      }
*/   }

   private void desabilitaMenusPermissaoUsuario()
   {
       if(controladorDSP.getInterfaceInicial().verificaPermissaoAtualizacaoProducao()){
           desabilitarMenus();
           if(mnuSelecionaPilha  != null) mnuSelecionaPilha.setEnabled(true);
           if(mnuDeselecionaPilha != null) mnuDeselecionaPilha.setEnabled(true);
           if(mnuExibirRastreabilidade != null) mnuExibirRastreabilidade.setEnabled(true);
           if(mnuUnificarPilha != null) mnuUnificarPilha.setEnabled(true);
       }
   }

   private void mnuUnificarPilhaActionPerformed(ActionEvent evt)
   {
      try
      {
         controladorDSP.verificaListaBaliza(true);
         controladorDSP.unificarPilhas();
      }
      catch (OperacaoCanceladaPeloUsuarioException ex)
      {
    	  controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
         interfaceMensagem = new InterfaceMensagem();
         interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
         interfaceMensagem.setDescricaoMensagem(ex.getMessage());
         controladorDSP.ativarMensagem(interfaceMensagem);
      }
      catch (ErroSistemicoException ex)
      {
    	  controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
         interfaceMensagem = new InterfaceMensagem();
         interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
         interfaceMensagem.setDescricaoMensagem(ex.getMessage());
         controladorDSP.ativarMensagem(interfaceMensagem);
      }
      catch (BalizasSelecionadasException balizaSelEx)
      {
    	  controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
         interfaceMensagem = new InterfaceMensagem();
         interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
         interfaceMensagem.setDescricaoMensagem(balizaSelEx.getMessage());
         controladorDSP.ativarMensagem(interfaceMensagem);
      }
   }
}
