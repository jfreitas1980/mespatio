/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hdntec.gestao.exceptions;

import com.hdntec.gestao.util.PropertiesUtil;

/**
 * Esta exceção é lançada quand tenta-se pegar a lista de pilhas virtuais de uma atividade que não manipula pilhas
 * @author andre (LT)
 */
public class AtividadeNaoManipulaPilhaException extends Exception
{
    /**
     * Serialização
     */
    private static final long serialVersionUID = 5518949313826318437L;

    public AtividadeNaoManipulaPilhaException() {
        super(PropertiesUtil.getMessage("exception.atividade.nao.manipula.pilha"));
    }

    public AtividadeNaoManipulaPilhaException(String message) {
        super(message);
    }

    public AtividadeNaoManipulaPilhaException(String message, Throwable cause) {
        super(message, cause);
    }

    public AtividadeNaoManipulaPilhaException(Throwable cause) {
        super(cause);
    }

}
