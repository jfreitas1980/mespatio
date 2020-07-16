package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.SwingConstants;

import com.hdntec.gestao.cliente.util.numeros.DSSStockyardFuncoesNumeros;
import com.hdntec.gestao.cliente.util.tabela.CFlexStockyardFuncoesTabela;
import com.hdntec.gestao.cliente.util.tabela.ColunaTabela;
import com.hdntec.gestao.domain.navios.entity.status.Carga;
import com.hdntec.gestao.domain.navios.entity.status.Navio;
import com.hdntec.gestao.util.PropertiesUtil;
import com.hdntec.gestao.util.datahora.DSSStockyardTimeUtil;

public class InformacoesNavioAtracadoPnl extends javax.swing.JPanel {

    /** O navio atracado com as informacoes a serem preenchidas */
    Navio navioAtracado;

    /** a lista com as informacoes das colunas da tabela de lista de cargas */
    private List<ColunaTabela> listaColunas;

    /** Vetor com as informacoes das cargas */
    private Vector listaInformacoesCarga;

    private final Integer COL_PRODUTO = 0;
    private final Integer COL_QTDE_CONTRATADA = 1;
    private final Integer COL_QTDE_EMBARCADA = 2;
    private Date horaSituacao;


    /** Creates new form InformacoesNavioAtracadoPnl */
    public InformacoesNavioAtracadoPnl(Navio navioAtracado,Date horaSituacao) {
        try {
            initComponents();
            this.horaSituacao = horaSituacao;
            this.navioAtracado = navioAtracado;
            criaColunaTabela();
            listaInformacoesCarga = new Vector();
            limpaInformacoesNavioAtracado();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void atualizaInformacoesNavioAtracado() {

        try {
            limpaInformacoesNavioAtracado();

            lblNomeNavio.setText(navioAtracado.getNomeNavio());
            lblDataEmbarque.setText(DSSStockyardTimeUtil.formatarData(navioAtracado.getDataEmbarque(), PropertiesUtil.buscarPropriedade("formato.campo.data")));
            lblDataSaida.setText(DSSStockyardTimeUtil.formatarData(navioAtracado.getDiaDeSaida(), PropertiesUtil.buscarPropriedade("formato.campo.data")));
            lblDataETA.setText(DSSStockyardTimeUtil.formatarData(navioAtracado.getEta(), PropertiesUtil.buscarPropriedade("formato.campo.data")));
            lblStatus.setText(navioAtracado.getStatusEmbarque().toString());
      
            Object[] dadosCarga = new Object[3];
            for (Carga cargaNavio : navioAtracado.getListaDeCargasDoNavio(this.horaSituacao)) {
                dadosCarga[COL_PRODUTO] = cargaNavio.getProduto().getTipoProduto().getCodigoFamiliaTipoProduto() + " - " + cargaNavio.getProduto().getTipoProduto().getCodigoTipoProduto();
                dadosCarga[COL_QTDE_EMBARCADA] = DSSStockyardFuncoesNumeros.getQtdeFormatada(cargaNavio.getProduto().getQuantidade(),2);
                if (cargaNavio.getOrientacaoDeEmbarque() != null) 
                {//caso a Orientacao de embarque seja nula, significa que uma nova Carga foi criada porem sua orientacao de embarque ainda não...
                    dadosCarga[COL_QTDE_CONTRATADA] = DSSStockyardFuncoesNumeros.getQtdeFormatada(cargaNavio.getOrientacaoDeEmbarque().getQuantidadeNecessaria(),2);
                }else
                {
                    dadosCarga[COL_QTDE_CONTRATADA] = DSSStockyardFuncoesNumeros.getQtdeFormatada(0d,2);
                }
                listaInformacoesCarga.add(new Vector(Arrays.asList(dadosCarga)));
            }

            CFlexStockyardFuncoesTabela.setInformacoesTabela(tblCargasNavio, listaInformacoesCarga, listaColunas);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Metodo limpa todas as informacoes do navio que esta atracado
     * @throws java.lang.Exception
     */
    public void limpaInformacoesNavioAtracado() throws Exception {
        lblNomeNavio.setText("");
        lblDataEmbarque.setText("");
        lblDataSaida.setText("");
        lblDataETA.setText("");
        lblStatus.setText("");
              
        listaInformacoesCarga.removeAllElements();
        CFlexStockyardFuncoesTabela.setInformacoesTabela(tblCargasNavio, listaInformacoesCarga, listaColunas);
    }

    /**
     * Metodo que cria a lista de colunas da tabela de lista de cargas do
     * navio atracado.
     */
    private void criaColunaTabela() {
        listaColunas = new ArrayList<ColunaTabela>();
        ColunaTabela col = null;

        col = new ColunaTabela();
        col.setAlinhamento(SwingConstants.CENTER);
        col.setEditar(Boolean.FALSE);
        col.setLargura(85);
        col.setRedimensionar(Boolean.FALSE);
        col.setTitulo(PropertiesUtil.getMessage("coluna.produto"));
        listaColunas.add(col);

        col = new ColunaTabela();
        col.setAlinhamento(SwingConstants.CENTER);
        col.setEditar(Boolean.FALSE);
        col.setLargura(80);
        col.setRedimensionar(Boolean.FALSE);
        col.setTitulo(PropertiesUtil.getMessage("coluna.qtde.contrato"));
        listaColunas.add(col);

        col = new ColunaTabela();
        col.setAlinhamento(SwingConstants.CENTER);
        col.setEditar(Boolean.FALSE);
        col.setLargura(95);
        col.setRedimensionar(Boolean.FALSE);
        col.setTitulo(PropertiesUtil.getMessage("coluna.qtde.embarcada"));
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

        jLabel1 = new javax.swing.JLabel();
        lblNomeNavio = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblDataEmbarque = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblDataSaida = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblDataETA = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        scrListaCargas = new javax.swing.JScrollPane();
        tblCargasNavio = new com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable();

        setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(99, 99, 99));
        jLabel1.setText(PropertiesUtil.getMessage("label.navio"));

        lblNomeNavio.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        lblNomeNavio.setForeground(new java.awt.Color(99, 99, 99));

        jLabel2.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(99, 99, 99));
        jLabel2.setText(PropertiesUtil.getMessage("label.data.embarque"));

        lblDataEmbarque.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        lblDataEmbarque.setForeground(new java.awt.Color(99, 99, 99));

        jLabel3.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(99, 99, 99));
        jLabel3.setText(PropertiesUtil.getMessage("label.saida"));

        lblDataSaida.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        lblDataSaida.setForeground(new java.awt.Color(99, 99, 99));

        jLabel4.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(99, 99, 99));
        jLabel4.setText(PropertiesUtil.getMessage("label.eta"));

        lblDataETA.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        lblDataETA.setForeground(new java.awt.Color(99, 99, 99));

        jLabel5.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(99, 99, 99));
        jLabel5.setText(PropertiesUtil.getMessage("label.status"));

        lblStatus.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        lblStatus.setForeground(new java.awt.Color(99, 99, 99));

        scrListaCargas.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

      tblCargasNavio.setModel(new javax.swing.table.DefaultTableModel(
         new Object [][] {

         },
         new String [] {

            }
        ));
        tblCargasNavio.setFont(new java.awt.Font("Tahoma", 0, 9)); // NOI18N
        scrListaCargas.setViewportView(tblCargasNavio);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblNomeNavio, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblDataSaida, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblDataETA, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblDataEmbarque, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(27, 27, 27))
                    .addComponent(scrListaCargas, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNomeNavio, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(lblDataEmbarque, javax.swing.GroupLayout.DEFAULT_SIZE, 13, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(lblDataSaida, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDataETA, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrListaCargas, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel lblDataETA;
    private javax.swing.JLabel lblDataEmbarque;
    private javax.swing.JLabel lblDataSaida;
    private javax.swing.JLabel lblNomeNavio;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JScrollPane scrListaCargas;
    private com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable tblCargasNavio;
    // End of variables declaration//GEN-END:variables

}
