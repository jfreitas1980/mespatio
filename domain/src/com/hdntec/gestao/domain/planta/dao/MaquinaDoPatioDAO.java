/**
 * 
 */
package com.hdntec.gestao.domain.planta.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.hdntec.gestao.domain.plano.dao.AtividadeDAO;
import com.hdntec.gestao.domain.planta.entity.status.MaquinaDoPatio;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;
import com.hdntec.gestao.hibernate.HibernateUtil;


/**
 * Classe para persistir as máquinas
 * @author andre
 * 
 */
public class MaquinaDoPatioDAO extends AbstractGenericDAO<MaquinaDoPatio> {

   // private AtividadeDAO dao = new AtividadeDAO();
	/**
     * Salva objeto maquina na entidade
     *
     * @param {@link MaquinaDoPatio}
     */
    public MaquinaDoPatio salvaMaquinaDoPatio(MaquinaDoPatio maquina) throws ErroSistemicoException {
        try {
     /*       if (maquina.getAtividade() != null) {
            	dao.salvar(maquina.getAtividade());
            }*/
        	MaquinaDoPatio maquinaSalva = super.salvar(maquina);
//            super.encerrarSessao();
            return maquinaSalva;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Altera o objeto maquina na entidade
     *
     * @param {@link MaquinaDoPatio}
     */
    public void alteraMaquinaDoPatio(MaquinaDoPatio maquina) throws ErroSistemicoException {
        try {
            super.atualizar(maquina);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Remove o objeto maquina da entidade
     *
     * @param {@link MaquinaDoPatio}
     */
    public void removeMaquinaDoPatio(MaquinaDoPatio maquina) throws ErroSistemicoException {
        try {
            super.deletar(maquina);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca maquina da entidade MaquinaDoPatio por exemplo
     *
     * @param {@link MaquinaDoPatio}
     * @return link List<MaquinaDoPatio>}
     */
    public MaquinaDoPatio buscarMaquinaDoPatio(MaquinaDoPatio maquina) throws ErroSistemicoException {
        try {
            MaquinaDoPatio maquinaPesquisada = super.buscarPorObjeto(maquina);
//            super.encerrarSessao();
            return maquinaPesquisada;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca lista da entidade MaquinaDoPatio
     * @return
     * @throws ErroSistemicoException
     */
    public List<MaquinaDoPatio> buscaListaDeMaquinasDoPatio() throws ErroSistemicoException {
        try {
            List<MaquinaDoPatio> listaMaquinas = super.buscarListaDeObjetos(new MaquinaDoPatio());
//            super.encerrarSessao();
            return listaMaquinas;
        } catch (HibernateException hbex) {
            hbex.printStackTrace();
            throw new ErroSistemicoException(hbex.getMessage());
        }
    }
    
    /**
     * Retorna uma lista de maquinas com suas manutencoes dentro do periodo informado
     * @param dataInicial
     * @param dataFinal
     * @return lista MaquinaDoPatio
     */
    @SuppressWarnings("unchecked")
	public List<MaquinaDoPatio> buscaListaMaquinaPorManutencao(
			MaquinaDoPatio maquina, Date dataInicial, Date dataFinal) {
    	Session session = HibernateUtil.getSession();
    	StringBuilder sSql = new StringBuilder();
    	sSql.append("select distinct(m) from "+MaquinaDoPatio.class.getName());
    	sSql.append(" m join fetch m.listaManutencao as manutencoes ");
    	sSql.append(" where manutencoes.dataInicial >= :dataInicial ");
    	sSql.append(" and manutencoes.dataFinal <= :dataFinal");
    	if (maquina != null) {
    		sSql.append(" and m.nomeMaquina = :nomeMaquina");
    	}
    	Query q = session.createQuery(sSql.toString());
    	q.setParameter("dataInicial", dataInicial);
    	q.setParameter("dataFinal", dataFinal);
    	if (maquina != null) {
    		q.setParameter("nomeMaquina", maquina.getNomeMaquina());
    	}

    	List<MaquinaDoPatio> result = q.list();

    	return result;
    }
}