/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.comparadores;

import java.util.Comparator;

import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.InterfacePilha;



/**
 *
 * @author bgomes
 */
public class ComparadorInterfacePilha implements Comparator<InterfacePilha> {

    @Override
    public int compare(InterfacePilha o1, InterfacePilha o2) {
        return o1.getPilhaVisualizada().getNomePilha().compareTo(o2.getPilhaVisualizada().getNomePilha());
    }
}
