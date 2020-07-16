/**
 * 
 */
package com.hdntec.gestao.domain.blendagem;

import java.util.List;

import com.hdntec.gestao.domain.produto.entity.Amostra;
import com.hdntec.gestao.domain.produto.entity.ItemDeControle;
import com.hdntec.gestao.domain.produto.entity.Qualidade;


/**
 * Controlador da entidade qualidade
 * 
 * @author andre
 *
 */
public class ControladorQualidade
{
   /** a qualidade controlada */
   private Qualidade qualidade;
   
   /**
    * Construtor passando a qualidade
    * @param qualidade
    */
   public ControladorQualidade(Qualidade qualidade)
   {
      this.qualidade = qualidade;
   }
   
   /**
    * Adiciona uma amostra a qualidade e atualiza os valores dos itens de qualidade
    * 
    * @param qualidade a qualidade a qual sera inserida a amostra
    * @param amostra a amostra inserida
    */
   public void adicionaAmostraDeQualidade(Amostra amostra)
   {
      this.qualidade.getListaDeAmostras().add(amostra);  
      atualizarValoresDosItensDeControleDaQualidadeAposInsercaoDaAmostra(this.qualidade.getListaDeItensDeControle(),amostra.getListaDeItensDeControle());
   }

   private double calculaValorMedio(ItemDeControle itemDeControle)
   {
      double acumuladorMedia = 0;
      int sizeAmostrasDoItem = 0;
      for (Amostra amostra : qualidade.getListaDeAmostras())
      {
         ItemDeControle itemDeControleNaAmostra = buscaSeHaItemDeControleEquivalenteNaLista(itemDeControle, amostra.getListaDeItensDeControle());
         if (itemDeControleNaAmostra != null)
         {
            acumuladorMedia += itemDeControleNaAmostra.getValor();
            sizeAmostrasDoItem++;
         }
      }
      
      return acumuladorMedia / sizeAmostrasDoItem;
   }
   
   private double calculaValorMedioEmbarcado(ItemDeControle itemDeControle)
   {
      double acumuladorMedia = 0;
      int sizeAmostrasDoItem = 0;
      for (Amostra amostra : qualidade.getListaDeAmostras())
      {
         ItemDeControle itemDeControleNaAmostra = buscaSeHaItemDeControleEquivalenteNaLista(itemDeControle, amostra.getListaDeItensDeControle());
         if (itemDeControleNaAmostra != null)
         {
            acumuladorMedia += itemDeControleNaAmostra.getEmbarcado();
            sizeAmostrasDoItem++;
         }
      }
      
      return acumuladorMedia / sizeAmostrasDoItem;
   }
   
   private double calculaDesvioPadrao(ItemDeControle itemDeControle)
   {
      double media = itemDeControle.getValor();
      double acumuladorVariancia = 0;
      int sizeAmostrasDoItem = 0;
      for (Amostra amostra : qualidade.getListaDeAmostras())
      {
         ItemDeControle itemDeControleNaAmostra = buscaSeHaItemDeControleEquivalenteNaLista(itemDeControle, amostra.getListaDeItensDeControle());
         if (itemDeControleNaAmostra != null)
         {
            acumuladorVariancia = Math.abs(itemDeControleNaAmostra.getValor() - media);
            sizeAmostrasDoItem++;
         }
      }
      
      double variancia = acumuladorVariancia / sizeAmostrasDoItem;
      
      return Math.sqrt(variancia);
   }
   
   private double calculaDesvioPadraoEmbarcado(ItemDeControle itemDeControle)
   {
      double media = itemDeControle.getEmbarcado();
      double acumuladorVariancia = 0;
      for (Amostra amostra : qualidade.getListaDeAmostras())
      {
         ItemDeControle itemDeControleNaAmostra = buscaSeHaItemDeControleEquivalenteNaLista(itemDeControle, amostra.getListaDeItensDeControle());
         if (itemDeControleNaAmostra != null)
            acumuladorVariancia = Math.abs(itemDeControleNaAmostra.getEmbarcado() - media);
      }
      
      double variancia = acumuladorVariancia / qualidade.getListaDeAmostras().size();
      
      return Math.sqrt(variancia);
   }
   
