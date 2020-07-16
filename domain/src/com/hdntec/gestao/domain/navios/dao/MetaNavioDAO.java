/**
 * 
 */
package com.hdntec.gestao.domain.navios.dao;


import java.util.List;

import org.hibernate.HibernateException;

import com.hdntec.gestao.domain.navios.entity.MetaNavio;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;


/**
 * Classe DAO para persistir a entidade Navio
 * 
 * @author hdn.luchetta -> Rodrigo Luchetta
 * 
 */
public class MetaNavioDAO extends AbstractGenericDAO<MetaNavio> {
    private MetaCargaDAO metaCargaDAO = new MetaCargaDAO();
    private	NavioDAO navioDAO = new NavioDAO();
	/**
     * Salva objeto MetaNavio na entidade
     * @param {@link MetaNavio}
     */
    public MetaNavio salvaMetaNavio(MetaNavio metaNavio) throws ErroSistemicoException {
        try {
           
        	
        	MetaNavio MetaNavioSalvo = super.salvar(metaNavio);
            navioDAO.salvaNavio(metaNavio.getListaStatus());
            if (metaNavio.getListaMetaCargas() != null) {
            	metaCargaDAO.salvaMetaCarga(metaNavio.getListaMetaCargas());
            }	
           // super.encerrarSessao();
            return MetaNavioSalvo;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Altera o objeto MetaNavio na entidade
     *
     * @param {@link MetaNavio}
     */
    public void alteraMetaNavio(MetaNavio MetaNavio) throws ErroSistemicoException {
        try {
            super.atualizar(MetaNavio);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Remove o objeto MetaNavio da entidade
     *
     * @param {@link MetaNavio}
     */
    public void removeMetaNavio(MetaNavio MetaNavio) throws ErroSistemicoException {
        try {
            super.deletar(MetaNavio);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca MetaNavio da entidade MetaNavio por exemplo
     *
     * @param {@link MetaNavio}
     * @return link List<MetaNavio>}
     */
    public List<MetaNavio> buscaPorExemploMetaNavio(MetaNavio MetaNavio) throws ErroSistemicoException {
        try {
            List<MetaNavio> listaPesquisada = super.buscarListaDeObjetos(MetaNavio);            
//            super.encerrarSessao();
            return listaPesquisada;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

}
