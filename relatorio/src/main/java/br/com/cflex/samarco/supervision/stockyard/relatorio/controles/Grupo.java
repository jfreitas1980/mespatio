package br.com.cflex.samarco.supervision.stockyard.relatorio.controles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.cflex.samarco.supervision.stockyard.relatorio.enums.CalculoColunaEnum;
import br.com.cflex.samarco.supervision.stockyard.relatorio.util.ConversorUtil;

public class Grupo {

	private int idGrupo;
	private String valorGrupo;
	private List<Linha> linhas;
	private Linha totalizacaoGrupo;
	private List<CalculoColunaEnum> tiposCalculoGrupo;
	private List<Grupo> subGrupos;
	private String labelGrupo;
	

	
	/**
	 * 
	 * @return
	 */
	public List<Linha> getLinhas() {
		if (linhas == null) {
			linhas = new ArrayList<Linha>();
		}
		return linhas;
	}
	
	/**
	 * @param registros the registros to set
	 */
	public void addLinha(Linha linha) {
		if (linhas == null) {
			linhas = new ArrayList<Linha>();
		}
		this.linhas.add(linha);
	}
	
	
	
	
	/**
	 * @return the idGrupo
	 */
	public int getIdGrupo() {
		return idGrupo;
	}

	/**
	 * @param idGrupo the idGrupo to set
	 */
	public void setIdGrupo(int idGrupo) {
		this.idGrupo = idGrupo;
	}

	/**
	 * @return the totalizacaoGrupo
	 */
	public Linha getTotalizacaoGrupo() {
		return totalizacaoGrupo;
	}

	/**
	 * @param totalizacaoGrupo the totalizacaoGrupo to set
	 */
	public void setTotalizacaoGrupo(Linha totalizacaoGrupo) {
		this.totalizacaoGrupo = totalizacaoGrupo;
	}

	/**
	 * @return the tiposCalculoGrupo
	 */
	public List<CalculoColunaEnum> getTiposCalculoGrupo() {
		return tiposCalculoGrupo;
	}

	/**
	 * @param tiposCalculoGrupo the tiposCalculoGrupo to set
	 */
	public void setTiposCalculoGrupo(List<CalculoColunaEnum> tiposCalculoGrupo) {
		this.tiposCalculoGrupo = tiposCalculoGrupo;
	}

	/**
	 * @return the subGrupos
	 */
	public List<Grupo> getSubGrupos() {
		if (subGrupos == null) {
			subGrupos = new ArrayList<Grupo>();
		}
		return subGrupos;
	}

	/**
	 * @param subGrupos the subGrupos to set
	 */
	public void setSubGrupos(List<Grupo> subGrupo) {
		this.subGrupos = subGrupo;
	}
	
	public void addSubGrupo(Grupo sub) {
		if (subGrupos == null) {
			subGrupos = new ArrayList<Grupo>();
		}
		this.subGrupos.add(sub);
	}

	/**
	 * @return the valorGrupo
	 */
	public String getValorGrupo() {
		return valorGrupo;
	}

	/**
	 * @param valorGrupo the valorGrupo to set
	 */
	public void setValorGrupo(String valorGrupo) {
		this.valorGrupo = valorGrupo;
	}

	/**
	 * 
	 * @return
	 */
	public String getLabelGrupo() {
		return labelGrupo;
	}
	/**
	 * 
	 * @param labelGrupo
	 */
	public void setLabelGrupo(String labelGrupo) {
		this.labelGrupo = labelGrupo;
	}

