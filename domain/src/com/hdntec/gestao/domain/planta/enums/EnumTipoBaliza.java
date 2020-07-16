/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hdntec.gestao.domain.planta.enums;

/**
 * 
 * @author Ricardo Trabalho
 * @author andre (LT)
 */
public enum EnumTipoBaliza
{
   /** balizas de operação normal, recebem qualquer produto*/
   NORMAL, 
   /** balizas destinadas para emergencia da TP15 */
   EMERGENCIA_P5,
   /** balizas destinadas para emergencia da TP17 */
   EMERGENCIA_TP17,   
   EMERGENCIA_TP009,
   /** balizas que recebem o pellet screening das usinas*/
   PELLET_SCREENING, 
   /** balizas especiais onde está localizado o peneiramento semi-móvel (PSM)*/
   PSM,
   /** balizas com material advindo da bacia */
   POND,
   /** balizas com material cque sobrou de recuperação para fazer rechego */
   NIVELAMENTO,
   /** balizas especiais destinadas à pelota resultante do peneiramento semi móvel*/
   PELOTA_PSM,
   /**balizas especiais destibadas ao fino resultante do peneiramento semi móvel*/
   SCREENING_PSM;

   @Override
   public String toString()
   {
      // Fiz um override para que as strings apareçam user-friendly nos JComboBox das edições.
      // Este código primeiro coloca todas as letras minúsculas e separa nomes que contenham o caracter "_"
      // Depois, ele faz um loop por todas as palavras, coloca as letras iniciais em maiúsculo, e isola o resto da palavra em variáveis separadas.
      // Finalmente, um espaço é adicionado entre as palavras e elas são adicionadas a um StringBuilder que as retorna como fixedname.
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
