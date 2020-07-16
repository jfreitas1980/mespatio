package br.com.cflex.samarco.supervision.stockyard.relatorio.controles;

import java.awt.Image;


public class Titulo {
	
	private Texto titulo;
	private Texto subTitulo;
	private Imagem logo;
	
	public Titulo(String strTitulo, String subTitulo, Image logo) {
		this.titulo = new Texto(strTitulo);
		this.subTitulo = new Texto(subTitulo);
		this.logo = new Imagem(logo);
	}
	
	/**
	 * @return the titulo
	 */
	public Texto getTitulo() {
		return titulo;
	}
	/**
	 * @param titulo the titulo to set
	 */
	public void setTitulo(Texto titulo) {
		this.titulo = titulo;
	}
	/**
	 * @return the subTitulo
	 */
	public Texto getSubTitulo() {
		return subTitulo;
	}
	/**
	 * @param subTitulo the subTitulo to set
	 */
	public void setSubTitulo(Texto subTitulo) {
		this.subTitulo = subTitulo;
	}
	/**
	 * @return the logo
	 */
	public Imagem getLogo() {
		return logo;
	}
	/**
	 * @param logo the logo to set
	 */
	public void setLogo(Imagem logo) {
		this.logo = logo;
	}
	
}
