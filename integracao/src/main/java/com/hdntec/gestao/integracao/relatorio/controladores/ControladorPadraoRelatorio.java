package com.hdntec.gestao.integracao.relatorio.controladores;

import java.util.List;

import javax.ejb.Stateless;

import com.hdntec.gestao.domain.relatorio.PadraoRelatorio;
import com.hdntec.gestao.domain.relatorio.dao.IPadraoRelatorioDAO;
import com.hdntec.gestao.domain.relatorio.dao.PadraoRelatorioDAO;
import com.hdntec.gestao.exceptions.ErroSistemicoException;


/**
 * 
 * <P><B>Description :</B><BR>
 * General ControladorPadraoRelatorio
 * </P>
 * <P>
 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
 * @since 18/06/2009
 * @version $Revision: 1.1 $
 */
@Stateless(mappedName = "bs/ControladorPadraoRelatorio")
public class ControladorPadraoRelatorio implements IControladorPadraoRelatorio {

	/** DAO */
	private static IPadraoRelatorioDAO DAO = new PadraoRelatorioDAO();
	
	@Override
	public List<PadraoRelatorio> buscarListaDePadraoRelatorio(
			PadraoRelatorio padraoRelatorio) throws ErroSistemicoException {
		
		return DAO.buscarPorExemploPadraoRelatorio(padraoRelatorio);
	}

	@Override
	public List<PadraoRelatorio> buscarTodos() throws ErroSistemicoException {
		
		return DAO.buscarTodos();
	}
	
	@Override
	public PadraoRelatorio buscarPorId(Long id) throws ErroSistemicoException {

		return DAO.buscarPadraoRelatorio(id);
	}

	@Override
	public void removerPadraoRelatorio(PadraoRelatorio padraoRelatorio)
			throws ErroSistemicoException {
		
		DAO.removerPadraoRelatorio(padraoRelatorio);
	}

	@Override
	public PadraoRelatorio salvarPadraoRelatorio(PadraoRelatorio padraoRelatorio)
			throws ErroSistemicoException {
		
		return DAO.salvarPadraoRelatorio(padraoRelatorio);
	}

}
