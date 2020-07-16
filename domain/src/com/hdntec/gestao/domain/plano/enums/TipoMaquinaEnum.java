package com.hdntec.gestao.domain.plano.enums;

/**
 * Tipos possíveis de máquinas que realizam atividades relevantes
 * @author bgomes
 * @author andre
 */
public enum TipoMaquinaEnum 
{
    /** Fazem o fluxo do pátio para as correias nas linhas de retomadas para embarque de produtos */
    RECUPERADORA,
    /** Faz o fluxo das correias que vêm das torres de distribuição ir para os pátios. A máquina empilha o produto nas balizas de um pátio específico. */
    EMPILHADEIRA,
    /** Máquinas que realizam as duas operações, a de empilhamento e a de  recuperação */
    EMPILHADEIRA_RECUPERADORA,
    /** É usada para recuperar de pilhas de emergência e para fazer rechego ( operação na qual uma parte da pilha de pelotas que foi separada e lançada para as periferias do pátio pela máquina recuperadora durante a recuperação é colocada de volta sobre a pilha ). As pilhas de emergência são usadas em situações específicas e essas pilhas são colocadas em uma moega que fica na correia. */
    PA_CARREGADEIRA;

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
