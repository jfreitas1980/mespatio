package com.hdntec.gestao.cliente.planoDeEmpilhamentoERecuperacaoCliente.atualizacao.edicao;

import java.util.ArrayList;
import java.util.List;

/**
 * Esta classe representa o controle de edicoes no modo de edicao, controlando as acoes de undo e redo.
 * @author Ricardo Trabalho
 */
public class ControladorAtualizacaoEdicao {

    private List<ICommand> listaAtualizacaoEdicao;

    // posicao na lista de edicoes guardadas que representa qual a edicao atual feita.
    private int position = -1;

    /**
     * Desfaz a ultima edicao.
     */
    public void undo() {
        if (this.position > -1) {
            this.listaAtualizacaoEdicao.get(position).execute();
            this.position--;
        }
    }

    /**
     * Desfaz todas as edicoes feitas no modo de edicao.
     */
    public void undoAll() {
        while (this.position > -1) {
            this.undo();
        }
    }

    /**
     * Este metodo refaz uma acao desfeita.
     */
    public void redo() {
        if (this.position < this.listaAtualizacaoEdicao.size() - 1) {
            this.position++;
            this.listaAtualizacaoEdicao.get(position).execute();
        }
    }

    public List<ICommand> getListaAtualizacaoEdicao() {
        return listaAtualizacaoEdicao;
    }

    public void setListaAtualizacaoEdicao(List<ICommand> listaAtualizacaoEdicao) {
        this.listaAtualizacaoEdicao = listaAtualizacaoEdicao;
    }

    /**
     * Adiciona acao de edicao na lista de edicoes.
     * @param edicao Atividade de edicao.
     */
    public void adicionarEdicao(ICommand edicao) {
        if (this.listaAtualizacaoEdicao == null) {
            this.listaAtualizacaoEdicao = new ArrayList<ICommand>();
        }
        for (int i = this.position + 1; i < this.listaAtualizacaoEdicao.size() - 1; i++) {
            this.listaAtualizacaoEdicao.remove(i);
        }
        this.listaAtualizacaoEdicao.add(edicao);
        this.position++;
    }

    /**
     * Limpa o controlador de undo e redo do modo de edicao.
     */
    public void limpaControlador() {
        this.listaAtualizacaoEdicao = null;
        this.position = -1;
    }
}
