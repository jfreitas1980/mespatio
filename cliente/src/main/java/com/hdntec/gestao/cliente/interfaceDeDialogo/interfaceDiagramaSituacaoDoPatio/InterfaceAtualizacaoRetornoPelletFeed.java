package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio;

import java.awt.Color;
import java.awt.Font;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.text.MaskFormatter;

import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.comparadores.ComparadorInterfacePilha;
import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.cliente.util.numeros.DSSStockyardFuncoesNumeros;
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.entity.LugarEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.enums.SentidoEmpilhamentoRecuperacaoEnum;
import com.hdntec.gestao.domain.plano.enums.TipoAtividadeEnum;
import com.hdntec.gestao.domain.planta.entity.MetaBaliza;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.Pilha;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.domain.produto.enums.TipoDeProdutoEnum;
import com.hdntec.gestao.domain.vo.atividades.MovimentacaoVO;
import com.hdntec.gestao.exceptions.AtividadeException;
import com.hdntec.gestao.exceptions.PilhaPelletFeedNaoEncontradaException;
import com.hdntec.gestao.exceptions.ValidacaoCampoException;
import com.hdntec.gestao.exceptions.ValidacaoObjetosOperacaoException;
import com.hdntec.gestao.services.controladores.impl.ControladorExecutarAtividadeMovimentacaoPilha;
import com.hdntec.gestao.util.PropertiesUtil;
import com.hdntec.gestao.util.datahora.DSSStockyardTimeUtil;

public class InterfaceAtualizacaoRetornoPelletFeed extends javax.swing.JDialog
{

   /** controlador das operacoes do DSP */
   private ControladorDSP controladorDSP;

   /** Interface maquina que executara a atividade */
   private InterfaceMaquinaDoPatio interfaceMaquinaDoPatio;

   /** Interface do patio onde a maquina se encontra para realizar o retorno */
   private InterfacePatio interfacePatio;

   /** A pilha selecionada para retorno */
   private Pilha interfacePilhaSelecionda;

   /** Controla se a edicao foi cancelada ou nao */
   private Boolean operacaoCanceladaPeloUsuario = Boolean.FALSE;

   /** Mapa contento a interface das balizas da pilha selecionada */
   private HashMap<Integer, Baliza> mapaInterfaceBalizasPilha;

   /** A atividade de retorno de pellet feed a ser executada */
   private Atividade atividadeRetornoPelletFeed;

   private Atividade atividadeExecutada;
   
   /** A quantidade total de produto existente entre as balizas selecionadas */
   private double quantidadeTotalProdutosBaliza = 0D;

   /** a interface de mensagem */
   private InterfaceMensagem interfaceMensagem;

   public InterfaceAtualizacaoRetornoPelletFeed(java.awt.Frame parent, boolean modal, ControladorDSP controladorDSP, InterfaceMaquinaDoPatio interfaceMaquinaDoPatio) throws PilhaPelletFeedNaoEncontradaException
   {
      super(parent, modal);
      initComponents();
      this.setTitle(PropertiesUtil.getMessage("tilulo.tela.atualizacao.retorno.pellet.feed"));
      this.controladorDSP = controladorDSP;
      this.interfaceMaquinaDoPatio = interfaceMaquinaDoPatio;
      this.interfacePatio = interfaceMaquinaDoPatio.getPatio();
      this.mapaInterfaceBalizasPilha = new HashMap<Integer, Baliza>();
      inicializarRetornoPelletFeed();
      atividadeExecutada = interfaceMaquinaDoPatio.getMaquinaDoPatioVisualizada().getMetaMaquina().existeMovimentacaoRetornoPelletFeedPendente();
      if (atividadeExecutada != null)
      {
         carregarInformacoesAtividade();
      }
      
      
   }

   private void carregarInformacoesAtividade()
   {  
                
      if (atividadeExecutada.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0) != null) 
      {
          LugarEmpilhamentoRecuperacao lugarEmpRec = atividadeExecutada.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0);
          
          
          //selecionando na combo de pilha origem a pilha origem
          MetaBaliza metaBalizaOperacao = atividadeExecutada.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaDeBalizas().get(0).getMetaBaliza();
          
          Baliza balizaOperacao = metaBalizaOperacao.retornaStatusHorario(atividadeExecutada.getDtInicio());
          
          int idxGerado = metaBalizaOperacao.getListaStatus().indexOf(balizaOperacao);
          
          balizaOperacao = metaBalizaOperacao.getListaStatus().get(idxGerado-1);
                              
          Pilha pilhaOrigem = balizaOperacao.retornaStatusHorario(atividadeExecutada.getDtInicio());
          
          cmbPilha.setSelectedItem(pilhaOrigem);
          cmbPilha.setEnabled(false);
          
