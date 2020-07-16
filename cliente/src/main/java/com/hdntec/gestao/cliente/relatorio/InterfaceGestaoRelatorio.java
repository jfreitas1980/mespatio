package com.hdntec.gestao.cliente.relatorio;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.hdntec.gestao.cliente.interfaceDeDialogo.InterfaceInicial;
import com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex;
import com.hdntec.gestao.domain.relatorio.Relatorio;
import com.hdntec.gestao.domain.relatorio.enums.EnumTipoRelatorio;
import com.hdntec.gestao.domain.relatorio.enums.EspecieRelatorioEnum;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.login.entity.Permission;
import com.hdntec.gestao.util.PropertiesUtil;

public class InterfaceGestaoRelatorio extends JDialog implements ActionListener
{

   private static final long serialVersionUID = 1L;

   private JPanel painelFiltros;

   private JLabel rotuloFiltroRelatorio;

   private JTextField filtroRelatorio;

   private JLabel rotuloFiltroUsuario;

   private JComboBox filtroUsuario;

   private JLabel rotuloFiltroInicio;

   private CalendarioHoraCFlex filtroInicio;

   private JLabel rotuloFiltroFim;

   private CalendarioHoraCFlex filtroFim;

   private JLabel rotuloTipo;

   private JComboBox filtroTipo;

   private JButton botaoFiltrar;

   private JButton botaoLimparFiltros;

   private JTable tabelaRelatorios;

   private StockYardTableModel modeloTabela;

   private JScrollPane barraRolagem;

   private JPanel painelBotoes;

   private JButton botaoGerarRelatorio;
   
   //private JButton botaoIndicadorTatico1;
   
   //private JButton botaoDeslocMaquina;

   private JButton botaoRecuperarRelatorio;

   private JButton botaoExcluir;

   private JButton botaoConfigurarPadrao;

   private JButton botaoSair;

   private ControladorInterfaceGestaoRelatorio controlador;

   private static InterfaceInicial interfaceInicial;
   
   public InterfaceGestaoRelatorio(InterfaceInicial parent, boolean modal) throws ErroSistemicoException
   {
	super(parent, modal);
	interfaceInicial = parent;
	controlador = new ControladorInterfaceGestaoRelatorio(interfaceInicial);
	initComponents();
	controlador.setInterfaceGestaoRelatorio(this);
	controlador.popularUsuariosFiltro(filtroUsuario);
	controlador.popularListaRelatorios();
	filtrarRelatorios();
    habilitaBotoes();
   }

