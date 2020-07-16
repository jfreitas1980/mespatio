package com.hdntec.gestao.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import com.hdntec.gestao.domain.planta.entity.MetaPier;
import com.hdntec.gestao.domain.planta.entity.status.Berco;
import com.hdntec.gestao.domain.planta.entity.status.Pier;
import com.hdntec.gestao.web.util.SessionUtil;


public class BercoController {
	private Berco berco;
	private List<SelectItem> estadoEstruturasList = new ArrayList<SelectItem>();
	//private List<SelectItem> navioList = new ArrayList<SelectItem>();
	
	/*@EJB(name = "dssStockYardSamarcoEAR/bs/ControladorNavios/local")
    private IControladorNavios navioService;*/

	/********************************
	 * Berco
	 ********************************/
	public String gravaBerco() {
		PierController pierController = (PierController) SessionUtil
				.getSessionMapValue("pierController");
		if (berco.getNavioAtracado()!= null) {
			berco.setNavioAtracado(null);
		}
		MetaPier pier = pierController.getPier();
		//Berco bercoAnterior = pierController.getBerco();
		//boolean removeu = pier.getListaDeBercosDeAtracacao().remove(bercoAnterior);
		//pier.addBerco(berco);
		return "voltarBerco";
	}

	/********************************
	 * Getters e Setters
	 ********************************/
	
	public Berco getBerco() {
		if (berco == null) {
			PierController pierController = (PierController)SessionUtil.getSessionMapValue("pierController");
			if (pierController.getBerco() != null) {
				berco = pierController.getBerco();
			}
			else {
				berco = new Berco();
			}
		}
		return berco;
	}

	public void setBerco(Berco berco) {
		this.berco = berco;
	}
	
	public List<SelectItem> getEstadoEstruturasList() {
		estadoEstruturasList.clear();
		/*for (EstEstadoMaquinaEnumoEnum : EstadoEstadoMaquinaEnum) {
			SelectItem item = new SelectItem(estadoEnum);
			estadoEstruturasList.add(item);
		}*/
		return estadoEstruturasList;
	}

	public void setEstadoEstruturasList(List<SelectItem> estadoEstruturasList) {
		this.estadoEstruturasList = estadoEstruturasList;
	}

	/*public List<SelectItem> getNavioList() {
		try {
			navioList.clear();
			List<Navio> lista = navioService.buscaNavio(new Navio());
			for(Navio navio : lista) {
				NavioUtil.addNavio(navio);
				SelectItem item = new SelectItem(navio,navio.getNomeNavio());
				navioList.add(item);
			}
		} catch (ErroSistemicoException e) {
			e.printStackTrace();
		}
		return navioList;
	}

	public void setNavioList(List<SelectItem> navioList) {
		this.navioList = navioList;
	}*/
}
