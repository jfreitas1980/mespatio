
package com.hdntec.gestao.domain.planta.entity.status;

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
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.ForeignKey;

import com.hdntec.gestao.domain.StatusEntity;
import com.hdntec.gestao.domain.planta.entity.MetaCorreia;
import com.hdntec.gestao.domain.planta.entity.MetaFiltragem;
import com.hdntec.gestao.domain.planta.entity.MetaPatio;
import com.hdntec.gestao.domain.planta.entity.MetaPier;
import com.hdntec.gestao.domain.planta.entity.MetaUsina;


/**
 * A planta Ã© a agregaÃ§Ã£o de todos os elementos do pÃ¡tio, Ã© o espaÃ§o fÃ­sico e o conceito de agrupamento de todos os itens de planta.
 * <p>
 * Persite. - Tamanho: - Volume: poucos registros. -PerÃ­odo de PersistÃªncia: cadastro de planta. -Freq. Update: raro. -Confiabilidade: deve ser confiÃ¡vel.
 * 
 * @author andre
 * 
 */
@Entity
public class Planta extends StatusEntity<Planta> {

    /** SerializaÃ§Ã£o do Objeto */
    private static final long serialVersionUID = -6131488519710280744L;

    /** Identificador da planta */
    private Long idPlanta;

    /** Nome da Planta */
    private String nomePlanta;

    /** a lista de piers da planta */
    private List<MetaPier> listaMetaPiers;

    /** a lista de patios da planta */
    private List<MetaPatio> listaMetaPatios;

    /** a lista de correias da planta */
    private List<MetaCorreia> listaMetaCorreias;

    /** a lista de usinas da planta */
    private List<MetaUsina> listaMetaUsinas;
    
    private List<MetaFiltragem> listaMetaFiltragem;

    
    /** lista de anotacoes que estÃ£o sendo inseridas no DSP */
    private List<Anotacao> listaDeAnotacoes;

