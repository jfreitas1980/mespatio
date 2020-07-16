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
import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.comparadores.ComparadorInterfacePilha;
import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.cliente.util.numeros.DSSStockyardFuncoesNumeros;
import com.hdntec.gestao.domain.navios.entity.MetaCarga;
import com.hdntec.gestao.domain.navios.entity.MetaNavio;
import com.hdntec.gestao.domain.navios.entity.status.Carga;
import com.hdntec.gestao.domain.navios.entity.status.OrientacaoDeEmbarque;
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.entity.LugarEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.enums.SentidoEmpilhamentoRecuperacaoEnum;
import com.hdntec.gestao.domain.planta.entity.MetaBaliza;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.MaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Pilha;
import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;
import com.hdntec.gestao.domain.produto.entity.Produto;
import com.hdntec.gestao.domain.vo.atividades.AtualizarRecuperacaoVO;
import com.hdntec.gestao.exceptions.AtividadeException;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.ValidacaoCampoException;
import com.hdntec.gestao.exceptions.ValidacaoObjetosOperacaoException;
import com.hdntec.gestao.services.controladores.IControladorExecutarAtividadeRecuperacao;
import com.hdntec.gestao.services.controladores.impl.ControladorExecutarAtividadeRecuperacao;
import com.hdntec.gestao.util.PropertiesUtil;
import com.hdntec.gestao.util.datahora.DSSStockyardTimeUtil;

@SuppressWarnings("serial")
public class InterfaceAtualizacaoRecuperacao extends javax.swing.JDialog
{

   /** o controlador de acesso ao subsistema de DSP */
   ControladorDSP controladorDSP;

   /** A maquina do patio usada na recuperacao */
   MaquinaDoPatio maquinaDoPatioEditada;

   /** a interface da maquina do patio usada na recuperacao */
   InterfaceMaquinaDoPatio interfaceMaquinaDoPatio;

   /** Controla se a edicao foi cancelada ou nao */
   private Boolean operacaoCanceladaPeloUsuario = Boolean.FALSE;

   /** a interface de mensagem */
   private InterfaceMensagem interfaceMensagem;

   /** a quantidade total de produtos do range de balizas selecionadas */
   private Double quantidadeTotalProdutosBaliza;
   
   /** ativividade de recuperacao */
   private Atividade atividadeAtualizacaoRecuperacao;
   private Atividade atividadeExecutada;
   /** controla o acesso a segunda vez em que a interface é aberta para terminar a atividade*/
   private boolean segundoAcesso = Boolean.FALSE;

private Pilha pilhaAtual;

   public InterfaceAtualizacaoRecuperacao(java.awt.Frame parent, boolean modal)
   {
   	super(parent, modal);
   	initComponents();
   }

   public InterfaceAtualizacaoRecuperacao(java.awt.Frame parent, boolean modal, ControladorDSP controladorDSP, MaquinaDoPatio maquinaDoPatioEditada, InterfaceMaquinaDoPatio interfaceMaquinaDoPatio) throws ErroSistemicoException
   {
   	super(parent, modal);
   	initComponents();
   	quantidadeTotalProdutosBaliza = 0D;
   	this.controladorDSP = controladorDSP;
   	this.interfaceMaquinaDoPatio = interfaceMaquinaDoPatio;
   	this.maquinaDoPatioEditada = maquinaDoPatioEditada;
   	this.operacaoCanceladaPeloUsuario = Boolean.FALSE;
   	montaComboDePilhas();
   	carregaDadosIniciais();
   	montaComboNaviosAtracado();
   	if (interfaceMaquinaDoPatio.getMaquinaDoPatioVisualizada().getAtividade() != null && interfaceMaquinaDoPatio.getMaquinaDoPatioVisualizada().getEstado().equals(EstadoMaquinaEnum.OPERACAO))
   	{
   	   carregarInformacoesAtividade();
   	}
   }

   private void carregarInformacoesAtividade()
   {
   	 atividadeExecutada = maquinaDoPatioEditada.getMetaMaquina().existeRecuperacaoPendente();
      
   	
   //selecionando na combo de pilha origem a pilha origem
        	 
   	
   	 Collections.sort(atividadeExecutada.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaDeBalizas(),Baliza.comparadorBaliza);
        if (atividadeExecutada.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getSentido() == SentidoEmpilhamentoRecuperacaoEnum.SUL_PARA_NORTE) 
        {
           Collections.reverse(atividadeExecutada.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaDeBalizas());
        }
   	
   	MetaBaliza metaBalizaOperacao = atividadeExecutada.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaDeBalizas().get(0).getMetaBaliza();
   
   	 
     Baliza balizaOperacao = metaBalizaOperacao.retornaStatusHorario(atividadeExecutada.getDtInicio());
     
     int idxGerado = metaBalizaOperacao.getListaStatus().indexOf(balizaOperacao);
     
     balizaOperacao = metaBalizaOperacao.getListaStatus().get(idxGerado-1);
     
     
     
     pilhaAtual = balizaOperacao.retornaStatusHorario(atividadeExecutada.getDtInicio());
     //cmbPilhaOrigem.addItem(pilhaOrigem);
   	 
   	 
   	// carregando a pilha selecionada anteriormente
   	//InterfacePilha interfacePilhaAtual = interfaceMaquinaDoPatio.getPosicao().getInterfacePilha();
     cmbPilhasPatio.addItem(pilhaAtual);
     cmbPilhasPatio.setSelectedItem(pilhaAtual);
   	cmbPilhasPatio.setEnabled(false);

      // ... obtendo a baliza inicial da lista de balizas do lugar de empilhamento e recuperacao da atividade
      LugarEmpilhamentoRecuperacao lugarEmpRec = atividadeExecutada.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0);
      for  (Baliza b : lugarEmpRec.getListaDeBalizas()) {
    	  if (b.getRetirandoMaterial() == 1) {    		        
    		  Baliza interfaceBalizaInicial = obterBalizaPeloNumero(b.getNumero());
    		  cmbBalizaInicial.setSelectedItem(interfaceBalizaInicial);
    	      cmbBalizaInicial.setEnabled(false);
    	      break;
    	  }       	            
      }
      
      // ... setando a baliza inicial no combo
      
