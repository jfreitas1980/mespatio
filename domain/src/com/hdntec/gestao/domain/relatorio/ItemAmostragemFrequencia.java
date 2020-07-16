package com.hdntec.gestao.domain.relatorio;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.ForeignKey;

import com.hdntec.gestao.domain.AuditTrail;
import com.hdntec.gestao.domain.produto.enums.TipoDeProdutoEnum;
import com.hdntec.gestao.util.BundleUtils;


@Entity
@SuppressWarnings("serial")
public class ItemAmostragemFrequencia extends AuditTrail {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "itemamostagem_seq")
    @SequenceGenerator(name = "itemamostagem_seq", sequenceName = "seqitemamostragem")
	private Long id;
	
	@ManyToOne
    @JoinColumn(name="codigo",nullable=false)
	private TabelaAmostragemFrequencia tabela;
	
	@Column(nullable = false)
    @Enumerated(EnumType.STRING)
	private TipoDeProdutoEnum tipoProduto;
	
	@Column(nullable = true, length = 30)
	private String codigoFamiliaTipoProduto;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SELECT)
	@ForeignKey(name = "fk_item_faixa", inverseName = "fk_faixa_item")
	@JoinTable(name = "item_faixaamostragem",
			joinColumns = @JoinColumn(name = "idItem"),
			inverseJoinColumns = @JoinColumn(name = "idFaixa"))
	private List<FaixaAmostragemFrequencia> listaFaixas;
	
	
	
	/**
	 * Get id
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 28/08/2009
	 */
	public Long getId() {
		return id;
	}
	/**
	 * Change id	
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param id 
	 * @since 28/08/2009
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * Get tabela
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 28/08/2009
	 */
	public TabelaAmostragemFrequencia getTabela() {
		return tabela;
	}
	/**
	 * Change tabela	
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param tabela 
	 * @since 28/08/2009
	 */
	public void setTabela(TabelaAmostragemFrequencia tabela) {
		this.tabela = tabela;
	}
	/**
	 * Get tipoProduto
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 25/08/2009
	 */
	public TipoDeProdutoEnum getTipoProduto() {
		return tipoProduto;
	}
	/**
	 * Change tipoProduto	
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param tipoProduto 
	 * @since 25/08/2009
	 */
	public void setTipoProduto(TipoDeProdutoEnum tipoProduto) {
		this.tipoProduto = tipoProduto;
	}
	/**
	 * Get codigoFamiliaTipoProduto
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 25/08/2009
	 */
	public String getCodigoFamiliaTipoProduto() {
		return codigoFamiliaTipoProduto;
	}
	/**
	 * Change codigoFamiliaTipoProduto	
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param codigoFamiliaTipoProduto 
	 * @since 25/08/2009
	 */
	public void setCodigoFamiliaTipoProduto(String codigoFamiliaTipoProduto) {
		this.codigoFamiliaTipoProduto = codigoFamiliaTipoProduto;
	}
	/**
	 * Get listaFaixas
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 25/08/2009
	 */
	public List<FaixaAmostragemFrequencia> getListaFaixas() {
		return listaFaixas;
	}
	/**
	 * Change listaFaixas	
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param listaFaixas 
	 * @since 25/08/2009
	 */
	public void setListaFaixas(List<FaixaAmostragemFrequencia> listaFaixas) {
		this.listaFaixas = listaFaixas;
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 5;
		result = prime
				* result
				+ ((codigoFamiliaTipoProduto == null) ? 0
						: codigoFamiliaTipoProduto.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((tabela == null) ? 0 : tabela.hashCode());
		result = prime * result
				+ ((tipoProduto == null) ? 0 : tipoProduto.hashCode());
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
		ItemAmostragemFrequencia other = (ItemAmostragemFrequencia) obj;
		if (codigoFamiliaTipoProduto == null) {
			if (other.codigoFamiliaTipoProduto != null)
				return false;
		} else if (!codigoFamiliaTipoProduto
				.equals(other.codigoFamiliaTipoProduto))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (tabela == null) {
			if (other.tabela != null)
				return false;
		} else if (!tabela.equals(other.tabela))
			return false;
		if (tipoProduto == null) {
			if (other.tipoProduto != null)
				return false;
		} else if (!tipoProduto.equals(other.tipoProduto))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(BundleUtils.getMessage("itemAmostragemFrequencia.tipoProduto"));
		result.append(": ").append(tipoProduto).append("\n");
		result.append(BundleUtils.getMessage("itemAmostragemFrequencia.familia"));
		result.append(": ").append(codigoFamiliaTipoProduto);
		return result.toString();
	}
}
