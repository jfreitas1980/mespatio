package com.hdntec.gestao.integracao.produto;

import java.util.List;

import com.hdntec.gestao.domain.produto.entity.TipoItemDeControle;
import com.hdntec.gestao.exceptions.ErroSistemicoException;


public interface IControladorTipoItemDeControle {

	public TipoItemDeControle salvaTipoItemDeControle(TipoItemDeControle tipoItemDeControle) throws ErroSistemicoException;

	public List<TipoItemDeControle> buscarTipoItemDeControle(TipoItemDeControle tipoItemDeControle) throws ErroSistemicoException;
	
	public void removeTipoItemDeControle(TipoItemDeControle tipoItemDeControle)	throws ErroSistemicoException;
	public List<TipoItemDeControle> buscarTodosItemDeControle() throws ErroSistemicoException;
	
	public void encerrarSessao();
}
