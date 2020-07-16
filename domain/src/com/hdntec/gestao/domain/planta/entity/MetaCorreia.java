
package com.hdntec.gestao.domain.planta.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.ForeignKey;

import com.hdntec.gestao.domain.AbstractMetaEntity;
import com.hdntec.gestao.domain.plano.enums.TipoAtividadeEnum;
import com.hdntec.gestao.domain.planta.entity.status.Correia;
import com.hdntec.gestao.domain.planta.entity.status.Filtragem;
import com.hdntec.gestao.domain.planta.entity.status.Manutencao;
import com.hdntec.gestao.domain.planta.entity.status.ManutencaoCorreia;
import com.hdntec.gestao.domain.planta.entity.status.MaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Planta;
import com.hdntec.gestao.domain.planta.entity.status.Usina;
import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;


/**
 * É a estrutura que transporta os produtos das usinas para os pátios, até as
 * máquinas empilhadeiras, nas irremediações dos pátios e dos pátios para o
 * píer, até a carregadora de navios.
 * <p>
 * Persite. - Tamanho: - Volume: poucos registros. -Período de Persistência:
 * cadastro de planta. - Freq. Update: raro. -Confiabilidade: deve ser
 * confiável.
 * 
 * @author andre
 * 
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {
                "numero", "nomeCorreia"}))
public class MetaCorreia extends AbstractMetaEntity<Correia> {

    /** Serialização do Objeto */
    private static final long serialVersionUID = 749117360809610855L;

    private Long idCorreia;

    /** Nome do Correia */
    private String nomeCorreia;

    /** numero da correia */
    private Integer numero;

    private Planta planta;

    /** a lista de máquinas presentes nesta correia */
    private List<MetaMaquinaDoPatio> listaDeMetaMaquinas;

    /** a lista de máquinas presentes nesta correia */
    private List<MetaUsina> listaDeMetaUsina;

    /** a lista de máquinas presentes nesta correia */
    private List<MetaFiltragem> listaDeMetaFiltragem;

    /** o Patio inferior onde a correia se apresentara */
    private MetaPatio metaPatioInferior;

    /** o Patio superior onde a correia se apresentara */
    private MetaPatio metaPatioSuperior;

    private List<ManutencaoCorreia> listaManutencao;

    /**
     * Construtor Padrao
     */
    public MetaCorreia() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "meta_correia_seq")
    @SequenceGenerator(name = "meta_correia_seq", sequenceName = "seq_meta_correia")
    public Long getIdCorreia() {
        return idCorreia;
    }

    public void setIdCorreia(Long idCorreia) {
        this.idCorreia = idCorreia;
    }

    @Column(nullable = false, length = 60)
    public String getNomeCorreia() {
        return nomeCorreia;
    }

    public void setNomeCorreia(String nomeCorreia) {
        this.nomeCorreia = nomeCorreia;
    }

    @Column(nullable = false)
    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    @Override
    public String toString() {
        return nomeCorreia;
    }

    @Override
    @OneToMany(fetch = FetchType.LAZY,mappedBy="metaCorreia")
    @Fetch(FetchMode.SELECT)    
    public List<Correia> getListaStatus() {
        return super.getListaStatus();
    }

    @OneToMany(fetch = FetchType.LAZY,mappedBy="metaCorreia")
    @Fetch(FetchMode.SELECT)
    public List<MetaMaquinaDoPatio> getListaDeMetaMaquinas() {
        if (listaDeMetaMaquinas == null) {
            listaDeMetaMaquinas = new ArrayList<MetaMaquinaDoPatio>();
        }
        return listaDeMetaMaquinas;
    }

    public void setListaDeMetaMaquinas(List<MetaMaquinaDoPatio> listaDeMaquinas) {
        this.listaDeMetaMaquinas = listaDeMaquinas;
    }

    @OneToOne
    @ForeignKey(name = "fk_correia_patioInferior")
    @JoinColumn(name = "id_patioinferior")
    public MetaPatio getMetaPatioInferior() {
        return metaPatioInferior;
    }

    public void setMetaPatioInferior(MetaPatio patioInferior) {
        this.metaPatioInferior = patioInferior;
    }

    @OneToOne
    @ForeignKey(name = "fk_correia_patiosuperior")
    @JoinColumn(name = "id_patiosuperior")
    public MetaPatio getMetaPatioSuperior() {
        return metaPatioSuperior;
    }

    public void setMetaPatioSuperior(MetaPatio patioSuperior) {
        this.metaPatioSuperior = patioSuperior;
    }

    @Override
    @Transient
    public void incluirNovoStatus(Correia novoStatus, Date horaStatus) {
        super.incluirNovoStatus(novoStatus, horaStatus);
        novoStatus.setMetaCorreia(this);
    }

    @Transient
    public void addMetaMaquinaDoPatio(List<MetaMaquinaDoPatio> itens) {
        for (MetaMaquinaDoPatio item : itens) {
            addMetaMaquinaDoPatio(item);
        }
    }

    @Transient
    public void addMetaMaquinaDoPatio(MetaMaquinaDoPatio item) {
        if (!getListaDeMetaMaquinas().contains(item)) {
            getListaDeMetaMaquinas().add(item);
            item.setMetaCorreia(this);
        }
    }

    @Transient
    public void addMetaUsina(List<MetaUsina> itens) {
        for (MetaUsina item : itens) {
            addMetaUsina(item);
        }
    }

    @Transient
    public void addMetaUsina(MetaUsina item) {
        if (!getListaDeMetaUsina().contains(item)) {
            getListaDeMetaUsina().add(item);
            item.setMetaCorreia(this);
        }
    }

    @Transient
    public void addMetaFiltragem(List<MetaFiltragem> itens) {
        for (MetaFiltragem item : itens) {
            addMetaFiltragem(item);
        }
    }

    @Transient
    public void addMetaFiltragem(MetaFiltragem item) {
        if (!getListaDeMetaFiltragem().contains(item)) {
            getListaDeMetaFiltragem().add(item);
            item.setMetaCorreia(this);
        }
    }

    @ManyToOne
    @JoinColumn(name = "ID_PLANTA", nullable = false, insertable = true)
    @ForeignKey(name = "fk_meta_corr_planta")
    public Planta getPlanta() {
        return planta;
    }

    public void setPlanta(Planta planta) {
        this.planta = planta;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((idCorreia == null) ? 0 : idCorreia.hashCode());
        result = prime * result + ((metaPatioInferior == null) ? 0 : metaPatioInferior.hashCode());
        result = prime * result + ((metaPatioSuperior == null) ? 0 : metaPatioSuperior.hashCode());
        result = prime * result + ((nomeCorreia == null) ? 0 : nomeCorreia.hashCode());
        result = prime * result + ((numero == null) ? 0 : numero.hashCode());
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
        MetaCorreia other = (MetaCorreia) obj;
        if (idCorreia == null) {
            if (other.idCorreia != null)
                return false;
        } else if (!idCorreia.equals(other.idCorreia))
            return false;
        if (metaPatioInferior == null) {
            if (other.metaPatioInferior != null)
                return false;
        } else if (!metaPatioInferior.equals(other.metaPatioInferior))
            return false;
        if (metaPatioSuperior == null) {
            if (other.metaPatioSuperior != null)
                return false;
        } else if (!metaPatioSuperior.equals(other.metaPatioSuperior))
            return false;
        if (nomeCorreia == null) {
            if (other.nomeCorreia != null)
                return false;
        } else if (!nomeCorreia.equals(other.nomeCorreia))
            return false;
        if (numero == null) {
            if (other.numero != null)
                return false;
        } else if (!numero.equals(other.numero))
            return false;
        if (planta == null) {
            if (other.planta != null)
                return false;
        } else if (!planta.equals(other.planta))
            return false;
        return true;
    }

    @Override
    public Correia clonarStatus(Date horario) {
        Correia correia = super.clonarStatus(horario);
        correia.setIdCorreia(null);
        return correia;
    }

    /**
     * 
     * TODO UNIFICAR ESSES METODOS EM ALGUMA CLASSE ABSTRATA
     * 
     * gerarAtividadeCorreia
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 17/06/2010
     * @see
     * @param 
     * @return List<Correia>
     */
    public static List<Correia> gerarAtividadeCorreia(MaquinaDoPatio novoStatus, Boolean finalizarAtividade) {
        List<Correia> correiasDestino = new ArrayList<Correia>();
        MetaCorreia metaCorreia = novoStatus.getMetaMaquina().getMetaCorreia();
        // aplica atividade na correia da maquina caso exista        
        if (metaCorreia != null) {
            Correia novaStatusCorreia = null;
            novaStatusCorreia = metaCorreia.clonarStatus(novoStatus.getDtInicio());

            /***
             *  TODO METODO PARA VERIFICAR ESTADO OCIOSO DA CORREIA
             * 
             */
            novaStatusCorreia.setEstado(novoStatus.getEstado());
            //novaStatusCorreia.setEstado(atualizaStatusOcioso(novoStatus.getEstado(),data,metaCorreia));
            correiasDestino.add(novaStatusCorreia);
        }
        return correiasDestino;
    }

    /**
     * gerarAtividadeCorreia
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 17/06/2010
     * @see
     * @param 
     * @return List<Correia>
     */
    public static List<Correia> gerarAtividadeCorreia(Usina novoStatus, Boolean finalizarAtividade) {
        List<Correia> correiasDestino = new ArrayList<Correia>();
        MetaCorreia metaCorreia = novoStatus.getMetaUsina().getMetaCorreia();
        // aplica atividade na correia da maquina caso exista        
        if (metaCorreia != null) {
            Correia novaStatusCorreia = null;
            novaStatusCorreia = metaCorreia.clonarStatus(novoStatus.getDtInicio());
            /***
             *  TODO METODO PARA VERIFICAR ESTADO OCIOSO DA CORREIA
             * 
             */
            novaStatusCorreia.setEstado(novoStatus.getEstado());
            correiasDestino.add(novaStatusCorreia);
        }
        return correiasDestino;
    }

    /**
     * gerarAtividadeCorreia
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 17/06/2010
     * @see
     * @param 
     * @return List<Correia>
     */
    public static List<Correia> gerarAtividadeCorreia(Filtragem novoStatus, Boolean finalizarAtividade) {
        List<Correia> correiasDestino = new ArrayList<Correia>();
        MetaCorreia metaCorreia = novoStatus.getMetaFiltragem().getMetaCorreia();
        // aplica atividade na correia da maquina caso exista        
        if (metaCorreia != null) {
            Correia novaStatusCorreia = null;
            novaStatusCorreia = metaCorreia.clonarStatus(novoStatus.getDtInicio());
            /***
             *  TODO METODO PARA VERIFICAR ESTADO OCIOSO DA CORREIA
             * 
             */
            novaStatusCorreia.setEstado(novoStatus.getEstado());
            //novaStatusCorreia.setEstado(atualizaStatusOcioso(novoStatus.getEstado(),data,metaCorreia));
            correiasDestino.add(novaStatusCorreia);
        }
        return correiasDestino;
    }

    /**
     * atualizaStatusOcioso
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 28/10/2010
     * @see
     * @param 
     * @return EstadoMaquinaEnum
     */
    public static EstadoMaquinaEnum atualizaStatusOcioso(EstadoMaquinaEnum estadoCorrente, Date hora, MetaCorreia correia) {
        EstadoMaquinaEnum estadoOciosaMaquina = estadoCorrente;
        try {

            //A correia nao pode estar sendo utilizada pelas maquinas para se tornar ociosa
            for (MetaMaquinaDoPatio maquina : correia.getListaDeMetaMaquinas()) {
                MaquinaDoPatio item = maquina.retornaStatusHorario(hora);
                if (item.getAtividade() != null && item.getEstado().equals(EstadoMaquinaEnum.OPERACAO)) {
                    estadoOciosaMaquina = EstadoMaquinaEnum.OPERACAO;
                }
            }

            //A correia nao pode estar sendo utilizada pelas usinas para se tornar ociosa

            for (MetaUsina usina : correia.getListaDeMetaUsina()) {
                Usina item = usina.retornaStatusHorario(hora);
                if (item.getAtividade() != null && item.getEstado().equals(EstadoMaquinaEnum.OPERACAO)
                                && item.getAtividade().getTipoAtividade().equals(TipoAtividadeEnum.ATUALIZACAO_RECUPERACAO)) {
                    estadoOciosaMaquina = EstadoMaquinaEnum.OPERACAO;
                }
            }

            for (MetaFiltragem filtro : correia.getListaDeMetaFiltragem()) {
                Filtragem item = filtro.retornaStatusHorario(hora);
                if (item.getAtividade() != null && item.getEstado().equals(EstadoMaquinaEnum.OPERACAO)
                                && item.getAtividade().getTipoAtividade().equals(TipoAtividadeEnum.ATUALIZACAO_RECUPERACAO)) {
                    estadoOciosaMaquina = EstadoMaquinaEnum.OPERACAO;
                }
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return estadoOciosaMaquina;

    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "metaCorreia")
    @Fetch(FetchMode.SELECT)
    public List<MetaUsina> getListaDeMetaUsina() {
        if (listaDeMetaUsina == null) {
            listaDeMetaUsina = new ArrayList<MetaUsina>();
        }
        return listaDeMetaUsina;
    }

    public void setListaDeMetaUsina(List<MetaUsina> listaDeMetaUsina) {
        this.listaDeMetaUsina = listaDeMetaUsina;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "metaCorreia")
    @Fetch(FetchMode.SELECT)
    public List<MetaFiltragem> getListaDeMetaFiltragem() {

        if (listaDeMetaFiltragem == null) {
            listaDeMetaFiltragem = new ArrayList<MetaFiltragem>();
        }
        return listaDeMetaFiltragem;
    }

    public void setListaDeMetaFiltragem(List<MetaFiltragem> listaDeMetaFiltragem) {
        this.listaDeMetaFiltragem = listaDeMetaFiltragem;
    }

    public void addManutencao(ManutencaoCorreia interdicao) {
        if (getListaManutencao() == null) {
            setListaManutencao(new ArrayList<ManutencaoCorreia>());
        }

        if (!getListaManutencao().contains(interdicao)) {
            getListaManutencao().add(interdicao);
            interdicao.setCorreia(this);
        }
    }

    public void addManutencao(List<ManutencaoCorreia> interdicoes) {
        if (interdicoes != null) {
            for (ManutencaoCorreia interdicao : interdicoes) {
                addManutencao(interdicao);
            }
        }
    }

    @Transient
    public Boolean correiaInterditado(Date horaExecucao) {
        Boolean result = Boolean.FALSE;
        Collections.sort(getListaManutencao(), Manutencao.comparadorManutencao);
        List<ManutencaoCorreia> campanhas = getListaManutencao();

        for (ManutencaoCorreia inter : campanhas) {
            if (inter.getDataFinal().getTime() >= horaExecucao.getTime()
                            && inter.getDataInicial().getTime() <= horaExecucao.getTime()) {
                result = Boolean.TRUE;
                break;
            }
        }
        return result;
    }

    @Transient
    public Boolean correiaInterditado(List<ManutencaoCorreia> lstInterdicao, Date horaExecucao) {
        Boolean result = Boolean.FALSE;
        Collections.sort(lstInterdicao, Manutencao.comparadorManutencao);
        for (ManutencaoCorreia inter : lstInterdicao) {
            if (inter.getDataFinal().getTime() >= horaExecucao.getTime()
                            && inter.getDataInicial().getTime() <= horaExecucao.getTime()) {
                result = Boolean.TRUE;
                break;
            }
        }
        return result;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = {
                    javax.persistence.CascadeType.MERGE, javax.persistence.CascadeType.PERSIST}, mappedBy = "correia")
    @Fetch(FetchMode.SELECT)
    @Cascade(value = {
                    CascadeType.SAVE_UPDATE, CascadeType.DELETE, CascadeType.DELETE_ORPHAN})
    public List<ManutencaoCorreia> getListaManutencao() {
        return listaManutencao;
    }

    public void setListaManutencao(List<ManutencaoCorreia> listaManutencao) {
        this.listaManutencao = listaManutencao;
    }

}
