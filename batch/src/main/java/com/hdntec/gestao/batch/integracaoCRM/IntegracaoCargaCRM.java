/**
 * Classe responsavel em armazenar as informacoes das cargas de um navio lidas do CRM
 *
 * @author Rodrigo Luchetta
 */
package com.hdntec.gestao.batch.integracaoCRM;

import java.io.Serializable;

public class IntegracaoCargaCRM implements Serializable {

    /** Serializacao do Objeto */
    private static final long serialVersionUID = -3310829471378700712L;

    /** Identificador sequencial do objeto */
    private Long idIntegracaoCargaCRM;

    /** Quantidade necessária da carga */
    private Double quantidadeCarga;

    /** Descricao da carga (identificador) */
    private String descricaoCarga;

    /** Codigo do produto */
    private String codigoProduto;

    /** Chave para join do SAP */
    private String sap_vbap_vbeln;

    /** Orientacao de Embarque da carga */
    private IntegracaoOrientEmbarqueCRM orientacaoEmbarque;

    public String getCodigoProduto() {
        return codigoProduto;
    }

    public void setCodigoProduto(String codigoProduto) {
        this.codigoProduto = codigoProduto;
    }

    public String getDescricaoCarga() {
        return descricaoCarga;
    }

    public void setDescricaoCarga(String descricaoCarga) {
        this.descricaoCarga = descricaoCarga;
    }

    public Long getIdIntegracaoCargaCRM() {
        return idIntegracaoCargaCRM;
    }

    public void setIdIntegracaoCargaCRM(Long idIntegracaoCargaCRM) {
        this.idIntegracaoCargaCRM = idIntegracaoCargaCRM;
    }

    public Double getQuantidadeCarga() {
        return quantidadeCarga;
    }

    public void setQuantidadeCarga(Double quantidadeCarga) {
        this.quantidadeCarga = quantidadeCarga;
    }

    public String getSap_vbap_vbeln() {
        return sap_vbap_vbeln;
    }

    public void setSap_vbap_vbeln(String sap_vbap_vbeln) {
        this.sap_vbap_vbeln = sap_vbap_vbeln;
    }

    public IntegracaoOrientEmbarqueCRM getOrientacaoEmbarque() {
        return orientacaoEmbarque;
    }

    public void setOrientacaoEmbarque(IntegracaoOrientEmbarqueCRM orientacaoEmbarque) {
        this.orientacaoEmbarque = orientacaoEmbarque;
    }

}
