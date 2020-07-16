package br.com.cflex.samarco.supervision.stockyard.relatorio.controles;

import java.text.NumberFormat;

import com.lowagie.text.Element;
import com.lowagie.text.Font;

import br.com.cflex.samarco.supervision.stockyard.relatorio.enums.CalculoColunaEnum;


/**
 * 
 * <P><B>Description :</B><BR>
 * General Celula
 * </P>
 * <P>
 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
 * @since 15/06/2009
 * @version $Revision: 1.1 $
 */
public class Celula {

	private Linha linha;
	private Coluna coluna;
	private Object conteudo;
	private int alinhamento;
	private int estiloFonte;
	
	public Celula(Linha linha, Coluna coluna, Object conteudo) {
		this.linha = linha;
		this.coluna = coluna;
		this.conteudo = conteudo;
		this.alinhamento = Element.ALIGN_CENTER;
		this.estiloFonte = Font.NORMAL;
	}
	
	/**
	 * @return the linha
	 */
	public Linha getLinha() {
		return linha;
	}
	/**
	 * @param linha the linha to set
	 */
	public void setLinha(Linha linha) {
		this.linha = linha;
	}
	/**
	 * @return the coluna
	 */
	public Coluna getColuna() {
		return coluna;
	}
	/**
	 * @param coluna the coluna to set
	 */
	public void setColuna(Coluna coluna) {
		this.coluna = coluna;
	}
	/**
	 * @return the conteudo
	 */
	public Object getConteudo() {
		if (conteudo == null) {
			conteudo = "";
			if (!CalculoColunaEnum.NAO_CALCULA.equals(coluna.getTipoCalculoColuna())) {
				conteudo = "-";
			}
		}
		return conteudo;
	}
	/**
	 * @param conteudo the conteudo to set
	 */
	public void setConteudo(Object conteudo) {
		this.conteudo = conteudo;
	}
	
	/**
	 * Get alinhamento
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 25/08/2009
	 */
	public int getAlinhamento() {
		return alinhamento;
	}

	/**
	 * Change alinhamento	
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param alinhamento 
	 * @since 25/08/2009
	 */
	public void setAlinhamento(int alinhamento) {
		this.alinhamento = alinhamento;
	}

	/**
	 * Get estiloFonte
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 25/08/2009
	 */
	public int getEstiloFonte() {
		return estiloFonte;
	}

	/**
	 * Change estiloFonte	
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param estiloFonte 
	 * @since 25/08/2009
	 */
	public void setEstiloFonte(int estiloFonte) {
		this.estiloFonte = estiloFonte;
	}

	/**
	 * 
	 * getConteudoStr
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 22/06/2009
	 * @see
	 * @return
	 * @return Returns the String.
	 */
	public String getConteudoStr() {
		String result = "";
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		if (conteudo instanceof Double) {
			result = nf.format(conteudo);
		}
		else if (conteudo instanceof String) {
			result = conteudo.toString();
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((coluna == null) ? 0 : coluna.hashCode());
		result = prime * result + ((linha == null) ? 0 : linha.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Celula other = (Celula) obj;
		if (coluna == null) {
			if (other.coluna != null)
				return false;
		} else if (!coluna.equals(other.coluna))
			return false;
		if (linha == null) {
			if (other.linha != null)
				return false;
		} else if (!linha.equals(other.linha))
			return false;
		return true;
	}

	
}
