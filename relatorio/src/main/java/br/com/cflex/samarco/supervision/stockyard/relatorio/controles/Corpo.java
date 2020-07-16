package br.com.cflex.samarco.supervision.stockyard.relatorio.controles;

import java.util.ArrayList;
import java.util.List;


public class Corpo {

	private List<Grupo> grupos;

	/**
	 * @return the grupos
	 */
	public List<Grupo> getGrupos() {
		if (grupos == null) {
			grupos = new ArrayList<Grupo>();
		}
		return grupos;
	}

	/**
	 * @param grupos the grupos to set
	 */
	public void addGrupo(Grupo grupo) {
		if (grupos == null) {
			grupos = new ArrayList<Grupo>();
		}
		this.grupos.add(grupo);
	}
	
	public void calculaGrupos(int numeroLinha) {
		
		for (Grupo grupo : this.getGrupos()) {
			grupo.totalizarGrupo(numeroLinha);
		}
	}
	
	/*public Map<Integer,Object> somaGrupos(int grupoTotalizacao) {
		
		Map<Integer,Object> result = new HashMap<Integer, Object>();
		
		for (Grupo grupo : this.getGrupos()) {
			Map<Integer,Object> parcialMap = new HashMap<Integer, Object>();
			if (grupo.getGrupoTotalizacao() == grupoTotalizacao) {
				parcialMap = grupo.somaRegistros();
			}
			
			for (Integer key : parcialMap.keySet()) {
				if (result.containsKey(key)) {
					Object obj = result.get(key);
					Object objParcial = parcialMap.get(key);
					if (obj instanceof Double && objParcial instanceof Double) {
						obj = ((Double)obj) + ((Double)objParcial);
					}
					result.put(key, obj);
				}
				else {
					result.put(key, parcialMap.get(key));
				}
			}
		}
		return result;
	}*/
}
