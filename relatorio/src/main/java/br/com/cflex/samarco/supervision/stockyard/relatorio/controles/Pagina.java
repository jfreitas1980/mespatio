package br.com.cflex.samarco.supervision.stockyard.relatorio.controles;

import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;

public class Pagina {

	private Rectangle tamanho = PageSize.A4;
	private boolean orientacao = true;
	private int numeroPagina;
	
	/**
	 * @return the tamanho
	 */
	public Rectangle getTamanho() {
		return tamanho;
	}
	/**
	 * @param tamanho the tamanho to set
	 */
	public void setTamanho(Rectangle tamanho) {
		this.tamanho = tamanho;
	}
	/**
	 * @return the orientacao
	 */
	public boolean isOrientacao() {
		return orientacao;
	}
	/**
	 * @param orientacao the orientacao to set
	 */
	public void setOrientacao(boolean orientacao) {
		this.orientacao = orientacao;
	}
	/**
	 * @return the numeroPagina
	 */
	public int getNumeroPagina() {
		return numeroPagina;
	}
	/**
	 * @param numeroPagina the numeroPagina to set
	 */
	public void setNumeroPagina(int numeroPagina) {
		this.numeroPagina = numeroPagina;
	}
}
