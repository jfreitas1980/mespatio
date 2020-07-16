/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hdntec.gestao.cliente.planoDeEmpilhamentoERecuperacaoCliente.atualizacao.edicao;

import java.util.Date;
import java.util.Map;

import com.hdntec.gestao.domain.planta.entity.status.Campanha;
import com.hdntec.gestao.domain.produto.entity.ItemDeControle;
import com.hdntec.gestao.domain.produto.entity.TipoItemDeControle;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;


/**
 * Esta classe implementa a interface ICommand que representa uma acao de edicao de correia no modo de edicao.
 * @author Ricardo Trabalho
 */
public class AtualizacaoEdicaoCampanha implements ICommand {

    private String nomeCampanha;

    private Campanha campanha;

    private Date dataInicial;
    private Date dataFinal;

    private Long duracao;

    private TipoProduto tipoProduto;

    private Long codigoFaseProcessoPelletFeed;
    
    private Long codigoFaseProcessoPelota;

    private Map<TipoItemDeControle, Double> listaValor;

    public AtualizacaoEdicaoCampanha(Campanha campanha, String nomeCampanha, Date dataInicial, Date dataFinal,Long duracao,
             TipoProduto tipoProduto, Long codigoFaseProcessoPelletFeed, Long codigoFaseProcessoPelota, Map<TipoItemDeControle, Double> listaValor) {
        this.campanha = campanha;
        this.nomeCampanha = nomeCampanha;
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
        this.duracao = duracao;
        this.tipoProduto = tipoProduto;
        this.codigoFaseProcessoPelletFeed = codigoFaseProcessoPelletFeed;
        this.codigoFaseProcessoPelota = codigoFaseProcessoPelota;
        this.listaValor = listaValor;
    }

    @Override
    public void execute() {
        this.campanha.setDtInicio(this.dataInicial);
        this.campanha.setDtFim(this.dataFinal);
        this.campanha.setNomeCampanha(this.nomeCampanha);
        this.campanha.setTipoProduto(this.tipoProduto);
        this.campanha.setCodigoFaseProcessoPelletFeed(this.codigoFaseProcessoPelletFeed);
        this.campanha.setCodigoFaseProcessoPelota(this.codigoFaseProcessoPelota);
        for (ItemDeControle itemDeControle : this.campanha.getQualidadeEstimada().getListaDeItensDeControle()) {
            itemDeControle.setValor(this.listaValor.get(itemDeControle.getTipoItemControle()));
        }
    }
}
