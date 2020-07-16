/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.comparadores;

import java.util.Comparator;

import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.InterfaceBaliza;


/**
 *
 * @author bgomes
 */
public class ComparadorInterfaceBaliza implements Comparator<InterfaceBaliza> {

    @Override
    public int compare(InterfaceBaliza o1, InterfaceBaliza o2) {
        return o1.getBalizaVisualizada().getNumero().compareTo(o2.getBalizaVisualizada().getNumero());
    }
}
