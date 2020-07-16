package com.hdntec.gestao.domain.integracao;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class IntegracaoParametros implements Serializable {

    /** Serializacao do Objeto */
    private static final long serialVersionUID = -8282515225622495770L;

    /** Identificador do Sistema */
    private Long idSistema;

    /** Data/Hora ultima atualização do sistema */
    private Date dataUltimaIntegracao;

    /** Data/Hora ultimo busca dos dados */
    private Date dataUltimaLeitura;

    /** Campo que indica se a tabela (campo ultima integracao) foi atualizado; (false =0; true =1) */
    private String atualizacaoCampoIntegracao;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDataUltimaIntegracao() {
        return dataUltimaIntegracao;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "integracaoparametros_seq")
    @SequenceGenerator(name = "integracaoparametros_seq", sequenceName = "seqintegracaoparametros")
    public Long getIdSistema() {
        return idSistema;
    }

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDataUltimaLeitura() {
        return dataUltimaLeitura;
    }

    @Column(nullable = false)
    public String getAtualizacaoCampoIntegracao() {
        return atualizacaoCampoIntegracao;
    }

    public void setAtualizacaoCampoIntegracao(String atualizacaoCampoIntegracao) {
        this.atualizacaoCampoIntegracao = atualizacaoCampoIntegracao;
    }

    public void setDataUltimaIntegracao(Date dataUltimaIntegracao) {
        this.dataUltimaIntegracao = dataUltimaIntegracao;
    }

    public void setIdSistema(Long idSistema) {
        this.idSistema = idSistema;
    }

    public void setDataUltimaLeitura(Date dataUltimaLeitura) {
        this.dataUltimaLeitura = dataUltimaLeitura;
    }
}