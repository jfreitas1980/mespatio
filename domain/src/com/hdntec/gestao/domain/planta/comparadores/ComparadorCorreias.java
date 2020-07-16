package com.hdntec.gestao.domain.planta.comparadores;

import java.util.Comparator;

import com.hdntec.gestao.domain.planta.entity.status.Correia;


public class ComparadorCorreias implements Comparator<Correia> {

    @Override
    public int compare(Correia o1, Correia o2) {
        return o1.getNumero().compareTo(o2.getNumero());
    }
}
