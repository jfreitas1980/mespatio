package com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import com.hdntec.gestao.cliente.interfaceDeDialogo.interfaceDiagramaSituacaoDoPatio.comparadores.ComparadorInterfaceBaliza;
import com.hdntec.gestao.cliente.messagens.InterfaceMensagem;
import com.hdntec.gestao.cliente.util.numeros.DSSStockyardFuncoesNumeros;
import com.hdntec.gestao.cliente.util.tabela.CFlexStockyardFuncoesTabela;
import com.hdntec.gestao.cliente.util.tabela.ColunaTabela;
import com.hdntec.gestao.cliente.util.texto.DSSStockyardFuncoesTexto;
import com.hdntec.gestao.cliente.util.texto.FixedLengthDocument;
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.planta.entity.MetaBaliza;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.Pilha;
import com.hdntec.gestao.domain.planta.enums.EnumTipoBaliza;
import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;
import com.hdntec.gestao.domain.produto.entity.ItemDeControle;
import com.hdntec.gestao.domain.produto.entity.ItemDeControleQualidade;
import com.hdntec.gestao.domain.produto.entity.Produto;
import com.hdntec.gestao.domain.produto.entity.Qualidade;
import com.hdntec.gestao.domain.produto.entity.TipoItemDeControle;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.domain.vo.atividades.EdicaoVO;
import com.hdntec.gestao.exceptions.AtividadeException;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.ValidacaoCampoException;
import com.hdntec.gestao.integracao.controladores.ControladorModelo;
import com.hdntec.gestao.integracao.controladores.IControladorModelo;
import com.hdntec.gestao.services.controladores.impl.ControladorExecutarEdicaoBalizas;
import com.hdntec.gestao.util.PropertiesUtil;
import com.hdntec.gestao.util.datahora.DSSStockyardTimeUtil;

public class InterfaceEdicaoDadosBaliza extends javax.swing.JDialog {

	/** A lista de balizas selecionadas para edicao */
	private List<InterfaceBaliza> listaBalizasSelecionadas;

	/** Constantes da tabela de informacoes das balizas selecionadas */
	private final Integer COL_BALIZA_SELECAO = 0;

	private final Integer COL_BALIZA_ID = 1;

	private final Integer COL_BALIZA_NOME = 2;

	private final Integer COL_BALIZA_NUMERO = 3;

	private final Integer COL_BALIZA_CAPACIDADE = 4;

	private final Integer COL_BALIZA_HORA_INICIO = 5;

	private final Integer COL_BALIZA_HORA_FIM = 6;

	private final Integer COL_BALIZA_ESTADO = 7;

	private final Integer COL_BALIZA_STATUS_EDICAO = 8;

	/** Constantes da tabela de itens de controle */
	private final Integer COL_ITEMCONTROLE_ID = 0;

	private final Integer COL_ITEMCONTROLE_DESCRICAO = 1;

	private final Integer COL_ITEMCONTROLE_RELEVANTE = 2;

	private final Integer COL_ITEMCONTROLE_VALOR = 3;

	private final Integer COL_ITEMCONTROLE_UNIDADE = 4;

	/** Constantes valoroes SIM/NAO */
	private final String ITEMCOMBO_SIM = PropertiesUtil
			.getMessage("mensagem.condicional.sim");

	private final String ITEMCOMBO_NAO = PropertiesUtil
			.getMessage("mensagem.condicional.nao");

	/** Constantes referente ao status da edicao da baliza */
	private final String STATUS_BALIZA_EDITADA = PropertiesUtil
			.getMessage("mensagem.baliza.editada");

	private final String STATUS_BALIZA_NAO_EDITADA = PropertiesUtil
			.getMessage("mensagem.baliza.nao.editada");

	/** Lista de colunas da tabela de informacoes das balizas selecionadas */
	private List<ColunaTabela> listaColunaInformacoes;

	/** Lista de colunas da tabela de itens de controle */
	private List<ColunaTabela> listaColunaItensControle;

	/** Vetor com os dados ta tabela de informacoes da baliza */
	private Vector vInformacoesBalizasSelecionadas;

	/** Vetor com os dados ta tabela de itens de controle */
	private Vector vItensControle;

	/** Controla se a edicao foi cancelada ou nÃ£o */
	private Boolean operacaoCanceladaPeloUsuario = Boolean.FALSE;

	/** acesso ao controlador do subsistema DSP */
	private ControladorDSP controladorDSP;

	/** as informaÃ§Ãµes necessarias para exibicao de mensagens */
	private InterfaceMensagem interfaceMensagem;

	/** a lista de itens de controle da qualidade real do produto */
	List<TipoItemDeControle> listaTiposItensControle;

	/** a interface da baliza selecionada para edicao */
	private InterfaceBaliza interfaceBalizaSelecionada;

	// para cada baliza da pilha editada, guarda o novo status que ja foi
	// gerado, evitando gerar multiplos status se mais de uma baliza for editada
	HashMap<Baliza, Baliza> mapaStatusBaliza;

	public InterfaceEdicaoDadosBaliza(java.awt.Frame parent, boolean modal,
			List<InterfaceBaliza> listaBalizas, ControladorDSP controladorDSP)
			throws ErroSistemicoException {
		super(parent, modal);
		initComponents();
		this.controladorDSP = controladorDSP;
		vInformacoesBalizasSelecionadas = new Vector();
		vItensControle = new Vector();
		this.listaBalizasSelecionadas = listaBalizas;
		// Darley Retirando chamada remota
		// IControladorModelo controladorModelo =
		// InterfaceInicial.lookUpModelo();
		IControladorModelo controladorModelo = new ControladorModelo();
		listaTiposItensControle = controladorModelo.buscarTiposItemControle();
		criaColunasInformacoesBalizasSelecionadas();
		criaColunasItensDeControle();
		carregaComboTiposDeProduto();
		carregaTabelaItensDeControle();
		atualizaTabelaInformacoesBalizasSelecionadas();
		manupilaObjetosEdicaoBaliza(Boolean.FALSE);
		mapaStatusBaliza = new HashMap<Baliza, Baliza>();
		controladorModelo = null;
	}

	/**
	 * Metodo que habita ou desabilita os objetos para edicao da baliza
	 * 
	 * @param habilitarObjetos
	 */
	private void manupilaObjetosEdicaoBaliza(Boolean habilitarObjetos) {
		txtNomeBaliza.setEnabled(Boolean.FALSE);
		txtLarguraBaliza.setEnabled(Boolean.FALSE);
		txtCapacidadeMaximaBaliza.setEnabled(Boolean.FALSE);
		dtIniCalendarioHoraCFlex.setEnabled(habilitarObjetos);
		dtFinCalendarioHoraCFlex.setEnabled(habilitarObjetos);
		cmbEstadoBaliza.setEnabled(habilitarObjetos);
		cmbTipoBaliza.setEnabled(Boolean.FALSE);
		cmbTipoProduto.setEnabled(habilitarObjetos);
		txtQuantidadeEstocada.setEnabled(habilitarObjetos);
	}

