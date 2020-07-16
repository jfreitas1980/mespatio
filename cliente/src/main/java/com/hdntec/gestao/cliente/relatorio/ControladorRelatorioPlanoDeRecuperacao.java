package com.hdntec.gestao.cliente.relatorio;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import br.com.cflex.samarco.supervision.stockyard.relatorio.GeradorPDF;
import br.com.cflex.samarco.supervision.stockyard.relatorio.RelatorioPDF;
import br.com.cflex.samarco.supervision.stockyard.relatorio.controles.Coluna;
import br.com.cflex.samarco.supervision.stockyard.relatorio.controles.TipoConteudo;
import br.com.cflex.samarco.supervision.stockyard.relatorio.enums.AlinhamentoEnum;
import br.com.cflex.samarco.supervision.stockyard.relatorio.enums.CalculoColunaEnum;
import br.com.cflex.samarco.supervision.stockyard.relatorio.enums.EstiloFonteEnum;
import br.com.cflex.samarco.supervision.stockyard.relatorio.enums.OrientacaoPaginaEnum;
import br.com.cflex.samarco.supervision.stockyard.relatorio.exception.ErroGeracaoRelatorioPDF;

import com.hdntec.gestao.cliente.util.numeros.DSSStockyardFuncoesNumeros;
import com.hdntec.gestao.domain.blendagem.Blendagem;
import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
import com.hdntec.gestao.domain.planta.comparadores.ComparadorBalizas;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.produto.entity.ItemDeControle;
import com.hdntec.gestao.domain.produto.entity.Produto;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.domain.produto.enums.EnumValorRegraFarol;
import com.hdntec.gestao.domain.relatorio.FaixaAmostragemFrequencia;
import com.hdntec.gestao.domain.relatorio.PlanoRecuperacaoVO;
import com.hdntec.gestao.domain.relatorio.Relatorio;
import com.hdntec.gestao.domain.relatorio.TabelaAmostragemFrequencia;
import com.hdntec.gestao.domain.relatorio.enums.EnumCamposRelatorio;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.ExcessoDeMaterialParaEmbarqueException;
import com.hdntec.gestao.exceptions.ProdutoIncompativelException;
import com.hdntec.gestao.integracao.controladores.ControladorModelo;
import com.hdntec.gestao.integracao.controladores.IControladorModelo;
import com.hdntec.gestao.integracao.produto.ControladorItemDeControle;
import com.hdntec.gestao.util.PropertiesUtil;

public class ControladorRelatorioPlanoDeRecuperacao {
	
	private final String SERVIDOR_RELATORIOS = PropertiesUtil.buscarPropriedade("pasta.servidor.relatorios").trim();
	private List<String> camposNaoUtilizados;
	private List<String> camposDefault;

    private SituacaoPatio situacao;

	
	public ControladorRelatorioPlanoDeRecuperacao() {		
		populaCamposNaoUtilizados();
		populaCamposDefault();
	}
	
	/**
	 * 
	 * gerarRelatorioPlanoDeRecuperacao
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 17/08/2009
	 * @see
	 * @param relatorio
	 * @param blendagem
	 * @throws IOException
	 * @throws ErroSistemicoException
	 * @return Returns the void.
	 */
	public void gerarRelatorioPlanoDeRecuperacao(Relatorio relatorio, Blendagem blendagem,SituacaoPatio situacao) throws IOException, ErroSistemicoException{
		
		this.situacao = situacao;
	    List<String> listaCamposRelatorio = 
			relatorio.getPadraoRelatorio().getSubListaPadraoCampos(camposNaoUtilizados);
		
		double totalCampos = relatorio.getPadraoRelatorio()
					.getTotalTamanhoCampos(camposNaoUtilizados);
		if (listaCamposRelatorio.size()<=0) {
			listaCamposRelatorio = camposDefault;
			totalCampos = calcularValorCamposDefault();
		}
		
		GeradorPDF pdf = new GeradorPDF(SERVIDOR_RELATORIOS, relatorio.getNomeRelatorio());
		ImageIcon logo = new ImageIcon(getClass().getResource("/images/logo_samarco.png"));
		String titulo = PropertiesUtil.getMessage("relatorio.planoDeRecuperacao.title");
		String subTitulo = PropertiesUtil.getMessage("relatorio.planoDeRecuperacao.subtitle");
		RelatorioPDF relatorioPDF = new RelatorioPDF(titulo, subTitulo, logo.getImage());
		
		relatorioPDF.getCabecalhoRelatorio().setListaDeDados(
				popularCabecalho(blendagem));
		relatorioPDF.getCabecalhoRelatorio().setQtdColunas(3);
		
		if (blendagem.getListaBalizasBlendadas() != null && 
				!blendagem.getListaBalizasBlendadas().isEmpty()) {
			
			
			criarCabecalhoTabela(relatorioPDF,listaCamposRelatorio,totalCampos);
			relatorioPDF.setDadosCorpoRelatorio(popularDados(blendagem,
					listaCamposRelatorio));
			
		}
		
		relatorioPDF.setDadosRodape(popularRodape(blendagem,
				listaCamposRelatorio));
		
		adicionarAmostragem(relatorioPDF, blendagem);
		
		adicionarObservacao(relatorioPDF,relatorio.getObservacao());
		
		pdf.addRelatorio(relatorioPDF);
		try {
		   pdf.gerarPDF(OrientacaoPaginaEnum.RETRATO);
		}
		catch (ErroGeracaoRelatorioPDF e) {
		   throw new ErroSistemicoException(e);
		}
	}
	
