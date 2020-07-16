package com.hdntec.gestao.domain.planta.entity.status;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import com.hdntec.gestao.domain.ComparadorStatusEntity;
import com.hdntec.gestao.domain.StatusEntity;


@Entity
public class Interdicao extends StatusEntity<Interdicao>
{

   /** Serializacao do objeto */
   private static final long serialVersionUID = -4742294757292603806L;

   public static ComparadorStatusEntity<Interdicao> comparadorInterdicao = new ComparadorStatusEntity<Interdicao>();


   public Interdicao()
   {
   }

   /** id da entidade */
   private Long idInterdicao;

   
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO, generator = "interdicao_seq")
   @SequenceGenerator(name = "interdicao_seq", sequenceName = "seqinterdicao")
   public Long getIdInterdicao()
   {
      return idInterdicao;
   }

   public void setIdInterdicao(Long idInterdicao)
   {
      this.idInterdicao = idInterdicao;
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
@Transient
public void setDataFinal(Date dataHoraDate) {
	// TODO Auto-generated method stub
	setDtFim(dataHoraDate);
}
@Transient
public void setDataInicial(Date dataHoraDate) {
	// TODO Auto-generated method stub
	setDtInicio(dataHoraDate);
}

@Override
public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((idInterdicao == null) ? 0 : idInterdicao.hashCode());
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
    Interdicao other = (Interdicao) obj;
    if (idInterdicao == null) {
        if (other.idInterdicao != null)
            return false;
    } else if (!idInterdicao.equals(other.idInterdicao))
        return false;
    return true;
}


}
