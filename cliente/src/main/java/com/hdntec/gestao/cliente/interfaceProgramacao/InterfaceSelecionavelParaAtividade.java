package com.hdntec.gestao.cliente.interfaceProgramacao;

/**
 * Interfaces que representam itens que podem ser selecionados para atividades de blendagem, empilhamento e recuperação.
 * 
 * @author andre
 * 
 */
public interface InterfaceSelecionavelParaAtividade {

    /**
     * Seleciona a interface.
     *
     * @return <code>true</code> se a seleção se deu com sucesso
     */
    public abstract void selecionar();

    /**
     * Deseleciona a interface.
     *
     * @return <code>true</code> se a deseleção se deu com sucesso
     */
    public abstract void deselecionar();

    /**
     * @return the selecionada
     */
    public abstract Boolean isSelecionada();

    /**
     * @param selecionada
     *           the selecionada to set
     */
    public abstract void setSelecionada(Boolean selecionada);

}