/**
 * 
 */
package com.hdntec.gestao.domain.produto.enums;

/**
 * Posseveis estados do farol
 * 
 * @author andre
 *
 */
public enum EnumEstadosDoFarol 
{
   /** compliant*/
   COMPLIANT,
   /** noncompliant */
   NONCOMPLIANT,
   /** overspecified*/
   OVERSPECIFIED;
   
   @Override
   public String toString()
   {
      //Fiz um override para que as strings aparecam user-friendly nos JComboBox das edicoes.
      //Este codigo primeiro coloca todas as letras minusculas e separa nomes que contenham o caracter "_"
      //Depois, ele faz um loop por todas as palavras, coloca as letras iniciais em maiusculo, e isola o resto da palavra em variaveis separadas.
      //Finalmente, um espaco e adicionado entre as palavras e elas sao adicionadas a um StringBuilder que as retorna como fixedname.
      String[] splitNames = name().toLowerCase().split("_");
      StringBuffer fixedName = new StringBuffer();
      for (int i = 0; i < splitNames.length; i++)
      {
         String firstLetter = splitNames[i].substring(0, 1).toUpperCase(), restOfWord = splitNames[i].substring(1), spacer = i == splitNames.length ? "" : " ";
         fixedName.append(firstLetter).append(restOfWord).append(spacer);
      }
      return fixedName.toString();
   }
   
}
