package com.hdntec.gestao.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Patio;
import com.hdntec.gestao.domain.planta.entity.status.Usina;
import com.hdntec.gestao.web.util.SessionUtil;


public class UsinaController {

	private Usina usina;
	private List<SelectItem> estadoEstruturasList = new ArrayList<SelectItem>();
	private List<SelectItem> patioList = new ArrayList<SelectItem>();
	
	public String gravaUsina() {
		PlantaController plantaController = (PlantaController)SessionUtil.getSessionMapValue("plantaController");
		SituacaoPatio situacaoPatio = null;//plantaController.getSituacaoPatio();
		//Usina usinaAnterior = plantaController.getUsina();
		//boolean removeu = situacaoPatio.getPlanta().getListaUsinas().remove(usinaAnterior);
		//situacaoPatio.getPlanta().addUsina(usina);
		return "voltarUsina";
	}
	
	public Usina getUsina() {
		if (usina == null) {
			PlantaController plantaController = (PlantaController)SessionUtil.getSessionMapValue("plantaController");
			if (plantaController.getUsina() != null) {
				usina = plantaController.getUsina();
			}
			else {
				usina = new Usina();
			}
		}
		return usina;
	}
	public void setUsina(Usina usina) {
		this.usina = usina;
	}
	public List<SelectItem> getEstadoEstruturasList() {
		estadoEstruturasList.clear();
		/*for (EstadoMaquinaEnumoEnum : doEstadoMaquinaEnum) {
			SelectItem item = new SelectItem(estadoEnum);
			estadoEstruturasList.add(item);
		}*/
		return estadoEstruturasList;
	}
	public void setEstadoEstruturasList(List<SelectItem> estadoEstruturasList) {
		this.estadoEstruturasList = estadoEstruturasList;
	}

	public List<SelectItem> getPatioList() {
		patioList.clear();
		PlantaController plantaController = (PlantaController)SessionUtil.getSessionMapValue("plantaController");
	/*	for (Patio patio : plantaController.getSituacaoPatio().getPlanta().getListaPatios()) {
			SelectItem item = new SelectItem(patio,patio.getNomePatio());
			patioList.add(item);
		}*/
		return patioList;
	}

	public void setPatioList(List<SelectItem> patioList) {
		this.patioList = patioList;
	}
	
}
