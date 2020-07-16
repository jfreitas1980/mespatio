/**
 * 
 */
package com.hdntec.gestao.domain.navios.dao;


import java.util.List;

import org.hibernate.HibernateException;

import com.hdntec.gestao.domain.navios.entity.MetaCarga;
import com.hdntec.gestao.domain.navios.entity.MetaNavio;
import com.hdntec.gestao.domain.planta.dao.BalizaDAO;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;


/**
 * Classe DAO para persistir a entidade Navio
 * 
 * @author hdn.luchetta -> Rodrigo Luchetta
 * 
 */
public class MetaCargaDAO extends AbstractGenericDAO<MetaCarga> {

	private CargaDAO cargaDAO= new CargaDAO();

	/**
     * Salva objeto MetaNavio na entidade
     * @param {@link MetaNavio}
     */
    public MetaCarga salvaMetaCarga(MetaCarga metaCarga) throws ErroSistemicoException {
        try {
        	MetaCarga MetaCargaSalvo = super.salvar(metaCarga);
        	cargaDAO.salvaCarga(metaCarga.getListaStatus());        	
//            super.encerrarSessao();
            return MetaCargaSalvo;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    
    public void salvaMetaCarga(List<MetaCarga> metaCargas) throws ErroSistemicoException {
        try {
        	for(MetaCarga m :metaCargas ) {
        		salvaMetaCarga(m);
        	}
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }
    /**
     * Altera o objeto MetaCarga na entidade
     *
     * @param {@link MetaCarga}
     */
    public void alteraMetaCarga(MetaCarga metaCarga) throws ErroSistemicoException {
        try {
        	cargaDAO.salvar(metaCarga.getListaStatus());
            
        	super.atualizar(metaCarga);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Remove o objeto MetaCarga da entidade
     *
     * @param {@link MetaCarga}
     */
    public void removeMetaCarga(MetaCarga MetaCarga) throws ErroSistemicoException {
        try {
            cargaDAO.removeCarga(MetaCarga.getListaStatus());
        	super.deletar(MetaCarga);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca MetaCarga da entidade MetaCarga por exemplo
     *
     * @param {@link MetaCarga}
     * @return link List<MetaCarga>}
     */
    public List<MetaCarga> buscaPorExemploMetaCarga(MetaCarga MetaCarga) throws ErroSistemicoException {
        try {
            List<MetaCarga> listaPesquisada = super.buscarListaDeObjetos(MetaCarga);            
//            super.encerrarSessao();
            return listaPesquisada;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

}
