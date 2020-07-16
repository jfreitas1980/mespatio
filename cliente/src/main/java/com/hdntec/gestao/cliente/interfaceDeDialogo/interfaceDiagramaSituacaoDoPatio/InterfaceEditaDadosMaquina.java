package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio;

import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.cliente.util.telas.DSSStockyardTelaUtil;
import com.hdntec.gestao.domain.plano.enums.TipoMaquinaEnum;
import com.hdntec.gestao.domain.planta.entity.MetaBaliza;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.MaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Patio;
import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;
import com.hdntec.gestao.exceptions.ValidacaoCampoException;
import com.hdntec.gestao.util.PropertiesUtil;

public abstract class InterfaceEditaDadosMaquina extends javax.swing.JPanel {

   private ControladorDSP controladorDSP;

    private Boolean operacaoCanceladaPeloUsuario = Boolean.FALSE;

    /** a interface das mensagens exibidas pela interface baliza */
    private InterfaceMensagem interfaceMensagem;

    
	private Integer posicaoNroBaliza;

	private MetaBaliza posicaoEditada;

    private Double taxaOperacao;

    private EstadoMaquinaEnum estado;

    /*estado anterior a edicao */
    private EstadoMaquinaEnum estadoAnteriorEdicao;

    /*A maquina que esta sendo editada */
    private MaquinaDoPatio maquinaEditada;

    /*guarda o indice do comboBox da atividade atual da maquina */
    private Integer indiceAtividade = -1;

    /* indice da atividade editada*/
    private Integer indiceAtividadeEditada = -1;

    //objetos guardados antes da edição, para poder ser recuperado
    private Baliza posicaoOriginal;

    private Double taxaOperacaoOriginal;

    private EstadoMaquinaEnum estadoOriginal;

    /** Flag que determina se a maquina podera ser invertida */
    private Boolean girarLanca;
    /** velocidade de deslocamento da maquina em balizas por hora */
    private Double velocidadeDeslocamento;
    //guarda a velocidade de delocamento original da maquina
    private Double velocidadeDeslocamentoOriginal;

    private InterfacePatio ipatio;
    private InterfacePatio ipatioDaMaquina;

