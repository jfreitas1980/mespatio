package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio;

import java.awt.Color;
import java.awt.Font;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.MaskFormatter;
import javax.swing.text.PlainDocument;

import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios.InterfaceBerco;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios.InterfaceCarga;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios.InterfaceNavio;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios.InterfacePier;
import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.cliente.util.numeros.DSSStockyardFuncoesNumeros;
import com.hdntec.gestao.domain.navios.entity.status.Carga;
import com.hdntec.gestao.domain.navios.entity.status.OrientacaoDeEmbarque;
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.enums.TipoAtividadeEnum;
import com.hdntec.gestao.domain.planta.entity.status.AtividadeCampanha;
import com.hdntec.gestao.domain.planta.entity.status.Campanha;
import com.hdntec.gestao.domain.planta.entity.status.Filtragem;
import com.hdntec.gestao.domain.planta.entity.status.MaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Usina;
import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;
import com.hdntec.gestao.domain.produto.entity.Produto;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.exceptions.AtividadeException;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.ValidacaoCampoException;
import com.hdntec.gestao.exceptions.ValidacaoObjetosOperacaoException;
import com.hdntec.gestao.util.PropertiesUtil;
import com.hdntec.gestao.util.datahora.DSSStockyardTimeUtil;

public class InterfaceAtualizacaoUsinaRecuperacao extends javax.swing.JDialog {

    /** o controlador de acesso ao subsistema de DSP */
    ControladorDSP controladorDSP;
    /** A usina usada na recuperacao */
    Usina usinaEditada;
    
    private Map<Integer,Campanha> mapComboCampanha;
    
    /** A usina usada na recuperacao */
    Filtragem filtragemEditada;
    
    /** a interface da usina usada na recuperacao */
    InterfaceUsina interfaceUsina;

    /** a interface da usina usada na recuperacao */
    InterfaceFiltragem interfaceFiltragem;

    /** Controla se a edicao foi cancelada ou nao */
    private Boolean operacaoCanceladaPeloUsuario = Boolean.FALSE;
    /** a interface de mensagem */
    private InterfaceMensagem interfaceMensagem;
    /** ativividade de recuperacao */
    
    private Atividade atividadeExecutada; 
    /** controla se a interface foi aberta para inicio ou finalizacao de atividade, para poder validar datas*/
    private boolean segundoAcesso = Boolean.FALSE;

