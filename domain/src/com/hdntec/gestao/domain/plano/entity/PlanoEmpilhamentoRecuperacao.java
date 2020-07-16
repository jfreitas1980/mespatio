/**
 * Um plano de empilhamento e recuperação é um conjunto de situações de pátio,
 * planejadas num horizonte de tempo.
 *
 * @author andre
 *
 */
package com.hdntec.gestao.domain.plano.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.hdntec.gestao.domain.StatusEntity;


@Entity
public class PlanoEmpilhamentoRecuperacao extends StatusEntity<PlanoEmpilhamentoRecuperacao>
{

   /** Serialização do objeto */
   private static final long serialVersionUID = -8974740270969223866L;

   /** identificador de PlanoDeEmpilhamentoERecuperacao */
   private Long idPlanoEmpRec;

   /** o horizonte de duração do plano (em horas)*/
   private Long horizonteDePlanejamento;

  
   /** status se o plano é oficial ou não */
   private Boolean ehOficial;

   /** a lista de situações de pátio do plano geradas pelas atividades*/
   private List<SituacaoPatio> listaSituacoesPatio;
  
   
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO, generator = "planoemprec_seq")
   @SequenceGenerator(name = "planoemprec_seq", sequenceName = "seqplanoemprec")
   public Long getIdPlanoEmpRec()
   {
      return idPlanoEmpRec;
   }

   @Column
   public Long getHorizonteDePlanejamento()
   {
      return horizonteDePlanejamento;
   }

  
   @Column
   public Boolean getEhOficial()
   {
      return ehOficial;
   }

   //TODO Darley SAXXXX Trocando o EAGER pelo LAZY
   @OneToMany(fetch = FetchType.LAZY,mappedBy="planoEmpilhamento")    
   @Fetch(FetchMode.SELECT)   
   public List<SituacaoPatio> getListaSituacoesPatio()
   {
      return listaSituacoesPatio;
   }

   public void setListaSituacoesPatio(List<SituacaoPatio> listaSituacoesPatio)
   {
      this.listaSituacoesPatio = listaSituacoesPatio;
   }

   public SituacaoPatio getSituacaoDePatioSelecionada(Integer indiceSituacaoPatioSelecionada)
   {
      return this.getListaSituacoesPatio().get(indiceSituacaoPatioSelecionada);
   }

  

   public void setEhOficial(Boolean ehOficial)
   {
      this.ehOficial = ehOficial;
   }

   public void setHorizonteDePlanejamento(Long horizonteDePlanejamento)
   {
      this.horizonteDePlanejamento = horizonteDePlanejamento;
   }

   public void setIdPlanoEmpRec(Long idPlanoEmpRec)
   {
      this.idPlanoEmpRec = idPlanoEmpRec;
   }

  


   public void addSituacaoPatio(SituacaoPatio item)
   {
      if (getListaSituacoesPatio() == null)
      {
         setListaSituacoesPatio(new ArrayList<SituacaoPatio>());
      }
      if (!getListaSituacoesPatio().contains(item))
      {
         item.setPlanoEmpilhamento(this);
         getListaSituacoesPatio().add(item);
      }
   }

@Override
public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((ehOficial == null) ? 0 : ehOficial.hashCode());
    result = prime * result + ((horizonteDePlanejamento == null) ? 0 : horizonteDePlanejamento.hashCode());
    result = prime * result + ((idPlanoEmpRec == null) ? 0 : idPlanoEmpRec.hashCode());
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
    PlanoEmpilhamentoRecuperacao other = (PlanoEmpilhamentoRecuperacao) obj;
    if (ehOficial == null) {
        if (other.ehOficial != null)
            return false;
    } else if (!ehOficial.equals(other.ehOficial))
        return false;
    if (horizonteDePlanejamento == null) {
        if (other.horizonteDePlanejamento != null)
            return false;
    } else if (!horizonteDePlanejamento.equals(other.horizonteDePlanejamento))
        return false;
    if (idPlanoEmpRec == null) {
        if (other.idPlanoEmpRec != null)
            return false;
    } else if (!idPlanoEmpRec.equals(other.idPlanoEmpRec))
        return false;
    return true;
}



public SituacaoPatio gerarSituacao(Atividade atividade) {     	      
    SituacaoPatio situacaoFinal = obterSituacaoPatioFinal();
    SituacaoPatio situacaoNova = situacaoFinal.copiarStatus();                         
    situacaoNova.setDtInsert(new Date(System.currentTimeMillis()));
    situacaoNova.setIdUser(1l);
    situacaoNova.setAtividade(atividade); 
    if (atividade.getFinalizada() && atividade.getDtFim() != null) {
       situacaoNova.setDtInicio(atividade.getDtFim());	
    } else {
       situacaoNova.setDtInicio(atividade.getDtInicio());
    }
	return situacaoNova;    
}



 /**
  * Retorna a situacao de patio final do plano de empilhamento do
  * usuario logado
  * @return
  */
 public SituacaoPatio obterSituacaoPatioFinal()
 {
    SituacaoPatio situacaoPatioFinal = new LinkedList<SituacaoPatio>(listaSituacoesPatio).getLast();
    return situacaoPatioFinal;
 }

   
}
