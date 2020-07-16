package com.hdntec.gestao.cliente.relatorio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import br.com.cflex.samarco.supervision.stockyard.relatorio.GeradorPDF;
import br.com.cflex.samarco.supervision.stockyard.relatorio.RelatorioPDF;
import br.com.cflex.samarco.supervision.stockyard.relatorio.controles.CabecalhoRelatorio;
import br.com.cflex.samarco.supervision.stockyard.relatorio.controles.Coluna;
import br.com.cflex.samarco.supervision.stockyard.relatorio.enums.CalculoColunaEnum;
import br.com.cflex.samarco.supervision.stockyard.relatorio.exception.ErroGeracaoRelatorioPDF;

import com.hdntec.gestao.cliente.util.numeros.DSSStockyardFuncoesNumeros;
import com.hdntec.gestao.cliente.util.texto.DSSStockyardFuncoesTexto;
import com.hdntec.gestao.domain.navios.entity.status.Carga;
import com.hdntec.gestao.domain.navios.entity.status.Navio;
import com.hdntec.gestao.domain.plano.comparadores.ComparadorSituacoesPatio;
import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
import com.hdntec.gestao.domain.plano.enums.TipoAtividadeEnum;
import com.hdntec.gestao.domain.relatorio.Relatorio;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.integracao.controladores.ControladorModelo;
import com.hdntec.gestao.integracao.controladores.IControladorModelo;
import com.hdntec.gestao.util.PropertiesUtil;
import com.hdntec.gestao.util.datahora.DSSStockyardTimeUtil;

/**
 * 
 * <P><B>Description :</B><BR>
 * General ControladorRelAtendimentoCarga
 * </P>
 * <P>
 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
 * @since 19/08/2009
 * @version $Revision: 1.1 $
 */
public class ControladorRelAtendimentoCarga {

	private final String SERVIDOR_RELATORIOS;	
	/**Guarda a qtd de meses para retroceder a data inicial do relatorio*/
	private int QTD_MESES_ATENDIMENTO_CARGA;
	
	private List<SituacaoPatio> situacaoPatioList;
	
	

	/**
	 * 
	 * @param controladorModelo
	 */
	public ControladorRelAtendimentoCarga() {		
		this.SERVIDOR_RELATORIOS = PropertiesUtil.buscarPropriedade("pasta.servidor.relatorios").trim();
		try {
			this.QTD_MESES_ATENDIMENTO_CARGA = Integer.valueOf( 
			PropertiesUtil.buscarPropriedade("qtd.meses.antendimento.carga"));
		}
		catch (NumberFormatException e) {
			this.QTD_MESES_ATENDIMENTO_CARGA = 2;
		}
		//isso para sempre manter a qtd de meses de atendimento retroativa.
		this.QTD_MESES_ATENDIMENTO_CARGA*=(-1);
	}
	
	
	/**
	 * 
	 * popularRelatorio
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 19/08/2009
	 * @see
	 * @param relatorio
	 * @throws IOException
	 * @throws ErroSistemicoException
	 * @return Returns the void.
	 */
	public void popularRelatorio(
			Relatorio relatorio) throws IOException, ErroSistemicoException {
		
		situacaoPatioList = new ArrayList<SituacaoPatio>();
		Date dataInicio = new Date(relatorio.getHorarioInicioRelatorio().getTime());//calcularDataInicio(
				//relatorio.getHorarioInicioRelatorio());
		//Darley removendo chamada remota
//		 IControladorModelo controladorModelo = InterfaceInicial.lookUpModelo();
		IControladorModelo controladorModelo = new ControladorModelo();
		situacaoPatioList =	controladorModelo.buscarListaSPOficialPorPeriodoEAtividade(
						dataInicio,
						relatorio.getHorarioFimRelatorio(),
						null);
		
		controladorModelo = null;
		List<SituacaoPatio> spRelatorioList = 
			filtrarSituacoesPorAtividadeSaidaDeNavio(situacaoPatioList,relatorio);
		
		if (spRelatorioList.size()>0) {
			gerarRelatorio(relatorio, spRelatorioList);
		}
		else {
			throw new ErroSistemicoException(PropertiesUtil.getMessage("relatorio.configurar.padrao.emptyreport"));
		}
		
	}
	
