package br.com.cflex.supervision.stockyard.cadastros.controller;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import org.richfaces.component.UIDataTable;

import br.com.cflex.supervision.stockYard.servidor.modelo.exceptions.ErroSistemicoException;
import br.com.cflex.supervision.stockYard.servidor.modelo.produto.CoeficienteDeDegradacao;
import br.com.cflex.supervision.stockYard.servidor.modelo.produto.IControladorCoeficienteDeDegradacao;
import br.com.cflex.supervision.stockYard.servidor.modelo.produto.IControladorProduto;
import br.com.cflex.supervision.stockYard.servidor.modelo.produto.TipoProduto;
import br.com.cflex.supervision.stockyard.util.SessionUtil;
import br.com.cflex.supervision.stockyard.util.TipoProdutoUtil;

public class CoeficienteDeDegradacaoController {
	
	private CoeficienteDeDegradacao coeficienteDeDegradacao;
	private CoeficienteDeDegradacao coeficienteDeDegradacaoPesquisa;
	private List<CoeficienteDeDegradacao> coeficienteDeDegradacaoList = new ArrayList<CoeficienteDeDegradacao>();
	private List<SelectItem> tipoProdutoList = new ArrayList<SelectItem>();
	private UIDataTable dataTableCoeficienteDeDegradacao;
	
	private static boolean isResearch = false;
	private boolean boolError = false;

	@EJB
	private IControladorCoeficienteDeDegradacao coeficienteDeDegradacaoService;
    /**produtoService*/
    @EJB
    private IControladorProduto produtoService;

    public CoeficienteDeDegradacaoController() {
    	coeficienteDeDegradacao = new CoeficienteDeDegradacao();
    	coeficienteDeDegradacao.setTipoDeProduto(new TipoProduto());
    	coeficienteDeDegradacaoPesquisa = new CoeficienteDeDegradacao();
    	coeficienteDeDegradacaoPesquisa.setTipoDeProduto(new TipoProduto());
    }

    public String buscaCoeficienteDeDegradacaoPorFiltro() {
        isResearch = true;
        return null;
    }

    public String limpaFiltro() {
        initEntity();
        return null;
    }

    private void initEntity() {
		isResearch = false;
		boolError = false;
		coeficienteDeDegradacao = new CoeficienteDeDegradacao();
		coeficienteDeDegradacaoPesquisa = new CoeficienteDeDegradacao();
	}
	
    public void selectItem() {
    	coeficienteDeDegradacao = (CoeficienteDeDegradacao)dataTableCoeficienteDeDegradacao.getRowData();        
    }

	private void initObjectList() {
		tipoProdutoList.clear();
	}

	public String novoCoeficienteDeDegradacao() {
		initEntity();
		initObjectList();
		return "novoCoeficienteDeDegradacao";
	}
	
	public String alteraCoeficienteDeDegradacao() {
        initObjectList();
        coeficienteDeDegradacao = (CoeficienteDeDegradacao)dataTableCoeficienteDeDegradacao.getRowData();        
        return "atualizarCoeficienteDeDegradacao";
    }
    
    public String excluiCoeficienteDeDegradacao() {        
        try {
        	coeficienteDeDegradacaoService.removeCoeficienteDeDegradacao(coeficienteDeDegradacao);
            SessionUtil.addSucessMessage("sucess");
        } catch (Exception e) {
        	e.printStackTrace();
        	SessionUtil.addErrorMessage("error");
        } finally {
            initEntity();
        }
        return null;
    }
    
    public String salvarCoeficienteDeDegradacao() {
    	try {
    		coeficienteDeDegradacao = coeficienteDeDegradacaoService.salvaCoeficienteDeDegradacao(coeficienteDeDegradacao);
    		SessionUtil.addSucessMessage("sucess");
    		return "atualizarCoeficienteDeDegradacao";
    	} catch (Exception e) {
    		e.printStackTrace();
    		SessionUtil.addErrorMessage("error");
    	}
    	return null;
    }
	
	public CoeficienteDeDegradacao getCoeficienteDeDegradacao() {
		return coeficienteDeDegradacao;
	}
	
	public void setCoeficienteDeDegradacao(
			CoeficienteDeDegradacao coeficienteDeDegradacao) {
		this.coeficienteDeDegradacao = coeficienteDeDegradacao;
	}
	
	public CoeficienteDeDegradacao getCoeficienteDeDegradacaoPesquisa() {
		return coeficienteDeDegradacaoPesquisa;
	}
	
	public void setCoeficienteDeDegradacaoPesquisa(
			CoeficienteDeDegradacao coeficienteDeDegradacaoPesquisa) {
		this.coeficienteDeDegradacaoPesquisa = coeficienteDeDegradacaoPesquisa;
	}
	
	public List<CoeficienteDeDegradacao> getCoeficienteDeDegradacaoList() {
		
		try {
			if (isResearch) {
				coeficienteDeDegradacaoList = coeficienteDeDegradacaoService
						.buscaCoeficienteDeDegradacaoPorFiltro(coeficienteDeDegradacaoPesquisa);
			} else {

				coeficienteDeDegradacaoList = coeficienteDeDegradacaoService
						.buscarCoeficienteDeDegradacao(new CoeficienteDeDegradacao());
			}
		} catch (ErroSistemicoException e) {
			e.printStackTrace();
			SessionUtil.addErrorMessage("error");
		}
		return coeficienteDeDegradacaoList;
	}
	
	public void setCoeficienteDegradacaoList(
			List<CoeficienteDeDegradacao> coeficienteDegradacaoList) {
		this.coeficienteDeDegradacaoList = coeficienteDegradacaoList;
	}
	
	public List<SelectItem> getTipoProdutoList() {
		try {
			tipoProdutoList.clear();
			List<TipoProduto> lista = produtoService.buscarTiposProduto(new TipoProduto());
			for(TipoProduto tipo : lista) {
				TipoProdutoUtil.addTipoProduto(tipo);
				SelectItem item = new SelectItem(tipo,tipo.getDescricaoTipoProduto());
				tipoProdutoList.add(item);
			}
		} catch (ErroSistemicoException e) {
			e.printStackTrace();
		}
		return tipoProdutoList;
	}
	
	public void setTipoProdutoList(List<SelectItem> tipoProdutoList) {
		this.tipoProdutoList = tipoProdutoList;
	}
	
	public UIDataTable getDataTableCoeficienteDeDegradacao() {
		return dataTableCoeficienteDeDegradacao;
	}
	
	public void setDataTableCoeficienteDeDegradacao(
			UIDataTable dataTableCoeficienteDeDegradacao) {
		this.dataTableCoeficienteDeDegradacao = dataTableCoeficienteDeDegradacao;
	}

	public static boolean isResearch() {
		return isResearch;
	}

	public static void setResearch(boolean isResearch) {
		CoeficienteDeDegradacaoController.isResearch = isResearch;
	}

	public boolean isBoolError() {
		return boolError;
	}

	public void setBoolError(boolean boolError) {
		this.boolError = boolError;
	}

	public IControladorCoeficienteDeDegradacao getCoeficienteDeDegradacaoService() {
		return coeficienteDeDegradacaoService;
	}

	public void setCoeficienteDeDegradacaoService(
			IControladorCoeficienteDeDegradacao coeficienteDeDegradacaoService) {
		this.coeficienteDeDegradacaoService = coeficienteDeDegradacaoService;
	}

	public void setCoeficienteDeDegradacaoList(
			List<CoeficienteDeDegradacao> coeficienteDeDegradacaoList) {
		this.coeficienteDeDegradacaoList = coeficienteDeDegradacaoList;
	}
}
