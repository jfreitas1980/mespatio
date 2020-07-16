/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.estruturasAuxiliares;

import java.util.List;

import com.hdntec.gestao.domain.plano.enums.SentidoEmpilhamentoRecuperacaoEnum;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;


/**
 *
 * @author guilherme
 */
public class TotalMovimentacao {
    private List<ProdutoMovimentacao> listaProdutoMovimentacao;
    private Baliza balizaEmpilhamento;
    private SentidoEmpilhamentoRecuperacaoEnum sentido;
    private Boolean destinoVazio;
    private String nomePilha;

    public TotalMovimentacao(List<ProdutoMovimentacao> listaProdutoMovimentacao, Baliza balizaEmpilhamento, SentidoEmpilhamentoRecuperacaoEnum sentido,Boolean destinoVazio, String nomeBaliza) {
        this.listaProdutoMovimentacao = listaProdutoMovimentacao;
        this.balizaEmpilhamento = balizaEmpilhamento;
        this.sentido = sentido;
        this.destinoVazio=destinoVazio;
        this.nomePilha=nomeBaliza;
    }

    public Baliza getBalizaEmpilhamento() {
        return balizaEmpilhamento;
    }

    public void setBalizaEmpilhamento(Baliza balizaEmpilhamento) {
        this.balizaEmpilhamento = balizaEmpilhamento;
    }

    public List<ProdutoMovimentacao> getListaProdutoMovimentacao() {
        return listaProdutoMovimentacao;
    }

    public void setListaProdutoMovimentacao(List<ProdutoMovimentacao> listaProdutoMovimentacao) {
        this.listaProdutoMovimentacao = listaProdutoMovimentacao;
    }

    public SentidoEmpilhamentoRecuperacaoEnum getSentido() {
        return sentido;
    }

    public void setSentido(SentidoEmpilhamentoRecuperacaoEnum sentido) {
        this.sentido = sentido;
    }

    public Boolean getDestinoVazio() {
        return destinoVazio;
    }

    public void setDestinoVazio(Boolean destinoVazio) {
        this.destinoVazio = destinoVazio;
    }

    public String getNomePilha() {
        return nomePilha;
    }

    public void setNomePilha(String nomePilha) {
        this.nomePilha = nomePilha;
    }



}