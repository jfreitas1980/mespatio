package com.hdntec.gestao.integracao.facade.mes.impl;

import java.util.Date;

import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.integracao.integracaoPIMS.IntegracaoSistemaPIMS;


public class PIMSFacadeImpl implements com.hdntec.gestao.integracao.facade.mes.PIMSFacade {

	@Override
	public Double calcular(Date inicio, Date fim, String tag) throws ErroSistemicoException {
		// TODO Auto-generated method stub
		IntegracaoSistemaPIMS obj= new IntegracaoSistemaPIMS();
		Double result = obj.recuperarQuantidadePIMS(inicio, fim, tag);
		obj = null;
		return result;
	}

}
