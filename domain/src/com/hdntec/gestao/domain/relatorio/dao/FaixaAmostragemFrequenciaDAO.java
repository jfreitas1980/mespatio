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

import com.hdntec.gestao.domain.relatorio.FaixaAmostragemFrequencia;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;


/**
 * <P><B>Description :</B><BR>
 * General FaixaAmostragemFrequenciaDAO
 * </P>
 * <P>
 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
 * @since 28/08/2009
 * @version $Revision: 1.1 $
 */
public class FaixaAmostragemFrequenciaDAO extends AbstractGenericDAO<FaixaAmostragemFrequencia> implements IFaixaAmostragemFrequenciaDAO {

	@Override
	public List<FaixaAmostragemFrequencia> buscarPorExemplo(FaixaAmostragemFrequencia faixa)
			throws ErroSistemicoException {
		try {
			List<FaixaAmostragemFrequencia> result = super.buscarListaDeObjetos(faixa);
			return result;
		} catch (HibernateException hbEx) {
			hbEx.printStackTrace();
			throw new ErroSistemicoException(hbEx);
		} finally {
//			super.encerrarSessao();
		}
	}
	
	@Override
	public void removerFaixaAmostragemFrequencia(FaixaAmostragemFrequencia faixa)
			throws ErroSistemicoException {
		try {
			super.deletar(faixa);
		} catch (HibernateException hbEx) {
			hbEx.printStackTrace();
			throw new ErroSistemicoException(hbEx);
		} finally {
//			super.encerrarSessao();
		}
	}
	
	@Override
	public FaixaAmostragemFrequencia salvarFaixaAmostragemFrequencia(
			FaixaAmostragemFrequencia faixa) throws ErroSistemicoException {
		try {
			faixa = super.salvar(faixa);
            return faixa;
            
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx);
        } finally {
//			super.encerrarSessao();
		}
	}
	
}
