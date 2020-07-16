package com.hdntec.gestao.domain.modelo.psm;

import java.io.Serializable;

import com.hdntec.gestao.domain.planta.entity.status.Patio;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;


public class Psm implements Serializable {

	/**
	 * serialização do objeto
	 */
	private static final long serialVersionUID = 7438805970293119100L;
	private Long idPsm;
    /** a taxa de operação da usina, em ton/h */
    private Double taxaDeOperacao;
    private Patio patio;
    /** o pellet screening que sai do peneiramento semi movél*/
    private TipoProduto tipoScreening;
    
    /** pelota que sai do peneiramento semi movél*/
    private TipoProduto tipoPelota;
    
    /** o produto que entra no peneiramento semi movél*/
    private TipoProduto tipoProduto;
    private Double percentualScreening;
    private Double percentualPelota;

    public TipoProduto getTipoScreening() {
		return tipoScreening;
	}
	public void setTipoScreening(TipoProduto tipoScreening) {
		this.tipoScreening = tipoScreening;
	}
	public TipoProduto getTipoPelota() {
		return tipoPelota;
	}
	public void setTipoPelota(TipoProduto tipoPelota) {
		this.tipoPelota = tipoPelota;
	}
	public TipoProduto getTipoProduto() {
		return tipoProduto;
	}
	public void setTipoProduto(TipoProduto tipoProduto) {
		this.tipoProduto = tipoProduto;
	}
	public Double getPercentualScreening() {
		return percentualScreening;
	}
	public void setPercentualScreening(Double percentualScreening) {
		this.percentualScreening = percentualScreening;
	}
	public Double getPercentualPelota() {
		return percentualPelota;
	}
	public void setPercentualPelota(Double percentualPelota) {
		this.percentualPelota = percentualPelota;
	}
	
	/**
	 * @param idPsm the idPsm to set
	 */
	public void setIdPsm(Long idPsm) {
		this.idPsm = idPsm;
	}
	/**
	 * @return the idPsm
	 */
	public Long getIdPsm() {
		return idPsm;
	}
	/**
	 * @param taxaDeOperacao the taxaDeOperacao to set
	 */
	public void setTaxaDeOperacao(Double taxaDeOperacao) {
		this.taxaDeOperacao = taxaDeOperacao;
	}
	/**
	 * @return the taxaDeOperacao
	 */
	public Double getTaxaDeOperacao() {
		return taxaDeOperacao;
	}
	/**
	 * @param patio the patio to set
	 */
	public void setPatio(Patio patio) {
		this.patio = patio;
	}
	/**
	 * @return the patio
	 */
	public Patio getPatio() {
		return patio;
	}

}
