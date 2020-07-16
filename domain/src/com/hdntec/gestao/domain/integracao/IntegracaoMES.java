package com.hdntec.gestao.domain.integracao;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.UniqueConstraint;

/**
 * Os navios são os equipamentos que são embarcados com as diversas cargas de produtos para um determinado cliente.
 * @author andre
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {
                "cdFaseProcesso", "cdItemControle", "cdTipoProcesso","cdAreaRespEd","dataLeituraInicio","dataLeitura","valorLeitura"}))

public class IntegracaoMES implements Serializable, Comparable<IntegracaoMES>
{

   /** Serialização do objeto */
   private static final long serialVersionUID = 7715905456755841137L;

   /** Identificador do objeto */
   private Long idIntegracaoMES;

   /** Codigo da fase do processo */
   private Long cdFaseProcesso;

   /** Codigo do Item de Controle */
   private Long cdItemControle;

   /** Codigo do tipo de processo */
   private String cdTipoProcesso;

   /** Codigo da Area Responsavel */
   private String cdAreaRespEd;

   /** data de inicio de leitura do sistema mes , apenas para usina*/
   private Date dataLeituraInicio;

   /** data da leitura do sistema mes */
   private Date dataLeitura;

   /** valor de leitura do sistema mes */
   private Double valorLeitura;

   @Column(nullable = false)
   public String getCdAreaRespEd()
   {
      return cdAreaRespEd;
   }

   @Column(nullable = false)
   public Long getCdFaseProcesso()
   {
      return cdFaseProcesso;
   }

   @Column(nullable = false)
   public Long getCdItemControle()
   {
      return cdItemControle;
   }

   @Column(nullable = false)
   public String getCdTipoProcesso()
   {
      return cdTipoProcesso;
   }

   @Column(nullable = true)
   @Temporal(javax.persistence.TemporalType.TIMESTAMP)
   public Date getDataLeituraInicio()
   {
      return dataLeituraInicio;
   }

   public void setDataLeituraInicio(Date dataLeituraInicio)
   {
      this.dataLeituraInicio = dataLeituraInicio;
   }

   
   @Column(nullable = false)
   @Temporal(javax.persistence.TemporalType.TIMESTAMP)
   public Date getDataLeitura()
   {
      return dataLeitura;
   }

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO, generator = "integracaoMES_seq")
   @SequenceGenerator(name = "integracaoMES_seq", sequenceName = "seqintegracaomes")
   public Long getIdIntegracaoMES()
   {
      return idIntegracaoMES;
   }


   
   public static long getSerialVersionUID()
   {
      return serialVersionUID;
   }

   public Double getValorLeitura()
   {
      return valorLeitura;
   }

   public void setCdAreaRespEd(String cdAreaRespEd)
   {
      this.cdAreaRespEd = cdAreaRespEd;
   }

   public void setCdFaseProcesso(Long cdFaseProcesso)
   {
      this.cdFaseProcesso = cdFaseProcesso;
   }

   public void setCdItemControle(Long cdItemControle)
   {
      this.cdItemControle = cdItemControle;
   }

   public void setCdTipoProcesso(String cdTipoProcesso)
   {
      this.cdTipoProcesso = cdTipoProcesso;
   }

   public void setDataLeitura(Date dataLeitura)
   {
      this.dataLeitura = dataLeitura;
   }

   public void setIdIntegracaoMES(Long idIntegracaoMES)
   {
      this.idIntegracaoMES = idIntegracaoMES;
   }

   
   public void setValorLeitura(Double valorLeitura)
   {
      this.valorLeitura = valorLeitura;
   }

   @Override
   public int compareTo(IntegracaoMES o)
   {
      return getDataLeitura().compareTo(o.getDataLeitura());
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
      final IntegracaoMES other = (IntegracaoMES) obj;
      if (this.idIntegracaoMES != other.idIntegracaoMES && (this.idIntegracaoMES == null || !this.idIntegracaoMES.equals(other.idIntegracaoMES)))
      {
         return false;
      }
      if (this.cdFaseProcesso != other.cdFaseProcesso && (this.cdFaseProcesso == null || !this.cdFaseProcesso.equals(other.cdFaseProcesso)))
      {
         return false;
      }
      if (this.cdItemControle != other.cdItemControle && (this.cdItemControle == null || !this.cdItemControle.equals(other.cdItemControle)))
      {
         return false;
      }
      if ((this.cdTipoProcesso == null) ? (other.cdTipoProcesso != null) : !this.cdTipoProcesso.equals(other.cdTipoProcesso))
      {
         return false;
      }
      if ((this.cdAreaRespEd == null) ? (other.cdAreaRespEd != null) : !this.cdAreaRespEd.equals(other.cdAreaRespEd))
      {
         return false;
      }      
      if (this.dataLeituraInicio != other.dataLeituraInicio && (this.dataLeituraInicio == null || !this.dataLeituraInicio.equals(other.dataLeituraInicio)))
      {
         return false;
      }
      if (this.dataLeitura != other.dataLeitura && (this.dataLeitura == null || !this.dataLeitura.equals(other.dataLeitura)))
      {
         return false;
      }
      if (this.valorLeitura != other.valorLeitura && (this.valorLeitura == null || !this.valorLeitura.equals(other.valorLeitura)))
      {
         return false;
      }
      return true;
   }

   @Override
   public int hashCode()
   {
      int hash = 7;
      hash = 73 * hash + (this.idIntegracaoMES != null ? this.idIntegracaoMES.hashCode() : 0);
      hash = 73 * hash + (this.cdFaseProcesso != null ? this.cdFaseProcesso.hashCode() : 0);
      hash = 73 * hash + (this.cdItemControle != null ? this.cdItemControle.hashCode() : 0);
      hash = 73 * hash + (this.cdTipoProcesso != null ? this.cdTipoProcesso.hashCode() : 0);
      hash = 73 * hash + (this.cdAreaRespEd != null ? this.cdAreaRespEd.hashCode() : 0);      
      hash = 73 * hash + (this.dataLeituraInicio != null ? this.dataLeituraInicio.hashCode() : 0);
      hash = 73 * hash + (this.dataLeitura != null ? this.dataLeitura.hashCode() : 0);
      hash = 73 * hash + (this.valorLeitura != null ? this.valorLeitura.hashCode() : 0);
      return hash;
   }
   
}
