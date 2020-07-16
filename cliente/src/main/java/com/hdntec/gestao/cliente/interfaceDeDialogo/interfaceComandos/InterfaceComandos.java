package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceComandos;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.hdntec.gestao.cliente.atualizacao.sistemasexternos.sap.InformacaoDiferencaEstoqueGUI;
import com.hdntec.gestao.cliente.interfaceDeDialogo.ControladorInterfaceInicial;
import com.hdntec.gestao.cliente.interfaceDeDialogo.InterfaceInicial;
import com.hdntec.gestao.cliente.interfaceDeDialogo.ModoDeOperacaoEnum;
import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.cliente.relatorio.InterfaceGerarRelatorio;
import com.hdntec.gestao.cliente.relatorio.InterfaceGestaoRelatorio;
import com.hdntec.gestao.domain.blendagem.Blendagem;
import com.hdntec.gestao.domain.navios.entity.status.Carga;
import com.hdntec.gestao.domain.navios.entity.status.FilaDeNavios;
import com.hdntec.gestao.domain.plano.entity.PlanoEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
import com.hdntec.gestao.domain.relatorio.enums.EspecieRelatorioEnum;
import com.hdntec.gestao.exceptions.BalizasSelecionadasException;
import com.hdntec.gestao.exceptions.BlendagemInvalidaException;
import com.hdntec.gestao.exceptions.CampanhaIncompativelException;
import com.hdntec.gestao.exceptions.CarregarOficialNaoNecessariaException;
import com.hdntec.gestao.exceptions.ConsolidacaoNaoNecessariaException;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.OperacaoCanceladaPeloUsuarioException;
import com.hdntec.gestao.exceptions.PermissaoDeUsuarioException;
import com.hdntec.gestao.exceptions.PilhaParaRecuperacaoSemMaquinaCapazException;
import com.hdntec.gestao.exceptions.PlanosOficiaisNaoLocalizadosException;
import com.hdntec.gestao.exceptions.RemocaoDePlanosNaoPermitidaException;
import com.hdntec.gestao.exceptions.SelecaoObjetoModoEmpilhamentoException;
import com.hdntec.gestao.exceptions.SelecaoObjetoModoRecuperacaoException;
import com.hdntec.gestao.exceptions.TempoInsuficienteException;
import com.hdntec.gestao.exceptions.ValidacaoObjetosOperacaoException;
import com.hdntec.gestao.util.PropertiesUtil;
import com.hdntec.gestao.util.datahora.DSSStockyardTimeUtil;

public class InterfaceComandos extends javax.swing.JPanel {

    /** o status da atualiza??o autom?tica de informa??es dos sistemas externos */
    private boolean atualizacaoAutomaticaAtivada;

    /** acesso as opera??es do subsistema de interface gr?fica de comandos */
    private ControladorInterfaceComandos controladorInterfaceComandos;

    /** interface gr?fica principal do sistema */
    private InterfaceInicial interfaceInicial;

    /** a interface das mensagens exibidas pela interface baliza */
    private InterfaceMensagem interfaceMensagem;

    /** gravar a cor padrao de botao*/
    public Color backgroundNormalDeBotao;

    public boolean integracaoMesAtivada = Boolean.TRUE;

    public InterfaceComandos(InterfaceInicial interfaceInicial, ControladorInterfaceInicial controladorInterfaceInicial) {
        this.interfaceInicial = interfaceInicial;
        initComponents();
        habilitaFuncoes();
        controladorInterfaceComandos = new ControladorInterfaceComandos();
        controladorInterfaceComandos.setInterfaceComandos(this);
        cmdSalvar.setEnabled(false);
        //ajustanto tamanho
        this.setBounds(0, 0, 880, 97);

        this.controladorInterfaceComandos.setInterfaceInicial(controladorInterfaceInicial);
        //permissoes de usuario
        bloqueiaAcaoUsuarioLeitura();
    }

    public boolean isAtualizacaoAutomaticaAtivada() {
        return atualizacaoAutomaticaAtivada;
    }

    public void setAtualizacaoAutomaticaAtivada(boolean atualizacaoAutomaticaAtivada) {
        this.atualizacaoAutomaticaAtivada = atualizacaoAutomaticaAtivada;
    }

    public ControladorInterfaceComandos getControladorInterfaceComandos() {
        return controladorInterfaceComandos;
    }

    public void setControladorInterfaceComandos(ControladorInterfaceComandos controladorInterfaceComandos) {
        this.controladorInterfaceComandos = controladorInterfaceComandos;
    }

    public InterfaceInicial getInterfaceInicial() {
        return interfaceInicial;
    }

    public void setInterfaceInicial(InterfaceInicial interfaceInicial) {
        this.interfaceInicial = interfaceInicial;

    }

