package br.com.cflex.supervision.stockyard.cadastros.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;

import org.richfaces.component.UIDataTable;

import util.datahora.DSSStockyardTimeUtil;
import br.com.cflex.supervision.stockYard.servidor.modelo.exceptions.ErroSistemicoException;
import br.com.cflex.supervision.stockYard.servidor.modelo.plano.CloneHelper;
import br.com.cflex.supervision.stockYard.servidor.modelo.plano.IControladorPlanoDeSituacoesDoPatio;
import br.com.cflex.supervision.stockYard.servidor.modelo.plano.PlanoEmpilhamentoRecuperacao;
import br.com.cflex.supervision.stockYard.servidor.modelo.plano.SituacaoPatio;
import br.com.cflex.supervision.stockYard.servidor.modelo.plano.comparadores.ComparadorSituacoesPatio;
import br.com.cflex.supervision.stockYard.servidor.modelo.planta.Correia;
import br.com.cflex.supervision.stockYard.servidor.modelo.planta.Patio;
import br.com.cflex.supervision.stockYard.servidor.modelo.planta.Pier;
import br.com.cflex.supervision.stockYard.servidor.modelo.planta.Planta;
import br.com.cflex.supervision.stockYard.servidor.modelo.planta.Usina;
import br.com.cflex.supervision.stockyard.util.SessionUtil;

public class PlantaController {
	/**Atributos da Planta*/
	private UIDataTable dataTablePlanta;
	private SituacaoPatio situacaoPatio;
	private Planta plantaPesquisa = new Planta();
	private List<SituacaoPatio> situacaoPatioList = new ArrayList<SituacaoPatio>();
	private PlanoEmpilhamentoRecuperacao planoEmpilhamentoRecuperacao;
	private static boolean isResearch = false;

	private Object objParaExcluir;
	
	/**Atributos do Pier*/
    private UIDataTable dataTablePier;
    private Pier pier;
    
    /**Atributos do Patio*/
    private UIDataTable dataTablePatio;
    private Patio patio;
    
    /**Atributos da Correia*/
    private UIDataTable dataTableCorreia;
    private Correia correia;
    
    /**Atributos da Usina*/
    private UIDataTable dataTableUsina;
    private Usina usina;
    
    /**planoService*/
    @EJB(name = "dssStockYardSamarcoEAR/bs/ControladorPlanoDeSituacoesDoPatio/local")
    private IControladorPlanoDeSituacoesDoPatio planoService;
    
    /********************************
     * Metodos da Planta
     ********************************/
	private void initEntity() {
		isResearch = false;
		situacaoPatio = new SituacaoPatio();
		plantaPesquisa = new Planta();
	}

    public String buscaPlantaPorFiltro() {
        isResearch = true;
        return null;
    }
    
    public String limpaFiltro() {
        initEntity();
        return null;
    }
    
    public void selectItem() {
        situacaoPatio = (SituacaoPatio)dataTablePlanta.getRowData();
    }
    
    public void selectPier() {
    	objParaExcluir = dataTablePier.getRowData();
    }
    
    public void selectPatio() {
    	objParaExcluir = dataTablePatio.getRowData();
    }
    
    public void selectCorreia() {
    	objParaExcluir = dataTableCorreia.getRowData();
    }
    
    public void selectUsina() {
    	objParaExcluir = dataTableUsina.getRowData();
    }    
    
    public String excluiObjeto() {
    	if (objParaExcluir instanceof Pier) {
    		removePier();
    	}
    	else if (objParaExcluir instanceof Patio) {
    		removePatio();
    	}
    	else if (objParaExcluir instanceof Correia) {
    		removeCorreia();
    	}
    	else if (objParaExcluir instanceof Usina) {
    		removeUsina();
    	}
    	return null;
    }
    
    public String alteraPlanta() {
    	SituacaoPatio situacaoPatioModelo = (SituacaoPatio)dataTablePlanta.getRowData();
    	planoEmpilhamentoRecuperacao = recuperarPlanoEmpilhamento(situacaoPatioModelo);
    	//Clonar a situacao de patio recuperada
    	situacaoPatio = new SituacaoPatio(); 
    	CloneHelper cloneHelper = new CloneHelper();    	
    	situacaoPatioModelo.limpaClone();
    	situacaoPatio.geraClone(situacaoPatioModelo, cloneHelper);    	
    	//situacaoPatio.geraClone(situacaoPatioModelo, cloneHelper);
    	//situacaoPatio = cloneHelper.clonarSituacaoPatio(situacaoPatioModelo);
    	Date  data = DSSStockyardTimeUtil.verificaAtualizaDataSituacaoClone(situacaoPatioModelo.getDataHora(), situacaoPatioModelo.getDataHora());
    	situacaoPatio.setDataHora(data);
		//situacaoPatio.limpaID();
    	return "atualizarPlanta";
    }
    
