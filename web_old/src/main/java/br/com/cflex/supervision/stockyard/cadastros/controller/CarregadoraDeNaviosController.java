package br.com.cflex.supervision.stockyard.cadastros.controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import br.com.cflex.supervision.stockYard.servidor.modelo.planta.CarregadoraDeNavios;
import br.com.cflex.supervision.stockYard.servidor.modelo.planta.EstadoMaquinaEnum;
import br.com.cflex.supervision.stockYard.servidor.modelo.planta.Pier;
import br.com.cflex.supervision.stockyard.util.SessionUtil;

public class CarregadoraDeNaviosController {

	private CarregadoraDeNavios carregadora;
	private List<SelectItem> estadoMaquinaList = new ArrayList<SelectItem>();

	/********************************
	 * CarregadoraDeNavios
	 ********************************/
	public String gravaCarregadoraDeNavios() {
		PierController pierController = (PierController) SessionUtil.getSessionMapValue("pierController");
		Pier pier = pierController.getPier();		
		pier.addCarregadoraNavio(carregadora);
		return "voltarCarregadora";
	}

	/********************************
	 * Getters e Setters
	 ********************************/
	public CarregadoraDeNavios getCarregadora() {
		if (carregadora == null) {
			PierController pierController = (PierController)SessionUtil.getSessionMapValue("pierController");
			if (pierController.getCarregadora() != null) {
				carregadora = pierController.getCarregadora();
			}
			else {
				carregadora = new CarregadoraDeNavios();
			}
		}
		return carregadora;
	}

	public void setCarregadora(CarregadoraDeNavios carregadora) {
		this.carregadora = carregadora;
	}

	public List<SelectItem> getEstadoMaquinaList() {
		estadoMaquinaList.clear();
		for (EstadoMaquinaEnum estadoEnum : EstadoMaquinaEnum.values()) {
			SelectItem item = new SelectItem(estadoEnum);
			estadoMaquinaList.add(item);
		}
		return estadoMaquinaList;
	}

	public void setEstadoMaquinaList(List<SelectItem> estadoMaquinaList) {
		this.estadoMaquinaList = estadoMaquinaList;
	}
}
