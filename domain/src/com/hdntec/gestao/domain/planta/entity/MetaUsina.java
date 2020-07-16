package com.hdntec.gestao.domain.planta.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.planta.entity.status.Campanha;
import com.hdntec.gestao.domain.planta.entity.status.Correia;
import com.hdntec.gestao.domain.planta.entity.status.Planta;
import com.hdntec.gestao.domain.planta.entity.status.Usina;
import com.hdntec.gestao.domain.planta.enums.EnumTipoBaliza;
import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;
import com.hdntec.gestao.domain.vo.atividades.AtividadeRecuperarEmpilharVO;


@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"nomeUsina"}))
public class MetaUsina extends AbstractMetaEntity<Usina> 
{

   /** Serializa  do Objeto */
   private static final long serialVersionUID = -1038231345790374262L;

   private Planta planta;
   
   private Long idUsina;

   /** Nome da Usina */
   private String nomeUsina;
   
   private MetaFiltragem filtragemOrigem;

   /** codigo da fase processo para ritimo de producao da usina */
   private Long codigoFaseProcessoUsina;

   /** codigo do item de controle para ritimo de producao da usina */
   private Long cdItemControleUsina;

   /** codigo do tipo processo para o ritimo de producao da Usina */
   private String cdTipoProcessoUsina;

   /** codigo da area responsavel para o ritimo de producao da Usina */
   private String cdAreaRespEdUsina;

   /** Patio onde o Pellet Screening será expurgado */
   private MetaPatio metaPatioExpurgoPellet;

   /** Patio onde a emergencia é empilhada */
   private List<MetaPatio> listaMetaPatioEmergencia;
   
   /** Mapa com os tipos de baliza utilizados para cada patio de emergencia */
   private HashMap<MetaPatio, ArrayList<EnumTipoBaliza>> mapaTiposEmergenciaPorPatio;

   private MetaCorreia metaCorreia;

   
   /** a campanha atual desta usina */
   private List<Campanha> listaCampanhas;
   
   /**
    * Construtor Padrao
    */
   public MetaUsina()
   {
   }


   @Id
   @GeneratedValue(strategy = GenerationType.AUTO, generator = "metusina_seq")
   @SequenceGenerator(name = "metusina_seq", sequenceName = "seqmetusina")
   public Long getIdUsina()
   {
      return idUsina;
   }

   @Column(nullable = false, length = 60)
   public String getNomeUsina()
   {
      return nomeUsina;
   }


   @Column(nullable = false)
   public Long getCodigoFaseProcessoUsina()
   {
      return codigoFaseProcessoUsina;
   }

   @Column(nullable = false)
   public Long getCdItemControleUsina()
   {
      return cdItemControleUsina;
   }

   @Column(nullable = false)
   public String getCdTipoProcessoUsina()
   {
      return cdTipoProcessoUsina;
   }

   @Column(nullable = false)
   public String getCdAreaRespEdUsina()
   {
      return cdAreaRespEdUsina;
   }


   @OneToOne
   @ForeignKey(name = "fk_usina_patio")
   @JoinColumn(name = "id_Patio_pellet")
   public MetaPatio getMetaPatioExpurgoPellet()
   {
      return metaPatioExpurgoPellet;
   }

   
   public void setCodigoFaseProcessoUsina(Long codigoFaseProcessoUsina)
   {
      this.codigoFaseProcessoUsina = codigoFaseProcessoUsina;
   }

   public void setCdItemControleUsina(Long cdItemControleUsina)
   {
      this.cdItemControleUsina = cdItemControleUsina;
   }

   public void setCdTipoProcessoUsina(String cdTipoProcessoUsina)
   {
      this.cdTipoProcessoUsina = cdTipoProcessoUsina;
   }

   public void setCdAreaRespEdUsina(String cdAreaRespEdUsina)
   {
      this.cdAreaRespEdUsina = cdAreaRespEdUsina;
   }

