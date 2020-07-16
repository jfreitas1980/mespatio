package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio;

import java.awt.Color;
import java.awt.Font;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.text.MaskFormatter;

import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios.InterfaceBerco;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios.InterfaceCarga;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios.InterfaceNavio;
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDeFilaDeNavios.InterfacePier;
import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.cliente.util.numeros.DSSStockyardFuncoesNumeros;
import com.hdntec.gestao.domain.navios.entity.MetaCarga;
import com.hdntec.gestao.domain.navios.entity.MetaNavio;
import com.hdntec.gestao.domain.navios.entity.status.Carga;
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.entity.LugarEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.enums.SentidoEmpilhamentoRecuperacaoEnum;
import com.hdntec.gestao.domain.plano.enums.TipoAtividadeEnum;
import com.hdntec.gestao.domain.plano.enums.TipoMaquinaEnum;
import com.hdntec.gestao.domain.planta.comparadores.ComparadorBalizas;
import com.hdntec.gestao.domain.planta.entity.MetaBaliza;
import com.hdntec.gestao.domain.planta.entity.MetaMaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.MaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Patio;
import com.hdntec.gestao.domain.planta.entity.status.Pilha;
import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;
import com.hdntec.gestao.domain.produto.entity.Produto;
import com.hdntec.gestao.domain.produto.enums.TipoDeProdutoEnum;
import com.hdntec.gestao.domain.vo.atividades.MovimentacaoVO;
import com.hdntec.gestao.exceptions.AtividadeException;
import com.hdntec.gestao.exceptions.ValidacaoCampoException;
import com.hdntec.gestao.exceptions.ValidacaoObjetosOperacaoException;
import com.hdntec.gestao.services.controladores.impl.ControladorExecutarAtividadeMovimentacaoPilha;
import com.hdntec.gestao.util.PropertiesUtil;
import com.hdntec.gestao.util.datahora.DSSStockyardTimeUtil;

public class InterfaceMovimentacao  extends javax.swing.JDialog
{
   /** o controlador das operacoes do DSP */
   private ControladorDSP controladorDSP;

   private TipoAtividadeEnum tipoAtividade;
   /** a interface da maquina do patio selecionada */
   private InterfaceMaquinaDoPatio interfaceMaquinaDoPatio;

   /** pa-carregadeira selecionada */
   private MaquinaDoPatio maquinaDoPatioOrigem;

   /** empilhadeira escolhida */
   private MaquinaDoPatio maquinaDoPatioDestino;
   
   /** identificador de cancelamento de operacao */
   private Boolean operacaoCanceladaPeloUsuario;

   /** a atividade de movimentacao */
   private Atividade atividadeMovimentar;

   private Atividade atividadeExecutada;
   /** a pilha origem */
   private Pilha pilhaOrigem;

   /** a interface de mensagens da interface */
   private InterfaceMensagem interfaceMensagem;
   
   /** Tipo de produto movimentado */
   private TipoDeProdutoEnum tipoProduto;
   
   private Date horaSituacao;
   /** taxa da movimentacao (ton / hora) */
   private double taxaMovimentacao;

   /** Creates new form InterfaceMovimentacaoPilhaEmergencia */
   public InterfaceMovimentacao(java.awt.Frame parent, boolean modal)
   {
      super(parent, modal);
      initComponents();
   }

   public InterfaceMovimentacao(java.awt.Frame parent, boolean modal, MaquinaDoPatio maquinaDoPatio, InterfaceMaquinaDoPatio interfaceMaquinaDoPatio, ControladorDSP controladorDSP, TipoDeProdutoEnum tipoProduto, TipoAtividadeEnum tipoAtividadeEnum, Date horaSituacao)
   {
      super(parent, modal);
      this.horaSituacao = horaSituacao;
      this.tipoAtividade = tipoAtividadeEnum;
      initComponents();      
      this.controladorDSP = controladorDSP;
      this.interfaceMaquinaDoPatio = interfaceMaquinaDoPatio;
      this.maquinaDoPatioOrigem = maquinaDoPatio;
      this.operacaoCanceladaPeloUsuario = false;
      this.tipoProduto = tipoProduto;
   	  carregarInformacoesIniciais();
      desabilitaPainelMovimentacaoCarregamento();
      desabilitaPainelMovimentacaoPatio();
      rdCarregamento.setSelected(false);
      rdPatio.setSelected(false);
      cmbBalizaFinalPilhaDestino.setEnabled(false);
      cmbBalizaFinalPilhaOrigem.setEnabled(false);
      txtTaxa.setEnabled(false);
      
      if (maquinaDoPatioOrigem.getAtividade() != null && !maquinaDoPatioOrigem.getAtividade().getTipoAtividade().equals(TipoAtividadeEnum.RETORNO_PELLET_FEED) && maquinaDoPatioOrigem.getEstado().equals(EstadoMaquinaEnum.OPERACAO))
      {
         carregarInformacoesAtividade();
      }
   }
   
   /**
    * 
    */
   private void selecionarPilhaOrigem() {
	   for(int i=0; i<cmbPilhaOrigem.getItemCount();i++) {
		   Object obj = cmbPilhaOrigem.getItemAt(i);		   
		   if (obj instanceof Pilha) {
			   Pilha pilha = (Pilha)obj;
			   if (pilha.getNomePilha().equals(pilhaOrigem.getNomePilha())) {
				   cmbPilhaOrigem.setSelectedIndex(i);
				   break;
			   }
		   }
	   }
   }
   
   /**
    * 
    * @param cmbBalizaSelecionada
    * @param balizaSelecionada
    */
   private void selecionarComboBaliza(JComboBox cmbBalizaSelecionada, Baliza balizaSelecionada) {
	   for(int i=0; i<cmbBalizaSelecionada.getItemCount();i++) {
		   Object obj = cmbBalizaSelecionada.getItemAt(i);
		   if (obj instanceof Baliza) {
			   Baliza baliza = (Baliza)obj;
			   if (baliza.getMetaBaliza().equals(balizaSelecionada.getMetaBaliza())) {
				   cmbBalizaSelecionada.setSelectedIndex(i);
				   break;
			   }
		   }
	   }
   }
   
   
   private void carregarInformacoesAtividade()
   {
      atividadeExecutada = maquinaDoPatioOrigem.getAtividade();
      rdCarregamento.setEnabled(false);
      rdPatio.setEnabled(false);
      cmbPilhaOrigem.setEnabled(false);
      
      //selecionando na combo de pilha origem a pilha origem
      MetaBaliza metaBalizaOperacao = atividadeExecutada.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaDeBalizas().get(0).getMetaBaliza();
      
      Baliza balizaOperacao = metaBalizaOperacao.retornaStatusHorario(atividadeExecutada.getDtInicio());
      
      int idxGerado = metaBalizaOperacao.getListaStatus().indexOf(balizaOperacao);
      
      balizaOperacao = metaBalizaOperacao.getListaStatus().get(idxGerado-1);
      
      
      
      pilhaOrigem = balizaOperacao.retornaStatusHorario(atividadeExecutada.getDtInicio());
      cmbPilhaOrigem.addItem(pilhaOrigem);
      //cmbPilhaOrigem.
      
      //cmbPilhaOrigem.setSelectedItem(pilhaOrigem);
      carregaInformacoesPilhaOrigem();
      
      //pilhaOrigem = atividadeExecutada.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaDeBalizas().get(0).retornaStatusHorario(atividadeExecutada.getDtInicio());
      //cmbPilhaOrigem.setSelectedItem(pilhaOrigem);
      //carregaInformacoesPilhaOrigem();
      //selecionarPilhaOrigem();
      
      // carrega as informacoes da pilha de origem
//      carregaInformacoesPilhaOrigem();
      LugarEmpilhamentoRecuperacao lugarEmpRecOrigem = atividadeExecutada.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0);
      Baliza balizaInicialOrigem = lugarEmpRecOrigem.getListaDeBalizas().get(0);
      Baliza balizaFinalOrigem = lugarEmpRecOrigem.getListaDeBalizas().get(lugarEmpRecOrigem.getListaDeBalizas().size()-1);
      cmbBalizaInicialPilhaOrigem.setEnabled(false);
      cmbBalizaInicialPilhaOrigem.setSelectedItem(balizaInicialOrigem);
      selecionarComboBaliza(cmbBalizaInicialPilhaOrigem, balizaInicialOrigem);
      cmbBalizaFinalPilhaOrigem.setEnabled(true);
      cmbBalizaFinalPilhaOrigem.setSelectedItem(balizaFinalOrigem);
      selecionarComboBaliza(cmbBalizaFinalPilhaOrigem, balizaFinalOrigem);
      
      
      //txtTaxa.setText(DSSStockyardFuncoesNumeros.getQtdeFormatada(lugarEmpRecOrigem.getTaxaDeOperacaoNaPilha(),1));
      
