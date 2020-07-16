package com.hdntec.gestao.integracao.relatorio.controladores;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.hdntec.gestao.domain.produto.entity.TipoItemDeControle;
import com.hdntec.gestao.domain.relatorio.CampoRelatorio;
import com.hdntec.gestao.domain.relatorio.PadraoRelatorio;
import com.hdntec.gestao.domain.relatorio.Relatorio;
import com.hdntec.gestao.domain.relatorio.enums.EnumCamposRelatorio;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.integracao.produto.ControladorProduto;
import com.hdntec.gestao.integracao.produto.IControladorProduto;


@Stateless(mappedName = "bs/ControladorGestaoRelatorio")
public class ControladorGestaoRelatorio implements IControladorGestaoRelatorio {

	@EJB
	private IControladorRelatorio controladorRelatorio;
	@EJB
	private IControladorPadraoRelatorio controladorPadraoRelatorio;
	@EJB
	private IControladorCampoRelatorio controladorCampoRelatorio;
    @EJB
    private IControladorProduto controladorProduto;
    
	@Override
	public void atualizarCamposRelatorio() throws ErroSistemicoException {
		
		//Pegando todos os campos do Enum.
		List<String> result = EnumCamposRelatorio.getValues();
		
		//Buscando todos os campos do TipoItemControle
		List<TipoItemDeControle> tipoItensDeControle = getControladorProduto().buscarTiposItemControle();
		for(TipoItemDeControle tipo : tipoItensDeControle) {
    		result.add(tipo.getDescricaoTipoItemControle());
    	}
		
    	//Buscar todos os campos ja gravados na base
    	List<CampoRelatorio> camposGravados = buscarCampos();
    	
		//Criando uma nova lista de CampoRelatorio
		List<CampoRelatorio> campoRelatorioList = new ArrayList<CampoRelatorio>();
    	for(String nome : result) {
    		if (nome.length()>60) {
    			nome = nome.substring(0, 61);
    		}
    		if (!nomeJaExistente(camposGravados, nome)) {
        		CampoRelatorio campo = new CampoRelatorio();
        		campo.setNomeCampo(nome);
        		campoRelatorioList.add(campo);
    		}
    	}
    	
    	//Gravando os campos novos.
    	if (!campoRelatorioList.isEmpty()) {
    		salvarListaDeCampoRelatorio(campoRelatorioList);
    	}
	}

	@Override
	public CampoRelatorio buscarCampoRelatorioPorId(Long id)
			throws ErroSistemicoException {
		if (controladorCampoRelatorio == null) {
			controladorCampoRelatorio = new ControladorCampoRelatorio();
		}
		return controladorCampoRelatorio.buscarPorId(id);
	}
	
	@Override
	public List<CampoRelatorio> buscarCampos() throws ErroSistemicoException {
		if (controladorCampoRelatorio == null) {
			controladorCampoRelatorio = new ControladorCampoRelatorio();
		}
		return controladorCampoRelatorio.buscarTodos();
	}
	
	@Override
	public List<CampoRelatorio> buscarCamposPorFiltro(CampoRelatorio filtro)
			throws ErroSistemicoException {
		if (controladorCampoRelatorio == null) {
			controladorCampoRelatorio = new ControladorCampoRelatorio();
		}
		return controladorCampoRelatorio.buscarPorExemplo(filtro);
	}

	@Override
	public PadraoRelatorio buscarPadraoRelatorioPorId(Long id)
			throws ErroSistemicoException {
		if (controladorPadraoRelatorio == null) {
			controladorPadraoRelatorio = new ControladorPadraoRelatorio();
		}
		return controladorPadraoRelatorio.buscarPorId(id);
	}
	
	@Override
	public List<PadraoRelatorio> buscarPadroesPorFiltro(PadraoRelatorio filtro)
			throws ErroSistemicoException {
		if (controladorPadraoRelatorio == null) {
			controladorPadraoRelatorio = new ControladorPadraoRelatorio();
		}
		return controladorPadraoRelatorio.buscarListaDePadraoRelatorio(filtro);
	}
	
	@Override
	public List<PadraoRelatorio> buscarPadroesRelatorio()
			throws ErroSistemicoException {
		if (controladorPadraoRelatorio == null) {
			controladorPadraoRelatorio = new ControladorPadraoRelatorio();
		}
		return controladorPadraoRelatorio.buscarTodos();
	}

	@Override
	public Relatorio buscarRelatorioPorId(Long id)
			throws ErroSistemicoException {
		if (controladorRelatorio == null) {
			controladorRelatorio = new ControladorRelatorio();
		}
		return controladorRelatorio.buscarPorId(id);
	}
	
	@Override
	public List<Relatorio> buscarRelatoriosPorFiltro(Relatorio filtro)
			throws ErroSistemicoException {
		if (controladorRelatorio == null) {
			controladorRelatorio = new ControladorRelatorio();
		}
		return controladorRelatorio.buscarListaDeRelatorio(filtro);
	}
	
