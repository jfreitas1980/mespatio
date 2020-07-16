package com.hdntec.gestao.exceptions;

import com.hdntec.gestao.util.PropertiesUtil;


public class SelecaoObjetoModoNuloException extends Exception {

    public SelecaoObjetoModoNuloException() {
        super(PropertiesUtil.buscarPropriedade("exception.selecao.objeto.modo.nulo"));
    }

    public SelecaoObjetoModoNuloException(String message) {
        super(message);
    }

    public SelecaoObjetoModoNuloException(String message, Throwable cause) {
        super(message, cause);
    }

    public SelecaoObjetoModoNuloException(Throwable cause) {
        super(cause);
    }
}
