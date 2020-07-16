package com.hdntec.gestao.cliente.relatorio;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import com.hdntec.gestao.cliente.interfaceDeDialogo.InterfaceInicial;
import com.hdntec.gestao.domain.relatorio.PadraoRelatorio;
import com.hdntec.gestao.domain.relatorio.Relatorio;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.util.PropertiesUtil;

public class InterfaceConfigurarPadraoRelatorio extends JDialog implements ActionListener
{

   private static final long serialVersionUID = 1L;

   private JTable tabelaConfiguracoes;

   private StockYardTableModel modeloTabela;

   private JScrollPane barraRolagem;

   private JPanel painelBotoes;

   private JButton botaoNovo;

   private JButton botaoAlterar;

   private JButton botaoExcluir;

   private JButton botaoVoltar;

   private ControladorInterfaceGestaoRelatorio controlador;

   private InterfaceGestaoRelatorio interfaceGestaoRelatorio;

   private List<PadraoRelatorio> listaPadroesRelatorios;

   public InterfaceConfigurarPadraoRelatorio(InterfaceGestaoRelatorio parent, ControladorInterfaceGestaoRelatorio controlador)
   {
	super(parent, true);
	this.controlador = controlador;
	this.interfaceGestaoRelatorio = parent;
	initComponents();
	atualizarComponentes();
   }

