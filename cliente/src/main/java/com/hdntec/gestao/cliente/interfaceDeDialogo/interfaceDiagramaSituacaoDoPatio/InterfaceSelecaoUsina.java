/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * InterfaceSelecaoUsina.java
 *
 * Created on 10/02/2009, 14:22:05
 */
package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio;

import java.awt.event.ActionEvent;
import java.text.NumberFormat;
import java.util.Locale;

import com.hdntec.gestao.domain.planta.entity.status.Usina;
import com.hdntec.gestao.domain.produto.enums.TipoDeProdutoEnum;
import com.hdntec.gestao.util.PropertiesUtil;

/**
 * 
 * @author Ricardo Trabalho
 * @author andre (LT)
 */
public abstract class InterfaceSelecaoUsina extends javax.swing.JPanel
{
   
   /**
    * Serialização
    */
   private static final long serialVersionUID = -16869695930048910L;
   
   private boolean operacaoCanceladaPeloUsuario;
   
   /** A usina em questão na seleção do produto */
   private Usina usinaVisualizada;
   
   /** O tipo de produto selecionado */
   private TipoDeProdutoEnum tipoDeProdutoSelecionado;
   
   public InterfaceSelecaoUsina(Usina usinaVisualizada)
   {
      this.usinaVisualizada = usinaVisualizada;
      initComponents();
   }
   
