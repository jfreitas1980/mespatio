package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.SwingConstants;

import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.cliente.util.tabela.CFlexStockyardFuncoesTabela;
import com.hdntec.gestao.cliente.util.tabela.CFlexStockyardTableModelCustom;
import com.hdntec.gestao.cliente.util.tabela.ColunaTabela;
import com.hdntec.gestao.cliente.util.texto.DSSStockyardFuncoesTexto;
import com.hdntec.gestao.domain.planta.dao.MetaBalizaDAO;
import com.hdntec.gestao.domain.planta.entity.MetaBaliza;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.Interdicao;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.ValidacaoCampoException;
import com.hdntec.gestao.util.PropertiesUtil;
import com.hdntec.gestao.util.datahora.DSSStockyardTimeUtil;

public class InterfaceInterdicoesBaliza extends javax.swing.JDialog
{

   /** Constantes referente as colunas da tabela de lista de balizas */
   private final int COL_LISTA_BALIZA_CHECK = 0;
   private final int COL_LISTA_BAZLIA_NOME = 1;
   private final int COL_LISTA_BALIZA_NUMERO = 2;

   /** Constantes referente a tabela de interdicoes já realizadas */
   private final int COL_INTERDICAO_BALIZA_LIBERACAO = 0;
   private final int COL_INTERDICAO_BALIZA_PATIO = 1;
   private final int COL_INTERDICAO_BALIZA_NUMERO = 2;
   private final int COL_INTERDICAO_BALIZA_NOME = 3;
   private final int COL_INTERDICAO_BALIZA_DATAINICIO = 4;
   private final int COL_INTERDICAO_BALIZA_DATAFINAL = 5;

   /** o controlador dos subsistemas do dsp */
   private ControladorDSP controladorDSP;

   /** a lista de colucas da tabela de balizas */
   private List<ColunaTabela> listaColunasBalizas;
   private List<ColunaTabela> listaColunasInterdicoes;

   /** o vetor referente a tabela de balizas */
   private Vector vInformacoesBalizas;
   private Vector vInformacoesInterdicoes;

   /** a interface de patio selecionada */
   private InterfacePatio interfacePatioSelecionada;

   /** a lista de balizas com interdição jah registrada */
   private List<Baliza> listaBalizasInterditadas;

   
   private HashMap<MetaBaliza,List<Interdicao>> mapBalizaInterdicoes;
   
   
   /** a interface das mensagens de interdicao */
   private InterfaceMensagem interfaceMensagem;

   /** flag de operacao cancelada pelo usuario */
   private Boolean operacaoCanceladaPeloUsuario;

   /** a lista de balizas clonada do patio */
     List<Baliza> listaBalizas;


   /** Creates new form InterfaceInterdicoes */
   public InterfaceInterdicoesBaliza(java.awt.Frame parent, boolean modal, ControladorDSP controladorDSP, InterfacePatio interfacePatio)
   {
      super(parent, modal);
      initComponents();
      this.controladorDSP = controladorDSP;
      if (interfacePatio != null)
      {
         habilitarInterdicaoBaliza();       
         this.interfacePatioSelecionada = interfacePatio;
         vInformacoesBalizas = new Vector();
         vInformacoesInterdicoes = new Vector();
         
         criaColunasListaBalizas();
         criaColunasListaInterdicoes();
         
         carregaComboPatio();
         
      }
      operacaoCanceladaPeloUsuario = false;
   }

   private void desabilitarInterdicaoBaliza()
   {
      chkSelecionarTodasBalizas.setEnabled(false);
      chkSelecionarTodasBalizasInterditadas.setEnabled(false);
      cmdInterditarBalizasSelecionadas.setEnabled(false);
      cmdLiberarInterdicoesSelecionadas.setEnabled(false);
      cmdLimparInformacoesBalizas.setEnabled(false);
      cmbPatio.setEnabled(false);
      tblInterdicoesRealizadas.setEnabled(false);
      tblListaBalizas.setEnabled(false);
   }

   private void habilitarInterdicaoBaliza()
   {
      chkSelecionarTodasBalizas.setEnabled(true);
      chkSelecionarTodasBalizasInterditadas.setEnabled(true);
      cmdInterditarBalizasSelecionadas.setEnabled(true);
      cmdLiberarInterdicoesSelecionadas.setEnabled(true);
      cmdLimparInformacoesBalizas.setEnabled(true);
      cmbPatio.setEnabled(true);
      tblInterdicoesRealizadas.setEnabled(true);
      tblListaBalizas.setEnabled(true);
   }


