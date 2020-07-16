package com.hdntec.gestao.domain.relatorio;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SituacaoPatioRelatorioVO implements Comparable<SituacaoPatioRelatorioVO>{

	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private String nomePilha;
	private String nomePatio;
	private Date dataInicio;
	private Date dataFim;
	private String nomeCliente;
	private String nomeProduto;
	private Double tonelada;
	private String emergencia;
	private String peneirar;
    private String codigoTipoProduto;
    private String codigoFamiliaTipoProduto;
    private Map<String,Double> itensDeControle;
	/**
	 * Get nomePilha
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 22/06/2009
	 */
	public String getNomePilha() {
		return nomePilha;
	}
	/**
	 * Change nomePilha	
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param nomePilha 
	 * @since 22/06/2009
	 */
	public void setNomePilha(String nomePilha) {
		this.nomePilha = nomePilha;
	}
	/**
	 * Get nomePatio
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 22/06/2009
	 */
	public String getNomePatio() {
		return nomePatio;
	}
	/**
	 * Change nomePatio	
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param nomePatio 
	 * @since 22/06/2009
	 */
	public void setNomePatio(String nomePatio) {
		this.nomePatio = nomePatio;
	}
	/**
	 * Get dataInicio
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 22/06/2009
	 */
	public Date getDataInicio() {
		return dataInicio;
	}
	/**
	 * 
	 * getDataInicioStr
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 22/06/2009
	 * @see
	 * @return
	 * @return Returns the String.
	 */
	public String getDataInicioStr() {
            if(dataInicio != null){
		return sdf.format(dataInicio);
            }
            return "";
	}
	/**
	 * Change dataInicio	
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param dataInicio 
	 * @since 22/06/2009
	 */
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}
	/**
	 * Get dataFim
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 22/06/2009
	 */
	public Date getDataFim() {
		return dataFim;
	}
	/**
	 * 
	 * getDataFimStr
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 22/06/2009
	 * @see
	 * @return
	 * @return Returns the String.
	 */
	public String getDataFimStr() {
            if(dataFim != null){
		return sdf.format(dataFim);
            }
            return "";
	}
	/**
	 * Change dataFim	
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param dataFim 
	 * @since 22/06/2009
	 */
	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}
	/**
	 * Get nomeCliente
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 22/06/2009
	 */
	public String getNomeCliente() {
		return nomeCliente;
	}
	/**
	 * Change nomeCliente	
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param nomeCliente 
	 * @since 22/06/2009
	 */
	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}
	/**
	 * Get nomeProduto
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 22/06/2009
	 */
	public String getNomeProduto() {
		return nomeProduto;
	}
	/**
	 * Change nomeProduto	
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param nomeProdtuo 
	 * @since 22/06/2009
	 */
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	/**
	 * Get tonelada
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 22/06/2009
	 */
	public Double getTonelada() {
		return tonelada;
	}
	/**
	 * Change tonelada	
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param tonelada 
	 * @since 22/06/2009
	 */
	public void setTonelada(Double tonelada) {
		this.tonelada = tonelada;
	}
	/**
	 * Get emergencia
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 22/06/2009
	 */
	public String getEmergencia() {
		return emergencia;
	}
	/**
	 * Change emergencia	
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param emergencia 
	 * @since 22/06/2009
	 */
	public void setEmergencia(String emergencia) {
		this.emergencia = emergencia;
	}
	/**
	 * Get peneirar
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 22/06/2009
	 */
	public String getPeneirar() {
		return peneirar;
	}
	/**
	 * Change peneirar	
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param peneirar 
	 * @since 22/06/2009
	 */
	public void setPeneirar(String peneirar) {
		this.peneirar = peneirar;
	}
	/**
	 * Get codigoTipoProduto
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 22/06/2009
	 */
	public String getCodigoTipoProduto() {
		return codigoTipoProduto;
	}
	/**
	 * Change codigoTipoProduto	
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param codigoTipoProduto 
	 * @since 22/06/2009
	 */
	public void setCodigoTipoProduto(String codigoTipoProduto) {
		this.codigoTipoProduto = codigoTipoProduto;
	}
	/**
	 * Get codigoFamiliaTipoProduto
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 22/06/2009
	 */
	public String getCodigoFamiliaTipoProduto() {
		return codigoFamiliaTipoProduto;
	}
	/**
	 * Change codigoFamiliaTipoProduto	
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param codigoFamiliaTipoProduto 
	 * @since 22/06/2009
	 */
	public void setCodigoFamiliaTipoProduto(String codigoFamiliaTipoProduto) {
		this.codigoFamiliaTipoProduto = codigoFamiliaTipoProduto;
	}

	/**
	 * 
	 * addItensDeControle
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 22/06/2009
	 * @see
	 * @param campo
	 * @param valor
	 * @return Returns the void.
	 */
	public void addItensDeControle(String campo, Double valor) {
		if (itensDeControle == null) {
			itensDeControle = new HashMap<String,Double>();
		}
		itensDeControle.put(campo, valor);
	}
	
	/**
	 * 
	 * getValorItensDeControle
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 22/06/2009
	 * @see
	 * @param campo
	 * @return
	 * @return Returns the Double.
	 */
	public Double getValorItensDeControle(String campo) {
		if (itensDeControle == null) {
			itensDeControle = new HashMap<String,Double>();
		}
		return itensDeControle.get(campo);
	}
	
	/**
	 * Get itensDeControle
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 22/06/2009
	 */
	public Map<String, Double> getItensDeControle() {
		return itensDeControle;
	}

	@Override
	public int compareTo(SituacaoPatioRelatorioVO obj) {
		if (this.codigoFamiliaTipoProduto == null || obj.getCodigoFamiliaTipoProduto() ==null) {
			return 0;
		}
		int result = this.codigoFamiliaTipoProduto.compareTo(obj.getCodigoFamiliaTipoProduto());
		if (result==0) {
			result = this.codigoTipoProduto.compareTo(obj.getCodigoTipoProduto());
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((codigoFamiliaTipoProduto == null) ? 0
						: codigoFamiliaTipoProduto.hashCode());
		result = prime
				* result
				+ ((codigoTipoProduto == null) ? 0 : codigoTipoProduto
						.hashCode());
		result = prime * result + ((dataFim == null) ? 0 : dataFim.hashCode());
		result = prime * result
				+ ((dataInicio == null) ? 0 : dataInicio.hashCode());
		result = prime * result
				+ ((emergencia == null) ? 0 : emergencia.hashCode());
		result = prime * result
				+ ((nomeCliente == null) ? 0 : nomeCliente.hashCode());
		result = prime * result
				+ ((nomePatio == null) ? 0 : nomePatio.hashCode());
		result = prime * result
				+ ((nomePilha == null) ? 0 : nomePilha.hashCode());
		result = prime * result
				+ ((nomeProduto == null) ? 0 : nomeProduto.hashCode());
		result = prime * result
				+ ((peneirar == null) ? 0 : peneirar.hashCode());
		result = prime * result
				+ ((tonelada == null) ? 0 : tonelada.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SituacaoPatioRelatorioVO other = (SituacaoPatioRelatorioVO) obj;
		if (codigoFamiliaTipoProduto == null) {
			if (other.codigoFamiliaTipoProduto != null)
				return false;
		} else if (!codigoFamiliaTipoProduto
				.equals(other.codigoFamiliaTipoProduto))
			return false;
		if (codigoTipoProduto == null) {
			if (other.codigoTipoProduto != null)
				return false;
		} else if (!codigoTipoProduto.equals(other.codigoTipoProduto))
			return false;
		if (dataFim == null) {
			if (other.dataFim != null)
				return false;
		} else if (!dataFim.equals(other.dataFim))
			return false;
		if (dataInicio == null) {
			if (other.dataInicio != null)
				return false;
		} else if (!dataInicio.equals(other.dataInicio))
			return false;
		if (emergencia == null) {
			if (other.emergencia != null)
				return false;
		} else if (!emergencia.equals(other.emergencia))
			return false;
		if (nomeCliente == null) {
			if (other.nomeCliente != null)
				return false;
		} else if (!nomeCliente.equals(other.nomeCliente))
			return false;
		if (nomePatio == null) {
			if (other.nomePatio != null)
				return false;
		} else if (!nomePatio.equals(other.nomePatio))
			return false;
		if (nomePilha == null) {
			if (other.nomePilha != null)
				return false;
		} else if (!nomePilha.equals(other.nomePilha))
			return false;
		if (nomeProduto == null) {
			if (other.nomeProduto != null)
				return false;
		} else if (!nomeProduto.equals(other.nomeProduto))
			return false;
		if (peneirar == null) {
			if (other.peneirar != null)
				return false;
		} else if (!peneirar.equals(other.peneirar))
			return false;
		if (tonelada == null) {
			if (other.tonelada != null)
				return false;
		} else if (!tonelada.equals(other.tonelada))
			return false;
		return true;
	}
	
}
