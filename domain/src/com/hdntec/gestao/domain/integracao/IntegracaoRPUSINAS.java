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
public class IntegracaoRPUSINAS implements Serializable
{

   /** Serialização do objeto */
   private static final long serialVersionUID = 1009407094119331844L;

   /** Identificador do objeto */
   private Long idIntegracaoRPUSINAS;

   /** Codigo da fase do processo */
   private Long cdFaseProcesso;

   /** Codigo do Item de Controle */
   private Long cdItemControle;

   /** Codigo do tipo de processo */
   private String cdTipoProcesso;

   /** Codigo da Area Responsavel */
   private String cdAreaRespEd;

   /** Flag de registro processado */
   private String processado;

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

   @Column(nullable = false)
   @Temporal(javax.persistence.TemporalType.TIMESTAMP)
   public Date getDataLeitura()
   {
      return dataLeitura;
   }

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO, generator = "integracaoRPUSINAS_seq")
   @SequenceGenerator(name = "integracaoRPUSINAS_seq", sequenceName = "seqintegracaorpusinas")
   public Long getIdIntegracaoRPUSINAS()
   {
      return idIntegracaoRPUSINAS;
   }

   public String getProcessado()
   {
      return processado;
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

   public void setIdIntegracaoRPUSINAS(Long idIntegracaoRPUSINAS)
   {
      this.idIntegracaoRPUSINAS = idIntegracaoRPUSINAS;
   }

   public void setProcessado(String processado)
   {
      this.processado = processado;
   }

   public void setValorLeitura(Double valorLeitura)
   {
      this.valorLeitura = valorLeitura;
   }
}
