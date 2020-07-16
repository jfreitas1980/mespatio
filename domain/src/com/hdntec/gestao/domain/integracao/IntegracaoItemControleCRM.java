package com.hdntec.gestao.domain.integracao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class IntegracaoItemControleCRM implements Serializable
{

   /** Serialização do objeto */
   private static final long serialVersionUID = -2818353182052741651L;

   /** identificador sequencial da integracao */
   private Long idIntegracaoIcCRM;

   /** mostra se item eh relevante */
   private Boolean relevante;

   /** descricao do tipo de produto */
   private String descricaoTipoItemControle;

   /** Unidade de medida da variacao do tipo de item de controle */
   private String unidade;

   /** O valor inicial da escala */
   private Integer inicioEscala;

   /** O valor final da escala */
   private Integer fimEscala;

   /** A multiplicidade em que a escala ira aparecer na barra numerica */
   private Integer multiplicidadeEscala;

   /** Coeficiente para calculo do valor estimado de itens de controle deste tipo. */
   private Double coeficiente;

   /** tipo de processo do item de controle (HH - Bi-horaria, DD - diaria) (para pellet feed) */
   private String tipoProcessoPelletFeed;

   /** tipo de processo do item de controle (HH - Bi-horaria, DD - diaria) (para pelota) */
   private String tipoProcessoPelota;

   /** Codigo da area resp ed (para pellet feed) */
   private String areaRespEDPelletFeed;

   /** Codigo da area resp ed (para pelota) */
   private String areaRespEDPelota;

   /** Codigo do item de controle usado pelo cliente (para pellet feed) */
   private Long cdTipoItemControlePelletFeed;

   /** Codigo do item de controle usado pelo cliente (para pelota) */
   private Long cdTipoItemControlePelota;

   /** o valor minimo garantido do item de controle */
   private Double valorGarantidoMinimo;

   /** o valor maximo garantido do item de controle */
   private Double valorGarantidoMaximo;

   /** o valor tipico minimo do item de controle */
   private Double valorTipicoMinimo;

   /** o valor tipico minimo do item de controle */
   private Double valorTipicoMaximo;

   @Column(nullable = true)
   public String getAreaRespEDPelletFeed()
   {
      return areaRespEDPelletFeed;
   }

   @Column(nullable = true)
   public String getAreaRespEDPelota()
   {
      return areaRespEDPelota;
   }

   @Column(nullable = true)
   public Long getCdTipoItemControlePelletFeed()
   {
      return cdTipoItemControlePelletFeed;
   }

   @Column(nullable = true)
   public Long getCdTipoItemControlePelota()
   {
      return cdTipoItemControlePelota;
   }

   @Column(nullable = false)
   public Double getCoeficiente()
   {
      return coeficiente;
   }

   @Column(nullable = false)
   public String getDescricaoTipoItemControle()
   {
      return descricaoTipoItemControle;
   }

   @Column(nullable = false)
   public Integer getFimEscala()
   {
      return fimEscala;
   }

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO, generator = "integracaoICCRM_seq")
   @SequenceGenerator(name = "integracaoICCRM_seq", sequenceName = "seqintegracaoiccrm")
   public Long getIdIntegracaoIcCRM()
   {
      return idIntegracaoIcCRM;
   }

   @Column(nullable = false)
   public Integer getInicioEscala()
   {
      return inicioEscala;
   }

   @Column(nullable = false)
   public Integer getMultiplicidadeEscala()
   {
      return multiplicidadeEscala;
   }

   @Column(nullable = false)
   public Boolean getRelevante()
   {
      return relevante;
   }

   @Column(nullable = true)
   public String getTipoProcessoPelletFeed()
   {
      return tipoProcessoPelletFeed;
   }

   @Column(nullable = true)
   public String getTipoProcessoPelota()
   {
      return tipoProcessoPelota;
   }

   @Column(nullable = false)
   public String getUnidade()
   {
      return unidade;
   }

   @Column(nullable = true)
   public Double getValorGarantidoMaximo()
   {
      return valorGarantidoMaximo;
   }

   @Column(nullable = true)
   public Double getValorGarantidoMinimo()
   {
      return valorGarantidoMinimo;
   }

   @Column(nullable = true)
   public Double getValorTipicoMaximo()
   {
      return valorTipicoMaximo;
   }

   @Column(nullable = true)
   public Double getValorTipicoMinimo()
   {
      return valorTipicoMinimo;
   }

   public void setAreaRespEDPelletFeed(String areaRespED)
   {
      this.areaRespEDPelletFeed = areaRespED;
   }

   public void setAreaRespEDPelota(String areaRespED)
   {
      this.areaRespEDPelota = areaRespED;
   }

   public void setCdTipoItemControlePelletFeed(Long cdTipoItemControle)
   {
      this.cdTipoItemControlePelletFeed = cdTipoItemControle;
   }

   public void setCdTipoItemControlePelota(Long cdTipoItemControle)
   {
      this.cdTipoItemControlePelota = cdTipoItemControle;
   }

   public void setCoeficiente(Double coeficiente)
   {
      this.coeficiente = coeficiente;
   }

   public void setDescricaoTipoItemControle(String descricaoTipoItemControle)
   {
      this.descricaoTipoItemControle = descricaoTipoItemControle;
   }

   public void setFimEscala(Integer fimEscala)
   {
      this.fimEscala = fimEscala;
   }

   public void setIdIntegracaoIcCRM(Long idIntegracaoIcCRM)
   {
      this.idIntegracaoIcCRM = idIntegracaoIcCRM;
   }

   public void setInicioEscala(Integer inicioEscala)
   {
      this.inicioEscala = inicioEscala;
   }

   public void setMultiplicidadeEscala(Integer multiplicidadeEscala)
   {
      this.multiplicidadeEscala = multiplicidadeEscala;
   }

   public void setRelevante(Boolean relevante)
   {
      this.relevante = relevante;
   }

   public void setTipoProcessoPelletFeed(String tipoProcesso)
   {
      this.tipoProcessoPelletFeed = tipoProcesso;
   }

   public void setTipoProcessoPelota(String tipoProcesso)
   {
      this.tipoProcessoPelota = tipoProcesso;
   }

   public void setUnidade(String unidade)
   {
      this.unidade = unidade;
   }

   public void setValorGarantidoMaximo(Double valorGarantidoMaximo)
   {
      this.valorGarantidoMaximo = valorGarantidoMaximo;
   }

   public void setValorGarantidoMinimo(Double valorGarantidoMinimo)
   {
      this.valorGarantidoMinimo = valorGarantidoMinimo;
   }

   public void setValorTipicoMaximo(Double valorTipicoMaximo)
   {
      this.valorTipicoMaximo = valorTipicoMaximo;
   }

   public void setValorTipicoMinimo(Double valorTipicoMinimo)
   {
      this.valorTipicoMinimo = valorTipicoMinimo;
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
      final IntegracaoItemControleCRM other = (IntegracaoItemControleCRM) obj;
      if (this.idIntegracaoIcCRM != other.idIntegracaoIcCRM && (this.idIntegracaoIcCRM == null || !this.idIntegracaoIcCRM.equals(other.idIntegracaoIcCRM)))
      {
         return false;
      }
      if (this.relevante != other.relevante && (this.relevante == null || !this.relevante.equals(other.relevante)))
      {
         return false;
      }
      if ((this.descricaoTipoItemControle == null) ? (other.descricaoTipoItemControle != null) : !this.descricaoTipoItemControle.equals(other.descricaoTipoItemControle))
      {
         return false;
      }
      if ((this.unidade == null) ? (other.unidade != null) : !this.unidade.equals(other.unidade))
      {
         return false;
      }
      if (this.inicioEscala != other.inicioEscala && (this.inicioEscala == null || !this.inicioEscala.equals(other.inicioEscala)))
      {
         return false;
      }
      if (this.fimEscala != other.fimEscala && (this.fimEscala == null || !this.fimEscala.equals(other.fimEscala)))
      {
         return false;
      }
      if (this.multiplicidadeEscala != other.multiplicidadeEscala && (this.multiplicidadeEscala == null || !this.multiplicidadeEscala.equals(other.multiplicidadeEscala)))
      {
         return false;
      }
      if (this.coeficiente != other.coeficiente && (this.coeficiente == null || !this.coeficiente.equals(other.coeficiente)))
      {
         return false;
      }
      if ((this.tipoProcessoPelletFeed == null) ? (other.tipoProcessoPelletFeed != null) : !this.tipoProcessoPelletFeed.equals(other.tipoProcessoPelletFeed))
      {
         return false;
      }
      if ((this.tipoProcessoPelota == null) ? (other.tipoProcessoPelota != null) : !this.tipoProcessoPelota.equals(other.tipoProcessoPelota))
      {
         return false;
      }
      if ((this.areaRespEDPelletFeed == null) ? (other.areaRespEDPelletFeed != null) : !this.areaRespEDPelletFeed.equals(other.areaRespEDPelletFeed))
      {
         return false;
      }
      if ((this.areaRespEDPelota == null) ? (other.areaRespEDPelota != null) : !this.areaRespEDPelota.equals(other.areaRespEDPelota))
      {
         return false;
      }
      if (this.cdTipoItemControlePelletFeed != other.cdTipoItemControlePelletFeed && (this.cdTipoItemControlePelletFeed == null || !this.cdTipoItemControlePelletFeed.equals(other.cdTipoItemControlePelletFeed)))
      {
         return false;
      }
      if (this.cdTipoItemControlePelota != other.cdTipoItemControlePelota && (this.cdTipoItemControlePelota == null || !this.cdTipoItemControlePelota.equals(other.cdTipoItemControlePelota)))
      {
         return false;
      }
      if (this.valorGarantidoMinimo != other.valorGarantidoMinimo && (this.valorGarantidoMinimo == null || !this.valorGarantidoMinimo.equals(other.valorGarantidoMinimo)))
      {
         return false;
      }
      if (this.valorGarantidoMaximo != other.valorGarantidoMaximo && (this.valorGarantidoMaximo == null || !this.valorGarantidoMaximo.equals(other.valorGarantidoMaximo)))
      {
         return false;
      }
      if (this.valorTipicoMinimo != other.valorTipicoMinimo && (this.valorTipicoMinimo == null || !this.valorTipicoMinimo.equals(other.valorTipicoMinimo)))
      {
         return false;
      }
      if (this.valorTipicoMaximo != other.valorTipicoMaximo && (this.valorTipicoMaximo == null || !this.valorTipicoMaximo.equals(other.valorTipicoMaximo)))
      {
         return false;
      }
      return true;
   }

   @Override
   public int hashCode()
   {
      int hash = 7;
      hash = 89 * hash + (this.idIntegracaoIcCRM != null ? this.idIntegracaoIcCRM.hashCode() : 0);
      hash = 89 * hash + (this.relevante != null ? this.relevante.hashCode() : 0);
      hash = 89 * hash + (this.descricaoTipoItemControle != null ? this.descricaoTipoItemControle.hashCode() : 0);
      hash = 89 * hash + (this.unidade != null ? this.unidade.hashCode() : 0);
      hash = 89 * hash + (this.inicioEscala != null ? this.inicioEscala.hashCode() : 0);
      hash = 89 * hash + (this.fimEscala != null ? this.fimEscala.hashCode() : 0);
      hash = 89 * hash + (this.multiplicidadeEscala != null ? this.multiplicidadeEscala.hashCode() : 0);
      hash = 89 * hash + (this.coeficiente != null ? this.coeficiente.hashCode() : 0);
      hash = 89 * hash + (this.tipoProcessoPelletFeed != null ? this.tipoProcessoPelletFeed.hashCode() : 0);
      hash = 89 * hash + (this.tipoProcessoPelota != null ? this.tipoProcessoPelota.hashCode() : 0);
      hash = 89 * hash + (this.areaRespEDPelletFeed != null ? this.areaRespEDPelletFeed.hashCode() : 0);
      hash = 89 * hash + (this.areaRespEDPelota != null ? this.areaRespEDPelota.hashCode() : 0);
      hash = 89 * hash + (this.cdTipoItemControlePelletFeed != null ? this.cdTipoItemControlePelletFeed.hashCode() : 0);
      hash = 89 * hash + (this.cdTipoItemControlePelota != null ? this.cdTipoItemControlePelota.hashCode() : 0);
      hash = 89 * hash + (this.valorGarantidoMinimo != null ? this.valorGarantidoMinimo.hashCode() : 0);
      hash = 89 * hash + (this.valorGarantidoMaximo != null ? this.valorGarantidoMaximo.hashCode() : 0);
      hash = 89 * hash + (this.valorTipicoMinimo != null ? this.valorTipicoMinimo.hashCode() : 0);
      hash = 89 * hash + (this.valorTipicoMaximo != null ? this.valorTipicoMaximo.hashCode() : 0);
      return hash;
   }

   
}
