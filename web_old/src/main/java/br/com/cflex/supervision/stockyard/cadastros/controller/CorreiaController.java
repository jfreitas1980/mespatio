package br.com.cflex.supervision.stockyard.cadastros.controller;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import org.richfaces.component.UIDataTable;

import br.com.cflex.supervision.stockYard.servidor.modelo.plano.SituacaoPatio;
import br.com.cflex.supervision.stockYard.servidor.modelo.planta.Correia;
import br.com.cflex.supervision.stockYard.servidor.modelo.planta.EstadoMaquinaEnum;
import br.com.cflex.supervision.stockYard.servidor.modelo.planta.IControladorPlanta;
import br.com.cflex.supervision.stockYard.servidor.modelo.planta.MaquinaDoPatio;
import br.com.cflex.supervision.stockYard.servidor.modelo.planta.Patio;
import br.com.cflex.supervision.stockYard.servidor.modelo.planta.Planta;
import br.com.cflex.supervision.stockyard.util.PatioUtil;
import br.com.cflex.supervision.stockyard.util.SessionUtil;

public class CorreiaController {

	private Correia correia;
	private MaquinaDoPatio maquina;
	//private MaquinaDoPatio maquinaClone;
	private List<SelectItem> estadoMaquinaList = new ArrayList<SelectItem>();
	private List<SelectItem> patioList = new ArrayList<SelectItem>();
	
	private UIDataTable dataTableMaquina;
	
	@EJB(name = "dssStockYardSamarcoEAR/bs/ControladorPlanta/local")
    private IControladorPlanta plantaService;

	
	/********************************
	 * Correia
	 ********************************/
	public String gravaCorreia() {
		PlantaController plantaController = (PlantaController)SessionUtil.getSessionMapValue("plantaController");
		SituacaoPatio situacaoPatio = plantaController.getSituacaoPatio();
		//Correia correiaAnterior = plantaController.getCorreia();
		//boolean removeu = situacaoPatio.getPlanta().getListaCorreias().remove(correiaAnterior);
		//validaPosicaoMaquinasDaCorreia();
		situacaoPatio.getPlanta().addCorreia(correia);
		return "voltarCorreia";
	}
	
	private void validaPosicaoMaquinasDaCorreia() {
		for(MaquinaDoPatio maquina : correia.getListaDeMaquinas()) {
			if (maquina.getPosicao()!= null) {
				Patio patio = maquina.obterPatioDeLocalizacao();
				if (!patio.getListaDeBalizas().contains(maquina.getPosicao())) {
					maquina.setPosicao(null);
				}
			}
		}
	}
	
	/********************************
	 * Maquina Do Patio
	 ********************************/	
    
	public String novaMaquina() {
		maquina = new MaquinaDoPatio();
		//maquinaClone = new MaquinaDoPatio();
		//maquinaClone.setCorreia(getCorreia());
    	Object obj = SessionUtil.getSessionMapValue("maquinaDoPatioController");
    	if (obj instanceof MaquinaDoPatioController) { 
    		MaquinaDoPatioController maquinaDoPatioController = (MaquinaDoPatioController)obj;
    		maquinaDoPatioController.setMaquina(maquina);
    		maquina.setCorreia(getCorreia());    		                           
    		maquinaDoPatioController.setOrigemCadastro(null);
    		
    	}
        return "atualizarMaquinaCorreia";
    }
    
	public String alteraMaquina() {
    	maquina = (MaquinaDoPatio)dataTableMaquina.getRowData();
    	//CloneHelper cloneHelper = new CloneHelper();
    	//maquinaClone = new MaquinaDoPatio();
    	//maquinaClone.geraClone(maquina,cloneHelper);
    	Object obj = SessionUtil.getSessionMapValue("maquinaDoPatioController");
    	if (obj instanceof MaquinaDoPatioController) { 
    		MaquinaDoPatioController maquinaDoPatioController = (MaquinaDoPatioController)obj;
    		maquinaDoPatioController.setMaquina(maquina);
    		maquinaDoPatioController.setOrigemCadastro(null);
    	}
        return "atualizarMaquinaCorreia";
    }

    public String removeMaquina() {    	
    	this.getCorreia().getListaDeMaquinas().remove(getMaquina());
        return "voltarMaquinaCorreia";
    } 
    
    public void selectMaquina() {
        maquina = (MaquinaDoPatio)dataTableMaquina.getRowData();
    }
    
	public Correia getCorreia() {
		if (correia == null) {
			PlantaController plantaController = (PlantaController)SessionUtil.getSessionMapValue("plantaController");
			if (plantaController.getCorreia() != null) {
				correia = plantaController.getCorreia();
			}
			else {
				correia = new Correia();
			}
		}
		return correia;
	}

	public void setCorreia(Correia correia) {
		this.correia = correia;
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

	public List<SelectItem> getPatioList() {
		patioList.clear();
		patioList.add(new SelectItem(null,""));
		PlantaController plantaController = (PlantaController)SessionUtil.getSessionMapValue("plantaController");
		Planta planta = plantaController.getSituacaoPatio().getPlanta();
		List<Patio> lista = planta.getListaPatios();		
		for(Patio patio : lista) {
			PatioUtil.addPatio(patio);
			SelectItem item = new SelectItem(patio,patio.getNomePatio());
			patioList.add(item);
		}
		return patioList;		
	}

	public void setPatioList(List<SelectItem> patioList) {
		this.patioList = patioList;
	}

	public IControladorPlanta getPlantaService() {
		return plantaService;
	}

	public void setPlantaService(IControladorPlanta plantaService) {
		this.plantaService = plantaService;
	}

	public UIDataTable getDataTableMaquina() {
		return dataTableMaquina;
	}

	public void setDataTableMaquina(UIDataTable dataTableMaquina) {
		this.dataTableMaquina = dataTableMaquina;
	}

	public MaquinaDoPatio getMaquina() {
		return maquina;
	}

	public void setMaquina(MaquinaDoPatio maquina) {
		this.maquina = maquina;
	}

		
}
