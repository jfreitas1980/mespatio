package com.hdntec.gestao.domain.planta.dao;

import java.util.List;

import org.hibernate.HibernateException;

import com.hdntec.gestao.domain.planta.entity.status.Planta;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;


/**
 * Classe DAO para persistir a entidade Planta
 * 
 * @author hdn.luchetta -> Rodrigo Luchetta
 * 
 */
public class PlantaDAO extends AbstractGenericDAO<Planta> {

    /**
     * Salva objeto planta na entidade
     *
     * @param {@link Planta}
     */
    public Planta salvaPlanta(Planta planta) throws ErroSistemicoException {
        try {
            Planta plantaSalva = super.salvar(planta);
//            super.encerrarSessao();
            return plantaSalva;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Altera o objeto planta na entidade
     *
     * @param {@link Planta}
     */
    public void alteraPlanta(Planta planta) throws ErroSistemicoException {
        try {
            super.atualizar(planta);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Remove o objeto planta da entidade
     *
     * @param {@link Planta}
     */
    public void removePlanta(Planta planta) throws ErroSistemicoException {
        try {
            super.deletar(planta);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca planta da entidade Planta por exemplo
     *
     * @param {@link Planta}
     * @return link List<Planta>}
     */
    public List<Planta> buscaPorExemploPlanta(Planta planta) throws ErroSistemicoException {
        try {
            List<Planta> listaPesquisada = super.buscarListaDeObjetos(planta);
//            super.encerrarSessao();
            return listaPesquisada;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }
}
