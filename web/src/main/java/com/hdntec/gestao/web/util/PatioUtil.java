package com.hdntec.gestao.web.util;

import java.util.HashMap;
import java.util.Map;

import com.hdntec.gestao.domain.planta.entity.status.Patio;


public class PatioUtil {
	/** hashCode, Patio */
	private static Map<Integer, Patio> mapPatio = new HashMap<Integer, Patio>();

	/**
	 * @return
	 */
	public static Map<Integer, Patio> getMapPatio() {
		return mapPatio;
	}

	/**
	 * @param mapPatio
	 */
	public static void setMapPatio(Map<Integer, Patio> mapPatio) {
		PatioUtil.mapPatio = mapPatio;
	}

	/**
	 * @param patio
	 */
	public static void addPatio(Patio patio) {
		if (mapPatio.containsKey(patio.hashCode())) {
			mapPatio.remove(patio.hashCode());
		}
		mapPatio.put(patio.hashCode(), patio);
	}

	/**
 @param hashCode
	 * @return
	 */
	public static Patio getPatio(Integer hashCode) {
		return mapPatio.get(hashCode);
	}
}
