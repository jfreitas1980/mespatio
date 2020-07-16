package com.hdntec.gestao.cliente.util.tabela;

public class FuncoesTabelaException extends RuntimeException {

    public FuncoesTabelaException() {
        super();
    }

    public FuncoesTabelaException(String message) {
        super(message);
    }

    public FuncoesTabelaException(String message, Throwable cause) {
        super(message, cause);
    }

    public FuncoesTabelaException(Throwable cause) {
        super(cause);
    }
}
