package com.hdntec.gestao.domain.relatorio.dao;

import java.util.List;

import com.hdntec.gestao.domain.relatorio.PadraoRelatorio;
import com.hdntec.gestao.exceptions.ErroSistemicoException;


/**
 * 
 * <P><B>Description :</B><BR>
 * General IPadraoRelatorioDAO
 * </P>
 * <P>
 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
 * @since 16/06/2009
 * @version $Revision: 1.1 $
 */
public interface IPadraoRelatorioDAO {

    /**
     * 
     * salvarPadraoRelatorio
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 16/06/2009
     * @see
     * @param padraoRelatorio
     * @return
     * @throws ErroSistemicoException
     * @return Returns the PadraoRelatorio.
     */
    PadraoRelatorio salvarPadraoRelatorio(PadraoRelatorio padraoRelatorio) throws ErroSistemicoException;
    
    /**
     * 
     * atualizarPadraoRelatorio
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 19/06/2009
     * @see
     * @param padraoRelatorio
     * @throws ErroSistemicoException
     * @return Returns the void.
     */
    void atualizarPadraoRelatorio(PadraoRelatorio padraoRelatorio) throws ErroSistemicoException;

    /**
     * 
     * removerPadraoRelatorio
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 16/06/2009
     * @see
     * @param padraoRelatorio
     * @throws ErroSistemicoException
     * @return Returns the void.
     */
    void removerPadraoRelatorio(PadraoRelatorio padraoRelatorio) throws ErroSistemicoException;

    /**
     * 
     * buscaPorExemploPadraoRelatorio
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 16/06/2009
     * @see
     * @param padraoRelatorio
     * @return
     * @throws ErroSistemicoException
     * @return Returns the List<PadraoRelatorio>.
     */
    List<PadraoRelatorio> buscarPorExemploPadraoRelatorio(PadraoRelatorio padraoRelatorio) throws ErroSistemicoException;
    
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
     * buscarPadraoRelatorio
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 19/06/2009
     * @see
     * @param id
     * @return
     * @throws ErroSistemicoException
     * @return Returns the PadraoRelatorio.
     */
    PadraoRelatorio buscarPadraoRelatorio(Long id) throws ErroSistemicoException;
}