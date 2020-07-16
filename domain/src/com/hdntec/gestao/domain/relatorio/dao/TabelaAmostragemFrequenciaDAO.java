/**
 * Created on 28/08/2009
 * Project : CFlexRS
 *
 * Copyright © 2008 CFLEX.
 * Brasil
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of CFLEX. 
 * 
 * $Id: codetemplates.xml,v 1.1 2008/02/14 18:38:19 Exp $
 */

package com.hdntec.gestao.domain.relatorio.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.hdntec.gestao.domain.relatorio.TabelaAmostragemFrequencia;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;
import com.hdntec.gestao.hibernate.HibernateUtil;


/**
 * <P><B>Description :</B><BR>
 * General TabelaAmostragemFrequenciaDAO
 * </P>
 * <P>
 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
 * @since 28/08/2009
 * @version $Revision: 1.1 $
 */
public class TabelaAmostragemFrequenciaDAO extends AbstractGenericDAO<TabelaAmostragemFrequencia> implements ITabelaAmostragemFrequenciaDAO {

	@Override
	public List<TabelaAmostragemFrequencia> buscarPorExemplo(
			TabelaAmostragemFrequencia tabela) throws ErroSistemicoException {
		try {
			List<TabelaAmostragemFrequencia> result = super.buscarListaDeObjetos(tabela);
			return result;
		} catch (HibernateException hbEx) {
			hbEx.printStackTrace();
			throw new ErroSistemicoException(hbEx);
		} finally {
//			super.encerrarSessao();
		}
	}
	
	@Override
	public TabelaAmostragemFrequencia buscarTabelaAtual() {
		TabelaAmostragemFrequencia tabela = null;
    	Session session =  HibernateUtil.getSession();
		StringBuilder sSql = new StringBuilder();
		sSql.append("Select distinct t from TabelaAmostragemFrequencia t "); 
		sSql.append(" order by t.data desc");
		Query q = session.createQuery(sSql.toString());
		q.setMaxResults(1);
		List<TabelaAmostragemFrequencia> lista = q.list();
		if (!lista.isEmpty()) {
			tabela = lista.get(0);
		}
//		super.encerrarSessao();
		return tabela;
	}

	@Override
	public void removerTabelaAmostragemFrequencia(
			TabelaAmostragemFrequencia tabela) throws ErroSistemicoException {
		try {
			super.deletar(tabela);
		} catch (HibernateException hbEx) {
			hbEx.printStackTrace();
			throw new ErroSistemicoException(hbEx);
		} finally {
//			super.encerrarSessao();
		}
	}

	@Override
	public TabelaAmostragemFrequencia salvarTabelaAmostragemFrequencia(
			TabelaAmostragemFrequencia tabela) throws ErroSistemicoException {
		try {
			tabela = super.salvar(tabela);
            return tabela;
            
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx);
        } finally {
//			super.encerrarSessao();
		}
	}
}