	/**
	 * Metodo que faz a totalizacao do grupo e seus subgrupos
	 * @param numeroLinha
	 */
	public void totalizarGrupo(int numeroLinha) {
		if (this.getTiposCalculoGrupo().size()>0) {
			totalizacaoGrupo = new Linha(numeroLinha++);
		}
		
		if (this.getSubGrupos().size()>0) {
			for (Grupo grupo : this.getSubGrupos()) {
				grupo.totalizarGrupo(numeroLinha);
			}
			if (this.getTiposCalculoGrupo().size()>0) {
				Map<Integer,Object> celulasNovas = new HashMap<Integer, Object>();
				List<Coluna> colunas = new ArrayList<Coluna>();
				for (Grupo grupo : this.getSubGrupos()) {
					for (Celula celula : grupo.getTotalizacaoGrupo().getCelulas()) {
						if (!colunas.contains(celula.getColuna())) {
							colunas.add(celula.getColuna());
						}
						double valor = 0;
						if (celulasNovas.containsKey(celula.getColuna().getNumero())) {
							Object obj = celulasNovas.get(celula.getColuna().getNumero());
							if (obj instanceof Double) {
								valor = ConversorUtil.convert(obj);
								valor += ConversorUtil.convert(celula.getConteudo());
								celulasNovas.put(celula.getColuna().getNumero(), valor);
							}
							else {
								celulasNovas.put(celula.getColuna().getNumero(), obj);
							}
						}
						else {
							celulasNovas.put(celula.getColuna().getNumero(), celula.getConteudo());
						}
					}
				}
				for(Coluna coluna : colunas) {
					Celula celulaNova = new Celula(totalizacaoGrupo,coluna,celulasNovas.get(coluna.getNumero()));
					totalizacaoGrupo.addCelula(celulaNova);
				}
			}
		}
		else {
			if (this.getTiposCalculoGrupo().size()>0) {
				Map<Integer,Object> totais = calculaValoresGrupo();

				for (Celula celula : this.getLinhas().get(0).getCelulas()) {
					Object conteudo = totais.get(celula.getColuna().getNumero());
					if (conteudo==null) {
						conteudo = "";
					}
					Celula celulaTotal = new Celula(totalizacaoGrupo,celula.getColuna(),conteudo);
					totalizacaoGrupo.addCelula(celulaTotal);
				}
			}
		}
	}

	/**
	 * Metodo que retorna a celula que realiza a soma da media
	 * @param linha
	 * @return
	 */
	private Celula getCelulaSoma(Linha linha) {
		List<Celula> celulas = linha.getCelulas();
		for(int i=0; i<celulas.size();i++ ) {
			
			Celula celula = celulas.get(i);
			if (celula.getColuna().getTipoCalculoColuna().equals(CalculoColunaEnum.SOMA) &&
					celula.getColuna().getGrupo()<=0) {
				return celula;
			}
		}
		return null;
	}
	
	/**
	 * Metodo que calcula os valores do grupo
	 * @return
	 */
	private Map<Integer,Object> calculaValoresGrupo() {
		
		Map<Integer,Object> result = new HashMap<Integer, Object>();
		
		int numeroCelulaSoma = -1;
		
		for (Linha registro : this.getLinhas()) {

			//Retornando a posicao da celula responvavel pela soma
			//Esta celula da soma eh utilizada para calcular a media
			Celula celulaSoma = getCelulaSoma(registro);
			if (celulaSoma != null) {
				numeroCelulaSoma = celulaSoma.getColuna().getNumero();
				Double valorSoma = new Double(0);
				if (result.containsKey(celulaSoma.getColuna().getNumero())) {
					valorSoma = ConversorUtil.convert(result.get(celulaSoma.getColuna().getNumero()));
				}
				valorSoma += ConversorUtil.convert(celulaSoma.getConteudo());
				result.put(celulaSoma.getColuna().getNumero(), valorSoma);
				
				List<Celula> celulas = registro.getCelulas();
				//Percorrendo celula por celula para calcular a media
				for( Celula celula : celulas) {

					if (celula.getColuna().getTipoCalculoColuna().equals(
							CalculoColunaEnum.MEDIA) &&
							celula.getColuna().getGrupo()<=0) {

						Double valorMedia   = ConversorUtil.convert(celula.getConteudo());
						Double valorProduto = ConversorUtil.convert(celulaSoma.getConteudo());
						
						Double valor = new Double(0);
						if (result.containsKey(celula.getColuna().getNumero())) {
							valor = ConversorUtil.convert(result.get(celula.getColuna().getNumero()));
						}
						valor += valorMedia*valorProduto;
						result.put(celula.getColuna().getNumero(), valor);
					}
				}
			}
		}
		
		if (numeroCelulaSoma != -1) {
			Object objSoma = result.get(numeroCelulaSoma);
			if (objSoma instanceof Double) {
				for (Integer key : result.keySet()) {
					Object obj = result.get(key);

					if (obj instanceof Double && key != numeroCelulaSoma) {
						Double dSoma = ConversorUtil.convert(objSoma);
						Double valor = 0d;
						if (dSoma.doubleValue()>0) {
							valor = ConversorUtil.convert(obj)/dSoma;
						}
						result.put(key, valor);
					}
				}
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
		result = prime * result + idGrupo;
		result = prime * result
				+ ((valorGrupo == null) ? 0 : valorGrupo.hashCode());
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
		Grupo other = (Grupo) obj;
		if (idGrupo != other.idGrupo)
			return false;
		if (valorGrupo == null) {
			if (other.valorGrupo != null)
				return false;
		} else if (!valorGrupo.equals(other.valorGrupo))
			return false;
		return true;
	}
}