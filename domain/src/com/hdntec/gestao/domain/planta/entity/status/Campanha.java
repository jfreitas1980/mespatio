
package com.hdntec.gestao.domain.planta.entity.status;

import java.util.Date;

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

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.ForeignKey;

import com.hdntec.gestao.domain.ComparadorStatusEntity;
import com.hdntec.gestao.domain.StatusEntity;
import com.hdntec.gestao.domain.planta.entity.MetaUsina;
import com.hdntec.gestao.domain.produto.entity.Qualidade;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.domain.produto.enums.TipoDeProdutoEnum;


/**
 * � o planejamento semanal que determina o que ser� produzido pelas usinas,
 * principalmente o tipo do produto e qualidade.
 * <p>
 * Persite. - Tamanho: - Volume: muitos registros. - Per�odo de Persist�ncia:
 * semanalmente - Freq. Update: comum, semanalmente. -Confiabilidade: deve ser
 * confi�vel.
 */
@Entity
public class Campanha extends StatusEntity<Campanha> {

    public static ComparadorStatusEntity<Campanha> comparadorCampanha = new ComparadorStatusEntity<Campanha>();

    /** serial gerado automaticamente */
    private static final long serialVersionUID = 4237983980897709341L;

    /** identificador de Campanha */
    private Long idCampanha;

    private MetaUsina metaUsina;

    /** Nome da Campanha */
    private String nomeCampanha;

    /** codigo da fase do processo quando a usina esta produzindo pellet feed */
    private Long codigoFaseProcessoPelletFeed;

    /**
     * codigo da fase do processo quando a usina esta produzindo pellet feed
     * qualidade quimico
     */
    private Long codigoFaseProcessoPelletFeedQAQ;

    /**
     * codigo da fase do processo quando a usina esta produzindo pellet feed
     * qualidade fisico
     */
    private Long codigoFaseProcessoPelletFeedQAF;

    /** codigo da fase do processo quando a usina esta produzindo pelota */
    private Long codigoFaseProcessoPelota;

    /** tipo de processo quando a usina esta produzindo pellet feed */
    private String tipoProcessoPelletFeed;

    /** tipo de processo quando a usina esta produzindo pelota */
    private String tipoProcessoPelota;

    /** Codigo da area resp ed (para pellet feed) */
    private String areaRespEDPelletFeed;

    /** Codigo da area resp ed (para pelota) */
    private String areaRespEDPelota;

    /** Codigo do item de controle usado pelo cliente (para pellet feed) */
    private Long cdTipoItemControlePelletFeed;

    /** Codigo do item de controle usado pelo cliente (para pelota) */
    private Long cdTipoItemControlePelota;

    /** o tipo de produto */
    private TipoProduto tipoProduto;

    /** Qualidade estimada do produto */
    private Qualidade qualidadeEstimada;

    /** quantidade prevista de produ desta campanha */
    private Double quantidadePrevista;

    /** o tipo de pellet feed dessa campanha */
    private TipoProduto tipoPellet;

    /** o pellet screening que sai da usina durante esta campanha */
    private TipoProduto tipoScreening;

    /** tipo de produto selecionado para operacao, campo n�o persistido */
    private TipoDeProdutoEnum tipoDeProdutoTemporario;

    /** quantidade de produto para operacao, campo n�o persistido */
    private Double quantidadeTemporaria;

    private Boolean finalizada;
    
    @OneToOne
    @ForeignKey(name = "fk_campanha_tipoProduto")
    @JoinColumn(name = "id_TipoProduto", nullable = false)
    public TipoProduto getTipoProduto() {
        return tipoProduto;
    }

    public void setTipoProduto(TipoProduto tipoProduto) {
        this.tipoProduto = tipoProduto;
    }

    @OneToOne
    //@Cascade(value = {
      //  CascadeType.ALL})
    @ForeignKey(name = "fk_campanha_qualidade")
    @JoinColumn(name = "id_QualidadeEstimada")
    public Qualidade getQualidadeEstimada() {
        return qualidadeEstimada;
    }

    public void setQualidadeEstimada(Qualidade qualidadeEstimada) {
        this.qualidadeEstimada = qualidadeEstimada;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "campanha_seq")
    @SequenceGenerator(name = "campanha_seq", sequenceName = "seqcampanha")
    public Long getIdCampanha() {
        return idCampanha;
    }

    public void setIdCampanha(Long idCampanha) {
        this.idCampanha = idCampanha;
    }

    @Column(nullable = false, length = 60)
    public String getNomeCampanha() {
        return nomeCampanha;
    }

    public void setNomeCampanha(String nomeCampanha) {
        this.nomeCampanha = nomeCampanha;
    }

    @Column(nullable = true)
    public Long getCodigoFaseProcessoPelletFeed() {
        return codigoFaseProcessoPelletFeed;
    }

    @Column(nullable = true)
    public Long getCodigoFaseProcessoPelota() {
        return codigoFaseProcessoPelota;
    }

    @Column(nullable = true)
    public String getTipoProcessoPelletFeed() {
        return tipoProcessoPelletFeed;
    }

    @Column(nullable = true)
    public String getTipoProcessoPelota() {
        return tipoProcessoPelota;
    }

    @Column(nullable = true)
    public String getAreaRespEDPelletFeed() {
        return areaRespEDPelletFeed;
    }

    @Column(nullable = true)
    public String getAreaRespEDPelota() {
        return areaRespEDPelota;
    }

    @Column(nullable = true)
    public Long getCdTipoItemControlePelletFeed() {
        return cdTipoItemControlePelletFeed;
    }

    @Column(nullable = true)
    public Long getCdTipoItemControlePelota() {
        return cdTipoItemControlePelota;
    }

