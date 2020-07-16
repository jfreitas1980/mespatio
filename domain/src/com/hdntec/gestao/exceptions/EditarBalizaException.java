/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hdntec.gestao.exceptions;

/**
 *
 * @author bgomes
 */
public class EditarBalizaException extends Exception {

    public EditarBalizaException() {
        super();
    }

    public EditarBalizaException(String message) {
        super(message);
    }

    public EditarBalizaException(String message, Throwable cause) {
        super(message, cause);
    }

    public EditarBalizaException(Throwable cause) {
        super(cause);
    }
}
