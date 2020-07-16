package com.hdntec.gestao.domain.planta.dao;

import java.util.List;

import org.hibernate.HibernateException;

import com.hdntec.gestao.domain.planta.entity.MetaPier;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;


/**
 * Classe DAO para persistir a entidade Pier
 * 
 * @author hdn.luchetta -> Rodrigo Luchetta
 * 
 */
public class MetaPierDAO extends AbstractGenericDAO<MetaPier> {

    private PierDAO pierDAO = new PierDAO();
	/**
     * Salva objeto pier na entidade
     *
     * @param {@link MetaMetaPier}
     */
    public MetaPier salvaMetaPier(MetaPier pier) throws ErroSistemicoException {
        try {
        	MetaPier pierSalvo = super.salvar(pier);
        	pierDAO.salvar(pier.getListaStatus());
        	
        	
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
     * @param {@link MetaPier}
     */
    public void alteraMetaPier(MetaPier pier) throws ErroSistemicoException {
        try {
        	pierDAO.atualizar(pier.getListaStatus());
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
     * @param {@link MetaPier}
     */
    public void removeMetaPier(MetaPier pier) throws ErroSistemicoException {
        try {
            super.deletar(pier);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca pier da entidade MetaPier por exemplo
     *
     * @param {@link MetaPier}
     * @return link List<MetaPier>}
     */
    public List<MetaPier> buscaPorExemploMetaPier(MetaPier pier) throws ErroSistemicoException {
        try {
            List<MetaPier> listaPesquisada = super.buscarListaDeObjetos(pier);
//            super.encerrarSessao();
            return listaPesquisada;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }
}
