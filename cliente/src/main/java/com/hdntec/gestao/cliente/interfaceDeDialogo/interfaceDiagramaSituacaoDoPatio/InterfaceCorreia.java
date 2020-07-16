package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.hdntec.gestao.cliente.interfaceDeDialogo.representacaoGrafica.RepresentacaoGrafica;
import com.hdntec.gestao.cliente.interfaceProgramacao.InterfaceInicializacao;
import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.cliente.util.numeros.DSSStockyardFuncoesNumeros;
import com.hdntec.gestao.domain.planta.entity.status.Correia;
import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;
import com.hdntec.gestao.exceptions.ModoDeEdicaoException;
import com.hdntec.gestao.exceptions.OperacaoCanceladaPeloUsuarioException;
import com.hdntec.gestao.util.PropertiesUtil;

/**
 * Interface gráfica que representa a {@link Correia}.
 *
 * @author andre
 *
 */
public class InterfaceCorreia extends RepresentacaoGrafica implements InterfaceInicializacao {

    /** serial gerado */
    private static final long serialVersionUID = -2125034079015560813L;
    /** a correia visualizada nesta interface */
    private Correia correiaVisualizada;
    /** a lista de máquinas que pertencem a esta correia, ou seja, que estão sobre a mesma */
    private List<InterfaceMaquinaDoPatio> listaDeInterfaceMaquinas;
    /** acesso às operações do subsistema de interface gráfica DSP */
    private ControladorDSP controladorDSP;
    /** a interface gráfica do DSP */
    private InterfaceDSP interfaceDSP;
    /** a interface das mensagens exibidas pela interface correia */
    private InterfaceMensagem interfaceMensagem;
    private int comprimentoCorreiaPatio;
    private int eixoXCorreia;
    private int tamanhoBorda;

    private Date horaSituacao;

    public Date getHoraSituacao() {
        return horaSituacao;
    }

    public void setHoraSituacao(Date horaSituacao) {
        this.horaSituacao = horaSituacao;
    }

    public InterfaceCorreia() {
        super();
        this.setOpaque(false);
    }

    @Override
    public void inicializaInterface() {
        defineDesenhoCorreira();
        defineDimensoesFixas();
        calculaPosicaoCorreia();
        criaPopMenuEdicaoCorreia();
        desabilitaMenusPermissaoUsuario();
        
        
        StringBuffer dados = new StringBuffer(); 
        dados.append("<html>").append(correiaVisualizada.getNomeCorreia()).append("<br>");
        dados.append(PropertiesUtil.getMessage("label.taxa.operacao")).append(DSSStockyardFuncoesNumeros.getQtdeFormatada(correiaVisualizada.getTaxaDeOperacao(), 2));
        
        this.setToolTipText(dados.toString());
        
    }

    private void defineDesenhoCorreira() {
        
        if (correiaVisualizada.getEstado().equals(EstadoMaquinaEnum.MANUTENCAO)) {
            setImagemDSP("esteira_manurencao.png");
        } else if (this.correiaVisualizada.getMetaCorreia().correiaInterditado(horaSituacao))
        {
            setImagemDSP("esteira_manurencao.png");        
        } else if (correiaVisualizada.getEstado().equals(EstadoMaquinaEnum.OPERACAO)) {
            setImagemDSP("esteira_operacao.png");
        } else {
            setImagemDSP("esteira_ociosa.png");
        }
    }

    @Override
    public void defineDimensoesFixas() {
        comprimentoCorreiaPatio = Integer.parseInt(PropertiesUtil.buscarPropriedade("dimensao.correia.comprimento").trim());
        eixoXCorreia = Integer.parseInt(PropertiesUtil.buscarPropriedade("dimensao.correia.eixo.x").trim());
        tamanhoBorda = Integer.parseInt(PropertiesUtil.buscarPropriedade("dimensao.correia.tamanho.borda").trim());
    }

