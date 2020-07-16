package com.hdntec.gestao.domain.planta.entity.status;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.hdntec.gestao.domain.ComparadorStatusEntity;
import com.hdntec.gestao.domain.planta.entity.MetaMaquinaDoPatio;


@Entity
public class ManutencaoMaquina extends Manutencao {

    private static final long serialVersionUID = -8425914360429968832L;

    public static ComparadorStatusEntity<ManutencaoMaquina> comparadorManutencao = new ComparadorStatusEntity<ManutencaoMaquina>();

    
    private MetaMaquinaDoPatio maquina;

    public ManutencaoMaquina() {
    }
    
    @ManyToOne    
    public MetaMaquinaDoPatio getMaquina() {
        return maquina;
    }

    public void setMaquina(MetaMaquinaDoPatio maquina) {
        this.maquina = maquina;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((maquina == null) ? 0 : maquina.hashCode());
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
        ManutencaoMaquina other = (ManutencaoMaquina) obj;
        if (maquina == null) {
            if (other.maquina != null)
                return false;
        } else if (!maquina.equals(other.maquina))
            return false;
        return true;
    }

    

}
