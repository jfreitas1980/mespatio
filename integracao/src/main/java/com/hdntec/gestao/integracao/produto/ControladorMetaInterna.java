package com.hdntec.gestao.integracao.produto;

import java.util.List;

import javax.ejb.Stateless;

import com.hdntec.gestao.domain.produto.dao.MetaInternaDAO;
import com.hdntec.gestao.domain.produto.entity.MetaInterna;
import com.hdntec.gestao.exceptions.ErroSistemicoException;


@Stateless(name = "bs/ControladorMetaInterna", mappedName = "bs/ControladorMetaInterna")
public class ControladorMetaInterna implements IControladorMetaInterna{

	@Override
	public List<MetaInterna> buscarMetaInterna(MetaInterna metaInterna)
			throws ErroSistemicoException {
        MetaInternaDAO metaInternaDAO = new MetaInternaDAO();
        return metaInternaDAO.buscaPorExemploMetaInterna(metaInterna);
	}

	@Override
	public List<MetaInterna> buscaMetaInternaPorFiltro(MetaInterna metaInterna)
			throws ErroSistemicoException {
        MetaInternaDAO metaInternaDAO = new MetaInternaDAO();
        return metaInternaDAO.buscaMetaInternaPorFiltro(metaInterna);
	}

	@Override
	public MetaInterna salvaMetaInterna(MetaInterna metaInterna)
			throws ErroSistemicoException {
  	  	/*if (metaInterna.getIdMetaInterna()==null&&
  	  			metaInterna.getDataDeCadastro()==null) {
  	  		metaInterna.setDataDeCadastro( new Date(System.currentTimeMillis()));
  	  	}*/
		MetaInternaDAO metaInternaDAO = new MetaInternaDAO();
		MetaInterna metaInternaSalva = metaInternaDAO.salvarMetaInterna(metaInterna);
		return metaInternaSalva;
	}

	@Override
	public void removeMetaInterna(MetaInterna metaInterna)
			throws ErroSistemicoException {
		MetaInternaDAO metaInternaDAO = new MetaInternaDAO();
        metaInternaDAO.removerMetaInterna(metaInterna);
	}
}
