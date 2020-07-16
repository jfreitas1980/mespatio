package com.hdntec.gestao.domain.planta.comparadores;

import java.util.Comparator;
import java.util.List;

import com.hdntec.gestao.domain.planta.entity.status.Baliza;


public class ComparadorBalizaNumero implements Comparator<Baliza> {

    @Override
    public int compare(Baliza o1, Baliza o2) {
        return o1.getNumero().compareTo(o2.getNumero());
    }

    public boolean equals(Baliza o1, Baliza o2) {
        return o1.getNumero().equals(o2.getNumero());
    }

    public boolean contains(List<Baliza> listaBalizas, Baliza baliza) {
        boolean retorno = false;
        for (Baliza balizaDaLista : listaBalizas) {
            if (equals(balizaDaLista, baliza)) {
                retorno = true;
            }
        }
        return retorno;
    }
}
