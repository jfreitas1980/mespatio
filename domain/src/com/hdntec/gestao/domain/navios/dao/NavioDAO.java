/**
 * 
 */
package com.hdntec.gestao.domain.navios.dao;


import java.util.List;

import org.hibernate.HibernateException;

import com.hdntec.gestao.domain.navios.entity.status.Navio;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;


/**
 * Classe DAO para persistir a entidade Navio
 * 
 * @author hdn.luchetta -> Rodrigo Luchetta
 * 
 */
public class NavioDAO extends AbstractGenericDAO<Navio> {

    /**
     * Salva objeto navio na entidade
     * @param {@link Navio}
     */
    public Navio salvaNavio(Navio navio) throws ErroSistemicoException {
        try {
            Navio navioSalvo = super.salvar(navio);
//            super.encerrarSessao();
            return navioSalvo;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    
    public void salvaNavio(List<Navio> navios) throws ErroSistemicoException {
        try {
            for(Navio navio : navios) {
            	salvaNavio(navio);
            } 	
//            super.encerrarSessao();           
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }
    /**
     * Altera o objeto navio na entidade
     *
     * @param {@link Navio}
     */
    public void alteraNavio(Navio navio) throws ErroSistemicoException {
        try {
            super.atualizar(navio);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Remove o objeto navio da entidade
     *
     * @param {@link Navio}
     */
    public void removeNavio(Navio navio) throws ErroSistemicoException {
        try {
            navio.setMetaNavio(null);
        	super.deletar(navio);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca navio da entidade Navio por exemplo
     *
     * @param {@link Navio}
     * @return link List<Navio>}
     */
    public List<Navio> buscaPorExemploNavio(Navio navio) throws ErroSistemicoException {
        try {
            List<Navio> listaPesquisada = super.buscarListaDeObjetos(navio);
            //acessaListasDoNavio(listaPesquisada);
//            super.encerrarSessao();
            return listaPesquisada;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }
     
}
