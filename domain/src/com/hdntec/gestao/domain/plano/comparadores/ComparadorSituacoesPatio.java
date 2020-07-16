package com.hdntec.gestao.domain.plano.comparadores;

import java.util.Comparator;

import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;


public class ComparadorSituacoesPatio implements Comparator<SituacaoPatio> {

    @Override
    public int compare(SituacaoPatio o1, SituacaoPatio o2) {
        return o1.getDtInicio().compareTo(o2.getDtInicio());
    }
}
