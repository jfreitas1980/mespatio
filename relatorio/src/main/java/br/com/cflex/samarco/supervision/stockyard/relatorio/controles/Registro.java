package br.com.cflex.samarco.supervision.stockyard.relatorio.controles;

import java.util.ArrayList;
import java.util.List;


public class Registro {
	private Linha linha;
	private List<Celula> celulas;
	
	public Registro(Linha linha) {
		this.linha = linha;
	}
	
	/**
	 * @return the linha
	 */
	public Linha getLinha() {
		return linha;
	}
	/**
	 * @param linha the linha to set
	 */
	public void setLinha(Linha linha) {
		this.linha = linha;
	}
	/**
	 * @return the celulas
	 */
	public List<Celula> getCelulas() {
		if (celulas == null) {
			celulas = new ArrayList<Celula>();
		}
		return celulas;
	}
	
	public void addCelula(Celula celula) {
		if (celulas == null) {
			celulas = new ArrayList<Celula>();
		}
		this.celulas.add(celula);
	}
	
}
