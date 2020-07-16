package com.hdntec.gestao.domain.relatorio;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 
 * <P><B>Description :Entidade que representa os campos configuraveis para o padrao de relatorio</B><BR>
 * General CampoPadraoRelatorio
 * </P>
 * <P>
 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
 * @since 16/06/2009
 * @version $Revision: 1.1 $
 */
@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"idPadraoRelatorio","idCampo"}))
public class PadraoCampo implements Serializable {
	
	/** serialVersionUID */
	private static final long serialVersionUID = -5507753186811972314L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "padraocampo_seq")
    @SequenceGenerator(name = "padraocampo_seq", sequenceName = "seqpadraocampo")
	private Long idPadraoCampo;
	
	@ManyToOne
	@JoinColumn(name="idPadraoRelatorio", nullable=false, insertable=true)
	private PadraoRelatorio padraoRelatorio;
	
	@ManyToOne
	@JoinColumn(name="idCampo", nullable=false)
	private CampoRelatorio campoRelatorio;
	
	@Column(nullable=false)
	private Integer ordem;

	/**
	 * Get idPadraoCampo
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 17/06/2009
	 */
	public Long getIdPadraoCampo() {
		return idPadraoCampo;
	}

	/**
	 * Change idPadraoCampo	
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param idPadraoCampo 
	 * @since 17/06/2009
	 */
	public void setIdPadraoCampo(Long idPadraoCampo) {
		this.idPadraoCampo = idPadraoCampo;
	}

	/**
	 * Get padraoRelatorio
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 17/06/2009
	 */
	public PadraoRelatorio getPadraoRelatorio() {
		return padraoRelatorio;
	}

	/**
	 * Change padraoRelatorio	
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param padraoRelatorio 
	 * @since 17/06/2009
	 */
	public void setPadraoRelatorio(PadraoRelatorio padraoRelatorio) {
		this.padraoRelatorio = padraoRelatorio;
	}

	/**
	 * Get campoRelatorio
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 17/06/2009
	 */
	public CampoRelatorio getCampoRelatorio() {
		return campoRelatorio;
	}

	/**
	 * Change campoRelatorio	
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param campoRelatorio 
	 * @since 17/06/2009
	 */
	public void setCampoRelatorio(CampoRelatorio campoRelatorio) {
		this.campoRelatorio = campoRelatorio;
	}

	/**
	 * Get ordem
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 17/06/2009
	 */
	public Integer getOrdem() {
		return ordem;
	}

	/**
	 * Change ordem	
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param ordem 
	 * @since 17/06/2009
	 */
	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((campoRelatorio == null) ? 0 : campoRelatorio.hashCode());
		result = prime * result
				+ ((idPadraoCampo == null) ? 0 : idPadraoCampo.hashCode());
		result = prime * result + ((ordem == null) ? 0 : ordem.hashCode());
		result = prime * result
				+ ((padraoRelatorio == null) ? 0 : padraoRelatorio.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PadraoCampo other = (PadraoCampo) obj;
		if (campoRelatorio == null) {
			if (other.campoRelatorio != null)
				return false;
		} else if (!campoRelatorio.equals(other.campoRelatorio))
			return false;
		if (idPadraoCampo == null) {
			if (other.idPadraoCampo != null)
				return false;
		} else if (!idPadraoCampo.equals(other.idPadraoCampo))
			return false;
		if (ordem == null) {
			if (other.ordem != null)
				return false;
		} else if (!ordem.equals(other.ordem))
			return false;
		if (padraoRelatorio == null) {
			if (other.padraoRelatorio != null)
				return false;
		} else if (!padraoRelatorio.equals(other.padraoRelatorio))
			return false;
		return true;
	}
	
	
}
