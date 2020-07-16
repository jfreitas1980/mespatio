package com.hdntec.gestao.domain.produto.dao;

import java.util.List;

import org.hibernate.HibernateException;

import com.hdntec.gestao.domain.produto.entity.Amostra;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;


/**
 * Classe DAO para persistir a entidade Amostra
 * 
 * @author hdn.luchetta -> Rodrigo Luchetta
 * 
 */
public class AmostraDAO extends AbstractGenericDAO<Amostra> {

    /**
     * Salva objeto amostra na entidade
     *
     * @param {@link Amostra}
     */
    public Amostra salvaAmostra(Amostra amostra) throws ErroSistemicoException {
        try {
            Amostra amostraSalva = super.salvar(amostra);
//            super.encerrarSessao();
            return amostraSalva;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Altera o objeto amostra na entidade
     *
     * @param {@link Amostra}
     */
    public void alteraAmostra(Amostra amostra) throws ErroSistemicoException {
        try {
            super.atualizar(amostra);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Remove o objeto amostra da entidade
     *
     * @param {@link Amostra}
     */
    public void removeAmostra(Amostra amostra) throws ErroSistemicoException {
        try {
            super.deletar(amostra);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca amostra da entidade Amostra por exemplo
     *
     * @param {@link Amostra}
     * @return link List<Amostra>}
     */
    public List<Amostra> buscaPorExemploAmostra(Amostra amostra) throws ErroSistemicoException {
        try {
            List<Amostra> listaPesquisada = super.buscarListaDeObjetos(amostra);
//            super.encerrarSessao();
            return listaPesquisada;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }
}
