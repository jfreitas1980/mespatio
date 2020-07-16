package com.hdntec.gestao.domain.produto.entity;

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
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.ForeignKey;

import com.hdntec.gestao.domain.ComparadorStatusEntity;
import com.hdntec.gestao.domain.StatusEntity;
import com.hdntec.gestao.domain.plano.enums.TipoAtividadeEnum;


/**
 * Essa classe representa a rastreabilidade do material que está no produto.
 *
 * @author andre
 *
 */
@Entity
public class Rastreabilidade extends StatusEntity<Rastreabilidade>
{

   public static ComparadorStatusEntity<Rastreabilidade> comparadorStatusRastreabilidade = new ComparadorStatusEntity<Rastreabilidade>();
    
    /** serialização do objeto */
   private static final long serialVersionUID = 1981720323063769057L;

   /** identificador da rastreabilidade */
   private Long idRastreabilidade;

   /** nome do porao destino do material recuperado */
   private String nomePorao;

   /** a quantidade de material */
   private Double quantidade;

   /** o horário de início da entrada de material */
   private Date horarioInicioEntradaDeMaterial;

   /** o horário de fim da entrada de material */
   private Date horarioFimEntradaDeMaterial;

   /** o tipo desta atividade */
   private TipoAtividadeEnum tipoAtividade;

   /** o produto que contem esta rastreabilidade */
   private List<Produto> produtos;

   
   /** transiente para carregar a interface */
   private Long numeroRastreabilidade;

   /* ============================ DADOS DE RASTREABILIDADE DA BALIZA =========================== */
   private String nomeBaliza;

   private Integer numeroBaliza;

   private Date dtHoraIniFormBaliza;

   private Date dtHoraFimFormBaliza;

   private String nomePilhaBaliza;

   private String nomePatioBaliza;

   private Integer numPatioBaliza;

   private Double larguraBaliza;

   private Double capacMaxBaliza;

   /* ============================ DADOS DE RASTREABILIDADE DA USINA =========================== */
   private String nomeUsina;

   private Double taxaDeOperacaoUsina;

   private String nomeCampanhaUsina;

   private Date dataFimUsina;

   private Date dataInicioUsina;

   private TipoProduto tipoScreeningUsina;

   private TipoProduto tipoPelletUsina;

   private TipoProduto tipoProdutoUsina;

   /**
    * Construtor padrão
    */
   public Rastreabilidade()
   {
   }

