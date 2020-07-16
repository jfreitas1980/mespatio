package com.hdntec.gestao.exceptions;

public class InverterPosicaoMaquinaException extends Exception {

    /**
    * 
    */
   private static final long serialVersionUID = 6672420454468307297L;

   public InverterPosicaoMaquinaException() {
        super();
    }

    public InverterPosicaoMaquinaException(String message) {
        super(message);
    }

    public InverterPosicaoMaquinaException(String message, Throwable cause) {
        super(message, cause);
    }

    public InverterPosicaoMaquinaException(Throwable cause) {
        super(cause);
    }
}
