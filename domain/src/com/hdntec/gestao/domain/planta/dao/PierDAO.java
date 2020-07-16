package com.hdntec.gestao.domain.planta.dao;

import java.util.List;

import org.hibernate.HibernateException;

import com.hdntec.gestao.domain.planta.entity.status.Pier;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;


/**
 * Classe DAO para persistir a entidade Pier
 * 
 * @author hdn.luchetta -> Rodrigo Luchetta
 * 
 */
public class PierDAO extends AbstractGenericDAO<Pier> {

    /**
     * Salva objeto pier na entidade
     *
     * @param {@link Pier}
     */
    public Pier salvaPier(Pier pier) throws ErroSistemicoException {
        try {
            Pier pierSalvo = super.salvar(pier);
//            super.encerrarSessao();
            return pierSalvo;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Altera o objeto pier na entidade
     *
     * @param {@link Pier}
     */
    public void alteraPier(Pier pier) throws ErroSistemicoException {
        try {
            super.atualizar(pier);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Remove o objeto pier da entidade
     *
     * @param {@link Pier}
     */
    public void removePier(Pier pier) throws ErroSistemicoException {
        try {
            super.deletar(pier);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca pier da entidade Pier por exemplo
     *
     * @param {@link Pier}
     * @return link List<Pier>}
     */
    public List<Pier> buscaPorExemploPier(Pier pier) throws ErroSistemicoException {
        try {
            List<Pier> listaPesquisada = super.buscarListaDeObjetos(pier);
//            super.encerrarSessao();
            return listaPesquisada;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }
}
