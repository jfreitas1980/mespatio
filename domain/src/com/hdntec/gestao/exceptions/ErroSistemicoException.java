package com.hdntec.gestao.exceptions;

import com.hdntec.gestao.util.PropertiesUtil;

public class ErroSistemicoException extends Exception {

    /**
    * serial gerado
    */
   private static final long serialVersionUID = -264303887113289471L;

   public ErroSistemicoException() {
        super(PropertiesUtil.getMessage("exception.erro.sistemico"));
    }

    public ErroSistemicoException(String message) {
        super(message);
    }

    public ErroSistemicoException(String message, Throwable cause) {
        super(message, cause);
    }

    public ErroSistemicoException(Throwable cause) {
        super(cause);
    }
    
    
}
