package com.hdntec.gestao.domain.plano.comparadores;

import java.util.Comparator;

import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;


public class ComparadorSituacaoPatioPorTipoAtividade implements Comparator<SituacaoPatio>{

	@Override
	public int compare(SituacaoPatio o1, SituacaoPatio o2) {
		return o1.getAtividade().getTipoAtividade().compareTo(
				o2.getAtividade().getTipoAtividade());
	}
}