    /** Creates new form InterfaceAtualizacaoUsinaRecuperacao */
    public InterfaceAtualizacaoUsinaRecuperacao(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public InterfaceAtualizacaoUsinaRecuperacao(java.awt.Frame parent, boolean modal, ControladorDSP controladorDSP, Usina usinaEditada,Filtragem filtragem, InterfaceUsina interfaceUsina,InterfaceFiltragem interfaceFiltragem) throws ErroSistemicoException {        
    	super(parent, modal);
    	this.controladorDSP = controladorDSP;
        this.interfaceUsina = interfaceUsina;
        this.interfaceFiltragem = interfaceFiltragem;
        this.usinaEditada = usinaEditada;
        this.filtragemEditada = filtragem;
        this.operacaoCanceladaPeloUsuario = Boolean.FALSE;
        initComponents();        
        initDatas();
        
        Date dataInicial = null;
        try {
            dataInicial = DSSStockyardTimeUtil.criaDataComString( txtDataHoraInicioRecuperacao.getText() , PropertiesUtil.buscarPropriedade("formato.campo.datahora"));            
        } catch (ValidacaoCampoException e) {
            visibilidadeBotoes(true);            
            controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            interfaceMensagem.setDescricaoMensagem(e.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
        }
        
        montaComboProdutoProduzido(dataInicial);        
        montaComboNaviosAtracado();
        if (this.interfaceUsina != null && this.interfaceUsina.getUsinaVisualizada().getEstado().equals(EstadoMaquinaEnum.OPERACAO) && this.interfaceUsina.getUsinaVisualizada().getAtividade() != null && this.interfaceUsina.getUsinaVisualizada().getAtividade().getTipoAtividade().equals(TipoAtividadeEnum.ATUALIZACAO_RECUPERACAO)  ) {
            atividadeExecutada = usinaEditada.getAtividade();            
        }else if (this.interfaceFiltragem != null && this.interfaceFiltragem.getFiltragemVisualizada().getEstado().equals(EstadoMaquinaEnum.OPERACAO) && this.interfaceFiltragem.getFiltragemVisualizada().getAtividade() != null && this.interfaceFiltragem.getFiltragemVisualizada().getAtividade().getTipoAtividade().equals(TipoAtividadeEnum.ATUALIZACAO_RECUPERACAO)  ) {
            atividadeExecutada = filtragemEditada.getAtividade();            
        }        
         
         if (atividadeExecutada != null) {
             carregarInformacoesAtividade();
         }
         
         
         
         
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlInformacoesUsina = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtDataHoraInicioRecuperacao = new javax.swing.JFormattedTextField();
        MaskFormatter fmtHoraInicioRecuperacao = new MaskFormatter();
        try {
            fmtHoraInicioRecuperacao.setMask(PropertiesUtil.buscarPropriedade("mascara.campo.datahora"));
            fmtHoraInicioRecuperacao.setPlaceholderCharacter(' ');
            fmtHoraInicioRecuperacao.install(txtDataHoraInicioRecuperacao);
        } catch (ParseException pex) {
            pex.printStackTrace();
        }
        jLabel7 = new javax.swing.JLabel();
        txtDataHoraFinalRecuperacao = new javax.swing.JFormattedTextField();
        MaskFormatter fmtHoraFinalRecuperacao = new MaskFormatter();
        try {
            fmtHoraFinalRecuperacao.setMask(PropertiesUtil.buscarPropriedade("mascara.campo.datahora"));
            fmtHoraFinalRecuperacao.setPlaceholderCharacter(' ');
            fmtHoraFinalRecuperacao.install(txtDataHoraFinalRecuperacao);
        } catch (ParseException pex) {
            pex.printStackTrace();
        }
        jLabel3 = new javax.swing.JLabel();
        txtNomeUsina = new javax.swing.JTextField();
        pnlInformacoesCampanha = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtNomeCampanha = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtDataInicialCampanha = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtDataFinalCampanha = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtTaxaOperacaoUsina = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        cmbProdutoProduzido = new javax.swing.JComboBox();
        txtCorProdutoProduzido = new javax.swing.JTextField();
        pnlInformacoesCargasNavio = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        cmbNavioAtracado = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        txtOrientacaoEmbarque = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        cmbCargasNavio = new javax.swing.JComboBox();
        txtCorProdutoCargaNavio = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtNumeroPorao = new javax.swing.JTextField();
        cmdDesistir = new javax.swing.JButton();
        cmdConfirmar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Atualização de Recuperação de Usina");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        pnlInformacoesUsina.setBorder(javax.swing.BorderFactory.createTitledBorder("Informações para recuperação da usina"));

        jLabel6.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel6.setText("Data hora inicio recuperação:");


        PlainDocument doc = (PlainDocument) txtDataHoraInicioRecuperacao.getDocument();   
        doc.addDocumentListener(new DocumentListener() {   
          public void changedUpdate(DocumentEvent e) {   
              txtDataHoraInicioRecuperacaoTextChanged();
          }   
          public void insertUpdate(DocumentEvent e) {   
              txtDataHoraInicioRecuperacaoTextChanged();
          }   
          public void removeUpdate(DocumentEvent e) {   
              //txtDataHoraInicioRecuperacaoTextChanged();
          }   
        });   


        PlainDocument doc1 = (PlainDocument) txtDataHoraFinalRecuperacao.getDocument();   
        doc1.addDocumentListener(new DocumentListener() {   
          public void changedUpdate(DocumentEvent e) {   
              //txtDataHoraFimRecuperacaoTextChanged();
          }   
          public void insertUpdate(DocumentEvent e) {   
            //  txtDataHoraFimRecuperacaoTextChanged();
          }   
          public void removeUpdate(DocumentEvent e) {   
              //txtDataHoraInicioRecuperacaoTextChanged();
          }   
        });   

        
        jLabel7.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel7.setText("Data hora término recuperação:");

        jLabel3.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel3.setText("Nome da usina:");

        txtNomeUsina.setEditable(false);
        txtNomeUsina.setFont(new java.awt.Font("Arial", 0, 12));

        pnlInformacoesCampanha.setBorder(javax.swing.BorderFactory.createTitledBorder("Informações da Campanha"));

        jLabel4.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel4.setText("Nome da campanha:");

        txtNomeCampanha.setEditable(false);
        txtNomeCampanha.setFont(new java.awt.Font("Arial", 0, 12));

        jLabel5.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel5.setText("Data inicial:");

        txtDataInicialCampanha.setEditable(false);
        txtDataInicialCampanha.setFont(new java.awt.Font("Arial", 0, 12));

        jLabel13.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel13.setText("Data final:");

        txtDataFinalCampanha.setEditable(false);
        txtDataFinalCampanha.setFont(new java.awt.Font("Arial", 0, 12));

        jLabel14.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel14.setText("Taxa de operação:");

        jLabel15.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel15.setText("ton/dia");

        txtTaxaOperacaoUsina.setEditable(false);
        txtTaxaOperacaoUsina.setFont(new java.awt.Font("Arial", 0, 12));

        jLabel2.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel2.setText("Produto produzido:");

        cmbProdutoProduzido.setFont(new java.awt.Font("Arial", 0, 12));
        cmbProdutoProduzido.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbProdutoProduzidoItemStateChanged(evt);
            }
        });

        txtCorProdutoProduzido.setEditable(false);
        txtCorProdutoProduzido.setFont(new java.awt.Font("Arial", 0, 12));

