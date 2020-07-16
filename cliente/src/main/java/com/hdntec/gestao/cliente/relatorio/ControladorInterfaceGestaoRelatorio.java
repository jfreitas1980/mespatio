package com.hdntec.gestao.cliente.relatorio;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import br.com.cflex.samarco.supervision.stockyard.relatorio.GeradorPDF;
import br.com.cflex.samarco.supervision.stockyard.relatorio.RelatorioPDF;
import br.com.cflex.samarco.supervision.stockyard.relatorio.controles.CabecalhoRelatorio;
import br.com.cflex.samarco.supervision.stockyard.relatorio.controles.Coluna;
import br.com.cflex.samarco.supervision.stockyard.relatorio.enums.CalculoColunaEnum;
import br.com.cflex.samarco.supervision.stockyard.relatorio.exception.ErroGeracaoRelatorioPDF;

import com.hdntec.gestao.cliente.interfaceDeDialogo.InterfaceInicial;
import com.hdntec.gestao.cliente.relatorio.util.GeradorImagemDSPUtil;
import com.hdntec.gestao.domain.blendagem.Blendagem;
import com.hdntec.gestao.domain.plano.entity.PlanoEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.Pilha;
import com.hdntec.gestao.domain.planta.enums.EnumTipoBaliza;
import com.hdntec.gestao.domain.produto.entity.ItemDeControle;
import com.hdntec.gestao.domain.produto.entity.ItemDeControleQualidade;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.domain.relatorio.PadraoCampo;
import com.hdntec.gestao.domain.relatorio.PadraoRelatorio;
import com.hdntec.gestao.domain.relatorio.Relatorio;
import com.hdntec.gestao.domain.relatorio.SituacaoPatioRelatorioVO;
import com.hdntec.gestao.domain.relatorio.enums.EnumCamposRelatorio;
import com.hdntec.gestao.domain.relatorio.enums.EnumTipoRelatorio;
import com.hdntec.gestao.domain.relatorio.enums.EspecieRelatorioEnum;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.integracao.controladores.ControladorModelo;
import com.hdntec.gestao.integracao.controladores.IControladorModelo;
import com.hdntec.gestao.integracao.relatorio.controladores.ControladorGestaoRelatorio;
import com.hdntec.gestao.integracao.relatorio.controladores.IControladorGestaoRelatorio;
import com.hdntec.gestao.login.entity.User;
import com.hdntec.gestao.util.PropertiesUtil;
import com.hdntec.gestao.util.datahora.DSSStockyardTimeUtil;

public class ControladorInterfaceGestaoRelatorio {

	private IControladorGestaoRelatorio controladorGestaoRelatorio;
	private List<Relatorio> relatorios;
	private List<Relatorio> relatoriosFiltro;
	private StockYardTableModel defaultTableModel;
	private final String SERVIDOR_RELATORIOS = PropertiesUtil.buscarPropriedade("pasta.servidor.relatorios").trim();
	private InterfaceInicial interfaceInicial;
	private InterfaceGestaoRelatorio interfaceGestaoRelatorio;
	private Map<Long, User> listaUsuarios = new HashMap<Long, User>();
	/**Controlador do Relatorio de Atendimento a Qualidade*/
	private ControladorRelAtendimentoQualidade controladorRelAQ;
	/**Controlador do Relatorio de Deslocamento de Maquinas*/
	private ControladorRelDeslocamentoMaquina controladorRelDM;
	/**Controlador do Relatorio de Plano de Recuperacao*/
	private ControladorRelatorioPlanoDeRecuperacao controladorRelPR;
	/**Controlador do Relatorio de Tempo de Atendimento da Carga*/
	private ControladorRelAtendimentoCarga controladorRelAC;
	/**Indica quais campos que realmente não pertence a este relatorio*/
	private List<String> camposNaoUtilizados;

	/**
	 * 
	 * @param interfaceInicial
	 * @throws ErroSistemicoException
	 */
	public ControladorInterfaceGestaoRelatorio(InterfaceInicial interfaceInicial) throws ErroSistemicoException
	{
		this.interfaceInicial = interfaceInicial;
		//Darley removendo a chamada remota
		//controladorGestaoRelatorio = lookUpGestaoRelatorio();		
		controladorGestaoRelatorio = new ControladorGestaoRelatorio();
		controladorRelAQ = new ControladorRelAtendimentoQualidade();
		controladorRelDM = new ControladorRelDeslocamentoMaquina();
		controladorRelPR = new ControladorRelatorioPlanoDeRecuperacao();
		controladorRelAC = new ControladorRelAtendimentoCarga();
		defaultTableModel = new StockYardTableModel(getColunas(), 0);
		populaCamposNaoUtilizados();
	}
	