   private void atualizarValoresDosItensDeControleDaQualidadeAposInsercaoDaAmostra(List<ItemDeControle> listaDeItensDeControleDaQualidade, List<ItemDeControle> listaDeItensDeControleDaAmostraInserida)
   {
      // Para cada item de controle da qualidade base ...
      for(ItemDeControle itemDeControleDaQualidade : listaDeItensDeControleDaQualidade)
      {
         //... encontraremos os itens de controle equivalentes na amostra...
         ItemDeControle itemDeControleDaAmostra = buscaSeHaItemDeControleEquivalenteNaLista(itemDeControleDaQualidade, listaDeItensDeControleDaAmostraInserida);
         //... somente os itens de controle comuns a qualidade e a amostra serao relevantes para a qualidade atualizada...
         if(itemDeControleDaAmostra!=null)
         {
            /*
             * Precisamos recalcular (quando cada um desses existir):
             * 1 - valor
             * 2 - desvioPadraoValor
             * 3 - embarcado
             * 4 - desvioPadraoEmbarcado
             */
            
            //1) - o valor medio sera a media dos valores dos itens equivalentes
            if(itemDeControleDaAmostra.getValor()!=null)
            {
               //... caso a qualidade ainda nao possua nenhum valor, o valor sera dado pelo valor da amostra ...
               itemDeControleDaQualidade.setValor(calculaValorMedio(itemDeControleDaQualidade));
               //2) - o desvio padrao medio sera calculado a partir das medias das amostras
//               itemDeControleDaQualidade.setDesvioPadraoValor(calculaDesvioPadrao(itemDeControleDaQualidade));
            }
            
            //3)o valor medio sera a media dos valores dos itens equivalentes
            if(itemDeControleDaAmostra.getEmbarcado()!=null)
            {
               //... caso a qualidade ainda nao possua nenhum valor, o valor sera dado pelo valor da amostra ...
               itemDeControleDaQualidade.setEmbarcado(calculaValorMedioEmbarcado(itemDeControleDaQualidade));
               //4)- o desvio padrao medio sera calculado a partir das medias das amostras
//               itemDeControleDaQualidade.setDesvioPadraoEmbarcado(calculaDesvioPadraoEmbarcado(itemDeControleDaQualidade));
            }
         }
      }     
   }
   
   /**
    * Busca um determinado item de controle em uma lista de itens de controle.
    * @param itemDeControleBuscado o item de controle buscado
    * @param listaDeItensDeControle a lista de itens de controle
    * @return o item de controle encontrado ou null caso nao o encontre
    */
   public static ItemDeControle buscaSeHaItemDeControleEquivalenteNaLista(ItemDeControle itemDeControleBuscado, List<ItemDeControle> listaDeItensDeControle)
   {
      ItemDeControle resultadoDaBusca = null;
      
      for(ItemDeControle itemDeControleAnalisado : listaDeItensDeControle)
      {
         if(itemDeControleAnalisado.getTipoItemControle().equals(itemDeControleBuscado.getTipoItemControle()))
         {
            resultadoDaBusca = itemDeControleAnalisado;
            break;
         }
      }
      
      return resultadoDaBusca;
   }

   /**
    * Calcula m�dia ponderada de dois desvios padrões, levando em consideração a regra de propagação de erro
    * 
    * @param desvioPadraoX
    * @param quantidadeX
    * @param desvioPadraoY
    * @param quantidadeY
    * @return o desvio padr�o m�dio ponderado pelas quantidades
    */
   public static Double calculaDesvioPadraoMedioPonderadoNaInsercao(Double desvioPadraoX, Double quantidadeX, Double desvioPadraoY, Double quantidadeY)
   {
      /*
       * Regra de propagacao de erro para a soma e subtracao de grandezas afetadas por erro
       * w = ax + by
       * desvioPadraoW = sqrt( (a*desvioPadraoX)^2 + (b*desvioPadraoY)^2 )
       * 
       * * para o caso acima:
       * 
       * a = quantidadeX/(quantidadeX+quantidadeY) 
       * b = quantidadeY/(quantidadeX+quantidadeY)
       * 
       */
      
      Double desvioPadraoW = (Math.sqrt( Math.pow((desvioPadraoX*quantidadeX)/(quantidadeX+quantidadeY),2) + Math.pow((desvioPadraoY*quantidadeY)/(quantidadeX+quantidadeY),2)));
      
      return desvioPadraoW;
   }

   /**
    * Calcula a media ponderada de dois valores
    * 
    * @param valor1
    * @param quantidade1
    * @param valor2
    * @param quantidade2
    * @return o valor medio ponderado pelas quantidades
    */
   public static Double calculaValorMedioPonderadoNaInsercao(Double valor1, Double quantidade1, Double valor2, Double quantidade2)
   {
      Double mediaPonderada = ((valor1*quantidade1)+(valor2*quantidade2))/(quantidade1+quantidade2);
      
      return mediaPonderada;
   }
   
