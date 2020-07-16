
package com.hdntec.gestao.domain.plano.entity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.ForeignKey;

import com.hdntec.gestao.domain.ComparadorLugarEmpilhamento;
import com.hdntec.gestao.domain.StatusEntity;
import com.hdntec.gestao.domain.navios.entity.status.Carga;
import com.hdntec.gestao.domain.plano.enums.TipoAtividadeEnum;
import com.hdntec.gestao.domain.planta.entity.status.AtividadeCampanha;
import com.hdntec.gestao.domain.planta.entity.status.Pilha;
import com.hdntec.gestao.domain.produto.entity.Amostra;


/**
 * Esta classe representa a entidade de atividade no banco, que pode ser uma atividade
 * do tipo : Recuperacao, Empilhamento, Chegada de Navio, Embarque de Navio, Mudanca
 * Campanha, Resultado de Amostragem, Saida de Navio.
 * @author Ricardo
 * @author andre(LT)
 */
@Entity
public class Atividade extends StatusEntity<Atividade> {

	private static ComparadorLugarEmpilhamento<LugarEmpilhamentoRecuperacao> comparadorStatus = new ComparadorLugarEmpilhamento<LugarEmpilhamentoRecuperacao>();

	/** serialização do objeto*/
    private static final long serialVersionUID = -454758014720785087L;

    private Boolean updated;
    
    @Transient
    public Boolean getUpdated() {
    	if (updated == null) updated = new Boolean(Boolean.FALSE);
		return updated;
	}

	public void setUpdated(Boolean updated) {
		this.updated = updated;
	}

	/** id da entidade*/
    private Long id;
    
    /** o tipo desta atividade */
    private TipoAtividadeEnum tipoAtividade;

    /** lugar de empilhamento da atividade */
    private List<LugarEmpilhamentoRecuperacao> listaDeLugaresDeEmpilhamentoRecuperacao;

    /** lista de informacoes para o relatorio de deslocamento de maquinas */
    private List<AtividadeRelatorio> listaDeAtividadesRelatorio;

    /** indica que essa atividade ja teve sua quantidade atualizada pelo MES */
    private Boolean quantidadeAtualizadaPeloMES = Boolean.FALSE;

    private Boolean finalizada = Boolean.FALSE;
    
    private MovimentacaoNavio movimentacaoNavio;

    private Atividade atividadeAnterior;
    
    public Atividade() {
    }

    @Override
    public String toString() {
        return tipoAtividade.toString();
    }

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    public TipoAtividadeEnum getTipoAtividade() {
        return tipoAtividade;
    }

    public void setTipoAtividade(TipoAtividadeEnum tipoAtividade) {
        this.tipoAtividade = tipoAtividade;
    }

    /**
     * Id de persistencia no banco
     * @return
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "atividade_seq")
    @SequenceGenerator(name = "atividade_seq", sequenceName = "seqatividade")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the listaDeLugaresDeEmpilhamentoRecuperacao
     */
    //TODO Darley SA11079 Otimizando colocando LAZY
    @OneToMany(fetch = FetchType.LAZY,mappedBy="atividade")
    @Fetch(FetchMode.SUBSELECT)
//    @Cascade(value = {
  //      CascadeType.ALL})
    public List<LugarEmpilhamentoRecuperacao> getListaDeLugaresDeEmpilhamentoRecuperacao() {
        if (listaDeLugaresDeEmpilhamentoRecuperacao != null) {
        	ordernar();
        }
    	return listaDeLugaresDeEmpilhamentoRecuperacao;
    }

    /**
     * @param listaDeLugaresDeEmpilhamentoRecuperacao the listaDeLugaresDeEmpilhamentoRecuperacao to set
     */
    public void setListaDeLugaresDeEmpilhamentoRecuperacao(
                    List<LugarEmpilhamentoRecuperacao> listaDeLugaresDeEmpilhamentoRecuperacao) {
        this.listaDeLugaresDeEmpilhamentoRecuperacao = listaDeLugaresDeEmpilhamentoRecuperacao;
    }

    @Transient
    public List<AtividadeRelatorio> getListaDeAtividadesRelatorio() {
        return listaDeAtividadesRelatorio;
    }

    public void setListaDeAtividadesRelatorio(List<AtividadeRelatorio> listaDeAtividadesRelatorio) {
        this.listaDeAtividadesRelatorio = listaDeAtividadesRelatorio;
    }

