package com.hdntec.gestao.domain.planta.entity.status;

import java.util.Date;

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
import com.hdntec.gestao.domain.navios.entity.status.Navio;
import com.hdntec.gestao.domain.planta.entity.MetaBerco;
import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;

/**
 * Estrutura para atracação dos navios.
 * 
 * @author andre
 * 
 */
@Entity
public class Berco extends StatusEntity<Berco>
{

   @Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((idBerco == null) ? 0 : idBerco.hashCode());
		result = prime * result
				+ ((metaBerco == null) ? 0 : metaBerco.hashCode());
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
		Berco other = (Berco) obj;
		if (idBerco == null) {
			if (other.idBerco != null)
				return false;
		} else if (!idBerco.equals(other.idBerco))
			return false;
		if (metaBerco == null) {
			if (other.metaBerco != null)
				return false;
		} else if (!metaBerco.equals(other.metaBerco))
			return false;
		return true;
	}

/** Serialização do Objeto */
   private static final long serialVersionUID = -4444922878038065152L;

   /** Código do Berço */
   private Long idBerco;

   /** estado de operação do berco */
   private EstadoMaquinaEnum estado;

   /** o Navio que esta atracado no berco */
   private Navio navioAtracado;

   private MetaBerco metaBerco;
   /**
    * Construtor Padrao.
    */
   public Berco()
   {
   }

   @ManyToOne
	@JoinColumn(name = "ID_META_BER", nullable = false, insertable = true)
	@ForeignKey(name = "fk_meta_berco")
   public MetaBerco getMetaBerco() {
	return metaBerco;
}


public void setMetaBerco(MetaBerco metaBerco) {
	this.metaBerco = metaBerco;
}


@Id
   @GeneratedValue(strategy = GenerationType.AUTO, generator = "berco_seq")
   @SequenceGenerator(name = "berco_seq", sequenceName = "seqberco")
   public Long getIdBerco()
   {
      return idBerco;
   }

   public void setIdBerco(Long idBerco)
   {
      this.idBerco = idBerco;
   }

   @Transient
   public String getNomeBerco()
   {
      return getMetaBerco().getNomeBerco();
   }

  
  

   @Transient
   public Double getComprimentoMaximo()
   {
      return getMetaBerco().getComprimentoMaximo();
   }

   @Transient
   public Double getBocaMaxima()
   {
      return getMetaBerco().getBocaMaxima();
   }
  

   @Transient
   public Double getCaladoMaximo()
   {
      return getMetaBerco().getCaladoMaximo();
   }


   @OneToOne
   @Cascade(CascadeType.SAVE_UPDATE)
   @ForeignKey(name = "fk_berco_navio")
   @JoinColumn(name = "id_Navio")
   public Navio getNavioAtracado()
   {
      return navioAtracado;
   }

   public void setNavioAtracado(Navio navioAtracado)
   {
      this.navioAtracado = navioAtracado;
   }

   @Transient
   public String getIdentificadorBerco()
   {
      return  getMetaBerco().getIdentificadorBerco();
   }

 
   @Transient
   public String getTagPims()
   {
      return getMetaBerco().getTagPims();
   }

     /**
    * Verifica se o berco em que o navio esta setado para atracar esta disponivel
    * @param navio
    * @return
    */
   public Boolean verificaBercoDisponivel(Date data) {
       boolean status = Boolean.FALSE;
       if (getNavioAtracado() == null) {
           status = Boolean.TRUE;
       }
       return status;
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