    /** Metodo que calcula a diferenca das posicoes das correias em relacao ao tamanho dos patios,
     * pois devemos somar essa diferenca para que as correias aparecam na linha de inicio de cada patio
     *
     * @return
     */
    private int calculaEixoYCorreia() {

        int comprimentoDesenhoDSP = 0, comprimentoPatios = 0, comprimentoTodasCorreias = 0, diferencaDivisaoCorreias = 0;
        int eixoYCorreia = 0, qtdeCorreiasInseridas = 0;

        // comprimento do Desenho do DSP
        comprimentoDesenhoDSP = (int) interfaceDSP.getComponenteInterfaceDSP().getBounds().getHeight();
        // comprimento dos Patios
        comprimentoPatios = comprimentoDesenhoDSP / interfaceDSP.getListaDePatios().size();
        // comprimento das correias em relacao ao comprimento do dsp
        comprimentoTodasCorreias = comprimentoDesenhoDSP / interfaceDSP.getListaCorreias().size();
        // diferenca entre o comprimento do patio com o comprimento da correia
        diferencaDivisaoCorreias = (comprimentoPatios - comprimentoTodasCorreias);
        // quantidade de correias jah inseridas no dsp (a contagem das correias inicia-se em zero)
        qtdeCorreiasInseridas = (interfaceDSP.getComponenteInterfaceDSP().getComponentCount() - 1) - interfaceDSP.getListaDePatios().size();

        // comprimento da correia calculado (subtraindo 1 do tamanho da lista pois a contagem de correias inicia-se em zero)
        if (qtdeCorreiasInseridas == interfaceDSP.getListaCorreias().size() - 1) {
            eixoYCorreia = (comprimentoTodasCorreias + diferencaDivisaoCorreias) * qtdeCorreiasInseridas - comprimentoCorreiaPatio;
        } else {
            eixoYCorreia = (comprimentoTodasCorreias + diferencaDivisaoCorreias) * qtdeCorreiasInseridas + tamanhoBorda;
        }

        return eixoYCorreia;
    }

    private void calculaPosicaoCorreia() {
        int eixoX = 0, eixoY = 0, comprimento = 0, largura = 0;

        comprimento = comprimentoCorreiaPatio;
        largura = (int) interfaceDSP.getComponenteInterfaceDSP().getBounds().getWidth() - tamanhoBorda;
        eixoX = eixoXCorreia;
        eixoY = calculaEixoYCorreia();

        this.setDimensaoImagem(eixoX, eixoY, largura, comprimento);
        this.setBounds(eixoX, eixoY, largura, comprimento);

    }

    public boolean mostrarCorreia(Correia correia) {
        throw new UnsupportedOperationException();
    }

    public ControladorDSP getControladorDSP() {
        return controladorDSP;
    }

    public void setControladorDSP(ControladorDSP controladorDSP) {
        this.controladorDSP = controladorDSP;
    }

    public Correia getCorreiaVisualizada() {
        return correiaVisualizada;
    }

    public void setCorreiaVisualizada(Correia correiaVisualizada) {
        this.correiaVisualizada = correiaVisualizada;
    }

    public InterfaceDSP getInterfaceDSP() {
        return interfaceDSP;
    }

    public void setInterfaceDSP(InterfaceDSP interfaceDSP) {
        this.interfaceDSP = interfaceDSP;
    }

    private InterfaceCorreia obterCorreiaClicada(java.awt.event.ActionEvent evt) {
        JMenuItem mnuItem = (JMenuItem) evt.getSource();
        JPopupMenu popupMenu = (JPopupMenu) mnuItem.getParent();
        return (InterfaceCorreia) popupMenu.getInvoker();
    }

