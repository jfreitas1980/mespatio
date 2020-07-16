package com.hdntec.gestao.domain.planta.entity;

import java.util.ArrayList;
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
import javax.persistence.OrderBy;
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
import com.hdntec.gestao.domain.planta.entity.status.Patio;
import com.hdntec.gestao.domain.planta.entity.status.Planta;
import com.hdntec.gestao.domain.planta.enums.EnumTipoBaliza;


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
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"numero","nomePatio"}))
public class MetaPatio  extends AbstractMetaEntity<Patio> 
{

   /** Serialização do Objeto */
   private static final long serialVersionUID = 8468597089080890920L;

   private Planta planta;
   
   private Long idPatio;

   /** Nome do Patio */
   private String nomePatio;

  
   /** numero do patio */
   private Integer numero;

   /** largura do pátio, em metros */
   private Double largura;

   /** comprimento do pátio, em metros */
   private Double comprimento;

   /** capacidade máxima do pátio, em toneladas */
   private Double capacidadeMaxima;

   /** a lista de balizas do pátio */
   private List<MetaBaliza> listaDeMetaBalizas;

   /**a lista de usinas que colocam pelletScreening no patio*/
   private List<MetaUsina> listaDeMetaUsinasExpurgoPelletScreening;

   
   /**
    * Construtor Padrao
    */
   public MetaPatio()
   {
   }

   
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO, generator = "patio_seq")
   @SequenceGenerator(name = "patio_seq", sequenceName = "seqpatio")
   public Long getIdPatio()
   {
      return idPatio;
   }

   public void setIdPatio(Long idPatio)
   {
      this.idPatio = idPatio;
   }

   @Column(nullable = false, length = 60)
   public String getNomePatio()
   {
      return nomePatio;
   }

   public void setNomePatio(String nomePatio)
   {
      this.nomePatio = nomePatio;
   }

   
   @Column(nullable = false)
   public Double getLargura()
   {
      return largura;
   }

   public void setLargura(Double largura)
   {
      this.largura = largura;
   }

   @Column(nullable = false)
   public Double getComprimento()
   {
      return comprimento;
   }

   public void setComprimento(Double comprimento)
   {
      this.comprimento = comprimento;
   }

   @Column(nullable = false)
   public Double getCapacidadeMaxima()
   {
      return capacidadeMaxima;
   }

   public void setCapacidadeMaxima(Double capacidadeMaxima)
   {
      this.capacidadeMaxima = capacidadeMaxima;
   }
   
   @OneToMany(fetch = FetchType.LAZY, mappedBy="metaPatio")
   @Fetch(FetchMode.SELECT)
  // @Cascade(CascadeType.ALL)
   @OrderBy("numero")
   public List<MetaBaliza> getListaDeMetaBalizas()
   {
      return listaDeMetaBalizas;
   }

  
   public void setListaDeMetaBalizas(List<MetaBaliza> listaDeBalizas)
   {
      this.listaDeMetaBalizas = listaDeBalizas;
   }

   @Column(nullable = false)
   public Integer getNumero()
   {
      return numero;
   }

   public void setNumero(Integer numero)
   {
      this.numero = numero;
   }

      @Override
   public String toString()
   {
      return getNomePatio();
   }


   
   @Override
   @OneToMany(fetch = FetchType.LAZY, mappedBy="metaPatio")
   @Fetch(FetchMode.SELECT)
   //@Cascade(value={CascadeType.ALL,CascadeType.SAVE_UPDATE})        
   public List<Patio> getListaStatus() {
	return super.getListaStatus();
   }		
   
   
   /**
    * Metodo que retorna uma lista de balizas ordenada de acordo com os tipo de baliza
    * passada como parametro 
    * @param tipoDeBaliza
    * @return
    */
   public List<MetaBaliza> obterBalizasPeloTipo(EnumTipoBaliza tipoDeBaliza)
   {
      List<MetaBaliza> listaBalizas = new ArrayList<MetaBaliza>();
      for (MetaBaliza baliza : this.getListaDeMetaBalizas())
      {
         if (baliza.getTipoBaliza().equals(tipoDeBaliza))
         {
            listaBalizas.add(baliza);
         }
      }
      return listaBalizas;
   }

   public void addBaliza(MetaBaliza baliza)
   {
      if (getListaDeMetaBalizas() == null)
      {
         setListaDeMetaBalizas(new ArrayList<MetaBaliza>());
      }

      if (!getListaDeMetaBalizas().contains(baliza))
      {
         getListaDeMetaBalizas().add(baliza);
         baliza.setMetaPatio(this);
      }
   }

   public void addBaliza(List<MetaBaliza> balizas)
   {
      if (balizas != null)
      {
         for (MetaBaliza baliza : balizas)
         {
            addBaliza(baliza);
         }
      }
   }

     
   @Override
   public void incluirNovoStatus(Patio novoStatus, Date horaStatus) {
      super.incluirNovoStatus(novoStatus, horaStatus);
      novoStatus.setMetaPatio(this);      
   }

   @Transient
   public List<MetaUsina> getListaDeMetaUsinasExpurgoPelletScreening() {
	 return listaDeMetaUsinasExpurgoPelletScreening;
 }


public void setListaDeMetaUsinasExpurgoPelletScreening(
		List<MetaUsina> listaDeMetaUsinasExpurgoPelletScreening) {
	this.listaDeMetaUsinasExpurgoPelletScreening = listaDeMetaUsinasExpurgoPelletScreening;
}


@ManyToOne
@JoinColumn(name = "ID_PLANTA", nullable = false, insertable = true)
@ForeignKey(name = "fk_meta_patio_planta")
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
    result = prime * result + ((capacidadeMaxima == null) ? 0 : capacidadeMaxima.hashCode());
    result = prime * result + ((comprimento == null) ? 0 : comprimento.hashCode());
    result = prime * result + ((idPatio == null) ? 0 : idPatio.hashCode());
    result = prime * result + ((largura == null) ? 0 : largura.hashCode());
    result = prime * result + ((nomePatio == null) ? 0 : nomePatio.hashCode());
    result = prime * result + ((numero == null) ? 0 : numero.hashCode());
    result = prime * result + ((planta == null) ? 0 : planta.hashCode());
    return result;
}


@Override
public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (getClass() != obj.getClass())
        return false;
    MetaPatio other = (MetaPatio) obj;
    if (capacidadeMaxima == null) {
        if (other.capacidadeMaxima != null)
            return false;
    } else if (!capacidadeMaxima.equals(other.capacidadeMaxima))
        return false;
    if (comprimento == null) {
        if (other.comprimento != null)
            return false;
    } else if (!comprimento.equals(other.comprimento))
        return false;
    if (idPatio == null) {
        if (other.idPatio != null)
            return false;
    } else if (!idPatio.equals(other.idPatio))
        return false;
    if (largura == null) {
        if (other.largura != null)
            return false;
    } else if (!largura.equals(other.largura))
        return false;
    if (nomePatio == null) {
        if (other.nomePatio != null)
            return false;
    } else if (!nomePatio.equals(other.nomePatio))
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

   
}
