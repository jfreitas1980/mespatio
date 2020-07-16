/*
 *  FuncoesTela.java
 *  Autor: Rodrigo Luchetta
 *  Data: 02/06/2005
 *
 */

package com.hdntec.gestao.cliente.util.telas;

import java.awt.Container;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable;


public class DSSStockyardTelaUtil {
 
    /**
     * Metodo que busca dentro de um JInternalFrame a classe JDialog que o adicionou
     * @param i_frame
     * @return
     */
    public static JDialog getParentDialog(JInternalFrame internalFrame) {
        JDialog parentDialog = null;
        Container cObj = null;

        for (cObj = internalFrame.getParent(); cObj != null; cObj = cObj.getParent()) {
             if (cObj instanceof JDialog) {
                parentDialog = (JDialog)cObj;
                break;
            }
        }

        return parentDialog;
    }

    /**
     * Metodo que busca dentro de um JInternalFrame a classe JDialog que o adicionou
     * @param i_frame
     * @return
     */
    public static JDialog getParentDialog(JPanel painel) {
        JDialog parentDialog = null;
        Container cObj = null;

        for (cObj = painel.getParent(); cObj != null; cObj = cObj.getParent()) {
             if (cObj instanceof JDialog) {
                parentDialog = (JDialog)cObj;
                break;
            }
        }

        return parentDialog;
    }

    /**
     * Metodo que busca dentro de um JInternalFrame a classe Frame que o adicionou
     * @param internalFrame
     * @return
     * @throws java.lang.Exception
     */
    public static Frame getParentFrame(JInternalFrame internalFrame) throws Exception {
        Frame parentFrame = null;
        Container cObj = null;

        for (cObj = internalFrame.getParent(); cObj != null; cObj = cObj.getParent()) {
             if (cObj instanceof Frame) {
                parentFrame = (Frame)cObj;
                break;
            }
        }

        return parentFrame;
    }
    
    
    /**
     * Metodo retorna o JScrollPane que adicionou a JTable
     * @param tabela
     * @return
     */
    public static JScrollPane getScrollPaneTable(CFlexStockyardJTable tabela) {
        JScrollPane scroll = null;
        Container cObj = null;

        for (cObj = tabela.getParent(); cObj != null; cObj = cObj.getParent()) {
             if (cObj instanceof JScrollPane) {
                scroll = (JScrollPane)cObj;
                break;
            }
        }

        return scroll;
    }
    
    
}

