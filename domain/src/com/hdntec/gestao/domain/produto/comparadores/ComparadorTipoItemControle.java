
package com.hdntec.gestao.domain.produto.comparadores;

import java.util.Comparator;

import com.hdntec.gestao.domain.produto.entity.TipoItemDeControle;


/**
 * Ordena os itensDeControle pelo atributo TipoItemDeControle.cdTipoItemControlePelota
 * @author Bruno Gomes
 */
public class ComparadorTipoItemControle implements Comparator<TipoItemDeControle>{

    @Override
    public int compare(TipoItemDeControle o1, TipoItemDeControle o2) {
        return o1.getDescricaoTipoItemControle().compareTo(o2.getDescricaoTipoItemControle());
    }

}