   	// ... carregando o combo de baliza final
   	cmbBalizaFinal.setEnabled(true);
   	montaComboBalizasPilha(pilhaAtual, cmbBalizaFinal);
   
   	// ... atualizando o combo do navio atracado com as informacoes da atividade que
   	// ... jah esta em execucao
   	cmbNavioAtracado.setEnabled(false);
    MetaNavio metaNavioAtividade = atividadeExecutada.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaCargas().get(0).getNavio(atividadeExecutada.getDtInicio()).getMetaNavio();

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
    MetaCarga metaCarga = atividadeExecutada.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaCargas().get(0).getMetaCarga();
   	
   	for (int i = 0; i < cmbCargasNavio.getItemCount(); i++)
   	{
   	   InterfaceCarga interfaceCarga = (InterfaceCarga) cmbCargasNavio.getItemAt(i);
   	   if (interfaceCarga.getCargaVisualizada().getMetaCarga().equals(metaCarga))
   	   {
   		cmbCargasNavio.setSelectedIndex(i);
   		break;
   	   }
   	}
   	
   	txtDataHoraFinalRecuperacao.setEnabled(true);
   	txtDataHoraFinalRecuperacao.setText(DSSStockyardTimeUtil.formatarData(DSSStockyardTimeUtil.obterDataHoraUltimaSituacaoParaAtividades(controladorDSP.getInterfaceInicial().getInterfaceInicial().getSituacaoPatioExibida().getDtInicio()), PropertiesUtil.buscarPropriedade("formato.campo.datahora")));
   	txtDataHoraFinalRecuperacao.setSelectionStart(11); // comeca a selecao no inicio da hora
   	txtDataHoraFinalRecuperacao.setSelectionEnd(txtDataHoraFinalRecuperacao.getText().length());
   	txtDataHoraFinalRecuperacao.requestFocus();	
   	txtDataHoraInicioRecuperacao.setEnabled(false);
   	txtDataHoraInicioRecuperacao.setText(DSSStockyardTimeUtil.formatarData(atividadeExecutada.getDtInicio(), PropertiesUtil.buscarPropriedade("formato.campo.datahora")));
   	
   	txtNumeroPorao.setEnabled(false);
   	txtNumeroPorao.setText(lugarEmpRec.getNomePorao());
   	
   	cmbCargasNavio.setEnabled(false);
   	//txtQuantidade.setEnabled(true);

