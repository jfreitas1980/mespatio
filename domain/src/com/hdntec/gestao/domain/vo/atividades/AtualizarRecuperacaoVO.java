
package com.hdntec.gestao.domain.vo.atividades;

import com.hdntec.gestao.domain.navios.entity.MetaCarga;

/** Classe contendo as informacoes coletadas na interface de recuperacao, utilizada para construir a atividade */
public class AtualizarRecuperacaoVO extends AtividadeRecuperarEmpilharVO {
    private static final long serialVersionUID = 1L;
    private String nomePorao;
    /**
     * carga da recuperacao
     */
    private MetaCarga metaCarga;

    @Override
    public MetaCarga getMetaCarga() {
        return metaCarga;
    }

    @Override
    public void setMetaCarga(MetaCarga metaCarga) {
        this.metaCarga = metaCarga;
    }

	public String getNomePorao() {
		return nomePorao;
	}

	public void setNomePorao(String nomePorao) {
		this.nomePorao = nomePorao;
	}

}
