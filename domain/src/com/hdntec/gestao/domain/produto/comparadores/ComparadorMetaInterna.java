package com.hdntec.gestao.domain.produto.comparadores;

import java.util.Comparator;

import com.hdntec.gestao.domain.produto.entity.MetaInterna;


public class ComparadorMetaInterna implements Comparator<MetaInterna> {

    @Override
    public int compare(MetaInterna metaInterna1, MetaInterna metaInterna2) {
       return 0;// metaInterna2.getDataDeCadastro().compareTo(metaInterna1.getDataDeCadastro());
    }
}
