package com.hdntec.gestao.domain.navios.entity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.hdntec.gestao.domain.AbstractMetaEntity;
import com.hdntec.gestao.domain.navios.entity.status.Navio;


/**
 *Os navios são os equipamentos que são embarcados com as diversas cargas de produtos para um determinado cliente.
 * @author andre
 * 
 */

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"sap_vbap_vbeln"}))
public class MetaNavio extends AbstractMetaEntity<Navio>
{
   /**
	 * 
	 */
	private static final long serialVersionUID = -5154343787029144134L;

/** identificador de meta navio */
   private Long idMetaNavio;
   
   /** Chave para join do SAP, esta é a mesma chave da tabela IntegracaoNavioCRM */
   private String sap_vbap_vbeln;

   private List<MetaCarga> listaMetaCargas;

   
   
   public MetaNavio()
   {
   }
   
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO, generator = "meta_nav_seq")
   @SequenceGenerator(name = "meta_nav_seq", sequenceName = "seq_meta_nav")
   public Long getIdMetaNavio()
   {
      return idMetaNavio;
   }
   
   public void setIdMetaNavio(Long idMetaNavio)
   {
      this.idMetaNavio = idMetaNavio;
   }
   
   @Column
   public String getSap_vbap_vbeln()
   {
      return sap_vbap_vbeln;
   }
   
   public void setSap_vbap_vbeln(String sap_vbap_vbeln)
   {
      this.sap_vbap_vbeln = sap_vbap_vbeln;
   }
   
   @Override
   @OneToMany(fetch = FetchType.LAZY,mappedBy="metaNavio")
   @Fetch(FetchMode.SELECT)
   //@Cascade(value={CascadeType.ALL,CascadeType.SAVE_UPDATE})        
   public List<Navio> getListaStatus() {
	return super.getListaStatus();
   }

   @OneToMany(fetch = FetchType.LAZY,mappedBy="metaNavio")
   @Fetch(FetchMode.SELECT)
   //@Cascade(value={CascadeType.ALL,CascadeType.SAVE_UPDATE,CascadeType.DELETE_ORPHAN})              
   public List<MetaCarga> getListaMetaCargas() {	   
       return listaMetaCargas;
   }

   public void setListaMetaCargas(List<MetaCarga> listaCargas) {
	   this.listaMetaCargas = listaCargas;
   }		
   
   @Override
   @Transient
   public void incluirNovoStatus(Navio novoStatus, Date horaStatus){
       super.incluirNovoStatus(novoStatus, horaStatus);
       novoStatus.setMetaNavio(this);
   }
   
   public void addMetaCarga(MetaCarga metaCarga) {
      if (getListaMetaCargas() == null) {
          setListaMetaCargas(new ArrayList<MetaCarga>());
      }
      if (!getListaMetaCargas().contains(metaCarga)) {
          getListaMetaCargas().add(metaCarga);
          metaCarga.setMetaNavio(this);
      }      
   }
    
   public void addMetaCarga(List<MetaCarga> metaCargas) {
       if (metaCargas != null) {
           for(MetaCarga meta : metaCargas) {
               addMetaCarga(meta);               
           }
       }       
    }

@Override
public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((idMetaNavio == null) ? 0 : idMetaNavio.hashCode());
    result = prime * result + ((sap_vbap_vbeln == null) ? 0 : sap_vbap_vbeln.hashCode());
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
    MetaNavio other = (MetaNavio) obj;
    if (idMetaNavio == null) {
        if (other.idMetaNavio != null)
            return false;
    } else if (!idMetaNavio.equals(other.idMetaNavio))
        return false;
    if (sap_vbap_vbeln == null) {
        if (other.sap_vbap_vbeln != null)
            return false;
    } else if (!sap_vbap_vbeln.equals(other.sap_vbap_vbeln))
        return false;
    return true;
}

@Override
public Navio clonarStatus(Date horario) {
    Navio result = null;
    result = super.clonarStatus(horario);
    result.setIdNavio(null);   
    return result;
}

}
