package br.com.cflex.samarco.supervision.stockyard.relatorio.controles;

import java.awt.Color;

import br.com.cflex.samarco.supervision.stockyard.relatorio.enums.EstiloFonteEnum;

import com.lowagie.text.Font;

public class Texto {
	
	private String conteudo;
	private Font font;

	public Texto(String conteudo) {
		this.conteudo = conteudo;
		this.font = new Font(Font.HELVETICA,9,Font.NORMAL);
	}
	
	public Texto(String conteudo,
			EstiloFonteEnum estilo,
			float tamanhoFonte,
			Color corFonte) {
		this.conteudo = conteudo;
		this.font = new Font(Font.HELVETICA,tamanhoFonte,estilo.getEstilo(),
				corFonte);
	}
	
	/**
	 * @return the conteudo
	 */
	public String getConteudo() {
		return conteudo;
	}

	/**
	 * @param conteudo the conteudo to set
	 */
	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}
	
	public double getDoubleValue() {
		double result = 0;
		try {
			result = Double.parseDouble(conteudo);
		}catch(NumberFormatException nfe) {
			result = 0;
		}
		return result;
	}

	/**
	 * Get font
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 26/08/2009
	 */
	public Font getFont() {
		return font;
	}
	
}