    @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {
      cmdConsolidarPlano = new javax.swing.JButton();
      cmdRemoverSituacao = new javax.swing.JButton();
      cmdGestaoRelatorio = new javax.swing.JButton();
      cmdLimparSelecao = new javax.swing.JButton();
      jbCarregarPlano = new javax.swing.JButton();
      cmdPesquisarDados = new javax.swing.JButton();
      jbAtualizarMES = new javax.swing.JButton();
      jSeparator2 = new javax.swing.JSeparator();
      jSeparator3 = new javax.swing.JSeparator();
      jSeparator4 = new javax.swing.JSeparator();
      jSeparator5 = new javax.swing.JSeparator();
      jlConsolidar = new javax.swing.JLabel();
      jlApagar = new javax.swing.JLabel();
      jlCarregar = new javax.swing.JLabel();
      jlLimpar = new javax.swing.JLabel();
      lblEstoque = new javax.swing.JLabel();
      jlSincronizar = new javax.swing.JLabel();
      jlSincronizarMES = new javax.swing.JLabel();
      cmdSalvar = new javax.swing.JButton();
      lbOficializar = new javax.swing.JLabel();
      jbAtualizarCRM = new javax.swing.JButton();
      jlSincronizarCRM = new javax.swing.JLabel();
      cmdEstoque = new javax.swing.JButton();
      jlPesquisar1 = new javax.swing.JLabel();

      setMaximumSize(new java.awt.Dimension(3276, 3276));
      setMinimumSize(new java.awt.Dimension(800, 90));
      setLayout(null);

      cmdConsolidarPlano.setBackground(new java.awt.Color(255, 255, 255));
      cmdConsolidarPlano.setIcon(new javax.swing.ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens") + "/save-a.png"));
      cmdConsolidarPlano.setToolTipText(PropertiesUtil.getMessage("tooltip.consolidar.plano.oficial.no.servidor"));
      cmdConsolidarPlano.setPressedIcon(new javax.swing.ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens") + "/save-b.png"));
      cmdConsolidarPlano.setRolloverIcon(new javax.swing.ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens") + "/save-c.png"));
      cmdConsolidarPlano.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            cmdConsolidarPlanoActionPerformed(evt);
         }
      });
      add(cmdConsolidarPlano);
      cmdConsolidarPlano.setBounds(25, 25, 35, 27);

      cmdRemoverSituacao.setIcon(new javax.swing.ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens") + "/apagar-a.png"));
      cmdRemoverSituacao.setToolTipText(PropertiesUtil.getMessage("tooltip.apaga.todas.situacoes.do.plano.partir.da.situacao.selecionada"));
      cmdRemoverSituacao.setPressedIcon(new javax.swing.ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens") + "/apagar-b.png"));
      cmdRemoverSituacao.setRolloverIcon(new javax.swing.ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens") + "/apagar-c.png"));
      cmdRemoverSituacao.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            cmdRemoverSituacaoActionPerformed(evt);
         }
      });
      add(cmdRemoverSituacao);
      cmdRemoverSituacao.setBounds(147, 25, 35, 27);

      cmdGestaoRelatorio.setIcon(new javax.swing.ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens") + "/relatorio-a.png"));
      cmdGestaoRelatorio.setToolTipText(PropertiesUtil.getMessage("tooltip.exibe.relatorio"));
      cmdGestaoRelatorio.setPressedIcon(new javax.swing.ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens") + "/relatorio-b.png"));
      cmdGestaoRelatorio.setRolloverIcon(new javax.swing.ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens") + "/relatorio-c.png"));
      add(cmdGestaoRelatorio);
      cmdGestaoRelatorio.setBounds(695, 25, 35, 27);

      cmdLimparSelecao.setIcon(new javax.swing.ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens") + "/limpar-a.png"));
      cmdLimparSelecao.setToolTipText(PropertiesUtil.getMessage("tooltip.limpa.item.selecionado"));
      cmdLimparSelecao.setPressedIcon(new javax.swing.ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens") + "/limpar-b.png"));
      cmdLimparSelecao.setRolloverIcon(new javax.swing.ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens") + "/limpar-c.png"));
      cmdLimparSelecao.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            cmdLimparSelecaoActionPerformed(evt);
         }
      });
      add(cmdLimparSelecao);
      cmdLimparSelecao.setBounds(275, 25, 35, 27);

      jbCarregarPlano.setIcon(new javax.swing.ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens") + "/carregar-a.png"));
      jbCarregarPlano.setToolTipText(PropertiesUtil.getMessage("tooltip.carregar.plano.oficial.servidor"));
      jbCarregarPlano.setPressedIcon(new javax.swing.ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens") + "/carregar-b.png"));
      jbCarregarPlano.setRolloverIcon(new javax.swing.ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens") + "/carregar-c.png"));
      jbCarregarPlano.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            jbCarregarPlanoActionPerformed(evt);
         }
      });
      add(jbCarregarPlano);
      jbCarregarPlano.setBounds(207, 25, 35, 27);

      cmdPesquisarDados.setIcon(new javax.swing.ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens") + "/pesquisar-a.png"));
      cmdPesquisarDados.setToolTipText(PropertiesUtil.getMessage("tooltip.consultar"));
      cmdPesquisarDados.setPressedIcon(new javax.swing.ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens") + "/pesquisar-b.png"));
      cmdPesquisarDados.setRolloverIcon(new javax.swing.ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens") + "/pesquisar-c.png"));
      cmdPesquisarDados.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            cmdPesquisarDadosActionPerformed(evt);
         }
      });
      add(cmdPesquisarDados);
      cmdPesquisarDados.setBounds(350, 25, 35, 27);

      jbAtualizarMES.setIcon(new javax.swing.ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens") + "/sincronizar-a.png"));
      jbAtualizarMES.setToolTipText(PropertiesUtil.getMessage("tooltip.atualiza.dsp.com.novos.dados.servidor"));
      //jbAtualizarMES.setEnabled(false);
      jbAtualizarMES.setPressedIcon(new javax.swing.ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens") + "/sincronizar-b.png"));
      jbAtualizarMES.setRolloverIcon(new javax.swing.ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens") + "/sincronizar-c.png"));
      jbAtualizarMES.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            jbAtualizarMESActionPerformed(evt);
         }
      });
      add(jbAtualizarMES);
      jbAtualizarMES.setBounds(515, 25, 35, 27);

      jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
      add(jSeparator2);
      jSeparator2.setBounds(260, 10, 10, 59);

      jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
      add(jSeparator3);
      jSeparator3.setBounds(330, 10, 10, 59);

      jSeparator4.setOrientation(javax.swing.SwingConstants.VERTICAL);
      add(jSeparator4);
      jSeparator4.setBounds(675, 10, 10, 59);

      jSeparator5.setOrientation(javax.swing.SwingConstants.VERTICAL);
      add(jSeparator5);
      jSeparator5.setBounds(475, 10, 10, 59);

      jlConsolidar.setFont(new java.awt.Font("Arial", 0, 12));
      jlConsolidar.setForeground(new java.awt.Color(99, 99, 99));
      jlConsolidar.setText(PropertiesUtil.getMessage("label.consolidar"));
      add(jlConsolidar);
      jlConsolidar.setBounds(10, 60, 70, 15);

      jlApagar.setFont(new java.awt.Font("Arial", 0, 12));
      jlApagar.setForeground(new java.awt.Color(99, 99, 99));
      jlApagar.setText(PropertiesUtil.getMessage("label.apagar"));
      add(jlApagar);
      jlApagar.setBounds(145, 60, 40, 15);

      jlCarregar.setFont(new java.awt.Font("Arial", 0, 12));
      jlCarregar.setForeground(new java.awt.Color(99, 99, 99));
      jlCarregar.setText(PropertiesUtil.getMessage("label.carregar"));
      add(jlCarregar);
      jlCarregar.setBounds(200, 60, 50, 15);

      jlLimpar.setFont(new java.awt.Font("Arial", 0, 12));
      jlLimpar.setForeground(new java.awt.Color(99, 99, 99));
      jlLimpar.setText(PropertiesUtil.getMessage("label.limpar"));
      add(jlLimpar);
      jlLimpar.setBounds(275, 60, 40, 15);

      lblEstoque.setFont(new java.awt.Font("Arial", 0, 12));
      lblEstoque.setForeground(new java.awt.Color(99, 99, 99));
      lblEstoque.setText(PropertiesUtil.getMessage("label.consultar.estoque"));
      add(lblEstoque);
      lblEstoque.setBounds(410, 60, 60, 15);

      jlSincronizar.setFont(new java.awt.Font("Arial", 0, 12));
      jlSincronizar.setForeground(new java.awt.Color(99, 99, 99));
      jlSincronizar.setText(PropertiesUtil.getMessage("label.relatorio"));
      add(jlSincronizar);
      jlSincronizar.setBounds(685, 60, 60, 15);

      jlSincronizarMES.setFont(new java.awt.Font("Arial", 0, 12));
      jlSincronizarMES.setForeground(new java.awt.Color(99, 99, 99));
      jlSincronizarMES.setText(PropertiesUtil.getMessage("label.sincronizar.mes"));
      add(jlSincronizarMES);
      jlSincronizarMES.setBounds(495, 60, 70, 15);

      cmdSalvar.setIcon(new javax.swing.ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens") + "/consolidar-a.png"));
      cmdSalvar.setPressedIcon(new javax.swing.ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens") + "/consolidar-b.png"));
      cmdSalvar.setRolloverIcon(new javax.swing.ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens") + "/consolidar-c.png"));
      cmdSalvar.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            cmdSalvarActionPerformed(evt);
         }
      });
      add(cmdSalvar);
      cmdSalvar.setBounds(89, 25, 35, 27);

      lbOficializar.setFont(new java.awt.Font("Arial", 0, 12));
      lbOficializar.setForeground(new java.awt.Color(99, 99, 99));
      lbOficializar.setText(PropertiesUtil.getMessage("label.oficializar"));
      add(lbOficializar);
      lbOficializar.setBounds(90, 60, 40, 15);

      jbAtualizarCRM.setIcon(new javax.swing.ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens") + "/sincronizar-a.png"));
      jbAtualizarCRM.setToolTipText(PropertiesUtil.getMessage("tooltip.atualiza.dsp.com.novos.dados.servidor"));
      //jbAtualizarCRM.setEnabled(false);
      jbAtualizarCRM.setPressedIcon(new javax.swing.ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens") + "/sincronizar-b.png"));
      jbAtualizarCRM.setRolloverIcon(new javax.swing.ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens") + "/sincronizar-c.png"));
      jbAtualizarCRM.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            jbAtualizarCRMActionPerformed(evt);
         }
      });
      add(jbAtualizarCRM);
      jbAtualizarCRM.setBounds(608, 25, 35, 27);

      jlSincronizarCRM.setFont(new java.awt.Font("Arial", 0, 12));
      jlSincronizarCRM.setForeground(new java.awt.Color(99, 99, 99));
      jlSincronizarCRM.setText(PropertiesUtil.getMessage("label.sincronizar.crm"));
      add(jlSincronizarCRM);
      jlSincronizarCRM.setBounds(585, 60, 80, 15);

      cmdEstoque.setIcon(new javax.swing.ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens") + "/estoque.jpg"));
      cmdEstoque.setToolTipText(PropertiesUtil.getMessage("tooltip.consultar.estoque"));
      cmdEstoque.setPressedIcon(new javax.swing.ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens") + "/estoque.jpg"));
      cmdEstoque.setRolloverIcon(new javax.swing.ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens") + "/estoque.jpg"));
      cmdEstoque.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            cmdEstoqueActionPerformed(evt);
         }
      });
      add(cmdEstoque);
      cmdEstoque.setBounds(415, 25, 35, 27);

      jlPesquisar1.setFont(new java.awt.Font("Arial", 0, 12));
      jlPesquisar1.setForeground(new java.awt.Color(99, 99, 99));
      jlPesquisar1.setText(PropertiesUtil.getMessage("label.pesquisar"));
      add(jlPesquisar1);
      jlPesquisar1.setBounds(340, 60, 60, 15);
      
      initMenuRelatorio();
      
   }// </editor-fold>//GEN-END:initComponents

    /**
     * 
     */
    private void initMenuRelatorio() {
        
    	//Criando o controlador do menu de relatorios
    	InterfaceMenuRelatorio interfaceMenuRelatorio = new InterfaceMenuRelatorio();
    	
    	//Criando o item do menu "Relatorio de DSP"
        menuItemGestaoRelatorio = new JMenuItem(
        		PropertiesUtil.getMessage("relatorio.menu.dsp"));
        menuItemGestaoRelatorio.addActionListener(new ActionListener(){
    		@Override
    		public void actionPerformed(ActionEvent e) {
    			showInterfaceGestaoRelatorio();
    		}
    	});
        interfaceMenuRelatorio.addItemRelatorio(menuItemGestaoRelatorio);
    	
        //Criando o item do menu "Plano de Recuperacao"
        menuItemPlanoRecuperacao = new JMenuItem(
        		PropertiesUtil.getMessage("relatorio.menu.planoRecuperacao"));
        menuItemPlanoRecuperacao.addActionListener(new ActionListener(){
    		@Override
    		public void actionPerformed(ActionEvent e) {
    			showInterfaceRelatorioPlanoDeRecuperacao();
    		}
    	});
        interfaceMenuRelatorio.addItemRelatorio(menuItemPlanoRecuperacao);
        
        //Criando o item do menu "Indicador de Qualidade"
        menuItemIndicadorQualidade = new JMenuItem(
        		PropertiesUtil.getMessage("relatorio.menu.indicadorQualidade"));
        menuItemIndicadorQualidade.addActionListener(new ActionListener(){
    		@Override
    		public void actionPerformed(ActionEvent e) {
    			showInterfaceRelatorioIndicadorQualidade();
    		}
    	});
        interfaceMenuRelatorio.addItemRelatorio(menuItemIndicadorQualidade);
        
        //Criando o item do menu "Relatorio de Operacao"
        menuItemOperacaoCarga = new JMenuItem(
        		PropertiesUtil.getMessage("relatorio.menu.operacao"));
        menuItemOperacaoCarga.addActionListener(new ActionListener(){
    		@Override
    		public void actionPerformed(ActionEvent e) {
    			showInterfaceRelatorioOperacao();
    		}
    	});
        interfaceMenuRelatorio.addItemRelatorio(menuItemOperacaoCarga);
        
        //Criando o item do menu "Tempo e Taxa de Atendimento de Carga"
        menuItemAtendimentoCarga = new JMenuItem(
        		PropertiesUtil.getMessage("relatorio.menu.atendimento"));
        menuItemAtendimentoCarga.addActionListener(new ActionListener(){
    		@Override
    		public void actionPerformed(ActionEvent e) {
    			showInterfaceRelatorioAtendimento();
    		}
    	});
        interfaceMenuRelatorio.addItemRelatorio(menuItemAtendimentoCarga);
        
        //Adicionando o menu de relatorio no evento do botao de relatorios
        cmdGestaoRelatorio.addMouseListener(interfaceMenuRelatorio);
    }
    
    /**
     * 
     * showInterfaceGestaoRelatorio
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 18/08/2009
     * @see
     * @return Returns the void.
     */
    private void showInterfaceGestaoRelatorio() {
	 try
	 {
	    InterfaceGestaoRelatorio gestaoRelatorio = 
	    	new InterfaceGestaoRelatorio(this.interfaceInicial, true);
	    gestaoRelatorio.setLocationRelativeTo(null);
	    gestaoRelatorio.setVisible(true);
	 }
	 catch (ErroSistemicoException e)
	 {
		 e.printStackTrace();
		 interfaceInicial.desativarMensagem();
	    interfaceMensagem = new InterfaceMensagem();
	    interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
	    interfaceMensagem.setDescricaoMensagem(e.getMessage());
	    controladorInterfaceComandos.ativarMensagem(interfaceMensagem);
	 }
	 catch (Exception ex)
	 {
		 ex.printStackTrace();
		 interfaceInicial.desativarMensagem();
	    interfaceMensagem = new InterfaceMensagem();
	    interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
	    interfaceMensagem.setDescricaoMensagem(ex.getMessage());
	    controladorInterfaceComandos.ativarMensagem(interfaceMensagem);
	 }
    }

    /**
     * 
     * showInterfaceRelatorioPlanoDeRecuperacao
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 18/08/2009
     * @see
     * @return Returns the void.
     */
    private void showInterfaceRelatorioPlanoDeRecuperacao() {
	 try
	 {
		Blendagem blendagem = getInterfaceInicial().getInterfaceBlendagem().getBlendagemVisualizada();
		
		if (blendagem == null || blendagem.getCargaSelecionada()==null) {
			JOptionPane.showMessageDialog(null, PropertiesUtil.getMessage("mensagem.option.pane.nenhuma.carga.selecionada"));
		}
		else if ( blendagem.getListaBalizasBlendadas() == null || 
				blendagem.getListaBalizasBlendadas().isEmpty()) {
			JOptionPane.showMessageDialog(null, PropertiesUtil.getMessage("mensagem.option.pane.nenhuma.baliza.selecionada"));
		}
		else {	
			InterfaceGerarRelatorio gestaoRelatorio = new 
				InterfaceGerarRelatorio(this.interfaceInicial,true,
						EspecieRelatorioEnum.PLANO_RECUPERACAO);
			gestaoRelatorio.setLocationRelativeTo(null);
		    gestaoRelatorio.setVisible(true);
			
		}
		
			
		
	 }
	 catch (ErroSistemicoException e)
	 {
		 e.printStackTrace();
		 interfaceInicial.desativarMensagem();
	    interfaceMensagem = new InterfaceMensagem();
	    interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
	    interfaceMensagem.setDescricaoMensagem(e.getMessage());
	    controladorInterfaceComandos.ativarMensagem(interfaceMensagem);
	 }
	 catch (Exception ex)
	 {
		 ex.printStackTrace();
		 interfaceInicial.desativarMensagem();
	    interfaceMensagem = new InterfaceMensagem();
	    interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
	    interfaceMensagem.setDescricaoMensagem(ex.getMessage());
	    controladorInterfaceComandos.ativarMensagem(interfaceMensagem);
	 }
    }

    /**
     * 
     * showInterfaceRelatorioIndicadorQualidade
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 18/08/2009
     * @see
     * @return Returns the void.
     */
    private void showInterfaceRelatorioIndicadorQualidade() {
    	showInterfaceGerarRelatorio(EspecieRelatorioEnum.INDICADOR_QUALIDADE);
    }
    
    /**
     * 
     * showInterfaceRelatorioOperacao
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 18/08/2009
     * @see
     * @return Returns the void.
     */
    private void showInterfaceRelatorioOperacao() {
    	showInterfaceGerarRelatorio(EspecieRelatorioEnum.DESLOCAMENTO_MAQUINA);
    }
    
    private void showInterfaceRelatorioAtendimento() {
    	showInterfaceGerarRelatorio(EspecieRelatorioEnum.ATENDIMENTO_CARGA);
    }
    
    /**
     * 
     * showInterfaceGerarRelatorio
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 18/08 /2009
     * @see
     * @param especie
     * @return Returns the void.
     */
    private void showInterfaceGerarRelatorio(EspecieRelatorioEnum especie) {
   	   try
   	   {
   		  InterfaceGerarRelatorio gerarRelatorio = new 
  			InterfaceGerarRelatorio(this.interfaceInicial,true,especie);
   		   
   		   gerarRelatorio.setLocationRelativeTo(null);
   		   gerarRelatorio.setVisible(true);
   	   }
   	   catch (ErroSistemicoException errEx)
   	   {
   		   JOptionPane.showMessageDialog(this, errEx.getMessage(), PropertiesUtil.getMessage("relatorio.gestao.title"), JOptionPane.ERROR_MESSAGE);
   	   }
   	   catch (Exception ex)
   	   {
   		   JOptionPane.showMessageDialog(this, PropertiesUtil.getMessage("relatorio.gestao.error"), PropertiesUtil.getMessage("relatorio.gestao.title"), JOptionPane.ERROR_MESSAGE);
   	   }
     }
    

    public void desabilitaFuncoes() {
        // Operacao dos botoes
        cmdPesquisarDados.setEnabled(false);
        jbCarregarPlano.setEnabled(false);
        cmdGestaoRelatorio.setEnabled(false);
        cmdConsolidarPlano.setEnabled(false);
        cmdRemoverSituacao.setEnabled(false);
        cmdGestaoRelatorio.setEnabled(false);
    }

    public void habilitaFuncoes() {
        // Operacao dos botoes
        cmdGestaoRelatorio.setEnabled(true);
        cmdPesquisarDados.setEnabled(true);
        cmdRemoverSituacao.setEnabled(true);
        //cmdSalvar.setEnabled(true);
        
        if (interfaceInicial.getSituacaoPatioExibida()!=null &&
        		interfaceInicial.getSituacaoPatioExibida().getPlanoEmpilhamento().getEhOficial()) {
            jbCarregarPlano.setEnabled(true);
            cmdConsolidarPlano.setEnabled(true);
        }else{
            jbCarregarPlano.setEnabled(false);
            cmdConsolidarPlano.setEnabled(false);
        }
    }

    private void verificarPossibilidadeDeConsolidacao() throws ConsolidacaoNaoNecessariaException {
        controladorInterfaceComandos.verificarPossibilidadeDeConsolidacao();
    }


    private void verificarPossibilidadeCarregarPlanoUsuario() throws CarregarOficialNaoNecessariaException {
        controladorInterfaceComandos.verificarPossibilidadeCarregarPlanoUsuario();
    }

    private void cmdConsolidarPlanoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdConsolidarPlanoActionPerformed

    	try {
    		verificarPossibilidadeDeConsolidacao();
    		desabilitarTodasFuncoes();

    		final InterfaceMensagem interfaceMensagemProcessamento = new InterfaceMensagem();
    		interfaceMensagemProcessamento.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_PROCESSAMENTO);
    		interfaceMensagemProcessamento.setProcessamentoAtividado(Boolean.TRUE);
    		interfaceMensagemProcessamento.setDescricaoMensagem(PropertiesUtil.buscarPropriedade("mensagem.processamento.consolidar.plano"));

    		/*new Thread("Thread botao Consolidar plano") {
                @Override
                public void run() {
                    try {
    		 */            JLabel pergunta = new JLabel(PropertiesUtil.getMessage("mensagem.option.pane.confirma.consolidacao.plano"));
    		 pergunta.setFont(new Font("Tahoma", Font.PLAIN, 11));
    		 int confirm = JOptionPane.showOptionDialog(
    				 interfaceInicial,
    				 pergunta,
    				 PropertiesUtil.getMessage("mensagem.option.pane.tipo.atencao") ,
    				 JOptionPane.YES_OPTION,
    				 JOptionPane.INFORMATION_MESSAGE,
    				 null,
    				 null,
    				 null);

    		 if (confirm == JOptionPane.YES_OPTION) {
    			 interfaceInicial.ativarMensagem(interfaceMensagemProcessamento);
    			 controladorInterfaceComandos.consolidarPlano();
    			 // limpa a mensagem de processamento
    			 interfaceInicial.desativarMensagem();
    		 }

    		 if (interfaceInicial.getModoDeOperacao().equals(ModoDeOperacaoEnum.EDITAR)) {
    			 // limpa o controlador de undo e redo do modo editar
    			 interfaceInicial.getInterfaceDSP().getControladorDSP().getControladorAtualizacaoEdicao().limpaControlador();
    		 }
    		/* SituacaoPatioDAO dao = new SituacaoPatioDAO();
             dao.deletarPilhas();*/
    		 habilitaFuncoes();

    		 /*                    } catch (ErroSistemicoException errSis) {
                        interfaceMensagem = new InterfaceMensagem();
                        interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
                        interfaceMensagem.setDescricaoMensagem(errSis.getMessage());
                        controladorInterfaceComandos.ativarMensagem(interfaceMensagem);
                    } finally {
                        // habilita todas as funcoes
                        habilitarTodasFuncoes();
                    }
    		  */               // }
    		 //}.start();            
    	} catch (ConsolidacaoNaoNecessariaException consEx) {

    		// Ã© preciso limpar a mensagem de processamento antes de iniciar uma outra mensagem
    		interfaceInicial.desativarMensagem();

    		interfaceMensagem = new InterfaceMensagem();
    		interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
    		interfaceMensagem.setDescricaoMensagem(consEx.getMessage());
    		controladorInterfaceComandos.ativarMensagem(interfaceMensagem);

    	} catch (ErroSistemicoException errSis) {
    		// Ã© preciso limpar a mensagem de processamento antes de iniciar uma outra mensagem
    		interfaceInicial.desativarMensagem();

    		interfaceMensagem = new InterfaceMensagem();
    		interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
    		interfaceMensagem.setDescricaoMensagem(errSis.getMessage());
    		controladorInterfaceComandos.ativarMensagem(interfaceMensagem);
    	} finally {

    		interfaceInicial.desativarMensagem();
    		// habilita todas as funcoes
    		habilitarTodasFuncoes();
    	}

    }//GEN-LAST:event_cmdConsolidarPlanoActionPerformed

    private void verificarPossibilidadeDeRemocaoDePlanos() throws RemocaoDePlanosNaoPermitidaException {
        controladorInterfaceComandos.verificarPossibilidadeDeRemocaoDePlanos();
    }

    private void cmdPesquisarDadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPesquisarDadosActionPerformed
        try {
            InterfacePesquisa interfacePesquisa = new InterfacePesquisa(this.interfaceInicial, true);
            interfacePesquisa.setControladorInterfaceComandos(controladorInterfaceComandos);
            interfacePesquisa.carregarFiltroPesquisarCampanha();
            interfacePesquisa.carregarFiltroPesquisarMaquinas();
            interfacePesquisa.carregarFiltroPesquisaNavios();
            interfacePesquisa.carregarFiltroPesquisarManutencaoMaquina();
            interfacePesquisa.setVisible(true);
        } catch (ErroSistemicoException errEx) {
        	interfaceInicial.desativarMensagem();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(errEx.getMessage());
            controladorInterfaceComandos.ativarMensagem(interfaceMensagem);
        } catch (Exception ex) {
        	interfaceInicial.desativarMensagem();
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            controladorInterfaceComandos.ativarMensagem(interfaceMensagem);
        }
    }//GEN-LAST:event_cmdPesquisarDadosActionPerformed