    public void addAtividadeRelatorio(AtividadeRelatorio relatorio) {
        if (listaDeAtividadesRelatorio == null) {
            listaDeAtividadesRelatorio = new ArrayList<AtividadeRelatorio>();
        }
        listaDeAtividadesRelatorio.add(relatorio);
    }

    @Column(nullable = false)
    public Boolean getQuantidadeAtualizadaPeloMES() {
        return quantidadeAtualizadaPeloMES;
    }

    /**
     * @param quantidadeAtualizadaPeloMES the quantidadeAtualizadaPeloMES to set
     */
    public void setQuantidadeAtualizadaPeloMES(Boolean quantidadeAtualizadaPeloMES) {
        this.quantidadeAtualizadaPeloMES = quantidadeAtualizadaPeloMES;
    }

    public void addLugarEmpilhamento(LugarEmpilhamentoRecuperacao item) {
        if (getListaDeLugaresDeEmpilhamentoRecuperacao() == null) {
            setListaDeLugaresDeEmpilhamentoRecuperacao(new ArrayList<LugarEmpilhamentoRecuperacao>());
        }
        //if (!getListaDeLugaresDeEmpilhamentoRecuperacao().contains(item)) {
            item.setAtividade(this);
            getListaDeLugaresDeEmpilhamentoRecuperacao().add(item);
        //}
    }

    public void addLugarEmpilhamento(List<LugarEmpilhamentoRecuperacao> itens) {
        if (itens != null) {
            for (LugarEmpilhamentoRecuperacao item : itens) {
                addLugarEmpilhamento(item);
            }
        }
    }

    @Transient
    @Deprecated /* Modificar para recuperação diretamente pelo lugar de empilhamento*/ 
    public Carga getCarga() 
    {
    	Carga  result = null;
    	if (listaDeLugaresDeEmpilhamentoRecuperacao.get(0).getListaCargas() != null && listaDeLugaresDeEmpilhamentoRecuperacao.get(0).getListaCargas().size() > 0) {    	    
    		result = listaDeLugaresDeEmpilhamentoRecuperacao.get(0).getListaCargas().get(0);
    	}
    	else if (listaDeLugaresDeEmpilhamentoRecuperacao.get(1).getListaCargas() != null && listaDeLugaresDeEmpilhamentoRecuperacao.get(1).getListaCargas().size() > 0) {
    		result = listaDeLugaresDeEmpilhamentoRecuperacao.get(1).getListaCargas().get(0);    		
    	}
    	return result;
    }

    /***
     * TODO METODO CRIADO APENAS PARA NAO DAR ERRO.... ESSE ATRIBUTO � UM PALIATIVO PARA STATUS DA CORREIA 
     * NOVA IMPLEMENTACAO DEVE RESOLVER ESSE PROBLEMA
     * @param false1
     */
    @Transient
    public void setSelecionarCorreiaC1(Boolean false1) {
        // TODO Auto-generated method stub

    }

    /***
     * TODO ADICIONAR ATIVIDADE DE CAMAPNHA NO LUGAR DE EMPILHAMENTO
     * 
     * @param item
     */
    @Transient
    public void addAtividadeCampanha(AtividadeCampanha item) {

    }

    @Transient
    public void addAtividadeCampanha(List<AtividadeCampanha> itens) {
        if (itens != null) {
            for (AtividadeCampanha item : itens) {
                addAtividadeCampanha(item);
            }
        }
    }

    /*
     * TODO IMPLEMENATR METODOS E ADICIONAR AO LUGHAR DE EMPILHAMENTO 
     * 
     * 
     */
    @Transient
    public void addCarga(Carga carga) {

    }

    @Transient
    public boolean isSelecionarCorreiaC1() {
        // TODO Auto-generated method stub
        return false;
    }

    @Transient
    public void setPilhaAtividade(Pilha pilhaOrigem) {
        // TODO Auto-generated method stub

    }

