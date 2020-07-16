package com.hdntec.gestao.exceptions;

public class CampanhaIncompativelException extends Exception {

    public CampanhaIncompativelException() {
        super();
    }

    public CampanhaIncompativelException(String message) {
        super(message);
    }

    public CampanhaIncompativelException(String message, Throwable cause) {
        super(message, cause);
    }

    public CampanhaIncompativelException(Throwable cause) {
        super(cause);
    }
}
