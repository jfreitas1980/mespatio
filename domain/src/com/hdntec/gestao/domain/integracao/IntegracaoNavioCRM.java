/**
 * Classe responsavel em armazenar as informacoes do navio ligas do CRM
 *
 * @author Rodrigo Luchetta
 */
package com.hdntec.gestao.domain.integracao;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.ForeignKey;

@Entity
/*@Table(uniqueConstraints = @UniqueConstraint(columnNames = {
                "sap_vbap_vbeln","processado"}))*/
public class IntegracaoNavioCRM implements Serializable
{

   /** Serializacao do Objeto */
   private static final long serialVersionUID = -4286961385867957547L;

   /** Identificador sequencia da integracao */
   private Long idIntegracaoNavioCRM;

   /** Codigo do Navio no CRM */
   private Long cdNavio;

   /** Nome do Navio no CRM */
   private String nomeNavio;

   /** Capacidade de carga do navio (DWT) */
   private Double capacidadeNavio;

   /** Data de Chegada do Navio */
   private Date dataChegadaNavio;

   /** Data de Saida do Navio */
   private Date dataSaidaNavio;

   /** Data do embarque do navio */
   private Date dataEmbarqueNavio;

   /** Status do embarque do navio */
   private String statusEmbarque;

   /** Flag de registro processado */
   private Boolean processado;

   /** Chave para join do SAP */
   private String sap_vbap_vbeln;

   /** Codigo do cliente no sistema MES */
   private String codigoCliente;

   /** Data do ETA */
   private Date dataETA;

   /** nome do cliente */
   private String nomeCliente;

   /** lista com as cargas do navio */
   private List<IntegracaoCargaCRM> listaCargasNavio;

   /** Flag que identifica apos atualizacao da tabela IntegracaoNavioCRM, se o navio foi inserido ou atualizado */
   private Boolean navioAtualizado;

   /* flag que identifica o berco onde o navio será atracado*/
   private String identificadorBerco;

   /** a data de chegada no navio na barra */
   private Date dataChegadaBarra;

   /** a data de atracacao do Navio */
   private Date dataAtracacao;

   /** a data de desatracacao do Navio */
   private Date dataDesatracacao;

   @Column(nullable = false)
   public Double getCapacidadeNavio()
   {
      return capacidadeNavio;
   }

   @Column(nullable = false)
   public Long getCdNavio()
   {
      return cdNavio;
   }

   @Column(nullable = false)
   @Temporal(TemporalType.TIMESTAMP)
   public Date getDataChegadaNavio()
   {
      return dataChegadaNavio;
   }

