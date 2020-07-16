package com.hdntec.gestao.cliente.planoDeEmpilhamentoERecuperacaoCliente.atualizacao.edicao;

/**
 * Interface que representa qualquer acao de edicao no modo de edicao. Ela segue o pattern Command.
 * @author Ricardo Trabalho
 */
public interface ICommand {

    /**
     * Desfaz uma acao de edicao feita anteriomente colocando os dados salvos de volta nos respectivos objetos
     */
    public void execute();
}