    /**
     * ContrutorPadrao
     */
    public Planta() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "planta_seq")
    @SequenceGenerator(name = "planta_seq", sequenceName = "seqplanta")
    public Long getIdPlanta() {
        return idPlanta;
    }

    public void setIdPlanta(Long idPlanta) {
        this.idPlanta = idPlanta;
    }

    //TODO Darley SA11079 Otimizando colocando LAZY
    @OneToMany(fetch = FetchType.LAZY,mappedBy="planta")
    @Fetch(FetchMode.SELECT)
    //@Cascade(CascadeType.ALL)
    @OrderBy("numero")
    public List<MetaCorreia> getListaMetaCorreias() {
        if (listaMetaCorreias == null) {
            listaMetaCorreias = new ArrayList<MetaCorreia>();
        }
        return listaMetaCorreias;
    }

    @Transient
    public List<Correia> getListaCorreias(Date dataAtual) {
        ArrayList<Correia> listaCorreias = new ArrayList<Correia>();
        for (MetaCorreia mc : getListaMetaCorreias()) {
            listaCorreias.add(mc.retornaStatusHorario(dataAtual));
        }

        return listaCorreias;
    }

    public void setListaMetaCorreias(List<MetaCorreia> listaCorreias) {
        this.listaMetaCorreias = listaCorreias;
    }

    public void setListaCorreias(List<Correia> listaCorreias, Date dataAtual) throws Exception {
        for (Correia correia : listaCorreias) {
            correia.getMetaCorreia().incluirNovoStatus(correia, dataAtual);
        }
    }

    public void setListaUsinas(List<Usina> listaUsinas, Date dataAtual) throws Exception {
        for (Usina usina : listaUsinas) {
            usina.getMetaUsina().incluirNovoStatus(usina, dataAtual);
        }
    }

    //TODO Darley SA11079 Otimizando colocando LAZY
    @OneToMany(fetch = FetchType.LAZY,mappedBy="planta")
    @Fetch(FetchMode.SELECT)
    //@Cascade(CascadeType.ALL)
    @OrderBy("numero")
    public List<MetaPatio> getListaMetaPatios() {
        if (listaMetaPatios == null) {
            listaMetaPatios = new ArrayList<MetaPatio>();
        }
        return listaMetaPatios;
    }

    @Transient
    public List<Patio> getListaPatios(Date dataAtual) {
        ArrayList<Patio> listaPatios = new ArrayList<Patio>();
        for (MetaPatio mp : getListaMetaPatios()) {
            listaPatios.add(mp.retornaStatusHorario(dataAtual));
        }

        return listaPatios;
    }

    public void setListaMetaPatios(List<MetaPatio> listaMetaPatios) {
        this.listaMetaPatios = listaMetaPatios;
    }

    public void setListaPatios(List<Patio> listaPatios, Date dataAtual) throws Exception {
        for (Patio patio : listaPatios) {
            patio.getMetaPatio().incluirNovoStatus(patio, dataAtual);
        }
    }

    //TODO Darley SA11079 Otimizando colocando LAZY
    @OneToMany(fetch = FetchType.LAZY,mappedBy="planta")
    @Fetch(FetchMode.SELECT)   
    //@Cascade(CascadeType.ALL)
    @OrderBy("nomePier")
    public List<MetaPier> getListaMetaPiers() {
        if (listaMetaPiers == null) {
            listaMetaPiers = new ArrayList<MetaPier>();
        }
        return listaMetaPiers;
    }

    @Transient
    public List<Pier> getListaPiers(Date dataAtual) {
        ArrayList<Pier> listaPiers = new ArrayList<Pier>();
        for (MetaPier mp : getListaMetaPiers()) {
            listaPiers.add(mp.retornaStatusHorario(dataAtual));
        }

        return listaPiers;
    }

    public void setListaMetaPiers(List<MetaPier> listaPiers) {
        this.listaMetaPiers = listaPiers;
    }

    //TODO Darley SA11079 Otimizando colocando LAZY
    @OneToMany(fetch = FetchType.LAZY,mappedBy="planta")
    @Fetch(FetchMode.SELECT)
    //@Cascade(CascadeType.ALL)
    @OrderBy("nomeUsina")
    public List<MetaUsina> getListaMetaUsinas() {
        return listaMetaUsinas;
    }

    @Transient
    public List<Usina> getListaUsinas(Date dataAtual) {
        ArrayList<Usina> listaUsinas = new ArrayList<Usina>();
        for (MetaUsina mu : getListaMetaUsinas()) {
            listaUsinas.add(mu.retornaStatusHorario(dataAtual));
        }

        return listaUsinas;
    }

    public void setListaMetaUsinas(List<MetaUsina> listaUsinas) {
        this.listaMetaUsinas = listaUsinas;
    }

    @Column(nullable = false, length = 60)
    public String getNomePlanta() {
        return nomePlanta;
    }

    public void setNomePlanta(String nomePlanta) {
        this.nomePlanta = nomePlanta;
    }

    //TODO Darley SA11079 Otimizando colocando LAZY
    @OneToMany(fetch = FetchType.LAZY, cascade = {
                    javax.persistence.CascadeType.MERGE, javax.persistence.CascadeType.PERSIST})
    @Fetch(FetchMode.SELECT)
    @Cascade(value = {
                    CascadeType.SAVE_UPDATE,CascadeType.DELETE,CascadeType.DELETE_ORPHAN})
    @ForeignKey(name = "fk_planta_anotacao", inverseName = "fk_anotacao_planta")
    @JoinTable(name = "Planta_Anotacao", joinColumns = @JoinColumn(name = "idPlanta"), inverseJoinColumns = @JoinColumn(name = "idAnotacao"))
    public List<Anotacao> getListaDeAnotacoes() {
        return listaDeAnotacoes;
    }

    public void setListaDeAnotacoes(List<Anotacao> listaDeAnotacoes) {
        this.listaDeAnotacoes = listaDeAnotacoes;
    }

    @Transient
    public List<Anotacao> getAnotacoes(Date horaExecucao)
    {       
       Collections.sort(this.getListaDeAnotacoes(),Anotacao.comparadorAnotacao);
       List<Anotacao> anotacoes =  this.getListaDeAnotacoes();
       List<Anotacao> itens =  new ArrayList<Anotacao>();
       
       for (Anotacao anotacao : anotacoes ) {           
          /***
           *  TODO IMPLEMENTAR REGRA DE DATA
           * 
           */
           if (anotacao.getDataFinal().getTime() >= horaExecucao.getTime() &&
               anotacao.getDataInicial().getTime() <= horaExecucao.getTime()) {               
              itens.add(anotacao);               
           }
       }       
       return itens;
    }
    

    
    @Transient
    public List<Berco> getListaBercos(Date dataAtual) {
        List<Berco> result = new ArrayList<Berco>();
        for (MetaPier metaPier : getListaMetaPiers())
        {
            Pier pier = metaPier.retornaStatusHorario(dataAtual);            
            result.addAll(pier.getListaDeBercosDeAtracacao(dataAtual));              
        }
        return result;
    }

    @Transient
    public List<MaquinaDoPatio> getListaMaquinasDoPatio(Date data) {
        List<MaquinaDoPatio> result = new ArrayList<MaquinaDoPatio>();
         for (Patio patio : getListaPatios(data))
         {
            result.addAll(patio.getListaDeMaquinasDoPatio(data));
         }

         for (Correia correia : getListaCorreias(data))
         {
            result.addAll(correia.getListaDeMaquinas(data));
         }
        return result;
    }
    
    public void addMetaCorreia(MetaCorreia correia)
    {
       if (getListaMetaCorreias() == null)
       {
          setListaMetaCorreias(new ArrayList<MetaCorreia>());
       }

       if (!getListaMetaCorreias().contains(correia))
       {
           getListaMetaCorreias().add(correia);
           correia.setPlanta(this);
       }
    }

    public void addMetaCorreia(List<MetaCorreia> correias)
    {
       if (correias != null)
       {
          for (MetaCorreia correia : correias)
          {
              addMetaCorreia(correia);
          }
       }
    }


    public void addMetaPatio(MetaPatio patio)
    {
       if (getListaMetaPatios() == null)
       {
          setListaMetaPatios(new ArrayList<MetaPatio>());
       }

       if (!getListaMetaPatios().contains(patio))
       {
           getListaMetaPatios().add(patio);
           patio.setPlanta(this);
       }
    }

    public void addMetaPatio(List<MetaPatio> patios)
    {
       if (patios != null)
       {
          for (MetaPatio patio : patios)
          {
              addMetaPatio(patio);
          }
       }
    }


    public void addMetaUsina(MetaUsina usina)
    {
       if (getListaMetaUsinas() == null)
       {
          setListaMetaUsinas(new ArrayList<MetaUsina>());
       }

       if (!getListaMetaUsinas().contains(usina))
       {
           getListaMetaUsinas().add(usina);
           usina.setPlanta(this);
       }
    }

    public void addMetaUsina(List<MetaUsina> usinas)
    {
       if (usinas != null)
       {
          for (MetaUsina usina : usinas)
          {
              addMetaUsina(usina);
          }
       }
    }


    public void addMetaPier(MetaPier pier)
    {
       if (getListaMetaPiers() == null)
       {
          setListaMetaPiers(new ArrayList<MetaPier>());
       }

       if (!getListaMetaPiers().contains(pier))
       {
           getListaMetaPiers().add(pier);
           pier.setPlanta(this);
       }
    }

    public void addMetaPier(List<MetaPier> piers)
    {
       if (piers != null)
       {
          for (MetaPier pier : piers)
          {
              addMetaPier(pier);
          }
       }
    }

    
    
    
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((idPlanta == null) ? 0 : idPlanta.hashCode());
        result = prime * result + ((nomePlanta == null) ? 0 : nomePlanta.hashCode());
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
        Planta other = (Planta) obj;
        if (idPlanta == null) {
            if (other.idPlanta != null)
                return false;
        } else if (!idPlanta.equals(other.idPlanta))
            return false;
        if (nomePlanta == null) {
            if (other.nomePlanta != null)
                return false;
        } else if (!nomePlanta.equals(other.nomePlanta))
            return false;
        return true;
    }
    
    @OneToMany(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    //@Cascade(CascadeType.ALL)
    @OrderBy("nomeFiltragem")
    public List<MetaFiltragem> getListaMetaFiltragem() {
        return listaMetaFiltragem;
    }

    public void setListaMetaFiltragem(List<MetaFiltragem> listaMetaFiltragem) {
        this.listaMetaFiltragem = listaMetaFiltragem;
    }

    
    public void addMetaFiltragem(MetaFiltragem usina)
    {
       if (getListaMetaFiltragem() == null)
       {
          setListaMetaFiltragem(new ArrayList<MetaFiltragem>());
       }

       if (!getListaMetaFiltragem().contains(usina))
       {
           getListaMetaFiltragem().add(usina);
           usina.setPlanta(this);
       }
    }

    public void addMetaFiltragem(List<MetaFiltragem> usinas)
    {
       if (usinas != null)
       {
          for (MetaFiltragem usina : usinas)
          {
              addMetaFiltragem(usina);
          }
       }
    }

}
