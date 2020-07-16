/**
 * 
 */
package com.hdntec.gestao.domain.planta.entity.status;

import java.io.Serializable;
import java.text.SimpleDateFormat;
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
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.ForeignKey;

import com.hdntec.gestao.domain.ComparadorStatusEntity;
import com.hdntec.gestao.domain.StatusEntity;
import com.hdntec.gestao.domain.navios.entity.status.Navio;


/**
 * Uma anotação representa uma série de comentários gravados no DSP, com registro de autor, data e hora.
 * 
 * @author andre
 *
 */
@Entity
public class Anotacao extends StatusEntity<Anotacao> {

   public static ComparadorStatusEntity<Anotacao> comparadorAnotacao = new ComparadorStatusEntity<Anotacao>();

   /** Serialização do objeto */
   private static final long serialVersionUID = -725735388809540954L;

   /** identificador da anotação */
   private Long idAnotacao;

   /** lista com os registros pertencentes a anotacao */
   private List<RegistroDaAnotacao> listaRegistrosDaAnotacao;

   /** posicao no patio da anotacao no eixo X */
   private Integer eixoX;

   /** posicao  no patio da anotacao no eixo Y */
   private Integer eixoY;

   /**
    * Construtor padrão
    */
   public Anotacao()
   {
   }


   /**
    * @return the idAnotacao
    */
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO, generator = "anotacao_seq")
   @SequenceGenerator(name = "anotacao_seq", sequenceName = "seqanotacao")
   public Long getIdAnotacao()
   {
      return idAnotacao;
   }

   /**
    * @param idAnotacao the idAnotacao to set
    */
   public void setIdAnotacao(Long idAnotacao)
   {
      this.idAnotacao = idAnotacao;
   }
   
   //TODO Darley SA11079 Otimizando colocando LAZY
   @OneToMany(mappedBy = "anotacao",fetch = FetchType.LAZY,cascade = {
                   javax.persistence.CascadeType.MERGE, javax.persistence.CascadeType.PERSIST})
   @Fetch(FetchMode.SELECT)
   @Cascade(value = {
                    CascadeType.SAVE_UPDATE,CascadeType.DELETE})
       
   public List<RegistroDaAnotacao> getListaRegistrosDaAnotacao()
   {
      return listaRegistrosDaAnotacao;
   }

   @Column(nullable = false)
   public Integer getEixoX()
   {
      return eixoX;
   }

   @Column(nullable = false)
   public Integer getEixoY()
   {
      return eixoY;
   }

   public void setEixoX(Integer eixoX)
   {
      this.eixoX = eixoX;
   }

   public void setEixoY(Integer eixoY)
   {
      this.eixoY = eixoY;
   }

   public void setListaRegistrosDaAnotacao(List<RegistroDaAnotacao> listaRegistrosDaAnotacao)
   {
      this.listaRegistrosDaAnotacao = listaRegistrosDaAnotacao;
   }

   
   public void addRegistrosDaAnotacao(List<RegistroDaAnotacao> listaRegistrosDaAnotacao)
   {
       if (this.getListaRegistrosDaAnotacao() == null) {
           this.setListaRegistrosDaAnotacao(new ArrayList<RegistroDaAnotacao>());
       }
       for(RegistroDaAnotacao reg : listaRegistrosDaAnotacao) {
           if (!this.getListaRegistrosDaAnotacao().contains(reg)) {
               this.getListaRegistrosDaAnotacao().add(reg);
               reg.setAnotacao(this);
           }
       }
       this.listaRegistrosDaAnotacao = listaRegistrosDaAnotacao;
   }

   
   @Override
   public String toString()
   {
      StringBuffer anotacao = new StringBuffer();
      if (listaRegistrosDaAnotacao != null)
      {
         for (RegistroDaAnotacao registroAnotacao : listaRegistrosDaAnotacao)
         {
            anotacao.append("Responsável: " + registroAnotacao.getUsuario()).append("<br>");
            anotacao.append("Descricao: " + registroAnotacao.getDescricao()).append("<br>");
            anotacao.append("Data: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(registroAnotacao.getDataAnotacao())).append("<br>");;            
         }
      }
      return anotacao.toString();
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
      final Anotacao other = (Anotacao) obj;
      if (this.idAnotacao != other.idAnotacao && (this.idAnotacao == null || !this.idAnotacao.equals(other.idAnotacao)))
      {
         return false;
      }
      if (this.listaRegistrosDaAnotacao != other.listaRegistrosDaAnotacao && (this.listaRegistrosDaAnotacao == null || !this.listaRegistrosDaAnotacao.equals(other.listaRegistrosDaAnotacao)))
      {
         return false;
      }
      if (this.eixoX != other.eixoX && (this.eixoX == null || !this.eixoX.equals(other.eixoX)))
      {
         return false;
      }
      if (this.eixoY != other.eixoY && (this.eixoY == null || !this.eixoY.equals(other.eixoY)))
      {
         return false;
      }
      return true;
   }

   @Override
   public int hashCode()
   {
      int hash = 3;
      hash = 97 * hash + (this.idAnotacao != null ? this.idAnotacao.hashCode() : 0);
      hash = 97 * hash + (this.eixoX != null ? this.eixoX.hashCode() : 0);
      hash = 97 * hash + (this.eixoY != null ? this.eixoY.hashCode() : 0);
      return hash;
   }

   @Transient
   public Date getDataFinal() {
       // TODO Auto-generated method stub
       return getDtFim();
   }

   @Transient
   public Date getDataInicial() {
       // TODO Auto-generated method stub
       return getDtInicio();
   }

}
