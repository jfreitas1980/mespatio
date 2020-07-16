/**
 * 
 */
package com.hdntec.gestao.domain.produto.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Tipos de metas internas da equipe
 * 
 * @author andre
 * 
 */
public enum TipoMetaInternaEnum 
{
   /** a meta interna de produção*/
   META_DE_PRODUCAO,
   /** a meta interna de embarque*/
   META_DE_EMBARQUE;
   
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
   
	public static List<TipoMetaInternaEnum> getTodos() {
		List<TipoMetaInternaEnum> result = new ArrayList<TipoMetaInternaEnum>();

		for (TipoMetaInternaEnum value : TipoMetaInternaEnum.values()) {
			result.add(value);
		}
		return result;
	}
}
