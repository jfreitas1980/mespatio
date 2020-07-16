package com.hdntec.gestao.domain.produto.comparadores;

import java.util.Comparator;

import com.hdntec.gestao.domain.produto.entity.TipoProduto;


/**
 * Ordena os itensDeControle pelo atributo TipoItemDeControle.cdTipoItemControlePelota
 * @author Bruno Gomes
 */
public class ComparadorTipoProduto implements Comparator<TipoProduto>{

    @Override
    public int compare(TipoProduto o1, TipoProduto o2) {
        return o1.getCdProduto().compareTo(o2.getCdProduto());
    }

}
