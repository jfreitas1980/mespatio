package com.hdntec.gestao.domain.planta.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.hdntec.gestao.domain.navios.dao.CargaDAO;
import com.hdntec.gestao.domain.navios.entity.MetaCarga;
import com.hdntec.gestao.domain.navios.entity.status.Carga;
import com.hdntec.gestao.domain.plano.entity.LugarEmpilhamentoRecuperacao;
import com.hdntec.gestao.domain.plano.enums.SentidoEmpilhamentoRecuperacaoEnum;
import com.hdntec.gestao.domain.planta.entity.MetaBaliza;
import com.hdntec.gestao.domain.planta.entity.MetaCorreia;
import com.hdntec.gestao.domain.planta.entity.MetaFiltragem;
import com.hdntec.gestao.domain.planta.entity.MetaMaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.MetaUsina;
import com.hdntec.gestao.domain.planta.entity.status.AtividadeCampanha;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.Campanha;
import com.hdntec.gestao.domain.planta.entity.status.Correia;
import com.hdntec.gestao.domain.planta.entity.status.Filtragem;
import com.hdntec.gestao.domain.planta.entity.status.MaquinaDoPatio;
import com.hdntec.gestao.domain.planta.entity.status.Usina;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;
import com.hdntec.gestao.hibernate.HibernateUtil;


/**
 * 
 * @author Ricardo Trabalho
 */
