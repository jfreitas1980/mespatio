package br.com.cflex.supervision.stockyard.cadastros.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import org.richfaces.component.UIDataTable;

import br.com.cflex.supervision.stockYard.servidor.modelo.exceptions.ErroSistemicoException;
import br.com.cflex.supervision.stockYard.servidor.modelo.planta.EstadoMaquinaEnum;
import br.com.cflex.supervision.stockYard.servidor.modelo.produto.CoeficienteDeDegradacao;
import br.com.cflex.supervision.stockYard.servidor.modelo.produto.IControladorCoeficienteDeDegradacao;
import br.com.cflex.supervision.stockYard.servidor.modelo.produto.IControladorMetaInterna;
import br.com.cflex.supervision.stockYard.servidor.modelo.produto.IControladorRegraFarol;
import br.com.cflex.supervision.stockYard.servidor.modelo.produto.IControladorTipoItemDeControle;
import br.com.cflex.supervision.stockYard.servidor.modelo.produto.MetaInterna;
import br.com.cflex.supervision.stockYard.servidor.modelo.produto.RegraFarol;
import br.com.cflex.supervision.stockYard.servidor.modelo.produto.TipoItemControleEnum;
import br.com.cflex.supervision.stockYard.servidor.modelo.produto.TipoItemDeControle;
import br.com.cflex.supervision.stockYard.servidor.modelo.produto.TipoMetaInternaEnum;
import br.com.cflex.supervision.stockyard.util.SessionUtil;

public class TipoItemDeControleController {

	private TipoItemDeControle tipoItemDeControle;
	private TipoItemDeControle tipoItemDeControlePesquisa = new TipoItemDeControle();
	private List<TipoItemDeControle> tipoItemDeControleList = new ArrayList<TipoItemDeControle>();
	private List<MetaInterna> metaInternaProducaoList = new ArrayList<MetaInterna>();		
	private List<MetaInterna> metaInternaList = new ArrayList<MetaInterna>();
	private List<RegraFarol> regraFarolList = new ArrayList<RegraFarol>();
	private List<CoeficienteDeDegradacao> coeficienteList = new ArrayList<CoeficienteDeDegradacao>();
	private List<SelectItem> metaInternaItemList = new ArrayList<SelectItem>();
	private List<SelectItem> metaInternaProducaoItemList = new ArrayList<SelectItem>();
	private List<SelectItem> regraFarolItemList = new ArrayList<SelectItem>();
	private List<SelectItem> coeficienteItemList = new ArrayList<SelectItem>();
	private UIDataTable dataTableTipoItemDeControle;
	private List<SelectItem> estadoEstruturaList = new ArrayList<SelectItem>();	
	private Map<Long, MetaInterna> metaInternaMap = new HashMap<Long, MetaInterna>();
	private Map<Long, RegraFarol> regraFarolMap = new HashMap<Long, RegraFarol>();
	private Map<Long, CoeficienteDeDegradacao> coeficienteMap = new HashMap<Long, CoeficienteDeDegradacao>(); 

	private static boolean isResearch = false;
	private boolean boolError = false;
	/**tipoItemDeControleService*/
	@EJB(name = "dssStockYardSamarcoEAR/bs/ControladorTipoItemDeControle/local")
	private IControladorTipoItemDeControle tipoItemDeControleService;
	@EJB(name = "dssStockYardSamarcoEAR/bs/ControladorCoeficienteDeDegradacao/local")
	private IControladorCoeficienteDeDegradacao coeficienteDeDegradacaoService;
	@EJB(name = "dssStockYardSamarcoEAR/bs/ControladorRegraFarol/local")
	private IControladorRegraFarol regraFarolService;
	@EJB(name = "dssStockYardSamarcoEAR/bs/ControladorTMetaInterna/local")
	private IControladorMetaInterna metaInternaService;
//	@EJB(name = "dssStockYardSamarcoEAR/bs/ControladorProduto/local")
//	private IControladorProduto produto;
	
	private void initEntity() {
		isResearch = false;
		boolError = false;
		tipoItemDeControle = new TipoItemDeControle();
		tipoItemDeControlePesquisa = new TipoItemDeControle();
		tipoItemDeControlePesquisa.setDescricaoTipoItemControle("");
		tipoItemDeControleList = new ArrayList<TipoItemDeControle>();
		metaInternaList = new ArrayList<MetaInterna>();
		metaInternaProducaoList = new ArrayList<MetaInterna>();
		regraFarolList = new ArrayList<RegraFarol>();
		coeficienteList = new ArrayList<CoeficienteDeDegradacao>();
	}
	