	/**
	 * Cria a lista de colunas para exibicao das informacoes das balizas
	 * selecionadas
	 */
	private void criaColunasInformacoesBalizasSelecionadas() {

		listaColunaInformacoes = new ArrayList<ColunaTabela>();
		ColunaTabela colInfo;

		colInfo = new ColunaTabela();
		colInfo.setAlinhamento(SwingConstants.CENTER);
		colInfo.setEditar(Boolean.TRUE);
		colInfo.setLargura(40);
		colInfo.setRedimensionar(Boolean.FALSE);
		colInfo.setTitulo(PropertiesUtil
				.getMessage("mensagem.coluna.tabela.sel"));
		colInfo.setTipoEditor(ColunaTabela.COL_TIPO_RADIOBUTTON);
		listaColunaInformacoes.add(colInfo);

		colInfo = new ColunaTabela();
		colInfo.setAlinhamento(SwingConstants.LEADING);
		colInfo.setEditar(Boolean.FALSE);
		colInfo.setLargura(40);
		colInfo.setRedimensionar(Boolean.FALSE);
		colInfo.setTitulo(PropertiesUtil
				.getMessage("mensagem.coluna.tabela.id"));
		listaColunaInformacoes.add(colInfo);

		colInfo = new ColunaTabela();
		colInfo.setAlinhamento(SwingConstants.LEADING);
		colInfo.setEditar(Boolean.FALSE);
		colInfo.setLargura(180);
		colInfo.setRedimensionar(Boolean.FALSE);
		colInfo.setTitulo(PropertiesUtil
				.getMessage("mensagem.coluna.tabela.descricao"));
		listaColunaInformacoes.add(colInfo);

		colInfo = new ColunaTabela();
		colInfo.setAlinhamento(SwingConstants.CENTER);
		colInfo.setEditar(Boolean.FALSE);
		colInfo.setLargura(50);
		colInfo.setRedimensionar(Boolean.FALSE);
		colInfo.setTitulo(PropertiesUtil
				.getMessage("mensagem.coluna.tabela.numero"));
		listaColunaInformacoes.add(colInfo);

		colInfo = new ColunaTabela();
		colInfo.setAlinhamento(SwingConstants.RIGHT);
		colInfo.setEditar(Boolean.FALSE);
		colInfo.setLargura(100);
		colInfo.setRedimensionar(Boolean.FALSE);
		colInfo.setTitulo(PropertiesUtil
				.getMessage("mensagem.coluna.tabela.capacidade"));
		listaColunaInformacoes.add(colInfo);

		colInfo = new ColunaTabela();
		colInfo.setAlinhamento(SwingConstants.CENTER);
		colInfo.setEditar(Boolean.FALSE);
		colInfo.setLargura(130);
		colInfo.setRedimensionar(Boolean.FALSE);
		colInfo.setTitulo(PropertiesUtil
				.getMessage("mensagem.coluna.tabela.inicio.formacao"));
		listaColunaInformacoes.add(colInfo);

		colInfo = new ColunaTabela();
		colInfo.setAlinhamento(SwingConstants.CENTER);
		colInfo.setEditar(Boolean.FALSE);
		colInfo.setLargura(130);
		colInfo.setRedimensionar(Boolean.FALSE);
		colInfo.setTitulo(PropertiesUtil
				.getMessage("mensagem.coluna.tabela.fim.formacao"));
		listaColunaInformacoes.add(colInfo);

		colInfo = new ColunaTabela();
		colInfo.setAlinhamento(SwingConstants.LEADING);
		colInfo.setEditar(Boolean.FALSE);
		colInfo.setLargura(100);
		colInfo.setRedimensionar(Boolean.FALSE);
		colInfo.setTitulo(PropertiesUtil
				.getMessage("mensagem.coluna.tabela.estado"));
		listaColunaInformacoes.add(colInfo);

		colInfo = new ColunaTabela();
		colInfo.setAlinhamento(SwingConstants.LEADING);
		colInfo.setEditar(Boolean.FALSE);
		colInfo.setLargura(90);
		colInfo.setRedimensionar(Boolean.FALSE);
		colInfo.setTitulo(PropertiesUtil
				.getMessage("mensagem.coluna.tabela.status"));
		listaColunaInformacoes.add(colInfo);
	}

