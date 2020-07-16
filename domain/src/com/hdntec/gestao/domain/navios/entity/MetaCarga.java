/**
 * Uma quantidade de {@link Produto} de certa {@link Qualidade} a ser colocada em um {@link Navio}.
 *
 * @author andre
 */
package com.hdntec.gestao.domain.navios.entity;

import java.util.ArrayList;
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
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.ForeignKey;

import com.hdntec.gestao.domain.AbstractMetaEntity;
import com.hdntec.gestao.domain.navios.entity.status.Carga;
import com.hdntec.gestao.domain.navios.entity.status.Navio;
import com.hdntec.gestao.domain.navios.entity.status.OrientacaoDeEmbarque;
import com.hdntec.gestao.domain.produto.entity.ItemDeControleOrientacaoEmbarque;
import com.hdntec.gestao.domain.produto.entity.ItemDeControleQualidade;
import com.hdntec.gestao.domain.produto.entity.Produto;
import com.hdntec.gestao.domain.produto.entity.Qualidade;
import com.hdntec.gestao.domain.vo.atividades.AtividadeRecuperarEmpilharVO;


@Entity
public class MetaCarga extends AbstractMetaEntity<Carga> {

	/** Serialização do objeto */
	private static final long serialVersionUID = 3421953802276463273L;

	/** identificador de carga */
	private Long idCarga;

	private MetaNavio metaNavio;

	/** identifica a carga no navio */
	private String identificadorCarga;

	/**
	 * armazena o caminho da importacao da planilha de Qualidade da Amostra do
	 * Produto da Carga
	 */
	private String caminhoCompletoPlanilha;

	/**
	 * Construtor Padrao.
	 */
	public MetaCarga() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "carga_seq")
	@SequenceGenerator(name = "carga_seq", sequenceName = "seqcarga")
	public Long getIdCarga() {
		return idCarga;
	}

	@Column(nullable = false)
	public String getIdentificadorCarga() {
		return identificadorCarga;
	}

	public void setIdCarga(Long idCarga) {
		this.idCarga = idCarga;
	}

	public void setIdentificadorCarga(String identificadorCarga) {
		this.identificadorCarga = identificadorCarga;
	}

	@Transient
	public String getCaminhoCompletoPlanilha() {
		return caminhoCompletoPlanilha;
	}

	public void setCaminhoCompletoPlanilha(String caminhoCompletoPlanilha) {
		this.caminhoCompletoPlanilha = caminhoCompletoPlanilha;
	}

	@ManyToOne
	@JoinColumn(name = "id_meta_navio", nullable = false, insertable = true)
	@ForeignKey(name = "fk_meta_carga_navio")
	public MetaNavio getMetaNavio() {
		return metaNavio;
	}

	public void setMetaNavio(MetaNavio metaNavio) {
		this.metaNavio = metaNavio;
	}

	@Override
	@OneToMany(fetch = FetchType.LAZY,mappedBy="metaCarga")
	@Fetch(FetchMode.SELECT)
	//@Cascade(value={CascadeType.ALL,CascadeType.SAVE_UPDATE})   	
	public List<Carga> getListaStatus() {
		return super.getListaStatus();
	}
	
	@Override
	@Transient
	 public void incluirNovoStatus(Carga novoStatus, Date horaStatus){
	       super.incluirNovoStatus(novoStatus, horaStatus);
	       novoStatus.setMetaCarga(this);
	 }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((idCarga == null) ? 0 : idCarga.hashCode());
        result = prime * result + ((identificadorCarga == null) ? 0 : identificadorCarga.hashCode());
        result = prime * result + ((metaNavio == null) ? 0 : metaNavio.hashCode());
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
        MetaCarga other = (MetaCarga) obj;
        if (idCarga == null) {
            if (other.idCarga != null)
                return false;
        } else if (!idCarga.equals(other.idCarga))
            return false;
        if (identificadorCarga == null) {
            if (other.identificadorCarga != null)
                return false;
        } else if (!identificadorCarga.equals(other.identificadorCarga))
            return false;
        if (metaNavio == null) {
            if (other.metaNavio != null)
                return false;
        } else if (!metaNavio.equals(other.metaNavio))
            return false;
        return true;
    }

    @Override
    public Carga clonarStatus(Date horario) {
        Carga result = null;
        result = super.clonarStatus(horario);
        result.setIdCarga(null);                
        if (result.getProduto() != null)
        {
            Produto p = result.getProduto().copiarStatus();
            result.setProduto(p);
        }    
      /*  if (result.getOrientacaoDeEmbarque() != null) {
        	OrientacaoDeEmbarque oE = result.getOrientacaoDeEmbarque().copiarStatus();
        	oE.setIdOrientacaoEmbarque(null);
        	
        	result.setOrientacaoDeEmbarque(oE);
        	oE.setCarga(result);
        	
            List<ItemDeControleOrientacaoEmbarque> itensControle = new ArrayList<ItemDeControleOrientacaoEmbarque>();
            for (ItemDeControleOrientacaoEmbarque item : oE.getListaItemDeControleOrientacaoEmbarque()) {
            	ItemDeControleOrientacaoEmbarque novoItem =(ItemDeControleOrientacaoEmbarque)item.copiarStatus();   
                novoItem.setIdItemDeControle(null);            
                //novoItem.setOrientacao(oE);             
                itensControle.add(novoItem);
            }         
            oE.setListaItemDeControleOrientacaoEmbarque(null);
            oE.setListaItemDeControleOrientacaoEmbarque(new ArrayList<ItemDeControleOrientacaoEmbarque>());
            for(ItemDeControleOrientacaoEmbarque item : itensControle) {
           	 item.setOrientacao(oE);
           	 oE.getListaItemDeControleOrientacaoEmbarque().add(item);            	 
            }

           // oE.getListaItemDeControleOrientacaoEmbarque().addAll(itensControle);
            itensControle = null;
        }*/
        return result;
    }


    /**
     * gerarAtividadeCarga
     * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
     * @since 15/07/2010
     * @see
     * @param 
     * @return Carga
     */
    public static Carga gerarAtividadeCarga(AtividadeRecuperarEmpilharVO recuperacaoVO) {        
            Carga novaCarga = null;
            MetaCarga metaCarga = recuperacaoVO.getMetaCarga(); 
            if (recuperacaoVO.getDataFim() != null ) {
                novaCarga = metaCarga.clonarStatus(recuperacaoVO.getDataFim());                                 
            } else {
                novaCarga = metaCarga.clonarStatus(recuperacaoVO.getDataInicio());                                
            }                                                
       return novaCarga;
    }

}
