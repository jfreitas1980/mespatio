/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hdntec.gestao.cliente.planoDeEmpilhamentoERecuperacaoCliente.atualizacao.edicao;

import java.util.Date;

import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios.InterfaceNavio;
import com.hdntec.gestao.domain.navios.entity.status.Cliente;
import com.hdntec.gestao.domain.planta.entity.status.Berco;


/**
 * Classe utilizada para guardar as informações do Navio da InterfaceNavio, para que possam ser recuperadas na ação de cancelar edição
 * @author Bruno Gomes
 */
public class AtualizacaoEdicaoNavioNaFilaDeNavios implements ICommand{

    private String nomeNavio;
    private Double dwt;
    private Date eta;
    private Date dataChegada;
    private Date dataSaida;
    private Date dataEmbarque;
    private String status;
    private Cliente cliente;
    private Berco berco;
    private InterfaceNavio interfaceNavio;

    public AtualizacaoEdicaoNavioNaFilaDeNavios(InterfaceNavio interfaceNavio, Double dwt, String nomeNavio, String status, Date eta,
            Date dataChegada,Date dataSaida, Date dataEmbarque, Cliente cliente, Berco berco){

        this.interfaceNavio = interfaceNavio;
        this.berco = berco;
        this.cliente = cliente;
        this.dwt = dwt;
        this.dataChegada = dataChegada;
        this.dataEmbarque = dataEmbarque;
        this.dataSaida = dataSaida;
        this.eta = eta;
        this.status = status;
        this.nomeNavio = nomeNavio;
    }

    @Override
    public void execute() {
        this.interfaceNavio.getNavioVisualizado().setNomeNavio(nomeNavio);
        this.interfaceNavio.getNavioVisualizado().setDwt(dwt);
        this.interfaceNavio.getNavioVisualizado().setDataEmbarque(dataEmbarque);
        this.interfaceNavio.getNavioVisualizado().setDiaDeChegada(dataChegada);
        this.interfaceNavio.getNavioVisualizado().setDiaDeSaida(dataSaida);
        this.interfaceNavio.getNavioVisualizado().setEta(eta);
        this.interfaceNavio.getNavioVisualizado().setBercoDeAtracacao(berco);
        this.interfaceNavio.getNavioVisualizado().setCliente(cliente);
        //this.interfaceNavio.getNavioVisualizado().setStatusEmbarque(status);
    }

}
