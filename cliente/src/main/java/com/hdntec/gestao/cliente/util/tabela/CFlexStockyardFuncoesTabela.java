package com.hdntec.gestao.cliente.util.tabela;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.ValidacaoCampoException;

public class CFlexStockyardFuncoesTabela {

    private static void dimensionaColunas(List<ColunaTabela> i_colunas, CFlexStockyardJTable i_table) throws FuncoesTabelaException {

        try {
            CFlexStockyardTableModelCustom model = (CFlexStockyardTableModelCustom) i_table.getModel();
            TableColumn col;
            ColunaTabela colVO;
            for (int i = i_table.getNumFixedCol(); i < i_colunas.size(); i++) {
                colVO = i_colunas.get(i);
                col = i_table.getColumnModel().getColumn(i - i_table.getNumFixedCol());
                col.setHeaderValue(colVO.getTitulo());
                col.setWidth(colVO.getLargura());
                col.setPreferredWidth(colVO.getLargura());
                if (!colVO.getRedimensionar()) {
                    col.setMaxWidth(colVO.getLargura());
                    col.setMinWidth(colVO.getLargura());
                }
                col.setResizable(colVO.getRedimensionar());
                model.setColEditable(i - i_table.getNumFixedCol(), colVO.getEditar());
                model.setColAligment(i - i_table.getNumFixedCol(), colVO.getAlinhamento());

                if (colVO.getTipoEditor() == ColunaTabela.COL_TIPO_CHECKBOX) {
                    col.setCellEditor(new MyCheckBoxEditor());
                    col.setCellRenderer(new MyCheckBoxRenderer());
                } else if (colVO.getTipoEditor() == ColunaTabela.COL_TIPO_RADIOBUTTON) {
                    col.setCellRenderer(new RadioButtonRenderer());
                    col.setCellEditor(new RadioButtonEditor(new JCheckBox()));
                } else if (colVO.getTipoEditor() == ColunaTabela.COL_TIPO_TEXT) {
                    col.setCellRenderer(new MyTextFieldRenderer());
                    col.setCellEditor(new MyTextFieldEditor());
                } else if (colVO.getTipoEditor() == ColunaTabela.COL_TIPO_COMPONENTE) {
                    col.setCellRenderer(new CFlexStockyardCelularPersonalizada());
                    col.setCellEditor(new CFlexStockyardCelularPersonalizada());
                } else if (colVO.getTipoEditor() == ColunaTabela.COL_TIPO_TEXT_LOST_FOCUS) {
                    col.setCellRenderer(new MyTextFieldRenderer());
                    col.setCellEditor(new CFlexStockyardTextLostFocus(i_table));
                
                } else if (colVO.getTipoEditor() == ColunaTabela.COL_TIPO_COMBOBOX) {
                    if (colVO.getVItensCombo() == null) {
                        throw new FuncoesTabelaException("Vetor de Itens esta nulo.");
                    }
                    col.setCellEditor(new MyComboBoxEditor(colVO.getVItensCombo()));
                }
            }
        } catch (FuncoesTabelaException ex) {
            ex.printStackTrace();
            throw new FuncoesTabelaException("Erro no dimensionamento da tabela");
        }
    }

