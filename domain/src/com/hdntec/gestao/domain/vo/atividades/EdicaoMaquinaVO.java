package com.hdntec.gestao.domain.vo.atividades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.entity.LugarEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.planta.entity.MetaBaliza;
import com.hdntec.gestao.domain.planta.entity.MetaMaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.MetaPatio;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.MaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Patio;
import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;


public class EdicaoMaquinaVO implements Serializable {

    /** estado de operação da máquina */
    private EstadoMaquinaEnum estado;
    /**
     * identifica se a máquina está apontando o braco para o pátio inferior
     * ou superior
     */
    private Boolean bracoNoPatioInferior;

    /** a taxa de operação em ton/h */
    private Double taxaDeOperacaoNominal;

    /** a posição da máquina */
    private MetaBaliza posicao;

    /** a posição da máquina */
    private MetaMaquinaDoPatio maquina;

    
    /** a velocidade do deslocamento da maquina em balizas por hora (baliza/h) */
    private Double velocidadeDeslocamento;
    
    private Boolean girarLanca;

    public EstadoMaquinaEnum getEstado() {
		return estado;
	}

	public void setEstado(EstadoMaquinaEnum estado) {
		this.estado = estado;
	}

	public Boolean getBracoNoPatioInferior() {
		return bracoNoPatioInferior;
	}

	public void setBracoNoPatioInferior(Boolean bracoNoPatioInferior) {
		this.bracoNoPatioInferior = bracoNoPatioInferior;
	}

	public Double getTaxaDeOperacaoNominal() {
		return taxaDeOperacaoNominal;
	}

	public void setTaxaDeOperacaoNominal(Double taxaDeOperacaoNominal) {
		this.taxaDeOperacaoNominal = taxaDeOperacaoNominal;
	}

	public MetaBaliza getPosicao() {
		return posicao;
	}

	public void setPosicao(MetaBaliza posicao) {
		this.posicao = posicao;
	}

	public Double getVelocidadeDeslocamento() {
		return velocidadeDeslocamento;
	}

	public void setVelocidadeDeslocamento(Double velocidadeDeslocamento) {
		this.velocidadeDeslocamento = velocidadeDeslocamento;
	}

	public Double getTaxaDeOperacaoTemporaria() {
		return taxaDeOperacaoTemporaria;
	}

	public void setTaxaDeOperacaoTemporaria(Double taxaDeOperacaoTemporaria) {
		this.taxaDeOperacaoTemporaria = taxaDeOperacaoTemporaria;
	}

	public MetaPatio getPatio() {
		return patio;
	}

	public void setPatio(MetaPatio patio) {
		this.patio = patio;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	/** taxa de operacao temporaria (ton/h) */
    private Double taxaDeOperacaoTemporaria;

    private MetaPatio patio;
	
	private Date dataInicio;
	private Date dataFim;
	
	public EdicaoMaquinaVO() 
	{	
	}

	public MetaMaquinaDoPatio getMaquina() {
		return maquina;
	}

	public void setMaquina(MetaMaquinaDoPatio maquina) {
		this.maquina = maquina;
	}

	public Boolean getGirarLanca() {
		return girarLanca;
	}

	public void setGirarLanca(Boolean girarLanca) {
		this.girarLanca = girarLanca;
	}
			
}
