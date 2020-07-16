package com.hdntec.gestao.domain.produto.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.hdntec.gestao.domain.StatusEntity;


/**
 * É uma amostra do material. Essa amostra tem sua qualidade medida para ser utilizada como referência para decisões de blendagem.
 * <p>
 * Persite. - Tamanho: - Volume: muitos registros. - Período de Persistência: diariamente - Freq. Update: comum, diariamente. -Confiabilidade: deve ser confiável.
 * 
 * @author andre
 * 
 */
@Entity
public class Amostra extends StatusEntity<Amostra>
{

   /** id gerado para serializacao do objeto */
   private static final long serialVersionUID = 6499882382290808573L;

   /** identificador de amostra */
   private Long idAmostra;

   /** nome da amostra, que a identifica, usualmente informando a hora que foi medida */
   private String nomeAmostra;

   /** a lista de itens de controle desta qualidade */
   private List<ItemDeControleAmostra> listaDeItensDeControleAmostra;

      /**
    * Contrutor Padrao.
    */
   public Amostra()
   {
   }

   
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO, generator = "amostra_seq")
   @SequenceGenerator(name = "amostra_seq", sequenceName = "seqamostra")
   public Long getIdAmostra()
   {
      return idAmostra;
   }

   public void setIdAmostra(Long idAmostra)
   {
      this.idAmostra = idAmostra;
   }

   @Column(nullable = false, length = 60)
   public String getNomeAmostra()
   {
      return nomeAmostra;
   }

   public void setNomeAmostra(String nomeAmostra)
   {
      this.nomeAmostra = nomeAmostra;
   }
   
   //TODO Darley SAxxxx Trocando EAGER por LAZY
   @OneToMany(fetch = FetchType.LAZY, mappedBy = "amostra")
   @Fetch(FetchMode.SUBSELECT)
   @Cascade(value = CascadeType.ALL)
   public List<ItemDeControleAmostra> getListaDeItensDeControleAmostra()
   {
      if (listaDeItensDeControleAmostra == null)
      {
         listaDeItensDeControleAmostra = new ArrayList<ItemDeControleAmostra>();
      }
      return listaDeItensDeControleAmostra;
   }

   public void setListaDeItensDeControleAmostra(List<ItemDeControleAmostra> listaDeItensDeControleAmostra)
   {
      this.listaDeItensDeControleAmostra = listaDeItensDeControleAmostra;
   }

   @Transient
   public List<ItemDeControle> getListaDeItensDeControle()
   {
      List<ItemDeControle> result = new ArrayList<ItemDeControle>();
      result.addAll(getListaDeItensDeControleAmostra());
      return result;
   }

   public void setListaDeItensDeControle(List<ItemDeControle> listaDeItensDeControle)
   {
      for (ItemDeControle itemControle : listaDeItensDeControle)
      {
         ((ItemDeControleAmostra) itemControle).setAmostra(this);
         this.getListaDeItensDeControleAmostra().add((ItemDeControleAmostra) itemControle);
      }
   }

   @Override
   public String toString()
   {
      return nomeAmostra;
   }

}
