package com.hdntec.gestao.domain.vo.atividades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.hdntec.gestao.domain.planta.entity.status.Baliza;


public class EdicaoVO implements Serializable {

	/**
	 * 
	 */
	private List<Baliza> listaBalizas;	
    private Date dataInicio;
	private Date dataFim;
	private String nomePilha;
	
	public EdicaoVO() 
	{	
	}

	public List<Baliza> getListaBalizas() {
		return listaBalizas;
	}

	public void setListaBalizas(List<Baliza> listaBalizas) {
		this.listaBalizas = listaBalizas;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

    public String getNomePilha() {
        return nomePilha;
    }

    public void setNomePilha(String nomePilha) {
        this.nomePilha = nomePilha;
    }

	
		
}
