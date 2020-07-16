package com.hdntec.gestao.domain.planta.entity;

import java.util.ArrayList;
import java.util.Collections;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.Campanha;
import com.hdntec.gestao.domain.planta.entity.status.Interdicao;
import com.hdntec.gestao.domain.planta.entity.status.Pier;
import com.hdntec.gestao.domain.planta.entity.status.Planta;
import com.hdntec.gestao.domain.produto.entity.Produto;
import com.hdntec.gestao.exceptions.ValidacaoCampoException;
import com.hdntec.gestao.util.PropertiesUtil;


/**
 * Pier é uma estrutura de berços para aportamento. Nesse, localiza-se a
 * carregadora de navios para a escoagem de produção.
 * <p>
 * Persite. - Tamanho: - Volume: poucos registros. -Período de Persistência:
 * cadastro de planta. - Freq. Update: raro. -Confiabilidade: deve ser
 * confiável.
 * 
 * @author andre
 * 
 */
@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"nomePier"}))
public class MetaPier extends AbstractMetaEntity<Pier> {

	/** Serialização do Objeto */
	private static final long serialVersionUID = -2394239744959694237L;

	private Long idPier;

    private Planta planta;
	
	/** Nome do Pier */
	private String nomePier;

	/** a lista de carregadoras de navio existentes neste pier */

	private List<MetaCarregadoraDeNavios> listaDeMetaCarregadoraDeNavios;

	/** a lista de berços existentes neste píer */
	private List<MetaBerco> listaDeMetaBercosDeAtracacao;

