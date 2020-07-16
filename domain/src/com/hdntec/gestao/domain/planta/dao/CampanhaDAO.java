/**
 * 
 */
package com.hdntec.gestao.domain.planta.dao;

import java.util.List;

import org.hibernate.HibernateException;

import com.hdntec.gestao.domain.planta.entity.status.Campanha;
import com.hdntec.gestao.domain.produto.dao.QualidadeDAO;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;


/**
 * Classe DAO para persistir a entidade Campanha
 * 
 * @author hdn.luchetta -> Rodrigo Luchetta
 * 
 */
public class CampanhaDAO extends AbstractGenericDAO<Campanha> {

    private QualidadeDAO dao = new QualidadeDAO();
	/**
     * Salva campanha berco na entidade
     *
     * @param {@link Campanha}
     */
    public Campanha salvaCampanha(Campanha campanha) throws ErroSistemicoException {
        try {
        	dao.salvaQualidade(campanha.getQualidadeEstimada());
        	Campanha campanhaSalva = super.salvar(campanha);            
//            super.encerrarSessao();
            return campanhaSalva;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    public List<Campanha> salvaCampanha(List<Campanha> campanha) throws ErroSistemicoException {
        try {
        	for (Campanha c : campanha) {
        		dao.salvaQualidade(c.getQualidadeEstimada());
        	}	
        	List<Campanha> campanhaSalva = super.salvar(campanha);            
//            super.encerrarSessao();
            return campanhaSalva;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }
    
    /**
     * Altera o objeto campanha na entidade
     *
     * @param {@link Campanha}
     */
    public void alteraCampanha(Campanha campanha) throws ErroSistemicoException {
        try {
            super.atualizar(campanha);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Remove o objeto campanha da entidade
     *
     * @param {@link Campanha}
     */
    public void removeCampanha(Campanha campanha) throws ErroSistemicoException {
        try {
            super.deletar(campanha);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca campanha da entidade Campanha por exemplo
     *
     * @param {@link Campanha}
     * @return link List<Campanha>}
     */
    public List<Campanha> buscaPorExemploCampanha(Campanha campanha) throws ErroSistemicoException {
        try {
            List<Campanha> listaPesquisada = super.buscarListaDeObjetos(campanha);
//            super.encerrarSessao();
            return listaPesquisada;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }
}