    public void setTipoProcessoPelletFeed(String tipoProcessoPelletFeed) {
        this.tipoProcessoPelletFeed = tipoProcessoPelletFeed;
    }

    public void setTipoProcessoPelota(String tipoProcessoPelota) {
        this.tipoProcessoPelota = tipoProcessoPelota;
    }

    public void setAreaRespEDPelletFeed(String areaRespEDPelletFeed) {
        this.areaRespEDPelletFeed = areaRespEDPelletFeed;
    }

    public void setAreaRespEDPelota(String areaRespEDPelota) {
        this.areaRespEDPelota = areaRespEDPelota;
    }

    public void setCdTipoItemControlePelletFeed(Long cdTipoItemControlePelletFeed) {
        this.cdTipoItemControlePelletFeed = cdTipoItemControlePelletFeed;
    }

    public void setCdTipoItemControlePelota(Long cdTipoItemControlePelota) {
        this.cdTipoItemControlePelota = cdTipoItemControlePelota;
    }

    public void setCodigoFaseProcessoPelletFeed(Long codigoFaseProcesso) {
        this.codigoFaseProcessoPelletFeed = codigoFaseProcesso;
    }

    public void setCodigoFaseProcessoPelota(Long codigoFaseProcesso) {
        this.codigoFaseProcessoPelota = codigoFaseProcesso;
    }

    @OneToOne
    @ForeignKey(name = "fk_campanha_tipoPellet")
    @JoinColumn(name = "id_TipoPellet", nullable = false)
    public TipoProduto getTipoPellet() {
        return this.tipoPellet;
    }

    @OneToOne
    @ForeignKey(name = "fk_campanha_tipoScreening")
    @JoinColumn(name = "id_TipoScreening", nullable = false)
    public TipoProduto getTipoScreening() {
        return this.tipoScreening;
    }

    public void setTipoPellet(TipoProduto tipoPellet) {
        this.tipoPellet = tipoPellet;
    }

    public void setTipoScreening(TipoProduto tipoScreening) {
        this.tipoScreening = tipoScreening;
    }

    @Column(nullable = false)
    public Double getQuantidadePrevista() {
        return quantidadePrevista;
    }

    public void setQuantidadePrevista(Double quantidadePrevista) {
        this.quantidadePrevista = quantidadePrevista;
    }

    @Transient
    public TipoDeProdutoEnum getTipoDeProdutoTemporario() {
        return tipoDeProdutoTemporario;
    }

    public void setTipoDeProdutoTemporario(TipoDeProdutoEnum tipoDeProduto) {
        this.tipoDeProdutoTemporario = tipoDeProduto;
    }

    @Transient
    public Double getQuantidadeTemporaria() {
        return quantidadeTemporaria;
    }

    public void setQuantidadeTemporaria(Double quantidadeTemporaria) {
        this.quantidadeTemporaria = quantidadeTemporaria;
    }

    @Column(nullable = false, name = "CDFASEPELLETQUIMICO")
    public Long getCodigoFaseProcessoPelletFeedQAQ() {
        return codigoFaseProcessoPelletFeedQAQ;
    }

    public void setCodigoFaseProcessoPelletFeedQAQ(Long codigoFaseProcessoPelletFeedQAQ) {
        this.codigoFaseProcessoPelletFeedQAQ = codigoFaseProcessoPelletFeedQAQ;
    }

    @Column(nullable = false, name = "CDFASEPELLETFISICO")
    public Long getCodigoFaseProcessoPelletFeedQAF() {
        return codigoFaseProcessoPelletFeedQAF;
    }

    public void setCodigoFaseProcessoPelletFeedQAF(Long codigoFaseProcessoPelletFeedQAF) {
        this.codigoFaseProcessoPelletFeedQAF = codigoFaseProcessoPelletFeedQAF;
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
    public void setDataFinal(Date data) {
        // TODO Auto-generated method stub
        setDtFim(data);
    }

    @Transient
    public void setDataInicial(Date dataInicial) {
        // TODO Auto-generated method stub
        setDtInicio(dataInicial);
    }

    @Transient
    public Usina getUsina(Date dataAtual) {
        return getMetaUsina().retornaStatusHorario(dataAtual);
    }

    @Transient
    public Double getCaptacaoTemporaria() {
        return 0D;
    }

    public void setCaptacaoTemporaria(Double captacaoTemporaria) {

    }

    @ManyToOne
    @JoinColumn(name = "ID_META_USINA", nullable = false, insertable = true)
    @ForeignKey(name = "fk_meta_usin_camp")
    public MetaUsina getMetaUsina() {
        return metaUsina;
    }

  

    @Column(nullable = false)
    public Boolean getFinalizada() {
        return finalizada;
    }

    public void setFinalizada(Boolean finalizada) {
        this.finalizada = finalizada;
    }

    public void setMetaUsina(MetaUsina metaUsina) {
        this.metaUsina = metaUsina;
    }

    /**
     * retornarCampanhaTipoDeProduto
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 16/06/2010
     * @see
     * @param 
     * @return TipoDeProdutoEnum
     */
    public TipoDeProdutoEnum retornarCampanhaTipoDeProduto(TipoProduto tipoProduto) {
        TipoDeProdutoEnum result;
        if (tipoProduto.equals(this.getTipoProduto())) {
            result = TipoDeProdutoEnum.PELOTA;
        } else if (tipoProduto.equals(this.getTipoPellet())) {
            result = TipoDeProdutoEnum.PELLET_FEED;
        } else {
            result = TipoDeProdutoEnum.PELLET_SCREENING;
        }
        return result;
    }
}