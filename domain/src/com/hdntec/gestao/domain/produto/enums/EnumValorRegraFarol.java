/**
 * 
 */
package com.hdntec.gestao.domain.produto.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Valores para a regra do farol
 * 
 * @author andre
 *
 */
public enum EnumValorRegraFarol 
{
   /** overspecification acontece quando o valor assumido está acima da meta*/
   CRESCENTE,
   /** overspecification acontece quando o valor assumido está abaixo da meta*/
   DECRESCENTE,
   /** não acontece overspecification*/
   INDIFERENTE;
   
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
   
   public static List<EnumValorRegraFarol> getTodos() {
       List<EnumValorRegraFarol> result = new ArrayList<EnumValorRegraFarol>();

       for (EnumValorRegraFarol value : EnumValorRegraFarol.values()) {
           result.add(value);
       }
       return result;
   }
}
