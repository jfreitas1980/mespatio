package com.hdntec.gestao.domain.planta.dao;

import java.util.List;

import org.hibernate.HibernateException;

import com.hdntec.gestao.domain.planta.entity.MetaCarregadoraDeNavios;
import com.hdntec.gestao.domain.planta.entity.MetaPatio;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;


/**
 * Classe DAO para persistir a entidade Patio
 * 
 * @author hdn.luchetta -> Rodrigo Luchetta
 * 
 */
public class MetaPatioDAO extends AbstractGenericDAO<MetaPatio> {

    private PatioDAO dao = new PatioDAO();
	/**
     * Salva objeto patio na entidade
     *
     * @param {@link MetaPatio}
     */
    public MetaPatio salvaMetaPatio(MetaPatio patio) throws ErroSistemicoException {
        try {
            MetaPatio patioSalvo = super.salvar(patio);
            dao.salvar(patio.getListaStatus());
//            super.encerrarSessao();
            return patioSalvo;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    public List<MetaPatio> salvaMetaPatio(List<MetaPatio> patio) throws ErroSistemicoException {
        try {
        	List<MetaPatio> patioSalvo = super.salvar(patio);
            for (MetaPatio mMaquina : patio) {
             	dao.salvar(mMaquina.getListaStatus());
             }                
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
     * @param {@link MetaPatio}
     */
    public void alteraMetaPatio(MetaPatio patio) throws ErroSistemicoException {
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
     * @param {@link MetaPatio}
     */
    public void removeMetaPatio(MetaPatio patio) throws ErroSistemicoException {
        try {
            super.deletar(patio);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca patio da entidade MetaPatio por exemplo
     *
     * @param {@link MetaPatio}
     * @return link List<MetaPatio>}
     */
    public List<MetaPatio> buscaPorExemploMetaPatio(MetaPatio patio) throws ErroSistemicoException {
        try {
            List<MetaPatio> listaMetaPatios = super.buscarListaDeObjetos(patio);            
//            super.encerrarSessao();
            return listaMetaPatios;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }
}