   /**
    * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
    */
   @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents()
   {
      buttonGroupTipoProduto = new javax.swing.ButtonGroup();
      pelotaRadioButton = new javax.swing.JRadioButton();
      pelletRadioButton = new javax.swing.JRadioButton();
      screeningRadioButton = new javax.swing.JRadioButton();
      jLabel1 = new javax.swing.JLabel();
      jLabel2 = new javax.swing.JLabel();
      NumberFormat formatoNumero = NumberFormat.getNumberInstance(new Locale("pt","BR"));
      jTextQuantidadeProduto = new javax.swing.JFormattedTextField(formatoNumero);
      jButtonCancelar = new javax.swing.JButton();
      jButtonConfirmar = new javax.swing.JButton();
      buttonGroupTipoProduto.add(pelotaRadioButton);
      pelotaRadioButton.setText(usinaVisualizada.getMetaUsina().getCampanhaAtual(usinaVisualizada.getDtInicio()).getTipoProduto().getCodigoFamiliaTipoProduto() + " - " + usinaVisualizada.getMetaUsina().getCampanhaAtual(usinaVisualizada.getDtInicio()).getTipoProduto().getCodigoTipoProduto());
      pelotaRadioButton.addActionListener(new java.awt.event.ActionListener()
      {
         
         public void actionPerformed(java.awt.event.ActionEvent evt)
         {
            pelotaRadioButtonActionPerformed(evt);
         }
      });
      buttonGroupTipoProduto.add(pelletRadioButton);
      pelletRadioButton.setText(usinaVisualizada.getMetaUsina().getCampanhaAtual(usinaVisualizada.getDtInicio()).getTipoPellet().getCodigoTipoProduto());
      pelletRadioButton.addActionListener(new java.awt.event.ActionListener()
      {
         
         public void actionPerformed(java.awt.event.ActionEvent evt)
         {
            pelletRadioButtonActionPerformed(evt);
         }
      });
      buttonGroupTipoProduto.add(screeningRadioButton);
      screeningRadioButton.setText(PropertiesUtil.getMessage("radio.button.screening"));
      screeningRadioButton.addActionListener(new java.awt.event.ActionListener()
      {
         
         public void actionPerformed(java.awt.event.ActionEvent evt)
         {
            screeningRadioButtonActionPerformed(evt);
         }
      });
      jLabel1.setText(PropertiesUtil.getMessage("label.selecione.tipo.produto.desejado"));
      jLabel2.setText(PropertiesUtil.getMessage("label.quantidade.produto.ton"));
      jButtonCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icones/cancelar.png"))); // NOI18N
      jButtonCancelar.setText(PropertiesUtil.getMessage("botao.cancelar"));
      jButtonCancelar.addActionListener(new java.awt.event.ActionListener()
      {
         
         public void actionPerformed(java.awt.event.ActionEvent evt)
         {
            jButtonCancelarActionPerformed(evt);
         }
      });
      jButtonConfirmar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icones/confirmar.png"))); // NOI18N
      jButtonConfirmar.setText(PropertiesUtil.getMessage("botao.confirmar"));
      jButtonConfirmar.addActionListener(new java.awt.event.ActionListener()
      {
         
         public void actionPerformed(java.awt.event.ActionEvent evt)
         {
            jButtonConfirmarActionPerformed(evt);
         }
      });
      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
      this.setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
               layout.createSequentialGroup().addContainerGap().addGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(pelotaRadioButton).addGap(33, 33, 33).addComponent(pelletRadioButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE).addComponent(screeningRadioButton)).addComponent(jLabel1).addComponent(jLabel2).addComponent(jTextQuantidadeProduto, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)).addGroup(
                                 javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(jButtonCancelar).addGap(18, 18, 18).addComponent(jButtonConfirmar))).addGap(18, 18, 18)));
      layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
               layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(pelotaRadioButton).addComponent(pelletRadioButton).addComponent(screeningRadioButton)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextQuantidadeProduto, javax.swing.GroupLayout.PREFERRED_SIZE,
                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButtonConfirmar).addComponent(jButtonCancelar)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
   }// </editor-fold>//GEN-END:initComponents
   
   protected void screeningRadioButtonActionPerformed(ActionEvent evt)
   {
      tipoDeProdutoSelecionado = TipoDeProdutoEnum.PELLET_SCREENING;
   }
   
   protected void pelletRadioButtonActionPerformed(ActionEvent evt)
   {
      tipoDeProdutoSelecionado = TipoDeProdutoEnum.PELLET_FEED;
   }
   
   private void pelotaRadioButtonActionPerformed(java.awt.event.ActionEvent evt)
   {// GEN-FIRST:event_pelotaRadioButtonActionPerformed
      tipoDeProdutoSelecionado = TipoDeProdutoEnum.PELOTA;
   }// GEN-LAST:event_pelotaRadioButtonActionPerformed
   
   private void jButtonCancelarActionPerformed(java.awt.event.ActionEvent evt)
   {// GEN-FIRST:event_jButtonCancelarActionPerformed
      this.operacaoCanceladaPeloUsuario = true;
      fecharJanela();
      this.getParent().getParent().getParent().getParent().setVisible(false);
   }// GEN-LAST:event_jButtonCancelarActionPerformed
   
   private void jButtonConfirmarActionPerformed(java.awt.event.ActionEvent evt)
   {// GEN-FIRST:event_jButtonConfirmarActionPerformed
      this.operacaoCanceladaPeloUsuario = Boolean.FALSE;
      // chegando ate o JDialog
      this.getParent().getParent().getParent().getParent().setVisible(false);
   }// GEN-LAST:event_jButtonConfirmarActionPerformed
   
   public abstract void fecharJanela();
   
   public Boolean getOperacaoCanceladaPeloUsuario()
   {
      return this.operacaoCanceladaPeloUsuario;
   }
   
   public void setOperacaoCanceladaPeloUsuario(Boolean operacaoCanceladaPeloUsuario)
   {
      this.operacaoCanceladaPeloUsuario = operacaoCanceladaPeloUsuario;
   }
   
   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.ButtonGroup buttonGroupTipoProduto;
   
   private javax.swing.JButton jButtonCancelar;
   
   private javax.swing.JButton jButtonConfirmar;
   
   private javax.swing.JLabel jLabel1;
   
   private javax.swing.JLabel jLabel2;
   
   private javax.swing.JFormattedTextField jTextQuantidadeProduto;
   
   private javax.swing.JRadioButton pelletRadioButton;
   
   private javax.swing.JRadioButton pelotaRadioButton;
   
   private javax.swing.JRadioButton screeningRadioButton;
   
   // End of variables declaration//GEN-END:variables
   /**
    * @param operacaoCanceladaPeloUsuario
    *           the operacaoCanceladaPeloUsuario to set
    */
   public void setOperacaoCanceladaPeloUsuario(boolean operacaoCanceladaPeloUsuario)
   {
      this.operacaoCanceladaPeloUsuario = operacaoCanceladaPeloUsuario;
   }
   
   /**
    * @return the tipoDeProdutoSelecionado
    */
   public TipoDeProdutoEnum getTipoDeProdutoSelecionado()
   {
      return tipoDeProdutoSelecionado;
   }
   
   /**
    * @param tipoDeProdutoSelecionado
    *           the tipoDeProdutoSelecionado to set
    */
   public void setTipoDeProdutoSelecionado(TipoDeProdutoEnum tipoDeProdutoSelecionado)
   {
      this.tipoDeProdutoSelecionado = tipoDeProdutoSelecionado;
   }

   
   /**
    * @return the jTextQuantidadeProduto
    */
   public javax.swing.JTextField getJTextQuantidadeProduto()
   {
      return jTextQuantidadeProduto;
   }

   
   /**
    * @param textQuantidadeProduto the jTextQuantidadeProduto to set
    */
   public void setJTextQuantidadeProduto(javax.swing.JFormattedTextField textQuantidadeProduto)
   {
      jTextQuantidadeProduto = textQuantidadeProduto;
   }
}
