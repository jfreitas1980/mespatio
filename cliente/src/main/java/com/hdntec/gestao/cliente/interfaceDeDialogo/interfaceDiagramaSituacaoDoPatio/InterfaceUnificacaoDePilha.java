package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.SwingConstants;

import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.comparadores.ComparadorInterfaceBaliza;
import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.cliente.util.tabela.CFlexStockyardFuncoesTabela;
import com.hdntec.gestao.cliente.util.tabela.CFlexStockyardTableModelCustom;
import com.hdntec.gestao.cliente.util.tabela.ColunaTabela;
import com.hdntec.gestao.cliente.util.texto.DSSStockyardFuncoesTexto;
import com.hdntec.gestao.domain.blendagem.Blendagem;
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.Pilha;
import com.hdntec.gestao.domain.produto.entity.ItemDeControle;
import com.hdntec.gestao.domain.produto.entity.Produto;
import com.hdntec.gestao.domain.produto.entity.Qualidade;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.domain.vo.atividades.EdicaoVO;
import com.hdntec.gestao.exceptions.AtividadeException;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.ValidacaoCampoException;
import com.hdntec.gestao.services.controladores.impl.ControladorExecutarEdicaoBalizas;
import com.hdntec.gestao.util.PropertiesUtil;

public class InterfaceUnificacaoDePilha extends javax.swing.JDialog
{

   /** A lista de interface de baliza usada para unificacao */
   private List<InterfaceBaliza> listaBalizasSelecionadas;

   /** O controlador dos subsistema do DSP */
   private ControladorDSP controladorDSP;

   /** constantes referente as colunas da tabela */
   private final int COL_ID_BALIZA = 0;

   private final int COL_NOME_BALIZA = 1;

   private final int COL_NUMERO_BALIZA = 2;

   private final int COL_NOME_PILHA = 3;

   private final int COL_PRODUTO = 4;

   /* a lista de colunas da tabela */
   private List<ColunaTabela> listaColunas;

   /** Vetor com os dados ta tabela de informacoes da baliza */
   private Vector vInformacoesBalizas;

   /** a lista de pilhas contendo as pilhas diferentes entre as balizas selecionadas */
   private List<Pilha> listaPilhas;

   /** Tipo de produto selecionado para nova pilha */
   private TipoProduto tipoProdutoSelecionado;

   /** identificador de operacao cancelada pelo usuario */
   private Boolean operacaoCanceladaPeloUsuario;

   /** a interface de mensagem do usuario */
   private InterfaceMensagem interfaceMensagem;

   /** Creates new form InterfaceUnificacaoDePilha */
   public InterfaceUnificacaoDePilha(java.awt.Frame parent, boolean modal)
   {
      super(parent, modal);
      initComponents();
   }

   public InterfaceUnificacaoDePilha(java.awt.Frame parent, boolean modal, List<InterfaceBaliza> listaBalizasSelecionadas, ControladorDSP controladorDSP) throws ErroSistemicoException
   {
      super(parent, modal);
      initComponents();
      this.listaBalizasSelecionadas = listaBalizasSelecionadas;
      this.controladorDSP = controladorDSP;
      vInformacoesBalizas = new Vector();
      this.operacaoCanceladaPeloUsuario = false;
      criaColunas();
      exibeInformacoesBaliza();
      carregaComboTiposProduto();
      carregaPilhasDasBalizasSelecionadas();
   }

