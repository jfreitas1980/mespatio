/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hdntec.gestao.integracao.planta;

import com.hdntec.gestao.domain.planta.entity.status.Baliza;

/**
 *
 * @author Ricardo Trabalho
 */
public interface IMaquina {

    public Baliza getPosicao();

    public Double getTaxaDeOperacaoTemporaria();
    public void setPosicao(Baliza baliza);
    public Double getVelocidadeDeslocamento();
    public void setVelocidadeDeslocamento(Double velocidadeDeslocamento);
    public Double getTaxaDeOperacao();
    public void setTaxaDeOperacao(Double taxaDeOperacao);
}
