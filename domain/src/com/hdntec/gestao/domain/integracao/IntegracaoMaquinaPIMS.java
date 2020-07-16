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

@Entity
public class IntegracaoMaquinaPIMS implements Serializable
{

   /** Serialização do Objeto */
   private static final long serialVersionUID = 6631533997398089271L;

   /** Codigo sequencial da integracao */
   private Long idIntegracaoPIMS;

   /** Codigo da maquina */
   private String tagMaquina;

   /** data/hora da integracao */
   private Date dataHora;

   /** Estado da maquina */
   private String valorTag;

   @Column(nullable = false)
   public String getTagMaquina()
   {
      return tagMaquina;
   }

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO, generator = "integracaoPIMS_seq")
   @SequenceGenerator(name = "integracaoPIMS_seq", sequenceName = "seqintegracaopims")
   public Long getIdIntegracaoPIMS()
   {
      return idIntegracaoPIMS;
   }

   @Column
   @Temporal(javax.persistence.TemporalType.TIMESTAMP)
   public Date getDataHora()
   {
      return dataHora;
   }

   @Column(nullable = false)
   public String getValorTag()
   {
      return valorTag;
   }

   public void setTagMaquina(String tagMaquina)
   {
      this.tagMaquina = tagMaquina;
   }

   public void setIdIntegracaoPIMS(Long idIntegracaoPIMS)
   {
      this.idIntegracaoPIMS = idIntegracaoPIMS;
   }

   public void setDataHora(Date dataHora)
   {
      this.dataHora = dataHora;
   }

   public void setValorTag(String valorTag)
   {
      this.valorTag = valorTag;
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
      final IntegracaoMaquinaPIMS other = (IntegracaoMaquinaPIMS) obj;
      if (this.idIntegracaoPIMS != other.idIntegracaoPIMS && (this.idIntegracaoPIMS == null || !this.idIntegracaoPIMS.equals(other.idIntegracaoPIMS)))
      {
         return false;
      }
      if ((this.tagMaquina == null) ? (other.tagMaquina != null) : !this.tagMaquina.equals(other.tagMaquina))
      {
         return false;
      }
      if (this.dataHora != other.dataHora && (this.dataHora == null || !this.dataHora.equals(other.dataHora)))
      {
         return false;
      }
      if ((this.valorTag == null) ? (other.valorTag != null) : !this.valorTag.equals(other.valorTag))
      {
         return false;
      }
      return true;
   }

   @Override
   public int hashCode()
   {
      int hash = 7;
      hash = 73 * hash + (this.idIntegracaoPIMS != null ? this.idIntegracaoPIMS.hashCode() : 0);
      hash = 73 * hash + (this.tagMaquina != null ? this.tagMaquina.hashCode() : 0);
      hash = 73 * hash + (this.dataHora != null ? this.dataHora.hashCode() : 0);
      hash = 73 * hash + (this.valorTag != null ? this.valorTag.hashCode() : 0);
      return hash;
   }
   
}
