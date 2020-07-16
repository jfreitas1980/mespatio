package com.hdntec.gestao.integracao.produto;

import java.util.List;

import javax.ejb.Local;

import com.hdntec.gestao.domain.produto.entity.MetaInterna;
import com.hdntec.gestao.exceptions.ErroSistemicoException;


@Local
public interface IControladorMetaInterna {
	
	public MetaInterna salvaMetaInterna(MetaInterna metaInterna) throws ErroSistemicoException;

	public List<MetaInterna> buscarMetaInterna(MetaInterna metaInterna) throws ErroSistemicoException;

	public List<MetaInterna> buscaMetaInternaPorFiltro(MetaInterna metaInterna) throws ErroSistemicoException;

	public void removeMetaInterna(MetaInterna metaInterna) throws ErroSistemicoException;
}
