
package com.hdntec.gestao.domain.planta.entity.status;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.hdntec.gestao.domain.ComparadorStatusEntity;
import com.hdntec.gestao.domain.planta.entity.MetaCorreia;


@Entity
public class ManutencaoCorreia extends Manutencao {

    private static final long serialVersionUID = -8425914360429968832L;

    public static ComparadorStatusEntity<ManutencaoCorreia> comparadorManutencao = new ComparadorStatusEntity<ManutencaoCorreia>();
    
    private MetaCorreia correia;

    public ManutencaoCorreia() {
    }

    @ManyToOne    
    public MetaCorreia getCorreia() {
        return correia;
    }

    public void setCorreia(MetaCorreia correia) {
        this.correia = correia;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((correia == null) ? 0 : correia.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        ManutencaoCorreia other = (ManutencaoCorreia) obj;
        if (correia == null) {
            if (other.correia != null)
                return false;
        } else if (!correia.equals(other.correia))
            return false;
        return true;
    }

}
