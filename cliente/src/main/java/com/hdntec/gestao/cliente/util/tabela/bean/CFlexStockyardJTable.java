/*
 *  Classe: CFlexJTable.java
 *  Autor:  Rodrigo Luchetta
 *  Data:   17/11/2008
 *
 */
package com.hdntec.gestao.cliente.util.tabela.bean;

import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;

public class CFlexStockyardJTable extends javax.swing.JTable {
    
    private int numFixedCol = 0;
    
    public CFlexStockyardJTable() {
        super(); 
        initComponents();
        numFixedCol = 0;
    }
    
    public CFlexStockyardJTable(TableModel dm) {
        super(dm);
        initComponents();
        numFixedCol = 0;
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
    }// </editor-fold>//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    @Override
    public void tableChanged(TableModelEvent e) {        
        super.tableChanged(e);        
        repaint();      
    }    
    
    public void setNumFixedCol(int i_numFixedCol) {
        numFixedCol = i_numFixedCol;
    }
    
    public int getNumFixedCol() {
        return numFixedCol;
    }
    
  
}
