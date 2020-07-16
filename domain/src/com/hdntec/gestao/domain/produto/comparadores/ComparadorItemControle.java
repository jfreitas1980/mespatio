
package com.hdntec.gestao.domain.produto.comparadores;

import java.util.Comparator;

import com.hdntec.gestao.domain.produto.entity.ItemDeControle;


/**
 * Ordena os itensDeControle pelo atributo TipoItemDeControle.cdTipoItemControlePelota
 * @author Bruno Gomes
 */
public class ComparadorItemControle implements Comparator<ItemDeControle>{

    @Override
    public int compare(ItemDeControle o1, ItemDeControle o2) {
        return o1.getTipoItemControle().getCdTipoItemControlePelota().compareTo(o2.getTipoItemControle().getCdTipoItemControlePelota());
    }

}
