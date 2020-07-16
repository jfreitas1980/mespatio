package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

import com.hdntec.gestao.cliente.interfaceProgramacao.InterfaceInicializacao;


/**
 * Apresenta a numeracao das balizas do patio num intervalo a cada 10 balizas
 * @author Bruno Gomes
 */
public class InterfaceIdentificadorBaliza extends JLabel implements InterfaceInicializacao
{
    private InterfaceBaliza interfaceBaliza;
    private int eixoX;
    private int eixoY;

    public InterfaceIdentificadorBaliza(InterfaceBaliza interfaceBaliza){
        this.interfaceBaliza = interfaceBaliza;
    }

    @Override
    public void inicializaInterface() {
        this.setFont(new Font("Verdana", 0, 8));
        this.setText(String.valueOf(interfaceBaliza.getBalizaVisualizada().getNumero()) );
        this.setForeground(new Color(104, 104, 104));
        
        eixoX = interfaceBaliza.getX();
        eixoY = interfaceBaliza.getY() - 10;//diminui o valor em Y para o numero ficar sobre a baliza representada

        if(interfaceBaliza.getBalizaVisualizada().getNumero() >= 100){
            //adaptando posicao para que os numeros maiores que 100 fiquem examamente ao centro da baliza que representam, para n�o haver confus�o visual
            this.setBounds(eixoX - (interfaceBaliza.getWidth() / 2) , eixoY, 25, 16);
        }else{
            this.setBounds(eixoX, eixoY, 25, 16);
        }
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
