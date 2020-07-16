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
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.ForeignKey;

import com.hdntec.gestao.domain.StatusEntity;
import com.hdntec.gestao.domain.planta.entity.MetaCarregadoraDeNavios;
import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;


/**
 * Máquina utilizada para carregar os navios. Fica instalada no pier para
 * servir os navios que existem nos berços.
 * <p>
 * 
 */
@Entity
public class CarregadoraDeNavios extends StatusEntity<CarregadoraDeNavios>
{

   /** Serialização do Objeto */
   private static final long serialVersionUID = 2718915420664606114L;

   private Long idCarregadeiraDeNavios;

  
   /** estado de operação da Carregadeira de Navios */
   private EstadoMaquinaEnum estado;

   /** a taxa de operação em ton/h */
   private Double taxaDeOperacao;

   /** a data-hora da próxima manutenção */
   private Date proximaManutencao;
   
    @ManyToOne
	@JoinColumn(name = "ID_META_CAR", nullable = false, insertable = true)
	@ForeignKey(name = "fk_meta_car")
    public MetaCarregadoraDeNavios getMetaCarregadora() {
	return metaCarregadora;
}



public void setMetaCarregadora(MetaCarregadoraDeNavios metaCarregadora) {
	this.metaCarregadora = metaCarregadora;
}

private MetaCarregadoraDeNavios metaCarregadora;
  
   /**
    * Construtor Padrao.
    */
   public CarregadoraDeNavios()
   {
   }

  

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO, generator = "carregadoradenavios_seq")
   @SequenceGenerator(name = "carregadoradenavios_seq", sequenceName = "seqcarregadoradenavios")
   public Long getIdCarregadeiraDeNavios()
   {
      return idCarregadeiraDeNavios;
   }

   public void setIdCarregadeiraDeNavios(Long idCarregadeiraDeNavios)
   {
      this.idCarregadeiraDeNavios = idCarregadeiraDeNavios;
   }

   @Transient
   public String getNomeCarregadeiraDeNavios()
   {
      return getMetaCarregadora().getNomeCarregadeiraDeNavios();
   }

   public void setNomeCarregadeiraDeNavios(String nomeCarregadeiraDeNavios)
   {
	   getMetaCarregadora().setNomeCarregadeiraDeNavios(nomeCarregadeiraDeNavios);
   }

   @Column(nullable = false)
   @Enumerated(EnumType.STRING)
   public EstadoMaquinaEnum getEstado()
   {
      return estado;
   }

   public void setEstado(EstadoMaquinaEnum estado)
   {
      this.estado = estado;
   }

   @Column(nullable = false, precision = 2)
   public Double getTaxaDeOperacao()
   {
      return taxaDeOperacao;
   }

   public void setTaxaDeOperacao(Double taxaDeOperacao)
   {
      this.taxaDeOperacao = taxaDeOperacao;
   }

   @Column(nullable = true)
   @Temporal(TemporalType.DATE)
   public Date getProximaManutencao()
   {
      return proximaManutencao;
   }

   public void setProximaManutencao(Date proximaManutencao)
   {
      this.proximaManutencao = proximaManutencao;
   }



@Override
public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((estado == null) ? 0 : estado.hashCode());
    result = prime * result + ((idCarregadeiraDeNavios == null) ? 0 : idCarregadeiraDeNavios.hashCode());
    result = prime * result + ((metaCarregadora == null) ? 0 : metaCarregadora.hashCode());
    result = prime * result + ((taxaDeOperacao == null) ? 0 : taxaDeOperacao.hashCode());
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
    CarregadoraDeNavios other = (CarregadoraDeNavios) obj;
    if (estado == null) {
        if (other.estado != null)
            return false;
    } else if (!estado.equals(other.estado))
        return false;
    if (idCarregadeiraDeNavios == null) {
        if (other.idCarregadeiraDeNavios != null)
            return false;
    } else if (!idCarregadeiraDeNavios.equals(other.idCarregadeiraDeNavios))
        return false;
    if (metaCarregadora == null) {
        if (other.metaCarregadora != null)
            return false;
    } else if (!metaCarregadora.equals(other.metaCarregadora))
        return false;
    if (taxaDeOperacao == null) {
        if (other.taxaDeOperacao != null)
            return false;
    } else if (!taxaDeOperacao.equals(other.taxaDeOperacao))
        return false;
    return true;
}

      
}
