package br.com.cflex.samarco.supervision.stockyard.relatorio.controles;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

public class CabecalhoTabela {

	private Imagem imgLegenda;
	private Imagem imagem;
	private Linha linha;
	private List<Celula> celulas;
	
	public CabecalhoTabela(Linha linha) {
		this.imgLegenda = new Imagem(null);
		this.imagem = new Imagem(null);
		this.linha = linha;
	}
	
	public CabecalhoTabela(Image imgLegenda, Image img, Linha linha) {
		this.imgLegenda = new Imagem(imgLegenda);
		this.imagem = new Imagem(img);
		this.linha = linha;
	}
	
	/**
	 * 
	 * addCelula
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 25/06/2009
	 * @see
	 * @param celula
	 * @return Returns the void.
	 */
	public void addCelula(Celula celula) {
		if (celulas == null) {
			celulas = new ArrayList<Celula>();
		}
		celulas.add(celula);
	}
	
	/**
	 * 
	 * getCelulas
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 25/06/2009
	 * @see
	 * @return
	 * @return Returns the List<Celula>.
	 */
	public List<Celula> getCelulas() {
		if (celulas == null) {
			celulas = new ArrayList<Celula>();
		}
		return celulas;
	}

	/**
	 * 
	 * getImagem
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 25/06/2009
	 * @see
	 * @return
	 * @return Returns the Imagem.
	 */
	public Imagem getImagem() {
		return imagem;
	}

	/**
	 * 
	 * getLinha
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 25/06/2009
	 * @see
	 * @return
	 * @return Returns the Linha.
	 */
	public Linha getLinha() {
		return linha;
	}

	/**
	 * Get imgLegenda
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 25/06/2009
	 */
	public Imagem getImgLegenda() {
		return imgLegenda;
	}

	/**
	 * Change imgLegenda	
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param imgLegenda 
	 * @since 25/06/2009
	 */
	public void setImgLegenda(Imagem imgLegenda) {
		this.imgLegenda = imgLegenda;
	}

	public void setImagem(Imagem imagem) {
		this.imagem = imagem;
	}
	
}