	/**
	 * 
	 * @param navio
	 * @return
	 */
	private boolean validarCargasNavio(Navio navio,Date inicio) {
		boolean result = false;
		for (Carga carga : navio.getListaDeCargasDoNavio(inicio)) {
			if (calcularDatasEmbarque(carga)) {
				result = true;
			}
		}
		return result;
	}
	
	/**
	 * 
	 * @param relatorio
	 * @param listSituacaoPatio
	 * @throws IOException
	 * @throws ErroSistemicoException
	 */
	private void gerarRelatorio(Relatorio relatorio, 
			List<SituacaoPatio> listSituacaoPatio) throws IOException, ErroSistemicoException {

		GeradorPDF pdf = new GeradorPDF(SERVIDOR_RELATORIOS, relatorio.getNomeRelatorio());
		ImageIcon logo = new ImageIcon(getClass().getResource("/images/logo_samarco.png"));
		String titulo = PropertiesUtil.getMessage("relatorio.cabecalho.title.atendimentoCarga");
		String msgPeriodo = PropertiesUtil.getMessage("relatorio.atendimentoCarga.periodo");
		String msgEntre = PropertiesUtil.getMessage("relatorio.atendimentoCarga.entrePeriodo"); 
		//String msgTipo = PropertiesUtil.getMessage("relatorio.atendimentoCarga.tipo");
		String msgNavio = PropertiesUtil.getMessage("relatorio.atendimentoCarga.navio");
		String msgSituacaoPatio = PropertiesUtil.getMessage("relatorio.atendimentoCarga.situacaoPatio");

		for (SituacaoPatio situacaoPatio : listSituacaoPatio) {
			
			Navio navio = situacaoPatio.getAtividade().getMovimentacaoNavio().getNavio();
			if (validarCargasNavio(navio,situacaoPatio.getDtInicio())) {
				RelatorioPDF relatorioPDF = new RelatorioPDF(titulo, "", logo.getImage());
				//Dados informados para o relatorio
				CabecalhoRelatorio cabecalho = relatorioPDF.getCabecalhoRelatorio();
				cabecalho.setQtdColunas(2);
				String dataHoraInicio = DSSStockyardTimeUtil.formatarData(relatorio.getHorarioInicioRelatorio(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
				String dataHoraFim = DSSStockyardTimeUtil.formatarData(relatorio.getHorarioFimRelatorio(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
				String dataSituacao = DSSStockyardTimeUtil.formatarData(situacaoPatio.getDtInicio(), PropertiesUtil.buscarPropriedade("formato.campo.datahora"));
				cabecalho.addColuna(msgPeriodo+": "+dataHoraInicio+" "+msgEntre+" "+dataHoraFim);
				cabecalho.addColuna("");
				//Dados do relatorio 
				cabecalho.addColuna(msgNavio+": " + navio.getNomeNavio());
				cabecalho.addColuna(msgSituacaoPatio+": "+dataSituacao);
				//Criando as colunas do relatorio
				adicionarColunasRelatorio(relatorioPDF);
				//Recuperando os dados e passando para o relatorio
				Map<Integer, List<Object>> dados = popularDadosRelatorio(
						navio.getListaDeCargasDoNavio(situacaoPatio.getDtInicio()));
				relatorioPDF.setDadosCorpoRelatorio(dados);
				pdf.addRelatorio(relatorioPDF);
			}
		}
		
		try
		{
			if (pdf.getRelatorios() != null && pdf.getRelatorios().size()>0) {
				pdf.gerarPDF();
			}
			else {
				throw new ErroSistemicoException(PropertiesUtil.getMessage("relatorio.configurar.padrao.emptyreport"));
			}
		}
		catch (ErroGeracaoRelatorioPDF e)
		{
			throw new ErroSistemicoException(e);
		}
	}


	private void adicionarColunasRelatorio(RelatorioPDF relatorio)
	{
		String msgCarga = PropertiesUtil.getMessage(
				"relatorio.atendimentoCarga.item.carga");
		String msgDataInicio = PropertiesUtil.getMessage(
				"relatorio.atendimentoCarga.item.dataInicio");
		String msgDataFim = PropertiesUtil.getMessage(
				"relatorio.atendimentoCarga.item.dataFim");
		String msgQuantidade = PropertiesUtil.getMessage(
				"relatorio.atendimentoCarga.item.quantidade");
		String msgTempoGlobal = PropertiesUtil.getMessage(
				"relatorio.atendimentoCarga.item.tempoGlobal");
		String msgTaxaGlobal = PropertiesUtil.getMessage(
				"relatorio.atendimentoCarga.item.taxaGlobal");
		String msgTempoEfetivo = PropertiesUtil.getMessage(
				"relatorio.atendimentoCarga.item.tempoEfetivo");
		String msgTaxaEfetiva = PropertiesUtil.getMessage(
				"relatorio.atendimentoCarga.item.taxaEfetiva");
		
		int ordem = 1;
		relatorio.addColuna(new Coluna(ordem++,"Grupo",0,
				CalculoColunaEnum.NAO_CALCULA,true,1,null,false));
		relatorio.addColuna(new Coluna(ordem++, msgCarga, 14, 
				CalculoColunaEnum.NAO_CALCULA, false, 0, null, false));
		relatorio.addColuna(new Coluna(ordem++, msgDataInicio, 13, 
				CalculoColunaEnum.NAO_CALCULA, false, 0,null, false));
		relatorio.addColuna(new Coluna(ordem++, msgDataFim, 13, 
				CalculoColunaEnum.NAO_CALCULA, false, 0,null, false));
		relatorio.addColuna(new Coluna(ordem++, msgQuantidade, 12, 
				CalculoColunaEnum.NAO_CALCULA, false, 0,null, false));
		relatorio.addColuna(new Coluna(ordem++, msgTempoGlobal, 12, 
				CalculoColunaEnum.NAO_CALCULA, false, 0,null, false));
		relatorio.addColuna(new Coluna(ordem++, msgTaxaGlobal, 12, 
				CalculoColunaEnum.NAO_CALCULA, false, 0,null, false));
		relatorio.addColuna(new Coluna(ordem++, msgTempoEfetivo, 12, 
				CalculoColunaEnum.NAO_CALCULA, false, 0,null, false));
		relatorio.addColuna(new Coluna(ordem++, msgTaxaEfetiva, 12, 
				CalculoColunaEnum.NAO_CALCULA, false, 0,null, false));
		relatorio.ajustarTamanhoColuna(ordem - 1);
	}

	/**
	 * 
	 * popularDadosRelatorio
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 19/08/2009
	 * @see
	 * @param listaCarga
	 * @return
	 * @return Returns the Map<Integer,List<Object>>.
	 */
	private Map<Integer, List<Object>> popularDadosRelatorio(List<Carga> listaCarga) {
		
		Integer registros = 1;
		Map<Integer, List<Object>> result = new HashMap<Integer, List<Object>>();
		
		
		for (Carga carga : listaCarga) {

			//Data de inicio de embarque e data final de embarque nao sao
			//setadas na carga. Esse metodo calcula essas datas.
			//if (calcularDatasEmbarque(carga)) {

				long tempoGlobal =
					DSSStockyardTimeUtil.subtraiDatas(carga.getDtFimEmbarque(),
							carga.getDtInicioEmbarque());
				String strTempoGlobal = "";
				double taxaGlobal = 0;
				if (tempoGlobal > 0) {
					strTempoGlobal = formataHoras(tempoGlobal);
					taxaGlobal = calculaTaxa(tempoGlobal,carga.getProduto().getQuantidade());
				}

				long tempoEfetivo = calcularTempoEfetivo(carga);
				String strTempoEfetivo = "";
				double taxaEfetiva = 0;
				if (tempoEfetivo > 0) {
					strTempoEfetivo = formataHoras(tempoEfetivo);
					taxaEfetiva = calculaTaxa(tempoEfetivo,carga.getProduto().getQuantidade());
				}

				List<Object> dadosList = new ArrayList<Object>();
				dadosList.add("grupo1");
				dadosList.add(carga.getIdentificadorCarga());
				dadosList.add(DSSStockyardTimeUtil.formatarData(carga.getDtInicioEmbarque(),
						PropertiesUtil.buscarPropriedade("formato.campo.datahora")));
				dadosList.add(DSSStockyardTimeUtil.formatarData(carga.getDtFimEmbarque(),
						PropertiesUtil.buscarPropriedade("formato.campo.datahora")));
				dadosList.add(DSSStockyardFuncoesNumeros.getValorFormatado2d(
						carga.getProduto().getQuantidade()));
				dadosList.add(strTempoGlobal);
				dadosList.add(DSSStockyardFuncoesNumeros.getValorFormatado2d(
						taxaGlobal));
				dadosList.add(strTempoEfetivo);
				dadosList.add(DSSStockyardFuncoesNumeros.getValorFormatado2d(taxaEfetiva));

				result.put(registros++, dadosList);
			}
		//}
		return result;
	}
	
	/**
	 * 
	 * calculaTaxa
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 27/08/2009
	 * @see
	 * @param tempo
	 * @param qtd
	 * @return
	 * @return Returns the double.
	 */
	private double calculaTaxa(long tempo, double qtd) {
		if (qtd<=0) {
			qtd=1;
		}
		long[] horaCalc = DSSStockyardTimeUtil.converteTempoEmHms(tempo);
		double calc = horaCalc[0]+((horaCalc[1])/60.00);
		return (qtd/calc);
	}
	
	/**
	 * 
	 * filtrarSituacoesPorAtividadeSaidaDeNavio
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 19/08/2009
	 * @see
	 * @param listaSituacaoPatio
	 * @return
	 * @return Returns the List<SituacaoPatio>.
	 */
	private List<SituacaoPatio> filtrarSituacoesPorAtividadeSaidaDeNavio(
			List<SituacaoPatio> listaSituacaoPatio, Relatorio relatorio) {
		
		List<SituacaoPatio> result = new ArrayList<SituacaoPatio>();
		for (SituacaoPatio situacao : listaSituacaoPatio) {
			
			if (validaSituacaoNoPeriodo(situacao,relatorio) &&
					ehAtividadeSaidaDeNavio(situacao)) {
				result.add(situacao);
			}
		}
		return result;
	}
	
	/**
	 * 
	 * ehAtividadeSaidaDeNavio
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 19/08/2009
	 * @see
	 * @param situacao
	 * @return
	 * @return Returns the boolean.
	 */
	private boolean ehAtividadeSaidaDeNavio(SituacaoPatio situacao) {
		boolean result = false;
		if (situacao.getAtividade()!= null) {
			result = TipoAtividadeEnum.SAIDA_DE_NAVIO.equals(
					situacao.getAtividade().getTipoAtividade());
		}
		return result;
	}
	
	/**
	 * 
	 * ehAtividadeRecuperacao
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 21/08/2009
	 * @see
	 * @param situacao
	 * @return
	 * @return Returns the boolean.
	 */
	private boolean ehAtividadeRecuperacao(SituacaoPatio situacao) {
		boolean result = false;
		if (situacao.getAtividade()!= null) {
			
			if  (situacao.getAtividade().getTipoAtividade().equals(TipoAtividadeEnum.ATUALIZACAO_RECUPERACAO) || situacao.getAtividade().atividadeDeNavio()) {
				result = true;
			}			
		}
		return result;
	}
	
	/**
	 * 
	 * validaSituacaoNoPeriodo
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 21/08/2009
	 * @see
	 * @param situacao
	 * @param relatorio
	 * @return
	 * @return Returns the boolean.
	 */
	private boolean validaSituacaoNoPeriodo(SituacaoPatio situacao, 
			Relatorio relatorio) {
		//É valido se dataIni maior ou igual a zero
		int dataIni = situacao.getDtInicio().compareTo(
				relatorio.getHorarioInicioRelatorio());
		//É valido se dataFim menor ou igual a zero
		int dataFim = situacao.getDtInicio().compareTo(
				relatorio.getHorarioFimRelatorio());
		return (dataIni>=0&&dataFim<=0);
	}
	
	/**
	 * 
	 * calcularTempoEfetivo
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 21/08/2009
	 * @see
	 * @param carga
	 * @return
	 * @return Returns the Date.
	 */
	private long calcularTempoEfetivo(Carga carga) {
		
		List<Long> somaDatasList = new ArrayList<Long>();
		for (SituacaoPatio situacao : situacaoPatioList) {
			if (situacao.getAtividade() != null) {
				if (ehAtividadeRecuperacao(situacao)
					&& comparaNaviosPorNomeEta(situacao.getAtividade().getCarga().getNavio(situacao.getDtInicio()), carga.getNavio(situacao.getDtInicio()))
					&& comparaCargasPorIdentificador(situacao.getAtividade().getCarga(), carga)) {
					
					long tempo = calcularDataEntity(situacao.getAtividade().getDtInicio(),situacao.getAtividade().getDtFim());
					if (tempo>0) {
						somaDatasList.add(tempo);
					}
				}
			}
		}
		return DSSStockyardTimeUtil.somaDatas(somaDatasList);
	}
	
	/**
	 * 
	 * calcularDataEntity
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 21/08/2009
	 * @see
	 * @param datas
	 * @return
	 * @return Returns the Date.
	 */
	private long calcularDataEntity(Date dataIni,Date dataFim ) {
		long result = 0;
			
			result = DSSStockyardTimeUtil.subtraiDatas(
					dataFim, dataIni);
		
		return result;
	}
	
	/**
	 * 
	 * formataHoras
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 21/08/2009
	 * @see
	 * @param tempo
	 * @return
	 * @return Returns the String.
	 */
	private String formataHoras(long tempo) {
		
		long[] horasTempoGlobal = 
			DSSStockyardTimeUtil.converteTempoEmHms(tempo);
		String result = 
			DSSStockyardFuncoesTexto.zerosStr(2, horasTempoGlobal[0])+":"+
			DSSStockyardFuncoesTexto.zerosStr(2, horasTempoGlobal[1])+":"+
			DSSStockyardFuncoesTexto.zerosStr(2, horasTempoGlobal[2]);
		return result;
	}
	
	/**
	 * Data de inicio de embarque e data final de embarque nao sao
	 * setadas na carga. Esse metodo calcula essas datas.
	 * @param carga
	 */
	private boolean calcularDatasEmbarque(Carga carga) {
		
		boolean result = false;
		//Ordenar a lista
		Collections.sort(situacaoPatioList, new ComparadorSituacoesPatio());
		
		//Recuperando a data de inicio de embarque
		for(SituacaoPatio situacao : situacaoPatioList) {
			System.out.println("situacao :" + situacao.getAtividade().getId());
			if ( ehAtividadeRecuperacao(situacao) 
				&& comparaNaviosPorNomeEta(situacao.getAtividade().getCarga().getNavio(situacao.getDtInicio()),carga.getNavio(situacao.getDtInicio()))
				&& comparaCargasPorIdentificador(situacao.getAtividade().getCarga(),carga)) {
					carga.setDtInicioEmbarque(situacao.getDtInicio());
					result = true;
					break;
				}
							
		}
		
		if (result) {
			//Recuperando a data final de embarque
			for(int i = situacaoPatioList.size()-1; i >= 0; i--) {
				SituacaoPatio situacao = situacaoPatioList.get(i);				
				System.out.println("situacao :" + situacao.getAtividade().getId());
				System.out.println("situacao indice:" + i);
				if (ehAtividadeRecuperacao(situacao)
						&& comparaNaviosPorNomeEta(situacao.getAtividade().getCarga().getNavio(situacao.getDtInicio()),carga.getNavio(situacao.getDtInicio()))
						&& comparaCargasPorIdentificador(situacao.getAtividade().getCarga(),carga)) {
						carga.setDtFimEmbarque(situacao.getDtInicio());
						  break;
					}
				
				}
		}		
		return result;		
	}
	
	/**
	 * 
	 * @param navioA
	 * @param navioB
	 * @return
	 */
	private boolean comparaNaviosPorNomeEta(Navio navioA, Navio navioB) {
		boolean result = false;
		if (navioA.getMetaNavio().equals(navioB.getMetaNavio())) { 				
			result = true;
		}
		return result;
	}

	/**
	 * 
	 * @param cargaA
	 * @param cargaB
	 * @return
	 */
	private boolean comparaCargasPorIdentificador(Carga cargaA, Carga cargaB) {
		boolean result = false;
		if (cargaA.getIdentificadorCarga().equals(
				cargaB.getIdentificadorCarga())) {
			result = true;
		}
		return result;
	}
	
}