/********************************************************************************
 *
 * OBTableModel Class
 * Author   : Rodrigo Luchetta
 * Purpose  : A TableModel that enables column sorting by clicking the column
 *
 * This class extends the DefaultTableModel class and enables sorting of columns
 * It sorts the TableModel's dataVector whenever a column is clicked.
 * Clicking the same column again causes it to be sorted in reverse order.
 * The sort is based on Java's Collection class sort method.
 *
 * Usage :
 *
 * OBTableModel model = new OBTableModel();
 * JTable table = new JTable(model);
 * model.addMouseListenerToHeaderInTable(table);
 *
 *******************************************************************************/
package com.hdntec.gestao.cliente.util.tabela;

import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

public class CFlexStockyardTableModel extends DefaultTableModel implements Comparator, Serializable{

    protected   int         currCol;
    protected   Vector      ascendCol;  // this vector stores the state (ascending or descending) of each column
    protected   Integer     one         = new Integer(1);
    protected   Integer     minusOne    = new Integer(-1);
    protected   Vector      colEditable = new Vector() ;
    protected   Vector      cellLocked = new Vector();
    protected   Vector      colItemCombo = new Vector(); //Esta coluna recebera Item Combo utilizado pela classe ConsPadrao
    protected Object[] m_titulos;
    protected Object[][] m_dados;
    protected int numColFixed = 0;
    
    public CFlexStockyardTableModel() {
        super();
        ascendCol = new Vector();
    }
    
    public CFlexStockyardTableModel(Object[][] data, Object[] titles) {
        super(data,titles); 
        ascendCol = new Vector();
        m_titulos = titles;
        m_dados = data;
        for (int i = 0; i < titles.length; i++) {
            colEditable.add(new Boolean(false));
            ascendCol.add(one);
        }
    }
 
    public CFlexStockyardTableModel(Object[] titles) {
        super(titles,10);
        m_titulos = titles;
        ascendCol = new Vector();
        
        
        for (int i = 0; i < titles.length; i++) {
            colEditable.add(new Boolean(false));
            ascendCol.add(one);
        }
    }

    @Override
    public Class getColumnClass(int c) {
        //Para evitar erro de null, procurando em todos as linhas
        for (int i=0; i < this.getRowCount(); i++) {
            if (this.getValueAt(i, c) != null)
                return this.getValueAt(i, c).getClass();
        }
        //Se todas estao vazias, retornar como tipo String
        String ret = "";
        return ret.getClass();
    } 

    public void limpaVectorCellNotEditable(){
        cellLocked.removeAllElements();
    }
    
    public void setColEditable(int col, boolean editable) { //Anderson criou
        colEditable.set(col, new Boolean(editable));
    }
     
     public void setCellNotEditable(int col, int row) { //FT criou
         cellLocked.add(new Cell(row,col));
     }
     
    /*
     * Don't need to implement this method unless your table's
     * editable.
     */
    @Override
    public boolean isCellEditable(int row, int col) {
        if (cellLocked.size() > 0) {
            Cell cellTest;
            for (int i=0; i < cellLocked.size(); i ++) {
                cellTest =(Cell)cellLocked.get(i);
                if (cellTest.row == row && cellTest.col == col)
                    return false;
            }
        }
        return ((Boolean)colEditable.get(col)).booleanValue();
    }

    /*******************************************************************
     * addColumn methods are inherited from the DefaultTableModel class.
     *******************************************************************/
    @Override
    public void addColumn(Object columnName) {
        super.addColumn(columnName);
        ascendCol.add(one);
        colEditable.add(new Boolean(false));
    }

    @Override
    public void addColumn(Object columnName, Object[] columnData) {
        super.addColumn(columnName, columnData);
        ascendCol.add(one);
    }

    @Override
    public void addColumn(Object columnName, Vector columnData) {
        super.addColumn(columnName, columnData);
        ascendCol.add(one);
    }

