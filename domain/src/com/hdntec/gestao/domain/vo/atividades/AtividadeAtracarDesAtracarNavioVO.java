package com.hdntec.gestao.domain.vo.atividades;

import java.io.Serializable;
import java.util.Date;

import com.hdntec.gestao.domain.navios.entity.MetaNavio;
import com.hdntec.gestao.domain.plano.enums.TipoAtividadeEnum;
import com.hdntec.gestao.domain.planta.entity.MetaBerco;
import com.hdntec.gestao.domain.planta.entity.MetaPier;


public class AtividadeAtracarDesAtracarNavioVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MetaNavio metaNavio;
	private MetaPier  metaPierDestino;
	private MetaBerco metaBercoDestino;
	private TipoAtividadeEnum tipoAtividade;
	private Date dataInicio;    
	private Date dataFim;
	public AtividadeAtracarDesAtracarNavioVO() {
		
	}
    public MetaNavio getMetaNavio() {
        return metaNavio;
    }
    public void setMetaNavio(MetaNavio metaNavio) {
        this.metaNavio = metaNavio;
    }
    public MetaPier getMetaPierDestino() {
        return metaPierDestino;
    }
    public void setMetaPierDestino(MetaPier metaPierDestino) {
        this.metaPierDestino = metaPierDestino;
    }
    public MetaBerco getMetaBercoDestino() {
        return metaBercoDestino;
    }
    public void setMetaBercoDestino(MetaBerco metaBercoDestino) {
        this.metaBercoDestino = metaBercoDestino;
    }   
    public Date getDataInicio() {
        return dataInicio;
    }
    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }
    public TipoAtividadeEnum getTipoAtividade() {
        return tipoAtividade;
    }
    public void setTipoAtividade(TipoAtividadeEnum tipoAtividade) {
        this.tipoAtividade = tipoAtividade;
    }

			
}
