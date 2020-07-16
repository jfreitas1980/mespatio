package com.hdntec.gestao.domain.produto.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.ForeignKey;

import com.hdntec.gestao.domain.ComparadorStatusEntity;
import com.hdntec.gestao.domain.StatusEntity;
import com.hdntec.gestao.domain.produto.enums.TipoMetaInternaEnum;


/**
 * 
 * Metas geradas anualmente pela equipe para cada item de controle de cada tipo
 * de produto de pelota. Na Samarco, ao calcular a blendagem, além de apresentar
 * a comparação com as metas da orientação de embarque do cliente, a meta de
 * produção e de embarque também devem estar em evidência.
 * 
 * @author andre
 * 
 */
@Entity
@Table(uniqueConstraints=
    @UniqueConstraint(columnNames={"id_tipoproduto", "idTipoItemCtr","dt_inicio","dt_fim","tipoDaMetaInterna"}))
public class MetaInterna extends StatusEntity<MetaInterna> {

	public static ComparadorStatusEntity<MetaInterna> comparadorStatusMeta = new ComparadorStatusEntity<MetaInterna>();
	   
	/** serialização do objeto */
	private static final long serialVersionUID = 7115000536198475210L;

	/** id da entidade */
	private Long idMetaInterna;

	/** o tipo de pelota desta meta interna */
	private TipoProduto tipoPelota;

	/** o tipo de item relacionado a este coeficiente de degradacao */
	private TipoItemDeControle tipoItemDeControle;

	/**
	 * o limite superior do valor da meta interna para este tipo de item de
	 * controle relacionado a este tipo de pelota
	 */
	private Double limiteSuperiorValorMetaInterna;

	/**
	 * o limite inferior do valor da meta interna para este tipo de item de
	 * controle relacionado a este tipo de pelota
	 */
	private Double limiteInferiorValorMetaInterna;

	/** o tipo desta meta interna */
	private TipoMetaInternaEnum tipoDaMetaInterna;

	/**
	 * Construtor padrão
	 */
	public MetaInterna() {
	}

	/**
	 * @param limiteInferiorValorMetaInterna
	 * @param tipoDaMetaInterna
	 * @param tipoPelota
	 * @param limiteSuperiorValorMetaInterna
	 * @param dataDeCadastro
	 */
	public MetaInterna(Double limiteInferior, Double limiteSuperior,
			TipoProduto tipoPelota, Date dataDeCadastro,
			TipoMetaInternaEnum tipoDaMetaInterna) {
		this.limiteInferiorValorMetaInterna = limiteInferior;
		this.tipoDaMetaInterna = tipoDaMetaInterna;
		this.tipoPelota = tipoPelota;
		this.limiteSuperiorValorMetaInterna = limiteSuperior;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(tipoPelota.getCodigoFamiliaTipoProduto()).append("-");
		builder.append(tipoPelota.getCodigoTipoProduto()).append(
				limiteInferiorValorMetaInterna).append("-").append(
				limiteSuperiorValorMetaInterna);
		return builder.toString();
	}

	/**
	 * @return the tipoPelota
	 */
	@OneToOne
	@ForeignKey(name = "fk_metaInt_tipoproduto")
	@JoinColumn(name = "id_tipoproduto", nullable = false)
	public TipoProduto getTipoPelota() {
		return tipoPelota;
	}

	/**
	 * @param tipoPelota
	 *            the tipoPelota to set
	 */
	public void setTipoPelota(TipoProduto tipoPelota) {
		this.tipoPelota = tipoPelota;
	}

	/**
	 * @return the idMetaInterna
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "metaInt_seq")
	@SequenceGenerator(name = "metaInt_seq", sequenceName = "seqmetaInt")
	public Long getIdMetaInterna() {
		return idMetaInterna;
	}

	/**
	 * @param idMetaInterna
	 *            the idMetaInterna to set
	 */
	public void setIdMetaInterna(Long idMetaInterna) {
		this.idMetaInterna = idMetaInterna;
	}

	/**
	 * @return the tipoDaMetaInterna
	 */
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	public TipoMetaInternaEnum getTipoDaMetaInterna() {
		return tipoDaMetaInterna;
	}

