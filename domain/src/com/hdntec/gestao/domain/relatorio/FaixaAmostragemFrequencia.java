/**
 * Created on 25/08/2009
 * Project : CFlexRS
 *
 * Copyright © 2008 CFLEX.
 * Brasil
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of CFLEX. 
 * 
 * $Id: codetemplates.xml,v 1.1 2008/02/14 18:38:19 Exp $
 */

package com.hdntec.gestao.domain.relatorio;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.hdntec.gestao.domain.AuditTrail;
import com.hdntec.gestao.util.BundleUtils;


/**
 * <P><B>Description :</B><BR>
 * General FaixaAmostragemFrequencia
 * </P>
 * <P>
 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
 * @since 25/08/2009
 * @version $Revision: 1.1 $
 */
@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"faixaTonelagemInicial","faixaTonelagemFinal"}))
@SuppressWarnings("serial")
public class FaixaAmostragemFrequencia extends AuditTrail {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "faixaamostagem_seq")
    @SequenceGenerator(name = "faixaamostagem_seq", sequenceName = "seqfaixaamostragem")
	private Long idFaixa;

	/**faixa inicial Tonelagem navio (ton)*/
	@Column(nullable = false)
	private double faixaTonelagemInicial;
	/**faixa final Tonelagem navio (ton)*/
	@Column(nullable = false)
	private double faixaTonelagemFinal;
	/**Incremento a cada*/
	@Column(nullable = false)
	private double incremento;
	/**Teste de umidade e granulometria a cada*/
	@Column(nullable = false)
	private double granulometria;
	/**Testes de Tamboramento, compressão e análise química a cada*/
	@Column(nullable = false)
	private double tamboramento;

	
	
	/**
	 * Get idFaixa
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 28/08/2009
	 */
	public Long getIdFaixa() {
		return idFaixa;
	}
	/**
	 * Change idFaixa	
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param idFaixa 
	 * @since 28/08/2009
	 */
	public void setIdFaixa(Long idFaixa) {
		this.idFaixa = idFaixa;
	}
	/**
	 * Get faixaTonelagemInicial
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 25/08/2009
	 */
	public double getFaixaTonelagemInicial() {
		return faixaTonelagemInicial;
	}
	/**
	 * Change faixaTonelagemInicial	
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param faixaTonelagemInicial 
	 * @since 25/08/2009
	 */
	public void setFaixaTonelagemInicial(double faixaTonelagemInicial) {
		this.faixaTonelagemInicial = faixaTonelagemInicial;
	}
	/**
	 * Get faixaTonelagemFinal
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 25/08/2009
	 */
	public double getFaixaTonelagemFinal() {
		return faixaTonelagemFinal;
	}
	/**
	 * Change faixaTonelagemFinal	
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param faixaTonelagemFinal 
	 * @since 25/08/2009
	 */
	public void setFaixaTonelagemFinal(double faixaTonelagemFinal) {
		this.faixaTonelagemFinal = faixaTonelagemFinal;
	}
	/**
	 * Get incremento
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 25/08/2009
	 */
	public double getIncremento() {
		return incremento;
	}
	/**
	 * Change incremento	
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param incremento 
	 * @since 25/08/2009
	 */
	public void setIncremento(double incremento) {
		this.incremento = incremento;
	}
	/**
	 * Get granulometria
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 25/08/2009
	 */
	public double getGranulometria() {
		return granulometria;
	}
	/**
	 * Change granulometria	
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param granulometria 
	 * @since 25/08/2009
	 */
	public void setGranulometria(double granulometria) {
		this.granulometria = granulometria;
	}
	/**
	 * Get tamboramento
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 25/08/2009
	 */
	public double getTamboramento() {
		return tamboramento;
	}
	/**
	 * Change tamboramento	
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param tamboramento 
	 * @since 25/08/2009
	 */
	public void setTamboramento(double tamboramento) {
		this.tamboramento = tamboramento;
	}


	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(faixaTonelagemFinal);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(faixaTonelagemInicial);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(granulometria);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((idFaixa == null) ? 0 : idFaixa.hashCode());
		temp = Double.doubleToLongBits(incremento);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(tamboramento);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		FaixaAmostragemFrequencia other = (FaixaAmostragemFrequencia) obj;
		if (Double.doubleToLongBits(faixaTonelagemFinal) != Double
				.doubleToLongBits(other.faixaTonelagemFinal))
			return false;
		if (Double.doubleToLongBits(faixaTonelagemInicial) != Double
				.doubleToLongBits(other.faixaTonelagemInicial))
			return false;
		if (Double.doubleToLongBits(granulometria) != Double
				.doubleToLongBits(other.granulometria))
			return false;
		if (idFaixa == null) {
			if (other.idFaixa != null)
				return false;
		} else if (!idFaixa.equals(other.idFaixa))
			return false;
		if (Double.doubleToLongBits(incremento) != Double
				.doubleToLongBits(other.incremento))
			return false;
		if (Double.doubleToLongBits(tamboramento) != Double
				.doubleToLongBits(other.tamboramento))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(BundleUtils.getMessage("itemAmostragemFrequencia.faixaInicial"));
		result.append(": ").append(faixaTonelagemInicial).append("\n");
		result.append(BundleUtils.getMessage("itemAmostragemFrequencia.faixaFinal"));
		result.append(": ").append(faixaTonelagemFinal);
		return result.toString();
	}
}
