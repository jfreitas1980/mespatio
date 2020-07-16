/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NovaInterfaceNavio.java
 *
 * Created on 27/04/2009, 09:53:11
 */

package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingConstants;

import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.cliente.util.ComboBoxRederer;
import com.hdntec.gestao.cliente.util.tabela.ColunaTabela;
import com.hdntec.gestao.cliente.util.telas.DSSStockyardTelaUtil;
import com.hdntec.gestao.domain.navios.dao.MetaNavioDAO;
import com.hdntec.gestao.domain.navios.entity.MetaNavio;
import com.hdntec.gestao.domain.navios.entity.status.Carga;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.ValidacaoCampoException;
import com.hdntec.gestao.util.PropertiesUtil;

/**
 *
 * @author guilherme
 */
public abstract class InterfaceEditaNavioNaFilaDeNavios extends javax.swing.JPanel {

    private Boolean operacaoCanceladaPeloUsuario = Boolean.FALSE;
    private Integer operacaoSelecionada = null;
    private InterfaceMensagem interfaceMensagem;
    private EditaDadosNavioNaFilaDeNavios editaDadosNavioNaFilaDeNavios = new EditaDadosNavioNaFilaDeNavios(this);
    private EditaDadosOrientacaoDeEmbarqueDaCarga editaOrientacaoEmbarque = new EditaDadosOrientacaoDeEmbarqueDaCarga(this);
    private EdicaoDadosCarga edicaoDadosCarga = new EdicaoDadosCarga(this, null);
    private InterfaceNavio interfaceNavio;
    public static final int MODO_INSERIR = 1;
    public static final int MODO_EDITAR = 2;
    public static final int MODO_EDITAR_ATRACADO = 3;
    //tooltip do Status do navio
    private String[] tooltips = {PropertiesUtil.getMessage("tooltip.nao.confirmado"), PropertiesUtil.getMessage("tooltip.confirmado"), PropertiesUtil.getMessage("tooltip.nabarra"), PropertiesUtil.getMessage("tooltip.atracado"), PropertiesUtil.getMessage("tooltip.embarcado")};
    private List<ColunaTabela> listaColunasOrientEmbarqueItensControle;
    private List<ColunaTabela> listaColunasCargas;
    ControladorInterfaceFilaDeNavios controladorInterfaceFilaDeNavios;
    private List<TipoProduto> listaTipoProduto;
    private List<Carga> listaCargas;
    private List<Carga> listaCargasExcluidas = new ArrayList<Carga>();
    private List<InterfaceCarga> listaInterfaceCargasExcluidas = new ArrayList<InterfaceCarga>();
    private List<InterfaceCarga> listaInterfaceCargasIncluidas = new ArrayList<InterfaceCarga>();
    private List<Carga> listaCargasClonadas = new ArrayList<Carga>();

    /** Creates new form NovaInterfaceNavio */
    public InterfaceEditaNavioNaFilaDeNavios(InterfaceNavio interfaceNavio, ControladorInterfaceFilaDeNavios controladorInterfaceFilaDeNavios, Integer modoOperacao) {
        initComponents();
        this.interfaceNavio = interfaceNavio;
        this.operacaoSelecionada = modoOperacao;
        this.controladorInterfaceFilaDeNavios = controladorInterfaceFilaDeNavios;
        //atualiza os campos editaveis
        editaDadosNavioNaFilaDeNavios.setControladorInterfaceFilaDeNavios(controladorInterfaceFilaDeNavios);
        editaOrientacaoEmbarque.setControladorInterfaceFilaDeNavios(controladorInterfaceFilaDeNavios);
        edicaoDadosCarga.setControladorInterfaceFilaDeNavios(controladorInterfaceFilaDeNavios);
        insereToolTipComboBox();
        criaColunasTipoControle();

        try {
            listaTipoProduto = getInterfaceNavio().getControladorInterfaceFilaDeNavios().getInterfaceFilaDeNavios().getInterfaceInicial().getListaTiposProduto();
            atualizaCargas();
            if (interfaceNavio.getNavioVisualizado() != null) {
                atualizaCampos();
            }
        } catch (ErroSistemicoException ex) {
            Logger.getLogger(InterfaceEditaNavioNaFilaDeNavios.class.getName()).log(Level.SEVERE, null, ex);
        }
        criaColunasCargas();
        enableCamposDeTela(modoOperacao);
        cmbStatus.setEnabled(Boolean.FALSE);
    }

    private void insereToolTipComboBox() {
        ComboBoxRederer comboBoxRederer = new ComboBoxRederer();
        comboBoxRederer.setTooltips(tooltips);
        //adiciona do comboBox status do Navio
        getCmbStatus().setRenderer(comboBoxRederer);
    }

    public void atualizaCargas () {
        listaCargas = new ArrayList<Carga>();
        listaCargas.addAll(getInterfaceNavio().getNavioVisualizado().getListaDeCargasDoNavio(getInterfaceNavio().getHoraSituacao()));
    }

