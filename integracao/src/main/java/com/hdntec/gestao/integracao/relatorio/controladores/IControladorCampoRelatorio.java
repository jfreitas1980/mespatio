package com.hdntec.gestao.integracao.relatorio.controladores;

import java.util.List;

import javax.ejb.Local;

import com.hdntec.gestao.domain.relatorio.CampoRelatorio;
import com.hdntec.gestao.exceptions.ErroSistemicoException;


/**
 * 
 * <P><B>Description :</B><BR>
 * General IControladorCampoRelatorioRemote
 * </P>
 * <P>
 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
 * @since 17/06/2009
 * @version $Revision: 1.1 $
 */
@Local
public interface IControladorCampoRelatorio {

	/**
	 * 
	 * salvarCampoRelatorio
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 17/06/2009
	 * @see
	 * @param campoRelatorio
	 * @return
	 * @throws ErroSistemicoException
	 * @return Returns the CampoRelatorio.
	 */
    CampoRelatorio salvarCampoRelatorio(CampoRelatorio campoRelatorio) throws ErroSistemicoException;

    /**
     * 
     * salvarLista
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 17/06/2009
     * @see
     * @param campos
     * @return
     * @throws ErroSistemicoException
     * @return Returns the List<CampoRelatorio>.
     */
    List<CampoRelatorio> salvarLista(List<CampoRelatorio> campos) throws ErroSistemicoException;
    
    /**
     * 
     * removerCampoRelatorio
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 17/06/2009
     * @see
     * @param campoRelatorio
     * @throws ErroSistemicoException
     * @return Returns the void.
     */
    void removerCampoRelatorio(CampoRelatorio campoRelatorio) throws ErroSistemicoException;
    
    /**
     * 
     * buscarTodos
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 17/06/2009
     * @see
     * @return
     * @throws ErroSistemicoException
     * @return Returns the List<CampoRelatorio>.
     */
    List<CampoRelatorio> buscarTodos() throws ErroSistemicoException;
    
    /**
     * 
     * buscarPorExemplo
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 17/06/2009
     * @see
     * @param campoRelatorio
     * @return
     * @throws ErroSistemicoException
     * @return Returns the List<CampoRelatorio>.
     */
    List<CampoRelatorio> buscarPorExemplo(CampoRelatorio campoRelatorio) throws ErroSistemicoException;
    
    /**
     * 
     * buscarPorId
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 19/06/2009
     * @see
     * @param id
     * @return
     * @throws ErroSistemicoException
     * @return Returns the CampoRelatorio.
     */
    CampoRelatorio buscarPorId(Long id) throws ErroSistemicoException;
    
    /**
     * 
     * atualizarCampoRelatorio
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 18/06/2009
     * @see
     * @param campoRelatorio
     * @throws ErroSistemicoException
     * @return Returns the void.
     */
    void atualizarCampoRelatorio(CampoRelatorio campoRelatorio) throws ErroSistemicoException;
}
