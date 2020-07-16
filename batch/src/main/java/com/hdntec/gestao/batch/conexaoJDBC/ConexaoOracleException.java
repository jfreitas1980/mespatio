package com.hdntec.gestao.batch.conexaoJDBC;

public class ConexaoOracleException extends RuntimeException {
	
	public ConexaoOracleException() {
		super("Erro ao conectar ao banco oracle");
    }

    public ConexaoOracleException(String message) {
    	super(message);
    }

    public ConexaoOracleException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConexaoOracleException(Throwable cause) {
        super(cause);
    }
}
