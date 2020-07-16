package br.com.cflex.supervision.stockyard.cadastros.vo;

import java.io.Serializable;

import br.com.cflex.supervision.stockYard.servidor.modelo.pilhas.Baliza;
import br.com.cflex.supervision.stockYard.servidor.modelo.pilhas.EnumTipoBaliza;

public class BalizaVO implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	private Baliza balizaInicial;
	private Baliza balizaFinal;
	private EnumTipoBaliza tipo;
	
	public Baliza getBalizaInicial() {
		return balizaInicial;
	}
	public void setBalizaInicial(Baliza balizaInicial) {
		this.balizaInicial = balizaInicial;
	}
	public Baliza getBalizaFinal() {
		return balizaFinal;
	}
	public void setBalizaFinal(Baliza balizaFinal) {
		this.balizaFinal = balizaFinal;
	}
	public EnumTipoBaliza getTipo() {
		return tipo;
	}
	public void setTipo(EnumTipoBaliza tipo) {
		this.tipo = tipo;
	}
	
	
}
