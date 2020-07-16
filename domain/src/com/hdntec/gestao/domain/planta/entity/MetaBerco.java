
package com.hdntec.gestao.domain.planta.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
import com.hdntec.gestao.domain.navios.StatusNavioEnum;
import com.hdntec.gestao.domain.navios.entity.MetaNavio;
import com.hdntec.gestao.domain.navios.entity.status.Navio;
import com.hdntec.gestao.domain.plano.enums.TipoAtividadeEnum;
import com.hdntec.gestao.domain.planta.entity.status.Berco;
import com.hdntec.gestao.domain.planta.enums.EstadoMaquinaEnum;
import com.hdntec.gestao.domain.vo.atividades.AtividadeAtracarDesAtracarNavioVO;
import com.hdntec.gestao.exceptions.AtividadeException;


/**
 * Estrutura para atracação dos navios.
 * 
 * @author andre
 * 
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {
                "ID_META_PIER", "nomeBerco", "identificadorBerco"}))
public class MetaBerco extends AbstractMetaEntity<Berco> {

    /** Serialização do Objeto */
    private static final long serialVersionUID = -4444922878038065152L;

    /** Código do Berço */
    private Long idBerco;

    /** Nome do Berço */
    private String nomeBerco;

    private MetaPier metaPier;

    /** o comprimento limite máximo do berço em metros */
    private Double comprimentoMaximo;

    /** a boca máxima do berço em metros */
    private Double bocaMaxima;

    /** o calado máximo em metros */
    private Double caladoMaximo;

    /** flag que identifica o berco na base Mespatio e no sistema externo Samarco */
    private String identificadorBerco;

    /** Tag do pims para balanca no carregamento */
    private String tagPims;

    /**
    * Construtor Padrao.
    */
    public MetaBerco() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "berco_seq")
    @SequenceGenerator(name = "berco_seq", sequenceName = "seqberco")
    public Long getIdBerco() {
        return idBerco;
    }

    public void setIdBerco(Long idBerco) {
        this.idBerco = idBerco;
    }

    @Column(nullable = false, length = 60)
    public String getNomeBerco() {
        return nomeBerco;
    }

    @ManyToOne
    @JoinColumn(name = "ID_META_PIER", nullable = false, insertable = true)
    @ForeignKey(name = "fk_meta_pier_ber")
    public MetaPier getMetaPier() {
        return metaPier;
    }

    public void setMetaPier(MetaPier pier) {
        this.metaPier = pier;
    }

    public void setNomeBerco(String nomeBerco) {
        this.nomeBerco = nomeBerco;
    }

    @Column(precision = 2, nullable = false)
    public Double getComprimentoMaximo() {
        return comprimentoMaximo;
    }

    public void setComprimentoMaximo(Double comprimentoMaximo) {
        this.comprimentoMaximo = comprimentoMaximo;
    }

    @Column(precision = 2, nullable = false)
    public Double getBocaMaxima() {
        return bocaMaxima;
    }

    public void setBocaMaxima(Double bocaMaxima) {
        this.bocaMaxima = bocaMaxima;
    }

    @Column(precision = 2, nullable = false)
    public Double getCaladoMaximo() {
        return caladoMaximo;
    }

    public void setCaladoMaximo(Double caladoMaximo) {
        this.caladoMaximo = caladoMaximo;
    }

    @Column(nullable = false)
    public String getIdentificadorBerco() {
        return identificadorBerco;
    }

    public void setIdentificadorBerco(String identificadorBerco) {
        this.identificadorBerco = identificadorBerco;
    }

    @Column(nullable = true, length = 30)
    public String getTagPims() {
        return tagPims;
    }

    public void setTagPims(String tagPims) {
        this.tagPims = tagPims;
    }

    @Override
    @OneToMany(fetch = FetchType.LAZY,mappedBy="metaBerco")
    @Fetch(FetchMode.SELECT)
   // @Cascade(value={CascadeType.ALL,CascadeType.SAVE_UPDATE})
    //@OrderBy("dtInicio asc")
    public List<Berco> getListaStatus() {
        return super.getListaStatus();
    }

    @Override
    @Transient
    public void incluirNovoStatus(Berco novoStatus, Date horaStatus) {
        super.incluirNovoStatus(novoStatus, horaStatus);
        novoStatus.setMetaBerco(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((bocaMaxima == null) ? 0 : bocaMaxima.hashCode());
        result = prime * result + ((caladoMaximo == null) ? 0 : caladoMaximo.hashCode());
        result = prime * result + ((comprimentoMaximo == null) ? 0 : comprimentoMaximo.hashCode());
        result = prime * result + ((idBerco == null) ? 0 : idBerco.hashCode());
        result = prime * result + ((identificadorBerco == null) ? 0 : identificadorBerco.hashCode());
        result = prime * result + ((metaPier == null) ? 0 : metaPier.hashCode());
        result = prime * result + ((nomeBerco == null) ? 0 : nomeBerco.hashCode());
        result = prime * result + ((tagPims == null) ? 0 : tagPims.hashCode());
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
        MetaBerco other = (MetaBerco) obj;
        if (bocaMaxima == null) {
            if (other.bocaMaxima != null)
                return false;
        } else if (!bocaMaxima.equals(other.bocaMaxima))
            return false;
        if (caladoMaximo == null) {
            if (other.caladoMaximo != null)
                return false;
        } else if (!caladoMaximo.equals(other.caladoMaximo))
            return false;
        if (comprimentoMaximo == null) {
            if (other.comprimentoMaximo != null)
                return false;
        } else if (!comprimentoMaximo.equals(other.comprimentoMaximo))
            return false;
        if (idBerco == null) {
            if (other.idBerco != null)
                return false;
        } else if (!idBerco.equals(other.idBerco))
            return false;
        if (identificadorBerco == null) {
            if (other.identificadorBerco != null)
                return false;
        } else if (!identificadorBerco.equals(other.identificadorBerco))
            return false;
        if (metaPier == null) {
            if (other.metaPier != null)
                return false;
        } else if (!metaPier.equals(other.metaPier))
            return false;
        if (nomeBerco == null) {
            if (other.nomeBerco != null)
                return false;
        } else if (!nomeBerco.equals(other.nomeBerco))
            return false;
        if (tagPims == null) {
            if (other.tagPims != null)
                return false;
        } else if (!tagPims.equals(other.tagPims))
            return false;
        return true;
    }

   
    /**
     * TODO VERIFICAr SE DEVEMOS MUDAR STATUS DO PIER E DO BERCO, HJ NÃO TRATA ESSA INFORMAÇÃO ACHO RELEVANTE
     * erarAtividadeBerco
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 27/06/2010
     * @see
     * @param 
     * @return Berco
     */
    public Berco gerarAtividadeBerco(AtividadeAtracarDesAtracarNavioVO movimentarNavioVO) throws AtividadeException {        
                    
    	Berco  bercoCorrente = this.retornaStatusHorario(movimentarNavioVO.getDataInicio());
    	
    	if (!bercoCorrente.verificaBercoDisponivel(movimentarNavioVO.getDataInicio()) && movimentarNavioVO.getTipoAtividade().equals(TipoAtividadeEnum.CHEGADA_DE_NAVIO)) {
            throw new AtividadeException("Berço não disponivel !");
        }
        
        MetaNavio metaNavio = movimentarNavioVO.getMetaNavio();
        Navio navioAtracado = metaNavio.clonarStatus(movimentarNavioVO.getDataInicio());
                
        Berco  berco = this.clonarStatus(movimentarNavioVO.getDataInicio());
        
        if (movimentarNavioVO.getTipoAtividade().equals(TipoAtividadeEnum.CHEGADA_DE_NAVIO)) {                
                ocupar(navioAtracado, berco);
                navioAtracado.setStatusEmbarque(StatusNavioEnum.ATRACADO);
            } else if (movimentarNavioVO.getTipoAtividade().equals(TipoAtividadeEnum.SAIDA_DE_NAVIO)) {
                desocupar(navioAtracado, berco);
                navioAtracado.setStatusEmbarque(StatusNavioEnum.EMBARCADO);
            } else if (movimentarNavioVO.getTipoAtividade().equals(TipoAtividadeEnum.NAVIO_BARRA)) {
                desocupar(navioAtracado, berco);
                navioAtracado.setStatusEmbarque(StatusNavioEnum.BARRA);                                            
            }                                     
       return berco;
    }

    /**
     * ocupar
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 19/08/2010
     * @see
     * @param 
     * @return void
     */
    private void ocupar(Navio navioAtracado, Berco berco) {
        berco.setEstado(EstadoMaquinaEnum.OPERACAO);
        berco.setNavioAtracado(navioAtracado);
        navioAtracado.setBercoDeAtracacao(berco);
        navioAtracado.setIdentificadorBerco(berco.getIdentificadorBerco());
    }

    /**
     * desocupar
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 19/08/2010
     * @see
     * @param 
     * @return void
     */
    private void desocupar(Navio navioAtracado, Berco berco) {
        berco.setEstado(EstadoMaquinaEnum.OCIOSA);                
        berco.setNavioAtracado(null);
        navioAtracado.setBercoDeAtracacao(null);
        navioAtracado.setIdentificadorBerco(null);
    }

    @Override
    public Berco clonarStatus(Date horario) {
        Berco result = null;
        result = super.clonarStatus(horario);
        result.setIdBerco(null);   
        return result;
    }


}
