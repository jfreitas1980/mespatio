package com.hdntec.gestao.cliente.atualizacao.sistemasexternos.pims;

import java.util.TimerTask;

import com.hdntec.gestao.cliente.interfaceDeDialogo.IControladorInterfaceInicial;


public class AgendamentoIntegracaoPIMS extends TimerTask {

    private IControladorInterfaceInicial controladorInterfaceInicial;

    public AgendamentoIntegracaoPIMS(IControladorInterfaceInicial controladorInterfaceInicial) {
        this.controladorInterfaceInicial = controladorInterfaceInicial;
    }

    @Override
    public void run() {
      /*  try {
            controladorInterfaceInicial.executarIntegracaoSistemaPIMS(new Date(super.scheduledExecutionTime()));
        } catch (ErroSistemicoException errEx) {
            InterfaceMensagem interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setDescricaoMensagem(errEx.getMessage());
            interfaceMensagem.setTipoMensagem(interfaceMensagem.MENSAGEM_TIPO_ERRO);
            controladorInterfaceInicial.ativarMensagem(interfaceMensagem);
        }*/
    }
}
