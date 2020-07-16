package com.hdntec.gestao.integracao.relatorio.controladores;

import java.util.List;

import javax.ejb.Local;

import com.hdntec.gestao.domain.relatorio.PadraoRelatorio;
import com.hdntec.gestao.domain.relatorio.Relatorio;
import com.hdntec.gestao.exceptions.ErroSistemicoException;


/**
 * 
 * <P><B>Description :</B><BR>
 * General IControladorRelatorio
 * </P>
 * <P>
 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
 * @since 18/06/2009
 * @version $Revision: 1.1 $
 */
@Local
public interface IControladorRelatorio {

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
     * salvarListaDeRelatorio
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 18/06/2009
     * @see
     * @param relatorios
     * @return
     * @throws ErroSistemicoException
     * @return Returns the List<Relatorio>.
     */
    List<Relatorio> salvarListaDeRelatorio(List<Relatorio> relatorios) throws ErroSistemicoException;
    
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
     * buscarListaDeRelatorio
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 18/06/2009
     * @see
     * @param relatorio
     * @return
     * @throws ErroSistemicoException
     * @return Returns the List<Relatorio>.
     */
    List<Relatorio> buscarListaDeRelatorio(Relatorio relatorio) throws ErroSistemicoException;
    
    /**
     * 
     * buscarPorId
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 19/06/2009
     * @see
     * @param id
     * @return
     * @throws ErroSistemicoException
     * @return Returns the Relatorio.
     */
    Relatorio buscarPorId(Long id) throws ErroSistemicoException;
    
    /**
     * 
     * buscarRelatoriosPorPadrao
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 30/06/2009
     * @see
     * @param padraoRelatorio
     * @return
     * @throws ErroSistemicoException
     * @return Returns the List<Relatorio>.
     */
    List<Relatorio> buscarRelatoriosPorPadrao(PadraoRelatorio padraoRelatorio) throws ErroSistemicoException;
}
