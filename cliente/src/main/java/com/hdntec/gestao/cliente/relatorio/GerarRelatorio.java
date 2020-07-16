package com.hdntec.gestao.cliente.relatorio;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.hdntec.gestao.util.PropertiesUtil;
import com.hdntec.gestao.util.datahora.DateTimeGUI;


public class GerarRelatorio extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel painelTipo;
	private JRadioButton tipoLocalJRB;
	private JRadioButton tipoOficialJRB;
	private JPanel painelFormulario;
	private JLabel rotuloNomeRelatorio;
	private JTextField nomeRelatorioJTF;
	private JLabel rotuloPeriodo;
	private JLabel rotuloA;
	private DateTimeGUI inicioDTG;
	private DateTimeGUI fimDTG;
	private JLabel rotuloModelo;
	private JComboBox modeloJCB;
	private JPanel painelBotoes;
	private JButton botaoGerarRelatorio;
	private JButton botaoCancelar;

	public GerarRelatorio() {
		initComponents();
	}

	private void initComponents() {
		setTitle(PropertiesUtil.getMessage("relatorio.gerar.title"));
		setSize(500, 250);
		setResizable(false);

		Container container = getContentPane();
		container.setLayout(new BorderLayout());

		// tipo de relat�rio
		painelTipo = new JPanel(new GridBagLayout());
		painelTipo.setBorder(BorderFactory.createTitledBorder(PropertiesUtil.getMessage("relatorio.gerar.tipo.label")));
		tipoLocalJRB = new JRadioButton(PropertiesUtil.getMessage("relatorio.gerar.tipo.local.label"));
		painelTipo.add(tipoLocalJRB, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 12, 8, 12), 0, 0));
		tipoOficialJRB = new JRadioButton(PropertiesUtil.getMessage("relatorio.gerar.tipo.oficial.label"));
		painelTipo.add(tipoOficialJRB, new GridBagConstraints(2, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 12, 8, 12), 0, 0));
		container.add(painelTipo, BorderLayout.NORTH);

		// formul�rio
		painelFormulario = new JPanel(new GridBagLayout());
		rotuloNomeRelatorio = new JLabel(PropertiesUtil.getMessage("relatorio.gerar.formulario.nome.label"));
		painelFormulario.add(rotuloNomeRelatorio, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(8, 8, 8, 8), 0, 0));
		nomeRelatorioJTF = new JTextField(20);
		painelFormulario.add(nomeRelatorioJTF, new GridBagConstraints(1, 0, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(8, 8, 8, 8), 0, 0));

		rotuloPeriodo = new JLabel(PropertiesUtil.getMessage("relatorio.gerar.formulario.periodo.label"));
		painelFormulario.add(rotuloPeriodo, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(8, 8, 8, 8), 0, 0));
		inicioDTG = new DateTimeGUI(System.currentTimeMillis());
		painelFormulario.add(inicioDTG, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(8, 8, 8, 8), 0, 0));
		rotuloA = new JLabel(PropertiesUtil.getMessage("relatorio.gerar.formulario.rotulaa.label"));
		painelFormulario.add(rotuloA, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(8, 8, 8, 8), 0, 0));
		fimDTG = new DateTimeGUI(System.currentTimeMillis());
		painelFormulario.add(fimDTG, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(8, 8, 8, 8), 0, 0));

		rotuloModelo = new JLabel(PropertiesUtil.getMessage("relatorio.gerar.formulario.modelo.label"));
		painelFormulario.add(rotuloModelo, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(8, 8, 8, 8), 0, 0));

		modeloJCB = new JComboBox();
		painelFormulario.add(modeloJCB, new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(8, 8, 8, 8), 0, 0));
		container.add(painelFormulario, BorderLayout.CENTER);

		// bot�es
		painelBotoes = new JPanel(new GridBagLayout());
		botaoGerarRelatorio = new JButton(PropertiesUtil.getMessage("relatorio.gerar.formulario.botao.gerar"));
		painelBotoes.add(botaoGerarRelatorio, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
		botaoCancelar = new JButton(PropertiesUtil.getMessage("relatorio.gerar.formulario.botao.cancelar"));
		painelBotoes.add(botaoCancelar, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
		container.add(painelBotoes, BorderLayout.SOUTH);
	}

	public static void main(String[] args) {
		GerarRelatorio gr = new GerarRelatorio();
		gr.setVisible(true);
	}

}