    //TODO Darley SA11079 Otimizando colocando LAZY
    @ManyToMany(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    //@Cascade(value = CascadeType.SAVE_UPDATE)
    @ForeignKey(name = "fk_ativ_resultAmostra", inverseName = "fk_resultAmostra_ativ")
    @JoinTable(name = "ativ_resultAmostra", joinColumns = @JoinColumn(name = "idAtiv"), inverseJoinColumns = @JoinColumn(name = "idResultAmostra"))
    public List<Amostra> getListaResultadoAmostras() {
        return listaResultadoAmostras;
    }

    public void setListaResultadoAmostras(List<Amostra> listaResultadoAmostras) {
        this.listaResultadoAmostras = listaResultadoAmostras;
    }

    /** Lista de amostras */
    private List<Amostra> listaResultadoAmostras;

    @Transient
    public List<AtividadeCampanha> getListaDeAtividadesCampanha() {
        // TODO Auto-generated method stub
        return getListaDeLugaresDeEmpilhamentoRecuperacao().get(0).getListaAtividadeCampanhas();
    }

    /** Navio utilizado na atividade de Atracar e Desatracar */
    @OneToOne(mappedBy = "atividade")
    //@ForeignKey(name = "fk_atividade_movnavio")
   // @Cascade(value = {
       // CascadeType.ALL})
    public MovimentacaoNavio getMovimentacaoNavio() {
        return movimentacaoNavio;
    }

    public void setMovimentacaoNavio(MovimentacaoNavio movimentacaoNavio) {
        this.movimentacaoNavio = movimentacaoNavio;
    }

    public void addMovimentacaoNavio(MovimentacaoNavio item) {        
            item.setAtividade(this);
            setMovimentacaoNavio(item);        
    }

    private static Date getHoraAtividade(Date data) {
        Calendar calEvento = Calendar.getInstance();
        calEvento.setTimeInMillis(data.getTime());
        calEvento.add(Calendar.MILLISECOND, 1);           
        return calEvento.getTime();           
    }
    
    /**
     * caso a primeira data seja igual a segunda adicina um milisegundo para diferencia-las
     * @param dataNovaSituacao
     * @param dataSituacaoOriginal
     * @return
     */
     public static Date verificaAtualizaDataAtividade(Date dataNovaSituacao, Date dataSituacaoOriginal){
        if(dataNovaSituacao.compareTo(dataSituacaoOriginal) <= 0){
            dataNovaSituacao = getHoraAtividade(dataSituacaoOriginal);            
        }        
        return dataNovaSituacao;
    }

    @Column(nullable = false)
    public Boolean getFinalizada() {
		return finalizada;
	}

	public void setFinalizada(Boolean finalizada) {
		this.finalizada = finalizada;
	}

	@OneToOne(fetch = FetchType.LAZY)
    @ForeignKey(name = "fk_atividade_atividadeAnterior")
   // @Cascade(value = {
     //   CascadeType.SAVE_UPDATE})
    @JoinColumn(nullable=true)    
	public Atividade getAtividadeAnterior() {
		return atividadeAnterior;
	}

	public void setAtividadeAnterior(Atividade atividadeAnterior) {
		this.atividadeAnterior = atividadeAnterior;
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((atividadeAnterior == null) ? 0 : atividadeAnterior.hashCode());
        result = prime * result + ((finalizada == null) ? 0 : finalizada.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((quantidadeAtualizadaPeloMES == null) ? 0 : quantidadeAtualizadaPeloMES.hashCode());
        result = prime * result + ((tipoAtividade == null) ? 0 : tipoAtividade.hashCode());
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
        Atividade other = (Atividade) obj;
        if (atividadeAnterior == null) {
            if (other.atividadeAnterior != null)
                return false;
        } else if (!atividadeAnterior.equals(other.atividadeAnterior))
            return false;
        if (finalizada == null) {
            if (other.finalizada != null)
                return false;
        } else if (!finalizada.equals(other.finalizada))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (quantidadeAtualizadaPeloMES == null) {
            if (other.quantidadeAtualizadaPeloMES != null)
                return false;
        } else if (!quantidadeAtualizadaPeloMES.equals(other.quantidadeAtualizadaPeloMES))
            return false;
        if (tipoAtividade == null) {
            if (other.tipoAtividade != null)
                return false;
        } else if (!tipoAtividade.equals(other.tipoAtividade))
            return false;
        return true;
    }

    
	
    public void ordernar() {
        // TODO Auto-generated method stub            	
    	Collections.sort(this.listaDeLugaresDeEmpilhamentoRecuperacao, comparadorStatus);
    }
    
    public Boolean atividadeDeNavio() {
    	Boolean result = Boolean.FALSE;
    	if  ( ( this.getTipoAtividade().equals(TipoAtividadeEnum.MOVIMENTAR_PILHA_EMERGENCIA)
			|| this.getTipoAtividade().equals(TipoAtividadeEnum.MOVIMENTAR_PILHA_PELLET_FEED)
			|| this.getTipoAtividade().equals(TipoAtividadeEnum.MOVIMENTAR_PILHA_PSM)) && this.getCarga() != null) {
    		result = Boolean.TRUE;
	     }
      return result;
    }
}
