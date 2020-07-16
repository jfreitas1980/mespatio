package com.hdntec.gestao.web.util;

import java.util.HashMap;
import java.util.Map;

import com.hdntec.gestao.domain.produto.entity.TipoItemDeControle;



public class TipoItemControleUtil {

    public static final String NOVO_TIPO_PRODUTO = "novoTipoItem";

    public static final String ATUALIZAR_TIPO_PRODUTO = "atualizarTipoItem";

    public static final String CANCELAR_TIPO_PRODUTO = "cancelarTipoItem";
    
    private static Map<Long,TipoItemDeControle> mapTipoItem = new HashMap<Long,TipoItemDeControle>();


    public static Map<Long, TipoItemDeControle> getMapTipoItem() {
		return mapTipoItem;
	}


	public static void setMapTipoItem(Map<Long, TipoItemDeControle> mapTipoItem) {
		TipoItemControleUtil.mapTipoItem = mapTipoItem;
	}


	public static void addTipoItem(TipoItemDeControle TipoItem) {
    	if ( mapTipoItem.containsKey(TipoItem.getIdTipoItemDeControle()) ) {
    		mapTipoItem.remove(TipoItem.getIdTipoItemDeControle());
    	}
    	mapTipoItem.put(TipoItem.getIdTipoItemDeControle(), TipoItem);
    }
	
	public static TipoItemDeControle getTipoItem(Long id) {
		return mapTipoItem.get(id);
	}
}