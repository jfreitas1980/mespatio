package com.hdntec.gestao.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.planta.dao.MetaPierDAO;
import com.hdntec.gestao.domain.planta.entity.MetaCarregadoraDeNavios;
import com.hdntec.gestao.domain.planta.entity.MetaPier;
import com.hdntec.gestao.domain.planta.entity.status.CarregadoraDeNavios;
import com.hdntec.gestao.domain.planta.entity.status.Pier;
import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.web.util.SessionUtil;


public class CarregadoraDeNaviosController {

	private MetaCarregadoraDeNavios carregadora;
	
	private CarregadoraDeNavios carregadoraSelecionada;
    private CarregadoraDeNavios carregadoraAnterior;
    
	
	private List<SelectItem> estadoMaquinaList = new ArrayList<SelectItem>();

	/********************************
	 * CarregadoraDeNavios
	 ********************************/
	public String gravaCarregadoraDeNavios() {
		PierController pierController = (PierController) SessionUtil.getSessionMapValue("pierController");
		MetaCarregadoraDeNavios carregadora = pierController.getCarregadora();		
		if (!carregadoraAnterior.equals(carregadoraSelecionada)) {                      
		    carregadoraSelecionada.setDtInicio(Atividade.verificaAtualizaDataAtividade(carregadoraSelecionada.getDtInicio(),carregadoraAnterior.getDtInicio()));
		    carregadoraSelecionada.setIdCarregadeiraDeNavios(null);
		    carregadoraSelecionada.setDtFim(null);
            carregadora.incluirNovoStatus(carregadoraSelecionada, carregadoraSelecionada.getDtInicio());         
        } 
            MetaPierDAO dao = new MetaPierDAO();
            PierController plantaController = (PierController)SessionUtil.getSessionMapValue("pierController");                                   
            plantaController.getPier().addMetaCarregadoraNavio(carregadora);
            try {
                dao.salvaMetaPier(plantaController.getPier());
            } catch (ErroSistemicoException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
		return "voltarCarregadora";
	}

	/********************************
	 * Getters e Setters
	 ********************************/
	public MetaCarregadoraDeNavios getCarregadora() {
		if (carregadora == null) {
			PierController pierController = (PierController)SessionUtil.getSessionMapValue("pierController");
			if (pierController.getCarregadora() != null) {
				carregadora = pierController.getCarregadora();
			}
			else {
				carregadora = new MetaCarregadoraDeNavios();
			}
		}
		return carregadora;
	}

	public void setCarregadora(MetaCarregadoraDeNavios carregadora) {
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

    public CarregadoraDeNavios getCarregadoraSelecionada() {
        if (carregadoraSelecionada == null) {
            PierController pierController = (PierController)SessionUtil.getSessionMapValue("pierController");                                    
            carregadora.ordernar();
            carregadoraAnterior = pierController.getCarregadora().retornaStatusHorario(carregadora.getListaStatus().get(carregadora.getListaStatus().size()-1).getDtInicio());
            carregadoraSelecionada = pierController.getCarregadora().copiarStatus(carregadoraAnterior);               
        } 
        return carregadoraSelecionada;
    }

    public void setCarregadoraSelecionada(CarregadoraDeNavios carregadoraSelecionada) {
        this.carregadoraSelecionada = carregadoraSelecionada;
    }

    public CarregadoraDeNavios getCarregadoraAnterior() {
        return carregadoraAnterior;
    }

    public void setCarregadoraAnterior(CarregadoraDeNavios carregadoraAnterior) {
        this.carregadoraAnterior = carregadoraAnterior;
    }
}