      // verifica se a operacao é de pilha para pilha
      if (atividadeExecutada.getListaDeLugaresDeEmpilhamentoRecuperacao().get(1).getListaCargas() == null || atividadeExecutada.getListaDeLugaresDeEmpilhamentoRecuperacao().get(1).getListaCargas().isEmpty())
      {
         LugarEmpilhamentoRecuperacao lugarEmpRecDestino = atividadeExecutada.getListaDeLugaresDeEmpilhamentoRecuperacao().get(1);
         // carrega as informacoes da pilha de destino
         carregarInformacoesDestinoPatio();
         
         //txtTaxa.setText(DSSStockyardFuncoesNumeros.getQtdeFormatada(lugarEmpRecDestino.getTaxaDeOperacaoNaPilha(),1));
         rdPatio.setSelected(true);
         txtNomePilhaExistente.setText(lugarEmpRecDestino.getNomeDoLugarEmpRec());
         
         cmbPatioDestino.removeAllItems();
         cmbPatioDestino.setEnabled(false);
         cmbPatioDestino.addItem(lugarEmpRecDestino.getMaquinaDoPatio().getPatio());
         cmbPatioDestino.setSelectedItem(lugarEmpRecDestino.getMaquinaDoPatio().getPatio());
         
         cmbMaquinaEmpilhamento.removeAllItems();     
         cmbMaquinaEmpilhamento.setEnabled(false);
         cmbMaquinaEmpilhamento.addItem(lugarEmpRecDestino.getMaquinaDoPatio());
         cmbMaquinaEmpilhamento.setSelectedItem(lugarEmpRecDestino.getMaquinaDoPatio());     
      
         Baliza balizaInicialDestino = lugarEmpRecDestino.getListaDeBalizas().get(0);
         Baliza balizaFinalDestino = lugarEmpRecDestino.getListaDeBalizas().get(lugarEmpRecDestino.getListaDeBalizas().size()-1);         
         //cmbBalizaInicialPilhaDestino.addItem(balizaInicialDestino);
         cmbBalizaInicialPilhaDestino.setEnabled(false);         
         cmbBalizaInicialPilhaDestino.setSelectedItem(balizaInicialDestino);         
         selecionarComboBaliza(cmbBalizaInicialPilhaDestino, balizaInicialDestino);
         
         // se existir uma atividade de empilhamento pendente entao nao deixa mudar a baliza destino
         Atividade atividadeEmpilhamentoPendente = lugarEmpRecDestino.getMaquinaDoPatio().getMetaMaquina().existeEmpilhamentoPendente();
         if (atividadeEmpilhamentoPendente != null)
       	  	cmbBalizaFinalPilhaDestino.setEnabled(false);
         else
        	cmbBalizaFinalPilhaDestino.setEnabled(true);
         cmbBalizaFinalPilhaDestino.setSelectedItem(balizaFinalDestino);
         
         selecionarComboBaliza(cmbBalizaFinalPilhaDestino,balizaFinalDestino);
      
         
      }
      // operacao de pilha para navio
      else
      {
         montaComboNaviosAtracado();
         rdCarregamento.setSelected(true);
         // ... atualizando o combo do navio atracado com as informacoes da atividade que
         // ... jah esta em execucao
         cmbNavioAtracado.setEnabled(false);
         MetaNavio metaNavioAtividade = atividadeExecutada.getListaDeLugaresDeEmpilhamentoRecuperacao().get(1).getListaCargas().get(0).getNavio(atividadeExecutada.getDtInicio()).getMetaNavio();

         for (int i = 0; i < cmbNavioAtracado.getItemCount(); i++)
         {
            InterfaceNavio interfaceNavio = (InterfaceNavio) cmbNavioAtracado.getItemAt(i);
            if (interfaceNavio.getNavioVisualizado().getMetaNavio().equals(metaNavioAtividade))
            {
             cmbNavioAtracado.setSelectedIndex(i);
             break;
            }
         }

         // ... atualizando o combo de cargas do navio atracado com as informacoes da atividade que
         // ... jah esta em execucao
         MetaCarga metaCarga = atividadeExecutada.getListaDeLugaresDeEmpilhamentoRecuperacao().get(1).getListaCargas().get(0).getMetaCarga();
         
         for (int i = 0; i < cmbCargasNavio.getItemCount(); i++)
         {
            InterfaceCarga interfaceCarga = (InterfaceCarga) cmbCargasNavio.getItemAt(i);
            if (interfaceCarga.getCargaVisualizada().getMetaCarga().equals(metaCarga))
            {
             cmbCargasNavio.setSelectedIndex(i);
             break;
            }
         }
        
         cmbCargasNavio.setEnabled(false);
      }

      txtDataHoraTermino.setEnabled(true);
      txtDataHoraTermino.setText(DSSStockyardTimeUtil.formatarData(DSSStockyardTimeUtil.obterDataHoraUltimaSituacaoParaAtividades(controladorDSP.getInterfaceInicial().getInterfaceInicial().getSituacaoPatioExibida().getDtInicio()), PropertiesUtil.buscarPropriedade("formato.campo.datahora")));
      txtDataHoraTermino.setSelectionStart(11); // comeca a selecao no inicio da hora
      txtDataHoraTermino.setSelectionEnd(txtDataHoraTermino.getText().length());

      txtDataHoraInicio.setEnabled(false);
      txtDataHoraInicio.setText(DSSStockyardTimeUtil.formatarData(atividadeExecutada.getDtInicio(), PropertiesUtil.buscarPropriedade("formato.campo.datahora")));

