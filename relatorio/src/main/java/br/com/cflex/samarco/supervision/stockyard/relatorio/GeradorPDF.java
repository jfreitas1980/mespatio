package br.com.cflex.samarco.supervision.stockyard.relatorio;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.cflex.samarco.client.BundleUtils;
import br.com.cflex.samarco.supervision.stockyard.relatorio.controles.CabecalhoRelatorio;
import br.com.cflex.samarco.supervision.stockyard.relatorio.controles.Celula;
import br.com.cflex.samarco.supervision.stockyard.relatorio.controles.Complemento;
import br.com.cflex.samarco.supervision.stockyard.relatorio.controles.Grupo;
import br.com.cflex.samarco.supervision.stockyard.relatorio.controles.Linha;
import br.com.cflex.samarco.supervision.stockyard.relatorio.controles.Texto;
import br.com.cflex.samarco.supervision.stockyard.relatorio.enums.OrientacaoPaginaEnum;
import br.com.cflex.samarco.supervision.stockyard.relatorio.evento.RelatorioPdfEvento;
import br.com.cflex.samarco.supervision.stockyard.relatorio.exception.ErroGeracaoRelatorioPDF;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class GeradorPDF {
	private String diretorioPDF = "/Users/hdn/projetos/projetos/projetos-c/SAMARCO/novaVersao/relatorio";
	private String nomePDF;
	private List<RelatorioPDF> relatorios;
	private Document document;
	
	public GeradorPDF(String nomePDF) {
		this.nomePDF = nomePDF;
	}
	
	public GeradorPDF(String caminho, String nomePDF) {
		this.nomePDF = nomePDF;
		this.diretorioPDF = caminho;
	}
	
	/**
	 * @return the nomePDF
	 */
	public String getNomePDF() {
		if (nomePDF == null) {
			nomePDF = String.format("yyyyMMdd", new Date());
		}
		return nomePDF;
	}
	
	/**
	 * @param nomePDF the nomePDF to set
	 */
	protected void setNomePDF(String nomePDF) {
		this.nomePDF = nomePDF;
	}
	
	/**
	 * Get diretorioPDF
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 01/07/2009
	 */
	public String getDiretorioPDF() {
		return diretorioPDF;
	}

	/**
	 * Change diretorioPDF	
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param diretorioPDF 
	 * @since 01/07/2009
	 */
	public void setDiretorioPDF(String diretorio) {
		diretorioPDF = diretorio;
	}

	/**
	 * 
	 * @param relatorio
	 */
	public void addRelatorio(RelatorioPDF relatorio) {
		if (relatorios == null) {
			relatorios = new ArrayList<RelatorioPDF>();
		}
		relatorios.add(relatorio);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<RelatorioPDF> getRelatorios() {
		return this.relatorios;
	}
	
	/**
	 * 
	 * gerarPDFDSP
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 24/06/2009
	 * @see
	 * @return Returns the void.
	 */
	@Deprecated
	public void gerarPDFDSP() throws ErroGeracaoRelatorioPDF {

		try {
			Document document = new Document(PageSize.A4.rotate());
			File file = new File(diretorioPDF);
			
			if (!file.isDirectory()) {
				file.mkdir();
			}
			String nomepdf = diretorioPDF+File.separator+this.getNomePDF()+".pdf";
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(nomepdf));

            writer.setPageEvent(new RelatorioPdfEvento());

			document.open();
			boolean novoRelatorio = false;
			for (RelatorioPDF relatorio : this.getRelatorios()) {
				if (novoRelatorio) {
					document.newPage();
				}
				document.add(criarCabecalhoRelatorio(relatorio));
				if (relatorio.getCabecalhoTabela().getImagem().getImage() != null) {
					document.add(criarCabecalhoTabela(relatorio));
					if (relatorio.getTotalColunas()>0) {
						document.newPage();
						document.add(criarCabecalhoRelatorio(relatorio));
					}
				}
				if (relatorio.getTotalColunas()>0) {
					document.add(criarCorpoRelatorio(relatorio));
				}
				novoRelatorio = true;
			}
			document.close();
		
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
			de.printStackTrace();
			throw new ErroGeracaoRelatorioPDF();
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
			ioe.printStackTrace();
			throw new ErroGeracaoRelatorioPDF();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			throw new ErroGeracaoRelatorioPDF();
		}

	}
	
	public void gerarPDF(OrientacaoPaginaEnum orientacao) throws ErroGeracaoRelatorioPDF {
		if (OrientacaoPaginaEnum.RETRATO.equals(orientacao)) {
			this.document = new Document(PageSize.A4);
		}else {
			
			this.document = new Document(PageSize.A4.rotate());
		}
		gerarPDF();
	}
	
	public void gerarPDF() throws ErroGeracaoRelatorioPDF {

		try {
			if (this.document == null) {
				this.document = new Document(PageSize.A4.rotate());
			}
			File file = new File(diretorioPDF);
			
			if (!file.isDirectory()) {
				file.mkdir();
			}
			String nomepdf = diretorioPDF+File.separator+this.getNomePDF()+".pdf";
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(nomepdf));

            writer.setPageEvent(new RelatorioPdfEvento());

			document.open();
			boolean novoRelatorio = false;
			for (RelatorioPDF relatorio : this.getRelatorios()) {
				
				if (novoRelatorio && relatorio.isGeraNovaPagina()) {
					document.newPage();
				}
				
				//Criando o titulo do relatorio caso seja uma nova pagina
				if (relatorio.isGeraNovaPagina()) {
					document.add(criarTituloRelatorio(relatorio));
				}
				
				document.add(criarCabecalho(relatorio));
				
				if (relatorio.getCabecalhoTabela().getImagem().getImage() != null) {
					document.add(criarCabecalhoTabela(relatorio));
					if (relatorio.getTotalColunas()>0) {
						document.newPage();
						document.add(criarTituloRelatorio(relatorio));
						document.add(criarCabecalho(relatorio));
					}
				}
				
				if (relatorio.getTotalColunas()>0) {
					document.add(criarCorpoRelatorio(relatorio));
				}
				
				if (relatorio.getRodape()!=null) {
					document.add(criarRodapeRelatorio(relatorio));
				}
				
				if (!relatorio.getComplementos().isEmpty()) {
					document.add(criarComplementoRelatorio(relatorio));
				}
				novoRelatorio = true;
			}
			document.close();
		
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
			de.printStackTrace();
			throw new ErroGeracaoRelatorioPDF();
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
			ioe.printStackTrace();
			throw new ErroGeracaoRelatorioPDF();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			throw new ErroGeracaoRelatorioPDF();
		}

	}
	
	/**
	 * 
	 * abrirRelatorioPDF
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 24/06/2009
	 * @see
	 * @param nomepdf
	 * @throws ErroGeracaoRelatorioPDF
	 * @return Returns the void.
	 */
	public static void abrirRelatorioPDF(String caminho, String nomepdf)  throws ErroGeracaoRelatorioPDF {
		
    	try {
			String arquivo = caminho+File.separator+nomepdf+".pdf";
    		java.awt.Desktop desktop = java.awt.Desktop.getDesktop();  
    		desktop.open(new File(arquivo));
			
		} catch (IOException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			throw new ErroGeracaoRelatorioPDF(BundleUtils.getMessage("msg.error.abrir.pdf"));
		} catch(Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			throw new ErroGeracaoRelatorioPDF(BundleUtils.getMessage("msg.error.abrir.pdf"));
		}
	}
	
	/**
	 * 
	 * criarCabecalhoRelatorio
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 16/06/2009
	 * @see
	 * @param relatorio
	 * @return
	 * @throws BadElementException
	 * @throws IOException
	 * @return Returns the PdfPTable.
	 */
	@Deprecated
	private PdfPTable criarCabecalhoRelatorio(RelatorioPDF relatorio) throws BadElementException, IOException {
		
		PdfPTable tableTitulo = new PdfPTable(new float[]{0.18f, 0.82f});
		Image logoTitulo = Image.getInstance(relatorio.getCabecalhoRelatorio().getLogo().getImage(),Color.WHITE);
		logoTitulo.scalePercent(75);
		PdfPCell cellImg = new PdfPCell(logoTitulo, false);
		cellImg.setHorizontalAlignment(Element.ALIGN_CENTER);
		cellImg.setPadding(2);
		tableTitulo.addCell(cellImg);
		Paragraph p = new Paragraph();
		p.add(new Chunk(relatorio.getCabecalhoRelatorio().getTitulo().getConteudo()+"\n", new Font(Font.HELVETICA, 12, Font.BOLD)));
		p.add(new Chunk(relatorio.getCabecalhoRelatorio().getSubTitulo().getConteudo(), new Font(Font.HELVETICA, 12, Font.BOLDITALIC)));
		PdfPCell cell = new PdfPCell(p);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		tableTitulo.addCell(cell);
		tableTitulo.setWidthPercentage(100);
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");
		PdfPTable table = new PdfPTable(new float[]{0.33f,0.27f,0.15f});
		table.getDefaultCell().setBorder(0);
		p = new Paragraph(new Chunk("Periodo Relatorio: "+sdf.format(relatorio.getCabecalhoRelatorio().getDataInicioPeriodo())+" a "+
				sdf.format(relatorio.getCabecalhoRelatorio().getDataFimPeriodo()), new Font(Font.HELVETICA, 9, Font.BOLD)));
		table.addCell(p);
		table.getDefaultCell().setBorder(0);
		p = new Paragraph(new Chunk("Gerado por: "+relatorio.getCabecalhoRelatorio().getUsuario(), new Font(Font.HELVETICA, 9, Font.BOLD)));
		table.addCell(p);
		table.getDefaultCell().setBorder(0);
		p = new Paragraph(new Chunk("Tipo: "+relatorio.getCabecalhoRelatorio().getTipoRelatorio(), new Font(Font.HELVETICA, 9, Font.BOLD)));
		table.addCell(p);
		PdfPCell cellTable = new PdfPCell(table);
		cellTable.setColspan(2);
		
		PdfPTable linha2 =  new PdfPTable(new float[]{0.33f,0.42f});
		linha2.getDefaultCell().setBorder(0);
		p = new Paragraph(new Chunk("Data Situacao Patio: "+sdf.format(relatorio.getCabecalhoRelatorio().getDataSituacao()), new Font(Font.HELVETICA, 9, Font.BOLD)));
		linha2.addCell(p);
		linha2.getDefaultCell().setBorder(0);
		p = new Paragraph(new Chunk("Qtd. Pilhas da Situacao: "+relatorio.getCabecalhoRelatorio().getQtdPilhas(), new Font(Font.HELVETICA, 9, Font.BOLD)));
		linha2.addCell(p);	
		PdfPCell cellTable2 = new PdfPCell(linha2);
		cellTable2.setColspan(2);
		
		tableTitulo.addCell(cellTable);
		tableTitulo.addCell(cellTable2);		
		
		return tableTitulo;
	}

	/**
	 * 
	 * criarCabecalho
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 11/08/2009
	 * @see
	 * @param relatorio
	 * @return
	 * @throws BadElementException
	 * @throws IOException
	 * @return Returns the PdfPTable.
	 */
	private PdfPTable criarCabecalho(RelatorioPDF relatorio) throws BadElementException, IOException {
		CabecalhoRelatorio cabecalho = relatorio.getCabecalhoRelatorio();
		
		PdfPTable tableTitulo = new PdfPTable(1);
		PdfPTable linha = new PdfPTable(cabecalho.getQtdColunas());
		Paragraph p;
		for (String str : cabecalho.getListaDeDados()) {
			linha.getDefaultCell().setBorder(0);
			p = new Paragraph(new Chunk(str, new Font(Font.HELVETICA, 9, Font.BOLD)));
			linha.addCell(p);
		}
		linha.completeRow();
		PdfPCell tabelaColuna = new PdfPCell(linha);
		tabelaColuna.setColspan(2);
		tableTitulo.addCell(tabelaColuna);
		tableTitulo.setWidthPercentage(100);
		return tableTitulo;
	}
	
	/**
	 * 
	 * criarTituloRelatorio
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 11/08/2009
	 * @see
	 * @param relatorio
	 * @return
	 * @throws BadElementException
	 * @throws IOException
	 * @return Returns the PdfPTable.
	 */
	private PdfPTable criarTituloRelatorio(RelatorioPDF relatorio) throws BadElementException, IOException {
		
		CabecalhoRelatorio cabecalho = relatorio.getCabecalhoRelatorio();
		
		/** Cabecalho fixo para todos: imagem | titulo / subtitulo */
		PdfPTable tableTitulo = new PdfPTable(new float[]{0.18f, 0.82f});
		Image logoTitulo = Image.getInstance(relatorio.getCabecalhoRelatorio().getLogo().getImage(),Color.WHITE);
		logoTitulo.scalePercent(75);
		PdfPCell cellImg = new PdfPCell(logoTitulo, false);
		cellImg.setHorizontalAlignment(Element.ALIGN_CENTER);
		cellImg.setPadding(2);
		tableTitulo.addCell(cellImg);
		Paragraph p = new Paragraph();
		p.add(new Chunk(cabecalho.getTitulo().getConteudo()+"\n", new Font(Font.HELVETICA, 12, Font.BOLD)));
		p.add(new Chunk(cabecalho.getSubTitulo().getConteudo(), new Font(Font.HELVETICA, 12, Font.BOLDITALIC)));
		PdfPCell cell = new PdfPCell(p);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		tableTitulo.addCell(cell);
		tableTitulo.setWidthPercentage(100);
		
		return tableTitulo;
	}
	/**
	 * 
	 * criarCabecalhoTabela
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 16/06/2009
	 * @see
	 * @param relatorio
	 * @return
	 * @throws BadElementException
	 * @throws IOException
	 * @return Returns the PdfPTable.
	 */
	private PdfPTable criarCabecalhoTabela(RelatorioPDF relatorio) throws BadElementException, IOException {
		//Definindo o cabecalho
		PdfPTable tableCabecalho = new PdfPTable(new float[]{10f,90f});
		PdfPCell cellLivre = new PdfPCell();
		cellLivre.setPadding(10);
		cellLivre.setBorder(0);
		tableCabecalho.addCell(cellLivre);
		tableCabecalho.addCell(cellLivre);

		if (relatorio.getCabecalhoTabela().getImgLegenda().getImage() != null) {
			Image imgLegenda = Image.getInstance(relatorio.getCabecalhoTabela().getImgLegenda().getImage(),Color.WHITE);
			int scalePercent = 95;
			while (scalePercent>3 && imgLegenda.getScaledHeight()>380) {
				imgLegenda.scalePercent(scalePercent);
				scalePercent -= 3; 
			}
			//imgLegenda.scalePercent(95);
			PdfPCell cellLegenda = new PdfPCell(imgLegenda, false);
			cellLegenda.setHorizontalAlignment(Element.ALIGN_LEFT);
			cellLegenda.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cellLegenda.setBorder(0);
			cellLegenda.setPadding(2);
			tableCabecalho.addCell(cellLegenda);
		}
		if (relatorio.getCabecalhoTabela().getImagem().getImage() != null) {
			Image imgCabecalho = Image.getInstance(relatorio.getCabecalhoTabela().getImagem().getImage(),Color.WHITE);
			int scalePercent = 95;
			while (scalePercent>3 && imgCabecalho.getScaledWidth()>660 ) {
				imgCabecalho.scalePercent(scalePercent);
				scalePercent -= 3;
			}
			//imgCabecalho.scalePercent(52.50f);
			PdfPCell cell = new PdfPCell(imgCabecalho, false);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setPadding(2);
			cell.setBorder(0);
			tableCabecalho.addCell(cell);
		}

		tableCabecalho.setWidthPercentage(100);
		return tableCabecalho;
	}
	
	/**
	 * 
	 * criarCorpoRelatorio
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 01/07/2009
	 * @see
	 * @param relatorio
	 * @return
	 * @throws BadElementException
	 * @throws IOException
	 * @return Returns the PdfPTable.
	 */
	private PdfPTable criarCorpoRelatorio(RelatorioPDF relatorio) throws BadElementException, IOException {
		
		Paragraph p;
		float[] tam = relatorio.getTamanhoColunas();
		PdfPTable table = new PdfPTable(tam);
		for(Celula celula : relatorio.getCabecalhoTabela().getCelulas()) {
			if (!celula.getColuna().isOculta()) {
				p = new Paragraph();
				p.add(new Chunk(celula.getConteudoStr(), new Font(Font.HELVETICA, 6, Font.BOLD)));
				PdfPCell cell = new PdfPCell(p);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setBackgroundColor(new Color(0xC0, 0xC0, 0xC0));
				table.addCell(cell);
			}
		}
			
		gerarGrupoPDF(table,relatorio.getCorpo().getGrupos());

		table.setWidthPercentage(100);
		return table;
	}
	
	/**
	 * 
	 * gerarGrupoPDF
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 25/08/2009
	 * @see
	 * @param table
	 * @param grupos
	 * @return Returns the void.
	 */
	private void gerarGrupoPDF(PdfPTable table, List<Grupo> grupos) {
		Font font;
		for(Grupo grupo : grupos) {
			if (grupo.getSubGrupos().size()>0) {
				gerarGrupoPDF(table, grupo.getSubGrupos());
				
				if (grupo.getTotalizacaoGrupo() != null) {
					font = new Font(Font.HELVETICA, 6, Font.BOLD);
					Color color = new Color(0xC0, 0xC0, 0xC0);
					popularCelulasGrupo(table, grupo, color, font);
				}
			}
			else {
				
				for(Linha linha : grupo.getLinhas()) {
					font = new Font(Font.HELVETICA, 6, Font.NORMAL);
					popularCelulas(table, linha.getCelulas(), null, font);
				}
				if (grupo.getTotalizacaoGrupo() != null) {
					font = new Font(Font.HELVETICA, 6, Font.BOLD);
					Color color = new Color(0xC0, 0xC0, 0xC0);
					popularCelulasGrupo(table, grupo, color, font);
				}
			}
		}
	}
	
	/**
	 * Metodo responsavel em gerar o rodape do relatorio em pdf
	 * @param relatorio
	 * @return
	 * @throws BadElementException
	 * @throws IOException
	 */
	private PdfPTable criarRodapeRelatorio(RelatorioPDF relatorio) throws BadElementException, IOException {
		Font font;
		float[] tam = relatorio.getTamanhoColunas();
		PdfPTable table = new PdfPTable(tam);
		
		//Definindo o rodape do relatorio
		for(Linha linha : relatorio.getRodape().getLinhas()) {
				font = new Font(Font.HELVETICA, 6, Font.NORMAL);
				popularCelulas(table, linha.getCelulas(), null, font);
		}
		table.setWidthPercentage(100);
		return table;
	}
	
	/**
	 * 
	 * criarComplementoRelatorio
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 25/08/2009
	 * @see
	 * @param relatorio
	 * @return
	 * @return Returns the PdfPTable.
	 */
	private PdfPTable criarComplementoRelatorio(RelatorioPDF relatorio) {
		
		PdfPTable table = new PdfPTable(1);
		//Definindo o complemento do relatorio
		for(Complemento complemento : relatorio.getComplementos()) {
			PdfPCell cell = popularLinhasComplemento(table, complemento.getLinhas());
			table.addCell(cell);
		}
		table.setWidthPercentage(100);
		return table;
	}
	
	/**
	 * 
	 * popularLinhasComplemento
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 25/08/2009
	 * @see
	 * @param table
	 * @param linhas
	 * @return Returns the void.
	 */
	private PdfPCell popularLinhasComplemento(PdfPTable table, List<Linha> linhas) {
		Font font;
		Paragraph p;
		
		PdfPTable linhaTable = new PdfPTable(1);
		for(Linha linha : linhas) {
			for(Celula celula : linha.getCelulas()) {
				linhaTable.getDefaultCell().setBorder(0);
				font = new Font(Font.HELVETICA, 6, celula.getEstiloFonte());
				p = new Paragraph();
				p.add(new Chunk(celula.getConteudoStr(), font));
				PdfPCell cell = new PdfPCell(p);
				cell.setBorder(0);
				cell.setHorizontalAlignment(celula.getAlinhamento());
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				linhaTable.addCell(cell);
			}
		}
		linhaTable.completeRow();
		PdfPCell tabelaColuna = new PdfPCell(linhaTable);
		return tabelaColuna;
	}
	

	/**
	 * 
	 * popularCelulas
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 01/07/2009
	 * @see
	 * @param table
	 * @param celulas
	 * @param color
	 * @return Returns the void.
	 */
	private void popularCelulas(PdfPTable table, List<Celula> celulas, Color color, Font font) {
		Paragraph p;
		for(Celula celula : celulas) {
			if (!celula.getColuna().isOculta()) {
				
				String valor = celula.getConteudoStr();
				if (celula.getConteudo() instanceof Texto) {
					Texto texto = (Texto)celula.getConteudo();
					font = texto.getFont();
					valor = texto.getConteudo();
				}
				
				p = new Paragraph();
				p.add(new Chunk(valor, font));
				PdfPCell cell = new PdfPCell(p);
				if (color != null) {
					cell.setBackgroundColor(color);
				}
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table.addCell(cell);
			}
		}
	}
	
	/**
	 * 
	 * @param table
	 * @param celulas
	 * @param color
	 * @param font
	 */
	private void popularCelulasGrupo(PdfPTable table, Grupo grupo, Color color, Font font) {
		Paragraph p;
		for(Celula celula : grupo.getTotalizacaoGrupo().getCelulas()) {
			
			if (!celula.getColuna().isOculta()) {
				
				String valor = celula.getConteudoStr();
				if (celula.getConteudo() instanceof Texto) {
					Texto texto = (Texto)celula.getConteudo();
					font = texto.getFont();
					valor = texto.getConteudo();
				}
				
				
				if (!grupo.getTiposCalculoGrupo().contains(celula.getColuna().getTipoCalculoColuna())) {
					valor = "";
				}
				if (celula.getColuna().isLabelTotalizacao()) {
					valor = grupo.getLabelGrupo();
					if (celula.getColuna().isMostraValorGrupo()) {
						valor += ": "+grupo.getValorGrupo();
					}
				}
				p = new Paragraph();
				p.add(new Chunk(valor, font));
				PdfPCell cell = new PdfPCell(p);
				if (color != null) {
					cell.setBackgroundColor(color);
				}
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table.addCell(cell);
			}
		}
	}
}