    public String buscaPorFiltro() {
        isResearch = true;
        return null;
    }
    
    
    public String limpaFiltro() {
        initEntity();
        return null;
    }
    
    public void selectItem() {
    	tipoItemDeControle = (TipoItemDeControle)dataTableTipoItemDeControle.getRowData();        
    }

	public String novoTipoItemDeControle() {
		initEntity();
		return "novoTipoItemDeControle";
	}
	
	public String alteraTipoItemDeControle() {
		setTipoItemDeControle((TipoItemDeControle)dataTableTipoItemDeControle.getRowData());        
        return "atualizarTipoItemDeControle";
    }
    
    public String excluiTipoItemDeControle() {        
        try {
        	tipoItemDeControleService.removeTipoItemDeControle(tipoItemDeControle);
            SessionUtil.addSucessMessage("sucess");
        } catch (Exception e) {
        	e.printStackTrace();
        	SessionUtil.addErrorMessage("error");
        } finally {
            initEntity();
            tipoItemDeControleService.encerrarSessao();
        }
        return null;
    }
    
    public String salvarTipoItemDeControle() {
    	try {
    		metaInternaList.addAll(metaInternaProducaoList);    		
    		
    		//tipoItemDeControle.addMetaInterna(metaInternaList);
    		//tipoItemDeControle.addMetaInterna(metaInternaProducaoList);
    		if (tipoItemDeControle.getListaDeMetasInternas() != null) {
    			tipoItemDeControle.getListaDeMetasInternas().clear();
    		}
    		tipoItemDeControle.addMetaInterna(metaInternaList);
    		tipoItemDeControle.addMetaInterna(metaInternaProducaoList);
    		
    		if (tipoItemDeControle.getListaDeRegrasFarol() != null) {
    			tipoItemDeControle.getListaDeRegrasFarol().clear();
    		}
    		tipoItemDeControle.addListaDeRegrasFarol(regraFarolList);
    		
    		if (tipoItemDeControle.getListaDeCoeficientesDeDegradacao()!=null) {
    			tipoItemDeControle.getListaDeCoeficientesDeDegradacao().clear();
    		}
    		tipoItemDeControle.addListaDeCoeficientesDeDegradacao(coeficienteList);
    		//tipoItemDeControle.setListaDeRegrasFarol(regraFarolList);
    		//tipoItemDeControle.setListaDeCoeficientesDeDegradacao(coeficienteList);
    		tipoItemDeControle = tipoItemDeControleService.salvaTipoItemDeControle(tipoItemDeControle);
    		SessionUtil.addSucessMessage("sucess");

    		return "atualizarTipoItemDeControle";
    	} catch (Exception e) {
    		e.printStackTrace();
    		SessionUtil.addErrorMessage("error");
    	}
    	finally {
//    		tipoItemDeControleService.encerrarSessao();
    	}
    	return null;
    }
    
