package com.hdntec.gestao.domain.navios.entity.status;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
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
import com.hdntec.gestao.domain.produto.entity.ItemDeControle;
import com.hdntec.gestao.domain.produto.entity.ItemDeControleOrientacaoEmbarque;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;


/**
 * A orientação de embarque de uma determinada carga especifica a quantidade e a qualidade do produto esperado pelo
 * cliente que receberao o embarque, principalmente o valor meta de cada item de controle para que a carga esteja em
 * conformidade com o pedido.
 *
 * @author andre
 */
@Entity
public class OrientacaoDeEmbarque extends StatusEntity<OrientacaoDeEmbarque>
{

   /** Serializacao do objeto */
   private static final long serialVersionUID = 5683537127664821393L;

   /** valor identificador de orientacao de embarque */
   private Long idOrientacaoEmbarque;

   /** se ha penalizacao para o descumprimento deste orientação de embarque */
   private Boolean penalizacao;

   /** a carga que possui esta orientação de embarque */
   //private Carga carga;

   /** a lista de itens de controle desta orientação de embarque */
   private List<ItemDeControleOrientacaoEmbarque> listaItemDeControleOrientacaoEmbarque;

   /** o tipo de produto desta orientação de embarque*/
   private TipoProduto tipoProduto;

   /** a quantidade de material necessÃ¡ria para atender a esta orientação de embarque */
   private Double quantidadeNecessaria;


   public OrientacaoDeEmbarque()
   {
   }


   @Id
   @GeneratedValue(strategy = GenerationType.AUTO, generator = "orientacaoembarque_seq")
   @SequenceGenerator(name = "orientacaoembarque_seq", sequenceName = "seqorientacaoembarque")
   public Long getIdOrientacaoEmbarque()
   {
      return idOrientacaoEmbarque;
   }

   @Column
   public Boolean getPenalizacao()
   {
      return penalizacao;
   }

  /* @OneToOne
   //@Cascade(value ={CascadeType.SAVE_UPDATE})
   @JoinColumn(name="idCarga")
   public Carga getCarga()
   {
      return carga;
   }*/

   @Transient
   public List<ItemDeControle> getListaItemDeControle()
   {
      List<ItemDeControle> result = new ArrayList<ItemDeControle>();
      result.addAll(getListaItemDeControleOrientacaoEmbarque());
      return result;
   }

   public void setListaItemDeControle(List<ItemDeControle> listaItemDeControle)
   {
      for (ItemDeControle itemControle : listaItemDeControle)
      {
         ((ItemDeControleOrientacaoEmbarque) itemControle).setOrientacao(this);
         this.getListaItemDeControleOrientacaoEmbarque().add((ItemDeControleOrientacaoEmbarque) itemControle);
      }
   }
   //TODO Darley SAxxxx Trocando EAGER por LAZY
   @OneToMany(fetch = FetchType.LAZY, mappedBy = "orientacao")
   @Fetch(FetchMode.SUBSELECT)
   @Cascade(value = {CascadeType.SAVE_UPDATE,CascadeType.DELETE,CascadeType.DELETE_ORPHAN})   
   // @Cascade(value = {CascadeType.ALL, CascadeType.DELETE_ORPHAN})
   public List<ItemDeControleOrientacaoEmbarque> getListaItemDeControleOrientacaoEmbarque()
   {
      if (listaItemDeControleOrientacaoEmbarque == null)
      {
         listaItemDeControleOrientacaoEmbarque = new ArrayList<ItemDeControleOrientacaoEmbarque>();
      }
      return listaItemDeControleOrientacaoEmbarque;
   }

   public void setListaItemDeControleOrientacaoEmbarque(
         List<ItemDeControleOrientacaoEmbarque> listaItemDeControleOrientacaoEmbarque)
   {
      this.listaItemDeControleOrientacaoEmbarque = listaItemDeControleOrientacaoEmbarque;
   }

   @OneToOne
   @ForeignKey(name = "fk_orientEmbarq_tipoProduto")
   @JoinColumn(name = "id_TipoProduto")
   public TipoProduto getTipoProduto()
   {
      return tipoProduto;
   }

   public void setCarga(Carga carga)
   {
     // this.carga = carga;
   }

   public void setIdOrientacaoEmbarque(Long idOrientacaoEmbarque)
   {
      this.idOrientacaoEmbarque = idOrientacaoEmbarque;
   }

   public void setPenalizacao(Boolean penalizacao)
   {
      this.penalizacao = penalizacao;
   }

   public void setTipoProduto(TipoProduto tipoProduto)
   {
      this.tipoProduto = tipoProduto;
   }

   @Column(nullable = false)
   public Double getQuantidadeNecessaria()
   {
      return quantidadeNecessaria;
   }

   public void setQuantidadeNecessaria(Double quantidadeNecessaria)
   {
      this.quantidadeNecessaria = quantidadeNecessaria;
   }


}
