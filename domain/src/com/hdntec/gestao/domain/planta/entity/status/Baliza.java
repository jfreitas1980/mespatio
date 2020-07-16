
package com.hdntec.gestao.domain.planta.entity.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.ForeignKey;

import com.hdntec.gestao.domain.ComparadorStatusEntity;
import com.hdntec.gestao.domain.GenericMetaEntity;
import com.hdntec.gestao.domain.StatusEntity;
import com.hdntec.gestao.domain.planta.comparadores.ComparadorBalizas;
import com.hdntec.gestao.domain.planta.entity.MetaBaliza;
import com.hdntec.gestao.domain.planta.enums.EnumTipoBaliza;
import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;
import com.hdntec.gestao.domain.produto.entity.Produto;


/**
 * É o espaço físico que foi padronizado como medida mínima de um pátio
 * para a operação de empilhamento e recuperação.
 * <p>
 * Persite. - Tamanho: - Volume: mais de um centena de registros. - Período de
 * Persistência: cadastro de planta. - Freq. Update: algumas vezes por dia.
 * -Confiabilidade: deve ser confiável.
 * 
 * @author andre
 * 
 */
@Entity
public class Baliza extends StatusEntity<Baliza> implements GenericMetaEntity<Pilha> {


    private static ComparadorStatusEntity<Pilha> comparadorStatus = new ComparadorStatusEntity<Pilha>();
    protected List<Long> listaHoraInicioStatus;

    /** Serialização do objeto */
    private static final long serialVersionUID = -1449375616083250262L;

    public static ComparadorBalizas comparadorBaliza = new ComparadorBalizas();
    /** identificador de baliza */
    private Long idBaliza;

    /** o horário de início da formação da baliza */
    private Long retirandoMaterial;

    
    /** o horário de início da formação da baliza */
    private Date horarioInicioFormacao;

    /** o horário de fim de formação da baliza */
    private Date horarioFimFormacao;

    /** o estado da operação da baliza (ociosa, operação ou manutenção) */
    private EstadoMaquinaEnum estado;

    /** o produto colocado nesta baliza */
    private Produto produto;

   
    private MetaBaliza metaBaliza;

    private List<Pilha> pilhas;

    public void setMetaBaliza(MetaBaliza metaBaliza) {
        this.metaBaliza = metaBaliza;
    }

    public Baliza() {
    }

    @ManyToOne
    @JoinColumn(name = "ID_META_BAL", nullable = false, insertable = true)
    @ForeignKey(name = "fk_meta_baliza")
    public MetaBaliza getMetaBaliza() {
        return metaBaliza;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "baliza_seq")
    @SequenceGenerator(name = "baliza_seq", sequenceName = "seqbaliza")
    public Long getIdBaliza() {
        return idBaliza;
    }

    @Transient
    public String getNomeBaliza() {
        return metaBaliza.getNomeBaliza();
    }

    @Transient
    public Double getLargura() {
        return metaBaliza.getLargura();
    }

    @Transient
    public Integer getNumero() {
        return metaBaliza.getNumero();
    }

    @Transient
    public Double getCapacidadeMaxima() {
        return metaBaliza.getCapacidadeMaxima();
    }

    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getHorarioInicioFormacao() {
        return horarioInicioFormacao;
    }

    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getHorarioFimFormacao() {
        return horarioFimFormacao;
    }

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    public EstadoMaquinaEnum getEstado() {
        return estado;
    }

    //@ManyToMany//(mappedBy="listaDeBalizas")
    @ManyToMany(targetEntity=Pilha.class)
    @Cascade(value={CascadeType.ALL})           
        @JoinTable(
            name="Baliza_Pilha",	
            joinColumns={@JoinColumn(name="idBaliza")},
            inverseJoinColumns={@JoinColumn(name="idPilha")}
        )         
    public List<Pilha> getPilhas() {
        if (pilhas == null) {
        	pilhas = new ArrayList<Pilha>();
        }            
        return pilhas;
    }

    @OneToOne
    //@Cascade(value={CascadeType.SAVE_UPDATE})
    @ForeignKey(name = "fk_baliza_produto")
    @JoinColumn(name = "id_Produto")
    public Produto getProduto() {
        return produto;
    }

    @Transient
    public Patio getPatio() {
        return getMetaBaliza().getMetaPatio().retornaStatusHorario(this.getDtInicio());
    }

    public void setEstado(EstadoMaquinaEnum estado) {
        this.estado = estado;
    }

    public void setHorarioFimFormacao(Date horarioFimFormacao) {
        this.horarioFimFormacao = horarioFimFormacao;
    }

    public void setHorarioInicioFormacao(Date horarioInicioFormacao) {
        this.horarioInicioFormacao = horarioInicioFormacao;
    }

    public void setIdBaliza(Long idBaliza) {
        this.idBaliza = idBaliza;
    }

    
    public void setPatio(Patio patio) {

    }

