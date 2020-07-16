package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

import com.hdntec.gestao.cliente.interfaceProgramacao.InterfaceInicializacao;


/**
 *
 * @author Bruno Gomes
 */
public class InterfaceIdentificadorPatio extends JLabel implements InterfaceInicializacao
{
    private InterfacePatio iPatio;

    public InterfaceIdentificadorPatio(InterfacePatio interfacePatio){
        this.iPatio = interfacePatio;
    }

    @Override
    public void inicializaInterface() {
       this.setText(iPatio.getPatioVisualizado().getNomePatio());
       this.setFont(new Font("Verdana", Font.PLAIN, 11));
       this.setForeground(new Color(104, 104, 104));
       //posicao em que o nome do patio ira aparecer na interface
       this.setBounds(iPatio.getInterfaceDSP().getComponenteInterfaceDSP().getBounds().width  -80 ,  5  , 50, 10);
    }

    @Override
    public void defineDimensoesFixas() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void desabilitarMenus() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void habilitarMenus() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