    private static void dimensionaColunasFixas(List<ColunaTabela> i_colunas, CFlexStockyardJTable i_table) throws FuncoesTabelaException {
        try {

            TableColumn col;
            ColunaTabela colVO;
            for (int i = 0; i < i_table.getNumFixedCol(); i++) {
                colVO = i_colunas.get(i);
                col = i_table.getColumnModel().getColumn(i);
                col.setHeaderValue(colVO.getTitulo());
                col.setWidth(colVO.getLargura());
                col.setPreferredWidth(colVO.getLargura());
                if (!colVO.getRedimensionar()) {
                    col.setMaxWidth(colVO.getLargura());
                    col.setMinWidth(colVO.getLargura());
                }
                col.setResizable(colVO.getRedimensionar());

                if (colVO.getTipoEditor() == ColunaTabela.COL_TIPO_CHECKBOX) {
                    col.setCellEditor(new MyCheckBoxEditor());
                    col.setCellRenderer(new MyCheckBoxRenderer());
                } else if (colVO.getTipoEditor() == ColunaTabela.COL_TIPO_RADIOBUTTON) {
                    col.setCellRenderer(new RadioButtonRenderer());
                    col.setCellEditor(new RadioButtonEditor(new JCheckBox()));
                } else if (colVO.getTipoEditor() == ColunaTabela.COL_TIPO_TEXT) {
                    col.setCellRenderer(new MyTextFieldRenderer());
                    col.setCellEditor(new MyTextFieldEditor());
                } else if (colVO.getTipoEditor() == ColunaTabela.COL_TIPO_COMPONENTE) {
                    col.setCellRenderer(new CFlexStockyardCelularPersonalizada());
                    col.setCellEditor(new CFlexStockyardCelularPersonalizada());
                } else if (colVO.getTipoEditor() == ColunaTabela.COL_TIPO_COMPONENTE) {
                    col.setCellRenderer(new MyTextFieldRenderer());
                    col.setCellEditor(new CFlexStockyardTextLostFocus(i_table));
                }   
            }
        } catch (FuncoesTabelaException ex) {
            ex.printStackTrace();
            throw new FuncoesTabelaException("Erro no dimensionamento da tabela");
        }
    }

    static class MyTextFieldEditor extends DefaultCellEditor {

        public MyTextFieldEditor() {
            super(new JTextField());
        }
    }

    static class MyTextFieldRenderer extends JTextField implements TableCellRenderer {