    public String novaPlanta() {
        initEntity();
        planoEmpilhamentoRecuperacao = novoPlanoEmpilhamentoRecuperacao();
        situacaoPatio = novaSituacaoPatio();
        return "novaPlanta";
    }
    
    public String gravaPlanta() {
    	//situacaoPatio.limpaID();
    	planoEmpilhamentoRecuperacao.addSituacaoPatio(situacaoPatio);
    	situacaoPatio.setPlanoEmpilhamento(planoEmpilhamentoRecuperacao);
    	try {
    	    planoEmpilhamentoRecuperacao = 
				planoService.salvarPlanoDeEmpilhamentoERecuperacao(
						planoEmpilhamentoRecuperacao);            
			SessionUtil.addSucessMessage("sucess");
    		return "cancelarPlanta";
		} catch (ErroSistemicoException e) {
			e.printStackTrace();
			SessionUtil.addErrorMessage("error");
		}
    	return null;
    }
    
    public String excluiPlanta() {        
        try {
        	PlanoEmpilhamentoRecuperacao plano = recuperarPlanoEmpilhamento(situacaoPatio);
        	planoService.removerPlanoDeEmpilhamentoERecuperacao(plano);
            SessionUtil.addSucessMessage("sucess");
        } catch (Exception e) {
        	e.printStackTrace();
        	SessionUtil.addErrorMessage("error");
        } finally {
            initEntity();
        }
        return null;
    }

	public List<SituacaoPatio> getSituacaoPatioList() {
		if (isResearch) {
			situacaoPatioList = filtrarPlantas(plantaPesquisa.getNomePlanta());
		} else {
			situacaoPatioList = listarSituacaoPatio();
		}
    	return situacaoPatioList;
	}
    
    /********************************
     * Metodos do Pier
     ********************************/
    public String novoPier() {
    	pier = new Pier();
    	//pierClone = new Pier();
    	Object obj = SessionUtil.getSessionMapValue("pierController");
    	if (obj instanceof PierController) { 
    		PierController pierController = (PierController)obj;
    		pierController.setPier(pier);
    	}
        return "atualizarPier";
    }
    
    public String alteraPier() {
    	pier = (Pier)dataTablePier.getRowData();
    	//clonarItensPier();
    	Object obj = SessionUtil.getSessionMapValue("pierController");
    	if (obj instanceof PierController) { 
    		PierController pierController = (PierController)obj;
    		pierController.setPier(pier);
    	}
        return "atualizarPier";
    }

    public String removePier() {
    	Pier pierExclusao = (Pier)objParaExcluir;
    	this.situacaoPatio.getPlanta().getListaPiers().remove(pierExclusao);
        return null;
    }

    /********************************
     * Metodos do Patio
     ********************************/
    public String novoPatio() {
    	patio = new Patio();
    	//patioClone = new Patio();
    	Object obj = SessionUtil.getSessionMapValue("patioController");
    	if (obj instanceof PatioController) { 
    		PatioController patioController = (PatioController)obj;
    		patioController.setPatio(patio);
    	}
        return "atualizarPatio";
    }
    
    public String alteraPatio() {
    	patio = (Patio)dataTablePatio.getRowData();
    	//clonarPatio();
    	Object obj = SessionUtil.getSessionMapValue("patioController");
    	if (obj instanceof PatioController) { 
    		PatioController patioController = (PatioController)obj;
    		patioController.setPatio(patio);
    	}
    	return "atualizarPatio";
    }

