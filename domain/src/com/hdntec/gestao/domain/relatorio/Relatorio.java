package com.hdntec.gestao.domain.relatorio;

import java.io.Serializable;
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

import com.hdntec.gestao.domain.relatorio.enums.EnumTipoRelatorio;
import com.hdntec.gestao.login.entity.User;



/**
 * 
 * <P><B>Description :</B><BR>
 * General Relatorio
 * </P>
 * <P>
 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
 * @since 16/06/2009
 * @version $Revision: 1.1 $
 */
@Entity
public class Relatorio implements Serializable
{

   /** serialVersionUID */
   private static final long serialVersionUID = 5923724180944916947L;

   private Long idRelatorio;

   private String nomeRelatorio;

   private EnumTipoRelatorio tipoRelatorio;

   private Date horarioInicioRelatorio;

   private Date horarioFimRelatorio;

   private Long idUsuario;

   private PadraoRelatorio padraoRelatorio;

   private User usuario;

   private String observacao;

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO, generator = "relatorio_seq")
   @SequenceGenerator(name = "relatorio_seq", sequenceName = "seqrelatorio")
   public Long getIdRelatorio()
   {
      return idRelatorio;
   }

   public void setIdRelatorio(Long idRelatorio)
   {
      this.idRelatorio = idRelatorio;
   }

   @Column(nullable = false, length = 50)
   public String getNomeRelatorio()
   {
      return nomeRelatorio;
   }

   public void setNomeRelatorio(String nomeRelatorio)
   {
      this.nomeRelatorio = nomeRelatorio;
   }

   @Column(nullable = false)
   @Enumerated(EnumType.STRING)
   public EnumTipoRelatorio getTipoRelatorio()
   {
      return tipoRelatorio;
   }

   public void setTipoRelatorio(EnumTipoRelatorio tipoRelatorio)
   {
      this.tipoRelatorio = tipoRelatorio;
   }

   @Column(nullable = true)
   @Temporal(TemporalType.TIMESTAMP)
   public Date getHorarioInicioRelatorio()
   {
      return horarioInicioRelatorio;
   }

   public void setHorarioInicioRelatorio(Date horarioInicioRelatorio)
   {
      this.horarioInicioRelatorio = horarioInicioRelatorio;
   }

   @Column(nullable = true)
   @Temporal(TemporalType.TIMESTAMP)
   public Date getHorarioFimRelatorio()
   {
      return horarioFimRelatorio;
   }

   public void setHorarioFimRelatorio(Date horarioFimRelatorio)
   {
      this.horarioFimRelatorio = horarioFimRelatorio;
   }

   @Column(nullable = false)
   public Long getIdUsuario()
   {
      return idUsuario;
   }

   public void setIdUsuario(Long idUsuario)
   {
      this.idUsuario = idUsuario;
   }

   @ManyToOne
   @JoinColumn(name = "idPadraoRelatorio", nullable = false)
   public PadraoRelatorio getPadraoRelatorio()
   {
      return padraoRelatorio;
   }

   public void setPadraoRelatorio(PadraoRelatorio padraoRelatorio)
   {
      this.padraoRelatorio = padraoRelatorio;
   }

   @Transient
   public User getUsuario()
   {
      return usuario;
   }

   public void setUsuario(User usuario)
   {
      this.usuario = usuario;
   }

   @Transient
   public String getObservacao()
   {
      if (observacao == null)
      {
         observacao = "";
      }
      return observacao;
   }

   public void setObservacao(String observacao)
   {
      this.observacao = observacao;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (obj == null)
      {
         return false;
      }
      if (getClass() != obj.getClass())
      {
         return false;
      }
      final Relatorio other = (Relatorio) obj;
      if (this.idRelatorio != other.idRelatorio && (this.idRelatorio == null || !this.idRelatorio.equals(other.idRelatorio)))
      {
         return false;
      }
      if ((this.nomeRelatorio == null) ? (other.nomeRelatorio != null) : !this.nomeRelatorio.equals(other.nomeRelatorio))
      {
         return false;
      }
      if (this.tipoRelatorio != other.tipoRelatorio)
      {
         return false;
      }
      if (this.horarioInicioRelatorio != other.horarioInicioRelatorio && (this.horarioInicioRelatorio == null || !this.horarioInicioRelatorio.equals(other.horarioInicioRelatorio)))
      {
         return false;
      }
      if (this.horarioFimRelatorio != other.horarioFimRelatorio && (this.horarioFimRelatorio == null || !this.horarioFimRelatorio.equals(other.horarioFimRelatorio)))
      {
         return false;
      }
      if (this.idUsuario != other.idUsuario && (this.idUsuario == null || !this.idUsuario.equals(other.idUsuario)))
      {
         return false;
      }
      if (this.padraoRelatorio != other.padraoRelatorio && (this.padraoRelatorio == null || !this.padraoRelatorio.equals(other.padraoRelatorio)))
      {
         return false;
      }
      if (this.usuario != other.usuario && (this.usuario == null || !this.usuario.equals(other.usuario)))
      {
         return false;
      }
      if ((this.observacao == null) ? (other.observacao != null) : !this.observacao.equals(other.observacao))
      {
         return false;
      }
      return true;
   }

   @Override
   public int hashCode()
   {
      int hash = 7;
      hash = 79 * hash + (this.idRelatorio != null ? this.idRelatorio.hashCode() : 0);
      hash = 79 * hash + (this.nomeRelatorio != null ? this.nomeRelatorio.hashCode() : 0);
      hash = 79 * hash + this.tipoRelatorio.hashCode();
      hash = 79 * hash + (this.horarioInicioRelatorio != null ? this.horarioInicioRelatorio.hashCode() : 0);
      hash = 79 * hash + (this.horarioFimRelatorio != null ? this.horarioFimRelatorio.hashCode() : 0);
      hash = 79 * hash + (this.idUsuario != null ? this.idUsuario.hashCode() : 0);
      hash = 79 * hash + (this.observacao != null ? this.observacao.hashCode() : 0);
      return hash;
   }
   
}
