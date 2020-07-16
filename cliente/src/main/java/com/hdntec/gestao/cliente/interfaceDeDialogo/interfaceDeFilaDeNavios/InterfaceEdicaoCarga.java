/*
 * InterfaceEdicaoCarga.java
 *
 * Created on 28/01/2009, 08:31:01
 */

package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios;

import java.awt.Color;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.cliente.util.tabela.ColunaTabela;
import com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable;
import com.hdntec.gestao.cliente.util.telas.DSSStockyardTelaUtil;
import com.hdntec.gestao.domain.navios.entity.status.Carga;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.ValidacaoCampoException;
import com.hdntec.gestao.util.PropertiesUtil;

/**
 * Interface GUI que apresenta e os dados da Carga e seu respectivo Produto, do Navio selecionado que podem ser editados
 * @author bgomes
 */
public abstract class InterfaceEdicaoCarga extends javax.swing.JPanel {

    private Boolean operacaoCanceladaPeloUsuario = Boolean.FALSE;
    private List<ColunaTabela> listaColunas;
    private InterfaceNavio interfaceNavio;

    public static final int MODO_INSERIR = 1;
    public static final int MODO_EDITAR = 2;
    public static final int MODO_EXCLUIR = 3;
    public static final int OPERACAO_NULA = 4;
    private Integer operacaoSelecionada = null;
    private EdicaoDadosCarga edicaoDadosCarga = new EdicaoDadosCarga(null, this);
    private List<Carga> listaCargas;


