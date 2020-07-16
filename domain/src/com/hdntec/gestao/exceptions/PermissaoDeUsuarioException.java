package com.hdntec.gestao.exceptions;

/**
 *
 * @author Bruno Gomes
 */
public class PermissaoDeUsuarioException extends Exception{

     public PermissaoDeUsuarioException() {
        super();
    }

    public PermissaoDeUsuarioException(String message, Throwable cause) {
        super(message, cause);
    }

    public PermissaoDeUsuarioException(String message) {
        super(message);
    }

    public PermissaoDeUsuarioException(Throwable cause) {
        super(cause);
    }

}
