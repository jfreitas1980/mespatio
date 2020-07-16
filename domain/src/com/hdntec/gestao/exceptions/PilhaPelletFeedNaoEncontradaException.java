package com.hdntec.gestao.exceptions;

public class PilhaPelletFeedNaoEncontradaException extends Exception
{

   public PilhaPelletFeedNaoEncontradaException()
   {
      super();
   }

   public PilhaPelletFeedNaoEncontradaException(String message)
   {
      super(message);
   }

   public PilhaPelletFeedNaoEncontradaException(String message, Throwable cause)
   {
      super(message, cause);
   }

   public PilhaPelletFeedNaoEncontradaException(Throwable cause)
   {
      super(cause);
   }
}
