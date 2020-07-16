package com.hdntec.gestao.batch.controlador;

import java.util.Date;

public class IntegracaoParametrosVO {

    /** Identificador do Sistema */
    private Integer idSistema;

    /** Data/Hora da ultima integracao */
    private Date dataUltimaIntegracao;

    /** Data/Hora da ultima leitura */
    private Date dataUltimaLeitura;

    public Date getDataUltimaIntegracao() {
        return dataUltimaIntegracao;
    }

    public void setDataUltimaIntegracao(Date dataUltimaIntegracao) {
        this.dataUltimaIntegracao = dataUltimaIntegracao;
    }

    public Integer getIdSistema() {
        return idSistema;
    }

    public void setIdSistema(Integer idSistema) {
        this.idSistema = idSistema;
    }

    public Date getDataUltimaLeitura() {
        return dataUltimaLeitura;
    }

    public void setDataUltimaLeitura(Date dataUltimaLeitura) {
        this.dataUltimaLeitura = dataUltimaLeitura;
    }

}
