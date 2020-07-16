package com.hdntec.gestao.cliente.atualizacao.sistemasexternos.sap;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.SwingConstants;
import javax.swing.text.MaskFormatter;

import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.ControladorDSP;
import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.cliente.util.numeros.DSSStockyardFuncoesNumeros;
import com.hdntec.gestao.cliente.util.tabela.ColunaTabela;
import com.hdntec.gestao.domain.integracao.IntegracaoSAP;
import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Pilha;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.ValidacaoCampoException;
import com.hdntec.gestao.integracao.controladores.ControladorModelo;
import com.hdntec.gestao.integracao.controladores.IControladorModelo;
import com.hdntec.gestao.util.PropertiesUtil;
import com.hdntec.gestao.util.datahora.DSSStockyardTimeUtil;

public class InformacaoDiferencaEstoqueGUI extends javax.swing.JDialog
{

   private List<ColunaTabela> listaColunas;

   /** a interface de mensagem */
   private InterfaceMensagem interfaceMensagem;

   /** controlador das operacoes do DSP */
   private ControladorDSP controladorDSP;

   /** Lista com as informações da tabela de comparacoes */
   private List<TipoProdutoIntegracaoSAP> listaInformacoesEstoque;

   /** mapa com os tipos de produto cadastrados no sistema */
   private HashMap<String, TipoProduto> mapaTiposProdutoCadastrado;



   /** Creates new form InformacaoDiferencaEstoqueGUI */
   public InformacaoDiferencaEstoqueGUI(java.awt.Frame parent, boolean modal, ControladorDSP controladorDSP) throws ErroSistemicoException
   {
      super(parent, modal);
      initComponents();
      this.controladorDSP = controladorDSP;
      criaColunas();
      listaInformacoesEstoque = new ArrayList<TipoProdutoIntegracaoSAP>();
      criaMapaTiposProduto();
      carregaDadosTabela();
   }

   private void limparDadosPesquisa() throws ErroSistemicoException
   {
      listaInformacoesEstoque.clear();
      txtDataInicial.setText("");
      txtDataFinal.setText("");
      carregaDadosTabela();
      txtDataInicial.requestFocus();
   }

   /** Carregando o mapa dos tipos de produtos cadastrados */
   private void criaMapaTiposProduto() throws ErroSistemicoException
   {
      mapaTiposProdutoCadastrado = new HashMap<String, TipoProduto>();
      //Darley RMI OFF
//      IControladorModelo controladorModelo = InterfaceInicial.lookUpModelo();
      IControladorModelo controladorModelo = new ControladorModelo();
      List<TipoProduto> listaTiposProduto = controladorModelo.buscarTiposProduto(new TipoProduto());
      for (TipoProduto tipoProduto : listaTiposProduto)
      {
         if (tipoProduto.getCdProduto() != null && !tipoProduto.getCdProduto().equals("-"))
         {
            mapaTiposProdutoCadastrado.put(tipoProduto.getCdProduto(), tipoProduto);
         }
      }
      controladorModelo = null;
      controladorDSP.getInterfaceInicial().setModoOperacao(null);
   }

   private void criaColunas()
   {
      listaColunas = new ArrayList<ColunaTabela>();
      ColunaTabela coluna;

      coluna = new ColunaTabela();
      coluna.setTitulo(PropertiesUtil.getMessage("mensagem.produto.tipo"));
      coluna.setEditar(Boolean.FALSE);
      coluna.setLargura(150);
      coluna.setRedimensionar(Boolean.FALSE);
      coluna.setAlinhamento(SwingConstants.LEADING);
      listaColunas.add(coluna);

      coluna = new ColunaTabela();
      coluna.setTitulo(PropertiesUtil.getMessage("coluna.data.estoque"));
      coluna.setEditar(Boolean.FALSE);
      coluna.setLargura(120);
      coluna.setRedimensionar(Boolean.FALSE);
      coluna.setAlinhamento(SwingConstants.CENTER);
      listaColunas.add(coluna);

      coluna = new ColunaTabela();
      coluna.setTitulo(PropertiesUtil.getMessage("coluna.quantidade.SAP"));
      coluna.setEditar(Boolean.FALSE);
      coluna.setLargura(130);
      coluna.setRedimensionar(Boolean.FALSE);
      coluna.setAlinhamento(SwingConstants.RIGHT);
      listaColunas.add(coluna);

      coluna = new ColunaTabela();
      coluna.setTitulo(PropertiesUtil.getMessage("coluna.quantidade.MESPATIO"));
      coluna.setEditar(Boolean.FALSE);
      coluna.setLargura(150);
      coluna.setRedimensionar(Boolean.FALSE);
      coluna.setAlinhamento(SwingConstants.RIGHT);
      listaColunas.add(coluna);

      coluna = new ColunaTabela();
      coluna.setTitulo(PropertiesUtil.getMessage("coluna.diferenca.estoque"));
      coluna.setEditar(Boolean.FALSE);
      coluna.setLargura(130);
      coluna.setRedimensionar(Boolean.FALSE);
      coluna.setAlinhamento(SwingConstants.RIGHT);
      listaColunas.add(coluna);

   }