	/**
	 * 
	 * populaCamposNaoUtilizados
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 24/08/2009
	 * @see
	 * @return Returns the void.
	 */
	private void populaCamposNaoUtilizados() {
		camposNaoUtilizados = new ArrayList<String>();
		camposNaoUtilizados.add(EnumCamposRelatorio.BALIZA.toString());
		camposNaoUtilizados.add(EnumCamposRelatorio.IMAGEM_SITUACAO.toString());
	}
	/**
	 * Indica se aquele campo realmente pertence ao relatorio
	 * ehCampoDoRelatorio
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 24/08/2009
	 * @see
	 * @param nomeCampo
	 * @return
	 * @return Returns the boolean.
	 */
	private boolean ehCampoDoRelatorio(String nomeCampo) {
		return !camposNaoUtilizados.contains(nomeCampo);
	}

	/**
	 * 
	 * popularListaRelatorios
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 07/08/2009
	 * @see
	 * @throws ErroSistemicoException
	 * @return Returns the void.
	 */
	public void popularListaRelatorios() throws ErroSistemicoException
	{
		relatoriosFiltro = null;
		relatorios = controladorGestaoRelatorio.buscarRelatorios();
		novoModeloRelatorio();
	}

	/**
	 * 
	 * inicializarFiltro
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 07/08/2009
	 * @see
	 * @return Returns the void.
	 */
	public void inicializarFiltro() {
		if (relatoriosFiltro != null) {
			relatorios = relatoriosFiltro;
		}
	}

	/**
	 * 
	 * restaurarFiltroRelatorios
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 07/08/2009
	 * @see
	 * @return Returns the void.
	 */
	public void restaurarFiltroRelatorios()  {

		relatorios = relatoriosFiltro;
		relatoriosFiltro = null;
		novoModeloRelatorio();
	}

	/**
	 * filtrarListaRelatorios
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 5/08/2009
	 * @see
	 * @return
	 */
	public void filtrarListaRelatorios(List<Relatorio> filtro) {
		relatoriosFiltro = relatorios;
		relatorios = filtro;
		novoModeloRelatorio();
	}
	
	/**
	 * 
	 * popularUsuariosFiltro
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 07/08/2009
	 * @see
	 * @param obj
	 * @return Returns the void.
	 */
	public void popularUsuariosFiltro(JComboBox obj) {

		listaUsuarios.clear();
		UsuarioFiltro usuarioNull = new UsuarioFiltro(null);
		UsuarioFiltro usuario = new UsuarioFiltro(InterfaceInicial.getUsuarioLogado());
		listaUsuarios.put(usuario.getUser().getId(), usuario.getUser());
		obj.addItem(usuarioNull);
		obj.addItem(usuario);
	}

	/**
	 * 
	 * getColunas
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 07/08/2009
	 * @see
	 * @return
	 * @return Returns the String[].
	 */
	private String[] getColunas()
	{
		return new String[] {
				PropertiesUtil.getMessage("relatorio.gestao.table.coluna1"),
				PropertiesUtil.getMessage("relatorio.gestao.table.coluna2"),
				PropertiesUtil.getMessage("relatorio.gestao.table.coluna3"),
				PropertiesUtil.getMessage("relatorio.gestao.table.coluna4"),
				PropertiesUtil.getMessage("relatorio.gestao.table.coluna5")
		};
	}

