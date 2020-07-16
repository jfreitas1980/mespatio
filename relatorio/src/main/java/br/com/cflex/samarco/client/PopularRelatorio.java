package br.com.cflex.samarco.client;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import br.com.cflex.samarco.supervision.stockyard.relatorio.GeradorPDF;
import br.com.cflex.samarco.supervision.stockyard.relatorio.RelatorioPDF;
import br.com.cflex.samarco.supervision.stockyard.relatorio.controles.CabecalhoRelatorio;
import br.com.cflex.samarco.supervision.stockyard.relatorio.controles.Coluna;
import br.com.cflex.samarco.supervision.stockyard.relatorio.enums.CalculoColunaEnum;
import br.com.cflex.samarco.supervision.stockyard.relatorio.exception.ErroGeracaoRelatorioPDF;

class PopularRelatorio {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		PopularRelatorio teste = new PopularRelatorio();
		teste.relatorioDeslocamentoMaquina();
		teste.relatorioDSP();

	}
	
	private static void addColunas(RelatorioPDF relatorio) {
		List<CalculoColunaEnum> somenteSoma = new ArrayList<CalculoColunaEnum>();
		somenteSoma.add(CalculoColunaEnum.SOMA);
		List<CalculoColunaEnum> somenteMedia = new ArrayList<CalculoColunaEnum>();
		somenteMedia.add(CalculoColunaEnum.MEDIA);
		List<CalculoColunaEnum> somaMedia = new ArrayList<CalculoColunaEnum>();
		somaMedia.add(CalculoColunaEnum.SOMA);
		somaMedia.add(CalculoColunaEnum.MEDIA);
		
		int i =1;
		/*relatorio.addColuna(new Coluna(i++,"Grupo1",10,CalculoColunaEnum.VAZIO,true,1,
				new CalculoColunaEnum[]{CalculoColunaEnum.SOMA,CalculoColunaEnum.MEDIA},false,"Calculo"));
		relatorio.addColuna(new Coluna(i++,"Valor X",10,CalculoColunaEnum.VAZIO,false,0,null,true));
		relatorio.addColuna(new Coluna(i++,"Valor A",10,CalculoColunaEnum.VAZIO,false,0,null,false));
		relatorio.addColuna(new Coluna(i++,"Valor B",10,CalculoColunaEnum.VAZIO,false,0,null,false));
		relatorio.addColuna(new Coluna(i++,"Soma A",10,CalculoColunaEnum.SOMA,false,0,null,false));
		relatorio.addColuna(new Coluna(i++,"Media A",10,CalculoColunaEnum.MEDIA,false,0,null,false));
		relatorio.addColuna(new Coluna(i++,"Media B",10,CalculoColunaEnum.MEDIA,false,0,null,false));*/
		
		/*relatorio.addColuna(new Coluna(i++,"PATIO",16,CalculoColunaEnum.VAZIO,false,false,false));
		relatorio.addColuna(new Coluna(i++,"PILHAS",14,CalculoColunaEnum.VAZIO,false,false,true));
		relatorio.addColuna(new Coluna(i++,"BALIZA",14,CalculoColunaEnum.VAZIO,false,false,true));
		//Indica o grupo que soma e o grupo que calcula a media
		relatorio.addColuna(new Coluna(i++,"FAMILIA",0,CalculoColunaEnum.SOMA,true,true,false));
		relatorio.addColuna(new Coluna(i++,"CODPRODUTO",0,CalculoColunaEnum.MEDIA,true,true,false));
		
		relatorio.addColuna(new Coluna(i++,"TON",14,CalculoColunaEnum.SOMA,false,false,false));
		
		relatorio.addColuna(new Coluna(i++,"SiO2",14,CalculoColunaEnum.MEDIA,false,false,false));
		relatorio.addColuna(new Coluna(i++,"Fe",14,CalculoColunaEnum.MEDIA,false,false,false));
		relatorio.addColuna(new Coluna(i++,"B2",14,CalculoColunaEnum.MEDIA,false,false,false));*/

		
		relatorio.addColuna(new Coluna(i++,"PILHA",7,CalculoColunaEnum.NAO_CALCULA,false,0,null,false));
		relatorio.addColuna(new Coluna(i++,"PATIO",7,CalculoColunaEnum.NAO_CALCULA,false,0,null,false));
		relatorio.addColuna(new Coluna(i++,"INICIO",5,CalculoColunaEnum.NAO_CALCULA,false,0,null,false));
		relatorio.addColuna(new Coluna(i++,"FIM",5,CalculoColunaEnum.NAO_CALCULA,false,0,null,false));
		relatorio.addColuna(new Coluna(i++,"CLIENTE",10,CalculoColunaEnum.NAO_CALCULA,false,0,null,false));
		relatorio.addColuna(new Coluna(i++,"PRODUTO",10,CalculoColunaEnum.NAO_CALCULA,false,0,null,true));
		relatorio.addColuna(new Coluna(i++,"FAMILIA",0,CalculoColunaEnum.SOMA,true,1,new CalculoColunaEnum[]{CalculoColunaEnum.SOMA},false));
		relatorio.addColuna(new Coluna(i++,"CODPRODUTO",0,CalculoColunaEnum.MEDIA,true,2,new CalculoColunaEnum[]{CalculoColunaEnum.MEDIA},false));
		relatorio.addColuna(new Coluna(i++,"TON",5,CalculoColunaEnum.SOMA,false,0,null,false));
		relatorio.addColuna(new Coluna(i++,"Fe",5,CalculoColunaEnum.MEDIA,false,0,null,false));
		relatorio.addColuna(new Coluna(i++,"SiO2",5,CalculoColunaEnum.MEDIA,false,0,null,false));
		relatorio.addColuna(new Coluna(i++,"MgO/CaO",5,CalculoColunaEnum.MEDIA,false,0,null,false));
		relatorio.addColuna(new Coluna(i++,"B2",5,CalculoColunaEnum.MEDIA,false,0,null,false));
		relatorio.addColuna(new Coluna(i++,"-6.3",5,CalculoColunaEnum.MEDIA,false,0,null,false));
		relatorio.addColuna(new Coluna(i++,"Relacao",5,CalculoColunaEnum.MEDIA,false,0,null,false));
		relatorio.addColuna(new Coluna(i++,"TB",5,CalculoColunaEnum.MEDIA,false,0,null,false));
		relatorio.addColuna(new Coluna(i++,"C16",5,CalculoColunaEnum.MEDIA,false,0,null,false));
		relatorio.addColuna(new Coluna(i++,"EMERG.",6,CalculoColunaEnum.NAO_CALCULA,false,0,null,false));
		relatorio.addColuna(new Coluna(i++,"S/PENEIRAR",5,CalculoColunaEnum.NAO_CALCULA,false,0,null,false));
		

	}
	
	
	private static Map<Integer,List<Object>> newDados() throws IOException {
		
		Integer registros = 1;
		Map<Integer,List<Object>> dados = new HashMap<Integer, List<Object>>();
		List<Object> dadosList = new ArrayList<Object>();
		
		/*dadosList.add("X");
		dadosList.add("12");
		dadosList.add("1");
		dadosList.add("2");
		dadosList.add(new Double(150));
		dadosList.add(new Double(2));
		dadosList.add(new Double(3));
		dados.put(registros++, dadosList);*/
		
		/*dadosList = new ArrayList<Object>();
		dadosList.add("X");
		dadosList.add("12");
		dadosList.add("1");
		dadosList.add("2");
		dadosList.add(new Double(150));
		dadosList.add(new Double(4));
		dadosList.add(new Double(5));
		dados.put(registros++, dadosList);*/
		
		/*dadosList = new ArrayList<Object>();
		dadosList.add("X");
		dadosList.add("13");
		dadosList.add("1");
		dadosList.add("3");
		dadosList.add(new Double(150));
		dadosList.add(new Double(6));
		dadosList.add(new Double(7));
		dados.put(registros++, dadosList);*/
		
		/*dadosList = new ArrayList<Object>();
		dadosList.add("X");
		dadosList.add("13");
		dadosList.add("1");
		dadosList.add("3");
		dadosList.add(new Double(150));
		dadosList.add(new Double(8));
		dadosList.add(new Double(9));
		dados.put(registros++, dadosList);*/
		
		/*dadosList = new ArrayList<Object>();
		dadosList.add("X");
		dadosList.add("21");
		dadosList.add("2");
		dadosList.add("1");
		dadosList.add(new Double(100));
		dadosList.add(new Double(12));
		dadosList.add(new Double(11));
		dados.put(registros++, dadosList);*/
		
		/*dadosList = new ArrayList<Object>();
		dadosList.add("X");
		dadosList.add("21");
		dadosList.add("2");
		dadosList.add("1");
		dadosList.add(new Double(100));
		dadosList.add(new Double(14));
		dadosList.add(new Double(13));
		dados.put(registros++, dadosList);*/
		
		/*dadosList = new ArrayList<Object>();
		dadosList.add("X");
		dadosList.add("24");
		dadosList.add("2");
		dadosList.add("4");
		dadosList.add(new Double(100));
		dadosList.add(new Double(16));
		dadosList.add(new Double(15));
		dados.put(registros++, dadosList);*/
		
		/*dadosList = new ArrayList<Object>();
		dadosList.add("X");
		dadosList.add("24");
		dadosList.add("2");
		dadosList.add("4");
		dadosList.add(new Double(100));
		dadosList.add(new Double(18));
		dadosList.add(new Double(17));
		dados.put(registros++, dadosList);*/
		
		dadosList.add("PatioA");
		dadosList.add("PilhaA");
		dadosList.add("20/01");
		dadosList.add("XX");
		dadosList.add("XX");
		dadosList.add("PBF - MB45");
		dadosList.add("PBF");
		dadosList.add("MB45");
		dadosList.add(new Double(2500));
		dadosList.add(new Double(67.28));
		dadosList.add(new Double(0.00));
		dadosList.add(new Double(0.87));
		dadosList.add(new Double(0.43));
		dadosList.add(new Double(0.82));
		dadosList.add(new Double(0.80));
		dadosList.add(new Double(93.70));
		dadosList.add(new Double(300));
		dadosList.add("SIM");
		dadosList.add("");
		dados.put(registros++, dadosList);
		
		dadosList = new ArrayList<Object>();
		dadosList.add("71b"); //1
		dadosList.add("C"); //2
		dadosList.add("30/10/08 09:30h"); //3
		dadosList.add("30/10/08 20:25h"); //4
		dadosList.add("aaa"); //5
		dadosList.add("PBF - MB45"); //6
		dadosList.add("PBF");
		dadosList.add("MB45");
		dadosList.add(new Double(9600)); //7
		dadosList.add(new Double(67.33)); //8
		dadosList.add(new Double(1.88)); //9
		dadosList.add(new Double(0.86)); //10
		dadosList.add(new Double(0.45)); //11
		dadosList.add(new Double(0.90)); //12
		dadosList.add(new Double(1.15)); //13
		dadosList.add(new Double(93.24)); //14
		dadosList.add(new Double(290)); //15
		dadosList.add(""); //16
		dadosList.add(""); //17
		dados.put(registros++, dadosList);
		
		dadosList = new ArrayList<Object>();
		dadosList.add("71g");
		dadosList.add("B");
		dadosList.add("31/10/08 09:30h");
		dadosList.add("31/10/08 20:25h");
		dadosList.add("aaa");
		dadosList.add("PBF - MB45");
		dadosList.add("PBF");
		dadosList.add("MB45");
		dadosList.add(new Double(2500));
		dadosList.add(new Double(67.28));
		dadosList.add(new Double(0.00));
		dadosList.add(new Double(0.87));
		dadosList.add(new Double(0.43));
		dadosList.add(new Double(0.82));
		dadosList.add(new Double(0.80));
		dadosList.add(new Double(93.70));
		dadosList.add(new Double(300));
		dadosList.add("SIM");
		dadosList.add("");
		dados.put(registros++, dadosList);
		
		dadosList = new ArrayList<Object>();
		dadosList.add("71b"); //1
		dadosList.add("C"); //2
		dadosList.add("30/10/08 09:30h"); //3
		dadosList.add("30/10/08 20:25h"); //4
		dadosList.add("aaa456"); //5
		dadosList.add("PBF - MB46"); //6
		dadosList.add("PBF");
		dadosList.add("MB46");
		dadosList.add(new Double(9600)); //7
		dadosList.add(new Double(67.33)); //8
		dadosList.add(new Double(1.88)); //9
		dadosList.add(new Double(0.86)); //10
		dadosList.add(new Double(0.45)); //11
		dadosList.add(new Double(0.90)); //12
		dadosList.add(new Double(1.15)); //13
		dadosList.add(new Double(93.24)); //14
		dadosList.add(new Double(290)); //15
		dadosList.add(""); //16
		dadosList.add(""); //17
		dados.put(registros++, dadosList);
		
		dadosList = new ArrayList<Object>();
		dadosList.add("71g");
		dadosList.add("B");
		dadosList.add("31/10/08 09:30h");
		dadosList.add("31/10/08 20:25h");
		dadosList.add("aaa123");
		dadosList.add("PBF - MB46");
		dadosList.add("PBF");
		dadosList.add("MB46");
		dadosList.add(new Double(2400));
		dadosList.add(new Double(67.40));
		dadosList.add(new Double(1.89));
		dadosList.add(new Double(0.79));
		dadosList.add(new Double(0.42));
		dadosList.add(new Double(0.90));
		dadosList.add(new Double(1.10));
		dadosList.add(new Double(93.40));
		dadosList.add(new Double(297));
		dadosList.add("SIM");
		dadosList.add("");
		dados.put(registros++, dadosList);
		
		dadosList = new ArrayList<Object>();
		dadosList.add("76i");
		dadosList.add("C");
		dadosList.add("18/11/08 10:00h");
		dadosList.add("18/11/08 16:00h");
		dadosList.add("hhh");
		dadosList.add("PDR - MX");
		dadosList.add("PDR");
		dadosList.add("MX");
		dadosList.add(new Double(9000));
		dadosList.add(new Double(67.73));
		dadosList.add(new Double(1.39));
		dadosList.add(new Double(0.13));
		dadosList.add(new Double(0.54));
		dadosList.add(new Double(0.60));
		dadosList.add(new Double(1.35));
		dadosList.add(new Double(94.05));
		dadosList.add(new Double(284));
		dadosList.add("");
		dadosList.add("");
		dados.put(registros++, dadosList);
		
		dadosList = new ArrayList<Object>();
		dadosList.add("82a");
		dadosList.add("A");
		dadosList.add("26/11/08 14:00h");
		dadosList.add("26/11/08 17:30h");
		dadosList.add("iii");
		dadosList.add("PDR - MX");
		dadosList.add("PDR");
		dadosList.add("MX");
		dadosList.add(new Double(9000));
		dadosList.add(new Double(67.48));
		dadosList.add(new Double(1.46));
		dadosList.add(new Double(1.12));
		dadosList.add(new Double(0.77));
		dadosList.add(new Double(0.90));
		dadosList.add(new Double(1.00));
		dadosList.add(new Double(92.70));
		dadosList.add(new Double(280));
		dadosList.add("");
		dadosList.add("");
		dados.put(registros++, dadosList);
		
		dadosList = new ArrayList<Object>();
		dadosList.add("76i");
		dadosList.add("C");
		dadosList.add("18/11/08 10:00h");
		dadosList.add("18/11/08 16:00h");
		dadosList.add("hhh741");
		dadosList.add("PDR - MXZ");
		dadosList.add("PDR");
		dadosList.add("MXZ");
		dadosList.add(new Double(9000));
		dadosList.add(new Double(67.73));
		dadosList.add(new Double(1.39));
		dadosList.add(new Double(0.13));
		dadosList.add(new Double(0.54));
		dadosList.add(new Double(0.60));
		dadosList.add(new Double(1.35));
		dadosList.add(new Double(94.05));
		dadosList.add(new Double(284));
		dadosList.add("");
		dadosList.add("");
		dados.put(registros++, dadosList);
		
		dadosList = new ArrayList<Object>();
		dadosList.add("82a");
		dadosList.add("A");
		dadosList.add("26/11/08 14:00h");
		dadosList.add("26/11/08 17:30h");
		dadosList.add("iii258");
		dadosList.add("PDR - MXZ");
		dadosList.add("PDR");
		dadosList.add("MXZ");
		dadosList.add(new Double(9000));
		dadosList.add(new Double(67.48));
		dadosList.add(new Double(1.46));
		dadosList.add(new Double(1.12));
		dadosList.add(new Double(0.77));
		dadosList.add(new Double(0.90));
		dadosList.add(new Double(1.00));
		dadosList.add(new Double(92.70));
		dadosList.add(new Double(280));
		dadosList.add("");
		dadosList.add("");
		dados.put(registros++, dadosList);
		
		return dados;
	}
	
	private void relatorioDSP() {
		try {
			Image logo = ImageIO.read(new File("logo.png"));
			
		    String titulo = "GERENCIA PRODUCAO - DEPARTAMENTO ESTOCAGEM E EMBARQUE";
		    String subTitulo = "PERFIL DO PATIO";
		    Calendar calendario = Calendar.getInstance();
		    calendario.setTime(new Date());
		    calendario.add(Calendar.MONTH, -1);
			
		   List<String> dadosCabecalho = new ArrayList<String>();
		    
		    RelatorioPDF relatorioPDF = new RelatorioPDF(titulo, subTitulo, logo);
		    CabecalhoRelatorio cabecalho = relatorioPDF.getCabecalhoRelatorio();
		    cabecalho.setDataSituacao(new Date());
		    cabecalho.setDataInicioPeriodo(calendario.getTime());
		    calendario.add(Calendar.MONTH, 2);		    
		    cabecalho.setDataFimPeriodo(calendario.getTime());
			cabecalho.setUsuario("DARLEY R. PERES");
			cabecalho.setTipoRelatorio("INSTANTANEO");		
			cabecalho.setQtdPilhas(7);
			addColunas(relatorioPDF);
			
			/*incluindo dados de cabecalho*/
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");
			
			dadosCabecalho.add("Periodo Relatario: "+sdf.format(cabecalho.getDataInicioPeriodo())+" a "+
					sdf.format(cabecalho.getDataFimPeriodo()));
			dadosCabecalho.add("Gerado por: "+cabecalho.getUsuario());
			dadosCabecalho.add("Tipo: "+cabecalho.getTipoRelatorio());
			dadosCabecalho.add("Data Situacao Patio: "+sdf.format(cabecalho.getDataSituacao()));
			dadosCabecalho.add("Qtd. Pilhas da Situacao: "+cabecalho.getQtdPilhas());

			cabecalho.setListaDeDados(dadosCabecalho);
			cabecalho.setQtdColunas(3);
			
			Image imgLegenda = ImageIO.read(new File("legenda.png"));
			Image imgCabecalho = ImageIO.read(new File("cabecalho.png"));

			Map<Integer,List<Object>> dados = newDados();

			relatorioPDF.setImagemCabecalhoRelatorio(imgLegenda, imgCabecalho);
			
			relatorioPDF.setDadosCorpoRelatorio(dados);
			
			
			Map<Integer,List<Object>> dadosRodape = null;
			relatorioPDF.setDadosRodape(dadosRodape);

			GeradorPDF pdf = new GeradorPDF("c:\\relatorios","pdfTeste");
			pdf.addRelatorio(relatorioPDF);

			pdf.gerarPDF();
			
			GeradorPDF.abrirRelatorioPDF(pdf.getDiretorioPDF(),"pdfTeste");


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (ErroGeracaoRelatorioPDF e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void relatorioDeslocamentoMaquina() {
		try {
			GeradorPDF pdf = new GeradorPDF("c:\\relatorios","pdfTeste1");			
			Image logo = ImageIO.read(new File("logo.png"));
		    String titulo = "RELATORIO DE OPERACAO";
		    String subTitulo = "";
		   
		    //Tipo Empilhamento
		    RelatorioPDF relatorioPDF = new RelatorioPDF(titulo, subTitulo, logo);
		    CabecalhoRelatorio cabecalho = relatorioPDF.getCabecalhoRelatorio();
		    cabecalho.setQtdColunas(1);
		    cabecalho.addColuna("Teste");
		    cabecalho.addColuna("Tipo: Empilhamento");
		    addColunasEmpilhamento(relatorioPDF);
		    Map<Integer,List<Object>> dados = addDadosEmpilhamento();
		    relatorioPDF.setDadosCorpoRelatorio(dados);
			Map<Integer,List<Object>> dadosRodape = null;
			relatorioPDF.setDadosRodape(dadosRodape);
			pdf.addRelatorio(relatorioPDF);
			
		    //Tipo Recuperacao
		    relatorioPDF = new RelatorioPDF(titulo, subTitulo, logo);
		    relatorioPDF.setGeraNovaPagina(false);
		    cabecalho = relatorioPDF.getCabecalhoRelatorio();
		    cabecalho.setQtdColunas(1);
		    cabecalho.addColuna("");
		    cabecalho.addColuna("Tipo: Recuperacao");
		    addColunasRecuperacao(relatorioPDF);
		    dados = addDadosRecuperacao();
		    relatorioPDF.setDadosCorpoRelatorio(dados);
			relatorioPDF.setDadosRodape(dadosRodape);
			pdf.addRelatorio(relatorioPDF);

		    //Tipo Movimentacao
		    relatorioPDF = new RelatorioPDF(titulo, subTitulo, logo);
		    relatorioPDF.setGeraNovaPagina(false);
		    cabecalho = relatorioPDF.getCabecalhoRelatorio();
		    cabecalho.setQtdColunas(1);
		    cabecalho.addColuna("");
		    cabecalho.addColuna("Tipo: Movimentacao");
		    addColunasMovimentacao(relatorioPDF);
		    dados = addDadosMovimentacao();
		    relatorioPDF.setDadosCorpoRelatorio(dados);
			relatorioPDF.setDadosRodape(dadosRodape);
			pdf.addRelatorio(relatorioPDF);
			pdf.gerarPDF();
			
			GeradorPDF.abrirRelatorioPDF(pdf.getDiretorioPDF(),"pdfTeste");


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (ErroGeracaoRelatorioPDF e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void addColunasEmpilhamento(RelatorioPDF relatorio ) {
		int i = 1;
		relatorio.addColuna(new Coluna(i++,"Grupo",0,CalculoColunaEnum.NAO_CALCULA,true,1,null,false));
		relatorio.addColuna(new Coluna(i++,"Usina",15,CalculoColunaEnum.NAO_CALCULA,false,0,null,false));
		relatorio.addColuna(new Coluna(i++,"Patio",15,CalculoColunaEnum.NAO_CALCULA,false,0,null,false));
		relatorio.addColuna(new Coluna(i++,"Baliza",15,CalculoColunaEnum.NAO_CALCULA,false,0,null,false));
		relatorio.addColuna(new Coluna(i++,"Inicio",15,CalculoColunaEnum.NAO_CALCULA,false,0,null,false));
		relatorio.addColuna(new Coluna(i++,"Fim",15,CalculoColunaEnum.NAO_CALCULA,false,0,null,false));
		relatorio.addColuna(new Coluna(i++,"Maquina",15,CalculoColunaEnum.NAO_CALCULA,false,0,null,false));
		relatorio.addColuna(new Coluna(i++,"Quantidade",10,CalculoColunaEnum.NAO_CALCULA,false,0,null,false));
	}
	
	
	private Map<Integer,List<Object>> addDadosEmpilhamento() throws IOException {
		int registro = 1;
		Map<Integer,List<Object>> result = new HashMap<Integer, List<Object>>();
		
		List<Object> dadosList = new ArrayList<Object>();
		dadosList.add("grupoX");
		dadosList.add("Usina1");
		dadosList.add("Patio1");
		dadosList.add("Baliza1");
		dadosList.add("Inicio1");
		dadosList.add("Fim1");
		dadosList.add("Maquina1");
		dadosList.add("Quantidade1");
		result.put(registro++, dadosList);
		dadosList = new ArrayList<Object>();
		dadosList.add("grupoX");
		dadosList.add("Usina2");
		dadosList.add("Patio2");
		dadosList.add("Baliza2");
		dadosList.add("Inicio2");
		dadosList.add("Fim2");
		dadosList.add("Maquina2");
		dadosList.add("Quantidade2");
		result.put(registro++, dadosList);
		
		return result;
	}
	
	private void addColunasRecuperacao(RelatorioPDF relatorio) {
		int i = 1;
		relatorio.addColuna(new Coluna(i++,"Grupo",0,CalculoColunaEnum.NAO_CALCULA,true,1,null,false));
		relatorio.addColuna(new Coluna(i++,"Usina",13,CalculoColunaEnum.NAO_CALCULA,false,0,null,false));
		relatorio.addColuna(new Coluna(i++,"Patio",13,CalculoColunaEnum.NAO_CALCULA,false,0,null,false));
		relatorio.addColuna(new Coluna(i++,"Baliza",13,CalculoColunaEnum.NAO_CALCULA,false,0,null,false));
		relatorio.addColuna(new Coluna(i++,"Navio",13,CalculoColunaEnum.NAO_CALCULA,false,0,null,false));
		relatorio.addColuna(new Coluna(i++,"Carga",12,CalculoColunaEnum.NAO_CALCULA,false,0,null,false));
		relatorio.addColuna(new Coluna(i++,"Porao",12,CalculoColunaEnum.NAO_CALCULA,false,0,null,false));
		relatorio.addColuna(new Coluna(i++,"Maquina",12,CalculoColunaEnum.NAO_CALCULA,false,0,null,false));
		relatorio.addColuna(new Coluna(i++,"Quantidade",12,CalculoColunaEnum.NAO_CALCULA,false,0,null,false));
	}
	
	private Map<Integer,List<Object>> addDadosRecuperacao() throws IOException {
		int registro = 1;
		Map<Integer,List<Object>> result = new HashMap<Integer, List<Object>>();
		
		List<Object> dadosList = new ArrayList<Object>();
		dadosList.add("grupoX");
		dadosList.add("Usina1");
		dadosList.add("Patio1");
		dadosList.add("Baliza1");
		dadosList.add("Navio1");
		dadosList.add("Carga1");
		dadosList.add("Porao1");
		dadosList.add("Maquina1");
		dadosList.add("Quantidade1");
		result.put(registro++, dadosList);
		dadosList = new ArrayList<Object>();
		dadosList.add("grupoX");
		dadosList.add("Usina2");
		dadosList.add("Patio2");
		dadosList.add("Baliza2");
		dadosList.add("Navio2");
		dadosList.add("Carga2");
		dadosList.add("Porao2");
		dadosList.add("Maquina2");
		dadosList.add("Quantidade2");
		result.put(registro++, dadosList);
		
		return result;
	}
	
	private void addColunasMovimentacao(RelatorioPDF relatorio) {
		int i = 1;
		relatorio.addColuna(new Coluna(i++,"Grupo",0,CalculoColunaEnum.NAO_CALCULA,true,1,null,false));
		relatorio.addColuna(new Coluna(i++,"Baliza Origem",7,CalculoColunaEnum.NAO_CALCULA,false,0,null,false));
		relatorio.addColuna(new Coluna(i++,"Quantidade",7,CalculoColunaEnum.NAO_CALCULA,false,0,null,false));
		relatorio.addColuna(new Coluna(i++,"Baliza Destino",5,CalculoColunaEnum.NAO_CALCULA,false,0,null,false));
	}
	
	private Map<Integer,List<Object>> addDadosMovimentacao() throws IOException {
		int registro = 1;
		Map<Integer,List<Object>> result = new HashMap<Integer, List<Object>>();
		
		List<Object> dadosList = new ArrayList<Object>();
		dadosList.add("grupoX");
		dadosList.add("BalizaOrigem1");
		dadosList.add("Qtd1");
		dadosList.add("BalizaDestino1");
		result.put(registro++, dadosList);
		dadosList = new ArrayList<Object>();
		dadosList.add("grupoX");
		dadosList.add("BalizaOrigem2");
		dadosList.add("Qtd2");
		dadosList.add("BalizaDestino2");
		result.put(registro++, dadosList);
		
		return result;
	}
}