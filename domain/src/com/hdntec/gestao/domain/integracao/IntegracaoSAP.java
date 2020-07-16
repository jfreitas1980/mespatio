package com.hdntec.gestao.domain.integracao;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class IntegracaoSAP implements Serializable
{

   private static final long serialVersionUID = 1L;

   /** identificador sequencial da integracao */
   private Long idIntegracaoSAP;

   /** Codigo do SAP referente ao estoque de produtos */
   private Long cd_SAP_Dado;

   /** Identificador do grupo do SAP (Sempre 1 ) */
   private Long cd_SAP_Grupo;

   /** titulo do grupo do dado */
   private String titulo;

   /** data da exportacao do dado pelo SAP */
   private Date dataLeitura;

   /** Valor do Dado exportado pelo SAP */
   private Double valorEstoque;

   /** Flag de registro processado */
   private Boolean processado;

   /** Codigo do tipo de produto */
   private String codigoTipoProduto;

   @Column(nullable = false)
   public Long getCd_SAP_Dado()
   {
      return cd_SAP_Dado;
   }

   @Column(nullable = false)
   public Long getCd_SAP_Grupo()
   {
      return cd_SAP_Grupo;
   }

   @Column(nullable = false)
   @Temporal(TemporalType.TIMESTAMP)
   public Date getDataLeitura()
   {
      return dataLeitura;
   }

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO, generator = "integracaosap_seq")
   @SequenceGenerator(name = "integracaosap_seq", sequenceName = "seqintegracaosap")
   public Long getIdIntegracaoSAP()
   {
      return idIntegracaoSAP;
   }

   @Column(nullable = false, length = 90)
   public String getTitulo()
   {
      return titulo;
   }

   @Column(nullable = false)
   public Double getValorEstoque()
   {
      return valorEstoque;
   }

   @Column(nullable = false)
   public Boolean getProcessado()
   {
      return processado;
   }

   @Column(nullable = true, length = 3)
   public String getCodigoTipoProduto()
   {
      return codigoTipoProduto;
   }

   public void setCd_SAP_Dado(Long cd_SAP_Dado)
   {
      this.cd_SAP_Dado = cd_SAP_Dado;
   }

   public void setCd_SAP_Grupo(Long cd_SAP_Grupo)
   {
      this.cd_SAP_Grupo = cd_SAP_Grupo;
   }

   public void setDataLeitura(Date dataLeitura)
   {
      this.dataLeitura = dataLeitura;
   }

   public void setIdIntegracaoSAP(Long idIntegracaoSAP)
   {
      this.idIntegracaoSAP = idIntegracaoSAP;
   }

   public void setTitulo(String titulo)
   {
      this.titulo = titulo;
   }

   public void setValorEstoque(Double valorEstoque)
   {
      this.valorEstoque = valorEstoque;
   }

   public void setProcessado(Boolean processado)
   {
      this.processado = processado;
   }

   public void setCodigoTipoProduto(String codigoTipoProduto)
   {
      this.codigoTipoProduto = codigoTipoProduto;
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
      final IntegracaoSAP other = (IntegracaoSAP) obj;
      if (this.idIntegracaoSAP != other.idIntegracaoSAP && (this.idIntegracaoSAP == null || !this.idIntegracaoSAP.equals(other.idIntegracaoSAP)))
      {
         return false;
      }
      if (this.cd_SAP_Dado != other.cd_SAP_Dado && (this.cd_SAP_Dado == null || !this.cd_SAP_Dado.equals(other.cd_SAP_Dado)))
      {
         return false;
      }
      if (this.cd_SAP_Grupo != other.cd_SAP_Grupo && (this.cd_SAP_Grupo == null || !this.cd_SAP_Grupo.equals(other.cd_SAP_Grupo)))
      {
         return false;
      }
      if ((this.titulo == null) ? (other.titulo != null) : !this.titulo.equals(other.titulo))
      {
         return false;
      }
      if (this.dataLeitura != other.dataLeitura && (this.dataLeitura == null || !this.dataLeitura.equals(other.dataLeitura)))
      {
         return false;
      }
      if (this.valorEstoque != other.valorEstoque && (this.valorEstoque == null || !this.valorEstoque.equals(other.valorEstoque)))
      {
         return false;
      }
      if (this.processado != other.processado && (this.processado == null || !this.processado.equals(other.processado)))
      {
         return false;
      }
      if ((this.codigoTipoProduto == null) ? (other.codigoTipoProduto != null) : !this.codigoTipoProduto.equals(other.codigoTipoProduto))
      {
         return false;
      }
      return true;
   }

   @Override
   public int hashCode()
   {
      int hash = 5;
      hash = 53 * hash + (this.idIntegracaoSAP != null ? this.idIntegracaoSAP.hashCode() : 0);
      hash = 53 * hash + (this.cd_SAP_Dado != null ? this.cd_SAP_Dado.hashCode() : 0);
      hash = 53 * hash + (this.cd_SAP_Grupo != null ? this.cd_SAP_Grupo.hashCode() : 0);
      hash = 53 * hash + (this.titulo != null ? this.titulo.hashCode() : 0);
      hash = 53 * hash + (this.dataLeitura != null ? this.dataLeitura.hashCode() : 0);
      hash = 53 * hash + (this.valorEstoque != null ? this.valorEstoque.hashCode() : 0);
      hash = 53 * hash + (this.processado != null ? this.processado.hashCode() : 0);
      hash = 53 * hash + (this.codigoTipoProduto != null ? this.codigoTipoProduto.hashCode() : 0);
      return hash;
   }
   
}