    /** Creates new form InterfaceEditaDadosMaquina */
    /**
     *
     * @param maquinaEditada: obter a maquina a ser editada atraves da interface que a implementa (ex: InterfaceRecuperadora.getRecuperadoraVisualizada() )
     * @param taxaOperacao:     Taxa de operacao da maquina editada
     * @param estado :          o estado atual da maquina editada
     * @param posicao:          posicao atual da maquina
     */
    public InterfaceEditaDadosMaquina(InterfaceMaquinaDoPatio interfaceMaquina, Double taxaOperacao, EstadoMaquinaEnum estado, Baliza posicao, Double velDeslocamento) {
        initComponents();
        this.estado = estado;
        this.estadoAnteriorEdicao = estado;
        this.taxaOperacao = taxaOperacao;
        this.posicaoOriginal = posicao;
        this.maquinaEditada = interfaceMaquina.getMaquinaDoPatioVisualizada();
        this.ipatioDaMaquina = interfaceMaquina.getPatio();
        this.velocidadeDeslocamento = velDeslocamento;
        //apresenta dados da maquina nos campos de edicao
        this.jcEstado.setSelectedItem(interfaceMaquina.getMaquinaDoPatioVisualizada().getEstado());
        this.jTtaxaOperacao.setText(this.taxaOperacao.toString());

        this.controladorDSP = interfaceMaquina.getControladorDSP();

        // Verifica se a maquina eh uma pah-carregadeira. Se for, nao ha necessidade de ter o combo de lanca
        if (!interfaceMaquina.getMaquinaDoPatioVisualizada().getTipoDaMaquina().equals(TipoMaquinaEnum.PA_CARREGADEIRA)) {
        	this.cmbGirarLanca.setVisible(false);
        	this.jLEstado1.setVisible(false);
        	/*if (interfaceMaquina.getMaquinaDoPatioVisualizada().getGiraLanca()) {
                this.cmbGirarLanca.setSelectedIndex(0);
            } else {
                this.cmbGirarLanca.setSelectedIndex(1);
            }*/
            //para maquinas diferentes da pa-carregadeira não sera permitido edicao de patio
            
            this.lbPatio.setVisible(true);
            this.comboPatio.setVisible(true);
            this.comboPatio.setEditable(false);
            this.comboPatio.setEnabled(false);
            if(maquinaEditada.getEstado().equals(EstadoMaquinaEnum.MANUTENCAO)){
            	this.proximaBalizaJCB.setEditable(false);
            }
            carregaTodasBalizas(this.posicaoOriginal.getPatio());
        } else {
            this.cmbGirarLanca.setVisible(false);
            this.jLEstado1.setVisible(false);
            atualizaComboPatio();
        }
        
        this.jtfVelocidadeMaquina.setText(this.velocidadeDeslocamento.toString());
        
        //guarda os valores antes da edicao
        //this.posicaoOriginal = posicao;
        this.taxaOperacaoOriginal = taxaOperacao;
        this.estadoOriginal = estado;
        this.velocidadeDeslocamentoOriginal = velDeslocamento;

        comboPatio.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
            	comboPatioItemStateChanged(evt);
            }
        });

        this.proximaBalizaJCB.setSelectedItem(this.posicaoOriginal.getMetaBaliza());    
    }
   
    /**
     * Metodo criado abstrato, pois sua implementacao sera na classe que o instanciou
     */
    public abstract void fecharJanela();

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jlPosicao = new javax.swing.JLabel();
        //jTposicaobaliza = new javax.swing.JTextField();
        jbCancelar = new javax.swing.JButton();
        jbConfirmar = new javax.swing.JButton();
        jLTaxaOperacao = new javax.swing.JLabel();
        jTtaxaOperacao = new javax.swing.JTextField();
        jLEstado = new javax.swing.JLabel();
        jcEstado = new javax.swing.JComboBox();
        jlPosicao1 = new javax.swing.JLabel();
        jlPosicao2 = new javax.swing.JLabel();
        jLEstado1 = new javax.swing.JLabel();
        cmbGirarLanca = new javax.swing.JComboBox();
        jlVelocidade = new javax.swing.JLabel();
        jtfVelocidadeMaquina = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        lbPatio = new javax.swing.JLabel();
        comboPatio = new javax.swing.JComboBox();
        proximaBalizaJCB = new javax.swing.JComboBox();
        setBorder(javax.swing.BorderFactory.createTitledBorder(PropertiesUtil.getMessage("mensagem.titulo.informacao.edicao.maquina")));
        setMinimumSize(new java.awt.Dimension(633, 196));
        setPreferredSize(new java.awt.Dimension(633, 196));
        setLayout(null);

        jlPosicao.setFont(new java.awt.Font("Tahoma", 1, 11));
        
        
        
        //jbCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icones/exit.png"))); // NOI18N
        jbCancelar.setText(PropertiesUtil.getMessage("botao.desistir"));
        jbCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCancelarActionPerformed(evt);
            }
        });
        add(jbCancelar);
        jbCancelar.setBounds(360, 150, 120, 25);

        //jbConfirmar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icones/confirmar.png"))); // NOI18N
        jbConfirmar.setText(PropertiesUtil.getMessage("botao.confirmar"));
        jbConfirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbConfirmarActionPerformed(evt);
            }
        });
        add(jbConfirmar);
        jbConfirmar.setBounds(490, 150, 120, 23);

        jLTaxaOperacao.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLTaxaOperacao.setText(PropertiesUtil.getMessage("label.taxa.operacao"));
        add(jLTaxaOperacao);
        jLTaxaOperacao.setBounds(10, 30, 118, 14);
        add(jTtaxaOperacao);
        jTtaxaOperacao.setBounds(150, 30, 80, 20);

        jLEstado.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLEstado.setText(PropertiesUtil.getMessage("label.estado"));
        add(jLEstado);
        jLEstado.setBounds(390, 30, 118, 14);

        jcEstado.setModel(new javax.swing.DefaultComboBoxModel(new EstadoMaquinaEnum[] { EstadoMaquinaEnum.OCIOSA, EstadoMaquinaEnum.MANUTENCAO, EstadoMaquinaEnum.OPERACAO }));
        add(jcEstado);
        jcEstado.setBounds(518, 30, 90, 20);

        //jlPosicao1.setText(PropertiesUtil.getMessage("label.numero.baliza"));
        //add(jlPosicao1);
        //jlPosicao1.setBounds(250, 90, 150, 14);

        jlPosicao2.setText(PropertiesUtil.getMessage("label.tonelada.hora"));
        add(jlPosicao2);
        jlPosicao2.setBounds(250, 30, 102, 14);

        jLEstado1.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLEstado1.setText(PropertiesUtil.getMessage("label.gira.lanca"));
        add(jLEstado1);
        jLEstado1.setBounds(390, 60, 118, 14);

        cmbGirarLanca.setModel(new javax.swing.DefaultComboBoxModel(new String[] { PropertiesUtil.getMessage("mensagem.condicional.sim"), PropertiesUtil.getMessage("mensagem.condicional.nao") }));
        add(cmbGirarLanca);
        cmbGirarLanca.setBounds(518, 60, 90, 20);

        jlVelocidade.setFont(new java.awt.Font("Tahoma", 1, 11));
        jlVelocidade.setText(PropertiesUtil.getMessage("label.velocidade"));
        jlVelocidade.setToolTipText(PropertiesUtil.getMessage("tooltip.velocidade.deslocamento.maquina.baliza"));
        add(jlVelocidade);
        jlVelocidade.setBounds(10, 60, 118, 14);
        add(jtfVelocidadeMaquina);
        jtfVelocidadeMaquina.setBounds(150, 60, 80, 20);

        jLabel2.setText(PropertiesUtil.getMessage("label.baliza.hora"));
        add(jLabel2);
        jLabel2.setBounds(250, 60, 102, 14);

        lbPatio.setFont(new java.awt.Font("Tahoma", 1, 11));
        lbPatio.setText(PropertiesUtil.getMessage("label.patio.1"));
        add(lbPatio);
        lbPatio.setBounds(10, 90, 118, 14);

        add(comboPatio);
        comboPatio.setBounds(150, 90, 90, 20);
        
        
        jlPosicao.setText(PropertiesUtil.getMessage("label.posicao"));
        add(jlPosicao);
        jlPosicao.setBounds(10, 120, 118, 14);
        
        add(proximaBalizaJCB);
        proximaBalizaJCB.setBounds(150, 120, 120, 20);

        
    }// </editor-fold>//GEN-END:initComponents

    private void comboPatioItemStateChanged(ItemEvent evt) {
		 InterfacePatio ipatioLocal = (InterfacePatio) comboPatio.getSelectedItem();
        if (ipatioLocal != null) {            
            carregaTodasBalizas(ipatioLocal.getPatioVisualizado());         
        }		
	}

	private void jbCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCancelarActionPerformed
        this.operacaoCanceladaPeloUsuario = Boolean.TRUE;
        fecharJanela();
        // chegando ate o JDialog
        DSSStockyardTelaUtil.getParentDialog(this).setVisible(false);
    }//GEN-LAST:event_jbCancelarActionPerformed

    private void jbConfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbConfirmarActionPerformed
       //valida os dados
       if(validadaDadosEditados()){
           this.operacaoCanceladaPeloUsuario = Boolean.FALSE;
           // chegando ate o JDialog
           DSSStockyardTelaUtil.getParentDialog(this).setVisible(false);
           
       }

    }//GEN-LAST:event_jbConfirmarActionPerformed

    private boolean validadaDadosEditados()
    {//TODO Aqui são feitas validacoes simples, as mais especificas para cada objeto deve ser feita no nivel acima
        try{
        	List<String> listaParametros;
            //guarda patio editado para a maquina pa-carregadeira
            ipatio = (InterfacePatio) getComboPatio().getSelectedItem();
            posicaoEditada = (MetaBaliza) proximaBalizaJCB.getSelectedItem();
            
            //valida a taxa de operacao da maquina
            taxaOperacao = Double.parseDouble(jTtaxaOperacao.getText().trim());
            if(taxaOperacao <= 0){
                listaParametros = new ArrayList<String>();
                listaParametros.add(jLTaxaOperacao.getText());
                throw new ValidacaoCampoException("exception.validacao.campo.negativo", listaParametros);
            }
            
            if (cmbGirarLanca.getSelectedIndex() == 0) {
                girarLanca = Boolean.TRUE;
            } else {
                girarLanca = Boolean.FALSE;
            }
            //maquinaEditada.setGiraLanca(girarLanca);
            //valida a velocidade de deslocamento
            estado = (EstadoMaquinaEnum) jcEstado.getSelectedItem();
            
            velocidadeDeslocamento = Double.parseDouble(jtfVelocidadeMaquina.getText().trim());
            if(velocidadeDeslocamento <= 0){
                listaParametros = new ArrayList<String>();
                listaParametros.add(jlVelocidade.getText());
                throw new ValidacaoCampoException("exception.validacao.campo.negativo", listaParametros);
            }
            
            return true;
        }catch (ValidacaoCampoException ex) {
        	controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            controladorDSP.ativarMensagem(interfaceMensagem);
            return false;
        } catch (NumberFormatException nbex) {
        	controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            interfaceMensagem.setDescricaoMensagem("Informe somente números");
            controladorDSP.ativarMensagem(interfaceMensagem);
            return false;
        }
    }

  
    public MetaBaliza getPosicaoEditada() {
		return posicaoEditada;
	}

	public void setPosicaoEditada(MetaBaliza posicaoEditada) {
		this.posicaoEditada = posicaoEditada;
	}

	public void setIpatio(InterfacePatio ipatio) {
		this.ipatio = ipatio;
	}

	public EstadoMaquinaEnum getEstado() {
        return estado;
    }

    public void setEstado(EstadoMaquinaEnum estado) {
        this.estado = estado;
    }

    public Boolean getOperacaoCanceladaPeloUsuario() {
        return operacaoCanceladaPeloUsuario;
    }

    public void setOperacaoCanceladaPeloUsuario(Boolean operacaoCanceladaPeloUsuario) {
        this.operacaoCanceladaPeloUsuario = operacaoCanceladaPeloUsuario;
    }

    public Integer getPosicaoNroBaliza() {
        return posicaoNroBaliza;
    }


    public JTextField getJTtaxaOperacao() {
        return jTtaxaOperacao;
    }

    public void setJTtaxaOperacao(JTextField jTtaxaOperacao) {
        this.jTtaxaOperacao = jTtaxaOperacao;
    }

    public Double getTaxaOperacao() {
        return taxaOperacao;
    }

    public void setTaxaOperacao(Double taxaOperacao) {
        this.taxaOperacao = taxaOperacao;
    }

    public Double getVelocidadeDeslocamento() {
        return velocidadeDeslocamento;
    }
    
    public ControladorDSP getControladorDSP() {
        return controladorDSP;
    }

    public void setControladorDSP(ControladorDSP controladorDSP) {
        this.controladorDSP = controladorDSP;
    }

    public Integer getIndiceAtividade() {
        return indiceAtividade;
    }

    public void setIndiceAtividade(Integer indiceAtividade) {
        this.indiceAtividade = indiceAtividade;
    }

    public Integer getIndiceAtividadeEditada() {
        return indiceAtividadeEditada;
    }

    public void setIndiceAtividadeEditada(Integer indiceAtividadeEditada) {
        this.indiceAtividadeEditada = indiceAtividadeEditada;
    }

    public EstadoMaquinaEnum getEstadoOriginal() {
        return estadoOriginal;
    }

    public Baliza getPosicaoOriginal() {
        return posicaoOriginal;
    }

    public Double getTaxaOperacaoOriginal() {
        return taxaOperacaoOriginal;
    }

    public Boolean getGirarLanca() {
        return girarLanca;
    }

    public Double getVelocidadeDeslocamentoOriginal() {
        return velocidadeDeslocamentoOriginal;
    }
    

    public void setGirarLanca(Boolean girarLanca) {
        this.girarLanca = girarLanca;
    }

    public JComboBox getComboPatio() {
        return comboPatio;
    }

    public InterfacePatio getIpatio() {
        return ipatio;
    }

    public InterfacePatio getIpatioDaMaquina() {
        return ipatioDaMaquina;
    }
    
    private void atualizaComboPatio(){
        getComboPatio().removeAllItems();
        for(InterfacePatio iPatio : controladorDSP.getInterfaceDSP().getListaDePatios() ){
            getComboPatio().addItem(iPatio);
        }
       getComboPatio().setSelectedItem(ipatioDaMaquina);
       carregaTodasBalizas(ipatioDaMaquina.getPatioVisualizado());
    }

    
    
    private void carregaTodasBalizas(Patio ipatioLocal) {
        proximaBalizaJCB.removeAllItems();
        if (ipatioLocal != null)
        for (MetaBaliza baliza : ipatioLocal.getMetaPatio().getListaDeMetaBalizas()) {           
                proximaBalizaJCB.addItem(baliza);                
        }
    }

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbGirarLanca;
    private javax.swing.JComboBox comboPatio;
    private javax.swing.JLabel jLEstado;
    private javax.swing.JLabel jLEstado1;
    private javax.swing.JLabel jLTaxaOperacao;
    private javax.swing.JLabel jLabel2;
    //private javax.swing.JTextField jTposicaobaliza;
    private javax.swing.JTextField jTtaxaOperacao;
    private javax.swing.JButton jbCancelar;
    private javax.swing.JButton jbConfirmar;
    private javax.swing.JComboBox jcEstado;
    private javax.swing.JLabel jlPosicao;
    private javax.swing.JLabel jlPosicao1;
    private javax.swing.JLabel jlPosicao2;
    private javax.swing.JLabel jlVelocidade;
    private javax.swing.JTextField jtfVelocidadeMaquina;
    private javax.swing.JLabel lbPatio;
    private javax.swing.JComboBox proximaBalizaJCB;
    // End of variables declaration//GEN-END:variables

}
