package br.com.cflex.samarco.supervision.stockyard.relatorio.exception;

import br.com.cflex.samarco.client.BundleUtils;

public class ErroGeracaoRelatorioPDF extends Exception {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	public ErroGeracaoRelatorioPDF() {
		super(BundleUtils.getMessage("msg.error.gerar.relatorio"));
	}

	public ErroGeracaoRelatorioPDF(String message) {
		super(message);
	}

	public ErroGeracaoRelatorioPDF(String message, Throwable cause) {
		super(message, cause);
	}

	public ErroGeracaoRelatorioPDF(Throwable cause) {
		super(cause);
	}
}
