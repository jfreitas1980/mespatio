package com.hdntec.gestao.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import com.hdntec.gestao.domain.plano.enums.TipoMaquinaEnum;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.MaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Patio;
import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;
import com.hdntec.gestao.web.util.BalizaUtil;
import com.hdntec.gestao.web.util.SessionUtil;


public class MaquinaDoPatioController {

	private String origemCadastro;
	private MaquinaDoPatio maquina;
	private List<SelectItem> estadoMaquinaList = new ArrayList<SelectItem>();
	private List<SelectItem> posicaoList = new ArrayList<SelectItem>();
	private List<SelectItem> tipoDaMaquinaList = new ArrayList<SelectItem>();

	public String gravaMaquina() {
		String result = "";
		if ("Patio".equals(origemCadastro)) {
			PatioController patioController = (PatioController)SessionUtil.getSessionMapValue("patioController");			
			//patioController.getPatio().addMaquina(maquina);
			//maquina.setPatio(patioController.getPatio());
			result = "voltarMaquinaPatio";
		}
		else if ("Correia".equals(origemCadastro)) {
			CorreiaController correiaController = (CorreiaController)SessionUtil.getSessionMapValue("correiaController");			
			//correiaController.getCorreia().addMaquina(maquina);
			//maquina.setCorreia(correiaController.getCorreia());
			result = "voltarMaquinaCorreia";
		}
		return result;
	}
	
	public String cancelaMaquina() {
		return "Patio".equals(origemCadastro)?"voltarMaquinaPatio":"voltarMaquinaCorreia";
	}
	
	
	public MaquinaDoPatio getMaquina() {
		
		if (origemCadastro == null) {
			Map<String,String> parametros = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
			origemCadastro = parametros.get("origemCadastro");
		}
		
		if (maquina == null) {
			if ("Patio".equals(origemCadastro)) {
				PatioController patioController = (PatioController)SessionUtil.getSessionMapValue("patioController");
				/*if (patioController.getMaquinaDoPatio() != null) {
					 maquina = patioController.getMaquinaDoPatio();
				}
				else {
					maquina = new MaquinaDoPatio();
					maquina.setPatio(patioController.getPatio());
				    maquina.setTipoDaMaquina(TipoMaquinaEnum.PA_CARREGADEIRA);
					
				}*/
			}
			else if ("Correia".equals(origemCadastro)) {
				CorreiaController correiaController = (CorreiaController)SessionUtil.getSessionMapValue("correiaController");
				if (correiaController.getMaquina() != null) {
					 maquina = correiaController.getMaquina();
					// maquina.setCorreia(correiaController.getCorreia());					 					 	                 
				}
				else {
					maquina = new MaquinaDoPatio();
					//maquina.setCorreia(correiaController.getCorreia());                                        				
				}
			}
		}
		return maquina;
	}

	public void setMaquina(MaquinaDoPatio maquina) {
		this.maquina = maquina;
	}

	public List<SelectItem> getEstadoMaquinaList() {
		estadoMaquinaList.clear();
		for(EstadoMaquinaEnum estadoEnum : EstadoMaquinaEnum.values() ) {
			SelectItem item = new SelectItem(estadoEnum);
			estadoMaquinaList.add(item);
		}
		return estadoMaquinaList;
	}

	public void setEstadoMaquinaList(List<SelectItem> estadoMaquinaList) {
		this.estadoMaquinaList = estadoMaquinaList;
	}

	public List<SelectItem> getPosicaoList() {
		
	    Patio patio = null;
	    if (getMaquina().getCorreia() != null) {
	        patio = getMaquina().obterPatioDeLocalizacaoCorreia(); 
	    } else {
	        patio =getMaquina().obterPatioDeLocalizacao();   
	    }
		if (patio != null) {
			posicaoList.clear();
			/*List<Baliza> balizaList = patio.getListaDeBalizas();
			SelectItem nullItem = new SelectItem(null,"");
			posicaoList.add(nullItem);
			for(Baliza baliza : balizaList) {
				BalizaUtil.addBaliza(baliza);
				posicaoList.add(new SelectItem(baliza,baliza.getNomeBaliza()));
			}*/
		}
		return posicaoList;
	}

	public void setPosicaoList(List<SelectItem> posicaoList) {
		this.posicaoList = posicaoList;
	}

	public List<SelectItem> getTipoDaMaquinaList() {
		tipoDaMaquinaList.clear();
		if ("Patio".equals(origemCadastro)) {
			SelectItem item = new SelectItem(TipoMaquinaEnum.PA_CARREGADEIRA);
			tipoDaMaquinaList.add(item);	
		}
		else {
			for(TipoMaquinaEnum tipoEnum : TipoMaquinaEnum.values() ) {
				SelectItem item = new SelectItem(tipoEnum);
				tipoDaMaquinaList.add(item);
			}
		}
		return tipoDaMaquinaList;
	}

	public void setTipoDaMaquinaList(List<SelectItem> tipoDaMaquinaList) {
		this.tipoDaMaquinaList = tipoDaMaquinaList;
	}
	
	public String getOrigemCadastro() {
		return origemCadastro;
	}


	public void setOrigemCadastro(String origemCadastro) {
		this.origemCadastro = origemCadastro;
	}

	public String getTipoPatio() {
		return "Patio";
	}
	
}
