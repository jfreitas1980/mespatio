package com.hdntec.gestao.domain.produto.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.ForeignKey;

import com.hdntec.gestao.domain.StatusEntity;


/**
 * A qualidade do produto são as medidas de suas propriedades físicas, químicas e metalúrgicas. Cada uma dessas propriedades é chamada de {@link ItemDeControle}.
 * <p>
 * Persite. - Tamanho: - Volume: muitos registros. - Período de Persistência: diariamente. - Freq. Update: comum, varias vezes ao dia. -Confiabilidade: deve ser confiável.
 * 
 * @author andre
 * 
 */
@Entity
public class Qualidade extends StatusEntity<Qualidade>
{



/** serial gerado automaticamente */
   private static final long serialVersionUID = 6647910663887876432L;

   /** identificador de qualidade */
   private Long idQualidade;
   private Produto produto;
   
   /** se é real ou estimada */
   private Boolean ehReal;

   /** a lista de amostras dessa qualidade */
   private List<Amostra> listaDeAmostras;

   /** a lista de itens de controle desta qualidade */
   private List<ItemDeControleQualidade> listaDeItensDeControleQualidade;

   
   /**
    * Construtor Padrao
    */
   public Qualidade()
   {
   }

 
   /**
    * @return the idQualidade
    */
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO, generator = "qualidade_seq")
   @SequenceGenerator(name = "qualidade_seq", sequenceName = "seqqualidade")
   public Long getIdQualidade()
   {
      return idQualidade;
   }

   /**
    * @param idQualidade
    *           the idQualidade to set
    */
   public void setIdQualidade(Long idQualidade)
   {
      this.idQualidade = idQualidade;
   }

   /**
    * @return the ehReal
    */
   @Column(nullable = false)
   public Boolean getEhReal()
   {
      return ehReal;
   }

   /**
    * @param ehReal
    *           the ehReal to set
    */
   public void setEhReal(Boolean ehReal)
   {
      this.ehReal = ehReal;
   }

   //TODO Darley SAxxxx Trocando EAGER por LAZY
   @ManyToMany(fetch = FetchType.LAZY, mappedBy = "qualidade",targetEntity=ItemDeControleQualidade.class)
                   @Cascade(value = {CascadeType.SAVE_UPDATE,CascadeType.DELETE_ORPHAN,CascadeType.DELETE})   
   public List<ItemDeControleQualidade> getListaDeItensDeControleQualidade()
   {
      if (listaDeItensDeControleQualidade == null)
      {
         listaDeItensDeControleQualidade = new ArrayList<ItemDeControleQualidade>();
      }
      return listaDeItensDeControleQualidade;
   }

   public void setListaDeItensDeControleQualidade(List<ItemDeControleQualidade> listaDeItensDeControleQualidade)
   {
      this.listaDeItensDeControleQualidade = listaDeItensDeControleQualidade;
   }

   @Transient
   public List<ItemDeControle> getListaDeItensDeControle()
   {
      List<ItemDeControle> result = new ArrayList<ItemDeControle>();
      result.addAll(getListaDeItensDeControleQualidade());
      return result;
   }

   /**
    * @param listaDeItensDeControle
    *           the listaDeItensDeControle to set
    */
   public void setListaDeItensDeControle(List<ItemDeControle> listaDeItensDeControle)
   {
      for (ItemDeControle itemControle : listaDeItensDeControle)
      {
         ((ItemDeControleQualidade) itemControle).setQualidade(this);
         this.getListaDeItensDeControleQualidade().add((ItemDeControleQualidade) itemControle);
      }
   }

   //TODO Darley SAxxxx Trocando EAGER por LAZY
   @OneToMany(fetch = FetchType.LAZY)
   @Fetch(FetchMode.SELECT)
   @Cascade(value = CascadeType.ALL)
   @ForeignKey(name = "fk_qualidade_amostra", inverseName = "fk_amostra_qualidade")
   @JoinTable(name = "Qualidade_Amostra", joinColumns = @JoinColumn(name = "idQualidade"), inverseJoinColumns = @JoinColumn(name = "idAmostra"))
   public List<Amostra> getListaDeAmostras()
   {
      return listaDeAmostras;
   }

   public void setListaDeAmostras(List<Amostra> listaDeAmostras)
   {
      this.listaDeAmostras = listaDeAmostras;
   }

   @Override
   public String toString()
   {
      if (ehReal)
      {
         return ("Qualidade real");
      }
      else
      {
         return ("Qualidade esperando resultado bi-horário");
      }
   }


@Override
public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((ehReal == null) ? 0 : ehReal.hashCode());
    result = prime * result + ((idQualidade == null) ? 0 : idQualidade.hashCode());
    result = prime * result + ((listaDeAmostras == null) ? 0 : listaDeAmostras.hashCode());
    result = prime * result + ((listaDeItensDeControleQualidade == null) ? 0 : listaDeItensDeControleQualidade.hashCode());
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
    Qualidade other = (Qualidade) obj;
    if (ehReal == null) {
        if (other.ehReal != null)
            return false;
    } else if (!ehReal.equals(other.ehReal))
        return false;
    if (idQualidade == null) {
        if (other.idQualidade != null)
            return false;
    } else if (!idQualidade.equals(other.idQualidade))
        return false;
    if (listaDeAmostras == null) {
        if (other.listaDeAmostras != null)
            return false;
    } else if (!listaDeAmostras.equals(other.listaDeAmostras))
        return false;
    if (listaDeItensDeControleQualidade == null) {
        if (other.listaDeItensDeControleQualidade != null)
            return false;
    } else if (!listaDeItensDeControleQualidade.equals(other.listaDeItensDeControleQualidade))
        return false;
    return true;
}


/*@OneToOne//(mappedBy="orientacaoDeEmbarque")   
@Cascade(value = { CascadeType.SAVE_UPDATE })
@ForeignKey(name = "fk_qualidade_produto")
@JoinColumn(name = "id_Produto")
public Produto getProduto() {
	return this.produto; 
}*/


@OneToOne//(mappedBy="orientacaoDeEmbarque")  
//@Cascade(value = { CascadeType.SAVE_UPDATE })
@ForeignKey(name = "fk_qualidade_produto")
@JoinColumn(name = "id_Produto",nullable=true,updatable=false)
public Produto getProduto() {
    return this.produto;
}


public void setProduto(Produto produto) {
	this.produto = produto;
}
}