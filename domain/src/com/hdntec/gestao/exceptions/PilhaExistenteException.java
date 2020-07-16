package com.hdntec.gestao.exceptions;

public class PilhaExistenteException extends Exception
{

   public PilhaExistenteException()
   {
      super();
   }

   public PilhaExistenteException(String message)
   {
      super(message);
   }

   public PilhaExistenteException(String message, Throwable cause)
   {
      super(message, cause);
   }

   public PilhaExistenteException(Throwable cause)
   {
      super(cause);
   }
}
