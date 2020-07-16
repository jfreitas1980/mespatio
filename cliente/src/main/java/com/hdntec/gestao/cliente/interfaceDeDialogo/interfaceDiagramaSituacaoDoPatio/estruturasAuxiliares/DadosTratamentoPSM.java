/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.estruturasAuxiliares;

import java.util.List;

import com.hdntec.gestao.domain.plano.enums.SentidoEmpilhamentoRecuperacaoEnum;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.MaquinaDoPatio;


/**
 *
 * @author guilherme
 */
public class DadosTratamentoPSM {

    private List<ProdutoMovimentacao> listaProdutoTratado;
    private Baliza balizaInicialPellet;
    private Baliza balizaInicialProduto;
    private Baliza balizaInicialLixo;
    private SentidoEmpilhamentoRecuperacaoEnum sentidoPellet;
    private SentidoEmpilhamentoRecuperacaoEnum sentidoProduto;
    private SentidoEmpilhamentoRecuperacaoEnum sentidoLixo;
    private String nomePilhaPellet;
    private String nomePilhaProduto;
    private String nomePilhaLixo;
    private MaquinaDoPatio maquinaDoPatio;

    public DadosTratamentoPSM(List<ProdutoMovimentacao> listaProdutoTratado, Baliza balizaInicialPellet, Baliza balizaInicialProduto, Baliza balizaInicialLixo, SentidoEmpilhamentoRecuperacaoEnum sentidoPellet, SentidoEmpilhamentoRecuperacaoEnum sentidoProduto, SentidoEmpilhamentoRecuperacaoEnum sentidoLixo, String nomePilhaPellet, String nomePilhaProduto, String nomePilhaLixo, MaquinaDoPatio maquinaDoPatio) {
        this.listaProdutoTratado = listaProdutoTratado;
        this.balizaInicialPellet = balizaInicialPellet;
        this.balizaInicialProduto = balizaInicialProduto;
        this.balizaInicialLixo = balizaInicialLixo;
        this.sentidoPellet = sentidoPellet;
        this.sentidoProduto = sentidoProduto;
        this.sentidoLixo = sentidoLixo;
        this.nomePilhaPellet = nomePilhaPellet;
        this.nomePilhaProduto = nomePilhaProduto;
        this.nomePilhaLixo = nomePilhaLixo;
        this.maquinaDoPatio = maquinaDoPatio;

    }

    /**
     * @return the listaProdutoTratado
     */
    public List<ProdutoMovimentacao> getListaProdutoTratado() {
        return listaProdutoTratado;
    }

    /**
     * @param listaProdutoTratado the listaProdutoTratado to set
     */
    public void setListaProdutoTratado(List<ProdutoMovimentacao> listaProdutoTratado) {
        this.listaProdutoTratado = listaProdutoTratado;
    }

    /**
     * @return the balizaInicialPellet
     */
    public Baliza getBalizaInicialPellet() {
        return balizaInicialPellet;
    }

    /**
     * @param balizaInicialPellet the balizaInicialPellet to set
     */
    public void setBalizaInicialPellet(Baliza balizaInicialPellet) {
        this.balizaInicialPellet = balizaInicialPellet;
    }

    /**
     * @return the balizaInicialProduto
     */
    public Baliza getBalizaInicialProduto() {
        return balizaInicialProduto;
    }

    /**
     * @param balizaInicialProduto the balizaInicialProduto to set
     */
    public void setBalizaInicialProduto(Baliza balizaInicialProduto) {
        this.balizaInicialProduto = balizaInicialProduto;
    }

    /**
     * @return the sentidoPellet
     */
    public SentidoEmpilhamentoRecuperacaoEnum getSentidoPellet() {
        return sentidoPellet;
    }

    /**
     * @param sentidoPellet the sentidoPellet to set
     */
    public void setSentidoPellet(SentidoEmpilhamentoRecuperacaoEnum sentidoPellet) {
        this.sentidoPellet = sentidoPellet;
    }

    /**
     * @return the sentidoProduto
     */
    public SentidoEmpilhamentoRecuperacaoEnum getSentidoProduto() {
        return sentidoProduto;
    }

    /**
     * @param sentidoProduto the sentidoProduto to set
     */
    public void setSentidoProduto(SentidoEmpilhamentoRecuperacaoEnum sentidoProduto) {
        this.sentidoProduto = sentidoProduto;
    }

    /**
     * @return the nomePilhaPellet
     */
    public String getNomePilhaPellet() {
        return nomePilhaPellet;
    }

    /**
     * @param nomePilhaPellet the nomePilhaPellet to set
     */
    public void setNomePilhaPellet(String nomePilhaPellet) {
        this.nomePilhaPellet = nomePilhaPellet;
    }

    /**
     * @return the nomePilhaProduto
     */
    public String getNomePilhaProduto() {
        return nomePilhaProduto;
    }

    /**
     * @param nomePilhaProduto the nomePilhaProduto to set
     */
    public void setNomePilhaProduto(String nomePilhaProduto) {
        this.nomePilhaProduto = nomePilhaProduto;
    }

    /**
     * @return the maquinaDoPatio
     */
    public MaquinaDoPatio getMaquinaDoPatio() {
        return maquinaDoPatio;
    }

    /**
     * @param maquinaDoPatio the maquinaDoPatio to set
     */
    public void setMaquinaDoPatio(MaquinaDoPatio maquinaDoPatio) {
        this.maquinaDoPatio = maquinaDoPatio;
    }

    /**
     * @return the balizaInicialLixo
     */
    public Baliza getBalizaInicialLixo() {
        return balizaInicialLixo;
    }

    /**
     * @param balizaInicialLixo the balizaInicialLixo to set
     */
    public void setBalizaInicialLixo(Baliza balizaInicialLixo) {
        this.balizaInicialLixo = balizaInicialLixo;
    }

    /**
     * @return the sentidoLixo
     */
    public SentidoEmpilhamentoRecuperacaoEnum getSentidoLixo() {
        return sentidoLixo;
    }

    /**
     * @param sentidoLixo the sentidoLixo to set
     */
    public void setSentidoLixo(SentidoEmpilhamentoRecuperacaoEnum sentidoLixo) {
        this.sentidoLixo = sentidoLixo;
    }

    /**
     * @return the nomePilhaLixo
     */
    public String getNomePilhaLixo() {
        return nomePilhaLixo;
    }

    /**
     * @param nomePilhaLixo the nomePilhaLixo to set
     */
    public void setNomePilhaLixo(String nomePilhaLixo) {
        this.nomePilhaLixo = nomePilhaLixo;
    }


}
