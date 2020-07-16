/**
 * Classe que desenha o navio e suas informações que será usada para como Aba na fila de Navios
 * @author Rodrigo Luchetta
 */
package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.hdntec.gestao.cliente.interfaceProgramacao.InterfaceInicializacao;
import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.ModoDeEdicaoException;
import com.hdntec.gestao.exceptions.OperacaoCanceladaPeloUsuarioException;
import com.hdntec.gestao.util.PropertiesUtil;
import com.hdntec.gestao.util.datahora.DSSStockyardTimeUtil;

public class NavioNaFilaPnl extends javax.swing.JPanel implements InterfaceInicializacao {

    /** a interface navio a ser desenhada */
    private InterfaceNavio interfaceNavio;
    private InterfaceMensagem interfaceMensagem;
    
    public NavioNaFilaPnl(InterfaceNavio interfaceNavio) {
        initComponents();
        this.setComponentPopupMenu(popMenuFilaNavio);
        this.interfaceNavio = interfaceNavio;
        this.interfaceNavio.setImagemDSP("navio.png");
        this.interfaceNavio.setDesenharNavioFila(Boolean.TRUE);
        this.interfaceNavio.setComponenteInterfaceFilaNavios(pnlNavio);
        this.inicializaInterface();
        this.setBackground(Color.WHITE);
    }

    private void carregaInformacoesNavio() {
        lblNomeNavio.setText(interfaceNavio.getNavioVisualizado().getNomeNavio());
        lblDataEmbarque.setText(DSSStockyardTimeUtil.formatarData(interfaceNavio.getNavioVisualizado().getEta(), "dd/MM/yyyy"));
        lblNomeCliente.setText(interfaceNavio.getNavioVisualizado().getCliente().getNomeCliente());
        if (interfaceNavio.getNavioVisualizado().getListaDeCargasDoNavio(interfaceNavio.getHoraSituacao()) != null) {
            lblNroCargas.setText(String.valueOf(interfaceNavio.getNavioVisualizado().getListaDeCargasDoNavio(interfaceNavio.getHoraSituacao()).size()));
        } else {
            lblNroCargas.setText("0");
        }
        lbStatus.setText(interfaceNavio.getNavioVisualizado().getStatusEmbarque().toString());
    }

