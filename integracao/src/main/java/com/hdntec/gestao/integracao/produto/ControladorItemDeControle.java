/**
 * 
 */
package com.hdntec.gestao.integracao.produto;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.hdntec.gestao.domain.produto.entity.ItemDeControle;
import com.hdntec.gestao.domain.produto.entity.MetaInterna;
import com.hdntec.gestao.domain.produto.entity.TipoItemCoeficiente;
import com.hdntec.gestao.domain.produto.entity.TipoItemRegraFarol;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.domain.produto.enums.EnumEstadosDoFarol;
import com.hdntec.gestao.domain.produto.enums.EnumValorRegraFarol;
import com.hdntec.gestao.domain.produto.enums.TipoMetaInternaEnum;


/**
 * Controlador da entidade item de controle
 * 
 * @author andre
 *
 */
public class ControladorItemDeControle
{
   /** o item de controle controlado */
   private ItemDeControle itemDeControle;

   /*quarda informação se dos itensDeControle ja pequisados */
   private HashMap<Long , List<MetaInterna> > mapaMetaInterna;
   
   /**
    * Construtor passando o item de controle
    * @param itemDeControle
    */
   public ControladorItemDeControle(ItemDeControle itemDeControle)
   {
      this.itemDeControle = itemDeControle;      
   }
   public ControladorItemDeControle(){
       mapaMetaInterna = new HashMap<Long, List<MetaInterna>>();
   }

   /**
    * @return the itemDeControle
    */
   public ItemDeControle getItemDeControle()
   {
      return itemDeControle;
   }

   /**
    * @param itemDeControle
    *           the itemDeControle to set
    */
   public void setItemDeControle(ItemDeControle itemDeControle)
   {
      this.itemDeControle = itemDeControle;
   }

   /**
    * Busca uma meta interna pelo tipo de produto e pelo tipo de meta
    * @param tipoProdutoDaMeta
    * @param dataHoraSituacaoPatio
    * @param tipoDaMetaInterna
    * @return
    */
    public MetaInterna buscaMetaInterna(TipoProduto tipoProdutoDaMeta, Date dataHoraSituacaoPatio, TipoMetaInternaEnum tipoDaMetaInterna) {        
        MetaInterna metaInternaProcurada = itemDeControle.getTipoItemControle().getMetaInterna(dataHoraSituacaoPatio,tipoDaMetaInterna);
        return metaInternaProcurada;
    }
   
   /**
    * Determina se farol assumira o estado verde (compliant), vermelho (noncompliant) ou azul (overspecified) baseado no valor do item de controle e na sua regra de farol
    * @param dataHoraSituacaoPatio 
    * @return
    */
   public EnumEstadosDoFarol determinaEstadoDoFarol(TipoProduto tipoProduto, TipoMetaInternaEnum tipoMetaInterna, Date dataHoraSituacaoPatio)
   {
      TipoItemRegraFarol regraFarol = null;
      
      regraFarol = getItemDeControle().getTipoItemControle().getTipoItemRegraFarol(dataHoraSituacaoPatio);
      
      
      if (regraFarol != null)
      {
               EnumEstadosDoFarol estadoRetorno = null;
               MetaInterna meta = buscaMetaInterna(tipoProduto, dataHoraSituacaoPatio, tipoMetaInterna);
               if (meta != null)
               {
                  EnumValorItemControleXMeta enumValorItemVsMeta = calculaItemDeControleContraMeta(meta, this.itemDeControle, tipoProduto, dataHoraSituacaoPatio);
                  if (enumValorItemVsMeta != null)
                  {
                     if (regraFarol.getValorRegraFarol().equals(EnumValorRegraFarol.CRESCENTE))
                     {
                        if (enumValorItemVsMeta.equals(EnumValorItemControleXMeta.INFERIOR))
                        {
                           estadoRetorno = EnumEstadosDoFarol.NONCOMPLIANT;
                        } else if (enumValorItemVsMeta.equals(EnumValorItemControleXMeta.NAFAIXA))
                        {
                           estadoRetorno = EnumEstadosDoFarol.COMPLIANT;
                        } else if (enumValorItemVsMeta.equals(EnumValorItemControleXMeta.SUPERIOR))
                        {
                           estadoRetorno = EnumEstadosDoFarol.OVERSPECIFIED;
                        }
                     } else if (regraFarol.getValorRegraFarol().equals(EnumValorRegraFarol.DECRESCENTE))
                     {
                        if (enumValorItemVsMeta.equals(EnumValorItemControleXMeta.INFERIOR))
                        {
                           estadoRetorno = EnumEstadosDoFarol.OVERSPECIFIED;
                        } else if (enumValorItemVsMeta.equals(EnumValorItemControleXMeta.NAFAIXA))
                        {
                           estadoRetorno = EnumEstadosDoFarol.COMPLIANT;
                        } else if (enumValorItemVsMeta.equals(EnumValorItemControleXMeta.SUPERIOR))
                        {
                           estadoRetorno = EnumEstadosDoFarol.NONCOMPLIANT;
                        }
                     } else if (regraFarol.getValorRegraFarol().equals(EnumValorRegraFarol.INDIFERENTE))
                     {
                        if (enumValorItemVsMeta.equals(EnumValorItemControleXMeta.INFERIOR))
                        {
                           estadoRetorno = EnumEstadosDoFarol.NONCOMPLIANT;
                        } else if (enumValorItemVsMeta.equals(EnumValorItemControleXMeta.NAFAIXA))
                        {
                           estadoRetorno = EnumEstadosDoFarol.COMPLIANT;
                        } else if (enumValorItemVsMeta.equals(EnumValorItemControleXMeta.SUPERIOR))
                        {
                           estadoRetorno = EnumEstadosDoFarol.NONCOMPLIANT;
                        }
                     }
                  }
               return estadoRetorno;
            }         
      }
      return null;
   }