        javax.swing.GroupLayout pnlInformacoesCampanhaLayout = new javax.swing.GroupLayout(pnlInformacoesCampanha);
        pnlInformacoesCampanha.setLayout(pnlInformacoesCampanhaLayout);
        pnlInformacoesCampanhaLayout.setHorizontalGroup(
            pnlInformacoesCampanhaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInformacoesCampanhaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInformacoesCampanhaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInformacoesCampanhaLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbProdutoProduzido, 0, 180, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(txtCorProdutoProduzido, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(213, 213, 213))
                    .addGroup(pnlInformacoesCampanhaLayout.createSequentialGroup()
                        .addGroup(pnlInformacoesCampanhaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlInformacoesCampanhaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlInformacoesCampanhaLayout.createSequentialGroup()
                                .addComponent(txtDataInicialCampanha, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(24, 24, 24)
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDataFinalCampanha, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(71, Short.MAX_VALUE))
                            .addGroup(pnlInformacoesCampanhaLayout.createSequentialGroup()
                                .addComponent(txtNomeCampanha, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTaxaOperacaoUsina, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel15)
                                .addGap(88, 88, 88))))))
        );
        pnlInformacoesCampanhaLayout.setVerticalGroup(
            pnlInformacoesCampanhaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInformacoesCampanhaLayout.createSequentialGroup()
                .addGroup(pnlInformacoesCampanhaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cmbProdutoProduzido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCorProdutoProduzido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlInformacoesCampanhaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtNomeCampanha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(txtTaxaOperacaoUsina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInformacoesCampanhaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInformacoesCampanhaLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(pnlInformacoesCampanhaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDataFinalCampanha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13)
                            .addComponent(txtDataInicialCampanha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlInformacoesCampanhaLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(1, 1, 1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlInformacoesUsinaLayout = new javax.swing.GroupLayout(pnlInformacoesUsina);
        pnlInformacoesUsina.setLayout(pnlInformacoesUsinaLayout);
        pnlInformacoesUsinaLayout.setHorizontalGroup(
            pnlInformacoesUsinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlInformacoesUsinaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInformacoesUsinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pnlInformacoesCampanha, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlInformacoesUsinaLayout.createSequentialGroup()
                        .addGroup(pnlInformacoesUsinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlInformacoesUsinaLayout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(txtDataHoraInicioRecuperacao, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlInformacoesUsinaLayout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDataHoraFinalRecuperacao, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNomeUsina, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnlInformacoesUsinaLayout.setVerticalGroup(
            pnlInformacoesUsinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInformacoesUsinaLayout.createSequentialGroup()
                .addGroup(pnlInformacoesUsinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtDataHoraInicioRecuperacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txtNomeUsina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlInformacoesUsinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtDataHoraFinalRecuperacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(pnlInformacoesCampanha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlInformacoesCargasNavio.setBorder(javax.swing.BorderFactory.createTitledBorder("Seleção de informações navio"));

        jLabel8.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel8.setText("Navio atracado:");

        cmbNavioAtracado.setFont(new java.awt.Font("Arial", 0, 12));
        cmbNavioAtracado.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbNavioAtracadoItemStateChanged(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel9.setText("Orientação:");

        txtOrientacaoEmbarque.setEditable(false);
        txtOrientacaoEmbarque.setFont(new java.awt.Font("Arial", 0, 12));

        jLabel10.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel10.setText("Cargas do navio:");

        cmbCargasNavio.setFont(new java.awt.Font("Arial", 0, 12));
        cmbCargasNavio.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbCargasNavioItemStateChanged(evt);
            }
        });

        txtCorProdutoCargaNavio.setEditable(false);
        txtCorProdutoCargaNavio.setFont(new java.awt.Font("Arial", 0, 12));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel1.setText("Porão:");

        txtNumeroPorao.setFont(new java.awt.Font("Arial", 0, 12));

        javax.swing.GroupLayout pnlInformacoesCargasNavioLayout = new javax.swing.GroupLayout(pnlInformacoesCargasNavio);
        pnlInformacoesCargasNavio.setLayout(pnlInformacoesCargasNavioLayout);
        pnlInformacoesCargasNavioLayout.setHorizontalGroup(
            pnlInformacoesCargasNavioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInformacoesCargasNavioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInformacoesCargasNavioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInformacoesCargasNavioLayout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbCargasNavio, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtCorProdutoCargaNavio, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlInformacoesCargasNavioLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNumeroPorao, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbNavioAtracado, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtOrientacaoEmbarque, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnlInformacoesCargasNavioLayout.setVerticalGroup(
            pnlInformacoesCargasNavioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInformacoesCargasNavioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInformacoesCargasNavioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtNumeroPorao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(txtOrientacaoEmbarque, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbNavioAtracado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addGroup(pnlInformacoesCargasNavioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(cmbCargasNavio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCorProdutoCargaNavio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cmdDesistir.setFont(new java.awt.Font("Arial", 1, 14));
      //  cmdDesistir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icones/exit.png"))); // NOI18N
        cmdDesistir.setText("Desistir");
        cmdDesistir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdDesistirActionPerformed(evt);
            }
        });

        cmdConfirmar.setFont(new java.awt.Font("Arial", 1, 14));
       // cmdConfirmar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icones/ok.png"))); // NOI18N
        cmdConfirmar.setText("Confirmar");
        cmdConfirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdConfirmarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlInformacoesUsina, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(cmdDesistir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmdConfirmar))
                    .addComponent(pnlInformacoesCargasNavio, javax.swing.GroupLayout.DEFAULT_SIZE, 615, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlInformacoesUsina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlInformacoesCargasNavio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdConfirmar)
                    .addComponent(cmdDesistir))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Metodo que carrega o combo de produto produzido
     * com os produtos produzido pela usina no momento da
     * recuperacao
     */
    private void montaComboProdutoProduzido(Date data) {
        // ... limpando os itens antes de iniciar a adicao
        
        mapComboCampanha = new HashMap<Integer,Campanha>();
        cmbProdutoProduzido.removeAllItems();
        List<Campanha> campanhas = new ArrayList<Campanha>();
        if (interfaceUsina != null) {
            campanhas = interfaceUsina.getUsinaVisualizada().getMetaUsina().getCampanhas(data);
        } else {
            for (InterfaceUsina interfaceUsina : controladorDSP.getInterfaceDSP().getListaUsinas()) {
                if (interfaceUsina.getUsinaVisualizada().getMetaUsina().getFiltragemOrigem() != null 
                                && interfaceUsina.getUsinaVisualizada().getMetaUsina().getCampanhaAtual(data) != null) {                        
                    campanhas = interfaceUsina.getUsinaVisualizada().getMetaUsina().getCampanhas(data);
                    break;
                }
            }    
        }        
        if (campanhas != null) {
            // ... adicionando os produtos produzido no combo
            Integer index = 0;
            if (filtragemEditada != null) {                 
               for (Campanha campanha : campanhas) {
                   cmbProdutoProduzido.addItem(campanha.getTipoPellet());
                   mapComboCampanha.put(index, campanha);
                   index++;
               }    
               
            } else {                
                for (Campanha campanha : campanhas) {
                    cmbProdutoProduzido.addItem(campanha.getTipoProduto());
                    mapComboCampanha.put(index, campanha);
                    index++;
                }    
            }            
        }    
    }

    public Boolean getOperacaoCanceladaPeloUsuario() {
        return operacaoCanceladaPeloUsuario;
    }

   private void cmbNavioAtracadoItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_cmbNavioAtracadoItemStateChanged
   {//GEN-HEADEREND:event_cmbNavioAtracadoItemStateChanged
       InterfaceNavio interfaceNavioSelecionado = (InterfaceNavio) cmbNavioAtracado.getSelectedItem();
       montaComboCargasNavio(interfaceNavioSelecionado);
   }//GEN-LAST:event_cmbNavioAtracadoItemStateChanged

   private void cmbCargasNavioItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_cmbCargasNavioItemStateChanged
   {//GEN-HEADEREND:event_cmbCargasNavioItemStateChanged
       InterfaceCarga interfaceCargaSelecionada = (InterfaceCarga) cmbCargasNavio.getSelectedItem();
       if (interfaceCargaSelecionada != null) {
           String[] rgbProdutoCargaSelecionada = interfaceCargaSelecionada.getCargaVisualizada().getProduto().getTipoProduto().getCorIdentificacao().split(",");
           txtCorProdutoCargaNavio.setBackground(new Color(Integer.parseInt(rgbProdutoCargaSelecionada[0]), Integer.parseInt(rgbProdutoCargaSelecionada[1]), Integer.parseInt(rgbProdutoCargaSelecionada[2])));
           txtOrientacaoEmbarque.setText(interfaceCargaSelecionada.getCargaVisualizada().getOrientacaoDeEmbarque().getTipoProduto().toString());
       }
   }//GEN-LAST:event_cmbCargasNavioItemStateChanged

   private void cmdDesistirActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmdDesistirActionPerformed
   {//GEN-HEADEREND:event_cmdDesistirActionPerformed
       operacaoCanceladaPeloUsuario = true;
       setVisible(false);
   }//GEN-LAST:event_cmdDesistirActionPerformed

   private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
   {//GEN-HEADEREND:event_formWindowClosing
       operacaoCanceladaPeloUsuario = true;
   }//GEN-LAST:event_formWindowClosing

   private void cmbProdutoProduzidoItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_cmbProdutoProduzidoItemStateChanged
   {//GEN-HEADEREND:event_cmbProdutoProduzidoItemStateChanged
       // ... obtem o tipo produto selecionado do combo de produto produzido da usina
       TipoProduto tipoProdutoUsina = (TipoProduto) cmbProdutoProduzido.getSelectedItem();
       if (tipoProdutoUsina != null) {
           if (!mapComboCampanha.isEmpty()) {
               Campanha c = mapComboCampanha.get(cmbProdutoProduzido.getSelectedIndex());
               carregarDadosCampanha(c);
               //  ... pinta a caixa de texto com a cor do produto selecionado
               String[] rgbProdutoProduzidoUsina = tipoProdutoUsina.getCorIdentificacao().split(",");
               txtCorProdutoProduzido.setBackground(new Color(Integer.parseInt(rgbProdutoProduzidoUsina[0]), Integer.parseInt(rgbProdutoProduzidoUsina[1]), Integer.parseInt(rgbProdutoProduzidoUsina[2])));
           }    
       }    
   }//GEN-LAST:event_cmbProdutoProduzidoItemStateChanged

   private void cmdConfirmarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmdConfirmarActionPerformed
   {//GEN-HEADEREND:event_cmdConfirmarActionPerformed
       try {
    	   Date dataInicioAtividade = null;    	   
           validarAtividadeAtualizacaoRecuperacaoUsina();
           Date dataHoraOcorrenciaEvento = null;
           Date dataFinalAtividade = null;
           if(segundoAcesso){
        	   dataFinalAtividade = validaDataFinal();
        	   dataInicioAtividade = atividadeExecutada.getDtInicio(); 
               dataHoraOcorrenciaEvento = DSSStockyardTimeUtil.criaDataComString(txtDataHoraFinalRecuperacao.getText(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
           }else{
        	   dataInicioAtividade =  validaDataInicial();
               dataHoraOcorrenciaEvento = DSSStockyardTimeUtil.criaDataComString(txtDataHoraInicioRecuperacao.getText(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
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
                  if (txtDataHoraInicioRecuperacao.isEnabled())
                  {
                     txtDataHoraInicioRecuperacao.requestFocus();
                  }
                  else
                  {
                     txtDataHoraFinalRecuperacao.requestFocus();
                  }
                  return;
               }
            }
            if (usinaEditada != null) {
                List<MaquinaDoPatio> maquinas = controladorDSP.getInterfaceInicial()
                                .verificarEmpilhamentoEmAndamentoNaUsina(usinaEditada, dataInicioAtividade);
                if (maquinas.size() > 0) {
                    StringBuilder strMessage = new StringBuilder(PropertiesUtil
                                    .getMessage("aviso.encerrar.recuperacao.usina")
                                    + "\n");
                    for (MaquinaDoPatio maquina : maquinas) {
                        strMessage.append(maquina.getAtividade().getTipoAtividade().toString());
                        strMessage.append(" - ");
                        strMessage.append(maquina.getNomeMaquina());
                        strMessage.append("\n");
                    }
                    strMessage.append(PropertiesUtil.getMessage("aviso.encerrar.recuperacao.usina.continuar"));

                    int confirm = JOptionPane.showOptionDialog(null, strMessage.toString(), PropertiesUtil
                                    .getMessage("popup.atencao"), JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE,
                                    null, null, null);

                    if (confirm == JOptionPane.YES_OPTION) {
                        encerrarEmpilhamentosEmExecucaoDaUsina(maquinas, dataInicioAtividade);
                        dataInicioAtividade = validaDataInicial();
                    }
                }
            } else if (filtragemEditada != null) {
                List<MaquinaDoPatio> maquinas = controladorDSP.getInterfaceInicial()
                                .verificarEmpilhamentoEmAndamentoNaFiltragem(filtragemEditada, dataInicioAtividade);
                if (maquinas.size() > 0) {
                    StringBuilder strMessage = new StringBuilder(PropertiesUtil
                                    .getMessage("aviso.encerrar.recuperacao.usina")
                                    + "\n");
                    for (MaquinaDoPatio maquina : maquinas) {
                        strMessage.append(maquina.getAtividade().getTipoAtividade().toString());
                        strMessage.append(" - ");
                        strMessage.append(maquina.getNomeMaquina());
                        strMessage.append("\n");
                    }
                    strMessage.append(PropertiesUtil.getMessage("aviso.encerrar.recuperacao.usina.continuar"));

                    int confirm = JOptionPane.showOptionDialog(null, strMessage.toString(), PropertiesUtil
                                    .getMessage("popup.atencao"), JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE,
                                    null, null, null);

                    if (confirm == JOptionPane.YES_OPTION) {
                        encerrarEmpilhamentosEmExecucaoDaFiltragem(maquinas, dataInicioAtividade);
                        dataInicioAtividade = validaDataInicial();
                    }
                }

            }
            
            
            visibilidadeBotoes(false);
            
            InterfaceCarga interfaceCargaSelecionada = (InterfaceCarga) cmbCargasNavio.getSelectedItem();
            TipoProduto tipoProdutoSelecionado = (TipoProduto) cmbProdutoProduzido.getSelectedItem();            
            Campanha campanha = mapComboCampanha.get(cmbProdutoProduzido.getSelectedIndex());
            
            if (validarCampanhaDiferente(campanha)) {
                visibilidadeBotoes(true);
                return;
            }
            
            controladorDSP.getInterfaceInicial().atualizaRecuperacaoUsina(
            		usinaEditada,filtragemEditada, interfaceCargaSelecionada.getCargaVisualizada(),
            		tipoProdutoSelecionado, txtNumeroPorao.getText(),
            		dataFinalAtividade,dataInicioAtividade,atividadeExecutada,campanha);

           operacaoCanceladaPeloUsuario = Boolean.FALSE;           
           visibilidadeBotoes(true);
           // chegando ate o JDialog
            setVisible(false);
       } catch (ValidacaoCampoException vcEx) {
    	   visibilidadeBotoes(true);
    	   
           controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
           interfaceMensagem = new InterfaceMensagem();
           interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
           interfaceMensagem.setDescricaoMensagem(vcEx.getMessage());
           controladorDSP.ativarMensagem(interfaceMensagem);
       } catch (ValidacaoObjetosOperacaoException ex) {
    	   visibilidadeBotoes(true);
    	   
           controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
           interfaceMensagem = new InterfaceMensagem();
           interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
           interfaceMensagem.setDescricaoMensagem(ex.getMessage());
           controladorDSP.ativarMensagem(interfaceMensagem);
       } catch (AtividadeException e) {
        // TODO Auto-generated catch block
           visibilidadeBotoes(true);
           
           controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
           interfaceMensagem = new InterfaceMensagem();
           interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
           interfaceMensagem.setDescricaoMensagem(e.getMessage());
           controladorDSP.ativarMensagem(interfaceMensagem);
    }

   }//GEN-LAST:event_cmdConfirmarActionPerformed

   private void txtDataHoraInicioRecuperacaoTextChanged() {//GEN-FIRST:event_txtDataHoraInicioRecuperacaoInputMethodTextChanged
       try {
        if (txtDataHoraInicioRecuperacao.getText() != "") {
            Date dataHoraOcorrenciaEvento = DSSStockyardTimeUtil.criaDataComString(txtDataHoraInicioRecuperacao.getText(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
            carregaDadosIniciais(txtDataHoraInicioRecuperacao.getText());
            montaComboProdutoProduzido(dataHoraOcorrenciaEvento);            
        }
       } catch (ValidacaoCampoException e) {
           visibilidadeBotoes(true);           
           controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
           interfaceMensagem = new InterfaceMensagem();
           interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
           interfaceMensagem.setDescricaoMensagem(e.getMessage());
           controladorDSP.ativarMensagem(interfaceMensagem);
    }
       
       
       // TODO add your handling code here:
   }//GEN-LAST:event_txtDataHoraInicioRecuperacaoInputMethodTextChanged

   private void txtDataHoraFimRecuperacaoTextChanged() {//GEN-FIRST:event_txtDataHoraInicioRecuperacaoInputMethodTextChanged
       try {
        if (txtDataHoraFinalRecuperacao.getText() != "") {
            Date dataHoraOcorrenciaEvento = DSSStockyardTimeUtil.criaDataComString(txtDataHoraFinalRecuperacao.getText(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
            carregaDadosIniciais(txtDataHoraFinalRecuperacao.getText());
            montaComboProdutoProduzido(dataHoraOcorrenciaEvento);
            //txtDataHoraFinalRecuperacao.requestFocus();
        }
       } catch (ValidacaoCampoException e) {
           visibilidadeBotoes(true);           
           controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
           interfaceMensagem = new InterfaceMensagem();
           interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
           interfaceMensagem.setDescricaoMensagem(e.getMessage());
           controladorDSP.ativarMensagem(interfaceMensagem);
    }
       
       
       // TODO add your handling code here:
   }//GEN-LAST:event_txtDataHoraInicioRecuperacaoInputMethodTextChanged

   
   /**
    * Metodo que encerra os empilhamentos em operacao das maquinas informadas
    */
   private void encerrarEmpilhamentosEmExecucaoDaUsina(List<MaquinaDoPatio> maquinas, Date dataAtividade)
   throws ValidacaoObjetosOperacaoException {

	   try {
		   for(MaquinaDoPatio maquina : maquinas) {
		       dataAtividade = Atividade.verificaAtualizaDataAtividade(dataAtividade, controladorDSP.getInterfaceInicial().getPlanejamento().getControladorDePlano().obterSituacaoPatioFinal().getDtInicio());
			   controladorDSP.getInterfaceInicial().finalizarAtualizacaoEmpilhamento(maquina.getAtividade(), dataAtividade);
					   
		   }
	   } catch (AtividadeException e) {
		   e.printStackTrace();
		   throw new ValidacaoObjetosOperacaoException(e);
	   }

   }
    
   
   /**
    * Metodo que encerra os empilhamentos em operacao das maquinas informadas
    */
   private void encerrarEmpilhamentosEmExecucaoDaFiltragem(List<MaquinaDoPatio> maquinas, Date dataAtividade)
   throws ValidacaoObjetosOperacaoException {

       try {
           for(MaquinaDoPatio maquina : maquinas) {
               dataAtividade = Atividade.verificaAtualizaDataAtividade(dataAtividade, controladorDSP.getInterfaceInicial().getPlanejamento().getControladorDePlano().obterSituacaoPatioFinal().getDtInicio());
               controladorDSP.getInterfaceInicial().finalizarAtualizacaoEmpilhamento(maquina.getAtividade(), dataAtividade);
                       
           }
       } catch (AtividadeException e) {
           e.printStackTrace();
           throw new ValidacaoObjetosOperacaoException(e);
       }

   }
   /**
     * Metodo que valida as informacoes digitadas pelo usuario para uma atividade de atualizacao
     * de recuperacao
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.ValidacaoCampoException
     */
    private void validarAtividadeAtualizacaoRecuperacaoUsina() throws ValidacaoCampoException {

        if (txtNumeroPorao.getText().trim().equals("")) {
            throw new ValidacaoCampoException(PropertiesUtil.getMessage("mensagem.numero.porao.nao.info"));
        }
        if(cmbNavioAtracado.getSelectedItem() == null || cmbNavioAtracado.getItemCount() == 0 )
        {//verifica se existe algum navio carregado no combo
        	throw new ValidacaoCampoException(PropertiesUtil.getMessage("aviso.navio.para.recuperacao.nao.encontrado"));
        }
        else if(cmbCargasNavio.getSelectedItem() == null || cmbCargasNavio.getItemCount() == 0 )
        {//verifica se existe carga para este navio
        	throw new ValidacaoCampoException(PropertiesUtil.getMessage("aviso.carga.para.navio.nao.encontrada"));
        }

        // ... obtem o produto da carga selecionada do navio atracado selecionado
        Produto produtoDaCarga = ((InterfaceCarga) cmbCargasNavio.getSelectedItem()).getCargaVisualizada().getProduto();
        // ... obtem a orientacao de embarque da carga selecionada do navio atracado selecionado
        OrientacaoDeEmbarque orientacaoEmbarque = ((InterfaceCarga) cmbCargasNavio.getSelectedItem()).getCargaVisualizada().getOrientacaoDeEmbarque();

        // ... obtem o produto da usina selecionada para recuperacao
        TipoProduto produtoUsinaSelecionado = (TipoProduto) cmbProdutoProduzido.getSelectedItem();
        
        if ((usinaEditada != null && usinaEditada.getAtividade() == null) || (filtragemEditada != null && filtragemEditada.getAtividade() == null)) {
            // ... valida se o produto da baliza selecionada eh compatível com o produto da carga seleciondada
            if (produtoUsinaSelecionado != null && !produtoUsinaSelecionado.getIdTipoProduto().equals(produtoDaCarga.getTipoProduto().getIdTipoProduto())) {
                JLabel pergunta = new JLabel(PropertiesUtil.getMessage("mensagem.produto.incompativel.usina"));
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

                if (confirm != JOptionPane.YES_OPTION) {
                    throw new ValidacaoCampoException(PropertiesUtil.getMessage("mensagem.produto.carga.incompativel.usina"));
                } else if (confirm == JOptionPane.YES_OPTION) {
                    Carga carga = ((InterfaceCarga) cmbCargasNavio.getSelectedItem()).getCargaVisualizada();
                    List<String> listaTipoProduto = new ArrayList<String>();
                    listaTipoProduto.add(produtoUsinaSelecionado.toString());
                    controladorDSP.verificaEGerarLogBlendNaAtualizacao(listaTipoProduto, carga);
                }
            }

            if (txtNumeroPorao.getText().trim().equals("")) {
                throw new ValidacaoCampoException(PropertiesUtil.getMessage("mensagem.numero.porao.nao.info"));
            }
        } 
    }

    /**
     * Monta o combo com todos os navios atracados
     */
    private void montaComboNaviosAtracado() {
        cmbNavioAtracado.setEnabled(true);
        cmbNavioAtracado.removeAllItems();
        for (InterfacePier interfacePier : controladorDSP.getInterfaceInicial().getInterfaceInicial().getInterfaceFilaDeNavios().getListaDePiers()) {
            for (InterfaceBerco interfaceBerco : interfacePier.getListaDeBercos()) {
                if (interfaceBerco.getNavioAtendido() != null) {
                    cmbNavioAtracado.addItem(interfaceBerco.getNavioAtendido());
                }
            }
        }
    }

    /**
     * Monta o combo com todas as cargas do navio atracado selecionado para
     * atividade de atualizacao recuperacao
     */
    private void montaComboCargasNavio(InterfaceNavio interfaceNavio) {
        cmbCargasNavio.removeAllItems();
        for (InterfaceCarga interfaceCarga : interfaceNavio.getListaDecarga()) {
            cmbCargasNavio.addItem(interfaceCarga);
        }
    }

    /**
     * Carrega algums dados iniciais para inicializacao da atividade
     * de recuperacao
     */
    private void carregaDadosIniciais(String value) {
        Campanha campanha = null; 
        Date dataInicial = null;
        try {
            dataInicial = DSSStockyardTimeUtil.criaDataComString( value , PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
        } catch (ValidacaoCampoException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (filtragemEditada != null) {
            txtNomeUsina.setText(interfaceFiltragem.getFiltragemVisualizada().getMetaFiltragem().getNomeFiltragem());
            txtTaxaOperacaoUsina.setText(DSSStockyardFuncoesNumeros.getQtdeFormatada(interfaceFiltragem.getUsinaVisualizada().getTaxaDeOperacao(), 2));
            campanha = interfaceFiltragem.getUsinaVisualizada().getMetaUsina().getCampanhaAtual(dataInicial);
        } else {       
            txtNomeUsina.setText(interfaceUsina.getUsinaVisualizada().getNomeUsina());
            txtTaxaOperacaoUsina.setText(DSSStockyardFuncoesNumeros.getQtdeFormatada(usinaEditada.getTaxaDeOperacao(), 2));
            campanha = usinaEditada.getMetaUsina().getCampanhaAtual(dataInicial);
        }    
        if (campanha != null) {     
         carregarDadosCampanha(campanha);
        } else {
            visibilidadeBotoes(true);            
            controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            interfaceMensagem.setDescricaoMensagem("Não existe campanha disponível no período !");
            controladorDSP.ativarMensagem(interfaceMensagem);            
        }
        txtDataHoraInicioRecuperacao.requestFocus();       
    }

    private void initDatas() {
        txtDataHoraFinalRecuperacao.setEnabled(false);
        txtDataHoraInicioRecuperacao.setText(DSSStockyardTimeUtil.formatarData(DSSStockyardTimeUtil.obterDataHoraUltimaSituacaoParaAtividades(controladorDSP.getInterfaceInicial().getInterfaceInicial().getSituacaoPatioExibida().getDtInicio()), PropertiesUtil.buscarPropriedade("formato.campo.datahora")));
        txtDataHoraInicioRecuperacao.setSelectionStart(11); // comeca a selecao no inicio da hora
        txtDataHoraInicioRecuperacao.setSelectionEnd(txtDataHoraInicioRecuperacao.getText().length());
    }

    private void carregarDadosCampanha(Campanha c) {                   
            txtDataInicialCampanha.setText(DSSStockyardTimeUtil.formatarData(c.getDataInicial(), PropertiesUtil.buscarPropriedade("formato.campo.datahora")));
            txtDataFinalCampanha.setText(DSSStockyardTimeUtil.formatarData(c.getDataFinal(), PropertiesUtil.buscarPropriedade("formato.campo.datahora")));
            txtNomeCampanha.setText(c.getNomeCampanha());           
    }

    private void carregarInformacoesAtividade() 
    {
        //muda valor de controle para este acesso de encerramento de atividade
        segundoAcesso = Boolean.TRUE;
        txtDataHoraInicioRecuperacao.setText(DSSStockyardTimeUtil.formatarData(atividadeExecutada.getDtInicio(), PropertiesUtil.buscarPropriedade("formato.campo.datahora")));
        txtDataHoraInicioRecuperacao.setEnabled(false);
        txtDataHoraFinalRecuperacao.setEnabled(true);
        txtDataHoraFinalRecuperacao.setText(DSSStockyardTimeUtil.formatarData(DSSStockyardTimeUtil.obterDataHoraUltimaSituacaoParaAtividades(controladorDSP.getInterfaceInicial().getInterfaceInicial().getSituacaoPatioExibida().getDtInicio()), PropertiesUtil.buscarPropriedade("formato.campo.datahora")));
        txtDataHoraFinalRecuperacao.setSelectionStart(11); // comeca a selecao no inicio da hora
        txtDataHoraFinalRecuperacao.setSelectionEnd(txtDataHoraFinalRecuperacao.getText().length());
        txtDataHoraFinalRecuperacao.requestFocus();

        cmbCargasNavio.setEnabled(false);
        txtNumeroPorao.setEnabled(false);
        txtNumeroPorao.setText(atividadeExecutada.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getNomePorao());

        // ... atualizando o combo do navio atracado com as informacoes da atividade que
        // ... jah esta em execucao
        cmbNavioAtracado.setEnabled(false);
        for (int i = 0; i < cmbNavioAtracado.getItemCount(); i++) {
            InterfaceNavio interfaceNavio = (InterfaceNavio) cmbNavioAtracado.getItemAt(i);
            if (interfaceNavio.getNavioVisualizado().getNomeNavio().equals(atividadeExecutada.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaCargas().get(0).getNavio(atividadeExecutada.getDtInicio()).getNomeNavio()) && interfaceNavio.getNavioVisualizado().getDiaDeChegada().equals(atividadeExecutada.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaCargas().get(0).getNavio(atividadeExecutada.getDtInicio()).getDiaDeChegada())) {
                cmbNavioAtracado.setSelectedIndex(i);
                break;
            }
        }

        // ... atualizando o combo de cargas do navio atracado com as informacoes da atividade que
        // ... jah esta em execucao
        for (int i = 0; i < cmbCargasNavio.getItemCount(); i++) {
            InterfaceCarga interfaceCarga = (InterfaceCarga) cmbCargasNavio.getItemAt(i);
            if (interfaceCarga.getCargaVisualizada().getIdentificadorCarga().equals(atividadeExecutada.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaCargas().get(0).getIdentificadorCarga()) && interfaceCarga.getCargaVisualizada().getOrientacaoDeEmbarque().getTipoProduto().getDescricaoTipoProduto().equals(atividadeExecutada.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaCargas().get(0).getOrientacaoDeEmbarque().getTipoProduto().getDescricaoTipoProduto())) {
                cmbCargasNavio.setSelectedIndex(i);
                break;
            }
        }
    }

    /**
     * valida se a data final da atividade não é menor que a data inicial ou menor que a data da ultima situação de patio
     */
    private Date validaDataFinal() throws ValidacaoCampoException
    {
        Date dataInicial = DSSStockyardTimeUtil.criaDataComString( txtDataHoraInicioRecuperacao.getText() , PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
        Date dataFinal = DSSStockyardTimeUtil.criaDataComString( txtDataHoraFinalRecuperacao.getText() , PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
        if(dataFinal.before(dataInicial)){
            throw new ValidacaoCampoException(PropertiesUtil.getMessage("aviso.data.periodo.final.deve.ser.maior.que.periodo.inicial"));
        }
        dataFinal = Atividade.verificaAtualizaDataAtividade(dataFinal, controladorDSP.getInterfaceInicial().getPlanejamento().getControladorDePlano().obterSituacaoPatioFinal().getDtInicio());
        if(dataFinal.before(controladorDSP.getInterfaceInicial().getPlanejamento().getControladorDePlano().obterSituacaoPatioFinal().getDtInicio()) ){
            throw new ValidacaoCampoException(PropertiesUtil.getMessage("aviso.data.final.menor.ultima.situacao.patio"));
        }
        return dataFinal;
    }

    private Date validaDataInicial() throws ValidacaoCampoException
    {
        Date dataInicial = DSSStockyardTimeUtil.criaDataComString( txtDataHoraInicioRecuperacao.getText() , PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
        dataInicial = Atividade.verificaAtualizaDataAtividade(dataInicial, controladorDSP.getInterfaceInicial().getPlanejamento().getControladorDePlano().obterSituacaoPatioFinal().getDtInicio());
    	        
        if (!controladorDSP.validaDAtaMenorUltimaSituacaoPatio(dataInicial))
        {
            throw new ValidacaoCampoException(PropertiesUtil.getMessage("aviso.data.inicial.menor.ultima.situacao.patio"));
        }
        return dataInicial;
    }
    
    /**
     * modifica o status dos botoes "enabled" para true ou false
     * @param visivel
     */
    private void visibilidadeBotoes(boolean visivel){
 	   this.cmdConfirmar.setEnabled(visivel);
 	   this.cmdDesistir.setEnabled(visivel);
    }

    /**
     * validarCampanhaDiferente
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 02/12/2010
     * @see
     * @param 
     * @return void
     */
    private Boolean validarCampanhaDiferente(Campanha campanhaAtual) {
        Boolean campanhaDiferente = Boolean.FALSE;
        StringBuilder campanhasUsinas = new StringBuilder("Campanhas diferentes !!\n");
        if (atividadeExecutada != null) {
            List<AtividadeCampanha> atvCampanha = atividadeExecutada.getListaDeAtividadesCampanha(); 
           if (usinaEditada != null) {
                                                                    
                    if (campanhaAtual != null) {                                    
                        for(AtividadeCampanha atvCmp : atvCampanha) {
                            if (atvCmp.getNomeUsina().equals(usinaEditada.getNomeUsina()) && !atvCmp.getCampanha().equals(campanhaAtual)) {
                                campanhasUsinas.append("A Campanha selecionada : " + campanhaAtual.getNomeCampanha() + " para a Usina : " + usinaEditada.getNomeUsina() + " é diferente da campanha " + atvCmp.getCampanha().getNomeCampanha() + " selecionada no inicio da atividade ! \n" );
                                campanhaDiferente = Boolean.TRUE;
                            } 
                        }    
                    }
                
           }
            if (filtragemEditada != null) {
                    if (campanhaAtual != null) {                                    
                        for(AtividadeCampanha atvCmp : atvCampanha) {
                            if (atvCmp.getNomeUsina().equals(campanhaAtual.getMetaUsina().getNomeUsina()) && !atvCmp.getCampanha().equals(campanhaAtual)) {
                                campanhasUsinas.append("A Campanha selecionada : " + campanhaAtual.getNomeCampanha() + " para a Filtragem : " + filtragemEditada.getMetaFiltragem().getNomeFiltragem() + " é diferente da campanha " + atvCmp.getCampanha().getNomeCampanha() + " selecionada no inicio da atividade ! \n" );
                                campanhaDiferente = Boolean.TRUE;
                            } 
                        }    
                    }
                }            
        }
        if (campanhaDiferente) {
            JLabel pergunta = new JLabel(campanhasUsinas.toString());
            pergunta.setFont(new Font("Tahoma", Font.PLAIN, 11));
            JOptionPane.showOptionDialog(null,
                            //pergunta,
                            campanhasUsinas.toString(), PropertiesUtil.getMessage("popup.atencao"),
                            JOptionPane.CLOSED_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
        }
    
        return campanhaDiferente;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbCargasNavio;
    private javax.swing.JComboBox cmbNavioAtracado;
    private javax.swing.JComboBox cmbProdutoProduzido;
    private javax.swing.JButton cmdConfirmar;
    private javax.swing.JButton cmdDesistir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel pnlInformacoesCampanha;
    private javax.swing.JPanel pnlInformacoesCargasNavio;
    private javax.swing.JPanel pnlInformacoesUsina;
    private javax.swing.JTextField txtCorProdutoCargaNavio;
    private javax.swing.JTextField txtCorProdutoProduzido;
    private javax.swing.JTextField txtDataFinalCampanha;
    private javax.swing.JFormattedTextField txtDataHoraFinalRecuperacao;
    private javax.swing.JFormattedTextField txtDataHoraInicioRecuperacao;
    private javax.swing.JTextField txtDataInicialCampanha;
    private javax.swing.JTextField txtNomeCampanha;
    private javax.swing.JTextField txtNomeUsina;
    private javax.swing.JTextField txtNumeroPorao;
    private javax.swing.JTextField txtOrientacaoEmbarque;
    private javax.swing.JTextField txtTaxaOperacaoUsina;
    // End of variables declaration//GEN-END:variables
}
