/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hdntec.gestao.exceptions;

import com.hdntec.gestao.util.PropertiesUtil;

/**
 * Esta exceção é lançada quando há uma tentativa de maniuplar dados relativos a duração temporal (horário de início e fim de execução) de uma atividade que não tem duração, ou seja, que é pontual.
 * @author andre (LT)
 */
public class AtividadePontualException extends Exception
{
    /**
     * Serialização
     */
    private static final long serialVersionUID = 3764127816668450943L;

    public AtividadePontualException() {
        super(PropertiesUtil.getMessage("exception.manipular.dados.temporal"));
    }

    public AtividadePontualException(String message) {
        super(message);
    }

    public AtividadePontualException(String message, Throwable cause) {
        super(message, cause);
    }

    public AtividadePontualException(Throwable cause) {
        super(cause);
    }


    @Override
    public Throwable getCause() {
        return super.getCause();
    }

    @Override
    public String getLocalizedMessage() {
        return super.getLocalizedMessage();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public synchronized Throwable initCause(Throwable cause) {
        return super.initCause(cause);
    }

}
