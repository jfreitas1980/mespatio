package com.hdntec.gestao.batch.conexaoJDBC;

public class ConexaoPostgresException extends RuntimeException {
	
	public ConexaoPostgresException() {
		super("Erro ao conectar ao banco oracle");
    }

    public ConexaoPostgresException(String message) {
    	super(message);
    }

    public ConexaoPostgresException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConexaoPostgresException(Throwable cause) {
        super(cause);
    }
}
