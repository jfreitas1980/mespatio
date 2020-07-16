package com.hdntec.gestao.integracao.produto;

import java.util.List;

import javax.ejb.Stateless;

import com.hdntec.gestao.domain.produto.dao.TipoItemDeControleDAO;
import com.hdntec.gestao.domain.produto.entity.TipoItemDeControle;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.HibernateUtil;


@Stateless(name = "bs/ControladorTipoItemDeControle", mappedName = "bs/ControladorTipoItemDeControle")
public class ControladorTipoItemDeControle implements IControladorTipoItemDeControle {

	@Override
	public List<TipoItemDeControle> buscarTipoItemDeControle(
			TipoItemDeControle tipoItemDeControle)
			throws ErroSistemicoException {
        TipoItemDeControleDAO tipoItemDeControleDAO = new TipoItemDeControleDAO();
        return tipoItemDeControleDAO.buscarListaDeObjetos(tipoItemDeControle);
	}

	@Override
	public void removeTipoItemDeControle(TipoItemDeControle tipoItemDeControle)
			throws ErroSistemicoException {
		TipoItemDeControleDAO tipoItemDeControleDAO = new TipoItemDeControleDAO();
        tipoItemDeControleDAO.removeTipoItemDeControle(tipoItemDeControle);
	}

	@Override
	public TipoItemDeControle salvaTipoItemDeControle(
			TipoItemDeControle tipoItemDeControle)
			throws ErroSistemicoException {
		TipoItemDeControleDAO tipoItemDeControleDAO = new TipoItemDeControleDAO();
		TipoItemDeControle tipoItemDeControleSalva = tipoItemDeControleDAO.salvaTipoItemDeControle(tipoItemDeControle);
		return tipoItemDeControleSalva;
	}

	@Override
	public List<TipoItemDeControle> buscarTodosItemDeControle()
			throws ErroSistemicoException {
        TipoItemDeControleDAO tipoItemDeControleDAO = new TipoItemDeControleDAO();
        return tipoItemDeControleDAO.buscarTodos();
	}
	//TODO Somente teste, remover daqui.
	public void encerrarSessao() {
		HibernateUtil.closeSession();
	}
}
