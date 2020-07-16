package com.hdntec.gestao.cliente.planoDeEmpilhamentoERecuperacaoCliente.atualizacao.edicao;

import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.InterfaceCorreia;
import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;


/**
 * Esta classe implementa a interface ICommand que representa uma acao de edicao de correia no modo de edicao.
 * @author Ricardo Trabalho
 */
public class AtualizacaoEdicaoCorreia implements ICommand {

    InterfaceCorreia interfaceCorreia = null;

    private Double taxaDeOperacao = null;

    private EstadoMaquinaEnum estado = null;

    /**
     * Este contrutor guardas os dados anteriores a uma edicao.
     * @param interfaceCorreia
     * @param taxaDeOperacao Double que representa a taxa de operacao da correia
     * @param estado Integer que representa em qual estado a maquina se encontra (Ocioso, Manutencao, Atividade)
     */
    public AtualizacaoEdicaoCorreia(InterfaceCorreia interfaceCorreia, Double taxaDeOperacao, EstadoMaquinaEnum estado) {
        this.interfaceCorreia = interfaceCorreia;
        this.taxaDeOperacao = taxaDeOperacao;
        this.estado = estado;
    }

    /**
     * Refaz edicao em correia visualizada
     */
    @Override
    public void execute() {
        this.interfaceCorreia.getCorreiaVisualizada().setEstado(this.estado);
        this.interfaceCorreia.getCorreiaVisualizada().setTaxaDeOperacao(this.taxaDeOperacao);
    }
}