   /**
    * Determina se farol assumira o estado verde (compliant), vermelho (noncompliant) ou azul (overspecified) baseado no valor do item de controle e na sua regra de farol
    * 
    * @param dataHoraSituacaoDoPatio
    * @return
    */
   public EnumEstadosDoFarol determinaEstadoDoFarol(TipoProduto tipoProduto, ItemDeControle itemDeControleOrientacaoCarga, Date dataHoraSituacaoDoPatio)
   {
      
      TipoItemRegraFarol regraFarol = null;
       
      regraFarol = getItemDeControle().getTipoItemControle().getTipoItemRegraFarol(dataHoraSituacaoDoPatio);       
          
      if (regraFarol != null) {                 
               EnumEstadosDoFarol estadoRetorno = null;
               if (itemDeControleOrientacaoCarga != null)
               {
                  EnumValorItemControleXMeta enumValorItemVsMeta = calculaItemDeControleContraItemDeControleOrientacaoEmbarque(itemDeControleOrientacaoCarga, this.itemDeControle, tipoProduto, dataHoraSituacaoDoPatio);
                  if (enumValorItemVsMeta != null)
                  {
                     if (regraFarol.getValorRegraFarol().equals(EnumValorRegraFarol.CRESCENTE))
                     {
                        if (enumValorItemVsMeta.equals(EnumValorItemControleXMeta.INFERIOR))
                        {
                           estadoRetorno = EnumEstadosDoFarol.NONCOMPLIANT;
                        } else if (enumValorItemVsMeta.equals(EnumValorItemControleXMeta.NAFAIXA))
                        {
                           estadoRetorno = EnumEstadosDoFarol.COMPLIANT;
                        } else if (enumValorItemVsMeta.equals(EnumValorItemControleXMeta.SUPERIOR))
                        {
                           estadoRetorno = EnumEstadosDoFarol.OVERSPECIFIED;
                        }
                     } else if (regraFarol.getValorRegraFarol().equals(EnumValorRegraFarol.DECRESCENTE))
                     {
                        if (enumValorItemVsMeta.equals(EnumValorItemControleXMeta.INFERIOR))
                        {
                           estadoRetorno = EnumEstadosDoFarol.OVERSPECIFIED;
                        } else if (enumValorItemVsMeta.equals(EnumValorItemControleXMeta.NAFAIXA))
                        {
                           estadoRetorno = EnumEstadosDoFarol.COMPLIANT;
                        } else if (enumValorItemVsMeta.equals(EnumValorItemControleXMeta.SUPERIOR))
                        {
                           estadoRetorno = EnumEstadosDoFarol.NONCOMPLIANT;
                        }
                     } else if (regraFarol.getValorRegraFarol().equals(EnumValorRegraFarol.INDIFERENTE))
                     {
                        if (enumValorItemVsMeta.equals(EnumValorItemControleXMeta.INFERIOR))
                        {
                           estadoRetorno = EnumEstadosDoFarol.NONCOMPLIANT;
                        } else if (enumValorItemVsMeta.equals(EnumValorItemControleXMeta.NAFAIXA))
                        {
                           estadoRetorno = EnumEstadosDoFarol.COMPLIANT;
                        } else if (enumValorItemVsMeta.equals(EnumValorItemControleXMeta.SUPERIOR))
                        {
                           estadoRetorno = EnumEstadosDoFarol.NONCOMPLIANT;
                        }
                     }
                  }
               }
               return estadoRetorno;                     
      }
      return null;
   }

