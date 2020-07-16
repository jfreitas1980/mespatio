package br.com.cflex.supervision.stockyard.util;

import java.util.HashMap;
import java.util.Map;

import br.com.cflex.supervision.stockYard.servidor.modelo.plano.Atividade;

public class AtividadeUtil {
	private static Map<Long, Atividade> mapAtividade = new HashMap<Long, Atividade>();

	/**
	 * @return
	 */
	public static Map<Long, Atividade> getMapAtividade() {
		return mapAtividade;
	}

	/**
	 * @param mapAtividade
	 */
	public static void setMapAtividade(Map<Long, Atividade> mapAtividade) {
		AtividadeUtil.mapAtividade = mapAtividade;
	}
	
	/**
	 * @param atividade
	 */
	public static void addAtividade(Atividade atividade) {
    	if ( mapAtividade.containsKey(atividade.getId()) ) {
    		mapAtividade.remove(atividade.getId());
    	}
    	mapAtividade.put(atividade.getId(), atividade);
    }
	
	/**
	 * @param id
	 * @return
	 */
	public static Atividade getAtividade(Long id) {
		return mapAtividade.get(id);
	}
}
