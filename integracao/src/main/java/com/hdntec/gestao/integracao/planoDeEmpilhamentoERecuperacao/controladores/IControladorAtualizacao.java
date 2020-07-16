package com.hdntec.gestao.integracao.planoDeEmpilhamentoERecuperacao.controladores;

import java.util.List;

import javax.ejb.Remote;

import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;


/**
 * Interface de acesso às operações do subsistema de atualização.
 * 
 * @author andre
 * 
 */

@Remote
public interface IControladorAtualizacao {

    /**
     * Planeja atividades de atualização.
     * @param novasAtividadesAtualizacao as novas atividades de atualização a serem planejadas
     */
    public void planejarAtividadesAtualizacao(List<Atividade> novasAtividadesAtualizacao);

    /**
     * Envia mensagem para os clientes avisando que há novas situações de pátio.
     */
    public void enviaMensagemAtualizacaoDeSituacaoDePatio();

    /**
     * Cria uma lista de situacoes de patio a partir de uma lista de chegadas de navio
     * @param listaDeChegadaDeNavios - as novas atividades
     * @return List<SituacaoPatio> - a lista de situacoes de patio gerada
     */
    public List<SituacaoPatio> criaSituacoesChegadaDeNavios(List<Atividade> listaDeChegadaDeNavios);

    /**
     * Cria uma lista de situacoes de patio a partir de uma lista de mudanca de campanha
     * @param listaDeMudancasDeCampanha - as novas atividades
     * @return List<SituacaoPatio> - a lista de situacoes de patio gerada
     */
    public List<SituacaoPatio> criaSituacoesMudancaDeCampanha(List<Atividade> listaDeMudancasDeCampanha);

    /**
     * Cria uma lista de situacoes de patio a partir de uma lista resultados de amostragem
     * @param listaDeResultadosAmostragem - as novas atividades
     * @return List<SituacaoPatio> - a lista de situacoes de patio gerada
     */
    public List<SituacaoPatio> criaSituacoesResultadoAmostragem(List<Atividade> listaDeResultadosAmostragem);

    /**
     * Cria uma lista de situacoes de patio a partir de uma lista de saida de navios
     * @param listaDeSaidasDeNavios - as novas atividades
     * @return List<SituacaoPatio> - a lista de situacoes de patio gerada
     */
    public List<SituacaoPatio> criaSituacoesSaidaDeNavios(List<Atividade> listaDeSaidasDeNavios);

    /**
     * Cria uma lista de situacoes de patio a partir de uma lista de embarque de navios
     * @param listaDeEmbarquesDeNavios - as novas atividades
     * @return List<SituacaoPatio> - a lista de situacoes de patio gerada
     */
    public List<SituacaoPatio> criaSituacoesEmbarqueDeNavios(List<Atividade> listaDeEmbarquesDeNavios);
}