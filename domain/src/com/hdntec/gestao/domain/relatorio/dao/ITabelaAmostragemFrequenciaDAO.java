package com.hdntec.gestao.domain.relatorio.dao;

import java.util.List;

import com.hdntec.gestao.domain.relatorio.TabelaAmostragemFrequencia;
import com.hdntec.gestao.exceptions.ErroSistemicoException;


/**
 * 
 * <P><B>Description :</B><BR>
 * General ITabelaAmostragemFrequenciaDAO
 * </P>
 * <P>
 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
 * @since 28/08/2009
 * @version $Revision: 1.1 $
 */
public interface ITabelaAmostragemFrequenciaDAO {
	
	/**
	 * 
	 * buscarPorExemplo
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 28/08/2009
	 * @see
	 * @param tabela
	 * @return
	 * @throws ErroSistemicoException
	 * @return Returns the List<TabelaAmostragemFrequencia>.
	 */
	List<TabelaAmostragemFrequencia> buscarPorExemplo(TabelaAmostragemFrequencia tabela) throws ErroSistemicoException;
	
	/**
	 * 
	 * buscarTabelaAtual
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 28/08/2009
	 * @see
	 * @return
	 * @return Returns the TabelaAmostragemFrequencia.
	 */
	TabelaAmostragemFrequencia buscarTabelaAtual();
	
	/**
	 * 
	 * salvarTabelaAmostragemFrequencia
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 28/08/2009
	 * @see
	 * @param tabela
	 * @return
	 * @throws ErroSistemicoException
	 * @return Returns the TabelaAmostragemFrequencia.
	 */
	TabelaAmostragemFrequencia salvarTabelaAmostragemFrequencia(TabelaAmostragemFrequencia tabela) throws ErroSistemicoException;
	
	/**
	 * 
	 * removerTabelaAmostragemFrequencia
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 28/08/2009
	 * @see
	 * @param tabela
	 * @throws ErroSistemicoException
	 * @return Returns the void.
	 */
	void removerTabelaAmostragemFrequencia(TabelaAmostragemFrequencia tabela) throws ErroSistemicoException;
}