	private void adicionarAmostragem(RelatorioPDF relatorioPDF,
			Blendagem blendagem) {
	    
		double cargaPrevista = blendagem.getCargaSelecionada().getOrientacaoDeEmbarque().getQuantidadeNecessaria().doubleValue();
		TipoProduto tipoProduto = blendagem.getCargaSelecionada().getOrientacaoDeEmbarque().getTipoProduto();
		
		//TabelaAmostragemFrequencia tabela = new TabelaAmostragemFrequencia();
		//Darley removendo chamada remota
//		 IControladorModelo controladorModelo = InterfaceInicial.lookUpModelo();
		IControladorModelo controladorModelo = new ControladorModelo();
		TabelaAmostragemFrequencia tabela = controladorModelo.recuperarTabelaAmostragemAtual();
		if (tabela != null) {
			FaixaAmostragemFrequencia faixa = tabela.obterFaixa(cargaPrevista, 
					tipoProduto.getTipoDeProduto(), 
					tipoProduto.getCodigoFamiliaTipoProduto());

			if (faixa != null) {

				List<Object> dados = new ArrayList<Object>();
				List<TipoConteudo> formato = new ArrayList<TipoConteudo>();

				formato.add(new TipoConteudo(
						AlinhamentoEnum.CENTRALIZADO,
						EstiloFonteEnum.NEGRITO));
				dados.add("");
				formato.add(new TipoConteudo(
						AlinhamentoEnum.CENTRALIZADO,
						EstiloFonteEnum.NEGRITO));
				dados.add(PropertiesUtil.getMessage("relatorio.planoDeRecuperacao.amostragem.titulo"));
				formato.add(new TipoConteudo(
						AlinhamentoEnum.ESQUERDA,
						EstiloFonteEnum.NEGRITO));
				dados.add(PropertiesUtil.getMessage("relatorio.planoDeRecuperacao.amostragem"));
				formato.add(new TipoConteudo(
						AlinhamentoEnum.ESQUERDA,
						EstiloFonteEnum.NORMAL));
				String strCorte = PropertiesUtil.getMessage("relatorio.planoDeRecuperacao.amostragem.corte")+
				"  "+DSSStockyardFuncoesNumeros.getValorFormatado(faixa.getIncremento(), 0)
				+" "+PropertiesUtil.getMessage("relatorio.planoDeRecuperacao.amostragem.tmn");
				dados.add(strCorte);
				formato.add(new TipoConteudo(
						AlinhamentoEnum.ESQUERDA,
						EstiloFonteEnum.NEGRITO));
				dados.add(PropertiesUtil.getMessage("relatorio.planoDeRecuperacao.amostragem.frequencia"));
				formato.add(new TipoConteudo(
						AlinhamentoEnum.ESQUERDA,
						EstiloFonteEnum.NORMAL));

				String strUmidade = PropertiesUtil.getMessage("relatorio.planoDeRecuperacao.amostragem.umidade")+
				"  "+DSSStockyardFuncoesNumeros.getValorFormatado(faixa.getGranulometria(), 0)
				+" "+PropertiesUtil.getMessage("relatorio.planoDeRecuperacao.amostragem.tmn");
				dados.add(strUmidade);
				formato.add(new TipoConteudo(
						AlinhamentoEnum.ESQUERDA,
						EstiloFonteEnum.NORMAL));
				String strTambor = PropertiesUtil.getMessage("relatorio.planoDeRecuperacao.amostragem.tamboramento") +
				"  "+DSSStockyardFuncoesNumeros.getValorFormatado(faixa.getTamboramento(), 0)
				+" "+PropertiesUtil.getMessage("relatorio.planoDeRecuperacao.amostragem.tmn");
				dados.add(strTambor);
				formato.add(new TipoConteudo(
						AlinhamentoEnum.ESQUERDA,
						EstiloFonteEnum.NORMAL));
				String strNorma = PropertiesUtil.getMessage("relatorio.planoDeRecuperacao.amostragem.norma")+
				" "+tabela.getCodigo();
				dados.add(strNorma);
				formato.add(new TipoConteudo(
						AlinhamentoEnum.ESQUERDA,
						EstiloFonteEnum.NORMAL));
				dados.add(PropertiesUtil.getMessage("relatorio.planoDeRecuperacao.amostragem.analise"));
				formato.add(new TipoConteudo(
						AlinhamentoEnum.ESQUERDA,
						EstiloFonteEnum.NORMAL));
				dados.add("");
				formato.add(new TipoConteudo(
						AlinhamentoEnum.ESQUERDA,
						EstiloFonteEnum.NORMAL));
				dados.add("");
				controladorModelo = null;
				relatorioPDF.addComplementoRelatorio(dados, formato);
			}
		}
	}
	