   private enum EnumValorItemControleXMeta {
      /** O valor do item de controle e menor que limite inferior da meta */
      INFERIOR,
      /** O valor do item de controle esta na faixa da meta */
      NAFAIXA,
      /** O valor do item de controle e maior que limite superior da meta */
      SUPERIOR;
   }

   /**
    * Indica se o valor do item de controle e inferior, esta na faixa ou e superior a faixa da meta interna.
    * e importante observar que o valor utilizado do item de controle segue a seguinte prioridade:
    * 1 - embarcado
    * 2 - estimado
    * 3 - valor do blend
    * 
    * Isso significa que se o produto resultante tiver valor embarcado, ele sera usado. Se nao tiver, e tiver estimado, sera usado o estimado. O valor do blend so e usado caso os outros dois nao existam.
    * 
    * @param metaInterna
    *           a meta interna
    * @param itemControle
    *           o item de controle considerado
    * @param tipoDeProdutoDaCarga
    *           o tipo de produto da carga
    * @param dataHoraSituacaoPatio
    *           a data hora da situacao onde se calcula o valor do item de controle vs a Meta Interna
    * @return
    */
   private EnumValorItemControleXMeta calculaItemDeControleContraMeta(MetaInterna metaInterna, ItemDeControle itemControle, TipoProduto tipoDeProdutoDaCarga, Date dataHoraSituacaoPatio)
   {
      Double valorEmbarcado = itemControle.getEmbarcado();
      Double valor = itemControle.getValor();
      Double valorEstimado = null;
      
      Double valorInferiorMeta = metaInterna.getLimiteInferiorValorMetaInterna();
      Double valorSuperiorMeta = metaInterna.getLimiteSuperiorValorMetaInterna();
      if (valor != null)
      {
         TipoItemCoeficiente coeficienteDegradacao = buscarCoeficienteDegradacaoDoPeriodoDoTipoItemDeControleParaUmDeterminadoTipoProduto(dataHoraSituacaoPatio, itemControle);
         if (coeficienteDegradacao != null) {
        	 valorEstimado = (valor + (valor * coeficienteDegradacao.getValorDoCoeficiente()));
         } else {
        	 valorEstimado = valor; 
         }   
      }
      if (valorInferiorMeta != null && valorSuperiorMeta != null)
      {
         if (valorEmbarcado != null)
         {
            if (valorInferiorMeta > valorEmbarcado)
               return EnumValorItemControleXMeta.INFERIOR;
            if (valorSuperiorMeta < valorEmbarcado)
               return EnumValorItemControleXMeta.SUPERIOR;
            return EnumValorItemControleXMeta.NAFAIXA;
         } else if (valorEstimado != null)
         {
            if (valorInferiorMeta > valorEstimado)
               return EnumValorItemControleXMeta.INFERIOR;
            if (valorSuperiorMeta < valorEstimado)
               return EnumValorItemControleXMeta.SUPERIOR;
            return EnumValorItemControleXMeta.NAFAIXA;
         } else if (valor != null)
         {
            if (valorInferiorMeta > valor)
               return EnumValorItemControleXMeta.INFERIOR;
            if (valorSuperiorMeta < valor)
               return EnumValorItemControleXMeta.SUPERIOR;
            return EnumValorItemControleXMeta.NAFAIXA;
         }
      }
      return null;
   }

