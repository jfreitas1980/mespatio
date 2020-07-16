package com.hdntec.gestao.domain.relatorio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.hdntec.gestao.domain.relatorio.enums.EnumCamposRelatorio;


/**
 * 
 * <P><B>Description :</B><BR>
 * General PadraoRelatorio
 * </P>
 * <P>
 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
 * @since 16/06/2009
 * @version $Revision: 1.1 $
 */
@Entity
public class PadraoRelatorio implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 6880261159380213352L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "padraorelatorio_seq")
    @SequenceGenerator(name = "padraorelatorio_seq", sequenceName = "seqpadraorelatorio")
	private Long idPadraoRelatorio;
	
	@Column(nullable = false, length = 50)
	private String nomePadraoRelatorio;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, 
    		mappedBy = "padraoRelatorio")
    @Fetch(FetchMode.SELECT)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE)
	@OrderBy("ordem asc")
	private List<PadraoCampo> listaDeCampos;

	 
	/**
	 * Get idPadraoRelatorio
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 16/06/2009
	 */
	public Long getIdPadraoRelatorio() {
		return idPadraoRelatorio;
	}

	/**
	 * Change idPadraoRelatorio	
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param idPadraoRelatorio 
	 * @since 16/06/2009
	 */
	public void setIdPadraoRelatorio(Long idPadraoRelatorio) {
		this.idPadraoRelatorio = idPadraoRelatorio;
	}

	/**
	 * Get nomePadraoRelatorio
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 16/06/2009
	 */
	public String getNomePadraoRelatorio() {
		return nomePadraoRelatorio;
	}

	/**
	 * Change nomePadraoRelatorio	
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param nomePadraoRelatorio 
	 * @since 16/06/2009
	 */
	public void setNomePadraoRelatorio(String nomePadraoRelatorio) {
		this.nomePadraoRelatorio = nomePadraoRelatorio;
	}

	/**
	 * Get listaDeCampos
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 17/06/2009
	 */
	public List<PadraoCampo> getListaDeCampos() {
		if (listaDeCampos==null) {
			listaDeCampos = new ArrayList<PadraoCampo>();
		}
		return listaDeCampos;
	}

	/**
	 * Change listaDeCampos	
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @param listaDeCampos 
	 * @since 17/06/2009
	 */
	public void setListaDeCampos(List<PadraoCampo> listaDeCampos) {
		this.listaDeCampos = listaDeCampos;
	}
	
	/**
	 * 
	 * addPadraoCampo
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 18/06/2009
	 * @see
	 * @param padraoCampo
	 * @return Returns the void.
	 */
	public void addPadraoCampo(PadraoCampo padraoCampo) {
		List<PadraoCampo> aux = getListaDeCampos();
		aux.remove(padraoCampo);
		aux.add(padraoCampo);
	}
	
	/**
	 * 
	 * getTotalCampos
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 22/06/2009
	 * @see
	 * @return
	 * @return Returns the int.
	 */
	public int getTotalCampos() {
		return getListaDeCampos().size();
	}

	/**
	 * 
	 * getTotalTamanhoCampos
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 25/06/2009
	 * @see
	 * @return
	 * @return Returns the double.
	 */
	public double getTotalTamanhoCampos() {
		double somaCampos = 0;
		for ( PadraoCampo padraoCampo : getListaDeCampos() ) {

			String nomeCampo = padraoCampo.getCampoRelatorio().getNomeCampo();

			if (EnumCamposRelatorio.PILHA.toString().equals(nomeCampo)) {
				somaCampos += EnumCamposRelatorio.PILHA.getPeso();
			}
			else if (EnumCamposRelatorio.PATIO.toString().equals(nomeCampo)) {
				somaCampos += EnumCamposRelatorio.PATIO.getPeso();
			}
			else if (EnumCamposRelatorio.INICIO.toString().equals(nomeCampo)) {
				somaCampos += EnumCamposRelatorio.INICIO.getPeso();
			}
			else if (EnumCamposRelatorio.FIM.toString().equals(nomeCampo)) {
				somaCampos += EnumCamposRelatorio.FIM.getPeso();
			}
			else if (EnumCamposRelatorio.CLIENTE.toString().equals(nomeCampo)) {
				somaCampos += EnumCamposRelatorio.CLIENTE.getPeso();
			}
			else if (EnumCamposRelatorio.PRODUTO.toString().equals(nomeCampo)) {
				somaCampos += EnumCamposRelatorio.PRODUTO.getPeso();
			}
			else if (EnumCamposRelatorio.TON.toString().equals(nomeCampo)) {
				somaCampos += EnumCamposRelatorio.TON.getPeso();
			}
			else if (EnumCamposRelatorio.EMERG.toString().equals(nomeCampo)) {
				somaCampos += EnumCamposRelatorio.EMERG.getPeso();
			}
			else if (EnumCamposRelatorio.BALIZA.toString().equals(nomeCampo)) {
				somaCampos += EnumCamposRelatorio.BALIZA.getPeso();
			}
			else if (!EnumCamposRelatorio.IMAGEM_SITUACAO.toString().equals(nomeCampo)) {
				somaCampos += EnumCamposRelatorio.getPesoNaoDefinido();
			}
		}
		return somaCampos;
	}
	
	/**
	 * 
	 * getTotalTamanhoCampos
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 17/08/2009
	 * @see
	 * @param camposExcluidos
	 * @return
	 * @return Returns the double.
	 */
	public double getTotalTamanhoCampos(List<String> camposExcluidos) {
		double somaCampos = 0;
		for ( PadraoCampo padraoCampo : getListaDeCampos() ) {

			String nomeCampo = padraoCampo.getCampoRelatorio().getNomeCampo();

			if (!camposExcluidos.contains(nomeCampo)) {
				if (EnumCamposRelatorio.PILHA.toString().equals(nomeCampo)) {
					somaCampos += EnumCamposRelatorio.PILHA.getPeso();
				}
				else if (EnumCamposRelatorio.PATIO.toString().equals(nomeCampo)) {
					somaCampos += EnumCamposRelatorio.PATIO.getPeso();
				}
				else if (EnumCamposRelatorio.INICIO.toString().equals(nomeCampo)) {
					somaCampos += EnumCamposRelatorio.INICIO.getPeso();
				}
				else if (EnumCamposRelatorio.FIM.toString().equals(nomeCampo)) {
					somaCampos += EnumCamposRelatorio.FIM.getPeso();
				}
				else if (EnumCamposRelatorio.CLIENTE.toString().equals(nomeCampo)) {
					somaCampos += EnumCamposRelatorio.CLIENTE.getPeso();
				}
				else if (EnumCamposRelatorio.PRODUTO.toString().equals(nomeCampo)) {
					somaCampos += EnumCamposRelatorio.PRODUTO.getPeso();
				}
				else if (EnumCamposRelatorio.TON.toString().equals(nomeCampo)) {
					somaCampos += EnumCamposRelatorio.TON.getPeso();
				}
				else if (EnumCamposRelatorio.EMERG.toString().equals(nomeCampo)) {
					somaCampos += EnumCamposRelatorio.EMERG.getPeso();
				}
				else if (EnumCamposRelatorio.BALIZA.toString().equals(nomeCampo)) {
					somaCampos += EnumCamposRelatorio.BALIZA.getPeso();
				}
				else {
					somaCampos += EnumCamposRelatorio.getPesoNaoDefinido();
				}
			}
		}
		return somaCampos;
	}
	
	/**
	 * 
	 * getSubListaPadraoCampos
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 20/08/2009
	 * @see
	 * @param camposExcluidos
	 * @return
	 * @return Returns the List<PadraoCampo>.
	 */
	public List<String> getSubListaPadraoCampos(List<String> camposExcluidos) {
		List<String> result = new ArrayList<String>();
		for (PadraoCampo campo : getListaDeCampos()) {
			if (!camposExcluidos.contains(
					campo.getCampoRelatorio().getNomeCampo())) {
				result.add(campo.getCampoRelatorio().getNomeCampo());
			}
		}
		return result;
	}
	/**
	 * 
	 * contemCampo
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 22/06/2009
	 * @see
	 * @param nomeCampo
	 * @return
	 * @return Returns the boolean.
	 */
	public boolean contemCampo(String nomeCampo) {
		boolean result = false;
		List<PadraoCampo> listaCampos = getListaDeCampos();
		for(PadraoCampo padraoCampo : listaCampos) {
			if (nomeCampo.equals(padraoCampo.getCampoRelatorio().getNomeCampo())) {
				result = true;
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((idPadraoRelatorio == null) ? 0 : idPadraoRelatorio
						.hashCode());
		result = prime
				* result
				+ ((nomePadraoRelatorio == null) ? 0 : nomePadraoRelatorio
						.hashCode());
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
		PadraoRelatorio other = (PadraoRelatorio) obj;
		if (idPadraoRelatorio == null) {
			if (other.idPadraoRelatorio != null)
				return false;
		} else if (!idPadraoRelatorio.equals(other.idPadraoRelatorio))
			return false;
		if (nomePadraoRelatorio == null) {
			if (other.nomePadraoRelatorio != null)
				return false;
		} else if (!nomePadraoRelatorio.equals(other.nomePadraoRelatorio))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		
		return this.getNomePadraoRelatorio();
	}
}
