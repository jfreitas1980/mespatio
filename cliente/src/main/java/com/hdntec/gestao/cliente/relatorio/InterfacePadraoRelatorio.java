package com.hdntec.gestao.cliente.relatorio;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import com.hdntec.gestao.domain.relatorio.CampoRelatorio;
import com.hdntec.gestao.domain.relatorio.PadraoCampo;
import com.hdntec.gestao.domain.relatorio.PadraoRelatorio;
import com.hdntec.gestao.domain.relatorio.enums.EnumCamposRelatorio;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.util.PropertiesUtil;

public class InterfacePadraoRelatorio extends JDialog implements ActionListener
{

   private static final long serialVersionUID = 1L;

   private JPanel painelCampoNome;

   private JLabel rotuloCampoNome;

   private JTextField campoNome;

   private JCheckBox opcaoMostrarGrafico;

   private JPanel painelCampos;

   private JLabel rotuloCampos;

   private JList listaCampos;

   private JScrollPane rolagemCampos;

   private JLabel rotuloCamposSelecionados;

   private JList listaCamposSelecionados;

   private JScrollPane rolagemCamposSelecionados;

   private JPanel painelSetas;

   private JButton botaoSelecionar;

   private JButton botaoRemover;

   private JButton botaoSelecionarTodos;

   private JButton botaoRemoverTodos;

   private JPanel painelOrdem;

   private JButton botaoCima;

   private JButton botaoBaixo;

   private JPanel painelBotoes;

   private JButton botaoGravar;

   private JButton botaoCancelar;

   private InterfaceConfigurarPadraoRelatorio interfaceConfigurarPadraoRelatorio;

   private PadraoRelatorio padraoRelatorio;

   private List<Object> listaDeCampos;

   private List<Object> listaDeCamposSelecionados;

   private CampoRelatorio campoImagem;

   private ControladorInterfaceGestaoRelatorio controlador;

   public InterfacePadraoRelatorio(InterfaceConfigurarPadraoRelatorio parent, ControladorInterfaceGestaoRelatorio controlador, PadraoRelatorio padrao)
   {
	super(parent, true);
	this.interfaceConfigurarPadraoRelatorio = parent;
	this.controlador = controlador;
	this.padraoRelatorio = padrao;
	buscarPadraoRelatorio();
	initComponents();
	preencherCampos();
	preencheListaDeCampos();
   }

   /**
    *
    * buscarPadraoRelatorio
    * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
    * @since 29/06/2009
    * @see
    * @return Returns the void.
    */
   private void buscarPadraoRelatorio()
   {
	try
	{
	   CampoRelatorio filtro = new CampoRelatorio();
	   filtro.setNomeCampo(EnumCamposRelatorio.IMAGEM_SITUACAO.toString());
	   List<CampoRelatorio> lista = controlador.getControladorGestaoRelatorio().buscarCamposPorFiltro(filtro);
	   if (!lista.isEmpty())
	   {
		campoImagem = lista.get(0);
	   }

	   if (padraoRelatorio != null)
	   {
		padraoRelatorio = controlador.getControladorGestaoRelatorio().
			buscarPadraoRelatorioPorId(padraoRelatorio.getIdPadraoRelatorio());
	   }
	}
	catch (ErroSistemicoException e)
	{
		e.printStackTrace();
	   /*InterfaceMensagem interfaceMensagem = new InterfaceMensagem();
	   interfaceMensagem.setTipoMensagem(interfaceMensagem.MENSAGEM_TIPO_ERRO);
	   interfaceMensagem.setDescricaoMensagem(e.getMessage());
	   interfaceConfigurarPadraoRelatorio.getInterfaceInicial().ativarMensagem(interfaceMensagem);*/
	}
   }