   /**
    * Indica se o valor do item de controle e inferior, esta na faixa ou e superior a faixa da meta da orientacao de embarque da carga
    * e importante observar que o valor utilizado do item de controle segue a seguinte prioridade:
    * 1 - embarcado
    * 2 - estimado
    * 3 - valor do blend
    * 
    * Isso significa que se o produto resultante tiver valor embarcado, ele sera usado. Se nao tiver, e tiver estimado, sera usado o estimado. O valor do blend so e usado caso os outros dois nao existam.
    * 
    * @param itemControleDaOrientacaoDeEmbarqueDaCarga
    *           o item de controle da orienta  de embarque da carga
    * @param itemControle
    *           o item de controle em quest�o
    * @param tipoDeProdutoDaCarga
    *           o tipo de produto da carga
    * @param dataHoraSituacaoPatio
    *           a data hora da situa  do p�tio
    * @return
    */
   private EnumValorItemControleXMeta calculaItemDeControleContraItemDeControleOrientacaoEmbarque(ItemDeControle itemControleDaOrientacaoDeEmbarqueDaCarga, ItemDeControle itemControle, TipoProduto tipoDeProdutoDaCarga, Date dataHoraSituacaoPatio)
   {
      Double valorEmbarcado = itemControle.getEmbarcado();
      Double valor = itemControle.getValor();
      Double valorEstimado = null;
      Double valorInferiorMeta = itemControleDaOrientacaoDeEmbarqueDaCarga.getLimInfMetaOrientacaoEmb();
      Double valorSuperiorMeta = itemControleDaOrientacaoDeEmbarqueDaCarga.getLimSupMetaOrientacaoEmb();
      if (valor != null)
      {
         TipoItemCoeficiente coeficienteDegradacao = buscarCoeficienteDegradacaoDoPeriodoDoTipoItemDeControleParaUmDeterminadoTipoProduto(dataHoraSituacaoPatio, itemControle);
         if (coeficienteDegradacao != null) {
            valorEstimado = (valor + coeficienteDegradacao.getValorDoCoeficiente());
         } else {
        	 valorEstimado = valor; 
         }
      }
      if (valorInferiorMeta != null && valorSuperiorMeta != null)
      {
         if (valorEmbarcado != null)
         {
            if (valorInferiorMeta > valorEmbarcado)
               return EnumValorItemControleXMeta.INFERIOR;
            if (valorSuperiorMeta < valorEmbarcado)
               return EnumValorItemControleXMeta.SUPERIOR;
            return EnumValorItemControleXMeta.NAFAIXA;
         } else if (valorEstimado != null)
         {
            if (valorInferiorMeta > valorEstimado)
               return EnumValorItemControleXMeta.INFERIOR;
            if (valorSuperiorMeta < valorEstimado)
               return EnumValorItemControleXMeta.SUPERIOR;
            return EnumValorItemControleXMeta.NAFAIXA;
         } else if (valor != null)
         {
            if (valorInferiorMeta > valor)
               return EnumValorItemControleXMeta.INFERIOR;
            if (valorSuperiorMeta < valor)
               return EnumValorItemControleXMeta.SUPERIOR;
            return EnumValorItemControleXMeta.NAFAIXA;
         } else
            return null;
      } else
      {
         return null;
      }
   }

   /**
    * Busca o coeficiente de degradacao para este item de controle de acordo com a sua data de cadastro e a data da situacao de patio
    * Ex: a lista de coeficientes deste item de controle possui 4 valores cadastrados
    *       1 - coeficiente cadastrado em 01/01/2006
    *       2 - coeficiente cadastrado em 01/01/2007
    *       3 - coeficiente cadastrado em 01/01/2008
    *       4 - coeficiente cadastrado em 01/01/2009
    * A situacao de patio em questao foi criada em 31/07/2008, sendo assim o coeficiente usado seria o cadastrado em 01/01/2008 e
    * caso o coeficiente nao estiver cadastrado o valor estimado para este item de controle nao sera exibido
    * 
    * @param dataHoraSituacaoPatio
    */
   public static TipoItemCoeficiente buscarCoeficienteDegradacaoDoPeriodoDoTipoItemDeControleParaUmDeterminadoTipoProduto(Date dataHoraSituacaoPatio, ItemDeControle itemControle)
   {
       
       TipoItemCoeficiente coeficienteDegradacao = itemControle.getTipoItemControle().getTipoItemCoeficiente(dataHoraSituacaoPatio);
       
      return coeficienteDegradacao;
   }
   
   /**
    * 
    * @param tipoProduto
    * @param itemDeControle
 * @param date 
    * @return
    */
   public static EnumValorRegraFarol determinaValorRegraFarol(TipoProduto tipoProduto,
		   ItemDeControle itemDeControle, Date dataHoraSituacaoDoPatio) {

	   EnumValorRegraFarol result = null;
	   
	   TipoItemRegraFarol regraFarol = itemDeControle.getTipoItemControle().getTipoItemRegraFarol(dataHoraSituacaoDoPatio); 
		   
	   if (regraFarol != null) {
              result = regraFarol.getValorRegraFarol();
	   } else {
	       result = EnumValorRegraFarol.INDIFERENTE;                
	    }	   
	   return result;
   }
}
