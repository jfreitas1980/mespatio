package com.hdntec.gestao.web.util;

import java.util.HashMap;
import java.util.Map;

import com.hdntec.gestao.domain.planta.entity.status.Campanha;


public class CampanhaUtil {
	private static Map<Long, Campanha> mapCampanha = new HashMap<Long, Campanha>();

	/**
	 * @return
	 */
	public static Map<Long, Campanha> getMapCampanha() {
		return mapCampanha;
	}

	/**
	 * @param mapCampanha
	 */
	public static void setMapCampanha(Map<Long, Campanha> mapCampanha) {
		CampanhaUtil.mapCampanha = mapCampanha;
	}
	
	/**
	 * @param campanha
	 */
	public static void addCampanha(Campanha campanha) {
    	if ( mapCampanha.containsKey(campanha.getIdCampanha()) ) {
    		mapCampanha.remove(campanha.getIdCampanha());
    	}
    	mapCampanha.put(campanha.getIdCampanha(), campanha);
    }
	
	/**
	 * @param id
	 * @return
	 */
	public static Campanha getCampanha(Long id) {
		return mapCampanha.get(id);
	}
}
