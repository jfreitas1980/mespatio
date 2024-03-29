/*
 * InterfaceAtracarNavio.java
 * Created on 03/09/2009
 */

package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios;

import java.awt.Rectangle;
import java.util.List;

import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex;
import com.hdntec.gestao.domain.plano.enums.TipoAtividadeEnum;
import com.hdntec.gestao.domain.planta.entity.MetaBerco;
import com.hdntec.gestao.domain.vo.atividades.AtividadeAtracarDesAtracarNavioVO;
import com.hdntec.gestao.exceptions.AtividadeException;
import com.hdntec.gestao.util.PropertiesUtil;
import com.hdntec.gestao.util.datahora.DSSStockyardTimeUtil;

/**
 * Interface GUI para realizar a atracao de um navio manualmente
 * @author Bruno Gomes
 */
public class InterfaceAtracarNavio extends javax.swing.JDialog {

  private AtividadeAtracarDesAtracarNavioVO movimentacaoVO;
  private List<MetaBerco> bercos; 
	  
	  private InterfaceNavio interfaceNavio;
      /** identificador de operacao cancelada pelo usuário */
   private Boolean operacaoCanceladaPeloUsuario = Boolean.FALSE;
 
    /** Creates new form InterfaceAtracarNavio */
    public InterfaceAtracarNavio(java.awt.Frame parent, boolean modal, InterfaceNavio interfaceNavio) {
        super(parent, modal);
        bercos = interfaceNavio.getControladorInterfaceFilaDeNavios().getInterfaceInicial().getInterfaceInicial().bercosPier();
        initComponents();
        this.interfaceNavio = interfaceNavio;
            
        carregaDados();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lbNomeNavio = new javax.swing.JLabel();
        jtNomeNavio = new javax.swing.JTextField();
        lbStatus = new javax.swing.JLabel();
        comboStatus = new javax.swing.JComboBox();
        lbBerco = new javax.swing.JLabel();
        comboBerco = new javax.swing.JComboBox();
        jSeparator1 = new javax.swing.JSeparator();
        cmdDesistir = new javax.swing.JButton();
        cmdConfirmar = new javax.swing.JButton();
        lbDataSaida = new javax.swing.JLabel();
        dtSaidaDenavio = new com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex();
        
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(PropertiesUtil.getMessage("titulo.atracar.navio"));

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(null);

        lbNomeNavio.setText(PropertiesUtil.getMessage("relatorio.planoDeRecuperacao.navio"));
        jPanel1.add(lbNomeNavio);
        lbNomeNavio.setBounds(10, 20, 102, 14);
        jPanel1.add(jtNomeNavio);
        jtNomeNavio.setBounds(140, 20, 190, 20);

        lbStatus.setText(PropertiesUtil.getMessage("label.status"));
        jPanel1.add(lbStatus);
        lbStatus.setBounds(10, 50, 102, 14);

        comboStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] {PropertiesUtil.getMessage("combo.atracado") }));
        jPanel1.add(comboStatus);
        comboStatus.setBounds(140, 50, 70, 20);

        lbBerco.setText(PropertiesUtil.getMessage("label.berco"));
        jPanel1.add(lbBerco);
        lbBerco.setBounds(10, 80, 102, 14);

        comboBerco.setModel(new javax.swing.DefaultComboBoxModel(new String[] { PropertiesUtil.getMessage("mensagem.interface.inicial.berco.oeste"), PropertiesUtil.getMessage("mensagem.interface.inicial.berco.leste") }));
        jPanel1.add(comboBerco);
        comboBerco.setBounds(140, 80, 120, 20);
        jPanel1.add(jSeparator1);
        jSeparator1.setBounds(10, 140, 330, 10);

        cmdDesistir.setFont(new java.awt.Font("Arial", 1, 12));
        StringBuffer value = new StringBuffer();
        value.append(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens")).append("/exit.png");
        cmdDesistir.setIcon(new javax.swing.ImageIcon(value.toString())); // NOI18N
        cmdDesistir.setText("Desistir");
        cmdDesistir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdDesistirActionPerformed(evt);
            }
        });
        jPanel1.add(cmdDesistir);
        cmdDesistir.setBounds(50, 150, 120, 23);

        cmdConfirmar.setFont(new java.awt.Font("Arial", 1, 12));
        value = new StringBuffer();
        value.append(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens")).append("/ok.png");
        cmdConfirmar.setIcon(new javax.swing.ImageIcon(value.toString())); // NOI18N
        cmdConfirmar.setText("Confirmar");
        cmdConfirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdConfirmarActionPerformed(evt);
            }
        });
        jPanel1.add(cmdConfirmar);
        cmdConfirmar.setBounds(180, 150, 130, 23);

        lbDataSaida.setText(PropertiesUtil.getMessage("label.data.chegada"));
        jPanel1.add(lbDataSaida);
        lbDataSaida.setBounds(10, 110, 100, 14);
        jPanel1.add(dtSaidaDenavio);
        dtSaidaDenavio.setBounds(140, 110, 196, 20);
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdConfirmarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmdConfirmarActionPerformed
    {//GEN-HEADEREND:event_cmdConfirmarActionPerformed
        if(executaAtividade()){
            this.operacaoCanceladaPeloUsuario = Boolean.FALSE;
           // chegando ate o JDialog
           this.setVisible(false);
        }
    }//GEN-LAST:event_cmdConfirmarActionPerformed

    private void cmdDesistirActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmdDesistirActionPerformed
    {//GEN-HEADEREND:event_cmdDesistirActionPerformed
        this.operacaoCanceladaPeloUsuario = true;
        this.setVisible(false);
    }//GEN-LAST:event_cmdDesistirActionPerformed

    private boolean executaAtividade(){
        try {
            atualizaDadosNavio();
            interfaceNavio.getControladorInterfaceFilaDeNavios().getInterfaceInicial().verificaMovimentacaoNavio(movimentacaoVO);
                        
        
        } catch (AtividadeException e) {
        	interfaceNavio.getControladorInterfaceFilaDeNavios().getInterfaceInicial().desativarMensagemProcessamento();
            InterfaceMensagem interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            interfaceMensagem.setDescricaoMensagem(e.getMessage());
            interfaceNavio.getControladorInterfaceFilaDeNavios().ativarMensagem(interfaceMensagem);
            return false;
		}
        return true;
    }

    private void atualizaDadosNavio(){      	
    	movimentacaoVO.setDataInicio(this.dtSaidaDenavio.getDataHoraDate());
    	String indentificador = null;
    	if (comboBerco.getSelectedItem().equals(PropertiesUtil.getMessage("mensagem.interface.inicial.berco.oeste"))) {
    		indentificador = "W";
    	} else {
    		indentificador = "E";
    	}

    	for (MetaBerco metaBerco : bercos) {
    			if (metaBerco.getIdentificadorBerco().equalsIgnoreCase(indentificador)) {
    				movimentacaoVO.setMetaBercoDestino(metaBerco);	
    				break;
    			}
    	}    		
        
    }

    private void carregaDados(){

        this.dtSaidaDenavio.limpaDataHora();
        
        this.dtSaidaDenavio.setDataHora(DSSStockyardTimeUtil.obterDataHoraUltimaSituacaoParaAtividades(interfaceNavio.getControladorInterfaceFilaDeNavios().
                        getInterfaceInicial().getInterfaceInicial().getInterfaceDSP().getControladorDSP().getInterfaceInicial().getInterfaceInicial().getSituacaoPatioExibida().getDtInicio()));
        movimentacaoVO = new AtividadeAtracarDesAtracarNavioVO();
    	movimentacaoVO.setMetaNavio(interfaceNavio.getNavioVisualizado().getMetaNavio());
    	movimentacaoVO.setTipoAtividade(TipoAtividadeEnum.CHEGADA_DE_NAVIO);
    	
    	jtNomeNavio.setText(interfaceNavio.getNavioVisualizado().getNomeNavio());
        jtNomeNavio.setEnabled(false);
        comboStatus.setEnabled(false);

        //seta posicao da janela
        Rectangle rec = interfaceNavio.getControladorInterfaceFilaDeNavios().getInterfaceInicial().getInterfaceInicial().getBounds();
        this.setLocation(rec.width / 2 , rec.height / 2);
    }

    public Boolean getOperacaoCanceladaPeloUsuario() {
        return operacaoCanceladaPeloUsuario;
    }
   private com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex dtSaidaDenavio;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdConfirmar;
    private javax.swing.JButton cmdDesistir;
    private javax.swing.JComboBox comboBerco;
    private javax.swing.JComboBox comboStatus;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jtNomeNavio;
    private javax.swing.JLabel lbBerco;
    private javax.swing.JLabel lbDataSaida;
    private javax.swing.JLabel lbNomeNavio;
    private javax.swing.JLabel lbStatus;
    // End of variables declaration//GEN-END:variables

   public CalendarioHoraCFlex getDtSaidaDenavio()
   {
      return dtSaidaDenavio;
   }

}
