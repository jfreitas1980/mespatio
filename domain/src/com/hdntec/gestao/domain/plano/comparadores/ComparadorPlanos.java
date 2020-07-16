package com.hdntec.gestao.domain.plano.comparadores;

import java.util.Comparator;

import com.hdntec.gestao.domain.plano.entity.PlanoEmpilhamentoRecuperacao;


/**
 * Comparador de planos de empilhamento e recuperacao por data/hora de inicio do plano.
 * @author Rodrigo Luchetta
 */
public class ComparadorPlanos implements Comparator<PlanoEmpilhamentoRecuperacao> {

    @Override
    public int compare(PlanoEmpilhamentoRecuperacao o1, PlanoEmpilhamentoRecuperacao o2) {
        return o1.getDtInicio().compareTo(o2.getDtInicio());
    }
}
