package com.hdntec.gestao.domain.produto.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import com.hdntec.gestao.domain.StatusEntity;


/**
 * Itens de controle são as propriedades físicas, químicas e metalúrgicas que compõem a {@link Qualidade} de um {@link Produto}. *
 * <p>
 * Esses principais valores são:
 * <ul>
 * <li>meta - a meta do item de controle, determinado pela carga a ser atendida
 * <li>embarcado - o valor do item de controle medido no embarque
 * <li>estimado - o valor estimado do item de controle, calculado através de coeficientes empíricos
 * <li>blend - o valor da média ponderada da blendagem de diferentes produtos
 * </ul>
 * 
 * @author andre
 */
/* @Entity public class ItemDeControle implements Serializable { */
@MappedSuperclass
public abstract class ItemDeControle extends StatusEntity<ItemDeControle>
{
 /** Log para Login e Logout*/
   private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("ItemDeControle");

   /** serial gerado automaticamente */
   private static final long serialVersionUID = 1021058646721491861L;

   /** identificador de item de controle */
   private Long idItemDeControle;

   /** o tipo de item de controle */
   private TipoItemDeControle tipoItemControle;

   /** o valor calculado do item de controle */
   private Double valor;

   /** o desvio padrão do valor */
//   private Double desvioPadraoValor;

   /** o valor medido no embarque para este item de controle blendado */
   private Double embarcado;

   /** o desvio padrão do valor medido no embarque para este item de controle blendado */
//   private Double desvioPadraoEmbarcado;

   /** o limite inferior do valor da meta da orientação de embarque da carga do navio */
   private Double limInfMetaOrientacaoEmb;

   /** o limite superior do valor da meta de orientação de embarque da carga do navio */
   private Double limSupMetaOrientacaoEmb;

   public ItemDeControle()
   {
   }

  
   @Id
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "itemdecontrole_seq")
   @SequenceGenerator(name = "itemdecontrole_seq", sequenceName = "seqitemdecontrole")
   public Long getIdItemDeControle()
   {
      return idItemDeControle;
   }

   @OneToOne
   @JoinColumn(name = "id_TipoItemControle", insertable = true, updatable = false, nullable = false)
   public TipoItemDeControle getTipoItemControle()
   {
      return tipoItemControle;
   }

  /* @Column(nullable = true)
   public Double getValor()
   {
      return valor;
   }*/
   @Column(nullable = true)
   public Double getValor()
   {
      return valor;
   }

   public void setIdItemDeControle(Long idItemDeControle)
   {
      this.idItemDeControle = idItemDeControle;
   }

   public void setTipoItemControle(TipoItemDeControle tipoItemControle)
   {
      this.tipoItemControle = tipoItemControle;
   }

  /* public void setValor(Double valor)
   {
    
       if (valor != null && (valor.isInfinite() || valor.isNaN())) { 
           logger.warn("Attempted to send a NaN valor or infinity value to the database this is not supported. "); 
           valor = null; 
       } 

	   this.valor =valor;
   }*/
   public void setValor(Double valor)
   {
	   this.valor = valor;
   }

   /**
    * @return the desvioPadraoValor
    */
//   @Column(nullable = true)
//   public Double getDesvioPadraoValor()
//   {
//      return desvioPadraoValor;
//   }

   /**
    * @param desvioPadraoValor
    *           the desvioPadraoValor to set
    */
//   public void setDesvioPadraoValor(Double desvioPadraoValor)
//   {
//      this.desvioPadraoValor = desvioPadraoValor;
//   }

   /**
    * @return the embarcado
    */
   @Column(nullable = true)
   public Double getEmbarcado()
   {
      return embarcado;
   }

   /**
    * @param embarcado
    *           the embarcado to set
    */
   public void setEmbarcado(Double embarcado)
   {
	   if (embarcado != null && (embarcado.isInfinite() || embarcado.isNaN())) { 
           logger.warn("Attempted to send a NaN embarcado or infinity value to the database this is not supported. "); 
           embarcado = null; 
       } 
	  
	   this.embarcado = embarcado;
   }

   /**
    * @return the desvioPadraoEmbarcado
    */
//   @Column(nullable = true)
//   public Double getDesvioPadraoEmbarcado()
//   {
//      return desvioPadraoEmbarcado;
//   }

   /**
    * @param desvioPadraoEmbarcado
    *           the desvioPadraoEmbarcado to set
    */
//   public void setDesvioPadraoEmbarcado(Double desvioPadraoEmbarcado)
//   {
//      this.desvioPadraoEmbarcado = desvioPadraoEmbarcado;
//   }

   /**
    * @return the limInfMetaOrientacaoEmb
    */
   @Column(nullable = true)
   public Double getLimInfMetaOrientacaoEmb()
   {
      return limInfMetaOrientacaoEmb;
   }

   /**
    * @param limInfMetaOrientacaoEmb
    *           the limInfMetaOrientacaoEmb to set
    */
   public void setLimInfMetaOrientacaoEmb(Double limInfMetaOrientacaoEmb)
   {
      this.limInfMetaOrientacaoEmb = limInfMetaOrientacaoEmb;
   }

   /**
    * @return the limSupMetaOrientacaoEmb
    */
   @Column(nullable = true)
   public Double getLimSupMetaOrientacaoEmb()
   {
      return limSupMetaOrientacaoEmb;
   }

   /**

    * @param limSupMetaOrientacaoEmb
    *           the limSupMetaOrientacaoEmb to set
    */
   public void setLimSupMetaOrientacaoEmb(Double limSupMetaOrientacaoEmb)
   {
      this.limSupMetaOrientacaoEmb = limSupMetaOrientacaoEmb;
   }

   public Double cloneDouble(Double value)
   {
      Double novoValor = null;
      if (value != null)
      {
         novoValor = new Double(value);
      } else {
          novoValor = new Double(0.0d); 
      }
      return novoValor;
   }

   public Long cloneLong(Long value)
   {
      Long novoValor = null;
      if (value != null)
      {
         novoValor = new Long(value);
      }
      return novoValor;
   }

   public Integer cloneInteger(Integer value)
   {
      Integer novoValor = null;
      if (value != null)
      {
         novoValor = new Integer(value);
      }
      return novoValor;
   }

   public Boolean cloneBoolean(Boolean value)
   {
      Boolean novoValor = null;
      if (value != null)
      {
         novoValor = new Boolean(value);
      }
      return novoValor;
   }

   
}
