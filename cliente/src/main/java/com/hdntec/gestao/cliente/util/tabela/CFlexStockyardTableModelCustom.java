package com.hdntec.gestao.cliente.util.tabela;

import java.awt.Color;
import java.awt.Component;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

    
/**
 * Esta eh uma implementacao da <code>TablePrincipalModel</code> que
 * usa um <code>Vector</code> para armazenar cores e alinhamentos. <p>
 *
 * <strong>Observacao:</strong>
 * Veja os metodos: getModelRenderer(), getModelRendererMultiLine() <p>
 * <strong>Aviso:</strong>
 * Nao foi testado para grandes quantidades de dados.
 *
 * @version 1.0 14/07/05
 * @author Roger Camargo
 *
 * @see #getModelRenderer
 * @see #getModelRendererMultiLine
 * @see #setRowColor(int, Color)
 * @see #setCellColor(int, int, Color)
 * @see #setColAligment(int, int)
 * @see #setZebrado(boolean)
 */

public class CFlexStockyardTableModelCustom extends CFlexStockyardTableModel implements Comparator, Serializable, TableModelListener {

    protected Vector  cellColor     = new Vector();
    protected Vector  cellAlignment = new Vector();
    protected boolean zebrado       = false;
    protected Color   zebradoColor  = Color.lightGray;
    protected int numColFixed = 0;
    protected Object[] m_titulos;
    protected Object[][] m_dados;
    
    public CFlexStockyardTableModelCustom() {
        super();
    }    

    /**
     *  Construtor do <code>OBTableModelCustom</code> eh o
     *  mesmo do TablePrincipalModel
     *
     * @param columnNames       the names of the columns
     * @see #getModelRenderer
     * @see #getModelRendererMultiLine
     */    
    public CFlexStockyardTableModelCustom(Object[] titles) {
        super(titles);
    }
    
    /**
     *  Construtor do <code>OBTableModelCustom</code> eh o
     *  mesmo do TablePrincipalModel
     *
     * @param data              the data of the table
     * @param columnNames       the names of the columns
     * @see #getModelRenderer
     * @see #getModelRendererMultiLine
     */    
    public CFlexStockyardTableModelCustom(Object[][] data, Object[] titles) {
        super(data,titles);
    }
 
    /**
     * Retorna o renderer para ser setado ao <code>TablePrincipalModel</code><p>
     * Exemplo de como usar o Renderer: <p>
     *
     *   TablePrincipalModelColor model = new TablePrincipalModelColor(dado,colunas);
     *   model.setColEditable( 1, true);
     *   model.setRowColor( 0 , new Color(255, 255, 180) );
     *   model.setColAligment( 2, SwingConstants.RIGHT );
     *   jtblTable.setModel( model ); <p>
     *            
     *   TableCellRenderer renderer = model.getModelRenderer();
     *   jtblTable.setDefaultRenderer(Class.forName("java.lang.Object" ), renderer );
     *
     * @return                  TablePrincipalModelCellRenderer
     */
    public TablePrincipalModelCellRenderer getModelRenderer() { 
        return new TablePrincipalModelCellRenderer();
    }    

    /**
     * Retorna o renderer para ser setado ao <code>TablePrincipalModel</code><p>
     * -O uso eh o mesmo do <code>getModelRenderer()</code>,
     *  com a diferenca que pode se utilizar \n nas string.
     *
     * @return                  TablePrincipalModelCellRenderer
     */
    public TablePrincipalModelCellRendererMultiLine getModelRendererMultiLine() { 
        return new TablePrincipalModelCellRendererMultiLine();
    }

    /**
     * Seta a cor de uma celula especifica <p>
     *
     * @param   row     linha
     * @param   col     coluna
     * @param   color   cor
     *
     * @return  nada
     */    
    public void setCellColor( int row, int col, Color color) { //Roger criou
        cellColor.add(new CellFormat(row,col,color));
    }    

    /**
     * Adiciona a coluna ao model <p>
     *
     * @param   columnName      titulo da coluna na tabela
     *
     * @return  nada
     */    
    @Override
    public void addColumn(Object columnName) {
        super.addColumn(columnName);
        ascendCol.add(one);
    }

    /**
     * Adiciona a coluna ao model <p>
     *
     * @param   columnName      titulo da coluna na tabela
     * @param   columnData      dados da Coluna na tabela
     *
     * @return  nada
     */    
    @Override
    public void addColumn(Object columnName, Object[] columnData) {
        super.addColumn(columnName, columnData);
        ascendCol.add(one);
    }

    public void setNumColFixed(int i_numColFixed) {
        numColFixed = i_numColFixed;
    }
    
    public int getNumColFixed() {
        return numColFixed;
    }
    
    @Override
    public int getColumnCount() { 
        return super.getColumnCount() - numColFixed;
    }
    
    @Override
    public int getRowCount() { 
        return super.getDataVector().size();
    }
    
    @Override
    public String getColumnName(int col) { 
        return super.getColumnName(col + numColFixed); 
    }
    
    @Override
    public Object getValueAt(int row, int col) { 
        return super.getValueAt(row,col + numColFixed); 
    }    
    
    @Override
    public void setValueAt(Object obj, int row, int col) { 
        super.setValueAt(obj, row, col + numColFixed);
    }
    
