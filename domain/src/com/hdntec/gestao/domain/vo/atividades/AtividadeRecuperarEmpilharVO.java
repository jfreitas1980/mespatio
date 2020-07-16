package com.hdntec.gestao.domain.vo.atividades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hdntec.gestao.domain.navios.entity.MetaCarga;
import com.hdntec.gestao.domain.navios.entity.status.Cliente;
import com.hdntec.gestao.domain.plano.entity.Atividade;
import com.hdntec.gestao.domain.plano.enums.SentidoEmpilhamentoRecuperacaoEnum;
import com.hdntec.gestao.domain.planta.entity.MetaBaliza;
import com.hdntec.gestao.domain.planta.entity.MetaFiltragem;
import com.hdntec.gestao.domain.planta.entity.MetaMaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.MetaUsina;
import com.hdntec.gestao.domain.planta.entity.status.Campanha;
import com.hdntec.gestao.domain.produto.entity.Produto;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;


/**
 * <P><B>Description :</B><BR>
 * General AtividadeRecuperarEmpilharVO
 * </P>
 * <P>
 * <B>
 * SAs : <BR>
 * 8148
 * </B>
 * @author <a href="mailto:hdn.jesse@cflex.com.br">hdn.jesse</a>
 * @since 04/11/2010
 * @version $Revision: 1.1 $
 */
public class AtividadeRecuperarEmpilharVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nomePilha;
	private Cliente cliente;
	private List<MetaUsina> listaUsinas;
	private TipoProduto tipoProduto;
	private List<MetaMaquinaDoPatio> listaMaquinas;
	private List<MetaFiltragem> listaFiltragens;	
	private List<MetaBaliza> listaBalizas;
    private MetaCarga metaCarga;
	private Date dataInicio;
	private Date dataFim;
	private Produto produtoMovimentado;
	private SentidoEmpilhamentoRecuperacaoEnum sentidoRecuperacao;
	private SentidoEmpilhamentoRecuperacaoEnum sentidoEmpilhamento;
	private Atividade atividadeAnterior;
	private List<Campanha> campanhas;
	private Map<MetaUsina, Campanha> mapUsinaCampanha;
	private Map<MetaFiltragem, Campanha> mapFiltragemCampanha;
	public AtividadeRecuperarEmpilharVO() {
		
	}

	public String getNomePilha() {
		return nomePilha;
	}

	public void setNomePilha(String nomePilha) {
		this.nomePilha = nomePilha;
	}

	public List<MetaUsina> getListaUsinas() {
		if (listaUsinas == null) {
			listaUsinas = new ArrayList<MetaUsina>();
		}
		return listaUsinas;
	}

	public void setListaUsinas(List<MetaUsina> listaUsinas) {
		this.listaUsinas = listaUsinas;
	}

	public TipoProduto getTipoProduto() {
		return tipoProduto;
	}

	public void setTipoProduto(TipoProduto tipoProduto) {
		this.tipoProduto = tipoProduto;
	}

	public List<MetaMaquinaDoPatio> getListaMaquinas() {
		if (listaMaquinas == null) {
			listaMaquinas = new ArrayList<MetaMaquinaDoPatio>();
		}
		return listaMaquinas;
	}

	public void setListaMaquinas(List<MetaMaquinaDoPatio> listaMaquinas) {
		this.listaMaquinas = listaMaquinas;
	}

	public List<MetaBaliza> getListaBalizas() {
		if (listaBalizas == null) {
			listaBalizas = new ArrayList<MetaBaliza>();
		}
		return listaBalizas;
	}

	public void setListaBalizas(List<MetaBaliza> listaBalizas) {
		this.listaBalizas = listaBalizas;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}
	
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
   
    public SentidoEmpilhamentoRecuperacaoEnum getSentidoRecuperacao()
   {
      return sentidoRecuperacao;
   }

   public void setSentidoRecuperacao(SentidoEmpilhamentoRecuperacaoEnum sentidoRecuperacao)
   {
      this.sentidoRecuperacao = sentidoRecuperacao;
   }

   public MetaCarga getMetaCarga() {
        return metaCarga;
    }

    public void setMetaCarga(MetaCarga metaCarga) {
        this.metaCarga = metaCarga;
    }

    public SentidoEmpilhamentoRecuperacaoEnum getSentidoEmpilhamento() {
        return sentidoEmpilhamento;
    }

    public void setSentidoEmpilhamento(SentidoEmpilhamentoRecuperacaoEnum sentidoEmpilhamento) {
        this.sentidoEmpilhamento = sentidoEmpilhamento;
    }

    public Produto getProdutoMovimentado() {
        return produtoMovimentado;
    }

    public void setProdutoMovimentado(Produto produtoMovimentado) {
        this.produtoMovimentado = produtoMovimentado;
    }

    public Atividade getAtividadeAnterior() {
        return atividadeAnterior;
    }

    public void setAtividadeAnterior(Atividade atividadeAnterior) {
        this.atividadeAnterior = atividadeAnterior;
    }

    public List<MetaFiltragem> getListaFiltragens() {
        if (listaFiltragens == null) {
            listaFiltragens = new ArrayList<MetaFiltragem>();
        }
        return listaFiltragens;
    }

    public void setListaFiltragens(List<MetaFiltragem> listaFiltragens) {
        this.listaFiltragens = listaFiltragens;
    }

    public List<Campanha> getCampanhas() {
        return campanhas;
    }

    public void setCampanhas(List<Campanha> campanhas) {
        this.campanhas = campanhas;
    }

    public Map<MetaUsina, Campanha> getMapUsinaCampanha() {
        return mapUsinaCampanha;
    }

    public void setMapUsinaCampanha(Map<MetaUsina, Campanha> mapUsinaCampanha) {
        if (mapUsinaCampanha == null) mapUsinaCampanha = new HashMap<MetaUsina, Campanha>();
        this.mapUsinaCampanha = mapUsinaCampanha;
    }

    public Map<MetaFiltragem, Campanha> getMapFiltragemCampanha() {
        if (mapFiltragemCampanha == null) mapFiltragemCampanha = new HashMap<MetaFiltragem, Campanha>();
        return mapFiltragemCampanha;
    }

    public void setMapFiltragemCampanha(Map<MetaFiltragem, Campanha> mapFiltragemCampanha) {
        this.mapFiltragemCampanha = mapFiltragemCampanha;
    }

   
		
}
