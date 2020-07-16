package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio;

import java.awt.Font;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.MaskFormatter;
import javax.swing.text.PlainDocument;

import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.cliente.util.numeros.DSSStockyardFuncoesNumeros;
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.entity.LugarEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.enums.TipoAtividadeEnum;
import com.hdntec.gestao.domain.planta.entity.MetaBaliza;
import com.hdntec.gestao.domain.planta.entity.MetaMaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.MetaPatio;
import com.hdntec.gestao.domain.planta.entity.MetaUsina;
import com.hdntec.gestao.domain.planta.entity.status.AtividadeCampanha;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.Campanha;
import com.hdntec.gestao.domain.planta.entity.status.Usina;
import com.hdntec.gestao.domain.planta.enums.EnumTipoBaliza;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.domain.vo.atividades.EmpilhamentoEmergenciaVO;
import com.hdntec.gestao.exceptions.AtividadeException;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.ValidacaoCampoException;
import com.hdntec.gestao.exceptions.ValidacaoObjetosOperacaoException;
import com.hdntec.gestao.services.controladores.impl.ControladorExecutarAtividadeEmpilhamentoEmergencia;
import com.hdntec.gestao.util.PropertiesUtil;
import com.hdntec.gestao.util.datahora.DSSStockyardTimeUtil;

public class InterfaceAtualizacaoPilhaEmergencia extends javax.swing.JDialog
{
   /** Controlador de operações do DSP */
   private ControladorDSP controladorDSP;
   private Map<Integer,Campanha> mapComboCampanha;  
   private Map<MetaPatio,Map<EnumTipoBaliza,JCheckBox>> mapBalizaSelecionaveis;
   private Map<MetaPatio,List<MetaBaliza>> mapBalizaSelecionadaAtividade;
   
   /** A interface da Usina utilizada para atualizacao */
   private InterfaceUsina interfaceUsina;

   /** A usina utilizada na atualizacao */
   private Usina usinaSelecionada;

   /** Identificador de operacao cancelada */
   private Boolean operacaoCanceladaPeloUsuario;

   /** A interface mensagem usada para exibicao de alertas */
   private InterfaceMensagem interfaceMensagem;

   /** A atividade do tipo pilha de emergencia */
   private Atividade atividadeAtualizacaoPilhaEmergencia;
   
   private Atividade atividadeExecutada; 
   
   int numeroTotalPatioTipoBaliza = 0;
   
   /** Creates new form InterfaceAtualizacaoPilhaEmergencia */
   public InterfaceAtualizacaoPilhaEmergencia(java.awt.Frame parent, boolean modal)
   {
      super(parent, modal);
      initComponents();
   }

   public InterfaceAtualizacaoPilhaEmergencia(java.awt.Frame parent, boolean modal, ControladorDSP controladorDSP, Usina usinaSelecionada, InterfaceUsina interfaceUsina) throws ErroSistemicoException
   {
      super(parent, modal);
      initComponents();
      this.controladorDSP = controladorDSP;
      this.interfaceUsina = interfaceUsina;
      this.usinaSelecionada = usinaSelecionada;
      this.operacaoCanceladaPeloUsuario = Boolean.FALSE;
      
      if (usinaSelecionada.getAtividade() != null && usinaSelecionada.getAtividade().getTipoAtividade() == TipoAtividadeEnum.ATUALIZACAO_PILHA_DE_EMERGENCIA) {
          atividadeExecutada =   usinaSelecionada.getAtividade();  
      }
      
      montaMapPatioEmergencia(usinaSelecionada);
      montaPainelEmergenciasUsina();
      carregaDadosIniciais();
      txtDataHoraEventoFim.setEnabled(false);
      if (atividadeExecutada != null)
      {
         carregaInformacoesAtividade();
      }
   }

private void montaMapPatioEmergencia(Usina usinaSelecionada) {
    //TODO criar isso na base
      HashMap<MetaPatio, ArrayList<EnumTipoBaliza>> mapaTipoEmergenciaPorPatio = new HashMap<MetaPatio, ArrayList<EnumTipoBaliza>>();
      for (MetaPatio mp : usinaSelecionada.getMetaUsina().getListaMetaPatioEmergencia())
      {    	  
          Boolean tp15 = Boolean.FALSE;
          Boolean tp17 = Boolean.FALSE;
          Boolean tp09 = Boolean.FALSE;
          ArrayList<EnumTipoBaliza> listaTipos = new ArrayList<EnumTipoBaliza>();
          for (MetaBaliza mb : mp.getListaDeMetaBalizas()) {
              if (!tp15 && mb.getTipoBaliza().equals(EnumTipoBaliza.EMERGENCIA_P5)) {
                  tp15 = Boolean.TRUE;
                  listaTipos.add(EnumTipoBaliza.EMERGENCIA_P5);
              }
              if (!tp17 && mb.getTipoBaliza().equals(EnumTipoBaliza.EMERGENCIA_TP17)) {
                  tp17 = Boolean.TRUE;
                  listaTipos.add(EnumTipoBaliza.EMERGENCIA_TP17);
              }
              
              if (!tp09 && mb.getTipoBaliza().equals(EnumTipoBaliza.EMERGENCIA_TP009)) {
                  tp09 = Boolean.TRUE;
                  listaTipos.add(EnumTipoBaliza.EMERGENCIA_TP009);
              }
              
            if (tp15 && tp17 && tp09) {
               break;      
            }          
          }
    	  if (listaTipos.size() > 0) {
    	      mapaTipoEmergenciaPorPatio.put(mp, listaTipos);
    	  }    
      }
      usinaSelecionada.getMetaUsina().setMapaTiposEmergenciaPorPatio(mapaTipoEmergenciaPorPatio);
}

