package com.hdntec.gestao.domain.planta.entity.status;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import org.hibernate.annotations.ForeignKey;

import com.hdntec.gestao.domain.StatusEntity;
import com.hdntec.gestao.domain.planta.entity.MetaBerco;
import com.hdntec.gestao.domain.planta.entity.MetaPier;
import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;

/**
 * Pier é uma estrutura de berços para aportamento. Nesse, localiza-se a carregadora de navios para a escoagem de produção.
 * <p>
 * Persite. - Tamanho: - Volume: poucos registros. -Período de Persistência: cadastro de planta. - Freq. Update: raro. -Confiabilidade: deve ser confiável.
 * 
 * @author andre
 * 
 */
@Entity
public class Pier extends StatusEntity<Pier>
{

   /** Serialização do Objeto */
   private static final long serialVersionUID = -2394239744959694237L;

   private Long idPier;

   /** estado de operação do pier */
   private EstadoMaquinaEnum estado;

   private MetaPier metaPier;
   
      /**
    * Construtor Padrao.
    */
   public Pier()
   {
   }


   @Id
   @GeneratedValue(strategy = GenerationType.AUTO, generator = "pier_seq")
   @SequenceGenerator(name = "pier_seq", sequenceName = "seqpier")
   public Long getIdPier()
   {
      return idPier;
   }

   public void setIdPier(Long idPier)
   {
      this.idPier = idPier;
   }

   @ManyToOne
   @JoinColumn(name = "ID_META_PIER", nullable = false, insertable = true)
   @ForeignKey(name = "fk_meta_pier_pier")	
   public MetaPier getMetaPier() {
	return metaPier;
}


public void setMetaPier(MetaPier metaPier) {
	this.metaPier = metaPier;
}


@Transient
   public String getNomePier()
   {
      return getMetaPier().getNomePier();
   }

   public void setNomePier(String nomePier)
   {
	   getMetaPier().setNomePier(nomePier);
   }

   @Column(nullable = false)
   @Enumerated(EnumType.STRING)
  

  @Transient
   public List<CarregadoraDeNavios> getListaDeCarregadoraDeNavios()
   {
      return null;
   }

   public void setListaDeCarregadoraDeNavios(
         List<CarregadoraDeNavios> listaDeCarregadoraDeNavios)
   {
      
   }

   @Transient
   public List<Berco> getListaDeBercosDeAtracacao(Date data)
   {
       List<Berco> result = new ArrayList<Berco>();
       for (MetaBerco berco : getMetaPier().getListaDeMetaBercosDeAtracacao()) {
           result.add(berco.retornaStatusHorario(data));
      }
       return result;
   }

   public void setListaDeBercosDeAtracacao(List<Berco> listaDeBercosDeAtracacao)
   {
    
   }

   

   
   public void addCarregadoraNavio(CarregadoraDeNavios carregadora)
   {
      if (getListaDeCarregadoraDeNavios() == null)
      {
         setListaDeCarregadoraDeNavios(new ArrayList<CarregadoraDeNavios>());
      }
      if (!getListaDeCarregadoraDeNavios().contains(carregadora))
      {
         getListaDeCarregadoraDeNavios().add(carregadora);
      }
   }

   public void addBerco(Berco berco)
   {
      if (getListaDeBercosDeAtracacao(berco.getDtInicio()) == null)
      {
         setListaDeBercosDeAtracacao(new ArrayList<Berco>());
      }
      if (!getListaDeBercosDeAtracacao(berco.getDtInicio()).contains(berco))
      {
         getListaDeBercosDeAtracacao(berco.getDtInicio()).add(berco);
      }
   }

  

   @Column(nullable = false)
   @Enumerated(EnumType.STRING)
public EstadoMaquinaEnum getEstado() {
    return estado;
}


public void setEstado(EstadoMaquinaEnum estado) {
    this.estado = estado;
}


@Override
public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((estado == null) ? 0 : estado.hashCode());
    result = prime * result + ((idPier == null) ? 0 : idPier.hashCode());
    result = prime * result + ((metaPier == null) ? 0 : metaPier.hashCode());
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
    Pier other = (Pier) obj;
    if (estado == null) {
        if (other.estado != null)
            return false;
    } else if (!estado.equals(other.estado))
        return false;
    if (idPier == null) {
        if (other.idPier != null)
            return false;
    } else if (!idPier.equals(other.idPier))
        return false;
    if (metaPier == null) {
        if (other.metaPier != null)
            return false;
    } else if (!metaPier.equals(other.metaPier))
        return false;
    return true;
}



   
}
