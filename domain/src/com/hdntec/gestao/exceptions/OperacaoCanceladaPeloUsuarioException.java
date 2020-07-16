package com.hdntec.gestao.exceptions;

public class OperacaoCanceladaPeloUsuarioException extends Exception {

    public OperacaoCanceladaPeloUsuarioException() {
        super("Operação cancelada pelo usuário");
    }

    public OperacaoCanceladaPeloUsuarioException(String message) {
        super(message);
    }

    public OperacaoCanceladaPeloUsuarioException(String message, Throwable cause) {
        super(message, cause);
    }

    public OperacaoCanceladaPeloUsuarioException(Throwable cause) {
        super(cause);
    }
}
