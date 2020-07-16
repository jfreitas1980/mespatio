/**
 * 
 */
package com.hdntec.gestao.exceptions;

/**
 * Exceção lançada quando a qualidade estimada de um produto é null. Essa situação nunca pode acontecer. 
 *  
 * @author andre
 * 
 */
public class ProdutoSemQualidadeEstimadaException extends Exception
{

   /**
    * 
    */
   private static final long serialVersionUID = -7823303108044412570L;

   /**
    * 
    */
   public ProdutoSemQualidadeEstimadaException()
   {
      super();
   }

   /**
    * @param message
    * @param cause
    */
   public ProdutoSemQualidadeEstimadaException(String message, Throwable cause)
   {
      super(message, cause);
   }

   /**
    * @param message
    */
   public ProdutoSemQualidadeEstimadaException(String message)
   {
      super(message);
   }

   /**
    * @param cause
    */
   public ProdutoSemQualidadeEstimadaException(Throwable cause)
   {
      super(cause);
   }
}
