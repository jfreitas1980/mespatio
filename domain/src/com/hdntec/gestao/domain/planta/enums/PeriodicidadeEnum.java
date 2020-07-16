/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hdntec.gestao.domain.planta.enums;

import com.hdntec.gestao.util.PropertiesUtil;


/**
 *
 * @author guilherme
 */
public enum PeriodicidadeEnum {

    NUNCA(0),
    DIARIA(1),
    SEMANAL(2),
    QUINZENAL(3),
    MENSAL(4);
    private Integer valor;

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    public Integer getValor() {
        return valor;
    }

    PeriodicidadeEnum(Integer valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        String retorno = null;
        if (name().equals(NUNCA.name())) {
            retorno = PropertiesUtil.getMessage("enum.nunca");
        } else if (name().equals(DIARIA.name())) {
            retorno = PropertiesUtil.getMessage("enum.diaria");
        } else if (name().equals(SEMANAL.name())) {
            retorno = PropertiesUtil.getMessage("enum.semanal");
        } else if (name().equals(QUINZENAL.name())) {
            retorno = PropertiesUtil.getMessage("enum.quinzenal");
        } else if (name().equals(MENSAL.name())) {
            retorno = PropertiesUtil.getMessage("enum.mensal");
        } else {
            retorno = PropertiesUtil.getMessage("enum.retorno.sem.chave.de.bundle.cadastrada");
        }
        return retorno;
    }
}
