package com.hdntec.gestao.domain.planta.entity.status;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import org.hibernate.annotations.ForeignKey;

import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.entity.LugarEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.planta.entity.MetaFiltragem;
import com.hdntec.gestao.domain.planta.entity.MetaUsina;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.domain.produto.enums.TipoDeProdutoEnum;
import com.hdntec.gestao.domain.vo.atividades.AtividadeRecuperarEmpilharVO;


/**
 *
 * @author Ricardo Trabalho
 */
@Entity
public class AtividadeCampanha implements Serializable
{

   private static final long serialVersionUID = 466452231368600031L;

   
   private TipoProduto tipoProduto;
   
   /** Chave de identificacao desta atividade no banco de dados. */
   private Long idAtividadeCampanha;


   /** Referencia de campanha correspondente a esta atividade.*/
   private Campanha campanha;
   
   
   private LugarEmpilhamentoRecuperacao lugarEmpilhamento;
   
   
   /** usina da campanha */
   private String nomeUsina;

   /** quantidade de material para ser recolhido da usina na atividade. */
   private Double quantidade;

   private Double qtdTotalProduzida;

   /** taxa de operacao da recuperacao da usina */
   private Double taxaOperacaoUsina;

   
   public AtividadeCampanha()
   {
   }

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO, generator = "atvcampanha_seq")
   @SequenceGenerator(name = "atvcampanha_seq", sequenceName = "seqatvcampanha")
   public Long getIdAtividadeCampanha()
   {
      return idAtividadeCampanha;
   }

   public void setIdAtividadeCampanha(Long idAtividadeCampanha)
   {
      this.idAtividadeCampanha = idAtividadeCampanha;
   }

   public void setQuantidade(Double quantidadeTemporaria)
   {
      this.quantidade = quantidadeTemporaria;
   }

   @OneToOne   
   @ForeignKey(name = "fk_ativCampannha_campanha")
   @JoinColumn(name = "idCampanha")
   public Campanha getCampanha()
   {
      return campanha;
   }

   public void setCampanha(Campanha campanha)
   {
      this.campanha = campanha;
   }

   
   @Column(length = 60, nullable = false)
   public String getNomeUsina()
   {
      return nomeUsina;
   }

   
   @Column(nullable = false)
   public Double getTaxaOperacaoUsina()
   {
      return taxaOperacaoUsina;
   }

   public void setTaxaOperacaoUsina(Double taxaOperacaoUsina)
   {
      this.taxaOperacaoUsina = taxaOperacaoUsina;
   }

   public void setNomeUsina(String nomeUsina)
   {
      this.nomeUsina = nomeUsina;
   }

   
   @Transient
   public Double getQtdTotalProduzida()
   {
      return qtdTotalProduzida;
   }

   public void setQtdTotalProduzida(Double qtdTotalProduzida)
   {
      this.qtdTotalProduzida = qtdTotalProduzida;
   }

   
   @Column(nullable = true)
   public Double getQuantidade()
   {
      return quantidade;
   }

    /*@Transient  
    public Atividade getAtividade() {
        return getLugarEmpilhamento().getAtividade();
    }*/
    
    @Transient
	public Correia getCorreia() {
		// TODO Auto-generated method stub
		return null;
	}

   /* @ManyToOne
    @ForeignKey(name = "fk_lugar_atvcamp")
    @JoinColumn(name = "id_lugar",nullable=false,insertable=true,updatable=true)   
    public LugarEmpilhamentoRecuperacao getLugarEmpilhamento() {
        return lugarEmpilhamento;
    }

    public void setLugarEmpilhamento(LugarEmpilhamentoRecuperacao lugarEmpilhamento) {
        this.lugarEmpilhamento = lugarEmpilhamento;
    }
*/
    /**
     * gerarAtividadeCampanha
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 16/06/2010
     * @see
     * @param 
     * @return AtividadeCampanha
     */
    public static  AtividadeCampanha gerarAtividadeCampanha(Campanha campanhaSelecionada,TipoProduto tipoProduto) {
        AtividadeCampanha atividadeCamp = new AtividadeCampanha();
        atividadeCamp.setCampanha(campanhaSelecionada);
        atividadeCamp.setTipoProduto(tipoProduto);        
        atividadeCamp.setNomeUsina(campanhaSelecionada.getMetaUsina().getNomeUsina());
        atividadeCamp.setTaxaOperacaoUsina(1D);
        atividadeCamp.setQuantidade(null);        
        return atividadeCamp;
    }
    
   @Transient
    public TipoDeProdutoEnum getTipoDeProduto()
    {
       return getTipoProduto().getTipoDeProduto();
    }

    /**
     * gerarAtividadesCampanha
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 17/06/2010
     * @see
     * @param 
     * @return List<AtividadeCampanha>
     */
    public static List<AtividadeCampanha> gerarAtividadesCampanha(AtividadeRecuperarEmpilharVO recuperacaoVO) {
        List<AtividadeCampanha> atividadesCampanhas = new ArrayList<AtividadeCampanha>();
        /**
         * Recupera a campanha corrente das usinas selecionadas 
         */        
            for(Campanha campanhaAtual : recuperacaoVO.getCampanhas()) {                         
               AtividadeCampanha atividadeCampanha = AtividadeCampanha.gerarAtividadeCampanha(campanhaAtual, recuperacaoVO.getTipoProduto());
               atividadesCampanhas.add(atividadeCampanha);
            }        
        return atividadesCampanhas;
    }

    /**
     * gerarAtividadesCampanha
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 17/06/2010
     * @see
     * @param 
     * @return List<AtividadeCampanha>
     */
    public static List<AtividadeCampanha> gerarAtividadesMultiCampanha(AtividadeRecuperarEmpilharVO recuperacaoVO) {
        List<AtividadeCampanha> atividadesCampanhas = new ArrayList<AtividadeCampanha>();
        
        
        /**
         * Recupera a campanha corrente das usinas selecionadas 
         */        
            for(MetaUsina usina : recuperacaoVO.getListaUsinas()) {                         
                Campanha campanhaAtual = recuperacaoVO.getMapUsinaCampanha().get(usina);
                recuperacaoVO.setTipoProduto(campanhaAtual.getTipoProduto());
                AtividadeCampanha atividadeCampanha = AtividadeCampanha.gerarAtividadeCampanha(campanhaAtual, campanhaAtual.getTipoProduto());
               atividadesCampanhas.add(atividadeCampanha);
            }
        
            for(MetaFiltragem usina : recuperacaoVO.getListaFiltragens()) {                         
                Campanha campanhaAtual = recuperacaoVO.getMapFiltragemCampanha().get(usina);
                recuperacaoVO.setTipoProduto(campanhaAtual.getTipoPellet());
                AtividadeCampanha atividadeCampanha = AtividadeCampanha.gerarAtividadeCampanha(campanhaAtual, campanhaAtual.getTipoPellet());
                atividadesCampanhas.add(atividadeCampanha);
            }
            
       
        return atividadesCampanhas;
    }
    @OneToOne
    @ForeignKey(name = "fk_atvcampanha_tipoProduto")
    @JoinColumn(name = "id_TipoProduto")
    public TipoProduto getTipoProduto() {
        return tipoProduto;
    }

    public void setTipoProduto(TipoProduto tipoProduto) {
        this.tipoProduto = tipoProduto;
    }
    
}
