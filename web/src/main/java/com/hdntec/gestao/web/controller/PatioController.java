package com.hdntec.gestao.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.component.UIDataTable;

import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
import com.hdntec.gestao.domain.plano.enums.TipoMaquinaEnum;
import com.hdntec.gestao.domain.planta.comparadores.ComparadorBalizaNumero;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.Correia;
import com.hdntec.gestao.domain.planta.entity.status.MaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Patio;
import com.hdntec.gestao.domain.planta.enums.EnumTipoBaliza;
import com.hdntec.gestao.web.util.SessionUtil;
import com.hdntec.gestao.web.vo.BalizaVO;


public class PatioController {

	private UIDataTable dataTableBaliza;
	private UIDataTable dataTableMaquina;
	private Patio patio;
	private List<SelectItem> estadoEstruturaList = new ArrayList<SelectItem>();
	private List<BalizaVO> balizaList;
	private Long qtdBalizas = new Long(1);
	private MaquinaDoPatio maquinaDoPatio;
	//private MaquinaDoPatio maquinaDoPatioClone;
	private Object objParaExcluir;

	/********************************************
	 * Metodos do Patio
	 ********************************************/
	public String gravaPatio() {
		PlantaController plantaController = (PlantaController)SessionUtil.getSessionMapValue("plantaController");
		SituacaoPatio situacaoPatio = null;//plantaController.getSituacaoPatio();
		//Patio patioAnterior = plantaController.getPatio();				
		//boolean removeu = situacaoPatio.getPlanta().getListaPatios().remove(patioAnterior);
		//situacaoPatio.getPlanta().addPatio(patio);			
		return "voltarPatio";
	}
	
	/********************************
     * Exclusao de objetos
     ********************************/    
	public void selectBalizas() {
    	objParaExcluir = dataTableBaliza.getRowData();
    }
    
    public void selectMaquina() {
    	objParaExcluir = dataTableMaquina.getRowData();
    }
    
    public String excluiObjeto() {
    	if (objParaExcluir instanceof BalizaVO) {
    		removeBalizas();
    	}
    	else if (objParaExcluir instanceof MaquinaDoPatio) {
    		removeMaquina();
    	}
    	return null;
    }
    
	/********************************************
	 * Metodos da Baliza
	 ********************************************/
	public String novaBaliza() {
		Object obj = SessionUtil.getSessionMapValue("balizaController");
    	if (obj instanceof BalizaController) { 
    		BalizaController balizaController = (BalizaController)obj;
    		balizaController.setBaliza(new Baliza());
    	}
        return "atualizarBaliza";
	}
	
	public String removeBalizas() {
		BalizaVO balizaVO = (BalizaVO)objParaExcluir;
		/*List<Baliza> balizaList = patio.getListaDeBalizas();
		Collections.sort(balizaList, new ComparadorBalizaNumero());
		if (permiteExclusaoBalizas(balizaVO, balizaList)) {
			int posBalizaInicial = balizaList.indexOf(balizaVO.getBalizaInicial());
			int posBalizaFinal = balizaList.indexOf(balizaVO.getBalizaFinal());
			balizaList.subList(posBalizaInicial, posBalizaFinal+1).clear();
		}
		else {
			SessionUtil.addErrorMessage("balizaReferenciada");
		}*/
		return null;
	}
	
	private boolean permiteExclusaoBalizas(BalizaVO balizaVO, List<Baliza> balizaList) {
		boolean result = true;
		int posBalizaInicial = balizaList.indexOf(balizaVO.getBalizaInicial());
		int posBalizaFinal = balizaList.indexOf(balizaVO.getBalizaFinal());
		List<Baliza> balizasSelecionadas = balizaList.subList(posBalizaInicial, posBalizaFinal+1);
		
		/*for (MaquinaDoPatio maquinaDoPatio : getPatio().getListaDeMaquinasDoPatio()) {
			
			if (balizasSelecionadas.contains(maquinaDoPatio.getPosicao())) {
				result = false;
				break;
			}
		}*/
		
		if (result) {
			PlantaController plantaController = (PlantaController)SessionUtil.getSessionMapValue("plantaController");
			/*for (Correia correia : plantaController.getSituacaoPatio().getPlanta().getListaCorreias()) {
				for( MaquinaDoPatio maquinaDoPatio : correia.getListaDeMaquinas()) {
					if (balizasSelecionadas.contains(maquinaDoPatio.getPosicao())) {
						result = false;
						break;
					}				
				}
				if (!result) {
					break;
				}
			}*/
		}
		return result;
	}
	
	/********************************************
	 * Metodos da Maquina do Patio
	 ********************************************/
	public String novaMaquina() {
		Object obj = SessionUtil.getSessionMapValue("maquinaDoPatioController");
		maquinaDoPatio = new MaquinaDoPatio();
		maquinaDoPatio.setPatio(this.getPatio());
		//maquinaDoPatio.setTipoDaMaquina(TipoMaquinaEnum.PA_CARREGADEIRA);
		//maquinaDoPatioClone = new MaquinaDoPatio();
		//maquinaDoPatioClone.setPatio(getPatio());
		//maquinaDoPatioClone.setTipoDaMaquina(TipoMaquinaEnum.PA_CARREGADEIRA);
    	if (obj instanceof MaquinaDoPatioController) {
    		MaquinaDoPatioController maquinaController = (MaquinaDoPatioController)obj;
    		maquinaController.setMaquina(maquinaDoPatio);
    		maquinaController.setOrigemCadastro(null);
    	}
        return "atualizarMaquinaPatio";
	}
	