   @Column(nullable = false)
   @Temporal(TemporalType.TIMESTAMP)
   public Date getDataSaidaNavio()
   {
      return dataSaidaNavio;
   }

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO, generator = "integracaonaviocrm_seq")
   @SequenceGenerator(name = "integracaonaviocrm_seq", sequenceName = "seqintegracaonaviocrm")
   public Long getIdIntegracaoNavioCRM()
   {
      return idIntegracaoNavioCRM;
   }

   @Column(nullable = false, length = 60)
   public String getNomeNavio()
   {
      return nomeNavio;
   }

   @Column(nullable = false, length = 1)
   public String getStatusEmbarque()
   {
      return statusEmbarque;
   }

   @Column(nullable = false)
   @Temporal(TemporalType.TIMESTAMP)
   public Date getDataEmbarqueNavio()
   {
      return dataEmbarqueNavio;
   }

   @Column(nullable = false, length = 10)
   public String getSap_vbap_vbeln()
   {
      return sap_vbap_vbeln;
   }

   @Column(nullable = false)
   public Boolean getProcessado()
   {
      return processado;
   }

   @Column(nullable = false, length = 3)
   public String getCodigoCliente()
   {
      return codigoCliente;
   }

   @Column(nullable = false)
   @Temporal(TemporalType.TIMESTAMP)
   public Date getDataETA()
   {
      return dataETA;
   }

   @Column(nullable = false, length = 60)
   public String getNomeCliente()
   {
      return nomeCliente;
   }
   //TODO Darley SA11079 Otimizando colocando LAZY
   @OneToMany(fetch = FetchType.LAZY)
   @Fetch(FetchMode.SELECT)
   @Cascade(value = CascadeType.ALL)
   @ForeignKey(name = "fk_integranavio_carga", inverseName = "fk_carga_integranavio")
   @JoinTable(name = "IntegraNavio_Carga", joinColumns = @JoinColumn(name = "idIntegracaoNavioCRM"), inverseJoinColumns = @JoinColumn(name = "idIntegracaoCargaCRM"))
   public List<IntegracaoCargaCRM> getListaCargasNavio()
   {
      return listaCargasNavio;
   }

   @Column
   public Boolean getNavioAtualizado()
   {
      return navioAtualizado;
   }

   @Column
   public String getIdentificadorBerco()
   {
      return identificadorBerco;
   }

   @Column(nullable = true)
   @Temporal(TemporalType.TIMESTAMP)
   public Date getDataAtracacao()
   {
      return dataAtracacao;
   }

   @Column(nullable = true)
   @Temporal(TemporalType.TIMESTAMP)
   public Date getDataChegadaBarra()
   {
      return dataChegadaBarra;
   }

   @Column(nullable = true)
   @Temporal(TemporalType.TIMESTAMP)
   public Date getDataDesatracacao()
   {
      return dataDesatracacao;
   }

   public void setIdentificadorBerco(String identificadorBerco)
   {
      this.identificadorBerco = identificadorBerco;
   }

   public void setNavioAtualizado(Boolean navioAtualizado)
   {
      this.navioAtualizado = navioAtualizado;
   }

   public void setCapacidadeNavio(Double capacidadeNavio)
   {
      this.capacidadeNavio = capacidadeNavio;
   }

   public void setCdNavio(Long cdNavio)
   {
      this.cdNavio = cdNavio;
   }

   public void setDataChegadaNavio(Date dataChegadaNavio)
   {
      this.dataChegadaNavio = dataChegadaNavio;
   }

   public void setDataSaidaNavio(Date dataSaidaNavio)
   {
      this.dataSaidaNavio = dataSaidaNavio;
   }

   public void setIdIntegracaoNavioCRM(Long idIntegracaoNavioCRM)
   {
      this.idIntegracaoNavioCRM = idIntegracaoNavioCRM;
   }

   public void setNomeNavio(String nomeNavio)
   {
      this.nomeNavio = nomeNavio;
   }

   public void setStatusEmbarque(String statusEmbarque)
   {
      this.statusEmbarque = statusEmbarque;
   }

   public void setDataEmbarqueNavio(Date dataEmbarqueNavio)
   {
      this.dataEmbarqueNavio = dataEmbarqueNavio;
   }

   public void setProcessado(Boolean processado)
   {
      this.processado = processado;
   }

   public void setSap_vbap_vbeln(String sap_vbap_vbeln)
   {
      this.sap_vbap_vbeln = sap_vbap_vbeln;
   }

   public void setCodigoCliente(String codigoCliente)
   {
      this.codigoCliente = codigoCliente;
   }

   public void setDataETA(Date dataETA)
   {
      this.dataETA = dataETA;
   }

   public void setListaCargasNavio(List<IntegracaoCargaCRM> listaCargasNavio)
   {
      this.listaCargasNavio = listaCargasNavio;
   }

   public void setNomeCliente(String nomeCliente)
   {
      this.nomeCliente = nomeCliente;
   }

   public void setDataAtracacao(Date dataAtracacao)
   {
      this.dataAtracacao = dataAtracacao;
   }

   public void setDataChegadaBarra(Date dataChegadaBarra)
   {
      this.dataChegadaBarra = dataChegadaBarra;
   }

   public void setDataDesatracacao(Date dataDesatracacao)
   {
      this.dataDesatracacao = dataDesatracacao;
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
      final IntegracaoNavioCRM other = (IntegracaoNavioCRM) obj;
      if (this.idIntegracaoNavioCRM != other.idIntegracaoNavioCRM && (this.idIntegracaoNavioCRM == null || !this.idIntegracaoNavioCRM.equals(other.idIntegracaoNavioCRM)))
      {
         return false;
      }
      if (this.cdNavio != other.cdNavio && (this.cdNavio == null || !this.cdNavio.equals(other.cdNavio)))
      {
         return false;
      }
      if ((this.nomeNavio == null) ? (other.nomeNavio != null) : !this.nomeNavio.equals(other.nomeNavio))
      {
         return false;
      }
      if (this.capacidadeNavio != other.capacidadeNavio && (this.capacidadeNavio == null || !this.capacidadeNavio.equals(other.capacidadeNavio)))
      {
         return false;
      }
      if (this.dataChegadaNavio != other.dataChegadaNavio && (this.dataChegadaNavio == null || !this.dataChegadaNavio.equals(other.dataChegadaNavio)))
      {
         return false;
      }
      if (this.dataSaidaNavio != other.dataSaidaNavio && (this.dataSaidaNavio == null || !this.dataSaidaNavio.equals(other.dataSaidaNavio)))
      {
         return false;
      }
      if (this.dataEmbarqueNavio != other.dataEmbarqueNavio && (this.dataEmbarqueNavio == null || !this.dataEmbarqueNavio.equals(other.dataEmbarqueNavio)))
      {
         return false;
      }
      if ((this.statusEmbarque == null) ? (other.statusEmbarque != null) : !this.statusEmbarque.equals(other.statusEmbarque))
      {
         return false;
      }
      if (this.processado != other.processado && (this.processado == null || !this.processado.equals(other.processado)))
      {
         return false;
      }
      if ((this.sap_vbap_vbeln == null) ? (other.sap_vbap_vbeln != null) : !this.sap_vbap_vbeln.equals(other.sap_vbap_vbeln))
      {
         return false;
      }
      if ((this.codigoCliente == null) ? (other.codigoCliente != null) : !this.codigoCliente.equals(other.codigoCliente))
      {
         return false;
      }
      if (this.dataETA != other.dataETA && (this.dataETA == null || !this.dataETA.equals(other.dataETA)))
      {
         return false;
      }
      if ((this.nomeCliente == null) ? (other.nomeCliente != null) : !this.nomeCliente.equals(other.nomeCliente))
      {
         return false;
      }
      if (this.listaCargasNavio != other.listaCargasNavio && (this.listaCargasNavio == null || !this.listaCargasNavio.equals(other.listaCargasNavio)))
      {
         return false;
      }
      if (this.navioAtualizado != other.navioAtualizado && (this.navioAtualizado == null || !this.navioAtualizado.equals(other.navioAtualizado)))
      {
         return false;
      }
      if ((this.identificadorBerco == null) ? (other.identificadorBerco != null) : !this.identificadorBerco.equals(other.identificadorBerco))
      {
         return false;
      }
      if (this.dataChegadaBarra != other.dataChegadaBarra && (this.dataChegadaBarra == null || !this.dataChegadaBarra.equals(other.dataChegadaBarra)))
      {
         return false;
      }
      if (this.dataAtracacao != other.dataAtracacao && (this.dataAtracacao == null || !this.dataAtracacao.equals(other.dataAtracacao)))
      {
         return false;
      }
      if (this.dataDesatracacao != other.dataDesatracacao && (this.dataDesatracacao == null || !this.dataDesatracacao.equals(other.dataDesatracacao)))
      {
         return false;
      }
      return true;
   }

   @Override
   public int hashCode()
   {
      int hash = 5;
      hash = 29 * hash + (this.idIntegracaoNavioCRM != null ? this.idIntegracaoNavioCRM.hashCode() : 0);
      hash = 29 * hash + (this.cdNavio != null ? this.cdNavio.hashCode() : 0);
      hash = 29 * hash + (this.nomeNavio != null ? this.nomeNavio.hashCode() : 0);
      hash = 29 * hash + (this.capacidadeNavio != null ? this.capacidadeNavio.hashCode() : 0);
      hash = 29 * hash + (this.dataChegadaNavio != null ? this.dataChegadaNavio.hashCode() : 0);
      hash = 29 * hash + (this.dataSaidaNavio != null ? this.dataSaidaNavio.hashCode() : 0);
      hash = 29 * hash + (this.dataEmbarqueNavio != null ? this.dataEmbarqueNavio.hashCode() : 0);
      hash = 29 * hash + (this.statusEmbarque != null ? this.statusEmbarque.hashCode() : 0);
      hash = 29 * hash + (this.processado != null ? this.processado.hashCode() : 0);
      hash = 29 * hash + (this.sap_vbap_vbeln != null ? this.sap_vbap_vbeln.hashCode() : 0);
      hash = 29 * hash + (this.codigoCliente != null ? this.codigoCliente.hashCode() : 0);
      hash = 29 * hash + (this.dataETA != null ? this.dataETA.hashCode() : 0);
      hash = 29 * hash + (this.nomeCliente != null ? this.nomeCliente.hashCode() : 0);
      hash = 29 * hash + (this.navioAtualizado != null ? this.navioAtualizado.hashCode() : 0);
      hash = 29 * hash + (this.identificadorBerco != null ? this.identificadorBerco.hashCode() : 0);
      hash = 29 * hash + (this.dataChegadaBarra != null ? this.dataChegadaBarra.hashCode() : 0);
      hash = 29 * hash + (this.dataAtracacao != null ? this.dataAtracacao.hashCode() : 0);
      hash = 29 * hash + (this.dataDesatracacao != null ? this.dataDesatracacao.hashCode() : 0);
      return hash;
   }
   
}