    /**
     * Metodo que cria a lista de colunas para exibicao das informacoes das balizas selecionadas
     */
    private void criaColunasListaBalizas() {

        listaColunasBalizas = new ArrayList<ColunaTabela>();
        ColunaTabela colInfo;

        colInfo = new ColunaTabela();
        colInfo.setAlinhamento(SwingConstants.CENTER);
        colInfo.setEditar(Boolean.TRUE);
        colInfo.setLargura(80);
        colInfo.setRedimensionar(Boolean.FALSE);
        colInfo.setTitulo("Seleção");
        colInfo.setTipoEditor(ColunaTabela.COL_TIPO_CHECKBOX);
        listaColunasBalizas.add(colInfo);

        colInfo = new ColunaTabela();
        colInfo.setAlinhamento(SwingConstants.LEADING);
        colInfo.setEditar(Boolean.FALSE);
        colInfo.setLargura(120);
        colInfo.setRedimensionar(Boolean.FALSE);
        colInfo.setTitulo("Descrição");
        listaColunasBalizas.add(colInfo);

        colInfo = new ColunaTabela();
        colInfo.setAlinhamento(SwingConstants.CENTER);
        colInfo.setEditar(Boolean.FALSE);
        colInfo.setLargura(80);
        colInfo.setRedimensionar(Boolean.FALSE);
        colInfo.setTitulo("Número");
        listaColunasBalizas.add(colInfo);

    }

    /** Metodo que cria as colunas referente as balizas já interditadas */
    private void criaColunasListaInterdicoes()
    {
        listaColunasInterdicoes = new ArrayList<ColunaTabela>();
        ColunaTabela colInfo;

        colInfo = new ColunaTabela();
        colInfo.setAlinhamento(SwingConstants.CENTER);
        colInfo.setEditar(Boolean.TRUE);
        colInfo.setLargura(50);
        colInfo.setRedimensionar(Boolean.FALSE);
        colInfo.setTitulo("Liberar");
        colInfo.setTipoEditor(ColunaTabela.COL_TIPO_CHECKBOX);
        listaColunasInterdicoes.add(colInfo);
        
        colInfo = new ColunaTabela();
        colInfo.setAlinhamento(SwingConstants.CENTER);
        colInfo.setEditar(Boolean.FALSE);
        colInfo.setLargura(50);
        colInfo.setRedimensionar(Boolean.FALSE);
        colInfo.setTitulo("Pátio");
        listaColunasInterdicoes.add(colInfo);

        colInfo = new ColunaTabela();
        colInfo.setAlinhamento(SwingConstants.CENTER);
        colInfo.setEditar(Boolean.FALSE);
        colInfo.setLargura(50);
        colInfo.setRedimensionar(Boolean.FALSE);
        colInfo.setTitulo("Número");
        listaColunasInterdicoes.add(colInfo);

        colInfo = new ColunaTabela();
        colInfo.setAlinhamento(SwingConstants.LEADING);
        colInfo.setEditar(Boolean.FALSE);
        colInfo.setLargura(100);
        colInfo.setRedimensionar(Boolean.FALSE);
        colInfo.setTitulo("Descrição");
        listaColunasInterdicoes.add(colInfo);

        colInfo = new ColunaTabela();
        colInfo.setAlinhamento(SwingConstants.CENTER);
        colInfo.setEditar(Boolean.FALSE);
        colInfo.setLargura(130);
        colInfo.setRedimensionar(Boolean.FALSE);
        colInfo.setTitulo("Início Interdição");
        listaColunasInterdicoes.add(colInfo);

        colInfo = new ColunaTabela();
        colInfo.setAlinhamento(SwingConstants.CENTER);
        colInfo.setEditar(Boolean.FALSE);
        colInfo.setLargura(130);
        colInfo.setRedimensionar(Boolean.FALSE);
        colInfo.setTitulo("Final Interdição");
        listaColunasInterdicoes.add(colInfo);

       
    }