    private void criaPopMenuEdicaoCorreia() {
        JPopupMenu popMnuEditarCorreia = new JPopupMenu();

        JMenuItem mnuEditarCorreia = new JMenuItem();
        mnuEditarCorreia.setText(PropertiesUtil.getMessage("menu.editar.correia"));
        mnuEditarCorreia.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editarCorreiaActionPerformed(evt);
            }
        });

        popMnuEditarCorreia.add(mnuEditarCorreia);

        JMenuItem mnuListaManutencoes = new JMenuItem();
        mnuListaManutencoes.setText(PropertiesUtil.getMessage("mensagem.manutencao.popup"));
        mnuListaManutencoes.setToolTipText(PropertiesUtil.getMessage("mensagem.manutencao.popup"));
        mnuListaManutencoes.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                apresentaListaManutencoes(evt);

            }
        });

        popMnuEditarCorreia.add(mnuListaManutencoes);


        this.setComponentPopupMenu(popMnuEditarCorreia);
    }

    private void editarCorreiaActionPerformed(ActionEvent evt) {
        try {
            InterfaceCorreia correiaClicada = obterCorreiaClicada(evt);
            if (correiaClicada.getCorreiaVisualizada().equals(correiaVisualizada)) {
                controladorDSP.verificarModoDeEdicao();
                controladorDSP.getInterfaceInicial().ativaModoEdicao();
                solicitaDadosEdicaoCorreia(evt);
            }
        } catch (ModoDeEdicaoException mdex) {
        	controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(mdex.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
        } catch (OperacaoCanceladaPeloUsuarioException operCancelEx) {
        	controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
            interfaceMensagem.setDescricaoMensagem(operCancelEx.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
        }
    }

    private void solicitaDadosEdicaoCorreia(java.awt.event.ActionEvent evt) throws OperacaoCanceladaPeloUsuarioException {
        interfaceDSP.getControladorDSP().getInterfaceDadosEdicao().setControladorDSP(interfaceDSP.getControladorDSP());
        interfaceDSP.getControladorDSP().getInterfaceDadosEdicao().solicitaDadosEdicaoCorreia(evt, this);
    }

    /**
     * Metodo que apresenta a interface de lista de manutencoes da maquina
     * visualizada.
     *
     * @param evt
     */
    private void apresentaListaManutencoes(ActionEvent evt) {
        try {
            final InterfaceManutencaoCorreia interfaceManutencao = new InterfaceManutencaoCorreia(this.controladorDSP,this) {

                @Override
                public void fecharJanela() {
                    controladorDSP.getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida().setStatusEdicao(
                            Boolean.FALSE);
                    this.setOperacaoCanceladaPeloUsuario(Boolean.TRUE);
                }
            };

            JDialog dialogTratamentoPSM = new JDialog(controladorDSP.getInterfaceDSP().getInterfaceInicial(), true);
            dialogTratamentoPSM.addWindowListener(new java.awt.event.WindowAdapter() {

                @Override
                public void windowClosing(java.awt.event.WindowEvent evt) {
                    controladorDSP.getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida().setStatusEdicao(
                            Boolean.FALSE);
                    interfaceManutencao.setOperacaoCanceladaPeloUsuario(Boolean.TRUE);
                }
            });

            dialogTratamentoPSM.setLayout(new BorderLayout());
            dialogTratamentoPSM.setTitle(PropertiesUtil.getMessage("mensagem.manutencao.popup"));
            dialogTratamentoPSM.getContentPane().add(interfaceManutencao);
            dialogTratamentoPSM.pack();
            dialogTratamentoPSM.setLocationRelativeTo(null);
            dialogTratamentoPSM.setVisible(true);

            // A operacao de edicao da lista de manutencoes foi cancelada pelo
            // usuário
            if (interfaceManutencao.getOperacaoCanceladaPeloUsuario()) {
                dialogTratamentoPSM.setVisible(false);
                controladorDSP.getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida().setStatusEdicao(
                        Boolean.FALSE);
                controladorDSP.getInterfaceInicial().getInterfaceComandos().cancelarModoOperacao();
                throw new OperacaoCanceladaPeloUsuarioException(
                        PropertiesUtil.buscarPropriedade("exception.operacao.cancelada.pelo.usuario"));
            }

        } catch (OperacaoCanceladaPeloUsuarioException ex) {
        	controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
        }
    }

    /**
     *
     * @return a lista de máquinas sobre esta correia
     */
    public List<InterfaceMaquinaDoPatio> getListaDeMaquinas() {
        return listaDeInterfaceMaquinas;
    }

    /**
     * @return the comprimentoCorreiaPatio
     */
    public int getComprimentoCorreiaPatio() {
        return comprimentoCorreiaPatio;
    }

    /**
     * @param comprimentoCorreiaPatio the comprimentoCorreiaPatio to set
     */
    public void setComprimentoCorreiaPatio(int comprimentoCorreiaPatio) {
        this.comprimentoCorreiaPatio = comprimentoCorreiaPatio;
    }

    /**
     * @return the listaDeInterfaceMaquinas
     */
    public List<InterfaceMaquinaDoPatio> getListaDeInterfaceMaquinas() {
        return listaDeInterfaceMaquinas;
    }

    /**
     * @param listaDeInterfaceMaquinas the listaDeInterfaceMaquinas to set
     */
    public void setListaDeInterfaceMaquinas(List<InterfaceMaquinaDoPatio> listaDeInterfaceMaquinas) {
        this.listaDeInterfaceMaquinas = listaDeInterfaceMaquinas;
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
    
    private void ativaMensagem(String mensagem) {
    	controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
		interfaceMensagem = new InterfaceMensagem();
		interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_PROCESSAMENTO);
		interfaceMensagem.setProcessamentoAtividado(Boolean.TRUE);
		interfaceMensagem.setDescricaoMensagem(mensagem);
		controladorDSP.ativarMensagem(interfaceMensagem);
	}

    /**
     * boqueia todos os menus se a permissao de usuario for de leitura "nao pode executar atualizacoes de producao"
     */
    private void desabilitaMenusPermissaoUsuario()
    {
        if (controladorDSP!= null) {
            if(controladorDSP.getInterfaceInicial().verificaPermissaoAtualizacaoProducao())
            {
                desabilitarMenus();
            }
        }   
    }
    

}
