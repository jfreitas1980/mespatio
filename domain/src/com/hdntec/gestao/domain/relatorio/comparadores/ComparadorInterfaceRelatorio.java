/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hdntec.gestao.domain.relatorio.comparadores;

import java.util.Comparator;

import com.hdntec.gestao.domain.relatorio.PadraoRelatorio;


public class ComparadorInterfaceRelatorio implements Comparator<PadraoRelatorio> {

    @Override
    public int compare(PadraoRelatorio o1, PadraoRelatorio o2) {
        return o1.getNomePadraoRelatorio().compareTo(o2.getNomePadraoRelatorio());
    }
}
