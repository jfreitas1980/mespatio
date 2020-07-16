package com.hdntec.gestao.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

import com.hdntec.gestao.domain.integracao.IntegracaoParametros;
import com.hdntec.gestao.domain.navios.StatusNavioEnum;
import com.hdntec.gestao.domain.navios.entity.MetaCarga;
import com.hdntec.gestao.domain.navios.entity.MetaNavio;
import com.hdntec.gestao.domain.navios.entity.status.Carga;
import com.hdntec.gestao.domain.navios.entity.status.Cliente;
import com.hdntec.gestao.domain.navios.entity.status.Navio;
import com.hdntec.gestao.domain.navios.entity.status.OrientacaoDeEmbarque;
import com.hdntec.gestao.domain.plano.entity.PlanoEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
import com.hdntec.gestao.domain.plano.enums.TipoMaquinaEnum;
import com.hdntec.gestao.domain.planta.comparadores.ComparadorBalizas;
import com.hdntec.gestao.domain.planta.comparadores.ComparadorMetaBalizas;
import com.hdntec.gestao.domain.planta.entity.MetaBaliza;
import com.hdntec.gestao.domain.planta.entity.MetaBerco;
import com.hdntec.gestao.domain.planta.entity.MetaCarregadoraDeNavios;
import com.hdntec.gestao.domain.planta.entity.MetaCorreia;
import com.hdntec.gestao.domain.planta.entity.MetaFiltragem;
import com.hdntec.gestao.domain.planta.entity.MetaMaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.MetaPatio;
import com.hdntec.gestao.domain.planta.entity.MetaPier;
import com.hdntec.gestao.domain.planta.entity.MetaUsina;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.Berco;
import com.hdntec.gestao.domain.planta.entity.status.Campanha;
import com.hdntec.gestao.domain.planta.entity.status.CarregadoraDeNavios;
import com.hdntec.gestao.domain.planta.entity.status.Correia;
import com.hdntec.gestao.domain.planta.entity.status.Filtragem;
import com.hdntec.gestao.domain.planta.entity.status.MaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Patio;
import com.hdntec.gestao.domain.planta.entity.status.Pier;
import com.hdntec.gestao.domain.planta.entity.status.Pilha;
import com.hdntec.gestao.domain.planta.entity.status.Usina;
import com.hdntec.gestao.domain.planta.enums.EnumTipoBaliza;
import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;
import com.hdntec.gestao.domain.produto.entity.Amostra;
import com.hdntec.gestao.domain.produto.entity.ItemDeControle;
import com.hdntec.gestao.domain.produto.entity.ItemDeControleAmostra;
import com.hdntec.gestao.domain.produto.entity.ItemDeControleOrientacaoEmbarque;
import com.hdntec.gestao.domain.produto.entity.ItemDeControleQualidade;
import com.hdntec.gestao.domain.produto.entity.Produto;
import com.hdntec.gestao.domain.produto.entity.Qualidade;
import com.hdntec.gestao.domain.produto.entity.TipoItemCoeficiente;
import com.hdntec.gestao.domain.produto.entity.TipoItemDeControle;
import com.hdntec.gestao.domain.produto.entity.TipoItemRegraFarol;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.domain.produto.enums.EnumValorRegraFarol;
import com.hdntec.gestao.domain.produto.enums.TipoDeProdutoEnum;
import com.hdntec.gestao.domain.produto.enums.TipoItemControleEnum;
import com.hdntec.gestao.domain.relatorio.FaixaAmostragemFrequencia;
import com.hdntec.gestao.domain.relatorio.ItemAmostragemFrequencia;
import com.hdntec.gestao.domain.relatorio.TabelaAmostragemFrequencia;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.ValidacaoCampoException;


/**
 * <P>
 * <B>Description :</B><BR>
 * General GeradorModeloUtilCenarioCompleto
 * </P>
 * <P>
 * <B> SAs : <BR>
 * 8148 </B>
 * 
 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
 * @since 09/06/2010
 * @version $Revision: 1.1 $
 */
public class GeradorModeloUtilCenarioCompleto {

	private static final int FASEPROCESSOU3_710050020 = 710050020;

	public static final int FASEPROCESSOU1_480050020 = 480050020;

	public static final int FASEPROCESSOU2_550050020 = 550050020;

	public static final String NOME_PATIO_A = "Patio A";

	public static final String NOME_PATIO_B = "Patio B";

	public static final String NOME_PATIO_C = "Patio C";

	public static final String NOME_BERCO_Leste = "Berco leste";

	public static final String NOME_BERCO_Oeste = "Berco oeste";

	public static final String NOME_USINA_1 = "Usina 1";

	public static final String NOME_USINA_2 = "Usina 2";

	public static final String NOME_USINA_3 = "Usina 3";

	public static final String NOME_PIER = "Pier 1";

	public static final String NOME_CARREGADORA_DE_NAVIOS = "Carregadora de navios";

	public static final String NOME_CORREIA_1 = "C2";// 56TP01

	public static final String NOME_CORREIA_2 = "C1";// 56TP04

	public static final String NOME_CORREIA_3 = "09TP001";// 56TP06

	public static final String NOME_CORREIA_4 = "08TP009";// 56TP07

	public static final String NOME_CORREIA_09TP001 = "09TP001";

	public static final String NOME_CORREIA_08TP009 = "08TP009";

	public static final String NOME_EMPILHADEIRA_1 = "Empilhadeira 1"; // EMP 1

	public static final String NOME_EMPILHADEIRA_2 = "Empilhadeira 2"; // EMP 2

	public static final String EMPILHADEIRA_RECUPERADORA = "Empilhadeira Recuperadora 1";

	public static final String NOME_RECUPERADORA = "Recuperadora";

	public static final String DESC_PRODUTO_MB45 = "MB45";

	public static String NOME_BALIZA = "BALIZA_";

	public static final String NOME_CLIENTE = "Cflex-Iron_";

	public static final String NOME_NAVIO = "Navio-cielo_";

	public static String NOME_PILHA = "Pilha ";

	public static final String DESC_FILA_NAVIOS = "Fila de navios";

	public static final String NOME_PLANTA = "Planta";

	public static MetaPatio patio1;

	public static MetaPatio patio2;

	public static MetaPatio patio3;

	public static final String NOME_FILA_DE_NAVIOS = "fila de navios";

	public static final String NOME_AMOSTRA = "amostra_";
    
	public static final String[] itensControlePelotaFisico = { "+19mm",
			"-19+16", "-16+14", "-14+12,5 mm", "-12,5+9 mm", "-9+8 mm",
			"-8+6,3 mm", "-6,3mm", "- 6,3 + 5,0 mm", "- 5,0 + 0,5 mm",
			"-0,5mm", "-16,0mm+12,5mm", "-16+9mm", "-16+8mm", "DiamMed",
			"Relação 12,5/9", "TB+6,3", "AB-0,5", "C 16,0", "-200c16",
			"-150c16", "-100c16", "DPc16", "C 12,5", "-200c12", "-150c12",
			"-100c12", "DPc12", "H2O", "LTD+6,3", "LTD-0,5", "Porosidade",
			"GI", "DELTA P", "DR/DT", "Redutibilidade", "Bulk D." };
	
	
	public static final String[] itensControlePelotaFisicoDados = {
			"20;HH;LFU;0;2;0,1;0;decrescente;", "21;HH;LFU;0;10;1;0;decrescente;", "496;HH;LFU;5;30;1;0;crescente;",
			"22;HH;LFU;30;50;1;0;indiferente;", "23;HH;LFU;25;60;1;0;indiferente;", "24;HH;LFU;0,5;6;0,2;0;decrescente;",
			"25;HH;LFU;0,5;4;0,2;0;decrescente;", "26;HH;LFU;0,4;6;0,01;-0,4;decrescente;",
			"544;HH;LFU;0;2;0,01;0;decrescente;", "86;HH;LFU;0;4;0,02;0;decrescente;",
			"87;HH;LFU;0;1;0,1;0;decrescente;", "2022;HH;LFU;25;60;1;0;indiferente;", "27;HH;LFU;80;97;1;0;crescente;",
			"28;HH;LFU;70;100;1;0;crescente;", "595;HH;LFU;8;16;0,1;0;indiferente;",
			"2021;HH;LFU;0,4;3;0,1;-0,1;indiferente;", "29;HH;LFU;91;95;0,1;-0,2;crescente;",
			"30;HH;LFU;4;8;0,1;0,3;decrescente;", "31;HH;LFU;240;370;5;-14;crescente;", "32;HH;LFU;10;40;1;0;decrescente;",
			"33;HH;LFU;5;30;1;0;decrescente;", "257;HH;LFU;0;10;1;0;decrescente;", "34;HH;LFU;80;180;2;0;decrescente;",
			"35;HH;LFU;220;330;5;0;crescente;", "36;HH;LFU;0;30;1;0;decrescente;", "37;HH;LFU;0;20;1;0;decrescente;",
			"258;HH;LFU;0;10;1;0;decrescente;", "38;HH;LFU;100;200;5;0;decrescente;",
			"1;HH;LFU;0,5;4,8;0,05;0;indiferente;", "39;DD;LFU;70;90;1;0;crescente;",
			"42;DD;LFU;10;20;1;0;decrescente;", "43;DD;LFU;27;37;0,2;0;indiferente;",
			"44;DD;LFU;10;25;0,5;0;decrescente;", "46;DD;LFU;0;20;1;0;decrescente;",
			"48;DD;LFU;0,8;2;0,1;0;crescente;", "402;DD;LFU;65;87;1;0;crescente;",
			"198;DD;LFU;1,5;2,5;0,1;0;crescente;" };

	public static final String[] itensControlePelotaQuimico = { "Fe", "SiO2",
			"CaO", "MgO", "B2", "Si+Al", "kgCar/T", "C", "Mn", "FeO", "Al2O3",
			"P", "B4" };

	public static final String[] itensControlePelotaQuimicoDados = {
			"2;HH;LQU;64,5;68,5;0,1;0;Crescente;", "4;HH;LQU;1;3,5;0,01;0;decrescente;",
			"6;HH;LQU;2,5;0,5;0,05;0;indiferente;", "7;HH;LQU;0,05;0,35;0,01;0;indiferente;",
			"18;HH;LQU;0,25;1,2;0,01;0;indiferente;", "122;HH;LQU;1,4;4,3;0,05;0;indiferente;",
			"84;HH;LQU;10;25;1;0;indiferente;", "493;HH;LQU;0,5;2;0,02;0;indiferente;",
			"115;HH;LQU;0,04;0,07;0,001;0;decrescente;", "3;DD;LQU;0;2;0,02;0;decrescente;",
			"5;DD;LQU;0,35;0,75;0,01;0;decrescente;", "8;DD;LQU;0,03;0,055;0,001;0;decrescente;",
			"19;DD;LQU;0,6;0,85;0,01;0;indiferente;" };

	public static final String[] itensControlePelletQuimico = { "PPC", "%C Ret" };
	public static final String[] itensControlePelletQuimicoDados = {
			"13;DD;LQU;0;1;0,1;0;", "625;HH;LQU;0;1;0,1;0;" };

	public static final String[] itensControlePelletFisico = { "SE", "+200#",
			"-325#" };

	public static final String[] itensControlePelletFisicoDados = {
			"17;HH;LFU;0;1;0,1;0;", "62;HH;LFU;0;1;0,1;0;",
			"16;HH;LFU;0;1;0,1;0;" };

	// Constantes referentes a Baliza
	public static final int LARGURA_BALIZA = 10;

	public static final int MAXIMA_CAPACIDADE_BALIZA = 8000;

	private static Random ran = new Random();

	private static final int NUMERO_SITUACOES_PATIO = 1;

	public static final String NOME_CAMPANHA = "Pelotas ";

	private static List<TipoItemDeControle> listaTiposItensControle = new ArrayList<TipoItemDeControle>();

