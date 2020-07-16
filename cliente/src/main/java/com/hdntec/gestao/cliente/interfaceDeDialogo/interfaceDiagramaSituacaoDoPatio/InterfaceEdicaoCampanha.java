package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.hdntec.gestao.exceptions.ValidacaoCampoException;
import com.hdntec.gestao.util.PropertiesUtil;
import com.hdntec.gestao.util.datahora.DSSStockyardTimeUtil;

public abstract class InterfaceEdicaoCampanha extends javax.swing.JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7887134367650756388L;
	
	private InterfaceMensagem interfaceMensagem;
    private Boolean operacaoCanceladaPeloUsuario;
//    private InterfaceUsina interfaceUsina;
    private ControladorDSP controladorDSP;
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
    private Long codigoFaseProcessoPelletFeed;
    private Long codigoFaseProcessoPelota;
    /** usado para mapear itemDeControle e o valorGarantidoMinimo, futuramente usar um valor com Devio padrão e propagação de erro */
    private Map<TipoItemDeControle, Double> listaValor;
    private List<TipoItemDeControle> listaTiposItemDeControle;

    Qualidade qualidadeCampanhaClonada = null;

    private enum ACAO {

        CRIACAO,
        EDICAO
    }
    private ACAO acao;

    /** Creates new form InterfaceEditarCampanha */
    public InterfaceEdicaoCampanha(ControladorDSP controlador, Usina usina, 
    		List<TipoProduto> listaTiposProduto, 
    		List<TipoItemDeControle> listaTiposItemDeControle,
    		Campanha campanhaSelecionada) {
    	
        initComponents();
//        this.interfaceUsina = interfaceUsina;
        this.controladorDSP = controlador;
        this.usina = usina;//this.interfaceUsina.getUsinaVisualizada();
        this.listaTiposProduto = listaTiposProduto;
        this.listaTiposItemDeControle = listaTiposItemDeControle;
        carregaComboTiposDeProduto();
        carregaComboTiposDeProdutoScreening();


        
        if (campanhaSelecionada == null) {

            this.acao = ACAO.CRIACAO;

            this.qualidadeCampanhaClonada = new Qualidade();
            this.qualidadeCampanhaClonada.setListaDeAmostras(null);
            this.qualidadeCampanhaClonada.setEhReal(false);
            this.qualidadeCampanhaClonada.setDtInicio(new Date(System.currentTimeMillis()));
            this.qualidadeCampanhaClonada.setDtInsert(new Date(System.currentTimeMillis()));

            this.campanha = new Campanha();
            this.campanha.setCaptacaoTemporaria(new Double(0));
            this.campanha.setCodigoFaseProcessoPelletFeed(new Long(0));
            this.campanha.setCodigoFaseProcessoPelota(new Long(0));
            this.campanha.setDataInicial(new Date());
            this.campanha.setDataFinal(new Date());
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
            this.codigoFaseProcessoPelota = this.campanha.getCodigoFaseProcessoPelota();

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
           // this.usina.setCampanhaAtual(this.campanha);
        } else {
            this.acao = ACAO.EDICAO;
            //é preciso pegar a campanha selecionada e nao mais a campanhaAtual
            this.campanha = campanhaSelecionada;//this.usina.getCampanhaAtual();

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
            //this.qualidadeCampanhaClonada = cloneHelper.verifyQualidade(this.campanha.getQualidadeEstimada());
            for (ItemDeControle itemDeControle : this.qualidadeCampanhaClonada.getListaDeItensDeControle()) {
                listaValor.put(itemDeControle.getTipoItemControle(), itemDeControle.getValor());
            }
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

        //Coluna valor item de controle.
        coluna = new ColunaTabela();
        coluna.setLargura(120);
        coluna.setTitulo(PropertiesUtil.getMessage("mensagem.coluna.tabela.devio.padrao.valor"));
        coluna.setAlinhamento(SwingConstants.LEFT);
        coluna.setRedimensionar(true);
        coluna.setEditar(true);
        coluna.setTipoEditor(ColunaTabela.COL_TIPO_TEXT);
        listaColunas.add(coluna);

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
        // multiplica-se por 24, pois internamente usamos tonelada/hora e na tela usaremos tonelada/dia
        this.txtTaxaOperacaoMaxima.setText(DSSStockyardFuncoesNumeros.getQtdeFormatada(this.usina.getTaxaDeOperacao() * 24, 2));

        this.dtIniCalendarioHoraCFlex.setDataHora(this.dataInicial);
        this.dtFinCalendarioHoraCFlex.setDataHora(this.dataFinal);
        this.jTextFaseProcesso.setText(this.codigoFaseProcessoPelletFeed.toString());
        //TODO
        //this.jTextFaseProcesso.setText(this.codigoFaseProcessoPelota.toString());
        this.jTextNomeCampanha.setText(this.nomeCampanha);
        this.cmbTipoProduto.setSelectedItem(this.campanha.getTipoProduto());
        this.cmbTipoProdutoPallet.setSelectedItem(this.campanha.getTipoPellet());
        this.cmbTipoProdutoScreening.setSelectedItem(this.campanha.getTipoScreening());

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
      jLabel7 = new javax.swing.JLabel();
      jLabel8 = new javax.swing.JLabel();
      cmbTipoProduto = new javax.swing.JComboBox();
      jLabelNomeUsina = new javax.swing.JLabel();
      jTextNomeCampanha = new javax.swing.JTextField();
      jScrollPane1 = new javax.swing.JScrollPane();
      tblItensControle = new com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable();
      cmdDesistir = new javax.swing.JButton();
      jButtonConfirmar = new javax.swing.JButton();
      jTextFaseProcesso = new javax.swing.JTextField();
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

      jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12));
      jLabel1.setText(PropertiesUtil.getMessage("label.usina"));

      jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12));
      jLabel2.setText(PropertiesUtil.getMessage("label.estado.usina"));

      jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12));
      jLabel3.setText(PropertiesUtil.getMessage("label.taxa.operacao.maxima"));

      jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12));
      jLabel4.setText(PropertiesUtil.getMessage("label.campanha"));

      jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12));
      jLabel5.setText(PropertiesUtil.getMessage("label.data.inicio"));

      jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12));
      jLabel7.setText(PropertiesUtil.getMessage("label.codigo.fase.processo"));

      jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12));
      jLabel8.setText(PropertiesUtil.getMessage("label.tipo.produto"));

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
      value.append(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens")).append("/confirmar.png");


      jButtonConfirmar.setIcon(new javax.swing.ImageIcon(value.toString())); // NOI18N
      jButtonConfirmar.setText(PropertiesUtil.getMessage("botao.confirmar"));
      jButtonConfirmar.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButtonConfirmarActionPerformed(evt);
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

      
      value = new StringBuffer();
      value.append(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens")).append("/exit.png");
      cmdDesistir.setIcon(new javax.swing.ImageIcon(value.toString())); // NOI18N
      
      cmdDesistir.setText(PropertiesUtil.getMessage("botao.desistir"));
      cmdDesistir.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            cmdDesistirActionPerformed(evt);
         }
      });
      jTextFaseProcesso.setFont(new java.awt.Font("Tahoma", 0, 12));

      jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12));
      jLabel9.setText(PropertiesUtil.getMessage("label.data.final"));

      jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12));
      jLabel10.setText(PropertiesUtil.getMessage("label.ton.dia"));

      cmbEstadoUsina.setFont(new java.awt.Font("Tahoma", 0, 12));
      cmbEstadoUsina.setModel(new javax.swing.DefaultComboBoxModel(new Vector(Arrays.asList(EstadoMaquinaEnum.values()))));

      txtTaxaOperacaoMaxima.setFont(new java.awt.Font("Tahoma", 0, 12));

      lblCorTipoProduto.setFont(new java.awt.Font("Tahoma", 1, 12));
      lblCorTipoProduto.setOpaque(true);

      jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12));
      jLabel11.setText(PropertiesUtil.getMessage("label.tipo.pellet.feed"));

      jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12));
      jLabel12.setText(PropertiesUtil.getMessage("label.tipo.screening"));

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

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
      this.setLayout(layout);
      layout.setHorizontalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(layout.createSequentialGroup()
                  .addContainerGap()
                  .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                     .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelNomeUsina, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(cmbEstadoUsina, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                     .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                           .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                              .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                 .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel4)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextNomeCampanha, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                              .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                              .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                 .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel8)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(cmbTipoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(lblCorTipoProduto, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE))
                                 .addGroup(layout.createSequentialGroup()
                                    .addGap(10, 10, 10)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(dtFinCalendarioHoraCFlex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                 .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel7)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextFaseProcesso, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))))
                           .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                              .addComponent(jLabel11)
                              .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                              .addComponent(cmbTipoProdutoPallet, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                              .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                              .addComponent(lblCorTipoProdutoPallet, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                              .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                              .addComponent(jLabel12)
                              .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                              .addComponent(cmbTipoProdutoScreening, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                              .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                              .addComponent(lblCorTipoProdutoScreening, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(17, 17, 17))))
               .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                  .addGap(14, 14, 14)
                  .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 649, Short.MAX_VALUE))
               .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                  .addContainerGap()
                  .addComponent(cmdDesistir)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(jButtonConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap())
      );
      layout.setVerticalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                  .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addComponent(cmbEstadoUsina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
               .addComponent(jLabelNomeUsina, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(11, 11, 11)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel4)
               .addComponent(jTextNomeCampanha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel7)
               .addComponent(jTextFaseProcesso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(layout.createSequentialGroup()
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                     .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                     .addComponent(dtIniCalendarioHoraCFlex, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                     .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING)
                     .addComponent(dtFinCalendarioHoraCFlex, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                     .addComponent(jLabel3)
                     .addComponent(txtTaxaOperacaoMaxima, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                     .addComponent(jLabel10)
                     .addComponent(jLabel8)
                     .addComponent(cmbTipoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                     .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel11)
                        .addComponent(cmbTipoProdutoPallet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                     .addComponent(lblCorTipoProdutoPallet, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                     .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel12)
                        .addComponent(cmbTipoProdutoScreening, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
               .addGroup(layout.createSequentialGroup()
                  .addGap(32, 32, 32)
                  .addComponent(lblCorTipoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                  .addComponent(lblCorTipoProdutoScreening, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jButtonConfirmar)
               .addComponent(cmdDesistir))
            .addGap(38, 38, 38))
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
            this.operacaoCanceladaPeloUsuario = Boolean.FALSE;

            // obtendo o JDialog que adicionou o painel
            DSSStockyardTelaUtil.getParentDialog(this).setVisible(false);
        } catch (ValidacaoCampoException vCampoEx) {
        	controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(vCampoEx.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
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

    public abstract void fecharJanela();

    public Boolean getOperacaoCanceladaPeloUsuario() {
        return this.operacaoCanceladaPeloUsuario;
    }

    public void setOperacaoCanceladaPeloUsuario(Boolean operacaoCanceladaPeloUsuario) {
        this.operacaoCanceladaPeloUsuario = operacaoCanceladaPeloUsuario;
    }

   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JComboBox cmbEstadoUsina;
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
   private javax.swing.JLabel jLabel2;
   private javax.swing.JLabel jLabel3;
   private javax.swing.JLabel jLabel4;
   private javax.swing.JLabel jLabel5;
   private javax.swing.JLabel jLabel7;
   private javax.swing.JLabel jLabel8;
   private javax.swing.JLabel jLabel9;
   private javax.swing.JLabel jLabelNomeUsina;
   private javax.swing.JScrollPane jScrollPane1;
   private javax.swing.JTextField jTextFaseProcesso;
   private javax.swing.JTextField jTextNomeCampanha;
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
        //TODO
        //this.codigoFaseProcessoPelota = DSSStockyardFuncoesNumeros.getStringToLong(this.jTextFaseProcesso.getText());
        this.nomeCampanha = this.jTextNomeCampanha.getText();
        this.tipoProduto = (TipoProduto) this.cmbTipoProduto.getSelectedItem();

        this.campanha.setNomeCampanha(this.nomeCampanha);
        this.campanha.setDataInicial(this.dataInicial);
        this.campanha.setDataFinal(this.dataFinal);
        this.campanha.setTipoProduto(this.tipoProduto);
        this.campanha.setCodigoFaseProcessoPelletFeed(this.codigoFaseProcessoPelletFeed);
        this.campanha.setCodigoFaseProcessoPelota(this.codigoFaseProcessoPelota);
        this.campanha.setTipoPellet(this.tipoProdutoPelletFeed);
        this.campanha.setTipoScreening(this.tipoProdutoScreening);
        this.campanha.setQuantidadePrevista(0.00);
        this.campanha.setTipoPellet(this.tipoProdutoPelletFeed);
        this.campanha.setTipoScreening(this.tipoProdutoScreening);

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