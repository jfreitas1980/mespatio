package com.hdntec.gestao.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.component.UIDataTable;

import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.planta.dao.MetaPierDAO;
import com.hdntec.gestao.domain.planta.entity.MetaCarregadoraDeNavios;
import com.hdntec.gestao.domain.planta.entity.MetaPier;
import com.hdntec.gestao.domain.planta.entity.status.Berco;
import com.hdntec.gestao.domain.planta.entity.status.CarregadoraDeNavios;
import com.hdntec.gestao.domain.planta.entity.status.Pier;
import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.web.util.SessionUtil;


public class PierController {

	private UIDataTable dataTableCarregadora;
	private UIDataTable dataTableBerco;
	private MetaPier pier;
	private Pier pierSelecionado;
	private Pier pierAnterior;
	private MetaCarregadoraDeNavios carregadora;
    private CarregadoraDeNavios carregadoraStatus;

	//private CarregadoraDeNavios carregadoraClone;
	private Berco berco;
	//private Berco bercoClone;
	private List<SelectItem> estadoEstruturaList = new ArrayList<SelectItem>();
	Object objParaExcluir;
	
	/********************************
     * Pier
     ********************************/
	public String gravaPier() {
		
	    if (!pierAnterior.equals(pierSelecionado)) {	        	        
	        pierSelecionado.setDtInicio(Atividade.verificaAtualizaDataAtividade(pierSelecionado.getDtInicio(),pierAnterior.getDtInicio()));
	        pierSelecionado.setIdPier(null);
	        pierSelecionado.setDtFim(null);
	        pier.incluirNovoStatus(pierSelecionado, pierSelecionado.getDtInicio());	        
	    } 
	        MetaPierDAO dao = new MetaPierDAO();
		    PlantaController plantaController = (PlantaController)SessionUtil.getSessionMapValue("plantaController");    		    		   		    
		    plantaController.getPlantaPesquisa().addMetaPier(pier);
		    try {
		        dao.salvaMetaPier(pier);
		    } catch (ErroSistemicoException e) {
		        // TODO Auto-generated catch block
		        e.printStackTrace();
		    }
	       
		return "voltarPier";
	}
	
	/********************************
     * Pier
     ********************************/
    public String cancelar() {        
        MetaPier meta = pierSelecionado.getMetaPier();
        meta.getListaStatus().remove(pierSelecionado);
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
		carregadora = new MetaCarregadoraDeNavios();
    	//carregadoraClone = new CarregadoraDeNavios();
    	Object obj = SessionUtil.getSessionMapValue("carregadoraDeNaviosController");
    	if (obj instanceof CarregadoraDeNaviosController) { 
    		CarregadoraDeNaviosController carregadoraDeNaviosController = (CarregadoraDeNaviosController)obj;
    		//carregadoraDeNaviosController.setCarregadora(carregadora);
    	}
        return "atualizarCarregadora";
    }
    
    public String alteraCarregadora() {
    	carregadora = (MetaCarregadoraDeNavios)dataTableCarregadora.getRowData();
    	try {
			Object obj = SessionUtil.getSessionMapValue("carregadoraDeNaviosController");
			if (obj instanceof CarregadoraDeNaviosController) { 
				CarregadoraDeNaviosController carregadoraDeNaviosController = (CarregadoraDeNaviosController)obj;
				carregadoraDeNaviosController.setCarregadora(carregadora);
				carregadoraDeNaviosController.setCarregadoraSelecionada(null);
				carregadoraDeNaviosController.setCarregadoraAnterior(null);
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
    	//this.getPier().getListaDeCarregadoraDeNavios().remove(carregadoraExclusao);
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
    	//this.getPier().getListaDeBercosDeAtracacao().remove(bercoExclusao);
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
	
	public MetaPier getPier() {
		if (pier == null) {
			PlantaController plantaController = (PlantaController)SessionUtil.getSessionMapValue("plantaController");
			if (plantaController.getPier() != null) {
				pier = plantaController.getPier();					
			}
			else {
				pier = new MetaPier();								
			}			        
		}		
		return pier;
	}
	
	public PierController() {
	    pierSelecionado = null;
        pierAnterior = null;        
    }

    public void setPier(MetaPier pier) {
		this.pier = pier;
	}
	
	public List<SelectItem> getEstadoEstruturaList() {
		estadoEstruturaList.clear();
		for(EstadoMaquinaEnum estadoEnum: EstadoMaquinaEnum.values() ) {
			SelectItem item = new SelectItem(estadoEnum);
			estadoEstruturaList.add(item);
		}
		return estadoEstruturaList;
	}
	
	public void setEstadoEstruturaList(List<SelectItem> estadoEstruturaList) {
		this.estadoEstruturaList = estadoEstruturaList;
	}

	public MetaCarregadoraDeNavios getCarregadora() {
		return carregadora;
	}

	public void setCarregadora(MetaCarregadoraDeNavios carregadora) {
		this.carregadora = carregadora;
	}

	public Berco getBerco() {
		return berco;
	}

	public void setBerco(Berco berco) {
		this.berco = berco;
	}

    public Pier getPierSelecionado() {
        if (pierSelecionado == null) {
            PlantaController plantaController = (PlantaController)SessionUtil.getSessionMapValue("plantaController");                                    
            pier.ordernar();
            pierAnterior = plantaController.getPier().retornaStatusHorario(pier.getListaStatus().get(pier.getListaStatus().size()-1).getDtInicio());
            pierSelecionado = plantaController.getPier().copiarStatus(pierAnterior);               
        } 
        return pierSelecionado;
    }

    public void setPierSelecionado(Pier pierSelecionado) {
        this.pierSelecionado = pierSelecionado;
    }

    public Pier getPierAnterior() {
        return pierAnterior;
    }

    public void setPierAnterior(Pier pierAnterior) {
        this.pierAnterior = pierAnterior;
    }

}