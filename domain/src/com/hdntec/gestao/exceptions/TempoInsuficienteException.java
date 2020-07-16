package com.hdntec.gestao.exceptions;

/**
 * Classe de excecao para quando o tempo de execucao de alguma atividade for insuficiente.
 * @author Ricardo Trabalho
 */
public class TempoInsuficienteException extends Exception {

    public TempoInsuficienteException() {
        super();
    }

    public TempoInsuficienteException(String message) {
        super(message);
    }

    public TempoInsuficienteException(String message, Throwable cause) {
        super(message, cause);
    }

    public TempoInsuficienteException(Throwable cause) {
        super(cause);
    }
}
