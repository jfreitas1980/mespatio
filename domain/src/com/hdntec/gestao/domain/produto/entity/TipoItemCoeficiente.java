
package com.hdntec.gestao.domain.produto.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
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



@Entity
@Table(name = "TipoItemCoeficiente",
                uniqueConstraints=
                @UniqueConstraint(columnNames={"ID_ITEM", "dt_inicio","dt_fim","valorDoCoeficiente"}))                
public class TipoItemCoeficiente extends StatusEntity<TipoItemCoeficiente> {
    private long id;
    
    public static ComparadorStatusEntity<TipoItemCoeficiente> comparadorStatusTipoItemCoeficiente = new ComparadorStatusEntity<TipoItemCoeficiente>();
    
    private TipoItemDeControle tipoItem;   
    
    /** o valor do coeficiente de degradacao */
    private Double valorDoCoeficiente;

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID",nullable=false,insertable=true)
    @GeneratedValue(generator = "TipoItemCoeficienteSeq")
    @SequenceGenerator(name = "TipoItemCoeficienteSeq", sequenceName = "SEQ_TipoCoeficiente")    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne  
    @JoinColumn(name="ID_ITEM",nullable=false)
    @ForeignKey(name = "fk_tipoItemTipo")    
    public TipoItemDeControle getTipoItem() {
        return tipoItem;
    }

    public void setTipoItem(TipoItemDeControle tipoItem) {
        this.tipoItem = tipoItem;
    }

    @Column(nullable = false)
    public Double getValorDoCoeficiente()
    {
       return valorDoCoeficiente;
    }


    
    public void setValorDoCoeficiente(Double valorDoCoeficiente)
    {
       this.valorDoCoeficiente = valorDoCoeficiente;
    }

    
}
