/**
 * Classe responsavel em armazenar as informacoes das cargas de um navio lidas do CRM
 *
 * @author Rodrigo Luchetta
 */
package com.hdntec.gestao.domain.integracao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.ForeignKey;

@Entity
public class IntegracaoCargaCRM implements Serializable {

    /** Serializacao do Objeto */
    private static final long serialVersionUID = -3310829471378700712L;

    /** Identificador sequencial do objeto */
    private Long idIntegracaoCargaCRM;

    /** Quantidade necessária da carga */
    private Double quantidadeCarga;

    /** Descricao da carga (identificador) */
    private String descricaoCarga;

    /** Codigo do produto */
    private String codigoProduto;

    /** Chave para join do SAP */
    private String sap_vbap_vbeln;

    /** Orientacao de Embarque da carga */
    private IntegracaoOrientEmbarqueCRM orientacaoEmbarque;

    @Column(nullable=false, length=3)
    public String getCodigoProduto() {
        return codigoProduto;
    }

    @Column(nullable=false, length=60)
    public String getDescricaoCarga() {
        return descricaoCarga;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "integracaocargacrm_seq")
    @SequenceGenerator(name = "integracaocargacrm_seq", sequenceName = "seqintegracaocargacrm")
    public Long getIdIntegracaoCargaCRM() {
        return idIntegracaoCargaCRM;
    }

    @Column(nullable=false)
    public Double getQuantidadeCarga() {
        return quantidadeCarga;
    }

    @Column(nullable=false, length=10)
    public String getSap_vbap_vbeln() {
        return sap_vbap_vbeln;
    }

    @OneToOne
    @Cascade(value = CascadeType.ALL)
    @ForeignKey(name = "fk_integracarga_orientEmbarq")
    @JoinColumn(name = "idIntegracaoOrientEmbarqueCRM")
    public IntegracaoOrientEmbarqueCRM getOrientacaoEmbarque() {
        return orientacaoEmbarque;
    }

    public void setCodigoProduto(String codigoProduto) {
        this.codigoProduto = codigoProduto;
    }

    public void setDescricaoCarga(String descricaoCarga) {
        this.descricaoCarga = descricaoCarga;
    }

    public void setIdIntegracaoCargaCRM(Long idIntegracaoCargaCRM) {
        this.idIntegracaoCargaCRM = idIntegracaoCargaCRM;
    }

    public void setQuantidadeCarga(Double quantidadeCarga) {
        this.quantidadeCarga = quantidadeCarga;
    }

    public void setSap_vbap_vbeln(String sap_vbap_vbeln) {
        this.sap_vbap_vbeln = sap_vbap_vbeln;
    }

    public void setOrientacaoEmbarque(IntegracaoOrientEmbarqueCRM orientacaoEmbarque) {
        this.orientacaoEmbarque = orientacaoEmbarque;
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
      final IntegracaoCargaCRM other = (IntegracaoCargaCRM) obj;
      if (this.idIntegracaoCargaCRM != other.idIntegracaoCargaCRM && (this.idIntegracaoCargaCRM == null || !this.idIntegracaoCargaCRM.equals(other.idIntegracaoCargaCRM)))
      {
         return false;
      }
      if (this.quantidadeCarga != other.quantidadeCarga && (this.quantidadeCarga == null || !this.quantidadeCarga.equals(other.quantidadeCarga)))
      {
         return false;
      }
      if ((this.descricaoCarga == null) ? (other.descricaoCarga != null) : !this.descricaoCarga.equals(other.descricaoCarga))
      {
         return false;
      }
      if ((this.codigoProduto == null) ? (other.codigoProduto != null) : !this.codigoProduto.equals(other.codigoProduto))
      {
         return false;
      }
      if ((this.sap_vbap_vbeln == null) ? (other.sap_vbap_vbeln != null) : !this.sap_vbap_vbeln.equals(other.sap_vbap_vbeln))
      {
         return false;
      }
      if (this.orientacaoEmbarque != other.orientacaoEmbarque && (this.orientacaoEmbarque == null || !this.orientacaoEmbarque.equals(other.orientacaoEmbarque)))
      {
         return false;
      }
      return true;
   }

   @Override
   public int hashCode()
   {
      int hash = 3;
      hash = 53 * hash + (this.idIntegracaoCargaCRM != null ? this.idIntegracaoCargaCRM.hashCode() : 0);
      hash = 53 * hash + (this.quantidadeCarga != null ? this.quantidadeCarga.hashCode() : 0);
      hash = 53 * hash + (this.descricaoCarga != null ? this.descricaoCarga.hashCode() : 0);
      hash = 53 * hash + (this.codigoProduto != null ? this.codigoProduto.hashCode() : 0);
      hash = 53 * hash + (this.sap_vbap_vbeln != null ? this.sap_vbap_vbeln.hashCode() : 0);
      return hash;
   }

}