      segundoAcesso = Boolean.TRUE;
   }

   /**
    * obtem a interface baliza pelo numero da lista de balizas da pilha
    * @param numeroBaliza
    * @return
    */
   private Baliza obterBalizaPeloNumero(int numeroBaliza)
   {
   	Baliza interfaceBalizaEncontrada = null;
   	if (cmbPilhasPatio.getSelectedIndex() > 0)
   	{
   	   List<Baliza> listaInterfaceBalizasPilha = ((Pilha) cmbPilhasPatio.getSelectedItem()).getListaDeBalizas();
   	   for (Baliza interfaceBaliza : listaInterfaceBalizasPilha)
   	   {
   		if (interfaceBaliza.getNumero().intValue() == numeroBaliza && interfaceBaliza.getProduto() != null)
   		{
   		   interfaceBalizaEncontrada = interfaceBaliza;
   		   break;
   		}
   	   }
   	}
   	return interfaceBalizaEncontrada;
   }

   public Boolean getOperacaoCanceladaPeloUsuario()
   {
      return operacaoCanceladaPeloUsuario;
   }

   /** This method is called from within the constructor to
    * initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is
    * always regenerated by the Form Editor.
    */
   @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      pnlInterfaceAtualizacaoRecuperacao = new javax.swing.JPanel();
      pnlInformacoesPilhaBaliza = new javax.swing.JPanel();
      jLabel2 = new javax.swing.JLabel();
      txtNomePatio = new javax.swing.JTextField();
      jLabel3 = new javax.swing.JLabel();
      jLabel4 = new javax.swing.JLabel();
      cmbPilhasPatio = new javax.swing.JComboBox();
      cmbBalizaInicial = new javax.swing.JComboBox();
      jLabel5 = new javax.swing.JLabel();
      cmbBalizaFinal = new javax.swing.JComboBox();
      jLabel6 = new javax.swing.JLabel();
      txtDataHoraInicioRecuperacao = new javax.swing.JFormattedTextField();
      MaskFormatter fmtHoraInicioRecuperacao = new MaskFormatter();
      try {
         fmtHoraInicioRecuperacao.setMask(PropertiesUtil.buscarPropriedade("mascara.campo.datahora"));
         fmtHoraInicioRecuperacao.setPlaceholderCharacter(' ');
         fmtHoraInicioRecuperacao.install(txtDataHoraInicioRecuperacao);
      } catch (ParseException pex) {
         pex.printStackTrace();
      }
      jLabel7 = new javax.swing.JLabel();
      txtDataHoraFinalRecuperacao = new javax.swing.JFormattedTextField();
      MaskFormatter fmtHoraFinalRecuperacao = new MaskFormatter();
      try {
         fmtHoraFinalRecuperacao.setMask(PropertiesUtil.buscarPropriedade("mascara.campo.datahora"));
         fmtHoraFinalRecuperacao.setPlaceholderCharacter(' ');
         fmtHoraFinalRecuperacao.install(txtDataHoraFinalRecuperacao);
      } catch (ParseException pex) {
         pex.printStackTrace();
      }
      txtCorProdutoBalizaInicial = new javax.swing.JTextField();
      txtCorProdutoBalizaFinal = new javax.swing.JTextField();
      pnlInformacoesCargasNavio = new javax.swing.JPanel();
      jLabel8 = new javax.swing.JLabel();
      cmbNavioAtracado = new javax.swing.JComboBox();
      jLabel9 = new javax.swing.JLabel();
      txtOrientacaoEmbarque = new javax.swing.JTextField();
      txtCorProdutoOrientacao = new javax.swing.JTextField();
      jLabel10 = new javax.swing.JLabel();
      cmbCargasNavio = new javax.swing.JComboBox();
      txtCorProdutoCargaNavio = new javax.swing.JTextField();
      jLabel1 = new javax.swing.JLabel();
      txtNumeroPorao = new javax.swing.JTextField();
      cmdConfirmar = new javax.swing.JButton();
      cmdDesistir = new javax.swing.JButton();

      setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
      setTitle("Atualizacão de Recuperação de Máquina");
      addWindowListener(new java.awt.event.WindowAdapter() {
         @Override
        public void windowClosing(java.awt.event.WindowEvent evt) {
            formWindowClosing(evt);
         }
      });

      pnlInformacoesPilhaBaliza.setBorder(javax.swing.BorderFactory.createTitledBorder("Informações sobre a recuperação"));

      jLabel2.setFont(new java.awt.Font("Arial", 1, 12));
      jLabel2.setText("Nome do pátio:");

      txtNomePatio.setEditable(false);
      txtNomePatio.setFont(new java.awt.Font("Arial", 0, 12));

      jLabel3.setFont(new java.awt.Font("Arial", 1, 12));
      jLabel3.setText("Baliza Inicial:");

      jLabel4.setFont(new java.awt.Font("Arial", 1, 12));
      jLabel4.setText("Pilhas do pátio:");

      cmbPilhasPatio.setFont(new java.awt.Font("Arial", 0, 12));
      cmbPilhasPatio.addItemListener(new java.awt.event.ItemListener() {
         public void itemStateChanged(java.awt.event.ItemEvent evt) {
            cmbPilhasPatioItemStateChanged(evt);
         }
      });

      cmbBalizaInicial.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
      cmbBalizaInicial.addItemListener(new java.awt.event.ItemListener() {
         public void itemStateChanged(java.awt.event.ItemEvent evt) {
            cmbBalizaInicialItemStateChanged(evt);
         }
      });

      jLabel5.setFont(new java.awt.Font("Arial", 1, 12));
      jLabel5.setText("Baliza final:");

      cmbBalizaFinal.setFont(new java.awt.Font("Arial", 0, 12));
      cmbBalizaFinal.addItemListener(new java.awt.event.ItemListener() {
         public void itemStateChanged(java.awt.event.ItemEvent evt) {
            cmbBalizaFinalItemStateChanged(evt);
         }
      });

      jLabel6.setFont(new java.awt.Font("Arial", 1, 12));
      jLabel6.setText("Data hora inicio recuperação:");

      jLabel7.setFont(new java.awt.Font("Arial", 1, 12));
      jLabel7.setText("Data hora término recuperação:");

      txtCorProdutoBalizaInicial.setEditable(false);
      txtCorProdutoBalizaInicial.setFont(new java.awt.Font("Arial", 0, 12));

      txtCorProdutoBalizaFinal.setEditable(false);
      txtCorProdutoBalizaFinal.setFont(new java.awt.Font("Arial", 0, 12));

      javax.swing.GroupLayout pnlInformacoesPilhaBalizaLayout = new javax.swing.GroupLayout(pnlInformacoesPilhaBaliza);
      pnlInformacoesPilhaBaliza.setLayout(pnlInformacoesPilhaBalizaLayout);
      pnlInformacoesPilhaBalizaLayout.setHorizontalGroup(
         pnlInformacoesPilhaBalizaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(pnlInformacoesPilhaBalizaLayout.createSequentialGroup()
            .addContainerGap()
            .addGroup(pnlInformacoesPilhaBalizaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(pnlInformacoesPilhaBalizaLayout.createSequentialGroup()
                  .addComponent(jLabel6)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(txtDataHoraInicioRecuperacao, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addGap(18, 18, 18)
                  .addComponent(jLabel7)
                  .addGap(6, 6, 6)
                  .addComponent(txtDataHoraFinalRecuperacao, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE))
               .addGroup(pnlInformacoesPilhaBalizaLayout.createSequentialGroup()
                  .addGroup(pnlInformacoesPilhaBalizaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                     .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlInformacoesPilhaBalizaLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbBalizaInicial, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                     .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlInformacoesPilhaBalizaLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNomePatio, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                  .addGroup(pnlInformacoesPilhaBalizaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                     .addGroup(pnlInformacoesPilhaBalizaLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbPilhasPatio, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                     .addGroup(pnlInformacoesPilhaBalizaLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCorProdutoBalizaInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbBalizaFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCorProdutoBalizaFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 156, Short.MAX_VALUE)))
            .addGap(10, 10, 10))
      );
      pnlInformacoesPilhaBalizaLayout.setVerticalGroup(
         pnlInformacoesPilhaBalizaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(pnlInformacoesPilhaBalizaLayout.createSequentialGroup()
            .addGroup(pnlInformacoesPilhaBalizaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel2)
               .addComponent(txtNomePatio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel4)
               .addComponent(cmbPilhasPatio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(18, 18, 18)
            .addGroup(pnlInformacoesPilhaBalizaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel3)
               .addComponent(cmbBalizaInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel5)
               .addComponent(cmbBalizaFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(txtCorProdutoBalizaFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(txtCorProdutoBalizaInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(18, 18, 18)
            .addGroup(pnlInformacoesPilhaBalizaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(txtDataHoraInicioRecuperacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel7)
               .addComponent(txtDataHoraFinalRecuperacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel6))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );

      pnlInformacoesCargasNavio.setBorder(javax.swing.BorderFactory.createTitledBorder("Seleção de informações navio"));

      jLabel8.setFont(new java.awt.Font("Arial", 1, 12));
      jLabel8.setText("Navio atracado:");

      cmbNavioAtracado.setFont(new java.awt.Font("Arial", 0, 12));
      cmbNavioAtracado.addItemListener(new java.awt.event.ItemListener() {
         public void itemStateChanged(java.awt.event.ItemEvent evt) {
            cmbNavioAtracadoItemStateChanged(evt);
         }
      });

      jLabel9.setFont(new java.awt.Font("Arial", 1, 12));
      jLabel9.setText("Orientação embarque:");

      txtOrientacaoEmbarque.setEditable(false);
      txtOrientacaoEmbarque.setFont(new java.awt.Font("Arial", 0, 12));

      txtCorProdutoOrientacao.setEditable(false);
      txtCorProdutoOrientacao.setFont(new java.awt.Font("Arial", 0, 12));

      jLabel10.setFont(new java.awt.Font("Arial", 1, 12));
      jLabel10.setText("Cargas do navio:");

      cmbCargasNavio.setFont(new java.awt.Font("Arial", 0, 12));
      cmbCargasNavio.addItemListener(new java.awt.event.ItemListener() {
         public void itemStateChanged(java.awt.event.ItemEvent evt) {
            cmbCargasNavioItemStateChanged(evt);
         }
      });

      txtCorProdutoCargaNavio.setEditable(false);
      txtCorProdutoCargaNavio.setFont(new java.awt.Font("Arial", 0, 12));

      jLabel1.setFont(new java.awt.Font("Arial", 1, 12));
      jLabel1.setText("Número do porão:");

      txtNumeroPorao.setFont(new java.awt.Font("Arial", 0, 12));

      javax.swing.GroupLayout pnlInformacoesCargasNavioLayout = new javax.swing.GroupLayout(pnlInformacoesCargasNavio);
      pnlInformacoesCargasNavio.setLayout(pnlInformacoesCargasNavioLayout);
      pnlInformacoesCargasNavioLayout.setHorizontalGroup(
         pnlInformacoesCargasNavioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(pnlInformacoesCargasNavioLayout.createSequentialGroup()
            .addContainerGap()
            .addGroup(pnlInformacoesCargasNavioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(pnlInformacoesCargasNavioLayout.createSequentialGroup()
                  .addComponent(jLabel10)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(cmbCargasNavio, 0, 261, Short.MAX_VALUE)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(txtCorProdutoCargaNavio, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addGap(24, 24, 24))
               .addGroup(pnlInformacoesCargasNavioLayout.createSequentialGroup()
                  .addComponent(jLabel1)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(txtNumeroPorao, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                  .addComponent(jLabel8)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(cmbNavioAtracado, 0, 176, Short.MAX_VALUE)))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel9)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(txtOrientacaoEmbarque, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(txtCorProdutoOrientacao, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
      );
      pnlInformacoesCargasNavioLayout.setVerticalGroup(
         pnlInformacoesCargasNavioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(pnlInformacoesCargasNavioLayout.createSequentialGroup()
            .addContainerGap()
            .addGroup(pnlInformacoesCargasNavioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel1)
               .addComponent(txtNumeroPorao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(cmbNavioAtracado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel8)
               .addComponent(jLabel9)
               .addComponent(txtOrientacaoEmbarque, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(txtCorProdutoOrientacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(18, 18, 18)
            .addGroup(pnlInformacoesCargasNavioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel10)
               .addComponent(cmbCargasNavio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(txtCorProdutoCargaNavio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );

      cmdConfirmar.setFont(new java.awt.Font("Arial", 1, 14));
      StringBuffer value = new StringBuffer();
      value.append(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens")).append("/ok.png");
      cmdConfirmar.setIcon(new javax.swing.ImageIcon(value.toString())); // NOI18N
      cmdConfirmar.setText("Confirmar");
      cmdConfirmar.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            cmdConfirmarActionPerformed(evt);
         }
      });

      cmdDesistir.setFont(new java.awt.Font("Arial", 1, 14));

      value = new StringBuffer();
      value.append(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens")).append("/exit.png");
      cmdDesistir.setIcon(new javax.swing.ImageIcon(value.toString())); // NOI18N
      cmdDesistir.setText("Desistir");
      cmdDesistir.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            cmdDesistirActionPerformed(evt);
         }
      });

      javax.swing.GroupLayout pnlInterfaceAtualizacaoRecuperacaoLayout = new javax.swing.GroupLayout(pnlInterfaceAtualizacaoRecuperacao);
      pnlInterfaceAtualizacaoRecuperacao.setLayout(pnlInterfaceAtualizacaoRecuperacaoLayout);
      pnlInterfaceAtualizacaoRecuperacaoLayout.setHorizontalGroup(
         pnlInterfaceAtualizacaoRecuperacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(pnlInterfaceAtualizacaoRecuperacaoLayout.createSequentialGroup()
            .addContainerGap()
            .addGroup(pnlInterfaceAtualizacaoRecuperacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(pnlInformacoesPilhaBaliza, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
               .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlInterfaceAtualizacaoRecuperacaoLayout.createSequentialGroup()
                  .addComponent(cmdDesistir)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                  .addComponent(cmdConfirmar))
               .addComponent(pnlInformacoesCargasNavio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
      );
      pnlInterfaceAtualizacaoRecuperacaoLayout.setVerticalGroup(
         pnlInterfaceAtualizacaoRecuperacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(pnlInterfaceAtualizacaoRecuperacaoLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(pnlInformacoesPilhaBaliza, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(pnlInformacoesCargasNavio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(pnlInterfaceAtualizacaoRecuperacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(cmdConfirmar)
               .addComponent(cmdDesistir))
            .addContainerGap(30, Short.MAX_VALUE))
      );

      getContentPane().add(pnlInterfaceAtualizacaoRecuperacao, java.awt.BorderLayout.CENTER);

      pack();
   }// </editor-fold>//GEN-END:initComponents

   private void cmbPilhasPatioItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_cmbPilhasPatioItemStateChanged
   {//GEN-HEADEREND:event_cmbPilhasPatioItemStateChanged
	if (cmbPilhasPatio.getSelectedIndex() > 0)
	{
	   Pilha interfacePilhaSelecionada = (Pilha) cmbPilhasPatio.getSelectedItem();
	   montaComboBalizasPilha(interfacePilhaSelecionada, cmbBalizaInicial);
	}
	else
	{
	   cmbBalizaInicial.removeAllItems();
	   cmbBalizaFinal.removeAllItems();
	}
   }//GEN-LAST:event_cmbPilhasPatioItemStateChanged

   private void cmbBalizaInicialItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_cmbBalizaInicialItemStateChanged
   {//GEN-HEADEREND:event_cmbBalizaInicialItemStateChanged
	if (cmbPilhasPatio.getSelectedIndex() > 0)
	{
	   Baliza interfaceBalizaSelecionada = (Baliza) cmbBalizaInicial.getSelectedItem();
	   if (interfaceBalizaSelecionada != null)
	   {
		String[] rgbProdutoBalizaSelecionada = interfaceBalizaSelecionada.getProduto().getTipoProduto().getCorIdentificacao().split(",");
		txtCorProdutoBalizaInicial.setBackground(new Color(Integer.parseInt(rgbProdutoBalizaSelecionada[0]), Integer.parseInt(rgbProdutoBalizaSelecionada[1]), Integer.parseInt(rgbProdutoBalizaSelecionada[2])));
	   }
	}
   }//GEN-LAST:event_cmbBalizaInicialItemStateChanged

   private void cmbBalizaFinalItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_cmbBalizaFinalItemStateChanged
   {//GEN-HEADEREND:event_cmbBalizaFinalItemStateChanged
	if (cmbPilhasPatio.getSelectedIndex() > 0 && cmbBalizaFinal.getItemCount() > 0)
	{
	   Baliza interfaceBalizaSelecionada = (Baliza) cmbBalizaFinal.getSelectedItem();
	   if (interfaceBalizaSelecionada != null)
	   {
		String[] rgbProdutoBalizaSelecionada = interfaceBalizaSelecionada.getProduto().getTipoProduto().getCorIdentificacao().split(",");
		txtCorProdutoBalizaFinal.setBackground(new Color(Integer.parseInt(rgbProdutoBalizaSelecionada[0]), Integer.parseInt(rgbProdutoBalizaSelecionada[1]), Integer.parseInt(rgbProdutoBalizaSelecionada[2])));
		carregaQuantidadeBalizas();
	   }
	}
   }//GEN-LAST:event_cmbBalizaFinalItemStateChanged

   private void carregaQuantidadeBalizas()
   {
      if (cmbBalizaInicial.getSelectedItem() == null || cmbBalizaFinal.getSelectedItem() == null)
         return;
      
   	// ... obtendo o numero da baliza inicial e final
   	int numeroBalizaInicial = ((Baliza) cmbBalizaInicial.getSelectedItem()).getNumero();
   	int numeroBalizaFinal = ((Baliza) cmbBalizaFinal.getSelectedItem()).getNumero();
   	
   	int maiorBaliza = Math.max(numeroBalizaInicial, numeroBalizaFinal);
   	int menorBaliza = Math.min(numeroBalizaInicial, numeroBalizaFinal);
   
   	// ... percorrendo o range de balizas e somando as quantidades dos produtos das mesmas
   	quantidadeTotalProdutosBaliza = 0D;
   	for (int i = menorBaliza; i <= maiorBaliza; i++)
   	{
   	   Baliza interfaceBaliza = obterBalizaPeloNumero(i);
   	   quantidadeTotalProdutosBaliza += DSSStockyardFuncoesNumeros.arredondaValor(interfaceBaliza.getProduto().getQuantidade(),2);
   	}
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

         if(interfaceCargaSelecionada.getCargaVisualizada().getOrientacaoDeEmbarque() != null){
        	 txtOrientacaoEmbarque.setText(interfaceCargaSelecionada.getCargaVisualizada().getOrientacaoDeEmbarque().getTipoProduto().toString());
        	 String[] rgbProdutoOrientacao = interfaceCargaSelecionada.getCargaVisualizada().getOrientacaoDeEmbarque().getTipoProduto().getCorIdentificacao().split(",");
        	 txtCorProdutoOrientacao.setBackground(new Color(Integer.parseInt(rgbProdutoOrientacao[0]), Integer.parseInt(rgbProdutoOrientacao[1]), Integer.parseInt(rgbProdutoOrientacao[2])));
         }
      }
   }//GEN-LAST:event_cmbCargasNavioItemStateChanged

   private void cmdConfirmarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmdConfirmarActionPerformed
   {//GEN-HEADEREND:event_cmdConfirmarActionPerformed

	   atualizaRecuperacao();

}//GEN-LAST:event_cmdConfirmarActionPerformed

   private void atualizaRecuperacao(){
	   Date inicio = null;
       Date fim = null;
       Date dataStatus = null;
       double diferencaHoras = 0;
       double tempoValidacaoHorasFutura = 0;
	   Boolean finalizar = Boolean.FALSE;
       try {
               if (validarAtividadeAtualizacaoRecuperacao()) {                   
                   //validacao dos campos de data
                   if (segundoAcesso) {
                	   finalizar = Boolean.TRUE;
                       validaDadaHora(txtDataHoraInicioRecuperacao.getText(), txtDataHoraFinalRecuperacao.getText());                                              
                       fim = validaDataAtividade(txtDataHoraFinalRecuperacao.getText());
                       dataStatus = fim;
                       diferencaHoras = DSSStockyardTimeUtil.diferencaEmHoras(controladorDSP.getInterfaceInicial().getInterfaceInicial().getSituacaoPatioExibida().getDtInicio(), fim);
                   } else {
                	   inicio = validaDataAtividade(txtDataHoraInicioRecuperacao.getText());
                	   diferencaHoras = DSSStockyardTimeUtil.diferencaEmHoras(controladorDSP.getInterfaceInicial().getInterfaceInicial().getSituacaoPatioExibida().getDtInicio(), inicio);
                	   dataStatus= inicio;                	
                   }
                    
                    tempoValidacaoHorasFutura = new Double(PropertiesUtil.buscarPropriedade("quantidade.horas.aviso.data.futura.atividades"));

                   if (diferencaHoras >= tempoValidacaoHorasFutura) {
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

                       if (confirm == JOptionPane.NO_OPTION) {
                           if (txtDataHoraInicioRecuperacao.isEnabled()) {
                               txtDataHoraInicioRecuperacao.requestFocus();
                           } else {
                               txtDataHoraFinalRecuperacao.requestFocus();
                           }
                           return;
                       }
                   }
                   
                   visibilidadeCampos(false);
                   this.repaint();

                   
                   AtualizarRecuperacaoVO empilhamentoVO = new AtualizarRecuperacaoVO(); 
                   
                   
                   if (finalizar) {
                	   fim = Atividade.verificaAtualizaDataAtividade(fim, controladorDSP.getInterfaceInicial().getPlanejamento().getControladorDePlano().obterSituacaoPatioFinal().getDtInicio());
                	   empilhamentoVO.setDataInicio(atividadeExecutada.getDtInicio());
                	   empilhamentoVO.setDataFim(fim);                 	   
                   } else {                	   
                	   inicio = Atividade.verificaAtualizaDataAtividade(inicio, controladorDSP.getInterfaceInicial().getPlanejamento().getControladorDePlano().obterSituacaoPatioFinal().getDtInicio());   
                	   empilhamentoVO.setDataInicio(inicio);
                   }

                   empilhamentoVO.setAtividadeAnterior(atividadeExecutada);
                   
                   // Carga
                   // seta a carga
                   InterfaceCarga interfaceCargaSelecionada = (InterfaceCarga) cmbCargasNavio.getSelectedItem();
                   MetaCarga metaCarga= interfaceCargaSelecionada.getCargaVisualizada().getMetaCarga();

                   
                   int numeroBalizaInicial;
                   int numeroBalizaFinal;
                   List<MetaBaliza> listaBalizasSelecionadas = new ArrayList<MetaBaliza>();
                   if (pilhaAtual == null) {
                      pilhaAtual = ((Baliza) cmbBalizaInicial.getSelectedItem()).retornaStatusHorario(dataStatus);
                   }   
                   if (finalizar) {                   	                   
                       numeroBalizaInicial = ((Baliza) cmbBalizaInicial.getSelectedItem()).getNumero();
                       numeroBalizaFinal = ((Baliza) cmbBalizaFinal.getSelectedItem()).getNumero();
                       int maiorBaliza = Math.max(numeroBalizaInicial, numeroBalizaFinal);
                       int menorBaliza = Math.min(numeroBalizaInicial, numeroBalizaFinal);

                       // define a lista de balizas alteradas nesta atividade
                       
                       for (int i = menorBaliza; i <= maiorBaliza; i++) {
                           Baliza interfaceBaliza = obterBalizaPeloNumero(i);                           
                           listaBalizasSelecionadas.add(interfaceBaliza.getMetaBaliza());                           
                       }
                       
                       //seta o sentido da recuperacao
                       if (numeroBalizaFinal > numeroBalizaInicial) {
                    	   empilhamentoVO.setSentidoRecuperacao(SentidoEmpilhamentoRecuperacaoEnum.NORTE_PARA_SUL);
                       } else {
                    	   empilhamentoVO.setSentidoRecuperacao(SentidoEmpilhamentoRecuperacaoEnum.SUL_PARA_NORTE);                    	   
                       }
                   } else {
                       // ainda nao fechou a atividade, inclui apenas a primeira baliza do intervalo
                       numeroBalizaInicial = ((Baliza) cmbBalizaInicial.getSelectedItem()).getNumero();
                       listaBalizasSelecionadas.add(obterBalizaPeloNumero(numeroBalizaInicial).getMetaBaliza());
                   }	   
                   
                   
                   
                   empilhamentoVO.setNomePilha(pilhaAtual.getNomePilha());            
                   empilhamentoVO.getListaBalizas().addAll(listaBalizasSelecionadas);
                   empilhamentoVO.setMetaCarga(metaCarga);                   
                   empilhamentoVO.setCliente(pilhaAtual.getCliente());                               
                   empilhamentoVO.getListaMaquinas().add(maquinaDoPatioEditada.getMetaMaquina());
                   empilhamentoVO.setNomePorao(txtNumeroPorao.getText());
                   empilhamentoVO.setTipoProduto(pilhaAtual.getListaDeBalizas().get(0).getProduto().getTipoProduto());
                   
                   IControladorExecutarAtividadeRecuperacao service = ControladorExecutarAtividadeRecuperacao.getInstance();
                   List<LugarEmpilhamentoRecuperacao> lugaresAnteriores = new ArrayList<LugarEmpilhamentoRecuperacao>();
                   try {
                	   
                	   atividadeAtualizacaoRecuperacao =  service.recuperar(empilhamentoVO,lugaresAnteriores);
				} catch (AtividadeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                   controladorDSP.getInterfaceInicial().atualizarRecuperacao(atividadeAtualizacaoRecuperacao);
                   // atualiza no controlador de plano o plano de empilhamento e recuperacao do usuario que acabou de ser atualizado pelo MES
                   controladorDSP.getInterfaceInicial().getInterfaceInicial().atualizarDSP();
                   operacaoCanceladaPeloUsuario = Boolean.FALSE;
                   
                   // chegando ate o JDialog
                   setVisible(false);
               }

           } catch (ValidacaoObjetosOperacaoException ex) {
               controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
               interfaceMensagem = new InterfaceMensagem();
               interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
               interfaceMensagem.setDescricaoMensagem(ex.getMessage());
               controladorDSP.ativarMensagem(interfaceMensagem);
//            Logger.getLogger(InterfaceAtualizacaoRecuperacao.class.getName()).log(Level.SEVERE, null, ex);
           } catch (ValidacaoCampoException vcEx) {
               controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
               interfaceMensagem = new InterfaceMensagem();
               interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
               interfaceMensagem.setDescricaoMensagem(vcEx.getMessage());
               controladorDSP.ativarMensagem(interfaceMensagem);
           }
           finally {
        	   visibilidadeCampos(true);
           }
   }


   private void cmdDesistirActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmdDesistirActionPerformed
   {//GEN-HEADEREND:event_cmdDesistirActionPerformed
   	operacaoCanceladaPeloUsuario = true;
   	setVisible(false);
   }//GEN-LAST:event_cmdDesistirActionPerformed

   private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
   {//GEN-HEADEREND:event_formWindowClosing
      operacaoCanceladaPeloUsuario = true;
   }//GEN-LAST:event_formWindowClosing

   /**
    * Monta o combo com todas as balizas da pilha selecionada
    */
   private void montaComboBalizasPilha(Pilha interfacePilhaSelecionada, JComboBox cmbInterfaceBaliza)
   {
   	cmbInterfaceBaliza.removeAllItems();
   	Collections.sort(interfacePilhaSelecionada.getListaDeBalizas(), Baliza.comparadorBaliza);
   	for (Baliza balizaDaPilha : interfacePilhaSelecionada.getListaDeBalizas())
   	{
   	        cmbInterfaceBaliza.addItem(balizaDaPilha);       
   	}
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

   /**
    * Monta o combo com todas as pilhas que estao no patio onde
    * a lanca da maquina esta posicionada
    */
   private void montaComboDePilhas()
   {
	cmbPilhasPatio.setEnabled(true);
	cmbPilhasPatio.removeAllItems();

	// ... adiciona no elemento zero do combo a opcao de Selecionar
	cmbPilhasPatio.addItem(PropertiesUtil.getMessage("combo.selecionar"));

	InterfacePatio interfacePatioDaMaquina = interfaceMaquinaDoPatio.getPosicao().getInterfacePatio();
	Collections.sort(interfacePatioDaMaquina.getListaDePilhas(), new ComparadorInterfacePilha());
	for (InterfacePilha interfacePilha : interfacePatioDaMaquina.getListaDePilhas())
	{
	   cmbPilhasPatio.addItem(interfacePilha.getPilhaVisualizada());
	}
   }

   /**
    * Carrega algums dados iniciais para inicializacao da atividade
    * de recuperacao
    */
   private void carregaDadosIniciais()
   {
   	cmbBalizaFinal.setEnabled(false);
   	txtDataHoraFinalRecuperacao.setEnabled(false);
   
   	txtDataHoraInicioRecuperacao.setText(DSSStockyardTimeUtil.formatarData(DSSStockyardTimeUtil.obterDataHoraUltimaSituacaoParaAtividades(controladorDSP.getInterfaceInicial().getInterfaceInicial().getSituacaoPatioExibida().getDtInicio()), PropertiesUtil.buscarPropriedade("formato.campo.datahora")));
   	txtDataHoraInicioRecuperacao.setSelectionStart(11); // comeca a selecao no inicio da hora
   	txtDataHoraInicioRecuperacao.setSelectionEnd(txtDataHoraInicioRecuperacao.getText().length());
   	txtDataHoraInicioRecuperacao.requestFocus();
   
   	txtDataHoraFinalRecuperacao.setText("");
   	txtNomePatio.setText(maquinaDoPatioEditada.getPosicao().getPatio().getNomePatio());

   }

   /**
    * Monta o combo com todas as cargas do navio atracado selecionado para
    * atividade de atualizacao recuperacao
    */
   private void montaComboCargasNavio(InterfaceNavio interfaceNavio)
   {
   	cmbCargasNavio.removeAllItems();
   	for (InterfaceCarga interfaceCarga : interfaceNavio.getListaDecarga())
   	{
   	   cmbCargasNavio.addItem(interfaceCarga);
   	}
   }

   /**
    * Metodo que valida as informacoes digitadas pelo usuario para uma atividade de atualizacao
    * de recuperacao
    * @throws br.com.cflex.supervision.stockYard.cliente.exception.ValidacaoCampoException
    */
   private boolean validarAtividadeAtualizacaoRecuperacao() throws ValidacaoCampoException
   {
    boolean dadosValidos = Boolean.TRUE;

    //validando a pilha selecionada
    if(cmbPilhasPatio.getSelectedIndex() <= 0 || cmbPilhasPatio.getItemCount() <= 0 ) {
    	throw new ValidacaoCampoException(PropertiesUtil.getMessage("aviso.pilha.para.recuperacao.nao.encontrada"));
    }
    //validando a baliza inicial selecionada
    if(cmbBalizaInicial.getSelectedItem() == null || cmbBalizaInicial.getItemCount() <= 0 ) {
    	throw new ValidacaoCampoException(PropertiesUtil.getMessage("aviso.baliza.inicial.para.recuperacao.nao.encontrada"));
    }
	if (txtNumeroPorao.getText().trim().equals(""))
	{
	   throw new ValidacaoCampoException(PropertiesUtil.getMessage("mensagem.porao.numero.nao.informado"));
	}
    if(cmbNavioAtracado.getSelectedItem() == null || cmbNavioAtracado.getItemCount() == 0 )
    {//verifica se existe algum navio carregado no combo
    	throw new ValidacaoCampoException(PropertiesUtil.getMessage("aviso.navio.para.recuperacao.nao.encontrado"));
    }
    else if(cmbCargasNavio.getSelectedItem() == null || cmbCargasNavio.getItemCount() == 0 )
    {//verifica se existe carga para este navio
    	throw new ValidacaoCampoException(PropertiesUtil.getMessage("aviso.carga.para.navio.nao.encontrada"));
    }

   	// ... obtem o produto da carga selecionada do navio atracado selecionado
   	Produto produtoDaCarga = ((InterfaceCarga) cmbCargasNavio.getSelectedItem()).getCargaVisualizada().getProduto();
   	// ... obtem a orientacao de embarque da carga selecionada do navio atracado selecionado
   	OrientacaoDeEmbarque orientacaoEmbarque = ((InterfaceCarga) cmbCargasNavio.getSelectedItem()).getCargaVisualizada().getOrientacaoDeEmbarque();
   	if(orientacaoEmbarque == null){
   		throw new ValidacaoCampoException(PropertiesUtil.getMessage("aviso.orientacao.embarque.nao.encontrada"));
   	}
   
   	if (atividadeExecutada == null || maquinaDoPatioEditada.getEstado().equals(EstadoMaquinaEnum.OCIOSA))
   	{
   	   // ... obtem o produto da baliza selecionada para recuperacao
   	   Produto produBalizaInicialSelecionada = null;
   	   if (cmbPilhasPatio.getSelectedIndex() > 0)
   	   {
   		produBalizaInicialSelecionada = ((Baliza) cmbBalizaInicial.getSelectedItem()).getProduto();
   	   }
   
   	   // ... valida se o produto da baliza selecionada é compativel com o produto da carga seleciondada
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
   
   		if (confirm != JOptionPane.YES_OPTION)
   		{
		   dadosValidos = Boolean.FALSE;
   		}
            else if(confirm == JOptionPane.YES_OPTION){
               Carga carga = ((InterfaceCarga) cmbCargasNavio.getSelectedItem()).getCargaVisualizada();
               List<String> listaTipoProduto = new ArrayList<String>();
               listaTipoProduto.add(produBalizaInicialSelecionada.getTipoProduto().toString());
               controladorDSP.verificaEGerarLogBlendNaAtualizacao(listaTipoProduto, carga);
            }
	   }
	}
	else
	{
	   // ... obtem o produto da baliza selecionada para recuperacao
	   Produto produBalizaFinalSelecionada = null;
	   if (cmbPilhasPatio.getSelectedIndex() > 0)
	   {
	      produBalizaFinalSelecionada = ((Baliza) cmbBalizaFinal.getSelectedItem()).getProduto();
	   }

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

		if (confirm != JOptionPane.YES_OPTION)
		{
		   dadosValidos = Boolean.FALSE;
		}
            else if (confirm == JOptionPane.YES_OPTION) {
               Carga carga = ((InterfaceCarga) cmbCargasNavio.getSelectedItem()).getCargaVisualizada();
               List<String> listaTipoProduto = new ArrayList<String>();
               listaTipoProduto.add(produBalizaFinalSelecionada.getTipoProduto().toString());
               controladorDSP.verificaEGerarLogBlendNaAtualizacao(listaTipoProduto, carga);
            }
	   }
	}
   	
    return dadosValidos;

   }

   private void validaDadaHora(String strDataInicial, String strDataFinal) throws ValidacaoCampoException
   {
       Date dataInicial = DSSStockyardTimeUtil.criaDataComString( strDataInicial, PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
       Date dataFinal = DSSStockyardTimeUtil.criaDataComString( strDataFinal, PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
       
       dataInicial = DSSStockyardTimeUtil.verificaAtualizaDataSituacaoCloneRecuperacao(dataInicial, controladorDSP.getInterfaceInicial().getPlanejamento().getControladorDePlano().obterSituacaoPatioFinal().getDtInicio());
       dataFinal = DSSStockyardTimeUtil.verificaAtualizaDataSituacaoCloneRecuperacao(dataFinal, dataInicial);
       
       if(dataFinal.before(dataInicial) ||  dataFinal.equals(dataInicial)) {
           throw new ValidacaoCampoException(PropertiesUtil.getMessage("aviso.data.periodo.final.deve.ser.maior.que.periodo.inicial"));
       }
   }

   /**
    * seta o status para operacao
    * @param pilha
    */
   private void modificaStatusPilha(Pilha pilha, EstadoMaquinaEnum estado){
       for(Baliza baliza : pilha.getListaDeBalizas()){
           baliza.setEstado(estado);
       }
   }

   /**
    * verifica se a data inicial da atividade de recuperacao não é menor que a data da ultima situação de patio
    * @throws br.com.cflex.supervision.stockYard.cliente.exception.ValidacaoCampoException
    */
   private Date validaDataAtividade(String value)throws ValidacaoCampoException{
       Date data = DSSStockyardTimeUtil.criaDataComString( value, PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
       data = DSSStockyardTimeUtil.verificaAtualizaDataSituacaoCloneRecuperacao(data, controladorDSP.getInterfaceInicial().getPlanejamento().getControladorDePlano().obterSituacaoPatioFinal().getDtInicio());
       if(!controladorDSP.validaDAtaMenorUltimaSituacaoPatio(data )){
           throw new ValidacaoCampoException(PropertiesUtil.getMessage("aviso.mensagem.data.inicial.menor.que.data.situacao.patio"));
       }
       return data;
   }
   
   /**
    * modifica o status dos botoes "enabled" para true ou false
    * @param visivel
    */
   private void visibilidadeBotoes(boolean visivel){
	   this.cmdConfirmar.setEnabled(visivel);
	   this.cmdDesistir.setEnabled(visivel);
   }
   
   private void visibilidadeCampos(boolean visivel) {
	   cmbBalizaFinal.setEnabled(visivel);
	   cmbBalizaInicial.setEnabled(visivel);
	   cmbCargasNavio.setEnabled(visivel);
	   cmbNavioAtracado.setEnabled(visivel);
	   cmbPilhasPatio.setEnabled(visivel);
	   txtCorProdutoBalizaFinal.setEnabled(visivel);
	   txtCorProdutoBalizaInicial.setEnabled(visivel);
	   txtCorProdutoCargaNavio.setEnabled(visivel);
	   txtCorProdutoOrientacao.setEnabled(visivel);
	   txtDataHoraFinalRecuperacao.setEnabled(visivel);
	   txtDataHoraInicioRecuperacao.setEnabled(visivel);
	   txtNomePatio.setEnabled(visivel);
	   txtNumeroPorao.setEnabled(visivel);
	   txtOrientacaoEmbarque.setEnabled(visivel);
	   visibilidadeBotoes(visivel);
   }
   

   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JComboBox cmbBalizaFinal;
   private javax.swing.JComboBox cmbBalizaInicial;
   private javax.swing.JComboBox cmbCargasNavio;
   private javax.swing.JComboBox cmbNavioAtracado;
   private javax.swing.JComboBox cmbPilhasPatio;
   private javax.swing.JButton cmdConfirmar;
   private javax.swing.JButton cmdDesistir;
   private javax.swing.JLabel jLabel1;
   private javax.swing.JLabel jLabel10;
   private javax.swing.JLabel jLabel2;
   private javax.swing.JLabel jLabel3;
   private javax.swing.JLabel jLabel4;
   private javax.swing.JLabel jLabel5;
   private javax.swing.JLabel jLabel6;
   private javax.swing.JLabel jLabel7;
   private javax.swing.JLabel jLabel8;
   private javax.swing.JLabel jLabel9;
   private javax.swing.JPanel pnlInformacoesCargasNavio;
   private javax.swing.JPanel pnlInformacoesPilhaBaliza;
   private javax.swing.JPanel pnlInterfaceAtualizacaoRecuperacao;
   private javax.swing.JTextField txtCorProdutoBalizaFinal;
   private javax.swing.JTextField txtCorProdutoBalizaInicial;
   private javax.swing.JTextField txtCorProdutoCargaNavio;
   private javax.swing.JTextField txtCorProdutoOrientacao;
   private javax.swing.JFormattedTextField txtDataHoraFinalRecuperacao;
   private javax.swing.JFormattedTextField txtDataHoraInicioRecuperacao;
   private javax.swing.JTextField txtNomePatio;
   private javax.swing.JTextField txtNumeroPorao;
   private javax.swing.JTextField txtOrientacaoEmbarque;
   // End of variables declaration//GEN-END:variables

}
