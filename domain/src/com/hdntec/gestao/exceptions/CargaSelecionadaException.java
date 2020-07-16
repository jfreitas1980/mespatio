package com.hdntec.gestao.exceptions;

public class CargaSelecionadaException extends Exception {

    public CargaSelecionadaException() {
        super();
    }

    public CargaSelecionadaException(String message) {
        super(message);
    }

    public CargaSelecionadaException(String message, Throwable cause) {
        super(message, cause);
    }

    public CargaSelecionadaException(Throwable cause) {
        super(cause);
    }
}