      txtNumeroPorao.setEnabled(false);
      txtNumeroPorao.setText(atividadeExecutada.getListaDeLugaresDeEmpilhamentoRecuperacao().get(1).getNomePorao());                  
      
   }
   
   private void carregarInformacoesIniciais()
   {
      cmbPilhaOrigem.removeAllItems();
      List<Pilha> pilhasOrigem = null;
      
      if (maquinaDoPatioOrigem.getAtividade() == null) {
	      // carrega as possiveis pilhas de origem
	      if (this.tipoAtividade.equals(TipoAtividadeEnum.MOVIMENTAR_PILHA_EMERGENCIA)) {
	          pilhasOrigem =   controladorDSP.getInterfaceInicial().getInterfaceInicial().getSituacaoPatioExibida().obterListaPilhasEmergenciaMovimentacao(maquinaDoPatioOrigem.getPatio());
	      }else {
	          pilhasOrigem = controladorDSP.getInterfaceInicial().getInterfaceInicial().getSituacaoPatioExibida().obterListaPilhasPSMPelletFeedParaMovimentacao(tipoProduto,maquinaDoPatioOrigem.getPatio());
	      }
	      
	      for (Pilha pilha : pilhasOrigem)
	      {
	         cmbPilhaOrigem.addItem(pilha);
	      }
	
	      // carrega as balizas da pilha
	      if (cmbPilhaOrigem.getItemCount() > 0)
	      {
	         pilhaOrigem = (Pilha) cmbPilhaOrigem.getSelectedItem();
	         carregaInformacoesPilhaOrigem();
	      }
      }   

      txtDataHoraInicio.setEnabled(true);
      txtDataHoraInicio.setText(DSSStockyardTimeUtil.formatarData(DSSStockyardTimeUtil.obterDataHoraUltimaSituacaoParaAtividades(controladorDSP.getInterfaceInicial().getInterfaceInicial().getSituacaoPatioExibida().getDtInicio()), PropertiesUtil.buscarPropriedade("formato.campo.datahora")));
      txtDataHoraInicio.setSelectionStart(11); // comeca a selecao no inicio da hora
      txtDataHoraInicio.setSelectionEnd(txtDataHoraInicio.getText().length());
      
      txtTaxa.setText(DSSStockyardFuncoesNumeros.getQtdeFormatada(maquinaDoPatioOrigem.getTaxaDeOperacaoNominal(),1));
   }

   /** This method is called from within the constructor to
    * initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is
    * always regenerated by the Form Editor.
    */
   @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grpOpcoesCarregamento = new javax.swing.ButtonGroup();
        pnlPrincipal = new javax.swing.JPanel();
        pnlOpcoesCarregamento = new javax.swing.JPanel();
        rdPatio = new javax.swing.JRadioButton();
        rdCarregamento = new javax.swing.JRadioButton();
        pnlInformacoesEmergencia = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtTaxa = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cmbPilhaOrigem = new javax.swing.JComboBox();
        cmbBalizaFinalPilhaOrigem = new javax.swing.JComboBox();
        cmbBalizaInicialPilhaOrigem = new javax.swing.JComboBox();
        cmdDesistir = new javax.swing.JButton();
        cmdConfirmar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        cmbBalizaInicialPilhaDestino = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        cmbBalizaFinalPilhaDestino = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        txtNomePilhaExistente = new javax.swing.JTextField();
        lblCorTipoProdutoPilhaExistente = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        cmbMaquinaEmpilhamento = new javax.swing.JComboBox();
        cmbPatioDestino = new javax.swing.JComboBox();
        pnlInformacoesCargasNavio = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        cmbNavioAtracado = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        txtOrientacaoEmbarque = new javax.swing.JTextField();
        txtCorProdutoOrientacao = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        cmbCargasNavio = new javax.swing.JComboBox();
        txtCorProdutoCargaNavio = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtNumeroPorao = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        txtDataHoraInicio = new javax.swing.JFormattedTextField();
        MaskFormatter fmtHoraInicioRecuperacao = new MaskFormatter();
        try {
            fmtHoraInicioRecuperacao.setMask(PropertiesUtil.buscarPropriedade("mascara.campo.datahora"));
            fmtHoraInicioRecuperacao.setPlaceholderCharacter(' ');
            fmtHoraInicioRecuperacao.install(txtDataHoraInicio);
        } catch (ParseException pex) {
            pex.printStackTrace();
        }
        jLabel16 = new javax.swing.JLabel();
        txtDataHoraTermino = new javax.swing.JFormattedTextField();
        MaskFormatter fmtHoraTerminoRecuperacao = new MaskFormatter();
 try {
    fmtHoraTerminoRecuperacao.setMask(PropertiesUtil.buscarPropriedade("mascara.campo.datahora"));
    fmtHoraTerminoRecuperacao.setPlaceholderCharacter(' ');
    fmtHoraTerminoRecuperacao.install(txtDataHoraTermino);
 } catch (ParseException pex) {
    pex.printStackTrace();
 }

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(PropertiesUtil.getMessage("mensagem.titulo.interface.movimentacao.pilha.emergencia") +"-"+ this.tipoAtividade.toString());
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        pnlOpcoesCarregamento.setBorder(javax.swing.BorderFactory.createTitledBorder(PropertiesUtil.getMessage("mensagem.titulo.painel.opcoes.movimentacao")));

        grpOpcoesCarregamento.add(rdPatio);
        rdPatio.setText(PropertiesUtil.getMessage("label.radio.opcao.patio"));
        rdPatio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdPatioActionPerformed(evt);
            }
        });

        grpOpcoesCarregamento.add(rdCarregamento);
        rdCarregamento.setText(PropertiesUtil.getMessage("label.radio.opcao.carregamento"));
        rdCarregamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdCarregamentoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlOpcoesCarregamentoLayout = new javax.swing.GroupLayout(pnlOpcoesCarregamento);
        pnlOpcoesCarregamento.setLayout(pnlOpcoesCarregamentoLayout);
        pnlOpcoesCarregamentoLayout.setHorizontalGroup(
            pnlOpcoesCarregamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOpcoesCarregamentoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlOpcoesCarregamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rdCarregamento, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
                    .addComponent(rdPatio, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlOpcoesCarregamentoLayout.setVerticalGroup(
            pnlOpcoesCarregamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOpcoesCarregamentoLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(rdPatio)
                .addGap(18, 18, 18)
                .addComponent(rdCarregamento)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        pnlInformacoesEmergencia.setBorder(javax.swing.BorderFactory.createTitledBorder(PropertiesUtil.getMessage("mensagem.titulo.painel.informacoes.emergencia")));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel1.setText(PropertiesUtil.getMessage("label.informacoes.emergencia.pilha"));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel2.setText(PropertiesUtil.getMessage("label.taxaDeMovimentacao"));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel3.setText(PropertiesUtil.getMessage("label.informacoes.movimentacao.balizaInicial"));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel5.setText(PropertiesUtil.getMessage("label.informacoes.movimentacao.balizaFinal"));

        cmbPilhaOrigem.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbPilhaOrigemItemStateChanged(evt);
            }
        });

        cmbBalizaFinalPilhaOrigem.setEnabled(false);
        cmbBalizaFinalPilhaOrigem.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbBalizaFinalPilhaOrigemItemStateChanged(evt);
            }
        });

        cmbBalizaInicialPilhaOrigem.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbBalizaInicialPilhaOrigemItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout pnlInformacoesEmergenciaLayout = new javax.swing.GroupLayout(pnlInformacoesEmergencia);
        pnlInformacoesEmergencia.setLayout(pnlInformacoesEmergenciaLayout);
        pnlInformacoesEmergenciaLayout.setHorizontalGroup(
            pnlInformacoesEmergenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInformacoesEmergenciaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInformacoesEmergenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlInformacoesEmergenciaLayout.createSequentialGroup()
                        .addComponent(jLabel3, 0, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbBalizaInicialPilhaOrigem, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cmbBalizaFinalPilhaOrigem, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlInformacoesEmergenciaLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbPilhaOrigem, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTaxa, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnlInformacoesEmergenciaLayout.setVerticalGroup(
            pnlInformacoesEmergenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInformacoesEmergenciaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInformacoesEmergenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cmbPilhaOrigem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(txtTaxa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(pnlInformacoesEmergenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlInformacoesEmergenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cmbBalizaInicialPilhaOrigem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)
                        .addComponent(cmbBalizaFinalPilhaOrigem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel3))
                .addGap(15, 15, 15))
        );

        cmdDesistir.setFont(new java.awt.Font("Tahoma", 1, 12));
        //cmdDesistir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icones/exit.png"))); // NOI18N
        cmdDesistir.setText(PropertiesUtil.getMessage("botao.desistir"));
        cmdDesistir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdDesistirActionPerformed(evt);
            }
        });

        cmdConfirmar.setFont(new java.awt.Font("Tahoma", 1, 12));
        //cmdConfirmar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icones/confirmar.png"))); // NOI18N
        cmdConfirmar.setText(PropertiesUtil.getMessage("botao.confirmar"));
        cmdConfirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdConfirmarActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(PropertiesUtil.getMessage("mensagem.titulo.painel.movimentacao.patio")));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel6.setText(PropertiesUtil.getMessage("label.baliza.inicial"));

        cmbBalizaInicialPilhaDestino.setFont(new java.awt.Font("Arial", 0, 12));
        cmbBalizaInicialPilhaDestino.setEnabled(false);
        cmbBalizaInicialPilhaDestino.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbBalizaInicialPilhaDestinoItemStateChanged(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel7.setText(PropertiesUtil.getMessage("label.texto.selecao.baliza.final"));

        cmbBalizaFinalPilhaDestino.setFont(new java.awt.Font("Arial", 0, 12));
        cmbBalizaFinalPilhaDestino.setEnabled(false);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel8.setText(PropertiesUtil.getMessage("label.informacoes.emergencia.pilha"));

        txtNomePilhaExistente.setEditable(false);
        txtNomePilhaExistente.setEnabled(false);

        lblCorTipoProdutoPilhaExistente.setFont(new java.awt.Font("Tahoma", 0, 14));
        lblCorTipoProdutoPilhaExistente.setOpaque(true);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel10.setText(PropertiesUtil.getMessage("label.patio.destino.pilha.emergencia"));

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel17.setText(PropertiesUtil.getMessage("label.maquina.do.patio"));

        cmbMaquinaEmpilhamento.setFont(new java.awt.Font("Arial", 0, 12));
        cmbMaquinaEmpilhamento.setEnabled(false);

        cmbPatioDestino.setFont(new java.awt.Font("Arial", 0, 12));
        cmbPatioDestino.setEnabled(false);
        cmbPatioDestino.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbPatioDestinoItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNomePilhaExistente, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbBalizaInicialPilhaDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbPatioDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbMaquinaEmpilhamento, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbBalizaFinalPilhaDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(231, 231, 231)
                .addComponent(lblCorTipoProdutoPilhaExistente, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbPatioDestino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbMaquinaEmpilhamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(cmbBalizaInicialPilhaDestino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtNomePilhaExistente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(cmbBalizaFinalPilhaDestino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblCorTipoProdutoPilhaExistente, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlInformacoesCargasNavio.setBorder(javax.swing.BorderFactory.createTitledBorder("Seleção de informações navio"));

        jLabel11.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel11.setText("Navio atracado:");

        cmbNavioAtracado.setFont(new java.awt.Font("Arial", 0, 12));
        cmbNavioAtracado.setEnabled(false);
        cmbNavioAtracado.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbNavioAtracadoItemStateChanged(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel12.setText("Orientação embarque:");

        txtOrientacaoEmbarque.setEditable(false);
        txtOrientacaoEmbarque.setFont(new java.awt.Font("Arial", 0, 12));

        txtCorProdutoOrientacao.setEditable(false);
        txtCorProdutoOrientacao.setFont(new java.awt.Font("Arial", 0, 12));

        jLabel13.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel13.setText("Cargas do navio:");

        cmbCargasNavio.setFont(new java.awt.Font("Arial", 0, 12));
        cmbCargasNavio.setEnabled(false);
        cmbCargasNavio.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbCargasNavioItemStateChanged(evt);
            }
        });

        txtCorProdutoCargaNavio.setEditable(false);
        txtCorProdutoCargaNavio.setFont(new java.awt.Font("Arial", 0, 12));

        jLabel14.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel14.setText("Número do porão:");

        txtNumeroPorao.setFont(new java.awt.Font("Arial", 0, 12));
        txtNumeroPorao.setEnabled(false);

        javax.swing.GroupLayout pnlInformacoesCargasNavioLayout = new javax.swing.GroupLayout(pnlInformacoesCargasNavio);
        pnlInformacoesCargasNavio.setLayout(pnlInformacoesCargasNavioLayout);
        pnlInformacoesCargasNavioLayout.setHorizontalGroup(
            pnlInformacoesCargasNavioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInformacoesCargasNavioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInformacoesCargasNavioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInformacoesCargasNavioLayout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbCargasNavio, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCorProdutoCargaNavio, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlInformacoesCargasNavioLayout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNumeroPorao, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbNavioAtracado, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtOrientacaoEmbarque, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCorProdutoOrientacao, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        pnlInformacoesCargasNavioLayout.setVerticalGroup(
            pnlInformacoesCargasNavioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInformacoesCargasNavioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInformacoesCargasNavioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtNumeroPorao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbNavioAtracado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(txtOrientacaoEmbarque, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCorProdutoOrientacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlInformacoesCargasNavioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(cmbCargasNavio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCorProdutoCargaNavio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(PropertiesUtil.getMessage("mensagem.titulo.painel.horario")));

        jLabel15.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel15.setText("Data início:");

        jLabel16.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel16.setText("Data término:");

        txtDataHoraTermino.setEnabled(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtDataHoraInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(63, 63, 63)
                .addComponent(jLabel16)
                .addGap(18, 18, 18)
                .addComponent(txtDataHoraTermino, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(178, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16)
                    .addComponent(txtDataHoraTermino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDataHoraInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlPrincipalLayout = new javax.swing.GroupLayout(pnlPrincipal);
        pnlPrincipal.setLayout(pnlPrincipalLayout);
        pnlPrincipalLayout.setHorizontalGroup(
            pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPrincipalLayout.createSequentialGroup()
                        .addComponent(cmdDesistir, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(258, 258, 258))
                    .addGroup(pnlPrincipalLayout.createSequentialGroup()
                        .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlInformacoesCargasNavio, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, 0, 735, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlPrincipalLayout.createSequentialGroup()
                                .addComponent(pnlOpcoesCarregamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pnlInformacoesEmergencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        pnlPrincipalLayout.setVerticalGroup(
            pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlInformacoesEmergencia, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                    .addComponent(pnlOpcoesCarregamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlInformacoesCargasNavio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdDesistir))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        getContentPane().add(pnlPrincipal, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
       this.operacaoCanceladaPeloUsuario = true;
    }//GEN-LAST:event_formWindowClosing

    private void cmdDesistirActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmdDesistirActionPerformed
    {//GEN-HEADEREND:event_cmdDesistirActionPerformed
       this.operacaoCanceladaPeloUsuario = true;
       setVisible(false);
    }//GEN-LAST:event_cmdDesistirActionPerformed

    private void cmbPilhaOrigemItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_cmbPilhaOrigemItemStateChanged
    {//GEN-HEADEREND:event_cmbPilhaOrigemItemStateChanged
       pilhaOrigem = (Pilha) cmbPilhaOrigem.getSelectedItem();
       carregaInformacoesPilhaOrigem();
    }//GEN-LAST:event_cmbPilhaOrigemItemStateChanged

    private void rdPatioActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rdPatioActionPerformed
    {//GEN-HEADEREND:event_rdPatioActionPerformed
       if (rdPatio.isSelected())
       {
          habilitaPainelMovimentacaoPatio();
          desabilitaPainelMovimentacaoCarregamento();
          carregarInformacoesDestinoPatio();
       }
    }//GEN-LAST:event_rdPatioActionPerformed

   private void desabilitaPainelMovimentacaoCarregamento()
   {
      txtNumeroPorao.setEnabled(false);
      cmbNavioAtracado.setEnabled(false);
      cmbCargasNavio.setEnabled(false);
   }

   private void habilitaPainelMovimentacaoCarregamento()
   {
      txtNumeroPorao.setEnabled(true);
      cmbNavioAtracado.setEnabled(true);
      cmbCargasNavio.setEnabled(true);
   }

    private void cmbNavioAtracadoItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_cmbNavioAtracadoItemStateChanged
    {//GEN-HEADEREND:event_cmbNavioAtracadoItemStateChanged
       InterfaceNavio interfaceNavioSelecionado = (InterfaceNavio) cmbNavioAtracado.getSelectedItem();
       montaComboCargasNavio(interfaceNavioSelecionado);
    }//GEN-LAST:event_cmbNavioAtracadoItemStateChanged

    private void cmbCargasNavioItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_cmbCargasNavioItemStateChanged
    {//GEN-HEADEREND:event_cmbCargasNavioItemStateChanged
       InterfaceCarga interfaceCargaSelecionada = (InterfaceCarga) cmbCargasNavio.getSelectedItem();
       if (interfaceCargaSelecionada != null)
       {
          String[] rgbProdutoCargaSelecionada = interfaceCargaSelecionada.getCargaVisualizada().getProduto().getTipoProduto().getCorIdentificacao().split(",");
          txtCorProdutoCargaNavio.setBackground(new Color(Integer.parseInt(rgbProdutoCargaSelecionada[0]), Integer.parseInt(rgbProdutoCargaSelecionada[1]), Integer.parseInt(rgbProdutoCargaSelecionada[2])));

          txtOrientacaoEmbarque.setText(interfaceCargaSelecionada.getCargaVisualizada().getOrientacaoDeEmbarque().getTipoProduto().toString());
          String[] rgbProdutoOrientacao = interfaceCargaSelecionada.getCargaVisualizada().getOrientacaoDeEmbarque().getTipoProduto().getCorIdentificacao().split(",");
          txtCorProdutoOrientacao.setBackground(new Color(Integer.parseInt(rgbProdutoOrientacao[0]), Integer.parseInt(rgbProdutoOrientacao[1]), Integer.parseInt(rgbProdutoOrientacao[2])));
       }
    }//GEN-LAST:event_cmbCargasNavioItemStateChanged

    private void rdCarregamentoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rdCarregamentoActionPerformed
    {//GEN-HEADEREND:event_rdCarregamentoActionPerformed
       if (rdCarregamento.isSelected())
       {
          habilitaPainelMovimentacaoCarregamento();
          desabilitaPainelMovimentacaoPatio();
          montaComboNaviosAtracado();
          txtNumeroPorao.requestFocus();
       }
    }//GEN-LAST:event_rdCarregamentoActionPerformed

    private void cmdConfirmarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmdConfirmarActionPerformed
    {//GEN-HEADEREND:event_cmdConfirmarActionPerformed
       try
       {
          validarInformacoesMovimentacaoPilha();

          Date dataHoraOcorrenciaEvento = DSSStockyardTimeUtil.criaDataComString(txtDataHoraInicio.getText(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));

          double diferencaHoras = DSSStockyardTimeUtil.diferencaEmHoras(controladorDSP.getInterfaceInicial().getInterfaceInicial().getSituacaoPatioExibida().getDtInicio(), dataHoraOcorrenciaEvento);
          double tempoValidacaoHorasFutura = new Double(PropertiesUtil.buscarPropriedade("quantidade.horas.aviso.data.futura.atividades"));

          if (diferencaHoras >= tempoValidacaoHorasFutura)
          {
             JLabel pergunta = new JLabel(PropertiesUtil.getMessage("aviso.hora.atividade.superior.ao.parametro.futuro"));
             pergunta.setFont(new Font("Tahoma", Font.PLAIN, 11));
             int confirm = JOptionPane.showOptionDialog(
                   this,
                   pergunta,
                   PropertiesUtil.getMessage("popup.atencao"),
                   JOptionPane.YES_OPTION,
                   JOptionPane.INFORMATION_MESSAGE,
                   null,
                   null,
                   null);

             if (confirm == JOptionPane.NO_OPTION)
             {
                if (txtDataHoraInicio.isEnabled())
                {
                   txtDataHoraInicio.requestFocus();
                }
                return;
             }
          }

          criaAtividadeMovimentacaoPilha();
          
          operacaoCanceladaPeloUsuario = Boolean.FALSE;
          this.setVisible(false);
       }
       catch (AtividadeException ex)
       {
          controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
          interfaceMensagem = new InterfaceMensagem();
          interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
          interfaceMensagem.setDescricaoMensagem(ex.getMessage());
          controladorDSP.ativarMensagem(interfaceMensagem);
       }
       catch (ValidacaoObjetosOperacaoException ex)
       {
          controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
          interfaceMensagem = new InterfaceMensagem();
          interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
          interfaceMensagem.setDescricaoMensagem(ex.getMessage());
          controladorDSP.ativarMensagem(interfaceMensagem);
       }
       catch (ValidacaoCampoException vcEx)
       {
          controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
          interfaceMensagem = new InterfaceMensagem();
          interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
          interfaceMensagem.setDescricaoMensagem(vcEx.getMessage());
          controladorDSP.ativarMensagem(interfaceMensagem);
       }
    }//GEN-LAST:event_cmdConfirmarActionPerformed

    private void cmbBalizaInicialPilhaDestinoItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_cmbBalizaInicialPilhaDestinoItemStateChanged
    {//GEN-HEADEREND:event_cmbBalizaInicialPilhaDestinoItemStateChanged       
            Baliza balizaInicial = (Baliza)cmbBalizaInicialPilhaDestino.getSelectedItem();
           Date dataAtual = null;
        try {
            dataAtual = DSSStockyardTimeUtil.criaDataComString(txtDataHoraInicio.getText(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
        } catch (ValidacaoCampoException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
           if (balizaInicial != null)
           {
               Pilha pilhaAtual = balizaInicial.retornaStatusHorario(dataAtual);           
               if (pilhaAtual != null)
               txtNomePilhaExistente.setText(pilhaAtual.getNomePilha());
           }
           txtNomePilhaExistente.setEditable(true);          
    }//GEN-LAST:event_cmbBalizaInicialPilhaDestinoItemStateChanged

    private void cmbBalizaFinalPilhaOrigemItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbBalizaFinalPilhaOrigemItemStateChanged
    }//GEN-LAST:event_cmbBalizaFinalPilhaOrigemItemStateChanged

    private void cmbBalizaInicialPilhaOrigemItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbBalizaInicialPilhaOrigemItemStateChanged
    }//GEN-LAST:event_cmbBalizaInicialPilhaOrigemItemStateChanged

    private void cmbPatioDestinoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbPatioDestinoItemStateChanged
    	Patio patio = (Patio)cmbPatioDestino.getSelectedItem();
        if (patio != null) {
        	carregaMaquinasQueAtendemPatio(patio);
        	carregaBalizasPatioDestino(patio);
        }	
    }//GEN-LAST:event_cmbPatioDestinoItemStateChanged

   private void carregaMaquinasQueAtendemPatio(Patio patioSelecionado)
   {
      cmbMaquinaEmpilhamento.removeAllItems();
      // retorna a lista de maquinas que atendem o patio selecionado
      List<MaquinaDoPatio> listaMaquinas = retornaMaquinasQueAtendemPatio(patioSelecionado);
      for (MaquinaDoPatio empilhadeira : listaMaquinas)
      {
         cmbMaquinaEmpilhamento.addItem(empilhadeira);
      }
   }
   private void criaAtividadeMovimentacaoPilha() throws ValidacaoCampoException, AtividadeException, ValidacaoObjetosOperacaoException
   {
      MovimentacaoVO movimentacaoVO = new MovimentacaoVO();
      movimentacaoVO.setTipoAtividade(this.tipoAtividade);

      double quantidadeBalizasOrigem = 0D;
      Date dataInicioAtividade = null;
      Date dataFinalAtividade = null;
      if (atividadeExecutada != null && atividadeExecutada.getDtInicio() != null) {
          dataInicioAtividade = atividadeExecutada.getDtInicio(); 
          dataFinalAtividade = DSSStockyardTimeUtil.criaDataComString(txtDataHoraTermino.getText(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
          dataFinalAtividade = Atividade.verificaAtualizaDataAtividade(dataFinalAtividade, controladorDSP.getInterfaceInicial().getPlanejamento().getControladorDePlano().obterSituacaoPatioFinal().getDtInicio());
      } else {   
          dataInicioAtividade = DSSStockyardTimeUtil.criaDataComString(txtDataHoraInicio.getText(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
          dataInicioAtividade = Atividade.verificaAtualizaDataAtividade(dataInicioAtividade, controladorDSP.getInterfaceInicial().getPlanejamento().getControladorDePlano().obterSituacaoPatioFinal().getDtInicio());
          //se o usuario configurou para datas iguais (hora/minuto) o sistema deve diferenciar por milisegundos as ativiadades
      }
      

          // campos a serem setados:
          // 1) A lista de balizas da pilha virtual
          // 2) Nome
          // 3) Maquina
          // 4) Seta o tipo de movimentacao da maquina
          // nesta pilha virtual, que inicialmente sera
          // sempre ma MOVIMENTACAO em direcao a pilha
          // 5) Setar a pilha como executada
          // 6) Setar a quantidade total de material para pilha
          // 7) O tipo de produto a ser recuperado
          // 8) setar o nome do porao

          int numeroBalizaInicialOrigem = 0;
          int numeroBalizaFinalOrigem = 0;
          List<Baliza> listaBalizasSelecionadasOrigem = new ArrayList<Baliza>();
          int numeroBalizaInicialDestino = 0;
          int numeroBalizaFinalDestino = 0;
          List<Baliza> listaBalizasSelecionadasDestino = new ArrayList<Baliza>();
          
       // ainda nao fechou a atividade, inclui apenas a primeira baliza do intervalo
             numeroBalizaInicialOrigem = ((Baliza) cmbBalizaInicialPilhaOrigem.getSelectedItem()).getNumero();             
             //listaBalizasSelecionadasOrigem.add(obterBalizaPeloNumero(numeroBalizaInicialOrigem, ((Baliza) cmbBalizaInicialPilhaOrigem.getSelectedItem()).getPatio()));
             numeroBalizaFinalOrigem = ((Baliza) cmbBalizaInicialPilhaOrigem.getSelectedItem()).getNumero();
             
             
             if (cmbBalizaFinalPilhaOrigem.getSelectedItem() != null && dataFinalAtividade != null) {
                 numeroBalizaFinalOrigem = ((Baliza) cmbBalizaFinalPilhaOrigem.getSelectedItem()).getNumero();
             }   
             
             
             int maiorBaliza = Math.max(numeroBalizaInicialOrigem, numeroBalizaFinalOrigem);
             int menorBaliza = Math.min(numeroBalizaInicialOrigem, numeroBalizaFinalOrigem);

             // define a lista de balizas alteradas nesta atividade
             
             for (int i = menorBaliza; i <= maiorBaliza; i++) {                
                 Baliza baliza = obterBalizaPeloNumero(i, ((Baliza) cmbBalizaInicialPilhaOrigem.getSelectedItem()).getPatio(),horaSituacao);
                 quantidadeBalizasOrigem += baliza.getProduto().getQuantidade();
                 listaBalizasSelecionadasOrigem.add(baliza);                                        
             }
             
             List<MetaBaliza> metaBalizaOrigem = new ArrayList<MetaBaliza>();   
              for(Baliza balizaOrigem : listaBalizasSelecionadasOrigem) {
                  metaBalizaOrigem.add(balizaOrigem.getMetaBaliza());
              }

             movimentacaoVO.setListaBalizas(metaBalizaOrigem);
             //seta o sentido do empilhamento
             if (numeroBalizaFinalOrigem > numeroBalizaInicialOrigem)
             {
                 movimentacaoVO.setSentidoRecuperacao(SentidoEmpilhamentoRecuperacaoEnum.NORTE_PARA_SUL);
             }
             else
             {
                 movimentacaoVO.setSentidoRecuperacao(SentidoEmpilhamentoRecuperacaoEnum.SUL_PARA_NORTE);
             }

             

             movimentacaoVO.setTipoProduto(pilhaOrigem.getListaDeBalizas().get(0).getProduto().getTipoProduto());
             movimentacaoVO.setNomePilha(((Pilha)cmbPilhaOrigem.getSelectedItem()).getNomePilha().trim());
             movimentacaoVO.setDataInicio(dataInicioAtividade);
             movimentacaoVO.setDataFim(dataFinalAtividade);
             movimentacaoVO.setQuantidadeMovimentacao(quantidadeBalizasOrigem);
             movimentacaoVO.setTxtNumeroPorao("");
             movimentacaoVO.setAtividadeAnterior(atividadeExecutada);
             
             if (rdPatio.isSelected()) {
        
                 numeroBalizaInicialDestino = ((Baliza) cmbBalizaInicialPilhaDestino.getSelectedItem()).getNumero();
                 numeroBalizaFinalDestino = ((Baliza) cmbBalizaInicialPilhaDestino.getSelectedItem()).getNumero();
                 
                 if (cmbBalizaFinalPilhaDestino.getSelectedItem() != null && dataFinalAtividade != null) {
                     numeroBalizaFinalDestino = ((Baliza) cmbBalizaFinalPilhaDestino.getSelectedItem()).getNumero();
                 }   
                 
                 maiorBaliza = Math.max(numeroBalizaInicialDestino, numeroBalizaFinalDestino);
                 menorBaliza = Math.min(numeroBalizaInicialDestino, numeroBalizaFinalDestino);
                 
                 // define a lista de balizas alteradas nesta atividade
                 
                 for (int i = menorBaliza; i <= maiorBaliza; i++) {
                     listaBalizasSelecionadasDestino.add(obterBalizaPeloNumero(i, ((Baliza) cmbBalizaInicialPilhaDestino.getSelectedItem()).getPatio(),horaSituacao));                         
                 }
                 
                 List<MetaBaliza> metaBalizaDestino = new ArrayList<MetaBaliza>();   
                 for(Baliza balizaDestino : listaBalizasSelecionadasDestino) {
                     metaBalizaDestino.add(balizaDestino.getMetaBaliza());
                 }
    
                 
                 movimentacaoVO.setListaBalizasDestino(metaBalizaDestino);
                 movimentacaoVO.setNomePilhaDestino(txtNomePilhaExistente.getText().trim());
                 
                 List<MetaMaquinaDoPatio> listaMaquinas = new ArrayList<MetaMaquinaDoPatio>();
                 listaMaquinas.add(maquinaDoPatioOrigem.getMetaMaquina());
                 listaMaquinas.add( ((MaquinaDoPatio) cmbMaquinaEmpilhamento.getSelectedItem()).getMetaMaquina() );
                 movimentacaoVO.setListaMaquinas(listaMaquinas);
          } else {
              // Carga
              // seta a carga
              InterfaceCarga interfaceCargaSelecionada = (InterfaceCarga) cmbCargasNavio.getSelectedItem();
              MetaCarga metaCarga= interfaceCargaSelecionada.getCargaVisualizada().getMetaCarga();
              movimentacaoVO.setMetaCarga(metaCarga);
              movimentacaoVO.setTxtNumeroPorao(txtNumeroPorao.getText());
              List<MetaMaquinaDoPatio> listaMaquinas = new ArrayList<MetaMaquinaDoPatio>();
              listaMaquinas.add(maquinaDoPatioOrigem.getMetaMaquina());
              movimentacaoVO.setListaMaquinas(listaMaquinas);
          }
      
        ControladorExecutarAtividadeMovimentacaoPilha service = ControladorExecutarAtividadeMovimentacaoPilha.getInstance();
      
        atividadeMovimentar = service.movimentar(movimentacaoVO);
        controladorDSP.getInterfaceInicial().movimentarPilhaPSMPelletFeed(atividadeMovimentar);
        controladorDSP.getInterfaceInicial().getInterfaceInicial().atualizarDSP();
   
      
    }

   private void validarInformacoesMovimentacaoPilha() throws ValidacaoObjetosOperacaoException, ValidacaoCampoException
   {
      maquinaDoPatioDestino = (MaquinaDoPatio) cmbMaquinaEmpilhamento.getSelectedItem();
      
      // verifica se uma maquina do patio foi selecionada
      if (rdPatio.isSelected() && maquinaDoPatioDestino == null)
         throw new ValidacaoObjetosOperacaoException(PropertiesUtil.getMessage("aviso.patio.maquina.nao.selecionada"));
      
      // se existir uma atividade de empilhamento pendente entao a baliza destino deve ser igual a baliza do empilhamento
      if (maquinaDoPatioDestino != null) {
		      Atividade atividadeEmpilhamentoPendente = maquinaDoPatioDestino.getMetaMaquina().existeEmpilhamentoPendente();
		      if (atividadeEmpilhamentoPendente != null)
		      {
		    	  Baliza balizaEmpilhamento = atividadeEmpilhamentoPendente.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaDeBalizas().get(0);
		    	  
		    	  boolean balizaDiferenteEmpilhamento = false;
		    	  if (cmbBalizaInicialPilhaDestino.isEnabled() && !((Baliza) cmbBalizaInicialPilhaDestino.getSelectedItem()).getMetaBaliza().equals(balizaEmpilhamento.getMetaBaliza()))
		    		  balizaDiferenteEmpilhamento = true;
		    	  if (cmbBalizaFinalPilhaDestino.isEnabled() && ((Baliza) cmbBalizaFinalPilhaDestino.getSelectedItem()).getMetaBaliza().equals(balizaEmpilhamento.getMetaBaliza()))
		    		  balizaDiferenteEmpilhamento = true;
		    	  if (balizaDiferenteEmpilhamento)
		    		  throw new ValidacaoObjetosOperacaoException(PropertiesUtil.getMessage("aviso.atividade.empilhamento.balizas.diferentes"));   	  
		      }
      }	      
      
      taxaMovimentacao = DSSStockyardFuncoesNumeros.getStringToDouble(txtTaxa.getText());
      if (taxaMovimentacao <= 0)
         throw new ValidacaoObjetosOperacaoException(PropertiesUtil.getMessage("aviso.patio.maquina.nao.selecionada"));
      
      if (rdCarregamento.isSelected())
      {
          //verifica se existe algum navio no berco
          if(cmbNavioAtracado.getSelectedItem() == null){
              throw new ValidacaoObjetosOperacaoException(PropertiesUtil.getMessage("aviso.navio.nao.encontrado.no.berco"));
          }

         // ... obtem o produto da carga selecionada do navio atracado selecionado
         Produto produtoDaCarga = ((InterfaceCarga) cmbCargasNavio.getSelectedItem()).getCargaVisualizada().getProduto();

         if (maquinaDoPatioOrigem.getAtividade() == null || maquinaDoPatioOrigem.getEstado().equals(EstadoMaquinaEnum.OCIOSA))
         {
            // ... obtem o produto da baliza selecionada para recuperacao
            Produto produBalizaInicialSelecionada = pilhaOrigem.getListaDeBalizas().get(0).getProduto();

            // ... valida se o produto da baliza selecionada eh compativel com o produto da carga seleciondada
            if (produBalizaInicialSelecionada != null && !produBalizaInicialSelecionada.getTipoProduto().getIdTipoProduto().equals(produtoDaCarga.getTipoProduto().getIdTipoProduto()))
            {
               JLabel pergunta = new JLabel(PropertiesUtil.getMessage("mensagem.produto.incompativel.baliza.inicial"));
               pergunta.setFont(new Font("Tahoma", Font.PLAIN, 11));
               int confirm = JOptionPane.showOptionDialog(
                     this,
                     pergunta,
                     PropertiesUtil.getMessage("popup.atencao"),
                     JOptionPane.YES_OPTION,
                     JOptionPane.INFORMATION_MESSAGE,
                     null,
                     null,
                     null);

               if (confirm == JOptionPane.YES_OPTION)
               {
                  Carga carga = ((InterfaceCarga) cmbCargasNavio.getSelectedItem()).getCargaVisualizada();
                  List<String> listaTipoProduto = new ArrayList<String>();
                  listaTipoProduto.add(produBalizaInicialSelecionada.getTipoProduto().toString());
                  controladorDSP.verificaEGerarLogBlendNaAtualizacao(listaTipoProduto, carga);
               }
            }

            if (txtNumeroPorao.getText().trim().equals(""))
            {
               throw new ValidacaoCampoException(PropertiesUtil.getMessage("mensagem.porao.numero.nao.informado"));
            }
         }
         else
         {
            // ... obtem o produto da baliza selecionada para recuperacao
            Produto produBalizaFinalSelecionada = pilhaOrigem.getListaDeBalizas().get(0).getProduto();

            // ... valida se o produto da baliza selecionada ? compat?vel com o produto da carga seleciondada
            if (produBalizaFinalSelecionada != null && !produBalizaFinalSelecionada.getTipoProduto().getIdTipoProduto().equals(produtoDaCarga.getTipoProduto().getIdTipoProduto()))
            {
               JLabel pergunta = new JLabel(PropertiesUtil.getMessage("mensagem.produto.incompativel.baliza.final"));
               pergunta.setFont(new Font("Tahoma", Font.PLAIN, 11));
               int confirm = JOptionPane.showOptionDialog(
                     this,
                     pergunta,
                     PropertiesUtil.getMessage("popup.atencao"),
                     JOptionPane.YES_OPTION,
                     JOptionPane.INFORMATION_MESSAGE,
                     null,
                     null,
                     null);

               if (confirm == JOptionPane.YES_OPTION)
               {
                  Carga carga = ((InterfaceCarga) cmbCargasNavio.getSelectedItem()).getCargaVisualizada();
                  List<String> listaTipoProduto = new ArrayList<String>();
                  listaTipoProduto.add(produBalizaFinalSelecionada.getTipoProduto().toString());
                  controladorDSP.verificaEGerarLogBlendNaAtualizacao(listaTipoProduto, carga);
               }
            }
         }
      }

      if (rdPatio.isSelected())
      {
         if (txtNomePilhaExistente.getText().trim().equals(""))
         {
            throw new ValidacaoCampoException(PropertiesUtil.getMessage("aviso.nome.pilha.vazio"));
         }
      }

      validaDataAtividade(DSSStockyardTimeUtil.criaDataComString(txtDataHoraInicio.getText(), PropertiesUtil.buscarPropriedade("formato.campo.datahora")));
   }

   private void desabilitaPainelMovimentacaoPatio()
   {
      cmbBalizaInicialPilhaDestino.setEnabled(false);
      cmbBalizaFinalPilhaDestino.setEnabled(false);
      txtNomePilhaExistente.setEnabled(false);
      cmbMaquinaEmpilhamento.setEnabled(false);
      cmbPatioDestino.setEnabled(false);
   }

   private void habilitaPainelMovimentacaoPatio()
   {
      cmbBalizaInicialPilhaDestino.setEnabled(true);
//      cmbBalizaFinalPilhaDestino.setEnabled(true);
      txtNomePilhaExistente.setEnabled(true);
      cmbMaquinaEmpilhamento.setEnabled(true);
      cmbPatioDestino.setEnabled(true);
   }

   /**
    * Monta o combo com todas as cargas do navio atracado selecionado para
    * atividade de atualizacao recuperacao
    */
   private void montaComboCargasNavio(InterfaceNavio interfaceNavio)
   {
      if (interfaceNavio != null)
      {
         cmbCargasNavio.removeAllItems();
         for (InterfaceCarga interfaceCarga : interfaceNavio.getListaDecarga())
         {
            cmbCargasNavio.addItem(interfaceCarga);
         }
      }
   }

   private void carregarInformacoesDestinoPatio()
   {
      cmbPatioDestino.removeAllItems();
      List<InterfacePatio> listaPatios = controladorDSP.getListaDePatios();
      for (InterfacePatio interfacePatio : listaPatios)
      {
    	  Patio patio = interfacePatio.getPatioVisualizado();
    	  cmbPatioDestino.addItem(patio);
      }
      Patio patioSelecionado = (Patio)cmbPatioDestino.getSelectedItem();

      carregaMaquinasQueAtendemPatio(patioSelecionado);
      
      carregaBalizasPatioDestino(patioSelecionado);
   
   }
   
   private void carregaBalizasPatioDestino(Patio patioSelecionado)
   {
      if (patioSelecionado != null)
      {
         cmbBalizaInicialPilhaDestino.removeAllItems();
         cmbBalizaFinalPilhaDestino.removeAllItems();
         Date dataAtual = null;
         try {
             dataAtual = DSSStockyardTimeUtil.criaDataComString(txtDataHoraInicio.getText(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
         } catch (ValidacaoCampoException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }
         for (Baliza balizas : patioSelecionado.getListaDeBalizas(dataAtual))
         {
            cmbBalizaInicialPilhaDestino.addItem(balizas);
            cmbBalizaFinalPilhaDestino.addItem(balizas);
         }
      }
   }
   
   private List<MaquinaDoPatio> retornaMaquinasQueAtendemPatio(Patio patioSelecionado)
   {
      List<MaquinaDoPatio> listaMaquinas = new ArrayList<MaquinaDoPatio>();
      
      if (patioSelecionado ==  null)
         return listaMaquinas;
      
      for (InterfaceCorreia interfaceCorreia : controladorDSP.getInterfaceDSP().getListaCorreias())
      {
         // verifica se a correia atende o patio selecionado
         if ((interfaceCorreia.getCorreiaVisualizada().getPatioInferior() != null && interfaceCorreia.getCorreiaVisualizada().getPatioInferior().equals(patioSelecionado)) ||
             (interfaceCorreia.getCorreiaVisualizada().getPatioSuperior() != null && interfaceCorreia.getCorreiaVisualizada().getPatioSuperior().equals(patioSelecionado)))
         {
            for (InterfaceMaquinaDoPatio interfaceMaquina : interfaceCorreia.getListaDeInterfaceMaquinas())
            {
               MaquinaDoPatio maquinaEmpilhamento = interfaceMaquina.getMaquinaDoPatioVisualizada();
               // verifica se a maquina é uma empilhadeira e se ela esta com estado de ociosa
               if ((maquinaEmpilhamento.getTipoDaMaquina().equals(TipoMaquinaEnum.EMPILHADEIRA) ||
                        maquinaEmpilhamento.getTipoDaMaquina().equals(TipoMaquinaEnum.EMPILHADEIRA_RECUPERADORA))) // &&
                        //maquinaEmpilhamento.getEstado().equals(EstadoMaquinaEnum.OCIOSA))
                  listaMaquinas.add(maquinaEmpilhamento); 
            }
         }
      }
      
      return listaMaquinas;
   }

   private void carregaInformacoesPilhaOrigem()
   {
      cmbBalizaInicialPilhaOrigem.removeAllItems();
      cmbBalizaFinalPilhaOrigem.removeAllItems();
      cmbBalizaInicialPilhaOrigem.setEnabled(true);
      cmbBalizaFinalPilhaOrigem.setEnabled(true);
      List<Baliza> listaBalizasPilha = pilhaOrigem.getListaDeBalizas();
      Collections.sort(listaBalizasPilha, new ComparadorBalizas());
      for (Baliza balizaPilha : listaBalizasPilha)
      {
         cmbBalizaInicialPilhaOrigem.addItem(balizaPilha);
         cmbBalizaFinalPilhaOrigem.addItem(balizaPilha);
      }
      cmbBalizaFinalPilhaOrigem.setSelectedIndex(cmbBalizaFinalPilhaOrigem.getItemCount()-1);
      cmbBalizaFinalPilhaOrigem.setEnabled(false);      
   }

   /**
    * Monta o combo com todos os navios atracados
    */
   private void montaComboNaviosAtracado()
   {
      cmbNavioAtracado.setEnabled(true);
      cmbNavioAtracado.removeAllItems();
      for (InterfacePier interfacePier : controladorDSP.getInterfaceInicial().getInterfaceInicial().getInterfaceFilaDeNavios().getListaDePiers())
      {
         for (InterfaceBerco interfaceBerco : interfacePier.getListaDeBercos())
         {
            if (interfaceBerco.getNavioAtendido() != null)
            {
               cmbNavioAtracado.addItem(interfaceBerco.getNavioAtendido());
            }
         }
      }
   }

   private void validaDadaHora(String strDataInicial, String strDataFinal) throws ValidacaoCampoException
   {
      Date dataInicial = DSSStockyardTimeUtil.criaDataComString(strDataInicial, PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
      Date dataFinal = DSSStockyardTimeUtil.criaDataComString(strDataFinal, PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
      if (dataFinal.before(dataInicial))
      {
         throw new ValidacaoCampoException(PropertiesUtil.getMessage("aviso.data.periodo.final.deve.ser.maior.que.periodo.inicial"));
      }
   }

   /**
    * verifica se a data inicial da atividade de recuperacao não é menor que a data da ultima situação de patio
    * @throws br.com.cflex.supervision.stockYard.cliente.exception.ValidacaoCampoException
    */
   private void validaDataAtividade(Date data) throws ValidacaoCampoException
   {
      data = DSSStockyardTimeUtil.verificaAtualizaDataSituacaoClone(data, controladorDSP.getInterfaceInicial().getPlanejamento().getControladorDePlano().obterSituacaoPatioFinal().getDtInicio());
      if (!controladorDSP.validaDAtaMenorUltimaSituacaoPatio(data))
      {
         throw new ValidacaoCampoException(PropertiesUtil.getMessage("aviso.mensagem.data.inicial.menor.que.data.situacao.patio"));
      }
   }

   /**
    * seta o status para operacao
    * @param pilha
    */
   private void modificaStatusPilha(Pilha pilha, EstadoMaquinaEnum estado)
   {
      for (Baliza baliza : pilha.getListaDeBalizas())
      {
         baliza.setEstado(estado);
      }
   }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbBalizaFinalPilhaDestino;
    private javax.swing.JComboBox cmbBalizaFinalPilhaOrigem;
    private javax.swing.JComboBox cmbBalizaInicialPilhaDestino;
    private javax.swing.JComboBox cmbBalizaInicialPilhaOrigem;
    private javax.swing.JComboBox cmbCargasNavio;
    private javax.swing.JComboBox cmbMaquinaEmpilhamento;
    private javax.swing.JComboBox cmbNavioAtracado;
    private javax.swing.JComboBox cmbPatioDestino;
    private javax.swing.JComboBox cmbPilhaOrigem;
    private javax.swing.JButton cmdConfirmar;
    private javax.swing.JButton cmdDesistir;
    private javax.swing.ButtonGroup grpOpcoesCarregamento;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblCorTipoProdutoPilhaExistente;
    private javax.swing.JPanel pnlInformacoesCargasNavio;
    private javax.swing.JPanel pnlInformacoesEmergencia;
    private javax.swing.JPanel pnlOpcoesCarregamento;
    private javax.swing.JPanel pnlPrincipal;
    private javax.swing.JRadioButton rdCarregamento;
    private javax.swing.JRadioButton rdPatio;
    private javax.swing.JTextField txtCorProdutoCargaNavio;
    private javax.swing.JTextField txtCorProdutoOrientacao;
    private javax.swing.JFormattedTextField txtDataHoraInicio;
    private javax.swing.JFormattedTextField txtDataHoraTermino;
    private javax.swing.JTextField txtNomePilhaExistente;
    private javax.swing.JTextField txtNumeroPorao;
    private javax.swing.JTextField txtOrientacaoEmbarque;
    private javax.swing.JTextField txtTaxa;
    // End of variables declaration//GEN-END:variables

   public Boolean getOperacaoCanceladaPeloUsuario()
   {
      return operacaoCanceladaPeloUsuario;
   }

   /**
    * obtem a baliza pelo numero da lista de balizas da pilha
    * @param numeroBaliza
    * @return
    */
   private Baliza obterBalizaPeloNumero(int numeroBaliza)
   {
      Baliza balizaEncontrada = null;
      for (Baliza baliza : pilhaOrigem.getListaDeBalizas())
      {
         if (baliza.getNumero().intValue() == numeroBaliza && baliza.getProduto() != null)
         {
            balizaEncontrada = baliza;
            break;
         }
      }
      return balizaEncontrada;
   }

   /**
    * obtem a baliza pelo numero da lista de balizas da pilha
    * @param numeroBaliza
    * @return
    */
   private Baliza obterBalizaPeloNumero(int numeroBaliza, Patio patio,Date horaSituacao )
   {
      Baliza balizaEncontrada = null;      
      for (Baliza baliza : patio.getListaDeBalizas(horaSituacao))
      {
         if (baliza.getNumero().intValue() == numeroBaliza)
         {
            balizaEncontrada = baliza;
            break;
         }
      }
      return balizaEncontrada;
   }

}
