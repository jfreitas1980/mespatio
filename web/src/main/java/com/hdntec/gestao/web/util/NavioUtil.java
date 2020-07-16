package com.hdntec.gestao.web.util;

import java.util.HashMap;
import java.util.Map;

import com.hdntec.gestao.domain.navios.entity.status.Navio;


public class NavioUtil {

	private static Map<Long, Navio> mapNavio = new HashMap<Long, Navio>();

	/**
	 * @return
	 */
	public static Map<Long, Navio> getMapNavio() {
		return mapNavio;
	}

	/**
	 * @param mapNavio
	 */
	public static void setMapNavio(Map<Long, Navio> mapNavio) {
		NavioUtil.mapNavio = mapNavio;
	}
	
	/**
	 * @param navio
	 */
	public static void addNavio(Navio navio) {
    	if ( mapNavio.containsKey(navio.getIdNavio()) ) {
    		mapNavio.remove(navio.getIdNavio());
    	}
    	mapNavio.put(navio.getIdNavio(), navio);
    }
	
	/**
	 * @param id
	 * @return
	 */
	public static Navio getNavio(Long id) {
		return mapNavio.get(id);
	}
}
