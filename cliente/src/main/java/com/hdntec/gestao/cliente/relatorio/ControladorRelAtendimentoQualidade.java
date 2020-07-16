package com.hdntec.gestao.cliente.relatorio;

import java.awt.Color;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import br.com.cflex.samarco.supervision.stockyard.relatorio.GeradorPDF;
import br.com.cflex.samarco.supervision.stockyard.relatorio.RelatorioPDF;
import br.com.cflex.samarco.supervision.stockyard.relatorio.controles.CabecalhoRelatorio;
import br.com.cflex.samarco.supervision.stockyard.relatorio.controles.Coluna;
import br.com.cflex.samarco.supervision.stockyard.relatorio.controles.Texto;
import br.com.cflex.samarco.supervision.stockyard.relatorio.enums.CalculoColunaEnum;
import br.com.cflex.samarco.supervision.stockyard.relatorio.enums.EstiloFonteEnum;
import br.com.cflex.samarco.supervision.stockyard.relatorio.exception.ErroGeracaoRelatorioPDF;

import com.hdntec.gestao.cliente.util.numeros.DSSStockyardFuncoesNumeros;
import com.hdntec.gestao.domain.navios.entity.status.Carga;
import com.hdntec.gestao.domain.navios.entity.status.Navio;
import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
import com.hdntec.gestao.domain.plano.enums.TipoAtividadeEnum;
import com.hdntec.gestao.domain.planta.entity.status.Berco;
import com.hdntec.gestao.domain.produto.entity.ItemDeControle;
import com.hdntec.gestao.domain.produto.entity.TipoItemCoeficiente;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.domain.produto.enums.EnumEstadosDoFarol;
import com.hdntec.gestao.domain.produto.enums.EnumValorRegraFarol;
import com.hdntec.gestao.domain.relatorio.Relatorio;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.integracao.controladores.ControladorModelo;
import com.hdntec.gestao.integracao.controladores.IControladorModelo;
import com.hdntec.gestao.integracao.produto.ControladorItemDeControle;
import com.hdntec.gestao.util.PropertiesUtil;
import com.hdntec.gestao.util.datahora.DSSStockyardTimeUtil;

/**
 * 
 * <P><B>Description :</B><BR>
 * General ControladorRelAtendimentoQualidade
 * </P>
 * <P>
 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
 * @since 07/08/2009
 * @version $Revision: 1.1 $
 */
public class ControladorRelAtendimentoQualidade {

	private final String SERVIDOR_RELATORIOS;
	//private IControladorModelo controladorModelo;

	/**
	 * 
	 * @param controladorModelo
	 */
	public ControladorRelAtendimentoQualidade() {
		//this.controladorModelo = controladorModelo;
		this.SERVIDOR_RELATORIOS = PropertiesUtil.buscarPropriedade("pasta.servidor.relatorios").trim();
	}


	/**
	 * 
	 * popularRelatorio
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 07/08/2009
	 * @see
	 * @param spAtracados
	 * @param situacaoPatioList
	 * @param relatorio
	 * @return Returns the void.
	 */
	public void popularRelatorio(
			Relatorio relatorio) throws IOException, ErroSistemicoException {
		
		SituacaoPatio spAtracados = null;
		List<SituacaoPatio> situacaoPatioList = new ArrayList<SituacaoPatio>();
		//Removendo chamada remota
//		 IControladorModelo controladorModelo = InterfaceInicial.lookUpModelo();
		 IControladorModelo controladorModelo = new ControladorModelo();
			spAtracados = controladorModelo.buscarSituacaoPatioOficialAtualPorPeriodo(
					relatorio.getHorarioInicioRelatorio(),
					relatorio.getHorarioFimRelatorio());


			situacaoPatioList =	controladorModelo.buscarListaSPOficialPorPeriodoEAtividade(
						relatorio.getHorarioInicioRelatorio(),
						relatorio.getHorarioFimRelatorio(),
						TipoAtividadeEnum.SAIDA_DE_NAVIO);

			controladorModelo = null;
		
		
		//Preciso guardar o navio e a situacao do patio em que ele estava
		Map<Navio,SituacaoPatio> naviosRelatorio = new HashMap<Navio,SituacaoPatio>();
		if (spAtracados != null && spAtracados.getPlanta() != null) {

			for( Berco berco : spAtracados.getPlanta().getListaBercos(spAtracados.getDtInicio()) ) {
				if (berco.getNavioAtracado() != null &&	berco.getNavioAtracado().getDiaDeSaida()!= null) { 
					int posInicio = berco.getNavioAtracado().getDiaDeSaida().compareTo(relatorio.getHorarioInicioRelatorio());
					int posFim = berco.getNavioAtracado().getDiaDeSaida().compareTo(relatorio.getHorarioFimRelatorio());
					if (posInicio>=0 && posFim<=0) {
						naviosRelatorio.put(berco.getNavioAtracado(),spAtracados);
					}
				}
			}
		}
		for (SituacaoPatio situacaoPatio : situacaoPatioList) {
			if (!naviosRelatorio.containsKey(situacaoPatio.getAtividade().getMovimentacaoNavio().getNavio())) {
				naviosRelatorio.put(situacaoPatio.getAtividade().getMovimentacaoNavio().getNavio(),situacaoPatio);
			}
		}

		if (naviosRelatorio.size()>0) {

			gerarRelatorio(relatorio, naviosRelatorio);

		}
		else {
			throw new ErroSistemicoException(PropertiesUtil.getMessage("relatorio.configurar.padrao.emptyreport"));
		}
	}
	
