
package com.hdntec.gestao.domain.plano.entity;

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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.ForeignKey;

import com.hdntec.gestao.domain.StatusEntity;
import com.hdntec.gestao.domain.navios.entity.MetaCarga;
import com.hdntec.gestao.domain.navios.entity.MetaNavio;
import com.hdntec.gestao.domain.navios.entity.status.Carga;
import com.hdntec.gestao.domain.navios.entity.status.Navio;
import com.hdntec.gestao.domain.plano.enums.SentidoEmpilhamentoRecuperacaoEnum;
import com.hdntec.gestao.domain.plano.enums.TipoMovimentacaoEnum;
import com.hdntec.gestao.domain.planta.entity.MetaBaliza;
import com.hdntec.gestao.domain.planta.entity.MetaCorreia;
import com.hdntec.gestao.domain.planta.entity.MetaFiltragem;
import com.hdntec.gestao.domain.planta.entity.MetaMaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.MetaUsina;
import com.hdntec.gestao.domain.planta.entity.status.AtividadeCampanha;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.Correia;
import com.hdntec.gestao.domain.planta.entity.status.Filtragem;
import com.hdntec.gestao.domain.planta.entity.status.MaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Pilha;
import com.hdntec.gestao.domain.planta.entity.status.Usina;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;


/**
 * Representa uma pilha virtual, um lugar de empilhamento ou recuperacao
 * 
 * @author andre
 * 
 */
@Entity
public class LugarEmpilhamentoRecuperacao extends StatusEntity<LugarEmpilhamentoRecuperacao> {

    /** Serialização */
    private static final long serialVersionUID = -4060095851777388634L;

    /** id da entidade */
    private Long idLugarEmpilhaRecupera;

    private Integer ordem;

    @Column(nullable=false)
	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}




	/** Sentido da atividade deste lugar de empilhamento ou recuperação */
    private SentidoEmpilhamentoRecuperacaoEnum sentido;

    /** Nome de porao que esta sendo utilizado na recuperacao */
    private String nomePorao;

    private TipoProduto tipoProduto;

    /**
     * a lista de balizas referentes a este lugar de empilhamento ou recuperação
     */
    private List<Baliza> listaDeBalizas;

    private List<Pilha> listaDePilhasEditadas;

    
    @Transient
    public List<Pilha> getListaDePilhasEditadas() {
		  if (listaDePilhasEditadas == null) {
			  listaDePilhasEditadas = new ArrayList<Pilha>();
		  }
    	return listaDePilhasEditadas;
	}


    
    
	/**
     * A máquina responsável pela atividade neste lugar de empilhamento ou
     * recuperação
     */
    private List<MaquinaDoPatio> listaMaquinaDoPatio;

    private List<Correia> listaCorreias;

    /**
     * A máquina responsável pela atividade neste lugar de empilhamento ou
     * recuperação
     */
    private List<Carga> listaCargas;

    /** Referencia de campanha correspondente a esta atividade. */

    private List<AtividadeCampanha> listaAtividadeCampanhas;

    /** Referencia da usina correspondente a esta atividade. */

    private List<Usina> listaUsinas;

    /** Referencia da usina correspondente a esta atividade. */

    private List<Filtragem> listaFiltragens;

    /** Referencia do navio da atividade. */

    private Navio navio;

    /**
     * propriedade para determinar se este lugar de empilhamento ou recuperação
     * já foi executado ou ainda é para um plano futuro
     */
    private Boolean executado;

    /**
     * valor transiente na atividade, utilizada apenas para execucao do plano
     * virtual
     */
    private TipoMovimentacaoEnum tipoMovimento;

    /** A quantidade de produto deste lugar de empilhamento ou recuperação */
    private Double quantidade;

    /** Taxa de operacao da atividade neste lugar de empilhamento ou recuperação */
    private Double taxaDeOperacaoNaPilha;

    private Atividade atividade;

    /** o nome do lugar de empilhamento ou recuperação */
    private String nomeDoLugarEmpRec;

    public LugarEmpilhamentoRecuperacao() {
    }

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    public SentidoEmpilhamentoRecuperacaoEnum getSentido() {
        return sentido;
    }

    /**
     * @param sentido
     *            the sentido to set
     */
    public void setSentido(SentidoEmpilhamentoRecuperacaoEnum sentido) {
        this.sentido = sentido;
    }

    // TODO Darley SA11079 Otimizando colocando LAZY
    @OneToMany(fetch = FetchType.LAZY)     
    @Fetch(FetchMode.SELECT)  
