package com.hdntec.gestao.domain.vo.atividades;

import java.util.List;
import java.util.Map;

import com.hdntec.gestao.domain.planta.entity.MetaBaliza;
import com.hdntec.gestao.domain.planta.entity.MetaPatio;



public class EmpilhamentoEmergenciaVO extends AtividadeRecuperarEmpilharVO 
{
	private double taxaOperacao;
	
	/** mapa com as balizas de emergencia utilizadas por patio */
	Map<MetaPatio, List<MetaBaliza>> mapaBalizasPorPatio;
	
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
	public double getTaxaOperacao() {
		return taxaOperacao;
	}
	public void setTaxaOperacao(double taxaOperacao) {
		this.taxaOperacao = taxaOperacao;
	}
	public Map<MetaPatio, List<MetaBaliza>> getMapaBalizasPorPatio() {
		return mapaBalizasPorPatio;
	}
	public void setMapaBalizasPorPatio(
			Map<MetaPatio, List<MetaBaliza>> mapaBalizasPorPatio) {
		this.mapaBalizasPorPatio = mapaBalizasPorPatio;
	}
        
}

