/**
 * Created on 25/08/2009
 * Project : CFlexRS
 *
 * Copyright © 2008 CFLEX.
 * Brasil
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of CFLEX. 
 * 
 * $Id: codetemplates.xml,v 1.1 2008/02/14 18:38:19 Exp $
 */

package com.hdntec.gestao.domain.relatorio;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.hdntec.gestao.domain.AuditTrail;
import com.hdntec.gestao.domain.produto.enums.TipoDeProdutoEnum;
import com.hdntec.gestao.util.BundleUtils;
import com.hdntec.gestao.util.PropertiesUtil;


/**
 * <P><B>Description : Tabela de amostragem Fixa. Utilizada no relatorio de Planejamento de Carga</B><BR>
 * General TabelaAmostragemFrequencia
 * </P>
 * <P>
 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
 * @since 25/08/2009
 * @version $Revision: 1.1 $
 */
@Entity
@SuppressWarnings("serial")
public class TabelaAmostragemFrequencia extends AuditTrail {
	
	@Id
	@Column(nullable=false,length=30)
	private String codigo;
	
	@Column(nullable=false)
	private int revisao;
	
	@Column(nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
	private Date data;
	
	@Column(nullable=false, length=30)
	private String classificacao;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, 
    		mappedBy = "tabela")
    @Fetch(FetchMode.JOIN)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE)
	private List<ItemAmostragemFrequencia> listaItens;
	
	
	/**
	 * 
	 */
	public TabelaAmostragemFrequencia() {
		//Dados dessa tabela foram retiradas da norma descrita abaixo.
		/*codigo = "U-DEO - 0 - Q 05";//"U-DE-Q05";
		revisao = 1;
		Calendar calendar = Calendar.getInstance();
		calendar.set(2008, 11, 12);
		data = calendar.getTime();
		classificacao = "Reservada";
		//TODO analisar como recuperar as informacoes da norma de amostragem
		//TODO atualmente está hardcode.
		popularItensAmostragem();*/
	}
	
	/**
	 * Get codigo
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 25/08/2009
	 */
	public String getCodigo() {
		return codigo;
	}
	/**
	 * Change codigo	
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param codigo 
	 * @since 25/08/2009
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	/**
	 * Get revisao
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 25/08/2009
	 */
	public int getRevisao() {
		return revisao;
	}
	/**
	 * Change revisao	
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param revisao 
	 * @since 25/08/2009
	 */
	public void setRevisao(int revisao) {
		this.revisao = revisao;
	}
	/**
	 * Get data
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 25/08/2009
	 */
	public Date getData() {
		return data;
	}
	/**
	 * Change data	
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param data 
	 * @since 25/08/2009
	 */
	public void setData(Date data) {
		this.data = data;
	}
	/**
	 * Get classificacao
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 25/08/2009
	 */
	public String getClassificacao() {
		return classificacao;
	}
	/**
	 * Change classificacao	
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param classificacao 
	 * @since 25/08/2009
	 */
	public void setClassificacao(String classificacao) {
		this.classificacao = classificacao;
	}
	
	/**
	 * Get listaItens
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 25/08/2009
	 */
	public List<ItemAmostragemFrequencia> getListaItens() {
		if (listaItens == null) {
			listaItens = new ArrayList<ItemAmostragemFrequencia>();
		}
		return listaItens;
	}
	
	/**
	 * 
	 * setListaItens
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 28/08/2009
	 * @see
	 * @param itens
	 * @return Returns the void.
	 */
	public void setListaItens(List<ItemAmostragemFrequencia> itens) {
		this.listaItens = itens;
	}
	
	/**
	 * 
	 * addListaItens
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 28/08/2009
	 * @see
	 * @param item
	 * @return Returns the void.
	 */
	public void addItemTabela(ItemAmostragemFrequencia item) {
		if (listaItens == null) {
			listaItens = new ArrayList<ItemAmostragemFrequencia>();
		}
		if (listaItens.contains(item)) {
			listaItens.remove(item);
		}
		listaItens.add(item);
	}

	/**
	 * Recupera a faixa de amostragem e frequencia de acordo com o tipo do produto
	 * e também a cargaPrevista informada
	 * 
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 25/08/2009
	 * @see
	 * @param cargaPrevista
	 * @param tipoProduto
	 * @param codFamilia
	 * @return
	 * @return Returns the FaixaAmostragemFrequencia.
	 */
	public FaixaAmostragemFrequencia obterFaixa(double cargaPrevista,
			TipoDeProdutoEnum tipoProduto, String codFamilia) {

		FaixaAmostragemFrequencia faixa = null;

		for( ItemAmostragemFrequencia item : this.getListaItens()) {
			if (item.getTipoProduto().equals(tipoProduto)) {
				if (codFamilia==null || 
						codFamilia.equals(item.getCodigoFamiliaTipoProduto())) {
					faixa = recuperarFaixaAmostragem(item,cargaPrevista);
					if (faixa != null) {
						break;
					}
				}
			}
		}
		return faixa;
	}
	
	/**
	 * 
	 * recuperarFaixaAmostragem
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 25/08/2009
	 * @see
	 * @param item
	 * @param cargaPrevista
	 * @return
	 * @return Returns the FaixaAmostragemFrequencia.
	 */
	private FaixaAmostragemFrequencia recuperarFaixaAmostragem(
			ItemAmostragemFrequencia item,
			double cargaPrevista) {
		
		FaixaAmostragemFrequencia faixa = null;
		for (FaixaAmostragemFrequencia faixaAmostragem : item.getListaFaixas()) {
			
			if (faixaAmostragem.getFaixaTonelagemInicial()<=cargaPrevista &&
					cargaPrevista <=faixaAmostragem.getFaixaTonelagemFinal()) {
				faixa = faixaAmostragem;
				break;
			}
		}
		return faixa;
	}
	
	/**
	 * 
	 * popularItensAmostragem
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 25/08/2009
	 * @see
	 * @return Returns the void.
	 */
	private void popularItensAmostragem() {
		listaItens = new ArrayList<ItemAmostragemFrequencia>();
		List<ItemAmostragemFrequencia> pelotaPDR = popularListaItensPelotaPDR();
		List<ItemAmostragemFrequencia> pelotaPBF = popularListaItensPelotaPBF();
		List<ItemAmostragemFrequencia> sinterFeed = popularListaItensSinterFeed();
		List<ItemAmostragemFrequencia> pelletFeed = popularListaItensPelletFeed();
		listaItens.addAll(pelotaPDR);
		listaItens.addAll(pelotaPBF);
		listaItens.addAll(pelletFeed);
		listaItens.addAll(sinterFeed);
	}
	
	/**
	 * Metodo que popula os Itens da Pelota PDR de acordo com a norma.
	 * popularListaItensPelotaPDR
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 25/08/2009
	 * @see
	 * @return Returns the void.
	 */
	private List<ItemAmostragemFrequencia> popularListaItensPelotaPDR() {
		List<ItemAmostragemFrequencia> itens = new ArrayList<ItemAmostragemFrequencia>();
		ItemAmostragemFrequencia item = new ItemAmostragemFrequencia();
		item.setCodigoFamiliaTipoProduto("PDR");
		item.setTipoProduto(TipoDeProdutoEnum.PELOTA);
		item.setListaFaixas(popularListaFaixaPelota());
		itens.add(item);
		return itens;
	}
	/**
	 * Metodo que popula os Itens da Pelota PBF de acordo com a norma.
	 * popularListaItensPelotaPDR
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 25/08/2009
	 * @see
	 * @return Returns the void.
	 */
	private List<ItemAmostragemFrequencia> popularListaItensPelotaPBF() {
		List<ItemAmostragemFrequencia> itens = new ArrayList<ItemAmostragemFrequencia>();
		ItemAmostragemFrequencia item = new ItemAmostragemFrequencia();
		item.setCodigoFamiliaTipoProduto("PBF");
		item.setTipoProduto(TipoDeProdutoEnum.PELOTA);
		item.setListaFaixas(popularListaFaixaPelota());
		itens.add(item);
		return itens;
	}
	
	/**
	 * 
	 * popularListaItensPelletFeed
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 25/08/2009
	 * @see
	 * @return
	 * @return Returns the List<ItemAmostragemFrequencia>.
	 */
	private List<ItemAmostragemFrequencia> popularListaItensPelletFeed() {
		List<ItemAmostragemFrequencia> itens = new ArrayList<ItemAmostragemFrequencia>();
		ItemAmostragemFrequencia item = new ItemAmostragemFrequencia();
		item.setCodigoFamiliaTipoProduto("");
		item.setTipoProduto(TipoDeProdutoEnum.PELLET_FEED);
		item.setListaFaixas(popularListaFaixaPellet());
		itens.add(item);
		return itens;
	}

	/**
	 * 
	 * popularListaItensSinterFeed
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 25/08/2009
	 * @see
	 * @return
	 * @return Returns the List<ItemAmostragemFrequencia>.
	 */
	private List<ItemAmostragemFrequencia> popularListaItensSinterFeed() {
		List<ItemAmostragemFrequencia> itens = new ArrayList<ItemAmostragemFrequencia>();
		ItemAmostragemFrequencia item = new ItemAmostragemFrequencia();
		item.setCodigoFamiliaTipoProduto("");
		item.setTipoProduto(TipoDeProdutoEnum.SINTER_FEED);
		item.setListaFaixas(popularListaFaixaPellet());
		itens.add(item);
		return itens;
	}
	
	/**
	 * Metodo que popula as faixas da pelota
	 * popularListaFaixaPelota
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 25/08/2009
	 * @see
	 * @return
	 * @return Returns the List<FaixaAmostragemFrequencia>.
	 */
	private List<FaixaAmostragemFrequencia> popularListaFaixaPelota() {
		List<FaixaAmostragemFrequencia> lista = new ArrayList<FaixaAmostragemFrequencia>();
		FaixaAmostragemFrequencia faixa = new FaixaAmostragemFrequencia();
		//Primeira Faixa
		faixa.setFaixaTonelagemInicial(0.00);
		faixa.setFaixaTonelagemFinal(29999.99);
		faixa.setIncremento(400);
		faixa.setGranulometria(1600);
		faixa.setTamboramento(6400);
		lista.add(faixa);
		//Segunda Faixa
		faixa = new FaixaAmostragemFrequencia();
		faixa.setFaixaTonelagemInicial(30000.00);
		faixa.setFaixaTonelagemFinal(49999.99);
		faixa.setIncremento(500);
		faixa.setGranulometria(2000);
		faixa.setTamboramento(8000);
		lista.add(faixa);
		//Terceira Faixa
		faixa = new FaixaAmostragemFrequencia();
		faixa.setFaixaTonelagemInicial(50000.00);
		faixa.setFaixaTonelagemFinal(100000.00);
		faixa.setIncremento(750);
		faixa.setGranulometria(3000);
		faixa.setTamboramento(12000);
		lista.add(faixa);
		//Quarta Faixa
		faixa = new FaixaAmostragemFrequencia();
		faixa.setFaixaTonelagemInicial(100000.01);
		faixa.setFaixaTonelagemFinal(Double.MAX_VALUE);
		faixa.setIncremento(1000);
		faixa.setGranulometria(4000);
		faixa.setTamboramento(16000);
		lista.add(faixa);
		return lista;
	}
	
	/**
	 * Metodo que popula as faixas da pelota
	 * popularListaFaixaPelota
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 25/08/2009
	 * @see
	 * @return
	 * @return Returns the List<FaixaAmostragemFrequencia>.
	 */
	private List<FaixaAmostragemFrequencia> popularListaFaixaPellet() {
		List<FaixaAmostragemFrequencia> lista = new ArrayList<FaixaAmostragemFrequencia>();
		FaixaAmostragemFrequencia faixa = new FaixaAmostragemFrequencia();
		//Primeira Faixa
		faixa.setFaixaTonelagemInicial(2000.00);
		faixa.setFaixaTonelagemFinal(4999.99);
		faixa.setIncremento(125);
		faixa.setGranulometria(500);
		faixa.setTamboramento(2000);
		lista.add(faixa);
		//Segunda Faixa
		faixa = new FaixaAmostragemFrequencia();
		faixa.setFaixaTonelagemInicial(5000.00);
		faixa.setFaixaTonelagemFinal(29999.99);
		faixa.setIncremento(250);
		faixa.setGranulometria(1000);
		faixa.setTamboramento(4000);
		lista.add(faixa);
		//Terceira Faixa
		faixa = new FaixaAmostragemFrequencia();
		faixa.setFaixaTonelagemInicial(30000.00);
		faixa.setFaixaTonelagemFinal(44999.99);
		faixa.setIncremento(500);
		faixa.setGranulometria(2000);
		faixa.setTamboramento(8000);
		lista.add(faixa);
		//Quarta Faixa
		faixa = new FaixaAmostragemFrequencia();
		faixa.setFaixaTonelagemInicial(45000.00);
		faixa.setFaixaTonelagemFinal(100000.00);
		faixa.setIncremento(750);
		faixa.setGranulometria(3000);
		faixa.setTamboramento(12000);
		lista.add(faixa);
		//Quinta Faixa
		faixa = new FaixaAmostragemFrequencia();
		faixa.setFaixaTonelagemInicial(100000.01);
		faixa.setFaixaTonelagemFinal(Double.MAX_VALUE);
		faixa.setIncremento(1000);
		faixa.setGranulometria(4000);
		faixa.setTamboramento(16000);
		lista.add(faixa);
		return lista;
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((classificacao == null) ? 0 : classificacao.hashCode());
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + revisao;
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
		TabelaAmostragemFrequencia other = (TabelaAmostragemFrequencia) obj;
		if (classificacao == null) {
			if (other.classificacao != null)
				return false;
		} else if (!classificacao.equals(other.classificacao))
			return false;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (revisao != other.revisao)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		//BundleUtils.getMessage("formato.campo.data")
		SimpleDateFormat sdf = new SimpleDateFormat(PropertiesUtil.buscarPropriedade("formato.campo.data"));
		StringBuilder result = new StringBuilder();
		result.append(BundleUtils.getMessage("tabelaAmostragemFrequencia.codigo"));
		result.append(": ").append(codigo).append("\n");
		result.append(BundleUtils.getMessage("tabelaAmostragemFrequencia.revisao"));
		result.append(": ").append(revisao).append("\n");
		result.append(BundleUtils.getMessage("tabelaAmostragemFrequencia.data"));
		result.append(": ").append(sdf.format(data)).append("\n");
		result.append(BundleUtils.getMessage("tabelaAmostragemFrequencia.classificacao"));
		result.append(": ").append(classificacao);
		return result.toString();
	}
	
	
}