    /**
     * Metodo que carrega o combo de patio com os patios existentes no DSP
     */
    private void carregaComboPatio()
    {
       cmbPatio.removeAllItems();       
       mapBalizaInterdicoes = new HashMap<MetaBaliza,List<Interdicao>>();
       // adicionando o patio selecionado
       cmbPatio.addItem(interfacePatioSelecionada);
       cmbPatio.setEnabled(false);
       try {
        atualizarListaDeBalizas();
    } catch (ErroSistemicoException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
       habilitarInterdicaoBaliza();
    }

   /** This method is called from within the constructor to
    * initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is
    * always regenerated by the Form Editor.
    */
   @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grpTipoInterdicao = new javax.swing.ButtonGroup();
        pnlPrincipal = new javax.swing.JPanel();
        pnlInformacoesBaliza = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmbPatio = new javax.swing.JComboBox();
        scrListaBalizas = new javax.swing.JScrollPane();
        tblListaBalizas = new com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable();
        chkSelecionarTodasBalizas = new javax.swing.JCheckBox();
        cmdLimparInformacoesBalizas = new javax.swing.JButton();
        cmdInterditarBalizasSelecionadas = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        dataHoraInicioInterdicao = new com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex();
        jLabel3 = new javax.swing.JLabel();
        dataHoraFinalInterdicao = new com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex();
        pnlInterdicoesRealizadas = new javax.swing.JPanel();
        scrInterdicoesRealizadas = new javax.swing.JScrollPane();
        tblInterdicoesRealizadas = new com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable();
        cmdLiberarInterdicoesSelecionadas = new javax.swing.JButton();
        chkSelecionarTodasBalizasInterditadas = new javax.swing.JCheckBox();
        cmdDesistir = new javax.swing.JButton();
        cmdConfirmar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Interdições de Balizas e Pier");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        pnlInformacoesBaliza.setBorder(javax.swing.BorderFactory.createTitledBorder("Informações para interdição de baliza"));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel1.setText("Pátio:");

        cmbPatio.setFont(new java.awt.Font("Arial", 0, 12));
        cmbPatio.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbPatioItemStateChanged(evt);
            }
        });

        tblListaBalizas.setModel(new CFlexStockyardTableModelCustom());
        scrListaBalizas.setViewportView(tblListaBalizas);

        chkSelecionarTodasBalizas.setFont(new java.awt.Font("Arial", 1, 12));
        chkSelecionarTodasBalizas.setText("Selecionar todas as balizas");
        chkSelecionarTodasBalizas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSelecionarTodasBalizasActionPerformed(evt);
            }
        });

        cmdLimparInformacoesBalizas.setFont(new java.awt.Font("Arial", 1, 12));
        //cmdLimparInformacoesBalizas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icones/eraser.png"))); // NOI18N
        cmdLimparInformacoesBalizas.setText("Limpar");
        cmdLimparInformacoesBalizas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdLimparInformacoesBalizasActionPerformed(evt);
            }
        });

        cmdInterditarBalizasSelecionadas.setFont(new java.awt.Font("Arial", 1, 12));
        //cmdInterditarBalizasSelecionadas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icones/up.png"))); // NOI18N
        cmdInterditarBalizasSelecionadas.setText("Interditar");
        cmdInterditarBalizasSelecionadas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdInterditarBalizasSelecionadasActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel2.setText("Data e hora de início:");

        dataHoraInicioInterdicao.setFont(new java.awt.Font("Arial", 0, 12));

        jLabel3.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel3.setText("Data e hora de término:");

        dataHoraFinalInterdicao.setFont(new java.awt.Font("Arial", 0, 12));

        javax.swing.GroupLayout pnlInformacoesBalizaLayout = new javax.swing.GroupLayout(pnlInformacoesBaliza);
        pnlInformacoesBaliza.setLayout(pnlInformacoesBalizaLayout);
        pnlInformacoesBalizaLayout.setHorizontalGroup(
            pnlInformacoesBalizaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInformacoesBalizaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInformacoesBalizaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInformacoesBalizaLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbPatio, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(chkSelecionarTodasBalizas, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlInformacoesBalizaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlInformacoesBalizaLayout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dataHoraFinalInterdicao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlInformacoesBalizaLayout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addGap(46, 46, 46)
                            .addComponent(dataHoraInicioInterdicao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(118, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlInformacoesBalizaLayout.createSequentialGroup()
                .addContainerGap(296, Short.MAX_VALUE)
                .addComponent(cmdInterditarBalizasSelecionadas, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cmdLimparInformacoesBalizas, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(pnlInformacoesBalizaLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(scrListaBalizas, javax.swing.GroupLayout.PREFERRED_SIZE, 469, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(41, Short.MAX_VALUE))
        );
        pnlInformacoesBalizaLayout.setVerticalGroup(
            pnlInformacoesBalizaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInformacoesBalizaLayout.createSequentialGroup()
                .addContainerGap(11, Short.MAX_VALUE)
                .addGroup(pnlInformacoesBalizaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cmbPatio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkSelecionarTodasBalizas))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInformacoesBalizaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(dataHoraInicioInterdicao, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(pnlInformacoesBalizaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(dataHoraFinalInterdicao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addComponent(scrListaBalizas, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlInformacoesBalizaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdLimparInformacoesBalizas, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdInterditarBalizasSelecionadas)))
        );

        pnlInterdicoesRealizadas.setBorder(javax.swing.BorderFactory.createTitledBorder("Interdições já realizadas no sistema"));

        tblInterdicoesRealizadas.setModel(new CFlexStockyardTableModelCustom());
        scrInterdicoesRealizadas.setViewportView(tblInterdicoesRealizadas);

        cmdLiberarInterdicoesSelecionadas.setFont(new java.awt.Font("Arial", 1, 12));
        //cmdLiberarInterdicoesSelecionadas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icones/window_new.png"))); // NOI18N
        cmdLiberarInterdicoesSelecionadas.setText("Liberar Selecionadas");
        cmdLiberarInterdicoesSelecionadas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdLiberarInterdicoesSelecionadasActionPerformed(evt);
            }
        });

        chkSelecionarTodasBalizasInterditadas.setFont(new java.awt.Font("Arial", 1, 12));
        chkSelecionarTodasBalizasInterditadas.setText("Selecionar todas as balizas para liberação");
        chkSelecionarTodasBalizasInterditadas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSelecionarTodasBalizasInterditadasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlInterdicoesRealizadasLayout = new javax.swing.GroupLayout(pnlInterdicoesRealizadas);
        pnlInterdicoesRealizadas.setLayout(pnlInterdicoesRealizadasLayout);
        pnlInterdicoesRealizadasLayout.setHorizontalGroup(
            pnlInterdicoesRealizadasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInterdicoesRealizadasLayout.createSequentialGroup()
                .addGroup(pnlInterdicoesRealizadasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInterdicoesRealizadasLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(chkSelecionarTodasBalizasInterditadas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 78, Short.MAX_VALUE)
                        .addComponent(cmdLiberarInterdicoesSelecionadas, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlInterdicoesRealizadasLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(scrInterdicoesRealizadas, javax.swing.GroupLayout.DEFAULT_SIZE, 522, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlInterdicoesRealizadasLayout.setVerticalGroup(
            pnlInterdicoesRealizadasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInterdicoesRealizadasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrInterdicoesRealizadas, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlInterdicoesRealizadasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkSelecionarTodasBalizasInterditadas)
                    .addComponent(cmdLiberarInterdicoesSelecionadas, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)))
        );

        cmdDesistir.setFont(new java.awt.Font("Tahoma", 1, 12));
        cmdDesistir.setText(PropertiesUtil.getMessage("botao.desistir"));
        cmdDesistir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdDesistirActionPerformed(evt);
            }
        });

        cmdConfirmar.setFont(new java.awt.Font("Tahoma", 1, 12));
        //cmdConfirmar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icones/confirmar.png"))); // NOI18N
        cmdConfirmar.setText(PropertiesUtil.getMessage("botao.confirmar"));
        cmdConfirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdConfirmarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlPrincipalLayout = new javax.swing.GroupLayout(pnlPrincipal);
        pnlPrincipal.setLayout(pnlPrincipalLayout);
        pnlPrincipalLayout.setHorizontalGroup(
            pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlPrincipalLayout.createSequentialGroup()
                        .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnlInformacoesBaliza, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlInterdicoesRealizadas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(14, 14, 14))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPrincipalLayout.createSequentialGroup()
                        .addComponent(cmdDesistir, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        pnlPrincipalLayout.setVerticalGroup(
            pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlInformacoesBaliza, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlInterdicoesRealizadas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdDesistir))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlInformacoesBaliza.getAccessibleContext().setAccessibleName("Informações para de baliza");

        getContentPane().add(pnlPrincipal, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


   private void cmbPatioItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_cmbPatioItemStateChanged
   {//GEN-HEADEREND:event_cmbPatioItemStateChanged
      try {
         interfacePatioSelecionada = (InterfacePatio) cmbPatio.getSelectedItem();
         atualizarListaDeBalizas();
         atualizarTabelaDeInterdicoes();
      }
      catch (ErroSistemicoException errEx)
      {
         controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
         interfaceMensagem = new InterfaceMensagem(errEx.getMessage(), InterfaceMensagem.MENSAGEM_TIPO_ERRO);
         controladorDSP.ativarMensagem(interfaceMensagem);
      }
   }//GEN-LAST:event_cmbPatioItemStateChanged

   private void limpaTabelaInformacoesBalizas() throws ErroSistemicoException
   {
            
       vInformacoesBalizas.removeAllElements();
      // desenha as informacoes na tabela
      CFlexStockyardFuncoesTabela.setInformacoesTabela(tblListaBalizas, vInformacoesBalizas, listaColunasBalizas);
      chkSelecionarTodasBalizas.setSelected(false);
   }

   private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
   {//GEN-HEADEREND:event_formWindowClosing
      operacaoCanceladaPeloUsuario = true;
   }//GEN-LAST:event_formWindowClosing

   private void chkSelecionarTodasBalizasActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chkSelecionarTodasBalizasActionPerformed
   {//GEN-HEADEREND:event_chkSelecionarTodasBalizasActionPerformed
      if (chkSelecionarTodasBalizas.isSelected())
      {
         selecionarTodasBalizas();
      }
      else
      {
         deselecionarTodasBalizas();
      }
   }//GEN-LAST:event_chkSelecionarTodasBalizasActionPerformed

   private void cmdLimparInformacoesBalizasActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmdLimparInformacoesBalizasActionPerformed
   {//GEN-HEADEREND:event_cmdLimparInformacoesBalizasActionPerformed
      try
      {
         limpaTabelaInformacoesBalizas();
         cmbPatio.setSelectedIndex(0);
      }
      catch (ErroSistemicoException errEx)
      {
         controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
         interfaceMensagem = new InterfaceMensagem(errEx.getMessage(), InterfaceMensagem.MENSAGEM_TIPO_ERRO);
         controladorDSP.ativarMensagem(interfaceMensagem);
      }
   }//GEN-LAST:event_cmdLimparInformacoesBalizasActionPerformed

   private void cmdInterditarBalizasSelecionadasActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmdInterditarBalizasSelecionadasActionPerformed
   {//GEN-HEADEREND:event_cmdInterditarBalizasSelecionadasActionPerformed
      try
      {        
         interditarBalizasSelecionadas();
         atualizarTabelaDeInterdicoes();
      }
      catch (ValidacaoCampoException vcEx)
      {
         controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
         interfaceMensagem = new InterfaceMensagem(vcEx.getMessage(), InterfaceMensagem.MENSAGEM_TIPO_ERRO);
         controladorDSP.ativarMensagem(interfaceMensagem);
      }
      catch (ErroSistemicoException errEx)
      {
         controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
         interfaceMensagem = new InterfaceMensagem(errEx.getMessage(), InterfaceMensagem.MENSAGEM_TIPO_ERRO);
         controladorDSP.ativarMensagem(interfaceMensagem);
      }
   }//GEN-LAST:event_cmdInterditarBalizasSelecionadasActionPerformed

   private void chkSelecionarTodasBalizasInterditadasActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chkSelecionarTodasBalizasInterditadasActionPerformed
   {//GEN-HEADEREND:event_chkSelecionarTodasBalizasInterditadasActionPerformed
      if (chkSelecionarTodasBalizasInterditadas.isSelected())
      {
         selecionarTodasBalizasInterditadas();
      }
      else
      {
         deselecionarTodasBalizasInterditadas();
      }
   }//GEN-LAST:event_chkSelecionarTodasBalizasInterditadasActionPerformed

   private void cmdLiberarInterdicoesSelecionadasActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmdLiberarInterdicoesSelecionadasActionPerformed
   {//GEN-HEADEREND:event_cmdLiberarInterdicoesSelecionadasActionPerformed
      try 
      {
         liberarInterdicoesSelecionadas();
         atualizarTabelaDeInterdicoes();
         atualizarListaDeBalizas();
      }
      catch (ErroSistemicoException errEx)
      {
         controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
         interfaceMensagem = new InterfaceMensagem(errEx.getMessage(), InterfaceMensagem.MENSAGEM_TIPO_ERRO);
         controladorDSP.ativarMensagem(interfaceMensagem);
      }
      
   }//GEN-LAST:event_cmdLiberarInterdicoesSelecionadasActionPerformed

   private void cmdDesistirActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmdDesistirActionPerformed
   {//GEN-HEADEREND:event_cmdDesistirActionPerformed
      operacaoCanceladaPeloUsuario = true;
      setVisible(false);
   }//GEN-LAST:event_cmdDesistirActionPerformed

   private void cmdConfirmarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmdConfirmarActionPerformed
   {//GEN-HEADEREND:event_cmdConfirmarActionPerformed
      if (interfacePatioSelecionada != null)
      {
         MetaBalizaDAO dao = new MetaBalizaDAO();
         for (Baliza baliza : getListaBalizasInterditadas()) {
             List<Interdicao> itens = mapBalizaInterdicoes.get(baliza.getMetaBaliza());
             baliza.getMetaBaliza().getListaInterdicao().clear();
             baliza.getMetaBaliza().addInterdicao(itens);
             try {
                dao.salvaMetaBaliza(baliza.getMetaBaliza());
            } catch (ErroSistemicoException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
         }             
      }
     
      operacaoCanceladaPeloUsuario = false;
      setVisible(false);
   }//GEN-LAST:event_cmdConfirmarActionPerformed

   /** Metodo que seleciona toda a lista de interdicoes


   /** Metodo que libera as interdicoes selecionadas */
   private void liberarInterdicoesSelecionadas()
   {
      for (int i = 0; i < tblInterdicoesRealizadas.getRowCount(); i++)
      {
         if (tblInterdicoesRealizadas.getValueAt(i, COL_INTERDICAO_BALIZA_LIBERACAO).toString().toUpperCase().equals("TRUE"))
         {
            Integer numeroBalizaInterditada = new Integer(tblInterdicoesRealizadas.getValueAt(i, COL_INTERDICAO_BALIZA_NUMERO).toString());
            Baliza balizaInterditada = obterBalizaDaListaPeloNumero(getListaBalizasInterditadas(), numeroBalizaInterditada);
            if (balizaInterditada != null)
            {
                try {
                List<Interdicao> itens = mapBalizaInterdicoes.get(balizaInterditada.getMetaBaliza());
                String inicio = tblInterdicoesRealizadas.getValueAt(i, COL_INTERDICAO_BALIZA_DATAINICIO).toString();
                String fim = tblInterdicoesRealizadas.getValueAt(i, COL_INTERDICAO_BALIZA_DATAFINAL).toString();
                
               
                Date dataInicio = DSSStockyardTimeUtil.criaDataComString(inicio, PropertiesUtil
                                    .buscarPropriedade("formato.campo.datahora"));
                
                Date dataFim = DSSStockyardTimeUtil.criaDataComString(fim, PropertiesUtil
                                .buscarPropriedade("formato.campo.datahora"));
                
                Interdicao interdicaoBaliza = new Interdicao();
                interdicaoBaliza.setDataInicial(dataInicio);
                interdicaoBaliza.setDataFinal(dataFim);
                int index = itens.indexOf(interdicaoBaliza);
                   if (index != -1 ) {
                       interdicaoBaliza = itens.remove(index);                    
                   }
                } catch (ValidacaoCampoException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
               
               
               //listaInterdicoesRemovidas
            }
         }
      }
      chkSelecionarTodasBalizasInterditadas.setSelected(false);
   }

   /** Metodo que valida as informacoes de interdicao de balizas */
   private void validarInformacoesInterdicao(Baliza baliza, Interdicao interdicaoNova) throws ValidacaoCampoException
   {
      if (dataHoraFinalInterdicao.getDataHoraDate().before(dataHoraInicioInterdicao.getDataHoraDate()))
      {
         throw new ValidacaoCampoException(PropertiesUtil.getMessage("valida.datas.manutencao.inicial.maior.final"));
      }
      
      boolean encontrouSelecao = false;
      for (int i = 0; i < tblListaBalizas.getRowCount(); i++)
      {
         if (tblListaBalizas.getValueAt(i, COL_LISTA_BALIZA_CHECK).toString().toUpperCase().equals("TRUE"))
         {
            encontrouSelecao = true;
            break;
         }
      }
      if (!encontrouSelecao)
      {
         throw new ValidacaoCampoException("Nenhuma baliza foi selecionada para interdição");
      }

      
      List<Interdicao> lstInterdicao = mapBalizaInterdicoes.get(baliza.getMetaBaliza());
      /*****************************
       * 
       * 
       *****************************/
      for (Interdicao interdicao : lstInterdicao)
      {                 
          //if (this.interdicaoAtual != null && !this.interdicaoAtual.equals(interdicao)) {
              if ((interdicao.getDataInicial().getTime() > interdicaoNova.getInicio() &&
                              interdicao.getDataInicial().getTime() < interdicaoNova.getFim()) ||                    
                   (interdicao.getDataInicial().getTime() == interdicaoNova.getInicio() &&
                    interdicao.getDataFinal().getTime() == interdicaoNova.getFim()) ||                                

                    (interdicao.getDataFinal().getTime() > interdicaoNova.getInicio() && 
                                   interdicao.getDataFinal().getTime() < interdicaoNova.getFim()) ||                     
                   (interdicaoNova.getInicio() > interdicao.getDataInicial().getTime() &&
                   interdicaoNova.getInicio() < interdicao.getDataFinal().getTime()) ||
                  
                   (interdicaoNova.getFim() > interdicao.getDataInicial().getTime() && 
                  interdicaoNova.getFim() < interdicao.getDataFinal().getTime()))         
                      throw new ValidacaoCampoException("Existem interdição no mesmo período definido."); 
          }
      }
  


   /** Metodo que recarrega a tabela de balizas interditadas */
   private void atualizarTabelaDeInterdicoes() throws ErroSistemicoException
   {
      vInformacoesInterdicoes.removeAllElements();
      
      for (Baliza baliza : getListaBalizasInterditadas())
      {
         List<Interdicao> lstItens = mapBalizaInterdicoes.get(baliza.getMetaBaliza());
         if (lstItens != null && !lstItens.isEmpty()) {
         for (Interdicao interdicao : lstItens)
         {            
             Object[] dados = new Object[6];
             dados[COL_INTERDICAO_BALIZA_PATIO] = baliza.getPatio().getNomePatio();
             dados[COL_INTERDICAO_BALIZA_NUMERO] = DSSStockyardFuncoesTexto.getCodigoFormatado(3, baliza.getNumero());
             dados[COL_INTERDICAO_BALIZA_NOME] = baliza.getNomeBaliza();
             dados[COL_INTERDICAO_BALIZA_DATAINICIO] = DSSStockyardTimeUtil.formatarData(interdicao.getDataInicial(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
             dados[COL_INTERDICAO_BALIZA_DATAFINAL] = DSSStockyardTimeUtil.formatarData(interdicao.getDataFinal(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
             dados[COL_INTERDICAO_BALIZA_LIBERACAO] = new JCheckBox("", false);
             vInformacoesInterdicoes.add(new Vector(Arrays.asList(dados)));
         }
         } 
      }   

      // desenha as informacoes na tabela
      CFlexStockyardFuncoesTabela.setInformacoesTabela(tblInterdicoesRealizadas, vInformacoesInterdicoes, listaColunasInterdicoes);
      chkSelecionarTodasBalizas.setSelected(false);
   }

   /** Metodo que executa a interdicao das balizas selecionadas 
 * @throws ValidacaoCampoException */
   private void interditarBalizasSelecionadas() throws ErroSistemicoException, ValidacaoCampoException
   {
      for (int i = 0; i < tblListaBalizas.getRowCount(); i++)
      {
         if (tblListaBalizas.getValueAt(i, COL_LISTA_BALIZA_CHECK).toString().toUpperCase().equals("TRUE"))
         {
            Integer numeroBaliza = new Integer(tblListaBalizas.getValueAt(i, COL_LISTA_BALIZA_NUMERO).toString());
            Baliza balizaInterditada = obterBalizaDaListaPeloNumero(listaBalizas, numeroBaliza);
            if (balizaInterditada != null)
            {
               criaInterdicaoParaBaliza(balizaInterditada);             
            }
         }
      }
      // desenha as informacoes na tabela
      CFlexStockyardFuncoesTabela.setInformacoesTabela(tblListaBalizas, vInformacoesBalizas, listaColunasBalizas);
   }

  /* *//** Metodo que remove uma baliza interditada da lista de balizas do patio exibida *//*
   private void removerBalizaDaLista(Integer numeroBaliza) throws ErroSistemicoException
   {
      for (int i = 0; i < vInformacoesBalizas.size(); i++)
      {
         Vector vDados = (Vector) vInformacoesBalizas.elementAt(i);
         if (new Integer(vDados.elementAt(COL_LISTA_BALIZA_NUMERO).toString()).equals(numeroBaliza))
         {
            vInformacoesBalizas.remove(i);
            break;
         }
      }
   }*/

   /** Metodo que cria uma interdicao para uma determinada baliza 
 * @throws ValidacaoCampoException */
   private void criaInterdicaoParaBaliza(Baliza balizaInterdicao) throws ValidacaoCampoException
   {
      Interdicao interdicaoBaliza = new Interdicao();

      Date dataInicio = DSSStockyardTimeUtil.criaDataComString(dataHoraInicioInterdicao.getDataHora(), PropertiesUtil
                      .buscarPropriedade("formato.campo.datahora"));
  
      Date dataFim = DSSStockyardTimeUtil.criaDataComString(dataHoraFinalInterdicao.getDataHora(), PropertiesUtil
                  .buscarPropriedade("formato.campo.datahora"));
      
      interdicaoBaliza.setDataInicial(dataInicio);
      interdicaoBaliza.setDataFinal(dataFim);
      
      validarInformacoesInterdicao(balizaInterdicao,interdicaoBaliza);           
      
      List<Interdicao> itens = mapBalizaInterdicoes.get(balizaInterdicao.getMetaBaliza());
      itens.add(interdicaoBaliza);
      addListaBalizasInterditadas(balizaInterdicao);
   }

    /** Metodo que seleciona todas as balizas da tabela */
   private void selecionarTodasBalizas()
   {
      for (int i = 0; i < tblListaBalizas.getRowCount(); i++)
      {
         tblListaBalizas.setValueAt("TRUE", i, COL_LISTA_BALIZA_CHECK);
      }
   }

   /** Metodo que seleciona todas as balizas da tabela */
   private void deselecionarTodasBalizas()
   {
      for (int i = 0; i < tblListaBalizas.getRowCount(); i++)
      {
         tblListaBalizas.setValueAt("FALSE", i, COL_LISTA_BALIZA_CHECK);
      }
   }

   /** Metodo que seleciona todas as balizas da tabela */
   private void selecionarTodasBalizasInterditadas()
   {
      for (int i = 0; i < tblInterdicoesRealizadas.getRowCount(); i++)
      {
         tblInterdicoesRealizadas.setValueAt("TRUE", i, COL_INTERDICAO_BALIZA_LIBERACAO);
      }
   }

   /** Metodo que seleciona todas as balizas da tabela */
   private void deselecionarTodasBalizasInterditadas()
   {
      for (int i = 0; i < tblInterdicoesRealizadas.getRowCount(); i++)
      {
         tblInterdicoesRealizadas.setValueAt("FALSE", i, COL_INTERDICAO_BALIZA_LIBERACAO);
      }
   }

   /** Metodo que atualiza a lista de balizas para interdicao
    * de acordo com a lista de balizas do patio selecionado
    */
   private void atualizarListaDeBalizas() throws ErroSistemicoException
   {
       listaBalizas = interfacePatioSelecionada.getPatioVisualizado().getListaDeBalizas(interfacePatioSelecionada.getHoraSituacao());
              
      Collections.sort(listaBalizas, Baliza.comparadorBaliza);
      vInformacoesBalizas.removeAllElements();
      for (Baliza baliza : listaBalizas)
      {
          List<Interdicao> itens = mapBalizaInterdicoes.get(baliza.getMetaBaliza());          
          if (baliza.getMetaBaliza().getListaInterdicao() != null
              && !baliza.getMetaBaliza().getListaInterdicao().isEmpty() ) {
              if (itens == null) itens = new ArrayList<Interdicao>();
              itens.removeAll(baliza.getMetaBaliza().getListaInterdicao());
              itens.addAll(baliza.getMetaBaliza().getListaInterdicao());
              addListaBalizasInterditadas(baliza);
              mapBalizaInterdicoes.put(baliza.getMetaBaliza(), itens);
          } else if (itens == null) {
              itens = new ArrayList<Interdicao>();
              mapBalizaInterdicoes.put(baliza.getMetaBaliza(), itens);
          }
              Object[] dados = new Object[3];
              dados[COL_LISTA_BALIZA_CHECK] = new JCheckBox("", Boolean.FALSE);
              dados[COL_LISTA_BAZLIA_NOME] = baliza.getNomeBaliza();
              dados[COL_LISTA_BALIZA_NUMERO] = DSSStockyardFuncoesTexto.getCodigoFormatado(3, baliza.getNumero());
              vInformacoesBalizas.add(new Vector(Arrays.asList(dados)));
         
          
         /* // caso a lista de interdicao da baliza nao for nula e nao for vazia significa que ela jah esta interditada
         if (baliza.getMetaBaliza().balizaInterditado(horaExecucao) != null && !baliza.getMetaBaliza().getListaInterdicao().isEmpty())
         {
            addListaBalizasInterditadas(baliza);
         }
         else
         {
            Object[] dados = new Object[3];
            dados[COL_LISTA_BALIZA_CHECK] = new JCheckBox("", Boolean.FALSE);
            dados[COL_LISTA_BAZLIA_NOME] = baliza.getNomeBaliza();
            dados[COL_LISTA_BALIZA_NUMERO] = DSSStockyardFuncoesTexto.getCodigoFormatado(3, baliza.getNumero());
            vInformacoesBalizas.add(new Vector(Arrays.asList(dados)));
         }
*/         // desenha as informacoes na tabela
         CFlexStockyardFuncoesTabela.setInformacoesTabela(tblListaBalizas, vInformacoesBalizas, listaColunasBalizas);
      }

      // removendo as balizas interditadas da lista de balizas clonadas
      /*for (Baliza balizaInterditada : getListaBalizasInterditadas())
      {
          listaBalizas.remove(balizaInterditada);
      }*/
   }

   public List<Baliza> getListaBalizasInterditadas()
   {
      if (listaBalizasInterditadas == null)
      {
         listaBalizasInterditadas = new ArrayList<Baliza>();
      }
      return listaBalizasInterditadas;
   }

   /**
    * Metodo que adiciona uma baliza a lista de balizas interditadas
    * @param balizaInterditada
    */
   public void addListaBalizasInterditadas(Baliza balizaInterditada)
   {
       if (!getListaBalizasInterditadas().contains(balizaInterditada))
       {
          getListaBalizasInterditadas().add(balizaInterditada);
       }
   }

   public Boolean getOperacaoCanceladaPeloUsuario()
   {
      return operacaoCanceladaPeloUsuario;
   }

   private Baliza obterBalizaDaListaPeloNumero(List<Baliza> listaDeBalizas, Integer numeroBaliza)
   {
      Baliza balizaRetorno = null;
      for (Baliza balizaClonada : listaDeBalizas)
      {
         if (balizaClonada.getNumero().equals(numeroBaliza))
         {
            balizaRetorno = balizaClonada;
            break;
         }
      }
      return balizaRetorno;
   }

  


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chkSelecionarTodasBalizas;
    private javax.swing.JCheckBox chkSelecionarTodasBalizasInterditadas;
    private javax.swing.JComboBox cmbPatio;
    private javax.swing.JButton cmdConfirmar;
    private javax.swing.JButton cmdDesistir;
    private javax.swing.JButton cmdInterditarBalizasSelecionadas;
    private javax.swing.JButton cmdLiberarInterdicoesSelecionadas;
    private javax.swing.JButton cmdLimparInformacoesBalizas;
    private com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex dataHoraFinalInterdicao;
    private com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex dataHoraInicioInterdicao;
    private javax.swing.ButtonGroup grpTipoInterdicao;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel pnlInformacoesBaliza;
    private javax.swing.JPanel pnlInterdicoesRealizadas;
    private javax.swing.JPanel pnlPrincipal;
    private javax.swing.JScrollPane scrInterdicoesRealizadas;
    private javax.swing.JScrollPane scrListaBalizas;
    private com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable tblInterdicoesRealizadas;
    private com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable tblListaBalizas;
    // End of variables declaration//GEN-END:variables
}