   private void initComponents()
   {
	setTitle(PropertiesUtil.getMessage("relatorio.configurar.padrao.title"));
	setSize(400, 300);

	Container container = getContentPane();
	container.setLayout(new BorderLayout());

	modeloTabela = criarModeloTabela();
	tabelaConfiguracoes = new JTable(modeloTabela);
	tabelaConfiguracoes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	barraRolagem = new JScrollPane(tabelaConfiguracoes);
	container.add(barraRolagem, BorderLayout.CENTER);

	painelBotoes = new JPanel(new GridBagLayout());
	botaoNovo = new JButton(PropertiesUtil.getMessage("relatorio.configurar.padrao.botao.novo"));
	botaoNovo.addActionListener(this);
	painelBotoes.add(botaoNovo, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
	botaoAlterar = new JButton(PropertiesUtil.getMessage("relatorio.configurar.padrao.botao.alterar"));
	botaoAlterar.addActionListener(this);
	painelBotoes.add(botaoAlterar, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
	botaoExcluir = new JButton(PropertiesUtil.getMessage("relatorio.configurar.padrao.botao.excluir"));
	botaoExcluir.addActionListener(this);
	painelBotoes.add(botaoExcluir, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
	botaoVoltar = new JButton(PropertiesUtil.getMessage("relatorio.configurar.padrao.botao.voltar"));
	botaoVoltar.addActionListener(this);
	painelBotoes.add(botaoVoltar, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
	container.add(painelBotoes, BorderLayout.SOUTH);
   }

   private StockYardTableModel criarModeloTabela()
   {
	StockYardTableModel modelo = new StockYardTableModel();

	try
	{
	   listaPadroesRelatorios = controlador.getControladorGestaoRelatorio().buscarPadroesRelatorio();
	   modelo.addColumn(PropertiesUtil.getMessage("relatorio.configurar.padrao.table.coluna1"), listaPadroesRelatorios.toArray());
	}
	catch (ErroSistemicoException errEx)
	{
		errEx.printStackTrace();
	   /*InterfaceMensagem interfaceMensagem = new InterfaceMensagem();
	   interfaceMensagem.setTipoMensagem(interfaceMensagem.MENSAGEM_TIPO_ERRO);
	   interfaceMensagem.setDescricaoMensagem(errEx.getMessage());
	   interfaceGestaoRelatorio.getInterfaceInicial().ativarMensagem(interfaceMensagem);*/
	}

	return modelo;
   }

   @Override
   public void actionPerformed(ActionEvent e)
   {
	if (e.getSource() == botaoNovo)
	{
	   InterfacePadraoRelatorio padraoRelatorio = new InterfacePadraoRelatorio(this, controlador, null);
	   padraoRelatorio.setLocationRelativeTo(null);
	   padraoRelatorio.setVisible(true);
	}
	else if (e.getSource() == botaoAlterar)
	{
	   PadraoRelatorio padraoSelecionado = null;
	   if (tabelaConfiguracoes.getSelectedRow() >= 0)
	   {
		for (PadraoRelatorio padraoRelatorio : listaPadroesRelatorios)
		{
		   Object padrao = tabelaConfiguracoes.getModel().getValueAt(tabelaConfiguracoes.getSelectedRow(), 0);
		   if (padraoRelatorio.getNomePadraoRelatorio().equals(((PadraoRelatorio) padrao).getNomePadraoRelatorio()))
		   {
			padraoSelecionado = padraoRelatorio;
		   }
		}
		InterfacePadraoRelatorio padraoRelatorio = new InterfacePadraoRelatorio(this, controlador, padraoSelecionado);
		padraoRelatorio.setLocationRelativeTo(null);
		padraoRelatorio.setVisible(true);
	   }

	}
	else if (e.getSource() == botaoExcluir)
	{
	   PadraoRelatorio padraoSelecionado = null;
	   if (tabelaConfiguracoes.getSelectedRow() >= 0)
	   {
		for (PadraoRelatorio padraoRelatorio : listaPadroesRelatorios)
		{
		   Object padrao = tabelaConfiguracoes.getModel().getValueAt(tabelaConfiguracoes.getSelectedRow(), 0);
		   if (padraoRelatorio.getNomePadraoRelatorio().equals(((PadraoRelatorio) padrao).getNomePadraoRelatorio()))
		   {
			padraoSelecionado = padraoRelatorio;
		   }
		}
		removerPadraoRelatorio(padraoSelecionado);
		atualizaTabela();
	   }
	}
	else if (e.getSource() == botaoVoltar)
	{
	   dispose();
	}
   }

   /**
    *
    * removerPadraoRelatorio
    * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
    * @since 30/06/2009
    * @see
    * @param padrao
    * @return Returns the void.
    */
   private void removerPadraoRelatorio(PadraoRelatorio padrao)
   {
	try
	{
	   if (padrao == null)
	   {
		JOptionPane.showMessageDialog(this, PropertiesUtil.getMessage("relatorio.configurar.padrao.excluir.notfound"), PropertiesUtil.getMessage("relatorio.configurar.padrao.title"), JOptionPane.INFORMATION_MESSAGE);
	   }
	   else
	   {
		List<Relatorio> relatoriosDoPadrao = controlador.getControladorGestaoRelatorio().buscarRelatoriosPorPadrao(padrao);
		if (relatoriosDoPadrao.isEmpty())
		{

		   int opcao = JOptionPane.showConfirmDialog(this, PropertiesUtil.getMessage("relatorio.configurar.padrao.excluir.question"), PropertiesUtil.getMessage("relatorio.configurar.padrao.title"), JOptionPane.YES_NO_OPTION);
		   if (JOptionPane.YES_OPTION == opcao)
		   {
			controlador.getControladorGestaoRelatorio().removerPadraoRelatorio(padrao);
		   }
		}
		else
		{
		   JOptionPane.showMessageDialog(this, PropertiesUtil.getMessage("relatorio.configurar.padrao.excluir.naopermitida"), PropertiesUtil.getMessage("relatorio.configurar.padrao.title"), JOptionPane.INFORMATION_MESSAGE);
		}
	   }
	}
	catch (ErroSistemicoException esEx)
	{
	   JOptionPane.showMessageDialog(this, PropertiesUtil.getMessage("relatorio.configurar.padrao.excluir.error"), PropertiesUtil.getMessage("relatorio.configurar.padrao.title"), JOptionPane.INFORMATION_MESSAGE);
	}
   }

   public void atualizaTabela()
   {
	modeloTabela = criarModeloTabela();
	tabelaConfiguracoes.setModel(modeloTabela);
	atualizarComponentes();
   }

   private void atualizarComponentes()
   {
	if (tabelaConfiguracoes.getModel().getRowCount() == 0)
	{
	   botaoExcluir.setEnabled(false);
	   botaoAlterar.setEnabled(false);
	}
	else
	{
	   botaoExcluir.setEnabled(true);
	   botaoAlterar.setEnabled(true);
	}
   }

   public InterfaceInicial getInterfaceInicial()
   {
	return this.interfaceGestaoRelatorio.getInterfaceInicial();
   }
}