    /**
     * Adiciona a coluna ao model <p>
     *
     * @param   columnName      titulo da coluna na tabela
     * @param   columnData      dados da Coluna na tabela
     *
     * @return  nada
     */    
    @Override
    public void addColumn(Object columnName, Vector columnData) {
        super.addColumn(columnName, columnData);
        ascendCol.add(one);
    }
    
    /**
     * Seta a cor de uma linha inteira <p>
     *
     * @param   row     linha
     * @param   color   cor
     *
     * @return  nada
     */    
    public void setRowColor(int row, Color color) {        
        for (int i=0; i < super.columnIdentifiers.size(); i++ ) {
            cellColor.add(new CellFormat(row, i,color));
        }
    }
    
    public void setRowColor(int[] row, Color color) {        
        for (int l=0; l < row.length; l++ ) {
            for (int i=0; i < super.columnIdentifiers.size(); i++ ) {
                cellColor.add(new CellFormat(row[l], i,color));
            }
        }
    }

    /**
     * Seta o alinhamento de uma coluna especifica <p>
     *
     * @param   col     coluna
     * @param   color   alinhamento
     *
     * @return  nada
     */
    public void setColAligment(int col, int aligment) {        
        for (int i=0; i < super.dataVector.size(); i++ ) {
            cellAlignment.add( new CellFormat(i, col, aligment) );
        }
    }
    
    /**
     * Configura de modo que a <code>jtable</code> tenha o efeito zebrado,
     * ou seja, as linhas pares de cor diferente.<p>
     *
     * @param   value   true/false
     *
     * @return  nada
     * @see #setZebradoColor(Color)
     */
    public void setZebrado(boolean value) {
        zebrado = value;
    }
    
    /**
     * Configura a cor para as linhas pares do efeito zebrado.
     * a cor padrao eh <code>gray</code>. <p>
     * @param   color   cor
     *
     * @return  nada
     * @see #setZebrado(boolean)
     */
    public void setZebradoColor( Color color ) {
        zebradoColor = color;
    }
    
    /* Cell Render */
    class TablePrincipalModelCellRenderer extends DefaultTableCellRenderer {    

        @Override
        public Component getTableCellRendererComponent
                (JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            
            Component cell = super.getTableCellRendererComponent
                (table, value, isSelected, hasFocus, row, column);

            /* formatando cor de fundo da celula */
            Color color = setColorCustom( row, column, table.getBackground() );            
            setBackground( color );
            
            /* formatando alinhamento da celula */
            int alignment = setAlignmentCustom( row, column, SwingConstants.LEFT );
            setHorizontalAlignment( alignment );
            
            return cell; 
            
        }
    }            
    
    /*multiline */
    class TablePrincipalModelCellRendererMultiLine extends JTextArea implements TableCellRenderer {

        public TablePrincipalModelCellRendererMultiLine() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setOpaque(true);
        }        
        
        public Component getTableCellRendererComponent(JTable table, Object value,
                   boolean isSelected, boolean hasFocus, int row, int column) {
                    
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }
        
            setFont(table.getFont());
        
            if (hasFocus) {
                setBorder( UIManager.getBorder("Table.focusCellHighlightBorder") );
                if (table.isCellEditable(row, column)) {
                    setForeground( UIManager.getColor("Table.focusCellForeground") );
                    setBackground( UIManager.getColor("Table.focusCellBackground") );
                }
            } else {
                setBorder(new EmptyBorder(1, 2, 1, 2));
            }
        
            setText((value == null) ? "" : value.toString());
        
            /* formatando cor de fundo da celula */
            Color color = setColorCustom( row, column, table.getBackground() );            
            setBackground( color );
                
            return this;
        }
    }   
    
    private Color setColorCustom( int row, int column, Color defaultColor ) {
        
        /**
         * nesta primeira versao foi testado apenas em tabelas pequenas
         * (100 registros de 10 colunas) e o desempenho foi bom!
         * para grande numero de registros com varias cores
         * pode ficar lendo (nao testado)
         * talvez pode ser implementado um vector de cores
         * com o mesmo formato dos dados
         * assim neste momento verifica as coordenadas (x,y) e pega a cor.
         * ao invez de ficar fazendo um FOR para cada celula.
         */

        Color color = defaultColor;
        
        if ( zebrado ) {
            if ( row % 2 == 0 ) {
                color = zebradoColor;
            }
        }
        
        for (int i=cellColor.size()-1; i >= 0 ; i-- ) {
            CellFormat item = (CellFormat) cellColor.get(i);
            if ( item.row == row && item.col == column ) {
                color = item.color;
                break;
            }
        }    
        return color;
    }
    
    private int setAlignmentCustom( int row, int column, int defaultAlignment ) {    

        /* formatando alinhamento da celula */
        int alignment = defaultAlignment;

        for (int i=cellAlignment.size()-1; i >= 0 ; i-- ) {
            CellFormat item = (CellFormat) cellAlignment.get(i);
            if ( item.row == row && item.col == column ) {
                alignment = item.alignment;
                break;
            }
        }
        return alignment;
    }
    
    public void tableChanged(TableModelEvent e) {
        fireTableChanged(e);
    }
}
class CellFormat {
    int row;
    int col;
    int alignment;
    Color color;
    
    public CellFormat(int i_row, int i_col, Color i_color) {
        row = i_row;
        col = i_col;
        color = i_color;
    }
    public CellFormat(int i_row, int i_col, int i_alignment) {
        row = i_row;
        col = i_col;
        alignment = i_alignment;
    }        
}    
  
