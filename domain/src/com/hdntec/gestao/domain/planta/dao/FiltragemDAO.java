package com.hdntec.gestao.domain.planta.dao;

import java.util.List;

import org.hibernate.HibernateException;

import com.hdntec.gestao.domain.planta.entity.status.Filtragem;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;


/**
 * Classe DAO para persistir a entidade Filtragem
 * 
 * @author hdn.luchetta -> Rodrigo Luchetta
 * 
 */
public class FiltragemDAO extends AbstractGenericDAO<Filtragem> {

    /**
     * Salva objeto pier na entidade
     *
     * @param {@link Filtragem}
     */
    public Filtragem salvaFiltragem(Filtragem pier) throws ErroSistemicoException {
        try {
            Filtragem FiltragemSalva = super.salvar(pier);
//            super.encerrarSessao();
            return FiltragemSalva;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Altera o objeto pier na entidade
     *
     * @param {@link Filtragem}
     */
    public void alteraFiltragem(Filtragem pier) throws ErroSistemicoException {
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
     * @param {@link Filtragem}
     */
    public void removeFiltragem(Filtragem pier) throws ErroSistemicoException {
        try {
            super.deletar(pier);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca pier da entidade Filtragem por exemplo
     *
     * @param {@link Filtragem}
     * @return link List<Filtragem>}
     */
    public List<Filtragem> buscaPorExemploFiltragem(Filtragem Filtragem) throws ErroSistemicoException {
        try {
            List<Filtragem> listaPesquisada = super.buscarListaDeObjetos(Filtragem);
//            super.encerrarSessao();
            return listaPesquisada;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }
}