	private void adicionarObservacao(RelatorioPDF relatorioPDF, String observacao) {
		List<Object> dados = new ArrayList<Object>();
		List<TipoConteudo> formato = new ArrayList<TipoConteudo>();
		
		formato.add(new TipoConteudo(
				AlinhamentoEnum.CENTRALIZADO,
				EstiloFonteEnum.NEGRITO));
		dados.add("");
		formato.add(new TipoConteudo(
				AlinhamentoEnum.CENTRALIZADO,
				EstiloFonteEnum.NEGRITO));
		dados.add(PropertiesUtil.getMessage("relatorio.planoDeRecuperacao.observacao"));
		formato.add(new TipoConteudo(
				AlinhamentoEnum.ESQUERDA,
				EstiloFonteEnum.NORMAL));
		dados.add(observacao);
		for (int i=0; i<10;i++) {
			formato.add(new TipoConteudo(
				AlinhamentoEnum.ESQUERDA,
				EstiloFonteEnum.NORMAL));
			dados.add("");
		}
		formato.add(new TipoConteudo(
				AlinhamentoEnum.CENTRALIZADO,
				EstiloFonteEnum.NORMAL));
		dados.add("_______________________________________________              _______________________________________________");
		String tecnico=PropertiesUtil.getMessage("relatorio.planoDeRecuperacao.assinatura.tecnico");
		String engenheiro=PropertiesUtil.getMessage("relatorio.planoDeRecuperacao.assinatura.engenheiro");
		formato.add(new TipoConteudo(
				AlinhamentoEnum.CENTRALIZADO,
				EstiloFonteEnum.NORMAL));
		dados.add(tecnico+"                                                                 "+engenheiro);
		formato.add(new TipoConteudo(
				AlinhamentoEnum.CENTRALIZADO,
				EstiloFonteEnum.NORMAL));
		dados.add("");
		
		relatorioPDF.addComplementoRelatorio(dados, formato);
	}
	
	
	/**
	 * Metodo que valida o campo do padrao escolhido se ele realmente pertence 
	 * ao relatorio
	 */
	private boolean ehCampoDoRelatorio(String nomeCampo) {
		
		return !camposNaoUtilizados.contains(nomeCampo);
	}
	