        public MyTextFieldRenderer() {
            super();
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                super.setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }
            setText(value.toString());
            
            
            return this;
        }
    }

    static class MyComboBoxEditor extends DefaultCellEditor {

        public MyComboBoxEditor(Vector items, int i) {
            super(new JComboBox(items));
        }

        public MyComboBoxEditor(Vector items) {
            super(new JComboBox(items));
        }
    }

    static class MyCheckBoxEditor extends DefaultCellEditor {

        public MyCheckBoxEditor() {
            super(new JCheckBox());
        }
    }

    static class MyCheckBoxRenderer extends JCheckBox implements TableCellRenderer {

        /**
       * 
       */
      private static final long serialVersionUID = 3257585817447132469L;

      public MyCheckBoxRenderer() {
            super();
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected && !hasFocus) {
                setForeground(table.getSelectionForeground());
                super.setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }
            if (value != null && value.toString().toUpperCase().equals("TRUE")) {
                setSelected(true);
            } else {
                setSelected(false);
            }
            return this;
        }
    }

    static class RadioButtonRenderer implements TableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            if (value == null) {
                return null;
            }

            return (Component) value;
        }
    }

    static class RadioButtonEditor extends DefaultCellEditor implements ItemListener {

        private JRadioButton button;

        public RadioButtonEditor(JCheckBox checkBox) {
            super(checkBox);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {

            if (value == null) {
                return null;
            }

            button = (JRadioButton) value;
            button.addItemListener(this);
            return (Component) value;
        }

        @Override
        public Object getCellEditorValue() {
            button.removeItemListener(this);
            return button;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            super.fireEditingStopped();
        }
    }

    private static Dimension getDimensionFixedTable(List<ColunaTabela> i_colunas, CFlexStockyardJTable i_table) {

        Dimension dimFixedTable;

        int largFixedTable = 0;
        int alturaFixedTable = 0;

        for (ColunaTabela colVO : i_colunas) {
            largFixedTable += colVO.getLargura();
        }

        alturaFixedTable = (int) (i_table.getPreferredSize()).getHeight();
        dimFixedTable = new Dimension(largFixedTable, alturaFixedTable);

        return dimFixedTable;
    }

    public static void setTabelaZebrada(JTable i_table, Color i_corZebra) {
        ((CFlexStockyardTableModelCustom) i_table.getModel()).setZebrado(true);
        ((CFlexStockyardTableModelCustom) i_table.getModel()).setZebradoColor(i_corZebra);
    }


    public static void setTabelaZebrada(CFlexStockyardJTable i_table, boolean i_zebrado) {
        ((CFlexStockyardTableModelCustom) i_table.getModel()).setZebrado(i_zebrado);
    }

    public static void setCoresLinhaTabela(CFlexStockyardJTable i_table, List<CorLinhasTabela> i_CorLinhas) {
        for (CorLinhasTabela corLinhaTabela : i_CorLinhas) {
            ((CFlexStockyardTableModelCustom) i_table.getModel()).setRowColor(corLinhaTabela.getNumeroLinha(), corLinhaTabela.getCorLinha());
        }
    }

    private static AbstractTableModel getFixedModel(CFlexStockyardTableModelCustom i_model, int i_numColFixed) {
        final CFlexStockyardTableModelCustom model = i_model;
        final int numFixedCol = i_numColFixed;
        AbstractTableModel fixedModel = new AbstractTableModel() {

            @Override
            public int getColumnCount() {
                return numFixedCol;
            }

            @Override
            public int getRowCount() {
                return model.getDataVector().size();
            }

            @Override
            public String getColumnName(int col) {
                col = col - numFixedCol;
                return model.getColumnName(col);
            }

            @Override
            public Object getValueAt(int row, int col) {
                col = col - numFixedCol;
                return model.getValueAt(row, col);
            }
        };
        return fixedModel;
    }

    private static JScrollPane getScrollPaneTable(CFlexStockyardJTable i_table) {
        JScrollPane scroll = null;
        Container cObj = null;

        for (cObj = i_table.getParent(); cObj != null; cObj = cObj.getParent()) {
            if (cObj instanceof JScrollPane) {
                scroll = (JScrollPane) cObj;
                break;
            }
        }

        return scroll;
    }

    public static void setLinhasTabela(CFlexStockyardJTable i_table, Vector i_dados, List<ColunaTabela> i_colunas) throws Exception {
        try {
            CFlexStockyardJTable fixedTable;

            int nroObjectRadio = 0;
            int indexGroup = 0;
            // Verificando quantas colunas possuem instancias de RadioButton
            Object[][] arrayDadosCount = new Object[i_dados.size()][i_colunas.size()];
            for (int i = 0; i < i_dados.size(); i++) {
                Object[] arrayLinha = new Object[i_colunas.size()];
                ((Vector)i_dados.elementAt(i)).toArray(arrayLinha);
                arrayDadosCount[i] = arrayLinha;
                for (int j = 0; j < arrayLinha.length; j++) {
                    Object objColuna = arrayLinha[j];
                    if (objColuna instanceof JRadioButton) {
                        nroObjectRadio++;
                    }
                }
            }

            ButtonGroup[] grpRadioTable = new ButtonGroup[nroObjectRadio];
            for (int i = 0; i < grpRadioTable.length; i++) {
                grpRadioTable[i] = new ButtonGroup();
            }

            Object[][] arrayDados = new Object[i_dados.size()][i_colunas.size()];
            for (int i = 0; i < i_dados.size(); i++) {
                Object[] arrayLinha = new Object[i_colunas.size()];
                ((Vector)i_dados.elementAt(i)).toArray(arrayLinha);
                arrayDados[i] = arrayLinha;
                indexGroup = 0;
                for (int j = 0; j < arrayLinha.length; j++) {
                    Object objColuna = arrayLinha[j];
                    if (objColuna instanceof JRadioButton) {
                        grpRadioTable[indexGroup].add((JRadioButton)objColuna);
                        indexGroup++;
                    }
                }
            }

            String[] colunasTabela = new String[i_colunas.size()];
            int i = 0;
            for (ColunaTabela coluna : i_colunas) {
                colunasTabela[i] = coluna.getTitulo();
                i++;
            }

            CFlexStockyardTableModelCustom model = new CFlexStockyardTableModelCustom(arrayDados, colunasTabela);
            if (i_table.getNumFixedCol() > 0) {
                model.setNumColFixed(i_table.getNumFixedCol());
            }

            i_table.setModel(model);

            TableCellRenderer renderer = model.getModelRenderer();
            i_table.setDefaultRenderer(Class.forName("java.lang.Object" ), renderer );

            model.addMouseListenerToHeaderInTable(i_table);

            if (i_table.getNumFixedCol() > 0) {
                fixedTable = new CFlexStockyardJTable(getFixedModel(model, i_table.getNumFixedCol()));
                fixedTable.setNumFixedCol(i_table.getNumFixedCol());
                fixedTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                fixedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                JScrollPane scrollTable = (JScrollPane)i_table.getParent();
                fixedTable.setPreferredSize(getDimensionFixedTable(i_colunas, i_table));
                JViewport viewport = new JViewport();
                viewport.setView(fixedTable);
                viewport.setPreferredSize(fixedTable.getPreferredSize());
                scrollTable.setRowHeaderView(viewport);
                scrollTable.setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER,fixedTable.getTableHeader());
                dimensionaColunasFixas(i_colunas, fixedTable);
            }

            dimensionaColunas(i_colunas, i_table);

        } catch (Exception ex) {
            throw new Exception(ex.getMessage() + " setDadosTabela");
        }
    }

    public  static void setDadosTabela(CFlexStockyardJTable i_table, List<List> i_dados, List<ColunaTabela> i_colunas) {
        CFlexStockyardJTable fixedTable;

        int nroObjectRadio = 0;
        int indexGroup = 0;
        // Verificando quantas colunas possuem instancias de RadioButton
        Object[][] arrayDadosCount = new Object[i_dados.size()][i_colunas.size()];
        for (int i = 0; i < i_dados.size(); i++) {
            Object[] arrayLinha = new Object[i_colunas.size()];
            i_dados.get(i).toArray(arrayLinha);
            arrayDadosCount[i] = arrayLinha;
            for (int j = 0; j < arrayLinha.length; j++) {
                Object objColuna = arrayLinha[j];
                if (objColuna instanceof JRadioButton) {
                    nroObjectRadio++;
                }
            }
        }

        ButtonGroup[] grpRadioTable = new ButtonGroup[nroObjectRadio];
        for (int i = 0; i < grpRadioTable.length; i++) {
            grpRadioTable[i] = new ButtonGroup();
        }

        ColunaTabela colVO;
        Object[][] arrayDados = new Object[i_dados.size()][i_colunas.size()];
        for (int i = 0; i < i_dados.size(); i++) {
            Object[] arrayLinha = new Object[i_colunas.size()];
            i_dados.get(i).toArray(arrayLinha);
            arrayDados[i] = arrayLinha;
            indexGroup = 0;
            for (int j = 0; j < arrayLinha.length; j++) {
                Object objColuna = arrayLinha[j];
                if (objColuna instanceof JRadioButton) {
                    grpRadioTable[indexGroup].add((JRadioButton) objColuna);
                    indexGroup++;
                }
            }
        }

        String[] colunasTabela = new String[i_colunas.size()];
        for (int i = 0; i < i_colunas.size(); i++) {
            colVO = i_colunas.get(i);
            colunasTabela[i] = colVO.getTitulo();
        }

        CFlexStockyardTableModelCustom model = new CFlexStockyardTableModelCustom(arrayDados, colunasTabela);
        if (i_table.getNumFixedCol() > 0) {
            model.setNumColFixed(i_table.getNumFixedCol());
        }

        i_table.setModel(model);

        TableCellRenderer renderer = model.getModelRenderer();
        try {
            i_table.setDefaultRenderer(Class.forName("java.lang.Object"), renderer);
        } catch (ClassNotFoundException classeNaoEncontradaEx) {
            classeNaoEncontradaEx.printStackTrace();
        }

        model.addMouseListenerToHeaderInTable(i_table);

        if (i_table.getNumFixedCol() > 0) {
            fixedTable = new CFlexStockyardJTable(getFixedModel(model, i_table.getNumFixedCol()));
            fixedTable.setNumFixedCol(i_table.getNumFixedCol());
            fixedTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            fixedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane scrollTable = CFlexStockyardFuncoesTabela.getScrollPaneTable(i_table);
            fixedTable.setPreferredSize(getDimensionFixedTable(i_colunas, i_table));
            JViewport viewport = new JViewport();
            viewport.setView(fixedTable);
            viewport.setPreferredSize(fixedTable.getPreferredSize());
            scrollTable.setRowHeaderView(viewport);
            scrollTable.setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, fixedTable.getTableHeader());
            dimensionaColunasFixas(i_colunas, fixedTable);
        }

        dimensionaColunas(i_colunas, i_table);
    }

    public static void setInformacoesTabela(CFlexStockyardJTable i_table, Vector i_dados, List<ColunaTabela> listaColunas) throws ErroSistemicoException {
        try {
            CFlexStockyardJTable fixedTable;

            int nroObjectRadio = 0;
            int indexGroup = 0;
            // Verificando quantas colunas possuem instancias de RadioButton
            Object[][] arrayDadosCount = new Object[i_dados.size()][listaColunas.size()];
            for (int i = 0; i < i_dados.size(); i++) {
                Object[] arrayLinha = new Object[listaColunas.size()];
                ((Vector)i_dados.elementAt(i)).toArray(arrayLinha);
                arrayDadosCount[i] = arrayLinha;
                for (int j = 0; j < arrayLinha.length; j++) {
                    Object objColuna = arrayLinha[j];
                    if (objColuna instanceof JRadioButton) {
                        nroObjectRadio++;
                    }
                }
            }

            ButtonGroup[] grpRadioTable = new ButtonGroup[nroObjectRadio];
            for (int i = 0; i < grpRadioTable.length; i++) {
                grpRadioTable[i] = new ButtonGroup();
            }

            Object[][] arrayDados = new Object[i_dados.size()][listaColunas.size()];
            for (int i = 0; i < i_dados.size(); i++) {
                Object[] arrayLinha = new Object[listaColunas.size()];
                ((Vector)i_dados.elementAt(i)).toArray(arrayLinha);
                arrayDados[i] = arrayLinha;
                indexGroup = 0;
                for (int j = 0; j < arrayLinha.length; j++) {
                    Object objColuna = arrayLinha[j];
                    if (objColuna instanceof JRadioButton) {
                        grpRadioTable[indexGroup].add((JRadioButton)objColuna);
                        indexGroup++;
                    }
                }
            }

            String[] colunasTabela = new String[listaColunas.size()];
            int i = 0;
            for (ColunaTabela colVO : listaColunas) {
                colunasTabela[i] = colVO.getTitulo();
                i++;
            }

            CFlexStockyardTableModelCustom model = new CFlexStockyardTableModelCustom(arrayDados, colunasTabela);
            if (i_table.getNumFixedCol() > 0) {
                model.setNumColFixed(i_table.getNumFixedCol());
            }

            i_table.setModel(model);

            TableCellRenderer renderer = model.getModelRenderer();
            i_table.setDefaultRenderer(Class.forName("java.lang.Object" ), renderer );

            model.addMouseListenerToHeaderInTable(i_table);

            if (i_table.getNumFixedCol() > 0) {
                fixedTable = new CFlexStockyardJTable(getFixedModel(model, i_table.getNumFixedCol()));
                fixedTable.setNumFixedCol(i_table.getNumFixedCol());
                fixedTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                fixedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                JScrollPane scrollTable = (JScrollPane)i_table.getParent();
                fixedTable.setPreferredSize(getDimensionFixedTable(listaColunas, i_table));
                JViewport viewport = new JViewport();
                viewport.setView(fixedTable);
                viewport.setPreferredSize(fixedTable.getPreferredSize());
                scrollTable.setRowHeaderView(viewport);
                scrollTable.setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER,fixedTable.getTableHeader());
                dimensionaColunasFixas(listaColunas, fixedTable);
            }

            dimensionaColunas(listaColunas, i_table);

        } catch (Exception ex) {
            throw new ErroSistemicoException(ex.getMessage());
        }
    }


    /**
     * Valida a seleção de um objeto tabela 
     * @param tabela
     * @throws br.com.cflex.supervision.stockYard.cliente.exception.ValidacaoCampoException
     */
    public static void validaSelecaoTabela(CFlexStockyardJTable tabela) throws ValidacaoCampoException {
        if (tabela.getSelectedRowCount() > 1) {
            throw new ValidacaoCampoException("Selecione um registro de cada vez.");
        }

        if (tabela.getSelectedRowCount() == 0) {
            throw new ValidacaoCampoException("Nenhum registro foi selecionado.");
        }
    }


}
