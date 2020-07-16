
package com.hdntec.gestao.domain.planta.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.ForeignKey;

import com.hdntec.gestao.domain.AbstractMetaEntity;
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.planta.entity.status.Campanha;
import com.hdntec.gestao.domain.planta.entity.status.Correia;
import com.hdntec.gestao.domain.planta.entity.status.Filtragem;
import com.hdntec.gestao.domain.planta.entity.status.Planta;
import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;
import com.hdntec.gestao.domain.vo.atividades.AtividadeRecuperarEmpilharVO;


@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {
    "nomeFiltragem"}))
public class MetaFiltragem extends AbstractMetaEntity<Filtragem> {

    /** Serializa  do Objeto */
    private static final long serialVersionUID = -1038231345790374262L;

    private Planta planta;

    private Long idFiltragem;

    /** Nome da Usina */
    private String nomeFiltragem;

    private Campanha campanha;

    private MetaCorreia metaCorreia;

    /**
     * Construtor Padrao
     */
    public MetaFiltragem() {
    }

    @Override
    @OneToMany(fetch = FetchType.LAZY,mappedBy="metaFiltragem")
    @Fetch(FetchMode.SELECT)
   // @Cascade(CascadeType.ALL)
    // @OrderBy("dtInicio asc")
    public List<Filtragem> getListaStatus() {
        return super.getListaStatus();
    }

    @Override
    public void incluirNovoStatus(Filtragem novoStatus, Date horaStatus) {
        super.incluirNovoStatus(novoStatus, horaStatus);
        novoStatus.setMetaFiltragem(this);
    }

    @ManyToOne
    @JoinColumn(name = "ID_PLANTA", nullable = false, insertable = true)
    @ForeignKey(name = "fk_meta_filtragem_planta")
    public Planta getPlanta() {
        return planta;
    }

    public void setPlanta(Planta planta) {
        this.planta = planta;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "filtragem_seq")
    @SequenceGenerator(name = "filtragem_seq", sequenceName = "seqFiltragem")
    public Long getIdFiltragem() {
        return idFiltragem;
    }

    public void setIdFiltragem(Long idFiltragem) {
        this.idFiltragem = idFiltragem;
    }

    public String getNomeFiltragem() {
        return nomeFiltragem;
    }

    public void setNomeFiltragem(String nomeFiltragem) {
        this.nomeFiltragem = nomeFiltragem;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((idFiltragem == null) ? 0 : idFiltragem.hashCode());
        result = prime * result + ((nomeFiltragem == null) ? 0 : nomeFiltragem.hashCode());
        result = prime * result + ((planta == null) ? 0 : planta.hashCode());
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
        MetaFiltragem other = (MetaFiltragem) obj;
        if (idFiltragem == null) {
            if (other.idFiltragem != null)
                return false;
        } else if (!idFiltragem.equals(other.idFiltragem))
            return false;
        if (nomeFiltragem == null) {
            if (other.nomeFiltragem != null)
                return false;
        } else if (!nomeFiltragem.equals(other.nomeFiltragem))
            return false;
        if (planta == null) {
            if (other.planta != null)
                return false;
        } else if (!planta.equals(other.planta))
            return false;
        return true;
    }

    /**
     * gerarAtividadeUsina
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 06/07/2010
     * @see
     * @param 
     * @return List<Usina>
     */
    public static List<Filtragem> gerarAtividadeFiltragem(AtividadeRecuperarEmpilharVO recuperacaoVO, Atividade atividade,
                    Boolean finalizarAtividade,List<Correia> correiasDestino) {
        List<Filtragem> result = new ArrayList<Filtragem>();
        Atividade atividadeAnterior = null;
        /**
         *  2- aplica atividade nas usinas 
         */
        for (MetaFiltragem metaFiltragem : recuperacaoVO.getListaFiltragens()) {
            Filtragem filtragem = null;

            if (finalizarAtividade) {
                filtragem = metaFiltragem.clonarStatus(recuperacaoVO.getDataFim());
            } else {
                filtragem = metaFiltragem.clonarStatus(recuperacaoVO.getDataInicio());
            }

            if (filtragem.getAtividade() != null && filtragem.getEstado().equals(EstadoMaquinaEnum.OPERACAO)) {
                atividadeAnterior = filtragem.getAtividade();
                atividade.setFinalizada(Boolean.TRUE);
            }

            filtragem.setAtividade(atividade);

            // seta o estado da maquina
            if (finalizarAtividade) {
                filtragem.setEstado(EstadoMaquinaEnum.OCIOSA);
                filtragem.setAtividade(null);
            } else {
                filtragem.setEstado(EstadoMaquinaEnum.OPERACAO);
            }
            if (correiasDestino != null)
                //gera atividade para a correia da maquina                   
            //gera atividade para a correia da maquina   
            correiasDestino.addAll(MetaCorreia.gerarAtividadeCorreia(filtragem,finalizarAtividade));
           
            
            result.add(filtragem);
            
            
            
        }

        result.addAll(finalizarAtividadeFiltragem(recuperacaoVO, finalizarAtividade, atividadeAnterior));
        return result;
    }

    /**
     * finalizarAtividadeUsina
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 06/07/2010
     * @see
     * @param 
     * @return List<Usina>
     */
    public static List<Filtragem> finalizarAtividadeFiltragem(AtividadeRecuperarEmpilharVO recuperacaoVO,
                    Boolean finalizarAtividade, Atividade atividadeAnterior) {
        List<Filtragem> result = new ArrayList<Filtragem>();

        if (atividadeAnterior != null
                        && atividadeAnterior.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaFiltragens() != null) {
            List<Filtragem> usinaAnteriores = atividadeAnterior.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0)
                            .getListaFiltragens();
            List<MetaFiltragem> usinaParada = new ArrayList<MetaFiltragem>();

            for (Filtragem usina : usinaAnteriores) {
                if (!recuperacaoVO.getListaFiltragens().contains(usina.getMetaFiltragem())) {
                    if (usina.getEstado().equals(EstadoMaquinaEnum.OPERACAO))
                        usinaParada.add(usina.getMetaFiltragem());
                }
            }

            if (usinaParada.size() > 0) {

                for (MetaFiltragem metaUsina : usinaParada) {
                    Filtragem usina = null;
                    if (finalizarAtividade) {
                        usina = metaUsina.clonarStatus(recuperacaoVO.getDataFim());
                    } else {
                        usina = metaUsina.clonarStatus(recuperacaoVO.getDataInicio());
                    }

                    usina.setAtividade(null);
                    usina.setEstado(EstadoMaquinaEnum.OCIOSA);
                    result.add(usina);
                }
            }
        }
        return result;
    }

    @Override
    public Filtragem clonarStatus(Date horario) {
        Filtragem result = null;
        result = super.clonarStatus(horario);
        result.setIdFiltragem(null);
        return result;
    }

   

    @ManyToOne
    public MetaCorreia getMetaCorreia() {
        return metaCorreia;
    }

    public void setMetaCorreia(MetaCorreia metaCorreia) {
        this.metaCorreia = metaCorreia;
    }

}