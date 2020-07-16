/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * InterfaceTratamentoPSM.java
 *
 * Created on 03/06/2009, 12:04:11
 * @author Guilherme
 */
package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio;

/**
 *
 * @author guilherme
 */
public abstract class InterfaceTratamentoPSM extends javax.swing.JPanel {

   /*  Lista de balizas do tipo PSM 
    private List<Baliza> listaBalizasPSM = new ArrayList<Baliza>();

     Lista de balizas que podem receber o resultado do peneiramento 
    private List<Baliza> listaBalizasDoPatio = new ArrayList<Baliza>();

    *//** Lista de colunas da tabela de informacoes das balizas selecionadas *//*
    private List<ColunaTabela> listaColunaInformacoes = new ArrayList<ColunaTabela>();

    *//** Lista com as balizas que receberao os pellet-psm *//*
    private List<Baliza> listaBalizasDestinoPellet = new ArrayList<Baliza>();
    
    *//** Lista com as balizas que receberao as pelotas-psm *//*
    private List<Baliza> listaBalizasDestinoPelota = new ArrayList<Baliza>();

    *//** Lista com as balizas que receberao as lixo *//*
    private List<Baliza> listaBalizasDestinoLixo = new ArrayList<Baliza>();
    
    *//**Lista das informações da tabela*//*
    private List<ProdutoMovimentacao> listaProdutoMovimentacao;

    *//** Vetor com os dados ta tabela de informacoes da baliza *//*
    private Vector vInformacoesBalizasSelecionadas;

    *//** Flag para marcar quando houver uma operacao cancelada pelo usuario *//*
    private Boolean operacaoCanceladaPeloUsuario = Boolean.FALSE;

    *//** acesso ao controlador do subsistema DSP *//*
    private ControladorDSP controladorDSP;

    *//** Maquina no patio que ira transportar o produto tratado. *//*
    private MaquinaDoPatio maquina;

    private DadosTratamentoPSM dadosTratamentoPSM;
    
    *//** Controla quando no nome da pilha deve ser apagado do textbox e quando nao deve *//*
    private Boolean apagaNomePilhaPellet = Boolean.FALSE;
    private Boolean apagaNomePilhaPelota = Boolean.FALSE;
    private Boolean apagaNomePilhaLixo = Boolean.FALSE;

	*//** Constantes referentes as porcentagens de valor que resultam do tratamento. *//*
    private Double PORCENTAGEM_PELLET_SCREENING = 0.0d; 
    private Double PORCENTAGEM_PELOTA = 0.0d;
    private Double PORCENTAGEM_LIXO = 0.0d;

    *//** Constantes da tabela de informacoes das balizas selecionadas *//*
    private final Integer COL_BALIZA_SELECAO = 0;
    private final Integer COL_BALIZA_NOME = 1;
    private final Integer COL_BALIZA_NUMERO = 2;
    private final Integer COL_BALIZA_QUANTIDADE = 3;
    public static Integer COL_BALIZA_QUANTIDADE_MOVIMENTACAO = 4;

    *//** Creates new form InterfaceTratamentoPSM *//*
    public InterfaceTratamentoPSM(ControladorDSP controladorDSP, List<Baliza> listaBalizasPSM, List<Baliza> listaBalizasDoPatio, List<MaquinaDoPatio> maquinasDoPatio) {
        initComponents();
        this.listaBalizasPSM = listaBalizasPSM;
        this.listaBalizasDoPatio = listaBalizasDoPatio;
        

         Recupera a pa-carregadeira do patio que irá movimentar o produto resultante
         * do tratamento.
         
        for (MaquinaDoPatio maquinaDoPatio : maquinasDoPatio) {
            if (maquinaDoPatio.getTipoDaMaquina().equals(TipoMaquinaEnum.PA_CARREGADEIRA)) {
                this.maquina = maquinaDoPatio;
            }
        }
        
        this.controladorDSP = controladorDSP;
        PORCENTAGEM_PELLET_SCREENING = this.retornaProcentagemTratamentoProdutoResultante(TipoDeProdutoEnum.PELLET_PSM);
        PORCENTAGEM_PELOTA = this.retornaProcentagemTratamentoProdutoResultante(TipoDeProdutoEnum.PELOTA_PSM);
        PORCENTAGEM_LIXO = this.retornaProcentagemTratamentoProdutoResultante(TipoDeProdutoEnum.LIXO);
        vInformacoesBalizasSelecionadas = new Vector();

        // Monta combos de Sentido de formacao da baliza.
        this.montaCombosSentido();

        //Monta combo de balizas que poderao receber o pellet-psm e as pelotas-psm.
        this.montaComboBaliza(listaBalizasDoPatio);

        // Cria tabela com informacoes das balizas com PSM ...
        this.criaColunasInformacoesBalizasPSM();
        // ... e popula a tabela com as informacoes necessarias.
        this.generateListaProdutosMovimentacao();
        
        try {
            this.atualizaTabelaInformacoesBalizasSelecionadas();
        } catch (ErroSistemicoException ex) {
            Logger.getLogger(InterfaceMovimentacao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    *//**
     * Monta os combos de sentido de empilhamento.
     *//*
    private void montaCombosSentido() {

        // Monta combo de Sentido de formacao da baliza de pellet screening
        cmbSentidoPellet.addItem(SentidoEmpilhamentoRecuperacaoEnum.NORTE_PARA_SUL);
        cmbSentidoPellet.addItem(SentidoEmpilhamentoRecuperacaoEnum.SUL_PARA_NORTE);

        // Monta combo de Sentido de formacao da baliza de produto
        cmbSentidoProduto.addItem(SentidoEmpilhamentoRecuperacaoEnum.NORTE_PARA_SUL);
        cmbSentidoProduto.addItem(SentidoEmpilhamentoRecuperacaoEnum.SUL_PARA_NORTE);

        // Monta combo de Sentido de formacao da baliza de lixo
        cmbSentidoLixo.addItem(SentidoEmpilhamentoRecuperacaoEnum.NORTE_PARA_SUL);
        cmbSentidoLixo.addItem(SentidoEmpilhamentoRecuperacaoEnum.SUL_PARA_NORTE);

    }

    *//**
     * Monta o combo de baliza de tanto para Pellet quanto para pelotas.
     *
     * @param listaBaliza
     * @param comboBaliza
     *//*
    private void montaComboBaliza(List<Baliza> listaBaliza) {
        for (Baliza baliza : listaBaliza) {
            cmbBalizaPellet.addItem(baliza);
            cmbBalizaProduto.addItem(baliza);
            cmbBalizaLixo.addItem(baliza);
        }
    }

    *//**
     * Gera lista com PSM para tratamento.
     *//*
    private void generateListaProdutosMovimentacao() {
        listaProdutoMovimentacao = new ArrayList<ProdutoMovimentacao>();
        for (Baliza baliza : listaBalizasPSM) {
            listaProdutoMovimentacao.add(new ProdutoMovimentacao(baliza, Boolean.FALSE, 0.0));
        }
    }

    *//**
     * Metodo que cria as colunas para a tabela com as balizas que possuem
     * produto do tipo PSM nas balizas do tipo PSM.
     *//*
    private void criaColunasInformacoesBalizasPSM() {
        listaColunaInformacoes = new ArrayList<ColunaTabela>();
        ColunaTabela colInfo;

        colInfo = new ColunaTabela();
        colInfo.setAlinhamento(SwingConstants.CENTER);
        colInfo.setEditar(Boolean.TRUE);
        colInfo.setLargura(40);
        colInfo.setRedimensionar(Boolean.FALSE);
        colInfo.setTitulo("Sel");
        colInfo.setTipoEditor(ColunaTabela.COL_TIPO_CHECKBOX);
        listaColunaInformacoes.add(colInfo);

        colInfo = new ColunaTabela();
        colInfo.setAlinhamento(SwingConstants.LEADING);
        colInfo.setEditar(Boolean.FALSE);
        colInfo.setLargura(180);
        colInfo.setRedimensionar(Boolean.FALSE);
        colInfo.setTitulo("Descrição");
        listaColunaInformacoes.add(colInfo);

        colInfo = new ColunaTabela();
        colInfo.setAlinhamento(SwingConstants.CENTER);
        colInfo.setEditar(Boolean.FALSE);
        colInfo.setLargura(130);
        colInfo.setRedimensionar(Boolean.FALSE);
        colInfo.setTitulo("Número");
        listaColunaInformacoes.add(colInfo);

        colInfo = new ColunaTabela();
        colInfo.setAlinhamento(SwingConstants.RIGHT);
        colInfo.setEditar(Boolean.FALSE);
        colInfo.setLargura(100);
        colInfo.setRedimensionar(Boolean.FALSE);
        colInfo.setTitulo("Quantidade");
        listaColunaInformacoes.add(colInfo);

        colInfo = new ColunaTabela();
        colInfo.setAlinhamento(SwingConstants.LEADING);
        colInfo.setEditar(Boolean.TRUE);
        colInfo.setLargura(130);
        colInfo.setRedimensionar(Boolean.FALSE);
        colInfo.setTitulo("Qtd. movimentada");
        listaColunaInformacoes.add(colInfo);
    }

    *//**
     * Atualiza as informacoes na tabela de lista de balizas que possuem produto
     * do tipo PSM para ser tratado.
     *
     * @throws ErroSistemicoException
     *//*
    private void atualizaTabelaInformacoesBalizasSelecionadas() throws ErroSistemicoException {

        vInformacoesBalizasSelecionadas.removeAllElements();
        for (ProdutoMovimentacao produtoMovimentacao : listaProdutoMovimentacao) {
            Object[] dados = new Object[5];
            dados[COL_BALIZA_SELECAO] = new JCheckBox("", produtoMovimentacao.getSelecionada());
            dados[COL_BALIZA_NOME] = produtoMovimentacao.getBalizaSelecionada().getNomeBaliza();
            dados[COL_BALIZA_NUMERO] = DSSStockyardFuncoesTexto.zerosStr(3, produtoMovimentacao.getBalizaSelecionada().getNumero());
            dados[COL_BALIZA_QUANTIDADE] = DSSStockyardFuncoesNumeros.getQtdeFormatada(produtoMovimentacao.getBalizaSelecionada().getProduto().getQuantidade(), 2);
            dados[COL_BALIZA_QUANTIDADE_MOVIMENTACAO] = DSSStockyardFuncoesNumeros.getQtdeFormatada(produtoMovimentacao.getQuantidadeMovimentacao(), 2);

            vInformacoesBalizasSelecionadas.add(new Vector(Arrays.asList(dados)));
        }
        CFlexStockyardFuncoesTabela.setInformacoesTabela(balizasPSMCFlexStockyardJTable, vInformacoesBalizasSelecionadas, listaColunaInformacoes);
    }

    *//**
     * Trata o campo de quantidade que foi alterado.
     *//*
    private void trataCampoNumero() {
        if (balizasPSMCFlexStockyardJTable.getSelectedColumn() == COL_BALIZA_QUANTIDADE_MOVIMENTACAO) {
            Double maximo = listaProdutoMovimentacao.get(balizasPSMCFlexStockyardJTable.getSelectedRow()).getBalizaSelecionada().getProduto().getQuantidade();

            if (!((String) balizasPSMCFlexStockyardJTable.getValueAt(balizasPSMCFlexStockyardJTable.getSelectedRow(), balizasPSMCFlexStockyardJTable.getSelectedColumn())).trim().isEmpty()) {
                try {
                    String value = ((String) balizasPSMCFlexStockyardJTable.getValueAt(balizasPSMCFlexStockyardJTable.getSelectedRow(), balizasPSMCFlexStockyardJTable.getSelectedColumn())).replace('.', ';').replace(',', '.').replace(';', ',');
                    if (Double.parseDouble(value) > 0) {
                        if (Double.parseDouble(value) > maximo) {
                            balizasPSMCFlexStockyardJTable.setValueAt(DSSStockyardFuncoesNumeros.getQtdeFormatada(maximo, 2), balizasPSMCFlexStockyardJTable.getSelectedRow(), COL_BALIZA_QUANTIDADE_MOVIMENTACAO);
                        }
                        balizasPSMCFlexStockyardJTable.setValueAt(Boolean.TRUE, balizasPSMCFlexStockyardJTable.getSelectedRow(), COL_BALIZA_SELECAO);
                        listaProdutoMovimentacao.get(balizasPSMCFlexStockyardJTable.getSelectedRow()).setSelecionada(Boolean.TRUE);
                    } else {
                        balizasPSMCFlexStockyardJTable.setValueAt(Boolean.FALSE, balizasPSMCFlexStockyardJTable.getSelectedRow(), COL_BALIZA_SELECAO);
                        listaProdutoMovimentacao.get(balizasPSMCFlexStockyardJTable.getSelectedRow()).setSelecionada(Boolean.FALSE);
                    }
                    listaProdutoMovimentacao.get(balizasPSMCFlexStockyardJTable.getSelectedRow()).setQuantidadeMovimentacao(Double.parseDouble(value));

                } catch (Exception ex) {
                    balizasPSMCFlexStockyardJTable.setValueAt(DSSStockyardFuncoesNumeros.getQtdeFormatada(listaProdutoMovimentacao.get(balizasPSMCFlexStockyardJTable.getSelectedRow()).getQuantidadeMovimentacao(), 2), balizasPSMCFlexStockyardJTable.getSelectedRow(), balizasPSMCFlexStockyardJTable.getSelectedColumn());
                }
            } else {
                balizasPSMCFlexStockyardJTable.setValueAt(Boolean.FALSE, balizasPSMCFlexStockyardJTable.getSelectedRow(), COL_BALIZA_SELECAO);
            }

        }
        updateInformationOnDestination();
    }

    *//**
     * Atualiza as informacoes de quantidade quando o total eh alterado.
     * Atualza o total de pellet de acordo com a porcentagem pre-configurada assim
     * como a quantidade de pelotas.
     *//*
    private void updateInformationOnDestination() {
        double value = 0.0;
        for (ProdutoMovimentacao produtoMovimentacao : listaProdutoMovimentacao) {
            value += produtoMovimentacao.getQuantidadeMovimentacao();
        }

        // Quantidade total tratada
        txtTotalTratado.setText(String.valueOf(value));

        // Quantidade resultante de Pellet Screening
        txtQtdPellet.setText(DSSStockyardFuncoesNumeros.getQtdeFormatada(value * PORCENTAGEM_PELLET_SCREENING, 2));

        // Quantidade resultante de produto
        txtQtdProduto.setText(DSSStockyardFuncoesNumeros.getQtdeFormatada(value * PORCENTAGEM_PELOTA, 2));

        // Quantidade resultante de Lixo
        txtQtdLixo.setText(DSSStockyardFuncoesNumeros.getQtdeFormatada(value * PORCENTAGEM_LIXO, 2));
    }


    *//**
     * Retorna o volume disponivel na baliza.
     * 
     * @param baliza
     * @return quantidade
     *//*
    private Double getVolumeDisponivelNaBaliza(Baliza baliza) {
        Double quantidade = Double.POSITIVE_INFINITY;
        for (ProdutoMovimentacao produtoMovimentacao : listaProdutoMovimentacao) {
            if (produtoMovimentacao.getBalizaSelecionada().equals(baliza)) {
                quantidade = baliza.getCapacidadeMaxima() - baliza.getProduto().getQuantidade() + produtoMovimentacao.getQuantidadeMovimentacao();
                break;
            }
        }
        if (quantidade.isInfinite()) {
            if (baliza.getProduto() != null && baliza.getProduto().getQuantidade() != null) {
                quantidade = baliza.getCapacidadeMaxima() - baliza.getProduto().getQuantidade();
            } else {
                quantidade = baliza.getCapacidadeMaxima();
            }

        }
        return quantidade;
    }

    *//**
     * Verifica se a baliza pode receber o produto resultante do tratamento.
     *
     * @param balizaInicial
     * @param balizaAnalizada
     * @param tipoBaliza
     * @return resultado
     *//*
    private Boolean isBalizaValidaParaMovimentacao(Baliza balizaInicial, Baliza balizaAnalizada, EnumTipoBaliza tipoBaliza) {
        Boolean resultado = Boolean.TRUE;

        // Verifica se a baliza iniciada eh do mesmo tipo da baliza inicial.
        if (!balizaAnalizada.getTipoBaliza().equals(tipoBaliza)) {
            return false;
        }

        // se a baliza for de uma pilha, deve ser a mesma da baliza inicial, caso contrário, basta ser de um tipo adequado.
        if (balizaAnalizada.getPilha() != null) {
            if (balizaInicial.getPilha() == null) {
                resultado = Boolean.FALSE;
            } else {
                if (!balizaAnalizada.getPilha().equals(balizaInicial.getPilha())) {
                    resultado = Boolean.FALSE;
                }
            }
        }

        return resultado;
    }

    *//**
     * Verifica se existe volume disponível para movimentar o produto resultante
     * do tratamento.
     *
     * @param quantidade
     * @param inicioMovimentacao
     * @param direcao
     * @return ok
     *//*
    private Boolean verificaVolumeEspacoDisponivel(Double quantidade, Baliza inicioMovimentacao, SentidoEmpilhamentoRecuperacaoEnum direcao, List<Baliza> listaBalizasDestino) {
        Boolean ok = Boolean.FALSE;
        Boolean startCount = Boolean.FALSE;
        if (direcao.equals(SentidoEmpilhamentoRecuperacaoEnum.NORTE_PARA_SUL)) {
            //iterar do começo para o fim
            for (Baliza baliza : maquina.getPatio().getListaDeBalizas()) {
                if (!startCount) {
                    startCount = baliza.equals(inicioMovimentacao);
                }
                if (startCount) {

                    if (isBalizaValidaParaMovimentacao(inicioMovimentacao, baliza, baliza.getTipoBaliza())) {
                        quantidade -= getVolumeDisponivelNaBaliza(baliza);
                        listaBalizasDestino.add(baliza);
                    } else {
                        return false;
                    }
                    if (quantidade <= 0) {
                        return true;
                    }
                }
            }
        } else {
            //iterar do fim para o começo
            List<Baliza> listaDeBalizas = maquina.getPatio().getListaDeBalizas();

            for (int i = listaDeBalizas.size() - 1; i >= 0; i--) {
                Baliza baliza = listaDeBalizas.get(i);
                if (!startCount) {
                    startCount = baliza.equals(inicioMovimentacao);
                }
                if (startCount) {

                    if (isBalizaValidaParaMovimentacao(inicioMovimentacao, baliza, baliza.getTipoBaliza())) {
                        quantidade -= getVolumeDisponivelNaBaliza(baliza);
                        listaBalizasDestino.add(baliza);
                    } else {
                        return false;
                    }
                    if (quantidade <= 0) {
                        return true;
                    }
                }
            }
        }
        return ok;
    }
    
    *//**
     * Verifica se existe conflito no destino das balizas de resultado do peneiramento.
     * 
     * @return
     *//*
    private Boolean verificaBalizasDeDestinoDosProdutos() {
    	for (Baliza balizaPellet : listaBalizasDestinoPellet) {
			for (Baliza balizaPelota : listaBalizasDestinoPelota) {
				if (balizaPelota.getNumero().equals(balizaPellet.getNumero())) {
					return Boolean.FALSE;
				}
			}
		}
    	return Boolean.TRUE;
    }

    *//**
     *
     * @return ValidadeInformacaoEnum
     *//*
    private ValidadeInformacaoEnum verificaValidadeDeMovimentacao() {
        if (txtPilhaPellet.getText() == null || txtPilhaPellet.getText().trim().isEmpty()) {
            return ValidadeInformacaoEnum.FALTA_NOME_BALIZA;
        }

        if (txtPilhaProduto.getText() == null || txtPilhaProduto.getText().trim().isEmpty()) {
            return ValidadeInformacaoEnum.FALTA_NOME_BALIZA;
        }

        if (txtPilhaLixo.getText() == null || txtPilhaLixo.getText().trim().isEmpty()) {
            return ValidadeInformacaoEnum.FALTA_NOME_BALIZA;
        }

        Double quantidade = 0.0;
        for (ProdutoMovimentacao produtoMovimentacao : listaProdutoMovimentacao) {
            quantidade += produtoMovimentacao.getQuantidadeMovimentacao();
        }
        if (quantidade == 0) {
            return ValidadeInformacaoEnum.FALTA_PRODUTO;
        }
        
        if (((Baliza)cmbBalizaPellet.getSelectedItem()).getNumero().equals(((Baliza)cmbBalizaProduto.getSelectedItem()).getNumero())) {
        	return ValidadeInformacaoEnum.MESMO_LUGAR;
        }

        if (((Baliza)cmbBalizaPellet.getSelectedItem()).getNumero().equals(((Baliza)cmbBalizaLixo.getSelectedItem()).getNumero())) {
        	return ValidadeInformacaoEnum.MESMO_LUGAR;
        }

        if (((Baliza)cmbBalizaProduto.getSelectedItem()).getNumero().equals(((Baliza)cmbBalizaLixo.getSelectedItem()).getNumero())) {
        	return ValidadeInformacaoEnum.MESMO_LUGAR;
        }
        
        listaBalizasDestinoPellet = new ArrayList<Baliza>();
        listaBalizasDestinoPelota = new ArrayList<Baliza>();
        listaBalizasDestinoLixo = new ArrayList<Baliza>();
        try {
			if (!verificaVolumeEspacoDisponivel(DSSStockyardFuncoesNumeros.getStringToDouble(txtQtdPellet.getText()), (Baliza)cmbBalizaPellet.getSelectedItem(), (SentidoEmpilhamentoRecuperacaoEnum)cmbSentidoPellet.getSelectedItem(), listaBalizasDestinoPellet)) {
			    return ValidadeInformacaoEnum.NAO_CABE;
			}
			
			if (!verificaVolumeEspacoDisponivel(DSSStockyardFuncoesNumeros.getStringToDouble(txtQtdProduto.getText()), (Baliza)cmbBalizaProduto.getSelectedItem(), (SentidoEmpilhamentoRecuperacaoEnum)cmbSentidoProduto.getSelectedItem(), listaBalizasDestinoPelota)) {
	            return ValidadeInformacaoEnum.NAO_CABE;
	        }

                        	if (!verificaVolumeEspacoDisponivel(DSSStockyardFuncoesNumeros.getStringToDouble(txtQtdLixo.getText()), (Baliza)cmbBalizaLixo.getSelectedItem(), (SentidoEmpilhamentoRecuperacaoEnum)cmbSentidoLixo.getSelectedItem(), listaBalizasDestinoLixo)) {
	            return ValidadeInformacaoEnum.NAO_CABE;
	        }
		} catch (ValidacaoCampoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        if (!verificaBalizasDeDestinoDosProdutos()) {
            return ValidadeInformacaoEnum.MESMO_LUGAR;
        }

        return ValidadeInformacaoEnum.OK;
    }

    *//** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     *//*
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        balizasPSMCFlexStockyardJTable = new com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable();
        lblTotalTratado = new javax.swing.JLabel();
        txtTotalTratado = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        cmbBalizaPellet = new javax.swing.JComboBox();
        lblBalizaIniPellet = new javax.swing.JLabel();
        lblSentidoPellet = new javax.swing.JLabel();
        cmbSentidoPellet = new javax.swing.JComboBox();
        lblQtdPellet = new javax.swing.JLabel();
        txtQtdPellet = new javax.swing.JTextField();
        lblPilha = new javax.swing.JLabel();
        txtPilhaPellet = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        lblBalizaProduto = new javax.swing.JLabel();
        cmbBalizaProduto = new javax.swing.JComboBox();
        lblSentidoProduto = new javax.swing.JLabel();
        cmbSentidoProduto = new javax.swing.JComboBox();
        lblQtdProduto = new javax.swing.JLabel();
        txtQtdProduto = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtPilhaProduto = new javax.swing.JTextField();
        btnConfirmar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtPilhaLixo = new javax.swing.JTextField();
        cmbBalizaLixo = new javax.swing.JComboBox();
        cmbSentidoLixo = new javax.swing.JComboBox();
        txtQtdLixo = new javax.swing.JTextField();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Balizas do tipo PSM com produto"));

        balizasPSMCFlexStockyardJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        balizasPSMCFlexStockyardJTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                balizasPSMCFlexStockyardJTableMouseClicked(evt);
            }
        });
        balizasPSMCFlexStockyardJTable.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                balizasPSMCFlexStockyardJTableKeyReleased(evt);
            }
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                balizasPSMCFlexStockyardJTableKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(balizasPSMCFlexStockyardJTable);

        lblTotalTratado.setText("Total tratado");

        txtTotalTratado.setEditable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 733, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(lblTotalTratado)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTotalTratado, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTotalTratado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotalTratado)))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Balizas de destino do Pellet Screening"));
        jPanel2.setRequestFocusEnabled(false);

        cmbBalizaPellet.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbBalizaPelletItemStateChanged(evt);
            }
        });

        lblBalizaIniPellet.setText("Baliza");

        lblSentidoPellet.setText("Sentido");

        lblQtdPellet.setText("Qtd. de produto");

        txtQtdPellet.setEnabled(false);

        lblPilha.setText("Pilha");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblPilha)
                        .addGap(10, 10, 10)
                        .addComponent(txtPilhaPellet, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblBalizaIniPellet)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbBalizaPellet, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblSentidoPellet)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSentidoPellet, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblQtdPellet)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtQtdPellet, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPilhaPellet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPilha))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBalizaIniPellet)
                    .addComponent(cmbBalizaPellet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSentidoPellet)
                    .addComponent(cmbSentidoPellet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblQtdPellet)
                    .addComponent(txtQtdPellet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Balizas de destino das pelotas"));
        jPanel3.setRequestFocusEnabled(false);

        lblBalizaProduto.setText("Baliza");

        cmbBalizaProduto.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbBalizaProdutoItemStateChanged(evt);
            }
        });

        lblSentidoProduto.setText("Sentido");

        lblQtdProduto.setText("Qtd. de produto");

        txtQtdProduto.setEnabled(false);

        jLabel1.setText("Pilha");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtPilhaProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(lblBalizaProduto)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbBalizaProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(lblSentidoProduto)
                        .addGap(10, 10, 10)
                        .addComponent(cmbSentidoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(lblQtdProduto)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtQtdProduto, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtPilhaProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBalizaProduto)
                    .addComponent(cmbBalizaProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbSentidoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSentidoProduto))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblQtdProduto)
                    .addComponent(txtQtdProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnConfirmar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icones/ok.png"))); // NOI18N
        btnConfirmar.setText("Confirmar");
        btnConfirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmarActionPerformed(evt);
            }
        });

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icones/exit.png"))); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.setPreferredSize(new java.awt.Dimension(101, 25));
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Balizas de destino Lixo"));

        jLabel2.setText("Pilha");

        jLabel3.setText("Baliza");

        jLabel4.setText("Sentido");

        jLabel5.setText("Qtd. de produto");

        txtPilhaLixo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPilhaLixoActionPerformed(evt);
            }
        });

        cmbBalizaLixo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbBalizaLixoItemStateChanged(evt);
            }
        });

        txtQtdLixo.setEnabled(false);
        txtQtdLixo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtQtdLixoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtPilhaLixo, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtQtdLixo, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbBalizaLixo, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSentidoLixo, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtPilhaLixo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cmbBalizaLixo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cmbSentidoLixo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtQtdLixo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(10, 10, 10)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(btnConfirmar))
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnConfirmar)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void balizasPSMCFlexStockyardJTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_balizasPSMCFlexStockyardJTableKeyReleased
        trataCampoNumero();
    }//GEN-LAST:event_balizasPSMCFlexStockyardJTableKeyReleased

    private void balizasPSMCFlexStockyardJTableKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_balizasPSMCFlexStockyardJTableKeyTyped
        evt.consume();
    }//GEN-LAST:event_balizasPSMCFlexStockyardJTableKeyTyped

    private void balizasPSMCFlexStockyardJTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_balizasPSMCFlexStockyardJTableMouseClicked
        if (balizasPSMCFlexStockyardJTable.getSelectedColumn() == COL_BALIZA_SELECAO) {
            if (balizasPSMCFlexStockyardJTable.getValueAt(balizasPSMCFlexStockyardJTable.getSelectedRow(), balizasPSMCFlexStockyardJTable.getSelectedColumn()).equals(Boolean.TRUE)) {
                Double value = listaProdutoMovimentacao.get(balizasPSMCFlexStockyardJTable.getSelectedRow()).getBalizaSelecionada().getProduto().getQuantidade();
                balizasPSMCFlexStockyardJTable.setValueAt(DSSStockyardFuncoesNumeros.getQtdeFormatada(value, 2), balizasPSMCFlexStockyardJTable.getSelectedRow(), COL_BALIZA_QUANTIDADE_MOVIMENTACAO);
                listaProdutoMovimentacao.get(balizasPSMCFlexStockyardJTable.getSelectedRow()).setQuantidadeMovimentacao(value);
                listaProdutoMovimentacao.get(balizasPSMCFlexStockyardJTable.getSelectedRow()).setSelecionada(Boolean.TRUE);
            } else {
                balizasPSMCFlexStockyardJTable.setValueAt(DSSStockyardFuncoesNumeros.getQtdeFormatada(0.0, 2), balizasPSMCFlexStockyardJTable.getSelectedRow(), COL_BALIZA_QUANTIDADE_MOVIMENTACAO);
                listaProdutoMovimentacao.get(balizasPSMCFlexStockyardJTable.getSelectedRow()).setQuantidadeMovimentacao(0.0);
                listaProdutoMovimentacao.get(balizasPSMCFlexStockyardJTable.getSelectedRow()).setSelecionada(Boolean.FALSE);
            }
            updateInformationOnDestination();

        } else {
            trataCampoNumero();
        }
    }//GEN-LAST:event_balizasPSMCFlexStockyardJTableMouseClicked

    private void cmbBalizaPelletItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbBalizaPelletItemStateChanged
        Baliza balizaSelecionada = (Baliza) cmbBalizaPellet.getSelectedItem();
        if (balizaSelecionada != null) {
            if (balizaSelecionada.getPilha() != null && balizaSelecionada.getPilha().getNomePilha() != null && !balizaSelecionada.getPilha().getNomePilha().isEmpty()) {
                txtPilhaPellet.setText(balizaSelecionada.getPilha().getNomePilha());
                apagaNomePilhaPellet = Boolean.TRUE;
                txtPilhaPellet.setEditable(Boolean.FALSE);
            } else {
            	if (txtPilhaPellet.getText() != null && apagaNomePilhaPellet == Boolean.TRUE) {
            		txtPilhaPellet.setText("");
            		apagaNomePilhaPellet = Boolean.FALSE;
            	}
                txtPilhaPellet.setEditable(Boolean.TRUE);
            }
        }
    }//GEN-LAST:event_cmbBalizaPelletItemStateChanged

    private void cmbBalizaProdutoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbBalizaProdutoItemStateChanged
        Baliza balizaSelecionada = (Baliza) cmbBalizaProduto.getSelectedItem();
        if (balizaSelecionada != null) {
            if (balizaSelecionada.getPilha() != null &&
                    balizaSelecionada.getPilha().getNomePilha() != null &&
                    !balizaSelecionada.getPilha().getNomePilha().isEmpty()) {
                txtPilhaProduto.setText(balizaSelecionada.getPilha().getNomePilha());
                apagaNomePilhaPelota = Boolean.TRUE;
                txtPilhaProduto.setEditable(Boolean.FALSE);
            } else {
            	if (txtPilhaProduto.getText() != null && apagaNomePilhaPelota == Boolean.TRUE) {
            		txtPilhaProduto.setText("");
            		apagaNomePilhaPelota = Boolean.FALSE;
            	}
                
                txtPilhaProduto.setEditable(Boolean.TRUE);
            }
        }
    }//GEN-LAST:event_cmbBalizaProdutoItemStateChanged

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        this.fecharJanela();
        DSSStockyardTelaUtil.getParentDialog(this).setVisible(false);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnConfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmarActionPerformed
        ValidadeInformacaoEnum resultadoValidacao = verificaValidadeDeMovimentacao();
        if (resultadoValidacao.equals(ValidadeInformacaoEnum.OK)) {
            List<ProdutoMovimentacao> listaProdutoMovimentacaoRetorno = new ArrayList<ProdutoMovimentacao>();
            for (ProdutoMovimentacao produtoMovimentacao : listaProdutoMovimentacao) {
                if (produtoMovimentacao.getSelecionada()) {
                    listaProdutoMovimentacaoRetorno.add(produtoMovimentacao);
                }
            }
            
            dadosTratamentoPSM = new DadosTratamentoPSM(listaProdutoMovimentacaoRetorno, (Baliza)cmbBalizaPellet.getSelectedItem(), (Baliza)cmbBalizaProduto.getSelectedItem(), (Baliza)cmbBalizaLixo.getSelectedItem(), (SentidoEmpilhamentoRecuperacaoEnum)cmbSentidoPellet.getSelectedItem(), (SentidoEmpilhamentoRecuperacaoEnum)cmbSentidoProduto.getSelectedItem(), (SentidoEmpilhamentoRecuperacaoEnum)cmbSentidoLixo.getSelectedItem(), txtPilhaPellet.getText(), txtPilhaProduto.getText(), txtPilhaLixo.getText(), maquina);
            this.operacaoCanceladaPeloUsuario = Boolean.FALSE;
            // obtendo o JDialog que adicionou o painel
            DSSStockyardTelaUtil.getParentDialog(this).setVisible(false);
        } else if (resultadoValidacao.equals(ValidadeInformacaoEnum.FALTA_NOME_BALIZA)) {
            Object[] options = {"Ok"};

            JOptionPane.showOptionDialog(this, "Defina o nome da pilha para movimentacao", "Dados inválidos",
                    JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
        } else if (resultadoValidacao.equals(ValidadeInformacaoEnum.FALTA_PRODUTO)) {
            Object[] options = {"Ok"};

            JOptionPane.showOptionDialog(this, "Defina as balizas de origem para a movimentacao", "Dados inválidos",
                    JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
        } else if (resultadoValidacao.equals(ValidadeInformacaoEnum.NAO_CABE)) {
            Object[] options = {"Ok"};

            JOptionPane.showOptionDialog(this, "Lugar de movimentação não comporta todo o material selecionado", "Dados inválidos",
                    JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
        } else if (resultadoValidacao.equals(ValidadeInformacaoEnum.MESMO_LUGAR)) {
            Object[] options = {"Ok"};

            JOptionPane.showOptionDialog(this, "Lugar de destino dos produtos são conflitantes", "Dados inválidos",
                    JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
        }
    }//GEN-LAST:event_btnConfirmarActionPerformed

    private void txtPilhaLixoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPilhaLixoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPilhaLixoActionPerformed

    private void cmbBalizaLixoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbBalizaLixoItemStateChanged
        Baliza balizaSelecionada = (Baliza) cmbBalizaLixo.getSelectedItem();
        if (balizaSelecionada != null) {
            if (balizaSelecionada.getPilha() != null &&
                    balizaSelecionada.getPilha().getNomePilha() != null &&
                    !balizaSelecionada.getPilha().getNomePilha().isEmpty()) {
                txtPilhaLixo.setText(balizaSelecionada.getPilha().getNomePilha());
                apagaNomePilhaLixo = Boolean.TRUE;
                txtPilhaLixo.setEditable(Boolean.FALSE);
            } else {
            	if (txtPilhaLixo.getText() != null && apagaNomePilhaLixo == Boolean.TRUE) {
            		txtPilhaLixo.setText("");
            		apagaNomePilhaLixo = Boolean.FALSE;
            	}

                txtPilhaLixo.setEditable(Boolean.TRUE);
            }
        }
    }//GEN-LAST:event_cmbBalizaLixoItemStateChanged

    private void txtQtdLixoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtQtdLixoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtQtdLixoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable balizasPSMCFlexStockyardJTable;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnConfirmar;
    private javax.swing.JComboBox cmbBalizaLixo;
    private javax.swing.JComboBox cmbBalizaPellet;
    private javax.swing.JComboBox cmbBalizaProduto;
    private javax.swing.JComboBox cmbSentidoLixo;
    private javax.swing.JComboBox cmbSentidoPellet;
    private javax.swing.JComboBox cmbSentidoProduto;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBalizaIniPellet;
    private javax.swing.JLabel lblBalizaProduto;
    private javax.swing.JLabel lblPilha;
    private javax.swing.JLabel lblQtdPellet;
    private javax.swing.JLabel lblQtdProduto;
    private javax.swing.JLabel lblSentidoPellet;
    private javax.swing.JLabel lblSentidoProduto;
    private javax.swing.JLabel lblTotalTratado;
    private javax.swing.JTextField txtPilhaLixo;
    private javax.swing.JTextField txtPilhaPellet;
    private javax.swing.JTextField txtPilhaProduto;
    private javax.swing.JTextField txtQtdLixo;
    private javax.swing.JTextField txtQtdPellet;
    private javax.swing.JTextField txtQtdProduto;
    private javax.swing.JTextField txtTotalTratado;
    // End of variables declaration//GEN-END:variables

    *//**
     * Metodo criado abstrato, pois sua implementacao sera na classe que o instanciou
     *//*
    public abstract void fecharJanela();

    *//**
     * @return the operacaoCanceladaPeloUsuario
     *//*
    public Boolean getOperacaoCanceladaPeloUsuario() {
        return operacaoCanceladaPeloUsuario;
    }

    *//**
     * @param operacaoCanceladaPeloUsuario the operacaoCanceladaPeloUsuario to set
     *//*
    public void setOperacaoCanceladaPeloUsuario(Boolean operacaoCanceladaPeloUsuario) {
        this.operacaoCanceladaPeloUsuario = operacaoCanceladaPeloUsuario;
    }

    *//**
     * @return the dadosTratamentoPSM
     *//*
    public DadosTratamentoPSM getDadosTratamentoPSM() {
        return dadosTratamentoPSM;
    }

    *//**
     * @param dadosTratamentoPSM the dadosTratamentoPSM to set
     *//*
    public void setDadosTratamentoPSM(DadosTratamentoPSM dadosTratamentoPSM) {
        this.dadosTratamentoPSM = dadosTratamentoPSM;
    }

    public enum ValidadeInformacaoEnum {

        *//** Movimentacao pode ser realizada sem problemas *//*
        OK,
        *//**
         * Quantidades excedem o lugar determinado para movimentacao
         *//*
        NAO_CABE,
        *//** Resultantes de tratamento são movidos para a mesma baliza *//*
        MESMO_LUGAR,
        *//** NOME DA BALIZA DEVE SER FORNECIDO *//*
        FALTA_NOME_BALIZA,
        *//** SEM QUANTIDADES DEFINIDAS*//*
        FALTA_PRODUTO;

        @Override
        public String toString() {
            String retorno = null;
            if (name().equals(OK.name())) {
                retorno = PropertiesUtil.buscarPropriedade("enum.operacao");
            } else if (name().equals(NAO_CABE.name())) {
                retorno = PropertiesUtil.buscarPropriedade("enum.manutencao");
            } else if (name().equals(FALTA_NOME_BALIZA.name())) {
                retorno = PropertiesUtil.buscarPropriedade("enum.ociosa");
            } else if (name().equals(FALTA_PRODUTO.name())) {
                retorno = PropertiesUtil.buscarPropriedade("enum.ociosa");
            } else {
                retorno = "Retorno sem chave de bundle cadastrada";
            }
            return retorno;
        }
    }

    public Double retornaProcentagemTratamentoProdutoResultante(TipoDeProdutoEnum tipoProduto) {
        try {
            List<TipoProduto> listaTipoProduto = controladorDSP.getInterfaceInicial().getInterfaceInicial().getListaTiposProduto();
        
            for (TipoProduto tipoProdutoNaLista : listaTipoProduto) {
                if (tipoProdutoNaLista.getTipoDeProduto().equals(tipoProduto)) {
                    return tipoProdutoNaLista.getPorcentagemResultadoPSM();
                }
            }
        } catch (ErroSistemicoException ex) {
            Logger.getLogger(InterfaceMaquinaDoPatio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
*/
}
