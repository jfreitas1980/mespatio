package br.com.cflex.samarco.supervision.stockyard.relatorio.controles;

import br.com.cflex.samarco.supervision.stockyard.relatorio.enums.AlinhamentoEnum;
import br.com.cflex.samarco.supervision.stockyard.relatorio.enums.EstiloFonteEnum;

public class TipoConteudo {

	private AlinhamentoEnum alinhamento;
	private EstiloFonteEnum estiloFonte;
	
	public TipoConteudo(AlinhamentoEnum alinha, EstiloFonteEnum estilo) {
		this.alinhamento = alinha;
		this.estiloFonte = estilo;
	}
	
	/**
	 * Get alinhamento
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 25/08/2009
	 */
	public AlinhamentoEnum getAlinhamento() {
		return alinhamento;
	}
	/**
	 * Change alinhamento	
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param alinhamento 
	 * @since 25/08/2009
	 */
	public void setAlinhamento(AlinhamentoEnum alinhamento) {
		this.alinhamento = alinhamento;
	}
	/**
	 * Get estiloFonte
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 25/08/2009
	 */
	public EstiloFonteEnum getEstiloFonte() {
		return estiloFonte;
	}
	/**
	 * Change estiloFonte	
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param estiloFonte 
	 * @since 25/08/2009
	 */
	public void setEstiloFonte(EstiloFonteEnum estiloFonte) {
		this.estiloFonte = estiloFonte;
	}
	
	
}