	/**
	 * Atualiza as informacoes na tabela de lista de balizas selecionadas para
	 * edicao
	 */
	private void atualizaTabelaInformacoesBalizasSelecionadas()
			throws ErroSistemicoException {

		// ordena a lista de balizas no momento da exibicao
		Collections.sort(listaBalizasSelecionadas,
				new ComparadorInterfaceBaliza());

		vInformacoesBalizasSelecionadas.removeAllElements();
		for (InterfaceBaliza interfaceBaliza : listaBalizasSelecionadas) {
			Object[] dados = new Object[9];

			dados[COL_BALIZA_SELECAO] = new JRadioButton();
			dados[COL_BALIZA_ID] = interfaceBaliza.getBalizaVisualizada()
					.getIdBaliza();
			dados[COL_BALIZA_NOME] = interfaceBaliza.getBalizaVisualizada()
					.getNomeBaliza();
			dados[COL_BALIZA_NUMERO] = DSSStockyardFuncoesTexto.zerosStr(3,
					interfaceBaliza.getBalizaVisualizada().getNumero());
			dados[COL_BALIZA_CAPACIDADE] = DSSStockyardFuncoesNumeros
					.getQtdeFormatada(interfaceBaliza.getBalizaVisualizada()
							.getCapacidadeMaxima(), 2);
			if (interfaceBaliza.getBalizaVisualizada()
					.getHorarioInicioFormacao() != null) {
				dados[COL_BALIZA_HORA_INICIO] = DSSStockyardTimeUtil
						.formatarData(interfaceBaliza.getBalizaVisualizada()
								.getHorarioInicioFormacao(), PropertiesUtil
								.buscarPropriedade("formato.campo.datahora"));
			}
			if (interfaceBaliza.getBalizaVisualizada().getHorarioFimFormacao() != null) {
				dados[COL_BALIZA_HORA_FIM] = DSSStockyardTimeUtil.formatarData(
						interfaceBaliza.getBalizaVisualizada()
								.getHorarioFimFormacao(), PropertiesUtil
								.buscarPropriedade("formato.campo.datahora"));
			}
			dados[COL_BALIZA_ESTADO] = interfaceBaliza.getBalizaVisualizada()
					.getEstado();
			dados[COL_BALIZA_STATUS_EDICAO] = STATUS_BALIZA_NAO_EDITADA;

			vInformacoesBalizasSelecionadas
					.add(new Vector(Arrays.asList(dados)));
		}

		CFlexStockyardFuncoesTabela.setInformacoesTabela(
				tblInformacoesBalizasSelecionadas,
				vInformacoesBalizasSelecionadas, listaColunaInformacoes);
	}

	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		pnlEdicaoPrincipal = new javax.swing.JPanel();
		pnlInfBalizaSelecionada = new javax.swing.JPanel();
		scrInfBalizasSelecionadas = new javax.swing.JScrollPane();
		tblInformacoesBalizasSelecionadas = new com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable();
		cmdDesistir = new javax.swing.JButton();
		cmdConfirmarEdicaoBalizas = new javax.swing.JButton();
		pnlDadosBaliza = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		txtNomeBaliza = new javax.swing.JTextField();
		jLabel2 = new javax.swing.JLabel();
		lblNumeroBaliza = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		txtLarguraBaliza = new javax.swing.JTextField();
		jLabel4 = new javax.swing.JLabel();
		jLabel5 = new javax.swing.JLabel();
		txtCapacidadeMaximaBaliza = new javax.swing.JTextField();
		jLabel6 = new javax.swing.JLabel();
		jLabel7 = new javax.swing.JLabel();
		jLabel8 = new javax.swing.JLabel();
		jLabel9 = new javax.swing.JLabel();
		cmbEstadoBaliza = new javax.swing.JComboBox();
		jLabel10 = new javax.swing.JLabel();
		cmbTipoBaliza = new javax.swing.JComboBox();
		dtFinCalendarioHoraCFlex = new com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex();
		dtIniCalendarioHoraCFlex = new com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex();
		pnlDadosProdutoBaliza = new javax.swing.JPanel();
		jLabel11 = new javax.swing.JLabel();
		cmbTipoProduto = new javax.swing.JComboBox();
		jLabel12 = new javax.swing.JLabel();
		txtQuantidadeEstocada = new javax.swing.JTextField();
		jLabel13 = new javax.swing.JLabel();
		jLabel15 = new javax.swing.JLabel();
		lblDescricaoTipoProduto = new javax.swing.JLabel();
		jLabel16 = new javax.swing.JLabel();
		lblCodigoInsumo = new javax.swing.JLabel();
		pnlListaItensControle = new javax.swing.JPanel();
		scrListaItensControle = new javax.swing.JScrollPane();
		tblItensControle = new com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable();
		lblCorTipoProduto = new javax.swing.JLabel();
		cmdConfirmaEdicao = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(PropertiesUtil.getMessage("titulo.mensagem.edicao.baliza"));
		addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosed(java.awt.event.WindowEvent evt) {
				formWindowClosed(evt);
			}
		});

		pnlInfBalizaSelecionada
				.setBorder(javax.swing.BorderFactory.createTitledBorder(PropertiesUtil
						.getMessage("mensagem.titulo.informacao.baliza.selecionada")));
		pnlInfBalizaSelecionada.setLayout(new java.awt.BorderLayout());

		tblInformacoesBalizasSelecionadas
				.setModel(new javax.swing.table.DefaultTableModel(
						new Object[][] {

						}, new String[] {

						}));
		tblInformacoesBalizasSelecionadas
				.addMouseListener(new java.awt.event.MouseAdapter() {
					@Override
					public void mouseClicked(java.awt.event.MouseEvent evt) {
						tblInformacoesBalizasSelecionadasMouseClicked(evt);
					}
				});
		scrInfBalizasSelecionadas
				.setViewportView(tblInformacoesBalizasSelecionadas);

		pnlInfBalizaSelecionada.add(scrInfBalizasSelecionadas,
				java.awt.BorderLayout.CENTER);

		cmdDesistir.setFont(new java.awt.Font("Tahoma", 1, 12));
		// cmdDesistir.setIcon(new
		// javax.swing.ImageIcon(getClass().getResource("/resources/icones/exit.png")));
		// // NOI18N
		cmdDesistir.setText("Desistir");
		cmdDesistir.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cmdDesistirActionPerformed(evt);
			}
		});

		cmdConfirmarEdicaoBalizas.setFont(new java.awt.Font("Tahoma", 1, 12));
		// cmdConfirmarEdicaoBalizas.setIcon(new
		// javax.swing.ImageIcon(getClass().getResource("/resources/icones/confirmar.png")));
		// // NOI18N
		cmdConfirmarEdicaoBalizas.setText(PropertiesUtil
				.getMessage("botao.concluir.edicao"));
		cmdConfirmarEdicaoBalizas
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						cmdConfirmarEdicaoBalizasActionPerformed(evt);
					}
				});

		pnlDadosBaliza.setBorder(javax.swing.BorderFactory
				.createTitledBorder(PropertiesUtil
						.getMessage("titulo.dados.baliza.para.edicao")));

		jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12));
		jLabel1.setText(PropertiesUtil.getMessage("label.nome.baliza"));

		txtNomeBaliza.setDocument(new FixedLengthDocument(60));
		txtNomeBaliza.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				txtNomeBalizaActionPerformed(evt);
			}
		});

		jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12));
		jLabel2.setText(PropertiesUtil.getMessage("label.numero"));

		lblNumeroBaliza.setFont(new java.awt.Font("Tahoma", 0, 12));
		lblNumeroBaliza.setForeground(java.awt.Color.blue);

		jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12));
		jLabel3.setText(PropertiesUtil.getMessage("label.largura"));

		jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12));
		jLabel4.setText(PropertiesUtil.getMessage("label.mts"));

		jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12));
		jLabel5.setText(PropertiesUtil.getMessage("label.capacidade.maxima"));

		jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12));
		jLabel6.setText(PropertiesUtil.getMessage("label.tonelada"));

		jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12));
		jLabel7.setText(PropertiesUtil
				.getMessage("label.data.hora.inicio.formacao"));

		jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12));
		jLabel8.setText(PropertiesUtil
				.getMessage("label.data.hora.final.formacao"));

		jLabel9.setFont(new java.awt.Font("Tahoma", 0, 12));
		jLabel9.setText(PropertiesUtil.getMessage("label.estado"));

		cmbEstadoBaliza.setModel(new javax.swing.DefaultComboBoxModel(
				new EstadoMaquinaEnum[] { EstadoMaquinaEnum.OCIOSA,
						EstadoMaquinaEnum.OPERACAO,
						EstadoMaquinaEnum.MANUTENCAO }));

		jLabel10.setFont(new java.awt.Font("Tahoma", 0, 12));
		jLabel10.setText(PropertiesUtil.getMessage("label.tipo.baliza"));

		cmbTipoBaliza.setModel(new javax.swing.DefaultComboBoxModel(new Vector(
				Arrays.asList(EnumTipoBaliza.values()))));

		javax.swing.GroupLayout pnlDadosBalizaLayout = new javax.swing.GroupLayout(
				pnlDadosBaliza);
		pnlDadosBaliza.setLayout(pnlDadosBalizaLayout);
		pnlDadosBalizaLayout
				.setHorizontalGroup(pnlDadosBalizaLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								pnlDadosBalizaLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												pnlDadosBalizaLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																pnlDadosBalizaLayout
																		.createSequentialGroup()
																		.addComponent(
																				jLabel1)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				txtNomeBaliza,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				219,
																				javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addGap(18,
																				18,
																				18)
																		.addComponent(
																				jLabel2)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				lblNumeroBaliza,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				54,
																				javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																		.addComponent(
																				jLabel3)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				txtLarguraBaliza,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				69,
																				javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				jLabel4)
																		.addGap(18,
																				18,
																				18)
																		.addComponent(
																				jLabel5)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				txtCapacidadeMaximaBaliza,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				103,
																				Short.MAX_VALUE)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																		.addComponent(
																				jLabel6))
														.addGroup(
																pnlDadosBalizaLayout
																		.createSequentialGroup()
																		.addComponent(
																				jLabel7)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																		.addComponent(
																				dtIniCalendarioHoraCFlex,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addGap(18,
																				18,
																				18)
																		.addComponent(
																				jLabel8)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																		.addComponent(
																				dtFinCalendarioHoraCFlex,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				javax.swing.GroupLayout.PREFERRED_SIZE))
														.addGroup(
																pnlDadosBalizaLayout
																		.createSequentialGroup()
																		.addComponent(
																				jLabel9)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				cmbEstadoBaliza,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				148,
																				javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addGap(18,
																				18,
																				18)
																		.addComponent(
																				jLabel10)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																		.addComponent(
																				cmbTipoBaliza,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				148,
																				javax.swing.GroupLayout.PREFERRED_SIZE)))
										.addContainerGap()));
		pnlDadosBalizaLayout
				.setVerticalGroup(pnlDadosBalizaLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								pnlDadosBalizaLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												pnlDadosBalizaLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																pnlDadosBalizaLayout
																		.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.BASELINE)
																		.addComponent(
																				jLabel1)
																		.addComponent(
																				txtNomeBaliza,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addComponent(
																				jLabel2))
														.addGroup(
																pnlDadosBalizaLayout
																		.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.BASELINE)
																		.addComponent(
																				jLabel3)
																		.addComponent(
																				txtLarguraBaliza,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addComponent(
																				jLabel4)
																		.addComponent(
																				jLabel5)
																		.addComponent(
																				jLabel6)
																		.addComponent(
																				txtCapacidadeMaximaBaliza,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				javax.swing.GroupLayout.PREFERRED_SIZE))
														.addGroup(
																pnlDadosBalizaLayout
																		.createSequentialGroup()
																		.addGap(3,
																				3,
																				3)
																		.addComponent(
																				lblNumeroBaliza,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				17,
																				javax.swing.GroupLayout.PREFERRED_SIZE)))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												pnlDadosBalizaLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(jLabel7)
														.addComponent(
																dtIniCalendarioHoraCFlex,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(jLabel8)
														.addComponent(
																dtFinCalendarioHoraCFlex,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												pnlDadosBalizaLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(jLabel9)
														.addComponent(
																cmbEstadoBaliza,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(jLabel10)
														.addComponent(
																cmbTipoBaliza,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));

		pnlDadosProdutoBaliza.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Dados do produto e de sua qualidade"));

		jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12));
		jLabel11.setText(PropertiesUtil.getMessage("label.tipo.produto"));

		cmbTipoProduto.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
				cmbTipoProdutoItemStateChanged(evt);
			}
		});

		jLabel12.setFont(new java.awt.Font("Tahoma", 0, 12));
		jLabel12.setText(PropertiesUtil
				.getMessage("label.quantidade.estocada.baliza"));

		jLabel13.setFont(new java.awt.Font("Tahoma", 0, 12));
		jLabel13.setText(PropertiesUtil.getMessage("label.tonelada"));

		jLabel15.setFont(new java.awt.Font("Tahoma", 0, 12));
		jLabel15.setText(PropertiesUtil.getMessage("label.descricao"));

		lblDescricaoTipoProduto.setFont(new java.awt.Font("Tahoma", 0, 12));
		lblDescricaoTipoProduto.setForeground(java.awt.Color.blue);

		jLabel16.setFont(new java.awt.Font("Tahoma", 0, 12));
		jLabel16.setText(PropertiesUtil.getMessage("label.codigo.insumo"));

		lblCodigoInsumo.setFont(new java.awt.Font("Tahoma", 0, 12));
		lblCodigoInsumo.setForeground(java.awt.Color.blue);

		pnlListaItensControle.setBorder(javax.swing.BorderFactory
				.createTitledBorder(PropertiesUtil
						.getMessage("titulo.lista.itens.controle")));
		pnlListaItensControle.setLayout(new java.awt.BorderLayout());

		tblItensControle.setModel(new javax.swing.table.DefaultTableModel(
				new Object[][] {

				}, new String[] {

				}));
		scrListaItensControle.setViewportView(tblItensControle);

		pnlListaItensControle.add(scrListaItensControle,
				java.awt.BorderLayout.CENTER);

		lblCorTipoProduto.setFont(new java.awt.Font("Tahoma", 0, 12));
		lblCorTipoProduto.setOpaque(true);

		javax.swing.GroupLayout pnlDadosProdutoBalizaLayout = new javax.swing.GroupLayout(
				pnlDadosProdutoBaliza);
		pnlDadosProdutoBaliza.setLayout(pnlDadosProdutoBalizaLayout);
		pnlDadosProdutoBalizaLayout
				.setHorizontalGroup(pnlDadosProdutoBalizaLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								pnlDadosProdutoBalizaLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												pnlDadosProdutoBalizaLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																pnlDadosProdutoBalizaLayout
																		.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.LEADING,
																				false)
																		.addGroup(
																				pnlDadosProdutoBalizaLayout
																						.createSequentialGroup()
																						.addComponent(
																								jLabel11)
																						.addPreferredGap(
																								javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																						.addComponent(
																								cmbTipoProduto,
																								javax.swing.GroupLayout.PREFERRED_SIZE,
																								160,
																								javax.swing.GroupLayout.PREFERRED_SIZE)
																						.addPreferredGap(
																								javax.swing.LayoutStyle.ComponentPlacement.RELATED,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE)
																						.addComponent(
																								lblCorTipoProduto,
																								javax.swing.GroupLayout.PREFERRED_SIZE,
																								51,
																								javax.swing.GroupLayout.PREFERRED_SIZE))
																		.addGroup(
																				pnlDadosProdutoBalizaLayout
																						.createSequentialGroup()
																						.addComponent(
																								jLabel12)
																						.addPreferredGap(
																								javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																						.addComponent(
																								txtQuantidadeEstocada,
																								javax.swing.GroupLayout.PREFERRED_SIZE,
																								108,
																								javax.swing.GroupLayout.PREFERRED_SIZE)
																						.addPreferredGap(
																								javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																						.addComponent(
																								jLabel13,
																								javax.swing.GroupLayout.PREFERRED_SIZE,
																								25,
																								javax.swing.GroupLayout.PREFERRED_SIZE)))
														.addGroup(
																pnlDadosProdutoBalizaLayout
																		.createSequentialGroup()
																		.addComponent(
																				jLabel16)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				lblCodigoInsumo,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				51,
																				javax.swing.GroupLayout.PREFERRED_SIZE))
														.addGroup(
																pnlDadosProdutoBalizaLayout
																		.createSequentialGroup()
																		.addComponent(
																				jLabel15)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																		.addComponent(
																				lblDescricaoTipoProduto,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				251,
																				javax.swing.GroupLayout.PREFERRED_SIZE)))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												pnlListaItensControle,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												527, Short.MAX_VALUE)
										.addContainerGap()));
		pnlDadosProdutoBalizaLayout
				.setVerticalGroup(pnlDadosProdutoBalizaLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								pnlDadosProdutoBalizaLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												pnlDadosProdutoBalizaLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																pnlListaItensControle,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																186,
																Short.MAX_VALUE)
														.addGroup(
																pnlDadosProdutoBalizaLayout
																		.createSequentialGroup()
																		.addGroup(
																				pnlDadosProdutoBalizaLayout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.TRAILING)
																						.addComponent(
																								lblCorTipoProduto,
																								javax.swing.GroupLayout.PREFERRED_SIZE,
																								27,
																								javax.swing.GroupLayout.PREFERRED_SIZE)
																						.addGroup(
																								pnlDadosProdutoBalizaLayout
																										.createParallelGroup(
																												javax.swing.GroupLayout.Alignment.BASELINE)
																										.addComponent(
																												jLabel11)
																										.addComponent(
																												cmbTipoProduto,
																												javax.swing.GroupLayout.PREFERRED_SIZE,
																												javax.swing.GroupLayout.DEFAULT_SIZE,
																												javax.swing.GroupLayout.PREFERRED_SIZE)))
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																		.addGroup(
																				pnlDadosProdutoBalizaLayout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.LEADING)
																						.addComponent(
																								jLabel15)
																						.addComponent(
																								lblDescricaoTipoProduto,
																								javax.swing.GroupLayout.PREFERRED_SIZE,
																								17,
																								javax.swing.GroupLayout.PREFERRED_SIZE))
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addGroup(
																				pnlDadosProdutoBalizaLayout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.TRAILING)
																						.addComponent(
																								lblCodigoInsumo,
																								javax.swing.GroupLayout.PREFERRED_SIZE,
																								17,
																								javax.swing.GroupLayout.PREFERRED_SIZE)
																						.addComponent(
																								jLabel16))
																		.addGap(13,
																				13,
																				13)
																		.addGroup(
																				pnlDadosProdutoBalizaLayout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.BASELINE)
																						.addComponent(
																								jLabel12)
																						.addComponent(
																								txtQuantidadeEstocada,
																								javax.swing.GroupLayout.PREFERRED_SIZE,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								javax.swing.GroupLayout.PREFERRED_SIZE)
																						.addComponent(
																								jLabel13))))
										.addContainerGap()));

		cmdConfirmaEdicao.setFont(new java.awt.Font("Tahoma", 1, 12));
		// cmdConfirmaEdicao.setIcon(new
		// javax.swing.ImageIcon(getClass().getResource("/resources/icones/edit.png")));
		// // NOI18N
		cmdConfirmaEdicao.setText(PropertiesUtil
				.getMessage("botao.confima.edicao"));
		cmdConfirmaEdicao
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						cmdConfirmaEdicaoActionPerformed(evt);
					}
				});

		javax.swing.GroupLayout pnlEdicaoPrincipalLayout = new javax.swing.GroupLayout(
				pnlEdicaoPrincipal);
		pnlEdicaoPrincipal.setLayout(pnlEdicaoPrincipalLayout);
		pnlEdicaoPrincipalLayout
				.setHorizontalGroup(pnlEdicaoPrincipalLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								javax.swing.GroupLayout.Alignment.TRAILING,
								pnlEdicaoPrincipalLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												pnlEdicaoPrincipalLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.TRAILING)
														.addComponent(
																pnlDadosProdutoBaliza,
																javax.swing.GroupLayout.Alignment.LEADING,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(
																pnlDadosBaliza,
																javax.swing.GroupLayout.Alignment.LEADING,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(
																pnlInfBalizaSelecionada,
																javax.swing.GroupLayout.Alignment.LEADING,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																879,
																Short.MAX_VALUE)
														.addGroup(
																pnlEdicaoPrincipalLayout
																		.createSequentialGroup()
																		.addComponent(
																				cmdDesistir)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				cmdConfirmaEdicao)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				cmdConfirmarEdicaoBalizas)))
										.addContainerGap()));
		pnlEdicaoPrincipalLayout
				.setVerticalGroup(pnlEdicaoPrincipalLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								pnlEdicaoPrincipalLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(
												pnlInfBalizaSelecionada,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												163,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												pnlDadosBaliza,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(
												pnlDadosProdutoBaliza,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addGap(18, 18, 18)
										.addGroup(
												pnlEdicaoPrincipalLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																cmdConfirmarEdicaoBalizas)
														.addComponent(
																cmdDesistir)
														.addComponent(
																cmdConfirmaEdicao,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																27,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addContainerGap()));

		getContentPane().add(pnlEdicaoPrincipal, java.awt.BorderLayout.CENTER);

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void cmdDesistirActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdDesistirActionPerformed
		operacaoCanceladaPeloUsuario = Boolean.TRUE;
		controladorDSP.deselecionaBalizas();
		this.dispose();
	}// GEN-LAST:event_cmdDesistirActionPerformed

	private void formWindowClosed(java.awt.event.WindowEvent evt) {// GEN-FIRST:event_formWindowClosed
		operacaoCanceladaPeloUsuario = Boolean.TRUE;
	}// GEN-LAST:event_formWindowClosed

	private void tblInformacoesBalizasSelecionadasMouseClicked(
			java.awt.event.MouseEvent evt) {// GEN-FIRST:event_tblInformacoesBalizasSelecionadasMouseClicked
		try {
			exibeDadosBalizaSelecionada();
			manupilaObjetosEdicaoBaliza(Boolean.TRUE);
		} catch (ValidacaoCampoException vCampoEx) {
			controladorDSP.getInterfaceInicial()
					.desativarMensagemProcessamento();
			interfaceMensagem = new InterfaceMensagem();
			interfaceMensagem
					.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
			interfaceMensagem.setDescricaoMensagem(vCampoEx.getMessage());
			controladorDSP.ativarMensagem(interfaceMensagem);
		} catch (ErroSistemicoException erroSisEx) {
			controladorDSP.getInterfaceInicial()
					.desativarMensagemProcessamento();
			interfaceMensagem = new InterfaceMensagem();
			interfaceMensagem
					.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
			interfaceMensagem.setDescricaoMensagem(erroSisEx.getMessage());
			controladorDSP.ativarMensagem(interfaceMensagem);
		}
	}// GEN-LAST:event_tblInformacoesBalizasSelecionadasMouseClicked

	private void cmbTipoProdutoItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_cmbTipoProdutoItemStateChanged
		TipoProduto tipoProdutoSelecionado = (TipoProduto) cmbTipoProduto
				.getSelectedItem();
		if (tipoProdutoSelecionado.getCodigoInsumoTipoProduto() != null) {
			lblCodigoInsumo.setText(tipoProdutoSelecionado
					.getCodigoInsumoTipoProduto().toString());
		} else {
			lblCodigoInsumo.setText("");
		}
		lblDescricaoTipoProduto.setText(tipoProdutoSelecionado
				.getDescricaoTipoProduto());

		String[] rgb = tipoProdutoSelecionado.getCorIdentificacao().split(",");
		lblCorTipoProduto.setBackground(new Color(Integer.parseInt(rgb[0]),
				Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2])));

		if (tipoProdutoSelecionado.getIdTipoProduto().longValue() == 0) {
			dtIniCalendarioHoraCFlex.limpaDataHora();
			dtFinCalendarioHoraCFlex.limpaDataHora();
		}
	}// GEN-LAST:event_cmbTipoProdutoItemStateChanged

	/**
	 * Limpa as informacoes dos campos editados para a selecao de uma nova
	 * baliza
	 */
	private void limpaCamposEditados() {
		txtNomeBaliza.setText("");
		txtLarguraBaliza.setText("");
		txtCapacidadeMaximaBaliza.setText("");
		dtIniCalendarioHoraCFlex.limpaDataHora();
		dtFinCalendarioHoraCFlex.limpaDataHora();
		cmbTipoProduto.setSelectedIndex(0);
		txtQuantidadeEstocada.setText("");
	}

	private void cmdConfirmaEdicaoActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdConfirmaEdicaoActionPerformed
		try {
			validarDadosEditados();
			confirmarEdicaoBalizaSelecionada();
			limpaCamposEditados();
			manupilaObjetosEdicaoBaliza(Boolean.FALSE);
		} catch (ValidacaoCampoException vCampoEx) {
			controladorDSP.getInterfaceInicial()
					.desativarMensagemProcessamento();
			interfaceMensagem = new InterfaceMensagem();
			interfaceMensagem
					.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
			interfaceMensagem.setDescricaoMensagem(vCampoEx.getMessage());
			controladorDSP.ativarMensagem(interfaceMensagem);
		} catch (ErroSistemicoException erroSisEx) {
			controladorDSP.getInterfaceInicial()
					.desativarMensagemProcessamento();
			interfaceMensagem = new InterfaceMensagem();
			interfaceMensagem
					.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ERRO);
			interfaceMensagem.setDescricaoMensagem(erroSisEx.getMessage());
			controladorDSP.ativarMensagem(interfaceMensagem);
		}
	}// GEN-LAST:event_cmdConfirmaEdicaoActionPerformed

	private void cmdConfirmarEdicaoBalizasActionPerformed(
			java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmdConfirmarEdicaoBalizasActionPerformed

		try {

			/** cria o VO para a edicao */
			EdicaoVO edicaoVO = new EdicaoVO();
			edicaoVO.setNomePilha("");
			Date dataAtividade = controladorDSP.getInterfaceInicial()
					.getPlanejamento().getControladorDePlano()
					.obterSituacaoPatioFinal().getDtInicio();
			dataAtividade = Atividade.verificaAtualizaDataAtividade(
					dataAtividade, dataAtividade);

			edicaoVO.setDataInicio(dataAtividade);
			edicaoVO.setDataFim(dataAtividade);

			List<Baliza> listaBalizasEditadas = new ArrayList<Baliza>();
			Set<Baliza> keys = mapaStatusBaliza.keySet();
			for (Baliza balizaEditada : keys)
				listaBalizasEditadas.add(mapaStatusBaliza.get(balizaEditada));

			if (listaBalizasEditadas == null || listaBalizasEditadas.isEmpty()) {
				controladorDSP.getInterfaceInicial().getInterfaceInicial()
						.atualizarDSP();
				controladorDSP.getInterfaceInicial().getInterfaceComandos()
						.finalizarEdicoes();

			} else {
				edicaoVO.setListaBalizas(listaBalizasEditadas);
				Pilha pilhaCorrente = null;
				for (Baliza balizasEditadas : listaBalizasEditadas) {
					  MetaBaliza metaBalizaOperacao = balizasEditadas.getMetaBaliza();
    			      
					  int idxGerado = metaBalizaOperacao.getListaStatus().indexOf(balizasEditadas);
				      
				      Baliza balizaOperacao = metaBalizaOperacao.getListaStatus().get(idxGerado-1);
				      
				      pilhaCorrente = balizaOperacao.retornaStatusHorario(dataAtividade);
				      if (pilhaCorrente != null) {
				    	  break;
				      }
				}
				
				
				if (pilhaCorrente != null) {
					edicaoVO.setNomePilha(pilhaCorrente.getNomePilha());
				}
				//} else {
					String nomePilha = JOptionPane
							.showInputDialog(
									null,
									PropertiesUtil
											.getMessage("mensagem.option.pane.informe.nome.pilha"),
											edicaoVO.getNomePilha());
					if (nomePilha == null || nomePilha.trim().isEmpty()) {
						throw new ValidacaoCampoException(
								PropertiesUtil
										.getMessage("aviso.nome.pilha.vazio"));
					}
					edicaoVO.setNomePilha(nomePilha);

				//}

				ControladorExecutarEdicaoBalizas service = ControladorExecutarEdicaoBalizas
						.getInstance();
				Atividade atividadeEdicaoBaliza = service
						.editarBalizas(edicaoVO);

				controladorDSP.getInterfaceInicial().editarBalizas(
						atividadeEdicaoBaliza);
				controladorDSP.getInterfaceInicial().getInterfaceInicial()
						.atualizarDSP();

				controladorDSP.getInterfaceInicial().getInterfaceComandos()
						.finalizarEdicoes();
			}
		} catch (AtividadeException ex) {
			controladorDSP.getInterfaceInicial()
					.desativarMensagemProcessamento();
			interfaceMensagem = new InterfaceMensagem();
			interfaceMensagem
					.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
			interfaceMensagem.setDescricaoMensagem(ex.getMessage());
			controladorDSP.ativarMensagem(interfaceMensagem);
		} catch (ValidacaoCampoException e) {
			controladorDSP.getInterfaceInicial()
					.desativarMensagemProcessamento();
			interfaceMensagem = new InterfaceMensagem();
			interfaceMensagem
					.setTipoMensagem(InterfaceMensagem.MENSAGEM_TIPO_ALERTA);
			interfaceMensagem.setDescricaoMensagem(e.getMessage());
			controladorDSP.ativarMensagem(interfaceMensagem);
		}
		operacaoCanceladaPeloUsuario = Boolean.FALSE;
		this.dispose();
		// }
	}// GEN-LAST:event_cmdConfirmarEdicaoBalizasActionPerformed

	private void txtNomeBalizaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtNomeBalizaActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_txtNomeBalizaActionPerformed

	/**
	 * Valida as informacoes de edicao da baliza antes da confirmaÃ§Ã£o
	 * 
	 * @throws br.com.cflex.supervision.stockYard.cliente.exception.ValidacaoCampoException
	 */
	private void validarDadosEditados() throws ValidacaoCampoException {
		if (txtNomeBaliza.getText().trim().equals("")) {
			throw new ValidacaoCampoException(
					PropertiesUtil
							.getMessage("aviso.nome.baliza.nao.informado"));
		}

		Double larguraBaliza = DSSStockyardFuncoesNumeros
				.getStringToDouble(txtLarguraBaliza.getText().trim());
		if (larguraBaliza == 0) {
			throw new ValidacaoCampoException(
					PropertiesUtil
							.getMessage("aviso.largura.baliza.deve.ser.maior.que.zero"));
		}

		Double capacidadeMaxima = DSSStockyardFuncoesNumeros
				.getStringToDouble(txtCapacidadeMaximaBaliza.getText().trim());
		if (capacidadeMaxima < 0) {
			throw new ValidacaoCampoException(
					PropertiesUtil
							.getMessage("aviso.capacidade.maxima.baliza.maior.igual.zero"));
		}

		TipoProduto tipoProdutoSel = (TipoProduto) cmbTipoProduto
				.getSelectedItem();
		if (tipoProdutoSel.getIdTipoProduto().longValue() != 0) {
			DSSStockyardTimeUtil.validarDataHora(dtIniCalendarioHoraCFlex
					.getDataHora(), PropertiesUtil
					.getMessage("mensagem.data.hora.inicio.formacao.baliza"));

			DSSStockyardTimeUtil.validarDataHora(dtFinCalendarioHoraCFlex
					.getDataHora(), PropertiesUtil
					.getMessage("mensagem.data.hora.final.formacao.baliza"));

			Double quantidadeEstocada = DSSStockyardFuncoesNumeros
					.getStringToDouble(txtQuantidadeEstocada.getText().trim());
			if (quantidadeEstocada <= 0) {
				throw new ValidacaoCampoException(
						PropertiesUtil
								.getMessage("aviso.campo.quantidade.estocada.deve.maior.que.zero"));
			} else if (quantidadeEstocada > capacidadeMaxima) {
				throw new ValidacaoCampoException(
						PropertiesUtil
								.getMessage("aviso.nao.eh.possivel.estocar.mais.produto.que.capacidade.maxima.baliza"));
			}
		} else {
			Double quantidadeEstocada = DSSStockyardFuncoesNumeros
					.getStringToDouble(txtQuantidadeEstocada.getText().trim());
			if (quantidadeEstocada < 0) {
				throw new ValidacaoCampoException(
						PropertiesUtil
								.getMessage("aviso.campo.quantidade.estocada.deve.menor.que.zero"));
			} else if (quantidadeEstocada > capacidadeMaxima) {
				throw new ValidacaoCampoException(
						PropertiesUtil
								.getMessage("aviso.nao.eh.possivel.estocar.mais.produto.que.capacidade.maxima.baliza"));
			}
		}
	}

	private void confirmarEdicaoBalizaSelecionada()
			throws ValidacaoCampoException, ErroSistemicoException {
		Baliza balizaAlterada = interfaceBalizaSelecionada
				.getBalizaVisualizada();
		Baliza novoStatusBalizaAlterada = null;

		Date horaSituacaoAtual = controladorDSP.getInterfaceInicial()
				.getInterfaceInicial().getSituacaoPatioExibida().getDtInicio();

		// ajusta a hora pois a edicao é pontual
		horaSituacaoAtual = Atividade.verificaAtualizaDataAtividade(
				horaSituacaoAtual, horaSituacaoAtual);

		// cria um novo status das balizas da pilha editada
		Pilha pilhaAtual = balizaAlterada
				.retornaStatusHorario(horaSituacaoAtual);
		if (pilhaAtual != null) {
			for (Baliza balizaPilha : pilhaAtual.getListaDeBalizas()) {
				Baliza novoStatusBaliza = mapaStatusBaliza.get(balizaPilha);
				if (novoStatusBaliza == null) {
					novoStatusBaliza = balizaPilha.getMetaBaliza()
							.clonarStatus(horaSituacaoAtual);
					mapaStatusBaliza.put(balizaPilha, novoStatusBaliza);
				}
				if (balizaPilha.getMetaBaliza().equals(
						balizaAlterada.getMetaBaliza()))
					novoStatusBalizaAlterada = novoStatusBaliza;
			}
		} else {
			novoStatusBalizaAlterada = mapaStatusBaliza.get(balizaAlterada);
			if (novoStatusBalizaAlterada == null) {
				novoStatusBalizaAlterada = balizaAlterada.getMetaBaliza()
						.clonarStatus(horaSituacaoAtual);
				mapaStatusBaliza.put(balizaAlterada, novoStatusBalizaAlterada);
			}
		}

		novoStatusBalizaAlterada.getMetaBaliza().setNomeBaliza(
				txtNomeBaliza.getText().trim());
		novoStatusBalizaAlterada.getMetaBaliza().setLargura(
				DSSStockyardFuncoesNumeros.getStringToDouble(txtLarguraBaliza
						.getText().trim()));
		novoStatusBalizaAlterada.getMetaBaliza().setCapacidadeMaxima(
				DSSStockyardFuncoesNumeros
						.getStringToDouble(txtCapacidadeMaximaBaliza.getText()
								.trim()));
		novoStatusBalizaAlterada
				.setHorarioInicioFormacao(dtIniCalendarioHoraCFlex
						.getDataHoraDate());
		novoStatusBalizaAlterada.setHorarioFimFormacao(dtFinCalendarioHoraCFlex
				.getDataHoraDate());
		novoStatusBalizaAlterada.getMetaBaliza().setTipoBaliza(
				(EnumTipoBaliza) cmbTipoBaliza.getSelectedItem());
		novoStatusBalizaAlterada.setEstado((EstadoMaquinaEnum) cmbEstadoBaliza
				.getSelectedItem());

		TipoProduto tipoProdutoSel = (TipoProduto) cmbTipoProduto
				.getSelectedItem();
		Double quantidadeEstocada = DSSStockyardFuncoesNumeros
				.getStringToDouble(txtQuantidadeEstocada.getText().trim());

		if (balizaAlterada.getProduto() != null) {
			if (tipoProdutoSel.getIdTipoProduto().longValue() == 0) {
				novoStatusBalizaAlterada.setProduto(null);
				novoStatusBalizaAlterada.setHorarioFimFormacao(null);
				novoStatusBalizaAlterada.setHorarioInicioFormacao(null);
				// baliza.getPilha().getListaDeBalizas().remove(baliza);
				// baliza.setPilha(null);
				// Pilha.decompoePilhaEditada(baliza.retornaStatusHorario(horaSituacaoAtual),horaSituacaoAtual);
			} else if (tipoProdutoSel.getIdTipoProduto().equals(
					balizaAlterada.getProduto().getTipoProduto()
							.getIdTipoProduto())) {
				novoStatusBalizaAlterada.getProduto().setQuantidade(
						quantidadeEstocada);
				// clona a qualidade do produto alterado para verificar se ela
				// sera diferente da qualidade existente
				// qualidadeProdutoClonada =
				// cloneHelper.verifyQualidade(baliza.getProduto().getQualidade());
			} else {
				novoStatusBalizaAlterada.getProduto().setTipoProduto(
						tipoProdutoSel);
				novoStatusBalizaAlterada.getProduto().setQuantidade(
						quantidadeEstocada);
				// clona a qualidade do produto alterado para verificar se ela
				// sera diferente da qualidade existente
				// qualidadeProdutoClonada =
				// cloneHelper.verifyQualidade(baliza.getProduto().getQualidade());
			}

			
			
			
			
			// percorrendo a tabela com os itens de controle exibida na tela
			for (int i = 0; i < tblItensControle.getRowCount(); i++) {
				Long idTipoItemControle = new Long(tblItensControle.getValueAt(
						i, COL_ITEMCONTROLE_ID).toString());
				if (novoStatusBalizaAlterada.getProduto() != null) {
					// procura o item de controle na lista de itens de controle
					// da qualidade clonada do produto
					for (ItemDeControle itemControleQualidade : novoStatusBalizaAlterada
							.getProduto().getQualidade()
							.getListaDeItensDeControle()) {
						if (itemControleQualidade.getTipoItemControle()
								.getIdTipoItemDeControle()
								.equals(idTipoItemControle)) {
							// atualiza os atributos relevante e valor do item
							// de controle
							if (tblItensControle
									.getValueAt(i, COL_ITEMCONTROLE_RELEVANTE)
									.toString().equals(ITEMCOMBO_NAO)) {
								itemControleQualidade.getTipoItemControle()
										.setRelevante(Boolean.FALSE);
							} else {
								itemControleQualidade.getTipoItemControle()
										.setRelevante(Boolean.TRUE);
							}
							
							
							//TableCellEditor editor = tblItensControle.getCellEditor(i, COL_ITEMCONTROLE_VALOR);
							
							//((CFlexStockyardTextLostFocus)editor);
							
							String valorItemControle = tblItensControle
									.getValueAt(i, COL_ITEMCONTROLE_VALOR)
									.toString();

							if (!valorItemControle.trim().equals("")) {
								Double valorItem = DSSStockyardFuncoesNumeros
										.getStringToDouble(valorItemControle);
								itemControleQualidade.setValor(valorItem);
							}
						}
					}
				}
			}
		} else {
			if (tipoProdutoSel.getIdTipoProduto().longValue() != 0) {
				Produto produtoNovo = new Produto();

				produtoNovo.setQuantidade(quantidadeEstocada);
				produtoNovo.setTipoProduto(tipoProdutoSel);
				produtoNovo.setDtInicio(horaSituacaoAtual);
				produtoNovo.setDtInsert(horaSituacaoAtual);

				// criando qualidade para produto novo
				Qualidade qualidadeReal = new Qualidade();
				qualidadeReal.setDtInicio(horaSituacaoAtual);
				qualidadeReal.setDtInsert(horaSituacaoAtual);
				qualidadeReal.setEhReal(Boolean.TRUE);
				qualidadeReal
						.setListaDeItensDeControle(criaNovaListaItensDeControle(horaSituacaoAtual));
				produtoNovo.setQualidade(qualidadeReal);
				qualidadeReal.setProduto(produtoNovo);
				novoStatusBalizaAlterada.setProduto(produtoNovo);
			} else {
				novoStatusBalizaAlterada.getProduto().setQuantidade(0D);
				novoStatusBalizaAlterada.setProduto(null);
				novoStatusBalizaAlterada.setHorarioFimFormacao(null);
				novoStatusBalizaAlterada.setHorarioInicioFormacao(null);
				// Pilha.decompoePilhaEditada(novoStatusBaliza.retornaStatusHorario(horaSituacaoAtual),
				// horaSituacaoAtual);

				// baliza.getPilha().getListaDeBalizas().remove(baliza);
				// baliza.setPilha(null);
			}
		}

		// atualizando o status da edicao da baliza
		for (int i = 0; i < tblInformacoesBalizasSelecionadas.getRowCount(); i++) {
			Integer nroBalizaTabela = new Integer(
					tblInformacoesBalizasSelecionadas.getValueAt(i,
							COL_BALIZA_NUMERO).toString());
			if (nroBalizaTabela.equals(balizaAlterada.getNumero())) {
				// Faltou atualizar os dados na tabela de balizas
				// dados[COL_BALIZA_SELECAO] = new JRadioButton();
				// dados[COL_BALIZA_ID] = baliza.getIdBaliza();
				// dados[COL_BALIZA_NOME] = baliza.getNomeBaliza();
				tblInformacoesBalizasSelecionadas.setValueAt(
						balizaAlterada.getNomeBaliza(), i, COL_BALIZA_NOME);

				// dados[COL_BALIZA_NUMERO] =
				// DSSStockyardFuncoesTexto.zerosStr(3, baliza.getNumero());
				// dados[COL_BALIZA_CAPACIDADE] =
				// DSSStockyardFuncoesNumeros.getQtdeFormatada(baliza.getCapacidadeMaxima(),
				// 2);
				tblInformacoesBalizasSelecionadas.setValueAt(
						DSSStockyardFuncoesNumeros.getQtdeFormatada(
								balizaAlterada.getCapacidadeMaxima(), 2), i,
						COL_BALIZA_CAPACIDADE);
				if (balizaAlterada.getHorarioInicioFormacao() != null) {
					// dados[COL_BALIZA_HORA_INICIO] =
					// DSSStockyardTimeUtil.formatarData(baliza.getHorarioInicioFormacao(),
					// PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
					String data = DSSStockyardTimeUtil
							.formatarData(
									balizaAlterada.getHorarioInicioFormacao(),
									PropertiesUtil
											.buscarPropriedade("formato.campo.datahora"));
					tblInformacoesBalizasSelecionadas.setValueAt(data, i,
							COL_BALIZA_HORA_INICIO);
				}
				if (balizaAlterada.getHorarioFimFormacao() != null) {
					// dados[COL_BALIZA_HORA_FIM] =
					// DSSStockyardTimeUtil.formatarData(baliza.getHorarioFimFormacao(),
					// PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
					String data = DSSStockyardTimeUtil
							.formatarData(
									balizaAlterada.getHorarioFimFormacao(),
									PropertiesUtil
											.buscarPropriedade("formato.campo.datahora"));
					tblInformacoesBalizasSelecionadas.setValueAt(data, i,
							COL_BALIZA_HORA_FIM);
				}
				// dados[COL_BALIZA_ESTADO] = baliza.getEstado();
				tblInformacoesBalizasSelecionadas.setValueAt(
						balizaAlterada.getEstado(), i, COL_BALIZA_ESTADO);

				tblInformacoesBalizasSelecionadas.setValueAt(
						STATUS_BALIZA_EDITADA, i, COL_BALIZA_STATUS_EDICAO);
				break;
			}
		}

	}

	/**
	 * Cria uma nova lista de itens de controle a partir dos tipos exibidos na
	 * tela
	 * 
	 * @return
	 * @throws br.com.cflex.supervision.stockYard.cliente.exception.ValidacaoCampoException
	 */
	private List<ItemDeControle> criaNovaListaItensDeControle(Date horaAtual)
			throws ValidacaoCampoException {

		List<ItemDeControle> listaItensControle = new ArrayList<ItemDeControle>();

		// percorre a lista de tipo de itens de controle
		for (TipoItemDeControle tipoItemControle : listaTiposItensControle) {
			// cria um novo objeto item de controle
			/** JESSÃ‰ 18/6 */
			// ItemDeControle itemControle = new ItemDeControle();
			ItemDeControle itemControle = new ItemDeControleQualidade();
			itemControle.setDtInicio(horaAtual);
			itemControle.setDtInsert(horaAtual);
			itemControle.setTipoItemControle(tipoItemControle);

			// percorre a lista de tipos de item de controle exibida
			for (int i = 0; i < tblItensControle.getRowCount(); i++) {
				Long idTipoItemControle = new Long(tblItensControle.getValueAt(
						i, COL_ITEMCONTROLE_ID).toString());
				if (itemControle.getTipoItemControle()
						.getIdTipoItemDeControle().equals(idTipoItemControle)) {
					if (tblItensControle
							.getValueAt(i, COL_ITEMCONTROLE_RELEVANTE)
							.toString().equals(ITEMCOMBO_NAO)) {
						itemControle.getTipoItemControle().setRelevante(
								Boolean.FALSE);
					} else {
						itemControle.getTipoItemControle().setRelevante(
								Boolean.TRUE);
					}
					String valorItemControle = tblItensControle.getValueAt(i,
							COL_ITEMCONTROLE_VALOR).toString();

					if (!valorItemControle.trim().equals("")) {
						Double valorItem = DSSStockyardFuncoesNumeros
								.getStringToDouble(valorItemControle);
						itemControle.setValor(valorItem);
					}
					break;
				}
			}
			listaItensControle.add(itemControle);
		}

		return listaItensControle;
	}

	/**
	 * Metodo que preenche as informações da baliza selecionada para que possa
	 * ser editada
	 */
	private void exibeDadosBalizaSelecionada() throws ValidacaoCampoException,
			ErroSistemicoException {
		CFlexStockyardFuncoesTabela
				.validaSelecaoTabela(tblInformacoesBalizasSelecionadas);

		Integer nroBalizaSelecionada = new Integer(
				tblInformacoesBalizasSelecionadas.getValueAt(
						tblInformacoesBalizasSelecionadas.getSelectedRow(),
						COL_BALIZA_NUMERO).toString());
		interfaceBalizaSelecionada = obterInterfaceBalizaDaLista(nroBalizaSelecionada);

		txtNomeBaliza.setText(interfaceBalizaSelecionada.getBalizaVisualizada()
				.getNomeBaliza());
		lblNumeroBaliza.setText(DSSStockyardFuncoesTexto.getCodigoFormatado(3,
				interfaceBalizaSelecionada.getBalizaVisualizada().getNumero()));
		txtLarguraBaliza.setText(DSSStockyardFuncoesNumeros.getQtdeFormatada(
				interfaceBalizaSelecionada.getBalizaVisualizada().getLargura(),
				3));
		txtCapacidadeMaximaBaliza.setText(DSSStockyardFuncoesNumeros
				.getQtdeFormatada(interfaceBalizaSelecionada
						.getBalizaVisualizada().getCapacidadeMaxima(), 3));
		if (interfaceBalizaSelecionada.getBalizaVisualizada()
				.getHorarioInicioFormacao() != null) {
			dtIniCalendarioHoraCFlex.setDataHora(interfaceBalizaSelecionada
					.getBalizaVisualizada().getHorarioInicioFormacao());
		} else {
			dtIniCalendarioHoraCFlex.limpaDataHora();
		}
		if (interfaceBalizaSelecionada.getBalizaVisualizada()
				.getHorarioFimFormacao() != null) {
			dtFinCalendarioHoraCFlex.setDataHora(interfaceBalizaSelecionada
					.getBalizaVisualizada().getHorarioFimFormacao());
		} else {
			dtFinCalendarioHoraCFlex.limpaDataHora();
		}
		cmbEstadoBaliza.setSelectedItem(interfaceBalizaSelecionada
				.getBalizaVisualizada().getEstado());
		cmbTipoBaliza.setSelectedItem(interfaceBalizaSelecionada
				.getBalizaVisualizada().getTipoBaliza());

		if (interfaceBalizaSelecionada.getBalizaVisualizada().getProduto() != null) {
			cmbTipoProduto.setSelectedItem(interfaceBalizaSelecionada
					.getBalizaVisualizada().getProduto().getTipoProduto());
			lblDescricaoTipoProduto.setText(interfaceBalizaSelecionada
					.getBalizaVisualizada().getProduto().getTipoProduto()
					.getDescricaoTipoProduto());
			if (interfaceBalizaSelecionada.getBalizaVisualizada().getProduto()
					.getTipoProduto().getCodigoInsumoTipoProduto() != null) {
				lblCodigoInsumo.setText(interfaceBalizaSelecionada
						.getBalizaVisualizada().getProduto().getTipoProduto()
						.getCodigoInsumoTipoProduto().toString());
			} else {
				lblCodigoInsumo.setText("");
			}
			txtQuantidadeEstocada.setText(DSSStockyardFuncoesNumeros
					.getQtdeFormatada(interfaceBalizaSelecionada
							.getBalizaVisualizada().getProduto()
							.getQuantidade(), 2));
		}

		if (interfaceBalizaSelecionada.getBalizaVisualizada().getProduto() != null) {
			// atualizando a lista de itens de controle com os valores da baliza
			// selecionada
			for (ItemDeControle itemDeControle : interfaceBalizaSelecionada
					.getBalizaVisualizada().getProduto().getQualidade()
					.getListaDeItensDeControle()) {
				// buscando item de controle na tabela carregada
				for (int i = 0; i < tblItensControle.getRowCount(); i++) {
					Long idTipoItemControle = new Long(tblItensControle
							.getValueAt(i, COL_ITEMCONTROLE_ID).toString());
					if (itemDeControle.getTipoItemControle()
							.getIdTipoItemDeControle()
							.equals(idTipoItemControle)) {
						if (itemDeControle.getValor() != null) {
							tblItensControle.setValueAt(
									DSSStockyardFuncoesNumeros
											.getQtdeFormatada(
													itemDeControle.getValor(),
													3), i,
									COL_ITEMCONTROLE_VALOR);
						} else {
							tblItensControle.setValueAt("", i,
									COL_ITEMCONTROLE_VALOR);
						}
						if (itemDeControle.getTipoItemControle().getRelevante()) {
							tblItensControle.setValueAt(ITEMCOMBO_SIM, i,
									COL_ITEMCONTROLE_RELEVANTE);
						} else {
							tblItensControle.setValueAt(ITEMCOMBO_NAO, i,
									COL_ITEMCONTROLE_RELEVANTE);
						}
						break;
					}
				}
			}
		} else {
			cmbTipoProduto.setSelectedIndex(0);
			carregaTabelaItensDeControle();
		}
	}

	/**
	 * Carrega no campo tipo de produto todos os tipos de produto cadastrado no
	 * sistema
	 * 
	 * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
	 */
	private void carregaComboTiposDeProduto() throws ErroSistemicoException {
		// Darley retirando chamada remota
		// IControladorModelo controladorModelo =
		// InterfaceInicial.lookUpModelo();
		IControladorModelo controladorModelo = new ControladorModelo();
		List<TipoProduto> listaTiposProduto = controladorModelo
				.buscarTiposProduto(new TipoProduto());

		TipoProduto tipoProdutoVazio = new TipoProduto();
		tipoProdutoVazio.setCodigoFamiliaTipoProduto("");
		tipoProdutoVazio.setCodigoInsumoTipoProduto(null);
		tipoProdutoVazio.setCodigoTipoProduto("");
		tipoProdutoVazio.setCorIdentificacao("236,233,216");
		tipoProdutoVazio.setIdTipoProduto(new Long(0));
		cmbTipoProduto.addItem(tipoProdutoVazio);

		for (TipoProduto tipoProduto : listaTiposProduto) {
			cmbTipoProduto.addItem(tipoProduto);
		}
		controladorModelo = null;
	}

	/**
	 * Cria a lista de colunas para exibicao das informacoes das balizas
	 * selecionadas
	 */
	private void criaColunasItensDeControle() {

		listaColunaItensControle = new ArrayList<ColunaTabela>();
		ColunaTabela colInfo;

		colInfo = new ColunaTabela();
		colInfo.setAlinhamento(SwingConstants.LEADING);
		colInfo.setEditar(Boolean.FALSE);
		colInfo.setLargura(40);
		colInfo.setRedimensionar(Boolean.FALSE);
		colInfo.setTitulo(PropertiesUtil
				.getMessage("mensagem.coluna.tabela.id"));
		listaColunaItensControle.add(colInfo);

		colInfo = new ColunaTabela();
		colInfo.setAlinhamento(SwingConstants.LEADING);
		colInfo.setEditar(Boolean.FALSE);
		colInfo.setLargura(150);
		colInfo.setRedimensionar(Boolean.FALSE);
		colInfo.setTitulo(PropertiesUtil
				.getMessage("mensagem.coluna.tabela.descricao"));
		listaColunaItensControle.add(colInfo);

		String[] valoresCombo = new String[2];
		valoresCombo[0] = ITEMCOMBO_SIM;
		valoresCombo[1] = ITEMCOMBO_NAO;

		colInfo = new ColunaTabela();
		colInfo.setAlinhamento(SwingConstants.CENTER);
		colInfo.setEditar(Boolean.TRUE);
		colInfo.setLargura(80);
		colInfo.setRedimensionar(Boolean.FALSE);
		colInfo.setTipoEditor(ColunaTabela.COL_TIPO_COMBOBOX);
		colInfo.setTitulo(PropertiesUtil
				.getMessage("mensagem.coluna.tabela.relevante"));
		colInfo.setVItensCombo(new Vector(Arrays.asList(valoresCombo)));
		listaColunaItensControle.add(colInfo);

		colInfo = new ColunaTabela();
		colInfo.setAlinhamento(SwingConstants.RIGHT);
		colInfo.setEditar(Boolean.TRUE);
		colInfo.setLargura(100);
		colInfo.setRedimensionar(Boolean.FALSE);
		colInfo.setTitulo(PropertiesUtil
				.getMessage("mensagem.coluna.tabela.valor"));
		colInfo.setTipoEditor(ColunaTabela.COL_TIPO_TEXT_LOST_FOCUS);
		listaColunaItensControle.add(colInfo);

		colInfo = new ColunaTabela();
		colInfo.setAlinhamento(SwingConstants.CENTER);
		colInfo.setEditar(Boolean.FALSE);
		colInfo.setLargura(50);
		colInfo.setRedimensionar(Boolean.FALSE);
		colInfo.setTitulo(PropertiesUtil
				.getMessage("mensagem.coluna.tabela.unidade"));
		listaColunaItensControle.add(colInfo);

	}

	/**
	 * carrega a tabela de itens de controle com os items de controle cadastrado
	 */
	private void carregaTabelaItensDeControle() throws ErroSistemicoException {
		vItensControle.removeAllElements();
		for (TipoItemDeControle tipoItemControle : listaTiposItensControle) {
			Object[] dados = new Object[5];

			dados[COL_ITEMCONTROLE_ID] = tipoItemControle
					.getIdTipoItemDeControle();
			dados[COL_ITEMCONTROLE_DESCRICAO] = tipoItemControle
					.getDescricaoTipoItemControle();
			dados[COL_ITEMCONTROLE_RELEVANTE] = ITEMCOMBO_NAO;
			dados[COL_ITEMCONTROLE_VALOR] = "";
			dados[COL_ITEMCONTROLE_UNIDADE] = tipoItemControle.getUnidade();

			vItensControle.add(new Vector(Arrays.asList(dados)));
		}

		tblItensControle.setRowHeight(22);
		CFlexStockyardFuncoesTabela.setInformacoesTabela(tblItensControle,
				vItensControle, listaColunaItensControle);
	}

	public Boolean getOperacaoCanceladaPeloUsuario() {
		return operacaoCanceladaPeloUsuario;
	}

	public void setOperacaoCanceladaPeloUsuario(
			Boolean operacaoCanceladaPeloUsuario) {
		this.operacaoCanceladaPeloUsuario = operacaoCanceladaPeloUsuario;
	}

	/**
	 * Pesquisa a baliza selecionada na lista de balizas selecionadas para
	 * edicao, procurando pelo numero da baliza
	 */
	private InterfaceBaliza obterInterfaceBalizaDaLista(Integer nroBaliza) {
		InterfaceBaliza interfaceBalizaClicada = null;
		for (InterfaceBaliza interfaceBaliza : listaBalizasSelecionadas) {
			if (interfaceBaliza.getBalizaVisualizada().getNumero()
					.equals(nroBaliza)) {
				interfaceBalizaClicada = interfaceBaliza;
				break;
			}
		}
		return interfaceBalizaClicada;
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JComboBox cmbEstadoBaliza;
	private javax.swing.JComboBox cmbTipoBaliza;
	private javax.swing.JComboBox cmbTipoProduto;
	private javax.swing.JButton cmdConfirmaEdicao;
	private javax.swing.JButton cmdConfirmarEdicaoBalizas;
	private javax.swing.JButton cmdDesistir;
	private com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex dtFinCalendarioHoraCFlex;
	private com.hdntec.gestao.cliente.util.datahora.CalendarioHoraCFlex dtIniCalendarioHoraCFlex;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel10;
	private javax.swing.JLabel jLabel11;
	private javax.swing.JLabel jLabel12;
	private javax.swing.JLabel jLabel13;
	private javax.swing.JLabel jLabel15;
	private javax.swing.JLabel jLabel16;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JLabel jLabel8;
	private javax.swing.JLabel jLabel9;
	private javax.swing.JLabel lblCodigoInsumo;
	private javax.swing.JLabel lblCorTipoProduto;
	private javax.swing.JLabel lblDescricaoTipoProduto;
	private javax.swing.JLabel lblNumeroBaliza;
	private javax.swing.JPanel pnlDadosBaliza;
	private javax.swing.JPanel pnlDadosProdutoBaliza;
	private javax.swing.JPanel pnlEdicaoPrincipal;
	private javax.swing.JPanel pnlInfBalizaSelecionada;
	private javax.swing.JPanel pnlListaItensControle;
	private javax.swing.JScrollPane scrInfBalizasSelecionadas;
	private javax.swing.JScrollPane scrListaItensControle;
	private com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable tblInformacoesBalizasSelecionadas;
	private com.hdntec.gestao.cliente.util.tabela.bean.CFlexStockyardJTable tblItensControle;
	private javax.swing.JTextField txtCapacidadeMaximaBaliza;
	private javax.swing.JTextField txtLarguraBaliza;
	private javax.swing.JTextField txtNomeBaliza;
	private javax.swing.JTextField txtQuantidadeEstocada;
	// End of variables declaration//GEN-END:variables

}
