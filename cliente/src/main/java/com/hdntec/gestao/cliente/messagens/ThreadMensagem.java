/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hdntec.gestao.cliente.messagens;

import com.hdntec.gestao.cliente.interfaceDeDialogo.InterfaceInicial;

/**
 *
 * @author cflex
 */
public class ThreadMensagem extends Thread {

    /** a interface mensagem que executara o processo */
    InterfaceInicial interfaceInicial;

    /** a interface mensagem para exibicao */
    InterfaceMensagem interfaceMensagem;

    public ThreadMensagem (InterfaceInicial interfaceInicial, InterfaceMensagem interfaceMensagem) {
        this.interfaceInicial = interfaceInicial;
        this.interfaceMensagem = interfaceMensagem;
        this.setName("thread interface mensagem");
    }

    @Override
    public void run() {
        try {
            interfaceInicial.getPnlMensagens().setVisible(true);
            interfaceInicial.getLblMensagem().setOpaque(true);
            interfaceInicial.getLblMensagem().setIcon(interfaceMensagem.getImagemTipoMensagem());
            interfaceInicial.getLblMensagem().setText(interfaceMensagem.getDescricaoMensagem());
            interfaceInicial.getLblMensagem().setBackground(interfaceMensagem.getCorFundoMensagem());
            interfaceInicial.getLblMensagem().setForeground(interfaceMensagem.getCorFonteMensagem());

            if (interfaceMensagem != null) {

                // caso a mensagem não for uma mensagem de processamento
                if (!interfaceMensagem.getTipoMensagem().equals(InterfaceMensagem.MENSAGEM_TIPO_PROCESSAMENTO)) {

                    // ativa a exibicao de uma mensagem na tela
                    interfaceInicial.getPnlMensagens().setVisible(true);
                    interfaceInicial.getLblMensagem().setOpaque(true);
                    interfaceInicial.getLblMensagem().setIcon(interfaceMensagem.getImagemTipoMensagem());
                    interfaceInicial.getLblMensagem().setText(interfaceMensagem.getDescricaoMensagem());

                    // para a execucao para que a mensagem possa ser vista pelo usuario
                    Thread.sleep(interfaceMensagem.getTempoExibicaoMensagem());

                    // limpa a exibicao da mensagem após o termino do processamento
                    interfaceInicial.getPnlMensagens().setVisible(false);
                    interfaceInicial.getLblMensagem().setText("");
                    interfaceInicial.getLblMensagem().setIcon(null);
                } else {
                    // enquanto a interface da mensagem estiver com atributo de processamento verdadeiro
                    int i = 0;
                    while (interfaceMensagem.getProcessamentoAtividado() && this.isAlive()) {
                        if ((i % 2) == 0) {
                            // exibe a mensagem de processamento
                            interfaceInicial.getPnlMensagens().setVisible(true);
                            interfaceInicial.getLblMensagem().setIcon(interfaceMensagem.getImagemTipoMensagem());
                            interfaceInicial.getLblMensagem().setText(interfaceMensagem.getDescricaoMensagem());
                            // aguarda meio segundo antes de apagar a mensagem
                            Thread.sleep(400);
                        } else {
                            interfaceInicial.getPnlMensagens().setVisible(false);
                            interfaceInicial.getLblMensagem().setText("");
                            interfaceInicial.getLblMensagem().setIcon(null);
                            // aguarda meio segundo antes de exibir novamente a mensagem
                            Thread.sleep(400);
                        }
                        i++;
                    }
                }
            }
        } catch (InterruptedException ie) {
            //ie.printStackTrace();
        }
    }

    public void setInterfaceMensagem(InterfaceMensagem interfaceMensagem) {
        this.interfaceMensagem = interfaceMensagem;
    }

}
