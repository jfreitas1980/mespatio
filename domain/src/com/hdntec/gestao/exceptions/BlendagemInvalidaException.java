package com.hdntec.gestao.exceptions;

public class BlendagemInvalidaException extends Exception {

    /**
    * 
    */
   private static final long serialVersionUID = 941221337516443003L;

   public BlendagemInvalidaException() {
        super();
    }

    public BlendagemInvalidaException(String message) {
        super(message);
    }

    public BlendagemInvalidaException(String message, Throwable cause) {
        super(message, cause);
    }

    public BlendagemInvalidaException(Throwable cause) {
        super(cause);
    }
}
