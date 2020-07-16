package br.com.cflex.supervision.stockyard.cadastros.controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.component.UIDataTable;

import br.com.cflex.supervision.stockYard.servidor.modelo.exceptions.ErroSistemicoException;
import br.com.cflex.supervision.stockYard.servidor.modelo.produto.TipoDeProdutoEnum;
import br.com.cflex.supervision.stockYard.servidor.modelo.produto.TipoProduto;
import br.com.cflex.supervision.stockYard.servidor.modelo.produto.dao.TipoProdutoDAO;
import br.com.cflex.supervision.stockyard.util.SessionUtil;
import br.com.cflex.supervision.stockyard.util.TipoProdutoUtil;

/**
 * 
 * <P><B>Description :</B><BR>
 * General TipoProdutoController
 * </P>
 * <P>
 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
 * @since 07/07/2009
 * @version $Revision: 1.1 $
 */
public class TipoProdutoController {

    /**dataTableTipoProduto*/
    private UIDataTable dataTableTipoProduto;
    /**tipoProduto*/
    private TipoProduto tipoProduto;
    /**tipoProdutoPesquisa*/
    private TipoProduto tipoProdutoPesquisa = new TipoProduto();        
    /**tipoProdutoList*/
    private List<TipoProduto> tipoProdutoList = new ArrayList<TipoProduto>();
    
    private List<SelectItem> insumoList = new ArrayList<SelectItem>();
    
    private List<SelectItem> tipoList = new ArrayList<SelectItem>();
    
    /**isResearch*/
    private static boolean isResearch = false;
    /** boolError
     * Utilizado para não continuar a transação caso haja elementos nullos 
     **/
    private boolean boolError = false;
    /**produtoService*/
    /*@EJB
    private IControladorProduto produtoService;*/
    
    private static TipoProdutoDAO tipoProdutoDAO = new TipoProdutoDAO();
    
    
    
    
    public String buscaTipoProdutoPorFiltro() {
        isResearch = true;
        return null;
    }
    
    
    public String limpaFiltro() {
        initEntity();
        return null;
    }


    public List<TipoProduto> getTipoProdutoList() {
    	try {
    		if (isResearch) {
//    			tipoProdutoList = produtoService.buscarTiposProduto(tipoProdutoPesquisa);
    			tipoProdutoList = tipoProdutoDAO.buscaPorExemploTipoProduto(tipoProdutoPesquisa);
    		} else {
//    			tipoProdutoList = produtoService.buscarTiposProduto(new TipoProduto());
    			tipoProdutoList = tipoProdutoDAO.buscaPorExemploTipoProduto(new TipoProduto());
    		}
    	} catch (ErroSistemicoException e) {
    		e.printStackTrace();
    		SessionUtil.addErrorMessage("pesquisa_tipoproduto_erro");
    	} finally {
//    		produtoService.encerrarSessao();
    	}
    	
    	return tipoProdutoList;
    }
    
    public String novoTipoProduto() {
        initEntity();
        initObjectList();
        return TipoProdutoUtil.NOVO_TIPO_PRODUTO;
    }


    private void initEntity() {
        isResearch = false;
        boolError = false;
        tipoProduto = new TipoProduto();        
        tipoProdutoPesquisa = new TipoProduto();        
    }

    private void initObjectList() {        
    	insumoList.clear();
    	tipoList.clear();
    }



    
    public String gravaTipoProduto() {
    	try {

//    		tipoProduto = produtoService.salvaTipoProduto(tipoProduto);
    		tipoProduto = tipoProdutoDAO.salvaTipoProduto(tipoProduto);
    		SessionUtil.addSucessMessage("sucess");

    		return TipoProdutoUtil.ATUALIZAR_TIPO_PRODUTO;

    	} catch (Exception e) {
    		e.printStackTrace();
    		SessionUtil.addErrorMessage("error");
    	}
    	finally {
//    		produtoService.encerrarSessao();
    	}
    	return null;
    }


