/**
 * Os navios são os equipamentos que são embarcados com as diversas cargas de produtos para um determinado cliente.
 *
 * @author andre
 */

package com.hdntec.gestao.domain.navios.entity.status;

import java.util.ArrayList;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.ForeignKey;

import com.hdntec.gestao.domain.StatusEntity;
import com.hdntec.gestao.domain.navios.StatusNavioEnum;
import com.hdntec.gestao.domain.navios.entity.MetaCarga;
import com.hdntec.gestao.domain.navios.entity.MetaNavio;
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.planta.entity.status.Berco;


@Entity
public class Navio extends StatusEntity<Navio> {

	private Atividade atividade;
	
	/** Serialização do objeto */
    private static final long serialVersionUID = 1413018878520691935L;

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((atividade == null) ? 0 : atividade.hashCode());
		result = prime * result + ((idNavio == null) ? 0 : idNavio.hashCode());
		result = prime
				* result
				+ ((identificadorBerco == null) ? 0 : identificadorBerco
						.hashCode());
		result = prime * result
				+ ((metaNavio == null) ? 0 : metaNavio.hashCode());
		result = prime * result
				+ ((nomeNavio == null) ? 0 : nomeNavio.hashCode());
		result = prime * result
				+ ((statusEmbarque == null) ? 0 : statusEmbarque.hashCode());
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
		Navio other = (Navio) obj;
		if (atividade == null) {
			if (other.atividade != null)
				return false;
		} else if (!atividade.equals(other.atividade))
			return false;
		if (idNavio == null) {
			if (other.idNavio != null)
				return false;
		} else if (!idNavio.equals(other.idNavio))
			return false;
		if (identificadorBerco == null) {
			if (other.identificadorBerco != null)
				return false;
		} else if (!identificadorBerco.equals(other.identificadorBerco))
			return false;
		if (metaNavio == null) {
			if (other.metaNavio != null)
				return false;
		} else if (!metaNavio.equals(other.metaNavio))
			return false;
		if (nomeNavio == null) {
			if (other.nomeNavio != null)
				return false;
		} else if (!nomeNavio.equals(other.nomeNavio))
			return false;
		if (statusEmbarque != other.statusEmbarque)
			return false;
		return true;
	}

	/** identificador de navio */
    private Long idNavio;

    /** o nome do navio */
    private String nomeNavio;

    /** o deadweight tonnage do navio, ou quanto ele carrega de carga */
    private Double dwt;

    /** estimated time of arrival do navio */
    private Date eta;

    /** O período de laydays, que são os dias que o navio tem reservado para o
     * embarque, é separado em chegada e saidas */
    private Date diaDeChegada; // laydays

    /** O período de laydays, que são os dias que o navio tem reservado para
     * o embarque, é separado em chegada e saidas */
    private Date diaDeSaida; // laydays

    /** Data de embarque do navio */
    private Date dataEmbarque;

    /** o status do embarque do navio */
    private StatusNavioEnum statusEmbarque;

    /** o berço de atracação deste navio */
    private Berco bercoDeAtracacao;

    /** o cliente deste navio */
    private Cliente cliente;

    /** flag que identifica o berco onde ira atracar o navio */
    private String identificadorBerco;

    /** a data de chegada no navio na barra */
    private Date dataChegadaBarra;

    /** a data de atracacao do Navio */
    private Date dataAtracacao;

    /** a data de desatracacao do Navio */
    private Date dataDesatracacao;

    private MetaNavio metaNavio;

    public Navio() {
    }

    /**
     * @return the idNavio
     */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "navio_seq")
    @SequenceGenerator(name = "navio_seq", sequenceName = "seqnavio")
    public Long getIdNavio() {
        return idNavio;
    }

    @Column(length = 60, nullable = false)
    public String getNomeNavio() {
        return nomeNavio;
    }

    @Column(length = 10, precision = 2, nullable = false)
    public Double getDwt() {
        return dwt;
    }

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getEta() {
        return eta;
    }

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDiaDeChegada() {
        return diaDeChegada;
    }

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDiaDeSaida() {
        return diaDeSaida;
    }

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDataEmbarque() {
        return dataEmbarque;
    }

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    public StatusNavioEnum getStatusEmbarque() {
        return statusEmbarque;
    }

    @OneToOne//(targetEntity=Berco.class,mappedBy="navioAtracado")
    @ForeignKey(name = "fk_navio_berco")
    @JoinColumn(name = "id_Berco")
    public Berco getBercoDeAtracacao() {
        return bercoDeAtracacao;
    }

    @Transient
    public List<Carga> getListaDeCargasDoNavio(Date hora) {
        List<Carga> result = new ArrayList<Carga>();
        
        // verifica se o navio ja tem uma lista de cargas (quando o navio é criado ele não tem cargas)
        if (getMetaNavio().getListaMetaCargas() == null)
        	return result;
        
        for (MetaCarga metaCarga : getMetaNavio().getListaMetaCargas()) {
            result.add(metaCarga.retornaStatusHorario(hora));
        }
        return result;
    }
    
    public void incluiNovaCarga(Carga novaCarga, Date hora)
    {
		// para inserir a nova carga primeiro apaga os status da meta carga a partir deste horario (supondo que a edição ocorre sempre na ultima situaçao)
		novaCarga.getMetaCarga().removeStatusAPartirDeHorario(hora);
		novaCarga.getMetaCarga().incluirNovoStatus(novaCarga, hora);
    }

    @OneToOne
    @ForeignKey(name = "fk_navio_cliente")
    @JoinColumn(name = "id_Cliente", nullable = false)
    public Cliente getCliente() {
        return cliente;
    }

    @Transient
    public String getSap_vbap_vbeln() {
        return metaNavio.getSap_vbap_vbeln();
    }

    @Column
    public String getIdentificadorBerco() {
        return identificadorBerco;
    }

    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDataAtracacao() {
        return dataAtracacao;
    }

    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDataChegadaBarra() {
        return dataChegadaBarra;
    }

    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDataDesatracacao() {
        return dataDesatracacao;
    }

    public void setIdentificadorBerco(String identificadorBerco) {
        this.identificadorBerco = identificadorBerco;
    }

    public void setSap_vbap_vbeln(String sap_vbap_vbeln) {
        metaNavio.setSap_vbap_vbeln(sap_vbap_vbeln);
    }

    public void setBercoDeAtracacao(Berco bercoDeAtracacao) {
        this.bercoDeAtracacao = bercoDeAtracacao;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setDataEmbarque(Date dataEmbarque) {
        this.dataEmbarque = dataEmbarque;
    }

    public void setDiaDeChegada(Date diaDeChegada) {
        this.diaDeChegada = diaDeChegada;
    }

    public void setDiaDeSaida(Date diaDeSaida) {
        this.diaDeSaida = diaDeSaida;
    }

    public void setDwt(Double dwt) {
        this.dwt = dwt;
    }

    public void setEta(Date eta) {
        this.eta = eta;
    }

    public void setIdNavio(Long idNavio) {
        this.idNavio = idNavio;
    }

    public void setNomeNavio(String nomeNavio) {
        this.nomeNavio = nomeNavio;
    }

    public void setStatusEmbarque(StatusNavioEnum statusEmbarque) {
        this.statusEmbarque = statusEmbarque;
    }

    public void setDataAtracacao(Date dataAtracacao) {
        this.dataAtracacao = dataAtracacao;
    }

    public void setDataChegadaBarra(Date dataChegadaBarra) {
        this.dataChegadaBarra = dataChegadaBarra;
    }

    public void setDataDesatracacao(Date dataDesatracacao) {
        this.dataDesatracacao = dataDesatracacao;
    }

    @Override
    public String toString() {
        return this.getNomeNavio();
    }

    public void setMetaNavio(MetaNavio metaNavio) {
        this.metaNavio = metaNavio;
    }

    @ManyToOne
    @JoinColumn(name = "ID_META_NAVIO", nullable = false, insertable = true)
    @ForeignKey(name = "fk_meta_navio")
    public MetaNavio getMetaNavio() {
        return metaNavio;
    }

    /**
    * naFila
    * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
    * @since 24/06/2010
    * @see
    * @param 
    * @return Boolean
    */
    @Transient
    public Boolean naFila() {
        Boolean result = Boolean.FALSE;
        if (this.getEta() != null && !this.getStatusEmbarque().equals(StatusNavioEnum.ATRACADO)
                        && !this.getStatusEmbarque().equals(StatusNavioEnum.EMBARCADO) && !this.getStatusEmbarque().equals(StatusNavioEnum.EXCLUIDO)) {
            result = Boolean.TRUE;
        }
        return result;
    }

    @OneToOne
    @ForeignKey(name = "fk_navio_atividade")
    @JoinColumn(name = "idAtividade", nullable = true)
    public Atividade getAtividade()
    {
       return atividade;
    }
    
    public void setAtividade(Atividade atividade) {
    	this.atividade = atividade;
    }


}
