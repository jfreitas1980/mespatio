package com.hdntec.gestao.integracao.relatorio.controladores;

import java.util.List;

import javax.ejb.Stateless;

import com.hdntec.gestao.domain.relatorio.CampoRelatorio;
import com.hdntec.gestao.domain.relatorio.dao.CampoRelatorioDAO;
import com.hdntec.gestao.domain.relatorio.dao.ICampoRelatorioDAO;
import com.hdntec.gestao.exceptions.ErroSistemicoException;


/**
 * 
 * <P><B>Description :</B><BR>
 * General ControladorCampoRelatorio
 * </P>
 * <P>
 * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
 * @since 18/06/2009
 * @version $Revision: 1.1 $
 */
@Stateless(mappedName = "bs/ControladorCampoRelatorio")
public class ControladorCampoRelatorio implements IControladorCampoRelatorio {

	/** DAO */
	private static ICampoRelatorioDAO DAO = new CampoRelatorioDAO(); 
	
	@Override
	public List<CampoRelatorio> buscarPorExemplo(CampoRelatorio campoRelatorio)
			throws ErroSistemicoException {
		return DAO.buscarPorExemplo(campoRelatorio);
	}

	@Override
	public List<CampoRelatorio> buscarTodos() throws ErroSistemicoException {
		return DAO.buscarTodos();
	}
	
	@Override
	public CampoRelatorio buscarPorId(Long id) throws ErroSistemicoException {
		
		return DAO.buscarCampoRelatorio(id);
	}

	@Override
	public void removerCampoRelatorio(CampoRelatorio campoRelatorio)
			throws ErroSistemicoException {
		DAO.removerCampoRelatorio(campoRelatorio);
	}

	@Override
	public CampoRelatorio salvarCampoRelatorio(CampoRelatorio campoRelatorio)
			throws ErroSistemicoException {
		//if (campoRelatorio.getIdCampo()==null || campoRelatorio.getIdCampo()<=0) {
			campoRelatorio = DAO.salvarCampoRelatorio(campoRelatorio);
		/*}
		else {
			DAO.atualizarCampoRelatorio(campoRelatorio);
		}*/
		return campoRelatorio;
	}

	@Override
	public List<CampoRelatorio> salvarLista(List<CampoRelatorio> campos)
			throws ErroSistemicoException {
		return DAO.salvarLista(campos);
	}
	@Override
	public void atualizarCampoRelatorio(CampoRelatorio campoRelatorio)
		throws ErroSistemicoException {
		DAO.atualizarCampoRelatorio(campoRelatorio);
	}
}