    private void jbCarregarPlanoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCarregarPlanoActionPerformed
    try {
    	verificarPossibilidadeCarregarPlanoUsuario();
    	desabilitarTodasFuncoes();
    	final InterfaceMensagem interfaceMensagemProcessamento = new InterfaceMensagem();
        interfaceMensagemProcessamento.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_PROCESSAMENTO);
        interfaceMensagemProcessamento.setProcessamentoAtividado(Boolean.TRUE);
        interfaceMensagemProcessamento.setDescricaoMensagem(PropertiesUtil.getMessage("mensagem.processamento.carregando.plano"));

        new Thread("Thread botao carregar plano") {
            @Override
            public void run() {
                try {
                   JLabel pergunta = new JLabel(PropertiesUtil.getMessage("mensagem.option.pane.confirma.carregamento.plano.usuario.perda.dados.planejado"));
                   pergunta.setFont(new Font("Tahoma", Font.PLAIN, 11));
                   int confirm = JOptionPane.showOptionDialog(interfaceInicial, pergunta, PropertiesUtil.getMessage("mensagem.option.pane.tipo.atencao"), JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                    if (confirm == JOptionPane.YES_OPTION) {
                        interfaceInicial.ativarMensagem(interfaceMensagemProcessamento);
                        //salva o plano oficial atual para que possa ser carregado um plano de usuario atualizado caso necessario
                        controladorInterfaceComandos.consolidarPlano();
                        //carrega plano de usario
                        controladorInterfaceComandos.buscarPlanoEmpilhamentoRecuperacaoDoUsuario(InterfaceInicial.getUsuarioLogado().getId());
                        // limpa a mensagem de processamento
                        interfaceInicial.desativarMensagem();
                        //vai para ultima situacao de patio
                        controladorInterfaceComandos.getInterfaceInicial().exibirUltimaSituacaoPatio();

                        //aprensentando no titulo da janela que o plano apresentado passou a ser o do usuario
                        interfaceInicial.setTitle(PropertiesUtil.getMessage("titulo.interfaceinicial.mensagem.mespatio") + " - " + PropertiesUtil.getMessage("titulo.plano.usuario"));
                    }

                    if (interfaceInicial.getModoDeOperacao().equals(ModoDeOperacaoEnum.EDITAR)) {
                        // limpa o controlador de undo e redo do modo editar
                        interfaceInicial.getInterfaceDSP().getControladorDSP().getControladorAtualizacaoEdicao().limpaControlador();
                    }

                    

                } catch (ErroSistemicoException errSis) {
                	interfaceInicial.desativarMensagem();
                    interfaceMensagem = new InterfaceMensagem();
                    interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
                    interfaceMensagem.setDescricaoMensagem(errSis.getMessage());
                    controladorInterfaceComandos.ativarMensagem(interfaceMensagem);
                } catch (PlanosOficiaisNaoLocalizadosException e) {
                	interfaceInicial.desativarMensagem();
                	interfaceMensagem = new InterfaceMensagem();
                    interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
                    interfaceMensagem.setDescricaoMensagem(e.getMessage());
                    controladorInterfaceComandos.ativarMensagem(interfaceMensagem);
				} finally {
					interfaceInicial.desativarMensagem();
                    // habilita todas as funcoes
                    habilitarTodasFuncoes();
                }
            }
        }.start();
      } catch (CarregarOficialNaoNecessariaException consEx) {
    	  interfaceInicial.desativarMensagem();
          interfaceMensagem = new InterfaceMensagem();
          interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
          interfaceMensagem.setDescricaoMensagem(consEx.getMessage());
          controladorInterfaceComandos.ativarMensagem(interfaceMensagem);
      } catch (Exception consEx) {
    	  interfaceInicial.desativarMensagem();
        interfaceMensagem = new InterfaceMensagem();
        interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
        interfaceMensagem.setDescricaoMensagem(consEx.getMessage());
        controladorInterfaceComandos.ativarMensagem(interfaceMensagem);
      }

    }//GEN-LAST:event_jbCarregarPlanoActionPerformed

    private void cmdLimparSelecaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdLimparSelecaoActionPerformed
    	interfaceInicial.setModoDeOperacao(ModoDeOperacaoEnum.OPERACAONULA);
        interfaceInicial.getControladorInterfaceInicial().removeSelecoes();
    }//GEN-LAST:event_cmdLimparSelecaoActionPerformed

    private void cmdRemoverSituacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRemoverSituacaoActionPerformed

        try {
            //verifica qual o nivel de permissao do usuario logado
            verificaPermissaoUsuarioApagarPlano();

            //verificarPossibilidadeDeRemocaoDePlanos();
            desabilitarTodasFuncoes();

            final InterfaceMensagem interfaceMensagemProcessamento = new InterfaceMensagem();
            interfaceMensagemProcessamento.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_PROCESSAMENTO);
            interfaceMensagemProcessamento.setProcessamentoAtividado(Boolean.TRUE);
            interfaceMensagemProcessamento.setDescricaoMensagem(PropertiesUtil.buscarPropriedade("mensagem.processamento.apagar.plano"));

