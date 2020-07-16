/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hdntec.gestao.domain.planta.comparadores;

import java.util.Comparator;

import com.hdntec.gestao.domain.planta.entity.status.Berco;


/**
 *
 * @author Bruno Gomes
 */
public class ComparadorBerco implements Comparator<Berco> {

    public int compare(Berco o1, Berco o2) {
        return o1.getNomeBerco().compareTo(o2.getNomeBerco());
    }
}
