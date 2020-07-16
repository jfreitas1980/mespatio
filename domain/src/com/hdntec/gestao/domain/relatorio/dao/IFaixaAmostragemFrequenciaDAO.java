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

import com.hdntec.gestao.domain.relatorio.FaixaAmostragemFrequencia;
import com.hdntec.gestao.exceptions.ErroSistemicoException;


/**
 * <P><B>Description :</B><BR>
 * General IFaixaAmostragemFrequenciaDAO
 * </P>
 * <P>
 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
 * @since 28/08/2009
 * @version $Revision: 1.1 $
 */
public interface IFaixaAmostragemFrequenciaDAO {

	/**
	 * 
	 * buscarPorExemplo
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 28/08/2009
	 * @see
	 * @param faixa
	 * @return
	 * @throws ErroSistemicoException
	 * @return Returns the List<FaixaAmostragemFrequencia>.
	 */
	List<FaixaAmostragemFrequencia> buscarPorExemplo(FaixaAmostragemFrequencia faixa) throws ErroSistemicoException;
	
	/**
	 * 
	 * salvarFaixaAmostragemFrequencia
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 28/08/2009
	 * @see
	 * @param faixa
	 * @return
	 * @throws ErroSistemicoException
	 * @return Returns the FaixaAmostragemFrequencia.
	 */
	FaixaAmostragemFrequencia salvarFaixaAmostragemFrequencia(FaixaAmostragemFrequencia faixa) throws ErroSistemicoException;
	
	/**
	 * 
	 * removerFaixaAmostragemFrequencia
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 28/08/2009
	 * @see
	 * @param faixa
	 * @throws ErroSistemicoException
	 * @return Returns the void.
	 */
	void removerFaixaAmostragemFrequencia(FaixaAmostragemFrequencia faixa) throws ErroSistemicoException;
}