	private static final String IDENTIFICADOR_CARGA = "Carga ";

	private static List<Cliente> cliente;

	private static IntegracaoParametros integracaoParametros;

	// Constantes que define a Norma de Amostragem e frequencia cadastrada
	public static final String CODIGO_NORMA = "U-DEO - 0 - Q 05";
	public static final int REVISAO_NORMA = 1;
	public static final String CLASSIFICACAO_NORMA = "Reservada";
	public static final String DATA_NORMA = "12/11/2008";

	/**
	 * associarPatioUsinaExpurgoPellet
	 * 
	 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
	 * @since 09/06/2010
	 * @see
	 * @param
	 * @return void
	 */
	private static void associarPatioUsinaExpurgoPellet(MetaPatio patio,
			MetaUsina usina) {
		if (patio.getListaDeMetaUsinasExpurgoPelletScreening() == null) {
			patio
					.setListaDeMetaUsinasExpurgoPelletScreening(new ArrayList<MetaUsina>());
		}
		patio.getListaDeMetaUsinasExpurgoPelletScreening().add(usina);
	}

	/**
	 * criaCampanha
	 * 
	 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
	 * @since 14/06/2010
	 * @see
	 * @param
	 * @return Campanha
	 */
	public static Campanha criaCampanha(TipoProduto tipoProduto,
			TipoProduto tipoPellet, TipoProduto tipoScreening,
			MetaUsina metaUsina, long timeDefault, Long faseProcessoPelota,
			Long faseProcessoPellet, Long faseProcessoPelletQuimico,
			Long faseProcessoPelletFisico, String tipoProcessoPellet,
			String tipoProcessoPelota, String areaPelota, String areaPellet,
			Long tipoItemPelota, Long tipoItemPellet) {
		// cria objeto campanha e seta seus atributos

		Campanha campanha = new Campanha();
		campanha.setNomeCampanha(NOME_CAMPANHA + metaUsina.getNomeUsina());
		campanha.setIdUser(1L);
		campanha.setDtInsert(new Date(timeDefault));
		campanha.setDataInicial(new Date(timeDefault));
		campanha.setDataFinal(new Date("30/07/2010"));
		campanha.setQuantidadePrevista(new Double(100000));

		/***
		 * TODO VALIDAR ATRIBUTOS DA CAMPANHA
		 * 
		 */
		campanha.setCodigoFaseProcessoPelletFeed(faseProcessoPellet);
		campanha.setCodigoFaseProcessoPelletFeedQAQ(faseProcessoPelletQuimico);
		campanha.setCodigoFaseProcessoPelletFeedQAF(faseProcessoPelletFisico);
		campanha.setCodigoFaseProcessoPelota(faseProcessoPelota);
		campanha.setTipoProcessoPelletFeed(tipoProcessoPellet);
		campanha.setTipoProcessoPelota(tipoProcessoPelota);

		campanha.setAreaRespEDPelletFeed(areaPellet);
		campanha.setAreaRespEDPelota(areaPelota);

		campanha.setCdTipoItemControlePelletFeed(tipoItemPellet);
		campanha.setCdTipoItemControlePelota(tipoItemPelota);

		campanha.setTipoProduto(tipoProduto);
		campanha.setTipoPellet(tipoPellet);
		campanha.setTipoScreening(tipoScreening);

		/***
		 * TODO CRIAR QUALIDADE ESTIMADA
		 * 
		 */
		 campanha.setQualidadeEstimada(criaQualidadeProduto(Boolean.FALSE, campanha.getNomeCampanha()));
		return campanha;
	}

	/**
	 * criaCliente
	 * 
	 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
	 * @since 09/06/2010
	 * @see
	 * @param
	 * @return List<Cliente>
	 */
	public static List<Cliente> criaCliente() {
		List<Cliente> lst = new ArrayList<Cliente>();
		for (int i = 0; i < 30; i++) {
			Cliente cliente1 = new Cliente();
			cliente1.setNomeCliente(NOME_CLIENTE + i);
			cliente1.setTaxaDeEstadia(new Double("1000"));
			cliente1.setCodigoCliente("C" + i);
			cliente1.setDtInsert(new Date(System.currentTimeMillis()));
			cliente1.setIdUser(1L);
			lst.add(cliente1);
		}

		return lst;
	}

	/**
	 * criaCorreia
	 * 
	 * @param patioSuperior
	 * @param patioInferior
	 * @param nomeCorreia
	 * @param numeroCorreia
	 * @return
	 */
	private static MetaCorreia criaCorreia(MetaPatio patioSuperior,
			MetaPatio patioInferior, String nomeCorreia, Integer numeroCorreia,
			long timeDefault) {
		MetaCorreia correia = new MetaCorreia();
		correia.setNumero(numeroCorreia);
		correia.setNomeCorreia(nomeCorreia);
		correia.setMetaPatioInferior(patioInferior);
		correia.setMetaPatioSuperior(patioSuperior);
		correia.setDtInsert(new Date(timeDefault));
		correia.setIdUser(1L);
		Correia correiaStatus = new Correia();
		correiaStatus.setEstado(EstadoMaquinaEnum.OCIOSA);
		correiaStatus.setTaxaDeOperacao(new Double(7000));
		correiaStatus.setDtInsert(new Date(timeDefault));
		correiaStatus.setIdUser(1L);

		correia.incluirNovoStatus(correiaStatus, new Date(timeDefault));

		return correia;
	}

	/**
	 * criaCorreiaComEmpilhadeira
	 * 
	 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
	 * @since 09/06/2010
	 * @see
	 * @param
	 * @return MetaMaquinaDoPatio
	 */
	private static MetaMaquinaDoPatio criaCorreiaComEmpilhadeira(
			MetaCorreia correia, String nomeEmpilhadeira,
			Boolean bracoPatioInferior, long timeDefault) {
		MetaMaquinaDoPatio maquina;
		MetaBaliza baliza;
		if (correia.getMetaPatioSuperior() != null) {
			baliza = correia.getMetaPatioSuperior().getListaDeMetaBalizas()
					.get(20);
		} else {
			baliza = correia.getMetaPatioInferior().getListaDeMetaBalizas()
					.get(50);
		}

		maquina = criaEmpilhadeira(nomeEmpilhadeira, baliza, correia,
				bracoPatioInferior, timeDefault);

		correia.addMetaMaquinaDoPatio(maquina);

		return maquina;
	}

	/**
	 * criaCorreiaComEmpilhadeiraRecuperadora
	 * 
	 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
	 * @since 09/06/2010
	 * @see
	 * @param
	 * @return MetaMaquinaDoPatio
	 */
	private static MetaMaquinaDoPatio criaCorreiaComEmpilhadeiraRecuperadora(
			MetaCorreia correia, String nomeEmpilhadeiraRecuperadora,
			Boolean bracoNoPatioInferior, long timeDefault) {
		MetaMaquinaDoPatio maquina;
		MetaBaliza baliza;
		if (correia.getMetaPatioSuperior() != null) {
			baliza = correia.getMetaPatioSuperior().getListaDeMetaBalizas()
					.get(40);
		} else {
			baliza = correia.getMetaPatioInferior().getListaDeMetaBalizas()
					.get(30);
		}

		maquina = criaEmpilhadeiraRecuperadora(nomeEmpilhadeiraRecuperadora,
				baliza, correia, bracoNoPatioInferior, timeDefault);

		correia.addMetaMaquinaDoPatio(maquina);

		return maquina;
	}

	/**
	 * criaCorreiaComRecuperadora
	 * 
	 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
	 * @since 09/06/2010
	 * @see
	 * @param
	 * @return MetaMaquinaDoPatio
	 */
	private static MetaMaquinaDoPatio criaCorreiaComRecuperadora(
			MetaCorreia correia, String nomeRecuperadora,
			Boolean bracoNoPatioInferior, long timeDefault) {
		MetaMaquinaDoPatio maquina;
		MetaBaliza baliza;
		if (correia.getMetaPatioSuperior() != null) {
			baliza = correia.getMetaPatioSuperior().getListaDeMetaBalizas()
					.get(80);
		} else {
			baliza = correia.getMetaPatioSuperior().getListaDeMetaBalizas()
					.get(100);
		}

		maquina = criaRecuperadora(nomeRecuperadora, baliza, correia,
				bracoNoPatioInferior, timeDefault);

		correia.addMetaMaquinaDoPatio(maquina);

		return maquina;
	}

	/**
	 * criaEmpilhadeira
	 * 
	 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
	 * @since 09/06/2010
	 * @see
	 * @param
	 * @return MetaMaquinaDoPatio
	 */
	private static MetaMaquinaDoPatio criaEmpilhadeira(String nomeEmpilhadeira,
			MetaBaliza baliza, MetaCorreia correia, Boolean bracoPatioInferior,
			long timeDefault) {
		MetaMaquinaDoPatio empilhadeira = new MetaMaquinaDoPatio();
		empilhadeira.setNomeMaquina(nomeEmpilhadeira);
		empilhadeira.setIdUser(1L);
		empilhadeira.setDtInsert(new Date(timeDefault));
		empilhadeira.setTipoDaMaquina(TipoMaquinaEnum.EMPILHADEIRA);
		empilhadeira.setGiraLanca(Boolean.FALSE);
		empilhadeira.setMetaCorreia(correia);

		MaquinaDoPatio maquina = new MaquinaDoPatio();

		maquina.setEstado(EstadoMaquinaEnum.OCIOSA);
		maquina.setVelocidadeDeslocamento(new Double(10));
		maquina.setTaxaDeOperacaoNominal(new Double(2000));
		maquina.setProximaManutencao(new Date());
		maquina.setBracoNoPatioInferior(bracoPatioInferior);
		maquina.setPosicao(baliza.retornaStatusHorario(new Date(timeDefault)));
		maquina.setPatio(baliza.getMetaPatio().retornaStatusHorario(
				new Date(timeDefault)));
		maquina.setIdUser(1L);
		maquina.setDtInsert(new Date(timeDefault));

		empilhadeira.incluirNovoStatus(maquina, new Date(timeDefault));

		return empilhadeira;
	}

	/**
	 * criaEmpilhadeiraRecuperadora
	 * 
	 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
	 * @since 09/06/2010
	 * @see
	 * @param
	 * @return MetaMaquinaDoPatio
	 */
	private static MetaMaquinaDoPatio criaEmpilhadeiraRecuperadora(
			String nomeEmpilhadeiraRecuperadora, MetaBaliza baliza,
			MetaCorreia correia, Boolean bracoPatioInferior, long timeDefault) {
		MetaMaquinaDoPatio empilhadeiraRecuperadora = new MetaMaquinaDoPatio();
		empilhadeiraRecuperadora.setNomeMaquina(nomeEmpilhadeiraRecuperadora);
		empilhadeiraRecuperadora.setTagPimsBalanca("309TP003-WQI004-R");
		empilhadeiraRecuperadora
				.setTipoDaMaquina(TipoMaquinaEnum.EMPILHADEIRA_RECUPERADORA);
		empilhadeiraRecuperadora.setGiraLanca(Boolean.TRUE);
		empilhadeiraRecuperadora.setMetaCorreia(correia);

		empilhadeiraRecuperadora.setIdUser(1L);
		empilhadeiraRecuperadora.setDtInsert(new Date(timeDefault));

		MaquinaDoPatio maquina = new MaquinaDoPatio();

		maquina.setEstado(EstadoMaquinaEnum.OCIOSA);
		maquina.setVelocidadeDeslocamento(new Double(10));
		maquina.setTaxaDeOperacaoNominal(new Double(7000));
		maquina.setProximaManutencao(new Date());
		maquina.setPosicao(baliza.retornaStatusHorario(new Date(timeDefault)));
		maquina.setPatio(baliza.getMetaPatio().retornaStatusHorario(
				new Date(timeDefault)));

		maquina.setBracoNoPatioInferior(bracoPatioInferior);
		maquina.setIdUser(1L);
		maquina.setDtInsert(new Date(timeDefault));

		empilhadeiraRecuperadora.incluirNovoStatus(maquina, new Date(
				timeDefault));

		return empilhadeiraRecuperadora;
	}

