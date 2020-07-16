package br.com.cflex.samarco.supervision.stockyard.relatorio.controles;

import java.util.ArrayList;
import java.util.List;

public class Complemento {

	private List<Linha> linhas;

	/**
	 * @return the linhas
	 */
	public List<Linha> getLinhas() {
		if (linhas == null) {
			linhas = new ArrayList<Linha>();
		}
		return linhas;
	}

	/**
	 * @param linhas the linhas to set
	 */
	public void setLinhas(List<Linha> linhas) {
		this.linhas = linhas;
	}
	/**
	 * @param registros the registros to set
	 */
	public void addLinha(Linha linha) {
		if (linhas == null) {
			linhas = new ArrayList<Linha>();
		}
		this.linhas.add(linha);
	}
}
