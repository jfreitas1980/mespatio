package com.hdntec.gestao.domain.planta.comparadores;

import java.util.Comparator;

import com.hdntec.gestao.domain.planta.entity.MetaBaliza;


public class ComparadorMetaBalizas implements Comparator<MetaBaliza> {

    @Override
    public int compare(MetaBaliza o1, MetaBaliza o2) {
        return o1.getNumero().compareTo(o2.getNumero());
    }
}