	private static ItemDeControle criaItemDeControleAmostra(
			TipoItemDeControle tipoItemDeControle, double valor) {
		// item de controle
		ItemDeControle itemControle = new ItemDeControleAmostra();
		itemControle.setIdUser(1l);
		itemControle.setDtInicio(new Date(System.currentTimeMillis()));
		itemControle.setValor(valor);
		itemControle.setTipoItemControle(tipoItemDeControle);
		// itemControle.setDesvioPadraoValor(new Double(valor * 0.1));

		return itemControle;
	}

	private static ItemDeControle criaItemDeControleComValorVazio(
			TipoItemDeControle tipoItemDeControle) {
		// item de controle
		ItemDeControle itemControle = new ItemDeControleQualidade();
		itemControle.setIdUser(1l);
		itemControle.setDtInicio(new Date(System.currentTimeMillis()));
		itemControle.setTipoItemControle(tipoItemDeControle);

		return itemControle;
	}

	private static ItemDeControle criaItemDeControleEmbarcado(
			TipoItemDeControle tipoItemDeControle, double valor,
			double embarcado) {
		// item de controle
		ItemDeControle itemControle = new ItemDeControleAmostra();
		itemControle.setTipoItemControle(tipoItemDeControle);
		itemControle.setValor(valor);
		itemControle.setIdUser(1l);
		itemControle.setDtInicio(new Date(System.currentTimeMillis()));
		// itemControle.setDesvioPadraoValor(new Double(valor * 0.1));
		itemControle.setEmbarcado(embarcado);
		// itemControle.setDesvioPadraoEmbarcado(new Double(embarcado * 0.1));

		return itemControle;
	}

	private static ItemDeControle criaItemDeControleParaOrientacaoDeEmbarque(
			TipoItemDeControle tipoItemDeControle, double valorInferior,
			double valorSuperior) {
		// item de controle
		ItemDeControle itemControle = new ItemDeControleOrientacaoEmbarque();
		itemControle.setIdUser(1l);
		itemControle.setDtInicio(new Date(System.currentTimeMillis()));
		itemControle.setTipoItemControle(tipoItemDeControle);
		itemControle.setLimInfMetaOrientacaoEmb(valorInferior);
		itemControle.setLimSupMetaOrientacaoEmb(valorSuperior);

		return itemControle;
	}

	/**
	 * criaListaBalizasParaPilha
	 * 
	 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
	 * @since 10/06/2010
	 * @see
	 * @param
	 * @return List<Baliza>
	 */
	private static List<Baliza> criaListaBalizasParaPilha(
			List<TipoProduto> tipoProdutos, List<MetaBaliza> balizas, int from,
			int to, Pilha pilha, long timeDefault) {
		List<Baliza> listaBaliza = new ArrayList<Baliza>();
		TipoProduto tipoProduto = tipoProdutos.get(ran.nextInt(tipoProdutos
				.size() - 1));

		for (int i = from; i <= to; i++) {
			Baliza baliza = balizas.get(i - 1).retornaStatusHorario(
					new Date(timeDefault));
			baliza.setProduto(criaProduto(tipoProduto, baliza.getDtInicio()
					.getTime()));
			baliza.getProduto().setQuantidade(1000D);
			baliza.setEstado(EstadoMaquinaEnum.OCIOSA);
			// baliza.addPilha(pilha);
			baliza.setHorarioInicioFormacao(new Date(timeDefault));
			baliza.setHorarioFimFormacao(new Date(timeDefault));
			listaBaliza.add(baliza);
		}
		Collections.sort(listaBaliza, new ComparadorBalizas());
		return listaBaliza;
	}

	/**
	 * criaListaCargasNavio
	 * 
	 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
	 * @since 23/06/2010
	 * @see
	 * @param
	 * @return List<MetaCarga>
	 */
	private static List<MetaCarga> criaListaCargasNavio(MetaNavio metaNavio,
			List<TipoProduto> tipoProduto, long timeDefault, Cliente cliente) {

		List<MetaCarga> listaCargas = new ArrayList<MetaCarga>();

		for (int i = 0; i < 5; i++) {
			MetaCarga metaCarga = new MetaCarga();
			metaCarga.setDtInsert(new Date(timeDefault));
			metaCarga.setIdUser(1L);
			metaCarga.setIdentificadorCarga(IDENTIFICADOR_CARGA
					+ i
					+ (metaNavio.retornaStatusHorario(new Date(timeDefault)))
							.getNomeNavio());

			Carga carga = new Carga();
			// adicionando nome do identificador
			
			carga.setIdUser(1L);
			// Criando objeto produto para carga

			Produto p = Produto.criaProduto(tipoProduto.get(i), timeDefault);
			p.setQuantidade(new Double(1000));

			 p.setQualidade(criaQualidadeProdutoEmbarcado(Boolean.TRUE,"Qualidade_navio1"));
			// setando o produto a carga
			carga.setProduto(p);

			
			 OrientacaoDeEmbarque orientacaoDeEmbarque = new OrientacaoDeEmbarque(); 
			 orientacaoDeEmbarque.setIdUser(1L);
			 orientacaoDeEmbarque.setDtInsert(new Date(System.currentTimeMillis()));
			 orientacaoDeEmbarque.setDtInicio(new Date(System.currentTimeMillis()));
			 orientacaoDeEmbarque.setCarga(carga);
			 orientacaoDeEmbarque.setTipoProduto(p.getTipoProduto());			 
			 orientacaoDeEmbarque.setPenalizacao(true); 
			 orientacaoDeEmbarque.setListaItemDeControle(criaListaDeItensDeControleDaOrientacaoDeEmbarqueDoProduto());
			 orientacaoDeEmbarque.setQuantidadeNecessaria(new Double(25000));
			  
			 carga.setOrientacaoDeEmbarque(orientacaoDeEmbarque);
			 

			metaCarga.incluirNovoStatus(carga, new Date(timeDefault));

			listaCargas.add(metaCarga);
		}
		return listaCargas;
	}

	/**
	 * criaListaCorreias
	 * 
	 * @param listaPatio
	 * @return
	 */
	public static List<MetaCorreia> criaListaCorreias(
			List<MetaPatio> listaPatio, long timeDefault) {
		List<MetaCorreia> listaCorreia = new ArrayList<MetaCorreia>();
		// criando correias

		MetaCorreia correia1 = criaCorreia(null, listaPatio.get(0),
				NOME_CORREIA_1, new Integer(1), timeDefault);
		MetaCorreia correia2 = criaCorreia(listaPatio.get(0),
				listaPatio.get(1), NOME_CORREIA_2, new Integer(2), timeDefault);
		MetaCorreia correia3 = criaCorreia(listaPatio.get(1),
				listaPatio.get(2), NOME_CORREIA_3, new Integer(3), timeDefault);
		MetaCorreia correia4 = criaCorreia(listaPatio.get(2), null,
				NOME_CORREIA_4, new Integer(4), timeDefault);

		// adicionando as correias a lista de correias
		listaCorreia.add(correia1);
		listaCorreia.add(correia2);
		listaCorreia.add(correia3);
		listaCorreia.add(correia4);

		// adicionando as maquinas nas correias
		List<MetaMaquinaDoPatio> maquinas = criarListaMaquinasCorrerias(
				timeDefault, listaCorreia);

		return listaCorreia;
	}

	/**
	 * criaListaDeBalizas
	 * 
	 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
	 * @since 09/06/2010
	 * @see
	 * @param
	 * @return List<MetaBaliza>
	 */
	public static List<MetaBaliza> criaListaDeBalizas(int numeroDeBalizas,
			MetaPatio patio, long timeDefault) {
		// Cria objeto lista de balizas
		List<MetaBaliza> listaBalizas = new ArrayList<MetaBaliza>();
		// cria Objeto baliza
		MetaBaliza baliza;
		for (int i = 1; i <= numeroDeBalizas; i++) {
			baliza = new MetaBaliza();
			baliza.setNomeBaliza(NOME_BALIZA + i);
			baliza.setCapacidadeMaxima(8000D);
			baliza.setDtInsert(new Date(timeDefault));
			baliza.setIdUser(1L);
			/**
			 * coloca na baliza seu tipo especifico, no patio 1 as primeiras 10
			 * balizas sao do tipo de emergencia e da 11 a 20 sao do tipo
			 * especial, sendo o restante normal. No patio 2 e 3 as primeira 10
			 * balizas sao especiais e o restante normal.
			 */
			if (patio.getNomePatio().equals(patio1.getNomePatio())) {
				if (i <= 5) {
					baliza.setTipoBaliza(EnumTipoBaliza.EMERGENCIA_P5);
				} else if (i >= 6 && i <= 10) {
					baliza.setTipoBaliza(EnumTipoBaliza.EMERGENCIA_TP17);
				} else {
					if (i <= 30) {
						baliza.setTipoBaliza(EnumTipoBaliza.PELLET_SCREENING);
					} else if (i <= 100) {
						baliza.setTipoBaliza(EnumTipoBaliza.NORMAL);
					} else {
						baliza.setTipoBaliza(EnumTipoBaliza.PSM);
					}
				}
			} else {
				if (i <= 10) {
					if (patio.getNomePatio().equals(patio3.getNomePatio())) {
					    baliza.setTipoBaliza(EnumTipoBaliza.EMERGENCIA_TP009);
					} else {
					    baliza.setTipoBaliza(EnumTipoBaliza.NORMAL);     
					}
				/*} else if (i >= 6 && i <= 10) {
					baliza.setTipoBaliza(EnumTipoBaliza.EMERGENCIA_TP17);*/
				} else if (i <= 30) {
					baliza.setTipoBaliza(EnumTipoBaliza.PELLET_SCREENING);
				} else {
					baliza.setTipoBaliza(EnumTipoBaliza.NORMAL);
				}
			}
			baliza.setLargura(new Double(LARGURA_BALIZA));
			baliza.setNumero(new Integer(i));
			baliza.setCapacidadeMaxima(new Double(MAXIMA_CAPACIDADE_BALIZA));

			Baliza balizaStatus = new Baliza();
			balizaStatus.setDtInsert(new Date(timeDefault));
			balizaStatus.setIdUser(1L);
			balizaStatus.setEstado(EstadoMaquinaEnum.OCIOSA);
			baliza.incluirNovoStatus(balizaStatus, new Date(timeDefault));
			// Adicionando o objeto baliza a lista de balizas
			listaBalizas.add(baliza);
		}
		Collections.sort(listaBalizas, new ComparadorMetaBalizas());
		patio.addBaliza(listaBalizas);
		return listaBalizas;
	}

	private static List<ItemDeControle> criaListaDeItensDeControleComValoresVazios() {
		List<ItemDeControle> listaDeItensDeControleComValoresVazio = new ArrayList<ItemDeControle>();
		for (TipoItemDeControle tipoItemDeControle : listaTiposItensControle) {
			if (tipoItemDeControle.getRelevante()) {
				listaDeItensDeControleComValoresVazio
						.add(criaItemDeControleComValorVazio(tipoItemDeControle));
			}
		}
		return listaDeItensDeControleComValoresVazio;
	}

