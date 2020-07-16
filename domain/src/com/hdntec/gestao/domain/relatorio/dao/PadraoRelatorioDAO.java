package com.hdntec.gestao.domain.relatorio.dao;

import java.util.List;

import org.hibernate.HibernateException;

import com.hdntec.gestao.domain.relatorio.PadraoRelatorio;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;


public class PadraoRelatorioDAO extends AbstractGenericDAO<PadraoRelatorio> implements IPadraoRelatorioDAO {

	@Override
	public List<PadraoRelatorio> buscarPorExemploPadraoRelatorio(
			PadraoRelatorio padraoRelatorio) throws ErroSistemicoException {
		try {
			List<PadraoRelatorio> result = super.buscarListaDeObjetos(padraoRelatorio);
			return result;
		} catch (HibernateException hbEx) {
			hbEx.printStackTrace();
			throw new ErroSistemicoException(hbEx.getMessage());
		} finally {
//			super.encerrarSessao();
		}
	}

	@Override
	public List<PadraoRelatorio> buscarTodos() throws ErroSistemicoException {
		try {
			List<PadraoRelatorio> result = super.buscarListaDeObjetos(new PadraoRelatorio());
//			super.encerrarSessao();
			return result;
		} catch (HibernateException hbEx) {
			hbEx.printStackTrace();
			throw new ErroSistemicoException(hbEx.getMessage());
		} finally {
//			super.encerrarSessao();
		}
	}
	
	@Override
	public PadraoRelatorio buscarPadraoRelatorio(Long id)
			throws ErroSistemicoException {
		try {
			/*PadraoRelatorio padrao = new PadraoRelatorio();
			padrao.setIdPadraoRelatorio(id);*/
			PadraoRelatorio padrao = super.getObjeto(PadraoRelatorio.class, id);
			return padrao;
		} catch (HibernateException hbEx) {
			hbEx.printStackTrace();
			throw new ErroSistemicoException(hbEx.getMessage());
		} finally {
//			super.encerrarSessao();
		}
	}

	@Override
	public void removerPadraoRelatorio(PadraoRelatorio padraoRelatorio)
			throws ErroSistemicoException {
		try {
			super.deletar(padraoRelatorio);
		} catch (HibernateException hbEx) {
			hbEx.printStackTrace();
			throw new ErroSistemicoException(hbEx.getMessage());
		} finally {
//			super.encerrarSessao();
		}
	}

	@Override
	public PadraoRelatorio salvarPadraoRelatorio(PadraoRelatorio padraoRelatorio)
			throws ErroSistemicoException {
		
		try {
			padraoRelatorio = super.salvar(padraoRelatorio);
            return padraoRelatorio;
            
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        } finally {
//			super.encerrarSessao();
		}
	}
	
	@Override
	public void atualizarPadraoRelatorio(PadraoRelatorio padraoRelatorio)
			throws ErroSistemicoException {
		try {
			super.atualizar(padraoRelatorio);
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        } finally {
//			super.encerrarSessao();
		}
		
	}
	
}
