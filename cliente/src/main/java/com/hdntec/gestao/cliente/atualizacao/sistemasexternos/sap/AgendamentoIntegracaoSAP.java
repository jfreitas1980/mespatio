package com.hdntec.gestao.cliente.atualizacao.sistemasexternos.sap;

import java.util.Date;
import java.util.TimerTask;

import com.hdntec.gestao.cliente.interfaceDeDialogo.IControladorInterfaceInicial;
import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.exceptions.ErroSistemicoException;


public class AgendamentoIntegracaoSAP extends TimerTask {

    private IControladorInterfaceInicial controladorInterfaceInicial;

    public AgendamentoIntegracaoSAP(IControladorInterfaceInicial controladorInterfaceInicial) {
        this.controladorInterfaceInicial = controladorInterfaceInicial;
    }

    @Override
    public void run() {
        try {
            controladorInterfaceInicial.executarIntegracaoSistemaSAP(new Date(super.scheduledExecutionTime()));
        } catch (ErroSistemicoException errEx) {
        	controladorInterfaceInicial.desativarMensagemProcessamento();
            InterfaceMensagem interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setDescricaoMensagem(errEx.getMessage());
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            controladorInterfaceInicial.ativarMensagem(interfaceMensagem);
        }
    }
}
