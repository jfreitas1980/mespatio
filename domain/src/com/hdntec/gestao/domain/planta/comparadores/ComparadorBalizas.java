package com.hdntec.gestao.domain.planta.comparadores;

import java.util.Comparator;

import com.hdntec.gestao.domain.planta.entity.status.Baliza;


public class ComparadorBalizas implements Comparator<Baliza> {

    @Override
    public int compare(Baliza o1, Baliza o2) {
        return o1.getNumero().compareTo(o2.getNumero());
    }
}
