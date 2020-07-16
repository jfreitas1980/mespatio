package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceNavegacao;

import java.util.List;

import com.hdntec.gestao.cliente.interfaceDeDialogo.IControladorInterfaceInicial;
import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;


/**
 * Controlador das operações do subsistema de interface gráfica de navegação de eventos
 * @author andre
 */
public class ControladorInterfaceNavegacaoEventos implements IControladorInterfaceNavegacaoEventos {

    /** a interface gráfica de navegação de eventos, que apresenta a barra de eventos */
    private InterfaceNavegacaoEventos interfaceNavegacaoEventos;

    /** acesso às operações do subsistema de interface principal */
    private IControladorInterfaceInicial interfaceInicial;

    public InterfaceNavegacaoEventos getInterfaceNavegacaoEventos() {
        return interfaceNavegacaoEventos;
    }

    public void setInterfaceNavegacaoEventos(InterfaceNavegacaoEventos interfaceNavegacaoEventos) {
        this.interfaceNavegacaoEventos = interfaceNavegacaoEventos;
    }

    public IControladorInterfaceInicial getInterfaceInicial() {
        return interfaceInicial;
    }

    public void setInterfaceInicial(IControladorInterfaceInicial interfaceInicial) {
        this.interfaceInicial = interfaceInicial;
    }

    @Override
    public void irParaOPresente() {
        interfaceNavegacaoEventos.irParaOPresente();
    }

    @Override
    public void atualizarSituacaoSelecionada(int indiceNovaSituacaoSelecionada) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void atulizarInterfaceNavegacaoEventos(List<SituacaoPatio> situacoesDePatio){
        Integer quantidadeSituacoes = situacoesDePatio.size();
        this.interfaceInicial.getInterfaceInicial().getSliderNavegacao().setMaximum(quantidadeSituacoes);
        this.interfaceInicial.getInterfaceInicial().setIndiceSituacaoPatioSelecionada(0);
        //this.interfaceInicial.getInterfaceInicial().montaPatio();
    }
}
