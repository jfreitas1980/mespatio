package com.hdntec.gestao.cliente.integracao.facade.mes.impl;

import java.util.Date;

import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.integracao.integracaoPIMS.IntegracaoSistemaPIMS;


public class PIMSFacadeImpl implements com.hdntec.gestao.integracao.facade.mes.PIMSFacade {

	@Override
	public Double calcular(Date inicio, Date fim, String tag) throws ErroSistemicoException {
		// TODO Auto-generated method stub
		IntegracaoSistemaPIMS obj= new IntegracaoSistemaPIMS();
		try {
			Double result = obj.recuperarQuantidadePIMS(inicio, fim, tag);
			return result;
		} catch(ErroSistemicoException ex) {
			throw new ErroSistemicoException("Erro de integração com sistema PIMS",ex);
		} finally {
			obj = null;
		}
	}

}
