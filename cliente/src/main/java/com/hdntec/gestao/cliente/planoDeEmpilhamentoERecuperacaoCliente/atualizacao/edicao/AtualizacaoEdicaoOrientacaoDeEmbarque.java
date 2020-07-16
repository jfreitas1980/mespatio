
package com.hdntec.gestao.cliente.planoDeEmpilhamentoERecuperacaoCliente.atualizacao.edicao;

import java.util.List;

import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios.InterfaceNavio;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios.RetornaObjetosDaInterfaceFilaDeNavios;
import com.hdntec.gestao.domain.navios.entity.status.Carga;
import com.hdntec.gestao.domain.produto.entity.ItemDeControle;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;


/**
 *
 * @author bgomes
 */
public class AtualizacaoEdicaoOrientacaoDeEmbarque implements ICommand{

    private InterfaceNavio interfaceNavio;
    private  List<ItemDeControle> listaItensDeControle;
    private Boolean penalizacao;
    private TipoProduto tipoProduto;
    private Carga carga;
    private String identificadorCarga;
    private RetornaObjetosDaInterfaceFilaDeNavios roifn = new RetornaObjetosDaInterfaceFilaDeNavios();
    private Double quantidade;

    public AtualizacaoEdicaoOrientacaoDeEmbarque(InterfaceNavio interfaceNavio, Boolean penalizacao, TipoProduto tipoProduto, List<ItemDeControle> listaItensDeControle, String identificadorCarga, Double quantidade){
        this.interfaceNavio = interfaceNavio;
        this.listaItensDeControle = listaItensDeControle;
        this.penalizacao = penalizacao;
        this.tipoProduto = tipoProduto;
        this.identificadorCarga = identificadorCarga;
        this.quantidade = quantidade;
    }

    @Override
    public void execute() {
        carga = roifn.retornaCargaSelecionada(identificadorCarga, interfaceNavio.getNavioVisualizado().getListaDeCargasDoNavio(interfaceNavio.getHoraSituacao()));

        carga.getOrientacaoDeEmbarque().setQuantidadeNecessaria(quantidade);
        carga.getOrientacaoDeEmbarque().setListaItemDeControle(listaItensDeControle);
        carga.getOrientacaoDeEmbarque().setPenalizacao(penalizacao);
        carga.getOrientacaoDeEmbarque().setTipoProduto(tipoProduto);
    }

}
