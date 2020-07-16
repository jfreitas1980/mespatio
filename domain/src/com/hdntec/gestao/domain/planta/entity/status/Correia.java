package com.hdntec.gestao.domain.planta.entity.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.ForeignKey;

import com.hdntec.gestao.domain.StatusEntity;
import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
import com.hdntec.gestao.domain.planta.entity.MetaCorreia;
import com.hdntec.gestao.domain.planta.entity.MetaMaquinaDoPatio;
import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;


/**
 * É a estrutura que transporta os produtos das usinas para os pátios, até as
 * máquinas empilhadeiras, nas irremediações dos pátios e dos pátios para o
 * píer, até a carregadora de navios.
 * <p>
 * Persite. - Tamanho: - Volume: poucos registros. -Período de Persistência:
 * cadastro de planta. - Freq. Update: raro. -Confiabilidade: deve ser
 * confiável.
 * 
 * @author andre
 * 
 */
@Entity
public class Correia extends StatusEntity<Correia>
{

   /** Serialização do Objeto */
   private static final long serialVersionUID = 749117360809610855L;

   private Long idCorreia;

   /** estado de operação da Correia */
   private EstadoMaquinaEnum estado;

   /** a taxa de operação da correia, em ton/h */
   private Double taxaDeOperacao;

   private MetaCorreia metaCorreia;

   
   /**
    * Construtor Padrao
    */
   public Correia()
   {
   }


   @Id
   @GeneratedValue(strategy = GenerationType.AUTO, generator = "correia_seq")
   @SequenceGenerator(name = "correia_seq", sequenceName = "seqcorreia")
   public Long getIdCorreia()
   {
      return idCorreia;
   }

	@ManyToOne
	@JoinColumn(name = "ID_META_COR", nullable = false, insertable = true)
	@ForeignKey(name = "fk_meta_cor")
	public MetaCorreia getMetaCorreia() {
		return metaCorreia;
	}


public void setMetaCorreia(MetaCorreia metaCorreia) {
	this.metaCorreia = metaCorreia;
}


public void setIdCorreia(Long idCorreia)
   {
      this.idCorreia = idCorreia;
   }

   @Transient
   public String getNomeCorreia()
   {
      return getMetaCorreia().getNomeCorreia();
   }



   /**
    * @return the estado
    */
   @Column(nullable = false)
   @Enumerated(EnumType.STRING)
   public EstadoMaquinaEnum getEstado()
   {
      return estado;
   }

   /**
    * @param estado the estado to set
    */
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

   @Transient
   public Patio getPatioInferior()
   {
       if  (getMetaCorreia().getMetaPatioInferior() != null ) {
        return getMetaCorreia().getMetaPatioInferior().retornaStatusHorario(this.getDtInicio());
       }
       return null;
   }

   public void setPatioInferior(Patio patioInferior)
   {
     
   }

   @Transient
   public Patio getPatioSuperior()
   {
       if  (getMetaCorreia().getMetaPatioSuperior() != null ) {
           return getMetaCorreia().getMetaPatioSuperior().retornaStatusHorario(this.getDtInicio()); 
       }   
       return null;
   }

   public void setPatioSuperior(Patio patioSuperior)
   {
   
   }

   @Transient
   public Integer getNumero()
   {
      return getMetaCorreia().getNumero();
   }

   
   @Transient
   public List<MaquinaDoPatio> getListaDeMaquinas(Date data)
   {
       List<MaquinaDoPatio> result = new ArrayList<MaquinaDoPatio>();
       for (MetaMaquinaDoPatio metaMaquina : getMetaCorreia().getListaDeMetaMaquinas()) {
           result.add(metaMaquina.retornaStatusHorario(data));
       }
       return result;
   }

   /**
    * @param listaDeMaquinas the listaDeMaquinas to set
    */
   public void setListaDeMaquinas(List<MaquinaDoPatio> listaDeMaquinas)
   {
    
   }
   
   
   @Override
   public String toString()
   {
      return getMetaCorreia().getNomeCorreia();
   }

   /**
    * Atualiza o status da correia para Ociosa se nenhuma maquina ou 
    * usina estiver operando sobre ela para a situacao informada
    * @param sitPatio
    */
   public void atualizaStatusOcioso(SituacaoPatio sitPatio) {

	   boolean estadoOciosaMaquina = true;
	   //A correia nao pode estar sendo utilizada pelas maquinas para se tornar ociosa
	   for(MaquinaDoPatio maquina : getListaDeMaquinas(sitPatio.getDtInicio())){
		   if(maquina.getEstado().equals(EstadoMaquinaEnum.OPERACAO)){
			   estadoOciosaMaquina = false;
		   }
	   }

	   //A correia nao pode estar sendo utilizada pelas usinas para se tornar ociosa
	   try {
		for(Usina usina : sitPatio.getPlanta().getListaUsinas(sitPatio.getDtInicio())){
			   //if (usina.getAtividade() != null) {
				   //estadoOciosaMaquina = false;
			   //}
		   }
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	   if (estadoOciosaMaquina) {
		   setEstado(EstadoMaquinaEnum.OCIOSA);
	   }

   }

   
}
