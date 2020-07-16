package com.hdntec.gestao.domain.planta.dao;

import java.util.List;

import org.hibernate.HibernateException;

import com.hdntec.gestao.domain.planta.entity.status.Usina;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;


/**
 * Classe DAO para persistir a entidade Usina
 * 
 * @author hdn.luchetta -> Rodrigo Luchetta
 * 
 */
public class UsinaDAO extends AbstractGenericDAO<Usina> {

    /**
     * Salva objeto pier na entidade
     *
     * @param {@link Usina}
     */
    public Usina salvaUsina(Usina pier) throws ErroSistemicoException {
        try {
            Usina usinaSalva = super.salvar(pier);
//            super.encerrarSessao();
            return usinaSalva;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Altera o objeto pier na entidade
     *
     * @param {@link Usina}
     */
    public void alteraUsina(Usina pier) throws ErroSistemicoException {
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
     * @param {@link Usina}
     */
    public void removeUsina(Usina pier) throws ErroSistemicoException {
        try {
            super.deletar(pier);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca pier da entidade Usina por exemplo
     *
     * @param {@link Usina}
     * @return link List<Usina>}
     */
    public List<Usina> buscaPorExemploUsina(Usina usina) throws ErroSistemicoException {
        try {
            List<Usina> listaPesquisada = super.buscarListaDeObjetos(usina);
//            super.encerrarSessao();
            return listaPesquisada;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }
}
