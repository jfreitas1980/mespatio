package com.hdntec.gestao.integracao.relatorio.controladores;

import java.util.List;

import javax.ejb.Remote;

import com.hdntec.gestao.domain.relatorio.CampoRelatorio;
import com.hdntec.gestao.domain.relatorio.PadraoRelatorio;
import com.hdntec.gestao.domain.relatorio.Relatorio;
import com.hdntec.gestao.exceptions.ErroSistemicoException;


@Remote
public interface IControladorGestaoRelatorio {
	
	/**
	 * Metodo que atualiza na base todos os campos possiveis 
	 * para formar o relatorio.
	 * 
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 18/06/2009
	 * @see
	 * @throws ErroSistemicoException
	 * @return Returns the void.
	 */
	void atualizarCamposRelatorio() throws ErroSistemicoException;

	/**
	 * 
	 * buscarCampoRelatorioPorId
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 19/06/2009
	 * @see
	 * @param id
	 * @return
	 * @throws ErroSistemicoException
	 * @return Returns the CampoRelatorio.
	 */
	CampoRelatorio buscarCampoRelatorioPorId(Long id) throws ErroSistemicoException;
	
	/**
	 * Busca na base todos os campos cadastrados para o relatorio 
	 * 
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 18/06/2009
	 * @see
	 * @return
	 * @throws ErroSistemicoException
	 * @return Returns the List<CampoRelatorio>.
	 */
	List<CampoRelatorio> buscarCampos() throws ErroSistemicoException;
	
	/**
	 * Busca na base os campos do relatorio de acordo com o filtro informado 
	 * 
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 19/06/2009
	 * @see
	 * @param filtro
	 * @return
	 * @throws ErroSistemicoException
	 * @return Returns the List<CampoRelatorio>.
	 */
	List<CampoRelatorio> buscarCamposPorFiltro(CampoRelatorio filtro) throws ErroSistemicoException;
	
	/**
	 * Grava os objetos da lista na base de dados
	 * 
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 18/06/2009
	 * @see
	 * @param campos
	 * @return
	 * @throws ErroSistemicoException
	 * @return Returns the List<CampoRelatorio>.
	 */
	List<CampoRelatorio> salvarListaDeCampoRelatorio(List<CampoRelatorio> campos) throws ErroSistemicoException;
	
	/**
	 * Grava/Atualiza na base de dados o objeto campo relatorio
	 * 
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 18/06/2009
	 * @see
	 * @param campoRelatorio
	 * @return
	 * @throws ErroSistemicoException
	 * @return Returns the CampoRelatorio.
	 */
	CampoRelatorio salvarCampoRelatorio(CampoRelatorio campoRelatorio) throws ErroSistemicoException;

	/**
	 * Remove da base de dados o objeto campo relatorio.
	 * 
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 18/06/2009
	 * @see
	 * @param campoRelatorio
	 * @throws ErroSistemicoException
	 * @return Returns the void.
	 */
	void removerCampoRelatorio(CampoRelatorio campoRelatorio) throws ErroSistemicoException;
	
	/**
	 * 
	 * buscarPadraoRelatorioPorId
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 19/06/2009
	 * @see
	 * @param id
	 * @return
	 * @throws ErroSistemicoException
	 * @return Returns the PadraoRelatorio.
	 */
	PadraoRelatorio buscarPadraoRelatorioPorId(Long id) throws ErroSistemicoException;
	
	/**
	 * 
	 * buscarPadroesPorFiltro
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 19/06/2009
	 * @see
	 * @param filtro
	 * @return
	 * @throws ErroSistemicoException
	 * @return Returns the List<PadraoRelatorio>.
	 */
	List<PadraoRelatorio> buscarPadroesPorFiltro(PadraoRelatorio filtro) throws ErroSistemicoException;
	
	/**
	 * Busca na base todos os padroes de relatorio.
	 * 
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 18/06/2009
	 * @see
	 * @return
	 * @throws ErroSistemicoException
	 * @return Returns the List<PadraoRelatorio>.
	 */
	List<PadraoRelatorio> buscarPadroesRelatorio() throws ErroSistemicoException;

	/**
	 * Grava/Atualiza na base de dados o objeto padrao de relatorio
	 * 
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
	 * Remove da base o objeto padrao de relatorio.
	 * 
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
	 * buscarRelatorioPorId
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 19/06/2009
	 * @see
	 * @param id
	 * @return
	 * @throws ErroSistemicoException
	 * @return Returns the Relatorio.
	 */
	Relatorio buscarRelatorioPorId(Long id) throws ErroSistemicoException;
	
	/**
	 * 
	 * buscarRelatoriosPorFiltro
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 19/06/2009
	 * @see
	 * @param filtro
	 * @return
	 * @throws ErroSistemicoException
	 * @return Returns the List<Relatorio>.
	 */
	List<Relatorio> buscarRelatoriosPorFiltro(Relatorio filtro) throws ErroSistemicoException;
	
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
    
	/**
	 * Busca na base todos os relatorios.
	 * 
	 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
	 * @since 18/06/2009
	 * @see
	 * @return
	 * @throws ErroSistemicoException
	 * @return Returns the List<Relatorio>.
	 */
	List<Relatorio> buscarRelatorios() throws ErroSistemicoException;
	
	/**
	 * Grava/Atualiza na base o objeto relatorio.
	 * 
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
	 * Remove da base o objeto relatorio.
	 * 
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
}
