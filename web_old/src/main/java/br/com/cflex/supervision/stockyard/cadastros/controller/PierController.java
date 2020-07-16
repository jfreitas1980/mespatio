package br.com.cflex.supervision.stockyard.cadastros.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.commons.beanutils.BeanUtils;
import org.richfaces.component.UIDataTable;

import br.com.cflex.supervision.stockYard.servidor.modelo.plano.SituacaoPatio;
import br.com.cflex.supervision.stockYard.servidor.modelo.planta.Berco;
import br.com.cflex.supervision.stockYard.servidor.modelo.planta.CarregadoraDeNavios;
import br.com.cflex.supervision.stockYard.servidor.modelo.planta.EstadoMaquinaEnumimport br.com.cflex.supervision.stockYard.servidor.modelo.planta.Pier;
import br.com.cflex.supervision.stockyard.util.SessionUtil;

public class PierController {

	private UIDataTable dataTableCarregadora;
	private UIDataTable dataTableBerco;
	private Pier pier;
	private CarregadoraDeNavios carregadora;
	//private CarregadoraDeNavios carregadoraClone;
	private Berco berco;
	//private Berco bercoClone;
	private List<SelectItem> estadoEstruturaList = new ArrayList<SelectItem>();
	Object objParaExcluir;
	
	/********************************
     * Pier
     ********************************/
	public String gravaPier() {
		PlantaController plantaController = (PlantaController)SessionUtil.getSessionMapValue("plantaController");
		SituacaoPatio situacaoPatio = plantaController.getSituacaoPatio();
		Pier pierAnterior = plantaController.getPier();
		boolean removeu = situacaoPatio.getPlanta().getListaPiers().remove(pierAnterior);
		situacaoPatio.getPlanta().getListaPiers().add(pier);
		return "voltarPier";
	}
	
	/********************************
     * Exclusao de objetos
     ********************************/    
	public void selectBerco() {
    	objParaExcluir = dataTableBerco.getRowData();
    }
    
    public void selectCarregadora() {
    	objParaExcluir = dataTableCarregadora.getRowData();
    }
    
    public String excluiObjeto() {
    	if (objParaExcluir instanceof Berco) {
    		removeBerco();
    	}
    	else if (objParaExcluir instanceof CarregadoraDeNavios) {
    		removeCarregadora();
    	}
    	return null;
    }

	/********************************
     * Carregadora de Navios
     ********************************/
	public String novaCarregadora() {
		carregadora = new CarregadoraDeNavios();
    	//carregadoraClone = new CarregadoraDeNavios();
    	Object obj = SessionUtil.getSessionMapValue("carregadoraDeNaviosController");
    	if (obj instanceof CarregadoraDeNaviosController) { 
    		CarregadoraDeNaviosController carregadoraDeNaviosController = (CarregadoraDeNaviosController)obj;
    		carregadoraDeNaviosController.setCarregadora(carregadora);
    	}
        return "atualizarCarregadora";
    }
    
    public String alteraCarregadora() {
    	carregadora = (CarregadoraDeNavios)dataTableCarregadora.getRowData();
    	try {
			//carregadoraClone = (CarregadoraDeNavios)BeanUtils.cloneBean(carregadora);
			/*CloneHelper cloneHelper = new CloneHelper();
			carregadoraClone = new CarregadoraDeNavios(carregadora,cloneHelper);*/
			Object obj = SessionUtil.getSessionMapValue("carregadoraDeNaviosController");
			if (obj instanceof CarregadoraDeNaviosController) { 
				CarregadoraDeNaviosController carregadoraDeNaviosController = (CarregadoraDeNaviosController)obj;
				carregadoraDeNaviosController.setCarregadora(carregadora);
			}
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return "atualizarCarregadora";
    }

    public String removeCarregadora() {
    	//CarregadoraDeNavios carregadoraExclusao = (CarregadoraDeNavios)dataTableCarregadora.getRowData();
    	CarregadoraDeNavios carregadoraExclusao = (CarregadoraDeNavios)objParaExcluir;
    	this.getPier().getListaDeCarregadoraDeNavios().remove(carregadoraExclusao);
        return "voltarCarregadora";
    } 
	
	/********************************
     * Berco
     ********************************/
	public String novoBerco() {
		berco = new Berco();
    	//bercoClone = new Berco();
    	Object obj = SessionUtil.getSessionMapValue("bercoController");
    	if (obj instanceof BercoController) { 
    		BercoController bercoController = (BercoController)obj;
    		bercoController.setBerco(berco);
    	}
        return "atualizarBerco";
    }
    
    public String alteraBerco() {
    	berco = (Berco)dataTableBerco.getRowData();
    	try {
			//bercoClone = (Berco)BeanUtils.cloneBean(berco); //nao usa o CloneHelper porque o CloneHelper nao funciona sem o ID.
			/*CloneHelper cloneHelper = new CloneHelper();
			bercoClone = new Berco(berco,cloneHelper);*/
			Object obj = SessionUtil.getSessionMapValue("bercoController");
			if (obj instanceof BercoController) { 
				BercoController bercoController = (BercoController)obj;
				bercoController.setBerco(berco);
			}   
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return "atualizarBerco";
    }

    public String removeBerco() {
    	//Berco bercoExclusao = (Berco)dataTableBerco.getRowData();
    	Berco bercoExclusao = (Berco)objParaExcluir;
    	this.getPier().getListaDeBercosDeAtracacao().remove(bercoExclusao);
        return "voltarBerco";
    }
	
    
    /********************************
     * Getters e Setters
     ********************************/
	public UIDataTable getDataTableCarregadora() {
		return dataTableCarregadora;
	}
	
	public void setDataTableCarregadora(UIDataTable dataTableCarregadora) {
		this.dataTableCarregadora = dataTableCarregadora;
	}
	
	public UIDataTable getDataTableBerco() {
		return dataTableBerco;
	}
	
	public void setDataTableBerco(UIDataTable dataTableBerco) {
		this.dataTableBerco = dataTableBerco;
	}
	
	public Pier getPier() {
		if (pier == null) {
			PlantaController plantaController = (PlantaController)SessionUtil.getSessionMapValue("plantaController");
			if (plantaController.getPier() != null) {
				pier = plantaController.getPier();
			}
			else {
				pier = new Pier();
			}
		}
		return pier;
	}
	
	public void setPier(Pier pier) {
		this.pier = pier;
	}
	
	public List<SelectItem> getEstadoEstruturaList() {
		estadoEstruturaList.clear();
		for(EstEstadoMaquinaEnumoEnum : EstadoEstadoMaquinaEnum ) {
			SelectItem item = new SelectItem(estadoEnum);
			estadoEstruturaList.add(item);
		}
		return estadoEstruturaList;
	}
	
	public void setEstadoEstruturaList(List<SelectItem> estadoEstruturaList) {
		this.estadoEstruturaList = estadoEstruturaList;
	}

	public CarregadoraDeNavios getCarregadora() {
		return carregadora;
	}

	public void setCarregadora(CarregadoraDeNavios carregadora) {
		this.carregadora = carregadora;
	}

/*	public CarregadoraDeNavios getCarregadoraClone() {
		return carregadoraClone;
	}

	public void setCarregadoraClone(CarregadoraDeNavios carregadoraClone) {
		this.carregadoraClone = carregadoraClone;
	}*/

	public Berco getBerco() {
		return berco;
	}

	public void setBerco(Berco berco) {
		this.berco = berco;
	}

/*	public Berco getBercoClone() {
		return bercoClone;
	}

	public void setBercoClone(Berco bercoClone) {
		this.bercoClone = bercoClone;
	}*/
}