/*            new Thread("Thread botao Apagar situacao") {
                @Override
                public void run() { */
            try {

                        Integer indiceDaSituacaoDePatioExibida = interfaceInicial.getIndiceSituacaoPatioSelecionada();
                        SituacaoPatio situacaoPatioInicialExclusao;
                        if (indiceDaSituacaoDePatioExibida == controladorInterfaceComandos.getInterfaceInicial().getPlanejamento().obterSituacoesDePatio().size())
                        {
                           situacaoPatioInicialExclusao = new LinkedList<SituacaoPatio>(controladorInterfaceComandos.getInterfaceInicial().getPlanejamento().obterSituacoesDePatio()).getLast();
                        }
                        else
                        {
                           situacaoPatioInicialExclusao = controladorInterfaceComandos.getInterfaceInicial().getPlanejamento().obterSituacoesDePatio().get(indiceDaSituacaoDePatioExibida);
                        }
                        
                        JLabel pergunta = new JLabel("<html><body><center>" + 
                                "TODAS AS SITUAÇÕES DE PÁTIO A PARTIR DE " + DSSStockyardTimeUtil.formatarData(situacaoPatioInicialExclusao.getDtInicio(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"))+
                                "<br> SERÃO EXCLUÍDAS. CONFIRMA EXCLUSÃO ?" + "</center></body></html>");
                        pergunta.setFont(new Font("Tahoma", Font.BOLD, 18));
                        pergunta.setForeground(Color.RED);

                        int confirm = JOptionPane.showOptionDialog(interfaceInicial, pergunta, PropertiesUtil.getMessage("mensagem.option.pane.tipo.atencao"), JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                        if (confirm == JOptionPane.YES_OPTION) {
                            interfaceInicial.ativarMensagem(interfaceMensagemProcessamento);
                            controladorInterfaceComandos.apagarSituacaoPatio(indiceDaSituacaoDePatioExibida);
                            // limpando a mensagem de processamento
                            interfaceInicial.desativarMensagem();                            
              
                        }

                  
                            // limpa o controlador de undo e redo do modo editar
                            interfaceInicial.getInterfaceDSP().getControladorDSP().getControladorAtualizacaoEdicao().limpaControlador();
                            //SituacaoPatioDAO dao = new SituacaoPatioDAO();
                            //dao.deletarPilhas();                            
                            interfaceInicial.atualizarDSP();
                          

//                        habilitaFuncoes();

                    } catch (ErroSistemicoException errSis) {
                    	//eh preciso desativar a mensagem antes de iniciar uma outra 
                    	interfaceInicial.desativarMensagem();
                    	
                        interfaceMensagem = new InterfaceMensagem();
                        interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
                        interfaceMensagem.setDescricaoMensagem(errSis.getMessage());
                        controladorInterfaceComandos.ativarMensagem(interfaceMensagem);
                    }
                    
                    finally {
                    	//eh preciso desativar a mensagem antes de iniciar uma outra 
                    	interfaceInicial.desativarMensagem();
                        habilitarTodasFuncoes();
                    }
//                }
//            }.start();
        } catch (PermissaoDeUsuarioException ex) {
        	//eh preciso desativar a mensagem antes de iniciar uma outra 
        	interfaceInicial.desativarMensagem();
        	
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            interfaceMensagem.setDescricaoMensagem(ex.getMessage());
            controladorInterfaceComandos.ativarMensagem(interfaceMensagem);
        } /*catch (RemocaoDePlanosNaoPermitidaException removeEx) {
        	//eh preciso desativar a mensagem antes de iniciar uma outra 
        	interfaceInicial.desativarMensagem();
        	
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(removeEx.getMessage());
            controladorInterfaceComandos.ativarMensagem(interfaceMensagem);
        }*/
    }//GEN-LAST:event_cmdRemoverSituacaoActionPerformed

    /** Metodo que realiza a finalizacao de uma determinada edicao no sistema */
    public void finalizarEdicoes() {

        desabilitarTodasFuncoes();

        // limpa as visualizacao da interface blendagem
        interfaceInicial.getControladorInterfaceInicial().limparBlendagem();
        interfaceInicial.getInterfaceBlendagem().limparVisualizacao();

        //retorna o statusEdicao da SituacaoDePatio e FilaDeNavios para FALSE
        interfaceInicial.getSituacaoPatioExibida().setStatusEdicao(Boolean.FALSE);
        
        // Anula a operacao ap?s a conclusao da mesma.
        interfaceInicial.setModoDeOperacao(ModoDeOperacaoEnum.OPERACAONULA);

        // ativando a necessidade da consolidacao
        interfaceInicial.getControladorInterfaceInicial().getPlanejamento().ativarNecessidadeDeConsolidacao();

        // atualiza a situacao de patio exibida
        //interfaceInicial.montaPatio();
        //foi substituido o metodo montaPatio pelo montaInterfaceDSP 
        interfaceInicial.montaInterfaceDSP();
        interfaceInicial.getControladorInterfaceInicial().removeSelecoes();
        interfaceInicial.getControladorInterfaceInicial().exibirUltimaSituacaoPatio();
        // Operacao dos botoes
        habilitarTodasFuncoes();
//        habilitaFuncoes();
    }


    private void jButtonRealizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRealizarActionPerformed

        desabilitarTodasFuncoes();

        new Thread("Thread botao Confimar") {
            @Override
            public void run() {
                try {
                    //Se estiver no modo recuperacao ...
                    if (interfaceInicial.getModoDeOperacao().equals(ModoDeOperacaoEnum.RECUPERAR)) {
                        Carga carga = null;
                        //... carregamos a carga, que n?o pode ser null! Tem que haver carga para recuperar. Mas, calma! Essa verifica??o s? ser? feita em InterfaceDadosEmpilhamentoERecuperacao.solicitaDadosRecuperacao()...
                        if (interfaceInicial.getInterfaceBlendagem().getBlendagemVisualizada() != null) {
                            if (interfaceInicial.getInterfaceBlendagem().getBlendagemVisualizada().getCargaSelecionada() != null) {
                                carga = interfaceInicial.getInterfaceBlendagem().getBlendagemVisualizada().getCargaSelecionada();
                            }
                        }
                        //... e ordenamos a recupera??o, informando a carga.
                        controladorInterfaceComandos.recuperar(carga);

                        //verifica se eh necessario gravar um log desta operacao
                        interfaceInicial.verificaGeracaoDeLogBlend(carga);

                    //Caso seja empilhamento...
                    } else if (interfaceInicial.getModoDeOperacao().equals(ModoDeOperacaoEnum.EMPILHAR)) {
                        Carga carga = null;
                        //... carregamos a carga, que PODE ser null! n?o ? necess?rio carga para empilhar. Se tiver carga, no entanto, poderemos setar o cliente da pilha quando ela nascer!!
                        if (interfaceInicial.getInterfaceBlendagem().getBlendagemVisualizada() != null && interfaceInicial.getInterfaceBlendagem().getBlendagemVisualizada().getCargaSelecionada() != null) {
                            carga = interfaceInicial.getInterfaceBlendagem().getBlendagemVisualizada().getCargaSelecionada();
                        }
                        //... assim, simplesmente ordenamos o empilhamento!
                        controladorInterfaceComandos.empilhar(carga);
                    }

                    // limpa a mensagem de processamento
                    interfaceInicial.desativarMensagem();

                    // limpa as visualizacao da interface blendagem
                    interfaceInicial.getControladorInterfaceInicial().limparBlendagem();
                    interfaceInicial.getInterfaceBlendagem().limparVisualizacao();

                    //retorna o statusEdicao da SituacaoDePatio e FilaDeNavios para FALSE
                    interfaceInicial.getSituacaoPatioExibida().setStatusEdicao(Boolean.FALSE);
                    FilaDeNavios filaDeNavios = interfaceInicial.getControladorInterfaceFilaNavios().getInterfaceFilaDeNavios().getFilaDeNaviosVisualizada();
                    if(filaDeNavios!=null)
                       //filaDeNavios.setStatusEdicao(Boolean.FALSE);

                    // Anula a operacao ap?s a conclusao da mesma.
                    interfaceInicial.setModoDeOperacao(ModoDeOperacaoEnum.OPERACAONULA);

                    // atualiza a situacao de patio exibida
                    //interfaceInicial.montaPatio();
                    interfaceInicial.getControladorInterfaceInicial().removeSelecoes();

                    interfaceInicial.getControladorInterfaceInicial().exibirUltimaSituacaoPatio();

                    // Operacao dos botoes
                    habilitaFuncoes();

                } catch (OperacaoCanceladaPeloUsuarioException operCancelEx) {
                    cancelarModoOperacao();
                    controladorInterfaceComandos.getInterfaceInicial().desativarMensagemProcessamento();
                    interfaceMensagem = new InterfaceMensagem();
                    interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_INFORMACAO);
                    interfaceMensagem.setDescricaoMensagem(operCancelEx.getMessage());
                    controladorInterfaceComandos.ativarMensagem(interfaceMensagem);
                } catch (ValidacaoObjetosOperacaoException exOper) {
                	controladorInterfaceComandos.getInterfaceInicial().desativarMensagemProcessamento();
                    interfaceMensagem = new InterfaceMensagem();
                    interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
                    interfaceMensagem.setDescricaoMensagem(exOper.getMessage());
                    controladorInterfaceComandos.ativarMensagem(interfaceMensagem);
                    interfaceInicial.getControladorInterfaceInicial().removeSelecoes();
                } catch (PilhaParaRecuperacaoSemMaquinaCapazException e) {
                	controladorInterfaceComandos.getInterfaceInicial().desativarMensagemProcessamento();
                    interfaceMensagem = new InterfaceMensagem();
                    interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
                    interfaceMensagem.setDescricaoMensagem(e.getMessage());
                    controladorInterfaceComandos.ativarMensagem(interfaceMensagem);
                } catch (BlendagemInvalidaException ex) {
                	controladorInterfaceComandos.getInterfaceInicial().desativarMensagemProcessamento();
                    interfaceMensagem = new InterfaceMensagem();
                    interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
                    interfaceMensagem.setDescricaoMensagem(ex.getMessage());
                    controladorInterfaceComandos.ativarMensagem(interfaceMensagem);
                } catch (CampanhaIncompativelException ex) {
                	controladorInterfaceComandos.getInterfaceInicial().desativarMensagemProcessamento();
                    interfaceMensagem = new InterfaceMensagem();
                    interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
                    interfaceMensagem.setDescricaoMensagem(ex.getMessage());
                } catch (TempoInsuficienteException ex) {
                	controladorInterfaceComandos.getInterfaceInicial().desativarMensagemProcessamento();
                    interfaceMensagem = new InterfaceMensagem();
                    interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
                    interfaceMensagem.setDescricaoMensagem(ex.getMessage());
                    controladorInterfaceComandos.ativarMensagem(interfaceMensagem);
                } catch (BalizasSelecionadasException ex) {
                    cancelarModoOperacao();
                    interfaceInicial.desativarMensagem();
                    interfaceInicial.getControladorInterfaceInicial().removeSelecoes();
                    // habilita todas as funcoes
                    habilitarTodasFuncoes();
                    // Operacao dos botoes
                    habilitaFuncoes();
                    interfaceMensagem = new InterfaceMensagem();
                    interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
                    interfaceMensagem.setDescricaoMensagem(ex.getMessage());
                    controladorInterfaceComandos.ativarMensagem(interfaceMensagem);
                } catch (SelecaoObjetoModoEmpilhamentoException e) {
                	controladorInterfaceComandos.getInterfaceInicial().desativarMensagemProcessamento();
                    interfaceMensagem = new InterfaceMensagem();
                    interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
                    interfaceMensagem.setDescricaoMensagem(e.getMessage());
                    controladorInterfaceComandos.ativarMensagem(interfaceMensagem);
                } catch (SelecaoObjetoModoRecuperacaoException e) {
                	controladorInterfaceComandos.getInterfaceInicial().desativarMensagemProcessamento();
                    interfaceMensagem = new InterfaceMensagem();
                    interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
                    interfaceMensagem.setDescricaoMensagem(e.getMessage());
                    controladorInterfaceComandos.ativarMensagem(interfaceMensagem);
                } finally {
                    controladorInterfaceComandos.getInterfaceInicial().desativarMensagemProcessamento();
                    // habilita todas as funcoes
                    habilitarTodasFuncoes();
                }
            }
        }.start();

    }//GEN-LAST:event_jButtonRealizarActionPerformed

    private void jbAtualizarMESActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAtualizarMESActionPerformed
       
       final InterfaceMensagem interfaceMensagemProcessamento = new InterfaceMensagem();
       interfaceMensagemProcessamento.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_PROCESSAMENTO);
       interfaceMensagemProcessamento.setProcessamentoAtividado(Boolean.TRUE);
       interfaceMensagemProcessamento.setDescricaoMensagem(PropertiesUtil.buscarPropriedade("mensagem.processamento.carregar.atualizacao.mes"));
       
           desabilitarTodasFuncoes();
           
            new Thread("Thread botao atualizar com MES") {

                @Override
                public void run() {
                	boolean desabilitouCRM = false;
                    try {
                    	desabilitaIconeAtualizacaoMES();
                    	if (jbAtualizarCRM.isEnabled()) {
                    		desabilitouCRM = true;
                    		desabilitaIconeAtualizacaoCRM();
                    	}
                        getInterfaceInicial().setExecutandoIntegracao(Boolean.TRUE);
                    	controladorInterfaceComandos.ativarMensagem(interfaceMensagemProcessamento);

                        // o plano de empilhamento e recuperacao do usuario que esta em memoria
                        PlanoEmpilhamentoRecuperacao planoEmpilhamentoRecuperacao = controladorInterfaceComandos.getInterfaceInicial().getPlanejamento().getControladorDePlano().getPlanoEmpilhamentoRecuperacao();

                        // executando a atualizacao do plano do usuario com as informacoes de bi-horarias do MES
                        planoEmpilhamentoRecuperacao = interfaceInicial.getControladorInterfaceInicial().executarIntegracaoSistemaMES(new Date(), planoEmpilhamentoRecuperacao);
                        
                        //modifica a coluna de verificacao da integracaoParametros e salva
                        interfaceInicial.atualizaDadosIntegracaoParametrosMES();
                        
                        //apos atualizacao dos dados desabilitar o icone novamente
                        desabilitaIconeAtualizacaoMES();

                        // atualiza no controlador de plano o plano de empilhamento e recuperacao do usuario que acabou de ser atualizado pelo MES
                        controladorInterfaceComandos.getInterfaceInicial().getPlanejamento().getControladorDePlano().setPlanoEmpilhamentoRecuperacao(planoEmpilhamentoRecuperacao);

                        controladorInterfaceComandos.getInterfaceInicial().getPlanejamento().getControladorDePlano().consolidarPlano();
                        
                        // atualiza o patio apos a consolidacao
                        interfaceInicial.montaInterfaceDSP();
                       // interfaceInicial.montaPatio();

                        //seta planejamento indicando nÃ£o necessidade de consolidar
                        interfaceInicial.getControladorInterfaceInicial().getPlanejamento().ativarNecessidadeDeConsolidacao();
                        integracaoMesAtivada = Boolean.TRUE;
                        interfaceInicial.getControladorInterfaceInicial().getPlanejamento().setConsolidacaoRealizada(Boolean.FALSE);
                        getInterfaceInicial().setExecutandoIntegracao(Boolean.FALSE);
                        
                    } catch (ErroSistemicoException errSis) {
                        controladorInterfaceComandos.getInterfaceInicial().desativarMensagemProcessamento();
                        interfaceMensagem = new InterfaceMensagem();
                        interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
                        interfaceMensagem.setDescricaoMensagem(errSis.getMessage());
                        controladorInterfaceComandos.ativarMensagem(interfaceMensagem);
                        getInterfaceInicial().setExecutandoIntegracao(Boolean.FALSE);
                    } finally {
                        // habilita todas as funcoes
                    	habilitarTodasFuncoes();                    	
                    	controladorInterfaceComandos.getInterfaceInicial().desativarMensagemProcessamento();
                    	getInterfaceInicial().setExecutandoIntegracao(Boolean.FALSE);
                    }
                }
            }.start();
           
        }
