
package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.swing.SwingConstants;

import com.hdntec.gestao.cliente.interfaceDeDialogo.IControladorInterfaceInicial;
import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.cliente.util.numeros.DSSStockyardFuncoesNumeros;
import com.hdntec.gestao.cliente.util.tabela.CFlexStockyardFuncoesTabela;
import com.hdntec.gestao.cliente.util.tabela.CFlexStockyardTableModelCustom;
import com.hdntec.gestao.cliente.util.tabela.ColunaTabela;
import com.hdntec.gestao.cliente.util.texto.DSSStockyardFuncoesTexto;
import com.hdntec.gestao.domain.produto.entity.Rastreabilidade;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.ValidacaoCampoException;
import com.hdntec.gestao.util.PropertiesUtil;
import com.hdntec.gestao.util.datahora.DSSStockyardTimeUtil;

/**
 *
 * @author Rodrigo
 */
public class InterfaceRastreabilidade extends javax.swing.JDialog {

    /** A lista de rastreabilidade que será apresentada ao usuario */
    private List<Rastreabilidade> listaRastreabilidade;

    /** a lista de colunas da tabela de exibicao */
    private List<ColunaTabela> listaColunas;

    /** o lista que contem as informacoes para a tabela de exibicao */
    private Vector listaInformacoesTabelaRastreabilidade;

    /** o objeto rastreabilidade selecionado da tabela */
    private Rastreabilidade rastreabilidadeSelecionada;

    /** as informações necessarias para exibicao de mensagens */
    private InterfaceMensagem interfaceMensagem;

    /** o controlador da interface inicial */
    private IControladorInterfaceInicial controladorInterfaceInicial;

    /** as constantes usadas para referenciar os numeros das colunas da tabela de exibicao */
    private final int COL_PATIO_BALIZA = 0;
    private final int COL_BALIZA = 1;
    private final int COL_USINA = 2;
    private final int COL_NOME_PORAO = 3;
    private final int COL_QUANTIDADE = 4;
    private final int COL_TIPO_OPERACAO = 5;
    private final int COL_HORARIO_INICIO = 6;
    private final int COL_HORARIO_FIM = 7;

    public InterfaceRastreabilidade(java.awt.Frame parent, boolean modal, List<Rastreabilidade> listaRastrabilidade)
                    throws ErroSistemicoException {
        super(parent, modal);
        initComponents();
        tblRastreabilidade.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblRastreabilidade.setCellSelectionEnabled(true);
        tblRastreabilidade.setColumnSelectionAllowed(false);
        tblRastreabilidade.setRowSelectionAllowed(true);
        this.listaRastreabilidade = listaRastrabilidade;
        criaColunasTabela();
        listaInformacoesTabelaRastreabilidade = new Vector();
        atualizaTabelaInformacoesRastreabilidade();
        inicializaDetalhesRastreabilidade();
    }

