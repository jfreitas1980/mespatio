package com.hdntec.gestao.batch.integracaoRPUSINAS;

import java.util.Date;

public class IntegracaoDadosRPUSINAS {

    /** Identificado da transacao */
    private Integer id;

    /** Codigo da fase do processo */
    private Long cdFaseProcesso;

    /** Codigo do Item de Controle */
    private Long cdItemControle;

    /** Codigo do tipo de processo */
    private String cdTipoProcesso;

    /** Codigo da area responsavel */
    private String cdAreaRespEd;

    /** Flag de registro processado */
    private String processado;

    /** Valor medido do sistema mes */
    private Double valorMedido;

    /** Data da medicao do sistema mes */
    private Date dataMedicao;

    public String getCdAreaRespEd() {
        return cdAreaRespEd;
    }

    public void setCdAreaRespEd(String cdAreaRespEd) {
        this.cdAreaRespEd = cdAreaRespEd;
    }

    public Long getCdFaseProcesso() {
        return cdFaseProcesso;
    }

    public void setCdFaseProcesso(Long cdFaseProcesso) {
        this.cdFaseProcesso = cdFaseProcesso;
    }

    public Long getCdItemControle() {
        return cdItemControle;
    }

    public void setCdItemControle(Long cdItemControle) {
        this.cdItemControle = cdItemControle;
    }

    public String getCdTipoProcesso() {
        return cdTipoProcesso;
    }

    public void setCdTipoProcesso(String cdTipoProcesso) {
        this.cdTipoProcesso = cdTipoProcesso;
    }

    public Date getDataMedicao() {
        return dataMedicao;
    }

    public void setDataMedicao(Date dataMedicao) {
        this.dataMedicao = dataMedicao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProcessado() {
        return processado;
    }

    public void setProcessado(String processado) {
        this.processado = processado;
    }

    public Double getValorMedido() {
        return valorMedido;
    }

    public void setValorMedido(Double valorMedido) {
        this.valorMedido = valorMedido;
    }

}
