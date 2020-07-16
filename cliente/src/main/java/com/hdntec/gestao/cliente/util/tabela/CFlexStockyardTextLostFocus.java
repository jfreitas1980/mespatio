package com.hdntec.gestao.cliente.util.tabela;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

import com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable;


//adiciona o editor da celula para que quando o usuário tirar o foco  
//da célula ela não perca o conteúdo  
public class CFlexStockyardTextLostFocus extends AbstractCellEditor implements TableCellEditor, FocusListener 
{  
    // Essa Classe interna serve para Dizer quando a celula foi alterada e o seu respectivo valor  
		JComponent component = null;  
       CFlexStockyardJTable table; 
         
       public CFlexStockyardTextLostFocus(CFlexStockyardJTable table)  
       {      	       	       	   
    	   this.table = table;
    	   this.component = new JTextField();
    	   getComponent().addFocusListener(this); 
       }  
       // This method is called when a cell value is edited by the user.  
       public Component getTableCellEditorComponent(JTable table, Object value,  
               boolean isSelected, int rowIndex, int vColIndex)   
       {  
        // 'value'é o valor contido na celula que esta na localizacao ((rowIndex, vColIndex)  
          
           if (isSelected) {  
               // cell (and perhaps other cells) are selected  
        	   this.table.getModel().  
               setValueAt(value,  
            		   rowIndex,  
            		   vColIndex);
               
           }  
     
           // Configure the component with the specified value  
           ((JTextField)getComponent()).setText((String)value);  
     
           this.table.getModel().  
           setValueAt(value,  
        		   rowIndex,  
        		   vColIndex);
           
           // Return the configured component  
           return getComponent();  
       }  
     
       // This method is called when editing is completed.  
       // It must return the new value to be stored in the cell.  
       public Object getCellEditorValue() {       
    	   return ((JTextField)getComponent()).getText();  
       }
	
	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub	
	    String value = ((JTextField)getComponent()).getText();    		
		this.table.getModel().  
                    setValueAt(value,  
                    		this.table.getSelectedRow(),  
                    		this.table.getSelectedColumn());  
     }
	public JComponent getComponent() {
		return component;
	}
	public void setComponent(JComponent component) {
		this.component = component;
	}
	          
}  
     