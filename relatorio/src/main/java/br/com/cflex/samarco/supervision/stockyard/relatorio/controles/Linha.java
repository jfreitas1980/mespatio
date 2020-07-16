package br.com.cflex.samarco.supervision.stockyard.relatorio.controles;

import java.util.ArrayList;
import java.util.List;


public class Linha {

	private int numero;
	private List<Celula> celulas;
	
	/**
	 * 
	 * @param numero
	 */
	public Linha(int numero) {
		this.numero = numero;
	}
	
	/**
	 * @return the numero
	 */
	public int getNumero() {
		return numero;
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
	/**
	 * @param celulas the celulas to set
	 */
	public void addCelula(Celula celula) {
		if (celulas == null) {
			celulas = new ArrayList<Celula>();
		}
		this.celulas.add(celula);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + numero;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Linha other = (Linha) obj;
		if (numero != other.numero)
			return false;
		return true;
	}

	
}