   private void initComponents()
   {
	setTitle(PropertiesUtil.getMessage("relatorio.gestao.title"));
	setSize(1100, 600);
	setResizable(false);

	Container container = getContentPane();
	container.setLayout(new BorderLayout());

	// filtros para busca relatorios
	painelFiltros = new JPanel(new GridBagLayout());
	painelFiltros.setBorder(BorderFactory.createTitledBorder(PropertiesUtil.getMessage("relatorio.gestao.filtro.title")));
	rotuloFiltroRelatorio = new JLabel(PropertiesUtil.getMessage("relatorio.gestao.filtro1.label"));
	painelFiltros.add(rotuloFiltroRelatorio, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 10, 0, 10), 0, 0));
	filtroRelatorio = new JTextField(15);
	filtroRelatorio.setEditable(true);
	painelFiltros.add(filtroRelatorio, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 10, 8, 10), 0, 0));
	rotuloFiltroUsuario = new JLabel(PropertiesUtil.getMessage("relatorio.gestao.filtro2.label"));
	painelFiltros.add(rotuloFiltroUsuario, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 10, 0, 10), 0, 0));
	filtroUsuario = new JComboBox();
	filtroUsuario.setEditable(true);
	painelFiltros.add(filtroUsuario, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 10, 8, 10), 0, 0));
	rotuloFiltroInicio = new JLabel(PropertiesUtil.getMessage("relatorio.gestao.filtro3.label"));
	painelFiltros.add(rotuloFiltroInicio, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 10, 0, 10), 0, 0));
	Calendar dataAtual = Calendar.getInstance();
	dataAtual.add(Calendar.YEAR,-1);
	dataAtual.set(Calendar.HOUR_OF_DAY, 0);
	dataAtual.set(Calendar.MINUTE, 0);
	dataAtual.set(Calendar.SECOND, 0);
	filtroInicio = new CalendarioHoraCFlex();
	filtroInicio.setDataHora(dataAtual.getTime());
	painelFiltros.add(filtroInicio, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 10, 8, 10), 0, 0));
	rotuloFiltroFim = new JLabel(PropertiesUtil.getMessage("relatorio.gestao.filtro4.label"));
	painelFiltros.add(rotuloFiltroFim, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 10, 0, 10), 0, 0));
	Calendar dataAtualFim = Calendar.getInstance();
	dataAtualFim.add(Calendar.YEAR,1);
	dataAtualFim.set(Calendar.HOUR_OF_DAY, 0);
	dataAtualFim.set(Calendar.MINUTE, 0);
	dataAtualFim.set(Calendar.SECOND, 0);
	filtroFim = new CalendarioHoraCFlex();
	filtroFim.setDataHora(dataAtualFim.getTime());
	painelFiltros.add(filtroFim, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 10, 8, 10), 0, 0));
	rotuloTipo = new JLabel(PropertiesUtil.getMessage("relatorio.gestao.filtro5.label"));
	painelFiltros.add(rotuloTipo, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 10, 0, 10), 0, 0));
	filtroTipo = new JComboBox(new String[]
		{
		   "",EnumTipoRelatorio.INSTANTANEO.name(), EnumTipoRelatorio.OFICIAL.name()
		});
	painelFiltros.add(filtroTipo, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 10, 8, 10), 0, 0));
	botaoFiltrar = new JButton(PropertiesUtil.getMessage("relatorio.gestao.botao.filtrar"));
	botaoFiltrar.addActionListener(this);
	painelFiltros.add(botaoFiltrar, new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 10, 8, 10), 0, 0));
	botaoLimparFiltros = new JButton(PropertiesUtil.getMessage("relatorio.gestao.botao.limpar"));
	botaoLimparFiltros.addActionListener(this);
	painelFiltros.add(botaoLimparFiltros, new GridBagConstraints(6, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 10, 8, 10), 0, 0));

	container.add(painelFiltros, BorderLayout.NORTH);

	// tabela de relat�rios
	modeloTabela = criarModeloTabela();
	tabelaRelatorios = new JTable(modeloTabela);
	tabelaRelatorios.setAutoCreateRowSorter(true);
	barraRolagem = new JScrollPane(tabelaRelatorios);
	container.add(barraRolagem, BorderLayout.CENTER);

	// bot�es
	painelBotoes = new JPanel(new GridBagLayout());
	botaoGerarRelatorio = new JButton(PropertiesUtil.getMessage("relatorio.gerar.title"));
	botaoGerarRelatorio.addActionListener(this);
	painelBotoes.add(botaoGerarRelatorio, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
	/*botaoIndicadorTatico1 = new JButton(PropertiesUtil.getMessage("relatorio.gerar.botao.tatico1"));
	botaoIndicadorTatico1.addActionListener(this);
	painelBotoes.add(botaoIndicadorTatico1, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
	botaoDeslocMaquina = new JButton(PropertiesUtil.getMessage("relatorio.gerar.botao.deslocamentoMaquina"));
	botaoDeslocMaquina.addActionListener(this);
	painelBotoes.add(botaoDeslocMaquina, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));*/	
	botaoRecuperarRelatorio = new JButton(PropertiesUtil.getMessage("relatorio.gestao.botao.recuperar"));
	botaoRecuperarRelatorio.addActionListener(this);
	painelBotoes.add(botaoRecuperarRelatorio, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
	botaoExcluir = new JButton(PropertiesUtil.getMessage("relatorio.configurar.padrao.botao.excluir"));
	botaoExcluir.addActionListener(this);
	painelBotoes.add(botaoExcluir, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
	botaoConfigurarPadrao = new JButton(PropertiesUtil.getMessage("relatorio.gestao.botao.configurar"));
	botaoConfigurarPadrao.addActionListener(this);
	painelBotoes.add(botaoConfigurarPadrao, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
	botaoSair = new JButton(PropertiesUtil.getMessage("relatorio.gestao.botao.sair"));
	botaoSair.addActionListener(this);
	painelBotoes.add(botaoSair, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
	container.add(painelBotoes, BorderLayout.SOUTH);
   }
      

   /**
    *
    * showInterfaceGerarRelatorio
    * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
    * @since 26/06/2009
    * @see
    * @return Returns the void.
    */
   private void showInterfaceGerarRelatorio(EspecieRelatorioEnum especie)
   {
	   try
	   {
		   controlador.setInterfaceGestaoRelatorio(this);
		   InterfaceGerarRelatorio gerarRelatorio = new InterfaceGerarRelatorio(this, true, especie);
		   gerarRelatorio.setLocationRelativeTo(null);
		   gerarRelatorio.setVisible(true);
	   }
	   catch (ErroSistemicoException errEx)
	   {
		   JOptionPane.showMessageDialog(this, errEx.getMessage(), PropertiesUtil.getMessage("relatorio.gestao.title"), JOptionPane.ERROR_MESSAGE);
	   }
	   catch (Exception ex)
	   {
		   JOptionPane.showMessageDialog(this, PropertiesUtil.getMessage("relatorio.gestao.error"), PropertiesUtil.getMessage("relatorio.gestao.title"), JOptionPane.ERROR_MESSAGE);
	   }
   }

   /**
    *
    * recuperarRelatorio
    * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
    * @since 27/06/2009
    * @see
    * @return Returns the void.
    */
   private void recuperarRelatorio()
   {
	try
	{
	   if (tabelaRelatorios.getSelectedRows().length > 0)
	   {
		List<String> lista = new ArrayList<String>();
		for (int i : tabelaRelatorios.getSelectedRows())
		{
		   Object obj = tabelaRelatorios.getValueAt(i, 0);
		   lista.add(obj.toString());
		}
		controlador.recuperarRelatorio(lista);
	   }
	   else
	   {
		JOptionPane.showMessageDialog(this, PropertiesUtil.getMessage("relatorio.gestao.error.selecionerelatorio"), PropertiesUtil.getMessage("relatorio.gestao.title"), JOptionPane.INFORMATION_MESSAGE);
	   }

	}
	catch (ErroSistemicoException errEx)
	{
		errEx.printStackTrace();
	   JOptionPane.showMessageDialog(this, errEx.getMessage(), PropertiesUtil.getMessage("relatorio.gestao.title"), JOptionPane.ERROR_MESSAGE);
	}
	catch (Exception ex)
	{
		ex.printStackTrace();
	   JOptionPane.showMessageDialog(this, PropertiesUtil.getMessage("relatorio.gestao.error.recuperar"), PropertiesUtil.getMessage("relatorio.gestao.title"), JOptionPane.ERROR_MESSAGE);
	}
   }

   private void excluirRelatorio()
   {
	if (tabelaRelatorios.getSelectedRows().length > 0)
	{
	   int opcao = JOptionPane.showConfirmDialog(this, PropertiesUtil.getMessage("relatorio.gestao.excluir.question"), PropertiesUtil.getMessage("relatorio.gestao.title"), JOptionPane.YES_NO_OPTION);
	   if (JOptionPane.YES_OPTION == opcao)
	   {
		try
		{
		   controlador.setInterfaceGestaoRelatorio(this);
		   if (validarExclusaoRelatorios())
		   {
			List<String> lista = new ArrayList<String>();
			for (int i : tabelaRelatorios.getSelectedRows())
			{
			   Object obj = tabelaRelatorios.getValueAt(i, 0);
			   lista.add(obj.toString());
			}
			controlador.excluirRelatorio(lista);
			tabelaRelatorios.setModel(controlador.getDefaultTableModel());
			filtrarRelatorios();
		   }

		}
		catch (ErroSistemicoException errEx)
		{
		   JOptionPane.showMessageDialog(this, errEx.getMessage(), PropertiesUtil.getMessage("relatorio.gestao.title"), JOptionPane.ERROR_MESSAGE);
		}
		catch (Exception ex)
		{
		   JOptionPane.showMessageDialog(this, PropertiesUtil.getMessage("relatorio.gestao.excluir.error"), PropertiesUtil.getMessage("relatorio.gestao.title"), JOptionPane.ERROR_MESSAGE);
		}
	   }
	}
	else
	{
	   JOptionPane.showMessageDialog(this, PropertiesUtil.getMessage("relatorio.gestao.error.selecionerelatorio"), PropertiesUtil.getMessage("relatorio.gestao.title"), JOptionPane.INFORMATION_MESSAGE);
	}
   }

   /**
    *
    * criarModeloTabela
    * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
    * @since 22/06/2009
    * @see
    * @return
    * @return Returns the DefaultTableModel.
    */
   private StockYardTableModel criarModeloTabela()
   {
	return controlador.getDefaultTableModel();
   }

   /**
    * Get interfaceInicial
    * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
    * @since 23/06/2009
    */
   public InterfaceInicial getInterfaceInicial()
   {
	return interfaceInicial;
   }

   /**
    * Change interfaceInicial
    * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
    * @param interfaceInicial
    * @since 23/06/2009
    */
   public void setInterfaceInicial(InterfaceInicial interfaceInicial)
   {
	InterfaceGestaoRelatorio.interfaceInicial = interfaceInicial;
   }

   @Override
   public void actionPerformed(ActionEvent e)
   {
	if (e.getSource() == botaoGerarRelatorio)
	{
		showInterfaceGerarRelatorio(EspecieRelatorioEnum.RELATORIO_DSP);
	}
	else if (e.getSource() == botaoRecuperarRelatorio)
	{
	   recuperarRelatorio();
	}
	else if (e.getSource() == botaoExcluir)
	{
	   excluirRelatorio();
	}
	else if (e.getSource() == botaoConfigurarPadrao)
	{
	   InterfaceConfigurarPadraoRelatorio configurarPadrao = new InterfaceConfigurarPadraoRelatorio(this, controlador);
	   configurarPadrao.setLocationRelativeTo(null);
	   configurarPadrao.setVisible(true);
	}
	else if (e.getSource() == botaoSair)
	{
	   dispose();
	}
	else if (e.getSource() == botaoFiltrar)
	{
	   filtrarRelatorios();
	}
	else if (e.getSource() == botaoLimparFiltros)
	{
		limparFiltros();
	   /*modeloTabela = criarModeloTabela();
	   tabelaRelatorios.setModel(modeloTabela);*/
	}
   }
   
   private void limparFiltros() {

	   filtroRelatorio.setText("");
	   Calendar dataAtualInicio = Calendar.getInstance();
	   dataAtualInicio.add(Calendar.YEAR, -1);
	   dataAtualInicio.set(Calendar.HOUR_OF_DAY, 0);
	   dataAtualInicio.set(Calendar.MINUTE, 0);
	   dataAtualInicio.set(Calendar.SECOND, 0);
	   Calendar dataAtualFim = Calendar.getInstance();
	   dataAtualFim.add(Calendar.YEAR, 1);
	   dataAtualFim.set(Calendar.HOUR_OF_DAY, 0);
	   dataAtualFim.set(Calendar.MINUTE, 0);
	   dataAtualFim.set(Calendar.SECOND, 0);
	   filtroInicio.setDataHora(dataAtualInicio.getTime());
	   filtroFim.setDataHora(dataAtualFim.getTime());
	   filtroTipo.setSelectedIndex(0);
	   filtroUsuario.setSelectedIndex(0);
	   controlador.restaurarFiltroRelatorios();
   }

   /**
    * Get controlador
    * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
    * @since 24/06/2009
    */
   public ControladorInterfaceGestaoRelatorio getControlador()
   {
	return controlador;
   }

   /**
    * Change controlador
    * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
    * @param controlador
    * @since 24/06/2009
    */
   public void setControlador(ControladorInterfaceGestaoRelatorio controlador)
   {
	this.controlador = controlador;
   }

   private void filtrarRelatorios()
   {
	//modeloTabela = controlador.getDefaultTableModel(); 
		//new StockYardTableModel(controlador.getColunas(), 0);
	
	   controlador.inicializarFiltro();
	List<Relatorio> listaRelatorios = controlador.getRelatorios();
	String nomeRelatorio = filtroRelatorio.getText();
	long inicio = filtroInicio.getDataHoraDate().getTime();
	long fim = filtroFim.getDataHoraDate().getTime();
	String tipo = (String) filtroTipo.getSelectedItem();
	Object obj = filtroUsuario.getSelectedItem();
	long idUsuario = -1;
	if (obj instanceof UsuarioFiltro) {
		if (((UsuarioFiltro)obj).getUser()!= null) {
			idUsuario = ((UsuarioFiltro)obj).getUser().getId();
		}
	}
	
	List<Relatorio> listaFiltrada = new ArrayList<Relatorio>();
	for (Relatorio relatorio : listaRelatorios)
	{
		
	   if (relatorio.getNomeRelatorio().contains(nomeRelatorio) || "".equals(nomeRelatorio))
	   {
		if (relatorio.getHorarioInicioRelatorio().getTime() >= inicio && relatorio.getHorarioInicioRelatorio().getTime() <= fim &&
			relatorio.getHorarioFimRelatorio().getTime() >= inicio && relatorio.getHorarioFimRelatorio().getTime() <= fim)
		{
		   if (relatorio.getTipoRelatorio().name().equals(tipo.toUpperCase()) || "".equals(tipo))
		   {
			   if (relatorio.getIdUsuario() == idUsuario || idUsuario == -1) {
				   listaFiltrada.add(relatorio);
			   }
		   }
		}
	   }
	}

	/*if (!listaFiltrada.isEmpty())
	{*/
	controlador.filtrarListaRelatorios(listaFiltrada);
		//inserirRelatorioFiltrado(listaFiltrada);
	//}
	
	/*modeloTabela = criarModeloTabela();
	tabelaRelatorios.setModel(modeloTabela);*/	
   }

   /*private void inserirRelatorioFiltrado(List<Relatorio> listaRelatorio)
   {
	for (Relatorio relatorio : listaRelatorio)
	{
	   Object[] rowData = new Object[5];
	   rowData[0] = relatorio.getNomeRelatorio();
	   rowData[1] = String.valueOf(relatorio.getIdUsuario());
	   rowData[2] = DSSStockyardTimeUtil.formatarData(relatorio.getHorarioInicioRelatorio(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
	   rowData[3] = DSSStockyardTimeUtil.formatarData(relatorio.getHorarioFimRelatorio(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
	   rowData[4] = relatorio.getTipoRelatorio().toString();
	   modeloTabela.addRow(rowData);
	}
   }*/

   private boolean validarExclusaoRelatorios()
   {
	int[] indices = tabelaRelatorios.getSelectedRows();
	for (int i = 0; i < indices.length; i++)
	{
	   String usuario = (String) tabelaRelatorios.getModel().getValueAt(indices[i], 1);
	   if (!InterfaceInicial.getUsuarioLogado().getName().equalsIgnoreCase(usuario))
	   {
		Object[] opcoes ={"Ok"};
		String mensagem = PropertiesUtil.getMessage("relatorio.gestao.excluir.permission") + (String) tabelaRelatorios.getModel().getValueAt(i, 0);
		JOptionPane.showOptionDialog(this, mensagem, PropertiesUtil.getMessage("relatorio.gestao.generic.error"), JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, opcoes, opcoes[0]);
		return false;
	   }
	}
	return true;
   }

   /**
    * Get tabelaRelatorios
    * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
    * @since 29/06/2009
    */
   public JTable getTabelaRelatorios()
   {
	return tabelaRelatorios;
   }

   /**
    * Change tabelaRelatorios
    * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
    * @param tabelaRelatorios
    * @since 29/06/2009
    */
   public void setTabelaRelatorios(JTable tabelaRelatorios)
   {
	this.tabelaRelatorios = tabelaRelatorios;
   }

   /**
    * Verifica se o usuario logado tem permissao para criar novos relatorios
    * @author <a href="mailto:bgomes@cflex.com.br"> Bruno <\a>
    * @since 10/07/2009
    * @return
    */
   private boolean verificaPermissaoUsuarioGerarRelatorio()
   {
       boolean permissao = Boolean.FALSE;
       for(Permission permission : InterfaceInicial.getUsuarioLogado().getProfile().getPermissions()){
           if(permission.getName().equalsIgnoreCase(PropertiesUtil.buscarPropriedade("nivel.acesso.nao.pode.gerar.relatorio")))
           {
               permissao = Boolean.FALSE;
           }
           else{
               permissao = Boolean.TRUE;
           }
       }
       return permissao;
   }
   /**
    * habilita os botoes da interface de acordo com as permissoes de usuario
    * @author <a href="mailto:bgomes@cflex.com.br"> Bruno <\a>
    * @since 10/07/2009 
    */
   public void habilitaBotoes()
   {
       //this.botaoGerarRelatorio.setEnabled(verificaPermissaoUsuarioGerarRelatorio());
   }
}
