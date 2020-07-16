package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceBlendagem;

import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.domain.blendagem.Blendagem;

/**
 * Interface de acesso ao subsistema de interface de blendagem.
 * 
 * @author andre
 * 
 */
public interface IControladorInterfaceBlendagem {

    /**
     * Apresenta na tela a {@link Blendagem}. A blendagem apresentada pode possuir ou não uma carga selecionada para atendimento.
     * 
     * @param blendagem
     *           a blendagem a ser apresentada na interface
     */
    public void apresentarBlendagem(Blendagem blendagem);

    /**
     * Limpa a visualização de blendagens. Utilizado quando muda-se de visualização de situação, e já não há mais seleções na tela.
     * 
     * @return <code>true</code> caso ocorra com sucesso
     */
    public boolean limparVisualizacao();

    /**
     * Ativa a exibicao da mensagem na tela inicial
     * @param InterfaceMensagem
     */
    public void ativarMensagem(InterfaceMensagem interfaceMensagem);
}
