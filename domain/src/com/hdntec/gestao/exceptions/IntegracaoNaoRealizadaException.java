package com.hdntec.gestao.exceptions;

public class IntegracaoNaoRealizadaException extends Exception {

    public IntegracaoNaoRealizadaException() {
        super();
    }

    public IntegracaoNaoRealizadaException(String message) {
        super(message);
    }

    public IntegracaoNaoRealizadaException(String message, Throwable cause) {
        super(message, cause);
    }

    public IntegracaoNaoRealizadaException(Throwable cause) {
        super(cause);
    }

}
