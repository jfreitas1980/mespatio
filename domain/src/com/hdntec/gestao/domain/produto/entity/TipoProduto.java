package com.hdntec.gestao.domain.produto.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import org.hibernate.annotations.ForeignKey;

import com.hdntec.gestao.domain.AuditTrail;
import com.hdntec.gestao.domain.produto.enums.TipoDeProdutoEnum;


/**
 * Define os tipos de {@link Produto} que são produzidos e comercializados.
 *
 * @author andre
 *
 */
@Entity
public class TipoProduto extends  AuditTrail
{

   /** Serialização do objeto */
   private static final long serialVersionUID = -272409720598562259L;

   /** codigo de tipo de produto */
   private Long idTipoProduto;

   /** Codigo do tipo de produto. EX: STD, HB, MB45, etc */
   private String codigoTipoProduto;

   /** Familia a qual o produto pertence */
   private String codigoFamiliaTipoProduto;

   /** insumo deste tipo de produto */
   private TipoProduto codigoInsumoTipoProduto;

   /** descricao do tipo de produto */
   private String descricaoTipoProduto;

   /** Cor de identificacao do Tipo de Produto. Sera gravado o RGB da cor no formato "999,999,999" */
   private String corIdentificacao;

   /** O tipo deste produto (Pelota, Pellet Feed ou Pellet Screening) */
   private TipoDeProdutoEnum tipoDeProduto;

   /** codigo que identifica o produto para integracao com SAP */
   private Long codigoSAPDado;

   /** Totalizador da quantidade do tipo de produto usando na integracao com sistema SAP */
   private Double quantidadeEstoquePatio;

   /** Porcentagem resultante do tratamento do PSM */
   private Double porcentagemResultadoPSM;

   /** Codigo do produto na tabela pro_produto */
   private String cdProduto;

   /**
    * Construtor Padrao
    */
   public TipoProduto()
   {
   }