    private void enableCamposDeTela(Integer modoOperacao) {   //se a interfaceNavio for igual a null, a unica opção valida eh apenas de inserir um navio
        if (modoOperacao == InterfaceEditaNavioNaFilaDeNavios.MODO_INSERIR) {
            getEditaDadosNavioNaFilaDeNavios().habilitaInserir();
            getEditaDadosNavioNaFilaDeNavios().inicializaCamposValorZero();
            getPainelTabulado().setEnabledAt(1, false);
            getPainelTabulado().setEnabledAt(2, false);
            getEditaOrientacaoEmbarque().atualizaDadosCampos();
        } else if (modoOperacao == InterfaceEditaNavioNaFilaDeNavios.MODO_EDITAR) {
            getEditaDadosNavioNaFilaDeNavios().atualizaDados(interfaceNavio);
            getEditaOrientacaoEmbarque().atualizaDadosCampos();
            getEdicaoDadosCarga().atualizaDadosTela();
        } else {
            getEditaDadosNavioNaFilaDeNavios().atualizaDados(interfaceNavio);
            getEditaDadosNavioNaFilaDeNavios().desabilitaCampos();
            getEditaOrientacaoEmbarque().atualizaDadosCampos();
            getEdicaoDadosCarga().atualizaDadosTela();
        }
    }

    private void criaColunasCargas () {
        setListaColunasCargas(new ArrayList<ColunaTabela>());
        ColunaTabela coluna;

        //Coluna com nome da carga.
        coluna = new ColunaTabela();
        coluna.setLargura(140);
        coluna.setTitulo(PropertiesUtil.getMessage("mensagem.carga"));
        coluna.setAlinhamento(SwingConstants.LEFT);
        coluna.setRedimensionar(true);
        coluna.setEditar(false);
        coluna.setTipoEditor(ColunaTabela.COL_TIPO_TEXT);
        getListaColunasCargas().add(coluna);

        //Coluna com a quantidade embarcada.
        coluna = new ColunaTabela();
        coluna.setLargura(150);
        coluna.setTitulo(PropertiesUtil.getMessage("mensagem.quantidade.embarcada"));
        coluna.setAlinhamento(SwingConstants.LEFT);
        coluna.setRedimensionar(true);
        coluna.setEditar(false);
        coluna.setTipoEditor(ColunaTabela.COL_TIPO_TEXT);
        getListaColunasCargas().add(coluna);

        //Coluna com o tipo de produto em um JComboBox
        coluna = new ColunaTabela();
        coluna.setLargura(120);
        coluna.setTitulo(PropertiesUtil.getMessage("label.tipo.produto"));
        coluna.setAlinhamento(SwingConstants.LEFT);
        coluna.setRedimensionar(true);
        coluna.setEditar(false);
        coluna.setTipoEditor(ColunaTabela.COL_TIPO_COMBOBOX);

        Vector<TipoProduto> vetorTipoProduto = new Vector<TipoProduto>();
        vetorTipoProduto.addAll(getListaTipoProduto());
        coluna.setVItensCombo(vetorTipoProduto);
        getListaColunasCargas().add(coluna);

        //Coluna carregada de acordo com o tipo de produto selecionado.
        coluna = new ColunaTabela();
        coluna.setLargura(120);
        coluna.setTitulo(PropertiesUtil.getMessage("mensagem.coluna.tabela.descricao"));
        coluna.setAlinhamento(SwingConstants.CENTER);
        coluna.setRedimensionar(true);
        coluna.setEditar(false);
        getListaColunasCargas().add(coluna);
    }

    private void criaColunasTipoControle() {
        setListaColunasOrientEmbarqueItensControle(new ArrayList<ColunaTabela>());
        ColunaTabela coluna;

        //Coluna descricao de tipo de item de controle.
        coluna = new ColunaTabela();
        coluna.setLargura(200);
        coluna.setTitulo(PropertiesUtil.getMessage("mensagem.coluna.tabela.tipo.item.controle"));
        coluna.setAlinhamento(SwingConstants.LEFT);
        coluna.setRedimensionar(true);
        coluna.setEditar(false);
        getListaColunasOrientEmbarqueItensControle().add(coluna);

        //Coluna valor item de controle.
        coluna = new ColunaTabela();
        coluna.setLargura(80);
        coluna.setTitulo(PropertiesUtil.getMessage("mensagem.coluna.tabela.valor"));
        coluna.setAlinhamento(SwingConstants.LEFT);
        coluna.setRedimensionar(true);
        coluna.setEditar(true);
        coluna.setTipoEditor(ColunaTabela.COL_TIPO_TEXT);
        getListaColunasOrientEmbarqueItensControle().add(coluna);

        //Coluna desvio padrao valor.
        /*coluna = new ColunaTabela();
        coluna.setLargura(120);
        coluna.setTitulo(PropertiesUtil.getMessage("mensagem.coluna.tabela.devio.padrao.valor"));
        coluna.setAlinhamento(SwingConstants.LEFT);
        coluna.setRedimensionar(true);
        coluna.setEditar(true);
        coluna.setTipoEditor(ColunaTabela.COL_TIPO_TEXT);
        getListaColunasOrientEmbarqueItensControle().add(coluna);*/

        //Coluna Unidade de medida do item de controle.
        coluna = new ColunaTabela();
        coluna.setLargura(50);
        coluna.setTitulo(PropertiesUtil.getMessage("mensagem.coluna.tabela.unidade"));
        coluna.setAlinhamento(SwingConstants.CENTER);
        coluna.setRedimensionar(true);
        coluna.setEditar(false);
        getListaColunasOrientEmbarqueItensControle().add(coluna);
    }

