package com.hdntec.gestao.domain.planta.comparadores;

import java.util.Comparator;

import com.hdntec.gestao.domain.planta.entity.status.Patio;


public class ComparadorPatios implements Comparator<Patio> {

    @Override
    public int compare(Patio o1, Patio o2) {
        return o1.getNumero().compareTo(o2.getNumero());
    }
}
