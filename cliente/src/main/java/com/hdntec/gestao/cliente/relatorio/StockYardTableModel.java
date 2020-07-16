package com.hdntec.gestao.cliente.relatorio;

import javax.swing.table.DefaultTableModel;

public class StockYardTableModel extends DefaultTableModel {

	private static final long serialVersionUID = 1L;

	public StockYardTableModel() {
		super();
	}

	public StockYardTableModel(String[] titles, int i) {
		super(titles, i);
	}

	@Override
    public boolean isCellEditable(int i, int j) {
		return false;
	}
}