	/**
	 * criaListaDeNavios
	 * 
	 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
	 * @since 10/06/2010
	 * @see
	 * @param
	 * @return List<MetaNavio>
	 */
	public static List<MetaNavio> criaListaDeNavios(List<Cliente> clientes,
			long timeDefault, List<TipoProduto> lstProduto)
			throws ErroSistemicoException {
		List<MetaNavio> listaDeNavios = new ArrayList<MetaNavio>();
        int i = 0;
		for (Cliente cliente : clientes) {
            if (i > 10) break;
		    MetaNavio meta = new MetaNavio();
			meta.setIdUser(1L);
			meta.setDtInsert(new Date(timeDefault));
			meta.setSap_vbap_vbeln("CODIGOSAP_" + cliente);

			Navio navio = new Navio();
			navio.setNomeNavio(NOME_NAVIO + cliente);
			navio.setDiaDeChegada(new Date());
			navio.setDiaDeSaida(new Date());
			navio.setDwt(new Double("170000"));
			navio.setStatusEmbarque(StatusNavioEnum.CONFIRMADO); // Status
			// Confirmado
			navio.setEta(new Date());
			navio.setDataEmbarque(new Date());
			navio.setDtInsert(new Date(timeDefault));
			navio.setIdUser(1L);

			meta.incluirNovoStatus(navio, new Date(timeDefault));
			navio.setCliente(cliente);

			meta.addMetaCarga(criaListaCargasNavio(meta, lstProduto,
					timeDefault, cliente));

			// adiciona objeto a lista
			listaDeNavios.add(meta);
			i++;
		}

		return listaDeNavios;
	}

	/**
	 * criaListaDePilhas
	 * 
	 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
	 * @since 10/06/2010
	 * @see
	 * @param
	 * @return List<Pilha>
	 */
	public static List<Pilha> criaListaDePilhas(List<MetaPatio> patios,
			Cliente cliente, List<TipoProduto> tipoProduto, long timeDefault) {
		// cria objeto lista de pilhas
		List<Pilha> listaPilha = new ArrayList<Pilha>();
		// Pilhas do Patio 1

		listaPilha.add(criaPilha(NOME_PILHA + 1, patios.get(0)
				.getListaDeMetaBalizas(), 25, 30, cliente, tipoProduto,
				timeDefault));
		listaPilha.add(criaPilha(NOME_PILHA + 2, patios.get(0)
				.getListaDeMetaBalizas(), 35, 50, cliente, tipoProduto,
				timeDefault));
		listaPilha.add(criaPilha(NOME_PILHA + 3, patios.get(0)
				.getListaDeMetaBalizas(), 60, 80, cliente, tipoProduto,
				timeDefault));
		listaPilha.add(criaPilha(NOME_PILHA + 4, patios.get(0)
				.getListaDeMetaBalizas(), 81, 90, cliente, tipoProduto,
				timeDefault));
		listaPilha.add(criaPilha(NOME_PILHA + 5, patios.get(0)
				.getListaDeMetaBalizas(), 92, 99, cliente, tipoProduto,
				timeDefault));
		listaPilha.add(criaPilha(NOME_PILHA + 7, patios.get(0)
				.getListaDeMetaBalizas(), 100, 110, cliente, tipoProduto,
				timeDefault));

		// Pilhas do Patio 2
		listaPilha.add(criaPilha(NOME_PILHA + 3, patios.get(1)
				.getListaDeMetaBalizas(), 25, 30, cliente, tipoProduto,
				timeDefault));
		listaPilha.add(criaPilha(NOME_PILHA + 6, patios.get(1)
				.getListaDeMetaBalizas(), 32, 35, cliente, tipoProduto,
				timeDefault));
		listaPilha.add(criaPilha(NOME_PILHA + 8, patios.get(1)
				.getListaDeMetaBalizas(), 50, 70, cliente, tipoProduto,
				timeDefault));
		listaPilha.add(criaPilha(NOME_PILHA + 7, patios.get(1)
				.getListaDeMetaBalizas(), 80, 100, cliente, tipoProduto,
				timeDefault));
		listaPilha.add(criaPilha(NOME_PILHA + 4, patios.get(1)
				.getListaDeMetaBalizas(), 101, 120, cliente, tipoProduto,
				timeDefault));

		// Pilha do patio 3
		listaPilha.add(criaPilha(NOME_PILHA + 9, patios.get(2)
				.getListaDeMetaBalizas(), 33, 49, cliente, tipoProduto,
				timeDefault));
		listaPilha.add(criaPilha(NOME_PILHA + 8, patios.get(2)
				.getListaDeMetaBalizas(), 50, 60, cliente, tipoProduto,
				timeDefault));
		listaPilha.add(criaPilha(NOME_PILHA + 10, patios.get(2)
				.getListaDeMetaBalizas(), 61, 80, cliente, tipoProduto,
				timeDefault));
		listaPilha.add(criaPilha(NOME_PILHA + 11, patios.get(2)
				.getListaDeMetaBalizas(), 90, 110, cliente, tipoProduto,
				timeDefault));
		listaPilha.add(criaPilha(NOME_PILHA + 12, patios.get(2)
				.getListaDeMetaBalizas(), 115, 130, cliente, tipoProduto,
				timeDefault));

		return listaPilha;
	}


	/**
	 * criaListaUsinas
	 * 
	 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
	 * @since 09/06/2010
	 * @see
	 * @param
	 * @return List<MetaUsina>
	 */
	public static List<MetaUsina> criaListaUsinas(long timeDefault,
			List<MetaPatio> patios,List<MetaFiltragem> filtragens,List<MetaCorreia> listaCorreia) {
		List<MetaUsina> listaDeUsinas = new ArrayList<MetaUsina>();
		
		MetaCorreia metaCorreia = null;
		for (MetaCorreia correia : listaCorreia) {
            if (correia.getNomeCorreia().equals(NOME_CORREIA_2)) {
                metaCorreia = correia;
                break;
            }
		}    
		
		for (MetaPatio patio : patios) {
	        MetaUsina usina = null;
		    if (patio.getNomePatio().equals(patio1.getNomePatio())) {
		        usina = criaUsina(NOME_USINA_1, patio,
		                        FASEPROCESSOU1_480050020, timeDefault);

		        usina.setFiltragemOrigem(filtragens.get(0));

		       
		        // Adiciona o objeto usina 1 a lista de usinas patio expurgo 1
		        listaDeUsinas.add(usina);

		        usina = criaUsina(NOME_USINA_2, patio,
		                        FASEPROCESSOU2_550050020, timeDefault);
		                usina.setFiltragemOrigem(filtragens.get(0));
		                
		                // Adiciona o objeto usina 2 a lista de usinas patio expurgo 1
		       
		        listaDeUsinas.add(usina);
            } else if (patio.getNomePatio().equals(patio3.getNomePatio())) {

                usina = criaUsina(NOME_USINA_3, patios.get(2),
                        FASEPROCESSOU3_710050020, timeDefault);


                
                usina.setFiltragemOrigem(filtragens.get(1));
                // Adiciona o objeto usina 2 a lista de usinas patio expurgo 3
                listaDeUsinas.add(usina);

            }
		    metaCorreia.addMetaUsina(listaDeUsinas);
		}

		return listaDeUsinas;

	}

	/**
	 * criaPaCarregadeira
	 * 
	 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
	 * @since 09/06/2010
	 * @see
	 * @param
	 * @return MetaMaquinaDoPatio
	 */
	public static MetaMaquinaDoPatio criaPaCarregadeira(
			String nomePaCarregadeira, MetaPatio patio, long timeDefault) {
		MetaMaquinaDoPatio metaMaquina = new MetaMaquinaDoPatio();
		metaMaquina.setNomeMaquina(nomePaCarregadeira);

		metaMaquina.setMetaCorreia(null);
		metaMaquina.setGiraLanca(Boolean.FALSE);
		metaMaquina.setTipoDaMaquina(TipoMaquinaEnum.PA_CARREGADEIRA);
		metaMaquina.setIdUser(1L);
		metaMaquina.setDtInsert(new Date(timeDefault));

		MaquinaDoPatio paCarregadeira = new MaquinaDoPatio();
		paCarregadeira.setEstado(EstadoMaquinaEnum.OCIOSA);
		paCarregadeira.setVelocidadeDeslocamento(new Double(60));
		paCarregadeira.setTaxaDeOperacaoNominal(new Double("50"));
		paCarregadeira.setVelocidadeDeslocamento(new Double("13"));
		paCarregadeira.setProximaManutencao(new Date());
		paCarregadeira.setIdUser(1L);
		paCarregadeira.setDtInsert(new Date(timeDefault));

		paCarregadeira.setPosicao(patio.retornaStatusHorario(
				new Date(timeDefault)).getListaDeBalizas(new Date(timeDefault))
				.get(0));
		paCarregadeira.setPatio(patio
				.retornaStatusHorario(new Date(timeDefault)));

		metaMaquina.incluirNovoStatus(paCarregadeira, new Date(timeDefault));

		return metaMaquina;
	}

	/**
	 * criaPatios
	 * 
	 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
	 * @since 09/06/2010
	 * @see
	 * @param
	 * @return List<MetaPatio>
	 */
	public static List<MetaPatio> criaPatios(long timeDefault) {
		// lista de maquinas dos patios
		List<MetaPatio> result = new ArrayList<MetaPatio>();
		// Cria o objeto patio A
		// Patio patio = new Patio();
		patio1 = new MetaPatio();
		patio1.setNumero(new Integer(1));
		patio1.setNomePatio(NOME_PATIO_A);
		patio1.setCapacidadeMaxima(new Double("750000"));
		patio1.setLargura(new Double("62.2"));
		patio1.setComprimento(new Double("1160"));
		patio1.setDtInsert(new Date(timeDefault));
		patio1.setIdUser(1L);

		Patio patioStatus1 = new Patio();
		patioStatus1.setEstado(EstadoMaquinaEnum.OPERACAO);
		patioStatus1.setDtInsert(new Date(timeDefault));
		patioStatus1.setIdUser(1L);

		patio1.incluirNovoStatus(patioStatus1, new Date(timeDefault));

		// Cria o objeto patio B
		patio2 = new MetaPatio();
		patio2.setNumero(new Integer(2));
		patio2.setNomePatio(NOME_PATIO_B);
		patio2.setCapacidadeMaxima(new Double("750000"));
		patio2.setLargura(new Double("62.2"));
		patio2.setComprimento(new Double("1160"));
		patio2.setDtInsert(new Date(timeDefault));
		patio2.setIdUser(1L);

		Patio patioStatus2 = new Patio();
		patioStatus2.setEstado(EstadoMaquinaEnum.OPERACAO);
		patioStatus2.setDtInsert(new Date(timeDefault));
		patioStatus2.setIdUser(1L);

		patio2.incluirNovoStatus(patioStatus2, new Date(timeDefault));

		// Cria o objeto patio C
		patio3 = new MetaPatio();
		patio3.setNumero(new Integer(3));
		patio3.setNomePatio(NOME_PATIO_C);
		// TODO ainda falta saber a capacidade de carga total da do patio 3
		patio3.setCapacidadeMaxima(new Double("750000"));// ????
		patio3.setLargura(new Double("62.2"));
		patio3.setComprimento(new Double("1200"));
		patio3.setDtInsert(new Date(timeDefault));
		patio3.setIdUser(1L);

		Patio patioStatus3 = new Patio();
		patioStatus3.setEstado(EstadoMaquinaEnum.OPERACAO);
		                patioStatus3.setDtInsert(new Date(timeDefault));
		patioStatus3.setIdUser(1L);

		patio3.incluirNovoStatus(patioStatus3, new Date(timeDefault));

		result.add(patio1);
		result.add(patio2);
		result.add(patio3);

		return result;

	}

	/**
	 * criaPilha
	 * 
	 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
	 * @since 10/06/2010
	 * @see
	 * @param
	 * @return Pilha
	 */
	public static Pilha criaPilha(String nomePilha, List<MetaBaliza> balizas,
			int from, int to, Cliente cliente, List<TipoProduto> tipoProdutos,
			long timeDefault) {
		Pilha pilha = new Pilha();
		pilha.setNomePilha(nomePilha);
		pilha.setCliente(cliente);
		pilha.setIdUser(1L);
		pilha.setDtInsert(new Date(timeDefault));
		pilha.setDtInicio(new Date(timeDefault));
		pilha.setHorarioInicioFormacao(new Date(timeDefault));
		pilha.setHorarioFimFormacao(new Date(timeDefault));
		List<Baliza> listaBalizas = criaListaBalizasParaPilha(tipoProdutos,
				balizas, from, to, pilha, timeDefault);
		pilha.addBaliza(listaBalizas);
		return pilha;
	}

