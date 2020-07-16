package com.hdntec.gestao.cliente.relatorio;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import com.hdntec.gestao.cliente.interfaceDeDialogo.InterfaceInicial;
import com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex;
import com.hdntec.gestao.domain.relatorio.PadraoRelatorio;
import com.hdntec.gestao.domain.relatorio.Relatorio;
import com.hdntec.gestao.domain.relatorio.enums.EnumTipoRelatorio;
import com.hdntec.gestao.domain.relatorio.enums.EspecieRelatorioEnum;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.login.entity.User;
import com.hdntec.gestao.util.PropertiesUtil;

public class InterfaceGerarRelatorio extends JDialog
{

   private static final long serialVersionUID = 1L;

   private JPanel painelTipo;

   private JRadioButton tipoLocalJRB;

   private JRadioButton tipoOficialJRB;

   private JPanel painelFormulario;

   private JLabel rotuloPeriodo;

   private JLabel rotuloA;

   private CalendarioHoraCFlex inicioDTG;

   private CalendarioHoraCFlex fimDTG;

   private JLabel rotuloModelo;

   private JComboBox modeloJCB;

   private JPanel painelBotoes;

   private JButton botaoGerarRelatorio;

   private JButton botaoCancelar;
   
   private JTextArea observacao;
   private JScrollPane scrollPane;

   private ControladorInterfaceGerarRelatorio controlador;

   private InterfaceGestaoRelatorio interfaceGestaoRelatorio;
   
   private EspecieRelatorioEnum especieRelatorio;

   /**
    * 
    * @param parent
    * @param modal
    * @param especieRelatorio
    * @throws ErroSistemicoException
    */
   public InterfaceGerarRelatorio(InterfaceGestaoRelatorio parent, 
		   boolean modal, 
		   EspecieRelatorioEnum especieRelatorio) throws ErroSistemicoException
   {
	super(parent, modal);

	this.especieRelatorio = especieRelatorio;
	controlador = new ControladorInterfaceGerarRelatorio(parent.getControlador());
	interfaceGestaoRelatorio = parent;
	initComponents();
	if (mostrarModeloPadrao()) {
		popularComboModelo();
	}
	addListenersBotoes();
   }
   
   public InterfaceGerarRelatorio(InterfaceInicial interfaceInicial, 
		   boolean modal, EspecieRelatorioEnum especieRelatorio) 
   throws ErroSistemicoException {
	   super(interfaceInicial,modal);
	   this.especieRelatorio = especieRelatorio;
	   ControladorInterfaceGestaoRelatorio controladorGestao = 
		   new ControladorInterfaceGestaoRelatorio(interfaceInicial);
	   this.controlador = new ControladorInterfaceGerarRelatorio(controladorGestao);
	   initComponents();
	   if (mostrarModeloPadrao()) {
			popularComboModelo();
		}
		addListenersBotoes();
   }

