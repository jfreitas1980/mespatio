package com.hdntec.gestao.domain.plano.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.hdntec.gestao.domain.planta.entity.status.Filtragem;
import com.hdntec.gestao.domain.planta.entity.status.Usina;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;


/**
 * <P><B>Description :</B><BR>
 * General AtividadeRelatorio
 * </P>
 * <P>
 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
 * @since 08/08/2009
 * @version $Revision: 1.1 $
 */
public class AtividadeRelatorio implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	private List<Usina> listaUsinas;
	private List<Filtragem> listaFiltragens;
	private String nomePatio;
    private String nomePatioDestino;
	private String dataInicio;
	private String dataFim;
	private String nomeMaquina;
	private String nomeMaquinaDestino;
	private String nomeNavio;
	private String strCarga;
	private String strPorao;
	private String strProduto;
	private String nomeBalizaOrigem;
	private Double qtdBalizaOrigem;
	private String nomeBalizaDestino;
	private Double qtdBalizaDestino;
	
	private String nomeBalizaPellet;
	private Double qtdBalizaPellet;
	private String nomeBalizaPelota;
	private Double qtdBalizaPelota;
	private TipoProduto tipoProduto;
	
	
	public List<Usina> getListaUsinas() {
		if (listaUsinas==null) {
			listaUsinas = new ArrayList<Usina>();
		}
		return listaUsinas;
	}
	public void setListaUsinas(List<Usina> listaUsinas) {
		this.listaUsinas = listaUsinas;
	}
	
	public void addUsina(Usina usina) {
		if (listaUsinas==null) {
			listaUsinas = new ArrayList<Usina>();
		}
		listaUsinas.add(usina);
	}
	
	public void addFiltragem(Filtragem filtro) {
        if (listaFiltragens==null) {
            listaFiltragens = new ArrayList<Filtragem>();
        }
        listaFiltragens.add(filtro);
    }
	
	public String getNomePatio() {
		return nomePatio;
	}
	public void setNomePatio(String nomePatio) {
		this.nomePatio = nomePatio;
	}
	
	public String getDataInicio() {
		return dataInicio;
	}
	public void setDataInicio(String dataInicio) {
		this.dataInicio = dataInicio;
	}
		
	public String getDataFim() {
		return dataFim;
	}
	public void setDataFim(String dataFim) {
		this.dataFim = dataFim;
	}
		
	public String getNomeMaquina() {
		return nomeMaquina;
	}
	public void setNomeMaquina(String nomeMaquina) {
		this.nomeMaquina = nomeMaquina;
	}
		
	public String getStrCarga() {
		return strCarga;
	}
	public void setStrCarga(String strCarga) {
		this.strCarga = strCarga;
	}
		
	public String getStrPorao() {
		return strPorao;
	}
	public void setStrPorao(String strPorao) {
		this.strPorao = strPorao;
	}
		
	public String getStrProduto() {
		return strProduto;
	}
	public void setStrProduto(String strProduto) {
		this.strProduto = strProduto;
	}
	
	public String getNomeBalizaOrigem() {
		return nomeBalizaOrigem;
	}
	public void setNomeBalizaOrigem(String nomeBalizaOrigem) {
		this.nomeBalizaOrigem = nomeBalizaOrigem;
	}
		
	public Double getQtdBalizaOrigem() {
		return qtdBalizaOrigem;
	}
	public void setQtdBalizaOrigem(Double qtdBalizaOrigem) {
		this.qtdBalizaOrigem = qtdBalizaOrigem;
	}
		
	public String getNomeBalizaDestino() {
		return nomeBalizaDestino;
	}
	public void setNomeBalizaDestino(String nomeBalizaDestino) {
		this.nomeBalizaDestino = nomeBalizaDestino;
	}
		
	public Double getQtdBalizaDestino() {
		return qtdBalizaDestino;
	}
	public void setQtdBalizaDestino(Double qtdBalizaDestino) {
		this.qtdBalizaDestino = qtdBalizaDestino;
	}
		
	public String getNomeBalizaPellet() {
		return nomeBalizaPellet;
	}
	public void setNomeBalizaPellet(String nomeBalizaPellet) {
		this.nomeBalizaPellet = nomeBalizaPellet;
	}
		
	public Double getQtdBalizaPellet() {
		return qtdBalizaPellet;
	}
	public void setQtdBalizaPellet(Double qtdBalizaPellet) {
		this.qtdBalizaPellet = qtdBalizaPellet;
	}
		
	public String getNomeBalizaPelota() {
		return nomeBalizaPelota;
	}
	public void setNomeBalizaPelota(String nomeBalizaPelota) {
		this.nomeBalizaPelota = nomeBalizaPelota;
	}
		
	public Double getQtdBalizaPelota() {
		return qtdBalizaPelota;
	}
	public void setQtdBalizaPelota(Double qtdBalizaPelota) {
		this.qtdBalizaPelota = qtdBalizaPelota;
	}
	
	public String getNomeNavio() {
		return nomeNavio;
	}
	public void setNomeNavio(String nomeNavio) {
		this.nomeNavio = nomeNavio;
	}
	public TipoProduto getTipoProduto() {
		return tipoProduto;
	}
	public void setTipoProduto(TipoProduto tipoProduto) {
		this.tipoProduto = tipoProduto;
	}
	@Override
	public String toString() {
		return this.nomeBalizaOrigem;
	}
    public List<Filtragem> getListaFiltragens() {
        return listaFiltragens;
    }
    public void setListaFiltragens(List<Filtragem> listaFiltragens) {
        this.listaFiltragens = listaFiltragens;
    }
    public String getNomePatioDestino() {
        return nomePatioDestino;
    }
    public void setNomePatioDestino(String nomePatioDestino) {
        this.nomePatioDestino = nomePatioDestino;
    }
    public String getNomeMaquinaDestino() {
        return nomeMaquinaDestino;
    }
    public void setNomeMaquinaDestino(String nomeMaquinaDestino) {
        this.nomeMaquinaDestino = nomeMaquinaDestino;
    }
	
}