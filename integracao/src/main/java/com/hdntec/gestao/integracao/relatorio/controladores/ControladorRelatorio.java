package com.hdntec.gestao.integracao.relatorio.controladores;

import java.util.List;

import javax.ejb.Stateless;

import com.hdntec.gestao.domain.relatorio.PadraoRelatorio;
import com.hdntec.gestao.domain.relatorio.Relatorio;
import com.hdntec.gestao.domain.relatorio.dao.IRelatorioDAO;
import com.hdntec.gestao.domain.relatorio.dao.RelatorioDAO;
import com.hdntec.gestao.exceptions.ErroSistemicoException;


/**
 * 
 * <P><B>Description :</B><BR>
 * General ControladorRelatorio
 * </P>
 * <P>
 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
 * @since 18/06/2009
 * @version $Revision: 1.1 $
 */
@Stateless(mappedName = "bs/ControladorRelatorio")
public class ControladorRelatorio implements IControladorRelatorio {

	/** DAO */
	private static IRelatorioDAO DAO = new RelatorioDAO();
	
	@Override
	public List<Relatorio> buscarListaDeRelatorio(Relatorio relatorio)
			throws ErroSistemicoException {
		return DAO.buscarPorExemplo(relatorio);
	}

	@Override
	public List<Relatorio> buscarTodos() throws ErroSistemicoException {
		return DAO.buscarTodos();
	}

	@Override
	public Relatorio buscarPorId(Long id) throws ErroSistemicoException {
		return DAO.buscarRelatorio(id);
	}
	
	@Override
	public void removerRelatorio(Relatorio relatorio)
			throws ErroSistemicoException {
		DAO.removerRelatorio(relatorio);
	}

	@Override
	public void removerRelatorios(List<Relatorio> relatorios)
			throws ErroSistemicoException {
		DAO.removerRelatorios(relatorios);
	}
	
	@Override
	public List<Relatorio> salvarListaDeRelatorio(List<Relatorio> relatorios)
			throws ErroSistemicoException {
		return DAO.salvarLista(relatorios);
	}

	@Override
	public Relatorio salvarRelatorio(Relatorio relatorio)
			throws ErroSistemicoException {
		return DAO.salvarRelatorio(relatorio);
	}

	@Override
	public List<Relatorio> buscarRelatoriosPorPadrao(
			PadraoRelatorio padraoRelatorio) throws ErroSistemicoException {
		// TODO Auto-generated method stub
		return DAO.buscarRelatoriosPorPadrao(padraoRelatorio.getIdPadraoRelatorio());
	}
}
	