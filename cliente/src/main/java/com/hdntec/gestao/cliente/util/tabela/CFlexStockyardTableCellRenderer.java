package com.hdntec.gestao.cliente.util.tabela;

import java.awt.Component;
import java.util.List;

import javax.swing.table.DefaultTableCellRenderer;

import com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable;

public class CFlexStockyardTableCellRenderer extends DefaultTableCellRenderer {
    
    private List<ColunaTabela> m_colunas;
            
    public CFlexStockyardTableCellRenderer(List<ColunaTabela> i_colunas) {
        m_colunas = i_colunas;
    }
    
    public Component getTableCellRendererComponent(CFlexStockyardJTable table, Object value,
               boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent
            (table, value, isSelected, hasFocus, row, column);
        
        setHorizontalAlignment(getAlinhamentoColuna(column));
        return cell; 

    }

    private Integer getAlinhamentoColuna(Integer i_coluna) {
        return m_colunas.get(i_coluna).getAlinhamento();
    }
    
}     