	   /** Lista com as manutencoes agendadas */
	   private List<Interdicao> listaInterdicao;

	
	/**
	 * Construtor Padrao.
	 */
	public MetaPier() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "metapier_seq")
	@SequenceGenerator(name = "metapier_seq", sequenceName = "seq_mpier")
	public Long getIdPier() {
		return idPier;
	}

	public void setIdPier(Long idPier) {
		this.idPier = idPier;
	}

	@Column(nullable = false, length = 60)
	public String getNomePier() {
		return nomePier;
	}

	public void setNomePier(String nomePier) {
		this.nomePier = nomePier;
	}

	@OneToMany(fetch = FetchType.LAZY,mappedBy="metaPier")
	@Fetch(FetchMode.SELECT)
	//@Cascade(CascadeType.ALL)
	public List<MetaCarregadoraDeNavios> getListaDeMetaCarregadoraDeNavios() {
		if (listaDeMetaCarregadoraDeNavios == null) {
			listaDeMetaCarregadoraDeNavios = new ArrayList<MetaCarregadoraDeNavios>();
		}
		return listaDeMetaCarregadoraDeNavios;
	}

	public void setListaDeMetaCarregadoraDeNavios(
			List<MetaCarregadoraDeNavios> listaDeCarregadoraDeNavios) {
		this.listaDeMetaCarregadoraDeNavios = listaDeCarregadoraDeNavios;
	}

	@OneToMany(fetch = FetchType.LAZY,mappedBy="metaPier")
	@Fetch(FetchMode.SELECT)
	//@Cascade(CascadeType.ALL)
	
	public List<MetaBerco> getListaDeMetaBercosDeAtracacao() {
		if (listaDeMetaBercosDeAtracacao == null) {
			listaDeMetaBercosDeAtracacao = new ArrayList<MetaBerco>();
		}

		return listaDeMetaBercosDeAtracacao;
	}

	public void setListaDeMetaBercosDeAtracacao(
			List<MetaBerco> listaDeBercosDeAtracacao) {
		this.listaDeMetaBercosDeAtracacao = listaDeBercosDeAtracacao;
	}

	@Override
	@OneToMany(fetch = FetchType.LAZY,mappedBy="metaPier")
	@Fetch(FetchMode.SELECT)
	//@Cascade(value={CascadeType.ALL,CascadeType.SAVE_UPDATE})  	
	public List<Pier> getListaStatus() {
		return super.getListaStatus();
	}
	
	@Override
	@Transient
	public void incluirNovoStatus(Pier novoStatus, Date horaStatus) {
		super.incluirNovoStatus(novoStatus, horaStatus);
		novoStatus.setMetaPier(this);
	}
	@Transient
    public void addMetaCarregadoraNavio(List<MetaCarregadoraDeNavios> itens) {
		for(MetaCarregadoraDeNavios item : itens) {
			addMetaCarregadoraNavio(item);
		}	
	}
	
	@Transient
	public void addMetaCarregadoraNavio(MetaCarregadoraDeNavios item) {
	   if (!getListaDeMetaCarregadoraDeNavios().contains(item)) {
		   getListaDeMetaCarregadoraDeNavios().add(item);
		   item.setMetaPier(this);
	   }
	}

	
	@Transient
    public void addMetaBerco(List<MetaBerco> itens) {
		for(MetaBerco item : itens) {
			addMetaBerco(item);
		}	
	}
	
	@Transient
	public void addMetaBerco(MetaBerco item) {
	   if (!getListaDeMetaBercosDeAtracacao().contains(item)) {
		   getListaDeMetaBercosDeAtracacao().add(item);
		   item.setMetaPier(this);
	   }
	}

	
	
	@ManyToOne
    @JoinColumn(name = "ID_PLANTA", nullable = false, insertable = true)
    @ForeignKey(name = "fk_meta_pier_planta")
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
        result = prime * result + ((idPier == null) ? 0 : idPier.hashCode());
        result = prime * result + ((nomePier == null) ? 0 : nomePier.hashCode());
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
        MetaPier other = (MetaPier) obj;
        if (idPier == null) {
            if (other.idPier != null)
                return false;
        } else if (!idPier.equals(other.idPier))
            return false;
        if (nomePier == null) {
            if (other.nomePier != null)
                return false;
        } else if (!nomePier.equals(other.nomePier))
            return false;
        if (planta == null) {
            if (other.planta != null)
                return false;
        } else if (!planta.equals(other.planta))
            return false;
        return true;
    }

    public void setListaInterdicao(List<Interdicao> listaInterdicao)
    {
       this.listaInterdicao = listaInterdicao;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = {
                    javax.persistence.CascadeType.MERGE, javax.persistence.CascadeType.PERSIST})
    @Fetch(FetchMode.SELECT)
    @Cascade(value = {
                    CascadeType.SAVE_UPDATE})
    @ForeignKey(name = "fk_pier_interdicao", inverseName = "fk_interdicao_pier")
    @JoinTable(name = "Pier_Interdicao", joinColumns = @JoinColumn(name = "idPier"), inverseJoinColumns = @JoinColumn(name = "idInterdicao"))
    public List<Interdicao> getListaInterdicao()
    {
       return listaInterdicao;
    }

    /**
     * addInterdicao
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 08/12/2010
     * @see
     * @param 
     * @return void
     */
    public void addInterdicao(Interdicao interdicao)
    {
       if (getListaInterdicao() == null)
       {
          setListaInterdicao(new ArrayList<Interdicao>());
       }

       if (!getListaInterdicao().contains(interdicao))
       {
          getListaInterdicao().add(interdicao);          
       }
    }

    /**
     * addInterdicao
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 08/12/2010
     * @see
     * @param 
     * @return void
     */
    public void addInterdicao(List<Interdicao> interdicoes)
    {
       if (interdicoes != null)
       {
          for (Interdicao interdicao : interdicoes)
          {
             addInterdicao(interdicao);
          }
       }
    }

    
    @Transient
    public Boolean pierInterditado(Date horaExecucao)
    {       
       Boolean result = Boolean.FALSE;
       Collections.sort(getListaInterdicao(),Interdicao.comparadorInterdicao);
       List<Interdicao> campanhas =  getListaInterdicao();
       
       for (Interdicao inter : campanhas ) {           
           if (inter.getDataFinal().getTime() >= horaExecucao.getTime() &&
                           inter.getDataInicial().getTime() <= horaExecucao.getTime()) {                                      
               result =  Boolean.TRUE;
               break;
           }
       }       
       return result;
    }
    
    @Transient
    public Boolean pierInterditado(List<Interdicao> lstInterdicao,Date horaExecucao)
    {       
       Boolean result = Boolean.FALSE;
       Collections.sort(lstInterdicao,Interdicao.comparadorInterdicao);
       for (Interdicao inter : lstInterdicao ) {           
           if (inter.getDataFinal().getTime() >= horaExecucao.getTime() &&
                           inter.getDataInicial().getTime() <= horaExecucao.getTime()) {                                      
               result =  Boolean.TRUE;
               break;
           }
       }       
       return result;
    }
    
    
    

    @Transient
    public List<Interdicao> getInterdicoesAbertas(Date horaExecucao)
    {       
       Collections.sort(getListaInterdicao(),Interdicao.comparadorInterdicao);
       List<Interdicao> campanhas =  getListaInterdicao();
       List<Interdicao> itens =  new ArrayList<Interdicao>();
       
       for (Interdicao inter : campanhas ) {           
          /***
           *  TODO IMPLEMENTAR REGRA DE DATA
           * 
           */
           if (inter.getDataFinal().getTime() >= horaExecucao.getTime() ) {               
              itens.add(inter);               
           }
       }       
       return itens;
    }
  }
