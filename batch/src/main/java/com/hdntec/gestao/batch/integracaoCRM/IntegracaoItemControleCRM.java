package com.hdntec.gestao.batch.integracaoCRM;

public class IntegracaoItemControleCRM {

    /** identificador de item de controle */
    private Long idIntegracaoItemControleCRM;

    /** mostra se item eh relevante */
    private Boolean relevante;

    /** descricao do tipo de produto */
    private String descricaoTipoItemControle;

    /** Unidade de medida da variacao do tipo de item de controle */
    private String unidade;

    /** O valor inicial da escala */
    private Integer inicioEscala;

    /** O valor final da escala */
    private Integer fimEscala;

    /** A multiplicidade em que a escala ira aparecer na barra numerica */
    private Integer multiplicidadeEscala;

    /** Coeficiente para calculo do valor estimado de itens de controle deste tipo. */
    private Double coeficiente;

    /** tipo de processo do item de controle (HH - Bi-horaria, DD - diaria) */
    private String tipoProcesso;

    /** Codigo da area resp ed */
    private String areaRespED;

    /** Codigo do item de controle usado pelo cliente */
    private Long cdTipoItemControle;

    /** o valor minimo garantido do item de controle */
    private Double valorGarantidoMinimo;

    /** o valor maximo garantido do item de controle */
    private Double valorGarantidoMaximo;

    /** o valor tipico minimo do item de controle */
    private Double valorTipicoMinimo;

    /** o valor tipico minimo do item de controle */
    private Double valorTipicoMaximo;

    public String getAreaRespED() {
        return areaRespED;
    }

    public void setAreaRespED(String areaRespED) {
        this.areaRespED = areaRespED;
    }

    public Long getCdTipoItemControle() {
        return cdTipoItemControle;
    }

    public void setCdTipoItemControle(Long cdTipoItemControle) {
        this.cdTipoItemControle = cdTipoItemControle;
    }

    public Double getCoeficiente() {
        return coeficiente;
    }

    public void setCoeficiente(Double coeficiente) {
        this.coeficiente = coeficiente;
    }

    public String getDescricaoTipoItemControle() {
        return descricaoTipoItemControle;
    }

    public void setDescricaoTipoItemControle(String descricaoTipoItemControle) {
        this.descricaoTipoItemControle = descricaoTipoItemControle;
    }

    public Integer getFimEscala() {
        return fimEscala;
    }

    public void setFimEscala(Integer fimEscala) {
        this.fimEscala = fimEscala;
    }

    public Long getIdIntegracaoItemControleCRM() {
        return idIntegracaoItemControleCRM;
    }

    public void setIdIntegracaoItemControleCRM(Long idIntegracaoItemControleCRM) {
        this.idIntegracaoItemControleCRM = idIntegracaoItemControleCRM;
    }

    public Integer getInicioEscala() {
        return inicioEscala;
    }

    public void setInicioEscala(Integer inicioEscala) {
        this.inicioEscala = inicioEscala;
    }

    public Integer getMultiplicidadeEscala() {
        return multiplicidadeEscala;
    }

    public void setMultiplicidadeEscala(Integer multiplicidadeEscala) {
        this.multiplicidadeEscala = multiplicidadeEscala;
    }

    public Boolean getRelevante() {
        return relevante;
    }

    public void setRelevante(Boolean relevante) {
        this.relevante = relevante;
    }

    public String getTipoProcesso() {
        return tipoProcesso;
    }

    public void setTipoProcesso(String tipoProcesso) {
        this.tipoProcesso = tipoProcesso;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public Double getValorGarantidoMaximo() {
        return valorGarantidoMaximo;
    }

    public void setValorGarantidoMaximo(Double valorGarantidoMaximo) {
        this.valorGarantidoMaximo = valorGarantidoMaximo;
    }

    public Double getValorGarantidoMinimo() {
        return valorGarantidoMinimo;
    }

    public void setValorGarantidoMinimo(Double valorGarantidoMinimo) {
        this.valorGarantidoMinimo = valorGarantidoMinimo;
    }

    public Double getValorTipicoMaximo() {
        return valorTipicoMaximo;
    }

    public void setValorTipicoMaximo(Double valorTipicoMaximo) {
        this.valorTipicoMaximo = valorTipicoMaximo;
    }

    public Double getValorTipicoMinimo() {
        return valorTipicoMinimo;
    }

    public void setValorTipicoMinimo(Double valorTipicoMinimo) {
        this.valorTipicoMinimo = valorTipicoMinimo;
    }

}
