package com.hdntec.gestao.integracao.facade.mes;

import java.util.Date;

import com.hdntec.gestao.exceptions.ErroSistemicoException;


public interface PIMSFacade {
  
	public Double calcular(Date inicio, Date fim, String tag) throws ErroSistemicoException;
}
