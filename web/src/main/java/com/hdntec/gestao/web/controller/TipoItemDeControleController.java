package com.hdntec.gestao.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import org.richfaces.component.UIDataTable;

import com.hdntec.gestao.domain.produto.dao.TipoItemDeControleDAO;
import com.hdntec.gestao.domain.produto.entity.MetaInterna;
import com.hdntec.gestao.domain.produto.entity.TipoItemDeControle;
import com.hdntec.gestao.domain.produto.enums.TipoItemControleEnum;
import com.hdntec.gestao.domain.produto.enums.TipoMetaInternaEnum;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.integracao.produto.IControladorMetaInterna;
import com.hdntec.gestao.integracao.produto.IControladorTipoItemDeControle;
import com.hdntec.gestao.web.util.SessionUtil;


public class TipoItemDeControleController {

    private TipoItemDeControle tipoItemDeControle;
    private TipoItemDeControle tipoItemDeControlePesquisa = new TipoItemDeControle();
    private List<TipoItemDeControle> tipoItemDeControleList = new ArrayList<TipoItemDeControle>();
    private List<MetaInterna> metaInternaProducaoList = new ArrayList<MetaInterna>();       
    private List<MetaInterna> metaInternaList = new ArrayList<MetaInterna>();
    private List<SelectItem> metaInternaItemList = new ArrayList<SelectItem>();
    private List<SelectItem> metaInternaProducaoItemList = new ArrayList<SelectItem>();
    private List<SelectItem> regraFarolItemList = new ArrayList<SelectItem>();
    private List<SelectItem> coeficienteItemList = new ArrayList<SelectItem>();
    private UIDataTable dataTableTipoItemDeControle;
    private List<SelectItem> estadoEstruturaList = new ArrayList<SelectItem>(); 
    private Map<Long, MetaInterna> metaInternaMap = new HashMap<Long, MetaInterna>();

    private static boolean isResearch = false;
    private boolean boolError = false;
    /**tipoItemDeControleService*/
    private TipoItemDeControleDAO dao = new TipoItemDeControleDAO(); 
   // @EJB(name = "dssStockYardSamarcoEAR/bs/ControladorTMetaInterna/local")
    private IControladorMetaInterna metaInternaService;
    
    private void initEntity() {
        isResearch = false;
        boolError = false;
        tipoItemDeControle = new TipoItemDeControle();
        tipoItemDeControlePesquisa = new TipoItemDeControle();
        tipoItemDeControlePesquisa.setDescricaoTipoItemControle("");
        tipoItemDeControleList = new ArrayList<TipoItemDeControle>();
        metaInternaList = new ArrayList<MetaInterna>();
        metaInternaProducaoList = new ArrayList<MetaInterna>();
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
            dao.removeTipoItemDeControle(tipoItemDeControle);
            SessionUtil.addSucessMessage("sucess");
        } catch (Exception e) {
            e.printStackTrace();
            SessionUtil.addErrorMessage("error");
        } finally {
            initEntity();
            dao.encerrarSessao();
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
            for (MetaInterna meta : metaInternaList)
                tipoItemDeControle.addMetaInterna(meta);
            for (MetaInterna meta : metaInternaProducaoList)                
                tipoItemDeControle.addMetaInterna(meta);
            
            tipoItemDeControle = dao.salvaTipoItemDeControle(tipoItemDeControle);
            SessionUtil.addSucessMessage("sucess");

            return "atualizarTipoItemDeControle";
        } catch (Exception e) {
            e.printStackTrace();
            SessionUtil.addErrorMessage("error");
        }
        finally {
//          tipoItemDeControleService.encerrarSessao();
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
                tipoItemDeControleList = dao
                        .buscarListaDeObjetos(tipoItemDeControlePesquisa);
            } else {                
                if (tipoItemDeControleList.isEmpty()) {
                    tipoItemDeControleList = dao.buscarTodos();
                }   
            }
        } catch (ErroSistemicoException e) {
            e.printStackTrace();
            SessionUtil.addErrorMessage("error");
        }
        finally {
//          tipoItemDeControleService.encerrarSessao();
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
    
    public List<MetaInterna> getMetaInternaList() {
        return metaInternaList;
    }

    public void setMetaInternaList(List<MetaInterna> metaInternaList) {
        this.metaInternaList = metaInternaList;
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

    
    public void setRegraFarolItemList(List<SelectItem> regraFarolItemList) {
        this.regraFarolItemList = regraFarolItemList;
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