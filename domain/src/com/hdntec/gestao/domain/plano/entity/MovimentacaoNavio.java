
package com.hdntec.gestao.domain.plano.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.ForeignKey;

import com.hdntec.gestao.domain.StatusEntity;
import com.hdntec.gestao.domain.navios.entity.status.Navio;
import com.hdntec.gestao.domain.planta.entity.status.Berco;


/**
 * Representa uma pilha virtual, um lugar de empilhamento ou recuperacao
 * 
 * @author andre
 * 
 */
@Entity
public class MovimentacaoNavio extends StatusEntity<MovimentacaoNavio> {

    /** Serialização */
    private static final long serialVersionUID = -4060095851777388634L;

    /** id da entidade */
    private Long idMovimentacaoNavio;

    private Berco bercoDestino;

    private Navio navio;
    
    /**
     * propriedade para determinar se este lugar de empilhamento ou recuperação
     * já foi executado ou ainda é para um plano futuro
     */
    private Boolean executado;
    
    private Atividade atividade;

    public MovimentacaoNavio() {
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "movNavio_seq")
    @SequenceGenerator(name = "movNavio_seq", sequenceName = "seqMovNavio")
    public Long getIdMovimentacaoNavio() {
        return idMovimentacaoNavio;
    }

    public void setIdMovimentacaoNavio(Long idMovimentacaoNavio) {
        this.idMovimentacaoNavio = idMovimentacaoNavio;
    }
    @OneToOne
    @ForeignKey(name = "fk_movnavio_berco")
    @JoinColumn(name = "id_Berco",nullable=true)
    // @Cascade(value = {
    //    CascadeType.MERGE,CascadeType.SAVE_UPDATE,CascadeType.DELETE_ORPHAN})
    public Berco getBercoDestino() {
        return bercoDestino;
    }

    public void setBercoDestino(Berco bercoDestino) {
        this.bercoDestino = bercoDestino;
    }
    @OneToOne
    @ForeignKey(name = "fk_movnavio_navio")
    @JoinColumn(name = "id_Navio",nullable=false)
    //@Cascade(value = {
      //  CascadeType.ALL})
    public Navio getNavio() {
        return navio;
    }

    public void setNavio(Navio navio) {
        this.navio = navio;
    }

    public Boolean getExecutado() {
        return executado;
    }

    public void setExecutado(Boolean executado) {
        this.executado = executado;
    }
    @OneToOne
   // @Cascade(CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "idAtividade", nullable = false)
    public Atividade getAtividade() {
        return atividade;
    }

    public void setAtividade(Atividade atividade) {
        this.atividade = atividade;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }


}