/*    @Cascade(value = {
                    CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.PERSIST})
*/    @ForeignKey(name = "fk_lugar_maquina")
    @JoinTable(name = "Lugar_Maquina", joinColumns = @JoinColumn(name = "idLugarEmpilhaRecupera"), inverseJoinColumns = @JoinColumn(name = "idMaquina"))
    public List<MaquinaDoPatio> getListaMaquinaDoPatio() {
        return listaMaquinaDoPatio;
    }

    public void setListaMaquinaDoPatio(List<MaquinaDoPatio> listamaquinaDoPatio) {
        this.listaMaquinaDoPatio = listamaquinaDoPatio;
    }

    // TODO Darley SA11079 Otimizando colocando LAZY
    @OneToMany(fetch = FetchType.LAZY)
    //@Cascade(value = {
      //              CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.PERSIST})
    @ForeignKey(name = "fk_lugar_baliza")
    @JoinTable(name = "Lugar_Baliza", joinColumns = @JoinColumn(name = "idLugarEmpilhaRecupera"), inverseJoinColumns = @JoinColumn(name = "idBaliza"))
    public List<Baliza> getListaDeBalizas() {
        return listaDeBalizas;
    }

    @Column(nullable = true)
    public Double getTaxaDeOperacaoNaPilha() {
        return taxaDeOperacaoNaPilha;
    }

    public void setTaxaDeOperacaoNaPilha(Double taxaDeOperacaoNaPilha) {
        this.taxaDeOperacaoNaPilha = taxaDeOperacaoNaPilha;
    }

    @OneToMany(fetch = FetchType.LAZY)
    //@Cascade(value = {
      //              CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.PERSIST})
    @ForeignKey(name = "fk_lugar_carga")
    @JoinTable(name = "Lugar_Carga", joinColumns = @JoinColumn(name = "idLugarEmpilhaRecupera"), inverseJoinColumns = @JoinColumn(name = "idCarga"))
    public List<Carga> getListaCargas() {
        return listaCargas;
    }

    public void setListaCargas(List<Carga> listaCargas) {
        this.listaCargas = listaCargas;
    }

    @OneToMany(fetch = FetchType.LAZY)
    //@Cascade(value = {
      //              CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.PERSIST})
    @ForeignKey(name = "fk_lugar_campanha")
    @JoinTable(name = "Lugar_Campanha", joinColumns = @JoinColumn(name = "idLugarEmpilhaRecupera"), inverseJoinColumns = @JoinColumn(name = "idCampanha"))
    public List<AtividadeCampanha> getListaAtividadeCampanhas() {
        return listaAtividadeCampanhas;
    }

    public void setListaAtividadeCampanhas(List<AtividadeCampanha> listaCampanhas) {
        this.listaAtividadeCampanhas = listaCampanhas;
    }

    public void setListaDeBalizas(List<Baliza> listaDeBalizas) {
        this.listaDeBalizas = listaDeBalizas;
    }

    /**
     * @return the executado
     */
    @Column
    public Boolean getExecutado() {
        return executado;
    }

    /**
     * @param executado
     *            the executado to set
     */
    public void setExecutado(Boolean executado) {
        this.executado = executado;
    }

    /**
     * Este metodo serve apenas para saber qual o tipo de movimentacao que está
     * acontecendo no momento com esta plano virtual, este metodo nao deve ser
     * utilizado para nenhum outro procedimento no sistema, pois pode
     * repercurtir em reflexo na criacao do plano virtual.
     * 
     * @return
     */
    @Transient
    public TipoMovimentacaoEnum getTipoMovimento() {
        return tipoMovimento;
    }

    /**
     * Este metodo serve apenas para saber qual o tipo de movimentacao que está
     * acontecendo no momento com esta plano virtual, este metodo nao deve ser
     * utilizado para nenhum outro procedimento no sistema, pois pode
     * repercurtir em reflexo na criacao do plano virtual.
     * 
     * @param tipoMovimento
     *            the tipoMovimento to set
     */
    public void setTipoMovimento(TipoMovimentacaoEnum tipoMovimento) {
        this.tipoMovimento = tipoMovimento;
    }

    /**
     * @return the idLugarEmpilhaRecupera
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "locEmpRec_seq")
    @SequenceGenerator(name = "locEmpRec_seq", sequenceName = "seqlocEmpRec")
    public Long getIdLugarEmpilhaRecupera() {
        return idLugarEmpilhaRecupera;
    }

    /**
     * @param idLugarEmpilhaRecupera
     *            the idLugarEmpilhaRecupera to set
     */
    public void setIdLugarEmpilhaRecupera(Long idLugarEmpilhaRecupera) {
        this.idLugarEmpilhaRecupera = idLugarEmpilhaRecupera;
    }

    @Column(nullable = false, length = 60)
    public String getNomeDoLugarEmpRec() {
        return nomeDoLugarEmpRec;
    }

    public void setNomeDoLugarEmpRec(String nomeDoLugarEmpRec) {
        this.nomeDoLugarEmpRec = nomeDoLugarEmpRec;
    }

    @Column(nullable = false)
    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        return this.getNomeDoLugarEmpRec();
    }

    @ManyToOne
    //@Cascade(CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "idAtividade", nullable = false)
    public Atividade getAtividade() {
        return atividade;
    }

    public void setAtividade(Atividade atividade) {
        this.atividade = atividade;
    }

    public void addPilhaVirtual(Baliza addItem) {
        if (getListaDeBalizas() == null) {
            setListaDeBalizas(new ArrayList<Baliza>());
        }
        if (!getListaDeBalizas().contains(addItem)) {
            getListaDeBalizas().add(addItem);
        }
    }

    public void addPilhaVirtual(List<Baliza> itens) {
        if (itens != null) {
            for (Baliza baliza : itens) {
                addPilhaVirtual(baliza);
            }
        }
    }

    public void addMaquinaDoPatio(MaquinaDoPatio addItem) {
        if (getListaMaquinaDoPatio() == null) {
            setListaMaquinaDoPatio(new ArrayList<MaquinaDoPatio>());
        }
        boolean adiciona = Boolean.TRUE;
        for (MaquinaDoPatio item : getListaMaquinaDoPatio()) {
            if (item.getMetaMaquina().equals(addItem.getMetaMaquina()))
                adiciona = Boolean.FALSE;
        }
        if (adiciona) {
            getListaMaquinaDoPatio().add(addItem);            
        }
    }

    public void addMaquinaDoPatio(List<MaquinaDoPatio> itens) {
        if (itens != null) {
            for (MaquinaDoPatio maquina : itens) {
                addMaquinaDoPatio(maquina);                
            }
        }
    }


    public void addPilhaEditada(Pilha addItem) {       
        
      if (!getListaDePilhasEditadas().contains(addItem)) {
    	  getListaDePilhasEditadas().add(addItem);                      
        }
    }

    public void addPilhaEditada(List<Pilha> itens) {
        if (itens != null) {
            for (Pilha pilha : itens) {
            	addPilhaEditada(pilha);                
            }
        }
    }

    
    @Transient
    public MaquinaDoPatio getMaquinaDoPatio() {
        // TODO Auto-generated method stub
        return getListaMaquinaDoPatio().get(0);
    }

    @Transient
    public MaquinaDoPatio getMaquinaEmpilhamentoEmergencia() {
        // TODO Auto-generated method stub
        return getListaMaquinaDoPatio().get(1);
    }

    public void setMaquinaEmpilhamentoEmergencia(MaquinaDoPatio selectedItem) {
        // TODO Auto-generated method stub
        addMaquinaDoPatio(selectedItem);
    }

    public void addBaliza(Baliza addItem) {
        if (getListaDeBalizas() == null) {
            setListaDeBalizas(new ArrayList<Baliza>());
        }
        boolean adiciona = Boolean.TRUE;
        for (Baliza item : getListaDeBalizas()) {
            if (item.getMetaBaliza().equals(addItem.getMetaBaliza()))
                adiciona = Boolean.FALSE;
        }
        if (adiciona) {
            getListaDeBalizas().add(addItem);
        }
    }

    public void addBaliza(List<Baliza> itens) {
        if (itens != null) {
            for (Baliza baliza : itens) {
                addBaliza(baliza);
            }
        }
    }

    public void addUsina(Usina addItem) {
        if (getListaUsinas() == null) {
            setListaUsinas(new ArrayList<Usina>());
        }
        if (!getListaUsinas().contains(addItem)) {
            getListaUsinas().add(addItem);
        }
    }

    public void addUsina(List<Usina> itens) {
        if (itens != null) {
            for (Usina usina : itens) {
                addUsina(usina);
            }
        }
    }

    public void addAtividadeCampanha(AtividadeCampanha addItem) {
        if (getListaAtividadeCampanhas() == null) {
            setListaAtividadeCampanhas(new ArrayList<AtividadeCampanha>());
        }
        if (!getListaAtividadeCampanhas().contains(addItem)) {
            getListaAtividadeCampanhas().add(addItem);
           // addItem.setLugarEmpilhamento(this);
        }
    }

    public void addAtividadeCampanha(List<AtividadeCampanha> itens) {
        if (itens != null) {
            for (AtividadeCampanha item : itens) {
                addAtividadeCampanha(item);
            }
        }
    }

    public void addCorreia(Correia addItem) {
        if (getListaCorreias() == null) {
            setListaCorreias(new ArrayList<Correia>());
        }
        if (!getListaCorreias().contains(addItem)) {
            getListaCorreias().add(addItem);
        }
    }

    public void addCorreia(List<Correia> itens) {
        if (itens != null) {
            for (Correia item : itens) {
                addCorreia(item);
            }
        }
    }

    public void addCarga(Carga addItem) {
        if (getListaCargas() == null) {
            setListaCargas(new ArrayList<Carga>());
        }
        if (!getListaCargas().contains(addItem)) {
            getListaCargas().add(addItem);
        }
    }

    @OneToMany(fetch = FetchType.LAZY)
    //@Cascade(CascadeType.ALL)
    @ForeignKey(name = "fk_lugar_correia")
    @JoinTable(name = "Lugar_Correia", joinColumns = @JoinColumn(name = "idLugarEmpilhaRecupera"), inverseJoinColumns = @JoinColumn(name = "idCorreia"))
    public List<Correia> getListaCorreias() {
        return listaCorreias;
    }

    public void setListaCorreias(List<Correia> listaCorreias) {
        this.listaCorreias = listaCorreias;
    }

    @Column(nullable = true)
    public String getNomePorao() {
        return nomePorao;
    }

    public void setNomePorao(String nomePorao) {
        this.nomePorao = nomePorao;
    }

    @OneToMany(fetch = FetchType.LAZY)
    //@Cascade(value = {
      //              CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.PERSIST})
    @ForeignKey(name = "fk_lugar_usina")
    @JoinTable(name = "Lugar_Usina", joinColumns = @JoinColumn(name = "idLugarEmpilhaRecupera"), inverseJoinColumns = @JoinColumn(name = "idUsina"))
    public List<Usina> getListaUsinas() {
        return listaUsinas;
    }

    public void setListaUsinas(List<Usina> listaUsinas) {
        this.listaUsinas = listaUsinas;
    }

    @Transient
    public Navio getNavio() {
        return navio;
    }

    public void setNavio(Navio navio) {
        this.navio = navio;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((executado == null) ? 0 : executado.hashCode());
        result = prime * result + ((idLugarEmpilhaRecupera == null) ? 0 : idLugarEmpilhaRecupera.hashCode());
        result = prime * result + ((nomePorao == null) ? 0 : nomePorao.hashCode());
        result = prime * result + ((sentido == null) ? 0 : sentido.hashCode());
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
        LugarEmpilhamentoRecuperacao other = (LugarEmpilhamentoRecuperacao) obj;
        if (executado == null) {
            if (other.executado != null)
                return false;
        } else if (!executado.equals(other.executado))
            return false;
        if (idLugarEmpilhaRecupera == null) {
            if (other.idLugarEmpilhaRecupera != null)
                return false;
        } else if (!idLugarEmpilhaRecupera.equals(other.idLugarEmpilhaRecupera))
            return false;
        if (nomePorao == null) {
            if (other.nomePorao != null)
                return false;
        } else if (!nomePorao.equals(other.nomePorao))
            return false;
        if (sentido == null) {
            if (other.sentido != null)
                return false;
        } else if (!sentido.equals(other.sentido))
            return false;
        return true;
    }

    @OneToOne
    @ForeignKey(name = "fk_lugarEmpRec_tipoProduto")
    @JoinColumn(name = "id_TipoProduto", nullable = true)
    public TipoProduto getTipoProduto() {
        return tipoProduto;
    }

    public void setTipoProduto(TipoProduto tipoProduto) {
        this.tipoProduto = tipoProduto;
    }

    @OneToMany(fetch = FetchType.LAZY)
    //@Cascade(value = {
      //              CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.PERSIST})
    @ForeignKey(name = "fk_lugar_filtragem")
    @JoinTable(name = "Lugar_Filtragem", joinColumns = @JoinColumn(name = "idLugarEmpilhaRecupera"), inverseJoinColumns = @JoinColumn(name = "idFiltragem"))
    public List<Filtragem> getListaFiltragens() {
        return listaFiltragens;
    }

    public void setListaFiltragens(List<Filtragem> listaFiltragens) {
        this.listaFiltragens = listaFiltragens;
    }

    public void addFiltragem(Filtragem addItem) {
        if (getListaFiltragens() == null) {
            setListaFiltragens(new ArrayList<Filtragem>());
        }
        boolean adiciona = Boolean.TRUE;
        for (Filtragem item : getListaFiltragens()) {
            if (item.getMetaFiltragem().equals(addItem.getMetaFiltragem()))
                adiciona = Boolean.FALSE;
        }
        if (adiciona) {
            getListaFiltragens().add(addItem);
        }
    }

    public void addFiltragem(List<Filtragem> itens) {
        if (itens != null) {
            for (Filtragem maquina : itens) {
                addFiltragem(maquina);
            }
        }
    }

    /**
     * limparStatus
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 23/09/2010
     * @see
     * @param 
     * @return void
     */
    public void limparStatus() {
        Date dataAtividade = this.getAtividade().getDtInicio(); 
        
        if (this.getAtividade().getDtFim() != null) {
            dataAtividade = this.getAtividade().getDtFim();
        }    
        
        if (this.getListaCargas() != null) {
            for (Carga c : this.getListaCargas()) {
                MetaCarga metaCarga = c.getMetaCarga();
                metaCarga.getListaStatus().remove(c);
            }
            
        }
        // correias
        if (this.getListaCorreias() != null) {
            for (Correia c : this.getListaCorreias()) {
                MetaCorreia metaCorreia = c.getMetaCorreia();
                metaCorreia.getListaStatus().remove(c);
            }
         
        }
        // balizas
        if (this.getListaDeBalizas() != null) {
            for (Baliza c : this.getListaDeBalizas()) {                                     
                MetaBaliza metaBaliza = c.getMetaBaliza();
                metaBaliza.getListaStatus().remove(c);
            }         
        }

        // filtragens
        if (this.getListaFiltragens() != null) {
            for (Filtragem c : this.getListaFiltragens()) {
                MetaFiltragem metaFiltragem = c.getMetaFiltragem();
                metaFiltragem.getListaStatus().remove(c);
            }
        }

        // maquinas
        if (this.getListaMaquinaDoPatio() != null) {
            for (MaquinaDoPatio c : this.getListaMaquinaDoPatio()) {
                MetaMaquinaDoPatio metaMaquina = c.getMetaMaquina();
                metaMaquina.getListaStatus().remove(c);
            }
            
        }
        // usina
        if (this.getListaUsinas() != null) {
            for (Usina c : this.getListaUsinas()) {
                MetaUsina metaUsina = c.getMetaUsina();
                metaUsina.getListaStatus().remove(c);
            }
        }
        
     // navio
        if (this.getNavio() != null) {            
                MetaNavio metaNavio = this.getNavio().getMetaNavio();
                metaNavio.getListaStatus().remove(this.getNavio());           
        }
        
        
    }

}