   private void initComponents()
   {
	setTitle(PropertiesUtil.getMessage("relatorio.padrao.title"));
	setSize(420, 280);

	Container container = getContentPane();
	container.setLayout(new BorderLayout());

	// nome
	painelCampoNome = new JPanel(new GridBagLayout());
	rotuloCampoNome = new JLabel(PropertiesUtil.getMessage("relatorio.padrao.nome.title"));
	painelCampoNome.add(rotuloCampoNome, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
	campoNome = new JTextField(20);
	painelCampoNome.add(campoNome, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
	opcaoMostrarGrafico = new JCheckBox(PropertiesUtil.getMessage("relatorio.padrao.show.patio.title"), true);
	painelCampoNome.add(opcaoMostrarGrafico, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 0, 0));
	container.add(painelCampoNome, BorderLayout.NORTH);

	// campos
	painelCampos = new JPanel(new GridBagLayout());
	// lista de campos
	rotuloCampos = new JLabel(PropertiesUtil.getMessage("relatorio.padrao.campos.title"));
	painelCampos.add(rotuloCampos, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 2, 1, 1), 0, 0));
	listaCampos = new JList();
	rolagemCampos = new JScrollPane(listaCampos);
	rolagemCampos.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	painelCampos.add(rolagemCampos, new GridBagConstraints(0, 1, 1, 2, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
	// setas adicionar/remover
	painelSetas = new JPanel(new GridBagLayout());
	botaoSelecionar = new JButton(new ImageIcon("resources/icones/paraDir.png"));
	botaoSelecionar.addActionListener(this);
	painelSetas.add(botaoSelecionar, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
	botaoSelecionarTodos = new JButton(new ImageIcon("resources/icones/paraDirTodos.png"));
	botaoSelecionarTodos.addActionListener(this);
	painelSetas.add(botaoSelecionarTodos, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
	botaoRemoverTodos = new JButton(new ImageIcon("resources/icones/paraEsqTodos.png"));
	botaoRemoverTodos.addActionListener(this);
	painelSetas.add(botaoRemoverTodos, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
	botaoRemover = new JButton(new ImageIcon("resources/icones/paraEsq.png"));
	botaoRemover.addActionListener(this);
	painelSetas.add(botaoRemover, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
	// lista de campos selecionados
	painelCampos.add(painelSetas, new GridBagConstraints(1, 1, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
	rotuloCamposSelecionados = new JLabel(PropertiesUtil.getMessage("relatorio.padrao.campos.selecionado.title"));
	painelCampos.add(rotuloCamposSelecionados, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
	listaCamposSelecionados = new JList();
	rolagemCamposSelecionados = new JScrollPane(listaCamposSelecionados);
	rolagemCamposSelecionados.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	painelCampos.add(rolagemCamposSelecionados, new GridBagConstraints(2, 1, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
	// setas subir/descer
	painelOrdem = new JPanel(new GridBagLayout());
	botaoCima = new JButton(new ImageIcon("resources/icones/paraCima.png"));
	botaoCima.addActionListener(this);
	painelOrdem.add(botaoCima, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
	botaoBaixo = new JButton(new ImageIcon("resources/icones/paraBaixo.png"));
	botaoBaixo.addActionListener(this);
	painelOrdem.add(botaoBaixo, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
	painelCampos.add(painelOrdem, new GridBagConstraints(3, 1, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
	container.add(painelCampos, BorderLayout.CENTER);

	// botoes
	painelBotoes = new JPanel(new GridBagLayout());
	botaoGravar = new JButton(PropertiesUtil.getMessage("relatorio.padrao.botao.gravar"));
	botaoGravar.addActionListener(this);
	painelBotoes.add(botaoGravar, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
	botaoCancelar = new JButton(PropertiesUtil.getMessage("relatorio.padrao.botao.cancelar"));
	botaoCancelar.addActionListener(this);
	painelBotoes.add(botaoCancelar, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
	container.add(painelBotoes, BorderLayout.SOUTH);
   }

   private void preencheListaDeCampos()
   {
	this.listaDeCampos = new ArrayList<Object>();

	List<CampoRelatorio> lista = null;
	try
	{
	   lista = controlador.getControladorGestaoRelatorio().buscarCampos();
	   if (lista != null && !lista.isEmpty())
	   {
		filtrarCampoGrafico(lista);
		this.listaDeCampos.addAll(lista);
	   }
	   else
	   {
		lista = new ArrayList<CampoRelatorio>();
	   }
	}
	catch (ErroSistemicoException e)
	{
		e.printStackTrace();
	   /*InterfaceMensagem interfaceMensagem = new InterfaceMensagem();
	   interfaceMensagem.setTipoMensagem(interfaceMensagem.MENSAGEM_TIPO_ERRO);
	   interfaceMensagem.setDescricaoMensagem(e.getMessage());
	   interfaceConfigurarPadraoRelatorio.getInterfaceInicial().ativarMensagem(interfaceMensagem);*/
	}
	preencheListaDeCamposSelecionados();
	removeListaDeCampos();
	updateJList();
   }

   private void preencheListaDeCamposSelecionados()
   {
	this.listaDeCamposSelecionados = new ArrayList<Object>();
	if (padraoRelatorio != null)
	{
	   for (PadraoCampo padraoCampo : padraoRelatorio.getListaDeCampos())
	   {
		if (!EnumCamposRelatorio.IMAGEM_SITUACAO.toString().equals(
			padraoCampo.getCampoRelatorio().getNomeCampo()))
		{
		   this.listaDeCamposSelecionados.add(padraoCampo.getCampoRelatorio());
		}
	   }
	}
   }

   private void removeListaDeCampos()
   {
	for (int i = 0; i < this.listaDeCamposSelecionados.size(); i++)
	{
	   CampoRelatorio campoRelatorio = (CampoRelatorio) this.listaDeCamposSelecionados.get(i);
	   this.listaDeCampos.remove(campoRelatorio);
	}
   }

   private void preencherCampos()
   {
	if (padraoRelatorio != null)
	{
	   campoNome.setText(padraoRelatorio.getNomePadraoRelatorio());
	   boolean contem = padraoRelatorio.contemCampo(EnumCamposRelatorio.IMAGEM_SITUACAO.name());
	   opcaoMostrarGrafico.setSelected(contem);
	}
   }

   @Override
   public void actionPerformed(ActionEvent ev)
   {
	if (ev.getSource() == botaoSelecionar)
	{
	   moveObjectsBetweenList(listaCamposSelecionados, listaCampos, listaDeCamposSelecionados, listaDeCampos);

	}
	else if (ev.getSource() == botaoRemover)
	{
	   moveObjectsBetweenList(listaCampos, listaCamposSelecionados, listaDeCampos, listaDeCamposSelecionados);

	}
	else if (ev.getSource() == botaoSelecionarTodos)
	{
	   selecionarTodos(listaCampos);
	   moveObjectsBetweenList(listaCamposSelecionados, listaCampos, listaDeCamposSelecionados, listaDeCampos);

	}
	else if (ev.getSource() == botaoRemoverTodos)
	{
	   selecionarTodos(listaCamposSelecionados);
	   moveObjectsBetweenList(listaCampos, listaCamposSelecionados, listaDeCampos, listaDeCamposSelecionados);

	}
	else if (ev.getSource() == botaoCima)
	{
	   alterarOrdemCampo(listaCamposSelecionados, true);

	}
	else if (ev.getSource() == botaoBaixo)
	{
	   alterarOrdemCampo(listaCamposSelecionados, false);

	}
	else if (ev.getSource() == botaoGravar)
	{
	   PadraoRelatorio padraoRelatorio = new PadraoRelatorio();
	   try
	   {
		if (valida())
		{
		   if (this.padraoRelatorio == null)
		   {
			padraoRelatorio.setNomePadraoRelatorio(campoNome.getText());
			padraoRelatorio.setListaDeCampos(getListaPadraoCampo(listaCamposSelecionados, padraoRelatorio));
		   }
		   else
		   {
			atualizaListaCampos();
			padraoRelatorio = this.padraoRelatorio;

		   }
		   controlador.getControladorGestaoRelatorio().salvarPadraoRelatorio(padraoRelatorio);
		   interfaceConfigurarPadraoRelatorio.atualizaTabela();
		   dispose();
		}
	   }
	   catch (Exception e)
	   {
		e.printStackTrace();
		JOptionPane.showMessageDialog(this, PropertiesUtil.getMessage("relatorio.padrao.error"), PropertiesUtil.getMessage("relatorio.padrao.title"), JOptionPane.ERROR_MESSAGE);
	   }

	}
	else if (ev.getSource() == botaoCancelar)
	{
	   dispose();
	}
   }

   private void filtrarCampoGrafico(List<CampoRelatorio> lista)
   {
	CampoRelatorio campoGrafico = null;
	for (CampoRelatorio campo : lista)
	{
	   if (campo.getNomeCampo().equals(EnumCamposRelatorio.IMAGEM_SITUACAO.name()))
	   {
		campoGrafico = campo;
	   }
	}
	if (campoGrafico != null)
	{
	   lista.remove(campoGrafico);
	}
   }

   private void selecionarTodos(JList jList)
   {
	if (jList.getModel().getSize() > 0)
	{
	   jList.setSelectionInterval(0, jList.getModel().getSize() - 1);
	}
   }

   private void moveObjectsBetweenList(JList toAdd, JList toRemove, List<Object> listToAdd, List<Object> listToRemove)
   {
	Object[] arraySelected = toRemove.getSelectedValues();
	removeFromAddOn(listToRemove, listToAdd, arraySelected);
	updateJList();
   }

   private void removeFromAddOn(List<Object> listToRemove, List<Object> listToAdd, Object[] array)
   {
	for (int i = 0; i < array.length; i++)
	{
	   Object obj = array[i];
	   listToRemove.remove(obj);
	   listToAdd.add(obj);
	}
   }

   private void updateJList()
   {
	Object[] leftArray = listaDeCampos.toArray();
	Object[] rightArray = listaDeCamposSelecionados.toArray();
	listaCampos.setListData(leftArray);
	listaCamposSelecionados.setListData(rightArray);
   }

   private List<PadraoCampo> getListaPadraoCampo(JList list, PadraoRelatorio padraoRelatorio)
	   throws ErroSistemicoException
   {
	List<PadraoCampo> listaPadraoCampo = new ArrayList<PadraoCampo>();

	List<CampoRelatorio> listaCampoRelatorio = controlador.getControladorGestaoRelatorio().buscarCampos();
	if (listaCampoRelatorio != null && !listaCampoRelatorio.isEmpty())
	{
	   for (int i = 0; i < listaCampoRelatorio.size(); i++)
	   {
		for (int j = 0; j < list.getModel().getSize(); j++)
		{
		   String campo = ((CampoRelatorio) list.getModel().getElementAt(j)).getNomeCampo();
		   String s = listaCampoRelatorio.get(i).getNomeCampo();
		   if (s.equals(campo))
		   {
			PadraoCampo padrao = new PadraoCampo();
			padrao.setPadraoRelatorio(padraoRelatorio);
			padrao.setCampoRelatorio(listaCampoRelatorio.get(i));
			padrao.setOrdem(j);
			listaPadraoCampo.add(padrao);
		   }
		}
	   }
	}
	if (opcaoMostrarGrafico.isSelected())
	{
	   PadraoCampo padrao = new PadraoCampo();
	   padrao.setCampoRelatorio(campoImagem);
	   padrao.setPadraoRelatorio(this.padraoRelatorio == null ? padraoRelatorio : this.padraoRelatorio);
	   padrao.setOrdem(0);
	   listaPadraoCampo.add(padrao);
	}
	return listaPadraoCampo;
   }

   private boolean valida()
   {
	String mensagem = "";
	boolean valido = true;
	Object[] options =
	{
	   "Ok"
	};
	if ("".equals(campoNome.getText()))
	{
	   mensagem = PropertiesUtil.getMessage("relatorio.padrao.nome.vazio");
	   JOptionPane.showOptionDialog(this, mensagem, PropertiesUtil.getMessage("relatorio.gestao.generic.error"), JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
	   valido = false;
	}
	else if (listaCamposSelecionados.getModel().getSize() <= 0 && !opcaoMostrarGrafico.isSelected())
	{
	   mensagem = PropertiesUtil.getMessage("relatorio.padrao.campos.vazio");
	   JOptionPane.showOptionDialog(this, mensagem, PropertiesUtil.getMessage("relatorio.gestao.generic.error"), JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
	   valido = false;
	}

	return valido;
   }

   private void alterarOrdemCampo(JList jList, boolean moverParaCima)
   {
	List<Object> lista = getListFromJList(jList);
	int indiceAtual = jList.getSelectedIndex();
	int novoIndice;
	if (moverParaCima)
	{
	   novoIndice = indiceAtual - 1;
	}
	else
	{
	   novoIndice = indiceAtual + 1;
	}
	if (novoIndice >= 0 && novoIndice <= lista.size() - 1)
	{
	   Object item1 = lista.get(indiceAtual);
	   Object item2 = lista.get(novoIndice);
	   lista.set(novoIndice, item1);
	   lista.set(indiceAtual, item2);

	   //Atualizando a lista de campos tambem.
	   listaDeCamposSelecionados.clear();
	   listaDeCamposSelecionados.addAll(lista);

	   listaCamposSelecionados.setListData(lista.toArray());
	   listaCamposSelecionados.setSelectedIndex(novoIndice);
	   rolagemCamposSelecionados.getVerticalScrollBar().setMaximum(lista.size() - 1);
	}
   }

   private List<Object> getListFromJList(JList list)
   {
	List<Object> objList = new ArrayList<Object>();
	for (int i = 0; i < list.getModel().getSize(); i++)
	{
	   objList.add(list.getModel().getElementAt(i));
	}
	return objList;
   }

   private void atualizaListaCampos()
   {

	padraoRelatorio.setNomePadraoRelatorio(campoNome.getText());
	List<PadraoCampo> listaCamposRemover = new ArrayList<PadraoCampo>();
	for (PadraoCampo padraoCampo : this.padraoRelatorio.getListaDeCampos())
	{

	   if (listaDeCamposSelecionados.contains(padraoCampo.getCampoRelatorio()))
	   {

		int index = listaDeCamposSelecionados.indexOf(padraoCampo.getCampoRelatorio());

		padraoCampo.setOrdem(index);

	   }
	   else if (EnumCamposRelatorio.IMAGEM_SITUACAO.name().equals(
		   padraoCampo.getCampoRelatorio().getNomeCampo()) &&
		   !opcaoMostrarGrafico.isSelected())
	   {
		listaCamposRemover.add(padraoCampo);
	   }
	   else if (!EnumCamposRelatorio.IMAGEM_SITUACAO.name().equals(
		   padraoCampo.getCampoRelatorio().getNomeCampo()))
	   {
		listaCamposRemover.add(padraoCampo);
	   }
	}

	this.padraoRelatorio.getListaDeCampos().removeAll(listaCamposRemover);

	for (int i = 0; i < listaDeCamposSelecionados.size(); i++)
	{
	   CampoRelatorio campo = (CampoRelatorio) listaDeCamposSelecionados.get(i);

	   if (!this.padraoRelatorio.contemCampo(campo.getNomeCampo()))
	   {
		PadraoCampo padrao = new PadraoCampo();
		padrao.setPadraoRelatorio(this.padraoRelatorio);
		padrao.setCampoRelatorio(campo);
		padrao.setOrdem(i);
		this.padraoRelatorio.addPadraoCampo(padrao);
	   }
	}

	if (opcaoMostrarGrafico.isSelected() &&
		!this.padraoRelatorio.contemCampo(EnumCamposRelatorio.IMAGEM_SITUACAO.toString()))
	{
	   PadraoCampo padrao = new PadraoCampo();
	   padrao.setCampoRelatorio(campoImagem);
	   padrao.setPadraoRelatorio(this.padraoRelatorio);
	   padrao.setOrdem(0);
	   this.padraoRelatorio.addPadraoCampo(padrao);
	}
   }
}
