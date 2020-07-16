package com.hdntec.gestao.domain.relatorio;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 * 
 * <P><B>Description :</B><BR>
 * General CampoRelatorio
 * </P>
 * <P>
 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
 * @since 17/06/2009
 * @version $Revision: 1.1 $
 */
@Entity
public class CampoRelatorio implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 4791831366967748468L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "camporelatorio_seq")
    @SequenceGenerator(name = "camporelatorio_seq", sequenceName = "seqcamporelatorio")
	private Long idCampo;
	
	@Column(nullable = false, length = 60)
	private String nomeCampo;

	/**
	 * Get idCampo
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 17/06/2009
	 */
	public Long getIdCampo() {
		return idCampo;
	}

	/**
	 * Change idCampo	
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param idCampo 
	 * @since 17/06/2009
	 */
	public void setIdCampo(Long idCampo) {
		this.idCampo = idCampo;
	}

	/**
	 * Get nomeCampo
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 17/06/2009
	 */
	public String getNomeCampo() {
		return nomeCampo;
	}

	/**
	 * Change nomeCampo	
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param nomeCampo 
	 * @since 17/06/2009
	 */
	public void setNomeCampo(String nomeCampo) {
		this.nomeCampo = nomeCampo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idCampo == null) ? 0 : idCampo.hashCode());
		result = prime * result
				+ ((nomeCampo == null) ? 0 : nomeCampo.hashCode());
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
		CampoRelatorio other = (CampoRelatorio) obj;
		if (idCampo == null) {
			if (other.idCampo != null)
				return false;
		} else if (!idCampo.equals(other.idCampo))
			return false;
		if (nomeCampo == null) {
			if (other.nomeCampo != null)
				return false;
		} else if (!nomeCampo.equals(other.nomeCampo))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return nomeCampo;
	}
}
