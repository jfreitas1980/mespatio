/**
 * Uma quantidade de {@link Produto} de certa {@link Qualidade} a ser colocada em um {@link Navio}.
 *
 * @author andre
 */
package com.hdntec.gestao.domain.navios.entity.status;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.ForeignKey;

import com.hdntec.gestao.domain.StatusEntity;
import com.hdntec.gestao.domain.navios.entity.MetaCarga;
import com.hdntec.gestao.domain.produto.entity.Produto;
import com.hdntec.gestao.domain.produto.entity.Qualidade;


@Entity
public class Carga extends StatusEntity<Carga> {

	/** Serialização do objeto */
	private static final long serialVersionUID = 3421953802276463273L;

	/** identificador de carga */
	private Long idCarga;
	
	/** orientacao de embarque dessa carga */
	private OrientacaoDeEmbarque orientacaoDeEmbarque;

	/** o produto desta carga */
	private Produto produto;

	/** flag de que informa se a carga foi editada */
	private Boolean cargaEditada;

	/** Data Inicio do embarque */
	private Date dtInicioEmbarque;

	/** Data Final de embarque */
	private Date dtFimEmbarque;

	private MetaCarga metaCarga;

	/**
	 * Construtor Padrao.
	 */
	public Carga() {
	}

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "carga_seq")
	@SequenceGenerator(name = "carga_seq", sequenceName = "seqcarga")
	public Long getIdCarga() {
		return idCarga;
	}

	@Transient
	public String getIdentificadorCarga() {
		return getMetaCarga().getIdentificadorCarga();
	}



    @OneToOne
    @Cascade(value = {  CascadeType.MERGE,CascadeType.PERSIST, CascadeType.SAVE_UPDATE })
    @ForeignKey(name = "fk_carga_orientacao")
	@JoinColumn(name = "id_OrientacaoEmbarque")
    public OrientacaoDeEmbarque getOrientacaoDeEmbarque() {
		return orientacaoDeEmbarque;
	}

	@OneToOne
	//@Cascade(CascadeType.ALL)
	@ForeignKey(name = "fk_carga_produto")
	@JoinColumn(name = "id_Produto")
	public Produto getProduto() {
		return produto;
	}

	@Transient
	public Navio getNavio(Date time) {
		return getMetaCarga().getMetaNavio().retornaStatusHorario(time);
	}
    
	@Transient
	public Cliente getCliente(Date time) {
		return getMetaCarga().getMetaNavio().retornaStatusHorario(time).getCliente();
	}

	
	
	public void setIdCarga(Long idCarga) {
		this.idCarga = idCarga;
	}

	
	public void setOrientacaoDeEmbarque(
			OrientacaoDeEmbarque orientacaoDeEmbarque) {
		this.orientacaoDeEmbarque = orientacaoDeEmbarque;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	
	
	@Transient
	public Boolean getCargaEditada() {
		return cargaEditada;
	}

	public void setCargaEditada(Boolean cargaEditada) {
		this.cargaEditada = cargaEditada;
	}



	@Column(nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDtFimEmbarque() {
		return dtFimEmbarque;
	}

	public void setDtFimEmbarque(Date dtFimEmbarque) {
		this.dtFimEmbarque = dtFimEmbarque;
	}

	@Column(nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDtInicioEmbarque() {
		return dtInicioEmbarque;
	}

	public void setDtInicioEmbarque(Date dtInicioEmbarque) {
		this.dtInicioEmbarque = dtInicioEmbarque;
	}

	@ManyToOne
	@JoinColumn(name = "ID_META_CARGA", nullable = false)
	@ForeignKey(name = "fk_mt_carg")
	public MetaCarga getMetaCarga() {
		return metaCarga;
	}

	public void setMetaCarga(MetaCarga metaCarga) {
		this.metaCarga = metaCarga;
	}
   @Transient
	public String getCaminhoCompletoPlanilha() {
		// TODO Auto-generated method stub
		return getMetaCarga().getCaminhoCompletoPlanilha();
	}
  
}