   /**
    * @param balizaDeOrigem
    * @param horarioFimEntradaDeMaterial
    * @param horarioInicioEntradaDeMaterial
    * @param idRastreabilidade
    * @param quantidade
    * @param usinaDeOrigem
    */
   public Rastreabilidade(Double capacMaxBaliza, Date dataFimUsina,
                          Date dataInicioUsina, Date dtHoraFimFormBaliza,
                          Date dtHoraIniFormBaliza, Date horarioFimEntradaDeMaterial,
                          Date horarioInicioEntradaDeMaterial, Long idRastreabilidade,
                          Double larguraBaliza, String nomeBaliza, String nomeCampanhaUsina,
                          String nomePatioBaliza, String nomePilhaBaliza, String nomePorao,
                          String nomeUsina, Integer numPatioBaliza, Integer numeroBaliza,
                          Long numeroRastreabilidade, Double quantidade,
                          Double taxaDeOperacaoUsina, TipoAtividadeEnum tipoAtividade,
                          TipoProduto tipoPelletUsina, TipoProduto tipoProdutoUsina,
                          TipoProduto tipoScreeningUsina)
   {
      super();
      this.capacMaxBaliza = capacMaxBaliza;
      this.dataFimUsina = dataFimUsina;
      this.dataInicioUsina = dataInicioUsina;
      this.dtHoraFimFormBaliza = dtHoraFimFormBaliza;
      this.dtHoraIniFormBaliza = dtHoraIniFormBaliza;
      this.horarioFimEntradaDeMaterial = horarioFimEntradaDeMaterial;
      this.horarioInicioEntradaDeMaterial = horarioInicioEntradaDeMaterial;
      this.idRastreabilidade = idRastreabilidade;
      this.larguraBaliza = larguraBaliza;
      this.nomeBaliza = nomeBaliza;
      this.nomeCampanhaUsina = nomeCampanhaUsina;
      this.nomePatioBaliza = nomePatioBaliza;
      this.nomePilhaBaliza = nomePilhaBaliza;
      this.nomePorao = nomePorao;
      this.nomeUsina = nomeUsina;
      this.numPatioBaliza = numPatioBaliza;
      this.numeroBaliza = numeroBaliza;
      this.numeroRastreabilidade = numeroRastreabilidade;
      this.quantidade = quantidade;
      this.taxaDeOperacaoUsina = taxaDeOperacaoUsina;
      this.tipoAtividade = tipoAtividade;
      this.tipoPelletUsina = tipoPelletUsina;
      this.tipoProdutoUsina = tipoProdutoUsina;
      this.tipoScreeningUsina = tipoScreeningUsina;
   }

   
   /**
    * @return the idRastreabilidade
    */
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO, generator = "rastrea_seq")
   @SequenceGenerator(name = "rastrea_seq", sequenceName = "seqproduto")
   public Long getIdRastreabilidade()
   {
      return idRastreabilidade;
   }

   /**
    * @param idRastreabilidade
    *           the idRastreabilidade to set
    */
   public void setIdRastreabilidade(Long idRastreabilidade)
   {
      this.idRastreabilidade = idRastreabilidade;
   }

   /**
    * @return the quantidade
    */
   @Column(nullable = false)
   public Double getQuantidade()
   {
      return quantidade;
   }

   /**
    * @param quantidade
    *           the quantidade to set
    */
   public void setQuantidade(Double quantidade)
   {
      this.quantidade = quantidade;
   }

   /**
    * @return the horarioInicioEntradaDeMaterial
    */
   @Column(nullable = false)
   @Temporal(TemporalType.TIMESTAMP)
   public Date getHorarioInicioEntradaDeMaterial()
   {
      return horarioInicioEntradaDeMaterial;
   }

   /**
    * @param horarioInicioEntradaDeMaterial
    *           the horarioInicioEntradaDeMaterial to set
    */
   public void setHorarioInicioEntradaDeMaterial(Date horarioInicioEntradaDeMaterial)
   {
      this.horarioInicioEntradaDeMaterial = horarioInicioEntradaDeMaterial;
   }

   /**
    * @return the horarioFimEntradaDeMaterial
    */
   @Column(nullable = false)
   @Temporal(TemporalType.TIMESTAMP)
   public Date getHorarioFimEntradaDeMaterial()
   {
      return horarioFimEntradaDeMaterial;
   }

   /**
    * @param horarioFimEntradaDeMaterial
    *           the horarioFimEntradaDeMaterial to set
    */
   public void setHorarioFimEntradaDeMaterial(Date horarioFimEntradaDeMaterial)
   {
      this.horarioFimEntradaDeMaterial = horarioFimEntradaDeMaterial;
   }

   @Column(nullable = true)
   public String getNomePorao()
   {
      return nomePorao;
   }

   /**
    * @param nomePorao
    *           the nomePorao to set
    */
   public void setNomePorao(String nomePorao)
   {
      this.nomePorao = nomePorao;
   }

   @Column(nullable = false)
   @Enumerated(EnumType.STRING)
   public TipoAtividadeEnum getTipoAtividade()
   {
      return tipoAtividade;
   }

   public void setTipoAtividade(TipoAtividadeEnum tipoAtividade)
   {
      this.tipoAtividade = tipoAtividade;
   }

   @Override
   public String toString()
   {
      StringBuffer value = new StringBuffer();
	   if (nomeBaliza != null)
      {
          value.append("Baliza ").append(nomeBaliza).append(" do pátio ").append(nomePatioBaliza).append(" - ").append(quantidade).append(": ").append(horarioInicioEntradaDeMaterial.toString()).append(" - ").append(horarioFimEntradaDeMaterial.toString());       	  
      }
      else if (nomeUsina != null)
      {
    	  value.append("Usina ").append(nomeUsina).append(" - ").append(quantidade).append(": ").append(horarioInicioEntradaDeMaterial.toString()).append(" - ").append(horarioFimEntradaDeMaterial.toString());    	  
      }
      else
      {
         return super.toString();
      }
	 return value.toString();  
   }

   @Column(nullable = false)
   public Long getNumeroRastreabilidade()
   {      
       return numeroRastreabilidade;
   }

   public void setNumeroRastreabilidade(Long numeroRastreabilidade)
   {
      this.numeroRastreabilidade = numeroRastreabilidade;
   }

   @Column(nullable = true)
   public String getNomeBaliza()
   {
      return nomeBaliza;
   }

   public void setNomeBaliza(String nomeBaliza)
   {
      this.nomeBaliza = nomeBaliza;
   }

   @Column(nullable = true)
   @Temporal(TemporalType.TIMESTAMP)
   public Date getDtHoraIniFormBaliza()
   {
      return dtHoraIniFormBaliza;
   }

   public void setDtHoraIniFormBaliza(
         Date dtHoraIniFormBaliza)
   {
      this.dtHoraIniFormBaliza = dtHoraIniFormBaliza;
   }

   @Column(nullable = true)
   @Temporal(TemporalType.TIMESTAMP)
   public Date getDtHoraFimFormBaliza()
   {
      return dtHoraFimFormBaliza;
   }

   public void setDtHoraFimFormBaliza(Date dtHoraFimFormBaliza)
   {
      this.dtHoraFimFormBaliza = dtHoraFimFormBaliza;
   }

   @Column(nullable = true)
   public String getNomePilhaBaliza()
   {
      return nomePilhaBaliza;
   }

   public void setNomePilhaBaliza(String nomePilhaBaliza)
   {
      this.nomePilhaBaliza = nomePilhaBaliza;
   }

   @Column(nullable = true)
   public String getNomePatioBaliza()
   {
      return nomePatioBaliza;
   }

   public void setNomePatioBaliza(String nomePatioBaliza)
   {
      this.nomePatioBaliza = nomePatioBaliza;
   }

   @Column(nullable = true)
   public Integer getNumPatioBaliza()
   {
      return numPatioBaliza;
   }

   public void setNumPatioBaliza(Integer numPatioBaliza)
   {
      this.numPatioBaliza = numPatioBaliza;
   }

   @Column(nullable = true)
   public Double getLarguraBaliza()
   {
      return larguraBaliza;
   }

   public void setLarguraBaliza(Double larguraBaliza)
   {
      this.larguraBaliza = larguraBaliza;
   }

   @Column(nullable = true)
   public Double getCapacMaxBaliza()
   {
      return capacMaxBaliza;
   }

   public void setCapacMaxBaliza(Double capacMaxBaliza)
   {
      this.capacMaxBaliza = capacMaxBaliza;
   }

   @Column(nullable = true)
   public String getNomeUsina()
   {
      return nomeUsina;
   }

   public void setNomeUsina(String nomeUsina)
   {
      this.nomeUsina = nomeUsina;
   }

   @Column(nullable = true)
   public Double getTaxaDeOperacaoUsina()
   {
      return taxaDeOperacaoUsina;
   }

   public void setTaxaDeOperacaoUsina(Double taxaDeOperacaoUsina)
   {
      this.taxaDeOperacaoUsina = taxaDeOperacaoUsina;
   }

   @Column(nullable = true)
   public String getNomeCampanhaUsina()
   {
      return nomeCampanhaUsina;
   }

   public void setNomeCampanhaUsina(String nomeCampanhaUsina)
   {
      this.nomeCampanhaUsina = nomeCampanhaUsina;
   }

   @Column(nullable = true)
   @Temporal(TemporalType.TIMESTAMP)
   public Date getDataFimUsina()
   {
      return dataFimUsina;
   }

   public void setDataFimUsina(Date dataFimUsina)
   {
      this.dataFimUsina = dataFimUsina;
   }

   @Column(nullable = true)
   @Temporal(TemporalType.TIMESTAMP)
   public Date getDataInicioUsina()
   {
      return dataInicioUsina;
   }

   public void setDataInicioUsina(Date dataInicioUsina)
   {
      this.dataInicioUsina = dataInicioUsina;
   }

   @OneToOne
   @ForeignKey(name = "fk_rastreab_tipoScreening")
   @JoinColumn(name = "idTipoScreening", nullable = true)
   public TipoProduto getTipoScreeningUsina()
   {
      return tipoScreeningUsina;
   }

   public void setTipoScreeningUsina(TipoProduto tipoScreeningUsina)
   {
      this.tipoScreeningUsina = tipoScreeningUsina;
   }

   @OneToOne
   @ForeignKey(name = "fk_rastreabilidade_tipoPellet")
   @JoinColumn(name = "id_TipoPellet", nullable = true)
   public TipoProduto getTipoPelletUsina()
   {
      return tipoPelletUsina;
   }

   public void setTipoPelletUsina(TipoProduto tipoPelletUsina)
   {
      this.tipoPelletUsina = tipoPelletUsina;
   }

   @OneToOne
   @ForeignKey(name = "fk_rastreabilidade_tipoProduto")
   @JoinColumn(name = "id_TipoProduto", nullable = true)
   public TipoProduto getTipoProdutoUsina()
   {
      return tipoProdutoUsina;
   }

   public void setTipoProdutoUsina(TipoProduto tipoProdutoUsina)
   {
      this.tipoProdutoUsina = tipoProdutoUsina;
   }

   @Column(nullable = true)
   public Integer getNumeroBaliza()
   {
      return numeroBaliza;
   }

   public void setNumeroBaliza(Integer numeroBaliza)
   {
      this.numeroBaliza = numeroBaliza;
   }

   @ManyToMany(fetch = FetchType.LAZY,mappedBy="rastreabilidades")   
   //@Cascade(value = { CascadeType.DELETE })
   @Fetch(FetchMode.SELECT)   
   @ForeignKey(name = "fk_rastr_prod", inverseName = "fk_rastr_prod_inv")  
   public List<Produto> getProdutos() {
       return produtos;
   }

   
   protected void addProduto(Produto produto) {
       if (this.getProdutos() == null) {
           this.setProdutos(new ArrayList<Produto>());
       }
       if (this.getProdutos().contains(produto)) {
           this.getProdutos().add(produto);
       }
   }
   
   
