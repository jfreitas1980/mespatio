/**
 * Classe responsavel em armazenar as informacoes da orientacao de embarque
 * de uma carga do navio lidas do CRM
 *
 * @author Rodrigo Luchetta
 */
package com.hdntec.gestao.batch.integracaoCRM;

import java.io.Serializable;
import java.util.List;


public class IntegracaoOrientEmbarqueCRM implements Serializable {

    /** Serializacao do objeto */
    private static final long serialVersionUID = 791208626015355291L;

    /** Identificador sequencial da integracao */
    private Long idIntegracaoOrientEmbarqueCRM;

    /** Descricao da orientacao de embarque */
    private String descricaoOrientacaoEmbarque;

    /** Codigo do cargo do contrato */
    private Long cdContractCargo;

    /** codigo do tipo de produto */
    private String codigoTipoProduto;

    /** descricao do tipo de produto */
    private String descricaoTipoProduto;

    /** a lista dos itens de controle da orientacao de embarque */
    private List<IntegracaoItemControleCRM> listaIntegracaoItensControle;

    public Long getIdIntegracaoOrientEmbarqueCRM() {
        return idIntegracaoOrientEmbarqueCRM;
    }

    public void setIdIntegracaoOrientEmbarqueCRM(Long idIntegracaoOrientEmbarqueCRM) {
        this.idIntegracaoOrientEmbarqueCRM = idIntegracaoOrientEmbarqueCRM;
    }

    public String getDescricaoOrientacaoEmbarque() {
        return descricaoOrientacaoEmbarque;
    }

    public void setDescricaoOrientacaoEmbarque(String descricaoOrientacaoEmbarque) {
        this.descricaoOrientacaoEmbarque = descricaoOrientacaoEmbarque;
    }

    public Long getCdContractCargo() {
        return cdContractCargo;
    }

    public void setCdContractCargo(Long cdContractCargo) {
        this.cdContractCargo = cdContractCargo;
    }

    public List<IntegracaoItemControleCRM> getListaIntegracaoItensControle() {
        return listaIntegracaoItensControle;
    }

    public void setListaIntegracaoItensControle(List<IntegracaoItemControleCRM> listaIntegracaoItensControle) {
        this.listaIntegracaoItensControle = listaIntegracaoItensControle;
    }

    public String getCodigoTipoProduto() {
        return codigoTipoProduto;
    }

    public void setCodigoTipoProduto(String codigoTipoProduto) {
        this.codigoTipoProduto = codigoTipoProduto;
    }

    public String getDescricaoTipoProduto() {
        return descricaoTipoProduto;
    }

    public void setDescricaoTipoProduto(String descricaoTipoProduto) {
        this.descricaoTipoProduto = descricaoTipoProduto;
    }

}