	/**
	 * @param tipoDaMetaInterna
	 *            the tipoDaMetaInterna to set
	 */
	public void setTipoDaMetaInterna(TipoMetaInternaEnum tipoDaMetaInterna) {
		this.tipoDaMetaInterna = tipoDaMetaInterna;
	}

	/**
	 * @return the limiteSuperiorValorMetaInterna
	 */
	@Column(nullable = true)
	public Double getLimiteSuperiorValorMetaInterna() {
		return limiteSuperiorValorMetaInterna;
	}

	/**
	 * @param limiteSuperiorValorMetaInterna
	 *            the limiteSuperiorValorMetaInterna to set
	 */
	public void setLimiteSuperiorValorMetaInterna(
			Double limiteSuperiorValorMetaInterna) {
		this.limiteSuperiorValorMetaInterna = limiteSuperiorValorMetaInterna;
	}

	/**
	 * @return the limiteInferiorValorMetaInterna
	 */
	@Column(nullable = true)
	public Double getLimiteInferiorValorMetaInterna() {
		return limiteInferiorValorMetaInterna;
	}

	/**
	 * @param limiteInferiorValorMetaInterna
	 *            the limiteInferiorValorMetaInterna to set
	 */
	public void setLimiteInferiorValorMetaInterna(
			Double limiteInferiorValorMetaInterna) {
		this.limiteInferiorValorMetaInterna = limiteInferiorValorMetaInterna;
	}

	@ManyToOne
	@ForeignKey(name = "fk_meta_tipoitem")
	@JoinColumn(name = "idTipoItemCtr", nullable = false)
	public TipoItemDeControle getTipoItemDeControle() {
		return tipoItemDeControle;
	}

	public void setTipoItemDeControle(TipoItemDeControle tipoItemDeControle) {
		this.tipoItemDeControle = tipoItemDeControle;
	}


	  @Transient
	   public Date getDataDeCadastro()
	   {
	      return this.getDtInicio();
	   }
	 
	  @Transient
      public void setDataDeCadastro(Date data)
      {
         this.setDtInicio(data);
      }
    
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((idMetaInterna == null) ? 0 : idMetaInterna.hashCode());
		result = prime
				* result
				+ ((limiteInferiorValorMetaInterna == null) ? 0
						: limiteInferiorValorMetaInterna.hashCode());
		result = prime
				* result
				+ ((limiteSuperiorValorMetaInterna == null) ? 0
						: limiteSuperiorValorMetaInterna.hashCode());
		result = prime
				* result
				+ ((tipoDaMetaInterna == null) ? 0 : tipoDaMetaInterna
						.hashCode());
		result = prime
				* result
				+ ((tipoItemDeControle == null) ? 0 : tipoItemDeControle
						.hashCode());
		result = prime * result
				+ ((tipoPelota == null) ? 0 : tipoPelota.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		MetaInterna other = (MetaInterna) obj;
		if (idMetaInterna == null) {
			if (other.idMetaInterna != null)
				return false;
		} else if (!idMetaInterna.equals(other.idMetaInterna))
			return false;
		if (limiteInferiorValorMetaInterna == null) {
			if (other.limiteInferiorValorMetaInterna != null)
				return false;
		} else if (!limiteInferiorValorMetaInterna
				.equals(other.limiteInferiorValorMetaInterna))
			return false;
		if (limiteSuperiorValorMetaInterna == null) {
			if (other.limiteSuperiorValorMetaInterna != null)
				return false;
		} else if (!limiteSuperiorValorMetaInterna
				.equals(other.limiteSuperiorValorMetaInterna))
			return false;
		if (tipoDaMetaInterna == null) {
			if (other.tipoDaMetaInterna != null)
				return false;
		} else if (!tipoDaMetaInterna.equals(other.tipoDaMetaInterna))
			return false;
		if (tipoItemDeControle == null) {
			if (other.tipoItemDeControle != null)
				return false;
		} else if (!tipoItemDeControle.equals(other.tipoItemDeControle))
			return false;
		if (tipoPelota == null) {
			if (other.tipoPelota != null)
				return false;
		} else if (!tipoPelota.equals(other.tipoPelota))
			return false;
		return true;
	}

}
