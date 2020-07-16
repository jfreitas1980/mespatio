/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hdntec.gestao.domain.navios.comparadores;

import java.util.Comparator;

import com.hdntec.gestao.domain.navios.entity.status.Navio;


/**
 *
 * @author Bruno Gomes
 */
public class ComparadorNavio implements Comparator<Navio>{

    @Override
    public int compare(Navio o1, Navio o2) {
        return o1.getEta().compareTo(o2.getEta());
    }

}
