
package com.hdntec.gestao.domain.produto.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.hdntec.gestao.domain.AuditTrail;
import com.hdntec.gestao.domain.planta.entity.status.Campanha;
import com.hdntec.gestao.domain.produto.enums.TipoItemControleEnum;
import com.hdntec.gestao.domain.produto.enums.TipoMetaInternaEnum;


/**
 * Define os tipos de {@link ItemDeControle} que sÃ£o analisados para
 * estabelecer a {@link Qualidade}
 * 
 * @author andre
 * 
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TipoItemDeControle extends AuditTrail {

	/** serial gerado automaticamente */
	private static final long serialVersionUID = -4665649287107778404L;

	/** o tipo desta atividade */
	private TipoItemControleEnum tipoItemControle;

	/** identificador de tipo de item de controle */
	private Long idTipoItemDeControle;

	/** descricao do tipo de produto */
	private String descricaoTipoItemControle;

	/** Unidade de medida da variacao do tipo de item de controle */
	private String unidade;

	/** O valor inicial da escala */
	private Double inicioEscala;

	/** O valor final da escala */
	private Double fimEscala;

	/** A multiplicidade em que a escala ira aparecer na barra numerica */
	private Double multiplicidadeEscala;
	
	/**
	 * tipo de processo do item de controle (HH - Bi-horaria, DD - diaria) -
	 * para pellet feed
	 */
	private String tipoProcessoPelletFeed;

	/**
	 * tipo de processo do item de controle (HH - Bi-horaria, DD - diaria) -
	 * para pelota
	 */
	private String tipoProcessoPelota;

	/** Codigo da area resp ed (para pellet feed) */
	private String areaRespEDPelletFeed;

	/** Codigo da area resp ed (para pelota) */
	private String areaRespEDPelota;

	/** Codigo do item de controle usado pelo cliente (para pellet feed) */
	private Long cdTipoItemControlePelletFeed;

	/** Codigo do item de controle usado pelo cliente (para pelota) */
	private Long cdTipoItemControlePelota;

	/**
	 * A lista de metas internas relacionada aos diferentes tipo de produto de
	 * pelota
	 */
	private List<MetaInterna> listaDeMetasInternas;

	/** mostra se este tipo de item eh relevante */
	private Boolean relevante;

	/** A lista de regras do tipo de item de controle por produto */
    private List<TipoItemCoeficiente> listaTipoItemDeControleCoeficiente;

    /** A lista de regras do tipo de item de controle por produto */
    private List<TipoItemRegraFarol> listaTipoItemRegraFarol;

    
	public TipoItemDeControle() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "tipoitemdecontrole_seq")
	@SequenceGenerator(name = "tipoitemdecontrole_seq", sequenceName = "seqtipoitemdecontrole")
	public Long getIdTipoItemDeControle() {
		return idTipoItemDeControle;
	}

	@Column(nullable = false, length = 60)
	public String getDescricaoTipoItemControle() {
		return descricaoTipoItemControle;
	}

	@Column(nullable = false, length = 60)
	public String getUnidade() {
		return unidade;
	}

	@Column(nullable = false)
	public Double getInicioEscala() {
		return inicioEscala;
	}

	@Column(nullable = false)
	public Double getFimEscala() {
		return fimEscala;
	}

	@Column(nullable = false)
	public Double getMultiplicidadeEscala() {
		return multiplicidadeEscala;
	}

	@Column(nullable = true)
	public String getAreaRespEDPelletFeed() {
		return areaRespEDPelletFeed;
	}

	@Column(nullable = true)
	public String getTipoProcessoPelletFeed() {
		return tipoProcessoPelletFeed;
	}

	@Column(nullable = true)
	public Long getCdTipoItemControlePelletFeed() {
		return cdTipoItemControlePelletFeed;
	}

	public void setAreaRespEDPelletFeed(String areaRespED) {
		this.areaRespEDPelletFeed = areaRespED;
	}

	@Column(nullable = true)
	public String getAreaRespEDPelota() {
		return areaRespEDPelota;
	}

	@Column(nullable = true)
	public String getTipoProcessoPelota() {
		return tipoProcessoPelota;
	}

	@Column(nullable = true)
	public Long getCdTipoItemControlePelota() {
		return cdTipoItemControlePelota;
	}

	public void setAreaRespEDPelota(String areaRespED) {
		this.areaRespEDPelota = areaRespED;
	}

	public void setDescricaoTipoItemControle(String descricaoTipoItemControle) {
		this.descricaoTipoItemControle = descricaoTipoItemControle;
	}

	public void setFimEscala(Double fimEscala) {
		this.fimEscala = fimEscala;
	}

	public void setIdTipoItemDeControle(Long idTipoItemDeControle) {
		this.idTipoItemDeControle = idTipoItemDeControle;
	}

	public void setInicioEscala(Double inicioEscala) {
		this.inicioEscala = inicioEscala;
	}

	public void setMultiplicidadeEscala(Double multiplicidadeEscala) {
		this.multiplicidadeEscala = multiplicidadeEscala;
	}

	public void setTipoProcessoPelletFeed(String tipoProcesso) {
		this.tipoProcessoPelletFeed = tipoProcesso;
	}

	public void setTipoProcessoPelota(String tipoProcesso) {
		this.tipoProcessoPelota = tipoProcesso;
	}

	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}

	public void setCdTipoItemControlePelletFeed(Long cdTipoItemControle) {
		this.cdTipoItemControlePelletFeed = cdTipoItemControle;
	}

	public void setCdTipoItemControlePelota(Long cdTipoItemControle) {
		this.cdTipoItemControlePelota = cdTipoItemControle;
	}

	@Override
	public String toString() {
		return this.getDescricaoTipoItemControle();
	}

	/**
	 * @return the listaDeMetasInternas
	 */
	// TODO Darley SAxxxx Trocando EAGER por LAZY
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoItemDeControle")
	@Fetch(FetchMode.SELECT)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@Cascade(value = { CascadeType.ALL })
	public List<MetaInterna> getListaDeMetasInternas() {
	    if (listaDeMetasInternas == null)
	        listaDeMetasInternas = new ArrayList<MetaInterna>();
	    return listaDeMetasInternas;
	}

	/**
	 * @param listaDeMetasInternas
	 *            the listaDeMetasInternas to set
	 */
	public void setListaDeMetasInternas(List<MetaInterna> listaDeMetasInternas) {
		this.listaDeMetasInternas = listaDeMetasInternas;
	}

	@Column(nullable = false)
	public Boolean getRelevante() {
		return relevante;
	}

	public void setRelevante(Boolean relevante) {
		this.relevante = relevante;
	}
	
	public void addMetaInterna(MetaInterna meta) {

		if (getListaDeMetasInternas() == null) {
			setListaDeMetasInternas(new ArrayList<MetaInterna>());
		}
		if (!getListaDeMetasInternas().contains(meta)) {
			getListaDeMetasInternas().add(meta);

		}
	}


	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	public TipoItemControleEnum getTipoItemControle() {
		return tipoItemControle;
	}

	public void setTipoItemControle(TipoItemControleEnum tipoItemControle) {
		this.tipoItemControle = tipoItemControle;
	}

	
	
    
    /**
     * addTipoItemCoeficiente
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 19/08/2010
     * @see
     * @param 
     * @return void
     */
    public void addTipoItemCoeficiente(TipoItemCoeficiente addItem) {
        
        if (!getListaTipoItemDeControleCoeficiente().contains(addItem)) {                        
            addItem.setTipoItem(this);
            getListaTipoItemDeControleCoeficiente().add(addItem);
        }    
    }


    
    public void addTipoItemRegraFarol(TipoItemRegraFarol addItem) {
        
        if (!getListaTipoItemRegraFarol().contains(addItem)) {                        
            addItem.setTipoItem(this);
            getListaTipoItemRegraFarol().add(addItem);
        }    
    }

    
    @OneToMany(fetch = FetchType.LAZY ,mappedBy="tipoItem")    
    @Cascade(value ={CascadeType.ALL})    
    @OrderBy("inicio asc")
    public List<TipoItemCoeficiente> getListaTipoItemDeControleCoeficiente() {
        if (listaTipoItemDeControleCoeficiente == null) 
            listaTipoItemDeControleCoeficiente = new ArrayList<TipoItemCoeficiente>();
        return listaTipoItemDeControleCoeficiente;
    }

    public void setListaTipoItemDeControleCoeficiente(List<TipoItemCoeficiente> listaTipoItemDeControleCoeficiente) {
        this.listaTipoItemDeControleCoeficiente = listaTipoItemDeControleCoeficiente;
    }

    @OneToMany(fetch = FetchType.LAZY ,mappedBy="tipoItem")    
    @Cascade(value ={CascadeType.ALL})    
    @OrderBy("inicio asc")
    public List<TipoItemRegraFarol> getListaTipoItemRegraFarol() {
        if (listaTipoItemRegraFarol == null)
            listaTipoItemRegraFarol = new ArrayList<TipoItemRegraFarol>();
        return listaTipoItemRegraFarol;
    }

    public void setListaTipoItemRegraFarol(List<TipoItemRegraFarol> listaTipoItemRegraFarol) {
        this.listaTipoItemRegraFarol = listaTipoItemRegraFarol;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((areaRespEDPelletFeed == null) ? 0 : areaRespEDPelletFeed.hashCode());
        result = prime * result + ((areaRespEDPelota == null) ? 0 : areaRespEDPelota.hashCode());
        result = prime * result + ((cdTipoItemControlePelletFeed == null) ? 0 : cdTipoItemControlePelletFeed.hashCode());
        result = prime * result + ((cdTipoItemControlePelota == null) ? 0 : cdTipoItemControlePelota.hashCode());
        result = prime * result + ((descricaoTipoItemControle == null) ? 0 : descricaoTipoItemControle.hashCode());
        result = prime * result + ((fimEscala == null) ? 0 : fimEscala.hashCode());
        result = prime * result + ((idTipoItemDeControle == null) ? 0 : idTipoItemDeControle.hashCode());
        result = prime * result + ((inicioEscala == null) ? 0 : inicioEscala.hashCode());
        result = prime * result + ((multiplicidadeEscala == null) ? 0 : multiplicidadeEscala.hashCode());
        result = prime * result + ((relevante == null) ? 0 : relevante.hashCode());
        result = prime * result + ((tipoItemControle == null) ? 0 : tipoItemControle.hashCode());
        result = prime * result + ((tipoProcessoPelletFeed == null) ? 0 : tipoProcessoPelletFeed.hashCode());
        result = prime * result + ((tipoProcessoPelota == null) ? 0 : tipoProcessoPelota.hashCode());
        result = prime * result + ((unidade == null) ? 0 : unidade.hashCode());
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
        TipoItemDeControle other = (TipoItemDeControle) obj;
        if (areaRespEDPelletFeed == null) {
            if (other.areaRespEDPelletFeed != null)
                return false;
        } else if (!areaRespEDPelletFeed.equals(other.areaRespEDPelletFeed))
            return false;
        if (areaRespEDPelota == null) {
            if (other.areaRespEDPelota != null)
                return false;
        } else if (!areaRespEDPelota.equals(other.areaRespEDPelota))
            return false;
        if (cdTipoItemControlePelletFeed == null) {
            if (other.cdTipoItemControlePelletFeed != null)
                return false;
        } else if (!cdTipoItemControlePelletFeed.equals(other.cdTipoItemControlePelletFeed))
            return false;
        if (cdTipoItemControlePelota == null) {
            if (other.cdTipoItemControlePelota != null)
                return false;
        } else if (!cdTipoItemControlePelota.equals(other.cdTipoItemControlePelota))
            return false;
        if (descricaoTipoItemControle == null) {
            if (other.descricaoTipoItemControle != null)
                return false;
        } else if (!descricaoTipoItemControle.equals(other.descricaoTipoItemControle))
            return false;
        if (fimEscala == null) {
            if (other.fimEscala != null)
                return false;
        } else if (!fimEscala.equals(other.fimEscala))
            return false;
        if (idTipoItemDeControle == null) {
            if (other.idTipoItemDeControle != null)
                return false;
        } else if (!idTipoItemDeControle.equals(other.idTipoItemDeControle))
            return false;
        if (inicioEscala == null) {
            if (other.inicioEscala != null)
                return false;
        } else if (!inicioEscala.equals(other.inicioEscala))
            return false;
        if (multiplicidadeEscala == null) {
            if (other.multiplicidadeEscala != null)
                return false;
        } else if (!multiplicidadeEscala.equals(other.multiplicidadeEscala))
            return false;
        if (relevante == null) {
            if (other.relevante != null)
                return false;
        } else if (!relevante.equals(other.relevante))
            return false;
        if (tipoItemControle == null) {
            if (other.tipoItemControle != null)
                return false;
        } else if (!tipoItemControle.equals(other.tipoItemControle))
            return false;
        if (tipoProcessoPelletFeed == null) {
            if (other.tipoProcessoPelletFeed != null)
                return false;
        } else if (!tipoProcessoPelletFeed.equals(other.tipoProcessoPelletFeed))
            return false;
        if (tipoProcessoPelota == null) {
            if (other.tipoProcessoPelota != null)
                return false;
        } else if (!tipoProcessoPelota.equals(other.tipoProcessoPelota))
            return false;
        if (unidade == null) {
            if (other.unidade != null)
                return false;
        } else if (!unidade.equals(other.unidade))
            return false;
        return true;
    }

    
    @Transient
    public MetaInterna getMetaInterna(Date horaExecucao,TipoMetaInternaEnum tipoDaMetaInterna)
    {
        MetaInterna result = null;
       Collections.sort(getListaDeMetasInternas(),MetaInterna.comparadorStatusMeta);
       List<MetaInterna> campanhas =  getListaDeMetasInternas();
       
       for (MetaInterna campanha : campanhas ) {           
          /***
           *  TODO IMPLEMENTAR REGRA DE DATA
           * 
           */
           if (horaExecucao.getTime() >= campanha.getDtInicio().getTime() &&  horaExecucao.getTime() <= campanha.getDtFim().getTime() && campanha.getTipoDaMetaInterna().equals(tipoDaMetaInterna)) {
               result = campanha;
               break;
           }
       }       
       return result;
    }

    @Transient
    public TipoItemCoeficiente getTipoItemCoeficiente(Date horaExecucao)
    {
        TipoItemCoeficiente result = null;
       Collections.sort(getListaTipoItemDeControleCoeficiente(),TipoItemCoeficiente.comparadorStatusTipoItemCoeficiente);
       List<TipoItemCoeficiente> campanhas =  getListaTipoItemDeControleCoeficiente();
       
       for (TipoItemCoeficiente campanha : campanhas ) {           
          /***
           *  TODO IMPLEMENTAR REGRA DE DATA
           * 
           */
           if (horaExecucao.getTime() >= campanha.getDtInicio().getTime() &&  horaExecucao.getTime() <= campanha.getDtFim().getTime()) {
               result = campanha;
               break;
           }
       }       
       return result;
    }
    
    @Transient
    public TipoItemRegraFarol getTipoItemRegraFarol(Date horaExecucao)
    {
        TipoItemRegraFarol result = null;
       Collections.sort(getListaTipoItemRegraFarol(),TipoItemRegraFarol.comparadorStatusTipoItemRegraFarol);
       List<TipoItemRegraFarol> campanhas =  getListaTipoItemRegraFarol();
       
       for (TipoItemRegraFarol campanha : campanhas ) {           
          /***
           *  TODO IMPLEMENTAR REGRA DE DATA
           * 
           */
           if (horaExecucao.getTime() >= campanha.getDtInicio().getTime() &&  horaExecucao.getTime() <= campanha.getDtFim().getTime()) {
               result = campanha;
               break;
           }
       }       
       return result;
    }
    
    
}
