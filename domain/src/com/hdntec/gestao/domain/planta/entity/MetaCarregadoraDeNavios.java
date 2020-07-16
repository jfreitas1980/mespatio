package com.hdntec.gestao.domain.planta.entity;

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
import com.hdntec.gestao.domain.planta.entity.status.CarregadoraDeNavios;


/**
 * Máquina utilizada para carregar os navios. Fica instalada no pier para
 * servir os navios que existem nos berços.
 * <p>
 * 
 */
@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"ID_META_PIER","nomeCarregadeiraDeNavios"}))
public class MetaCarregadoraDeNavios extends AbstractMetaEntity<CarregadoraDeNavios>
{

   /** Serialização do Objeto */
   private static final long serialVersionUID = 2718915420664606114L;

   private Long idCarregadeiraDeNavios;

   /** Nome da Carregadeira de Navios */
   private String nomeCarregadeiraDeNavios;

   private MetaPier metaPier;

   @ManyToOne
   @JoinColumn(name = "ID_META_PIER", nullable = false, insertable = true)
   @ForeignKey(name = "fk_meta_pier_car")	

   public MetaPier getMetaPier() {
	return metaPier;
}


public void setMetaPier(MetaPier metaPier) {
	this.metaPier = metaPier;
}


/**
    * Construtor Padrao.
    */
   public MetaCarregadoraDeNavios()
   {
   }


   @Id
   @GeneratedValue(strategy = GenerationType.AUTO, generator = "meta_carnav_seq")
   @SequenceGenerator(name = "meta_carnav_seq", sequenceName = "seq_meta_carnav")
   public Long getIdCarregadeiraDeNavios()
   {
      return idCarregadeiraDeNavios;
   }

   
   
   public void setIdCarregadeiraDeNavios(Long idCarregadeiraDeNavios)
   {
      this.idCarregadeiraDeNavios = idCarregadeiraDeNavios;
   }

   @Column(nullable = false, length = 60)
   public String getNomeCarregadeiraDeNavios()
   {
      return nomeCarregadeiraDeNavios;
   }

   public void setNomeCarregadeiraDeNavios(String nomeCarregadeiraDeNavios)
   {
      this.nomeCarregadeiraDeNavios = nomeCarregadeiraDeNavios;
   }

   @Override
   @OneToMany(fetch = FetchType.LAZY,mappedBy="metaCarregadora")
   @Fetch(FetchMode.SELECT)
   //@Cascade(value={CascadeType.ALL,CascadeType.SAVE_UPDATE})
   public List<CarregadoraDeNavios> getListaStatus() {
	return super.getListaStatus();
   }	

   @Override
   @Transient
	public void incluirNovoStatus(CarregadoraDeNavios novoStatus, Date horaStatus) {
	  super.incluirNovoStatus(novoStatus, horaStatus);
	  novoStatus.setMetaCarregadora(this);  
   }


@Override
public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime
			* result
			+ ((idCarregadeiraDeNavios == null) ? 0 : idCarregadeiraDeNavios
					.hashCode());
	result = prime * result + ((metaPier == null) ? 0 : metaPier.hashCode());
	result = prime
			* result
			+ ((nomeCarregadeiraDeNavios == null) ? 0
					: nomeCarregadeiraDeNavios.hashCode());
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
	MetaCarregadoraDeNavios other = (MetaCarregadoraDeNavios) obj;
	if (idCarregadeiraDeNavios == null) {
		if (other.idCarregadeiraDeNavios != null)
			return false;
	} else if (!idCarregadeiraDeNavios.equals(other.idCarregadeiraDeNavios))
		return false;
	if (metaPier == null) {
		if (other.metaPier != null)
			return false;
	} else if (!metaPier.equals(other.metaPier))
		return false;
	if (nomeCarregadeiraDeNavios == null) {
		if (other.nomeCarregadeiraDeNavios != null)
			return false;
	} else if (!nomeCarregadeiraDeNavios.equals(other.nomeCarregadeiraDeNavios))
		return false;
	return true;
}
	
}