   /**
    * @return the idTipoProduto
    */
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO, generator = "tipoproduto_seq")
   @SequenceGenerator(name = "tipoproduto_seq", sequenceName = "seqtipoproduto")
   public Long getIdTipoProduto()
   {
      return idTipoProduto;
   }

   /**
    * @param idTipoProduto
    *           the idTipoProduto to set
    */
   public void setIdTipoProduto(Long idTipoProduto)
   {
      this.idTipoProduto = idTipoProduto;
   }

   /**
    * @return the descricaoTipoProduto
    */
   @Column(nullable = false, length = 60)
   public String getDescricaoTipoProduto()
   {
      return descricaoTipoProduto;
   }

   /**
    * @param descricaoTipoProduto
    *           the descricaoTipoProduto to set
    */
   public void setDescricaoTipoProduto(String descricaoTipoProduto)
   {
      this.descricaoTipoProduto = descricaoTipoProduto;
   }

   @Column(nullable = false, length = 30)
   public String getCodigoTipoProduto()
   {
      return codigoTipoProduto;
   }

   public void setCodigoTipoProduto(String codigoTipoProduto)
   {
      this.codigoTipoProduto = codigoTipoProduto;
   }

   @Column(nullable = false, length = 11)
   public String getCorIdentificacao()
   {
      return corIdentificacao;
   }

   public void setCorIdentificacao(String corIdentificacao)
   {
      this.corIdentificacao = corIdentificacao;
   }

   @Column(nullable = false, length = 30)
   public String getCodigoFamiliaTipoProduto()
   {
      return codigoFamiliaTipoProduto;
   }

   public void setCodigoFamiliaTipoProduto(String codigoFamiliaProduto)
   {
      this.codigoFamiliaTipoProduto = codigoFamiliaProduto;
   }

   @OneToOne
   @ForeignKey(name = "fk_tipoProduto_tipoProduto")
   @JoinColumn(name = "idTipoProdutoInsumo", nullable = true)
   public TipoProduto getCodigoInsumoTipoProduto()
   {
      return codigoInsumoTipoProduto;
   }

   public void setCodigoInsumoTipoProduto(TipoProduto codigoInsumoProduto)
   {
      this.codigoInsumoTipoProduto = codigoInsumoProduto;
   }

   @Column(nullable = true)
   public Long getCodigoSAPDado()
   {
      return codigoSAPDado;
   }

   public void setCodigoSAPDado(Long codigoSAPDado)
   {
      this.codigoSAPDado = codigoSAPDado;
   }

   @Transient
   public Double getQuantidadeEstoquePatio()
   {
      return quantidadeEstoquePatio;
   }

   public void setQuantidadeEstoquePatio(Double quantidadeEstoquePatio)
   {
      this.quantidadeEstoquePatio = quantidadeEstoquePatio;
   }

   @Override
   public String toString()
   {
       StringBuffer value = new StringBuffer();
       value.append(this.getCodigoFamiliaTipoProduto()).append(" - ").append(this.getCodigoTipoProduto());
	   return value.toString();
   }

   /**
    * @return the tipoDeProduto
    */
   @Column(nullable = false)
   @Enumerated(EnumType.STRING)
   public TipoDeProdutoEnum getTipoDeProduto()
   {
      return tipoDeProduto;
   }

   /**
    * @param tipoDeProduto the tipoDeProduto to set
    */
   public void setTipoDeProduto(TipoDeProdutoEnum tipoDeProduto)
   {
      this.tipoDeProduto = tipoDeProduto;
   }

   /**
    * @return the porcentagemResultadoPSM
    */
   @Column(nullable = true)
   public Double getPorcentagemResultadoPSM()
   {
      return porcentagemResultadoPSM;
   }

   /**
    * @param porcentagemResultadoPSM the porcentagemResultadoPSM to set
    */
   public void setPorcentagemResultadoPSM(Double porcentagemResultadoPSM)
   {
      this.porcentagemResultadoPSM = porcentagemResultadoPSM;
   }

   @Column(nullable = true, length = 3)
   public String getCdProduto()
   {
      return cdProduto;
   }

   public void setCdProduto(String cdProduto)
   {
      this.cdProduto = cdProduto;
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
      final TipoProduto other = (TipoProduto) obj;
      if (this.idTipoProduto != other.idTipoProduto && (this.idTipoProduto == null || !this.idTipoProduto.equals(other.idTipoProduto)))
      {
         return false;
      }
      if ((this.codigoTipoProduto == null) ? (other.codigoTipoProduto != null) : !this.codigoTipoProduto.equals(other.codigoTipoProduto))
      {
         return false;
      }
      if ((this.codigoFamiliaTipoProduto == null) ? (other.codigoFamiliaTipoProduto != null) : !this.codigoFamiliaTipoProduto.equals(other.codigoFamiliaTipoProduto))
      {
         return false;
      }
      if (this.codigoInsumoTipoProduto != other.codigoInsumoTipoProduto && (this.codigoInsumoTipoProduto == null || !this.codigoInsumoTipoProduto.equals(other.codigoInsumoTipoProduto)))
      {
         return false;
      }
      if ((this.descricaoTipoProduto == null) ? (other.descricaoTipoProduto != null) : !this.descricaoTipoProduto.equals(other.descricaoTipoProduto))
      {
         return false;
      }
      if ((this.corIdentificacao == null) ? (other.corIdentificacao != null) : !this.corIdentificacao.equals(other.corIdentificacao))
      {
         return false;
      }
      if (this.tipoDeProduto != other.tipoDeProduto)
      {
         return false;
      }
      if (this.codigoSAPDado != other.codigoSAPDado && (this.codigoSAPDado == null || !this.codigoSAPDado.equals(other.codigoSAPDado)))
      {
         return false;
      }
      if (this.quantidadeEstoquePatio != other.quantidadeEstoquePatio && (this.quantidadeEstoquePatio == null || !this.quantidadeEstoquePatio.equals(other.quantidadeEstoquePatio)))
      {
         return false;
      }
      if (this.porcentagemResultadoPSM != other.porcentagemResultadoPSM && (this.porcentagemResultadoPSM == null || !this.porcentagemResultadoPSM.equals(other.porcentagemResultadoPSM)))
      {
         return false;
      }
      if ((this.cdProduto == null) ? (other.cdProduto != null) : !this.cdProduto.equals(other.cdProduto))
      {
         return false;
      }
      return true;
   }

   @Override
   public int hashCode()
   {
      int hash = 5;
      hash = 67 * hash + (this.idTipoProduto != null ? this.idTipoProduto.hashCode() : 0);
      hash = 67 * hash + (this.codigoTipoProduto != null ? this.codigoTipoProduto.hashCode() : 0);
      hash = 67 * hash + (this.codigoFamiliaTipoProduto != null ? this.codigoFamiliaTipoProduto.hashCode() : 0);
      hash = 67 * hash + (this.descricaoTipoProduto != null ? this.descricaoTipoProduto.hashCode() : 0);
      hash = 67 * hash + (this.corIdentificacao != null ? this.corIdentificacao.hashCode() : 0);
      hash = 67 * hash + (this.codigoSAPDado != null ? this.codigoSAPDado.hashCode() : 0);
      hash = 67 * hash + (this.quantidadeEstoquePatio != null ? this.quantidadeEstoquePatio.hashCode() : 0);
      hash = 67 * hash + (this.porcentagemResultadoPSM != null ? this.porcentagemResultadoPSM.hashCode() : 0);
      hash = 67 * hash + (this.cdProduto != null ? this.cdProduto.hashCode() : 0);
      return hash;
   }
   
}
