package br.com.cflex.supervision.stockyard.cadastros.controller;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import org.richfaces.component.UIDataTable;

import br.com.cflex.supervision.stockYard.servidor.modelo.exceptions.ErroSistemicoException;

import br.com.cflex.supervision.stockYard.servidor.modelo.produto.EnumValorRegraFarol;
import br.com.cflex.supervision.stockYard.servidor.modelo.produto.IControladorRegraFarol;
import br.com.cflex.supervision.stockYard.servidor.modelo.produto.RegraFarol;
import br.com.cflex.supervision.stockYard.servidor.modelo.produto.TipoProduto;
import br.com.cflex.supervision.stockyard.util.SessionUtil;
import br.com.cflex.supervision.stockyard.util.TipoProdutoUtil;

public class RegraFarolController {

	private RegraFarol regraFarol;
	private RegraFarol regraFarolPesquisa = new RegraFarol();
	private List<RegraFarol> regraFarolList = new ArrayList<RegraFarol>();
	private UIDataTable dataTableRegraFarol;
	private List<SelectItem> valorList = new ArrayList<SelectItem>();
	private List<SelectItem> tipoProdutoList = new ArrayList<SelectItem>();
	
	private static boolean isResearch = false;
	private boolean boolError = false;
	
    @EJB(name = "dssStockYardSamarcoEAR/bs/ControladorRegraFarol/local")
    private IControladorRegraFarol regraFarolService;

    public RegraFarolController() {
    	regraFarol = new RegraFarol();
    	regraFarol.setTipoProduto(new TipoProduto());
    	regraFarolPesquisa = new RegraFarol();
    	regraFarolPesquisa.setTipoProduto(new TipoProduto());
    }

    public void selectItem() {
    	regraFarol = (RegraFarol)dataTableRegraFarol.getRowData();        
    }

    private void initEntity() {
        isResearch = false;
        boolError = false;
        regraFarol = new RegraFarol();        
        regraFarolPesquisa = new RegraFarol();        
    }
	
    public String buscaPorFiltro() {
        isResearch = true;
        return null;
    }
    
    
    public String limpaFiltro() {
        initEntity();
        return null;
    }
    
    private void initObjectList() {        
    	valorList.clear();
    	tipoProdutoList.clear();
    }
    
    public String novaRegraFarol() {
        initEntity();
        initObjectList();
        return "novaRegraFarol";
    }
    
    public String alteraRegraFarol() {
        initObjectList();
        regraFarol = (RegraFarol)dataTableRegraFarol.getRowData();        
        return "atualizarRegraFarol";
    }
    
    public String excluiRegraFarol() {        
        try {
        	regraFarolService.removeRegraFarol(regraFarol);
            SessionUtil.addSucessMessage("sucess");
        } catch (Exception e) {
        	e.printStackTrace();
        	SessionUtil.addErrorMessage("error");
        } finally {
            initEntity();
            regraFarolService.encerrarSessao();
        }
        return null;
    }
    
    
    public String salvarRegraFarol() {
    	try {
    		regraFarol = regraFarolService.salvaRegraFarol(regraFarol);
    		SessionUtil.addSucessMessage("sucess");

    		return "atualizarRegraFarol";
    	} catch (Exception e) {
    		e.printStackTrace();
    		SessionUtil.addErrorMessage("error");
    	}
    	finally {
    		regraFarolService.encerrarSessao();
    	}
    	return null;
    }
    
	public List<RegraFarol> getRegraFarolList() {
    	try {
    		if (isResearch) {
    			TipoProduto aux = new TipoProduto();
    			aux.setDescricaoTipoProduto(regraFarolPesquisa.getTipoProduto().getDescricaoTipoProduto());
    			regraFarolPesquisa.setTipoProduto(aux);
    			regraFarolList = regraFarolService.buscarRegraFarolPorFiltro(regraFarolPesquisa);
    		} else {
    			regraFarolList = regraFarolService.buscarRegraFarol(new RegraFarol());
    		}
    	} catch (ErroSistemicoException e) {

    		e.printStackTrace();
    		SessionUtil.addErrorMessage("pesquisa_tipoproduto_erro");
    	}
    	finally {
    		regraFarolService.encerrarSessao();
    	}
    	return regraFarolList;
	}

	public void setRegraFarolList(List<RegraFarol> regraFarolList) {
		this.regraFarolList = regraFarolList;
	}

	public RegraFarol getRegraFarol() {
		return regraFarol;
	}

	public void setRegraFarol(RegraFarol regraFarol) {
		this.regraFarol = regraFarol;
	}

	public RegraFarol getRegraFarolPesquisa() {
		return regraFarolPesquisa;
	}

	public void setRegraFarolPesquisa(RegraFarol regraFarolPesquisa) {
		this.regraFarolPesquisa = regraFarolPesquisa;
	}

	public static boolean isResearch() {
		return isResearch;
	}

	public static void setResearch(boolean isResearch) {
		RegraFarolController.isResearch = isResearch;
	}

	public boolean isBoolError() {
		return boolError;
	}

	public void setBoolError(boolean boolError) {
		this.boolError = boolError;
	}

	public List<SelectItem> getValorList() {
		valorList.clear();
		for(EnumValorRegraFarol tipo :EnumValorRegraFarol.getTodos()) {
			SelectItem item = new SelectItem(tipo);
			valorList.add(item);
		}
		return valorList;
	}

	public void setValorList(List<SelectItem> valorList) {
		this.valorList = valorList;
	}

	public List<SelectItem> getTipoProdutoList() {
		
		try {
			tipoProdutoList.clear();
			List<TipoProduto> lista = regraFarolService.buscarTiposProduto(new TipoProduto());
			for(TipoProduto tipo : lista) {
				TipoProdutoUtil.addTipoProduto(tipo);
				SelectItem item = new SelectItem(tipo,tipo.getDescricaoTipoProduto());
				tipoProdutoList.add(item);
			}
		} catch (ErroSistemicoException e) {
			e.printStackTrace();
		}
		finally {
			regraFarolService.encerrarSessao();
		}
		return tipoProdutoList;
	}

	public void setTipoProdutoList(List<SelectItem> tipoProdutoList) {
		this.tipoProdutoList = tipoProdutoList;
	}

	public UIDataTable getDataTableRegraFarol() {
		return dataTableRegraFarol;
	}

	public void setDataTableRegraFarol(UIDataTable dataTableRegraFarol) {
		this.dataTableRegraFarol = dataTableRegraFarol;
	}

	
	

}
