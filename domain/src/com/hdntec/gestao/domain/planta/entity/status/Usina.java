package com.hdntec.gestao.domain.planta.entity.status;

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
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.ForeignKey;

import com.hdntec.gestao.domain.StatusEntity;
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.planta.entity.MetaUsina;
import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;


@Entity
public class Usina extends StatusEntity<Usina>
{

   /** Serializa  do Objeto */
   private static final long serialVersionUID = -1038231345790374262L;

   private Long idUsina;

      /** estado de operação do Usina */
   private EstadoMaquinaEnum estado;

   /** a taxa de opera  da usina, em ton/h */
   private Double taxaDeOperacao;
   
   /** Taxa de gera o e Pellet Screening */
   private Double taxaGeracaoPellet;

   private MetaUsina metaUsina;

   private Atividade atividade;

   
   /**
    * Construtor Padrao
    */
   public Usina()
   {
   }


   @Id
   @GeneratedValue(strategy = GenerationType.AUTO, generator = "usina_seq")
   @SequenceGenerator(name = "usina_seq", sequenceName = "sequsina")
   public Long getIdUsina()
   {
      return idUsina;
   }

   @Transient
   public String getNomeUsina()
   {
      return getMetaUsina().getNomeUsina();
   }

   @ManyToOne
	@JoinColumn(name = "ID_META_USI", nullable = false, insertable = true)
	@ForeignKey(name = "fk_meta_usina")
   public MetaUsina getMetaUsina() {
	return metaUsina;
   }


public void setMetaUsina(MetaUsina metaUsina) {
	this.metaUsina = metaUsina;
}


@Column(nullable = false)
   @Enumerated(EnumType.STRING)
   public EstadoMaquinaEnum getEstado()
   {
      return estado;
   }

   @Column(nullable = false)
   public Double getTaxaDeOperacao()
   {
      return taxaDeOperacao;
   }

  /* @Transient
   public Campanha getCampanhaAtual(Date data)
   {
      return getMetaUsina().getCampanhaAtual(data);
   }
*/   
   public Campanha retornaUltimaCampanha()
   {
	   return getMetaUsina().getListaCampanhas().get(getMetaUsina().getListaCampanhas().size() - 1);
   }

   @Transient
   public Long getCodigoFaseProcessoUsina()
   {
      return getMetaUsina().getCodigoFaseProcessoUsina();
   }

   @Transient
   public Long getCdItemControleUsina()
   {
      return getMetaUsina().getCdItemControleUsina();
   }

   @Transient
   public String getCdTipoProcessoUsina()
   {
      return getMetaUsina().getCdTipoProcessoUsina();
   }

   @Transient
   public String getCdAreaRespEdUsina()
   {
      return getMetaUsina().getCdAreaRespEdUsina();
   }
   
  @Transient
   public Patio getPatioExpurgoPellet()
   {
      return null;
   }

   @Column(nullable = false)
   public Double getTaxaGeracaoPellet()
   {
      return taxaGeracaoPellet;
   }
   
   public void setIdUsina(Long idUsina)
   {
      this.idUsina = idUsina;
   }


   public void setEstado(EstadoMaquinaEnum estado)
   {
      this.estado = estado;
   }

   public void setTaxaDeOperacao(Double taxaDeOperacao)
   {
      this.taxaDeOperacao = taxaDeOperacao;
   }

   public void setPatioExpurgoPellet(Patio patioExpurgoPellet)
   {
      
   }

   public void setTaxaGeracaoPellet(Double taxaGeracaoPellet)
   {
      this.taxaGeracaoPellet = taxaGeracaoPellet;
   }

   

   @OneToOne
  /* @Cascade(value = {
		      CascadeType.SAVE_UPDATE})*/
   @ForeignKey(name = "fk_usina_atividade")
   @JoinColumn(name = "idAtividade", nullable = true)
   public Atividade getAtividade()
   {
      return atividade;
   }

   
  @Transient
  public Boolean isUsinaSelecionada() {
	// TODO Auto-generated method stub
	  Boolean result = Boolean.FALSE;
	  if (this.getAtividade() != null && this.getEstado().equals(EstadoMaquinaEnum.OPERACAO)) {
		  result= Boolean.TRUE;
	  }
	  return result;
}


public void setAtividade(Atividade atividade) {
	this.atividade = atividade;
}


@Override
public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result + ((atividade == null) ? 0 : atividade.hashCode());
	result = prime * result + ((estado == null) ? 0 : estado.hashCode());
	result = prime * result + ((idUsina == null) ? 0 : idUsina.hashCode());
	result = prime * result + ((metaUsina == null) ? 0 : metaUsina.hashCode());
	result = prime * result
			+ ((taxaDeOperacao == null) ? 0 : taxaDeOperacao.hashCode());
	result = prime * result
			+ ((taxaGeracaoPellet == null) ? 0 : taxaGeracaoPellet.hashCode());
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
	Usina other = (Usina) obj;
	if (atividade == null) {
		if (other.atividade != null)
			return false;
	} else if (atividade != null && other.atividade == null) {
	    return false;
	} else if (!atividade.equals(other.atividade))
		return false;
	if (estado == null) {
		if (other.estado != null)
			return false;
	} else if (!estado.equals(other.estado))
		return false;
	if (idUsina == null) {
		if (other.idUsina != null)
			return false;
	} else if (!idUsina.equals(other.idUsina))
		return false;
	if (metaUsina == null) {
		if (other.metaUsina != null)
			return false;
	} else if (!metaUsina.equals(other.metaUsina))
		return false;
	if (taxaDeOperacao == null) {
		if (other.taxaDeOperacao != null)
			return false;
	} else if (!taxaDeOperacao.equals(other.taxaDeOperacao))
		return false;
	if (taxaGeracaoPellet == null) {
		if (other.taxaGeracaoPellet != null)
			return false;
	} else if (!taxaGeracaoPellet.equals(other.taxaGeracaoPellet))
		return false;
	return true;
}

   
}