    public String removePatio() {
        Patio patioExclusao = (Patio)objParaExcluir;
    	if (permiteExclusaoPatio(patioExclusao)) {
    		this.situacaoPatio.getPlanta().getListaPatios().remove(patioExclusao);
    		SessionUtil.addSucessMessage("sucess");
    	}
    	else {
    		SessionUtil.addErrorMessage("patioReferenciado");
    	}
        return null;
    }
    
    
    private boolean permiteExclusaoPatio(Patio patioExclusao) {
    	boolean result = true;
    	if (patioExclusao instanceof Patio) {
	    	//verificar na lista de correias da planta se existe o patio
	    	for(Correia correia : this.situacaoPatio.getPlanta().getListaCorreias()) {
	    		if ( patioExclusao.equals(correia.getPatioInferior()) ||
	    				patioExclusao.equals(correia.getPatioSuperior()) ) {
	    			result = false;
	    			break;
	    		}
	    	}
	    	if ( result ) {
		    	//verificar na lista de usinas se existe o patio
		    	for (Usina usina : this.situacaoPatio.getPlanta().getListaUsinas()) {
		    		if (patioExclusao.equals(usina.getPatioExpurgoPellet())) {
		    			result = false;
		    			break;
		    		}
		    	}
	    	}
    	}
    	return result; 
    }
    

    /********************************
     * Metodos da Correia
     ********************************/
    public String novaCorreia() {
    	correia = new Correia();

    	Object obj = SessionUtil.getSessionMapValue("correiaController");
    	if (obj instanceof CorreiaController) { 
    		CorreiaController correiaController = (CorreiaController)obj;
    		correiaController.setCorreia(correia);
    	}
        return "atualizarCorreia";
    }
    
    public String alteraCorreia() {
    	correia = (Correia)dataTableCorreia.getRowData();

    	Object obj = SessionUtil.getSessionMapValue("correiaController");
    	if (obj instanceof CorreiaController) { 
    		CorreiaController correiaController = (CorreiaController)obj;
    		correiaController.setCorreia(correia);
    	}
    	return "atualizarCorreia";
    }

    public String removeCorreia() {
    	Correia correiaExclusao = (Correia)objParaExcluir;
    	this.situacaoPatio.getPlanta().getListaCorreias().remove(correiaExclusao);
        return null;
    }
    


    /********************************
     * Metodos da Usina
     ********************************/
    public String novaUsina() {
    	usina = new Usina();

    	Object obj = SessionUtil.getSessionMapValue("usinaController");
    	if (obj instanceof UsinaController) { 
    		UsinaController usinaController = (UsinaController)obj;
    		usinaController.setUsina(usina);
    	}
        return "atualizarUsina";
    }
    