	public TipoItemDeControle getTipoItemDeControle() {
		return tipoItemDeControle;
	}
	public void setTipoItemDeControle(TipoItemDeControle tipoItemDeControle) {
		this.tipoItemDeControle = tipoItemDeControle;
		metaInternaProducaoList.clear();
		metaInternaList.clear();
		for (MetaInterna meta : tipoItemDeControle.getListaDeMetasInternas()) {
		    if (meta.getTipoDaMetaInterna().equals(TipoMetaInternaEnum.META_DE_EMBARQUE)) { 
		    			metaInternaList.add(meta);
		    } else {
				metaInternaProducaoList.add(meta);
		    }
		}
		
		regraFarolList.clear();
		for (RegraFarol regraFarol : tipoItemDeControle.getListaDeRegrasFarol()) {
			regraFarolList.add(regraFarol);
		}
		coeficienteList.clear();
		for (CoeficienteDeDegradacao coeficiente : tipoItemDeControle.getListaDeCoeficientesDeDegradacao()) {
			coeficienteList.add(coeficiente);
		}
	}
	public TipoItemDeControle getTipoItemDeControlePesquisa() {
		return tipoItemDeControlePesquisa;
	}
	public void setTipoItemDeControlePesquisa(
			TipoItemDeControle tipoItemDeControlePesquisa) {
		this.tipoItemDeControlePesquisa = tipoItemDeControlePesquisa;
	}
	public List<TipoItemDeControle> getTipoItemDeControleList() {
		
		try {
			if (isResearch) {
				tipoItemDeControleList = tipoItemDeControleService
						.buscarTipoItemDeControle(tipoItemDeControlePesquisa);
			} else {				
				if (tipoItemDeControleList.isEmpty()) {
					tipoItemDeControleList = tipoItemDeControleService.buscarTodosItemDeControle();
				}	
			}
		} catch (ErroSistemicoException e) {
			e.printStackTrace();
			SessionUtil.addErrorMessage("error");
		}
		finally {
//			tipoItemDeControleService.encerrarSessao();
		}
		return tipoItemDeControleList;
	}
	public void setTipoItemDeControleList(List<TipoItemDeControle> tipoItemDeControleList) {
		this.tipoItemDeControleList = tipoItemDeControleList;
	}
	public UIDataTable getDataTableTipoItemDeControle() {
		return dataTableTipoItemDeControle;
	}
	public void setDataTableTipoItemDeControle(
			UIDataTable dataTableTipoItemDeControle) {
		this.dataTableTipoItemDeControle = dataTableTipoItemDeControle;
	}
	public static boolean isResearch() {
		return isResearch;
	}
	public static void setResearch(boolean isResearch) {
		TipoItemDeControleController.isResearch = isResearch;
	}
	public boolean isBoolError() {
		return boolError;
	}
	public void setBoolError(boolean boolError) {
		this.boolError = boolError;
	}
	public IControladorTipoItemDeControle getTipoItemDeControleService() {
		return tipoItemDeControleService;
	}
	public void setTipoItemDeControleService(
			IControladorTipoItemDeControle tipoItemDeControleService) {
		this.tipoItemDeControleService = tipoItemDeControleService;
	}

	public List<MetaInterna> getMetaInternaList() {
		return metaInternaList;
	}

	public void setMetaInternaList(List<MetaInterna> metaInternaList) {
		this.metaInternaList = metaInternaList;
	}

	public List<RegraFarol> getRegraFarolList() {
		return regraFarolList;
	}

	public void setRegraFarolList(List<RegraFarol> regraFarolList) {
		this.regraFarolList = regraFarolList;
	}

	public List<CoeficienteDeDegradacao> getCoeficienteList() {
		return coeficienteList;
	}

	public void setCoeficienteList(List<CoeficienteDeDegradacao> coeficienteList) {
		this.coeficienteList = coeficienteList;
	}

	public IControladorCoeficienteDeDegradacao getCoeficienteDeDegradacaoService() {
		return coeficienteDeDegradacaoService;
	}

	public void setCoeficienteDeDegradacaoService(
			IControladorCoeficienteDeDegradacao coeficienteDeDegradacaoService) {
		this.coeficienteDeDegradacaoService = coeficienteDeDegradacaoService;
	}

	public IControladorRegraFarol getRegraFarolService() {
		return regraFarolService;
	}

	public void setRegraFarolService(IControladorRegraFarol regraFarolService) {
		this.regraFarolService = regraFarolService;
	}

	public IControladorMetaInterna getMetaInternaService() {
		return metaInternaService;
	}

	public void setMetaInternaService(IControladorMetaInterna metaInternaService) {
		this.metaInternaService = metaInternaService;
	}

	public List<SelectItem> getMetaInternaItemList() {
		if (metaInternaItemList.isEmpty()) {
			try {
				metaInternaItemList.clear();
				List<MetaInterna> lista = metaInternaService.buscarMetaInterna(new MetaInterna());
				for(MetaInterna metaInterna : lista) {
					if (metaInterna.getTipoDaMetaInterna().equals(TipoMetaInternaEnum.META_DE_EMBARQUE)) {
						SelectItem item = new SelectItem(metaInterna,metaInterna.toString());
						metaInternaItemList.add(item);
						metaInternaMap.put(metaInterna.getIdMetaInterna(), metaInterna);
					}	
				}
			} catch (ErroSistemicoException e) {
				e.printStackTrace();
			}	
		}
		return metaInternaItemList;
	}

