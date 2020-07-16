package com.hdntec.gestao.domain.plano.comparadores;

import java.util.Comparator;

import com.hdntec.gestao.domain.plano.entity.Atividade;


/**
 * Comparador de atividades por hora de inicio de atividade.
 * @author Ricardo Trabalho
 */
public class ComparadorAtividades implements Comparator<Atividade> {

    @Override
    public int compare(Atividade o1, Atividade o2) {
        return o1.getDtInicio().compareTo(o2.getDtInicio());
    }
}
