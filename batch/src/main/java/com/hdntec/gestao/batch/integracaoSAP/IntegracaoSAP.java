package com.hdntec.gestao.batch.integracaoSAP;

import java.io.Serializable;
import java.util.Date;

public class IntegracaoSAP implements Serializable {
    private static final long serialVersionUID = 1L;

    /** identificador sequencial da integracao */
    private Long idIntegracaoSAP;

    /** Codigo do SAP referente ao estoque de produtos */
    private Long cd_SAP_Dado;

    /** Identificador do grupo do SAP (Sempre 1 ) */
    private Long cd_SAP_Grupo;

    /** titulo do grupo do dado */
    private String titulo;

    /** data da exportacao do dado pelo SAP */
    private Date dataLeitura;

    /** Valor do Dado exportado pelo SAP */
    private Double valorEstoque;

    /** Flag de registro processado */
    private Boolean processado;

    /** Codigo do tipo de produto no mespatio */
    private String cdProduto;

    public Long getCd_SAP_Dado() {
        return cd_SAP_Dado;
    }

    public Long getCd_SAP_Grupo() {
        return cd_SAP_Grupo;
    }

    public Date getDataLeitura() {
        return dataLeitura;
    }

    public Long getIdIntegracaoSAP() {
        return idIntegracaoSAP;
    }

    public String getTitulo() {
        return titulo;
    }

    public Double getValorEstoque() {
        return valorEstoque;
    }

    public void setCd_SAP_Dado(Long cd_SAP_Dado) {
        this.cd_SAP_Dado = cd_SAP_Dado;
    }

    public void setCd_SAP_Grupo(Long cd_SAP_Grupo) {
        this.cd_SAP_Grupo = cd_SAP_Grupo;
    }

    public void setDataLeitura(Date dataLeitura) {
        this.dataLeitura = dataLeitura;
    }

    public void setIdIntegracaoSAP(Long idIntegracaoSAP) {
        this.idIntegracaoSAP = idIntegracaoSAP;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setValorEstoque(Double valorEstoque) {
        this.valorEstoque = valorEstoque;
    }

    public Boolean getProcessado() {
        return processado;
    }

    public void setProcessado(Boolean processado) {
        this.processado = processado;
    }

   public String getCdProduto()
   {
      return cdProduto;
   }

   public void setCdProduto(String cdProduto)
   {
      this.cdProduto = cdProduto;
   }
    
}
