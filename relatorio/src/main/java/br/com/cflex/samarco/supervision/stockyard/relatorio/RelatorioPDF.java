package br.com.cflex.samarco.supervision.stockyard.relatorio;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lowagie.text.Element;
import com.lowagie.text.Font;

import br.com.cflex.samarco.supervision.stockyard.relatorio.controles.CabecalhoRelatorio;
import br.com.cflex.samarco.supervision.stockyard.relatorio.controles.CabecalhoTabela;
import br.com.cflex.samarco.supervision.stockyard.relatorio.controles.Celula;
import br.com.cflex.samarco.supervision.stockyard.relatorio.controles.Coluna;
import br.com.cflex.samarco.supervision.stockyard.relatorio.controles.Complemento;
import br.com.cflex.samarco.supervision.stockyard.relatorio.controles.Corpo;
import br.com.cflex.samarco.supervision.stockyard.relatorio.controles.Grupo;
import br.com.cflex.samarco.supervision.stockyard.relatorio.controles.Imagem;
import br.com.cflex.samarco.supervision.stockyard.relatorio.controles.Linha;
import br.com.cflex.samarco.supervision.stockyard.relatorio.controles.Rodape;
import br.com.cflex.samarco.supervision.stockyard.relatorio.controles.Texto;
import br.com.cflex.samarco.supervision.stockyard.relatorio.controles.TipoConteudo;
import br.com.cflex.samarco.supervision.stockyard.relatorio.enums.AlinhamentoEnum;
import br.com.cflex.samarco.supervision.stockyard.relatorio.enums.EstiloFonteEnum;

public class RelatorioPDF {
	
	private CabecalhoRelatorio cabecalhoRelatorio;
	private CabecalhoTabela cabecalhoTabela;
	private Corpo corpo;
	private Rodape rodape;
	private List<Complemento> complementos;
	private List<Linha> linhas;
	private List<Coluna> colunas;
	private int totalColunas = 0;
	/**Indica se o relatorio vai ser gerado em uma nova pagina*/
	private boolean geraNovaPagina = true;
	
	/**
	 * Construtor do Relatorio
	 * @param strTitulo
	 * @param subTitulo
	 * @param logo
	 */
	public RelatorioPDF(String strTitulo, String subTitulo, Image logo) {
		this.totalColunas = 0;		
		cabecalhoRelatorio = new CabecalhoRelatorio(strTitulo, subTitulo, logo);
	}
	
	/**
	 * @return the linhas
	 */
	protected List<Linha> getLinhas() {
		if (linhas == null) {
			linhas = new ArrayList<Linha>();
		}
		return linhas;
	}
	
	/**
	 * @return the colunas
	 */
	protected List<Coluna> getColunas() {
		if (colunas == null) {
			colunas = new ArrayList<Coluna>();
		}
		return colunas;
	}
		
	/**
	 * @return the titulo
	 */
	public CabecalhoRelatorio getCabecalhoRelatorio() {
		return cabecalhoRelatorio;
	}
	
	/**
	 * @return the totalColunas
	 */
	protected int getTotalColunas() {
		return totalColunas;
	}
	
	/**
	 * @return the cabecalhoTabela
	 */
	protected CabecalhoTabela getCabecalhoTabela() {
		return cabecalhoTabela;
	}
	
	/**
	 * @return the corpo
	 */
	protected Corpo getCorpo() {
		return corpo;
	}
	
	/**
	 * 
	 * getRodape
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 16/06/2009
	 * @see
	 * @return
	 * @return Returns the Rodape.
	 */
	public Rodape getRodape() {
		return rodape;
	}
	/**
	 * 
	 * isGeraNovaPagina
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 11/08/2009
	 * @see
	 * @return
	 * @return Returns the boolean.
	 */
	public boolean isGeraNovaPagina() {
		return geraNovaPagina;
	}
	/**
	 * 
	 * setGeraNovaPagina
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 11/08/2009
	 * @see
	 * @param geraNovaPagina
	 * @return Returns the void.
	 */
	public void setGeraNovaPagina(boolean geraNovaPagina) {
		this.geraNovaPagina = geraNovaPagina;
	}