	/**
	 * 
	 * gerarRelatorio
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 07/08/2009
	 * @see
	 * @param relatorio
	 * @param navioMap
	 * @throws IOException
	 * @throws ErroSistemicoException
	 * @return Returns the void.
	 */
	private void gerarRelatorio(Relatorio relatorio, 
			Map<Navio,SituacaoPatio> navioMap) throws IOException, ErroSistemicoException {

		GeradorPDF pdf = new GeradorPDF(SERVIDOR_RELATORIOS, relatorio.getNomeRelatorio());
		ImageIcon logo = new ImageIcon(getClass().getResource("/images/logo_samarco.png"));
		String titulo = PropertiesUtil.getMessage("relatorio.cabecalho.title.tatico1");
		String subTitulo = PropertiesUtil.getMessage("relatorio.cabecalho.subtitle.tatico1");
		String msgPeriodo = PropertiesUtil.getMessage("relatorio.tatico.periodo");
		String msgEntre = PropertiesUtil.getMessage("relatorio.tatico.entrePeriodo"); 
		//String msgTipo = PropertiesUtil.getMessage("relatorio.tatico.tipo");
		String msgNavio = PropertiesUtil.getMessage("relatorio.tatico.navio");
		String msgCarga = PropertiesUtil.getMessage("relatorio.tatico.carga");
		String msgTotal = PropertiesUtil.getMessage("relatorio.tatico.total");
		String msgProduto = PropertiesUtil.getMessage("relatorio.tatico.produto");


		for (Navio navio : navioMap.keySet()) {

			SituacaoPatio situacaoPatio = navioMap.get(navio);
			for ( Carga carga : navio.getListaDeCargasDoNavio(situacaoPatio.getDtInicio()) ) {

				RelatorioPDF relatorioPDF = new RelatorioPDF(titulo, subTitulo, logo.getImage());
				//Dados informados para o relatorio
				CabecalhoRelatorio cabecalho = relatorioPDF.getCabecalhoRelatorio();
				cabecalho.setQtdColunas(3);
				String dataHoraInicio = DSSStockyardTimeUtil.formatarData(relatorio.getHorarioInicioRelatorio(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
				String dataHoraFim = DSSStockyardTimeUtil.formatarData(relatorio.getHorarioFimRelatorio(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
				cabecalho.addColuna(msgPeriodo+": "+dataHoraInicio+" "+msgEntre+" "+dataHoraFim);
				cabecalho.addColuna("");
				cabecalho.addColuna("");
				//Dados do relatorio
				cabecalho.addColuna(msgNavio+": " + navio.getNomeNavio());
				cabecalho.addColuna(msgCarga+": " + carga.getIdentificadorCarga());
				NumberFormat nf = NumberFormat.getInstance();
				nf.setMaximumFractionDigits(2);
				cabecalho.addColuna(msgTotal+": "+nf.format(carga.getProduto().getQuantidade()));
				cabecalho.addColuna(msgProduto+": "+carga.getProduto().getTipoProduto().toString());
				adicionarColunasRelatorio(relatorioPDF);
				Map<Integer, List<Object>> dados = popularDadosRelatorio(carga, situacaoPatio);
				relatorioPDF.setDadosCorpoRelatorio(dados);
				pdf.addRelatorio(relatorioPDF);
			}
		}

		try
		{
			pdf.gerarPDF();
		}
		catch (ErroGeracaoRelatorioPDF e)
		{
			throw new ErroSistemicoException(e);
		}
	}

	/**
	 * 
	 * adicionarColunasRelatorio
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 07/08/2009
	 * @see
	 * @param relatorio
	 * @return Returns the void.
	 */
	private void adicionarColunasRelatorio(RelatorioPDF relatorio)
	{
		int ordem = 1;
		relatorio.addColuna(new Coluna(ordem++,"Grupo",0,CalculoColunaEnum.NAO_CALCULA,true,1,null,false));
		relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.tatico.item.itemDeControle"), 30, CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
		relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.tatico.item.metaEmbarque"), 15, CalculoColunaEnum.NAO_CALCULA, false, 0,null, false));
		relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.tatico.item.producao"), 15, CalculoColunaEnum.NAO_CALCULA, false, 0,null, false));
		relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.tatico.item.estimado"), 15, CalculoColunaEnum.NAO_CALCULA, false, 0,null, false));
		relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.tatico.item.embarque"), 15, CalculoColunaEnum.NAO_CALCULA, false, 0,null, false));
		relatorio.addColuna(new Coluna(ordem++, PropertiesUtil.getMessage("relatorio.tatico.item.status"), 10, CalculoColunaEnum.NAO_CALCULA, false, 0,null, false));
		relatorio.ajustarTamanhoColuna(ordem - 1);
	}

	/**
	 * 
	 * popularDadosRelatorio
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 07/08/2009
	 * @see
	 * @param carga
	 * @param situacaoPatio
	 * @return
	 * @return Returns the Map<Integer,List<Object>>.
	 */
	private Map<Integer, List<Object>> popularDadosRelatorio(Carga carga, 
			SituacaoPatio situacaoPatio) {
		
		Integer registros = 1;
		Map<Integer, List<Object>> result = new HashMap<Integer, List<Object>>();

		TipoProduto tipoProduto = carga.getOrientacaoDeEmbarque().getTipoProduto();

		for ( ItemDeControle itemDeControle : carga.getProduto().getQualidade().getListaDeItensDeControle()) {

			TipoItemCoeficiente coeficiente = ControladorItemDeControle.
			buscarCoeficienteDegradacaoDoPeriodoDoTipoItemDeControleParaUmDeterminadoTipoProduto(
					situacaoPatio.getDtInicio(),
					itemDeControle);
			Double valorEstimado = null;
			if (coeficiente != null && itemDeControle.getValor() != null) {
				valorEstimado = (itemDeControle.getValor() + coeficiente.getValorDoCoeficiente());
			} else if ((coeficiente == null || coeficiente.getValorDoCoeficiente() == 0) && itemDeControle.getValor() != null) {
				valorEstimado = itemDeControle.getValor();
			}	
			
			String valorMetaEmbarque = calcularMetaEmbarque(carga,tipoProduto,
					itemDeControle,situacaoPatio.getDtInicio());


			ControladorItemDeControle controlador = 
				new ControladorItemDeControle(itemDeControle);
			
			ItemDeControle itemOE = obterItemControleOrientacaoEmbarque(carga, itemDeControle);
			EnumEstadosDoFarol estadoFarol = null;
			if (itemOE != null) {
				estadoFarol = controlador.determinaEstadoDoFarol(tipoProduto, 
						itemOE,	situacaoPatio.getDtInicio());
			}


			List<Object> dadosList = new ArrayList<Object>();
			dadosList.add("grupo1");
			dadosList.add(itemDeControle.getTipoItemControle().getDescricaoTipoItemControle());
			dadosList.add(valorMetaEmbarque);
			dadosList.add(itemDeControle.getValor());
			dadosList.add(valorEstimado);
			dadosList.add(itemDeControle.getEmbarcado());
			
			if (EnumEstadosDoFarol.COMPLIANT.equals(estadoFarol)) {
				Texto texto = new Texto(
						PropertiesUtil.getMessage("relatorio.tatico.item.ok"),
						EstiloFonteEnum.NORMAL,6,
						Color.GREEN);
				dadosList.add(texto);
			}
			else if (EnumEstadosDoFarol.NONCOMPLIANT.equals(estadoFarol)) {
				Texto texto = new Texto(
						PropertiesUtil.getMessage("relatorio.tatico.item.naoOk"),
						EstiloFonteEnum.NORMAL,6,
						Color.RED);
				dadosList.add(texto);
			}
			else if (EnumEstadosDoFarol.OVERSPECIFIED.equals(estadoFarol)) {
				Texto texto = new Texto(
						PropertiesUtil.getMessage("relatorio.tatico.item.ok"),
						EstiloFonteEnum.NORMAL,6,
						Color.BLUE);
				dadosList.add(texto);
			}
			else {
				dadosList.add("");
			}
			result.put(registros++, dadosList);
		}
		return result;
	}
	
	/**
	 * 
	 * obterItemControleOrientacaoEmbarque
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 31/08/2009
	 * @see
	 * @param carga
	 * @param itemDeControle
	 * @return
	 * @return Returns the ItemDeControle.
	 */
	private ItemDeControle obterItemControleOrientacaoEmbarque(Carga carga, ItemDeControle itemDeControle) {
		ItemDeControle result = null;
		for(ItemDeControle item : carga.getOrientacaoDeEmbarque().getListaItemDeControle()) {
			
			if (item.getTipoItemControle().getIdTipoItemDeControle().equals(
					itemDeControle.getTipoItemControle().getIdTipoItemDeControle())) {
				result = item;
				break;
			}
		}
		return result;
	}
	
	/**
	 * 
	 * calcularMetaEmbarque
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 18/08/2009
	 * @see
	 * @param carga
	 * @param tipoProduto
	 * @param itemDeControle
	 * @param dataSitucao 
	 * @return
	 * @return Returns the String.
	 */
	private String calcularMetaEmbarque(Carga carga, TipoProduto tipoProduto,
			ItemDeControle itemDeControle, Date dataSitucao) {
		
		String valorMetaEmbarque = "";
		for(ItemDeControle item : carga.getOrientacaoDeEmbarque().getListaItemDeControle()) {
			
			if (item.getTipoItemControle().getIdTipoItemDeControle().equals(
					itemDeControle.getTipoItemControle().getIdTipoItemDeControle())) {
				
				EnumValorRegraFarol valorRegraFarol =
				ControladorItemDeControle.determinaValorRegraFarol(
						tipoProduto, item,dataSitucao);
				if (EnumValorRegraFarol.CRESCENTE.equals(valorRegraFarol)) {
					if(item.getLimInfMetaOrientacaoEmb() != null){
						valorMetaEmbarque =	DSSStockyardFuncoesNumeros
						.getValorFormatado(item.getLimInfMetaOrientacaoEmb(), 2)+" "+
						PropertiesUtil.getMessage("relatorio.regra.crescente");
					}else{
						valorMetaEmbarque = PropertiesUtil.getMessage("relatorio.regra.nao.se.aplica");
					}
				}
				else if (EnumValorRegraFarol.DECRESCENTE.equals(valorRegraFarol)) {
					if(item.getLimSupMetaOrientacaoEmb() != null){
						valorMetaEmbarque =	PropertiesUtil.getMessage("relatorio.regra.decrescente")+" "+
						DSSStockyardFuncoesNumeros.getValorFormatado(item.getLimSupMetaOrientacaoEmb(), 2);
					}else{
						valorMetaEmbarque = PropertiesUtil.getMessage("relatorio.regra.nao.se.aplica");
					}
				}
				else if (EnumValorRegraFarol.INDIFERENTE.equals(valorRegraFarol)) {
					if(item.getLimInfMetaOrientacaoEmb() != null && item.getLimSupMetaOrientacaoEmb() != null){
						valorMetaEmbarque =	DSSStockyardFuncoesNumeros
						.getValorFormatado(item.getLimInfMetaOrientacaoEmb(), 2);
						valorMetaEmbarque += " "+PropertiesUtil.getMessage("relatorio.regra.indiferente")+" "+
						DSSStockyardFuncoesNumeros.getValorFormatado(item.getLimSupMetaOrientacaoEmb(), 2);
					}else{
						valorMetaEmbarque = PropertiesUtil.getMessage("relatorio.regra.nao.se.aplica");
					}
				}
			}
		}
		return valorMetaEmbarque;
	}
}
