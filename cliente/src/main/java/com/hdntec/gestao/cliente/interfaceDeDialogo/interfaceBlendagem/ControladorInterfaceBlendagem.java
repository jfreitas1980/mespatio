package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceBlendagem;

import com.hdntec.gestao.cliente.interfaceDeDialogo.IControladorInterfaceInicial;
import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.domain.blendagem.Blendagem;

/**
 * Controla as operações do subsistema de interface gráfica de blendagem.
 * 
 * @author andre
 * 
 */
public class ControladorInterfaceBlendagem implements IControladorInterfaceBlendagem {

    /** a interface gráfica que apresenta a blendagem e o atendimento à carga */
    private InterfaceBlendagem interfaceBlendagem;

    /** acesso ao subsistema de interface principal */
    private IControladorInterfaceInicial interfaceInicial;

    @Override
    public void apresentarBlendagem(Blendagem blendagem) {
        interfaceBlendagem.apresentarBlendagem(blendagem);
    }

    @Override
    public boolean limparVisualizacao() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void ativarMensagem(InterfaceMensagem interfaceMensagem) {
        interfaceInicial.ativarMensagem(interfaceMensagem);
    }

    /**
     * @return the interfaceBlendagem
     */
    public InterfaceBlendagem getInterfaceBlendagem() {
        return interfaceBlendagem;
    }

    /**
     * @param interfaceBlendagem
     *           the interfaceBlendagem to set
     */
    public void setInterfaceBlendagem(InterfaceBlendagem interfaceBlendagem) {
        this.interfaceBlendagem = interfaceBlendagem;
    }

    /**
     * @return the interfacePrincipal
     */
    public IControladorInterfaceInicial getInterfaceInicial() {
        return interfaceInicial;
    }

    /**
     * @param interfacePrincipal
     *           the interfacePrincipal to set
     */
    public void setInterfaceInicial(IControladorInterfaceInicial interfaceInicial) {
        this.interfaceInicial = interfaceInicial;
    }
}