    public String alteraTipoProduto() {
        initObjectList();
        tipoProduto = (TipoProduto) dataTableTipoProduto.getRowData();        
        return TipoProdutoUtil.ATUALIZAR_TIPO_PRODUTO;
    }

    

    public String excluiTipoProduto() {        
        try {
//            produtoService.removeTipoProduto(tipoProduto);
        	tipoProdutoDAO.removeTipoProduto(tipoProduto);
            SessionUtil.addSucessMessage("sucess");
        } catch (Exception e) {
        	e.printStackTrace();
        	SessionUtil.addErrorMessage("error");
        } finally {
//        	produtoService.encerrarSessao();
            initEntity();
        }
        return null;
    }



    public String cancelaTipoProduto() {
        tipoProduto = new TipoProduto();
        return TipoProdutoUtil.CANCELAR_TIPO_PRODUTO;
    }


    public void selectItem() {
        tipoProduto = (TipoProduto)dataTableTipoProduto.getRowData();        
    }


	public UIDataTable getDataTableTipoProduto() {
		return dataTableTipoProduto;
	}


	public void setDataTableTipoProduto(UIDataTable dataTableTipoProduto) {
		this.dataTableTipoProduto = dataTableTipoProduto;
	}


	public TipoProduto getTipoProduto() {
		return tipoProduto;
	}


	public void setTipoProduto(TipoProduto tipoProduto) {
		this.tipoProduto = tipoProduto;
	}


	public TipoProduto getTipoProdutoPesquisa() {
		return tipoProdutoPesquisa;
	}


	public void setTipoProdutoPesquisa(TipoProduto tipoProdutoPesquisa) {
		this.tipoProdutoPesquisa = tipoProdutoPesquisa;
	}


	public List<SelectItem> getInsumoList() {
			try {
				insumoList.clear();
//				List<TipoProduto> lista = produtoService.buscarTiposProduto(new TipoProduto());
				List<TipoProduto> lista = tipoProdutoDAO.buscaPorExemploTipoProduto(new TipoProduto());
				SelectItem nullItem = new SelectItem(null,"");
				insumoList.add(nullItem);
				for(TipoProduto tipo : lista) {
					TipoProdutoUtil.addTipoProduto(tipo);
					if (tipoProduto != null) {
						if (!tipo.getIdTipoProduto().equals(tipoProduto.getIdTipoProduto())){
							SelectItem item = new SelectItem(tipo,tipo.getDescricaoTipoProduto());
							insumoList.add(item);
						}
					}
					else {
						SelectItem item = new SelectItem(tipo,tipo.getDescricaoTipoProduto());
						insumoList.add(item);
					}
				}
			} catch (ErroSistemicoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
//				produtoService.encerrarSessao();
			}

		return insumoList;
	}

	public void setInsumoList(List<SelectItem> insumoList) {
		this.insumoList = insumoList;
	}


	public List<SelectItem> getTipoList() {
		tipoList.clear();
		for(TipoDeProdutoEnum tipo :TipoDeProdutoEnum.getTodos()) {
			SelectItem item = new SelectItem(tipo);
			tipoList.add(item);
		}
		return tipoList;
	}


	public void setTipoList(List<SelectItem> tipoList) {
		this.tipoList = tipoList;
	}


	public static boolean isResearch() {
		return isResearch;
	}


	public static void setResearch(boolean isResearch) {
		TipoProdutoController.isResearch = isResearch;
	}


	public boolean isBoolError() {
		return boolError;
	}


	public void setBoolError(boolean boolError) {
		this.boolError = boolError;
	}

/*
	public IControladorProduto getProdutoService() {
		return produtoService;
	}


	public void setProdutoService(IControladorProduto produtoService) {
		this.produtoService = produtoService;
	}
*/

	public void setTipoProdutoList(List<TipoProduto> tipoProdutoList) {
		this.tipoProdutoList = tipoProdutoList;
	}
}