    private void defineAcosParaFila() {
        mnuAtualizarCRMNavio.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    //verifica se a filaDeNavios pode ser edita... ou se houve alguma alteracao na situacaoDePatio
                    interfaceNavio.getControladorInterfaceFilaDeNavios().verificarModoDeEdicao();
                    //muda o estado interno do sistema para o MODO_EDICAO
                    interfaceNavio.getControladorInterfaceFilaDeNavios().getInterfaceFilaDeNavios().getInterfaceInicial().getControladorInterfaceInicial().ativaModoEdicao();
                    //chama metodo que apresenta a tela de edicao dos dados do Navio
                    interfaceNavio.getControladorInterfaceFilaDeNavios().atualizarNavioFilaDeNavios(interfaceNavio);
                } catch (OperacaoCanceladaPeloUsuarioException ex) {
                    interfaceNavio.getControladorInterfaceFilaDeNavios().getInterfaceInicial().desativarMensagemProcessamento();
                    interfaceMensagem = new InterfaceMensagem();
                    interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
                    interfaceMensagem.setDescricaoMensagem(ex.getMessage());
                    interfaceNavio.getControladorInterfaceFilaDeNavios().ativarMensagem(interfaceMensagem);
                } catch (ModoDeEdicaoException mdex) {
                    interfaceNavio.getControladorInterfaceFilaDeNavios().getInterfaceInicial().desativarMensagemProcessamento();
                    interfaceMensagem = new InterfaceMensagem();
                    interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
                    interfaceMensagem.setDescricaoMensagem(mdex.getMessage());
                    interfaceNavio.getControladorInterfaceFilaDeNavios().ativarMensagem(interfaceMensagem);
                } catch (ErroSistemicoException e) {
                    // TODO Auto-generated catch block
                    interfaceNavio.getControladorInterfaceFilaDeNavios().getInterfaceInicial().desativarMensagemProcessamento();
                    interfaceMensagem = new InterfaceMensagem();
                    interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
                    interfaceMensagem.setDescricaoMensagem(e.getMessage());
                    interfaceNavio.getControladorInterfaceFilaDeNavios().ativarMensagem(interfaceMensagem);
                    e.printStackTrace();
                }
            }
        });

        
        mnuInserirNavio.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    //verifica se a filaDeNavios pode ser edita... ou se houve alguma alteracao na situacaoDePatio
                    interfaceNavio.getControladorInterfaceFilaDeNavios().verificarModoDeEdicao();
                    //muda o estado interno do sistema para o MODO_EDICAO
                    interfaceNavio.getControladorInterfaceFilaDeNavios().getInterfaceFilaDeNavios().getInterfaceInicial().getControladorInterfaceInicial().ativaModoEdicao();
                    //chama metodo que apresenta a tela de edicao dos dados do Navio
                    interfaceNavio.getControladorInterfaceFilaDeNavios().inserirNavioFilaDeNavios(interfaceNavio);
                } catch (OperacaoCanceladaPeloUsuarioException ex) {
                	interfaceNavio.getControladorInterfaceFilaDeNavios().getInterfaceInicial().desativarMensagemProcessamento();
                    interfaceMensagem = new InterfaceMensagem();
                    interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
                    interfaceMensagem.setDescricaoMensagem(ex.getMessage());
                    interfaceNavio.getControladorInterfaceFilaDeNavios().ativarMensagem(interfaceMensagem);
                } catch (ModoDeEdicaoException mdex) {
                	interfaceNavio.getControladorInterfaceFilaDeNavios().getInterfaceInicial().desativarMensagemProcessamento();
                    interfaceMensagem = new InterfaceMensagem();
                    interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
                    interfaceMensagem.setDescricaoMensagem(mdex.getMessage());
                    interfaceNavio.getControladorInterfaceFilaDeNavios().ativarMensagem(interfaceMensagem);
                }
            }
        });

        mnuEditarMavio.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    //verifica se a filaDeNavios pode ser edita... ou se houve alguma alteracao na situacaoDePatio
                    interfaceNavio.getControladorInterfaceFilaDeNavios().verificarModoDeEdicao();
                    //muda o estado interno do sistema para o MODO_EDICAO
                    interfaceNavio.getControladorInterfaceFilaDeNavios().getInterfaceFilaDeNavios().getInterfaceInicial().getControladorInterfaceInicial().ativaModoEdicao();
                    //chama metodo que apresenta a tela de edicao dos dados do Navio
                    interfaceNavio.getControladorInterfaceFilaDeNavios().editarFilaDeNavios(interfaceNavio, InterfaceEditaNavioNaFilaDeNavios.MODO_EDITAR);
                } catch (OperacaoCanceladaPeloUsuarioException ex) {
                    interfaceMensagem = new InterfaceMensagem();
                    interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
                    interfaceMensagem.setDescricaoMensagem(ex.getMessage());
                    interfaceNavio.getControladorInterfaceFilaDeNavios().ativarMensagem(interfaceMensagem);
                } catch (ModoDeEdicaoException mdex) {
                    interfaceMensagem = new InterfaceMensagem();
                    interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
                    interfaceMensagem.setDescricaoMensagem(mdex.getMessage());
                    interfaceNavio.getControladorInterfaceFilaDeNavios().ativarMensagem(interfaceMensagem);
                }
            }
        });

        mnuExcluirNavio.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    //verifica se a filaDeNavios pode ser edita... ou se houve alguma alteracao na situacaoDePatio
                    interfaceNavio.getControladorInterfaceFilaDeNavios().verificarModoDeEdicao();
                    //muda o estado interno do sistema para o MODO_EDICAO
                    interfaceNavio.getControladorInterfaceFilaDeNavios().getInterfaceFilaDeNavios().getInterfaceInicial().getControladorInterfaceInicial().ativaModoEdicao();
                    //chama metodo que apresenta a tela de edicao dos dados do Navio
                    List<String> listaParam = new ArrayList<String>();
                    listaParam.add(interfaceNavio.getNavioVisualizado().getNomeNavio());
                    JLabel pergunta = new JLabel(PropertiesUtil.getMessage("mensagem.confirma.delecao.navio", listaParam));
                    pergunta.setFont(new Font("Tahoma", Font.PLAIN, 11));
                    int confirm = JOptionPane.showOptionDialog(interfaceNavio.getControladorInterfaceFilaDeNavios().getInterfaceFilaDeNavios().getInterfaceInicial(), pergunta, " Atenção ", JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                    if (confirm == JOptionPane.YES_OPTION) {
                        interfaceNavio.getControladorInterfaceFilaDeNavios().excluirNavioFilaDeNavios(interfaceNavio);
                        //Darley Retirando chamada remota
//                        IControladorModelo controladorModelo = InterfaceInicial.lookUpModelo();
                        //IControladorModelo controladorModelo = new ControladorModelo();
                        //controladorModelo.atualizaCliente(interfaceNavio.getNavioVisualizado().getCliente());
                        interfaceNavio.getControladorInterfaceFilaDeNavios().getInterfaceFilaDeNavios().getInterfaceInicial().getInterfaceComandos().finalizarEdicoes();                        
                    }
                    
                } catch (OperacaoCanceladaPeloUsuarioException ex) {
                    interfaceMensagem = new InterfaceMensagem();
                    interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
                    interfaceMensagem.setDescricaoMensagem(ex.getMessage());
                    interfaceNavio.getControladorInterfaceFilaDeNavios().ativarMensagem(interfaceMensagem);
                } catch (ModoDeEdicaoException mdex) {
                    interfaceMensagem = new InterfaceMensagem();
                    interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
                    interfaceMensagem.setDescricaoMensagem(mdex.getMessage());
                    interfaceNavio.getControladorInterfaceFilaDeNavios().ativarMensagem(interfaceMensagem);
                }    
            }
        });

        //menu para atracar um navio
        JMenuItem mnuAtracar = new JMenuItem();
        mnuAtracar.setText(PropertiesUtil.getMessage("titulo.atracar.navio"));
        mnuAtracar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                InterfaceAtracarNavio interfaceAtracarNavio = new InterfaceAtracarNavio(null, true, interfaceNavio);
                interfaceAtracarNavio.setVisible(true);

                if(interfaceAtracarNavio.getOperacaoCanceladaPeloUsuario()){
                        throw new OperacaoCanceladaPeloUsuarioException();
                }
                //verificar necessidade de atualizar DSP
                    interfaceNavio.getControladorInterfaceFilaDeNavios().getInterfaceInicial().getInterfaceInicial().atualizarDSP();
                    interfaceNavio.getControladorInterfaceFilaDeNavios().getInterfaceInicial().exibirUltimaSituacaoPatio();
                    //desativa mensagem de processamento para atracar navio
                    interfaceNavio.getControladorInterfaceFilaDeNavios().getInterfaceInicial().desativarMensagemProcessamento();

                } catch (OperacaoCanceladaPeloUsuarioException ex) {
                     interfaceMensagem = new InterfaceMensagem();
                    interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
                    interfaceMensagem.setDescricaoMensagem(ex.getMessage());
                    interfaceNavio.getControladorInterfaceFilaDeNavios().ativarMensagem(interfaceMensagem);
                }
            }
        });
        //adicionando menu de atracar ao popMenu
        popMenuFilaNavio.add(mnuAtracar);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popMenuFilaNavio = new javax.swing.JPopupMenu();
        mnuInserirNavio = new javax.swing.JMenuItem();
        mnuAtualizarCRMNavio = new javax.swing.JMenuItem();
        mnuEditarMavio = new javax.swing.JMenuItem();
        mnuExcluirNavio = new javax.swing.JMenuItem();
        pnlInformacoesNavio = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblNomeNavio = new javax.swing.JLabel();
        lblDataEmbarque = new javax.swing.JLabel();
        lblNomeCliente = new javax.swing.JLabel();
        lblNroCargas = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lbStatus = new javax.swing.JLabel();
        pnlNavio = new javax.swing.JPanel();

        
        mnuAtualizarCRMNavio.setText("Atualizar dados CRM");
        mnuAtualizarCRMNavio.setToolTipText("");
        mnuAtualizarCRMNavio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuInserirNavioActionPerformed(evt);
            }
        });
        //mnuAtualizarCRMNavio.setEnabled(false);
        popMenuFilaNavio.add(mnuAtualizarCRMNavio);
        
        mnuInserirNavio.setText("Inserir navio na fila de navios");
        mnuInserirNavio.setToolTipText("");
        mnuInserirNavio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuInserirNavioActionPerformed(evt);
            }
        });
        mnuInserirNavio.setEnabled(true);
        popMenuFilaNavio.add(mnuInserirNavio);

        mnuEditarMavio.setText("Editar navio");
        mnuEditarMavio.setToolTipText("");
        mnuEditarMavio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuEditarMavioActionPerformed(evt);
            }
        });        
        popMenuFilaNavio.add(mnuEditarMavio);

        mnuExcluirNavio.setText("Excluir navio");
        mnuExcluirNavio.setToolTipText("");
        mnuExcluirNavio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuExcluirNavioActionPerformed(evt);
            }
        });
        popMenuFilaNavio.add(mnuExcluirNavio);

        setLayout(null);

        jLabel1.setFont(new java.awt.Font("Verdana", 0, 11));
        jLabel1.setForeground(new java.awt.Color(99, 99, 99));
        jLabel1.setText("Nome:");

        jLabel2.setFont(new java.awt.Font("Verdana", 0, 11));
        jLabel2.setForeground(new java.awt.Color(99, 99, 99));
        jLabel2.setText("ETA:");

        jLabel3.setFont(new java.awt.Font("Verdana", 0, 11));
        jLabel3.setForeground(new java.awt.Color(99, 99, 99));
        jLabel3.setText("Cliente:");

        jLabel4.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(99, 99, 99));
        jLabel4.setText("Número de cargas:");

        lblNomeNavio.setFont(new java.awt.Font("Verdana", 0, 11));
        lblNomeNavio.setForeground(new java.awt.Color(99, 99, 99));

        lblDataEmbarque.setFont(new java.awt.Font("Verdana", 0, 11));
        lblDataEmbarque.setForeground(new java.awt.Color(99, 99, 99));

        lblNomeCliente.setFont(new java.awt.Font("Verdana", 0, 11));
        lblNomeCliente.setForeground(new java.awt.Color(99, 99, 99));

        lblNroCargas.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        lblNroCargas.setForeground(new java.awt.Color(99, 99, 99));

        jLabel5.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(99, 99, 99));
        jLabel5.setText(PropertiesUtil.getMessage("label.status"));

        lbStatus.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        lbStatus.setForeground(new java.awt.Color(99, 99, 99));

        javax.swing.GroupLayout pnlInformacoesNavioLayout = new javax.swing.GroupLayout(pnlInformacoesNavio);
        pnlInformacoesNavio.setLayout(pnlInformacoesNavioLayout);
        pnlInformacoesNavioLayout.setHorizontalGroup(
            pnlInformacoesNavioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInformacoesNavioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInformacoesNavioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInformacoesNavioLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblDataEmbarque, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlInformacoesNavioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(pnlInformacoesNavioLayout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lblNomeCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(pnlInformacoesNavioLayout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(lblNomeNavio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(pnlInformacoesNavioLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblNroCargas, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(119, 119, 119))
                    .addGroup(pnlInformacoesNavioLayout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnlInformacoesNavioLayout.setVerticalGroup(
            pnlInformacoesNavioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInformacoesNavioLayout.createSequentialGroup()
                .addGroup(pnlInformacoesNavioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(lblNomeNavio, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInformacoesNavioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNomeCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInformacoesNavioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(lblDataEmbarque, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInformacoesNavioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNroCargas, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInformacoesNavioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(lbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(pnlInformacoesNavio);
        pnlInformacoesNavio.setBounds(0, 10, 200, 100);

        pnlNavio.setOpaque(false);
        pnlNavio.setPreferredSize(new java.awt.Dimension(425, 90));
        pnlNavio.addContainerListener(new java.awt.event.ContainerAdapter() {
            @Override
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                pnlNavioComponentAdded(evt);
            }
        });
        pnlNavio.setLayout(null);
        add(pnlNavio);
        pnlNavio.setBounds(220, 10, 310, 85);
    }// </editor-fold>//GEN-END:initComponents

    private void pnlNavioComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_pnlNavioComponentAdded
        ((InterfaceInicializacao) evt.getChild()).inicializaInterface();
    }//GEN-LAST:event_pnlNavioComponentAdded

    private void mnuEditarMavioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuEditarMavioActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_mnuEditarMavioActionPerformed

    private void mnuInserirNavioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuInserirNavioActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_mnuInserirNavioActionPerformed

    private void mnuExcluirNavioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuExcluirNavioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mnuExcluirNavioActionPerformed

    public InterfaceNavio getInterfaceNavio() {
        return interfaceNavio;
    }

    public void setInterfaceNavio(InterfaceNavio interfaceNavio) {
        this.interfaceNavio = interfaceNavio;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel lbStatus;
    private javax.swing.JLabel lblDataEmbarque;
    private javax.swing.JLabel lblNomeCliente;
    private javax.swing.JLabel lblNomeNavio;
    private javax.swing.JLabel lblNroCargas;
    private javax.swing.JMenuItem mnuEditarMavio;
    private javax.swing.JMenuItem mnuExcluirNavio;
    private javax.swing.JMenuItem mnuInserirNavio;
    private javax.swing.JMenuItem mnuAtualizarCRMNavio;
    private javax.swing.JPanel pnlInformacoesNavio;
    private javax.swing.JPanel pnlNavio;
    private javax.swing.JPopupMenu popMenuFilaNavio;
    // End of variables declaration//GEN-END:variables

    @Override
    public void inicializaInterface() {
        carregaInformacoesNavio();
        pnlNavio.add(interfaceNavio);
        defineAcosParaFila();
        desabilitaMenusPermissaoUsuario();
        pnlInformacoesNavio.setBackground(Color.WHITE);
    }

    @Override
    public void defineDimensoesFixas() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

     /**
     * Metodo que desabilita os menus existentes neste objeto em caso de processamento
     */
    @Override
    public void desabilitarMenus() {
        if (this.getComponentPopupMenu() != null) {
            for (int i = 0; i < this.getComponentPopupMenu().getComponentCount(); i++) {
                this.getComponentPopupMenu().getComponent(i).setEnabled(false);
            }
        }
    }

    /**
     * Metodo que habilita os menus existentes neste objeto em caso de finalizacao processamento
     */
    @Override
    public void habilitarMenus() {
        if (this.getComponentPopupMenu() != null) {
            for (int i = 0; i < this.getComponentPopupMenu().getComponentCount(); i++) {
                this.getComponentPopupMenu().getComponent(i).setEnabled(true);
            }
        }
    }

    private void desabilitaMenusPermissaoUsuario()
    {
     /*   if(interfaceNavio.getControladorInterfaceFilaDeNavios().getInterfaceInicial().verificaPermissaoAtualizacaoProducao()){
            if (this.getComponentPopupMenu() != null) {
                for (int i = 0; i < this.getComponentPopupMenu().getComponentCount(); i++) {
                    this.getComponentPopupMenu().getComponent(i).setEnabled(false);
                }
            }
        }*/
    }

}
