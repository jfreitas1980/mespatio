package com.hdntec.gestao.exceptions;

import java.util.List;

import com.hdntec.gestao.util.PropertiesUtil;


/**
 * Exceptions para leitura da planilha<br>
 * (p.ex: celula vazia, valor invalido...)
 * @author Bruno Gomes
 */
public class LeituraPlanilhaException extends Exception{
    
    /**
    * Serialização
    */
   private static final long serialVersionUID = 8131133913293285714L;
   String mensagem;

    public LeituraPlanilhaException(){
        super();
    }

    public LeituraPlanilhaException(String chaveMensagem, List<String> listaParametros){
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

    public LeituraPlanilhaException(String message){
        this.mensagem = message;
    }

    public LeituraPlanilhaException(String message, Throwable cause){
        super(message, cause);
    }

    public LeituraPlanilhaException(Throwable cause){
        super(cause);
    }

    @Override
    public String getMessage(){
        return this.mensagem;
    }
}