   /**
    * Calcula o desvio padrao medio ponderado pelas quantidades dos produtos restantes apos a remocao de um elemento
    * 
    * @param desvioPadraoW o desvio padrao do produto resultante da blendagem antes da remocao do elemento
    * @param quantidadeXMaisY a quantidade do produto resultante da blendagem antes da remocao do elemento
    * @param desvioPadraoY o desvio padrao do elemento removido
    * @param quantidadeY a quantidade do elemento removido
    * @return o desvio padrao do produto resultante apos a remocao do produto da blendagem
    */
   public static Double calculaDesvioPadraoMedioPonderadoNaRemocao(Double desvioPadraoW, Double quantidadeXMaisY, Double desvioPadraoY, Double quantidadeY)
   {
      /*
       * Regra de propagacao de erro para a soma e subtracao de grandezas afetadas por erro
       * w = ax + by; 
       * desvioPadraoW = sqrt((a*desvioPadraoX)^2 + (b*desvioPadraoY)^2 );
       * 
       * para o caso acima:
       * 
       * a = quantidadeX/(quantidadeX+quantidadeY);
       * b = quantidadeY/(quantidadeX+quantidadeY);
       * 
       * dado que quantidadeX+quantidadeY=quantidadeXMaisY,
       * a= quantidadeX/(quantidadeXMaisY);
       * b= quantidadeY/(quantidadeXMaisY);
       * 
       * portanto,
       * 
       * desvioPadraoW = sqrt((quantidadeX/(quantidadeXMaisY)*desvioPadraoX)^2 + (quantidadeY/(quantidadeXMaisY)*desvioPadraoY)^2 );
       * 
       * da formula acima, queremos achar o desvioPadraoX, e temos que:
       * 
       * quantidadeX = quantidadeXMaisY-quantidadeY;
       * Entao,
       * 
       * desvioPadraoW = sqrt(((quantidadeXMaisY-quantidadeY)/(quantidadeXMaisY)*desvioPadraoX)^2 + (quantidadeY/(quantidadeXMaisY)*desvioPadraoY)^2 );
       * 
       * ((quantidadeXMaisY-quantidadeY)/(quantidadeXMaisY)*desvioPadraoX)^2 + (quantidadeY/(quantidadeXMaisY)*desvioPadraoY)^2 = desvioPadraoW^2;
       * 
       * ((quantidadeXMaisY-quantidadeY)/(quantidadeXMaisY)*desvioPadraoX)^2 = desvioPadraoW^2 - (quantidadeY/(quantidadeXMaisY)*desvioPadraoY)^2;
       * 
       * (quantidadeXMaisY-quantidadeY)/(quantidadeXMaisY)*desvioPadraoX = sqrt(desvioPadraoW^2 - (quantidadeY/(quantidadeXMaisY)*desvioPadraoY)^2);
       * 
       * desvioPadraoX = sqrt(desvioPadraoW^2 - (quantidadeY/(quantidadeXMaisY)*desvioPadraoY)^2)/((quantidadeXMaisY-quantidadeY)/(quantidadeXMaisY));
       */
      
      Double desvioPadraoX = Math.sqrt(Math.pow(desvioPadraoW, 2) - Math.pow((quantidadeY/(quantidadeXMaisY)*desvioPadraoY),2))/((quantidadeXMaisY-quantidadeY)/(quantidadeXMaisY)); 
      
      return desvioPadraoX;
   }

   /**
    * Calcula o valor medio ponderado apos a remocao de um dos produtos considerados anteriormente na media
    * 
    * @param valorMedio1Mais2 o valor medio total antes da remocao
    * @param quantidade1Mais2 a quantidade total antes da remocao
    * @param valor2 o valor do elemento removido
    * @param quantidade2 a quantidade do elemento removido
    * @return o valor medio do elemento resultante existente antes da remocao do elemento
    */
   public static Double calculaValorMedioPonderadoNaRemocao(Double valorMedio1Mais2, Double quantidade1Mais2, Double valor2, Double quantidade2)
   {
      /*
       * Na remocao, eu tenho o valor medio (valorMedio1Mais2), a quantidade total (quantidade1Mais2), o valor do produto removido (valor2)
       * e a quantidade do valor removido (quantidade2). Quero encontrar o valor do produto antes da insercao do produto removido (valor1).
       * Consigo a quantidade deste produto antes da insercao do produto removido (quantidade1) pela quantidade1Mais2 - quantidade2.
       * Dessa maneira, tenho todos os dados que preciso para achar valor1         
       *         
       * quantidade1Mais2 = quantidade1+quantidade2;
       * quantidade 1 = quantidade1Mais2 - quantidade2;
       * 
       * valorMedio1Mais2 = (valor1*quantidade1 + valor2*quantidade2) / quantidade1Mais2;
       * valorMedio1Mais2 = (valor1*(quantidade1Mais2-quantidade2) +valor2*quantidade2)/quantidade1Mais2;
       * valor1*(quantidade1Mais2-quantidade2) +valor2*quantidade2 = valorMedio1Mais2*quantidade1Mais2;
       * valor1*(quantidade1Mais2-quantidade2) = valorMedio1Mais2*quantidade1Mais2 - valor2*quantidade2;
       * valor1 = (valorMedio1Mais2*quantidade1Mais2 - valor2*quantidade2)/(quantidade1Mais2-quantidade2);
       */
      
      Double valor1 = (valorMedio1Mais2*quantidade1Mais2 - valor2*quantidade2)/(quantidade1Mais2-quantidade2);

      return valor1;
   }

   /**
    * @return the qualidade
    */
   public Qualidade getQualidade()
   {
      return qualidade;
   }

   /**
    * @param qualidade the qualidade to set
    */
   public void setQualidade(Qualidade qualidade)
   {
      this.qualidade = qualidade;
   }
}
