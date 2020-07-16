package com.hdntec.gestao.cliente.planoDeEmpilhamentoERecuperacaoCliente.atualizacao.edicao;

import java.util.Date;

import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.InterfaceBaliza;
import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;
import com.hdntec.gestao.domain.produto.entity.Produto;
import com.hdntec.gestao.domain.produto.entity.Qualidade;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;


/**
 *
 * @author bgomes
 */
public class AtualizacaoEdicaoBaliza implements ICommand {

    InterfaceBaliza interfaceBaliza;

    private Date inicioFormacaoBaliza;

    private Date finalFormacaoBaliza;

    private Date horaQualidadeProduto;

    private EstadoMaquinaEnum estadoBaliza;

    private Double quantidadeProduto;

    private Qualidade qualidade;

   // private String descricaoProduto;

    private Produto produto;

    private TipoProduto tipoProduto;

    //TODO Navio, Carga, e Cliente devera ser implementado quando fila de navios estiver pronta
    public AtualizacaoEdicaoBaliza(InterfaceBaliza interfaceBaliza, Date inicio, Date finalFormacao, Date horaQualidade, EstadoMaquinaEnum estado, Double quantidade, Qualidade qualidade, /*String descricao,*/ TipoProduto tipoProduto) {
        this.interfaceBaliza = interfaceBaliza;
        this.estadoBaliza = estado;
        this.finalFormacaoBaliza = finalFormacao;
        this.inicioFormacaoBaliza = inicio;
        this.horaQualidadeProduto = horaQualidade;
        this.quantidadeProduto = quantidade;
        this.qualidade = qualidade;
        //this.descricaoProduto = descricao;
        this.tipoProduto = tipoProduto;
        this.produto = interfaceBaliza.getBalizaVisualizada().getProduto();
    }

    @Override
    public void execute() {
        //dados da baliza
        this.interfaceBaliza.getBalizaVisualizada().setEstado(this.estadoBaliza);
        this.interfaceBaliza.getBalizaVisualizada().setHorarioInicioFormacao(this.inicioFormacaoBaliza);
        this.interfaceBaliza.getBalizaVisualizada().setHorarioFimFormacao(this.finalFormacaoBaliza);
        //dados dos produtos
        this.interfaceBaliza.getBalizaVisualizada().getProduto().setQuantidade(this.quantidadeProduto);
        //this.interfaceBaliza.getBalizaVisualizada().getProduto().setDescricaoProduto(this.descricaoProduto);
        this.interfaceBaliza.getBalizaVisualizada().getProduto().setTipoProduto(this.tipoProduto);
        this.interfaceBaliza.getBalizaVisualizada().getProduto().setQualidade(this.qualidade);
        this.interfaceBaliza.getBalizaVisualizada().setProduto(this.produto);
    }
}
