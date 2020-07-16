package br.com.cflex.supervision.stockyard.cadastros.controller;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import org.richfaces.component.UIDataTable;

import br.com.cflex.supervision.stockYard.servidor.modelo.exceptions.ErroSistemicoException;
import br.com.cflex.supervision.stockYard.servidor.modelo.produto.IControladorMetaInterna;
import br.com.cflex.supervision.stockYard.servidor.modelo.produto.IControladorProduto;
import br.com.cflex.supervision.stockYard.servidor.modelo.produto.MetaInterna;
import br.com.cflex.supervision.stockYard.servidor.modelo.produto.TipoMetaInternaEnum;
import br.com.cflex.supervision.stockYard.servidor.modelo.produto.TipoProduto;
import br.com.cflex.supervision.stockyard.util.SessionUtil;
import br.com.cflex.supervision.stockyard.util.TipoProdutoUtil;

public class MetaInternaController {

	private MetaInterna metaInterna;
	private MetaInterna metaInternaPesquisa;
	private List<MetaInterna> metaInternaList = new ArrayList<MetaInterna>();
	private List<SelectItem> tipoMetaList = new ArrayList<SelectItem>();
	private List<SelectItem> tipoPelotaList = new ArrayList<SelectItem>();
	private UIDataTable dataTableMetaInterna;

	private static boolean isResearch = false;
	private boolean boolError = false;
	/**metaInternaService*/
	@EJB(name = "dssStockYardSamarcoEAR/bs/ControladorMetaInterna/local")
	private IControladorMetaInterna metaInternaService;
    /**produtoService*/
    @EJB(name = "dssStockYardSamarcoEAR/bs/ControladorProduto/local")
    private IControladorProduto produtoService;

    public MetaInternaController() {
    	metaInternaPesquisa = new MetaInterna();
    	metaInternaPesquisa.setTipoPelota(new TipoProduto());
    }

    public String buscaMetaInternaPorFiltro() {
        isResearch = true;
        return null;
    }

    public String limpaFiltro() {
        initEntity();
        return null;
    }

    public void selectItem() {
		metaInterna = (MetaInterna)dataTableMetaInterna.getRowData();
	}

	private void initEntity() {
		isResearch = false;
		boolError = false;
		metaInterna = new MetaInterna();
		metaInternaPesquisa = new MetaInterna();
		metaInternaPesquisa.setTipoPelota(new TipoProduto());
	}

	private void initObjectList() {
		tipoMetaList.clear();
		tipoPelotaList.clear();
	}

	public String novaMetaInterna() {
		initEntity();
		initObjectList();
		return "novaMetaInterna";
	}

	public String alteraMetaInterna() {
		initObjectList();
		metaInterna = (MetaInterna)dataTableMetaInterna.getRowData();
		return "atualizarMetaInterna";
	}

	public String excluiMetaInterna() {
		try {
			metaInternaService.removeMetaInterna(metaInterna);
			SessionUtil.addSucessMessage("sucess");
		} catch (Exception e) {
			e.printStackTrace();
			SessionUtil.addErrorMessage("error");
		} finally {
			initEntity();
		}
		return null;
	}

	public String salvarMetaInterna() {
		try {
			metaInterna = metaInternaService.salvaMetaInterna(metaInterna);
			SessionUtil.addSucessMessage("sucess");

			return "atualizarMetaInterna";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			SessionUtil.addErrorMessage("error");
		}
		return null;
	}

	public MetaInterna getMetaInterna() {
		return metaInterna;
	}

	public void setMetaInterna(MetaInterna metaInterna) {
		this.metaInterna = metaInterna;
	}

	public MetaInterna getMetaInternaPesquisa() {
		return metaInternaPesquisa;
	}

	public void setMetaInternaPesquisa(MetaInterna metaInternaPesquisa) {
		this.metaInternaPesquisa = metaInternaPesquisa;
	}

	public List<MetaInterna> getMetaInternaList() {
		try {
			if (isResearch) {
				metaInternaList = metaInternaService.buscaMetaInternaPorFiltro(metaInternaPesquisa);
			} else {
				metaInternaList = metaInternaService.buscarMetaInterna(new MetaInterna());
			}
		} catch (ErroSistemicoException e) {
			e.printStackTrace();
			SessionUtil.addErrorMessage("error");
		}
		return metaInternaList;
	}

	public void setMetaInternaList(List<MetaInterna> metaInternaList) {
		this.metaInternaList = metaInternaList;
	}

	public UIDataTable getDataTableMetaInterna() {
		return dataTableMetaInterna;
	}

	public void setDataTableMetaInterna(UIDataTable dataTableMetaInterna) {
		this.dataTableMetaInterna = dataTableMetaInterna;
	}

	public static boolean isResearch() {
		return isResearch;
	}

	public static void setResearch(boolean isResearch) {
		MetaInternaController.isResearch = isResearch;
	}

	public boolean isBoolError() {
		return boolError;
	}

	public void setBoolError(boolean boolError) {
		this.boolError = boolError;
	}

	public List<SelectItem> getTipoMetaList() {
		tipoMetaList.clear();
		for (TipoMetaInternaEnum tipo : TipoMetaInternaEnum.getTodos()) {
			SelectItem item = new SelectItem(tipo);
			tipoMetaList.add(item);
		}
		return tipoMetaList;
	}

	public void setTipoMetaList(List<SelectItem> tipoMetaList) {
		this.tipoMetaList = tipoMetaList;
	}

	public IControladorMetaInterna getMetaInternaService() {
		return metaInternaService;
	}

	public void setMetaInternaService(IControladorMetaInterna metaInternaService) {
		this.metaInternaService = metaInternaService;
	}

	public List<SelectItem> getTipoPelotaList() {
		try {
			tipoPelotaList.clear();
			List<TipoProduto> lista = produtoService.buscarTiposProduto(new TipoProduto());
			for(TipoProduto tipo : lista) {
				TipoProdutoUtil.addTipoProduto(tipo);
				SelectItem item = new SelectItem(tipo,tipo.getDescricaoTipoProduto());
				tipoPelotaList.add(item);
			}
		} catch (ErroSistemicoException e) {
			e.printStackTrace();
		}
		return tipoPelotaList;
	}

	public void setTipoPelotaList(List<SelectItem> tipoPelotaList) {
		this.tipoPelotaList = tipoPelotaList;
	}
}