package com.hdntec.gestao.exceptions;

public class ProdutoIncompativelException extends Exception
{
   /**
    * 
    */
   private static final long serialVersionUID = -7803176829313288858L;

   public ProdutoIncompativelException()
   {
      super();
   }

   public ProdutoIncompativelException(String message)
   {
      super(message);
   }

   public ProdutoIncompativelException(String message, Throwable cause)
   {
      super(message, cause);
   }

   public ProdutoIncompativelException(Throwable cause)
   {
      super(cause);
   }
}