    public void atualizaCampos() throws ErroSistemicoException {
        //atualiza comboBox carga
        getCmbCarga().removeAllItems();
        if (operacaoSelecionada == MODO_EDITAR || operacaoSelecionada == MODO_EDITAR_ATRACADO) {
            for (Carga carga1 : getListaCargas()) {
                getCmbCarga().addItem(carga1.getIdentificadorCarga());
            }
        }
        //atualiza combo do tipoDeProduto
        getCmbTipoProduto().removeAllItems();
        for (TipoProduto tipoProduto : getListaTipoProduto()) {
            getCmbTipoProduto().addItem(tipoProduto);
        }
    }

    /**
     * Metodo criado abstrato, pois sua implementacao sera na classe que o instanciou
     */
    public abstract void fecharJanela();


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        painelTabulado = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        lblStatus = new javax.swing.JLabel();
        lblCliente = new javax.swing.JLabel();
        cmbStatus = new javax.swing.JComboBox();
        cmbCliente = new javax.swing.JComboBox();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel4 = new javax.swing.JPanel();
        lblDtChegada = new javax.swing.JLabel();
        lblDtEmbarque = new javax.swing.JLabel();
        lblDtSaida = new javax.swing.JLabel();
        dtChegadaCalendarioHoraCFlex = new com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex();
        dtEmbarqueCalendarioHoraCFlex = new com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex();
        dtSaidaCalendarioHoraCFlex = new com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex();
        jPanel5 = new javax.swing.JPanel();
        lblNomeNavio = new javax.swing.JLabel();
        lblDwt = new javax.swing.JLabel();
        lblEta = new javax.swing.JLabel();
        etaCalendarioHoraCFlex = new com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex();
        txtDwt = new javax.swing.JTextField();
        txtNomeNavio = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jSeparator3 = new javax.swing.JSeparator();
        jScrollPane3 = new javax.swing.JScrollPane();
        CargaCFlexStockyardJTable = new com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable();
        btnInserirCarga = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        lblPenalizacao = new javax.swing.JLabel();
        lblQuantidade = new javax.swing.JLabel();
        lblTipoProduto = new javax.swing.JLabel();
        lblCarga = new javax.swing.JLabel();
        cmbPenalizacao = new javax.swing.JComboBox();
        txtQtd = new javax.swing.JTextField();
        cmbCargaEmbarque = new javax.swing.JComboBox();
        cmbTipoProduto = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        OrientEmbarqueCFlexStockyardJTable = new com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable();
        jSeparator2 = new javax.swing.JSeparator();
        btnConcluirEdicao = new javax.swing.JButton();
        btnDesistirNavio = new javax.swing.JButton();
        btnConfirmarNavio = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Edita informações do navio na fila de navios"));

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblStatus.setText("Status");

