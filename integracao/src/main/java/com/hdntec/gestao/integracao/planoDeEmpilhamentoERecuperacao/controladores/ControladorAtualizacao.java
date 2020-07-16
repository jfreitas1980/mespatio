package com.hdntec.gestao.integracao.planoDeEmpilhamentoERecuperacao.controladores;

import java.util.List;

import javax.ejb.Stateless;

import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
import com.hdntec.gestao.integracao.controladores.IControladorModelo;


/**
 * Controla as operações do subsistema de atualização.
 * <p>
 * Requisitos especiais: - Envio de mensagens
 * 
 * @author andre
 * 
 */
@Stateless(name = "bs/ControladorAtualizacao", mappedName = "bs/ControladorAtualizacao")
public class ControladorAtualizacao implements IControladorAtualizacao {

    /** acesso às operações do subsistema de modelo de domínio */
    private IControladorModelo modelo;

    /** novas situações de pátio geradas na última atualização dos sistemas externos */
    private List<SituacaoPatio> listaDeNovasSituacoesDePatio;

    @Override
    public void enviaMensagemAtualizacaoDeSituacaoDePatio() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void planejarAtividadesAtualizacao(List<Atividade> novasAtividadesAtualizacao) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<SituacaoPatio> criaSituacoesChegadaDeNavios(List<Atividade> listaDeChegadaDeNavios) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<SituacaoPatio> criaSituacoesMudancaDeCampanha(List<Atividade> listaDeMudancasDeCampanha) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<SituacaoPatio> criaSituacoesResultadoAmostragem(List<Atividade> listaDeResultadosAmostragem) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<SituacaoPatio> criaSituacoesSaidaDeNavios(List<Atividade> listaDeSaidasDeNavios) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<SituacaoPatio> criaSituacoesEmbarqueDeNavios(List<Atividade> listaDeEmbarquesDeNavios) {
        throw new UnsupportedOperationException();
    }

    public IControladorModelo getModelo() {
        return modelo;
    }

    public void setModelo(IControladorModelo modelo) {
        this.modelo = modelo;
    }

    public List<SituacaoPatio> getListaDeNovasSituacoesDePatio() {
        return listaDeNovasSituacoesDePatio;
    }

    public void setListaDeNovasSituacoesDePatio(List<SituacaoPatio> listaDeNovasSituacoesDePatio) {
        this.listaDeNovasSituacoesDePatio = listaDeNovasSituacoesDePatio;
    }
}