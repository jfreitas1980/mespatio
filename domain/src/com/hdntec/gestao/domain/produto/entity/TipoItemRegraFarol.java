
package com.hdntec.gestao.domain.produto.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.ForeignKey;

import com.hdntec.gestao.domain.ComparadorStatusEntity;
import com.hdntec.gestao.domain.StatusEntity;
import com.hdntec.gestao.domain.produto.enums.EnumValorRegraFarol;



@Entity
@Table(name = "TipoItemRegraFarol",
                uniqueConstraints=
                @UniqueConstraint(columnNames={"ID_ITEM", "dt_inicio","dt_fim","valorRegraFarol"}))                
public class TipoItemRegraFarol extends StatusEntity<TipoItemRegraFarol> {
    private long id;
    
    public static ComparadorStatusEntity<TipoItemRegraFarol> comparadorStatusTipoItemRegraFarol = new ComparadorStatusEntity<TipoItemRegraFarol>();
    
    private TipoItemDeControle tipoItem;   
    
    /** Direcao de contabilizacao da regra */
    private EnumValorRegraFarol valorRegraFarol;

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID",nullable=false,insertable=true)
    @GeneratedValue(generator = "TipoItemRegraSeq")
    @SequenceGenerator(name = "TipoItemRegraSeq", sequenceName = "SEQ_TipoRegra")    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne  
    @JoinColumn(name="ID_ITEM",nullable=false)
    @ForeignKey(name = "fk_tipoItemTipoRegra")    
    public TipoItemDeControle getTipoItem() {
        return tipoItem;
    }

    public void setTipoItem(TipoItemDeControle tipoItem) {
        this.tipoItem = tipoItem;
    }

    /**
     * @param valorRegraFarol
     *            the valorRegraFarol to set
     */
    public void setValorRegraFarol(EnumValorRegraFarol valorRegraFarol) {
        this.valorRegraFarol = valorRegraFarol;
    }

    /**
     * @return the valorRegraFarol
     */
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    public EnumValorRegraFarol getValorRegraFarol() {
        return valorRegraFarol;
    }

    
}
