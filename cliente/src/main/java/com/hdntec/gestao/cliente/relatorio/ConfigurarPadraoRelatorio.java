package com.hdntec.gestao.cliente.relatorio;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.hdntec.gestao.util.PropertiesUtil;

public class ConfigurarPadraoRelatorio extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTable tabelaConfiguracoes;
	private DefaultTableModel modeloTabela;
	private JScrollPane barraRolagem;
	private JPanel painelBotoes;
	private JButton botaoNovo;
	private JButton botaoAlterar;
	private JButton botaoExcluir;
	private JButton botaoVoltar;

	public ConfigurarPadraoRelatorio() {
		initComponents();
	}

	private void initComponents() {
		setTitle(PropertiesUtil.getMessage("relatorio.configurar.padrao.title"));
		setSize(500, 250);

		Container container = getContentPane();
		container.setLayout(new BorderLayout());

		modeloTabela = criarModeloTabela();
		tabelaConfiguracoes = new JTable(modeloTabela);
		barraRolagem = new JScrollPane(tabelaConfiguracoes);
		container.add(barraRolagem, BorderLayout.CENTER);

		painelBotoes = new JPanel(new GridBagLayout());
		botaoNovo = new JButton(PropertiesUtil.getMessage("relatorio.configurar.padrao.botao.novo"));
		painelBotoes.add(botaoNovo, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
		botaoAlterar = new JButton(PropertiesUtil.getMessage("relatorio.configurar.padrao.botao.alterar"));
		painelBotoes.add(botaoAlterar, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
		botaoExcluir = new JButton(PropertiesUtil.getMessage("relatorio.configurar.padrao.botao.excluir"));
		painelBotoes.add(botaoExcluir, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
		botaoVoltar = new JButton(PropertiesUtil.getMessage("relatorio.configurar.padrao.botao.voltar"));
		painelBotoes.add(botaoVoltar, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
		container.add(painelBotoes, BorderLayout.SOUTH);
	}

	private DefaultTableModel criarModeloTabela() {
	    String[] nomeColunas = {PropertiesUtil.getMessage("relatorio.configurar.padrao.table.coluna1"),PropertiesUtil.getMessage("relatorio.configurar.padrao.table.coluna2")};
	    DefaultTableModel modelo = new DefaultTableModel(nomeColunas, 10);

	    return modelo;
	  }

	public static void main(String[] args) {
		ConfigurarPadraoRelatorio gr = new ConfigurarPadraoRelatorio();
		gr.setVisible(true);
	}

}