   private void criaColunas()
   {
      listaColunas = new ArrayList<ColunaTabela>();
      ColunaTabela colInfo;

      colInfo = new ColunaTabela();
      colInfo.setAlinhamento(SwingConstants.CENTER);
      colInfo.setEditar(Boolean.TRUE);
      colInfo.setLargura(60);
      colInfo.setRedimensionar(Boolean.FALSE);
      colInfo.setTitulo("Código");
      listaColunas.add(colInfo);

      colInfo = new ColunaTabela();
      colInfo.setAlinhamento(SwingConstants.LEADING);
      colInfo.setEditar(Boolean.TRUE);
      colInfo.setLargura(100);
      colInfo.setRedimensionar(Boolean.FALSE);
      colInfo.setTitulo("Nome da baliza");
      listaColunas.add(colInfo);

      colInfo = new ColunaTabela();
      colInfo.setAlinhamento(SwingConstants.CENTER);
      colInfo.setEditar(Boolean.TRUE);
      colInfo.setLargura(50);
      colInfo.setRedimensionar(Boolean.FALSE);
      colInfo.setTitulo("Número");
      listaColunas.add(colInfo);

      colInfo = new ColunaTabela();
      colInfo.setAlinhamento(SwingConstants.CENTER);
      colInfo.setEditar(Boolean.TRUE);
      colInfo.setLargura(60);
      colInfo.setRedimensionar(Boolean.FALSE);
      colInfo.setTitulo("Nome da pilha");
      listaColunas.add(colInfo);

      colInfo = new ColunaTabela();
      colInfo.setAlinhamento(SwingConstants.CENTER);
      colInfo.setEditar(Boolean.TRUE);
      colInfo.setLargura(80);
      colInfo.setRedimensionar(Boolean.FALSE);
      colInfo.setTitulo("Produto");
      listaColunas.add(colInfo);
   }

   /* Metodo que exibe as informacoes da lista de balizas selecionadas para unificacao */
   private void exibeInformacoesBaliza() throws ErroSistemicoException
   {
      // ordena a lista de balizas no momento da exibicao
      Collections.sort(listaBalizasSelecionadas, new ComparadorInterfaceBaliza());

      vInformacoesBalizas.removeAllElements();
      for (InterfaceBaliza interfaceBaliza : listaBalizasSelecionadas)
      {
         Object[] dados = new Object[5];

         dados[COL_ID_BALIZA] = interfaceBaliza.getBalizaVisualizada().getIdBaliza();
         dados[COL_NOME_BALIZA] = interfaceBaliza.getBalizaVisualizada().getNomeBaliza();
         dados[COL_NUMERO_BALIZA] = DSSStockyardFuncoesTexto.zerosStr(3, interfaceBaliza.getBalizaVisualizada().getNumero());
         dados[COL_NOME_PILHA] = "";
         dados[COL_PRODUTO] = "";
         Pilha pilhaAtual = interfaceBaliza.getBalizaVisualizada().retornaStatusHorario(interfaceBaliza.getInterfacePatio().getHoraSituacao());
         if (pilhaAtual != null)
         {
            dados[COL_NOME_PILHA] = pilhaAtual.getNomePilha();
         }
         if (interfaceBaliza.getBalizaVisualizada().getProduto() != null)
         {
            dados[COL_PRODUTO] = interfaceBaliza.getBalizaVisualizada().getProduto().getTipoProduto().toString();
         }
         vInformacoesBalizas.add(new Vector(Arrays.asList(dados)));
      }

      CFlexStockyardFuncoesTabela.setInformacoesTabela(tblBalizas, vInformacoesBalizas, listaColunas);
   }

   /** Metodo que carrega o combo com todos os tipos de produtos diferentes entre as balizas selecionadas */
   private void carregaComboTiposProduto()
   {
      List<TipoProduto> listaTiposProduto = new ArrayList<TipoProduto>();
      for (InterfaceBaliza interfaceBaliza : this.listaBalizasSelecionadas)
      {
         if (interfaceBaliza.getBalizaVisualizada().getProduto() != null)
         {
            if (!listaTiposProduto.contains(interfaceBaliza.getBalizaVisualizada().getProduto().getTipoProduto()))
            {
               listaTiposProduto.add(interfaceBaliza.getBalizaVisualizada().getProduto().getTipoProduto());
            }
         }
      }
      for (TipoProduto tipoProduto : listaTiposProduto)
      {
         cmbTipoProduto.addItem(tipoProduto);
      }
   }