public class LugarEmpilhamentoRecuperacaoDAO extends
		AbstractGenericDAO<LugarEmpilhamentoRecuperacao> {
	private MaquinaDoPatioDAO maquinaDAO = new MaquinaDoPatioDAO();
	private UsinaDAO usinaDAO = new UsinaDAO();
	private FiltragemDAO filtragemDAO = new FiltragemDAO();
	private BalizaDAO balizaDAO = new BalizaDAO();
	private CorreiaDAO correiaDAO = new CorreiaDAO();
	private CargaDAO cargaDAO = new CargaDAO();
	private AtividadeCampanhaDAO atividadeCampanhaDAO = new AtividadeCampanhaDAO();

	/*
	 * Salva atividade campanha na entidade
	 * 
	 * @param {@link LugarEmpilhamentoRecuperacao}
	 */
	public void removeLugarEmpilhamentoRecuperacao(
			LugarEmpilhamentoRecuperacao lugarEmpilhamentoRecuperacao)
			throws ErroSistemicoException {
		try {
            
		    List<AtividadeCampanha> itensAtividadeCampanha = new ArrayList<AtividadeCampanha>();
		    List<Filtragem> itensFiltragem = new ArrayList<Filtragem>();
		    List<Usina> itensUsina = new ArrayList<Usina>();
		    List<MaquinaDoPatio> itensMaquinaDoPatio = new ArrayList<MaquinaDoPatio>();
		    List<Correia> itensCorreia = new ArrayList<Correia>();
		    List<Baliza> itensBaliza = new ArrayList<Baliza>();
		    List<Carga> itensCarga = new ArrayList<Carga>();
		    
			// atividades campanha
			if (lugarEmpilhamentoRecuperacao.getListaAtividadeCampanhas() != null &&
            		lugarEmpilhamentoRecuperacao.getListaAtividadeCampanhas().size() > 0) {
				
				itensAtividadeCampanha.addAll(lugarEmpilhamentoRecuperacao.getListaAtividadeCampanhas());
			}	
			// Filtragem
			if (lugarEmpilhamentoRecuperacao.getListaFiltragens() != null && lugarEmpilhamentoRecuperacao.getListaFiltragens().size() > 0) {
				itensFiltragem.addAll(lugarEmpilhamentoRecuperacao.getListaFiltragens());
			}

            //Usina 			
			if (lugarEmpilhamentoRecuperacao.getListaUsinas() != null && lugarEmpilhamentoRecuperacao.getListaUsinas().size() > 0) {
				itensUsina.addAll(lugarEmpilhamentoRecuperacao.getListaUsinas());
			}

			//MaquinaDoPatio
			if (lugarEmpilhamentoRecuperacao.getListaMaquinaDoPatio() != null && lugarEmpilhamentoRecuperacao.getListaMaquinaDoPatio().size() > 0) {
				itensMaquinaDoPatio.addAll(lugarEmpilhamentoRecuperacao.getListaMaquinaDoPatio());
			}

			//Correia
			if (lugarEmpilhamentoRecuperacao.getListaCorreias() != null && lugarEmpilhamentoRecuperacao.getListaCorreias().size() > 0) {
				itensCorreia.addAll(lugarEmpilhamentoRecuperacao.getListaCorreias());
			}
			
			//Baliza
			if (lugarEmpilhamentoRecuperacao.getListaDeBalizas() != null && lugarEmpilhamentoRecuperacao.getListaDeBalizas().size() > 0) {
				itensBaliza.addAll(lugarEmpilhamentoRecuperacao.getListaDeBalizas());
			}


			//Carga
			if (lugarEmpilhamentoRecuperacao.getListaCargas() != null && lugarEmpilhamentoRecuperacao.getListaCargas().size() > 0) {
				itensCarga.addAll(lugarEmpilhamentoRecuperacao.getListaCargas());
			}


			super.deletar(lugarEmpilhamentoRecuperacao);

			
			//AtividadeCampanha
			for (AtividadeCampanha ac : itensAtividadeCampanha) {	
					atividadeCampanhaDAO.removeAtividadeCampanha(ac);
			}		
            
			//Filtragem			
			for (Filtragem f : itensFiltragem) {
				MetaFiltragem meta = f.getMetaFiltragem();				
				filtragemDAO.removeFiltragem(f);				
				meta.removerStatus(f);
			}
						
			//Usina			
			for (Usina u : itensUsina) {
				MetaUsina meta = u.getMetaUsina();
				usinaDAO.removeUsina(u);
				meta.removerStatus(u);
			}

			//MaquinaDoPatio
			for (MaquinaDoPatio m : itensMaquinaDoPatio) {
				MetaMaquinaDoPatio meta = m.getMetaMaquina();
				maquinaDAO.removeMaquinaDoPatio(m);
				meta.removerStatus(m);
			}
			
			//Correia
			for (Correia c : itensCorreia) {
				MetaCorreia meta = c.getMetaCorreia();
				correiaDAO.removeCorreia(c);
				meta.removerStatus(c);
			}

			//Baliza
			for (Baliza b : itensBaliza) {
				MetaBaliza meta = b.getMetaBaliza();				
				balizaDAO.removeBaliza(b);				
				b.removeStatusAPartirDeHorario(b.getDtInicio());
				meta.removerStatus(b);
				
				
			}
					
			//Carga
			for (Carga c : itensCarga) {
				MetaCarga meta = c.getMetaCarga();
				cargaDAO.removeCarga(c);
				meta.removerStatus(c);
			}
			if (lugarEmpilhamentoRecuperacao.getListaAtividadeCampanhas() != null)
				lugarEmpilhamentoRecuperacao.getListaAtividadeCampanhas()
						.clear();
			if (lugarEmpilhamentoRecuperacao.getListaDeBalizas() != null)
				lugarEmpilhamentoRecuperacao.getListaDeBalizas().clear();
			if (lugarEmpilhamentoRecuperacao.getListaDeBalizas() != null)
				lugarEmpilhamentoRecuperacao.getListaDeBalizas().clear();
			if (lugarEmpilhamentoRecuperacao.getListaCorreias() != null)
				lugarEmpilhamentoRecuperacao.getListaCorreias().clear();
			if (lugarEmpilhamentoRecuperacao.getListaFiltragens() != null)
				lugarEmpilhamentoRecuperacao.getListaFiltragens().clear();
			if (lugarEmpilhamentoRecuperacao.getListaUsinas() != null)
				lugarEmpilhamentoRecuperacao.getListaUsinas().clear();
			if (lugarEmpilhamentoRecuperacao.getListaMaquinaDoPatio() != null)
				lugarEmpilhamentoRecuperacao.getListaMaquinaDoPatio().clear();
			
			// super.encerrarSessao();
			
		} catch (HibernateException hbEx) {
			hbEx.printStackTrace();
			throw new ErroSistemicoException(hbEx.getMessage());
		}
	}

	public List<LugarEmpilhamentoRecuperacao> salvaLugarEmpilhamentoRecuperacao(
			List<LugarEmpilhamentoRecuperacao> listaDeLugaresDeEmpilhamentoRecuperacao)
			throws ErroSistemicoException {
		// TODO Auto-generated method stub
		List<LugarEmpilhamentoRecuperacao> itens = new ArrayList<LugarEmpilhamentoRecuperacao>();
		try {
			for (LugarEmpilhamentoRecuperacao lg : listaDeLugaresDeEmpilhamentoRecuperacao) {
				itens.add(salvaLugarEmpilhamentoRecuperacao(lg));
			}
			return itens;
		} catch (HibernateException hbEx) {
			hbEx.printStackTrace();
			throw new ErroSistemicoException(hbEx.getMessage());
		}
	}

	/**
	 * Altera o objeto atividade campanha na entidade
	 * 
	 * @param {@link LugarEmpilhamentoRecuperacao}
	 */
	public void alteraLugarEmpilhamentoRecuperacao(
			LugarEmpilhamentoRecuperacao LugarEmpilhamentoRecuperacao)
			throws ErroSistemicoException {
		try {
			super.atualizar(LugarEmpilhamentoRecuperacao);
			// super.encerrarSessao();
		} catch (HibernateException hbEx) {
			hbEx.printStackTrace();
			throw new ErroSistemicoException(hbEx.getMessage());
		}
	}

	/**
	 * Remove o objeto atividade campanha da entidade
	 * 
	 * @param {@link LugarEmpilhamentoRecuperacao}
	 */
	public void removeLugarEmpilhamentoRecuperacao(
			List<LugarEmpilhamentoRecuperacao> lugaresEmpilhamentoRecuperacao)
			throws ErroSistemicoException {
		try {
		
			
			for (LugarEmpilhamentoRecuperacao lg : lugaresEmpilhamentoRecuperacao) {
				removeLugarEmpilhamentoRecuperacao(lg);
			}
			// super.encerrarSessao();
		} catch (HibernateException hbEx) {
			hbEx.printStackTrace();
			throw new ErroSistemicoException(hbEx.getMessage());
		}
	}


	public LugarEmpilhamentoRecuperacao salvaLugarEmpilhamentoRecuperacao(
			LugarEmpilhamentoRecuperacao lugarEmpilhamentoRecuperacao)
			throws ErroSistemicoException {
		try {
            
			
			if (lugarEmpilhamentoRecuperacao.getListaAtividadeCampanhas() != null &&
            		lugarEmpilhamentoRecuperacao.getListaAtividadeCampanhas().size() > 0) {
			atividadeCampanhaDAO.salvar(lugarEmpilhamentoRecuperacao
					.getListaAtividadeCampanhas());
            }
			if (lugarEmpilhamentoRecuperacao.getListaDeBalizas() != null) {
				Collections.sort(lugarEmpilhamentoRecuperacao.getListaDeBalizas(),Baliza.comparadorBaliza);				
	            if (lugarEmpilhamentoRecuperacao.getSentido() != null && lugarEmpilhamentoRecuperacao.getSentido() == SentidoEmpilhamentoRecuperacaoEnum.NORTE_PARA_SUL) 
	            {
	               Collections.reverse(lugarEmpilhamentoRecuperacao.getListaDeBalizas());
	            }
				
				for (Baliza b : lugarEmpilhamentoRecuperacao
						.getListaDeBalizas()) {					
					balizaDAO.salvaBaliza(b);
				}
				
				
			}
			if (lugarEmpilhamentoRecuperacao.getListaCorreias() != null) {
				for (Correia c : lugarEmpilhamentoRecuperacao
						.getListaCorreias()) {
					correiaDAO.salvaCorreia(c);
				}
			}

			if (lugarEmpilhamentoRecuperacao.getListaCargas() != null) {
				for (Carga c : lugarEmpilhamentoRecuperacao
						.getListaCargas()) {
					cargaDAO.salvaCarga(c);
				}
			}
			
			if (lugarEmpilhamentoRecuperacao.getListaMaquinaDoPatio() != null) {
				for (MaquinaDoPatio m : lugarEmpilhamentoRecuperacao
						.getListaMaquinaDoPatio()) {
					//m.setAtividade(lugarEmpilhamentoRecuperacao.getAtividade());
					maquinaDAO.salvaMaquinaDoPatio(m);
					//metaMaquinaDAO.salvaMetaMaquinaDoPatio(m.getMetaMaquina());
				}
			}
			if (lugarEmpilhamentoRecuperacao.getListaFiltragens() != null) {
				for (Filtragem f : lugarEmpilhamentoRecuperacao
						.getListaFiltragens()) {
					filtragemDAO.salvaFiltragem(f);
				}
			}
			if (lugarEmpilhamentoRecuperacao.getListaUsinas() != null) {
				for (Usina u : lugarEmpilhamentoRecuperacao.getListaUsinas()) {
					usinaDAO.salvaUsina(u);
				}
			}
            
			//LugarEmpilhamentoRecuperacao atividadeSalva = super
				//	.salvar(lugarEmpilhamentoRecuperacao);

			// super.encerrarSessao();
			return lugarEmpilhamentoRecuperacao;
		} catch (HibernateException hbEx) {
			hbEx.printStackTrace();
			throw new ErroSistemicoException(hbEx.getMessage());
		}
	}

	
	
	/**
	 * Remove o objeto atividade campanha da entidade
	 * 
	 * @param {@link LugarEmpilhamentoRecuperacao}
	 */
	public List<LugarEmpilhamentoRecuperacao> buscarPorCampanha(
			Campanha campanha) throws ErroSistemicoException {
		List<LugarEmpilhamentoRecuperacao> objPesquisado = null;
		try {
			Session session = HibernateUtil.getSession();
			Criteria criteria = session.createCriteria(
					LugarEmpilhamentoRecuperacao.class, "sp");
			criteria.add(Restrictions.eq("sp.campanha", campanha));
			objPesquisado = criteria.list();

		} catch (HibernateException hbex) {
			hbex.printStackTrace();
			throw new ErroSistemicoException(hbex.getMessage());
		} finally {
			// encerrarSessao();
		}
		return objPesquisado;

	}

	/**
	 * Busca atividade campanha da entidade LugarEmpilhamentoRecuperacao por
	 * exemplo
	 * 
	 * @param {@link LugarEmpilhamentoRecuperacao}
	 * @return link List<LugarEmpilhamentoRecuperacao>}
	 */
	public List<LugarEmpilhamentoRecuperacao> buscaPorExemploLugarEmpilhamentoRecuperacao(
			LugarEmpilhamentoRecuperacao LugarEmpilhamentoRecuperacao)
			throws ErroSistemicoException {
		try {
			List<LugarEmpilhamentoRecuperacao> listaPesquisada = super
					.buscarListaDeObjetos(LugarEmpilhamentoRecuperacao);
			// super.encerrarSessao();
			return listaPesquisada;
		} catch (HibernateException hbEx) {
			hbEx.printStackTrace();
			throw new ErroSistemicoException(hbEx.getMessage());
		}
	}

}