   private void carregaDadosTabela() throws ErroSistemicoException
   {
      try {
         Vector dadosTabela = new Vector();
         for (TipoProdutoIntegracaoSAP tipoProduto : listaInformacoesEstoque) {
            if (tipoProduto.getTipoProduto() != null)
            {
               Object[] dados = new Object[5];
               dados[0] = tipoProduto.getTipoProduto().toString();
               dados[1] = DSSStockyardTimeUtil.formatarData(tipoProduto.getDataEstoque(), PropertiesUtil.buscarPropriedade("formato.campo.data"));
               dados[2] = DSSStockyardFuncoesNumeros.getQtdeFormatada(tipoProduto.getQuantidaeEstoqueSAP(), 2);
               dados[3] = DSSStockyardFuncoesNumeros.getQtdeFormatada(tipoProduto.getQuantidadeEstoqueMESPATIO(), 2);
               dados[4] = DSSStockyardFuncoesNumeros.getQtdeFormatada(tipoProduto.getQuantidadeProdutoDivergente(), 2);

               dadosTabela.add(new Vector(Arrays.asList(dados)));
            }
         }

      //   CFlexStockyardFuncoesTabela.setLinhasTabela(tblDiferencaEstoque, dadosTabela, listaColunas);

      }
      catch (Exception ex) {
         throw new ErroSistemicoException(ex.getMessage());
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

      scrDiferencaEstoque = new javax.swing.JScrollPane();
      tblDiferencaEstoque = new com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable();
      pnlFiltros = new javax.swing.JPanel();
      jLabel1 = new javax.swing.JLabel();
      txtDataInicial = new javax.swing.JFormattedTextField();
      MaskFormatter fmtDataHoraInicio = new MaskFormatter();
      try {
         fmtDataHoraInicio.setMask(PropertiesUtil.buscarPropriedade("mascara.campo.data"));
         fmtDataHoraInicio.setPlaceholderCharacter(' ');
         fmtDataHoraInicio.install(txtDataInicial);
      } catch (ParseException pex) {
         pex.printStackTrace();
      }
      jLabel2 = new javax.swing.JLabel();
      txtDataFinal = new javax.swing.JFormattedTextField();
      MaskFormatter fmtDataHoraFinal = new MaskFormatter();
      try {
         fmtDataHoraFinal.setMask(PropertiesUtil.buscarPropriedade("mascara.campo.data"));
         fmtDataHoraFinal.setPlaceholderCharacter(' ');
         fmtDataHoraFinal.install(txtDataFinal);
      } catch (ParseException pex) {
         pex.printStackTrace();
      }
      cmdPesquisar = new javax.swing.JButton();
      cmdPesquisar1 = new javax.swing.JButton();
      cmdDesistir = new javax.swing.JButton();

      setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
      setTitle(PropertiesUtil.getMessage("mensagem.titulo.integracao"));

      tblDiferencaEstoque.setModel(new javax.swing.table.DefaultTableModel(
         new Object [][] {

         },
         new String [] {

         }
      ));
      scrDiferencaEstoque.setViewportView(tblDiferencaEstoque);

      pnlFiltros.setBorder(javax.swing.BorderFactory.createTitledBorder(PropertiesUtil.getMessage("titulo.painel.filtro")));

      jLabel1.setFont(new java.awt.Font("Arial", 1, 12));
      jLabel1.setText(PropertiesUtil.getMessage("label.texto.comparar.estoque.periodo"));

      txtDataInicial.setFont(new java.awt.Font("Arial", 0, 12));

      jLabel2.setFont(new java.awt.Font("Arial", 1, 12));
      jLabel2.setText(PropertiesUtil.getMessage("label.texto.comparar.estoque.periodo.ate"));

      txtDataFinal.setFont(new java.awt.Font("Arial", 0, 12));

      cmdPesquisar.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
      cmdPesquisar.setText(PropertiesUtil.getMessage("label.pesquisar"));
      cmdPesquisar.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            cmdPesquisarActionPerformed(evt);
         }
      });

      cmdPesquisar1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
      cmdPesquisar1.setText(PropertiesUtil.getMessage("botao.limpar.dados"));
      cmdPesquisar1.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            cmdPesquisar1ActionPerformed(evt);
         }
      });

      javax.swing.GroupLayout pnlFiltrosLayout = new javax.swing.GroupLayout(pnlFiltros);
      pnlFiltros.setLayout(pnlFiltrosLayout);
      pnlFiltrosLayout.setHorizontalGroup(
         pnlFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(pnlFiltrosLayout.createSequentialGroup()
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(txtDataInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(txtDataFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(20, 20, 20)
            .addComponent(cmdPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(cmdPesquisar1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(20, Short.MAX_VALUE))
      );
      pnlFiltrosLayout.setVerticalGroup(
         pnlFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(pnlFiltrosLayout.createSequentialGroup()
            .addGroup(pnlFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel1)
               .addComponent(cmdPesquisar)
               .addComponent(txtDataInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(txtDataFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel2)
               .addComponent(cmdPesquisar1))
            .addContainerGap(18, Short.MAX_VALUE))
      );

      cmdDesistir.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
      cmdDesistir.setText(PropertiesUtil.getMessage("botao.desistir"));
      cmdDesistir.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            cmdDesistirActionPerformed(evt);
         }
      });

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
      getContentPane().setLayout(layout);
      layout.setHorizontalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(scrDiferencaEstoque, javax.swing.GroupLayout.DEFAULT_SIZE, 695, Short.MAX_VALUE)
               .addComponent(pnlFiltros, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
               .addComponent(cmdDesistir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap())
      );
      layout.setVerticalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(pnlFiltros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(scrDiferencaEstoque, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(cmdDesistir)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );

      pack();
   }// </editor-fold>//GEN-END:initComponents

   private void cmdPesquisarActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmdPesquisarActionPerformed
   {//GEN-HEADEREND:event_cmdPesquisarActionPerformed
      try
      {
         validarInformacoesFiltro();
         buscarInformacoesEstoque();
         carregaDadosTabela();
      }
      catch (ValidacaoCampoException vcEx)
      {
         controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
         interfaceMensagem = new InterfaceMensagem();
         interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
         interfaceMensagem.setDescricaoMensagem(vcEx.getMessage());
         controladorDSP.ativarMensagem(interfaceMensagem);
      }
      catch (ErroSistemicoException errEx)
      {
         controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
         interfaceMensagem = new InterfaceMensagem();
         interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
         interfaceMensagem.setDescricaoMensagem(errEx.getMessage());
         controladorDSP.ativarMensagem(interfaceMensagem);
      }
   }//GEN-LAST:event_cmdPesquisarActionPerformed

   private void cmdPesquisar1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmdPesquisar1ActionPerformed
   {//GEN-HEADEREND:event_cmdPesquisar1ActionPerformed
      try
      {
         limparDadosPesquisa();
      }
      catch (ErroSistemicoException errEx)
      {
         controladorDSP.getInterfaceInicial().desativarMensagemProcessamento();
         interfaceMensagem = new InterfaceMensagem();
         interfaceMensagem.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
         interfaceMensagem.setDescricaoMensagem(errEx.getMessage());
         controladorDSP.ativarMensagem(interfaceMensagem);
      }
   }//GEN-LAST:event_cmdPesquisar1ActionPerformed

   private void cmdDesistirActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmdDesistirActionPerformed
   {//GEN-HEADEREND:event_cmdDesistirActionPerformed
      this.setVisible(false);
   }//GEN-LAST:event_cmdDesistirActionPerformed

   private void buscarInformacoesEstoque() throws ValidacaoCampoException, ErroSistemicoException
   {
      Date dataInicial =null ;
	try {
		dataInicial = DSSStockyardTimeUtil.criaDataComString(txtDataInicial.getText(), PropertiesUtil.buscarPropriedade("formato.campo.data"));
	} catch (ValidacaoCampoException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      StringBuffer value = new StringBuffer();
      value.append(txtDataFinal.getText()).append(" 23:59:59");
      Date dataFinal = null;
	try {
		dataFinal = DSSStockyardTimeUtil.criaDataComString(value.toString(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
	} catch (ValidacaoCampoException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      value = null;
      //Darley RMI OFF
//      IControladorModelo controladorModelo = InterfaceInicial.lookUpModelo();
      IControladorModelo controladorModelo = new ControladorModelo();
      List<IntegracaoSAP> listaDadosSAP = controladorModelo.buscarListaDadosTotalizadosSAP(dataInicial, dataFinal);

      Date dataPesquisa = null;
      SituacaoPatio situacaoPatio = null;

      for (IntegracaoSAP integracaoSAP : listaDadosSAP)
      {
         if (dataPesquisa == null || !dataPesquisa.equals(integracaoSAP.getDataLeitura()))
         {
            dataPesquisa = integracaoSAP.getDataLeitura();
            situacaoPatio = controladorModelo.buscarUltimaSituacaoPatioDoDia(dataPesquisa);
         }
         if (situacaoPatio != null) listaInformacoesEstoque.add(montaTipoProdutoIntegracaoSAP(situacaoPatio, integracaoSAP));
      }
      controladorModelo = null;
      controladorDSP.getInterfaceInicial().setModoOperacao(null);
   }

   /**
    * Monta o objeto que sera exibido na tela
    * @param situacaoPatio
    * @param integracaoSAP
    * @return
    */
   private TipoProdutoIntegracaoSAP montaTipoProdutoIntegracaoSAP(SituacaoPatio situacaoPatio, IntegracaoSAP integracaoSAP)
   {
      TipoProdutoIntegracaoSAP tipoProdutoIntegracao = new TipoProdutoIntegracaoSAP();
      TipoProduto tipoProdutoSituacao = mapaTiposProdutoCadastrado.get(integracaoSAP.getCodigoTipoProduto());

      tipoProdutoIntegracao.setTipoProduto(tipoProdutoSituacao);
      tipoProdutoIntegracao.setQuantidaeEstoqueSAP(integracaoSAP.getValorEstoque());
      tipoProdutoIntegracao.setDataEstoque(integracaoSAP.getDataLeitura());
      
      if (tipoProdutoSituacao != null)
      {
         Double quantidadeEstoqueMesPatio = 0D;
         for (Pilha pilhaPatio : situacaoPatio.getListaDePilhasNosPatios(situacaoPatio.getDtInicio()))
         {
            if (pilhaPatio.verificarTipoProdutoPilha(tipoProdutoSituacao))
            {
               quantidadeEstoqueMesPatio += pilhaPatio.obterQuantidadeTotalProdutos();
            }
         }

         tipoProdutoIntegracao.setQuantidadeEstoqueMESPATIO(quantidadeEstoqueMesPatio);

         Double quantidadeDivergente = integracaoSAP.getValorEstoque() - quantidadeEstoqueMesPatio;
         tipoProdutoIntegracao.setQuantidadeProdutoDivergente(quantidadeDivergente);
      }
      else
      {
         tipoProdutoIntegracao.setQuantidadeEstoqueMESPATIO(0D);
         tipoProdutoIntegracao.setQuantidadeProdutoDivergente(integracaoSAP.getValorEstoque());
      }

      return tipoProdutoIntegracao;
   }



   private void validarInformacoesFiltro() throws ValidacaoCampoException
   {

      if (txtDataInicial.getText().equals(DSSStockyardTimeUtil.DATAHORA_EM_BRANCO))
      {
         throw new ValidacaoCampoException(PropertiesUtil.getMessage("mensagem.data.inicio.obrigatoria"));
      }

      try {
		DSSStockyardTimeUtil.validarData(txtDataInicial.getText(), PropertiesUtil.getMessage("aviso.validacao.data.inicial"));
	} catch (ValidacaoCampoException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

      if (txtDataFinal.getText().equals(DSSStockyardTimeUtil.DATAHORA_EM_BRANCO))
      {
         throw new ValidacaoCampoException(PropertiesUtil.getMessage("mensagem.data.final.obrigatoria"));
      }

      try {
		DSSStockyardTimeUtil.validarData(txtDataFinal.getText(), PropertiesUtil.getMessage("aviso.validacao.data.final"));
	} catch (ValidacaoCampoException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

      Date dataInicial = null;
	try {
		dataInicial = DSSStockyardTimeUtil.criaDataComString( txtDataInicial.getText(), PropertiesUtil.buscarPropriedade("formato.campo.data"));
	} catch (ValidacaoCampoException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      Date dataFinal = null;
	try {
		dataFinal = DSSStockyardTimeUtil.criaDataComString( txtDataFinal.getText(), PropertiesUtil.buscarPropriedade("formato.campo.data"));
	} catch (ValidacaoCampoException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      if(dataFinal.before(dataInicial)){
         throw new ValidacaoCampoException(PropertiesUtil.getMessage("aviso.data.periodo.final.deve.ser.maior.que.periodo.inicial"));
      }

   }

   public List<TipoProdutoIntegracaoSAP> getListaInformacoesEstoque()
   {
      return listaInformacoesEstoque;
   }

   public void setListaInformacoesEstoque(List<TipoProdutoIntegracaoSAP> listaInformacoesEstoque)
   {
      this.listaInformacoesEstoque = listaInformacoesEstoque;
   }

   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JButton cmdDesistir;
   private javax.swing.JButton cmdPesquisar;
   private javax.swing.JButton cmdPesquisar1;
   private javax.swing.JLabel jLabel1;
   private javax.swing.JLabel jLabel2;
   private javax.swing.JPanel pnlFiltros;
   private javax.swing.JScrollPane scrDiferencaEstoque;
   private com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable tblDiferencaEstoque;
   private javax.swing.JFormattedTextField txtDataFinal;
   private javax.swing.JFormattedTextField txtDataInicial;
   // End of variables declaration//GEN-END:variables

}