   /** Metodo que carrega a lista de pilhas com as diferentes pilhas
    * encontradas entre as balizas selecionadas
    */
   private void carregaPilhasDasBalizasSelecionadas()
   {
      listaPilhas = new ArrayList<Pilha>();
      for (InterfaceBaliza interfaceBaliza : this.listaBalizasSelecionadas)
      {
          Pilha pilhaAtual = interfaceBaliza.getBalizaVisualizada().retornaStatusHorario(interfaceBaliza.getInterfacePatio().getHoraSituacao());
          
          if (pilhaAtual != null)
         {
            if (!listaPilhas.contains(pilhaAtual))
            {
               listaPilhas.add(pilhaAtual);
            }
         }
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
      pnlSelecaoProduto = new javax.swing.JPanel();
      jLabel1 = new javax.swing.JLabel();
      jLabel2 = new javax.swing.JLabel();
      cmbTipoProduto = new javax.swing.JComboBox();
      txtNomePilha = new javax.swing.JTextField();
      txtCorTipoProduto = new javax.swing.JTextField();
      pnlInformacoesBalizas = new javax.swing.JPanel();
      scrBalizas = new javax.swing.JScrollPane();
      tblBalizas = new com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable();
      cmdDesistir = new javax.swing.JButton();
      cmdConfirmar = new javax.swing.JButton();

      setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
      setTitle("Unificação de Pilhas");
      addWindowListener(new java.awt.event.WindowAdapter() {
         @Override
        public void windowClosing(java.awt.event.WindowEvent evt) {
            formWindowClosing(evt);
         }
      });

      pnlSelecaoProduto.setBorder(javax.swing.BorderFactory.createTitledBorder("Informações da Nova Pilha"));

      jLabel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
      jLabel1.setText("Produto:");

      jLabel2.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
      jLabel2.setText("Nome da pilha:");

      cmbTipoProduto.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
      cmbTipoProduto.addItemListener(new java.awt.event.ItemListener() {
         public void itemStateChanged(java.awt.event.ItemEvent evt) {
            cmbTipoProdutoItemStateChanged(evt);
         }
      });

      txtNomePilha.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

      txtCorTipoProduto.setEditable(false);

      javax.swing.GroupLayout pnlSelecaoProdutoLayout = new javax.swing.GroupLayout(pnlSelecaoProduto);
      pnlSelecaoProduto.setLayout(pnlSelecaoProdutoLayout);
      pnlSelecaoProdutoLayout.setHorizontalGroup(
         pnlSelecaoProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(pnlSelecaoProdutoLayout.createSequentialGroup()
            .addContainerGap()
            .addGroup(pnlSelecaoProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
               .addGroup(pnlSelecaoProdutoLayout.createSequentialGroup()
                  .addComponent(jLabel1)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                  .addComponent(cmbTipoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
               .addGroup(pnlSelecaoProdutoLayout.createSequentialGroup()
                  .addComponent(jLabel2)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                  .addComponent(txtNomePilha)))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(txtCorTipoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(96, Short.MAX_VALUE))
      );
      pnlSelecaoProdutoLayout.setVerticalGroup(
         pnlSelecaoProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(pnlSelecaoProdutoLayout.createSequentialGroup()
            .addContainerGap()
            .addGroup(pnlSelecaoProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(txtCorTipoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addGroup(pnlSelecaoProdutoLayout.createSequentialGroup()
                  .addGroup(pnlSelecaoProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                     .addComponent(jLabel1)
                     .addComponent(cmbTipoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                  .addGap(18, 18, 18)
                  .addGroup(pnlSelecaoProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                     .addComponent(jLabel2)
                     .addComponent(txtNomePilha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );

      pnlInformacoesBalizas.setBorder(javax.swing.BorderFactory.createTitledBorder("Balizas a serem unificadas"));
      pnlInformacoesBalizas.setLayout(new java.awt.BorderLayout());

      tblBalizas.setModel(new CFlexStockyardTableModelCustom());
      scrBalizas.setViewportView(tblBalizas);

      pnlInformacoesBalizas.add(scrBalizas, java.awt.BorderLayout.CENTER);

      cmdDesistir.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
      //cmdDesistir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icones/exit.png"))); // NOI18N
      cmdDesistir.setText(PropertiesUtil.getMessage("botao.desistir"));
      cmdDesistir.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            cmdDesistirActionPerformed(evt);
         }
      });

      cmdConfirmar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
      //cmdConfirmar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icones/confirmar.png"))); // NOI18N
      cmdConfirmar.setText(PropertiesUtil.getMessage("botao.confirmar"));
      cmdConfirmar.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            cmdConfirmarActionPerformed(evt);
         }
      });

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
                  .addComponent(cmdConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
               .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                  .addComponent(pnlInformacoesBalizas, javax.swing.GroupLayout.Alignment.LEADING, 0, 0, Short.MAX_VALUE)
                  .addComponent(pnlSelecaoProduto, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addContainerGap())
      );
      pnlPrincipalLayout.setVerticalGroup(
         pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(pnlPrincipalLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(pnlSelecaoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(pnlInformacoesBalizas, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(cmdConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(cmdDesistir))
            .addContainerGap(24, Short.MAX_VALUE))
      );

      getContentPane().add(pnlPrincipal, java.awt.BorderLayout.CENTER);

      pack();
   }// </editor-fold>//GEN-END:initComponents

   private void cmbTipoProdutoItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_cmbTipoProdutoItemStateChanged
   {//GEN-HEADEREND:event_cmbTipoProdutoItemStateChanged
      if (cmbTipoProduto.getItemCount() > 0)
      {
         tipoProdutoSelecionado = (TipoProduto) cmbTipoProduto.getSelectedItem();
         String[] rgb = tipoProdutoSelecionado.getCorIdentificacao().split(",");
         txtCorTipoProduto.setBackground(new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2])));
      }
   }//GEN-LAST:event_cmbTipoProdutoItemStateChanged

   private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
   {//GEN-HEADEREND:event_formWindowClosing
      this.operacaoCanceladaPeloUsuario = true;
   }//GEN-LAST:event_formWindowClosing

   private void cmdDesistirActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmdDesistirActionPerformed
   {//GEN-HEADEREND:event_cmdDesistirActionPerformed
      this.operacaoCanceladaPeloUsuario = true;
      this.setVisible(false);
   }//GEN-LAST:event_cmdDesistirActionPerformed

   private void cmdConfirmarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmdConfirmarActionPerformed
   {//GEN-HEADEREND:event_cmdConfirmarActionPerformed
      try
      {
         validaInformacoes();
         executaUnificacaoDePilha();
         
         this.operacaoCanceladaPeloUsuario = false;
         this.setVisible(false);
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

   
   private void executaUnificacaoDePilha()
   {
	   try
	   {
	       // cria o VO para a unificacao
	       EdicaoVO edicaoVO = new EdicaoVO(); 
	                          
	       Date dataAtividade = controladorDSP.getInterfaceInicial().getPlanejamento().getControladorDePlano().obterSituacaoPatioFinal().getDtInicio();
	       dataAtividade = Atividade.verificaAtualizaDataAtividade(dataAtividade, dataAtividade);
	       
	       edicaoVO.setDataInicio(dataAtividade);
	       edicaoVO.setDataFim(dataAtividade);
	       
	       // nome da pilha
	       edicaoVO.setNomePilha(txtNomePilha.getText().trim());
	       
	       // seta os novos status das balizas
	       List<Baliza> listaBalizasUnificadas = new ArrayList<Baliza>();
	       for (InterfaceBaliza ib : listaBalizasSelecionadas)
	       {
	    	   Baliza novoStatusBaliza = ib.getBalizaVisualizada().getMetaBaliza().clonarStatus(dataAtividade);
	    	   listaBalizasUnificadas.add(novoStatusBaliza);
	       }
	       edicaoVO.setListaBalizas(listaBalizasUnificadas);
	       
	       // pegando uma lista de itens de controle de qualidade como base para novo produto
	       List<ItemDeControle> listaItensControlePadrao = controladorDSP.getInterfaceInicial().getInterfaceInicial().getSituacaoPatioExibida().getListaDePilhasNosPatios(controladorDSP.getInterfaceInicial().getInterfaceInicial().getSituacaoPatioExibida().getDtInicio()).get(0).getListaDeBalizas().get(0).getProduto().getQualidade().getListaDeItensDeControle();
	       
	       // seta o produto das novas balizas
	       for (Baliza novaBaliza : listaBalizasUnificadas)
	       {
	    	  if (novaBaliza.getProduto() == null)
	    	  {
	              Produto produtoNovo = new Produto();
	              produtoNovo.setQuantidade(0d);
	              if (novaBaliza.getProduto() != null)
	            	  produtoNovo.setQuantidade(novaBaliza.getProduto().getQuantidade());
	              produtoNovo.setDtInicio(dataAtividade);
	              produtoNovo.setDtInsert(dataAtividade);
	              novaBaliza.setProduto(produtoNovo);

	              // criando qualidade para produto novo
	              if (produtoNovo.getQualidade() == null)
	              {
		              Qualidade qualidadeReal = new Qualidade();
		              qualidadeReal.setDtInicio(dataAtividade);
		              qualidadeReal.setDtInsert(dataAtividade);
		              qualidadeReal.setEhReal(Boolean.FALSE);
		              qualidadeReal.setListaDeItensDeControle(Blendagem.criaListaDeItensDeControleComValoresVazios(listaItensControlePadrao));
		              for (ItemDeControle ic : qualidadeReal.getListaDeItensDeControle())
		              {
		            	  ic.setDtInicio(dataAtividade);
		            	  ic.setDtInsert(dataAtividade);
		              }
		              produtoNovo.setQualidade(qualidadeReal);
		              produtoNovo.getQualidade().setProduto(produtoNovo);
	              }
	    	  }
	    	  novaBaliza.getProduto().setTipoProduto(tipoProdutoSelecionado);
	       }
	       
	       ControladorExecutarEdicaoBalizas service = ControladorExecutarEdicaoBalizas.getInstance();
	       Atividade atividadeEdicaoBaliza =  service.unificarBalizas(edicaoVO);
	       
	       controladorDSP.getInterfaceInicial().editarBalizas(atividadeEdicaoBaliza);
	       controladorDSP.getInterfaceInicial().getInterfaceInicial().atualizarDSP();
	       
	       controladorDSP.getInterfaceInicial().getInterfaceComandos().finalizarEdicoes();
	   }
	   catch (AtividadeException ex)
	   {
	      controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
	      interfaceMensagem = new InterfaceMensagem();
	      interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
	      interfaceMensagem.setDescricaoMensagem(ex.getMessage());
	      controladorDSP.ativarMensagem(interfaceMensagem);
	   }
	   operacaoCanceladaPeloUsuario = Boolean.FALSE;
	   this.dispose();
	 //  }
	}//GEN-LAST:event_cmdConfirmarEdicaoBalizasActionPerformed

   /** Metodo que localiza uma baliza em uma determinada lista */
   private Baliza localizarBalizaNaLista(List<Baliza> listaBalizas, Baliza baliza)
   {
      Baliza balizaRetorno = null;
      for (Baliza balizaDaLista : listaBalizas)
      {
         if (baliza.getNumero().equals(balizaDaLista.getNumero()))
         {
            balizaRetorno = balizaDaLista;
            break;
         }
      }
      return balizaRetorno;
   }


   /** Metodo que valida as informacoes digitadas pelo usuario */
   private void validaInformacoes() throws ValidacaoCampoException
   {
      if (txtNomePilha.getText().trim().equals(""))
      {
         throw new ValidacaoCampoException(PropertiesUtil.getMessage("mensagem.pilha.nome.nao.preenchido"));
      }
   }
   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JComboBox cmbTipoProduto;
   private javax.swing.JButton cmdConfirmar;
   private javax.swing.JButton cmdDesistir;
   private javax.swing.JButton desistirJB;
   private javax.swing.JButton desistirJB1;
   private javax.swing.JLabel jLabel1;
   private javax.swing.JLabel jLabel2;
   private javax.swing.JPanel pnlInformacoesBalizas;
   private javax.swing.JPanel pnlPrincipal;
   private javax.swing.JPanel pnlSelecaoProduto;
   private javax.swing.JScrollPane scrBalizas;
   private com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable tblBalizas;
   private javax.swing.JTextField txtCorTipoProduto;
   private javax.swing.JTextField txtNomePilha;
   // End of variables declaration//GEN-END:variables

   public Boolean getOperacaoCanceladaPeloUsuario()
   {
      return operacaoCanceladaPeloUsuario;
   }
}

