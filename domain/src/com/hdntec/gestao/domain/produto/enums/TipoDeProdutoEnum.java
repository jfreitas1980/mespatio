/**
 * 
 */
package com.hdntec.gestao.domain.produto.enums;

import java.util.ArrayList;
import java.util.List;


/**
 * Tipos possíveis de produtos
 * 
 * @author andre
 *
 */
public enum TipoDeProdutoEnum
{
   /** Pelotas de minério-de-ferro*/
   PELOTA,
   /** O insumo da pelotização*/
   PELLET_FEED,
   /** O fino da pelotização*/
   PELLET_SCREENING,
   /**o fino do peneiramento semi móvel*/
  PELLET_PSM,
  /**a pelota do peneiramento semi móvel*/
  PELOTA_PSM,
  LIXO,
  PR,
  SINTER_FEED;
   
   @Override
   public String toString()
   {
      //Fiz um override para que as strings apareçam user-friendly nos JComboBox das edições.
      //Este código primeiro coloca todas as letras minúsculas e separa nomes que contenham o caracter "_"
      //Depois, ele faz um loop por todas as palavras, coloca as letras iniciais em maiúsculo, e isola o resto da palavra em variáveis separadas.
      //Finalmente, um espaço é adicionado entre as palavras e elas são adicionadas a um StringBuilder que as retorna como fixedname.
      String[] splitNames = name().toLowerCase().split("_");
      StringBuffer fixedName = new StringBuffer();
      for (int i = 0; i < splitNames.length; i++)
      {
         String firstLetter = splitNames[i].substring(0, 1).toUpperCase(), restOfWord = splitNames[i].substring(1), spacer = i == splitNames.length ? "" : " ";
         fixedName.append(firstLetter).append(restOfWord).append(spacer);
      }
      return fixedName.toString();
   }
   
   /**
    * 
    * getTodos
    * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
    * @since 08/07/2009
    * @see
    * @return
    * @return Returns the List<TipoDeProdutoEnum>.
    */
   public static List<TipoDeProdutoEnum> getTodos() {
	   List<TipoDeProdutoEnum> result = new ArrayList<TipoDeProdutoEnum>();
	   
	   for(TipoDeProdutoEnum value : TipoDeProdutoEnum.values()) {
		   result.add(value);
	   }
	   
	   return result;
   }
}
