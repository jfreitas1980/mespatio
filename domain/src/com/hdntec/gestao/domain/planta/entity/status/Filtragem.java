
package com.hdntec.gestao.domain.planta.entity.status;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.ForeignKey;

import com.hdntec.gestao.domain.StatusEntity;
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.planta.entity.MetaFiltragem;
import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;

@Entity
public class Filtragem extends StatusEntity<Filtragem> {

    /** Serializa  do Objeto */
    private static final long serialVersionUID = -1038231345790374262L;

    private Long idFiltragem;

    /** estado de operação do Usina */
    private EstadoMaquinaEnum estado;

    private MetaFiltragem metaFiltragem;

    private Atividade atividade;

    /**
     * Construtor Padrao
     */
    public Filtragem() {
    }
   
    @OneToOne
   /* @Cascade(value = {
    	      CascadeType.SAVE_UPDATE})*/
    @ForeignKey(name = "fk_filtragem_atividade")
    @JoinColumn(name = "idAtividade", nullable = true)
    public Atividade getAtividade() {
        return atividade;
    }

    public void setAtividade(Atividade atividade) {
        this.atividade = atividade;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "filt_seq")
    @SequenceGenerator(name = "filt_seq", sequenceName = "seqfilt")
    public Long getIdFiltragem() {
        return idFiltragem;
    }

    public void setIdFiltragem(Long idFiltragem) {
        this.idFiltragem = idFiltragem;
    }

    @ManyToOne
    @JoinColumn(name = "ID_META_FILT", nullable = false, insertable = true)
    @ForeignKey(name = "fk_meta_filt")
    public MetaFiltragem getMetaFiltragem() {
        return metaFiltragem;
    }

    public void setMetaFiltragem(MetaFiltragem metaFiltragem) {
        this.metaFiltragem = metaFiltragem;
    }

   
    @Transient
    public Boolean isFiltragemSelecionada() {
      // TODO Auto-generated method stub
        Boolean result = Boolean.FALSE;
        if (this.getAtividade() != null && this.getEstado().equals(EstadoMaquinaEnum.OPERACAO)) {            
                result= Boolean.TRUE;
        }
        return result;
  }
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    public EstadoMaquinaEnum getEstado() {
        return estado;
    }

    public void setEstado(EstadoMaquinaEnum estado) {
        this.estado = estado;
    }
        
}
