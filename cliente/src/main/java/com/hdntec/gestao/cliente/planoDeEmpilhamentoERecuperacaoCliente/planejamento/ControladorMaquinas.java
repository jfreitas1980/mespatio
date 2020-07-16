/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hdntec.gestao.cliente.planoDeEmpilhamentoERecuperacaoCliente.planejamento;

import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.integracao.planta.IMaquina;


/**
 *
 * @author Ricardo Trabalho
 */
public class ControladorMaquinas {

    /**
     * Desloca maquina de baliza de acordo com sua operacao atual e retorna o tempo da operacao.
     * operacao == true => Recuperancao ou empilhando.
     * operacao == false => Parada mas em funcionamento.
     * @param maquina Maquina a ser deslocada
     * @param balizaFinal Baliza onde devera se posicionar a maquina
     * @param operacao Modo de operacao da maquina no momento.
     * @return
     */
    public static Long deslocaMaquina(IMaquina maquina, Baliza balizaFinal){
        Long tempoDeslocamento = null;
        tempoDeslocamento = calculaTempoDeslocamentoNaoOperando(maquina, balizaFinal);
        maquina.setPosicao(balizaFinal);
        return tempoDeslocamento;
    }

  
    /** tempo de deslocamento ate uma baliza de uma maquina nao operando */
    private static Long calculaTempoDeslocamentoNaoOperando(IMaquina maquina, Baliza balizaFinal){
        Baliza balizaInicial = maquina.getPosicao();
        Long velocidadeDeslocamento = maquina.getVelocidadeDeslocamento().longValue();
        Integer mediaBalizas = Math.abs(balizaFinal.getNumero() - balizaInicial.getNumero());
        return mediaBalizas*velocidadeDeslocamento;
    }

    /** tempo de deslocamento ate uma baliza de uma maquina em operacao recolhendo todo conteudo das balizas */
    private static Long calculaTempoDeslocamentoOperando(IMaquina maquina, int indexBalizaInicial, int indexBalizaFinal){
        // Taxa de operacao por tonelada.
        Long taxaDeOperacao = maquina.getTaxaDeOperacaoTemporaria().longValue();
        return taxaDeOperacao*Math.abs(indexBalizaFinal - indexBalizaInicial);
    }


}