	/**
	 * criaProduto
	 * 
	 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
	 * @since 10/06/2010
	 * @see
	 * @param
	 * @return Produto
	 */
	public static Produto criaProduto(TipoProduto tipoProduto, long timeDefault) {
		// cria objeto produto para carga com dados reais
		// cria qualidade real e estimada do produto
		// 
		Produto p =  Produto.criaProduto(tipoProduto, timeDefault);
		p.setQualidade(criaQualidadeProduto(Boolean.TRUE, "amostra"));	    
		p.getQualidade().setProduto(p);
		return p;
	}

	private static Qualidade criaQualidadeProduto(Boolean ehReal,
			String nomeAmostra) {
		Qualidade qualidade = new Qualidade();
		qualidade.setIdUser(1L);
		qualidade.setDtInsert(new Date(System.currentTimeMillis()));
		qualidade.setDtInicio(new Date(System.currentTimeMillis()));
		qualidade.setEhReal(ehReal);
		qualidade.setListaDeAmostras(new ArrayList<Amostra>());
		qualidade
				.setListaDeItensDeControle(criaListaDeItensDeControleComValoresVazios());

		if (ehReal) {
			// Criando objeto amostra
			 Amostra amostra1 = criaAmostraDeQualidadeDeProduto();
			 Amostra amostra2 = criaAmostraDeQualidadeDeProduto();

			// Adicionando objeto lista de amostras ao objeto qualidade
			/* ControladorQualidade controladorQualidade = new
			 ControladorQualidade(qualidade);
			 controladorQualidade.adicionaAmostraDeQualidade(amostra1);
			 controladorQualidade.adicionaAmostraDeQualidade(amostra2);*/
		}

		return qualidade;
	}


	/**
	 * criarBercos
	 * 
	 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
	 * @since 09/06/2010
	 * @see
	 * @param
	 * @return List<MetaBerco>
	 */
	public static List<MetaBerco> criarBercos(long timeDefault) {
		List<MetaBerco> listaBercos = new ArrayList<MetaBerco>();
		// Cria Berco 1 e adiciona a lista de bercos de atracacao do pier

		MetaBerco berco = new MetaBerco();
		berco.setNomeBerco(NOME_BERCO_Oeste);
		berco.setComprimentoMaximo(new Double("308"));
		berco.setBocaMaxima(new Double("56"));
		berco.setCaladoMaximo(new Double("16.8"));
		berco.setIdentificadorBerco("W");// west
		berco.setTagPims("WIT_798A-OE");
		berco.setDtInsert(new Date(timeDefault));
		berco.setIdUser(1L);

		Berco bercoStatus = new Berco();
		bercoStatus.setEstado(EstadoMaquinaEnum.OCIOSA);
		bercoStatus.setDtInsert(new Date(timeDefault));
		bercoStatus.setIdUser(1L);

		berco.incluirNovoStatus(bercoStatus, new Date(timeDefault));

		listaBercos.add(berco);

		// Cria Berco 2 e adiciona a lista de bercos de atracacao do pier
		berco = new MetaBerco();
		berco.setNomeBerco(NOME_BERCO_Leste);
		berco.setComprimentoMaximo(new Double("240"));
		berco.setBocaMaxima(new Double("32"));
		berco.setCaladoMaximo(new Double("13"));
		berco.setIdentificadorBerco("E");// east
		berco.setTagPims("WIT_798A-LE");
		berco.setDtInsert(new Date(timeDefault));
		berco.setIdUser(1L);

		bercoStatus = new Berco();
		bercoStatus.setEstado(EstadoMaquinaEnum.OCIOSA);
		bercoStatus.setDtInsert(new Date(timeDefault));
		bercoStatus.setIdUser(1L);

		berco.incluirNovoStatus(bercoStatus, new Date(timeDefault));

		listaBercos.add(berco);

		return listaBercos;

	}

	/**
	 * criarCarregadoraNavio
	 * 
	 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
	 * @since 09/06/2010
	 * @see
	 * @param
	 * @return List<MetaCarregadoraDeNavios>
	 */
	public static List<MetaCarregadoraDeNavios> criarCarregadoraNavio(
			long timeDefault) {
		// cria lista de carregadora de Navios para Pier
		List<MetaCarregadoraDeNavios> listaCarregadoraNavios = new ArrayList<MetaCarregadoraDeNavios>();

		// cria o objeto carregadora de Navios
		MetaCarregadoraDeNavios carregaNavio = new MetaCarregadoraDeNavios();
		carregaNavio.setNomeCarregadeiraDeNavios(NOME_CARREGADORA_DE_NAVIOS);
		carregaNavio.setDtInsert(new Date(timeDefault));
		carregaNavio.setIdUser(1L);

		CarregadoraDeNavios carregaNavioStatus = new CarregadoraDeNavios();
		carregaNavioStatus.setTaxaDeOperacao(new Double("11200"));
		carregaNavioStatus.setProximaManutencao(new Date());
		carregaNavioStatus.setEstado(EstadoMaquinaEnum.OCIOSA);
		carregaNavioStatus.setDtInsert(new Date(timeDefault));
		carregaNavioStatus.setIdUser(1L);
		carregaNavio.incluirNovoStatus(carregaNavioStatus,
				new Date(timeDefault));

		// adiciona o objeto carregaNavio a lista de carregadoras de navios
		listaCarregadoraNavios.add(carregaNavio);
		// cria o objeto lista de bercos
		// Adiciona a lista de carregadora de navios ao objeto pier
		return listaCarregadoraNavios;

	}

	/**
	 * criaRecuperadora
	 * 
	 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
	 * @since 09/06/2010
	 * @see
	 * @param
	 * @return MetaMaquinaDoPatio
	 */
	private static MetaMaquinaDoPatio criaRecuperadora(String nomeRecuperadora,
			MetaBaliza baliza, MetaCorreia correia, Boolean bracoPatioInferior,
			long timeDefault) {
		MetaMaquinaDoPatio recuperadora = new MetaMaquinaDoPatio();
		recuperadora.setNomeMaquina(nomeRecuperadora);
		recuperadora.setTipoDaMaquina(TipoMaquinaEnum.RECUPERADORA);
		recuperadora.setGiraLanca(Boolean.TRUE);
		recuperadora.setTagPimsBalanca("309TP003-WQI003-R");
		recuperadora.setMetaCorreia(correia);
		recuperadora.setIdUser(1L);
		recuperadora.setDtInsert(new Date(timeDefault));

		MaquinaDoPatio maquina = new MaquinaDoPatio();
		maquina.setEstado(EstadoMaquinaEnum.OCIOSA);
		maquina.setVelocidadeDeslocamento(new Double(10));
		maquina.setTaxaDeOperacaoNominal(new Double(5000));
		maquina.setProximaManutencao(new Date());
		maquina.setPosicao(baliza.retornaStatusHorario(new Date(timeDefault)));
		maquina.setPatio(baliza.getMetaPatio().retornaStatusHorario(
				new Date(timeDefault)));
		maquina.setBracoNoPatioInferior(bracoPatioInferior);
		maquina.setIdUser(1L);
		maquina.setDtInsert(new Date(timeDefault));

		recuperadora.incluirNovoStatus(maquina, new Date(timeDefault));

		return recuperadora;
	}

	/**
	 * criarListaMaquinasCorrerias
	 * 
	 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
	 * @since 09/06/2010
	 * @see
	 * @param
	 * @return List<MetaMaquinaDoPatio>
	 */
	public static List<MetaMaquinaDoPatio> criarListaMaquinasCorrerias(
			long timeDefault, List<MetaCorreia> listaCorreia) {
		List<MetaMaquinaDoPatio> result = new ArrayList<MetaMaquinaDoPatio>();
		// objeto empilhadeira
		for (MetaCorreia correia : listaCorreia) {
		    if (correia.getNomeCorreia().equals(NOME_CORREIA_1)) {
		        result.add(criaCorreiaComEmpilhadeira(correia,
				"Empilhadeira 1", Boolean.TRUE, timeDefault));
		    } else if (correia.getNomeCorreia().equals(NOME_CORREIA_4)) {            
		        result.add(criaCorreiaComEmpilhadeira(correia,
				"Empilhadeira 2", Boolean.FALSE, timeDefault));
		// objeto recuperadora		
		    } else if (correia.getNomeCorreia().equals(NOME_CORREIA_3)) {
		        result.add(criaCorreiaComRecuperadora(correia,
				"Retomadora", Boolean.TRUE, timeDefault));
		// objeto empilhadeiraRecuperadora
            } else if (correia.getNomeCorreia().equals(NOME_CORREIA_2)) {		
                result.add(criaCorreiaComEmpilhadeiraRecuperadora(correia,
				"Empilhadeira-Recuperadora 1", Boolean.FALSE, timeDefault));
            }
	    }
		return result;
	}

	/**
	 * criarPier
	 * 
	 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
	 * @since 09/06/2010
	 * @see
	 * @param
	 * @return MetaPier
	 */
	public static MetaPier criarPier(long timeDefault) {
		// cria lista de pier para planta
		MetaPier pier = new MetaPier();
		pier.setNomePier(NOME_PIER);
		pier.setDtInsert(new Date(timeDefault));
		pier.setIdUser(1L);
		// cria estado
		Pier pierStatus = new Pier();
		pierStatus.setEstado(EstadoMaquinaEnum.OCIOSA);
		pierStatus.setDtInsert(new Date(timeDefault));
		pierStatus.setIdUser(1L);
		pier.incluirNovoStatus(pierStatus, new Date(timeDefault));
		return pier;
	}