   private void montaPainelEmergenciasUsina()
   {
       mapBalizaSelecionaveis = new HashMap<MetaPatio, Map<EnumTipoBaliza,JCheckBox>>();
	  for (MetaPatio patioEmergencia : usinaSelecionada.getMetaUsina().getListaMetaPatioEmergencia())
	  {
	      Map<EnumTipoBaliza,JCheckBox> mapaTipoBaliza = new HashMap<EnumTipoBaliza, JCheckBox>();
	      mapBalizaSelecionaveis.put(patioEmergencia, mapaTipoBaliza);
	      ArrayList<EnumTipoBaliza> tiposBalizaPatioEmergencia = usinaSelecionada.getMetaUsina().getMapaTiposEmergenciaPorPatio().get(patioEmergencia);		  
		  
	      for (EnumTipoBaliza tipoBaliza : tiposBalizaPatioEmergencia)
		  {
	          JCheckBox chkEmergenciaUsina = new JCheckBox();
	          mapaTipoBaliza.put(tipoBaliza, chkEmergenciaUsina);	          		      		      			  
			  String nomeEmergencia = patioEmergencia.getNomePatio() + " - " + tipoBaliza.toString();			  
			  chkEmergenciaUsina.setName(nomeEmergencia);
			  chkEmergenciaUsina.setText(nomeEmergencia);
			  pnlEmergenciasUsina.add(chkEmergenciaUsina);			  
			  
			  numeroTotalPatioTipoBaliza++;
		  }		
      }
   }

   /**
    * Metodo que carrega as informacoes ja existentes na atividade para finalizacao da mesma
    */
   private void carregaInformacoesAtividade()
   {  	      
		  List<Baliza> balizasAtividade = atividadeExecutada.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaDeBalizas();
		  mapBalizaSelecionadaAtividade = new HashMap<MetaPatio, List<MetaBaliza>>();
		  
		   recuperaEmergenciaSelecionada(balizasAtividade);
		        
      if (atividadeExecutada.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0) != null) 
      {
	      LugarEmpilhamentoRecuperacao lugarEmpRec = atividadeExecutada.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0);
	      // setando o nome da pilha
	      txtNomePilha.setText(lugarEmpRec.getNomeDoLugarEmpRec());
	      txtNomePilha.setEnabled(false);
	      // setando o tipo de produto selecionado
	      cmbTipoProduto.setSelectedItem(lugarEmpRec.getTipoProduto());
	     // cmbTipoProduto.setEnabled(false);
	      // setando a taxa de operacao
	      txtTaxaOperacao.setText(DSSStockyardFuncoesNumeros.getQtdeFormatada(lugarEmpRec.getTaxaDeOperacaoNaPilha(), 2));
	      txtTaxaOperacao.setEnabled(false);
	      
	      txtDataHoraEvento.setText(DSSStockyardTimeUtil.formatarData(atividadeExecutada.getDtInicio(), PropertiesUtil.buscarPropriedade("formato.campo.datahora")));
          txtDataHoraEvento.setSelectionStart(11); // comeca a selecao no inicio da hora
          txtDataHoraEvento.setSelectionEnd(txtDataHoraEvento.getText().length());
          txtDataHoraEvento.setEnabled(false);          
	      
	      txtDataHoraEventoFim.setText(DSSStockyardTimeUtil.formatarData(DSSStockyardTimeUtil.obterDataHoraUltimaSituacaoParaAtividades(controladorDSP.getInterfaceInicial().getInterfaceInicial().getSituacaoPatioExibida().getDtInicio()), PropertiesUtil.buscarPropriedade("formato.campo.datahora")));
	      txtDataHoraEventoFim.setSelectionStart(11); // comeca a selecao no inicio da hora
	      txtDataHoraEventoFim.setSelectionEnd(txtDataHoraEventoFim.getText().length());
	      txtDataHoraEventoFim.setEnabled(true);
	      txtDataHoraEventoFim.requestFocus();
	      
      }   
   }

