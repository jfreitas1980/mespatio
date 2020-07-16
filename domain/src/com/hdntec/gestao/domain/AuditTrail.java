package com.hdntec.gestao.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.OrderBy;

@MappedSuperclass
public class AuditTrail implements Serializable
{

   /** Serializacao do objeto */
   private static final long serialVersionUID = 1L;

   /** a data de criacao do obejto */
   private Date dtInsert;

   /** a data de atualizacao do objeto */
   private Date dtUpdate;

   /** o codigo do usuario que realizou a operacao */
   private Long idUser;

   //@Basic(optional = false) 
   @Column(name = "DT_INSERT",  nullable = true)
   @Temporal(TemporalType.TIMESTAMP)
   @OrderBy(clause = "desc")
   public Date getDtInsert()
   {	
	   return dtInsert;
   }

   public void setDtInsert(Date dtInsert)
   {
	this.dtInsert = dtInsert;
   }

   @Column(name = "DT_UPDATE", nullable = true)
   @Temporal(TemporalType.TIMESTAMP)
   public Date getDtUpdate()
   {
	return dtUpdate;
   }

   public void setDtUpdate(Date dtUpdate)
   {
	this.dtUpdate = dtUpdate;
   }

   @Column(name = "ID_USER", nullable = true)
   public Long getIdUser()
   {
	return idUser;
   }

   public void setIdUser(Long idUser)
   {
	this.idUser = idUser;
   }

   /*@Override
   public boolean equals(Object obj)
   {
	if (obj == null) {
	   return false;
	}
	if (getClass() != obj.getClass()) {
	   return false;
	}
	final AuditTrail other = (AuditTrail) obj;
	if (this.dtInsert != other.dtInsert && (this.dtInsert == null || !this.dtInsert.equals(other.dtInsert))) {
	   return false;
	}
	if (this.dtUpdate != other.dtUpdate && (this.dtUpdate == null || !this.dtUpdate.equals(other.dtUpdate))) {
	   return false;
	}
	if (this.idUser != other.idUser && (this.idUser == null || !this.idUser.equals(other.idUser))) {
	   return false;
	}
	return true;
   }

   @Override
   public int hashCode()
   {
	int hash = 3;
	hash = 53 * hash + (this.dtInsert != null ? this.dtInsert.hashCode() : 0);
	hash = 53 * hash + (this.dtUpdate != null ? this.dtUpdate.hashCode() : 0);
	hash = 53 * hash + (this.idUser != null ? this.idUser.hashCode() : 0);
	return hash;
   }*/

}