	public String alteraMaquina() {
    	maquinaDoPatio = (MaquinaDoPatio)dataTableMaquina.getRowData();
    	try {
    		//maquinaDoPatioClone = (MaquinaDoPatio)BeanUtils.cloneBean(maquinaDoPatio);
	    	Object obj = SessionUtil.getSessionMapValue("maquinaDoPatioController");
	    	if (obj instanceof MaquinaDoPatioController) { 
	    		MaquinaDoPatioController maquinaController = (MaquinaDoPatioController)obj;
	    		maquinaController.setMaquina(maquinaDoPatio);
	    		maquinaController.setOrigemCadastro(null);
	    	}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "atualizarMaquinaPatio";
	}
	
	public String removeMaquina() {
		MaquinaDoPatio maquinaExclusao = (MaquinaDoPatio)objParaExcluir;
    	//this.getPatio().getListaDeMaquinasDoPatio().remove(maquinaExclusao);
		return null;
	}
	
	/********************************************
	 * Metodos Getters e Setters
	 ********************************************/
	public Patio getPatio() {
		if (patio == null) {
			PlantaController plantaController = (PlantaController)SessionUtil.getSessionMapValue("plantaController");
			if (plantaController.getPatio() != null) {
				patio = plantaController.getPatio();
			}
			else {
				patio = new Patio();
			}
		}
		return patio;
	}

	public void setPatio(Patio patio) {
		this.patio = patio;
	}
	
	public List<SelectItem> getEstadoEstruturaList() {
		estadoEstruturaList.clear();
	/*	for(EstEstadoMaquinaEnumoEnum : EstadoEstadoMaquinaEnum ) {
			SelectItem item = new SelectItem(estadoEnum);
			estadoEstruturaList.add(item);
		}*/
		return estadoEstruturaList;
	}
	
	public void setEstadoEstruturaList(List<SelectItem> estadoEstruturaList) {
		this.estadoEstruturaList = estadoEstruturaList;
	}

	public List<BalizaVO> getBalizaList() {
		if (balizaList == null) {
			balizaList = new ArrayList<BalizaVO>();
		}
		populaBalizaList();
		return balizaList;
	}
	
	private void populaBalizaList() {
		balizaList.clear();
		/*List<Baliza> balizaLista = getPatio().getListaDeBalizas();
		if (balizaLista != null && balizaLista.size()>0) {
			Collections.sort(balizaLista, new ComparadorBalizaNumero());
			Baliza balizaInicial = balizaLista.get(0);
			Baliza balizaFinal = balizaLista.get(0);
			EnumTipoBaliza tipoBaliza = balizaInicial.getTipoBaliza();
			for(Baliza baliza : balizaLista) {
				if (tipoBaliza.equals(baliza.getTipoBaliza())) {
					balizaFinal = baliza;
				}
				else {
					addBalizaVO(balizaInicial,balizaFinal,tipoBaliza);
					balizaInicial = baliza;
					balizaFinal = baliza;
					tipoBaliza = baliza.getTipoBaliza();
				}
			}
			addBalizaVO(balizaInicial,balizaFinal,tipoBaliza);
		}*/
	}
	
	private void addBalizaVO(Baliza balizaInicial, Baliza balizaFinal, 
			EnumTipoBaliza tipoBaliza) {
		BalizaVO balizaVO = new BalizaVO();
		balizaVO.setBalizaInicial(balizaInicial);
		balizaVO.setBalizaFinal(balizaFinal);
		balizaVO.setTipo(tipoBaliza);
		balizaList.add(balizaVO);	
	}

	public void setBalizaList(List<BalizaVO> balizaList) {
		this.balizaList = balizaList;
	}

	public UIDataTable getDataTableBaliza() {
		return dataTableBaliza;
	}

	public void setDataTableBaliza(UIDataTable dataTableBaliza) {
		this.dataTableBaliza = dataTableBaliza;
	}

	public Long getQtdBalizas() {
		return qtdBalizas;
	}

	public void setQtdBalizas(Long qtdBalizas) {
		this.qtdBalizas = qtdBalizas;
	}

	public UIDataTable getDataTableMaquina() {
		return dataTableMaquina;
	}

	public void setDataTableMaquina(UIDataTable dataTableMaquina) {
		this.dataTableMaquina = dataTableMaquina;
	}

	public MaquinaDoPatio getMaquinaDoPatio() {
		if (maquinaDoPatio==null) {
			maquinaDoPatio = new MaquinaDoPatio();
		}
		return maquinaDoPatio;
	}

	public void setMaquinaDoPatio(MaquinaDoPatio maquinaDoPatio) {
		this.maquinaDoPatio = maquinaDoPatio;
	}

	/*public MaquinaDoPatio getMaquinaDoPatioClone() {
		return maquinaDoPatioClone;
	}

	public void setMaquinaDoPatioClone(MaquinaDoPatio maquinaDoPatioClone) {
		this.maquinaDoPatioClone = maquinaDoPatioClone;
	}*/
	
}
