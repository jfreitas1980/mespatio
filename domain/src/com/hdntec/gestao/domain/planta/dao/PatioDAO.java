package com.hdntec.gestao.domain.planta.dao;

import java.util.Collections;
import java.util.List;

import org.hibernate.HibernateException;

import com.hdntec.gestao.domain.planta.comparadores.ComparadorPatios;
import com.hdntec.gestao.domain.planta.entity.status.Patio;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;


/**
 * Classe DAO para persistir a entidade Patio
 * 
 * @author hdn.luchetta -> Rodrigo Luchetta
 * 
 */
public class PatioDAO extends AbstractGenericDAO<Patio> {

    /**
     * Salva objeto patio na entidade
     *
     * @param {@link Patio}
     */
    public Patio salvaPatio(Patio patio) throws ErroSistemicoException {
        try {
            Patio patioSalvo = super.salvar(patio);
//            super.encerrarSessao();
            return patioSalvo;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Altera o objeto patio na entidade
     *
     * @param {@link Patio}
     */
    public void alteraPatio(Patio patio) throws ErroSistemicoException {
        try {
            super.atualizar(patio);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Remove o objeto patio da entidade
     *
     * @param {@link Patio}
     */
    public void removePatio(Patio patio) throws ErroSistemicoException {
        try {
            super.deletar(patio);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca patio da entidade Patio por exemplo
     *
     * @param {@link Patio}
     * @return link List<Patio>}
     */
    public List<Patio> buscaPorExemploPatio(Patio patio) throws ErroSistemicoException {
        try {
            List<Patio> listaPatios = super.buscarListaDeObjetos(patio);
            Collections.sort(listaPatios, new ComparadorPatios());
//            super.encerrarSessao();
            return listaPatios;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }
}
