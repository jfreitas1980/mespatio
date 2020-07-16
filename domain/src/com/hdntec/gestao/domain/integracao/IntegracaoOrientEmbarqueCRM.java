/**
 * Classe responsavel em armazenar as informacoes da orientacao de embarque
 * de uma carga do navio lidas do CRM
 *
 * @author Rodrigo Luchetta
 */
package com.hdntec.gestao.domain.integracao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.ForeignKey;

@Entity
public class IntegracaoOrientEmbarqueCRM implements Serializable
{

   /** Serializacao do objeto */
   private static final long serialVersionUID = 791208626015355291L;

   /** Identificador sequencial da integracao */
   private Long idIntegracaoOrientEmbarqueCRM;

   /** Descricao da orientacao de embarque */
   private String descricaoOrientacaoEmbarque;

   /** Codigo do tipo de produto da orientacao de embarque */
   private String codigoTipoProduto;

   /** Descricao do tipo de produto da orientacao de embarque */
   private String descricaoTipoProduto;

   /** a lista de itens de controle da orientacao de embarque */
   private List<IntegracaoItemControleCRM> listaItensControleCRM;

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO, generator = "integracaooecrm_seq")
   @SequenceGenerator(name = "integracaooecrm_seq", sequenceName = "seqintegracaooecrm")
   public Long getIdIntegracaoOrientEmbarqueCRM()
   {
      return idIntegracaoOrientEmbarqueCRM;
   }

   @Column(nullable = false, length = 60)
   public String getDescricaoOrientacaoEmbarque()
   {
      return descricaoOrientacaoEmbarque;
   }

   @Column(nullable = true, length = 3)
   public String getCodigoTipoProduto()
   {
      return codigoTipoProduto;
   }

   @Column(nullable = true, length = 60)
   public String getDescricaoTipoProduto()
   {
      return descricaoTipoProduto;
   }
   //TODO Darley SA11079 Otimizando colocando LAZY
   @OneToMany(fetch = FetchType.LAZY)
   @Fetch(FetchMode.SELECT)
   @Cascade(CascadeType.ALL)
   @ForeignKey(name = "fk_integacaooe_itemcontrole", inverseName = "fk_itencontrole_integracaooe")
   @JoinTable(name = "IntegracaoOE_ItemControle", joinColumns = @JoinColumn(name = "idIntegracaoOrientEmbarqueCRM"), inverseJoinColumns = @JoinColumn(name = "idIntegracaoIcCRM"))
   public List<IntegracaoItemControleCRM> getListaItensControleCRM()
   {
      return listaItensControleCRM;
   }

   public void setIdIntegracaoOrientEmbarqueCRM(Long idIntegracaoOrientEmbarqueCRM)
   {
      this.idIntegracaoOrientEmbarqueCRM = idIntegracaoOrientEmbarqueCRM;
   }

   public void setDescricaoOrientacaoEmbarque(String descricaoOrientacaoEmbarque)
   {
      this.descricaoOrientacaoEmbarque = descricaoOrientacaoEmbarque;
   }

   public void setListaItensControleCRM(List<IntegracaoItemControleCRM> listaItensControleCRM)
   {
      this.listaItensControleCRM = listaItensControleCRM;
   }

   public void setCodigoTipoProduto(String codigoTipoProduto)
   {
      this.codigoTipoProduto = codigoTipoProduto;
   }

   public void setDescricaoTipoProduto(String descricaoTipoProduto)
   {
      this.descricaoTipoProduto = descricaoTipoProduto;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (obj == null)
      {
         return false;
      }
      if (getClass() != obj.getClass())
      {
         return false;
      }
      final IntegracaoOrientEmbarqueCRM other = (IntegracaoOrientEmbarqueCRM) obj;
      if (this.idIntegracaoOrientEmbarqueCRM != other.idIntegracaoOrientEmbarqueCRM && (this.idIntegracaoOrientEmbarqueCRM == null || !this.idIntegracaoOrientEmbarqueCRM.equals(other.idIntegracaoOrientEmbarqueCRM)))
      {
         return false;
      }
      if ((this.descricaoOrientacaoEmbarque == null) ? (other.descricaoOrientacaoEmbarque != null) : !this.descricaoOrientacaoEmbarque.equals(other.descricaoOrientacaoEmbarque))
      {
         return false;
      }
      if ((this.codigoTipoProduto == null) ? (other.codigoTipoProduto != null) : !this.codigoTipoProduto.equals(other.codigoTipoProduto))
      {
         return false;
      }
      if ((this.descricaoTipoProduto == null) ? (other.descricaoTipoProduto != null) : !this.descricaoTipoProduto.equals(other.descricaoTipoProduto))
      {
         return false;
      }
      return true;
   }

   @Override
   public int hashCode()
   {
      int hash = 7;
      hash = 79 * hash + (this.idIntegracaoOrientEmbarqueCRM != null ? this.idIntegracaoOrientEmbarqueCRM.hashCode() : 0);
      hash = 79 * hash + (this.descricaoOrientacaoEmbarque != null ? this.descricaoOrientacaoEmbarque.hashCode() : 0);
      hash = 79 * hash + (this.codigoTipoProduto != null ? this.codigoTipoProduto.hashCode() : 0);
      hash = 79 * hash + (this.descricaoTipoProduto != null ? this.descricaoTipoProduto.hashCode() : 0);
      return hash;
   }

}