	/**
	 * 
	 * setRodape
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 16/06/2009
	 * @see
	 * @param rodape
	 * @return Returns the void.
	 */
	protected void setRodape(Rodape rodape) {
		this.rodape = rodape;
	}

	protected void addLinha(Linha linha) {
		if (this.linhas == null) {
			this.linhas = new ArrayList<Linha>();
		}
		this.linhas.add(linha);
	}
	
	
	public boolean addColuna(Coluna coluna) {
		if (this.colunas == null) {
			this.colunas = new ArrayList<Coluna>();
		}
		if (validarTamanhoColuna(coluna)) {
			this.colunas.add(coluna);
			this.totalColunas++;
			return true;
		}
		return false;
	}
	
	/**
	 * Valida se no relatorio tem espaco para a coluna informada. 
	 * validarTamanhoColuna
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 20/06/2009
	 * @see
	 * @param coluna
	 * @return
	 * @return Returns the boolean.
	 */
	private boolean validarTamanhoColuna(Coluna coluna) {
		if (coluna.isOculta() || coluna.getTamanho()<=getEspacoRestanteRelatorio()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Retorna o espaco restante em porcentagem para adicionar novas colunas. 
	 * 
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 20/06/2009
	 * @see
	 * @return
	 * @return Returns the int.
	 */
	private float getEspacoRestanteRelatorio() {
		float total = 0;
		for(Coluna coluna : this.colunas) {
			if (!coluna.isOculta()) {
				total += coluna.getTamanho();
			}
		}
		return (100-total);
	}
	
	/**
	 * 
	 * ajustarTamanhoColuna
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 25/06/2009
	 * @see
	 * @param pos
	 * @return Returns the void.
	 */
	public void ajustarTamanhoColuna(int pos) {
		if (this.colunas != null && this.colunas.size()>0) {
			if (pos<0||pos>=this.colunas.size()){
				pos = colunas.size()-1;
			}
			float ajuste = colunas.get(pos).getTamanho()+getEspacoRestanteRelatorio();
			colunas.get(pos).setTamanho(ajuste);
		}
	}
	
	/**
	 * 
	 * getTamanhoColunas
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 20/06/2009
	 * @see
	 * @return
	 * @return Returns the float[].
	 */
	public float[] getTamanhoColunas() {
		List<Coluna> colunaList =  getColunasVisiveis();
		float[] result = new float[colunaList.size()];
			for(int i=0; i<colunaList.size();i++) {
				float tamanho = colunaList.get(i).getTamanho();
				result[i] =  tamanho / 100.00f;
			}

		return result;
	}
	/**
	 * 
	 * getColunasVisiveis
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 21/06/2009
	 * @see
	 * @return
	 * @return Returns the List<Coluna>.
	 */
	public List<Coluna> getColunasVisiveis() {
		List<Coluna> result = new ArrayList<Coluna>();
		for(Coluna coluna : colunas) {
			if (!coluna.isOculta()) {
				result.add(coluna);
			}
		}
		return result;
	}
	
	/**
	 * Metodo que configura o titulo, subtitulo e o logo do relatorio
	 * setTituloRelatorio
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 20/06/2009
	 * @see
	 * @param strTitulo
	 * @param subTitulo
	 * @param logo
	 * @return Returns the void.
	 */
	public void setTituloRelatorio(String strTitulo, String subTitulo, Image logo) {
		cabecalhoRelatorio.setTitulo(new Texto(strTitulo));
		cabecalhoRelatorio.setSubTitulo(new Texto(subTitulo));
		cabecalhoRelatorio.setLogo(new Imagem(logo));
	}
	
	/**
	 * Metodo que configura uma imagem no cabecalho do relatorio com uma legenda
	 * @param imgLegenda
	 * @param imgCabecalho
	 */
	public void setImagemCabecalhoRelatorio(Image imgLegenda, Image imgCabecalho) {
		configuraImagemCabecalho(imgLegenda, imgCabecalho);
	}
	
	/**
	 * Metodo que seta os dados do corpo do relatorio de acordo com as colunas informadas.
	 * setDadosRelatorio
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 25/06/2009
	 * @see
	 * @param imgLegenda
	 * @param imgCabecalho
	 * @param dados
	 * @return Returns the void.
	 */
	public void setDadosCorpoRelatorio(Map<Integer, List<Object>> dados) {
		
		if ( this.totalColunas > 0) {
			
			Grupo grupoPrincipal = null;
			
			if (this.corpo == null) {
				configuraCabecalhoTabela();
				this.corpo = new Corpo();
			}

			//Percorrendo a linha
			Collection<List<Object>> list = dados.values();
			for(List<Object> celulasValor : list) {

				//Criando a linha do grupo
				Linha linha = new Linha(this.getLinhas().size());
				this.addLinha(linha);
				
				//Obtendo o grupo pai
				int index = this.getCorpo().getGrupos().size();
				if (index>0) {
					grupoPrincipal = this.getCorpo().getGrupos().get(index-1);
				}
				Grupo grupoPai = grupoPrincipal;
				Grupo grupoFilho = grupoPai;
				Grupo grupoEncontrado = null;

				for (int posColuna=0; posColuna<this.totalColunas; posColuna++) {

					//Recuperando a coluna
					Coluna coluna = this.getColunas().get(posColuna);
					Object objConteudo = celulasValor.get(posColuna);
					//Criando uma nova celula para a linha e a coluna com um conteudo
					Celula celula = new Celula(linha,coluna,objConteudo);
					coluna.addCelula(celula);
					linha.addCelula(celula);

					
					if (coluna.getGrupo()>0) {
						grupoEncontrado = null;
						//Verificando em qual nivel de grupo procurar
						while( grupoFilho != null && grupoFilho.getIdGrupo()!=coluna.getGrupo()) {
							grupoPai = grupoFilho;
							if (grupoFilho.getSubGrupos().size()>0) {
								grupoFilho = grupoFilho.getSubGrupos().get(0);
							}
							else { 
								grupoFilho = null;
							}
						}
 
						if (grupoFilho != null) {
						if (grupoFilho.equals(grupoPai)) {
							for(Grupo grupo : this.corpo.getGrupos()) {
								if (grupo.getValorGrupo().equals(String.valueOf(objConteudo))) {
									grupoPai = grupo;
									grupoFilho = grupoPai;
									grupoEncontrado = grupo;
									break;
								}
							}
						}
						else {
							for(Grupo grupo : grupoPai.getSubGrupos()) {
								if (grupo.getValorGrupo().equals(String.valueOf(objConteudo))) {
									grupoEncontrado = grupo;
									break;
								}
							}
						}
						}
						//Verificando se encontrou o grupo
						if (grupoEncontrado == null) {
							grupoEncontrado = new Grupo();
							grupoEncontrado.setIdGrupo(coluna.getGrupo());
							grupoEncontrado.setTiposCalculoGrupo(coluna.getTipoCalculoGrupo());
							grupoEncontrado.setValorGrupo(String.valueOf(objConteudo));
							grupoEncontrado.setLabelGrupo(coluna.getLabelGrupo());
							if (grupoPai==null || grupoPai.getIdGrupo()==grupoEncontrado.getIdGrupo()) {
								this.corpo.addGrupo(grupoEncontrado);
								grupoPai = grupoEncontrado;
								grupoFilho = grupoPai;
							}
							else {
								grupoPai.addSubGrupo(grupoEncontrado);
							}
						}
					}
				}
				grupoEncontrado.addLinha(linha);
			}
			this.corpo.calculaGrupos(this.getLinhas().size());
		}
	}
	
	/**
	 * Configura os dados do Rodape
	 * @param dados
	 */
	public void setDadosRodape(Map<Integer, List<Object>> dados) {
		if ( this.totalColunas > 0 && dados != null) {

			this.rodape = new Rodape();
			//Percorrendo a linha
			Collection<List<Object>> list = dados.values();
			for(List<Object> celulasValor : list) {
				//Criando a linha do grupo
				Linha linha = new Linha(this.getLinhas().size());
				this.addLinha(linha);
				this.rodape.addLinha(linha);

				for (int posColuna=0; posColuna<this.totalColunas; posColuna++) {
					//Recuperando a coluna
					Coluna coluna = this.getColunas().get(posColuna);
					Object objConteudo = celulasValor.get(posColuna);
					//Criando uma nova celula para a linha e a coluna com um conteudo
					Celula celula = new Celula(linha,coluna,objConteudo);
					coluna.addCelula(celula);
					linha.addCelula(celula);
				}
			}
		}
	}
	
	
	/**
	 * Get complementos
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 25/08/2009
	 */
	public List<Complemento> getComplementos() {
		if (complementos == null) {
			complementos = new ArrayList<Complemento>();
		}
		return complementos;
	}

	/**
	 * 
	 * addComplementoRelatorio
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 25/08/2009
	 * @see
	 * @param dados
	 * @return Returns the void.
	 */
	public void addComplementoRelatorio(List<Object> dados, List<TipoConteudo> formato) {
		if (complementos == null) {
			complementos = new ArrayList<Complemento>();
		}
		Complemento complemento = new Complemento();
		complementos.add(complemento);
		//Criando uma coluna propria para o complemento
		Coluna coluna = new Coluna(1000,"Complemento");
		int i = 0; 
		for(Object obj : dados) {
			
			TipoConteudo tipoConteudo = formato.get(i++);
			
			//Criando a linha do complemento
			Linha linha = new Linha(this.getLinhas().size());
			//Criando a celula para receber a informacao
			Celula celula = new Celula(linha,coluna,obj);
			int alinhamento = 1;
			if (tipoConteudo.getAlinhamento().equals(AlinhamentoEnum.CENTRALIZADO)) {
				alinhamento = Element.ALIGN_CENTER;
			}
			else if (tipoConteudo.getAlinhamento().equals(AlinhamentoEnum.DIREITA)) {
				alinhamento = Element.ALIGN_RIGHT;
			}
			else if (tipoConteudo.getAlinhamento().equals(AlinhamentoEnum.ESQUERDA)) {
				alinhamento = Element.ALIGN_LEFT;
			}
			celula.setAlinhamento(alinhamento);
			int font = 0;
			if (tipoConteudo.getEstiloFonte().equals(EstiloFonteEnum.NORMAL)) {
				font = Font.NORMAL;
			}
			else if (tipoConteudo.getEstiloFonte().equals(EstiloFonteEnum.NEGRITO)) {
				font = Font.BOLD;
			}
			else if (tipoConteudo.getEstiloFonte().equals(EstiloFonteEnum.ITALICO)) {
				font = Font.ITALIC;
			}
			celula.setEstiloFonte(font);
			
			linha.addCelula(celula);
			this.addLinha(linha);
			complemento.addLinha(linha);
		}
	}
	
	
	/*private Grupo validarNovoGrupo(Grupo grupo,
			List<Object> celulasValor,
			Object[] valoresOrdenacao) {

		for (int i = 0; i<this.totalColunas;i++) {
			if (valoresOrdenacao[i] != null) {
				if (!celulasValor.get(i).equals(valoresOrdenacao[i])) {
					
					boolean totalizar = CalculoColunaEnum.SOMA.equals(this.colunas.get(i).getCalculo());
					calcularMediaGrupo(grupo,getLabelMedia(valoresOrdenacao));
					this.corpo.addGrupo(grupo);
					if (totalizar) {
						calcularTotalGrupos(grupo,valoresOrdenacao[i]);
					}
					//Cria um novo grupo
					Grupo novoGrupo = new Grupo();
					if (totalizar) {
						grupoTotalizacao++;
					}
					novoGrupo.setGrupoTotalizacao(grupoTotalizacao);
					return novoGrupo;
				}
			}
		}
		return grupo;
	}*/
	
	
	/*private String getLabelMedia(Object[] valoresOrdenacao) {
		String result = "";
		for (int i = 0; i<this.totalColunas;i++) {
			if (valoresOrdenacao[i] != null) {
				if (CalculoColunaEnum.MEDIA.equals(this.colunas.get(i).getCalculo())) {
					result = valoresOrdenacao[i].toString();
					break;
				}
			}		
		}
		return result;
	}*/
	

	/*private void calcularMediaGrupo(Grupo grupo, Object identificador) {
		Linha linha = new Linha();
		linha.setNumero(this.getLinhas().size());
		this.addLinha(linha);
		//Calculando a media
		Media media = new Media();
		media.setLinha(linha);
		Map<Integer,Object> mediaMap = grupo.calculaMedia();
		for(int i=0; i< this.totalColunas; i++) {
			Coluna coluna = this.getColunas().get(i);
			Object obj = mediaMap.get(coluna.getNumero());
			if (coluna.isLabelTotalizacao()) {
				obj = BundleUtils.getMessage("relatorio.label.total")+" "+identificador;
			}
			Celula celulaMedia = new Celula(linha,coluna,obj);
			linha.addCelula(celulaMedia);
			coluna.addCelula(celulaMedia);
			media.addCelula(celulaMedia);
		}
		grupo.setMedia(media);		
	}*/
	
	
	/*private void calcularTotalGrupos(Grupo grupo, Object identificador) {

		//Calcula a soma do grupo
		Linha linhaSoma = new Linha();
		linhaSoma.setNumero(this.getLinhas().size());
		this.addLinha(linhaSoma);
		Totalizacao totalizacao = new Totalizacao();
		totalizacao.setLinha(linhaSoma);
		Map<Integer,Object> somaMap = this.corpo.somaGrupos(grupo.getGrupoTotalizacao());
		for(int i=0; i< this.totalColunas; i++) {
			Coluna coluna = this.getColunas().get(i);
			Object obj = somaMap.get(coluna.getNumero());
			if (coluna.isLabelTotalizacao()) {
				obj = BundleUtils.getMessage("relatorio.label.totalcampanha")+" "+identificador;
			}
			Celula celulaSoma = new Celula(linhaSoma,coluna,obj);
			linhaSoma.addCelula(celulaSoma);
			coluna.addCelula(celulaSoma);
			totalizacao.addCelula(celulaSoma);
		}
		grupo.setTotal(totalizacao);
	}*/
	
	private void configuraImagemCabecalho(Image imgLegenda, Image imgCabecalho) {
		if (cabecalhoTabela == null) {
			//Adicionando a linha do cabecalho
			Linha linha = new Linha(this.getLinhas().size());
			this.addLinha(linha);
			//Criando o cabecalho
			this.cabecalhoTabela = new CabecalhoTabela(imgLegenda, imgCabecalho,linha);
		}
		else {
			cabecalhoTabela.setImgLegenda(new Imagem(imgLegenda));
			cabecalhoTabela.setImagem(new Imagem(imgCabecalho));
		}
	}
	private void configuraCabecalhoTabela() {
		Linha linha = null;
		if (cabecalhoTabela == null) {
			linha = new Linha(this.getLinhas().size());
			this.addLinha(linha);
			//Criando o cabecalho
			this.cabecalhoTabela = new CabecalhoTabela(linha);
		}
		linha = cabecalhoTabela.getLinha();
		//Criando as colunas do relatorio e as celulas do cabecalho
		for (int i=0; i<this.totalColunas; i++) {
			Coluna coluna = this.colunas.get(i);
			Celula celula = new Celula(linha,coluna,coluna.getTitulo());
			coluna.addCelula(celula);
			this.cabecalhoTabela.addCelula(celula);
		}
	}
}
