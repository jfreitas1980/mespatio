/**
 * 
 */
package com.hdntec.gestao.exceptions;


/**
 * Caso uma pilha seja selecionada no pátio para recuperação sem ao menos uma máquina capaz de recupera-la seja selecionada
 * @author andre
 *
 */
public class PilhaParaRecuperacaoSemMaquinaCapazException extends Exception
{

   /**
    * Serializacao
    */
   private static final long serialVersionUID = -3527165659152443934L;

   /**
    * 
    */
   public PilhaParaRecuperacaoSemMaquinaCapazException()
   {
      super();
   }

   /**
    * @param arg0
    * @param arg1
    */
   public PilhaParaRecuperacaoSemMaquinaCapazException(String arg0, Throwable arg1)
   {
      super(arg0, arg1);
   }

   /**
    * @param arg0
    */
   public PilhaParaRecuperacaoSemMaquinaCapazException(String arg0)
   {
      super(arg0);
   }

   /**
    * @param arg0
    */
   public PilhaParaRecuperacaoSemMaquinaCapazException(Throwable arg0)
   {
      super(arg0);
   }
}