	public void setMetaInternaItemList(List<SelectItem> metaInternaItemList) {
		this.metaInternaItemList = metaInternaItemList;
	}

	public List<SelectItem> getRegraFarolItemList() {
		if (regraFarolItemList.isEmpty()) {
			try {
				regraFarolItemList.clear();
				List<RegraFarol> lista = regraFarolService.buscarRegraFarol(new RegraFarol());
				for(RegraFarol regraFarol : lista) {
					SelectItem item = new SelectItem(regraFarol,regraFarol.toString());
					regraFarolItemList.add(item);
					regraFarolMap.put(regraFarol.getIdRegraFarol(), regraFarol);
				}
			} catch (ErroSistemicoException e) {
				e.printStackTrace();
			}
		}
		return regraFarolItemList;
	}

	public void setRegraFarolItemList(List<SelectItem> regraFarolItemList) {
		this.regraFarolItemList = regraFarolItemList;
	}

	public List<SelectItem> getCoeficienteItemList() {
		if (coeficienteItemList.isEmpty()) {
			try {
				coeficienteItemList.clear();
				List<CoeficienteDeDegradacao> lista = coeficienteDeDegradacaoService.buscarCoeficienteDeDegradacao(new CoeficienteDeDegradacao());
				for(CoeficienteDeDegradacao coeficiente : lista) {
					SelectItem item = new SelectItem(coeficiente,coeficiente.toString());
					coeficienteItemList.add(item);
					coeficienteMap.put(coeficiente.getIdCoeficiente(), coeficiente);
				}
			
			} catch (ErroSistemicoException e) {
				e.printStackTrace();
			}
		}
		return coeficienteItemList;
	}

	public void setCoeficienteItemList(List<SelectItem> coeficienteItemList) {
		this.coeficienteItemList = coeficienteItemList;
	}

	public Map<Long, MetaInterna> getMetaInternaMap() {
		return metaInternaMap;
	}

	public void setMetaInternaMap(Map<Long, MetaInterna> metaInternaMap) {
		this.metaInternaMap = metaInternaMap;
	}

	public Map<Long, RegraFarol> getRegraFarolMap() {
		return regraFarolMap;
	}

	public void setRegraFarolMap(Map<Long, RegraFarol> regraFarolMap) {
		this.regraFarolMap = regraFarolMap;
	}

	public Map<Long, CoeficienteDeDegradacao> getCoeficienteMap() {
		return coeficienteMap;
	}

	public void setCoeficienteMap(
			Map<Long, CoeficienteDeDegradacao> coeficienteMap) {
		this.coeficienteMap = coeficienteMap;
	}
	
	public List<SelectItem> getMetaInternaProducaoItemList() {
		if (metaInternaProducaoItemList.isEmpty()) {
			try {
				metaInternaProducaoItemList.clear();
				List<MetaInterna> lista = metaInternaService.buscarMetaInterna(new MetaInterna());
				for(MetaInterna metaInterna : lista) {
					if (metaInterna.getTipoDaMetaInterna().equals(TipoMetaInternaEnum.META_DE_PRODUCAO)) {
						SelectItem item = new SelectItem(metaInterna,metaInterna.toString());
						metaInternaProducaoItemList.add(item);
						metaInternaMap.put(metaInterna.getIdMetaInterna(), metaInterna);
					}		
				}
			} catch (ErroSistemicoException e) {
				e.printStackTrace();
			}	
		}		
		return metaInternaProducaoItemList;
	}

	public void setMetaInternaProducaoItemList(
			List<SelectItem> metaInternaProducaoItemList) {
		this.metaInternaProducaoItemList = metaInternaProducaoItemList;
	}

	public List<MetaInterna> getMetaInternaProducaoList() {
		return metaInternaProducaoList;
	}

	public void setMetaInternaProducaoList(List<MetaInterna> metaInternaProducaoList) {
		this.metaInternaProducaoList = metaInternaProducaoList;
	}

	public List<SelectItem> getEstadoEstruturaList() {
		estadoEstruturaList.clear();
		for(TipoItemControleEnum estadoEnum : TipoItemControleEnum.values() ) {
			SelectItem item = new SelectItem(estadoEnum);
			estadoEstruturaList.add(item);
		}
		return estadoEstruturaList;
	}
}