	/**
	 * 
	 * novoModeloRelatorio
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 07/08/2009
	 * @see
	 * @return Returns the void.
	 */
	private void novoModeloRelatorio()
	{
		defaultTableModel = new StockYardTableModel(getColunas(), 0);

		if (relatorios != null && relatorios.size() > 0 ) {
			for (Relatorio relatorio : relatorios)
			{
				User user = listaUsuarios.get(relatorio.getIdUsuario());
				String usuario = "";
				if (user != null) {
					usuario = user.getName();
				}
				Object[] rowData = new Object[6];
				rowData[0] = relatorio.getNomeRelatorio();
				rowData[1] = usuario;
				rowData[2] = DSSStockyardTimeUtil.formatarData(relatorio.getHorarioInicioRelatorio(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
				rowData[3] = DSSStockyardTimeUtil.formatarData(relatorio.getHorarioFimRelatorio(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
				rowData[4] = relatorio.getTipoRelatorio().toString();
				rowData[5] = relatorio;
				defaultTableModel.addRow(rowData);
			}
		}	
		interfaceGestaoRelatorio.getTabelaRelatorios().setModel(defaultTableModel);
	}

	/**
	 * 
	 * gerarRelatorio
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 07/08/2009
	 * @see
	 * @param relatorio
	 * @param isDSP
	 * @throws IOException
	 * @throws ErroSistemicoException
	 * @return Returns the void.
	 */
	public void gerarRelatorio(Relatorio relatorio, final EspecieRelatorioEnum especieRelatorio) throws IOException, ErroSistemicoException
	{
		if (EspecieRelatorioEnum.RELATORIO_DSP.equals(especieRelatorio)) {
			if (EnumTipoRelatorio.INSTANTANEO.equals(relatorio.getTipoRelatorio())) {
				popularRelatorioSituacaoLocal(relatorio);
			}
			else {
				popularRelatorioSituacaoOficial(relatorio);
			}
		}
		else if (EspecieRelatorioEnum.INDICADOR_QUALIDADE.equals(especieRelatorio)) {
			controladorRelAQ.popularRelatorio(relatorio);
		}
		else if (EspecieRelatorioEnum.DESLOCAMENTO_MAQUINA.equals(especieRelatorio)) {
			controladorRelDM.popularRelatorio(relatorio);
		}
		else if (EspecieRelatorioEnum.PLANO_RECUPERACAO.equals(especieRelatorio)) {
			Blendagem blendagem = getInterfaceInicial().getInterfaceBlendagem().getBlendagemVisualizada();
			
			
			SituacaoPatio situacao = getInterfaceInicial().getInterfaceDSP().getInterfaceInicial().getSituacaoPatioExibida();
			
			controladorRelPR.gerarRelatorioPlanoDeRecuperacao(relatorio, blendagem,situacao);
		}
		else if (EspecieRelatorioEnum.ATENDIMENTO_CARGA.equals(especieRelatorio)) {
			controladorRelAC.popularRelatorio(relatorio);
		}
	}

	/**
	 * 
	 * recuperarRelatorio
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 07/08/2009
	 * @see
	 * @param nomesRelatorio
	 * @throws IOException
	 * @throws ErroSistemicoException
	 * @return Returns the void.
	 */	public void recuperarRelatorio(List<String> nomesRelatorio) throws IOException, ErroSistemicoException
	{
		List<Relatorio> listaRelatorio = buscarRelatoriosPorNome(nomesRelatorio);
		StringBuffer arquivo = new StringBuffer();
		for (Relatorio relatorio : listaRelatorio)
		{
			arquivo = new StringBuffer();
			arquivo.append(SERVIDOR_RELATORIOS).append(File.separator).append(relatorio.getNomeRelatorio()).append(".pdf");
			File file = new File(arquivo.toString());

			if (!file.exists())
			{
				int opcao = JOptionPane.showConfirmDialog(null, PropertiesUtil.getMessage("relatorio.gestao.recuperar.relatorio.notfound"), PropertiesUtil.getMessage("relatorio.gestao.recuperar.relatorio.notfound.title"), JOptionPane.YES_NO_OPTION);
				if (JOptionPane.YES_OPTION == opcao)
				{
					gerarRelatorio(relatorio, EspecieRelatorioEnum.RELATORIO_DSP);
				}
			}
			abrirRelatorio(relatorio);
		}
		arquivo = null;
	}

	/**
	 * 
	 * buscarRelatoriosPorNome
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 07/08/2009
	 * @see
	 * @param nomesRelatorio
	 * @return
	 * @return Returns the List<Relatorio>.
	 */
	private List<Relatorio> buscarRelatoriosPorNome(List<String> nomesRelatorio)
	{
		List<Relatorio> listaRelatorio = new ArrayList<Relatorio>();
		for (String nome : nomesRelatorio)
		{
			Relatorio rel = encontrarRelatorioPorNome(nome);
			if (rel != null)
			{
				listaRelatorio.add(rel);
			}
		}
		return listaRelatorio;
	}

	/**
	 * 
	 * encontrarRelatorioPorNome
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 07/08/2009
	 * @see
	 * @param nome
	 * @return
	 * @return Returns the Relatorio.
	 */
	private Relatorio encontrarRelatorioPorNome(String nome)
	{
		Relatorio rel = null;
		for (Relatorio relatorio : this.getRelatorios())
		{
			if (relatorio.getNomeRelatorio().equalsIgnoreCase(nome))
			{
				rel = relatorio;
				break;
			}
		}
		return rel;
	}

	/**
	 * 
	 * excluirRelatorio
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 07/08/2009
	 * @see
	 * @param lista
	 * @throws ErroSistemicoException
	 * @return Returns the void.
	 */
	public void excluirRelatorio(List<String> lista) throws ErroSistemicoException
	{
		if (lista.size() > 0)
		{
			List<Relatorio> listaRelatorios = buscarRelatoriosPorNome(lista);
			controladorGestaoRelatorio.removerRelatorios(listaRelatorios);
            StringBuffer arquivo = new StringBuffer();
			for (Relatorio relatorio : listaRelatorios)
			{
				arquivo = new StringBuffer();
				arquivo.append(SERVIDOR_RELATORIOS).append(File.separator).append(relatorio.getNomeRelatorio()).append(".pdf");
				File file = new File(arquivo.toString());
				file.delete();
			}
			popularListaRelatorios();
		}
	}

	/**
	 * 
	 * abrirRelatorio
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 07/08/2009
	 * @see
	 * @param relatorio
	 * @throws IOException
	 * @throws ErroSistemicoException
	 * @return Returns the void.
	 */
	public void abrirRelatorio(Relatorio relatorio) throws IOException, ErroSistemicoException
	{
		try
		{
			GeradorPDF.abrirRelatorioPDF(SERVIDOR_RELATORIOS, relatorio.getNomeRelatorio());
		}
		catch (ErroGeracaoRelatorioPDF e)
		{
			e.printStackTrace();
			throw new ErroSistemicoException(e.getMessage());
		}
		finally {
			System.gc();
		}
	}

	/**
	 * 
	 * popularRelatorioSituacaoLocal
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 07/08/2009
	 * @see
	 * @param relatorio
	 * @throws IOException
	 * @throws ErroSistemicoException
	 * @return Returns the void.
	 */
	private void popularRelatorioSituacaoLocal(Relatorio relatorio) throws IOException, ErroSistemicoException
	{

		List<SituacaoPatio> situacaoPatioList = getInterfaceInicial().getControladorInterfaceInicial().
		getPlanejamento().getControladorDePlano().obterSituacao(relatorio.getHorarioInicioRelatorio(), relatorio.getHorarioFimRelatorio());

		
		
		gerarRelatorioPDF(relatorio, situacaoPatioList, relatorio.getTipoRelatorio().name());
	}
	
	/**
	 * 
	 * popularRelatorioSituacaoOficial
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 07/08/2009
	 * @see
	 * @param relatorio
	 * @throws IOException
	 * @throws ErroSistemicoException
	 * @return Returns the void.
	 */
	private void popularRelatorioSituacaoOficial(Relatorio relatorio) throws IOException, ErroSistemicoException
	{

		PlanoEmpilhamentoRecuperacao per;
		//Darley removendo a chamada remota
		 ///IControladorModelo controladorModelo = InterfaceInicial.lookUpModelo();
		 IControladorModelo controladorModelo = new ControladorModelo();
		per = controladorModelo.buscarPlanoEmpilhamentoRecuperacaoOficial();
		controladorModelo = null;
		List<SituacaoPatio> situacaoPatioList = getInterfaceInicial().getControladorInterfaceInicial().
		getPlanejamento().getControladorDePlano().obterSituacao(per.getListaSituacoesPatio(),
				relatorio.getHorarioInicioRelatorio(), relatorio.getHorarioFimRelatorio());

		gerarRelatorioPDF(relatorio, situacaoPatioList, relatorio.getTipoRelatorio().name());

	}
	
	/**
	 * 
	 * @param relatorio
	 */
	private void setUsuarioRelatorio(Relatorio relatorio) {
		//Recuperando o Usuario do Relatorio
		if (relatorio.getUsuario() == null) {
			User user = listaUsuarios.get(relatorio.getIdUsuario());
			if (user == null) {
				user = new User();
			}
			relatorio.setUsuario(user);
		}
	}

	/**
	 * 
	 * gerarRelatorioPDF
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 07/08/2009
	 * @see
	 * @param relatorio
	 * @param situacaoPatioList
	 * @param tipo
	 * @throws IOException
	 * @throws ErroSistemicoException
	 * @return Returns the void.
	 */
	private void gerarRelatorioPDF(Relatorio relatorio, List<SituacaoPatio> situacaoPatioList, String tipo) throws IOException, ErroSistemicoException
	{
		try {
			if (situacaoPatioList.isEmpty())
			{
				throw new ErroSistemicoException(PropertiesUtil.getMessage("relatorio.configurar.padrao.emptyreport"));
			}

			setUsuarioRelatorio(relatorio);

			GeradorPDF pdf = new GeradorPDF(SERVIDOR_RELATORIOS, relatorio.getNomeRelatorio());

			ImageIcon logo = new ImageIcon(getClass().getResource("/images/logo_samarco.png"));

			BufferedImage imageLegenda = null;
			if (relatorio.getPadraoRelatorio().contemCampo(EnumCamposRelatorio.IMAGEM_SITUACAO.toString())) {
				//Darley Removendo chamada remota
				//			IControladorModelo controladorModelo = InterfaceInicial.lookUpModelo();
				IControladorModelo controladorModelo = new ControladorModelo();
				List<TipoProduto> listaTiposProduto = controladorModelo.buscarTiposProduto(new TipoProduto());
				controladorModelo = null;
				imageLegenda = GeradorImagemDSPUtil.getImagemLegendaRelatorio(listaTiposProduto);
			}

			for (SituacaoPatio situacaoPatio : situacaoPatioList)
			{

				BufferedImage imageSituacao = null;

				if (relatorio.getPadraoRelatorio().contemCampo(EnumCamposRelatorio.IMAGEM_SITUACAO.toString()))
				{
					
					JPanel interfaceDSP = GeradorImagemDSPUtil.getPanelInterfaceDSP(situacaoPatio,
							getInterfaceInicial().getControladorInterfaceInicial());
					int width = interfaceDSP.getWidth();
					int height = interfaceDSP.getHeight();
					imageSituacao = GeradorImagemDSPUtil.getImageFromPanel(interfaceDSP, width, height);
					interfaceDSP = null;
				}

				String titulo = PropertiesUtil.getMessage("relatorio.cabecalho.title");
				String subTitulo = PropertiesUtil.getMessage("relatorio.cabecalho.subtitle");
				RelatorioPDF relatorioPDF = new RelatorioPDF(titulo, subTitulo, logo.getImage());
				CabecalhoRelatorio cabecalho = relatorioPDF.getCabecalhoRelatorio();
				cabecalho.setDataSituacao(situacaoPatio.getDtInicio());
				cabecalho.setDataInicioPeriodo(relatorio.getHorarioInicioRelatorio());
				cabecalho.setDataFimPeriodo(relatorio.getHorarioFimRelatorio());
				cabecalho.setUsuario(relatorio.getUsuario().getName());
				cabecalho.setTipoRelatorio(tipo);
				cabecalho.setQtdPilhas(situacaoPatio.getListaDePilhasNosPatios(situacaoPatio.getDtInicio()).size());
				adicionarColunasRelatorioPDF(relatorioPDF, relatorio.getPadraoRelatorio());



				Map<Integer, List<Object>> dados = popularDadosRelatorioPDF(situacaoPatio, relatorio.getPadraoRelatorio());
				relatorioPDF.setImagemCabecalhoRelatorio(imageLegenda, imageSituacao);
				relatorioPDF.setDadosCorpoRelatorio(dados);
				pdf.addRelatorio(relatorioPDF);

				//Tentativa de minimizar o consumo de memoria
				imageSituacao = null;
				System.gc();
			}

			imageLegenda = null;


			pdf.gerarPDFDSP();
		}
		catch (ErroGeracaoRelatorioPDF e)
		{
			throw new ErroSistemicoException(e);
		}
		finally {
			System.gc();
		}
	}

	/**
	 * 
	 * adicionarColunasRelatorioPDF
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 07/08/2009
	 * @see
	 * @param relatorio
	 * @param padrao
	 * @return Returns the void.
	 */
	private void adicionarColunasRelatorioPDF(RelatorioPDF relatorio, PadraoRelatorio padrao)
	{

		int ordem = 1;
		double totalCampos = padrao.getTotalTamanhoCampos();
		boolean gerouGrupo = false;

		for (PadraoCampo padraoCampo : padrao.getListaDeCampos())
		{
			String nomeCampo = padraoCampo.getCampoRelatorio().getNomeCampo();
			if (ehCampoDoRelatorio(nomeCampo)) {
				if (EnumCamposRelatorio.PILHA.toString().equals(nomeCampo))
				{
					float tamanho = (float) ((EnumCamposRelatorio.PILHA.getPeso() / totalCampos) * 100);
					tamanho = Math.round(tamanho * 100) / 100f;
					//relatorio.addColuna(new Coluna(ordem++, nomeCampo, tamanho, CalculoColunaEnum.VAZIO, false, false, false));
					relatorio.addColuna(new Coluna(ordem++,nomeCampo,tamanho,CalculoColunaEnum.NAO_CALCULA,false,0,null,false));
				}
				else if (EnumCamposRelatorio.PATIO.toString().equals(nomeCampo))
				{
					float tamanho = (float) ((EnumCamposRelatorio.PATIO.getPeso() / totalCampos) * 100);
					tamanho = Math.round(tamanho * 100) / 100f;
					//relatorio.addColuna(new Coluna(ordem++, nomeCampo, tamanho, CalculoColunaEnum.VAZIO, false, false, false));
					relatorio.addColuna(new Coluna(ordem++, nomeCampo, tamanho, CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
				}
				else if (EnumCamposRelatorio.INICIO.toString().equals(nomeCampo))
				{
					float tamanho = (float) ((EnumCamposRelatorio.INICIO.getPeso() / totalCampos) * 100);
					tamanho = Math.round(tamanho * 100) / 100f;
					//relatorio.addColuna(new Coluna(ordem++, nomeCampo, tamanho, CalculoColunaEnum.VAZIO, false, false, false));
					relatorio.addColuna(new Coluna(ordem++, nomeCampo, tamanho, CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
				}
				else if (EnumCamposRelatorio.FIM.toString().equals(nomeCampo))
				{
					float tamanho = (float) ((EnumCamposRelatorio.FIM.getPeso() / totalCampos) * 100);
					tamanho = Math.round(tamanho * 100) / 100f;
					//relatorio.addColuna(new Coluna(ordem++, nomeCampo, tamanho, CalculoColunaEnum.VAZIO, false, false, false));
					relatorio.addColuna(new Coluna(ordem++, nomeCampo, tamanho, CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
				}
				else if (EnumCamposRelatorio.CLIENTE.toString().equals(nomeCampo))
				{
					float tamanho = (float) ((EnumCamposRelatorio.CLIENTE.getPeso() / totalCampos) * 100);
					tamanho = Math.round(tamanho * 100) / 100f;
					relatorio.addColuna(new Coluna(ordem++, nomeCampo, tamanho, CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
				}
				else if (EnumCamposRelatorio.PRODUTO.toString().equals(nomeCampo))
				{
					float tamanho = (float) ((EnumCamposRelatorio.PRODUTO.getPeso() / totalCampos) * 100);
					tamanho = Math.round(tamanho * 100) / 100f;
					Coluna coluna = new Coluna(ordem++, nomeCampo, tamanho, CalculoColunaEnum.NAO_CALCULA, false, 0, null, true);
					coluna.setMostraValorGrupo(true);
					relatorio.addColuna(coluna);
					relatorio.addColuna(new Coluna(ordem++, 
							PropertiesUtil.getMessage("relatorio.corpo.coluna.familia"), 
							0, 
							CalculoColunaEnum.SOMA, 
							true, 
							1, 
							new CalculoColunaEnum[]{CalculoColunaEnum.SOMA},
							false,
							PropertiesUtil.getMessage("relatorio.corpo.coluna.familia")));

					relatorio.addColuna(new Coluna(ordem++, 
							PropertiesUtil.getMessage("relatorio.corpo.coluna.codproduto"),
							0,
							CalculoColunaEnum.MEDIA,
							true, 
							2,
							new CalculoColunaEnum[]{CalculoColunaEnum.SOMA,CalculoColunaEnum.MEDIA},
							false,
							PropertiesUtil.getMessage("relatorio.corpo.coluna.codproduto")));
					gerouGrupo = true;
				}
				else if (EnumCamposRelatorio.TON.toString().equals(nomeCampo))
				{
					float tamanho = (float) ((EnumCamposRelatorio.TON.getPeso() / totalCampos) * 100);
					tamanho = Math.round(tamanho * 100) / 100f;
					//relatorio.addColuna(new Coluna(ordem++, nomeCampo, tamanho, CalculoColunaEnum.SOMA, false, false, false));
					relatorio.addColuna(new Coluna(ordem++, nomeCampo, tamanho, CalculoColunaEnum.SOMA, false, 0, null, false));
				}
				else if (EnumCamposRelatorio.EMERG.toString().equals(nomeCampo))
				{
					float tamanho = (float) ((EnumCamposRelatorio.EMERG.getPeso() / totalCampos) * 100);
					tamanho = Math.round(tamanho * 100) / 100f;
					//relatorio.addColuna(new Coluna(ordem++, nomeCampo, tamanho, CalculoColunaEnum.VAZIO, false, false, false));
					relatorio.addColuna(new Coluna(ordem++, nomeCampo, tamanho, CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
				}
				else
				{
					float tamanho = (float) ((EnumCamposRelatorio.getPesoNaoDefinido() / totalCampos) * 100);
					tamanho = Math.round(tamanho * 100) / 100f;
					//relatorio.addColuna(new Coluna(ordem++, nomeCampo, tamanho, CalculoColunaEnum.MEDIA, false, false, false));
					relatorio.addColuna(new Coluna(ordem++, nomeCampo, tamanho, CalculoColunaEnum.MEDIA, false, 0, null, false));
				}
			}
		}

		if (!gerouGrupo) {
			relatorio.addColuna(new Coluna(ordem++, 
					"Grupo1", 
					0, 
					CalculoColunaEnum.NAO_CALCULA, 
					true, 
					1, 
					null,
					false));
		}
		relatorio.ajustarTamanhoColuna(ordem - 1);
	}

	/**
	 * 
	 * popularDadosRelatorioPDF
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 07/08/2009
	 * @see
	 * @param situacaoPatio
	 * @param padrao
	 * @return
	 * @throws IOException
	 * @return Returns the Map<Integer,List<Object>>.
	 */
	private Map<Integer, List<Object>> popularDadosRelatorioPDF(SituacaoPatio situacaoPatio, PadraoRelatorio padrao) throws IOException
	{
		Integer registros = 1;
		Map<Integer, List<Object>> dados = new HashMap<Integer, List<Object>>();

		List<SituacaoPatioRelatorioVO> listaDados = recuperarSituacaoPatioRelatorioVO(situacaoPatio);

		Collections.sort(listaDados);

		for (SituacaoPatioRelatorioVO situacao : listaDados)
		{
			boolean gerouGrupo = false;
			List<Object> dadosList = new ArrayList<Object>();

			for (PadraoCampo padraoCampo : padrao.getListaDeCampos())
			{
				String nomeCampo = padraoCampo.getCampoRelatorio().getNomeCampo();
				if (ehCampoDoRelatorio(nomeCampo)) {
					if (EnumCamposRelatorio.PILHA.toString().equals(nomeCampo))
					{
						dadosList.add(situacao.getNomePilha());
					}
					else if (EnumCamposRelatorio.PATIO.toString().equals(nomeCampo))
					{
						dadosList.add(situacao.getNomePatio());
					}
					else if (EnumCamposRelatorio.INICIO.toString().equals(nomeCampo))
					{
						dadosList.add(situacao.getDataInicioStr());
					}
					else if (EnumCamposRelatorio.FIM.toString().equals(nomeCampo))
					{                                            
						dadosList.add(situacao.getDataFimStr());                                           
					}
					else if (EnumCamposRelatorio.CLIENTE.toString().equals(nomeCampo))
					{
						dadosList.add(situacao.getNomeCliente());
					}
					else if (EnumCamposRelatorio.PRODUTO.toString().equals(nomeCampo))
					{
						dadosList.add(situacao.getNomeProduto());
						dadosList.add(situacao.getCodigoFamiliaTipoProduto());
						dadosList.add(situacao.getCodigoTipoProduto());
						gerouGrupo = true;
					}
					else if (EnumCamposRelatorio.TON.toString().equals(nomeCampo))
					{
						dadosList.add(situacao.getTonelada());

					}
					else if (EnumCamposRelatorio.EMERG.toString().equals(nomeCampo))
					{
						dadosList.add(situacao.getEmergencia());
					}
					else
					{
						//Se chegou ate aqui quer dizer que sao os itens de controle
						Double obj = situacao.getValorItensDeControle(nomeCampo);
						/*if (obj == null)
					{
						obj = new Double(0);
					}*/
						dadosList.add(obj);
					}
				}
			}
				
			if (!gerouGrupo) {
				dadosList.add("grupo1");
			}
			dados.put(registros++, dadosList);

		}
		return dados;
	}

	/**
	 * 
	 * recuperarSituacaoPatioRelatorioVO
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 07/08/2009
	 * @see
	 * @param situacaoPatio
	 * @return
	 * @return Returns the List<SituacaoPatioRelatorioVO>.
	 */
	private List<SituacaoPatioRelatorioVO> recuperarSituacaoPatioRelatorioVO(SituacaoPatio situacaoPatio)
	{

		List<SituacaoPatioRelatorioVO> listaRelatorio = new ArrayList<SituacaoPatioRelatorioVO>();

		for (Pilha pilha : situacaoPatio.getListaDePilhasNosPatios(situacaoPatio.getDtInicio()))
		{

			if (!pilha.getListaDeBalizas().isEmpty())
			{
				SituacaoPatioRelatorioVO situacaoRelatorio = new SituacaoPatioRelatorioVO();

				situacaoRelatorio.setNomePilha(pilha.getNomePilha());
				Baliza primeiraBaliza = pilha.getListaDeBalizas().get(0);
				Baliza ultimaBaliza = pilha.getListaDeBalizas().get(pilha.getListaDeBalizas().size() - 1);
				situacaoRelatorio.setNomePatio(primeiraBaliza.getPatio().getNomePatio());                 
				situacaoRelatorio.setDataInicio(pilha.getHorarioInicioFormacao());
				situacaoRelatorio.setDataFim(pilha.getHorarioFimFormacao());
				//situacaoRelatorio.setDataFim(ultimaBaliza.getHorarioFimFormacao());
				if (pilha.getCliente() != null)
				{
					situacaoRelatorio.setNomeCliente(pilha.getCliente().getNomeCliente());
				}
				else
				{
					situacaoRelatorio.setNomeCliente("");
				}
				if (primeiraBaliza.getProduto() != null) {
					situacaoRelatorio.setCodigoFamiliaTipoProduto(primeiraBaliza.getProduto().getTipoProduto().getCodigoFamiliaTipoProduto());
					situacaoRelatorio.setCodigoTipoProduto(primeiraBaliza.getProduto().getTipoProduto().getCodigoTipoProduto());
					StringBuffer produto = new StringBuffer().append(situacaoRelatorio.getCodigoFamiliaTipoProduto()).append(" - ").append(situacaoRelatorio.getCodigoTipoProduto());
					situacaoRelatorio.setNomeProduto(produto.toString());
				} else if (ultimaBaliza.getProduto() != null) {
					situacaoRelatorio.setCodigoFamiliaTipoProduto(ultimaBaliza.getProduto().getTipoProduto().getCodigoFamiliaTipoProduto());
					situacaoRelatorio.setCodigoTipoProduto(ultimaBaliza.getProduto().getTipoProduto().getCodigoTipoProduto());
					StringBuffer produto = new StringBuffer().append(situacaoRelatorio.getCodigoFamiliaTipoProduto()).append(" - ").append(situacaoRelatorio.getCodigoTipoProduto());
					situacaoRelatorio.setNomeProduto(produto.toString());
				}

				double tonelada = 0;
				boolean emergencia = false;
				for (Baliza baliza : pilha.getListaDeBalizas())
				{
					if (EnumTipoBaliza.EMERGENCIA_P5.equals(baliza.getTipoBaliza()) || EnumTipoBaliza.EMERGENCIA_TP17.equals(baliza.getTipoBaliza()) || EnumTipoBaliza.EMERGENCIA_TP009.equals(baliza.getTipoBaliza()))
					{
						emergencia = true;
					}
					if (baliza.getProduto() != null) {
						tonelada += baliza.getProduto().getQuantidade();
					
						for (ItemDeControle ic : baliza.getProduto().getQualidade().getListaDeItensDeControle())
		                {
		                    ItemDeControleQualidade icq = (ItemDeControleQualidade)ic;
		                    situacaoRelatorio.addItensDeControle(icq.getTipoItemControle().getDescricaoTipoItemControle(), icq.getValor());	                    
		                }
					}	
				}
				situacaoRelatorio.setEmergencia(emergencia ? PropertiesUtil.getMessage("relatorio.corpo.coluna.emergencia.label") : "");
				situacaoRelatorio.setTonelada(tonelada);
				situacaoRelatorio.setPeneirar("");
/*				//Popular os itens de controle
				if (primeiraBaliza.getProduto() != null) {
					for (ItemDeControle itemDeControle : primeiraBaliza.getProduto().getQualidade().getListaDeItensDeControle())
					{
						situacaoRelatorio.addItensDeControle(itemDeControle.getTipoItemControle().getDescricaoTipoItemControle(), itemDeControle.getValor());
					}
				}	
*/
				listaRelatorio.add(situacaoRelatorio);
			}
		}
		return listaRelatorio;
	}

	/**
	 * 
	 * lookUpGestaoRelatorio
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 07/08/2009
	 * @see
	 * @return
	 * @throws ErroSistemicoException
	 * @return Returns the IControladorGestaoRelatorio.
	 */
	private IControladorGestaoRelatorio lookUpGestaoRelatorio() throws ErroSistemicoException
	{
		try
		{
			//return (IControladorGestaoRelatorio) ServiceLocator.getRemote("bs/ControladorGestaoRelatorio", IControladorGestaoRelatorio.class);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new ErroSistemicoException(PropertiesUtil.getMessage("relatorio.lookup.error"));
		}
        return controladorGestaoRelatorio;
	}

	/**
	 * Get relatorios
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 22/06/2009
	 */
	public List<Relatorio> getRelatorios()
	{
		return relatorios;
	}

	/**
	 * Change relatorios
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param relatorios
	 * @since 22/06/2009
	 */
	public void setRelatorios(List<Relatorio> relatorios)
	{
		this.relatorios = relatorios;
	}

	/**
	 * Get defaultTableModel
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 22/06/2009
	 */
	public StockYardTableModel getDefaultTableModel()
	{
		return defaultTableModel;
	}

	/**
	 * Change defaultTableModel
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param defaultTableModel
	 * @since 22/06/2009
	 */
	public void setDefaultTableModel(StockYardTableModel defaultTableModel)
	{
		this.defaultTableModel = defaultTableModel;
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
	 * Get controladorGestaoRelatorio
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 24/06/2009
	 */
	public IControladorGestaoRelatorio getControladorGestaoRelatorio()
	{
		return controladorGestaoRelatorio;
	}

	/**
	 * Get interfaceGestaoRelatorio
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 29/06/2009
	 */
	public InterfaceGestaoRelatorio getInterfaceGestaoRelatorio()
	{
		return interfaceGestaoRelatorio;
	}

	/**
	 * Change interfaceGestaoRelatorio
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param interfaceGestaoRelatorio
	 * @since 29/06/2009
	 */
	public void setInterfaceGestaoRelatorio(
			InterfaceGestaoRelatorio interfaceGestaoRelatorio)
	{
		this.interfaceGestaoRelatorio = interfaceGestaoRelatorio;
	}
}