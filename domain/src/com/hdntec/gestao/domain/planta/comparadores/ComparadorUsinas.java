package com.hdntec.gestao.domain.planta.comparadores;

import java.util.Comparator;

import com.hdntec.gestao.domain.planta.entity.status.Usina;


public class ComparadorUsinas implements Comparator<Usina> {

    @Override
    public int compare(Usina o1, Usina o2) {
        return o1.getNomeUsina().compareTo(o2.getNomeUsina());
    }
}
