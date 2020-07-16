package com.hdntec.gestao.exceptions;

import java.util.List;

import com.hdntec.gestao.util.PropertiesUtil;


public class ObjetoPesquisadoNaoEncontradoException extends Exception {

    String mensagem;

    public ObjetoPesquisadoNaoEncontradoException() {
        super();
    }

    public ObjetoPesquisadoNaoEncontradoException(String chaveMensagem, List<String> listaParametros) {

        String message = "";
        String mensagemProperties = PropertiesUtil.buscarPropriedade(chaveMensagem);

        String[] arrayMensages = mensagemProperties.split("#P");

        for (int i = 0; i < arrayMensages.length; i++) {
            message += arrayMensages[i];
            if (i < arrayMensages.length - 1) {
                message += listaParametros.get(i);
            }
        }

        this.mensagem = message;
    }

    public ObjetoPesquisadoNaoEncontradoException(String message) {
        this.mensagem = message;
    }

    public ObjetoPesquisadoNaoEncontradoException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjetoPesquisadoNaoEncontradoException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        return this.mensagem;
    }
}