private void recuperaEmergenciaSelecionada(List<Baliza> balizasAtividade) {
    List<MetaBaliza> lstTP17 = null;
    List<MetaBaliza> lstTP15 = null;
    List<MetaBaliza> lstTP09 = null;    
    for (MetaPatio mp : usinaSelecionada.getMetaUsina().getListaMetaPatioEmergencia()) {
            Map<EnumTipoBaliza,JCheckBox> values = mapBalizaSelecionaveis.get(mp);                      
            JCheckBox chkEmergencia = values.get(EnumTipoBaliza.EMERGENCIA_TP17);
            if (chkEmergencia != null)                                       
                chkEmergencia.setEnabled(Boolean.FALSE);
            
            
                chkEmergencia = values.get(EnumTipoBaliza.EMERGENCIA_P5);                
                if (chkEmergencia != null)                                           
                chkEmergencia.setEnabled(Boolean.FALSE);
                
                chkEmergencia = values.get(EnumTipoBaliza.EMERGENCIA_TP009);                
                if (chkEmergencia != null)                                           
                chkEmergencia.setEnabled(Boolean.FALSE);
                
                
                
                lstTP15 = new ArrayList<MetaBaliza>();
                for (Baliza b : balizasAtividade) {
                   if (b.getMetaBaliza().getMetaPatio().equals(mp) && b.getMetaBaliza().getTipoBaliza().equals(EnumTipoBaliza.EMERGENCIA_P5)) {		               
                       lstTP15.add(b.getMetaBaliza());
                   }
                }
                 
          
            lstTP17 = new ArrayList<MetaBaliza>();
            for (Baliza b : balizasAtividade) {
                if (b.getMetaBaliza().getMetaPatio().equals(mp) && b.getMetaBaliza().getTipoBaliza().equals(EnumTipoBaliza.EMERGENCIA_TP17)) {
                       lstTP17.add(b.getMetaBaliza());
                }
            }
            
            lstTP09 = new ArrayList<MetaBaliza>();
            for (Baliza b : balizasAtividade) {
               if (b.getMetaBaliza().getMetaPatio().equals(mp) && b.getMetaBaliza().getTipoBaliza().equals(EnumTipoBaliza.EMERGENCIA_TP009)) {                    
                   lstTP09.add(b.getMetaBaliza());
               }
            }
          
            
            List<MetaBaliza> selecionadas = new ArrayList<MetaBaliza>();
            if (!lstTP17.isEmpty()) {
                selecionadas.addAll(lstTP17);		                               
                chkEmergencia = values.get(EnumTipoBaliza.EMERGENCIA_TP17);
                chkEmergencia.setEnabled(Boolean.FALSE);
                chkEmergencia.setSelected(Boolean.TRUE);

            }    
            if (!lstTP15.isEmpty()) {
                selecionadas.addAll(lstTP15);		                                
                chkEmergencia = values.get(EnumTipoBaliza.EMERGENCIA_P5);
                chkEmergencia.setEnabled(Boolean.FALSE);
                chkEmergencia.setSelected(Boolean.TRUE);
            }
            
            if (!lstTP09.isEmpty()) {
                selecionadas.addAll(lstTP09);                                       
                chkEmergencia = values.get(EnumTipoBaliza.EMERGENCIA_TP009);
                chkEmergencia.setEnabled(Boolean.FALSE);
                chkEmergencia.setSelected(Boolean.TRUE);
            }
            
            
            
            mapBalizaSelecionadaAtividade.put(mp,selecionadas);
        }
}

   private void carregaDadosIniciais()
   {

      lblUsina.setText(usinaSelecionada.getNomeUsina());
      txtTaxaOperacao.setText(DSSStockyardFuncoesNumeros.getQtdeFormatada(usinaSelecionada.getTaxaDeOperacao(), 2));
      carregaComboTipoProdutoUsina(DSSStockyardTimeUtil.obterDataHoraUltimaSituacaoParaAtividades(controladorDSP.getInterfaceInicial().getInterfaceInicial().getSituacaoPatioExibida().getDtInicio()));

      txtDataHoraEvento.setText(DSSStockyardTimeUtil.formatarData(DSSStockyardTimeUtil.obterDataHoraUltimaSituacaoParaAtividades(controladorDSP.getInterfaceInicial().getInterfaceInicial().getSituacaoPatioExibida().getDtInicio()), PropertiesUtil.buscarPropriedade("formato.campo.datahora")));
      txtDataHoraEvento.setSelectionStart(11); // comeca a selecao no inicio da hora
      txtDataHoraEvento.setSelectionEnd(txtDataHoraEvento.getText().length());
      txtDataHoraEvento.requestFocus();
   }

   private void carregaComboTipoProdutoUsina(Date horaAtual)
   {
      cmbTipoProduto.removeAllItems();
      mapComboCampanha = new HashMap<Integer,Campanha>();
      List<Campanha> campanhas = usinaSelecionada.getMetaUsina().getCampanhas(horaAtual);
      Integer index = 0;
      for (Campanha campanha : campanhas){
          cmbTipoProduto.addItem(campanha.getTipoProduto());
          mapComboCampanha.put(index, campanha);        
          index++;
      }
      
      
   }

   /** This method is called from within the constructor to
    * initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is
    * always regenerated by the Form Editor.
    */
   @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlPrincipal = new javax.swing.JPanel();
        pnlEmergenciasUsina = new javax.swing.JPanel();
        horarioJP = new javax.swing.JPanel();
        txtDataHoraEvento = new javax.swing.JFormattedTextField();
        MaskFormatter fmtDataHoraEvento = new MaskFormatter();
        try {
            fmtDataHoraEvento.setMask(PropertiesUtil.buscarPropriedade("mascara.campo.datahora"));
            fmtDataHoraEvento.setPlaceholderCharacter(' ');
            fmtDataHoraEvento.install(txtDataHoraEvento);
        } catch (ParseException pex) {
            pex.printStackTrace();
        }
        
        
        PlainDocument doc = (PlainDocument) txtDataHoraEvento.getDocument();   
        doc.addDocumentListener(new DocumentListener() {   
          public void changedUpdate(DocumentEvent e) {   
              txtDataHoraEventoTextChanged();
          }   
          public void insertUpdate(DocumentEvent e) {   
              txtDataHoraEventoTextChanged();
          }   
          public void removeUpdate(DocumentEvent e) {   
              //txtDataHoraInicioRecuperacaoTextChanged();
          }   
        });   

        
        horaEventoJL = new javax.swing.JLabel();
        horaEventoJL1 = new javax.swing.JLabel();
        txtDataHoraEventoFim = new javax.swing.JFormattedTextField();
        MaskFormatter fmtDataHoraEventoFim = new MaskFormatter();
        try {
            fmtDataHoraEventoFim.setMask(PropertiesUtil.buscarPropriedade("mascara.campo.datahora"));
            fmtDataHoraEventoFim.setPlaceholderCharacter(' ');
            fmtDataHoraEventoFim.install(txtDataHoraEventoFim);
        } catch (ParseException pex) {
            pex.printStackTrace();
        }
       
        PlainDocument doc1 = (PlainDocument) txtDataHoraEventoFim.getDocument();   
        doc1.addDocumentListener(new DocumentListener() {   
          public void changedUpdate(DocumentEvent e) {   
              txtDataHoraEventoFimTextChanged();
          }   
          public void insertUpdate(DocumentEvent e) {   
              txtDataHoraEventoFimTextChanged();
          }   
          public void removeUpdate(DocumentEvent e) {   
              //txtDataHoraInicioRecuperacaoTextChanged();
          }   
        });   
       
        cmdDesistir = new javax.swing.JButton();
        cmdConfirmar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblUsina = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtTaxaOperacao = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cmbTipoProduto = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        txtNomePilha = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(PropertiesUtil.getMessage("mensagem.titulo.interface.atualizacao.pilha.emergencia"));
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        pnlEmergenciasUsina.setBorder(javax.swing.BorderFactory.createTitledBorder(PropertiesUtil.getMessage("mensagem.titulo.painel.direcionamento.pilha")));
        pnlEmergenciasUsina.setLayout(new java.awt.GridLayout(1, 0, 15, 0));

        horarioJP.setBorder(javax.swing.BorderFactory.createTitledBorder(PropertiesUtil.getMessage("mensagem.titulo.painel.darahora")));

        horaEventoJL.setText(PropertiesUtil.getMessage("mensagem.ocorrencia.eventos"));

        horaEventoJL1.setText(PropertiesUtil.getMessage("mensagem.ocorrencia.eventos"));

        javax.swing.GroupLayout horarioJPLayout = new javax.swing.GroupLayout(horarioJP);
        horarioJP.setLayout(horarioJPLayout);
        horarioJPLayout.setHorizontalGroup(
            horarioJPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(horarioJPLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(horarioJPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(horarioJPLayout.createSequentialGroup()
                        .addComponent(horaEventoJL, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDataHoraEvento, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(horarioJPLayout.createSequentialGroup()
                        .addComponent(horaEventoJL1, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDataHoraEventoFim, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(74, Short.MAX_VALUE))
        );
        horarioJPLayout.setVerticalGroup(
            horarioJPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(horarioJPLayout.createSequentialGroup()
                .addGroup(horarioJPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(horaEventoJL, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDataHoraEvento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(horarioJPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(horaEventoJL1, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDataHoraEventoFim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        cmdDesistir.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        StringBuffer value = new StringBuffer();
        value.append(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens")).append("/exit.png");
        cmdDesistir.setIcon(new javax.swing.ImageIcon(value.toString())); // NOI18N

        cmdDesistir.setText(PropertiesUtil.getMessage("botao.desistir"));
        cmdDesistir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdDesistirActionPerformed(evt);
            }
        });

        cmdConfirmar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        value = new StringBuffer();
        value.append(PropertiesUtil.buscarPropriedade("tela.diretorio.imagens")).append("/confirmar.png");
        cmdConfirmar.setIcon(new javax.swing.ImageIcon(value.toString())); // NOI18N
        cmdConfirmar.setText(PropertiesUtil.getMessage("botao.confirmar"));
        cmdConfirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdConfirmarActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(PropertiesUtil.getMessage("mensagem.titulo.painel.informacoes.campanha")));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel1.setText(PropertiesUtil.getMessage("label.nome.usina"));

        lblUsina.setFont(new java.awt.Font("Tahoma", 1, 12));
        lblUsina.setText("Nome da Usina");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel2.setText(PropertiesUtil.getMessage("label.taxa.de.operacao"));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel3.setText(PropertiesUtil.getMessage("label.taxa.de.operacao.unidade"));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel4.setText(PropertiesUtil.getMessage("label.tipo.produto"));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel7.setText(PropertiesUtil.getMessage("label.nome.pilha"));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbTipoProduto, 0, 206, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, 0, 0, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 119, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(lblUsina, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtTaxaOperacao, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(13, 13, 13)
                .addComponent(txtNomePilha, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lblUsina)
                    .addComponent(jLabel7)
                    .addComponent(txtNomePilha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtTaxaOperacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cmbTipoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlPrincipalLayout = new javax.swing.GroupLayout(pnlPrincipal);
        pnlPrincipal.setLayout(pnlPrincipalLayout);
        pnlPrincipalLayout.setHorizontalGroup(
            pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPrincipalLayout.createSequentialGroup()
                .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPrincipalLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(horarioJP, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlEmergenciasUsina, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPrincipalLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPrincipalLayout.createSequentialGroup()
                .addContainerGap(127, Short.MAX_VALUE)
                .addComponent(cmdDesistir, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmdConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(120, 120, 120))
        );
        pnlPrincipalLayout.setVerticalGroup(
            pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlEmergenciasUsina, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(horarioJP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdDesistir))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        getContentPane().add(pnlPrincipal, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdDesistirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDesistirActionPerformed
       operacaoCanceladaPeloUsuario = true;
       setVisible(false);
    }//GEN-LAST:event_cmdDesistirActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
       operacaoCanceladaPeloUsuario = true;
    }//GEN-LAST:event_formWindowClosing

    private void cmdConfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdConfirmarActionPerformed
       try {
    	   Date dataHoraOcorrenciaEvento;
    	   if (atividadeExecutada != null) {
    		   dataHoraOcorrenciaEvento = DSSStockyardTimeUtil.criaDataComString(txtDataHoraEventoFim.getText(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
         } else {
        	 
        	 dataHoraOcorrenciaEvento = DSSStockyardTimeUtil.criaDataComString(txtDataHoraEvento.getText(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
         }	 
         
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
               txtDataHoraEvento.requestFocus();
               return;
            }
         }

          final InterfaceMensagem interfaceMensagemProcessamento = new InterfaceMensagem();
          interfaceMensagemProcessamento.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_PROCESSAMENTO);
          interfaceMensagemProcessamento.setProcessamentoAtividado(Boolean.TRUE);
          interfaceMensagemProcessamento.setDescricaoMensagem(PropertiesUtil.buscarPropriedade("mensagem.processamento.atualizacao.pilha.emergencia"));

          new Thread("Thread atualização pilha de emergencia")
          {

             @Override
             public void run()
             {
                try
                {

                   if (mapBalizaSelecionadaAtividade == null) 
                       carregarListaEmergenciasSelecionadas();
                   validarInformacoesPilhaEmergencia();
                   desHabilitarCampos();
                   controladorDSP.ativarMensagem(interfaceMensagemProcessamento);
                   /** cria o VO para o empilhamento da emergencia */
                   EmpilhamentoEmergenciaVO empilhamentoEmergenciaVO = new EmpilhamentoEmergenciaVO(); 
                                      
                   Date dataAtividade = null;
                   // verifica se esta setando a hora de inicio ou de termino
                   if (atividadeExecutada != null)
                   {
                       dataAtividade = DSSStockyardTimeUtil.criaDataComString(txtDataHoraEventoFim.getText(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
                       dataAtividade = Atividade.verificaAtualizaDataAtividade(dataAtividade, controladorDSP.getInterfaceInicial().getPlanejamento().getControladorDePlano().obterSituacaoPatioFinal().getDtInicio());                                           
                       empilhamentoEmergenciaVO.setDataInicio(atividadeExecutada.getDtInicio());
                       empilhamentoEmergenciaVO.setDataFim(dataAtividade);                	   
                	   empilhamentoEmergenciaVO.setMapaBalizasPorPatio(mapBalizaSelecionadaAtividade);
                	   empilhamentoEmergenciaVO.setAtividadeAnterior(atividadeExecutada);
                   }
                   else
                   {
                       dataAtividade = DSSStockyardTimeUtil.criaDataComString(txtDataHoraEvento.getText(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
                       dataAtividade = Atividade.verificaAtualizaDataAtividade(dataAtividade, controladorDSP.getInterfaceInicial().getPlanejamento().getControladorDePlano().obterSituacaoPatioFinal().getDtInicio());                                           
                       empilhamentoEmergenciaVO.setMapaBalizasPorPatio(mapBalizaSelecionadaAtividade);
                       empilhamentoEmergenciaVO.setDataInicio(dataAtividade);
                   }
                   
                   empilhamentoEmergenciaVO.setNomePilha(txtNomePilha.getText());
                   empilhamentoEmergenciaVO.setTaxaOperacao(DSSStockyardFuncoesNumeros.getStringToDouble(txtTaxaOperacao.getText()));
                   
                   // seta a usina
                   List<MetaUsina> listaMetaUsinas = new ArrayList<MetaUsina>();
                   listaMetaUsinas.add(usinaSelecionada.getMetaUsina());
                   empilhamentoEmergenciaVO.setListaUsinas(listaMetaUsinas);
                   
                   // seta a maquina
                   empilhamentoEmergenciaVO.setListaMaquinas(new ArrayList<MetaMaquinaDoPatio>());
                                     
                   // seta o tipo de produto
                   TipoProduto tipoProdutoSelecionado = (TipoProduto) cmbTipoProduto.getSelectedItem();
                   Campanha campanha = mapComboCampanha.get(cmbTipoProduto.getSelectedIndex());
                   empilhamentoEmergenciaVO.setTipoProduto(tipoProdutoSelecionado);
                   List<Campanha> campanhas = new ArrayList<Campanha>();
                   campanhas.add(campanha);
                   empilhamentoEmergenciaVO.setCampanhas(campanhas);
                    
                   //TODO ver de onde pega o cliente
                   empilhamentoEmergenciaVO.setCliente(null);
                   
                   //empilhamentoEmergenciaVO.setMapaBalizasPorPatio(mapaBalizasNoPatio);
                  
                   //Campanha campanhaAtual = usinaSelecionada.getMetaUsina().getCampanhaAtual(dataAtividade);
                   
                   if (validarCampanhaDiferente(campanha)) {
                       habilitarCampos();
                       return;
                   }
                   
                   ControladorExecutarAtividadeEmpilhamentoEmergencia service = ControladorExecutarAtividadeEmpilhamentoEmergencia.getInstance();
                   atividadeAtualizacaoPilhaEmergencia =  service.empilharEmergencia(empilhamentoEmergenciaVO);
                   
                   controladorDSP.getInterfaceInicial().atualizarEmpilhamentoEmergencia(atividadeAtualizacaoPilhaEmergencia);
                   controladorDSP.getInterfaceInicial().getInterfaceInicial().atualizarDSP();
                
                   operacaoCanceladaPeloUsuario = Boolean.FALSE;

                   // chegando ate o JDialog
                   setVisible(false);
                   cmdConfirmar.setEnabled(Boolean.TRUE);
                   cmdDesistir.setEnabled(Boolean.TRUE);
                }
                catch (AtividadeException ex)
                {
                	habilitarCampos();
                	controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
                   interfaceMensagem = new InterfaceMensagem();
                   interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
                   interfaceMensagem.setDescricaoMensagem(ex.getMessage());
                   controladorDSP.ativarMensagem(interfaceMensagem);
                }
                catch (ValidacaoCampoException vcEx)
                {
                	habilitarCampos();
                   controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
                   interfaceMensagem = new InterfaceMensagem();
                   interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
                   interfaceMensagem.setDescricaoMensagem(vcEx.getMessage());
                   controladorDSP.ativarMensagem(interfaceMensagem);
                }
                catch (ValidacaoObjetosOperacaoException ex)
                {
                	habilitarCampos();
                   controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
                   interfaceMensagem = new InterfaceMensagem();
                   interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
                   interfaceMensagem.setDescricaoMensagem(ex.getMessage());
                   controladorDSP.ativarMensagem(interfaceMensagem);
                }
             }
          }.start();
       }
       catch (ValidacaoCampoException vcEx)
       {
    	   habilitarCampos();
    	  controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
          interfaceMensagem = new InterfaceMensagem();
          interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
          interfaceMensagem.setDescricaoMensagem(vcEx.getMessage());
          controladorDSP.ativarMensagem(interfaceMensagem);
       }
    }//GEN-LAST:event_cmdConfirmarActionPerformed

	private void habilitarCampos() {
		cmdDesistir.setEnabled(Boolean.TRUE);
    	cmdConfirmar.setEnabled(Boolean.TRUE);
    	cmbTipoProduto.setEnabled(Boolean.TRUE);    	
        txtDataHoraEvento.setEnabled(Boolean.TRUE); 
    	txtNomePilha.setEnabled(Boolean.TRUE); 
    	txtTaxaOperacao.setEnabled(Boolean.TRUE);     	
	}

	private void desHabilitarCampos() {
		cmdDesistir.setEnabled(Boolean.FALSE);
    	cmdConfirmar.setEnabled(Boolean.FALSE);
    	cmbTipoProduto.setEnabled(Boolean.FALSE);    	
        txtDataHoraEvento.setEnabled(Boolean.FALSE); 
        txtDataHoraEventoFim.setEnabled(Boolean.FALSE);
        txtNomePilha.setEnabled(Boolean.FALSE); 
    	txtTaxaOperacao.setEnabled(Boolean.FALSE);     	
	}
	
   private void validarInformacoesPilhaEmergencia() throws ValidacaoCampoException
   {
     
     if (mapBalizaSelecionadaAtividade.isEmpty())
      {
         throw new ValidacaoCampoException(PropertiesUtil.getMessage("exception.validacao.pilha.emergencia.destino.nao.selecionado"));
      }

      if (txtDataHoraEvento.getText().equals(DSSStockyardTimeUtil.DATAHORA_EM_BRANCO))
      {
         throw new ValidacaoCampoException(PropertiesUtil.getMessage("exception.validacao.pilha.emergencia.darahora.vazia"));
      }

      DSSStockyardTimeUtil.validarDataHora(txtDataHoraEvento.getText(), PropertiesUtil.getMessage("mensagem.datahora.evento"));
      Date dataHoraOcorrenciaEvento = DSSStockyardTimeUtil.criaDataComString(txtDataHoraEvento.getText(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
      dataHoraOcorrenciaEvento = DSSStockyardTimeUtil.verificaAtualizaDataSituacaoClone(dataHoraOcorrenciaEvento, controladorDSP.getInterfaceInicial().getPlanejamento().getControladorDePlano().obterSituacaoPatioFinal().getDtInicio());

      if (!controladorDSP.validaDAtaMenorUltimaSituacaoPatio(dataHoraOcorrenciaEvento))
      {
         throw new ValidacaoCampoException(PropertiesUtil.getMessage("aviso.data.inicial.menor.ultima.situacao.patio"));
      }

      if (txtNomePilha.getText().trim().equals(""))
      {
         throw new ValidacaoCampoException(PropertiesUtil.getMessage("aviso.nome.pilha.vazio"));
      }

      if (txtTaxaOperacao.getText().trim().equals(""))
      {
         throw new ValidacaoCampoException(PropertiesUtil.getMessage("exception.validacao.pilha.emergencia.taxa.de.operacao.vazia"));
      }

      double taxaDeOperacao = DSSStockyardFuncoesNumeros.getStringToDouble(txtTaxaOperacao.getText().trim());
      if (taxaDeOperacao <= 0)
      {
         throw new ValidacaoCampoException(PropertiesUtil.getMessage("mensagem.taxa.operacao.maior.zero"));
      }

   }

   public Boolean getOperacaoCanceladaPeloUsuario()
   {
      return operacaoCanceladaPeloUsuario;
   }

   
   /**
    * Metodo que carrega uma lista com a lista de emergencias selecionadas
    * @throws br.com.cflex.supervision.stockYard.cliente.exception.ValidacaoCampoException
    */
   private void carregarListaEmergenciasSelecionadas()
   {       
       mapBalizaSelecionadaAtividade = new HashMap<MetaPatio, List<MetaBaliza>>();
       List<MetaBaliza> lstTP17 = null;
       List<MetaBaliza> lstTP15 = null;
       List<MetaBaliza> lstTP09 = null;
       for (MetaPatio mp : usinaSelecionada.getMetaUsina().getListaMetaPatioEmergencia()) {
            Map<EnumTipoBaliza,JCheckBox> values = mapBalizaSelecionaveis.get(mp);  
            
            JCheckBox chkEmergencia = values.get(EnumTipoBaliza.EMERGENCIA_P5);
            if (chkEmergencia != null) {
                if (chkEmergencia.isSelected())
                {
                    lstTP15 = new ArrayList<MetaBaliza>();
                    for (MetaBaliza b : mp.getListaDeMetaBalizas())
                        
                        if (b.getTipoBaliza().equals(EnumTipoBaliza.EMERGENCIA_P5)) {
                            lstTP15.add(b);                            
                        }   
                }
            }    
            
            chkEmergencia = values.get(EnumTipoBaliza.EMERGENCIA_TP17);
            if (chkEmergencia != null) {
                if (chkEmergencia.isSelected())
                {
                    lstTP17 = new ArrayList<MetaBaliza>();
                    for (MetaBaliza b : mp.getListaDeMetaBalizas())
                        if (b.getTipoBaliza().equals(EnumTipoBaliza.EMERGENCIA_TP17)) {
                            lstTP17.add(b);
                            
                        }
                }
            }
            
            
            chkEmergencia = values.get(EnumTipoBaliza.EMERGENCIA_TP009);
            if (chkEmergencia != null) {
                if (chkEmergencia.isSelected())
                {
                    lstTP09 = new ArrayList<MetaBaliza>();
                    for (MetaBaliza b : mp.getListaDeMetaBalizas())
                        if (b.getTipoBaliza().equals(EnumTipoBaliza.EMERGENCIA_TP009)) {
                            lstTP09.add(b);
                            
                        }
                }
            }
            
            List<MetaBaliza> selecionadas = new ArrayList<MetaBaliza>();
            
            if (lstTP17 != null)
                selecionadas.addAll(lstTP17);            
            if (lstTP15 != null)
                selecionadas.addAll(lstTP15);
            if (lstTP09 != null)
                selecionadas.addAll(lstTP09);
            
            if (!selecionadas.isEmpty())
                mapBalizaSelecionadaAtividade.put(mp,selecionadas);
            
       }
      }
   
   
   /**
    * validarCampanhaDiferente
    * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
    * @since 02/12/2010
    * @see
    * @param 
    * @return void
    */
   private Boolean validarCampanhaDiferente(Campanha campanhaAtual) {
       Boolean campanhaDiferente = Boolean.FALSE;
       StringBuilder campanhasUsinas = new StringBuilder("Campanhas diferentes !!\n");
       if (atividadeExecutada != null) {
           List<AtividadeCampanha> atvCampanha = atividadeExecutada.getListaDeAtividadesCampanha(); 
           List<Usina> usinas = atividadeExecutada.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaUsinas();
                 
          if (usinas != null) {
               for (Usina usina : usinas) {                                             
                   if (campanhaAtual != null) {                                    
                       for(AtividadeCampanha atvCmp : atvCampanha) {
                           if (atvCmp.getNomeUsina().equals(usina.getNomeUsina()) && !atvCmp.getCampanha().equals(campanhaAtual)) {
                               campanhasUsinas.append("A Campanha selecionada : " + campanhaAtual.getNomeCampanha() + " para a Usina : " + usina.getNomeUsina() + " é diferente da campanha " + atvCmp.getCampanha().getNomeCampanha() + " selecionada no inicio da atividade ! \n" );
                               campanhaDiferente = Boolean.TRUE;
                           } 
                       }    
                   }
               }
          }
                   
       }
       if (campanhaDiferente) {
           JLabel pergunta = new JLabel(campanhasUsinas.toString());
           pergunta.setFont(new Font("Tahoma", Font.PLAIN, 11));
           JOptionPane.showOptionDialog(null,
                           //pergunta,
                           campanhasUsinas.toString(), PropertiesUtil.getMessage("popup.atencao"),
                           JOptionPane.CLOSED_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
       }
   
       return campanhaDiferente;
   }

   
   

   private void txtDataHoraEventoTextChanged() {//GEN-FIRST:event_txtDataHoraInicioRecuperacaoInputMethodTextChanged
       try {
        if (txtDataHoraEvento.getText() != "") {
            Date dataHoraOcorrenciaEvento = DSSStockyardTimeUtil.criaDataComString(txtDataHoraEvento.getText(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
            carregaComboTipoProdutoUsina(dataHoraOcorrenciaEvento);            
        }
       } catch (ValidacaoCampoException e) {
           controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
           interfaceMensagem = new InterfaceMensagem();
           interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
           interfaceMensagem.setDescricaoMensagem(e.getMessage());
           controladorDSP.ativarMensagem(interfaceMensagem);
    }
   }  
       
       private void txtDataHoraEventoFimTextChanged() {//GEN-FIRST:event_txtDataHoraInicioRecuperacaoInputMethodTextChanged
           try {
            if (txtDataHoraEventoFim.getText() != "") {
                Date dataHoraOcorrenciaEvento = DSSStockyardTimeUtil.criaDataComString(txtDataHoraEventoFim.getText(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
                carregaComboTipoProdutoUsina(dataHoraOcorrenciaEvento);            
            }
           } catch (ValidacaoCampoException e) {
               controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
               interfaceMensagem = new InterfaceMensagem();
               interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
               interfaceMensagem.setDescricaoMensagem(e.getMessage());
               controladorDSP.ativarMensagem(interfaceMensagem);
        }
       
       // TODO add your handling code here:
   }//GEN-LAST:event_txtDataHoraInicioRecuperacaoInputMethodTextChanged

   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbTipoProduto;
    private javax.swing.JButton cmdConfirmar;
    private javax.swing.JButton cmdDesistir;
    private javax.swing.JLabel horaEventoJL;
    private javax.swing.JLabel horaEventoJL1;
    private javax.swing.JPanel horarioJP;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblUsina;
    private javax.swing.JPanel pnlEmergenciasUsina;
    private javax.swing.JPanel pnlPrincipal;
    private javax.swing.JFormattedTextField txtDataHoraEvento;
    private javax.swing.JFormattedTextField txtDataHoraEventoFim;
    private javax.swing.JTextField txtNomePilha;
    private javax.swing.JTextField txtTaxaOperacao;
    // End of variables declaration//GEN-END:variables

}