   /**
    *
    * initComponents
    * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
    * @since 24/06/2009
    * @see
    * @return Returns the void.
    */
   private void initComponents()
   {
	   if (EspecieRelatorioEnum.ATENDIMENTO_CARGA.equals(this.especieRelatorio)) {
		   setTitle(PropertiesUtil.getMessage("relatorio.cabecalho.title.atendimentoCarga"));
	   }
	   else if (EspecieRelatorioEnum.DESLOCAMENTO_MAQUINA.equals(this.especieRelatorio)) {
		   setTitle(PropertiesUtil.getMessage("relatorio.cabecalho.title.deslocamentoMaquina"));
	   }
	   else if (EspecieRelatorioEnum.INDICADOR_QUALIDADE.equals(this.especieRelatorio)) {
		   setTitle(PropertiesUtil.getMessage("relatorio.cabecalho.subtitle.tatico1"));
	   }
	   else if (EspecieRelatorioEnum.PLANO_RECUPERACAO.equals(this.especieRelatorio)) {
		   setTitle(PropertiesUtil.getMessage("relatorio.planoDeRecuperacao.title"));
	   }
	   else {
		   setTitle(PropertiesUtil.getMessage("relatorio.gerar.title"));
	   }
	setSize(600, 400);
	setResizable(false);

	Container container = getContentPane();
	container.setLayout(new BorderLayout());

	// tipo de relatorio
	if (mostrarPainelTipo()) {
		painelTipo = new JPanel(new GridBagLayout());
		painelTipo.setBorder(BorderFactory.createTitledBorder(PropertiesUtil.getMessage("relatorio.gerar.tipo.label")));
		tipoLocalJRB = new JRadioButton(PropertiesUtil.getMessage("relatorio.gerar.tipo.local.label"));
		tipoLocalJRB.setSelected(true);
		tipoLocalJRB.setName(PropertiesUtil.getMessage("relatorio.gerar.tipo.relatorio.label"));
		tipoOficialJRB = new JRadioButton(PropertiesUtil.getMessage("relatorio.gerar.tipo.oficial.label"));
		tipoOficialJRB.setName(PropertiesUtil.getMessage("relatorio.gerar.tipo.relatorio.label"));
		ButtonGroup btnGroup = new ButtonGroup();
		btnGroup.add(tipoLocalJRB);
		btnGroup.add(tipoOficialJRB);
		painelTipo.add(tipoLocalJRB, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 12, 8, 12), 0, 0));
		painelTipo.add(tipoOficialJRB, new GridBagConstraints(2, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 12, 8, 12), 0, 0));
		container.add(painelTipo, BorderLayout.NORTH);
	}

	// formulario
	painelFormulario = new JPanel(new GridBagLayout());
	if (mostrarPeriodo()) {
		rotuloPeriodo = new JLabel(PropertiesUtil.getMessage("relatorio.gerar.formulario.periodo.label"));
		painelFormulario.add(rotuloPeriodo, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(8, 8, 8, 8), 0, 0));
		inicioDTG = new CalendarioHoraCFlex();
		inicioDTG.setDataHora(new Date());
		painelFormulario.add(inicioDTG, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(8, 8, 8, 8), 0, 0));
		rotuloA = new JLabel(PropertiesUtil.getMessage("relatorio.gerar.formulario.rotulaa.label"));
		painelFormulario.add(rotuloA, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(8, 8, 8, 8), 0, 0));
		fimDTG = new CalendarioHoraCFlex();
		fimDTG.setDataHora(new Date());
		painelFormulario.add(fimDTG, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(8, 8, 8, 8), 0, 0));
	}
	if (mostrarModeloPadrao()) {//Soh deve mostrar o modelo se for DSP ou Plano Recuperacao
		rotuloModelo = new JLabel(PropertiesUtil.getMessage("relatorio.gerar.formulario.modelo.label"));
		painelFormulario.add(rotuloModelo, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(8, 8, 8, 8), 0, 0));
		modeloJCB = new JComboBox();
		painelFormulario.add(modeloJCB, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(8, 8, 8, 8), 0, 0));
	}
	if (mostrarPainelObservacao()) {
		JLabel rotuloObs = new JLabel(PropertiesUtil.getMessage("relatorio.gerar.formulario.obs.label"));
		painelFormulario.add(rotuloObs, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(8, 8, 8, 8), 0, 0));
		observacao = new JTextArea(3,50);
		scrollPane = new JScrollPane(observacao);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		painelFormulario.add(scrollPane, new GridBagConstraints(1, 2, 3, 5, 0.8, 0.8, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.BOTH, new Insets(0, 0, 0, 8), 0, 0));
	}
	container.add(painelFormulario, BorderLayout.CENTER);

	// botoes
	painelBotoes = new JPanel(new GridBagLayout());
	botaoGerarRelatorio = new JButton(PropertiesUtil.getMessage("relatorio.gerar.formulario.botao.gerar"));
	painelBotoes.add(botaoGerarRelatorio, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
	botaoCancelar = new JButton(PropertiesUtil.getMessage("relatorio.gerar.formulario.botao.cancelar"));
	painelBotoes.add(botaoCancelar, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
	container.add(painelBotoes, BorderLayout.SOUTH);
   }

   private void addListenersBotoes()
   {

	botaoGerarRelatorio.addActionListener(new ActionListener()
	{

	   @Override
	   public void actionPerformed(ActionEvent e)
	   {
		botaoGerarRelatorioActionPerformed();
	   }
	});


	botaoCancelar.addActionListener(new ActionListener()
	{

	   @Override
	   public void actionPerformed(ActionEvent event)
	   {
		botaoCancelarActionListener();
	   }
	});
   }

   /**
    *
    * criarComboModelo
    * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
    * @since 24/06/2009
    * @see
    * @return Returns the void.
    */
   private void popularComboModelo()
   {
	   controlador.popularComboBoxModelo(modeloJCB);
   }

   private void botaoGerarRelatorioActionPerformed()
   {

	if (validarCampos())
	{
		Relatorio relatorio = new Relatorio();
		User user = InterfaceInicial.getUsuarioLogado();
		relatorio.setIdUsuario(user.getId());
		relatorio.setUsuario(user);
		if (mostrarPainelTipo()) {
			EnumTipoRelatorio tipoRelatorio = tipoOficialJRB.isSelected() ? 
					EnumTipoRelatorio.OFICIAL : EnumTipoRelatorio.INSTANTANEO;
			relatorio.setTipoRelatorio(tipoRelatorio);
		}
		if (mostrarPeriodo()) {
			Date dataInicio = new Date(inicioDTG.getDataHoraDate().getTime());
			Date dataFim = new Date(fimDTG.getDataHoraDate().getTime());
			relatorio.setHorarioInicioRelatorio(dataInicio);
			relatorio.setHorarioFimRelatorio(dataFim);
		}
		if (mostrarModeloPadrao()) {
			PadraoRelatorio modelo = (PadraoRelatorio) modeloJCB.getSelectedItem();
			relatorio.setPadraoRelatorio(modelo);
		}
		if (mostrarPainelObservacao()) {
			relatorio.setObservacao(observacao.getText());
		}

	   try
	   {
		   this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		   controlador.gerarRelatorio(relatorio,this.especieRelatorio);
		   this.dispose();
	   }
	   catch (HeadlessException e)
	   {
		e.printStackTrace();
		JOptionPane.showMessageDialog(this, PropertiesUtil.getMessage("relatorio.gerar.error"), PropertiesUtil.getMessage("relatorio.gerar.title"), JOptionPane.INFORMATION_MESSAGE);
	   }
	   catch (IOException e)
	   {
		e.printStackTrace();
		JOptionPane.showMessageDialog(this, PropertiesUtil.getMessage("relatorio.gerar.error"), PropertiesUtil.getMessage("relatorio.gerar.title"), JOptionPane.INFORMATION_MESSAGE);
	   }
	   catch (ErroSistemicoException e)
	   {
		e.printStackTrace();
		JOptionPane.showMessageDialog(this, e.getMessage(), PropertiesUtil.getMessage("relatorio.gerar.title"), JOptionPane.INFORMATION_MESSAGE);
	   }
	   finally
	   {
		this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	   }
	}

   }

   private boolean validarCampos()
   {
	boolean result = true;
	if (mostrarPeriodo()) {
		if (inicioDTG.getDataHoraDate().getTime() <= 0)
		{
			JOptionPane.showMessageDialog(this, PropertiesUtil.getMessage("relatorio.gerar.datainicial.msg"));
			result = false;
		}
		else if (fimDTG.getDataHoraDate().getTime() <= 0)
		{
			JOptionPane.showMessageDialog(this, PropertiesUtil.getMessage("relatorio.gerar.datafinal.msg"));
			result = false;
		}
		else if (inicioDTG.getDataHoraDate().getTime() > fimDTG.getDataHoraDate().getTime())
		{
			JOptionPane.showMessageDialog(this, PropertiesUtil.getMessage("relatorio.gerar.datainicial.msg"));
			result = false;
		}
	}
	if (mostrarModeloPadrao() && 
			!(modeloJCB.getSelectedItem() instanceof PadraoRelatorio))
	{
	   result = false;
	}
	return result;
   }

   /**
    *
    * botaoCancelarActionListener
    * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
    * @since 24/06/2009
    * @see
    * @return Returns the void.
    */
   private void botaoCancelarActionListener()
   {
	this.dispose();
   }

   /**
    *
    * getInterfaceGestaoRelatorio
    * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
    * @since 24/06/2009
    * @see
    * @return
    * @return Returns the InterfaceGestaoRelatorio.
    */
   public InterfaceGestaoRelatorio getInterfaceGestaoRelatorio()
   {
	return interfaceGestaoRelatorio;
   }

   /**
    *
    * setInterfaceGestaoRelatorio
    * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
    * @since 24/06/2009
    * @see
    * @param interfaceGestaoRelatorio
    * @return Returns the void.
    */
   public void setInterfaceGestaoRelatorio(InterfaceGestaoRelatorio interfaceGestaoRelatorio)
   {
	this.interfaceGestaoRelatorio = interfaceGestaoRelatorio;
   }
   
   /**
    * Metodo que indica se deve mostar ou nao o modelo padrao de relatorio na tela
    * 
    * mostrarModeloPadrao
    * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
    * @since 14/08/2009
    * @see
    * @return
    * @return Returns the boolean.
    */
   private boolean mostrarModeloPadrao() {
	   return (EspecieRelatorioEnum.RELATORIO_DSP.equals(especieRelatorio)||
				EspecieRelatorioEnum.PLANO_RECUPERACAO.equals(especieRelatorio));
   }
   
   /**
    * Metodo que indica se deve mostrar o painel de tipo do relatorio
    * 
    * mostrarPainelTipo
    * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
    * @since 14/08/2009
    * @see
    * @return
    * @return Returns the boolean.
    */
   private boolean mostrarPainelTipo() {
	   return EspecieRelatorioEnum.RELATORIO_DSP.equals(especieRelatorio);
   }
   
   /**
    * 
    * mostrarPainelObservacao
    * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
    * @since 25/08/2009
    * @see
    * @return
    * @return Returns the boolean.
    */
   private boolean mostrarPainelObservacao() {
	   return EspecieRelatorioEnum.PLANO_RECUPERACAO.equals(especieRelatorio);
   }
   
   /**
    * Metodo que indica se deve mostrar o periodo de datas para o relatorio
    * 
    * mostrarPeriodo
    * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
    * @since 14/08/2009
    * @see
    * @return
    * @return Returns the boolean.
    */
   private boolean mostrarPeriodo() {
	   return !EspecieRelatorioEnum.PLANO_RECUPERACAO.equals(especieRelatorio);
   }
}