    /** Creates new form InterfaceEdicaoCarga */
    public InterfaceEdicaoCarga(InterfaceNavio interfaceNavio, InterfaceEditaNavioNaFilaDeNavios interfaceEditaNavioNaFilaDeNavios, Integer modoOperacao, String identificador) throws ErroSistemicoException {
        //este set deve ficar antes do initComponents, pois dentro dos metodos chamados nos Action, procuram dados a partir deste controlador
        edicaoDadosCarga.setControladorInterfaceFilaDeNavios(interfaceNavio.getControladorInterfaceFilaDeNavios());
        edicaoDadosCarga.setInterfaceEditaNavioNaFilaDeNavios(interfaceEditaNavioNaFilaDeNavios);
        initComponents();
        if (modoOperacao == MODO_INSERIR) {
            btnExcluirCarga.setEnabled(false);
        }

        this.interfaceNavio = interfaceNavio;
        criaColunas();
        atualizaCampos();
        listaCargas = interfaceEditaNavioNaFilaDeNavios.getListaCargas();

        this.operacaoSelecionada = modoOperacao;

        try
        {
           if (operacaoSelecionada == InterfaceEdicaoCarga.MODO_EDITAR)
              edicaoDadosCarga.atualizaDadosCarga(identificador);
           else
              edicaoDadosCarga.atualizaProdutoValorZero();
        } catch (ParseException ex) {
            Logger.getLogger(InterfaceEdicaoCarga.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(InterfaceEdicaoCarga.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

     private void criaColunas() {
        listaColunas = new ArrayList<ColunaTabela>();
        ColunaTabela coluna;

        //Coluna descricao de tipo de item de controle.
        coluna = new ColunaTabela();
        coluna.setLargura(200);
        coluna.setTitulo(PropertiesUtil.getMessage("label.tipocontrole"));
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

        //Coluna desvio valor padrao.
        /*coluna = new ColunaTabela();
        coluna.setLargura(100);
        coluna.setTitulo(PropertiesUtil.getMessage("mensagem.coluna.tabela.devio.padrao.valor"));
        coluna.setAlinhamento(SwingConstants.LEFT);
        coluna.setRedimensionar(true);
        coluna.setEditar(true);
        coluna.setTipoEditor(ColunaTabela.COL_TIPO_TEXT);
        listaColunas.add(coluna);*/

        //Coluna Unidade de medida do item de controle.
        coluna = new ColunaTabela();
        coluna.setLargura(100);
        coluna.setTitulo(PropertiesUtil.getMessage("mensagem.coluna.tabela.unidade"));
        coluna.setAlinhamento(SwingConstants.LEFT);
        coluna.setRedimensionar(true);
        coluna.setEditar(false);
        listaColunas.add(coluna);

    }

    private void atualizaCampos() throws ErroSistemicoException {
        jbConfirmar.setEnabled(true);
        //atualiza combo do tipoDeProduto
        getJcbTipoProduto().removeAllItems();
        for (TipoProduto tipoProduto : interfaceNavio.getControladorInterfaceFilaDeNavios().getInterfaceFilaDeNavios().getInterfaceInicial().getListaTiposProduto()) {
            getJcbTipoProduto().addItem(tipoProduto);
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

        grpOperacao = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        lblNomeCarga = new javax.swing.JLabel();
        txtNomeCarga = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        lblQtdEmbarcada = new javax.swing.JLabel();
        txtQtdEmbarcada = new javax.swing.JTextField();
        lblDescricao = new javax.swing.JLabel();
        jtfDescricao = new javax.swing.JTextField();
        lblTipoProduto = new javax.swing.JLabel();
        jcbTipoProduto = new javax.swing.JComboBox();
        lblQtdContratada = new javax.swing.JLabel();
        txtQtdContratada = new javax.swing.JTextField();
        jspItensDeControle = new javax.swing.JScrollPane();
        tblItensDeControle = new com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable();
        jbConfirmar = new javax.swing.JButton();
        cmdDesistir = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        btnExcluirCarga = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createTitledBorder(PropertiesUtil.getMessage("mensagem.carga.informacoes")));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        lblNomeCarga.setText(PropertiesUtil.getMessage("mensagem.carga"));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(lblNomeCarga)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtNomeCarga, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(404, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNomeCarga)
                    .addComponent(txtNomeCarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(PropertiesUtil.getMessage("mensagem.produto.embarcado")));

        lblQtdEmbarcada.setText(PropertiesUtil.getMessage("mensagem.quantidade.embarcada"));

        txtQtdEmbarcada.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtQtdEmbarcadaKeyReleased(evt);
            }
        });

        lblDescricao.setText(PropertiesUtil.getMessage("mensagem.coluna.tabela.descricao"));

        jtfDescricao.setEnabled(false);

        lblTipoProduto.setText(PropertiesUtil.getMessage("label.tipo.produto"));

        jcbTipoProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbTipoProdutoActionPerformed(evt);
            }
        });

        lblQtdContratada.setText(PropertiesUtil.getMessage("mensagem.quantidade.contratada"));

        txtQtdContratada.setEnabled(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblQtdEmbarcada)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtQtdEmbarcada, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblQtdContratada)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtQtdContratada, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)))
                .addGap(43, 43, 43)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDescricao)
                    .addComponent(lblTipoProduto))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jcbTipoProduto, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jtfDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(55, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblQtdEmbarcada, 0, 0, Short.MAX_VALUE)
                    .addComponent(txtQtdEmbarcada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblQtdContratada)
                    .addComponent(txtQtdContratada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, 43))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDescricao))
                .addGap(8, 8, 8)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTipoProduto)
                    .addComponent(jcbTipoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        tblItensDeControle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jspItensDeControle.setViewportView(tblItensDeControle);

        //jbConfirmar.setIcon(new javax.swing.ImageIcon(getClass().getResource(PropertiesUtil.buscarPropriedade("caminho.icone.confirmar")))); // NOI18N
        jbConfirmar.setText(PropertiesUtil.getMessage("botao.confirmar"));
        jbConfirmar.setPreferredSize(new java.awt.Dimension(113, 25));
        jbConfirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbConfirmarActionPerformed(evt);
            }
        });

        //cmdDesistir.setIcon(new javax.swing.ImageIcon(getClass().getResource(PropertiesUtil.buscarPropriedade("caminho.icone.desistir")))); // NOI18N
        cmdDesistir.setText(PropertiesUtil.getMessage("botao.desistir"));
        cmdDesistir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdDesistirActionPerformed(evt);
            }
        });

        //btnExcluirCarga.setIcon(new javax.swing.ImageIcon(getClass().getResource(PropertiesUtil.buscarPropriedade("caminho.icone.delete")))); // NOI18N
        btnExcluirCarga.setText(PropertiesUtil.getMessage("botao.excluir.carga"));
        btnExcluirCarga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirCargaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jspItensDeControle, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 542, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(cmdDesistir)
                        .addGap(13, 13, 13)
                        .addComponent(btnExcluirCarga)
                        .addGap(18, 18, 18)
                        .addComponent(jbConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 542, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jspItensDeControle, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdDesistir)
                    .addComponent(btnExcluirCarga))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jbConfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbConfirmarActionPerformed
        if (getOperacaoSelecionada() == MODO_EXCLUIR) {
            setOperacaoSelecionada(MODO_EDITAR);
        }
        if (edicaoDadosCarga.validaDados(interfaceNavio)) {
            this.operacaoCanceladaPeloUsuario = Boolean.FALSE;

            // chegando ate o JDialog
            DSSStockyardTelaUtil.getParentDialog(this).setVisible(false);
        }
    }//GEN-LAST:event_jbConfirmarActionPerformed

    private void cmdDesistirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDesistirActionPerformed
        setOperacaoSelecionada(OPERACAO_NULA);
        fecharJanela();
        // chegando ate o JDialog
        DSSStockyardTelaUtil.getParentDialog(this).setVisible(false);
}//GEN-LAST:event_cmdDesistirActionPerformed

    private void jcbTipoProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbTipoProdutoActionPerformed
        jtfDescricao.setText(((TipoProduto)jcbTipoProduto.getSelectedItem()).getDescricaoTipoProduto());

    }//GEN-LAST:event_jcbTipoProdutoActionPerformed

    private void txtQtdEmbarcadaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQtdEmbarcadaKeyReleased
        try{
            if(txtQtdEmbarcada.getText().length() > 15){
                txtQtdEmbarcada.setForeground(Color.RED);
                List<String> listaParametros = new ArrayList<String>();
                listaParametros.add(txtQtdEmbarcada.getText());
                listaParametros.add("15");
                throw new ValidacaoCampoException("exception.validacao.tamanho.de.campo", listaParametros);
            }else{
                txtQtdEmbarcada.setForeground(Color.BLACK);
            }
        }catch(ValidacaoCampoException ex){
        	interfaceNavio.getControladorInterfaceFilaDeNavios().getInterfaceInicial().desativarMensagemProcessamento();
            InterfaceMensagem interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            interfaceNavio.getControladorInterfaceFilaDeNavios().ativarMensagem(interfaceMensagem);
        }
    }//GEN-LAST:event_txtQtdEmbarcadaKeyReleased

    private void btnExcluirCargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirCargaActionPerformed
        this.operacaoSelecionada = MODO_EXCLUIR;
        edicaoDadosCarga.exclusaoCarga(); 
            this.operacaoCanceladaPeloUsuario = Boolean.FALSE;
            // chegando ate o JDialog
            DSSStockyardTelaUtil.getParentDialog(this).setVisible(false);
        
    }//GEN-LAST:event_btnExcluirCargaActionPerformed

    public JComboBox getJcbTipoProduto() {
        return jcbTipoProduto;
    }

    public JTextField getJtfDescricao() {
        return jtfDescricao;
    }

    public JTextField getJtfQuantidadeProduto() {
        return getTxtQtdEmbarcada();
    }

    public List<ColunaTabela> getListaColunas() {
        return listaColunas;
    }

    public InterfaceNavio getInterfaceNavio() {
        return interfaceNavio;
    }

    public CFlexStockyardJTable getTblItensDeControle() {
        return tblItensDeControle;
    }

    public Integer getOperacaoSelecionada() {
        return operacaoSelecionada;
    }

    public void setOperacaoSelecionada(Integer operacaoSelecionada) {
        this.operacaoSelecionada = operacaoSelecionada;
    }

    public Boolean getOperacaoCanceladaPeloUsuario() {
        return operacaoCanceladaPeloUsuario;
    }

    public void setOperacaoCanceladaPeloUsuario(Boolean operacaoCanceladaPeloUsuario) {
        this.operacaoCanceladaPeloUsuario = operacaoCanceladaPeloUsuario;
    }

    public JButton getJbCancelar() {
        return cmdDesistir;
    }

    public JButton getJbConfirmar() {
        return jbConfirmar;
    }

    public EdicaoDadosCarga getEdicaoDadosCarga() {
        return edicaoDadosCarga;
    }

    


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExcluirCarga;
    private javax.swing.JButton cmdDesistir;
    private javax.swing.ButtonGroup grpOperacao;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton jbConfirmar;
    private javax.swing.JComboBox jcbTipoProduto;
    private javax.swing.JScrollPane jspItensDeControle;
    private javax.swing.JTextField jtfDescricao;
    private javax.swing.JLabel lblDescricao;
    private javax.swing.JLabel lblNomeCarga;
    private javax.swing.JLabel lblQtdContratada;
    private javax.swing.JLabel lblQtdEmbarcada;
    private javax.swing.JLabel lblTipoProduto;
    private com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable tblItensDeControle;
    private javax.swing.JTextField txtNomeCarga;
    private javax.swing.JTextField txtQtdContratada;
    private javax.swing.JTextField txtQtdEmbarcada;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the txtNomeCarga
     */
    public javax.swing.JTextField getTxtNomeCarga() {
        return txtNomeCarga;
    }

    /**
     * @param jcbTipoProduto the jcbTipoProduto to set
     */
    public void setJcbTipoProduto(javax.swing.JComboBox jcbTipoProduto) {
        this.jcbTipoProduto = jcbTipoProduto;
    }

    /**
     * @param jtfDescricao the jtfDescricao to set
     */
    public void setJtfDescricao(javax.swing.JTextField jtfDescricao) {
        this.jtfDescricao = jtfDescricao;
    }

    /**
     * @param txtNomeCarga the txtNomeCarga to set
     */
    public void setTxtNomeCarga(javax.swing.JTextField txtNomeCarga) {
        this.txtNomeCarga = txtNomeCarga;
    }

    /**
     * @return the txtQtdContratada
     */
    public javax.swing.JTextField getTxtQtdContratada() {
        return txtQtdContratada;
    }

    /**
     * @return the txtQtdEmbarcada
     */
    public javax.swing.JTextField getTxtQtdEmbarcada() {
        return txtQtdEmbarcada;
    }

    /**
     * @return the listaCargas
     */
    public List<Carga> getListaCargas() {
        return listaCargas;
    }

    /**
     * @return the lblNomeCarga
     */
    public javax.swing.JLabel getLblNomeCarga() {
        return lblNomeCarga;
    }

    /**
     * @return the lblQtdContratada
     */
    public javax.swing.JLabel getLblQtdContratada() {
        return lblQtdContratada;
    }

    /**
     * @return the lblQtdEmbarcada
     */
    public javax.swing.JLabel getLblQtdEmbarcada() {
        return lblQtdEmbarcada;
    }

    /**
     * @return the lblTipoProduto
     */
    public javax.swing.JLabel getLblTipoProduto() {
        return lblTipoProduto;
    }

    
}
