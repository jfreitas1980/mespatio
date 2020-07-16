/**
 * 
 */
package com.hdntec.gestao.domain.planta.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.hdntec.gestao.domain.planta.entity.MetaBaliza;
import com.hdntec.gestao.domain.planta.entity.MetaMaquinaDoPatio;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;
import com.hdntec.gestao.hibernate.HibernateUtil;


/**
 * Classe para persistir as máquinas
 * @author andre
 * 
 */
public class MetaMaquinaDoPatioDAO extends AbstractGenericDAO<MetaMaquinaDoPatio> {

    private MaquinaDoPatioDAO dao = new MaquinaDoPatioDAO();
	/**
     * Salva objeto maquina na entidade
     *
     * @param {@link MetaMaquinaDoPatio}
     */
    public MetaMaquinaDoPatio salvaMetaMaquinaDoPatio(MetaMaquinaDoPatio maquina) throws ErroSistemicoException {
        try {
            MetaMaquinaDoPatio maquinaSalva = super.salvar(maquina);
            dao.salvar(maquina.getListaStatus());
//            super.encerrarSessao();
            return maquinaSalva;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    public List<MetaMaquinaDoPatio> salvaMetaMaquinaDoPatio(List<MetaMaquinaDoPatio> maquina) throws ErroSistemicoException {
        try {
        	List<MetaMaquinaDoPatio> maquinaSalva = super.salvar(maquina);
             for (MetaMaquinaDoPatio mMaquina : maquina) {
             	dao.salvar(mMaquina.getListaStatus());
             } 
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
     * @param {@link MetaMaquinaDoPatio}
     */
    public void alteraMetaMaquinaDoPatio(MetaMaquinaDoPatio maquina) throws ErroSistemicoException {
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
     * @param {@link MetaMaquinaDoPatio}
     */
    public void removeMetaMaquinaDoPatio(MetaMaquinaDoPatio maquina) throws ErroSistemicoException {
        try {
            super.deletar(maquina);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca maquina da entidade MetaMaquinaDoPatio por exemplo
     *
     * @param {@link MetaMaquinaDoPatio}
     * @return link List<MetaMaquinaDoPatio>}
     */
    public MetaMaquinaDoPatio buscarMetaMaquinaDoPatio(MetaMaquinaDoPatio maquina) throws ErroSistemicoException {
        try {
            MetaMaquinaDoPatio maquinaPesquisada = super.buscarPorObjeto(maquina);
//            super.encerrarSessao();
            return maquinaPesquisada;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca lista da entidade MetaMaquinaDoPatio
     * @return
     * @throws ErroSistemicoException
     */
    public List<MetaMaquinaDoPatio> buscaListaDeMaquinasDoPatio() throws ErroSistemicoException {
        try {
            List<MetaMaquinaDoPatio> listaMaquinas = super.buscarListaDeObjetos(new MetaMaquinaDoPatio());
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
     * @return lista MetaMaquinaDoPatio
     */
    @SuppressWarnings("unchecked")
	public List<MetaMaquinaDoPatio> buscaListaMaquinaPorManutencao(
			MetaMaquinaDoPatio maquina, Date dataInicial, Date dataFinal) {
    	Session session = HibernateUtil.getSession();
    	StringBuilder sSql = new StringBuilder();
    	sSql.append("select distinct(m) from "+MetaMaquinaDoPatio.class.getName());
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

    	List<MetaMaquinaDoPatio> result = q.list();

    	return result;
    }
    
   
    
}