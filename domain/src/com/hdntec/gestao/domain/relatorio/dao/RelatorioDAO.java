/**
 * Created on 17/06/2009
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

import com.hdntec.gestao.domain.relatorio.Relatorio;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;
import com.hdntec.gestao.hibernate.HibernateUtil;


/**
 * 
 * <P><B>Description :</B><BR>
 * General RelatorioDAO
 * </P>
 * <P>
 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
 * @since 18/06/2009
 * @version $Revision: 1.1 $
 */
public class RelatorioDAO extends AbstractGenericDAO<Relatorio> implements IRelatorioDAO {

	@Override
	public List<Relatorio> buscarPorExemplo(Relatorio relatorio)
			throws ErroSistemicoException {
		try {
			List<Relatorio> result = super.buscarListaDeObjetos(relatorio);
			return result;
		} catch (HibernateException hbEx) {
			hbEx.printStackTrace();
			throw new ErroSistemicoException(hbEx.getMessage());
		} finally {
//			super.encerrarSessao();
		}
	}
	
	public List<Relatorio> buscarRelatoriosPorPadrao(Long idPadraoRelatorio) 
			throws ErroSistemicoException {
		try {
			StringBuilder hql = new StringBuilder();
			hql.append("from Relatorio rel ");
			hql.append("where rel.padraoRelatorio.idPadraoRelatorio = :pId");
			Query query = HibernateUtil.getSession().createQuery(hql.toString());
			query.setParameter("pId", idPadraoRelatorio);
			List<Relatorio> result = query.list();
			return result;
		} catch (HibernateException hbEx) {
			hbEx.printStackTrace();
			throw new ErroSistemicoException(hbEx.getMessage());
		} finally {
//			super.encerrarSessao();
		}
	}

	@Override
	public List<Relatorio> buscarTodos() throws ErroSistemicoException {
		try {
			List<Relatorio> result = super.buscarListaDeObjetos(new Relatorio());
			return result;
		} catch (HibernateException hbEx) {
			hbEx.printStackTrace();
			throw new ErroSistemicoException(hbEx.getMessage());
		} finally {
//			super.encerrarSessao();
		}
	}
	
	@Override
	public Relatorio buscarRelatorio(Long id) throws ErroSistemicoException {
		try {
			/*Relatorio relatorio = new Relatorio();
			relatorio.setIdRelatorio(id);*/
			Relatorio relatorio = super.getObjeto(Relatorio.class, id);
			return relatorio;
		} catch (HibernateException hbEx) {
			hbEx.printStackTrace();
			throw new ErroSistemicoException(hbEx.getMessage());
		} finally {
//			super.encerrarSessao();
		}
	}

	@Override
	public void removerRelatorio(Relatorio relatorio)
			throws ErroSistemicoException {
		try {
			super.deletar(relatorio);
		} catch (HibernateException hbEx) {
			hbEx.printStackTrace();
			throw new ErroSistemicoException(hbEx.getMessage());
		} finally {
//			super.encerrarSessao();
		}
	}
	
	@Override
	public void removerRelatorios(List<Relatorio> relatorios)
			throws ErroSistemicoException {
		try {
			super.remover(relatorios);
		} catch (HibernateException hbEx) {
			hbEx.printStackTrace();
			throw new ErroSistemicoException(hbEx.getMessage());
		} finally {
//			super.encerrarSessao();
		}
	} 

	@Override
	public List<Relatorio> salvarLista(List<Relatorio> relatorios)
			throws ErroSistemicoException {
		try {
			List<Relatorio> result = super.salvar(relatorios);
            return result;
            
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        } finally {
//			super.encerrarSessao();
		}
	}

	@Override
	public Relatorio salvarRelatorio(Relatorio relatorio)
			throws ErroSistemicoException {
		try {
			relatorio = super.salvar(relatorio);
            return relatorio;
            
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        } finally {
//			super.encerrarSessao();
		}
	}

	
}
