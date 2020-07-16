package com.hdntec.gestao.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.model.SelectItem;

import com.hdntec.gestao.domain.planta.comparadores.ComparadorBalizaNumero;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.enums.EnumTipoBaliza;
import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;
import com.hdntec.gestao.web.util.SessionUtil;


public class BalizaController {
	
	private Baliza baliza;
	private List<SelectItem> estadoEstruturaList = new ArrayList<SelectItem>();
	private List<SelectItem> tipoList = new ArrayList<SelectItem>();

	
	/********************************************
	 * Metodos da Baliza
	 ********************************************/
	public String gravaBaliza() {
		PatioController controller = (PatioController)SessionUtil.getSessionMapValue("patioController");
		List<Baliza> balizaList = null;//controller.getPatio().getListaDeBalizas();
		int numero = 1;
		if (balizaList != null && balizaList.size()>0) {
			Collections.sort(balizaList, new ComparadorBalizaNumero());
			Baliza ultimaBaliza = balizaList.get(balizaList.size()-1);
			numero = ultimaBaliza.getNumero();
		}
		
		Long qtdBalizas = controller.getQtdBalizas();
		/*for(int i=1; i<=qtdBalizas; i++) {
			Baliza balizaNova =  new  Baliza();
			balizaNova.geraClone(baliza,helper);
			balizaNova.setNomeBaliza(baliza.getNomeBaliza()+String.valueOf(numero+i));
			balizaNova.setNumero(numero+i);
			balizaNova.setPatio(controller.getPatio());
			balizaNova.setTipoBaliza(baliza.getTipoBaliza());
			controller.getPatio().addBaliza(balizaNova);
		}*/
		return "voltarBaliza";
	}
	
	/********************************************
	 * Metodos Getters e Setters
	 ********************************************/
	public Baliza getBaliza() {
		if (baliza == null) {
			baliza = new Baliza();
		}
		return baliza;
	}

	public void setBaliza(Baliza baliza) {
		this.baliza = baliza;
	}
	
	public List<SelectItem> getEstadoEstruturaList() {
		estadoEstruturaList.clear();
		for(EstadoMaquinaEnum estadoEnum : EstadoMaquinaEnum.values() ) {
			SelectItem item = new SelectItem(estadoEnum);
			estadoEstruturaList.add(item);
		}
		return estadoEstruturaList;
	}
	
	public void setEstadoEstruturaList(List<SelectItem> estadoEstruturaList) {
		this.estadoEstruturaList = estadoEstruturaList;
	}

	public List<SelectItem> getTipoList() {
		tipoList.clear();
		for(EnumTipoBaliza tipo :EnumTipoBaliza.values()) {
			SelectItem item = new SelectItem(tipo);
			tipoList.add(item);
		}
		return tipoList;
	}

	public void setTipoList(List<SelectItem> tipoList) {
		this.tipoList = tipoList;
	}
	
}