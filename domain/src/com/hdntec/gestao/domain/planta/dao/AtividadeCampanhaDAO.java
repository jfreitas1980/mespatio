package com.hdntec.gestao.domain.planta.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.hdntec.gestao.domain.planta.entity.status.AtividadeCampanha;
import com.hdntec.gestao.domain.planta.entity.status.Campanha;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;
import com.hdntec.gestao.hibernate.HibernateUtil;


/**
 *
 * @author Ricardo Trabalho
 */
public class AtividadeCampanhaDAO extends AbstractGenericDAO<AtividadeCampanha> {

    /**
     * Salva atividade campanha na entidade
     * @param {@link AtividadeCampanha}
     */
    public AtividadeCampanha salvaAtividadeCampanha(AtividadeCampanha atividadeCampanha) throws ErroSistemicoException {
        try {
            AtividadeCampanha atividadeSalva = super.salvar(atividadeCampanha);
//            super.encerrarSessao();
            return atividadeSalva;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Altera o objeto atividade campanha na entidade
     * @param {@link AtividadeCampanha}
     */
    public void alteraAtividadeCampanha(AtividadeCampanha atividadeCampanha) throws ErroSistemicoException {
        try {
            super.atualizar(atividadeCampanha);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Remove o objeto atividade campanha da entidade
     * @param {@link AtividadeCampanha}
     */
    public void removeAtividadeCampanha(AtividadeCampanha atividadeCampanha) throws ErroSistemicoException {
        try {
            super.deletar(atividadeCampanha);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Remove o objeto atividade campanha da entidade
     * @param {@link AtividadeCampanha}
     */
    public List<AtividadeCampanha> buscarPorCampanha(Campanha campanha) throws ErroSistemicoException {
        List<AtividadeCampanha> objPesquisado = null;
        try {
            Session session = HibernateUtil.getSession();
            Criteria criteria = session.createCriteria(AtividadeCampanha.class, "sp");
            criteria.add(Restrictions.eq("sp.campanha", campanha));
            objPesquisado = criteria.list();
        
         } catch (HibernateException hbex) {
             hbex.printStackTrace();
             throw new ErroSistemicoException(hbex.getMessage());
          } finally {
//           encerrarSessao();
        }
        return objPesquisado;
        
    }

    
    /**
     * Busca atividade campanha da entidade AtividadeCampanha por exemplo
     * @param {@link AtividadeCampanha}
     * @return link List<AtividadeCampanha>}
     */
    public List<AtividadeCampanha> buscaPorExemploAtividadeCampanha(AtividadeCampanha atividadeCampanha) throws ErroSistemicoException {
        try {
            List<AtividadeCampanha> listaPesquisada = super.buscarListaDeObjetos(atividadeCampanha);
//            super.encerrarSessao();
            return listaPesquisada;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }
}