    /*****************************************************************
     * This method is the implementation of the Comparator interface.
     * It is used for sorting the rows
     *****************************************************************/
    @Override
    public int compare(Object v1, Object v2) {

        // the comparison is between 2 vectors, each representing a row
        // the comparison is done between 2 objects from the different rows that are in the column that is being sorted

        int ascending = ((Integer) ascendCol.get(currCol)).intValue();
        if (v1 == null && v2 == null) {
            return 0;
        } else if (v2 == null) { // Define null less than everything.
            return 1 * ascending;
        } else if (v1 == null) {
            return -1 * ascending;
        }

        Object o1 = ((Vector) v1).get(currCol);
        Object o2 = ((Vector) v2).get(currCol);

        // If both values are null, return 0.
        if (o1 == null && o2 == null) {
            return 0;
        } else if (o2 == null) { // Define null less than everything.
            return 1 * ascending;
        } else if (o1 == null) {
            return -1 * ascending;
        }

        if (o1 instanceof Number && o2 instanceof Number) {
            Number n1 = (Number) o1;
            double d1 = n1.doubleValue();
            Number n2 = (Number) o2;
            double d2 = n2.doubleValue();

            if (d1 == d2) {
                return 0;
            } else if (d1 > d2) {
                return 1 * ascending;
            } else {
                return -1 * ascending;
            }

        } else if (o1 instanceof Boolean && o2 instanceof Boolean) {
            Boolean bool1 = (Boolean) o1;
            boolean b1 = bool1.booleanValue();
            Boolean bool2 = (Boolean) o2;
            boolean b2 = bool2.booleanValue();

            if (b1 == b2) {
                return 0;
            } else if (b1) {
                return 1 * ascending;
            } else {
                return -1 * ascending;
            }

        } else {
            // default case
            if (o1 instanceof Comparable && o2 instanceof Comparable) {
                Comparable c1 = (Comparable) o1;
                Comparable c2 = (Comparable) o2; // superflous cast, no need for it!

                try {
                    return c1.compareTo(c2) * ascending;
                } catch (ClassCastException cce) {
                    // forget it... we'll deal with them like 2 normal objects below.
                }
            }

            String s1 = o1.toString();
            String s2 = o2.toString();
            return s1.compareTo(s2) * ascending;
        }
    }

    /***************************************************************************
     * This method sorts the rows using Java's Collections class.
     * After sorting, it changes the state of the column -
     * if the column was ascending, its new state is descending, and vice versa.
     ***************************************************************************/
    public void sort() {
        Collections.sort(dataVector, this);
        Integer val = (Integer) ascendCol.get(currCol);
        ascendCol.remove(currCol);
        if(val.equals(one)) // change the state of the column
            ascendCol.add(currCol, minusOne);
        else
            ascendCol.add(currCol, one);
    }

    public void sortByColumn(int column) {
        this.currCol = column;
        sort();
        fireTableChanged(new TableModelEvent(this));
    }

    // Add a mouse listener to the Table to trigger a table sort
    // when a column heading is clicked in the JTable.
    public void addMouseListenerToHeaderInTable(JTable table) {
        final CFlexStockyardTableModel sorter = this;
        final JTable tableView = table;
        tableView.setColumnSelectionAllowed(false);
        MouseAdapter listMouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                TableColumnModel columnModel = tableView.getColumnModel();
                int viewColumn = columnModel.getColumnIndexAtX(e.getX());
                int column = tableView.convertColumnIndexToModel(viewColumn);
                if (e.getClickCount() == 1 && column != -1) {
                    int shiftPressed = e.getModifiers()&InputEvent.SHIFT_MASK;
                    boolean ascending = (shiftPressed == 0);
                    
                    //sorter.sortByColumn(column);
                    sorter.sortByColumn(column);
                }
            }
        };
        JTableHeader th = tableView.getTableHeader();
        th.addMouseListener(listMouseListener);
    }
}

class Cell {
    int row;
    int col;
    public Cell(int i_row, int i_col) {
        row = i_row;
        col = i_col;
    }
    
}

