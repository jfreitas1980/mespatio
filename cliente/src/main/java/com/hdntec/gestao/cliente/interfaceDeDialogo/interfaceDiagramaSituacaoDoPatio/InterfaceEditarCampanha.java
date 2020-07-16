package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio;


import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.SwingConstants;

import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.cliente.util.numeros.DSSStockyardFuncoesNumeros;
import com.hdntec.gestao.cliente.util.tabela.CFlexStockyardFuncoesTabela;
import com.hdntec.gestao.cliente.util.tabela.ColunaTabela;
import com.hdntec.gestao.cliente.util.telas.DSSStockyardTelaUtil;
import com.hdntec.gestao.domain.planta.entity.status.Campanha;
import com.hdntec.gestao.domain.planta.entity.status.Usina;
import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;
import com.hdntec.gestao.domain.produto.entity.ItemDeControle;
import com.hdntec.gestao.domain.produto.entity.ItemDeControleQualidade;
import com.hdntec.gestao.domain.produto.entity.Qualidade;
import com.hdntec.gestao.domain.produto.entity.TipoItemDeControle;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.domain.produto.enums.TipoDeProdutoEnum;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.ValidacaoCampoException;
import com.hdntec.gestao.services.controladores.impl.ControladorUsinas;
import com.hdntec.gestao.util.PropertiesUtil;
import com.hdntec.gestao.util.datahora.DSSStockyardTimeUtil;

/**
 *
 * @author Ricardo Trabalho
 */