          // setando o nome da pilha
          txtHoraInicial.setText(DSSStockyardTimeUtil.formatarData(atividadeExecutada.getDtInicio(), PropertiesUtil.buscarPropriedade("formato.campo.datahora")));
          txtHoraFinal.setText(DSSStockyardTimeUtil.formatarData(DSSStockyardTimeUtil.obterDataHoraUltimaSituacaoParaAtividades(controladorDSP.getInterfaceInicial().getInterfaceInicial().getSituacaoPatioExibida().getDtInicio()), PropertiesUtil.buscarPropriedade("formato.campo.datahora")));
          txtHoraFinal.setSelectionStart(11); // comeca a selecao no inicio da hora
          txtHoraFinal.setSelectionEnd(txtHoraFinal.getText().length());
          txtHoraInicial.setEnabled(false);
          // setando o tipo de produto selecionado
          cmbBalizaInicial.setEnabled(false);          
          //Baliza balizaInicialOrigem = lugarEmpRec.getListaDeBalizas().get(0);
          cmbBalizaInicial.setSelectedItem(balizaOperacao);
          cmbBalizaFinal.setEnabled(true);
          txtHoraFinal.setEnabled(true);
          txtQuantidade.setEnabled(true);          
          txtHoraFinal.requestFocus();
          
      }   
   }

   
   private void inicializarRetornoPelletFeed() throws PilhaPelletFeedNaoEncontradaException
   {
      carregarComboPilhas();
      // Nao foi encontrada nenhuma pilha de pellet feed no patio onde a maquina se encontra
      if (cmbPilha.getItemCount() == 0)
      {
         throw new PilhaPelletFeedNaoEncontradaException(PropertiesUtil.getMessage("mensagem.exception.pilha.pellet.feed.nao.econtrada"));
      }
      carregarComboBalizas();
      lblNomePatioOrigem.setText(interfaceMaquinaDoPatio.getPatio().getPatioVisualizado().getNomePatio());
   }

   /**
    * Metodo auxiliar que carrega o combo com a lista de pilhas de pellet feed do pátio onde a maquina se encontra
    */
   private void carregarComboPilhas() 
   {

      // ... seta a hora final como sendo a data/hora atual da realizacao da operacao
       txtHoraFinal.setEnabled(false);
       txtHoraInicial.setText(DSSStockyardTimeUtil.formatarData(DSSStockyardTimeUtil.obterDataHoraUltimaSituacaoParaAtividades(controladorDSP.getInterfaceInicial().getInterfaceInicial().getSituacaoPatioExibida().getDtInicio()), PropertiesUtil.buscarPropriedade("formato.campo.datahora")));
       txtHoraInicial.setSelectionStart(11); // comeca a selecao no inicio da hora
       txtHoraInicial.setSelectionEnd(txtHoraInicial.getText().length());
       cmbPilha.removeAllItems();
       cmbBalizaFinal.setEnabled(false);
       txtQuantidade.setEnabled(false);
	InterfacePatio interfacePatioDaMaquina = interfaceMaquinaDoPatio.getPosicao().getInterfacePatio();
	Collections.sort(interfacePatioDaMaquina.getListaDePilhas(), new ComparadorInterfacePilha());

      for (InterfacePilha interfacePilhaPatio : interfacePatioDaMaquina.getListaDePilhas())
      {
         // verifica se a pilha pertence ao pátio onde a maquina esta localizada
         if (interfacePilhaPatio.getPilhaVisualizada().verificarPatioDaPilha(interfaceMaquinaDoPatio.getPatio().getPatioVisualizado())) {
            InterfaceBaliza interfaceBaliza = new LinkedList<InterfaceBaliza>(interfacePilhaPatio.getListaDeBalizas()).getFirst();
            // verifica se o tipo de produto da pilha eh do tipo peelet feed
            if (interfaceBaliza.getBalizaVisualizada().getProduto().getTipoProduto().getTipoDeProduto().equals(TipoDeProdutoEnum.PELLET_FEED)) {
               cmbPilha.addItem(interfacePilhaPatio.getPilhaVisualizada());
            }
         }
      }
   }

   /** Carrega o combo de balizas inicial e final de acordo com a
    * pilha selecionada para retorno
    */
   private void carregarComboBalizas()
   {
      cmbBalizaInicial.removeAllItems();
      cmbBalizaFinal.removeAllItems();

      mapaInterfaceBalizasPilha.clear();

      // ordena a lista de balizas da pilha selecionada
      Collections.sort(interfacePilhaSelecionda.getListaDeBalizas(), Baliza.comparadorBaliza);
      for (Baliza interfaceBalizaPilha : interfacePilhaSelecionda.getListaDeBalizas())
      {
         mapaInterfaceBalizasPilha.put(interfaceBalizaPilha.getNumero(), interfaceBalizaPilha);
         cmbBalizaInicial.addItem(interfaceBalizaPilha);
         cmbBalizaFinal.addItem(interfaceBalizaPilha);
      }
   }

   @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      pnlPrincipal = new javax.swing.JPanel();
      pnlSelecaoBalizas = new javax.swing.JPanel();
      jLabel1 = new javax.swing.JLabel();
      cmbPilha = new javax.swing.JComboBox();
      jLabel2 = new javax.swing.JLabel();
      cmbBalizaInicial = new javax.swing.JComboBox();
      jLabel3 = new javax.swing.JLabel();
      cmbBalizaFinal = new javax.swing.JComboBox();
      jLabel4 = new javax.swing.JLabel();
      lblNomePatioOrigem = new javax.swing.JLabel();
      lblCorTipoProdutoPilha = new javax.swing.JLabel();
      pnlHorarios = new javax.swing.JPanel();
      jLabel5 = new javax.swing.JLabel();
      txtHoraInicial = new javax.swing.JFormattedTextField();
      MaskFormatter fmtHoraInicio = new MaskFormatter();
      try {
         fmtHoraInicio.setMask(PropertiesUtil.buscarPropriedade("mascara.campo.datahora"));
         fmtHoraInicio.setPlaceholderCharacter(' ');
         fmtHoraInicio.install(txtHoraInicial);
      } catch (ParseException pex) {
         pex.printStackTrace();
      }
      jLabel6 = new javax.swing.JLabel();
      txtHoraFinal = new javax.swing.JFormattedTextField();
      MaskFormatter fmtHoraFinal = new MaskFormatter();
      try {
         fmtHoraFinal.setMask(PropertiesUtil.buscarPropriedade("mascara.campo.datahora"));
         fmtHoraFinal.setPlaceholderCharacter(' ');
         fmtHoraFinal.install(txtHoraFinal);
      } catch (ParseException pex) {
         pex.printStackTrace();
      }
      jLabel7 = new javax.swing.JLabel();
      txtQuantidade = new javax.swing.JTextField();
      cmdDesistir = new javax.swing.JButton();
      cmdConfirmar = new javax.swing.JButton();

      setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
      addWindowListener(new java.awt.event.WindowAdapter() {
         @Override
        public void windowClosing(java.awt.event.WindowEvent evt) {
            formWindowClosing(evt);
         }
      });

      pnlSelecaoBalizas.setBorder(javax.swing.BorderFactory.createTitledBorder(PropertiesUtil.getMessage("titulo.painel.retorno.pellet.feed.balizas.retorno")));

      jLabel1.setFont(new java.awt.Font("Arial", 1, 12));
      jLabel1.setText(PropertiesUtil.getMessage("label.texto.selecao.pilha"));

      cmbPilha.setFont(new java.awt.Font("Arial", 0, 12));
      cmbPilha.addItemListener(new java.awt.event.ItemListener() {
         public void itemStateChanged(java.awt.event.ItemEvent evt) {
            cmbPilhaItemStateChanged(evt);
         }
      });

      jLabel2.setFont(new java.awt.Font("Arial", 1, 12));
      jLabel2.setText(PropertiesUtil.getMessage("label.texto.selecao.baliza.inicial"));

      cmbBalizaInicial.setFont(new java.awt.Font("Arial", 0, 12));
      cmbBalizaInicial.addItemListener(new java.awt.event.ItemListener() {
         public void itemStateChanged(java.awt.event.ItemEvent evt) {
            cmbBalizaInicialItemStateChanged(evt);
         }
      });

      jLabel3.setFont(new java.awt.Font("Arial", 1, 12));
      jLabel3.setText(PropertiesUtil.getMessage("label.texto.selecao.baliza.final"));

      cmbBalizaFinal.setFont(new java.awt.Font("Arial", 0, 12));
      cmbBalizaFinal.addItemListener(new java.awt.event.ItemListener() {
         public void itemStateChanged(java.awt.event.ItemEvent evt) {
            cmbBalizaFinalItemStateChanged(evt);
         }
      });

      jLabel4.setFont(new java.awt.Font("Arial", 1, 12));
      jLabel4.setText(PropertiesUtil.getMessage("label.texto.informacao.nome.patio"));

      lblNomePatioOrigem.setFont(new java.awt.Font("Arial", 1, 12));
      lblNomePatioOrigem.setText("Nome Patio");

      lblCorTipoProdutoPilha.setOpaque(true);

      javax.swing.GroupLayout pnlSelecaoBalizasLayout = new javax.swing.GroupLayout(pnlSelecaoBalizas);
      pnlSelecaoBalizas.setLayout(pnlSelecaoBalizasLayout);
      pnlSelecaoBalizasLayout.setHorizontalGroup(
         pnlSelecaoBalizasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(pnlSelecaoBalizasLayout.createSequentialGroup()
            .addContainerGap()
            .addGroup(pnlSelecaoBalizasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
               .addComponent(jLabel2, 0, 0, Short.MAX_VALUE)
               .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(pnlSelecaoBalizasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(cmbPilha, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(cmbBalizaInicial, 0, 148, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(pnlSelecaoBalizasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
               .addGroup(pnlSelecaoBalizasLayout.createSequentialGroup()
                  .addComponent(lblCorTipoProdutoPilha, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                  .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(lblNomePatioOrigem, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
               .addGroup(pnlSelecaoBalizasLayout.createSequentialGroup()
                  .addGap(18, 18, 18)
                  .addComponent(jLabel3)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(cmbBalizaFinal, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );
      pnlSelecaoBalizasLayout.setVerticalGroup(
         pnlSelecaoBalizasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(pnlSelecaoBalizasLayout.createSequentialGroup()
            .addGroup(pnlSelecaoBalizasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(pnlSelecaoBalizasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                  .addComponent(jLabel1)
                  .addComponent(cmbPilha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addComponent(jLabel4)
                  .addComponent(lblNomePatioOrigem))
               .addComponent(lblCorTipoProdutoPilha, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(pnlSelecaoBalizasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel2)
               .addComponent(jLabel3)
               .addComponent(cmbBalizaFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(cmbBalizaInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );

      pnlHorarios.setBorder(javax.swing.BorderFactory.createTitledBorder(PropertiesUtil.getMessage("titulo.painel.retorno.pellet.feed.horarios")));

      jLabel5.setFont(new java.awt.Font("Arial", 1, 12));
      jLabel5.setText(PropertiesUtil.getMessage("label.texto.horario.retorno.inicial"));

      txtHoraInicial.setFont(new java.awt.Font("Arial", 0, 12));

      jLabel6.setFont(new java.awt.Font("Arial", 1, 12));
      jLabel6.setText(PropertiesUtil.getMessage("label.texto.horario.retorno.final"));

      txtHoraFinal.setFont(new java.awt.Font("Arial", 0, 12));
      /*txtHoraFinal.addFocusListener(new java.awt.event.FocusAdapter() {
         @Override
        public void focusLost(java.awt.event.FocusEvent evt) {
            txtHoraFinalFocusLost(evt);
         }
      });
*/
      jLabel7.setFont(new java.awt.Font("Arial", 1, 12));
      jLabel7.setText(PropertiesUtil.getMessage("label.texto.quantidade.retorno"));

      txtQuantidade.setFont(new java.awt.Font("Arial", 0, 12));

      javax.swing.GroupLayout pnlHorariosLayout = new javax.swing.GroupLayout(pnlHorarios);
      pnlHorarios.setLayout(pnlHorariosLayout);
      pnlHorariosLayout.setHorizontalGroup(
         pnlHorariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(pnlHorariosLayout.createSequentialGroup()
            .addContainerGap()
            .addGroup(pnlHorariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
               .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
               .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(pnlHorariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(pnlHorariosLayout.createSequentialGroup()
                  .addComponent(txtHoraFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addGap(18, 18, 18)
                  .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                  .addComponent(txtQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
               .addComponent(txtHoraInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(12, javax.swing.GroupLayout.PREFERRED_SIZE))
      );
      pnlHorariosLayout.setVerticalGroup(
         pnlHorariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(pnlHorariosLayout.createSequentialGroup()
            .addGroup(pnlHorariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel5)
               .addComponent(txtHoraInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(pnlHorariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel6)
               .addComponent(txtHoraFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel7)
               .addComponent(txtQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );

      cmdConfirmar.setFont(new java.awt.Font("Arial", 1, 14));
      StringBuffer value = new StringBuffer();
      value.append(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens")).append("/ok.png");
      cmdConfirmar.setIcon(new javax.swing.ImageIcon(value.toString())); // NOI18N
      cmdConfirmar.setText("Confirmar");
      cmdConfirmar.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            cmdConfirmarActionPerformed(evt);
         }
      });

      cmdDesistir.setFont(new java.awt.Font("Arial", 1, 14));

      value = new StringBuffer();
      value.append(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens")).append("/exit.png");
      cmdDesistir.setIcon(new javax.swing.ImageIcon(value.toString())); // NOI18N
      cmdDesistir.setText("Desistir");
      cmdDesistir.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            cmdDesistirActionPerformed(evt);
         }
      });


      javax.swing.GroupLayout pnlPrincipalLayout = new javax.swing.GroupLayout(pnlPrincipal);
      pnlPrincipal.setLayout(pnlPrincipalLayout);
      pnlPrincipalLayout.setHorizontalGroup(
         pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(pnlPrincipalLayout.createSequentialGroup()
            .addContainerGap()
            .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPrincipalLayout.createSequentialGroup()
                  .addComponent(cmdDesistir, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(cmdConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
               .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                  .addComponent(pnlHorarios, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                  .addComponent(pnlSelecaoBalizas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addContainerGap())
      );
      pnlPrincipalLayout.setVerticalGroup(
         pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(pnlPrincipalLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(pnlSelecaoBalizas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(pnlHorarios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(cmdConfirmar)
               .addComponent(cmdDesistir))
            .addContainerGap(12, Short.MAX_VALUE))
      );

      getContentPane().add(pnlPrincipal, java.awt.BorderLayout.CENTER);

      pack();
   }// </editor-fold>//GEN-END:initComponents

   private void cmbPilhaItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_cmbPilhaItemStateChanged
   {//GEN-HEADEREND:event_cmbPilhaItemStateChanged
	if (cmbPilha.getSelectedIndex() >= 0)
	{
         this.interfacePilhaSelecionda = (Pilha) cmbPilha.getSelectedItem();
         carregarComboBalizas();
         Baliza interfacebaliza = new LinkedList<Baliza>(interfacePilhaSelecionda.getListaDeBalizas()).getFirst();
         String[] rgbProdutoBalizaSelecionada = interfacebaliza.getProduto().getTipoProduto().getCorIdentificacao().split(",");
         lblCorTipoProdutoPilha.setBackground(new Color(Integer.parseInt(rgbProdutoBalizaSelecionada[0]), Integer.parseInt(rgbProdutoBalizaSelecionada[1]), Integer.parseInt(rgbProdutoBalizaSelecionada[2])));
	}
   }//GEN-LAST:event_cmbPilhaItemStateChanged

   private void cmdDesistirActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmdDesistirActionPerformed
   {//GEN-HEADEREND:event_cmdDesistirActionPerformed
      this.operacaoCanceladaPeloUsuario = Boolean.TRUE;
      this.setVisible(false);
   }//GEN-LAST:event_cmdDesistirActionPerformed

   private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
   {//GEN-HEADEREND:event_formWindowClosing
      this.operacaoCanceladaPeloUsuario = Boolean.TRUE;
   }//GEN-LAST:event_formWindowClosing

   private void cmbBalizaFinalItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_cmbBalizaFinalItemStateChanged
   {//GEN-HEADEREND:event_cmbBalizaFinalItemStateChanged
      try
      {
         carregaQuantidadeBalizas();
      }
      catch (ValidacaoCampoException vcEx)
      {
         controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
         interfaceMensagem = new InterfaceMensagem();
         interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
         interfaceMensagem.setDescricaoMensagem(vcEx.getMessage());
         controladorDSP.ativarMensagem(interfaceMensagem);
      }
   }//GEN-LAST:event_cmbBalizaFinalItemStateChanged

   private void cmdConfirmarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmdConfirmarActionPerformed
   {//GEN-HEADEREND:event_cmdConfirmarActionPerformed
      try
      {
         validarInformacoesRetornoPelletFeed();

         
         Date dataHoraOcorrenciaEvento = null;
         
         if (atividadeExecutada == null ) {
             dataHoraOcorrenciaEvento = DSSStockyardTimeUtil.criaDataComString(txtHoraInicial.getText(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
         } else {
             dataHoraOcorrenciaEvento = DSSStockyardTimeUtil.criaDataComString(txtHoraFinal.getText(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
         }
         
         double diferencaHoras = DSSStockyardTimeUtil.diferencaEmHoras(controladorDSP.getInterfaceInicial().getInterfaceInicial().getSituacaoPatioExibida().getDtInicio(), dataHoraOcorrenciaEvento);
         double tempoValidacaoHorasFutura = new Double(PropertiesUtil.buscarPropriedade("quantidade.horas.aviso.data.futura.atividades"));

         if (diferencaHoras >= tempoValidacaoHorasFutura)
         {
            JLabel pergunta = new JLabel(PropertiesUtil.getMessage("aviso.hora.atividade.superior.ao.parametro.futuro"));
            pergunta.setFont(new Font("Tahoma", Font.PLAIN, 11));
            int confirm = JOptionPane.showOptionDialog(
                  this,
                  pergunta,
                  PropertiesUtil.getMessage("popup.atencao"),
                  JOptionPane.YES_OPTION,
                  JOptionPane.INFORMATION_MESSAGE,
                  null,
                  null,
                  null);

            if (confirm == JOptionPane.NO_OPTION)
            {
               txtHoraFinal.requestFocus();
               return;
            }
         }
         montaAtividadeRetornoPelletFeed();
      }
      catch (ValidacaoCampoException vcEx)
      {
         controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
         interfaceMensagem = new InterfaceMensagem();
         interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
         interfaceMensagem.setDescricaoMensagem(vcEx.getMessage());
         controladorDSP.ativarMensagem(interfaceMensagem);
      }
      catch (ValidacaoObjetosOperacaoException ex)
      {
         controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
         interfaceMensagem = new InterfaceMensagem();
         interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
         interfaceMensagem.setDescricaoMensagem(ex.getMessage());
         controladorDSP.ativarMensagem(interfaceMensagem);
      } catch (AtividadeException e) {
          controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
          interfaceMensagem = new InterfaceMensagem();
          interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
          interfaceMensagem.setDescricaoMensagem(e.getMessage());
          controladorDSP.ativarMensagem(interfaceMensagem);
    }

   }//GEN-LAST:event_cmdConfirmarActionPerformed

   /*private void txtHoraFinalFocusLost(java.awt.event.FocusEvent evt)//GEN-FIRST:event_txtHoraFinalFocusLost
   {//GEN-HEADEREND:event_txtHoraFinalFocusLost
      try
      {
         // seta a data e hora inicial do inicio do retorno do pellet feed
         txtHoraInicial.setText(DSSStockyardTimeUtil.formatarData(calculaDataInicioOperacao(), PropertiesUtil.buscarPropriedade("formato.campo.datahora")));
      }
      catch (ValidacaoCampoException vcEx)
      {
         controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
         interfaceMensagem = new InterfaceMensagem();
         interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
         interfaceMensagem.setDescricaoMensagem(vcEx.getMessage());
         controladorDSP.ativarMensagem(interfaceMensagem);
      }
   }//GEN-LAST:event_txtHoraFinalFocusLost
*/
   private void cmbBalizaInicialItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_cmbBalizaInicialItemStateChanged
   {//GEN-HEADEREND:event_cmbBalizaInicialItemStateChanged
      try
      {
         carregaQuantidadeBalizas();
      }
      catch (ValidacaoCampoException vcEx)
      {
         controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
         interfaceMensagem = new InterfaceMensagem();
         interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
         interfaceMensagem.setDescricaoMensagem(vcEx.getMessage());
         controladorDSP.ativarMensagem(interfaceMensagem);
      }
   }//GEN-LAST:event_cmbBalizaInicialItemStateChanged

   private void montaAtividadeRetornoPelletFeed() throws ValidacaoCampoException, ValidacaoObjetosOperacaoException, AtividadeException
   {

       /** cria o VO para o empilhamento */
       MovimentacaoVO empilhamentoVO = new MovimentacaoVO(); 
       
       
       if (atividadeExecutada != null) {
           Date dataFinalAtividade = DSSStockyardTimeUtil.criaDataComString(txtHoraFinal.getText(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
           dataFinalAtividade = Atividade.verificaAtualizaDataAtividade(dataFinalAtividade, controladorDSP.getInterfaceInicial().getPlanejamento().getControladorDePlano().obterSituacaoPatioFinal().getDtInicio());    
           empilhamentoVO.setDataInicio(atividadeExecutada.getDtInicio());
           empilhamentoVO.setDataFim(dataFinalAtividade);    
           empilhamentoVO.setAtividadeAnterior(atividadeExecutada);
           Double quantidadeRecuperada = DSSStockyardFuncoesNumeros.getStringToDouble(txtQuantidade.getText());
           empilhamentoVO.setQuantidadeMovimentacao(quantidadeRecuperada);
           
       } else {
           Date dataInicioAtividade = DSSStockyardTimeUtil.criaDataComString(txtHoraInicial.getText(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));      
           dataInicioAtividade = Atividade.verificaAtualizaDataAtividade(dataInicioAtividade, controladorDSP.getInterfaceInicial().getPlanejamento().getControladorDePlano().obterSituacaoPatioFinal().getDtInicio());       
           empilhamentoVO.setDataInicio(dataInicioAtividade);
       }
       
       empilhamentoVO.setTipoAtividade(TipoAtividadeEnum.RETORNO_PELLET_FEED);
       
      int numeroBalizaInicial;
      int numeroBalizaFinal;
      TipoProduto tipoProduto = null;
      List<MetaBaliza> listaBalizasSelecionadas = new ArrayList<MetaBaliza>();

      // inclui apenas a primeira baliza do intervalo
      numeroBalizaInicial = ((Baliza) cmbBalizaInicial.getSelectedItem()).getNumero();

      // obtem a baliza final da realizacao do retorno do pellet feed
      numeroBalizaFinal = ((Baliza) cmbBalizaFinal.getSelectedItem()).getNumero();

      int maiorBaliza = Math.max(numeroBalizaInicial, numeroBalizaFinal);
      int menorBaliza = Math.min(numeroBalizaInicial, numeroBalizaFinal);


      if (atividadeExecutada != null) {
	      // define a lista de balizas alteradas nesta atividade
	      for (int i = menorBaliza; i <= maiorBaliza; i++) {
	          Baliza interfaceBaliza = mapaInterfaceBalizasPilha.get(i);
	          tipoProduto =interfaceBaliza.getProduto().getTipoProduto();
	          listaBalizasSelecionadas.add(interfaceBaliza.getMetaBaliza());
	      }
	      empilhamentoVO.getListaBalizas().addAll(listaBalizasSelecionadas);
      } else {
    	  Baliza interfaceBaliza = (Baliza) cmbBalizaInicial.getSelectedItem(); 
    	  listaBalizasSelecionadas.add(interfaceBaliza.getMetaBaliza());
    	  empilhamentoVO.getListaBalizas().addAll(listaBalizasSelecionadas);
      }
      
      //seta o sentido do retorno
      if(numeroBalizaFinal > numeroBalizaInicial){
          empilhamentoVO.setSentidoRecuperacao(SentidoEmpilhamentoRecuperacaoEnum.NORTE_PARA_SUL);
      }else{
          empilhamentoVO.setSentidoRecuperacao(SentidoEmpilhamentoRecuperacaoEnum.SUL_PARA_NORTE);
      }

      // 2)
      empilhamentoVO.setNomePilha(interfacePilhaSelecionda.getNomePilha());
      empilhamentoVO.setNomePilhaDestino("Retorno de pellet feed para filtragem");
      // 3)
      empilhamentoVO.getListaMaquinas().add(interfaceMaquinaDoPatio.getMaquinaDoPatioVisualizada().getMetaMaquina());
      
      empilhamentoVO.setTipoProduto(tipoProduto);

      ControladorExecutarAtividadeMovimentacaoPilha service = ControladorExecutarAtividadeMovimentacaoPilha.getInstance();
      
      atividadeRetornoPelletFeed = service.movimentar(empilhamentoVO);
      controladorDSP.getInterfaceInicial().movimentarPilhaPSMPelletFeed(atividadeRetornoPelletFeed);
      controladorDSP.getInterfaceInicial().getInterfaceInicial().atualizarDSP();
      operacaoCanceladaPeloUsuario = Boolean.FALSE;

      this.setVisible(false);

   }

   private void validarInformacoesRetornoPelletFeed() throws ValidacaoCampoException
   {

       Date dataInicial = DSSStockyardTimeUtil.criaDataComString( txtHoraInicial.getText(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
       Date dataFinal = null;
       if(atividadeExecutada != null) {           
           dataFinal = DSSStockyardTimeUtil.criaDataComString( txtHoraFinal.getText(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));                   
           dataFinal = Atividade.verificaAtualizaDataAtividade(dataFinal, controladorDSP
                   .getInterfaceInicial().getPlanejamento().getControladorDePlano()
                   .obterSituacaoPatioFinal().getDtInicio());
     	  
           if (dataFinal.before(dataInicial)) {
               throw new ValidacaoCampoException(PropertiesUtil.getMessage("aviso.data.periodo.final.deve.ser.maior.que.periodo.inicial"));
           }
          
       } else {
    	   dataInicial = Atividade.verificaAtualizaDataAtividade(dataInicial, controladorDSP
                   .getInterfaceInicial().getPlanejamento().getControladorDePlano()
                   .obterSituacaoPatioFinal().getDtInicio());
       }

       if (atividadeExecutada != null && !controladorDSP.validaDAtaMenorUltimaSituacaoPatio(dataFinal))
       {
          throw new ValidacaoCampoException(PropertiesUtil.getMessage("aviso.data.final.menor.ultima.situacao.patio"));
       }

      if (atividadeExecutada != null && txtQuantidade.getText().trim().equals(""))
      {
         throw new ValidacaoCampoException(PropertiesUtil.getMessage("mensagem.quantidade.nao.informada"));
      }
     
      if (atividadeExecutada != null) 
      {          
    	  
    	  Double quantidadeRecuperada = DSSStockyardFuncoesNumeros.arredondaValor(DSSStockyardFuncoesNumeros.getStringToDouble(txtQuantidade.getText()),2);
          Double quantidadeTotal  =DSSStockyardFuncoesNumeros.arredondarDoubleParaCimaAPartirDeNumCasasDecimais(quantidadeTotalProdutosBaliza,  2);
          if (quantidadeRecuperada > quantidadeTotal) {
         
              throw new ValidacaoCampoException(PropertiesUtil.getMessage("mensagem.quantidade.maiorque.existente") + " - Quantidade restante = "+quantidadeTotal);
          }
           if (quantidadeRecuperada <= 0)
           {
               throw new ValidacaoCampoException(PropertiesUtil.getMessage("mensagem.quantidade.maiorque.zero"));
           }
      }
   }

   private void carregaQuantidadeBalizas() throws ValidacaoCampoException
   {
      if (cmbBalizaInicial.getSelectedItem() == null || cmbBalizaFinal.getSelectedItem() == null)
      {
         return;
      }

   	// ... obtendo o numero da baliza inicial e final
   	int numeroBalizaInicial = ((Baliza) cmbBalizaInicial.getSelectedItem()).getNumero();
   	int numeroBalizaFinal = ((Baliza) cmbBalizaFinal.getSelectedItem()).getNumero();

   	int maiorBaliza = Math.max(numeroBalizaInicial, numeroBalizaFinal);
   	int menorBaliza = Math.min(numeroBalizaInicial, numeroBalizaFinal);

   	// ... percorrendo o range de balizas e somando as quantidades dos produtos das mesmas
   	quantidadeTotalProdutosBaliza = 0D;
   	for (int i = menorBaliza; i <= maiorBaliza; i++)
   	{
   	   Baliza interfaceBaliza = mapaInterfaceBalizasPilha.get(i);
   	   if (interfaceBaliza != null)
         {
            quantidadeTotalProdutosBaliza += interfaceBaliza.getProduto().getQuantidade();
         }
   	}

   	quantidadeTotalProdutosBaliza = DSSStockyardFuncoesNumeros.arredondaValor(quantidadeTotalProdutosBaliza, 2);
   	
   	// ... setando a quantidade no campo quantidade para falitar para o usuario
   	//txtQuantidade.setText(DSSStockyardFuncoesNumeros.getQtdeFormatada(quantidadeTotalProdutosBaliza, 2));

      // seta a data e hora inicial do inicio do retorno do pellet feed
    //txtHoraInicial.setText(DSSStockyardTimeUtil.formatarData(calculaDataInicioOperacao(), PropertiesUtil.buscarPropriedade("formato.campo.datahora")));
   }

   /**
    * Calcula a data de inicio da operacao de retorno de pellet feed baseado 
    * na taxa de operacao da maquina e da quantidade de produtos a ser retornado
    * @return
    * @throws br.com.cflex.supervision.stockYard.cliente.exception.ValidacaoCampoException
    */
   private Date calculaDataInicioOperacao() throws ValidacaoCampoException
   {
      // calcula a hora inicial baseado na taxa de operacao da maquina e na quantidade que ela esta retornando
      long tempoExecucaoOperacao = (long)(quantidadeTotalProdutosBaliza/interfaceMaquinaDoPatio.getMaquinaDoPatioVisualizada().getTaxaDeOperacaoNominal());
      // transforma o tempo de execucao da operacao de horas para milisegundos
      tempoExecucaoOperacao = tempoExecucaoOperacao * 3600 * 1000;
      // cria um date com a hora final da execucao da operacao
      Date horaFinalExecucao = DSSStockyardTimeUtil.criaDataComString(txtHoraFinal.getText(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
      // subtrai o tempo de execucao da operacao da hora final da operacao
      long horaInicioExecucao = horaFinalExecucao.getTime() - tempoExecucaoOperacao;
      // cria um date com a hora inicial da operacao
      Date horaInicialRetorno = new Date(horaInicioExecucao);

      return horaInicialRetorno;
   }


   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JComboBox cmbBalizaFinal;
   private javax.swing.JComboBox cmbBalizaInicial;
   private javax.swing.JComboBox cmbPilha;
   private javax.swing.JButton cmdConfirmar;
   private javax.swing.JButton cmdDesistir;
   private javax.swing.JLabel jLabel1;
   private javax.swing.JLabel jLabel2;
   private javax.swing.JLabel jLabel3;
   private javax.swing.JLabel jLabel4;
   private javax.swing.JLabel jLabel5;
   private javax.swing.JLabel jLabel6;
   private javax.swing.JLabel jLabel7;
   private javax.swing.JLabel lblCorTipoProdutoPilha;
   private javax.swing.JLabel lblNomePatioOrigem;
   private javax.swing.JPanel pnlHorarios;
   private javax.swing.JPanel pnlPrincipal;
   private javax.swing.JPanel pnlSelecaoBalizas;
   private javax.swing.JFormattedTextField txtHoraFinal;
   private javax.swing.JFormattedTextField txtHoraInicial;
   private javax.swing.JTextField txtQuantidade;
   // End of variables declaration//GEN-END:variables

   public Boolean getOperacaoCanceladaPeloUsuario()
   {
      return operacaoCanceladaPeloUsuario;
   }

   public Atividade getAtividadeRetornoPelletFeed()
   {
      return atividadeRetornoPelletFeed;
   }

}