    public String alteraUsina() {
    	usina = (Usina)dataTableUsina.getRowData();
    	try {
			Object obj = SessionUtil.getSessionMapValue("usinaController");
			if (obj instanceof UsinaController) { 
				UsinaController usinaController = (UsinaController)obj;
				usinaController.setUsina(usina);
			}		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return "atualizarUsina";
    }

    public String removeUsina() {
    	Usina usinaExclusao = (Usina)objParaExcluir;
    	this.situacaoPatio.getPlanta().getListaUsinas().remove(usinaExclusao);
        return null;
    }
    
    /**********************************
     * Setters e Getters
     **********************************/
    
	public UIDataTable getDataTablePlanta() {
		return dataTablePlanta;
	}

	public void setDataTablePlanta(UIDataTable dataTablePlanta) {
		this.dataTablePlanta = dataTablePlanta;
	}

	public SituacaoPatio getSituacaoPatio() {
		return situacaoPatio;
	}

	public void setSituacaoPatio(SituacaoPatio situacaoPatio) {
		this.situacaoPatio = situacaoPatio;
	}

	public static boolean isResearch() {
		return isResearch;
	}

	public static void setResearch(boolean isResearch) {
		PlantaController.isResearch = isResearch;
	}
	
	public Planta getPlantaPesquisa() {
		return plantaPesquisa;
	}

	public void setPlantaPesquisa(Planta plantaPesquisa) {
		this.plantaPesquisa = plantaPesquisa;
	}

	public IControladorPlanoDeSituacoesDoPatio getPlanoService() {
		return planoService;
	}


	public void setPlanoService(IControladorPlanoDeSituacoesDoPatio planoService) {
		this.planoService = planoService;
	}
	
	
	public List<SituacaoPatio> listarSituacaoPatio() {
    	try {
    		if (planoEmpilhamentoRecuperacao == null) {     		    
					 planoEmpilhamentoRecuperacao = planoService.buscarPlanoEmpilhamentoRecuperacaoOficial(0L);
    		}
    		
    		if (planoEmpilhamentoRecuperacao != null) {
					 situacaoPatioList.clear();
					 Collections.sort(planoEmpilhamentoRecuperacao.getListaSituacoesPatio(), new ComparadorSituacoesPatio());
			         situacaoPatioList.add(planoEmpilhamentoRecuperacao.getListaSituacoesPatio().get(planoEmpilhamentoRecuperacao.getListaSituacoesPatio().size() -1));			 
			}
			
		} catch (ErroSistemicoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return situacaoPatioList;
	}

	public void setSituacaoPatioList(List<SituacaoPatio> situacaoPatioList) {
		this.situacaoPatioList = situacaoPatioList;
	}

	public UIDataTable getDataTablePier() {
		return dataTablePier;
	}

	public void setDataTablePier(UIDataTable dataTablePier) {
		this.dataTablePier = dataTablePier;
	}

	public UIDataTable getDataTablePatio() {
		return dataTablePatio;
	}

	public void setDataTablePatio(UIDataTable dataTablePatio) {
		this.dataTablePatio = dataTablePatio;
	}

	public Pier getPier() {
		return pier;
	}

	public void setPier(Pier pier) {
		this.pier = pier;
	}

	public Patio getPatio() {
		return patio;
	}

	public void setPatio(Patio patio) {
		this.patio = patio;
	}

	public UIDataTable getDataTableCorreia() {
		return dataTableCorreia;
	}

	public void setDataTableCorreia(UIDataTable dataTableCorreia) {
		this.dataTableCorreia = dataTableCorreia;
	}

	public Correia getCorreia() {
		return correia;
	}

	public void setCorreia(Correia correia) {
		this.correia = correia;
	}
	
	public UIDataTable getDataTableUsina() {
		return dataTableUsina;
	}

	public void setDataTableUsina(UIDataTable dataTableUsina) {
		this.dataTableUsina = dataTableUsina;
	}

	public Usina getUsina() {
		return usina;
	}

	public void setUsina(Usina usina) {
		this.usina = usina;
	}

	public PlanoEmpilhamentoRecuperacao getPlanoEmpilhamentoRecuperacao() {
		return planoEmpilhamentoRecuperacao;
	}

	public void setPlanoEmpilhamentoRecuperacao(
			PlanoEmpilhamentoRecuperacao planoEmpilhamentoRecuperacao) {
		this.planoEmpilhamentoRecuperacao = planoEmpilhamentoRecuperacao;
	}

	/**
	 * 
	 * recuperarPlanoEmpilhamento
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 15/07/2009
	 * @see
	 * @param situacaoPatio
	 * @return
	 * @return Returns the PlanoEmpilhamentoRecuperacao.
	 */
	private PlanoEmpilhamentoRecuperacao recuperarPlanoEmpilhamento(SituacaoPatio situacaoPatio) {
			PlanoEmpilhamentoRecuperacao planoEmpilhamento = null; 	
			if (planoEmpilhamentoRecuperacao.getListaSituacoesPatio().contains(situacaoPatio)) {
			    planoEmpilhamento = planoEmpilhamentoRecuperacao;				
			}		
		return planoEmpilhamento;
	}

	private PlanoEmpilhamentoRecuperacao novoPlanoEmpilhamentoRecuperacao() {
		PlanoEmpilhamentoRecuperacao plano = new PlanoEmpilhamentoRecuperacao();
		plano.setDataInicio(new Date());
		plano.setEhOficial(Boolean.FALSE);
		plano.setUltimaModificacao(new Date());
		plano.setHorizonteDePlanejamento(DSSStockyardTimeUtil.obterValorDeIntervalos(new Long(3), null, null, new Long(0)));
		return plano;
	}

	private SituacaoPatio novaSituacaoPatio() {
		SituacaoPatio situacaoPatio = new SituacaoPatio();
		situacaoPatio.setDataHora(new Date(System.currentTimeMillis()));
		situacaoPatio.setEhRealizado(Boolean.FALSE);
		situacaoPatio.setPlanta(new Planta());
		return situacaoPatio;
	}


	private List<SituacaoPatio> filtrarPlantas(String nomePlanta) {
		listarSituacaoPatio();
		List<SituacaoPatio> resultado = new ArrayList<SituacaoPatio>();
		for(SituacaoPatio situacaoPatio : situacaoPatioList) {
			if (situacaoPatio.getPlanta().getNomePlanta().toLowerCase().contains(nomePlanta.toLowerCase())) {
				resultado.add(situacaoPatio);
			}
		}
		return resultado;
	}
}
