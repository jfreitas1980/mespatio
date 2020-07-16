/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hdntec.gestao.exceptions;

/**
 *
 * @author bgomes
 */
public class RastreabilidadeException extends Exception {

    public RastreabilidadeException() {
        super();
    }

    public RastreabilidadeException(String message) {
        super(message);
    }

    public RastreabilidadeException(String message, Throwable cause) {
        super(message, cause);
    }

    public RastreabilidadeException(Throwable cause) {
        super(cause);
    }
}
