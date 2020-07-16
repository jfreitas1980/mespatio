package com.hdntec.gestao.domain.produto.comparadores;

import java.util.Comparator;

import com.hdntec.gestao.domain.produto.entity.Rastreabilidade;


public class ComparadorRastreabilidade implements Comparator<Rastreabilidade> {

    @Override
    public int compare(Rastreabilidade rastreabilidade1, Rastreabilidade rastreabilidade2) {
        return rastreabilidade1.getNumeroRastreabilidade().compareTo(rastreabilidade2.getNumeroRastreabilidade());
    }
}
