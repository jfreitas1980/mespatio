/**
 * 
 */
package com.hdntec.gestao.domain.planta.enums;

import com.hdntec.gestao.util.PropertiesUtil;

/**
 * Estados possíveis para as máquinas.
 * 
 * @author andre
 * 
 */
public enum EstadoMaquinaEnum  {
	/** Máquina disponível para operação */
	OCIOSA,
	/**
	 * Máquina está sendo utilizada no momento em alguma operação. Portanto, não
	 * está disponível
	 */
	OPERACAO,
	/** Máquina está em manutenção. Portanto, não está disponível */
	MANUTENCAO;

	@Override
	public String toString() {
		String retorno = null;
		if (name().equals(OPERACAO.name())) {
			retorno = PropertiesUtil.buscarPropriedade("enum.operacao");
		} else if (name().equals(MANUTENCAO.name())) {
			retorno = PropertiesUtil.buscarPropriedade("enum.manutencao");
		} else if (name().equals(OCIOSA.name())) {
			retorno = PropertiesUtil.buscarPropriedade("enum.ociosa");
		} else {
			retorno = "Retorno sem chave de bundle cadastrada";
		}
		return retorno;
	}
}
