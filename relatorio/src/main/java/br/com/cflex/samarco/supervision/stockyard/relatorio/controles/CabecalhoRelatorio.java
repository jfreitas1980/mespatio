package br.com.cflex.samarco.supervision.stockyard.relatorio.controles;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CabecalhoRelatorio {
	
	private Texto titulo;
	private Texto subTitulo;
	private Imagem logo;
	
	private String usuario;
	private String tipoRelatorio;
	private Date dataInicioPeriodo;
	private Date dataFimPeriodo;
	private Date dataSituacao;
	private int qtdPilhas;
	
	private int qtdColunas;
	private List<String> listaDeDados;
	
	public CabecalhoRelatorio() {
		listaDeDados = new ArrayList<String>();
		qtdColunas = 1;
	}
	
	public CabecalhoRelatorio(String strTitulo, String subTitulo, Image logo) {
		this.titulo = new Texto(strTitulo);
		this.subTitulo = new Texto(subTitulo);
		this.logo = new Imagem(logo);
		listaDeDados = new ArrayList<String>();
		qtdColunas = 1;
	}
	
	public void addColuna(String str) {
		listaDeDados.add(str);
	}
	
	public List<String> getListaDeDados() {
		return listaDeDados;
	}

	public void setListaDeDados(List<String> listaDeDados) {
		this.listaDeDados = listaDeDados;
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

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getTipoRelatorio() {
		return tipoRelatorio;
	}

	public void setTipoRelatorio(String tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}

	public Date getDataInicioPeriodo() {
		return dataInicioPeriodo;
	}

	public void setDataInicioPeriodo(Date dataInicioPeriodo) {
		this.dataInicioPeriodo = dataInicioPeriodo;
	}

	public Date getDataFimPeriodo() {
		return dataFimPeriodo;
	}

	public void setDataFimPeriodo(Date dataFimPeriodo) {
		this.dataFimPeriodo = dataFimPeriodo;
	}

	public Date getDataSituacao() {
		return dataSituacao;
	}

	public void setDataSituacao(Date dataSituacao) {
		this.dataSituacao = dataSituacao;
	}

	public int getQtdPilhas() {
		return qtdPilhas;
	}

	public void setQtdPilhas(int qtdPilhas) {
		this.qtdPilhas = qtdPilhas;
	}

	public int getQtdColunas() {
		return qtdColunas;
	}

	public void setQtdColunas(int qtdColunas) {
		this.qtdColunas = qtdColunas;
	}
	
}
