
package com.hdntec.gestao.domain.planta.entity.status;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import com.hdntec.gestao.domain.ComparadorStatusEntity;
import com.hdntec.gestao.domain.StatusEntity;
import com.hdntec.gestao.domain.planta.enums.PeriodicidadeEnum;


@MappedSuperclass
public class Manutencao extends StatusEntity<Manutencao> {

    private static final long serialVersionUID = -8425914360429968832L;

    public static ComparadorStatusEntity<Manutencao> comparadorManutencao = new ComparadorStatusEntity<Manutencao>();

    
    
    public Manutencao() {
    }

    /** id da entidade */
    private Long idManutencao;

    private PeriodicidadeEnum periodicidade;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "manutencao_seq")
    @SequenceGenerator(name = "manutencao_seq", sequenceName = "seqmanutencao")
    public Long getIdManutencao() {
        return idManutencao;
    }

    public void setIdManutencao(Long idManutencao) {
        this.idManutencao = idManutencao;
    }


    @Transient
    public Date getDataInicial() {
        // TODO Auto-generated method stub
        return getDtInicio();
    }

    @Transient
    public Date getDataFinal() {
        // TODO Auto-generated method stub
        return getDtFim();
    }

    @Transient
    public void setDataFinal(Date dataHoraDate) {
        // TODO Auto-generated method stub
        setDtFim(dataHoraDate);
    }

    @Transient
    public void setDataInicial(Date dataInicial) {
        // TODO Auto-generated method stub
        setDtInicio(dataInicial);
    }

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    public PeriodicidadeEnum getPeriodicidade() {
        return periodicidade;
    }

    public void setPeriodicidade(PeriodicidadeEnum periodicidade) {
        this.periodicidade = periodicidade;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((idManutencao == null) ? 0 : idManutencao.hashCode());
        result = prime * result + ((periodicidade == null) ? 0 : periodicidade.hashCode());
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
        Manutencao other = (Manutencao) obj;
        if (idManutencao == null) {
            if (other.idManutencao != null)
                return false;
        } else if (!idManutencao.equals(other.idManutencao))
            return false;
        if (periodicidade == null) {
            if (other.periodicidade != null)
                return false;
        } else if (!periodicidade.equals(other.periodicidade))
            return false;
        return true;
    }

}