   public void setIdUsina(Long idUsina)
   {
      this.idUsina = idUsina;
   }

   public void setNomeUsina(String nomeUsina)
   {
      this.nomeUsina = nomeUsina;
   }

   
   public void setMetaPatioExpurgoPellet(MetaPatio patioExpurgoPellet)
   {
      this.metaPatioExpurgoPellet = patioExpurgoPellet;
   }
   
    @Override
	@OneToMany(fetch = FetchType.LAZY,mappedBy="metaUsina")
	@Fetch(FetchMode.SELECT)
	//@Cascade(value={CascadeType.ALL,CascadeType.SAVE_UPDATE})
	// @OrderBy("dtInicio asc")
	public List<Usina> getListaStatus() {
		return super.getListaStatus();
	}
    
    @Override
	public void incluirNovoStatus(Usina novoStatus, Date horaStatus) {
    	super.incluirNovoStatus(novoStatus, horaStatus);
    	novoStatus.setMetaUsina(this);
    }

    @ManyToOne
    @JoinColumn(name = "ID_PLANTA", nullable = false, insertable = true)
    @ForeignKey(name = "fk_meta_usina_planta")
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
        result = prime * result + ((cdAreaRespEdUsina == null) ? 0 : cdAreaRespEdUsina.hashCode());
        result = prime * result + ((cdItemControleUsina == null) ? 0 : cdItemControleUsina.hashCode());
        result = prime * result + ((cdTipoProcessoUsina == null) ? 0 : cdTipoProcessoUsina.hashCode());
        result = prime * result + ((codigoFaseProcessoUsina == null) ? 0 : codigoFaseProcessoUsina.hashCode());
        result = prime * result + ((idUsina == null) ? 0 : idUsina.hashCode());
        result = prime * result + ((nomeUsina == null) ? 0 : nomeUsina.hashCode());
        result = prime * result + ((planta == null) ? 0 : planta.hashCode());
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
        MetaUsina other = (MetaUsina) obj;
        if (cdAreaRespEdUsina == null) {
            if (other.cdAreaRespEdUsina != null)
                return false;
        } else if (!cdAreaRespEdUsina.equals(other.cdAreaRespEdUsina))
            return false;
        if (cdItemControleUsina == null) {
            if (other.cdItemControleUsina != null)
                return false;
        } else if (!cdItemControleUsina.equals(other.cdItemControleUsina))
            return false;
        if (cdTipoProcessoUsina == null) {
            if (other.cdTipoProcessoUsina != null)
                return false;
        } else if (!cdTipoProcessoUsina.equals(other.cdTipoProcessoUsina))
            return false;
        if (codigoFaseProcessoUsina == null) {
            if (other.codigoFaseProcessoUsina != null)
                return false;
        } else if (!codigoFaseProcessoUsina.equals(other.codigoFaseProcessoUsina))
            return false;
        if (idUsina == null) {
            if (other.idUsina != null)
                return false;
        } else if (!idUsina.equals(other.idUsina))
            return false;
        if (nomeUsina == null) {
            if (other.nomeUsina != null)
                return false;
        } else if (!nomeUsina.equals(other.nomeUsina))
            return false;
        if (planta == null) {
            if (other.planta != null)
                return false;
        } else if (!planta.equals(other.planta))
            return false;
        return true;
    }

    public void addCampanha(Campanha campanha) {
        if (!getListaCampanhas().contains(campanha)) {
            getListaCampanhas().add(campanha);
            campanha.setMetaUsina(this);            
        }
    }



    @OneToMany(fetch = FetchType.LAZY,mappedBy="metaUsina")
    @Fetch(FetchMode.SELECT)
    //@Cascade(CascadeType.ALL)    
    public List<Campanha> getListaCampanhas() {
        if (listaCampanhas == null) {
        	listaCampanhas = new ArrayList<Campanha>();
        }
    	return listaCampanhas;
    }


    public void setListaCampanhas(List<Campanha> listaMetaCampanhas) {
        this.listaCampanhas = listaMetaCampanhas;
    }

    @Transient
    public Campanha getCampanhaAtual(Date horaExecucao)
    {
       Campanha result = null;
       Collections.sort(getListaCampanhas(),Campanha.comparadorCampanha);
       List<Campanha> campanhas =  getListaCampanhas();
       
       for (Campanha campanha : campanhas ) {           
          /***
           *  TODO IMPLEMENTAR REGRA DE DATA
           * 
           */
           if (horaExecucao.getTime() >= campanha.getDataInicial().getTime() &&  horaExecucao.getTime() <= campanha.getDataFinal().getTime()) {
               result = campanha;
               break;
           }
       }       
       return result;
    }

    @Transient
    public List<Campanha> getCampanhas(Date horaExecucao)
    {       
       Collections.sort(getListaCampanhas(),Campanha.comparadorCampanha);
       List<Campanha> campanhas =  getListaCampanhas();
       List<Campanha> itens =  new ArrayList<Campanha>();
       
       for (Campanha campanha : campanhas ) {           
          /***
           *  TODO IMPLEMENTAR REGRA DE DATA
           * 
           */
           if (campanha.getDataFinal().getTime() >= horaExecucao.getTime() &&
               campanha.getDataInicial().getTime() <= horaExecucao.getTime()) {               
              itens.add(campanha);               
           }
       }       
       return itens;
    }
    
    
    @Transient
    public List<Campanha> getCampanhasFuturas(Date horaExecucao)
    {       
       Collections.sort(getListaCampanhas(),Campanha.comparadorCampanha);
       List<Campanha> campanhas =  getListaCampanhas();
       List<Campanha> itens =  new ArrayList<Campanha>();
       
       for (Campanha campanha : campanhas ) {           
          /***
           *  TODO IMPLEMENTAR REGRA DE DATA
           * 
           */
           if (campanha.getDataFinal().getTime() >= horaExecucao.getTime()) { //&&
               //campanha.getDataInicial().getTime() <= horaExecucao.getTime()) {               
              itens.add(campanha);               
           }
       }       
       return itens;
    }
    
    
    /**
     * gerarAtividadeUsina
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 06/07/2010
     * @see
     * @param 
     * @return List<Usina>
     */
    public static List<Usina> gerarAtividadeUsina(AtividadeRecuperarEmpilharVO recuperacaoVO, Atividade atividade,Boolean finalizarAtividade,List<Correia> correiasDestino) {
        List<Usina> result = new ArrayList<Usina>();        
        Atividade atividadeAnterior = null;        
        
        /**
         *  2- aplica atividade nas usinas 
         */
        for (MetaUsina metaUsina : recuperacaoVO.getListaUsinas()) {
        	Usina usina = null;

        	if (finalizarAtividade) {
        		usina = metaUsina.clonarStatus(recuperacaoVO.getDataFim());	
        	} else {
        		usina = metaUsina.clonarStatus(recuperacaoVO.getDataInicio());
        	}
        	
            if (usina.getAtividade() != null && usina.getEstado().equals(EstadoMaquinaEnum.OPERACAO
                         )) {            
            	atividadeAnterior = usina.getAtividade();
            	atividade.setFinalizada(Boolean.TRUE);                
            }
            
            usina.setAtividade(atividade);
            
            // seta o estado da maquina
            if (finalizarAtividade) {
            	usina.setEstado(EstadoMaquinaEnum.OCIOSA);
            	usina.setAtividade(null);
            } else {
            	usina.setEstado(EstadoMaquinaEnum.OPERACAO);
            }
            if (correiasDestino != null)
            //gera atividade para a correia da maquina   
            correiasDestino.addAll(MetaCorreia.gerarAtividadeCorreia(usina,finalizarAtividade));
            // adicona na lista                                
            
            result.add(usina);
        }
    
        
        result.addAll(finalizarAtividadeUsina(recuperacaoVO, finalizarAtividade, 
				atividadeAnterior,correiasDestino));    
        return result;
    }


	/**
	 * finalizarAtividadeUsina
	 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
	 * @since 06/07/2010
	 * @see
	 * @param 
	 * @return List<Usina>
	 */
	public static List<Usina> finalizarAtividadeUsina(
			AtividadeRecuperarEmpilharVO recuperacaoVO,
			Boolean finalizarAtividade, 
			Atividade atividadeAnterior,List<Correia> correiasDestino) {
		List<Usina> result = new ArrayList<Usina>();
		
		if (atividadeAnterior != null && atividadeAnterior.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaUsinas() != null) { 
	        List<Usina> usinaAnteriores = atividadeAnterior.getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaUsinas();
	        List<MetaUsina> usinaParada = new ArrayList<MetaUsina>();        	        
	        for (Usina usina : usinaAnteriores) {
	        	if (!recuperacaoVO.getListaUsinas().contains(usina.getMetaUsina())) {
	        		if (usina.getEstado().equals(EstadoMaquinaEnum.OPERACAO))       		    
	        		   usinaParada.add(usina.getMetaUsina());	 
	           }            		
	    	}
	        
	        if (usinaParada.size() > 0) {
	
	        	for (MetaUsina metaUsina : usinaParada) {
	            	Usina usina = null;
	            	if (finalizarAtividade) {
	            		usina = metaUsina.clonarStatus(recuperacaoVO.getDataFim());	
	            	} else {
	            		usina = metaUsina.clonarStatus(recuperacaoVO.getDataInicio());
	            	}
	            	
	                usina.setAtividade(null);                
	                usina.setEstado(EstadoMaquinaEnum.OCIOSA);       	
	 
	                if (correiasDestino != null)
	                    //gera atividade para a correia da maquina   
	                    correiasDestino.addAll(MetaCorreia.gerarAtividadeCorreia(usina,finalizarAtividade));
	                   
	                
	                result.add(usina);	                
	            }
	        }
        }
		
		return result;
	}

    @Override
    public Usina clonarStatus(Date horario) {
        Usina result = null;
        result = super.clonarStatus(horario);
        result.setIdUsina(null);                        
        return result;
    }

    @ManyToMany(fetch = FetchType.LAZY)
   // @Cascade(CascadeType.ALL)
    @ForeignKey(name = "fk_usina_patioemer")
    @JoinTable(name = "Usina_PatioEmergencia", joinColumns = @JoinColumn(name = "idUsina"), inverseJoinColumns = @JoinColumn(name = "idPatio"))
    public List<MetaPatio> getListaMetaPatioEmergencia() {
        return listaMetaPatioEmergencia;
    }

    public void setListaMetaPatioEmergencia(List<MetaPatio> listaMetaPatioEmergencia) {
        this.listaMetaPatioEmergencia = listaMetaPatioEmergencia;
    }
    @Transient
	public HashMap<MetaPatio, ArrayList<EnumTipoBaliza>> getMapaTiposEmergenciaPorPatio() {
		return mapaTiposEmergenciaPorPatio;
	}

	public void setMapaTiposEmergenciaPorPatio(
			HashMap<MetaPatio, ArrayList<EnumTipoBaliza>> mapaTiposEmergenciaPorPatio) {
		this.mapaTiposEmergenciaPorPatio = mapaTiposEmergenciaPorPatio;
	}


   @OneToOne   
   @ForeignKey(name = "fk_usina_filtragem")
   @JoinColumn(name = "id_filtragem",nullable=false)
   public MetaFiltragem getFiltragemOrigem() {
        return filtragemOrigem;
   }


    public void setFiltragemOrigem(MetaFiltragem filtragemOrigem) {
        this.filtragemOrigem = filtragemOrigem;
    }


    @ManyToOne    
    public MetaCorreia getMetaCorreia() {
        return metaCorreia;
    }


    public void setMetaCorreia(MetaCorreia metaCorreia) {
        this.metaCorreia = metaCorreia;
    }
}