    private void criaColunasTabela() {

        ColunaTabela col;
        listaColunas = new ArrayList<ColunaTabela>();

        col = new ColunaTabela();
        col.setTitulo(PropertiesUtil.getMessage("mensagem.coluna.tabela.nome.patio"));
        col.setLargura(90);
        col.setAlinhamento(SwingConstants.LEADING);
        col.setRedimensionar(Boolean.FALSE);
        col.setEditar(Boolean.FALSE);
        listaColunas.add(col);

        col = new ColunaTabela();
        col.setTitulo(PropertiesUtil.getMessage("mensagem.coluna.tabela.baliza.origem"));
        col.setLargura(90);
        col.setAlinhamento(SwingConstants.LEADING);
        col.setRedimensionar(Boolean.FALSE);
        col.setEditar(Boolean.FALSE);
        listaColunas.add(col);

        col = new ColunaTabela();
        col.setTitulo(PropertiesUtil.getMessage("mensagem.coluna.tabela.usina.origem"));
        col.setLargura(120);
        col.setAlinhamento(SwingConstants.LEADING);
        col.setRedimensionar(Boolean.FALSE);
        col.setEditar(Boolean.FALSE);
        listaColunas.add(col);

        col = new ColunaTabela();
        col.setTitulo(PropertiesUtil.getMessage("mensagem.coluna.tabela.nome.porao"));
        col.setLargura(80);
        col.setAlinhamento(SwingConstants.LEADING);
        col.setRedimensionar(Boolean.FALSE);
        col.setEditar(Boolean.FALSE);
        listaColunas.add(col);

        col = new ColunaTabela();
        col.setTitulo(PropertiesUtil.getMessage("mensagem.coluna.tabela.quantidade"));
        col.setLargura(100);
        col.setAlinhamento(SwingConstants.RIGHT);
        col.setRedimensionar(Boolean.FALSE);
        col.setEditar(Boolean.FALSE);
        listaColunas.add(col);

        col = new ColunaTabela();
        col.setTitulo(PropertiesUtil.getMessage("mensagem.coluna.tabela.tipo.operacao"));
        col.setLargura(110);
        col.setAlinhamento(SwingConstants.CENTER);
        col.setRedimensionar(Boolean.FALSE);
        col.setEditar(Boolean.FALSE);
        listaColunas.add(col);

        col = new ColunaTabela();
        col.setTitulo(PropertiesUtil.getMessage("mensagem.coluna.tabela.inicio.operacao"));
        col.setLargura(150);
        col.setAlinhamento(SwingConstants.CENTER);
        col.setRedimensionar(Boolean.FALSE);
        col.setEditar(Boolean.FALSE);
        listaColunas.add(col);

        col = new ColunaTabela();
        col.setTitulo(PropertiesUtil.getMessage("mensagem.coluna.tabela.termino.operacao"));
        col.setLargura(150);
        col.setAlinhamento(SwingConstants.CENTER);
        col.setRedimensionar(Boolean.FALSE);
        col.setEditar(Boolean.FALSE);
        listaColunas.add(col);

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlInterfaceRastreabilidade = new javax.swing.JPanel();
        pnlTabelaRastreabilidade = new javax.swing.JPanel();
        scrTabelaRastreabilidade = new javax.swing.JScrollPane();
        tblRastreabilidade = new com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable();
        cmdFechar = new javax.swing.JButton();
        pnlDetalhesBaliza = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtNumeroBaliza = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtNomeBaliza = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtLarguraBaliza = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtCapacidadeMaxima = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtHorarioInicioFormacao = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtHorarioFimFormacao = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtNomePilha = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtNomePatioBaliza = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        txtNumeroPatioBaliza = new javax.swing.JTextField();
        pnlDetalhesUsina = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        txtNomeUsina = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtTaxaOperacaoUsina = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtNomeCampanhaUsina = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtDataInicialCampanha = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtDataFinalCampanha = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtTipoProdutoUsina = new javax.swing.JTextField();
        txtCorTipoProdutoUsina = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtTipoProdutoPalletUsina = new javax.swing.JTextField();
        txtCorTipoProdutoPalletUsina = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtTipoProdutoScreeningUsina = new javax.swing.JTextField();
        txtCorTipoProdutoScreeningUsina = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(PropertiesUtil.getMessage("titulo.rastreabilidade.de.informacoes"));

        pnlTabelaRastreabilidade.setBorder(javax.swing.BorderFactory.createTitledBorder(PropertiesUtil
                        .getMessage("titulo.rastreabilidades.encontradas")));
        pnlTabelaRastreabilidade.setLayout(new java.awt.BorderLayout());

        tblRastreabilidade.setModel(new CFlexStockyardTableModelCustom());
        tblRastreabilidade.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblRastreabilidadeMouseClicked(evt);
            }
        });
        scrTabelaRastreabilidade.setViewportView(tblRastreabilidade);

        pnlTabelaRastreabilidade.add(scrTabelaRastreabilidade, java.awt.BorderLayout.CENTER);

        cmdFechar.setFont(new java.awt.Font("Tahoma", 1, 12));
        //cmdFechar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icones/exit.png"))); // NOI18N
        cmdFechar.setText(PropertiesUtil.getMessage("botao.fechar"));
        cmdFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdFecharActionPerformed(evt);
            }
        });

        pnlDetalhesBaliza.setBorder(javax.swing.BorderFactory.createTitledBorder(PropertiesUtil
                        .getMessage("titulo.detalhes.da.baliza")));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel1.setText(PropertiesUtil.getMessage("label.numero"));

        txtNumeroBaliza.setEditable(false);
        txtNumeroBaliza.setFont(new java.awt.Font("Tahoma", 0, 12));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel2.setText(PropertiesUtil.getMessage("label.nome.baliza"));

        txtNomeBaliza.setEditable(false);
        txtNomeBaliza.setFont(new java.awt.Font("Tahoma", 0, 12));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel3.setText(PropertiesUtil.getMessage("label.largura.baliza"));

        txtLarguraBaliza.setEditable(false);
        txtLarguraBaliza.setFont(new java.awt.Font("Tahoma", 0, 12));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel4.setText(PropertiesUtil.getMessage("label.capacidade.maxima"));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel5.setText(PropertiesUtil.getMessage("label.mts"));

        txtCapacidadeMaxima.setEditable(false);
        txtCapacidadeMaxima.setFont(new java.awt.Font("Tahoma", 0, 12));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel6.setText(PropertiesUtil.getMessage("label.tonelada"));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel7.setText(PropertiesUtil.getMessage("label.horario.de.inicio.formacao.baliza"));

        txtHorarioInicioFormacao.setEditable(false);
        txtHorarioInicioFormacao.setFont(new java.awt.Font("Tahoma", 0, 12));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel8.setText(PropertiesUtil.getMessage("label.horario.de.termino.formacao.baliza"));

        txtHorarioFimFormacao.setEditable(false);
        txtHorarioFimFormacao.setFont(new java.awt.Font("Tahoma", 0, 12));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel9.setText(PropertiesUtil.getMessage("label.nome.pilha.formada.pela.baliza"));

        txtNomePilha.setEditable(false);
        txtNomePilha.setFont(new java.awt.Font("Tahoma", 0, 12));

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel19.setText(PropertiesUtil.getMessage("label.nome.patio"));

        txtNomePatioBaliza.setEditable(false);
        txtNomePatioBaliza.setFont(new java.awt.Font("Tahoma", 0, 12));

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel20.setText(PropertiesUtil.getMessage("label.numero.do.patio"));

        txtNumeroPatioBaliza.setEditable(false);
        txtNumeroPatioBaliza.setFont(new java.awt.Font("Tahoma", 0, 12));

        javax.swing.GroupLayout pnlDetalhesBalizaLayout = new javax.swing.GroupLayout(pnlDetalhesBaliza);
        pnlDetalhesBaliza.setLayout(pnlDetalhesBalizaLayout);
        pnlDetalhesBalizaLayout
                        .setHorizontalGroup(pnlDetalhesBalizaLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(
                                                        pnlDetalhesBalizaLayout
                                                                        .createSequentialGroup()
                                                                        .addContainerGap()
                                                                        .addGroup(
                                                                                        pnlDetalhesBalizaLayout
                                                                                                        .createParallelGroup(
                                                                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                        .addGroup(
                                                                                                                        pnlDetalhesBalizaLayout
                                                                                                                                        .createParallelGroup(
                                                                                                                                                        javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                                                                        false)
                                                                                                                                        .addGroup(
                                                                                                                                                        pnlDetalhesBalizaLayout
                                                                                                                                                                        .createSequentialGroup()
                                                                                                                                                                        .addComponent(
                                                                                                                                                                                        jLabel9)
                                                                                                                                                                        .addPreferredGap(
                                                                                                                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                                                        .addComponent(
                                                                                                                                                                                        txtNomePilha))
                                                                                                                                        .addGroup(
                                                                                                                                                        pnlDetalhesBalizaLayout
                                                                                                                                                                        .createSequentialGroup()
                                                                                                                                                                        .addComponent(
                                                                                                                                                                                        jLabel8)
                                                                                                                                                                        .addPreferredGap(
                                                                                                                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                                                        .addComponent(
                                                                                                                                                                                        txtHorarioFimFormacao))
                                                                                                                                        .addGroup(
                                                                                                                                                        pnlDetalhesBalizaLayout
                                                                                                                                                                        .createSequentialGroup()
                                                                                                                                                                        .addComponent(
                                                                                                                                                                                        jLabel7)
                                                                                                                                                                        .addPreferredGap(
                                                                                                                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                                                        .addComponent(
                                                                                                                                                                                        txtHorarioInicioFormacao))
                                                                                                                                        .addGroup(
                                                                                                                                                        pnlDetalhesBalizaLayout
                                                                                                                                                                        .createSequentialGroup()
                                                                                                                                                                        .addComponent(
                                                                                                                                                                                        jLabel1)
                                                                                                                                                                        .addPreferredGap(
                                                                                                                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                                                        .addComponent(
                                                                                                                                                                                        txtNumeroBaliza,
                                                                                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                                        46,
                                                                                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                                                        .addPreferredGap(
                                                                                                                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                                                        .addComponent(
                                                                                                                                                                                        jLabel2)
                                                                                                                                                                        .addPreferredGap(
                                                                                                                                                                                        javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                                                                                        .addComponent(
                                                                                                                                                                                        txtNomeBaliza))
                                                                                                                                        .addGroup(
                                                                                                                                                        pnlDetalhesBalizaLayout
                                                                                                                                                                        .createSequentialGroup()
                                                                                                                                                                        .addComponent(
                                                                                                                                                                                        jLabel3)
                                                                                                                                                                        .addPreferredGap(
                                                                                                                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                                                        .addComponent(
                                                                                                                                                                                        txtLarguraBaliza,
                                                                                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                                        53,
                                                                                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                                                        .addGap(
                                                                                                                                                                                        4,
                                                                                                                                                                                        4,
                                                                                                                                                                                        4)
                                                                                                                                                                        .addComponent(
                                                                                                                                                                                        jLabel5)
                                                                                                                                                                        .addPreferredGap(
                                                                                                                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                                                        .addComponent(
                                                                                                                                                                                        jLabel4)
                                                                                                                                                                        .addPreferredGap(
                                                                                                                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                                                        .addComponent(
                                                                                                                                                                                        txtCapacidadeMaxima,
                                                                                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                                        61,
                                                                                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                                                        .addPreferredGap(
                                                                                                                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                                                        .addComponent(
                                                                                                                                                                                        jLabel6)))
                                                                                                        .addGroup(
                                                                                                                        pnlDetalhesBalizaLayout
                                                                                                                                        .createSequentialGroup()
                                                                                                                                        .addComponent(
                                                                                                                                                        jLabel19)
                                                                                                                                        .addPreferredGap(
                                                                                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                        .addComponent(
                                                                                                                                                        txtNomePatioBaliza,
                                                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                        149,
                                                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                        .addPreferredGap(
                                                                                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                        .addComponent(
                                                                                                                                                        jLabel20)
                                                                                                                                        .addPreferredGap(
                                                                                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                        .addComponent(
                                                                                                                                                        txtNumeroPatioBaliza,
                                                                                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                                        50,
                                                                                                                                                        Short.MAX_VALUE)))
                                                                        .addContainerGap()));
        pnlDetalhesBalizaLayout
                        .setVerticalGroup(pnlDetalhesBalizaLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(
                                                        pnlDetalhesBalizaLayout
                                                                        .createSequentialGroup()
                                                                        .addContainerGap()
                                                                        .addGroup(
                                                                                        pnlDetalhesBalizaLayout
                                                                                                        .createParallelGroup(
                                                                                                                        javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                        .addComponent(
                                                                                                                        jLabel1)
                                                                                                        .addComponent(
                                                                                                                        txtNumeroBaliza,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                        .addComponent(
                                                                                                                        jLabel2)
                                                                                                        .addComponent(
                                                                                                                        txtNomeBaliza,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                        .addPreferredGap(
                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addGroup(
                                                                                        pnlDetalhesBalizaLayout
                                                                                                        .createParallelGroup(
                                                                                                                        javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                        .addComponent(
                                                                                                                        jLabel3)
                                                                                                        .addComponent(
                                                                                                                        txtLarguraBaliza,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                        .addComponent(
                                                                                                                        jLabel5)
                                                                                                        .addComponent(
                                                                                                                        jLabel4)
                                                                                                        .addComponent(
                                                                                                                        txtCapacidadeMaxima,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                        .addComponent(
                                                                                                                        jLabel6))
                                                                        .addPreferredGap(
                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addGroup(
                                                                                        pnlDetalhesBalizaLayout
                                                                                                        .createParallelGroup(
                                                                                                                        javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                        .addComponent(
                                                                                                                        jLabel7)
                                                                                                        .addComponent(
                                                                                                                        txtHorarioInicioFormacao,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                        .addPreferredGap(
                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addGroup(
                                                                                        pnlDetalhesBalizaLayout
                                                                                                        .createParallelGroup(
                                                                                                                        javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                        .addComponent(
                                                                                                                        jLabel8)
                                                                                                        .addComponent(
                                                                                                                        txtHorarioFimFormacao,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                        .addPreferredGap(
                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addGroup(
                                                                                        pnlDetalhesBalizaLayout
                                                                                                        .createParallelGroup(
                                                                                                                        javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                        .addComponent(
                                                                                                                        jLabel9)
                                                                                                        .addComponent(
                                                                                                                        txtNomePilha,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                        .addPreferredGap(
                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addGroup(
                                                                                        pnlDetalhesBalizaLayout
                                                                                                        .createParallelGroup(
                                                                                                                        javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                        .addComponent(
                                                                                                                        jLabel19)
                                                                                                        .addComponent(
                                                                                                                        txtNomePatioBaliza,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                        .addComponent(
                                                                                                                        jLabel20)
                                                                                                        .addComponent(
                                                                                                                        txtNumeroPatioBaliza,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                        .addContainerGap(
                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                        Short.MAX_VALUE)));

        pnlDetalhesUsina.setBorder(javax.swing.BorderFactory.createTitledBorder(PropertiesUtil
                        .getMessage("titulo.detalhes.da.usina")));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel10.setText(PropertiesUtil.getMessage("labe.nome.da.usina"));

        txtNomeUsina.setEditable(false);
        txtNomeUsina.setFont(new java.awt.Font("Tahoma", 0, 12));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel11.setText(PropertiesUtil.getMessage("label.taxa.operacao"));

        txtTaxaOperacaoUsina.setEditable(false);
        txtTaxaOperacaoUsina.setFont(new java.awt.Font("Tahoma", 0, 12));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel12.setText(PropertiesUtil.getMessage("label.ton.dia"));

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel13.setText(PropertiesUtil.getMessage("label.nome.da.campanha"));

        txtNomeCampanhaUsina.setEditable(false);
        txtNomeCampanhaUsina.setFont(new java.awt.Font("Tahoma", 0, 12));

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel14.setText(PropertiesUtil.getMessage("label.data.inicio"));

        txtDataInicialCampanha.setEditable(false);
        txtDataInicialCampanha.setFont(new java.awt.Font("Tahoma", 0, 12));

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel15.setText(PropertiesUtil.getMessage("label.data.final"));

        txtDataFinalCampanha.setEditable(false);
        txtDataFinalCampanha.setFont(new java.awt.Font("Tahoma", 0, 12));

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel16.setText(PropertiesUtil.getMessage("label.tipo.produto"));

        txtTipoProdutoUsina.setEditable(false);
        txtTipoProdutoUsina.setFont(new java.awt.Font("Tahoma", 0, 12));

        txtCorTipoProdutoUsina.setEditable(false);
        txtCorTipoProdutoUsina.setFont(new java.awt.Font("Tahoma", 0, 12));

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel17.setText(PropertiesUtil.getMessage("label.pellet.feed"));

        txtTipoProdutoPalletUsina.setEditable(false);
        txtTipoProdutoPalletUsina.setFont(new java.awt.Font("Tahoma", 0, 12));

        txtCorTipoProdutoPalletUsina.setEditable(false);
        txtCorTipoProdutoPalletUsina.setFont(new java.awt.Font("Tahoma", 0, 12));

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel18.setText(PropertiesUtil.getMessage("label.screening"));

        txtTipoProdutoScreeningUsina.setEditable(false);
        txtTipoProdutoScreeningUsina.setFont(new java.awt.Font("Tahoma", 0, 12));

        txtCorTipoProdutoScreeningUsina.setEditable(false);
        txtCorTipoProdutoScreeningUsina.setFont(new java.awt.Font("Tahoma", 0, 12));

        javax.swing.GroupLayout pnlDetalhesUsinaLayout = new javax.swing.GroupLayout(pnlDetalhesUsina);
        pnlDetalhesUsina.setLayout(pnlDetalhesUsinaLayout);
        pnlDetalhesUsinaLayout
                        .setHorizontalGroup(pnlDetalhesUsinaLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(
                                                        pnlDetalhesUsinaLayout
                                                                        .createSequentialGroup()
                                                                        .addContainerGap()
                                                                        .addGroup(
                                                                                        pnlDetalhesUsinaLayout
                                                                                                        .createParallelGroup(
                                                                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                        .addGroup(
                                                                                                                        pnlDetalhesUsinaLayout
                                                                                                                                        .createSequentialGroup()
                                                                                                                                        .addComponent(
                                                                                                                                                        jLabel10)
                                                                                                                                        .addPreferredGap(
                                                                                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                        .addComponent(
                                                                                                                                                        txtNomeUsina,
                                                                                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                                        324,
                                                                                                                                                        Short.MAX_VALUE))
                                                                                                        .addGroup(
                                                                                                                        pnlDetalhesUsinaLayout
                                                                                                                                        .createSequentialGroup()
                                                                                                                                        .addComponent(
                                                                                                                                                        jLabel11)
                                                                                                                                        .addPreferredGap(
                                                                                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                        .addComponent(
                                                                                                                                                        txtTaxaOperacaoUsina,
                                                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                        69,
                                                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                        .addPreferredGap(
                                                                                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                        .addComponent(
                                                                                                                                                        jLabel12))
                                                                                                        .addGroup(
                                                                                                                        pnlDetalhesUsinaLayout
                                                                                                                                        .createSequentialGroup()
                                                                                                                                        .addComponent(
                                                                                                                                                        jLabel13)
                                                                                                                                        .addPreferredGap(
                                                                                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                        .addComponent(
                                                                                                                                                        txtNomeCampanhaUsina,
                                                                                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                                        294,
                                                                                                                                                        Short.MAX_VALUE))
                                                                                                        .addGroup(
                                                                                                                        javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                                        pnlDetalhesUsinaLayout
                                                                                                                                        .createSequentialGroup()
                                                                                                                                        .addComponent(
                                                                                                                                                        jLabel16)
                                                                                                                                        .addPreferredGap(
                                                                                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                        .addComponent(
                                                                                                                                                        txtTipoProdutoUsina,
                                                                                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                                        52,
                                                                                                                                                        Short.MAX_VALUE)
                                                                                                                                        .addPreferredGap(
                                                                                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                        .addComponent(
                                                                                                                                                        txtCorTipoProdutoUsina,
                                                                                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                                        61,
                                                                                                                                                        Short.MAX_VALUE)
                                                                                                                                        .addPreferredGap(
                                                                                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                        .addComponent(
                                                                                                                                                        jLabel17)
                                                                                                                                        .addPreferredGap(
                                                                                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                        .addComponent(
                                                                                                                                                        txtTipoProdutoPalletUsina,
                                                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                        90,
                                                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                        .addPreferredGap(
                                                                                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                        .addComponent(
                                                                                                                                                        txtCorTipoProdutoPalletUsina,
                                                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                        21,
                                                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                        .addGroup(
                                                                                                                        pnlDetalhesUsinaLayout
                                                                                                                                        .createSequentialGroup()
                                                                                                                                        .addComponent(
                                                                                                                                                        jLabel18)
                                                                                                                                        .addPreferredGap(
                                                                                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                        .addComponent(
                                                                                                                                                        txtTipoProdutoScreeningUsina,
                                                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                        90,
                                                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                        .addPreferredGap(
                                                                                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                        .addComponent(
                                                                                                                                                        txtCorTipoProdutoScreeningUsina,
                                                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                        21,
                                                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                        .addGroup(
                                                                                                                        pnlDetalhesUsinaLayout
                                                                                                                                        .createSequentialGroup()
                                                                                                                                        .addComponent(
                                                                                                                                                        jLabel14)
                                                                                                                                        .addPreferredGap(
                                                                                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                        .addComponent(
                                                                                                                                                        txtDataInicialCampanha,
                                                                                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                                        134,
                                                                                                                                                        Short.MAX_VALUE)
                                                                                                                                        .addPreferredGap(
                                                                                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                        .addComponent(
                                                                                                                                                        jLabel15)
                                                                                                                                        .addPreferredGap(
                                                                                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                        .addComponent(
                                                                                                                                                        txtDataFinalCampanha,
                                                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                        142,
                                                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                        .addContainerGap()));
        pnlDetalhesUsinaLayout
                        .setVerticalGroup(pnlDetalhesUsinaLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(
                                                        pnlDetalhesUsinaLayout
                                                                        .createSequentialGroup()
                                                                        .addGroup(
                                                                                        pnlDetalhesUsinaLayout
                                                                                                        .createParallelGroup(
                                                                                                                        javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                        .addComponent(
                                                                                                                        jLabel10)
                                                                                                        .addComponent(
                                                                                                                        txtNomeUsina,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                        .addPreferredGap(
                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addGroup(
                                                                                        pnlDetalhesUsinaLayout
                                                                                                        .createParallelGroup(
                                                                                                                        javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                        .addComponent(
                                                                                                                        jLabel11)
                                                                                                        .addComponent(
                                                                                                                        txtTaxaOperacaoUsina,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                        .addComponent(
                                                                                                                        jLabel12))
                                                                        .addPreferredGap(
                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addGroup(
                                                                                        pnlDetalhesUsinaLayout
                                                                                                        .createParallelGroup(
                                                                                                                        javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                        .addComponent(
                                                                                                                        jLabel13)
                                                                                                        .addComponent(
                                                                                                                        txtNomeCampanhaUsina,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                        .addPreferredGap(
                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addGroup(
                                                                                        pnlDetalhesUsinaLayout
                                                                                                        .createParallelGroup(
                                                                                                                        javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                        .addComponent(
                                                                                                                        jLabel14)
                                                                                                        .addComponent(
                                                                                                                        txtDataInicialCampanha,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                        .addComponent(
                                                                                                                        jLabel15)
                                                                                                        .addComponent(
                                                                                                                        txtDataFinalCampanha,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                        .addPreferredGap(
                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addGroup(
                                                                                        pnlDetalhesUsinaLayout
                                                                                                        .createParallelGroup(
                                                                                                                        javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                        .addComponent(
                                                                                                                        jLabel16)
                                                                                                        .addComponent(
                                                                                                                        txtTipoProdutoUsina,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                        .addComponent(
                                                                                                                        jLabel17)
                                                                                                        .addComponent(
                                                                                                                        txtTipoProdutoPalletUsina,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                        .addComponent(
                                                                                                                        txtCorTipoProdutoPalletUsina,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                        .addComponent(
                                                                                                                        txtCorTipoProdutoUsina,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                        .addPreferredGap(
                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addGroup(
                                                                                        pnlDetalhesUsinaLayout
                                                                                                        .createParallelGroup(
                                                                                                                        javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                        .addComponent(
                                                                                                                        jLabel18)
                                                                                                        .addComponent(
                                                                                                                        txtTipoProdutoScreeningUsina,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                        .addComponent(
                                                                                                                        txtCorTipoProdutoScreeningUsina,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                        .addContainerGap(22, Short.MAX_VALUE)));

        javax.swing.GroupLayout pnlInterfaceRastreabilidadeLayout = new javax.swing.GroupLayout(pnlInterfaceRastreabilidade);
        pnlInterfaceRastreabilidade.setLayout(pnlInterfaceRastreabilidadeLayout);
        pnlInterfaceRastreabilidadeLayout
                        .setHorizontalGroup(pnlInterfaceRastreabilidadeLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(
                                                        pnlInterfaceRastreabilidadeLayout
                                                                        .createSequentialGroup()
                                                                        .addContainerGap()
                                                                        .addGroup(
                                                                                        pnlInterfaceRastreabilidadeLayout
                                                                                                        .createParallelGroup(
                                                                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                        .addComponent(
                                                                                                                        pnlTabelaRastreabilidade,
                                                                                                                        javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                        911,
                                                                                                                        Short.MAX_VALUE)
                                                                                                        .addGroup(
                                                                                                                        pnlInterfaceRastreabilidadeLayout
                                                                                                                                        .createSequentialGroup()
                                                                                                                                        .addComponent(
                                                                                                                                                        pnlDetalhesBaliza,
                                                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                        .addPreferredGap(
                                                                                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                        .addComponent(
                                                                                                                                                        pnlDetalhesUsina,
                                                                                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                                        Short.MAX_VALUE))
                                                                                                        .addComponent(
                                                                                                                        cmdFechar,
                                                                                                                        javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                        107,
                                                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                        .addContainerGap()));
        pnlInterfaceRastreabilidadeLayout.setVerticalGroup(pnlInterfaceRastreabilidadeLayout.createParallelGroup(
                        javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                        pnlInterfaceRastreabilidadeLayout.createSequentialGroup().addContainerGap().addComponent(
                                        pnlTabelaRastreabilidade, javax.swing.GroupLayout.PREFERRED_SIZE, 222,
                                        javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(
                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(
                                        pnlInterfaceRastreabilidadeLayout.createParallelGroup(
                                                        javax.swing.GroupLayout.Alignment.LEADING).addComponent(
                                                        pnlDetalhesUsina, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(
                                                        pnlDetalhesBaliza, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(
                                                        cmdFechar, javax.swing.GroupLayout.PREFERRED_SIZE, 35,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));

        getContentPane().add(pnlInterfaceRastreabilidade, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFecharActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_cmdFecharActionPerformed

    private void tblRastreabilidadeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblRastreabilidadeMouseClicked
        try {
            CFlexStockyardFuncoesTabela.validaSelecaoTabela(tblRastreabilidade);
            int linhaSelecionada = tblRastreabilidade.getSelectedRow();
            tblRastreabilidade.setRowSelectionInterval(linhaSelecionada, linhaSelecionada);
            rastreabilidadeSelecionada = listaRastreabilidade.get(linhaSelecionada);
            inicializaDetalhesRastreabilidade();
            exibeDetalhesDaRastreabilidade();
        } catch (ValidacaoCampoException vCampoEx) {
            controladorInterfaceInicial.desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(vCampoEx.getMessage());
            controladorInterfaceInicial.ativarMensagem(interfaceMensagem);
        }

    }//GEN-LAST:event_tblRastreabilidadeMouseClicked

    /**
     * Metodo que monta a tabela de informacoes com as rastreabilidades
     * contidas no objeto
     */
    private void atualizaTabelaInformacoesRastreabilidade() throws ErroSistemicoException {

        listaInformacoesTabelaRastreabilidade.removeAllElements();
        Collections.sort(listaRastreabilidade,Rastreabilidade.comparadorStatusRastreabilidade);
        for (Rastreabilidade interfaceRastreabilidade : listaRastreabilidade) {
            Object[] dados = new Object[8];

            dados[COL_BALIZA] = interfaceRastreabilidade.getNomeBaliza();
            dados[COL_PATIO_BALIZA] = interfaceRastreabilidade.getNomePatioBaliza();
            dados[COL_USINA] = interfaceRastreabilidade.getNomeUsina();
            dados[COL_NOME_PORAO] = interfaceRastreabilidade.getNomePorao();
            dados[COL_QUANTIDADE] = DSSStockyardFuncoesNumeros.getQtdeFormatada(interfaceRastreabilidade.getQuantidade(), 3);
            dados[COL_TIPO_OPERACAO] = interfaceRastreabilidade.getTipoAtividade().toString();
            dados[COL_HORARIO_INICIO] = DSSStockyardTimeUtil
                            .formatarData(interfaceRastreabilidade.getHorarioInicioEntradaDeMaterial(), PropertiesUtil
                                            .buscarPropriedade("formato.campo.datahora"));
            dados[COL_HORARIO_FIM] = DSSStockyardTimeUtil.formatarData(interfaceRastreabilidade
                            .getHorarioFimEntradaDeMaterial(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));

            listaInformacoesTabelaRastreabilidade.add(new Vector(Arrays.asList(dados)));
        }

        CFlexStockyardFuncoesTabela.setInformacoesTabela(tblRastreabilidade, listaInformacoesTabelaRastreabilidade,
                        listaColunas);
    }

    /**
     * Metodo que inicializa os detalhes dos paineis contendo a rastreabilidade
     * do objeto
     */
    private void inicializaDetalhesRastreabilidade() {
        txtCapacidadeMaxima.setText("");
        txtCorTipoProdutoPalletUsina.setText("");
        txtCorTipoProdutoPalletUsina.setBackground(txtCorTipoProdutoPalletUsina.getDisabledTextColor());
        txtCorTipoProdutoUsina.setText("");
        txtCorTipoProdutoUsina.setBackground(txtCorTipoProdutoUsina.getDisabledTextColor());
        txtCorTipoProdutoScreeningUsina.setText("");
        txtCorTipoProdutoScreeningUsina.setBackground(txtCorTipoProdutoScreeningUsina.getDisabledTextColor());
        txtDataFinalCampanha.setText("");
        txtDataInicialCampanha.setText("");
        txtHorarioFimFormacao.setText("");
        txtHorarioInicioFormacao.setText("");
        txtLarguraBaliza.setText("");
        txtNomeBaliza.setText("");
        txtNomeCampanhaUsina.setText("");
        txtNomePilha.setText("");
        txtNomeUsina.setText("");
        txtNumeroBaliza.setText("");
        txtTaxaOperacaoUsina.setText("");
        txtTipoProdutoPalletUsina.setText("");
        txtTipoProdutoScreeningUsina.setText("");
        txtTipoProdutoUsina.setText("");
        txtNomePatioBaliza.setText("");
        txtNumeroPatioBaliza.setText("");
    }

    /**
     * Metodo que exibe os detalhes da rastreabilidade selecionada na
     * tabela
     */
    private void exibeDetalhesDaRastreabilidade() {

        if (rastreabilidadeSelecionada.getNomeBaliza() != null) {

            //Baliza baliza = rastreabilidadeSelecionada.getBalizaDeOrigem();

            txtNumeroBaliza.setText(DSSStockyardFuncoesTexto.getCodigoFormatado(3, rastreabilidadeSelecionada
                            .getNumeroBaliza()));
            txtNomeBaliza.setText(rastreabilidadeSelecionada.getNomeBaliza());
            txtLarguraBaliza.setText(DSSStockyardFuncoesNumeros.getQtdeFormatada(rastreabilidadeSelecionada
                            .getLarguraBaliza(), 2));
            txtCapacidadeMaxima.setText(DSSStockyardFuncoesNumeros.getQtdeFormatada(rastreabilidadeSelecionada
                            .getCapacMaxBaliza(), 3));
            txtHorarioInicioFormacao.setText(DSSStockyardTimeUtil.formatarData(rastreabilidadeSelecionada
                            .getDtHoraIniFormBaliza(), PropertiesUtil.buscarPropriedade("formato.campo.datahora")));
            txtHorarioFimFormacao.setText(DSSStockyardTimeUtil.formatarData(rastreabilidadeSelecionada
                            .getDtHoraFimFormBaliza(), PropertiesUtil.buscarPropriedade("formato.campo.datahora")));
            //if (baliza.getPilha() != null) {
            txtNomePilha.setText(rastreabilidadeSelecionada.getNomePilhaBaliza());
            //}
            txtNomePatioBaliza.setText(rastreabilidadeSelecionada.getNomePatioBaliza());
            txtNumeroPatioBaliza.setText(DSSStockyardFuncoesTexto.getCodigoFormatado(3, rastreabilidadeSelecionada
                            .getNumPatioBaliza()));
        }

        if (rastreabilidadeSelecionada.getNomeUsina() != null) {

            txtNomeUsina.setText(rastreabilidadeSelecionada.getNomeUsina());
            txtTaxaOperacaoUsina.setText(DSSStockyardFuncoesNumeros.getQtdeFormatada((rastreabilidadeSelecionada
                            .getTaxaDeOperacaoUsina() * 24), 3));
            txtNomeCampanhaUsina.setText(rastreabilidadeSelecionada.getNomeCampanhaUsina());
            txtDataInicialCampanha.setText(DSSStockyardTimeUtil.formatarData(
                            rastreabilidadeSelecionada.getDataInicioUsina(), PropertiesUtil
                                            .buscarPropriedade("formato.campo.datahora")));
            txtDataFinalCampanha.setText(DSSStockyardTimeUtil.formatarData(rastreabilidadeSelecionada.getDataFimUsina(),
                            PropertiesUtil.buscarPropriedade("formato.campo.datahora")));
            txtTipoProdutoUsina.setText(rastreabilidadeSelecionada.getTipoProdutoUsina().toString());
            txtCorTipoProdutoUsina.setBackground(criaCorTipoProduto(rastreabilidadeSelecionada.getTipoProdutoUsina()));
            txtTipoProdutoPalletUsina.setText(rastreabilidadeSelecionada.getTipoPelletUsina().toString());
            txtCorTipoProdutoPalletUsina.setBackground(criaCorTipoProduto(rastreabilidadeSelecionada.getTipoPelletUsina()));
            txtTipoProdutoScreeningUsina.setText(rastreabilidadeSelecionada.getTipoScreeningUsina().toString());
            txtCorTipoProdutoScreeningUsina.setBackground(criaCorTipoProduto(rastreabilidadeSelecionada
                            .getTipoScreeningUsina()));
            //Usina usina = rastreabilidadeSelecionada.getUsinaDeOrigem();

            txtNomeUsina.setText(rastreabilidadeSelecionada.getNomeUsina());
            txtTaxaOperacaoUsina.setText(DSSStockyardFuncoesNumeros.getQtdeFormatada((rastreabilidadeSelecionada
                            .getTaxaDeOperacaoUsina() * 24), 3));
            txtNomeCampanhaUsina.setText(rastreabilidadeSelecionada.getNomeCampanhaUsina());
            txtDataInicialCampanha.setText(DSSStockyardTimeUtil.formatarData(
                            rastreabilidadeSelecionada.getDataInicioUsina(), PropertiesUtil
                                            .buscarPropriedade("formato.campo.datahora")));
            txtDataFinalCampanha.setText(DSSStockyardTimeUtil.formatarData(rastreabilidadeSelecionada.getDataFimUsina(),
                            PropertiesUtil.buscarPropriedade("formato.campo.datahora")));
            txtTipoProdutoUsina.setText(rastreabilidadeSelecionada.getTipoProdutoUsina().toString());
            txtCorTipoProdutoUsina.setBackground(criaCorTipoProduto(rastreabilidadeSelecionada.getTipoProdutoUsina()));
            txtTipoProdutoPalletUsina.setText(rastreabilidadeSelecionada.getTipoPelletUsina().toString());
            txtCorTipoProdutoPalletUsina.setBackground(criaCorTipoProduto(rastreabilidadeSelecionada.getTipoPelletUsina()));
            txtTipoProdutoScreeningUsina.setText(rastreabilidadeSelecionada.getTipoScreeningUsina().toString());
            txtCorTipoProdutoScreeningUsina.setBackground(criaCorTipoProduto(rastreabilidadeSelecionada
                            .getTipoScreeningUsina()));
        }
    }

    /**
     * Cria um objeto Color com a cor de um tipo de produto especifico
     * @param tipoProduto
     * @return
     */
    private Color criaCorTipoProduto(TipoProduto tipoProduto) {
        String[] rgb = tipoProduto.getCorIdentificacao().split(",");
        Color corTipoProduto = new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
        return corTipoProduto;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdFechar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel pnlDetalhesBaliza;
    private javax.swing.JPanel pnlDetalhesUsina;
    private javax.swing.JPanel pnlInterfaceRastreabilidade;
    private javax.swing.JPanel pnlTabelaRastreabilidade;
    private javax.swing.JScrollPane scrTabelaRastreabilidade;
    private com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable tblRastreabilidade;
    private javax.swing.JTextField txtCapacidadeMaxima;
    private javax.swing.JTextField txtCorTipoProdutoPalletUsina;
    private javax.swing.JTextField txtCorTipoProdutoScreeningUsina;
    private javax.swing.JTextField txtCorTipoProdutoUsina;
    private javax.swing.JTextField txtDataFinalCampanha;
    private javax.swing.JTextField txtDataInicialCampanha;
    private javax.swing.JTextField txtHorarioFimFormacao;
    private javax.swing.JTextField txtHorarioInicioFormacao;
    private javax.swing.JTextField txtLarguraBaliza;
    private javax.swing.JTextField txtNomeBaliza;
    private javax.swing.JTextField txtNomeCampanhaUsina;
    private javax.swing.JTextField txtNomePatioBaliza;
    private javax.swing.JTextField txtNomePilha;
    private javax.swing.JTextField txtNomeUsina;
    private javax.swing.JTextField txtNumeroBaliza;
    private javax.swing.JTextField txtNumeroPatioBaliza;
    private javax.swing.JTextField txtTaxaOperacaoUsina;
    private javax.swing.JTextField txtTipoProdutoPalletUsina;
    private javax.swing.JTextField txtTipoProdutoScreeningUsina;
    private javax.swing.JTextField txtTipoProdutoUsina;

    // End of variables declaration//GEN-END:variables

    public IControladorInterfaceInicial getControladorInterfaceInicial() {
        return controladorInterfaceInicial;
    }

    public void setControladorInterfaceInicial(IControladorInterfaceInicial controladorInterfaceInicial) {
        this.controladorInterfaceInicial = controladorInterfaceInicial;
    }

}
