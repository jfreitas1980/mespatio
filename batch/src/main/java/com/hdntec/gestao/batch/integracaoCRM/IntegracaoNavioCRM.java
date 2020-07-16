/**
 * Classe responsavel em armazenar as informacoes do navio ligas do CRM
 *
 * @author Rodrigo Luchetta
 */
package com.hdntec.gestao.batch.integracaoCRM;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class IntegracaoNavioCRM implements Serializable {

    /** Serializacao do Objeto */
    private static final long serialVersionUID = -4286961385867957547L;

    /** Identificador sequencia da integracao */
    private Long idIntegracaoNavioCRM;

    /** Codigo do Navio no CRM */
    private Long cdNavio;

    /** Nome do Navio no CRM */
    private String nomeNavio;

    /** Capacidade de carga do navio (DWT) */
    private Double capacidadeNavio;

    /** Data de Chegada do Navio */
    private Date dataChegadaNavio;

    /** Data de Saida do Navio */
    private Date dataSaidaNavio;

    /** Data do embarque do navio */
    private Date dataEmbarqueNavio;

    /** Data do ETA */
    private Date dataETA;

    /** Status do embarque do navio */
    private String statusEmbarque;

    /** Flag de registro processado */
    private Boolean processado;

    /** Chave para join do SAP */
    private String sap_vbap_vbeln;

    /** Codigo do cliente no sistema MES */
    private String codigoCliente;

    /** nome do cliente */
    private String nomeCliente;

    /** lista com as cargas do navio */
    private List<IntegracaoCargaCRM> listaCargasNavio;

    /** Flag que identifica apos atualizacao da tabela IntegracaoNavioCRM, se o navio foi inserido ou atualizado */
    private Boolean navioAtualizado;

    /* flag que identifica o berco onde o navio será atracado*/
    private String identificadorBerco;

   /** a data de chegada no navio na barra */
   private Date dataChegadaBarra;

   /** a data de atracacao do Navio */
   private Date dataAtracacao;

   /** a data de desatracacao do Navio */
   private Date dataDesatracacao;


    public Double getCapacidadeNavio() {
        return capacidadeNavio;
    }

    public void setCapacidadeNavio(Double capacidadeNavio) {
        this.capacidadeNavio = capacidadeNavio;
    }

    public Long getCdNavio() {
        return cdNavio;
    }

    public void setCdNavio(Long cdNavio) {
        this.cdNavio = cdNavio;
    }

    public String getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public Date getDataChegadaNavio() {
        return dataChegadaNavio;
    }

    public void setDataChegadaNavio(Date dataChegadaNavio) {
        this.dataChegadaNavio = dataChegadaNavio;
    }

    public Date getDataEmbarqueNavio() {
        return dataEmbarqueNavio;
    }

    public void setDataEmbarqueNavio(Date dataEmbarqueNavio) {
        this.dataEmbarqueNavio = dataEmbarqueNavio;
    }

    public Date getDataSaidaNavio() {
        return dataSaidaNavio;
    }

    public void setDataSaidaNavio(Date dataSaidaNavio) {
        this.dataSaidaNavio = dataSaidaNavio;
    }

    public Long getIdIntegracaoNavioCRM() {
        return idIntegracaoNavioCRM;
    }

    public void setIdIntegracaoNavioCRM(Long idIntegracaoNavioCRM) {
        this.idIntegracaoNavioCRM = idIntegracaoNavioCRM;
    }

    public String getNomeNavio() {
        return nomeNavio;
    }

    public void setNomeNavio(String nomeNavio) {
        this.nomeNavio = nomeNavio;
    }

    public Boolean getProcessado() {
        return processado;
    }

    public void setProcessado(Boolean processado) {
        this.processado = processado;
    }

    public String getSap_vbap_vbeln() {
        return sap_vbap_vbeln;
    }

    public void setSap_vbap_vbeln(String sap_vbap_vbeln) {
        this.sap_vbap_vbeln = sap_vbap_vbeln;
    }

    public String getStatusEmbarque() {
        return statusEmbarque;
    }

    public void setStatusEmbarque(String statusEmbarque) {
        this.statusEmbarque = statusEmbarque;
    }

    public Date getDataETA() {
        return dataETA;
    }

    public void setDataETA(Date dataETA) {
        this.dataETA = dataETA;
    }

    public List<IntegracaoCargaCRM> getListaCargasNavio() {
        return listaCargasNavio;
    }

    public void setListaCargasNavio(List<IntegracaoCargaCRM> listaCargasNavio) {
        this.listaCargasNavio = listaCargasNavio;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }
    
    public Boolean getNavioAtualizado() {
        return navioAtualizado;
    }

    public void setNavioAtualizado(Boolean navioAtualizado) {
        this.navioAtualizado = navioAtualizado;
    }

    public String getIdentificadorBerco() {
        return identificadorBerco;
    }

    public void setIdentificadorBerco(String identificadorBerco) {
        this.identificadorBerco = identificadorBerco;
    }

   public Date getDataAtracacao()
   {
      return dataAtracacao;
   }

   public void setDataAtracacao(Date dataAtracacao)
   {
      this.dataAtracacao = dataAtracacao;
   }

   public Date getDataChegadaBarra()
   {
      return dataChegadaBarra;
   }

   public void setDataChegadaBarra(Date dataChegadaBarra)
   {
      this.dataChegadaBarra = dataChegadaBarra;
   }

   public Date getDataDesatracacao()
   {
      return dataDesatracacao;
   }

   public void setDataDesatracacao(Date dataDesatracacao)
   {
      this.dataDesatracacao = dataDesatracacao;
   }
   
   @Override
public String toString() {
	   return this.getNomeNavio() +"-"+ this.getStatusEmbarque() +"-"+ this.getDataETA();
   }
    
}