	private static SituacaoPatio criaSituacaoPatioParaPlanta(int nroSituacao,
			long timeDefault) {
		// vez

		criarTipoProduto();
		// controladorModelo.salvaListaTiposProduto(listaTiposProduto);
		// listaTiposProdutoPersistida =
		// controladorModelo.buscarTiposProduto(new TipoProduto());

		// cria um map com os objetos tipoItemDeControle
		// criaMapTipoItemControle(listaTiposProdutoPersistida);

		// controladorModelo.salvaListaTipoItemControle(
		// listaTiposItensControle);
		listaTiposItensControle.clear();
		// listaTiposItensControle =
		// controladorModelo.buscarTiposItemControle();

		// cria objeto cliente
		cliente = criaCliente();
		// cria e persiste objeto fila de navio
		// criaListaDeNavios(nroSituacao, cliente, controladorModelo);
		// filaNavio = new FilaDeNavios();
		// filaNavio.setDescricaoFilaNavios(NOME_FILA_DE_NAVIOS);
		// filaNavio.setListaDeNaviosNaFila(listaDeNaviosPersistida);

		criaPatios(timeDefault);
		// criaObjetoPlanta(timeDefault);

		// cria registro na tabela integracaoParametros
		// integracaoParametros = criaUmaIntegracaoParametros(new Long(2),
		// "15/12/2009 12:00:00", "15/12/2009 12:00:00", "FALSE");
		// controladorModelo.salvarParametrosSistemas(integracaoParametros);

		// integracaoParametros = criaUmaIntegracaoParametros(new Long(1),
		// "15/12/2009 00:00:00", "15/12/2009 00:00:00", "TRUE");
		// controladorModelo.salvarParametrosSistemas(integracaoParametros);

		// }

		// Cria uma nova situacao de patio.
		// SituacaoPatio situacaoPatio = new SituacaoPatio();
		// situacaoPatio.setDataHora(new Date(timeDefault));
		// situacaoPatio.setEhRealizado(Boolean.FALSE);
		// Adiciona um novo objeto planta com todos os seus itens de planta e
		// seus estados.
		// situacaoPatio.setPlanta(planta);
		/*
		 * for (Usina usina : situacaoPatio.getPlanta().getListaUsinas(new
		 * Date(timeDefault))) { // cria objeto campanha e associa a uma usina
		 * TipoProduto tipoDeProdutoDaUsina =
		 * listaTiposProdutoPersistida.get(2); TipoProduto tipoDePelletPFN =
		 * listaTiposProdutoPersistida.get(7); TipoProduto tipoDePelletPFL =
		 * listaTiposProdutoPersistida.get(6); TipoProduto tipoDeScreening =
		 * listaTiposProdutoPersistida.get(8); if
		 * (tipoDeProdutoDaUsina.getCodigoInsumoTipoProduto
		 * ().getCodigoTipoProduto().equalsIgnoreCase("PFN")) {
		 * criaCampanha(nroSituacao, tipoDeProdutoDaUsina, tipoDePelletPFN,
		 * tipoDeScreening, usina); } else if
		 * (tipoDeProdutoDaUsina.getCodigoInsumoTipoProduto
		 * ().getCodigoTipoProduto().equalsIgnoreCase("PFL")) {
		 * criaCampanha(nroSituacao, tipoDeProdutoDaUsina, tipoDePelletPFL,
		 * tipoDeScreening, usina); } else { criaCampanha(nroSituacao,
		 * tipoDeProdutoDaUsina, tipoDeScreening, tipoDeScreening, usina); } }
		 */// situacaoPatio.setFilaDeNavios(filaNavio);
		// filaNavio.setSituacao(situacaoPatio);
		// Cria lista de pilhas para situacao de patio
		// List<Pilha> listaPilha =
		// criaListaDePilhas(filaNavio.getListaDeNaviosNaFila
		// ().get(0).getCliente(), listaTiposProdutoPersistida.get(nroSituacao),
		// nroSituacao);
		// Adiciona a lista de pilhas ao objeto Situacao de Patio
		// situacaoPatio.setListaDePilhasNosPatios(listaPilha);
		return null;
	}

	public static List<TipoProduto> criarTipoProduto() {
		// Cria inicialmente os tipos de produto que serao utilizados nos
		// produtos das balizas e das cargas dos navios.
		List<TipoProduto> listaTiposProduto = new ArrayList<TipoProduto>();

		String[] listaDescricoesTiposProdutoBase = { "PSM-Pelota", "PSM-PSC",
				"PSM-Rejeito", "PSM-Residuo", "PSC", "PFN", "PFL", "PBF-STD",
				"PBF-HB", "PBF-MB45", "PBF-MB35", "PDR-HY", "PDR-MG", "PDR-MX",
				"POND" };

		String[] listaCodigosTipoProdutoBase = { "PSM-Pelota", "PSM-PSC",
				"PSM-Rejeito", "PSM-Residuo", "PSC", "PFN", "PFL", "STD", "HB",
				"MB45", "MB35", "HY", "MG", "MX", "Pond" };

		String[] listaCoresTipoProdutoBase = { "255,255,0", "255,255,74",
				"232,232,84", "255,255,166", "0,0,0", "51,153,102",
				"54,255,61", "0,0,255", "150,150,150", "166,255,255",
				"22,224,224", "255,0,0", "204,153,255", "255,153,0",
				"214,128,0" };

		String[] listaCdProdutoCRM = { "-", "-", "-", "-", "700", "250", "300",
				"400", "404", "407", "403", "503", "504", "500", "350" };

		String[] listaCdFamilia = { "-", "-", "-", "-", "-", "-", "-", "PBF",
				"PBF", "PBF", "PBF", "PDR", "PDR", "PDR", "-" };

		Long[] listaCodigoSap = { 0l, 0l, 0l, 0l, 0l, 0l, 0l, 12261l, 12262l,
				12263l, 12263l, 12264l, 12265l, 12266l, 12267l };

		Double[] listaPorcentagemTipoProdutoBase = { 0.36, 0.56, null, 0.08,
				null, null, null, null, null, null, null, null, null, null,
				null };

		TipoDeProdutoEnum[] tipoEnumBase = { TipoDeProdutoEnum.PELOTA_PSM,
				TipoDeProdutoEnum.PELLET_PSM, TipoDeProdutoEnum.PR,
				TipoDeProdutoEnum.LIXO, TipoDeProdutoEnum.PELLET_SCREENING,
				TipoDeProdutoEnum.PELLET_FEED, TipoDeProdutoEnum.PELLET_FEED,
				TipoDeProdutoEnum.PELOTA, TipoDeProdutoEnum.PELOTA,
				TipoDeProdutoEnum.PELOTA, TipoDeProdutoEnum.PELOTA,
				TipoDeProdutoEnum.PELOTA, TipoDeProdutoEnum.PELOTA,
				TipoDeProdutoEnum.PELOTA, TipoDeProdutoEnum.PELLET_FEED };
		listaTiposProduto = new ArrayList<TipoProduto>();

		int index = 0;
		for (String desc : listaDescricoesTiposProdutoBase) {
			TipoProduto insumo = null;
			if (index >= 7 && index <= 10) {
				insumo = listaTiposProduto.get(5);
			} else if (index >= 11 && index <=13) {
				insumo = listaTiposProduto.get(6);
			}
			listaTiposProduto.add(criaTipoProduto(desc,
					listaCodigosTipoProdutoBase[index],
					listaCoresTipoProdutoBase[index], listaCdFamilia[index],
					insumo, listaCodigoSap[index], tipoEnumBase[index],
					listaPorcentagemTipoProdutoBase[index],
					listaCdProdutoCRM[index]));
			index++;
		}

		return listaTiposProduto;

	}

	/**
	 * Cria tipo produto a partir dos parametros de entrada
	 * 
	 * @param descricao
	 *            - String de descricao do tipo produto
	 * @param codigo
	 * @param cor
	 *            - Cor do tipo produto utilizado na interface grafica
	 * @param familia
	 *            - Familia a quem este tipo produto pertence
	 * @param insumo
	 *            - Insumo utilizada para fabricar este tipo produto, este
	 *            parametro pode ser null dependendo do tipo produto a ser
	 *            criado.
	 * @param codigoSAP
	 *            - codigo sap utilizado para sincronizar com os sistemas
	 *            externos
	 * @return
	 */
	private static TipoProduto criaTipoProduto(String descricao, String codigo,
			String cor, String familia, TipoProduto insumo, Long codigoSAP,
			TipoDeProdutoEnum tipo, Double procentagemTratamentoPSM,
			String cdProdutoCRM) {
		TipoProduto tipoProduto = new TipoProduto();
		tipoProduto.setDescricaoTipoProduto(descricao);
		tipoProduto.setCodigoTipoProduto(codigo);
		tipoProduto.setCorIdentificacao(cor);
		tipoProduto.setCodigoFamiliaTipoProduto(familia);
		tipoProduto.setIdUser(1L);
		tipoProduto.setDtInsert(new Date(System.currentTimeMillis()));
		// o insumo pode ser null, se este tipo produto nao for o resultado de
		// outro tipo produto.
		tipoProduto.setCodigoInsumoTipoProduto(insumo);
		tipoProduto.setCodigoSAPDado(codigoSAP);
		tipoProduto.setTipoDeProduto(tipo);
		tipoProduto.setPorcentagemResultadoPSM(procentagemTratamentoPSM);
		tipoProduto.setCdProduto(cdProdutoCRM);
		return tipoProduto;
	}


    public static MetaFiltragem criaFiltragem(String nome,long timeDefault) {
        // Cria objeto Usina 1
        MetaFiltragem filtragem = new MetaFiltragem();
        filtragem.setNomeFiltragem(nome);       
        filtragem.setDtInsert(new Date(timeDefault));
        filtragem.setIdUser(1L);
        Filtragem filtragemStatus = new Filtragem();
        filtragemStatus.setEstado(EstadoMaquinaEnum.OCIOSA);
        filtragemStatus.setDtInsert(new Date(timeDefault));
        filtragemStatus.setIdUser(1L);

        filtragem.incluirNovoStatus(filtragemStatus, new Date(timeDefault));

        return filtragem;
    }

	
	
	/**
	 * criaUsina
	 * 
	 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
	 * @since 09/06/2010
	 * @see
	 * @param
	 * @return MetaUsina
	 */
	public static MetaUsina criaUsina(String nome, MetaPatio patio,
			int faseProcesso, long timeDefault) {
		// Cria objeto Usina 1
		MetaUsina usina = new MetaUsina();
		usina.setNomeUsina(nome);
		usina.setCodigoFaseProcessoUsina(new Long(faseProcesso));
		usina.setCdItemControleUsina(new Long(150));
		usina.setCdTipoProcessoUsina("HH");
		usina.setCdAreaRespEdUsina("CC");
		usina.setMetaPatioExpurgoPellet(patio);
		List<MetaPatio> metaPatiosEmergencia = new ArrayList<MetaPatio>();
		
		metaPatiosEmergencia.add(patio);
		
		usina.setListaMetaPatioEmergencia(metaPatiosEmergencia);
		
		usina.setDtInsert(new Date(timeDefault));
		usina.setIdUser(1L);
		Usina usinaStatus = new Usina();
		usinaStatus.setEstado(EstadoMaquinaEnum.OCIOSA);        		
		usinaStatus.setTaxaDeOperacao(new Double("750"));
		usinaStatus.setTaxaGeracaoPellet(0.02);
		usinaStatus.setDtInsert(new Date(timeDefault));
		usinaStatus.setIdUser(1L);

		usina.incluirNovoStatus(usinaStatus, new Date(timeDefault));

		return usina;
	}

	public static PlanoEmpilhamentoRecuperacao getPlanoEmpilhamentoRecuperacao() {

		PlanoEmpilhamentoRecuperacao plano = new PlanoEmpilhamentoRecuperacao();
		// plano.setDataInicio(new Date());
		plano.setEhOficial(Boolean.TRUE);
		plano.setIdUser(1L);
		// plano.setUltimaModificacao(new Date());
		// plano.setHorizonteDePlanejamento();

		// criar situaï¿½ï¿½o de pï¿½tio
		List<SituacaoPatio> listaSituacaoPatio = new ArrayList<SituacaoPatio>();
		SituacaoPatio situacaoPatio;
		for (int i = 0; i < NUMERO_SITUACOES_PATIO; i++) {
			// situacaoPatio =
			// GeradorModeloUtilCenarioCompleto.getSituacaoPatio(i,
			// controladorModelo);
			/*
			 * situacaoPatio.setIdUser(plano.getIdUser());
			 * situacaoPatio.setDataHora(new Date());
			 * situacaoPatio.setEhRealizado(Boolean.TRUE);
			 * situacaoPatio.setPlanoEmpilhamento(plano);
			 * listaSituacaoPatio.add(situacaoPatio);
			 */
		}

		plano.setListaSituacoesPatio(listaSituacaoPatio);

		// gerarTabelaAmostragemFrequencia(controladorModelo);

		return plano;
	}

	/**
	 * @return uma situaï¿½ï¿½o de patio
	 */
	public static SituacaoPatio getSituacaoPatio(int nroSituacao,
			long timeDefault) {
		return criaSituacaoPatioParaPlanta(nroSituacao, timeDefault);
	}