    public void setPilhas(List<Pilha> pilhas) {
        this.pilhas = pilhas;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

   
    
    @Transient
    public EnumTipoBaliza getTipoBaliza() {
        return metaBaliza.getTipoBaliza();
    }

    
    @Override
    public String toString() {
        return metaBaliza.toString();
    }

    

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((estado == null) ? 0 : estado.hashCode());
        result = prime * result + ((horarioFimFormacao == null) ? 0 : horarioFimFormacao.hashCode());
        result = prime * result + ((horarioInicioFormacao == null) ? 0 : horarioInicioFormacao.hashCode());
        result = prime * result + ((idBaliza == null) ? 0 : idBaliza.hashCode());
        result = prime * result + ((metaBaliza == null) ? 0 : metaBaliza.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Baliza other = (Baliza) obj;
        if (estado == null) {
            if (other.estado != null)
                return false;
        } else if (!estado.equals(other.estado))
            return false;
        if (horarioFimFormacao == null) {
            if (other.horarioFimFormacao != null)
                return false;
        } else if (!horarioFimFormacao.equals(other.horarioFimFormacao))
            return false;
        if (horarioInicioFormacao == null) {
            if (other.horarioInicioFormacao != null)
                return false;
        } else if (!horarioInicioFormacao.equals(other.horarioInicioFormacao))
            return false;
        if (idBaliza == null) {
            if (other.idBaliza != null)
                return false;
        } else if (!idBaliza.equals(other.idBaliza))
            return false;
        if (metaBaliza == null) {
            if (other.metaBaliza != null)
                return false;
        } else if (!metaBaliza.equals(other.metaBaliza))
            return false;
        return true;
    }

  

    /**
        * transiente utilizado apenas na realizacao de atividades na criacao do plano
        * virtual, nao deve ser usado em mais nenhum momento no sistema, pois pode repercurtir
        * em problemas em outras partes do sistema.
        */
    private Boolean utilizada;

    @Transient
    public Boolean isUtilizada() {
        return utilizada;
    }

    public void setUtilizada(Boolean utilizada) {
        this.utilizada = utilizada;
    }

    protected void addPilha(Pilha pilha) {
        incluirNovoStatus(pilha, pilha.getDtInicio());
      //  pilha.a
    }

    
        
    
    @Override
    public Pilha clonarStatus(Date horario) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Transient
    public void incluirNovoStatus(Pilha novoStatus, Date horaStatus) {
        try {
            boolean sobrescreveuStatusExistente = false;
            
            // encontra o indice correspondente ao novo status
            int indice = getIndiceIntervaloCorrespondente(horaStatus);

            
            // verifica se é um status que deve sobrescrever um status ja existente
            if (indice < getListaStatus().size())
            {
                if (getListaHoraInicioStatus().get(indice) == horaStatus.getTime())
                {
                    getListaStatus().set(indice, novoStatus);
                    sobrescreveuStatusExistente = true;
                }
            }
            
            if (!sobrescreveuStatusExistente)
            {
                // inclui o novo status na lista
                novoStatus.setDtInicio(horaStatus);
                getListaStatus().add(indice, novoStatus);
                if (indice > 0) {
                    Pilha statusAnterior = getListaStatus().get(indice - 1);
                    statusAnterior.setDtFim(horaStatus);
                }
//                getListaHoraInicioStatus().add(indice, horaStatus.getTime());
            }
            this.ordernar();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    @Transient
    public void removeStatusAPartirDeHorario(Date horario) {
        // TODO Auto-generated method stub
        // pega o indice correspondente ao horario
        try {

            int indice = getIndiceIntervaloCorrespondente(horario);

            // remove todos os elementos a partir do indice atual do status
            int sizeLista = getListaHoraInicioStatus().size();
            for (int i = indice; i < sizeLista; i++) {
                getListaHoraInicioStatus().remove(getListaHoraInicioStatus().size() - 1);
                getListaStatus().remove(getListaStatus().size() - 1);
            }
            this.ordernar();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Transient
    @Override
    public Pilha retornaStatusHorario(Date horario) {
        try {
            this.ordernar();
            // TODO Auto-generated method stub
            // pega o indice correspondente ao horario
            if (getListaHoraInicioStatus().size() == 0) {
                return null;
            }

            int indice = getIndiceIntervaloCorrespondente(horario);

            
            
            
            // se for o primeiro indice, verifica se o objeto ja tinha sido criado neste horario
            if (indice == 0 && getListaHoraInicioStatus().get(indice) > horario.getTime())
                return getListaStatus().get(indice);

            // se for o ultimo indice, entao retorna o indice anterior
            if (indice == getListaHoraInicioStatus().size())
                return getListaStatus().get(indice - 1);

            // se o horario encontrado corresponde exatamente ao horario passado como parametro entao devolve o status deste horario...
            if (getListaHoraInicioStatus().get(indice) == horario.getTime())
                return getListaStatus().get(indice);
            // ...senao retorna o status anterior ao indice encontrado
            else
                return getListaStatus().get(indice - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** 
     * Encontra o indice correspondente ao intervalo passado como parametro
     */
    @Transient
    private int getIndiceIntervaloCorrespondente(Date horario) {
        int indice = Collections.binarySearch(getListaHoraInicioStatus(), horario.getTime());

        if (indice >= 0)
            return indice;
        else
            return Math.abs(indice + 1);
    }
    
    @Transient
    public List<Pilha> getListaStatus() {        
        return getPilhas();
    }
    
    @Transient
    public List<Long> getListaHoraInicioStatus() {
            listaHoraInicioStatus = new ArrayList<Long>();
            
            this.ordernar();
            for (Pilha t : getListaStatus()) {
                listaHoraInicioStatus.add(t.getDtInicio().getTime());
            }

        return listaHoraInicioStatus;
    }

    public void setListaHoraInicioStatus(List<Long> listaHoraInicioStatus) {
        this.listaHoraInicioStatus = listaHoraInicioStatus;
    }

    @Override
    public Pilha copiarStatus(Pilha oldValue) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void ordernar() {
        // TODO Auto-generated method stub
        Collections.sort(this.getListaStatus(), comparadorStatus);
    }

	@Override
	public Boolean removerStatus(Pilha status) {
		// TODO Auto-generated method stub
		return Boolean.FALSE;
	}

    @Column(nullable = true)
	public Long getRetirandoMaterial() {
		return retirandoMaterial;
	}

	public void setRetirandoMaterial(Long retirandoMaterial) {
		this.retirandoMaterial = retirandoMaterial;
	}

	  
}