	private void criarCabecalhoTabela(RelatorioPDF relatorio,
			List<String> listaCampos, double totalCampos) {
		
		int ordem = 1;
		
		
		String lblPatio=PropertiesUtil.getMessage("relatorio.planoDeRecuperacao.patio");
		String lblPilha=PropertiesUtil.getMessage("relatorio.planoDeRecuperacao.pilha");
		String lblBaliza=PropertiesUtil.getMessage("relatorio.planoDeRecuperacao.baliza");
		String lblTons=PropertiesUtil.getMessage("relatorio.planoDeRecuperacao.tons");
		
		relatorio.addColuna(new Coluna(ordem++, "Grupo X", 0, 
				CalculoColunaEnum.NAO_CALCULA, true, 1,	null, false));
		
		for (String nomeCampo : listaCampos)
		{
			//String nomeCampo = padraoCampo.getCampoRelatorio().getNomeCampo();
			
			if (ehCampoDoRelatorio(nomeCampo)) {
				if (EnumCamposRelatorio.PILHA.toString().equals(nomeCampo))
				{
					float tamanho = (float) ((EnumCamposRelatorio.PILHA.getPeso() / totalCampos) * 100);
					tamanho = (float)Math.floor(tamanho * 1000) / 1000f;
					relatorio.addColuna(new Coluna(ordem++, lblPilha, tamanho, 
							CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));

				}
				else if (EnumCamposRelatorio.PATIO.toString().equals(nomeCampo))
				{
					float tamanho = (float) ((EnumCamposRelatorio.PATIO.getPeso() / totalCampos) * 100);
					tamanho = (float)Math.floor(tamanho * 1000) / 1000f;
					relatorio.addColuna(new Coluna(ordem++, lblPatio, tamanho, 
							CalculoColunaEnum.NAO_CALCULA, false, 0, null, true));
				}
				else if (EnumCamposRelatorio.BALIZA.toString().equals(nomeCampo))
				{
					float tamanho = (float) ((EnumCamposRelatorio.BALIZA.getPeso() / totalCampos) * 100);
					tamanho = (float)Math.floor(tamanho * 1000) / 1000f;
					relatorio.addColuna(new Coluna(ordem++, lblBaliza, tamanho,
							CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
				}
				else if (EnumCamposRelatorio.TON.toString().equals(nomeCampo))
				{
					float tamanho = (float) ((EnumCamposRelatorio.TON.getPeso() / totalCampos) * 100);
					tamanho = (float)Math.floor(tamanho * 1000) / 1000f;
					relatorio.addColuna(new Coluna(ordem++, lblTons, tamanho,
							CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));//SOMA
				}
				else 
				{
					float tamanho = (float) ((EnumCamposRelatorio.getPesoNaoDefinido() / totalCampos) * 100);
					tamanho = (float)Math.floor(tamanho * 1000) / 1000f;
					relatorio.addColuna(new Coluna(ordem++, nomeCampo, tamanho,
							CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));//MEDIA
				}
			}
		}
		relatorio.ajustarTamanhoColuna(ordem - 1);
	}
	
	
	
	
	private Map<Integer, List<Object>> popularDados(Blendagem blend,
			List<String> listaCampos/*PadraoRelatorio padrao*/) {
		
		Integer registros = 1;
		
		Map<Integer, List<Object>> dadosMap = new HashMap<Integer, List<Object>>();
		List<Blendagem> listaBlendagens = criaBalizasBlendadas(blend);
		
		List<PlanoRecuperacaoVO> listaVO = recuperarDadosBlendagem(listaBlendagens);
		listaVO.add(recuperarTotalizacaoBlendagem(blend));
		for(PlanoRecuperacaoVO planoVO : listaVO) {
			
			List<Object> listaColuna = new ArrayList<Object>();
			listaColuna.add("Grupo X");
			for (String nomeCampo : listaCampos) {
				//String nomeCampo = padraoCampo.getCampoRelatorio().getNomeCampo();
				if (ehCampoDoRelatorio(nomeCampo)) {
					if (EnumCamposRelatorio.PILHA.toString().equals(nomeCampo))
					{
						listaColuna.add(planoVO.getNomePilha());
					}
					else if (EnumCamposRelatorio.PATIO.toString().equals(nomeCampo))
					{
						listaColuna.add(planoVO.getNomePatio());
					}
					else if (EnumCamposRelatorio.BALIZA.toString().equals(nomeCampo))
					{
						listaColuna.add(planoVO.getIntervaloBalizas());
					}
					else if (EnumCamposRelatorio.TON.toString().equals(nomeCampo))
					{
						listaColuna.add(planoVO.getTonelada());

					}
					else 
					{
						//Se chegou ate aqui quer dizer que sao os itens de controle
						Double obj = planoVO.getValorItensDeControle(nomeCampo);
						listaColuna.add(obj);
					}
				}
			}
			dadosMap.put(registros++, listaColuna);
		}
		
		return dadosMap;
	}
	
	private List<PlanoRecuperacaoVO> recuperarDadosBlendagem(List<Blendagem> listaBlendagens) {
		
		List<PlanoRecuperacaoVO> listaResult = new ArrayList<PlanoRecuperacaoVO>();
		ComparadorBalizas comparadorBalizas = new ComparadorBalizas();
		StringBuffer value = new StringBuffer();
		for (Blendagem blendagem : listaBlendagens) {
			
			List<Baliza> balizaLista = blendagem.getListaBalizasBlendadas();
			Collections.sort(balizaLista, comparadorBalizas);
			Baliza balizaInicial = balizaLista.get(0);
			Baliza balizaFinal = balizaLista.get(balizaLista.size()-1);
			value = new StringBuffer();			
			value.append(balizaInicial.getNumero()).append(" - ").append(balizaFinal.getNumero());			 
			PlanoRecuperacaoVO planoVO = new PlanoRecuperacaoVO();			
			planoVO.setIntervaloBalizas(value.toString());
			planoVO.setNomePatio(balizaInicial.getPatio().getNomePatio());
			planoVO.setNomePilha(balizaInicial.retornaStatusHorario(situacao.getDtInicio()).getNomePilha());
			planoVO.setTonelada(blendagem.getProdutoResultante().getQuantidade());
			
			for (ItemDeControle item : blendagem.getProdutoResultante().
					getQualidade().getListaDeItensDeControleQualidade()) {
				
				planoVO.addItensDeControle(item.getTipoItemControle().getDescricaoTipoItemControle(), item.getValor());
			}
			
			listaResult.add(planoVO);
		}
		
		return listaResult;
	}
	
	/**
	 * 
	 * recuperarTotalizacaoBlendagem
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 26/08/2009
	 * @see
	 * @param blend
	 * @return
	 * @return Returns the PlanoRecuperacaoVO.
	 */
	private PlanoRecuperacaoVO recuperarTotalizacaoBlendagem(Blendagem blend) {
		
		PlanoRecuperacaoVO planoVO = new PlanoRecuperacaoVO();
		
		planoVO.setIntervaloBalizas("");
		planoVO.setNomePatio(PropertiesUtil.getMessage("relatorio.planoDeRecuperacao.calculado"));
		planoVO.setNomePilha("");
		planoVO.setTonelada(blend.getProdutoResultante().getQuantidade());
		for (ItemDeControle item : blend.getProdutoResultante().
				getQualidade().getListaDeItensDeControleQualidade()) {
			
			planoVO.addItensDeControle(
					item.getTipoItemControle().getDescricaoTipoItemControle(), 
					item.getValor());
		}
		return planoVO;
	}
	
	/**
	 * 
	 * criaBalizasBlendadas
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 26/08/2009
	 * @see
	 * @param blend
	 * @return
	 * @return Returns the List<Blendagem>.
	 */
	private List<Blendagem> criaBalizasBlendadas(Blendagem blend) {
		List<Blendagem> blendagemLista = new ArrayList<Blendagem>();
		//ComparadorBalizas comparadorBalizas = new ComparadorBalizas();
		
		List<Baliza> listaBalizas = blend.getListaBalizasBlendadas();
		Collections.sort(listaBalizas, Baliza.comparadorBaliza);

		Baliza balizaAux = null;
		Blendagem blendagem = Blendagem.getNewInstance();
		for (int i=0; i < listaBalizas.size(); i++) {
			Baliza baliza = listaBalizas.get(i);
			if (balizaAux != null) { // nao considera a primeira baliza
				if (balizaAux.retornaStatusHorario(situacao.getDtInicio()) != baliza.retornaStatusHorario(situacao.getDtInicio()) || (balizaAux.getNumero() + 1) != baliza.getNumero() ) {
					blendagemLista.add(blendagem);
					blendagem = Blendagem.getNewInstance();
				}
			}
			try {
				Produto produto = blendagem.adicionaBalizaBlendada(baliza);
				blendagem.inserirProdutoNaBlendagem(produto, produto.getQuantidade());
			} catch (ProdutoIncompativelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExcessoDeMaterialParaEmbarqueException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			balizaAux = baliza;
		}
		blendagemLista.add(blendagem);
		return blendagemLista;
	}
	
	
	private List<String> popularCabecalho(Blendagem blendagem) {
		List<String> cabecalho = new ArrayList<String>();
		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yy HH:mm");
		cabecalho.add(PropertiesUtil.getMessage("relatorio.planoDeRecuperacao.navio")+": " + 
				blendagem.getCargaSelecionada().getNavio(situacao.getDtInicio()).toString());
		cabecalho.add("");
		
		cabecalho.add(PropertiesUtil.getMessage("relatorio.planoDeRecuperacao.recuperadora"));		
		String cliente = PropertiesUtil.getMessage("relatorio.planoDeRecuperacao.comprador")+": ";
		if (blendagem.getCargaSelecionada().getCliente(situacao.getDtInicio()) != null) {
			cliente += blendagem.getCargaSelecionada().getCliente(situacao.getDtInicio()).getNomeCliente();
		}
		cabecalho.add(cliente);
		cabecalho.add("");
		cabecalho.add(PropertiesUtil.getMessage("relatorio.planoDeRecuperacao.laboratorio"));
		cabecalho.add(PropertiesUtil.getMessage("relatorio.planoDeRecuperacao.dataChegada")+": " + 
				formatador.format(blendagem.getCargaSelecionada().getNavio(situacao.getDtInicio()).getDiaDeChegada()));
		cabecalho.add(PropertiesUtil.getMessage("relatorio.planoDeRecuperacao.numeroCarregamento")+": " + 
				"");
		cabecalho.add(PropertiesUtil.getMessage("relatorio.planoDeRecuperacao.torre"));
		cabecalho.add(PropertiesUtil.getMessage("relatorio.planoDeRecuperacao.tipoMaterial")+": " + 
				blendagem.getCargaSelecionada().getOrientacaoDeEmbarque().getTipoProduto().toString());
		cabecalho.add(PropertiesUtil.getMessage("relatorio.planoDeRecuperacao.real")+": " + 
				"");
		cabecalho.add(PropertiesUtil.getMessage("relatorio.planoDeRecuperacao.salaControle"));
		cabecalho.add(PropertiesUtil.getMessage("relatorio.planoDeRecuperacao.cargaPrevista")+": " + 
				blendagem.getCargaSelecionada().getOrientacaoDeEmbarque().getQuantidadeNecessaria().toString());
		cabecalho.add(PropertiesUtil.getMessage("relatorio.planoDeRecuperacao.programado")+": " + 
				"");
		cabecalho.add(PropertiesUtil.getMessage("relatorio.planoDeRecuperacao.arquivo"));

		return cabecalho;
	}
	
	/**
	 * 
	 * popularRodape
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 17/08/2009
	 * @see
	 * @param blendagem
	 * @return
	 * @return Returns the Map<Integer,List<Object>>.
	 */
	private Map<Integer, List<Object>> popularRodape(Blendagem blendagem,
			List<String> listaCampos/*PadraoRelatorio padrao*/) {

		Integer registros = 1;

		Map<Integer, List<Object>> dadosRodape = new HashMap<Integer, List<Object>>();

		List<Object> listaColuna = new ArrayList<Object>();

		listaColuna.add(""); //coluna do grupo
		for (String nomeCampo : listaCampos) {
			//String nomeCampo = padraoCampo.getCampoRelatorio().getNomeCampo();
			if (ehCampoDoRelatorio(nomeCampo)) {
				if (EnumCamposRelatorio.PILHA.toString().equals(nomeCampo))
				{
					listaColuna.add("");
				}
				else if (EnumCamposRelatorio.PATIO.toString().equals(nomeCampo))
				{
					listaColuna.add(PropertiesUtil.getMessage("relatorio.planoDeRecuperacao.metaEmbarque")+" "+
							blendagem.getCargaSelecionada().getOrientacaoDeEmbarque().getTipoProduto().toString());
				}
				else if (EnumCamposRelatorio.BALIZA.toString().equals(nomeCampo))
				{
					listaColuna.add("");
				}
				else if (EnumCamposRelatorio.TON.toString().equals(nomeCampo))
				{
					listaColuna.add("");

				}
				else 
				{
					listaColuna.add(calcularItensRodape(blendagem, nomeCampo));
				}
			}
		}
		dadosRodape.put(registros++, listaColuna);

		return dadosRodape;
	}
	
	/**
	 * 
	 * calcularItensRodape
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 18/08/2009
	 * @see
	 * @param blendagem
	 * @param nomeCampo
	 * @return
	 * @return Returns the String.
	 */
	private String calcularItensRodape(Blendagem blendagem, String nomeCampo) {
		
		String result = null;
		
		TipoProduto tipoProduto = blendagem.getCargaSelecionada().
			getOrientacaoDeEmbarque().getTipoProduto();
		
		for (ItemDeControle item : blendagem.getCargaSelecionada().
				getOrientacaoDeEmbarque().
				getListaItemDeControleOrientacaoEmbarque()) {
			
			if (item.getTipoItemControle().getDescricaoTipoItemControle().
					equals(nomeCampo)) {
				
				EnumValorRegraFarol valorRegraFarol =
				ControladorItemDeControle.determinaValorRegraFarol(tipoProduto,
						item, situacao.getDtInicio()); 
				
				if (EnumValorRegraFarol.CRESCENTE.equals(valorRegraFarol)) {
					if(item.getLimInfMetaOrientacaoEmb() != null){
						result  =	DSSStockyardFuncoesNumeros
						.getValorFormatado(item.getLimInfMetaOrientacaoEmb(), 2)+" "+
						PropertiesUtil.getMessage("relatorio.regra.crescente");
					}else{
						result = PropertiesUtil.getMessage("relatorio.regra.nao.se.aplica");
					}
				} else if (EnumValorRegraFarol.DECRESCENTE.equals(valorRegraFarol)) {
					if(item.getLimSupMetaOrientacaoEmb() != null){
						result  =	DSSStockyardFuncoesNumeros
						.getValorFormatado(item.getLimSupMetaOrientacaoEmb(), 2)+" "+
						PropertiesUtil.getMessage("relatorio.regra.decrescente");
					}else{
						result = PropertiesUtil.getMessage("relatorio.regra.nao.se.aplica");
					}									
				}
				else if (EnumValorRegraFarol.INDIFERENTE.equals(valorRegraFarol)) {
					if(item.getLimInfMetaOrientacaoEmb() != null && item.getLimSupMetaOrientacaoEmb() != null){
						result =	DSSStockyardFuncoesNumeros
						.getValorFormatado(item.getLimInfMetaOrientacaoEmb(), 2);
						result += " "+PropertiesUtil.getMessage("relatorio.regra.indiferente")+" "+
						DSSStockyardFuncoesNumeros.getValorFormatado(item.getLimSupMetaOrientacaoEmb(), 2);
					}else{
						result = PropertiesUtil.getMessage("relatorio.regra.nao.se.aplica");
					}										
				}
				break;
			}
		}
		return result;
	}
	
	/**
	 * 
	 * populaCamposNaoUtilizados
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 20/08/2009
	 * @see
	 * @return Returns the void.
	 */
	private void populaCamposNaoUtilizados() {
		camposNaoUtilizados = new ArrayList<String>();
		camposNaoUtilizados.add(EnumCamposRelatorio.INICIO.toString());
		camposNaoUtilizados.add(EnumCamposRelatorio.FIM.toString());
		camposNaoUtilizados.add(EnumCamposRelatorio.CLIENTE.toString());
		camposNaoUtilizados.add(EnumCamposRelatorio.PRODUTO.toString());
		camposNaoUtilizados.add(EnumCamposRelatorio.EMERG.toString());
		camposNaoUtilizados.add(EnumCamposRelatorio.IMAGEM_SITUACAO.toString());
	}
	/**
	 * 
	 * populaCamposDefault
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 20/08/2009
	 * @see
	 * @return Returns the void.
	 */
	private void populaCamposDefault() {
		camposDefault = new ArrayList<String>();
		camposDefault.add(EnumCamposRelatorio.PATIO.toString());
		camposDefault.add(EnumCamposRelatorio.PILHA.toString());
		camposDefault.add(EnumCamposRelatorio.BALIZA.toString());
		camposDefault.add(EnumCamposRelatorio.TON.toString());
	}
	
	/**
	 * 
	 * calcularValorCamposDefault
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 20/08/2009
	 * @see
	 * @return
	 * @return Returns the double.
	 */
	private double calcularValorCamposDefault() {
		double result = 0;
		for (String campo : camposDefault) {
			result += EnumCamposRelatorio.valueOf(campo).getPeso();
		}
		return result;
	}
}