	@Override
	public List<Relatorio> buscarRelatoriosPorPadrao(
			PadraoRelatorio padraoRelatorio) throws ErroSistemicoException {
		if (controladorRelatorio == null) {
			controladorRelatorio = new ControladorRelatorio();
		}
		return controladorRelatorio.buscarRelatoriosPorPadrao(padraoRelatorio);
	}
	
	@Override
	public List<Relatorio> buscarRelatorios() throws ErroSistemicoException {
		if (controladorRelatorio == null) {
			controladorRelatorio = new ControladorRelatorio();
		}
		return controladorRelatorio.buscarTodos();
	}

	@Override
	public void removerCampoRelatorio(CampoRelatorio campoRelatorio)
			throws ErroSistemicoException {
		if (controladorCampoRelatorio == null) {
			controladorCampoRelatorio = new ControladorCampoRelatorio();
		}
		controladorCampoRelatorio.removerCampoRelatorio(campoRelatorio);
	}

	@Override
	public void removerPadraoRelatorio(PadraoRelatorio padraoRelatorio)
			throws ErroSistemicoException {
		if (controladorPadraoRelatorio == null) {
			controladorPadraoRelatorio = new ControladorPadraoRelatorio();
		}
		controladorPadraoRelatorio.removerPadraoRelatorio(padraoRelatorio);
	}

	@Override
	public void removerRelatorio(Relatorio relatorio)
			throws ErroSistemicoException {
		if (controladorRelatorio == null) {
			controladorRelatorio = new ControladorRelatorio();
		}
		controladorRelatorio.removerRelatorio(relatorio);
	}

	@Override
	public void removerRelatorios(List<Relatorio> relatorios)
			throws ErroSistemicoException {
		if (controladorRelatorio == null) {
			controladorRelatorio = new ControladorRelatorio();
		}
		controladorRelatorio.removerRelatorios(relatorios);
	}
	
	@Override
	public CampoRelatorio salvarCampoRelatorio(CampoRelatorio campoRelatorio)
			throws ErroSistemicoException {
		if (controladorCampoRelatorio == null) {
			controladorCampoRelatorio = new ControladorCampoRelatorio();
		}
		return controladorCampoRelatorio.salvarCampoRelatorio(campoRelatorio);
	}

	@Override
	public List<CampoRelatorio> salvarListaDeCampoRelatorio(
			List<CampoRelatorio> campos) throws ErroSistemicoException {
		if (controladorCampoRelatorio == null) {
			controladorCampoRelatorio = new ControladorCampoRelatorio();
		}
		return controladorCampoRelatorio.salvarLista(campos);
	}

	@Override
	public PadraoRelatorio salvarPadraoRelatorio(PadraoRelatorio padraoRelatorio)
			throws ErroSistemicoException {
		if (controladorPadraoRelatorio == null) {
			controladorPadraoRelatorio = new ControladorPadraoRelatorio();
		}
		return controladorPadraoRelatorio.salvarPadraoRelatorio(padraoRelatorio);
	}

	@Override
	public Relatorio salvarRelatorio(Relatorio relatorio)
			throws ErroSistemicoException {
		if (controladorRelatorio == null) {
			controladorRelatorio = new ControladorRelatorio();
		}
		return controladorRelatorio.salvarRelatorio(relatorio);
	}

	
	/**
	 * 
	 * nomeJaExistente
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 18/06/2009
	 * @see
	 * @param campos
	 * @param nome
	 * @return
	 * @return Returns the boolean.
	 */
	private boolean nomeJaExistente(List<CampoRelatorio> campos, String nome) {
		for(CampoRelatorio campo : campos) {
    		if (campo.getNomeCampo().equalsIgnoreCase(nome)) {
    			return true;
    		}
    	}
		return false;
	}

    public IControladorRelatorio getControladorRelatorio() {
        if (controladorRelatorio == null) {
            controladorRelatorio = new ControladorRelatorio();
        }
        return controladorRelatorio;
    }

    public void setControladorRelatorio(IControladorRelatorio controladorRelatorio) {
        this.controladorRelatorio = controladorRelatorio;
    }

    public IControladorPadraoRelatorio getControladorPadraoRelatorio() {
        if (controladorPadraoRelatorio == null) {
            controladorPadraoRelatorio = new ControladorPadraoRelatorio();
        }
        return controladorPadraoRelatorio;
    }

    public void setControladorPadraoRelatorio(IControladorPadraoRelatorio controladorPadraoRelatorio) {
        this.controladorPadraoRelatorio = controladorPadraoRelatorio;
    }

    public IControladorCampoRelatorio getControladorCampoRelatorio() {
        if (controladorCampoRelatorio == null) {
            controladorCampoRelatorio = new ControladorCampoRelatorio();
        }
        return controladorCampoRelatorio;
    }

    public void setControladorCampoRelatorio(IControladorCampoRelatorio controladorCampoRelatorio) {
        this.controladorCampoRelatorio = controladorCampoRelatorio;
    }

    public IControladorProduto getControladorProduto() {
        if (controladorProduto == null) {
            controladorProduto = new ControladorProduto();
        }
    
        return controladorProduto;
    }

    public void setControladorProduto(IControladorProduto controladorProduto) {
        this.controladorProduto = controladorProduto;
    }
}
