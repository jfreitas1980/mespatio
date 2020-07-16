package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio;

import java.awt.Font;

import javax.swing.JLabel;

import com.hdntec.gestao.cliente.interfaceProgramacao.InterfaceInicializacao;


/**
 *
 * @author Bruno Gomes
 */
public class InterfaceFinalBaliza extends JLabel implements InterfaceInicializacao{

    private InterfaceBaliza baliza;
    private int largura;
    private int eixoX;
    private int eixoY;
    private int altura;
    
    /**
     * construtor padrao
     */
    public InterfaceFinalBaliza(InterfaceBaliza baliza){
        this.baliza = baliza;
    }

    @Override
    public void inicializaInterface() {
        altura = 7;
        this.setFont(new Font("Dialog", 0, 7));
        this.setText("-|");

        largura = baliza.getWidth();
        eixoX = baliza.getX();
        //calculo para posicao da borda do final da baliza, o numero "50" foi tirado empiricamente ate atingir a distancia desejada
        eixoY = baliza.getHeight() + 50;
        this.setBounds(eixoX, eixoY, largura, altura);
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
