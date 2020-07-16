package com.hdntec.gestao.domain.relatorio;

import java.util.HashMap;
import java.util.Map;

public class PlanoRecuperacaoVO {
	
	private String nomePatio;
	private String nomePilha;
	private String intervaloBalizas;
	private Double tonelada;
    private Map<String,Double> itensDeControle;
    
	public String getNomePatio() {
		return nomePatio;
	}
	public void setNomePatio(String nomePatio) {
		this.nomePatio = nomePatio;
	}
	public String getNomePilha() {
		return nomePilha;
	}
	public void setNomePilha(String nomePilha) {
		this.nomePilha = nomePilha;
	}
	public String getIntervaloBalizas() {
		return intervaloBalizas;
	}
	public void setIntervaloBalizas(String intervaloBalizas) {
		this.intervaloBalizas = intervaloBalizas;
	}
	public Double getTonelada() {
		return tonelada;
	}
	public void setTonelada(Double tonelada) {
		this.tonelada = tonelada;
	}
	public Map<String, Double> getItensDeControle() {
		if (itensDeControle == null) {
			itensDeControle = new HashMap<String,Double>();
		}
		return itensDeControle;
	}
	public void setItensDeControle(Map<String, Double> itensDeControle) {
		this.itensDeControle = itensDeControle;
	}
    
	public void addItensDeControle(String campo, Double valor) {
		if (itensDeControle == null) {
			itensDeControle = new HashMap<String,Double>();
		}
		itensDeControle.put(campo, valor);
	}
	
	public Double getValorItensDeControle(String campo) {
		if (itensDeControle == null) {
			itensDeControle = new HashMap<String,Double>();
		}
		return itensDeControle.get(campo);
	}
    
}