        lblCliente.setText("Cliente");

        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "N", "C", "B", "A", "E" }));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCliente)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(lblStatus)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(399, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblStatus)
                    .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCliente)
                    .addComponent(cmbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblDtChegada.setText("Data de chegada");

        lblDtEmbarque.setText("Data de atracação");

        lblDtSaida.setText("Data de saída");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(lblDtChegada)
                        .addGap(18, 18, 18)
                        .addComponent(dtChegadaCalendarioHoraCFlex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                            .addComponent(lblDtSaida)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dtSaidaCalendarioHoraCFlex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                            .addComponent(lblDtEmbarque)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(dtEmbarqueCalendarioHoraCFlex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(189, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblDtChegada, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(dtChegadaCalendarioHoraCFlex, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(dtEmbarqueCalendarioHoraCFlex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(lblDtEmbarque)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblDtSaida)
                    .addComponent(dtSaidaCalendarioHoraCFlex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblNomeNavio.setText("Navio");

        lblDwt.setText("Dwt");

        lblEta.setText("ETA");

        txtDwt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDwtKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(lblNomeNavio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtNomeNavio, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(lblDwt)
                        .addGap(18, 18, 18)
                        .addComponent(txtDwt)))
                .addGap(57, 57, 57)
                .addComponent(lblEta)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(etaCalendarioHoraCFlex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNomeNavio)
                    .addComponent(txtNomeNavio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDwt)
                    .addComponent(lblEta)
                    .addComponent(txtDwt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(12, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(62, Short.MAX_VALUE)
                .addComponent(etaCalendarioHoraCFlex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        painelTabulado.addTab("Dados do navio", jPanel1);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Edita informações da Carga"));

        CargaCFlexStockyardJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        CargaCFlexStockyardJTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CargaCFlexStockyardJTableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(CargaCFlexStockyardJTable);

        StringBuffer value = new StringBuffer();
        value.append(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens")).append("/ok.png");
        btnInserirCarga.setIcon(new javax.swing.ImageIcon(value.toString())); // NOI18N
        btnInserirCarga.setText("Inserir nova carga");
        btnInserirCarga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInserirCargaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE)
                    .addComponent(btnInserirCarga, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(btnInserirCarga)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        painelTabulado.addTab("Edição de carga", jPanel3);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Edita informações da Orientacao de Embarque"));

        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblPenalizacao.setText("Penalização");

        lblQuantidade.setText("Quantidade");

        lblTipoProduto.setText("Tipo produto");

        lblCarga.setText("Carga");

        cmbPenalizacao.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Sim", "Não" }));

        txtQtd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtQtdKeyReleased(evt);
            }
        });

        cmbCargaEmbarque.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCargaEmbarqueActionPerformed(evt);
            }
        });

        cmbTipoProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTipoProdutoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPenalizacao)
                    .addComponent(lblQuantidade))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbPenalizacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtQtd, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblTipoProduto)
                    .addComponent(lblCarga))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbCargaEmbarque, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbTipoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPenalizacao)
                    .addComponent(cmbPenalizacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCarga)
                    .addComponent(cmbCargaEmbarque, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblQuantidade)
                    .addComponent(txtQtd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTipoProduto)
                    .addComponent(cmbTipoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        OrientEmbarqueCFlexStockyardJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(OrientEmbarqueCFlexStockyardJTable);

        
        value = new StringBuffer();
        value.append(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens")).append("/edit.png");
        btnConcluirEdicao.setIcon(new javax.swing.ImageIcon(value.toString())); // NOI18N
        btnConcluirEdicao.setText("Concluir edições");
        btnConcluirEdicao.setPreferredSize(new java.awt.Dimension(133, 23));
        btnConcluirEdicao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnConcluirEdicaoMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnConcluirEdicao, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnConcluirEdicao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60))
        );

        painelTabulado.addTab("Orientação de embarque", jPanel2);

        
        value = new StringBuffer();
        value.append(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens")).append("/exit.png");
        btnDesistirNavio.setIcon(new javax.swing.ImageIcon(value.toString())); // NOI18N
        
        btnDesistirNavio.setText("Desistir");
        btnDesistirNavio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDesistirActionPerformed(evt);
            }
        });

       value = new StringBuffer();
       value.append(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens")).append("/confirmar.png");
       btnConfirmarNavio.setIcon(new javax.swing.ImageIcon(value.toString())); // NOI18N
               
       btnConfirmarNavio.setText("Confirmar");
       btnConfirmarNavio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(377, Short.MAX_VALUE)
                .addComponent(btnDesistirNavio)
                .addGap(18, 18, 18)
                .addComponent(btnConfirmarNavio)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(painelTabulado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(painelTabulado, javax.swing.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnConfirmarNavio)
                    .addComponent(btnDesistirNavio, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        painelTabulado.getAccessibleContext().setAccessibleName("Dados do navio");
    }// </editor-fold>//GEN-END:initComponents

    private void btnDesistirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDesistirActionPerformed
        fecharJanela();
        this.getParent().getParent().getParent().getParent().setVisible(false);
	}//GEN-LAST:event_btnDesistirActionPerformed

    private void txtQtdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQtdKeyReleased
        try{
            if(txtQtd.getText().length() > 15){
                txtQtd.setForeground(Color.RED);
                List<String> listaParametros = new ArrayList<String>();
                listaParametros.add(txtQtd.getText());
                listaParametros.add("15");
                throw new ValidacaoCampoException("exception.validacao.tamanho.de.campo", listaParametros);
            }else{
                txtQtd.setForeground(Color.BLACK);
            }
        }catch(ValidacaoCampoException ex){
        	interfaceNavio.getControladorInterfaceFilaDeNavios().getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            interfaceNavio.getControladorInterfaceFilaDeNavios().ativarMensagem(interfaceMensagem);
        }
    }//GEN-LAST:event_txtQtdKeyReleased

    private void btnConfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmarActionPerformed
        if (editaDadosNavioNaFilaDeNavios.validaDados(interfaceNavio)) {
            if (!getListaCargasClonadas().isEmpty()) {
                editaDadosNavioNaFilaDeNavios.atualizaCargas(getInterfaceNavio().getNavioVisualizado().getListaDeCargasDoNavio(getInterfaceNavio().getHoraSituacao()), getListaCargasClonadas());
            }            
            this.operacaoCanceladaPeloUsuario = Boolean.FALSE;
            // chegando ate o JDialog
            
            
            MetaNavio metaNavio =  interfaceNavio.getNavioVisualizado().getMetaNavio();
            
            MetaNavioDAO dao = new MetaNavioDAO();
            try {
				dao.salvaMetaNavio(metaNavio);
			} catch (ErroSistemicoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            dao = null;

               
            DSSStockyardTelaUtil.getParentDialog(this).setVisible(false);
        }
    }//GEN-LAST:event_btnConfirmarActionPerformed

    private void btnInserirCargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInserirCargaActionPerformed
        try {
            controladorInterfaceFilaDeNavios.editarNavio(interfaceNavio, this, InterfaceEdicaoCarga.MODO_INSERIR, null);
        } catch (ErroSistemicoException ex) {
        	controladorInterfaceFilaDeNavios.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            interfaceMensagem.setDescricaoMensagem(PropertiesUtil.buscarPropriedade("exception.erro.sistemico"));
            controladorInterfaceFilaDeNavios.ativarMensagem(interfaceMensagem);
            Logger.getLogger(InterfaceEditaNavioNaFilaDeNavios.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
}//GEN-LAST:event_btnInserirCargaActionPerformed

    private void CargaCFlexStockyardJTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CargaCFlexStockyardJTableMouseClicked
        try {
            String identificadorCarga = (getCargaCFlexStockyardJTable().getValueAt(getCargaCFlexStockyardJTable().getSelectedRow(), 0)).toString();
            controladorInterfaceFilaDeNavios.editarNavio(interfaceNavio, this, InterfaceEdicaoCarga.MODO_EDITAR, identificadorCarga);
        } catch (ErroSistemicoException ex) {
        	controladorInterfaceFilaDeNavios.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            interfaceMensagem.setDescricaoMensagem(PropertiesUtil.buscarPropriedade("exception.erro.sistemico"));
            controladorInterfaceFilaDeNavios.ativarMensagem(interfaceMensagem);
            Logger.getLogger(InterfaceEditaNavioNaFilaDeNavios.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
}//GEN-LAST:event_CargaCFlexStockyardJTableMouseClicked

    private void cmbCargaEmbarqueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCargaEmbarqueActionPerformed
        editaOrientacaoEmbarque.atualizaDadosCampos();


    }//GEN-LAST:event_cmbCargaEmbarqueActionPerformed

    private void btnConcluirEdicaoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConcluirEdicaoMouseClicked
        if (editaOrientacaoEmbarque.validaDadosEditados()) {
        	controladorInterfaceFilaDeNavios.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
            interfaceMensagem.setDescricaoMensagem(PropertiesUtil.getMessage("label.filanavio.edicao.concluida"));
            controladorInterfaceFilaDeNavios.ativarMensagem(interfaceMensagem);
        }
    }//GEN-LAST:event_btnConcluirEdicaoMouseClicked


    private void cmbTipoProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTipoProdutoActionPerformed

    }//GEN-LAST:event_cmbTipoProdutoActionPerformed

    private void txtDwtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDwtKeyReleased
        try{
            if(txtDwt.getText().length() > 15){
                txtDwt.setForeground(Color.RED);
                List<String> listaParametros = new ArrayList<String>();
                listaParametros.add(txtDwt.getText());
                listaParametros.add("15");
                throw new ValidacaoCampoException("exception.validacao.tamanho.de.campo", listaParametros);
            }else{
                txtDwt.setForeground(Color.BLACK);
            }
        }catch(ValidacaoCampoException ex){
            interfaceNavio.getControladorInterfaceFilaDeNavios().getInterfaceInicial().desativarMensagemProcessamento();
            InterfaceMensagem interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            interfaceNavio.getControladorInterfaceFilaDeNavios().ativarMensagem(interfaceMensagem);
        }
}//GEN-LAST:event_txtDwtKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable CargaCFlexStockyardJTable;
    private com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable OrientEmbarqueCFlexStockyardJTable;
    private javax.swing.JButton btnConcluirEdicao;
    private javax.swing.JButton btnConfirmarNavio;
    private javax.swing.JButton btnDesistirNavio;
    private javax.swing.JButton btnInserirCarga;
    private javax.swing.JComboBox cmbCargaEmbarque;
    private javax.swing.JComboBox cmbCliente;
    private javax.swing.JComboBox cmbPenalizacao;
    private javax.swing.JComboBox cmbStatus;
    private javax.swing.JComboBox cmbTipoProduto;
    private com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex dtChegadaCalendarioHoraCFlex;
    private com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex dtEmbarqueCalendarioHoraCFlex;
    private com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex dtSaidaCalendarioHoraCFlex;
    private com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex etaCalendarioHoraCFlex;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel lblCarga;
    private javax.swing.JLabel lblCliente;
    private javax.swing.JLabel lblDtChegada;
    private javax.swing.JLabel lblDtEmbarque;
    private javax.swing.JLabel lblDtSaida;
    private javax.swing.JLabel lblDwt;
    private javax.swing.JLabel lblEta;
    private javax.swing.JLabel lblNomeNavio;
    private javax.swing.JLabel lblPenalizacao;
    private javax.swing.JLabel lblQuantidade;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTipoProduto;
    private javax.swing.JTabbedPane painelTabulado;
    private javax.swing.JTextField txtDwt;
    private javax.swing.JTextField txtNomeNavio;
    private javax.swing.JTextField txtQtd;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the interfaceNavio
     */
    public InterfaceNavio getInterfaceNavio() {
        return interfaceNavio;
    }

    /**
     * @param interfaceNavio the interfaceNavio to set
     */
    public void setInterfaceNavio(InterfaceNavio interfaceNavio) {
        this.interfaceNavio = interfaceNavio;
    }

    /**
     * @return the cmbCarga
     */
    public javax.swing.JComboBox getCmbCarga() {
        return cmbCargaEmbarque;
    }

    /**
     * @param cmbCarga the cmbCarga to set
     */
    public void setCmbCarga(javax.swing.JComboBox cmbCarga) {
        this.cmbCargaEmbarque = cmbCarga;
    }

    /**
     * @return the cmbCliente
     */
    public javax.swing.JComboBox getCmbCliente() {
        return cmbCliente;
    }

    /**
     * @param cmbCliente the cmbCliente to set
     */
    public void setCmbCliente(javax.swing.JComboBox cmbCliente) {
        this.cmbCliente = cmbCliente;
    }

    /**
     * @return the cmbPenalizacao
     */
    public javax.swing.JComboBox getCmbPenalizacao() {
        return cmbPenalizacao;
    }

    /**
     * @param cmbPenalizacao the cmbPenalizacao to set
     */
    public void setCmbPenalizacao(javax.swing.JComboBox cmbPenalizacao) {
        this.cmbPenalizacao = cmbPenalizacao;
    }

    /**
     * @return the cmbStatus
     */
    public javax.swing.JComboBox getCmbStatus() {
        return cmbStatus;
    }

    /**
     * @param cmbStatus the cmbStatus to set
     */
    public void setCmbStatus(javax.swing.JComboBox cmbStatus) {
        this.cmbStatus = cmbStatus;
    }

    /**
     * @return the cmbTipoProduto
     */
    public javax.swing.JComboBox getCmbTipoProduto() {
        return cmbTipoProduto;
    }

    /**
     * @param cmbTipoProduto the cmbTipoProduto to set
     */
    public void setCmbTipoProduto(javax.swing.JComboBox cmbTipoProduto) {
        this.cmbTipoProduto = cmbTipoProduto;
    }

    /**
     * @return the dtChegadaCalendarioHoraCFlex
     */
    public com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex getDtChegadaCalendarioHoraCFlex() {
        return dtChegadaCalendarioHoraCFlex;
    }

    /**
     * @param dtChegadaCalendarioHoraCFlex the dtChegadaCalendarioHoraCFlex to set
     */
    public void setDtChegadaCalendarioHoraCFlex(com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex dtChegadaCalendarioHoraCFlex) {
        this.dtChegadaCalendarioHoraCFlex = dtChegadaCalendarioHoraCFlex;
    }

    /**
     * @return the dtEmbarqueCalendarioHoraCFlex
     */
    public com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex getDtEmbarqueCalendarioHoraCFlex() {
        return dtEmbarqueCalendarioHoraCFlex;
    }

    /**
     * @param dtEmbarqueCalendarioHoraCFlex the dtEmbarqueCalendarioHoraCFlex to set
     */
    public void setDtEmbarqueCalendarioHoraCFlex(com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex dtEmbarqueCalendarioHoraCFlex) {
        this.dtEmbarqueCalendarioHoraCFlex = dtEmbarqueCalendarioHoraCFlex;
    }

    /**
     * @return the dtSaidaCalendarioHoraCFlex
     */
    public com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex getDtSaidaCalendarioHoraCFlex() {
        return dtSaidaCalendarioHoraCFlex;
    }

    /**
     * @param dtSaidaCalendarioHoraCFlex the dtSaidaCalendarioHoraCFlex to set
     */
    public void setDtSaidaCalendarioHoraCFlex(com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex dtSaidaCalendarioHoraCFlex) {
        this.dtSaidaCalendarioHoraCFlex = dtSaidaCalendarioHoraCFlex;
    }

    /**
     * @return the etaCalendarioHoraCFlex
     */
    public com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex getEtaCalendarioHoraCFlex() {
        return etaCalendarioHoraCFlex;
    }

    /**
     * @param etaCalendarioHoraCFlex the etaCalendarioHoraCFlex to set
     */
    public void setEtaCalendarioHoraCFlex(com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex etaCalendarioHoraCFlex) {
        this.etaCalendarioHoraCFlex = etaCalendarioHoraCFlex;
    }

    /**
     * @return the txtDwt
     */
    public javax.swing.JTextField getTxtDwt() {
        return txtDwt;
    }

    /**
     * @param txtDwt the txtDwt to set
     */
    public void setTxtDwt(javax.swing.JTextField txtDwt) {
        this.txtDwt = txtDwt;
    }

    /**
     * @return the txtNavio
     */
    public javax.swing.JTextField getTxtNomeNavio() {
        return txtNomeNavio;
    }

    /**
     * @param txtNavio the txtNavio to set
     */
    public void setTxtNavio(javax.swing.JTextField txtNavio) {
        this.txtNomeNavio = txtNavio;
    }

    /**
     * @return the txtQtd
     */
    public javax.swing.JTextField getTxtQtd() {
        return txtQtd;
    }

    /**
     * @param txtQtd the txtQtd to set
     */
    public void setTxtQtd(javax.swing.JTextField txtQtd) {
        this.txtQtd = txtQtd;
    }

    /**
     * @return the operacaoCanceladaPeloUsuario
     */
    public Boolean getOperacaoCanceladaPeloUsuario() {
        return operacaoCanceladaPeloUsuario;
    }

    /**
     * @param operacaoCanceladaPeloUsuario the operacaoCanceladaPeloUsuario to set
     */
    public void setOperacaoCanceladaPeloUsuario(Boolean operacaoCanceladaPeloUsuario) {
        this.operacaoCanceladaPeloUsuario = operacaoCanceladaPeloUsuario;
    }

    /**
     * @return the editaDadosNavioNaFilaDeNavios
     */
    public EditaDadosNavioNaFilaDeNavios getEditaDadosNavioNaFilaDeNavios() {
        return editaDadosNavioNaFilaDeNavios;
    }

    /**
     * @param editaDadosNavioNaFilaDeNavios the editaDadosNavioNaFilaDeNavios to set
     */
    public void setEditaDadosNavioNaFilaDeNavios(EditaDadosNavioNaFilaDeNavios editaDadosNavioNaFilaDeNavios) {
        this.editaDadosNavioNaFilaDeNavios = editaDadosNavioNaFilaDeNavios;
    }

    /**
     * @return the editaOrientacaoEmbarque
     */
    public EditaDadosOrientacaoDeEmbarqueDaCarga getEditaOrientacaoEmbarque() {
        return editaOrientacaoEmbarque;
    }

    /**
     * @param editaOrientacaoEmbarque the editaOrientacaoEmbarque to set
     */
    public void setEditaOrientacaoEmbarque(EditaDadosOrientacaoDeEmbarqueDaCarga editaOrientacaoEmbarque) {
        this.editaOrientacaoEmbarque = editaOrientacaoEmbarque;
    }

    /**
     * @return the edicaoDadosCarga
     */
    public EdicaoDadosCarga getEdicaoDadosCarga() {
        return edicaoDadosCarga;
    }

    /**
     * @param edicaoDadosCarga the edicaoDadosCarga to set
     */
    public void setEdicaoDadosCarga(EdicaoDadosCarga edicaoDadosCarga) {
        this.edicaoDadosCarga = edicaoDadosCarga;
    }

    /**
     * @return the operacaoSelecionada
     */
    public Integer getOperacaoSelecionada() {
        return operacaoSelecionada;
    }

    /**
     * @param operacaoSelecionada the operacaoSelecionada to set
     */
    public void setOperacaoSelecionada(Integer operacaoSelecionada) {
        this.operacaoSelecionada = operacaoSelecionada;
    }

    /**
     * @return the lblCarga
     */
    public javax.swing.JLabel getLblCarga() {
        return lblCarga;
    }

    /**
     * @param lblCarga the lblCarga to set
     */
    public void setLblCarga(javax.swing.JLabel lblCarga) {
        this.lblCarga = lblCarga;
    }

    /**
     * @return the lblCliente
     */
    public javax.swing.JLabel getLblCliente() {
        return lblCliente;
    }

    /**
     * @param lblCliente the lblCliente to set
     */
    public void setLblCliente(javax.swing.JLabel lblCliente) {
        this.lblCliente = lblCliente;
    }

    /**
     * @return the lblDtChegada
     */
    public javax.swing.JLabel getLblDtChegada() {
        return lblDtChegada;
    }

    /**
     * @param lblDtChegada the lblDtChegada to set
     */
    public void setLblDtChegada(javax.swing.JLabel lblDtChegada) {
        this.lblDtChegada = lblDtChegada;
    }

    /**
     * @return the lblDtEmbarque
     */
    public javax.swing.JLabel getLblDtEmbarque() {
        return lblDtEmbarque;
    }

    /**
     * @param lblDtEmbarque the lblDtEmbarque to set
     */
    public void setLblDtEmbarque(javax.swing.JLabel lblDtEmbarque) {
        this.lblDtEmbarque = lblDtEmbarque;
    }

    /**
     * @return the lblDtSaida
     */
    public javax.swing.JLabel getLblDtSaida() {
        return lblDtSaida;
    }

    /**
     * @param lblDtSaida the lblDtSaida to set
     */
    public void setLblDtSaida(javax.swing.JLabel lblDtSaida) {
        this.lblDtSaida = lblDtSaida;
    }

    /**
     * @return the lblDwt
     */
    public javax.swing.JLabel getLblDwt() {
        return lblDwt;
    }

    /**
     * @param lblDwt the lblDwt to set
     */
    public void setLblDwt(javax.swing.JLabel lblDwt) {
        this.lblDwt = lblDwt;
    }

    /**
     * @return the lblEta
     */
    public javax.swing.JLabel getLblEta() {
        return lblEta;
    }

    /**
     * @param lblEta the lblEta to set
     */
    public void setLblEta(javax.swing.JLabel lblEta) {
        this.lblEta = lblEta;
    }

    /**
     * @return the lblNavio
     */
    public javax.swing.JLabel getLblNomeNavio() {
        return lblNomeNavio;
    }

    /**
     * @param lblNavio the lblNavio to set
     */
    public void setLblNomeNavio(javax.swing.JLabel lblNavio) {
        this.lblNomeNavio = lblNavio;
    }

    /**
     * @return the lblPenalizacao
     */
    public javax.swing.JLabel getLblPenalizacao() {
        return lblPenalizacao;
    }

    /**
     * @param lblPenalizacao the lblPenalizacao to set
     */
    public void setLblPenalizacao(javax.swing.JLabel lblPenalizacao) {
        this.lblPenalizacao = lblPenalizacao;
    }

    /**
     * @return the lblQuantidade
     */
    public javax.swing.JLabel getLblQuantidade() {
        return lblQuantidade;
    }

    /**
     * @param lblQuantidade the lblQuantidade to set
     */
    public void setLblQuantidade(javax.swing.JLabel lblQuantidade) {
        this.lblQuantidade = lblQuantidade;
    }

    /**
     * @return the lblStatus
     */
    public javax.swing.JLabel getLblStatus() {
        return lblStatus;
    }

    /**
     * @param lblStatus the lblStatus to set
     */
    public void setLblStatus(javax.swing.JLabel lblStatus) {
        this.lblStatus = lblStatus;
    }

    /**
     * @return the lblTipoProduto
     */
    public javax.swing.JLabel getLblTipoProduto() {
        return lblTipoProduto;
    }

    /**
     * @param lblTipoProduto the lblTipoProduto to set
     */
    public void setLblTipoProduto(javax.swing.JLabel lblTipoProduto) {
        this.lblTipoProduto = lblTipoProduto;
    }


    /**
     * @return the btnConfirmarNavio
     */
    public javax.swing.JButton getBtnConfirmarNavio() {
        return btnConfirmarNavio;
    }

    /**
     * @param btnConfirmarNavio the btnConfirmarNavio to set
     */
    public void setBtnConfirmarNavio(javax.swing.JButton btnConfirmarNavio) {
        this.btnConfirmarNavio = btnConfirmarNavio;
    }



    /**
     * @return the btnDesistirNavio
     */
    public javax.swing.JButton getBtnDesistirNavio() {
        return btnDesistirNavio;
    }

    /**
     * @param btnDesistirNavio the btnDesistirNavio to set
     */
    public void setBtnDesistirNavio(javax.swing.JButton btnDesistirNavio) {
        this.btnDesistirNavio = btnDesistirNavio;
    }

    /**
     * @return the OrientEmbarqueCFlexStockyardJTable
     */
    public com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable getOrientEmbarqueCFlexStockyardJTable() {
        return OrientEmbarqueCFlexStockyardJTable;
    }

    /**
     * @param OrientEmbarqueCFlexStockyardJTable the OrientEmbarqueCFlexStockyardJTable to set
     */
    public void setOrientEmbarqueCFlexStockyardJTable(com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable OrientEmbarqueCFlexStockyardJTable) {
        this.OrientEmbarqueCFlexStockyardJTable = OrientEmbarqueCFlexStockyardJTable;
    }

    /**
     * @return the painelTabulado
     */
    public javax.swing.JTabbedPane getPainelTabulado() {
        return painelTabulado;
    }

    /**
     * @param painelTabulado the painelTabulado to set
     */
    public void setPainelTabulado(javax.swing.JTabbedPane painelTabulado) {
        this.painelTabulado = painelTabulado;
    }

    /**
     * @return the jPanel1
     */
    public javax.swing.JPanel getJPanel1() {
        return jPanel1;
    }

    /**
     * @param jPanel1 the jPanel1 to set
     */
    public void setJPanel1(javax.swing.JPanel jPanel1) {
        this.jPanel1 = jPanel1;
    }

    /**
     * @return the jPanel2
     */
    public javax.swing.JPanel getJPanel2() {
        return jPanel2;
    }

    /**
     * @param jPanel2 the jPanel2 to set
     */
    public void setJPanel2(javax.swing.JPanel jPanel2) {
        this.jPanel2 = jPanel2;
    }

    /**
     * @return the jPanel3
     */
    public javax.swing.JPanel getJPanel3() {
        return jPanel3;
    }

    /**
     * @param jPanel3 the jPanel3 to set
     */
    public void setJPanel3(javax.swing.JPanel jPanel3) {
        this.jPanel3 = jPanel3;
    }

    /**
     * @return the CargaCFlexStockyardJTable
     */
    public com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable getCargaCFlexStockyardJTable() {
        return CargaCFlexStockyardJTable;
    }

    /**
     * @param CargaCFlexStockyardJTable the CargaCFlexStockyardJTable to set
     */
    public void setCargaCFlexStockyardJTable(com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable CargaCFlexStockyardJTable) {
        this.CargaCFlexStockyardJTable = CargaCFlexStockyardJTable;
    }

    /**
     * @return the listaColunasOrientEmbarqueItensControle
     */
    public List<ColunaTabela> getListaColunasOrientEmbarqueItensControle() {
        return listaColunasOrientEmbarqueItensControle;
    }

    /**
     * @param listaColunasOrientEmbarqueItensControle the listaColunasOrientEmbarqueItensControle to set
     */
    public void setListaColunasOrientEmbarqueItensControle(List<ColunaTabela> listaColunasOrientEmbarqueItensControle) {
        this.listaColunasOrientEmbarqueItensControle = listaColunasOrientEmbarqueItensControle;
    }

    /**
     * @return the listaColunasCargas
     */
    public List<ColunaTabela> getListaColunasCargas() {
        return listaColunasCargas;
    }

    /**
     * @param listaColunasCargas the listaColunasCargas to set
     */
    public void setListaColunasCargas(List<ColunaTabela> listaColunasCargas) {
        this.listaColunasCargas = listaColunasCargas;
    }

    /**
     * @return the listaTipoProduto
     */
    public List<TipoProduto> getListaTipoProduto() {
        return listaTipoProduto;
    }

    /**
     * @param listaTipoProduto the listaTipoProduto to set
     */
    public void setListaTipoProduto(List<TipoProduto> listaTipoProduto) {
        this.listaTipoProduto = listaTipoProduto;
    }

    /**
     * @return the listaCargas
     */
    public List<Carga> getListaCargas() {
        return listaCargas;
    }

    /**
     * @param listaCargas the listaCargas to set
     */
    public void setListaCargas(List<Carga> listaCargas) {
        this.listaCargas = listaCargas;
    }

    /**
     * @return the listaCargasExcluidas
     */
    public List<Carga> getListaCargasExcluidas() {
        return listaCargasExcluidas;
    }

    /**
     * @param listaCargasExcluidas the listaCargasExcluidas to set
     */
    public void setListaCargasExcluidas(List<Carga> listaCargasExcluidas) {
        this.listaCargasExcluidas = listaCargasExcluidas;
    }

    /**
     * @return the listaInterfaceCargasExcluidas
     */
    public List<InterfaceCarga> getListaInterfaceCargasExcluidas() {
        return listaInterfaceCargasExcluidas;
    }

    /**
     * @return the listaInterfaceCargasIncluidas
     */
    public List<InterfaceCarga> getListaInterfaceCargasIncluidas() {
        return listaInterfaceCargasIncluidas;
    }

    /**
     * @return the listaCargasClonadas
     */
    public List<Carga> getListaCargasClonadas() {
        return listaCargasClonadas;
    }

    /**
     * @param listaCargasClonadas the listaCargasClonadas to set
     */
    public void setListaCargasClonadas(List<Carga> listaCargasClonadas) {
        this.listaCargasClonadas = listaCargasClonadas;
    }


}