		/**
	 * 
	 * gravaListaFaixaPelota
	 * 
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 28/08/2009
	 * @see
	 * @param listaFaixaPelota
	 * @param controladorModelo
	 * @throws ErroSistemicoException
	 * @return Returns the void.
	 */
	private static void gravaListaFaixaPelota(
			List<FaixaAmostragemFrequencia> listaFaixaPelota)
			throws ErroSistemicoException {

		// Primeira Faixa
		FaixaAmostragemFrequencia faixa = new FaixaAmostragemFrequencia();
		faixa.setDtInsert(new Date());
		faixa.setFaixaTonelagemInicial(0.00);
		faixa.setFaixaTonelagemFinal(29999.99);
		faixa.setIncremento(400);
		faixa.setGranulometria(1600);
		faixa.setTamboramento(6400);
		// faixa = controladorModelo.salvarFaixaAmostragemFrequencia(faixa);
		listaFaixaPelota.add(faixa);

		// Segunda Faixa
		faixa = new FaixaAmostragemFrequencia();
		faixa.setDtInsert(new Date());
		faixa.setFaixaTonelagemInicial(30000.00);
		faixa.setFaixaTonelagemFinal(49999.99);
		faixa.setIncremento(500);
		faixa.setGranulometria(2000);
		faixa.setTamboramento(8000);
		// faixa = controladorModelo.salvarFaixaAmostragemFrequencia(faixa);
		listaFaixaPelota.add(faixa);

		// Terceira Faixa
		faixa = new FaixaAmostragemFrequencia();
		faixa.setDtInsert(new Date());
		faixa.setFaixaTonelagemInicial(50000.00);
		faixa.setFaixaTonelagemFinal(100000.00);
		faixa.setIncremento(750);
		faixa.setGranulometria(3000);
		faixa.setTamboramento(12000);
		// faixa = controladorModelo.salvarFaixaAmostragemFrequencia(faixa);
		listaFaixaPelota.add(faixa);

		// Quarta Faixa
		faixa = new FaixaAmostragemFrequencia();
		faixa.setDtInsert(new Date());
		faixa.setFaixaTonelagemInicial(100000.01);
		faixa.setFaixaTonelagemFinal(999999999999.99);
		faixa.setIncremento(1000);
		faixa.setGranulometria(4000);
		faixa.setTamboramento(16000);
		// faixa = controladorModelo.salvarFaixaAmostragemFrequencia(faixa);
		listaFaixaPelota.add(faixa);
	}


	/**
	 * recuperarCorreiaPatio
	 * 
	 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
	 * @since 14/06/2010
	 * @see
	 * @param
	 * @return Correia
	 */
	public static Correia recuperarCorreiaEmpilhamentoPatio(
			MetaPatio metaPatio, Correia correia, Date inicioExecucao) {
		Correia result = null;
		if ((correia.getMetaCorreia().getMetaPatioInferior() != null && correia
				.getMetaCorreia().getMetaPatioInferior().equals(metaPatio))
				|| (correia.getMetaCorreia().getMetaPatioSuperior() != null && correia
						.getMetaCorreia().getMetaPatioSuperior().equals(
								metaPatio))) {
			// recupera meta maquinas
			List<MetaMaquinaDoPatio> maquinas = correia.getMetaCorreia()
					.getListaDeMetaMaquinas();
			MaquinaDoPatio maquina = recuperarMaquinaEmpilhamentoCorreia(
					inicioExecucao, maquinas);
			if (maquina != null) {
				result = correia;
			}
		}
		return result;
	}

	/**
	 * recuperarCorreiaPatio
	 * 
	 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
	 * @since 14/06/2010
	 * @see
	 * @param
	 * @return Correia
	 */
	public static Correia recuperarCorreiaRecuperacaoPatio(MetaPatio metaPatio,
			Correia correia, Date inicioExecucao) {
		Correia result = null;
		if ((correia.getMetaCorreia().getMetaPatioInferior() != null && correia
				.getMetaCorreia().getMetaPatioInferior().equals(metaPatio))
				|| (correia.getMetaCorreia().getMetaPatioSuperior() != null && correia
						.getMetaCorreia().getMetaPatioSuperior().equals(
								metaPatio))) {
			// recupera meta maquinas
			List<MetaMaquinaDoPatio> maquinas = correia.getMetaCorreia()
					.getListaDeMetaMaquinas();
			MaquinaDoPatio maquina = recuperarMaquinaRecuperadorCorreia(
					inicioExecucao, maquinas);
			if (maquina != null) {
				result = correia;
			}
		}
		return result;
	}

	/**
	 * recuperarMaquinaEmpilhamentoCorreia
	 * 
	 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
	 * @since 14/06/2010
	 * @see
	 * @param
	 * @return void
	 */
	public static MaquinaDoPatio recuperarMaquinaEmpilhamentoCorreia(
			Date inicioExecucao, List<MetaMaquinaDoPatio> maquinas) {
		MaquinaDoPatio maquina = null;
		// recupera a maquina pro teste
		for (MetaMaquinaDoPatio metaMaquina : maquinas) {

			if (metaMaquina.getTipoDaMaquina().equals(
					TipoMaquinaEnum.EMPILHADEIRA)
					|| metaMaquina.getTipoDaMaquina().equals(
							TipoMaquinaEnum.EMPILHADEIRA_RECUPERADORA)) {
				maquina = metaMaquina.retornaStatusHorario(inicioExecucao);
				if (maquina != null) {
					break;
				}
			}
		}
		return maquina;
	}

	/**
	 * recuperarMaquinaRecuperadorCorreia
	 * 
	 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
	 * @since 23/06/2010
	 * @see
	 * @param
	 * @return MaquinaDoPatio
	 */
	public static MaquinaDoPatio recuperarMaquinaRecuperadorCorreia(
			Date inicioExecucao, List<MetaMaquinaDoPatio> maquinas) {
		MaquinaDoPatio maquina = null;
		// recupera a maquina pro teste
		for (MetaMaquinaDoPatio metaMaquina : maquinas) {

			if (metaMaquina.getTipoDaMaquina().equals(
					TipoMaquinaEnum.RECUPERADORA)
					|| metaMaquina.getTipoDaMaquina().equals(
							TipoMaquinaEnum.EMPILHADEIRA_RECUPERADORA)) {
				maquina = metaMaquina.retornaStatusHorario(inicioExecucao);
				if (maquina != null) {
					break;
				}
			}
		}
		return maquina;
	}

	public static MaquinaDoPatio recuperarPaCarregadeiraCorreia(
			Date inicioExecucao, List<MetaMaquinaDoPatio> maquinas) {
		MaquinaDoPatio maquina = null;
		// recupera a maquina pro teste
		for (MetaMaquinaDoPatio metaMaquina : maquinas) {

			if (metaMaquina.getTipoDaMaquina().equals(
					TipoMaquinaEnum.PA_CARREGADEIRA)) {
				maquina = metaMaquina.retornaStatusHorario(inicioExecucao);
				if (maquina != null) {
					break;
				}
			}
		}
		return maquina;
	}

	
		
	
	public static List<TipoItemDeControle> criaListaTipoItemControle(long timeDefault) {

		List<TipoItemDeControle> result = new ArrayList<TipoItemDeControle>();

		int index = 0;
		for (String tipo : itensControlePelotaFisico) {
			// tipo item de controle
			TipoItemDeControle tipoItemDeControle = montaItemDeControlePelotaFisico(
					index, tipo,timeDefault);
			index++;
			result.add(tipoItemDeControle);					
		}
		index = 0;
		TipoItemCoeficiente tp = null;
		for (String tipo : itensControlePelotaQuimico) {
			// tipo item de controle
			TipoItemDeControle tipoItemDeControle = montaItemDeControlePelotaQuimico(
					index, tipo,timeDefault);
			index++;
			result.add(tipoItemDeControle);						
		}

		index = 0;
		for (String tipo : itensControlePelletFisico) {
			// tipo item de controle
			TipoItemDeControle tipoItemDeControle = montaItemDeControlePelletFisico(
					index, tipo,timeDefault);
			index++;
			result.add(tipoItemDeControle);			
		}

		index = 0;
		for (String tipo : itensControlePelletQuimico) {
			// tipo item de controle
			TipoItemDeControle tipoItemDeControle = montaItemDeControlePelletQuimico(
					index, tipo,timeDefault);
			index++;
			result.add(tipoItemDeControle);		
		}

		return result;
	}

	private static TipoItemDeControle montaItemDeControlePelotaFisico(
			int index, String tipo,long timeDefault) {
		TipoItemDeControle tipoItemDeControle = new TipoItemDeControle();
		tipoItemDeControle.setDescricaoTipoItemControle(tipo);
		tipoItemDeControle.setRelevante(Boolean.TRUE);
		tipoItemDeControle.setTipoItemControle(TipoItemControleEnum.FISICO);
		tipoItemDeControle.setUnidade("%");
    	
		/* item;tipo;área;Ini Escala;Fim Escala;Multiplicidade */

		String info = itensControlePelotaFisicoDados[index];

		tratarDados(tipoItemDeControle, info,timeDefault);
    
		return tipoItemDeControle;
	}

	private static TipoItemDeControle montaItemDeControlePelotaQuimico(
			int index, String tipo, long timeDefault) {
		TipoItemDeControle tipoItemDeControle = new TipoItemDeControle();
		tipoItemDeControle.setDescricaoTipoItemControle(tipo);
		tipoItemDeControle.setRelevante(Boolean.TRUE);
		tipoItemDeControle.setTipoItemControle(TipoItemControleEnum.QUIMICO);
		tipoItemDeControle.setUnidade("%");

		/* item;tipo;área;Ini Escala;Fim Escala;Multiplicidade */

		String info = itensControlePelotaQuimicoDados[index];

		tratarDados(tipoItemDeControle, info,timeDefault);

		return tipoItemDeControle;
	}

	private static TipoItemDeControle montaItemDeControlePelletFisico(
			int index, String tipo,long timeDefault) {
		TipoItemDeControle tipoItemDeControle = new TipoItemDeControle();
		tipoItemDeControle.setDescricaoTipoItemControle(tipo);
		tipoItemDeControle.setRelevante(Boolean.TRUE);
		tipoItemDeControle.setTipoItemControle(TipoItemControleEnum.FISICO);
		tipoItemDeControle.setUnidade("%");

		/* item;tipo;área;Ini Escala;Fim Escala;Multiplicidade */

		String info = itensControlePelletFisicoDados[index];

		StringTokenizer strTokElem = new StringTokenizer(info, ";");

		tratarDados(tipoItemDeControle, info,timeDefault);

		return tipoItemDeControle;
	}

	private static TipoItemDeControle montaItemDeControlePelletQuimico(
			int index, String tipo, long timeDefault) {
		TipoItemDeControle tipoItemDeControle = new TipoItemDeControle();
		tipoItemDeControle.setDescricaoTipoItemControle(tipo);
		tipoItemDeControle.setRelevante(Boolean.TRUE);
		tipoItemDeControle.setTipoItemControle(TipoItemControleEnum.QUIMICO);
		tipoItemDeControle.setUnidade("%");

		/* item;tipo;área;Ini Escala;Fim Escala;Multiplicidade */

		String info = itensControlePelletQuimicoDados[index];

		StringTokenizer strTokElem = new StringTokenizer(info, ";");

		tratarDados(tipoItemDeControle, info,timeDefault);

		return tipoItemDeControle;
	}

