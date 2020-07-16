/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hdntec.gestao.integracao.pilhas;

import java.io.Serializable;
import java.util.LinkedList;

import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.Patio;
import com.hdntec.gestao.domain.planta.entity.status.Pilha;


/**
 *
 * @author Ricardo Trabalho
 */
public class ControladorPilha implements Serializable{

    /** Serializacao do objeto */
    private static final long serialVersionUID = 6477680621487025585L;

    /** pilha que esta controlada no momento */
    Pilha pilha = null;

    ControladorPilha(Pilha pilha) {
        this.pilha = pilha;
    }

    /**
     * Retorna o patio no qual a pilha em questao pertence
     * @return
     */
    public Patio obterPatio() {
        Patio patioPilha = new LinkedList<Baliza>(pilha.getListaDeBalizas()).getFirst().getPatio();
        return patioPilha;
    }


}
