
package com.hdntec.gestao.cliente.planoDeEmpilhamentoERecuperacaoCliente.atualizacao.edicao;

import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios.InterfaceNavio;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios.RetornaObjetosDaInterfaceFilaDeNavios;
import com.hdntec.gestao.domain.navios.entity.status.Carga;
import com.hdntec.gestao.domain.produto.entity.Produto;
import com.hdntec.gestao.domain.produto.entity.Qualidade;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;


/**
 * Classe utilizada para guardar as informacoes da InterfaceNavio e da Carga editada, para que possa ser recuperada caso ação de edição seja
 * cancelada
 * @author bgomes
 */
public class AtualizacaoEdicaoCarga implements ICommand{

    InterfaceNavio interfaceNavio;
    private Double quantidadeProduto;
    private Produto produto;
   // private String descricao;
    private Qualidade qualidade;
    private TipoProduto tipoProduto;
    private Carga carga;
    private RetornaObjetosDaInterfaceFilaDeNavios roifn = new RetornaObjetosDaInterfaceFilaDeNavios();
   
    //TODO - a quantidade necessária mudou para a orientação de embarque
   public AtualizacaoEdicaoCarga(InterfaceNavio interfaceNavio, Carga carga,  Produto produto, /*String descricao,*/ Qualidade qualidade, TipoProduto tipoProduto,Double qtdeProduto /*, Double qtdeCarga */) {
        this.interfaceNavio = interfaceNavio;
        //this.quantidadeNecessariaCarga = qtdeCarga;//TODO - a quantidade necessária mudou para a orientação de embarque
        this.quantidadeProduto = qtdeProduto;
        this.produto = produto;
        //this.descricao = descricao;
        this.qualidade = qualidade;
        this.tipoProduto = tipoProduto;
        this.carga = carga;
    }
    
    @Override
    public void execute() {
        this.interfaceNavio.setListaDecarga(interfaceNavio.getListaDecarga());
        //this.interfaceNavio.getNavioVisualizado().setListaDeCargasDoNavio(interfaceNavio.getNavioVisualizado().getListaDeCargasDoNavio(interfaceNavio.getHoraSituacao()));

        Carga carga1 = roifn.retornaCargaDoNavio( this.interfaceNavio.getNavioVisualizado().getListaDeCargasDoNavio(interfaceNavio.getHoraSituacao()), carga);

        //carga.setQuantidadeNecessaria(quantidadeNecessariaCarga);//TODO - a quantidade necessária mudou para a orientação de embarque
        carga.setProduto(produto);
       //carga.getProduto().setDescricaoProduto(descricao);
        carga.getProduto().setQualidade(qualidade);
        carga.getProduto().setTipoProduto(tipoProduto);
        carga.getProduto().setQuantidade(quantidadeProduto);

        carga1 = carga;

    }

}
