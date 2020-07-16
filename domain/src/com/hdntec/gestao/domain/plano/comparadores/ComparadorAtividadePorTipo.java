package com.hdntec.gestao.domain.plano.comparadores;

import java.util.Comparator;

import com.hdntec.gestao.domain.plano.entity.Atividade;


/**
 * 
 * @author hdn.darley@cflex.com.br
 *
 */
public class ComparadorAtividadePorTipo implements Comparator<Atividade> {

	@Override
	public int compare(Atividade o1, Atividade o2) {
		return o1.getTipoAtividade().compareTo(o2.getTipoAtividade());
	}
}
