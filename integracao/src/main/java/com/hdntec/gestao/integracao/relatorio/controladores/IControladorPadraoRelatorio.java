package com.hdntec.gestao.integracao.relatorio.controladores;

import java.util.List;

import javax.ejb.Local;

import com.hdntec.gestao.domain.relatorio.PadraoRelatorio;
import com.hdntec.gestao.exceptions.ErroSistemicoException;


/**
 * 
 * <P><B>Description :</B><BR>
 * General IControladorPadraoRelatorio
 * </P>
 * <P>
 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
 * @since 18/06/2009
 * @version $Revision: 1.1 $
 */
@Local
public interface IControladorPadraoRelatorio {
    
	/**
	 * 
	 * salvarPadraoRelatorio
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 18/06/2009
	 * @see
	 * @param padraoRelatorio
	 * @return
	 * @throws ErroSistemicoException
	 * @return Returns the PadraoRelatorio.
	 */
    PadraoRelatorio salvarPadraoRelatorio(PadraoRelatorio padraoRelatorio) throws ErroSistemicoException;

    /**
     * 
     * removerPadraoRelatorio
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 18/06/2009
     * @see
     * @param padraoRelatorio
     * @throws ErroSistemicoException
     * @return Returns the void.
     */
    void removerPadraoRelatorio(PadraoRelatorio padraoRelatorio) throws ErroSistemicoException;
    
    /**
     * 
     * buscarListaDePadraoRelatorio
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 18/06/2009
     * @see
     * @param padraoRelatorio
     * @return
     * @throws ErroSistemicoException
     * @return Returns the List<PadraoRelatorio>.
     */
    List<PadraoRelatorio> buscarListaDePadraoRelatorio(PadraoRelatorio padraoRelatorio) throws ErroSistemicoException;
    
    /**
     * 
     * buscarTodos
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 18/06/2009
     * @see
     * @return
     * @throws ErroSistemicoException
     * @return Returns the List<PadraoRelatorio>.
     */
    List<PadraoRelatorio> buscarTodos() throws ErroSistemicoException;
 
    /**
     * 
     * buscarPorId
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 19/06/2009
     * @see
     * @param id
     * @return
     * @throws ErroSistemicoException
     * @return Returns the PadraoRelatorio.
     */
    PadraoRelatorio buscarPorId(Long id) throws ErroSistemicoException;
}