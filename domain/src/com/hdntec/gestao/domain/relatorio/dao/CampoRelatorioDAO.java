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

import com.hdntec.gestao.domain.relatorio.CampoRelatorio;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;


/**
 * 
 * <P><B>Description :</B><BR>
 * General CampoRelatorioDAO
 * </P>
 * <P>
 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
 * @since 17/06/2009
 * @version $Revision: 1.1 $
 */
public class CampoRelatorioDAO extends AbstractGenericDAO<CampoRelatorio> 
											implements ICampoRelatorioDAO {

	@Override
	public List<CampoRelatorio> buscarTodos()
			throws ErroSistemicoException {
		
		try {
			List<CampoRelatorio> result = super.buscarListaDeObjetos(new CampoRelatorio());
			return result;			
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
        finally {
//        	super.encerrarSessao();
        }
	}
	
	@Override
	public List<CampoRelatorio> buscarPorExemplo(CampoRelatorio campoRelatorio)
		throws ErroSistemicoException {
		
		try {
			List<CampoRelatorio> result = super.buscarListaDeObjetos(campoRelatorio);
			return result;			
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
        finally {
//        	super.encerrarSessao();
        }
	}
	
	@Override
	public CampoRelatorio buscarCampoRelatorio(Long id)
	throws ErroSistemicoException {
		try {
			CampoRelatorio campo = super.getObjeto(CampoRelatorio.class, id);
			return campo;
		} catch (HibernateException hbEx) {
			hbEx.printStackTrace();
			throw new ErroSistemicoException(hbEx.getMessage());
		}
        finally {
//        	super.encerrarSessao();
        }
	}

	@Override
	public void removerCampoRelatorio(
			CampoRelatorio campoRelatorio)
			throws ErroSistemicoException {
        try {
            super.deletar(campoRelatorio);
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        } finally {
//        	super.encerrarSessao();
        }
	}

	@Override
	public CampoRelatorio salvarCampoRelatorio(
			CampoRelatorio campoRelatorio)
			throws ErroSistemicoException {

		try {
			campoRelatorio = super.salvar(campoRelatorio);
            return campoRelatorio;
            
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        } finally {
//        	super.encerrarSessao();
        }
	}
	
	@Override
	public List<CampoRelatorio> salvarLista(
			List<CampoRelatorio> campos) throws ErroSistemicoException {

		try {
			campos = super.salvar(campos);
            return campos;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        } finally {
//        	super.encerrarSessao();
        }
	}


	@Override
	public void atualizarCampoRelatorio(CampoRelatorio campoRelatorio) 
		throws ErroSistemicoException {
		try {
			super.atualizar(campoRelatorio);
		} catch (HibernateException hbEx) {
			hbEx.printStackTrace();
			throw new ErroSistemicoException(hbEx.getMessage());
		} finally {
//        	super.encerrarSessao();
        }
	}
}