//}//GEN-LAST:event_jbAtualizarMESActionPerformed

    private void jbAtualizarCRMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAtualizarCRMActionPerformed

       final InterfaceMensagem interfaceMensagemProcessamento = new InterfaceMensagem();
       interfaceMensagemProcessamento.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_PROCESSAMENTO);
       interfaceMensagemProcessamento.setProcessamentoAtividado(Boolean.TRUE);
       interfaceMensagemProcessamento.setDescricaoMensagem(PropertiesUtil.buscarPropriedade("mensagem.processamento.carregar.atualizacao.crm"));
       
       new Thread("Thread botao atualiza com CRM")
       {
           @Override
           public void run()
           {        	   
        	   desabilitarTodasFuncoes();
        	   try
               {
            	   desabilitaIconeAtualizacaoCRM();
            	   if (jbAtualizarMES.isEnabled()) {            		   
            		   desabilitaIconeAtualizacaoMES();
            	   }
            	   
            	  getInterfaceInicial().setExecutandoIntegracao(Boolean.TRUE);
            	  controladorInterfaceComandos.ativarMensagem(interfaceMensagemProcessamento);
                  Date dataExcucao = new Date();
                  Date dataSituacao = controladorInterfaceComandos.getInterfaceInicial().getPlanejamento().getControladorDePlano().obterSituacaoPatioFinal().getDtInicio();
                  PlanoEmpilhamentoRecuperacao planoEmpilhamentoRecuperacao = interfaceInicial.getControladorInterfaceInicial().executarIntegracaoSistemaCRM(dataExcucao, interfaceInicial.getControladorInterfaceInicial().getPlanejamento().getControladorDePlano().getPlanoEmpilhamentoRecuperacao(),dataSituacao);
                  if (planoEmpilhamentoRecuperacao != null) { 
                      // atualiza no controlador de plano o plano de empilhamento e recuperacao do usuario que acabou de ser atualizado pelo CRM
                      interfaceInicial.getControladorInterfaceInicial().getPlanejamento().getControladorDePlano().setPlanoEmpilhamentoRecuperacao(planoEmpilhamentoRecuperacao);
    
                      // atualiza o patio apos a consolidacao
                      interfaceInicial.montaInterfaceDSP();;
                      controladorInterfaceComandos.getInterfaceInicial().exibirUltimaSituacaoPatio();
    
                      //modifica a coluna de verificacao da integracaoParametros e salva
                      interfaceInicial.atualizaDadosIntegracaoParametrosCRM();
                      //apos atualizacao dos dados desabilitar o icone novamente
                      desabilitaIconeAtualizacaoCRM();

                      //seta planejamento indicando nÃ£o necessidade de consolidar
                      interfaceInicial.getControladorInterfaceInicial().getPlanejamento().ativarNecessidadeDeConsolidacao();                      
                  }   
                  getInterfaceInicial().setExecutandoIntegracao(Boolean.FALSE);
                                                    
               } catch (ErroSistemicoException errSis) {
                  controladorInterfaceComandos.getInterfaceInicial().desativarMensagemProcessamento();
                  interfaceMensagem = new InterfaceMensagem();
                  interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
                  interfaceMensagem.setDescricaoMensagem(errSis.getMessage());
                  controladorInterfaceComandos.ativarMensagem(interfaceMensagem);
                  getInterfaceInicial().setExecutandoIntegracao(Boolean.FALSE);
               } finally {
                   // habilita todas as funcoes
                   habilitarTodasFuncoes();                   
            	   controladorInterfaceComandos.getInterfaceInicial().desativarMensagemProcessamento();
            	   getInterfaceInicial().setExecutandoIntegracao(Boolean.FALSE);
              }
          }
       }.start();
       

    }//GEN-LAST:event_jbAtualizarCRMActionPerformed

    private void cmdSalvarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmdSalvarActionPerformed
    {//GEN-HEADEREND:event_cmdSalvarActionPerformed
/*    	try {
            /**TODO ADICIONAR VALIDAR PERFIL OFICIALIZAR*/
    		//verificarPossibilidadeDeConsolidacao();
/*            desabilitarTodasFuncoes();

            final InterfaceMensagem interfaceMensagemProcessamento = new InterfaceMensagem();
            interfaceMensagemProcessamento.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_PROCESSAMENTO);
            interfaceMensagemProcessamento.setProcessamentoAtividado(Boolean.TRUE);
            interfaceMensagemProcessamento.setDescricaoMensagem(PropertiesUtil.getMessage("mensagem.processamento.oficializar.plano"));

            new Thread("Thread botao salvar") {
                @Override
                public void run() {
                    try {
                        if (interfaceInicial.getSituacaoPatioExibida().getPlanoEmpilhamento().getEhOficial())
                        {
                            JLabel pergunta = new JLabel(PropertiesUtil.getMessage("mensagem.option.pane.confirma.oficializar.plano"));
                            pergunta.setFont(new Font("Tahoma", Font.PLAIN, 11));
                            int confirm = JOptionPane.showOptionDialog(
                                    interfaceInicial,
                                    pergunta,
                                    PropertiesUtil.getMessage("mensagem.option.pane.tipo.atencao"),
                                    JOptionPane.YES_OPTION,
                                    JOptionPane.INFORMATION_MESSAGE,
                                    null,
                                    null,
                                    null);

                            if (confirm == JOptionPane.YES_OPTION) {
                                interfaceInicial.ativarMensagem(interfaceMensagemProcessamento);
                                salvaPlanoDeUsuario();
                            }
                        }else{
                            interfaceInicial.ativarMensagem(interfaceMensagemProcessamento);
                            salvaPlanoDeUsuario();
                        }

                        if (interfaceInicial.getModoDeOperacao().equals(ModoDeOperacaoEnum.EDITAR)) {
                            // limpa o controlador de undo e redo do modo editar
                            interfaceInicial.getInterfaceDSP().getControladorDSP().getControladorAtualizacaoEdicao().limpaControlador();
                        }
                    } catch (PlanosOficiaisNaoLocalizadosException ex) {
                        // limpa a mensagem de processamento
                        interfaceInicial.desativarMensagem();
                        
                        interfaceMensagem = new InterfaceMensagem();
                        interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
                        interfaceMensagem.setDescricaoMensagem(ex.getMessage());
                        controladorInterfaceComandos.ativarMensagem(interfaceMensagem);
                    } catch (ErroSistemicoException errSis) {
                        // limpa a mensagem de processamento
                        interfaceInicial.desativarMensagem();
                        
                        interfaceMensagem = new InterfaceMensagem();
                        interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
                        interfaceMensagem.setDescricaoMensagem(errSis.getMessage());
                        controladorInterfaceComandos.ativarMensagem(interfaceMensagem);
                    } finally {
                        // limpa a mensagem de processamento
                        interfaceInicial.desativarMensagem();
                        // habilita todas as funcoes
                        habilitarTodasFuncoes();
                    }
                }
            }.start();
        } catch (Exception consEx) {
            // limpa a mensagem de processamento
            interfaceInicial.desativarMensagem();
            
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
            interfaceMensagem.setDescricaoMensagem(consEx.getMessage());
            controladorInterfaceComandos.ativarMensagem(interfaceMensagem);
        }
*/
    }//GEN-LAST:event_cmdSalvarActionPerformed

    private void cmdEstoqueActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmdEstoqueActionPerformed
    {//GEN-HEADEREND:event_cmdEstoqueActionPerformed
      try
       {
          InformacaoDiferencaEstoqueGUI informacaoDiferencaEstoque = new InformacaoDiferencaEstoqueGUI(interfaceInicial, true, interfaceInicial.getInterfaceDSP().getControladorDSP());
          informacaoDiferencaEstoque.setLocationRelativeTo(null);
          informacaoDiferencaEstoque.setVisible(true);
       }
       catch (ErroSistemicoException errSis)
       {
          controladorInterfaceComandos.getInterfaceInicial().desativarMensagemProcessamento();
          interfaceMensagem = new InterfaceMensagem();
          interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
          interfaceMensagem.setDescricaoMensagem(errSis.getMessage());
          controladorInterfaceComandos.ativarMensagem(interfaceMensagem);
       }
    }//GEN-LAST:event_cmdEstoqueActionPerformed

    /**
     * habilita botao(icone) de atualizacao
     */
    public void habilitarIconeAtualizacaoMES(){
        //permissao usuario
        if (!controladorInterfaceComandos.verificaPermissaoAtualizacaoProducao()) {
            jbAtualizarMES.setEnabled(Boolean.TRUE);
            integracaoMesAtivada = Boolean.TRUE;

            //muda tipo de botao para piscante
            jbAtualizarMES.setIcon(new ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens") + "/sincronizar-d.gif"));

            interfaceInicial.desativarMensagem();
            //aviso em destaque na barra de mensagens
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            interfaceMensagem.setDescricaoMensagem(PropertiesUtil.buscarPropriedade("mensagem.aviso.atualizacao.mes.disponivel"));
            controladorInterfaceComandos.ativarMensagem(interfaceMensagem);
        }
    }

    
    /**
     * habilita botao(icone) de atualizacao
     */
    public void habilitarIconeAtualizacaoMESSemMsg(){
        //permissao usuario
        if (!controladorInterfaceComandos.verificaPermissaoAtualizacaoProducao()) {
            jbAtualizarMES.setEnabled(Boolean.TRUE);
            integracaoMesAtivada = Boolean.TRUE;

            //muda tipo de botao para piscante
            jbAtualizarMES.setIcon(new ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens") + "/sincronizar-a.png"));
           
        }
    }

    
    /*
     * habilita botao(icone) de atualizacao
     */
    public void habilitarIconeAtualizacaoCRM()
    {
        //permissao usuario
        if (!controladorInterfaceComandos.verificaPermissaoAtualizacaoProducao()) {
            jbAtualizarCRM.setEnabled(Boolean.TRUE);

            //muda tipo de botao para piscante
            jbAtualizarCRM.setIcon(new ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens") + "/sincronizar-d.gif"));

            interfaceInicial.desativarMensagem();
            //aviso em destaque na barra de mensagens
            interfaceMensagem = new InterfaceMensagem();
            interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
            interfaceMensagem.setDescricaoMensagem(PropertiesUtil.buscarPropriedade("mensagem.aviso.atualizacao.crm.disponivel"));
            controladorInterfaceComandos.ativarMensagem(interfaceMensagem);
        }
    }

    
    /**
     * habilita botao(icone) de atualizacao
     */
    public void habilitarIconeAtualizacaoCRMSemMsg()
    {
        //permissao usuario
        if (!controladorInterfaceComandos.verificaPermissaoAtualizacaoProducao()) {
            jbAtualizarCRM.setEnabled(Boolean.TRUE);

            //muda tipo de botao para piscante
            jbAtualizarCRM.setIcon(new ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens") + "/sincronizar-a.png"));
           
        }
    }
    
    private void desabilitaIconeAtualizacaoMES()
    {
        //volta o botao para o tipo normal e seta para desabilitado
        jbAtualizarMES.setIcon(new ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens") + "/sincronizar-a.png"));
        jbAtualizarMES.setEnabled(Boolean.FALSE);
    }

    private void desabilitaIconeAtualizacaoCRM()
    {
        //volta o botao para o tipo normal e seta para desabilitado
        jbAtualizarCRM.setIcon(new ImageIcon(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens") + "/sincronizar-a.png"));
        jbAtualizarCRM.setEnabled(Boolean.FALSE);
    }

    /**
     * Cancela a operacao e remove as selecoes feita pelo usuario na
     * tela do DSP.
     */
    public void cancelarModoOperacao() {

        //deseleciona todos os itens que n?o participam da blendagem
        interfaceInicial.getControladorInterfaceInicial().removeSelecoesQueNaoFacamParteDaBlendagem();

        // Se estiver no modo de operacao de edicao ele ira realizar undo de todas as alteracoes e limpar o controlador de undo e redo.
        if (interfaceInicial.getModoDeOperacao().equals(ModoDeOperacaoEnum.EDITAR)) {
            interfaceInicial.getInterfaceDSP().getControladorDSP().getControladorAtualizacaoEdicao().undoAll();
            interfaceInicial.getInterfaceDSP().getControladorDSP().getControladorAtualizacaoEdicao().limpaControlador();
            //ao cancelar  retornar o statusEdicao da SituacaoDePatio e FilaDeNavios para FALSE
            interfaceInicial.getSituacaoPatioExibida().setStatusEdicao(Boolean.FALSE);
            FilaDeNavios filaDeNavios = interfaceInicial.getControladorInterfaceFilaNavios().getInterfaceFilaDeNavios().getFilaDeNaviosVisualizada();
            if(filaDeNavios!=null)
              // filaDeNavios.setStatusEdicao(Boolean.FALSE);
            // Retorna os dados editados relacionados a fila de navios, pois o  ControladorInterfaceFilaNavios possui outra instancia do
            // ControladorAtualizacaoEdicao
            interfaceInicial.getControladorInterfaceFilaNavios().getControladorAtualizacaoEdicao().undoAll();
            interfaceInicial.getControladorInterfaceFilaNavios().getControladorAtualizacaoEdicao().limpaControlador();
        }

        // Operacao dos botoes
        habilitaFuncoes();

        // Seta o modo de operacao da interface principal
        interfaceInicial.setModoDeOperacao(ModoDeOperacaoEnum.OPERACAONULA);
        interfaceInicial.getControladorInterfaceInicial().removeSelecoes();
    }

    /**
     * Metodo que desabilita todas as funcionalidades do sistema como os
     * botoes de comando e opcoes de edicao quando o sistema estiver processamento
     * alguma operacao.
     */
    public void desabilitarTodasFuncoes() {
        // desabilitando botoes de comando
        cmdGestaoRelatorio.setEnabled(false);
        cmdConsolidarPlano.setEnabled(false);
        cmdLimparSelecao.setEnabled(false);
        cmdPesquisarDados.setEnabled(false);
        cmdRemoverSituacao.setEnabled(false);
        jbCarregarPlano.setEnabled(false);
        cmdSalvar.setEnabled(false);
//        jbAtualizarMES.setEnabled(false);
//        jbAtualizarCRM.setEnabled(false);
        // desabilitando o slider de navegacao
        controladorInterfaceComandos.getInterfaceInicial().getInterfaceInicial().getSliderNavegacao().setEnabled(false);

        // desabilitando os menus de edicao dos objetos
        controladorInterfaceComandos.desabilitarMenusDeEdicao();
    }

    /**
     * Metodo que desabilita todas as funcionalidades do sistema como os
     * botoes de comando e opcoes de edicao quando o sistema estiver processamento
     * alguma operacao.
     */
    public void habilitarTodasFuncoes() {
        // desabilitando botoes de comando
        cmdGestaoRelatorio.setEnabled(true);
        cmdLimparSelecao.setEnabled(true);
        cmdPesquisarDados.setEnabled(true);
        cmdRemoverSituacao.setEnabled(true);
        jbCarregarPlano.setEnabled(true);
        cmdConsolidarPlano.setEnabled(true);
 
        // desabilitando o slider de navegacao
        controladorInterfaceComandos.getInterfaceInicial().getInterfaceInicial().getSliderNavegacao().setEnabled(true);

        // desabilitando os menus de edicao dos objetos
        controladorInterfaceComandos.habilitarMenusDeEdicao();
        habilitaFuncoes();
        habilitarIconeAtualizacaoMESSemMsg();
        habilitarIconeAtualizacaoCRMSemMsg();
    }

    private void verificaPermissaoUsuarioApagarPlano() throws PermissaoDeUsuarioException{
         controladorInterfaceComandos.verificarPermissaoUsuarioApagarPlano();
    }

    private void salvaPlanoDeUsuario() throws ErroSistemicoException, PlanosOficiaisNaoLocalizadosException
    {
        controladorInterfaceComandos.oficializarPlano();
        if (interfaceInicial.getSituacaoPatioExibida().getPlanoEmpilhamento().getEhOficial()) {
            //carrega o plano do usuario salvo na interface
            controladorInterfaceComandos.buscarPlanoEmpilhamentoRecuperacaoDoUsuario(InterfaceInicial.getUsuarioLogado().getId());
        }
        //vai para ultima situacao de patio
        controladorInterfaceComandos.getInterfaceInicial().exibirUltimaSituacaoPatio();
        //aprensentando no titulo da janela que o plano apresentado passou a ser o do usuario
        interfaceInicial.setTitle(PropertiesUtil.getMessage("titulo.interfaceinicial.mensagem.mespatio") + " - " + PropertiesUtil.getMessage("titulo.plano.usuario"));
        //desdabilitando funcoes
        cmdConsolidarPlano.setEnabled(false);
        jbCarregarPlano.setEnabled(false);
    }

   private void bloqueiaAcaoUsuarioLeitura()
   {
       if (controladorInterfaceComandos.verificaPermissaoAtualizacaoProducao()) {
           cmdConsolidarPlano.setEnabled(false);
           cmdRemoverSituacao.setEnabled(false);
           jbAtualizarMES.setEnabled(false);
           jbAtualizarCRM.setEnabled(false);
       }
   }

    public boolean isIntegracaoMesAtivada() {
        return integracaoMesAtivada;
    }

    public void setIntegracaoMesAtivada(boolean integracaoMesAtivada) {
        this.integracaoMesAtivada = integracaoMesAtivada;
    }

   // Variables declaration - do not modify//GEN-BEGIN:variables
   /**Itens do menu de relatorio*/
   private javax.swing.JMenuItem menuItemGestaoRelatorio;
   private javax.swing.JMenuItem menuItemPlanoRecuperacao;
   private javax.swing.JMenuItem menuItemIndicadorQualidade;
   private javax.swing.JMenuItem menuItemOperacaoCarga;
   private javax.swing.JMenuItem menuItemAtendimentoCarga;
   /**Fim Itens do menu de relatorio*/
   private javax.swing.JButton cmdConsolidarPlano;
   private javax.swing.JButton cmdEstoque;
   private javax.swing.JButton cmdGestaoRelatorio;
   private javax.swing.JButton cmdLimparSelecao;
   private javax.swing.JButton cmdPesquisarDados;
   private javax.swing.JButton cmdRemoverSituacao;
   private javax.swing.JButton cmdSalvar;
   private javax.swing.JSeparator jSeparator2;
   private javax.swing.JSeparator jSeparator3;
   private javax.swing.JSeparator jSeparator4;
   private javax.swing.JSeparator jSeparator5;
   private javax.swing.JButton jbAtualizarCRM;
   private javax.swing.JButton jbAtualizarMES;
   private javax.swing.JButton jbCarregarPlano;
   private javax.swing.JLabel jlApagar;
   private javax.swing.JLabel jlCarregar;
   private javax.swing.JLabel jlConsolidar;
   private javax.swing.JLabel jlLimpar;
   private javax.swing.JLabel jlPesquisar1;
   private javax.swing.JLabel jlSincronizar;
   private javax.swing.JLabel jlSincronizarCRM;
   private javax.swing.JLabel jlSincronizarMES;
   private javax.swing.JLabel lbOficializar;
   private javax.swing.JLabel lblEstoque;
   // End of variables declaration//GEN-END:variables

}
