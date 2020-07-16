/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hdntec.gestao.domain.plano.enums;

public enum TipoAtividadeEnum {

    CHEGADA_DE_NAVIO,
    NAVIO_BARRA,
    NAVIO_EXCLUIDO,    
    MUDANCA_DE_CAMPANHA,    
    RESULTADO_DE_AMOSTRAGEM,
    SAIDA_DE_NAVIO,   
    TRATAMENTO_PSM,
    ATUALIZACAO_EMPILHAMENTO,
    ATUALIZACAO_RECUPERACAO,
    RETORNO_PELLET_FEED,
    ATUALIZACAO_PILHA_DE_EMERGENCIA,        
    MOVIMENTAR_PILHA_EMERGENCIA,
    MOVIMENTAR_PILHA_PSM,
    MOVIMENTAR_PILHA_PELLET_FEED,
    ATIVIDADE_EDICAO_BALIZAS,
    ATIVIDADE_EDICAO_MAQUINAS;


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

}