public abstract class InterfaceEditarCampanha extends javax.swing.JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7887134367650756388L;
	
	private InterfaceMensagem interfaceMensagem;
    private Boolean operacaoCanceladaPeloUsuario;
    private InterfaceUsina interfaceUsina;
    private Usina usina;
    private List<TipoProduto> listaTiposProduto;
    private List<ColunaTabela> listaColunas;
    private Campanha campanha;

    //Dados Editaveis
    private String nomeCampanha;
    private Date dataInicial;
    private Date dataFinal;
    private TipoProduto tipoProduto;
    private TipoProduto tipoProdutoPelletFeed;
    private TipoProduto tipoProdutoScreening;
    private Long codigoFaseProcessoPelletFeed = new Long(0);
    private Long codigoFaseProcessoPelletFeedQAQ = new Long(0);
    private Long codigoFaseProcessoPelletFeedQAF = new Long(0);
    private String codigoAreaPelletFeed = new String();
    private Long codigoTipoItemPelletFeed = new Long(0);
    private String codigoTipoProcessoPelletFeed = new String();
    private String codigoAreaPelota = new String();
    private Long codigoFaseProcessoPelota = new Long(0);
    private Long codigoTipoItemPelota = new Long(0);
    private String codigoTipoProcessoPelota=new String();
    private Boolean finalizada = Boolean.FALSE;
    
    /** usado para mapear itemDeControle e o valorGarantidoMinimo, futuramente usar um valor com Devio padrão e propagação de erro */
    private Map<TipoItemDeControle, Double> listaValor;
    private List<TipoItemDeControle> listaTiposItemDeControle;
    
    Qualidade qualidadeCampanhaClonada = null;

    public enum ACAO {

        CRIACAO,
        EDICAO
    }
    private ACAO acao;

    /** Creates new form InterfaceEditarCampanha */
    public InterfaceEditarCampanha(InterfaceUsina interfaceUsina, List<TipoProduto> listaTiposProduto, List<TipoItemDeControle> listaTiposItemDeControle, ACAO acao, Campanha campanha) {
        initComponents();        
        this.interfaceUsina = interfaceUsina;
        this.usina = this.interfaceUsina.getUsinaVisualizada();
        this.listaTiposProduto = listaTiposProduto;
        this.listaTiposItemDeControle = listaTiposItemDeControle;
        carregaComboTiposDeProduto();
        carregaComboTiposDeProdutoScreening();
        this.acao = acao;
        if (this.acao.equals(ACAO.CRIACAO)) {
            this.qualidadeCampanhaClonada = new Qualidade();
            this.qualidadeCampanhaClonada.setDtInicio(new Date(System.currentTimeMillis()));
            this.qualidadeCampanhaClonada.setDtInsert(new Date(System.currentTimeMillis()));
            this.qualidadeCampanhaClonada.setListaDeAmostras(null);
            this.qualidadeCampanhaClonada.setEhReal(false);

            this.campanha = new Campanha();
            this.campanha.setCaptacaoTemporaria(new Double(0));
            this.campanha.setCodigoFaseProcessoPelletFeed(new Long(420030020L));
            this.campanha.setCodigoFaseProcessoPelletFeedQAQ(new Long(420030010L));
            this.campanha.setCodigoFaseProcessoPelletFeedQAF(new Long(420060000L));
            this.campanha.setCodigoFaseProcessoPelota(new Long(480050020L));
            this.campanha.setFinalizada(Boolean.FALSE);
            
            this.campanha.setAreaRespEDPelletFeed("CC");
            this.campanha.setAreaRespEDPelota("CC");
            this.campanha.setCdTipoItemControlePelletFeed(new Long(150L));
            this.campanha.setCdTipoItemControlePelota(new Long(150L));
            
            this.campanha.setTipoProcessoPelletFeed("DD");
            this.campanha.setTipoProcessoPelota("HH");
            
            this.campanha.setQuantidadePrevista(0D);
            this.campanha.setQuantidadeTemporaria(0D);            
            
            
            Collections.sort(this.usina.getMetaUsina().getListaCampanhas(),Campanha.comparadorCampanha);
            Campanha campAtual = null;
            if (this.usina.getMetaUsina().getListaCampanhas() != null && !this.usina.getMetaUsina().getListaCampanhas().isEmpty()) {
                campAtual = this.usina.getMetaUsina().getListaCampanhas().get(this.usina.getMetaUsina().getListaCampanhas().size() -1);
            }    
            if ( campAtual != null) {
                this.campanha.setDataInicial(new Date(campAtual.getDataFinal().getTime()));
                this.campanha.setDataFinal(new Date(campAtual.getDataFinal().getTime()));
            } else {
                this.campanha.setDataInicial(new Date());
                this.campanha.setDataFinal(new Date());
            }
            this.campanha.setNomeCampanha(PropertiesUtil.getMessage("mensagem.campanha.anonima"));
            this.campanha.setQualidadeEstimada(this.qualidadeCampanhaClonada);

            //carrega campos editaveis.
            this.nomeCampanha = this.campanha.getNomeCampanha();
            this.dataInicial = this.campanha.getDataInicial();
            this.dataFinal = this.campanha.getDataFinal();
            this.tipoProduto = this.campanha.getTipoProduto();
            this.tipoProdutoPelletFeed = this.campanha.getTipoPellet();
            this.tipoProdutoScreening = this.campanha.getTipoScreening();

            this.codigoFaseProcessoPelletFeed = this.campanha.getCodigoFaseProcessoPelletFeed();
            this.codigoFaseProcessoPelletFeedQAQ = this.campanha.getCodigoFaseProcessoPelletFeedQAQ();
            this.codigoFaseProcessoPelletFeedQAF = this.campanha.getCodigoFaseProcessoPelletFeedQAF();

            this.codigoAreaPelletFeed = this.campanha.getAreaRespEDPelletFeed();
            this.codigoTipoItemPelletFeed = this.campanha.getCdTipoItemControlePelletFeed();
            this.codigoTipoProcessoPelletFeed = this.campanha.getTipoProcessoPelletFeed();

            this.codigoFaseProcessoPelota = this.campanha.getCodigoFaseProcessoPelota();
            this.codigoAreaPelota = this.campanha.getAreaRespEDPelota();
            this.codigoTipoItemPelota = this.campanha.getCdTipoItemControlePelota();
            this.codigoTipoProcessoPelota = this.campanha.getTipoProcessoPelota();


            this.listaValor = new HashMap<TipoItemDeControle, Double>();
            List<ItemDeControle> listaItensDeControle = new ArrayList<ItemDeControle>();
            for (TipoItemDeControle tipoItemDeControle : this.listaTiposItemDeControle) {
                /*JESSÉ 18/6*/
            	 //ItemDeControle itemDeControle = new ItemDeControle();
            	 ItemDeControle itemDeControle = new ItemDeControleQualidade();
            	 itemDeControle.setDtInicio(new Date(System.currentTimeMillis()));
            	 itemDeControle.setDtInsert(new Date(System.currentTimeMillis()));
                itemDeControle.setTipoItemControle(tipoItemDeControle);
                itemDeControle.setValor(0.00);
//                itemDeControle.setDesvioPadraoValor(0.00);
                listaItensDeControle.add(itemDeControle);
                listaValor.put(itemDeControle.getTipoItemControle(), itemDeControle.getValor());
            }
            this.qualidadeCampanhaClonada.setListaDeItensDeControle(listaItensDeControle);
        } else {
            if (campanha == null) {  
                this.campanha = this.usina.getMetaUsina().getCampanhaAtual(interfaceUsina.getHoraSituacao());
            } else {
                this.campanha = campanha;
            }

            
            //carrega campos editaveis.
            this.nomeCampanha = this.campanha.getNomeCampanha();
            this.dataInicial = this.campanha.getDataInicial();
            this.dataFinal = this.campanha.getDataFinal();
            this.tipoProduto = this.campanha.getTipoProduto();
            this.tipoProdutoPelletFeed = this.campanha.getTipoPellet();
            this.tipoProdutoScreening = this.campanha.getTipoScreening();
            this.codigoFaseProcessoPelletFeed = this.campanha.getCodigoFaseProcessoPelletFeed();
            this.codigoFaseProcessoPelota = this.campanha.getCodigoFaseProcessoPelota();
            this.listaValor = new HashMap<TipoItemDeControle, Double>();
            this.qualidadeCampanhaClonada = this.campanha.getQualidadeEstimada();
            for (ItemDeControle itemDeControle : this.qualidadeCampanhaClonada.getListaDeItensDeControle()) {
                listaValor.put(itemDeControle.getTipoItemControle(), itemDeControle.getValor());
            }
            this.codigoFaseProcessoPelletFeed = this.campanha.getCodigoFaseProcessoPelletFeed();
            this.codigoFaseProcessoPelletFeedQAQ = this.campanha.getCodigoFaseProcessoPelletFeedQAQ();
            this.codigoFaseProcessoPelletFeedQAF = this.campanha.getCodigoFaseProcessoPelletFeedQAF();

            this.codigoAreaPelletFeed = this.campanha.getAreaRespEDPelletFeed();
            this.codigoTipoItemPelletFeed = this.campanha.getCdTipoItemControlePelletFeed();
            this.codigoTipoProcessoPelletFeed = this.campanha.getTipoProcessoPelletFeed();

            this.codigoFaseProcessoPelota = this.campanha.getCodigoFaseProcessoPelota();
            this.codigoAreaPelota = this.campanha.getAreaRespEDPelota();
            this.codigoTipoItemPelota = this.campanha.getCdTipoItemControlePelota();
            this.codigoTipoProcessoPelota = this.campanha.getTipoProcessoPelota();
            this.finalizada = this.campanha.getFinalizada();         

        }
        this.criaColunas();
        this.atualizaCampos();
        
        
        

    }

    /**
     * Carrega no campo tipo de produto todos os tipos de produto cadastrado no sistema
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     */
    private void carregaComboTiposDeProduto() {
        cmbTipoProduto.removeAllItems();

        TipoProduto tipoProdutoVazio = new TipoProduto();
        tipoProdutoVazio.setCodigoFamiliaTipoProduto("");
        tipoProdutoVazio.setCodigoInsumoTipoProduto(null);
        tipoProdutoVazio.setCodigoTipoProduto("");
        tipoProdutoVazio.setCorIdentificacao("236,233,216");
        tipoProdutoVazio.setIdTipoProduto(new Long(0));
        cmbTipoProduto.addItem(tipoProdutoVazio);

        for (TipoProduto tipoProdutoDaLista : listaTiposProduto) {
            if (tipoProdutoDaLista.getTipoDeProduto().equals(TipoDeProdutoEnum.PELOTA)) {
                cmbTipoProduto.addItem(tipoProdutoDaLista);
            }
        }
    }

    /**
     * Carrega no campo tipo de pellet feed todos os tipos de produto pellet feed cadastrado no sistema
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     */
    private void carregaComboTiposDeProdutoPellet(TipoProduto tipoProdutoProduzido) {
        cmbTipoProdutoPallet.removeAllItems();
        for (TipoProduto tipoProdutoDaLista : listaTiposProduto) {
            if (tipoProdutoProduzido.getCodigoInsumoTipoProduto() != null) {
                if (tipoProdutoProduzido.getCodigoInsumoTipoProduto().getIdTipoProduto().equals(tipoProdutoDaLista.getIdTipoProduto())) {
                    cmbTipoProdutoPallet.addItem(tipoProdutoDaLista);
                }
            }
        }
    }

    /**
     * Carrega no campo tipo de pellet feed todos os tipos de produto pellet feed cadastrado no sistema
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     */
    private void carregaComboTiposDeProdutoScreening() {
        cmbTipoProdutoScreening.removeAllItems();
        for (TipoProduto tipoProdutoDaLista : listaTiposProduto) {
            if (tipoProdutoDaLista.getTipoDeProduto().equals(TipoDeProdutoEnum.PELLET_SCREENING)) {
                cmbTipoProdutoScreening.addItem(tipoProdutoDaLista);
            }
        }
     //   cmbTipoProdutoScreening.setSelectedIndex(0);
    }

    private void criaColunas() {
        listaColunas = new ArrayList<ColunaTabela>();
        ColunaTabela coluna;

        //Coluna descricao de tipo de item de controle.
        coluna = new ColunaTabela();
        coluna.setLargura(200);
        coluna.setTitulo(PropertiesUtil.getMessage("mensagem.coluna.tabela.tipo.item.controle"));
        coluna.setAlinhamento(SwingConstants.LEFT);
        coluna.setRedimensionar(true);
        coluna.setEditar(false);
        listaColunas.add(coluna);

        //Coluna valor item de controle.
        coluna = new ColunaTabela();
        coluna.setLargura(100);
        coluna.setTitulo(PropertiesUtil.getMessage("mensagem.coluna.tabela.valor"));
        coluna.setAlinhamento(SwingConstants.LEFT);
        coluna.setRedimensionar(true);
        coluna.setEditar(true);
        coluna.setTipoEditor(ColunaTabela.COL_TIPO_TEXT);
        listaColunas.add(coluna);

       /* //Coluna valor item de controle.
        coluna = new ColunaTabela();
        coluna.setLargura(120);
        coluna.setTitulo(PropertiesUtil.getMessage("mensagem.coluna.tabela.devio.padrao.valor"));
        coluna.setAlinhamento(SwingConstants.LEFT);
        coluna.setRedimensionar(true);
        coluna.setEditar(true);
        coluna.setTipoEditor(ColunaTabela.COL_TIPO_TEXT);
        listaColunas.add(coluna);*/

        //Coluna Unidade de medida do item de controle.
        coluna = new ColunaTabela();
        coluna.setLargura(50);
        coluna.setTitulo(PropertiesUtil.getMessage("mensagem.coluna.tabela.unidade"));
        coluna.setAlinhamento(SwingConstants.CENTER);
        coluna.setRedimensionar(true);
        coluna.setEditar(false);
        listaColunas.add(coluna);

    }

    private void atualizaCampos() {
        this.jLabelNomeUsina.setText(this.usina.getNomeUsina());
        this.cmbEstadoUsina.setSelectedItem(usina.getEstado());
        this.cmbFinaliza.setSelected(this.campanha.getFinalizada());
        // multiplica-se por 24, pois internamente usamos tonelada/hora e na tela usaremos tonelada/dia
        this.txtTaxaOperacaoMaxima.setText(DSSStockyardFuncoesNumeros.getQtdeFormatada(this.usina.getTaxaDeOperacao() * 24, 2));

        this.dtIniCalendarioHoraCFlex.setDataHora(this.dataInicial);
        this.dtFinCalendarioHoraCFlex.setDataHora(this.dataFinal);
        this.jTextFaseProcesso.setText(this.codigoFaseProcessoPelletFeed.toString());
        this.jTextFaseProcesso2.setText(this.codigoFaseProcessoPelletFeedQAQ.toString());
        this.jTextFaseProcesso3.setText(this.codigoFaseProcessoPelletFeedQAF.toString());

        this.jTextAreaPelletFeed.setText(this.codigoAreaPelletFeed.toString());
        this.jTextTipoItemPellet.setText(this.codigoTipoItemPelletFeed.toString());
        this.jTextTipoProcessoPellet.setText(this.codigoTipoProcessoPelletFeed.toString());

        //TODO
        this.jTextFaseProcesso1.setText(this.codigoFaseProcessoPelota.toString());
        this.jTextAreaPelota.setText(this.codigoAreaPelota.toString());
        this.jTextTipoItemPelota.setText(this.codigoTipoItemPelota.toString());
        this.jTextTipoProcessoPelota.setText(this.codigoTipoProcessoPelota.toString());

        this.jTextNomeCampanha.setText(this.nomeCampanha);
        this.cmbTipoProduto.setSelectedItem(this.campanha.getTipoProduto());
        this.cmbTipoProdutoPallet.setSelectedItem(this.campanha.getTipoPellet());
        this.cmbTipoProdutoScreening.setSelectedItem(this.campanha.getTipoScreening());
        this.cmbFinaliza.setSelected(this.campanha.getFinalizada());

        List<List> m_preDados = new ArrayList();
        for (ItemDeControle itemDeControle : qualidadeCampanhaClonada.getListaDeItensDeControle()) {
            List dados = new ArrayList();
            dados.add(itemDeControle.getTipoItemControle().getDescricaoTipoItemControle());
            if (itemDeControle.getValor() != null) {
                dados.add(itemDeControle.getValor());
            } else {
                dados.add(new Double(0.0));
            }
//            if (itemDeControle.getDesvioPadraoValor() != null) {
//                dados.add(itemDeControle.getDesvioPadraoValor());
//            } else {
//                dados.add(0.00);
//            }
            dados.add(itemDeControle.getTipoItemControle().getUnidade());
            m_preDados.add(new ArrayList(dados));
        }

        tblItensControle.setRowHeight(22);
        CFlexStockyardFuncoesTabela.setDadosTabela(this.tblItensControle, m_preDados, this.listaColunas);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
         
        cmbTipoProduto = new javax.swing.JComboBox();
        jLabelNomeUsina = new javax.swing.JLabel();
        jTextNomeCampanha = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblItensControle = new com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable();
        cmdDesistir = new javax.swing.JButton();
        jButtonConfirmar = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        cmbEstadoUsina = new javax.swing.JComboBox();
        txtTaxaOperacaoMaxima = new javax.swing.JTextField();
        lblCorTipoProduto = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        cmbTipoProdutoPallet = new javax.swing.JComboBox();
        lblCorTipoProdutoPallet = new javax.swing.JLabel();
        cmbTipoProdutoScreening = new javax.swing.JComboBox();
        lblCorTipoProdutoScreening = new javax.swing.JLabel();
        dtFinCalendarioHoraCFlex = new com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex();
        dtIniCalendarioHoraCFlex = new com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex();
        jPanel1 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jTextFaseProcesso = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jTextAreaPelletFeed = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jTextTipoItemPellet = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jTextTipoProcessoPellet = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jTextFaseProcesso2 = new javax.swing.JTextField();
        jTextFaseProcesso3 = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jTextFaseProcesso1 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jTextAreaPelota = new javax.swing.JTextField();
        jTextTipoItemPelota = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jTextTipoProcessoPelota = new javax.swing.JTextField();
        cmbFinaliza = new javax.swing.JCheckBox();

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel1.setText("Usina:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel2.setText("Estado da usina:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel3.setText("Taxa de operação maxima:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel4.setText("Campanha:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel5.setText("Data de início:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel8.setText("Tipo de produto:");

        cmbTipoProduto.setFont(new java.awt.Font("Tahoma", 0, 12));
        cmbTipoProduto.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTipoProdutoItemStateChanged(evt);
            }
        });

        jLabelNomeUsina.setFont(new java.awt.Font("Tahoma", 0, 12));
        jLabelNomeUsina.setForeground(java.awt.Color.blue);

        jTextNomeCampanha.setFont(new java.awt.Font("Tahoma", 0, 12));

        tblItensControle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblItensControle.setFont(new java.awt.Font("Tahoma", 0, 12));
        jScrollPane1.setViewportView(tblItensControle);
        StringBuffer value = new StringBuffer();
        value.append(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens")).append("/exit.png");
        
        cmdDesistir.setIcon(new javax.swing.ImageIcon(value.toString())); // NOI18N
        cmdDesistir.setText("Desistir");
        cmdDesistir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdDesistirActionPerformed(evt);
            }
        });

        value = new StringBuffer();
        value.append(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens")).append("/confirmar.png");
        
        jButtonConfirmar.setIcon(new javax.swing.ImageIcon(value.toString())); // NOI18N
        jButtonConfirmar.setText("Confirmar");
        jButtonConfirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConfirmarActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel9.setText("Data final:");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel10.setText("ton/dia");

        cmbEstadoUsina.setFont(new java.awt.Font("Tahoma", 0, 12));
        cmbEstadoUsina.setModel(new javax.swing.DefaultComboBoxModel(new Vector(Arrays.asList(EstadoMaquinaEnum.values()))));

        txtTaxaOperacaoMaxima.setFont(new java.awt.Font("Tahoma", 0, 12));

        lblCorTipoProduto.setFont(new java.awt.Font("Tahoma", 1, 12));
        lblCorTipoProduto.setOpaque(true);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel11.setText("Tipo de pellet feed:");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel12.setText("Tipo de screening:");

        cmbTipoProdutoPallet.setFont(new java.awt.Font("Tahoma", 0, 12));
        cmbTipoProdutoPallet.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTipoProdutoPalletItemStateChanged(evt);
            }
        });

        lblCorTipoProdutoPallet.setFont(new java.awt.Font("Tahoma", 1, 12));
        lblCorTipoProdutoPallet.setOpaque(true);

        cmbTipoProdutoScreening.setFont(new java.awt.Font("Tahoma", 0, 12));
        cmbTipoProdutoScreening.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTipoProdutoScreeningItemStateChanged(evt);
            }
        });

        lblCorTipoProdutoScreening.setFont(new java.awt.Font("Tahoma", 1, 12));
        lblCorTipoProdutoScreening.setOpaque(true);

        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel7.setText("Fase do processo pellet feed:");

        jTextFaseProcesso.setFont(new java.awt.Font("Tahoma", 0, 12));

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel14.setText("Área pellet feed:");

        jTextAreaPelletFeed.setFont(new java.awt.Font("Tahoma", 0, 12));
        jTextAreaPelletFeed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAreaPelletFeedActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel15.setText("Tipo Item  pellet feed:");

        jTextTipoItemPellet.setFont(new java.awt.Font("Tahoma", 0, 12));

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel19.setText("Tipo processo feed:");

        jTextTipoProcessoPellet.setFont(new java.awt.Font("Tahoma", 0, 12));

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel20.setText("FP Pellet Feed QA QUÍMICO:");

        jTextFaseProcesso2.setFont(new java.awt.Font("Tahoma", 0, 12));
        jTextFaseProcesso2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFaseProcesso2ActionPerformed(evt);
            }
        });

        jTextFaseProcesso3.setFont(new java.awt.Font("Tahoma", 0, 12));
        jTextFaseProcesso3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFaseProcesso3ActionPerformed(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel21.setText("FP Pellet Feed QA FÍSICO:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15)
                            .addComponent(jLabel19)
                            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFaseProcesso, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                            .addComponent(jTextTipoProcessoPellet, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                            .addComponent(jTextTipoItemPellet, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                            .addComponent(jTextFaseProcesso2, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                            .addComponent(jTextAreaPelletFeed, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE))
                        .addGap(12, 12, 12))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(28, 28, 28)
                        .addComponent(jTextFaseProcesso3, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jTextFaseProcesso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jTextAreaPelletFeed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(jTextTipoItemPellet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel19))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextTipoProcessoPellet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(jTextFaseProcesso2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(jTextFaseProcesso3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel13.setText("Fase do processo pelota:");

        jTextFaseProcesso1.setFont(new java.awt.Font("Tahoma", 0, 12));
        jTextFaseProcesso1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFaseProcesso1ActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel16.setText("Área  pelota:");

        jTextAreaPelota.setFont(new java.awt.Font("Tahoma", 0, 12));
        jTextAreaPelota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAreaPelotaActionPerformed(evt);
            }
        });

        jTextTipoItemPelota.setFont(new java.awt.Font("Tahoma", 0, 12));
        jTextTipoItemPelota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextTipoItemPelotaActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel17.setText("Tipo Item pelota:");

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel18.setText("Tipo processo pelota:");

        jTextTipoProcessoPelota.setFont(new java.awt.Font("Tahoma", 0, 12));
        jTextTipoProcessoPelota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextTipoProcessoPelotaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextTipoItemPelota, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextAreaPelota, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFaseProcesso1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(38, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextTipoProcessoPelota, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFaseProcesso1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jTextAreaPelota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextTipoItemPelota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextTipoProcessoPelota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18))
                .addContainerGap(65, Short.MAX_VALUE))
        );

        cmbFinaliza.setFont(new java.awt.Font("Tahoma", 1, 12));
        cmbFinaliza.setText("Finalizada");
        cmbFinaliza.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbFinalizaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 709, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(539, Short.MAX_VALUE)
                        .addComponent(cmdDesistir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelNomeUsina, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
                                .addGap(285, 285, 285))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel4)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTextNomeCampanha))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel3)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(txtTaxaOperacaoMaxima, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel5)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(dtIniCalendarioHoraCFlex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(15, 15, 15)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                            .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(dtFinCalendarioHoraCFlex, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                                .addComponent(jLabel8)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(cmbTipoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(lblCorTipoProduto, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE))
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(jLabel2)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(cmbEstadoUsina, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(18, 18, 18)
                                                .addComponent(cmbFinaliza))))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel11)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cmbTipoProdutoPallet, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblCorTipoProdutoPallet, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(12, 12, 12)
                                        .addComponent(jLabel12)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cmbTipoProdutoScreening, 0, 231, Short.MAX_VALUE)
                                        .addGap(1, 1, 1)
                                        .addComponent(lblCorTipoProdutoScreening, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(17, 17, 17)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelNomeUsina, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jTextNomeCampanha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbFinaliza)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbEstadoUsina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addComponent(dtIniCalendarioHoraCFlex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)
                            .addComponent(dtFinCalendarioHoraCFlex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtTaxaOperacaoMaxima, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10)
                            .addComponent(cmbTipoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel11)
                                .addComponent(cmbTipoProdutoPallet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblCorTipoProdutoPallet, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cmbTipoProdutoScreening, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel12))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(lblCorTipoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblCorTipoProdutoScreening, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonConfirmar)
                    .addComponent(cmdDesistir))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cmdDesistirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDesistirActionPerformed
        this.operacaoCanceladaPeloUsuario = true;
        fecharJanela();
        // obtendo o JDialog que adicionou o painel
        DSSStockyardTelaUtil.getParentDialog(this).setVisible(false);
}//GEN-LAST:event_cmdDesistirActionPerformed

    private void jButtonConfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConfirmarActionPerformed
        try {
            validaInformacoesEdicaoCampanha();
            this.setObjetosRetorno();
            if (this.acao.equals(ACAO.CRIACAO)) 
            {
                usina.getMetaUsina().addCampanha(this.campanha);    
            }
            ControladorUsinas.getInstance().salvarCampanha(this.campanha);
		
            this.operacaoCanceladaPeloUsuario = Boolean.FALSE;

            // obtendo o JDialog que adicionou o painel
            DSSStockyardTelaUtil.getParentDialog(this).setVisible(false);
        } catch (ErroSistemicoException ex) {
        	interfaceUsina.getControladorDSP().getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            interfaceUsina.getControladorDSP().ativarMensagem(interfaceMensagem);  
        } catch (ValidacaoCampoException vCampoEx) {
        	interfaceUsina.getControladorDSP().getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(vCampoEx.getMessage());
            interfaceUsina.getControladorDSP().ativarMensagem(interfaceMensagem);
        }
    }//GEN-LAST:event_jButtonConfirmarActionPerformed

    private void validaInformacoesEdicaoCampanha() throws ValidacaoCampoException {

        if (jTextNomeCampanha.getText().trim().equals("")) {
            throw new ValidacaoCampoException(PropertiesUtil.buscarPropriedade("mensagem.validacao.edicao.campanha.nome.campanha.vazio"));
        }

        if (jTextFaseProcesso.getText().trim().equals("") || jTextFaseProcesso.getText().trim().equals("0")) {
            throw new ValidacaoCampoException(PropertiesUtil.buscarPropriedade("mensagem.validacao.edicao.campanha.fase.processo.vazio"));
        }

        DSSStockyardTimeUtil.validarDataHora(dtIniCalendarioHoraCFlex.getDataHora(), PropertiesUtil.getMessage("aviso.data.hora.inicio.campanha"));

        DSSStockyardTimeUtil.validarDataHora(dtFinCalendarioHoraCFlex.getDataHora(), PropertiesUtil.getMessage("aviso.data.hora.fim.campanha"));
        
        Date dataInicialCampanha = DSSStockyardTimeUtil.criaDataComString(dtIniCalendarioHoraCFlex.getDataHora(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
        Date dataFinalCampanha = DSSStockyardTimeUtil.criaDataComString(dtFinCalendarioHoraCFlex.getDataHora(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));

        if (dataFinalCampanha.compareTo(dataInicialCampanha) < 0) {
            throw new ValidacaoCampoException(PropertiesUtil.buscarPropriedade("mensagem.validacao.edicao.campanha.datafinal.maior.datainicial"));
        }
        
        /**
         * TODO REFATORAR EM META USINA 
         * 
         * 
         */
                
        for (Campanha campanhaUsina : usina.getMetaUsina().getListaCampanhas())
        {        	
        	if (campanhaUsina != campanha)
        	{
	        	if ((campanhaUsina.getDataInicial().getTime() > dataInicialCampanha.getTime() && campanhaUsina.getDataInicial().getTime() < dataFinalCampanha.getTime()) ||
	        		(campanhaUsina.getDataFinal().getTime() > dataInicialCampanha.getTime() && campanhaUsina.getDataFinal().getTime() < dataFinalCampanha.getTime()) ||
	        		(dataInicialCampanha.getTime() > campanhaUsina.getDataInicial().getTime() && dataInicialCampanha.getTime() < campanhaUsina.getDataFinal().getTime()) ||
	        		(dataFinalCampanha.getTime() > campanhaUsina.getDataInicial().getTime() && dataFinalCampanha.getTime() < campanhaUsina.getDataFinal().getTime()))
	        		throw new ValidacaoCampoException(PropertiesUtil.buscarPropriedade("mensagem.validacao.edicao.campanha.ja.existente.periodo"));
        	}
        }

        Double taxaDeOperacao = DSSStockyardFuncoesNumeros.getStringToDouble(txtTaxaOperacaoMaxima.getText().trim());
        if (taxaDeOperacao <= 0) {
            throw new ValidacaoCampoException(PropertiesUtil.buscarPropriedade("mensagem.validacao.edicao.campanha.taxa.operacao.vazio"));
        }

        TipoProduto tipoProdutoSel = (TipoProduto) cmbTipoProduto.getSelectedItem();
        if (tipoProdutoSel == null) {
            throw new ValidacaoCampoException(PropertiesUtil.buscarPropriedade("mensagem.validacao.edicao.campanha.tipoproduto.nao.selecionado"));
        }

        TipoProduto tipoProdutoPelletSel = (TipoProduto) cmbTipoProdutoPallet.getSelectedItem();
        if (tipoProdutoPelletSel == null) {
            throw new ValidacaoCampoException(PropertiesUtil.buscarPropriedade("mensagem.validacao.edicao.campanha.tipoprodutopellet.nao.selecionado"));
        }

        TipoProduto tipoProdutoScreeningSel = (TipoProduto) cmbTipoProdutoScreening.getSelectedItem();
        if (tipoProdutoScreeningSel == null) {
            throw new ValidacaoCampoException(PropertiesUtil.buscarPropriedade("mensagem.validacao.edicao.campanha.tipoprodutoscreening.nao.selecionado"));
        }
    }

    private void cmbTipoProdutoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTipoProdutoItemStateChanged
        TipoProduto tipoProdutoSelecionado = (TipoProduto) cmbTipoProduto.getSelectedItem();
        if (tipoProdutoSelecionado != null) {
            String[] rgb = tipoProdutoSelecionado.getCorIdentificacao().split(",");
            lblCorTipoProduto.setBackground(new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2])));
            this.tipoProduto = tipoProdutoSelecionado;
            carregaComboTiposDeProdutoPellet(tipoProdutoSelecionado);
            carregaComboTiposDeProdutoScreening();
        }
    }//GEN-LAST:event_cmbTipoProdutoItemStateChanged

    private void cmbTipoProdutoPalletItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTipoProdutoPalletItemStateChanged
        TipoProduto tipoProdutoPelletSelecionado = (TipoProduto) cmbTipoProdutoPallet.getSelectedItem();
        if (tipoProdutoPelletSelecionado != null) {
            String[] rgb = tipoProdutoPelletSelecionado.getCorIdentificacao().split(",");
            lblCorTipoProdutoPallet.setBackground(new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2])));
            this.tipoProdutoPelletFeed = tipoProdutoPelletSelecionado;
        }
    }//GEN-LAST:event_cmbTipoProdutoPalletItemStateChanged

    private void cmbTipoProdutoScreeningItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTipoProdutoScreeningItemStateChanged
        TipoProduto tipoProdutoScreeningSelecionado = (TipoProduto) cmbTipoProdutoScreening.getSelectedItem();
        if (tipoProdutoScreeningSelecionado != null) {
            String[] rgb = tipoProdutoScreeningSelecionado.getCorIdentificacao().split(",");
            lblCorTipoProdutoScreening.setBackground(new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2])));
            this.tipoProdutoScreening = tipoProdutoScreeningSelecionado;
        }
    }//GEN-LAST:event_cmbTipoProdutoScreeningItemStateChanged

    private void jTextFaseProcesso1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFaseProcesso1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFaseProcesso1ActionPerformed

    private void jTextAreaPelotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAreaPelotaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextAreaPelotaActionPerformed

    private void jTextTipoItemPelotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextTipoItemPelotaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextTipoItemPelotaActionPerformed

    private void jTextAreaPelletFeedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAreaPelletFeedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextAreaPelletFeedActionPerformed

    private void jTextTipoProcessoPelotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextTipoProcessoPelotaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextTipoProcessoPelotaActionPerformed

    private void jTextFaseProcesso2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFaseProcesso2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFaseProcesso2ActionPerformed

    private void jTextFaseProcesso3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFaseProcesso3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFaseProcesso3ActionPerformed

    private void cmbFinalizaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbFinalizaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbFinalizaActionPerformed

    public abstract void fecharJanela();

    public Boolean getOperacaoCanceladaPeloUsuario() {
        return this.operacaoCanceladaPeloUsuario;
    }

    public void setOperacaoCanceladaPeloUsuario(Boolean operacaoCanceladaPeloUsuario) {
        this.operacaoCanceladaPeloUsuario = operacaoCanceladaPeloUsuario;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbEstadoUsina;
    private javax.swing.JCheckBox cmbFinaliza;
    private javax.swing.JComboBox cmbTipoProduto;
    private javax.swing.JComboBox cmbTipoProdutoPallet;
    private javax.swing.JComboBox cmbTipoProdutoScreening;
    private javax.swing.JButton cmdDesistir;
    private com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex dtFinCalendarioHoraCFlex;
    private com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex dtIniCalendarioHoraCFlex;
    private javax.swing.JButton jButtonConfirmar;
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
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelNomeUsina;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextAreaPelletFeed;
    private javax.swing.JTextField jTextAreaPelota;
    private javax.swing.JTextField jTextFaseProcesso;
    private javax.swing.JTextField jTextFaseProcesso1;
    private javax.swing.JTextField jTextFaseProcesso2;
    private javax.swing.JTextField jTextFaseProcesso3;
    private javax.swing.JTextField jTextNomeCampanha;
    private javax.swing.JTextField jTextTipoItemPellet;
    private javax.swing.JTextField jTextTipoItemPelota;
    private javax.swing.JTextField jTextTipoProcessoPellet;
    private javax.swing.JTextField jTextTipoProcessoPelota;
    private javax.swing.JLabel lblCorTipoProduto;
    private javax.swing.JLabel lblCorTipoProdutoPallet;
    private javax.swing.JLabel lblCorTipoProdutoScreening;
    private com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable tblItensControle;
    private javax.swing.JTextField txtTaxaOperacaoMaxima;
    // End of variables declaration//GEN-END:variables

    private void setObjetosRetorno() throws ValidacaoCampoException {

        // divide-se 24, pois na tela a taxa eh exibida em tonelada/dia e internamente devemos calcular em tonelada/hora
        this.usina.setTaxaDeOperacao(DSSStockyardFuncoesNumeros.getStringToDouble(txtTaxaOperacaoMaxima.getText()) / 24);
        this.usina.setEstado((EstadoMaquinaEnum) cmbEstadoUsina.getSelectedItem());

        //valida data inicial
        DSSStockyardTimeUtil.validarDataHora(this.dtIniCalendarioHoraCFlex.getDataHora(), PropertiesUtil.getMessage("aviso.validacao.data.inicial"));
        this.dataInicial = DSSStockyardTimeUtil.criaDataComString(this.dtIniCalendarioHoraCFlex.getDataHora(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
        //valida data final
        DSSStockyardTimeUtil.validarDataHora(this.dtFinCalendarioHoraCFlex.getDataHora(), PropertiesUtil.getMessage("aviso.validacao.data.final"));
        this.dataFinal = DSSStockyardTimeUtil.criaDataComString(this.dtFinCalendarioHoraCFlex.getDataHora(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));

        this.codigoFaseProcessoPelletFeed = DSSStockyardFuncoesNumeros.getStringToLong(this.jTextFaseProcesso.getText());
        this.codigoFaseProcessoPelletFeedQAQ = DSSStockyardFuncoesNumeros.getStringToLong(this.jTextFaseProcesso2.getText());
        this.codigoFaseProcessoPelletFeedQAF = DSSStockyardFuncoesNumeros.getStringToLong(this.jTextFaseProcesso3.getText());

        this.codigoAreaPelletFeed = this.jTextAreaPelletFeed.getText().trim();
        this.codigoTipoItemPelletFeed = DSSStockyardFuncoesNumeros.getStringToLong(this.jTextTipoItemPellet.getText());
        this.codigoTipoProcessoPelletFeed = this.jTextTipoProcessoPellet.getText().trim();
        //TODO
        this.codigoFaseProcessoPelota = DSSStockyardFuncoesNumeros.getStringToLong(this.jTextFaseProcesso1.getText());
        this.codigoAreaPelota = this.jTextAreaPelota.getText().trim();
        this.codigoTipoItemPelota = DSSStockyardFuncoesNumeros.getStringToLong(this.jTextTipoItemPelota.getText());
        this.codigoTipoProcessoPelota = this.jTextTipoProcessoPelota.getText().trim();

        this.nomeCampanha = this.jTextNomeCampanha.getText();
        this.tipoProduto = (TipoProduto) this.cmbTipoProduto.getSelectedItem();

        this.campanha.setNomeCampanha(this.nomeCampanha);
        this.campanha.setDataInicial(this.dataInicial);
        this.campanha.setDataFinal(this.dataFinal);
        this.campanha.setTipoProduto(this.tipoProduto);
        
        this.campanha.setCodigoFaseProcessoPelletFeed(this.codigoFaseProcessoPelletFeed);
        this.campanha.setCodigoFaseProcessoPelletFeedQAQ(this.codigoFaseProcessoPelletFeedQAQ);
        this.campanha.setCodigoFaseProcessoPelletFeedQAF(this.codigoFaseProcessoPelletFeedQAF);

        this.campanha.setTipoProcessoPelletFeed(this.codigoTipoProcessoPelletFeed);
        this.campanha.setCdTipoItemControlePelletFeed(this.codigoTipoItemPelletFeed);
        this.campanha.setAreaRespEDPelletFeed(this.codigoAreaPelletFeed);
        
        
        this.campanha.setCodigoFaseProcessoPelota(this.codigoFaseProcessoPelota);
        this.campanha.setTipoProcessoPelota(this.codigoTipoProcessoPelota);
        this.campanha.setCdTipoItemControlePelota(this.codigoTipoItemPelota);
        this.campanha.setAreaRespEDPelota(this.codigoAreaPelota);
        
        
        
        this.campanha.setTipoPellet(this.tipoProdutoPelletFeed);
        this.campanha.setTipoScreening(this.tipoProdutoScreening);
        this.campanha.setQuantidadePrevista(0.00);
        this.campanha.setTipoPellet(this.tipoProdutoPelletFeed);
        this.campanha.setTipoScreening(this.tipoProdutoScreening);
        this.campanha.setMetaUsina(usina.getMetaUsina());
        
        this.campanha.setFinalizada(this.cmbFinaliza.isSelected());
        
        for (int i = 0; i < this.tblItensControle.getRowCount(); i++) {
            for (ItemDeControle item : this.qualidadeCampanhaClonada.getListaDeItensDeControle()) {
                if (item.getTipoItemControle().getDescricaoTipoItemControle().equals(this.tblItensControle.getValueAt(i, 0))) {
                    item.setValor(DSSStockyardFuncoesNumeros.getStringToDouble(this.tblItensControle.getValueAt(i, 1).toString()));
//                    item.setDesvioPadraoValor(DSSStockyardFuncoesNumeros.getStringToDouble(this.tblItensControle.getValueAt(i, 2).toString()));
                }
            }
        }

        // ... verifica se os itens de controle da qualidade da campanha foram alteradas.
        // ... caso foram alterados a qualidade da campanha deve ser a qualidade nova clonada
        if (!qualidadeCampanhaClonada.equals(this.campanha.getQualidadeEstimada()))
        {
           this.campanha.setQualidadeEstimada(qualidadeCampanhaClonada);
        }
      
    }
}
