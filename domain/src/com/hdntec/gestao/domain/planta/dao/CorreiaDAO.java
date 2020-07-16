package com.hdntec.gestao.domain.planta.dao;


import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.hdntec.gestao.domain.planta.comparadores.ComparadorCorreias;
import com.hdntec.gestao.domain.planta.entity.status.Correia;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;
import com.hdntec.gestao.hibernate.HibernateUtil;


/**
 * Classe DAO para persistir a entidade Correia
 * 
 * @author hdn.luchetta -> Rodrigo Luchetta
 * 
 */
public class CorreiaDAO extends AbstractGenericDAO<Correia> {

    /**
     * Salva correia berco na entidade
     *
     * @param {@link Correia}
     */
    public Correia salvaCorreia(Correia correia) throws ErroSistemicoException {
        try {
            Correia correiaSalva = super.salvar(correia);
//            super.encerrarSessao();
            return correiaSalva;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Altera o objeto correia na entidade
     *
     * @param {@link Correia}
     */
    public void alteraCorreia(Correia correia) throws ErroSistemicoException {
        try {
            super.atualizar(correia);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Remove o objeto correia da entidade
     *
     * @param {@link Correia}
     */
    public void removeCorreia(Correia correia) throws ErroSistemicoException {
        try {
            super.deletar(correia);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca correia da entidade Correia por exemplo
     *
     * @param {@link Correia}
     * @return
     */
    public Correia buscarCorreia(Correia correia) throws ErroSistemicoException {
        try {
            Correia correiaRecuperada = super.buscarPorObjeto(correia);
//            super.encerrarSessao();
            return correiaRecuperada;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }
    
    /**
     * Busca correia da entidade Correia por exemplo
     *
     * @param {@link Correia}
     * @return link List<Correia>}
     */
    public List<Correia> buscaPorExemploCorreia(Correia correia) throws ErroSistemicoException {
        try {
            List<Correia> listaCorreia = super.buscarListaDeObjetos(correia);
            Collections.sort(listaCorreia, new ComparadorCorreias());
//            super.encerrarSessao();
            return listaCorreia;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }
    
    /**
     * Retorna uma lista de correias com suas manutencoes dentro do periodo informado
     * @param dataInicial
     * @param dataFinal
     * @return lista Correias
     */
    @SuppressWarnings("unchecked")
	public List<Correia> buscaListaCorreiaPorManutencao(
			Correia correia, Date dataInicial, Date dataFinal) {
    	Session session = HibernateUtil.getSession();
    	StringBuilder sSql = new StringBuilder();
    	sSql.append("select distinct(c) from "+Correia.class.getName());
    	sSql.append(" c join fetch c.listaManutencao as manutencoes ");
    	sSql.append(" where manutencoes.dataInicial >= :dataInicial ");
    	sSql.append(" and manutencoes.dataFinal <= :dataFinal");
    	if (correia != null) {
    		sSql.append(" and c.nomeCorreia = :nomeCorreia");
    	}
    	Query q = session.createQuery(sSql.toString());
    	q.setParameter("dataInicial", dataInicial);
    	q.setParameter("dataFinal", dataFinal);
    	if (correia != null) {
    		q.setParameter("nomeCorreia", correia.getNomeCorreia());
    	}
    	List<Correia> result = q.list();

    	return result;
    }
}
