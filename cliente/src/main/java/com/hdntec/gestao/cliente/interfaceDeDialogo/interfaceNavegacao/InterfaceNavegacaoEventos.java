package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceNavegacao;


import com.hdntec.gestao.cliente.interfaceDeDialogo.InterfaceInicial;

/**
 * Interface gráfica que apresenta a barra para navegação de eventos.
 * 
 * @author andre
 * 
 */
public class InterfaceNavegacaoEventos {

    /** o índice do evento selecionado atualmente */
    private int indiceSituacaoSelecionada;

    /** acesso às operações do subsistema de navegação de eventos */
    private ControladorInterfaceNavegacaoEventos controladorInterfaceNavegacaoEventos;

    /** interface gráfica principal */
    private InterfaceInicial interfaceInicial;

    public InterfaceNavegacaoEventos() {
        controladorInterfaceNavegacaoEventos = new ControladorInterfaceNavegacaoEventos();
    }

    public ControladorInterfaceNavegacaoEventos getControladorInterfaceNavegacaoEventos() {
        return controladorInterfaceNavegacaoEventos;
    }

    public void setControladorInterfaceNavegacaoEventos(ControladorInterfaceNavegacaoEventos controladorInterfaceNavegacaoEventos) {
        this.controladorInterfaceNavegacaoEventos = controladorInterfaceNavegacaoEventos;
    }

    public int getIndiceSituacaoSelecionada() {
        return indiceSituacaoSelecionada;
    }

    public void setIndiceSituacaoSelecionada(int indiceSituacaoSelecionada) {
        this.indiceSituacaoSelecionada = indiceSituacaoSelecionada;
    }

    public InterfaceInicial getInterfaceInicial() {
        return interfaceInicial;
    }

    public void setInterfaceInicial(InterfaceInicial interfaceInicial) {
        this.interfaceInicial = interfaceInicial;
    }

    /**
     * Muda a Situação apresentada na tela para a situação de pátio do presente
     */
    public void irParaOPresente() {
        //TODO precisa ser refeito o slider para varios planos, para depois emplementar o ir para a situacao no tempo presente.
    }

    /**
     * atualiza a Situação selecionda na barra de eventos para o índice passado como parâmetro
     * @param indiceNovaSituacaoSelecionada índice da nova Situação de Pátio a ser selecionada
     */
    public void atualizarSituacaoSelecionada(int indiceNovaSituacaoSelecionada) {
        throw new UnsupportedOperationException();
    }
}
