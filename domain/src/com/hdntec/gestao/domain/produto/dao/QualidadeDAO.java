package com.hdntec.gestao.domain.produto.dao;

import java.util.List;

import org.hibernate.HibernateException;

import com.hdntec.gestao.domain.produto.entity.Qualidade;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;


/**
 * Classe DAO para persistir a entidade Qualidade
 * 
 * @author hdn.luchetta -> Rodrigo Luchetta
 * 
 */
public class QualidadeDAO extends AbstractGenericDAO<Qualidade> {

    /**
     * Salva objeto qualidade na entidade
     *
     * @param {@link QualidadeDAO}
     */
    public Qualidade salvaQualidade(Qualidade qualidade) throws ErroSistemicoException {
        try {
            Qualidade qualidadeSalva = super.salvar(qualidade);
//            super.encerrarSessao();
            return qualidadeSalva;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Altera o objeto qualidade na entidade
     *
     * @param {@link QualidadeDAO}
     */
    public void alteraQualidade(Qualidade qualidade) throws ErroSistemicoException {
        try {
            super.atualizar(qualidade);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Remove o objeto qualidade da entidade
     *
     * @param {@link QualidadeDAO}
     */
    public void removeQualidade(Qualidade qualidade) throws ErroSistemicoException {
        try {
            super.deletar(qualidade);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca qualidade da entidade Qualidade por exemplo
     *
     * @param {@link QualidadeDAO}
     * @return link List<Qualidade>}
     */
    public List<Qualidade> buscaPorExemploQualidade(Qualidade qualidade) throws ErroSistemicoException {
        try {
            List<Qualidade> listaPesquisada = super.buscarListaDeObjetos(qualidade);
//            super.encerrarSessao();
            return listaPesquisada;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }
}
