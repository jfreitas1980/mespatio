/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.estruturasAuxiliares;

import com.hdntec.gestao.domain.planta.entity.status.Baliza;

/**
 *
 * @author cflex
 */
public class ProdutoMovimentacao {
    private Baliza balizaSelecionada;
    private Boolean selecionada;
    private Double quantidadeMovimentacao;

    public ProdutoMovimentacao(Baliza balizaSelecionada,Boolean selecionada,Double quantidadeMovimentacao )
    {
        this.balizaSelecionada=balizaSelecionada;
        this.selecionada=selecionada;
        this.quantidadeMovimentacao=quantidadeMovimentacao;
    }
    /**
     * @return the balizaSelecionada
     */
    public Baliza getBalizaSelecionada() {
        return balizaSelecionada;
    }

    /**
     * @param balizaSelecionada the balizaSelecionada to set
     */
    public void setBalizaSelecionada(Baliza balizaSelecionada) {
        this.balizaSelecionada = balizaSelecionada;
    }

    /**
     * @return the selecionada
     */
    public Boolean getSelecionada() {
        return selecionada;
    }

    /**
     * @param selecionada the selecionada to set
     */
    public void setSelecionada(Boolean selecionada) {
        this.selecionada = selecionada;
    }

    /**
     * @return the quantidadeMovimentacao
     */
    public Double getQuantidadeMovimentacao() {
        return quantidadeMovimentacao;
    }

    /**
     * @param quantidadeMovimentacao the quantidadeMovimentacao to set
     */
    public void setQuantidadeMovimentacao(Double quantidadeMovimentacao) {
        this.quantidadeMovimentacao = quantidadeMovimentacao;
    }

}
