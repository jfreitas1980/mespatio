package com.hdntec.gestao.domain.relatorio.dao;

import java.util.List;

import com.hdntec.gestao.domain.relatorio.Relatorio;
import com.hdntec.gestao.exceptions.ErroSistemicoException;


/**
 * 
 * <P><B>Description :</B><BR>
 * General IRelatorioDAO
 * </P>
 * <P>
 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
 * @since 18/06/2009
 * @version $Revision: 1.1 $
 */
public interface IRelatorioDAO {

	/**
	 * 
	 * buscarRelatoriosPorPadrao
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 30/06/2009
	 * @see
	 * @param idPadraoRelatorio
	 * @return
	 * @throws ErroSistemicoException
	 * @return Returns the List<Relatorio>.
	 */
	List<Relatorio> buscarRelatoriosPorPadrao(Long idPadraoRelatorio) throws ErroSistemicoException;
	
	/**
	 * 
	 * salvarRelatorio
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 18/06/2009
	 * @see
	 * @param relatorio
	 * @return
	 * @throws ErroSistemicoException
	 * @return Returns the Relatorio.
	 */
    Relatorio salvarRelatorio(Relatorio relatorio) throws ErroSistemicoException;

    /**
     * 
     * salvarLista
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 18/06/2009
     * @see
     * @param relatorios
     * @return
     * @throws ErroSistemicoException
     * @return Returns the List<Relatorio>.
     */
    List<Relatorio> salvarLista(List<Relatorio> relatorios) throws ErroSistemicoException;
    
    /**
     * 
     * removerRelatorio
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 18/06/2009
     * @see
     * @param relatorio
     * @throws ErroSistemicoException
     * @return Returns the void.
     */
    void removerRelatorio(Relatorio relatorio) throws ErroSistemicoException;
    
    /**
     * 
     * removerRelatorios
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 02/07/2009
     * @see
     * @param relatorios
     * @throws ErroSistemicoException
     * @return Returns the void.
     */
    void removerRelatorios(List<Relatorio> relatorios) throws ErroSistemicoException;
    
    /**
     * 
     * buscarTodos
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 18/06/2009
     * @see
     * @return
     * @throws ErroSistemicoException
     * @return Returns the List<Relatorio>.
     */
    List<Relatorio> buscarTodos() throws ErroSistemicoException;
    
    /**
     * 
     * buscarPorExemplo
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 18/06/2009
     * @see
     * @param relatorio
     * @return
     * @throws ErroSistemicoException
     * @return Returns the List<Relatorio>.
     */
    List<Relatorio> buscarPorExemplo(Relatorio relatorio) throws ErroSistemicoException;
    
    /**
     * 
     * buscarRelatorio
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 19/06/2009
     * @see
     * @param id
     * @return
     * @throws ErroSistemicoException
     * @return Returns the Relatorio.
     */
    Relatorio buscarRelatorio(Long id) throws ErroSistemicoException;
}