	private static void tratarDados(TipoItemDeControle tipoItemDeControle,
			String info, long timeDefault) {
		StringTokenizer strTokElem = new StringTokenizer(info, ";");

		tipoItemDeControle.setCdTipoItemControlePelota(new Long(Long
				.parseLong((String) strTokElem.nextElement())));
		tipoItemDeControle.setTipoProcessoPelota((String) strTokElem
				.nextElement());
		tipoItemDeControle.setAreaRespEDPelota((String) strTokElem
				.nextElement());

		tipoItemDeControle.setDtInsert(new Date(System.currentTimeMillis()));
		tipoItemDeControle.setIdUser(1L);

		tipoItemDeControle.setCdTipoItemControlePelletFeed(tipoItemDeControle
				.getCdTipoItemControlePelota());
		tipoItemDeControle.setTipoProcessoPelletFeed(tipoItemDeControle
				.getTipoProcessoPelota());
		tipoItemDeControle.setAreaRespEDPelletFeed(tipoItemDeControle
				.getAreaRespEDPelota());

		tipoItemDeControle.setInicioEscala(new Double(Double
				.parseDouble(((String) strTokElem.nextElement()).replaceAll(
						",", "."))));
		tipoItemDeControle.setFimEscala(new Double(Double
				.parseDouble(((String) strTokElem.nextElement()).replaceAll(
						",", "."))));
		tipoItemDeControle.setMultiplicidadeEscala(new Double(Double
				.parseDouble(((String) strTokElem.nextElement()).replaceAll(
						",", "."))));
		
		
		TipoItemCoeficiente tp = new TipoItemCoeficiente();        
        tp.setDtInicio(new Date(timeDefault));
        tp.setDtFim(new Date());
        tp.setIdUser(1l);
        tp.setDtInsert(new Date());
        tipoItemDeControle.addTipoItemCoeficiente(tp);			
        tp.setValorDoCoeficiente(new Double(Double
		                .parseDouble(((String) strTokElem.nextElement()).replaceAll(
		                        ",", "."))));
		try {
            String value = (String) strTokElem.nextElement();
            if (value != null && !value.equals("")) {
               TipoItemRegraFarol tpR = new TipoItemRegraFarol();
               tpR.setValorRegraFarol(EnumValorRegraFarol.valueOf(value.toUpperCase()));
               tpR.setDtInicio(new Date(timeDefault));
               tpR.setDtFim(new Date());
               tpR.setIdUser(1l);
               tpR.setDtInsert(new Date());
               tipoItemDeControle.addTipoItemRegraFarol(tpR);
            }
		} catch (Exception e) {
		    
            // TODO: handle exception
        }    
	}

	private static Amostra criaAmostraDeQualidadeDeProduto()
	   {

		Amostra amostra = new Amostra();

		// Criando objeto lista de itens de controle para a amostra.
		List<ItemDeControle> listaItemControle = new ArrayList<ItemDeControle>();
		for (TipoItemDeControle tipoItemDeControle : listaTiposItensControle)
		{
		  listaItemControle.add(criaItemDeControleAmostra(tipoItemDeControle, 1D));		  
		}

		amostra.setNomeAmostra(((new Date()).toString()));

		// Adicionando a lista de itens de controle a amostra e a qualidade.
		amostra.setListaDeItensDeControle(listaItemControle);

		return amostra;
	   }

	
	private static Qualidade criaQualidadeProdutoEmbarcado(Boolean ehReal,
			String nomeAmostra) {
		Qualidade qualidade = new Qualidade();
		qualidade.setEhReal(ehReal);
		qualidade.setListaDeAmostras(new ArrayList<Amostra>());
		qualidade.setIdUser(1L);
		qualidade.setDtInsert(new Date(System.currentTimeMillis()));
		qualidade.setDtInicio(new Date(System.currentTimeMillis()));
		qualidade
				.setListaDeItensDeControle(criaListaDeItensDeControleComValoresVazios());

		if (ehReal) {
			// Criando objeto lista de itens de controle para a amostra.
			List<ItemDeControle> listaItemControle = new ArrayList<ItemDeControle>();
			for (TipoItemDeControle tipoItemDeControle : listaTiposItensControle) {
				  listaItemControle.add(criaItemDeControleEmbarcado(tipoItemDeControle, ran.nextInt(150), ran.nextInt(150))); 
			}
	    }
		// Criando objeto amostra
		//Amostra amostra = new Amostra();
		//amostra.setNomeAmostra(nomeAmostra + ((new Date()).toString()));

		// Adicionando a lista de itens de controle a amostra e a qualidade.
		//amostra.setListaDeItensDeControle(listaItemControle);

		// Adicionando objeto lista de amostras ao objeto qualidade
		// ControladorQualidade controladorQualidade = new
		// ControladorQualidade(qualidade);
		// controladorQualidade.adicionaAmostraDeQualidade(amostra);

		return qualidade;

	}
			

	private static List<ItemDeControle> criaListaDeItensDeControleDaOrientacaoDeEmbarqueDoProduto()
	   {
		// Criando objeto lista de itens de controle
		List<ItemDeControle> listaItemControle = new ArrayList<ItemDeControle>();
		for (TipoItemDeControle tipoItemDeControle : listaTiposItensControle)
		{
			listaItemControle.add(criaItemDeControleParaOrientacaoDeEmbarque(tipoItemDeControle, ran.nextInt(150), ran.nextInt(350)));
	   }
		return listaItemControle;
	   }

	public static List<TipoItemDeControle> getListaTiposItensControle() {
		return listaTiposItensControle;
	}

	public static void setListaTiposItensControle(
			List<TipoItemDeControle> listaTiposItensControle) {
		GeradorModeloUtilCenarioCompleto.listaTiposItensControle = listaTiposItensControle;
	}

	

	   /**
     * 
     * gerarTabelaAmostragemFrequencia
     * 
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 28/08/2009
     * @see
     * @param controladorModelo
     * @throws ErroSistemicoException
     * @throws ValidacaoCampoException
     * @return Returns the void.
     */
    public static TabelaAmostragemFrequencia gerarTabelaAmostragemFrequencia( List<FaixaAmostragemFrequencia> listaFaixaPelota, List<FaixaAmostragemFrequencia> listaFaixaPellet)
            throws ErroSistemicoException, ValidacaoCampoException {
        // Gravando a tabela
        TabelaAmostragemFrequencia tabela = new TabelaAmostragemFrequencia();
        tabela.setDtInsert(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.set(2008, 11, 12, 0, 0, 0);
        tabela.setData(calendar.getTime());
        tabela.setCodigo(CODIGO_NORMA);
        tabela.setRevisao(REVISAO_NORMA);
        tabela.setClassificacao(CLASSIFICACAO_NORMA);
        ItemAmostragemFrequencia itemA = popularItemPelotaPDR(listaFaixaPelota);
        itemA.setTabela(tabela);
        tabela.addItemTabela(itemA);
        ItemAmostragemFrequencia itemB = popularItemPelotaPBF(listaFaixaPelota);
        itemB.setTabela(tabela);
        tabela.addItemTabela(itemB);
        ItemAmostragemFrequencia itemC = popularItemPelletFeed(listaFaixaPellet);
        itemC.setTabela(tabela);
        tabela.addItemTabela(itemC);
        ItemAmostragemFrequencia itemD = popularItemSinterFeed(listaFaixaPellet);
        itemD.setTabela(tabela);
        tabela.addItemTabela(itemD);
        return tabela;
    }

    public static void gerarListaFaixaAmostragem(List<FaixaAmostragemFrequencia> listaFaixaPelota,
                    List<FaixaAmostragemFrequencia> listaFaixaPellet) throws ErroSistemicoException {
        FaixaAmostragemFrequencia faixaFinal;
        gravaListaFaixaPelota(listaFaixaPelota);
        faixaFinal = listaFaixaPelota.get(listaFaixaPelota.size() - 1);
        gravaListaFaixaPellet(listaFaixaPellet);
        listaFaixaPellet.add(faixaFinal);
    }

    /**
     * 
     * gravaListaFaixaPellet
     * 
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 28/08/2009
     * @see
     * @param listaFaixaPellet
     * @param controladorModelo
     * @throws ErroSistemicoException
     * @return Returns the void.
     */
    private static void gravaListaFaixaPellet(
            List<FaixaAmostragemFrequencia> listaFaixaPellet) throws ErroSistemicoException {

        // Primeira Faixa
        FaixaAmostragemFrequencia faixa = new FaixaAmostragemFrequencia();
        faixa.setDtInsert(new Date());
        faixa.setFaixaTonelagemInicial(2000.00);
        faixa.setFaixaTonelagemFinal(4999.99);
        faixa.setIncremento(125);
        faixa.setGranulometria(500);
        faixa.setTamboramento(2000);        
        listaFaixaPellet.add(faixa);

        // Segunda Faixa
        faixa = new FaixaAmostragemFrequencia();
        faixa.setDtInsert(new Date());
        faixa.setFaixaTonelagemInicial(5000.00);
        faixa.setFaixaTonelagemFinal(29999.99);
        faixa.setIncremento(250);
        faixa.setGranulometria(1000);
        faixa.setTamboramento(4000);        
        listaFaixaPellet.add(faixa);

        // Terceira Faixa
        faixa = new FaixaAmostragemFrequencia();
        faixa.setDtInsert(new Date());
        faixa.setFaixaTonelagemInicial(30000.00);
        faixa.setFaixaTonelagemFinal(44999.99);
        faixa.setIncremento(500);
        faixa.setGranulometria(2000);
        faixa.setTamboramento(8000);        
        listaFaixaPellet.add(faixa);

        // Quarta Faixa
        faixa = new FaixaAmostragemFrequencia();
        faixa.setDtInsert(new Date());
        faixa.setFaixaTonelagemInicial(45000.00);
        faixa.setFaixaTonelagemFinal(100000.00);
        faixa.setIncremento(750);
        faixa.setGranulometria(3000);
        faixa.setTamboramento(12000);        
        listaFaixaPellet.add(faixa);

    }

    /**
     * 
     * popularItemPelotaPDR
     * 
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 28/08/2009
     * @see
     * @param listaFaixaPelota
     * @return
     * @return Returns the ItemAmostragemFrequencia.
     */
    private static ItemAmostragemFrequencia popularItemPelotaPDR(
            List<FaixaAmostragemFrequencia> listaFaixaPelota) {

        ItemAmostragemFrequencia item = new ItemAmostragemFrequencia();
        item.setDtInsert(new Date());
        item.setCodigoFamiliaTipoProduto("PDR");
        item.setTipoProduto(TipoDeProdutoEnum.PELOTA);
        item.setListaFaixas(listaFaixaPelota);

        return item;
    }

    /**
     * 
     * popularItemPelotaPBF
     * 
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 28/08/2009
     * @see
     * @param listaFaixaPelota
     * @return
     * @return Returns the ItemAmostragemFrequencia.
     */
    private static ItemAmostragemFrequencia popularItemPelotaPBF(
            List<FaixaAmostragemFrequencia> listaFaixaPelota) {

        ItemAmostragemFrequencia item = new ItemAmostragemFrequencia();
        item.setDtInsert(new Date());
        item.setCodigoFamiliaTipoProduto("PBF");
        item.setTipoProduto(TipoDeProdutoEnum.PELOTA);
        item.setListaFaixas(listaFaixaPelota);

        return item;
    }

    /**
     * 
     * popularItemPelletFeed
     * 
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 28/08/2009
     * @see
     * @param listaFaixaPellet
     * @return
     * @return Returns the ItemAmostragemFrequencia.
     */
    private static ItemAmostragemFrequencia popularItemPelletFeed(
            List<FaixaAmostragemFrequencia> listaFaixaPellet) {

        ItemAmostragemFrequencia item = new ItemAmostragemFrequencia();
        item.setDtInsert(new Date());
        item.setCodigoFamiliaTipoProduto("");
        item.setTipoProduto(TipoDeProdutoEnum.PELLET_FEED);
        item.setListaFaixas(listaFaixaPellet);

        return item;
    }

    /**
     * 
     * popularItemSinterFeed
     * 
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 28/08/2009
     * @see
     * @param listaFaixaPellet
     * @return
     * @return Returns the ItemAmostragemFrequencia.
     */
    private static ItemAmostragemFrequencia popularItemSinterFeed(
            List<FaixaAmostragemFrequencia> listaFaixaPellet) {

        ItemAmostragemFrequencia item = new ItemAmostragemFrequencia();
        item.setDtInsert(new Date());
        item.setCodigoFamiliaTipoProduto("");
        item.setTipoProduto(TipoDeProdutoEnum.SINTER_FEED);
        item.setListaFaixas(listaFaixaPellet);

        return item;
    }

    
	
}
