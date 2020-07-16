package br.com.cflex.samarco.supervision.stockyard.relatorio.controles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.cflex.samarco.supervision.stockyard.relatorio.enums.CalculoColunaEnum;

public class Coluna {

	private String titulo;
	private int numero;
	private List<Celula> celulas;
	private float tamanho;
	private CalculoColunaEnum tipoCalculoColuna;
	private boolean oculta;
	private int grupo;
	private boolean labelTotalizacao;
	private List<CalculoColunaEnum> tipoCalculoGrupo;
	private String labelGrupo;
	private boolean mostraValorGrupo;

	/**
	 * Construtor com o numero da coluna e o seu titulo
	 * @param numero
	 * @param titulo
	 */
	public Coluna(int numero, String titulo) {
		this.numero = numero;
		this.titulo = titulo;
		this.tipoCalculoColuna = CalculoColunaEnum.NAO_CALCULA;
		this.oculta = false;
		this.grupo = 1;
		this.labelTotalizacao = false;
		this.tipoCalculoGrupo = new ArrayList<CalculoColunaEnum>();
	}
	
	/**
	 * Construtor com todos os parametros para criar a coluna
	 * @param numero - Numero identificador da coluna
	 * @param titulo - Titulo da coluna
	 * @param tamanho - tamanho da coluna em % o total de todas as colunas tem que ser 100
	 * @param calculo - indica se a coluna vai ser calculada por grupo
	 * @param oculta - indica se a coluna deve aparecer ou nao.
	 * @param numeroGrupo - indica se a coluna define um novo grupo(acima de 0) ou nao(0)
	 * @param tiposCalculoGrupo - indica quais os calculos que o grupo deve mostrar
	 * @param labelTotalizacao - indica se na totalizacao do grupo vai ter um label de totalizacao
	 */
	public Coluna(int numero, String titulo, 
			float tamanho, CalculoColunaEnum calculo,
			boolean oculta, int numeroGrupo,
			CalculoColunaEnum[] tiposCalculoGrupo,
			boolean labelTotalizacao) {
		this.titulo = titulo;
		this.numero = numero;
		this.tamanho = tamanho;
		this.tipoCalculoColuna = calculo;
		this.oculta = oculta;
		this.grupo = numeroGrupo;
		this.labelTotalizacao = labelTotalizacao;
		if (tiposCalculoGrupo == null || tiposCalculoGrupo.length<=0) {
			this.tipoCalculoGrupo = new ArrayList<CalculoColunaEnum>();
		}
		else {
			this.tipoCalculoGrupo = Arrays.asList(tiposCalculoGrupo);
		}
		this.labelGrupo = "";
	}
	
	/**
	 * Construtor com todos os parametros para criar a coluna
	 * @param numero - Numero identificador da coluna
	 * @param titulo - Titulo da coluna
	 * @param tamanho - tamanho da coluna em % o total de todas as colunas tem que ser 100
	 * @param calculo - indica se a coluna vai ser calculada por grupo
	 * @param oculta - indica se a coluna deve aparecer ou nao.
	 * @param numeroGrupo - indica se a coluna define um novo grupo(acima de 0) ou nao(0)
	 * @param tiposCalculoGrupo - indica quais os calculos que o grupo deve mostrar
	 * @param labelTotalizacao - indica se na totalizacao do grupo vai ter um label de totalizacao
	 * @param label - o valor do label do grupo
	 */
	public Coluna(int numero, String titulo, 
			float tamanho, CalculoColunaEnum calculo,
			boolean oculta, int numeroGrupo,
			CalculoColunaEnum[] tiposCalculoGrupo,
			boolean labelTotalizacao,
			String label) {
		
		this(numero,titulo,tamanho,calculo,oculta,numeroGrupo,tiposCalculoGrupo,labelTotalizacao);
		this.labelGrupo = label;
	}
	
	
	/**
	 * @return the numero
	 */
	public int getNumero() {
		return numero;
	}
	/**
	 * @return the celulas
	 */
	public List<Celula> getCelulas() {
		if (celulas == null) {
			celulas = new ArrayList<Celula>();
		}
		return celulas;
	}
	
	public void addCelula(Celula celula) {
		if (celulas == null) {
			celulas = new ArrayList<Celula>();
		}
		this.celulas.add(celula);
	}
	
	/**
	 * @return the tamanho
	 */
	public float getTamanho() {
		return tamanho;
	}
	/**
	 * @param tamanho the tamanho to set
	 */
	public void setTamanho(float tamanho) {
		this.tamanho = tamanho;
	}
	
	/**
	 * Get oculta
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 20/06/2009
	 */
	public boolean isOculta() {
		return oculta;
	}

	/**
	 * Change oculta	
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param oculta 
	 * @since 20/06/2009
	 */
	public void setOculta(boolean oculta) {
		this.oculta = oculta;
	}

	/**
	 * Get grupo
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 20/06/2009
	 */
	public int getGrupo() {
		return grupo;
	}

	/**
	 * Change grupo	
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param grupo 
	 * @since 20/06/2009
	 */
	public void setGrupo(int grupo) {
		this.grupo = grupo;
	}

	/**
	 * Get titulo
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 20/06/2009
	 */
	public String getTitulo() {
		return titulo;
	}

	/**
	 * Get labelTotalizacao
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 01/07/2009
	 */
	public boolean isLabelTotalizacao() {
		return labelTotalizacao;
	}

	/**
	 * @return the tipoCalculoColuna
	 */
	public CalculoColunaEnum getTipoCalculoColuna() {
		if (tipoCalculoColuna==null) {
			tipoCalculoColuna = CalculoColunaEnum.NAO_CALCULA;
		}
		return tipoCalculoColuna;
	}

	/**
	 * @return the tipoCalculoGrupo
	 */
	public List<CalculoColunaEnum> getTipoCalculoGrupo() {
		return tipoCalculoGrupo;
	}
	/**
	 * 
	 * @return
	 */
	public String getLabelGrupo() {
		return labelGrupo;
	}
	/**
	 * 
	 * isMostraValorGrupo
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 11/08/2009
	 * @see
	 * @return
	 * @return Returns the boolean.
	 */
	public boolean isMostraValorGrupo() {
		return mostraValorGrupo;
	}
	/**
	 * 
	 * setMostraValorGrupo
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 11/08/2009
	 * @see
	 * @param mostraValorGrupo
	 * @return Returns the void.
	 */
	public void setMostraValorGrupo(boolean mostraValorGrupo) {
		this.mostraValorGrupo = mostraValorGrupo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + numero;
		result = prime * result + ((titulo == null) ? 0 : titulo.hashCode());
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
		Coluna other = (Coluna) obj;
		if (numero != other.numero)
			return false;
		if (titulo == null) {
			if (other.titulo != null)
				return false;
		} else if (!titulo.equals(other.titulo))
			return false;
		return true;
	}
}
