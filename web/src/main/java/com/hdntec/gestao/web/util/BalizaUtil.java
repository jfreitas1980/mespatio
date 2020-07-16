package com.hdntec.gestao.web.util;

import java.util.HashMap;
import java.util.Map;

import com.hdntec.gestao.domain.planta.entity.status.Baliza;


public class BalizaUtil {

	private static Map<String,Baliza> mapBaliza = new HashMap<String,Baliza>();

	public static Map<String, Baliza> getMapBaliza() {
		return mapBaliza;
	}

	public static void setMapBaliza(Map<String, Baliza> mapBaliza) {
		BalizaUtil.mapBaliza = mapBaliza;
	}
	
	public static void addBaliza(Baliza baliza) {
    	if ( mapBaliza.containsKey(baliza.getNomeBaliza()) ) {
    		mapBaliza.remove(baliza.getNomeBaliza());
    	}
    	mapBaliza.put(baliza.getNomeBaliza(), baliza);
    }
	
	
	public static Baliza getBaliza(String nome) {
		return mapBaliza.get(nome);
	}
}
