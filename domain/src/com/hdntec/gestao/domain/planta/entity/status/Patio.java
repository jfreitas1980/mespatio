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
import com.hdntec.gestao.domain.plano.enums.TipoMaquinaEnum;
import com.hdntec.gestao.domain.planta.dao.MetaMaquinaDoPatioDAO;
import com.hdntec.gestao.domain.planta.entity.MetaBaliza;
import com.hdntec.gestao.domain.planta.entity.MetaMaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.MetaPatio;
import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;

/**
 * É o espaço físico onde se estoca produtos para posterior embarque de
 * navios de clientes.
 * <p>
 * Essa estocagem se faz através das máquinas empilhadeiras, que empilham os
 * produtos no formato de pilhas.
 * <p>
 * As máquinas recuperadoras, posteriormente, são responsáveis por recuperar
 * essas pilhas, enviando o produto para embarque dos navios.
 * <p>
 * O pátio é marcado através de balizas
 * <p>
 * Persite. - Tamanho: - Volume: poucos registros. -Período de Persistência:
 * cadastro de planta. - Freq. Update: raro. -Confiabilidade: deve ser
 * confiável.
 * 
 * @author andre
 * 
 */
@Entity
public class Patio extends StatusEntity<Patio>
{

   /** Serialização do Objeto */
   private static final long serialVersionUID = 8468597089080890920L;

   
   private Long idPatio;
   
   private MetaPatio metaPatio;

   private EstadoMaquinaEnum estado;
   
   /**
    * Construtor Padrao
    */
   public Patio()
   {
   }


   @Id
   @GeneratedValue(strategy = GenerationType.AUTO, generator = "patio_seq")
   @SequenceGenerator(name = "patio_seq", sequenceName = "seqpatio")
   public Long getIdPatio()
   {
      return idPatio;
   }
   
   @ManyToOne
	@JoinColumn(name = "ID_META_PATIO", nullable = false, insertable = true)
	@ForeignKey(name = "fk_meta_pato")
   public MetaPatio getMetaPatio() {
	return metaPatio;
}


  public void setMetaPatio(MetaPatio metaPatio) {
	this.metaPatio = metaPatio;
   }


   public void setIdPatio(Long idPatio)
   {
      this.idPatio = idPatio;
   }

  
	
   
   
   @Transient
   public String getNomePatio()
   {
      return getMetaPatio().getNomePatio();
   }

   public void setNomePatio(String nomePatio)
   {
	   getMetaPatio().setNomePatio(nomePatio);
   }

  
   @Transient
   public Double getLargura()
   {
      return getMetaPatio().getLargura();
   }

   public void setLargura(Double largura)
   {
	   getMetaPatio().setLargura(largura);
   }

   @Transient
   public Double getComprimento()
   {
      return  getMetaPatio().getComprimento();
   }

   public void setComprimento(Double comprimento)
   {
	   getMetaPatio().setComprimento(comprimento);
   }

   @Transient
   public Double getCapacidadeMaxima()
   {
      return getMetaPatio().getCapacidadeMaxima();
   }

   public void setCapacidadeMaxima(Double capacidadeMaxima)
   {
	   getMetaPatio().setCapacidadeMaxima(capacidadeMaxima);
   }
   @Transient
   public List<Baliza> getListaDeBalizas(Date hora)
   {
      List<Baliza> result = new ArrayList<Baliza>();
	   
      for (MetaBaliza baliza : getMetaPatio().getListaDeMetaBalizas()) {
    	  result.add(baliza.retornaStatusHorario(hora));
      }
	 return result;  
   }

   
   
   @Transient
   public Integer getNumero()
   {
      return getMetaPatio().getNumero();
   }

   public void setNumero(Integer numero)
   {
	   getMetaPatio().setNumero(numero);
   }

  

   @Override
   public String toString()
   {
      return getNomePatio();
   }

  @Transient
   public List<MaquinaDoPatio> getListaDeMaquinasDoPatio(Date data) {
	// TODO Auto-generated method stub
      List<MaquinaDoPatio> listaDeMaquinasDoPatio = new ArrayList<MaquinaDoPatio>();
      MetaMaquinaDoPatioDAO dao = new  MetaMaquinaDoPatioDAO();
      MetaMaquinaDoPatio meta = new MetaMaquinaDoPatio();
      meta.setTipoDaMaquina(TipoMaquinaEnum.PA_CARREGADEIRA);
      List<MetaMaquinaDoPatio> result = dao.buscarListaDeObjetos(meta);
      for(MetaMaquinaDoPatio metaM : result) {
          MaquinaDoPatio maquina = metaM.retornaStatusHorario(data);
          if (maquina != null) {
             if  (maquina.getPatio().equals(this)) {
                 listaDeMaquinasDoPatio.add(maquina);
             }
          }
      }
      
      return listaDeMaquinasDoPatio;
}


@Override
public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((estado == null) ? 0 : estado.hashCode());
    result = prime * result + ((idPatio == null) ? 0 : idPatio.hashCode());
    result = prime * result + ((metaPatio == null) ? 0 : metaPatio.hashCode());
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
    Patio other = (Patio) obj;
    if (estado == null) {
        if (other.estado != null)
            return false;
    } else if (!estado.equals(other.estado))
        return false;
    if (idPatio == null) {
        if (other.idPatio != null)
            return false;
    } else if (!idPatio.equals(other.idPatio))
        return false;
    if (metaPatio == null) {
        if (other.metaPatio != null)
            return false;
    } else if (!metaPatio.equals(other.metaPatio))
        return false;
    return true;
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