public void setProdutos(List<Produto> produtos) {
    this.produtos = produtos;
}

@Override
public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result
			+ ((capacMaxBaliza == null) ? 0 : capacMaxBaliza.hashCode());
	result = prime * result
			+ ((dataFimUsina == null) ? 0 : dataFimUsina.hashCode());
	result = prime * result
			+ ((dataInicioUsina == null) ? 0 : dataInicioUsina.hashCode());
	result = prime
			* result
			+ ((dtHoraFimFormBaliza == null) ? 0 : dtHoraFimFormBaliza
					.hashCode());
	result = prime
			* result
			+ ((dtHoraIniFormBaliza == null) ? 0 : dtHoraIniFormBaliza
					.hashCode());
	result = prime
			* result
			+ ((horarioFimEntradaDeMaterial == null) ? 0
					: horarioFimEntradaDeMaterial.hashCode());
	result = prime
			* result
			+ ((horarioInicioEntradaDeMaterial == null) ? 0
					: horarioInicioEntradaDeMaterial.hashCode());
	result = prime * result
			+ ((idRastreabilidade == null) ? 0 : idRastreabilidade.hashCode());
	result = prime * result
			+ ((larguraBaliza == null) ? 0 : larguraBaliza.hashCode());
	result = prime * result
			+ ((nomeBaliza == null) ? 0 : nomeBaliza.hashCode());
	result = prime * result
			+ ((nomeCampanhaUsina == null) ? 0 : nomeCampanhaUsina.hashCode());
	result = prime * result
			+ ((nomePatioBaliza == null) ? 0 : nomePatioBaliza.hashCode());
	result = prime * result
			+ ((nomePilhaBaliza == null) ? 0 : nomePilhaBaliza.hashCode());
	result = prime * result + ((nomePorao == null) ? 0 : nomePorao.hashCode());
	result = prime * result + ((nomeUsina == null) ? 0 : nomeUsina.hashCode());
	result = prime * result
			+ ((numPatioBaliza == null) ? 0 : numPatioBaliza.hashCode());
	result = prime * result
			+ ((numeroBaliza == null) ? 0 : numeroBaliza.hashCode());
	result = prime
			* result
			+ ((numeroRastreabilidade == null) ? 0 : numeroRastreabilidade
					.hashCode());
	result = prime * result
			+ ((quantidade == null) ? 0 : quantidade.hashCode());
	result = prime
			* result
			+ ((taxaDeOperacaoUsina == null) ? 0 : taxaDeOperacaoUsina
					.hashCode());
	result = prime * result
			+ ((tipoAtividade == null) ? 0 : tipoAtividade.hashCode());
	result = prime * result
			+ ((tipoPelletUsina == null) ? 0 : tipoPelletUsina.hashCode());
	result = prime * result
			+ ((tipoProdutoUsina == null) ? 0 : tipoProdutoUsina.hashCode());
	result = prime
			* result
			+ ((tipoScreeningUsina == null) ? 0 : tipoScreeningUsina.hashCode());
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
	Rastreabilidade other = (Rastreabilidade) obj;
	if (capacMaxBaliza == null) {
		if (other.capacMaxBaliza != null)
			return false;
	} else if (!capacMaxBaliza.equals(other.capacMaxBaliza))
		return false;
	if (dataFimUsina == null) {
		if (other.dataFimUsina != null)
			return false;
	} else if (!dataFimUsina.equals(other.dataFimUsina))
		return false;
	if (dataInicioUsina == null) {
		if (other.dataInicioUsina != null)
			return false;
	} else if (!dataInicioUsina.equals(other.dataInicioUsina))
		return false;
	if (dtHoraFimFormBaliza == null) {
		if (other.dtHoraFimFormBaliza != null)
			return false;
	} else if (!dtHoraFimFormBaliza.equals(other.dtHoraFimFormBaliza))
		return false;
	if (dtHoraIniFormBaliza == null) {
		if (other.dtHoraIniFormBaliza != null)
			return false;
	} else if (!dtHoraIniFormBaliza.equals(other.dtHoraIniFormBaliza))
		return false;
	if (horarioFimEntradaDeMaterial == null) {
		if (other.horarioFimEntradaDeMaterial != null)
			return false;
	} else if (!horarioFimEntradaDeMaterial
			.equals(other.horarioFimEntradaDeMaterial))
		return false;
	if (horarioInicioEntradaDeMaterial == null) {
		if (other.horarioInicioEntradaDeMaterial != null)
			return false;
	} else if (!horarioInicioEntradaDeMaterial
			.equals(other.horarioInicioEntradaDeMaterial))
		return false;
	if (idRastreabilidade == null) {
		if (other.idRastreabilidade != null)
			return false;
	} else if (!idRastreabilidade.equals(other.idRastreabilidade))
		return false;
	if (larguraBaliza == null) {
		if (other.larguraBaliza != null)
			return false;
	} else if (!larguraBaliza.equals(other.larguraBaliza))
		return false;
	if (nomeBaliza == null) {
		if (other.nomeBaliza != null)
			return false;
	} else if (!nomeBaliza.equals(other.nomeBaliza))
		return false;
	if (nomeCampanhaUsina == null) {
		if (other.nomeCampanhaUsina != null)
			return false;
	} else if (!nomeCampanhaUsina.equals(other.nomeCampanhaUsina))
		return false;
	if (nomePatioBaliza == null) {
		if (other.nomePatioBaliza != null)
			return false;
	} else if (!nomePatioBaliza.equals(other.nomePatioBaliza))
		return false;
	if (nomePilhaBaliza == null) {
		if (other.nomePilhaBaliza != null)
			return false;
	} else if (!nomePilhaBaliza.equals(other.nomePilhaBaliza))
		return false;
	if (nomePorao == null) {
		if (other.nomePorao != null)
			return false;
	} else if (!nomePorao.equals(other.nomePorao))
		return false;
	if (nomeUsina == null) {
		if (other.nomeUsina != null)
			return false;
	} else if (!nomeUsina.equals(other.nomeUsina))
		return false;
	if (numPatioBaliza == null) {
		if (other.numPatioBaliza != null)
			return false;
	} else if (!numPatioBaliza.equals(other.numPatioBaliza))
		return false;
	if (numeroBaliza == null) {
		if (other.numeroBaliza != null)
			return false;
	} else if (!numeroBaliza.equals(other.numeroBaliza))
		return false;
	if (numeroRastreabilidade == null) {
		if (other.numeroRastreabilidade != null)
			return false;
	} else if (!numeroRastreabilidade.equals(other.numeroRastreabilidade))
		return false;
	if (quantidade == null) {
		if (other.quantidade != null)
			return false;
	} else if (!quantidade.equals(other.quantidade))
		return false;
	if (taxaDeOperacaoUsina == null) {
		if (other.taxaDeOperacaoUsina != null)
			return false;
	} else if (!taxaDeOperacaoUsina.equals(other.taxaDeOperacaoUsina))
		return false;
	if (tipoAtividade == null) {
		if (other.tipoAtividade != null)
			return false;
	} else if (!tipoAtividade.equals(other.tipoAtividade))
		return false;
	if (tipoPelletUsina == null) {
		if (other.tipoPelletUsina != null)
			return false;
	} else if (!tipoPelletUsina.equals(other.tipoPelletUsina))
		return false;
	if (tipoProdutoUsina == null) {
		if (other.tipoProdutoUsina != null)
			return false;
	} else if (!tipoProdutoUsina.equals(other.tipoProdutoUsina))
		return false;
	if (tipoScreeningUsina == null) {
		if (other.tipoScreeningUsina != null)
			return false;
	} else if (!tipoScreeningUsina.equals(other.tipoScreeningUsina))
		return false;
	return true;
}

   
}
