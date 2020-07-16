package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio;

import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;

import javax.swing.JLabel;

import com.hdntec.gestao.cliente.interfaceProgramacao.InterfaceInicializacao;
import com.hdntec.gestao.util.PropertiesUtil;

/**
 * Classe que apresenta o nome da pilha que esta representa
 * @author Bruno Gomes
 */
public class InterfaceNomeDaPilha extends JLabel implements InterfaceInicializacao{

    private InterfacePilha iPilha;
    private int altura;
    private int largura;
    private int eixoX;
    private int eixoY;

    public InterfaceNomeDaPilha(InterfacePilha pilha){
        this.iPilha = pilha;
    }

    @Override
    public void inicializaInterface() {
        defineDimensoesFixas();
        if (iPilha.getListaDeBalizas().size() == 0 ) return;
        InterfaceBaliza balizaInicial = new LinkedList<InterfaceBaliza>(iPilha.getListaDeBalizas()).getFirst();
        eixoX = balizaInicial.getX();
        eixoY = balizaInicial.getHeight() + Integer.parseInt( PropertiesUtil.buscarPropriedade("dimensao.nome.piha.complemento.eixo.x") );
        this.setFont(new Font("Verdana", Font.PLAIN, 11));
        this.setForeground(new Color(255, 255, 255));
        this.setBackground(new Color(153, 155, 157));

        this.setText(iPilha.getPilhaVisualizada().getNomePilha());

        this.setOpaque(true);
        this.setBounds(eixoX, eixoY, largura, altura);
    }

    @Override
    public void defineDimensoesFixas() {
        altura = Integer.parseInt( PropertiesUtil.buscarPropriedade("dimensao.nome.piha.label.altura") );
        largura = Integer.parseInt( PropertiesUtil.buscarPropriedade("dimensao.nome.piha.label.comprimento") );
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
