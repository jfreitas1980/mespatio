package com.hdntec.gestao.domain.planta.entity.status;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.ForeignKey;

import com.hdntec.gestao.domain.AuditTrail;


/**
 *
 * @author Bruno Gomes
 */
@Entity
public class RegistroDaAnotacao extends AuditTrail
{

   /** serializacao do objeto*/
   private static final long serialVersionUID = -1l;

   /** identificador do registro da anotacao */
   private Long IdRegistroAnotacao;

   /**descricao desejada da anotacao */
   private String descricao;

   /** nome do usuario que inseriu esta anotacao*/
   private String usuario;

   /** data/hora em que a anotacao foi editada/criada */
   private Date dataAnotacao;

   /** a anotacao que possui uma lista de registros de anotação */
   private Anotacao anotacao;

      /**
    * construtor padrão
    */
   public RegistroDaAnotacao()
   {
   }

   
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO, generator = "registroanotacao_seq")
   @SequenceGenerator(name = "registroanotacao_seq", sequenceName = "seqregistroanotacao")
   public Long getIdRegistroAnotacao()
   {
      return IdRegistroAnotacao;
   }

   public void setIdRegistroAnotacao(Long IdRegistroAnotacao)
   {
      this.IdRegistroAnotacao = IdRegistroAnotacao;
   }

   @Column(nullable = false)
   @Temporal(TemporalType.TIMESTAMP)
   public Date getDataAnotacao()
   {
      return dataAnotacao;
   }

   public void setDataAnotacao(Date dataAnotacao)
   {
      this.dataAnotacao = dataAnotacao;
   }

   @Column(nullable = false)
   public String getDescricao()
   {
      return descricao;
   }

   public void setDescricao(String descricao)
   {
      this.descricao = descricao;
   }

   @Column
   public String getUsuario()
   {
      return usuario;
   }

   @ManyToOne
   //@Cascade(CascadeType.SAVE_UPDATE)
   @ForeignKey(name = "fk_registroAnot_Anotacao")
   @JoinColumn(name = "id_Anotacao",nullable=false)
   public Anotacao getAnotacao()
   {
      return anotacao;
   }

   public void setAnotacao(Anotacao anotacao)
   {
      this.anotacao = anotacao;
   }

   public void setUsuario(String usuario)
   {
      this.usuario = usuario;
   }


@Override
public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((IdRegistroAnotacao == null) ? 0 : IdRegistroAnotacao.hashCode());
    result = prime * result + ((dataAnotacao == null) ? 0 : dataAnotacao.hashCode());
    result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
    result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
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
    RegistroDaAnotacao other = (RegistroDaAnotacao) obj;
    if (IdRegistroAnotacao == null) {
        if (other.IdRegistroAnotacao != null)
            return false;
    } else if (!IdRegistroAnotacao.equals(other.IdRegistroAnotacao))
        return false;
    if (dataAnotacao == null) {
        if (other.dataAnotacao != null)
            return false;
    } else if (!dataAnotacao.equals(other.dataAnotacao))
        return false;
    if (descricao == null) {
        if (other.descricao != null)
            return false;
    } else if (!descricao.equals(other.descricao))
        return false;
    if (usuario == null) {
        if (other.usuario != null)
            return false;
    } else if (!usuario.equals(other.usuario))
        return false;
    return true;
}
@Override
public String toString(){
    
    return this.getIdRegistroAnotacao() + " - " +this.descricao;
    
}
      
}
