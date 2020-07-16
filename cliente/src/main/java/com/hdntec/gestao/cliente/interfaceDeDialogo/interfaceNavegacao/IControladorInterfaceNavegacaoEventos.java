package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceNavegacao;

import java.util.List;

import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;


/**
 * Interface de acesso às operações do subsistema de interface gráfica de navegação de eventos
 * 
 * @author andre
 * 
 */
public interface IControladorInterfaceNavegacaoEventos {


    /**
     * Muda a Situação apresentada na tela para a situação de pátio do presente
     */
    public void irParaOPresente();

    /**
     * atualiza a Situação selecionda na barra de eventos para o índice passado como parâmetro
     * @param indiceSituacaoSelecionada índice da nova Situação selecionada
     */
    public void atualizarSituacaoSelecionada(int indiceSituacaoSelecionada);

    public void atulizarInterfaceNavegacaoEventos(List<SituacaoPatio> situacoesDePatio);
}