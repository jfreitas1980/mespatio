package com.hdntec.gestao.domain.planta.dao;

import java.util.List;

import org.hibernate.HibernateException;

import com.hdntec.gestao.domain.planta.entity.MetaBaliza;
import com.hdntec.gestao.domain.planta.entity.MetaUsina;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;


/**
 * Classe DAO para persistir a entidade Usina
 * 
 * @author hdn.luchetta -> Rodrigo Luchetta
 * 
 */
public class MetaUsinaDAO extends AbstractGenericDAO<MetaUsina> {

    private UsinaDAO dao = new UsinaDAO();
    private CampanhaDAO campanhaDAO = new CampanhaDAO();
	/**
     * Salva objeto usina na entidade
     *
     * @param {@link MetaUsina}
     */
    public MetaUsina salvaMetaUsina(MetaUsina usina) throws ErroSistemicoException {
        try {
            MetaUsina usinaSalva = super.salvar(usina);
            dao.salvar(usina.getListaStatus());
            campanhaDAO.salvaCampanha(usina.getListaCampanhas());
//            super.encerrarSessao();
            return usinaSalva;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }
    
    public List<MetaUsina> salvaMetaUsina(List<MetaUsina> usina) throws ErroSistemicoException {
        try {
        	List<MetaUsina> usinaSalva = super.salvar(usina);
            for (MetaUsina mBaliza : usina) {
            	dao.salvar(mBaliza.getListaStatus());
            	campanhaDAO.salvaCampanha(mBaliza.getListaCampanhas());
            }
            
            
//            super.encerrarSessao();
            return usinaSalva;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Altera o objeto usina na entidade
     *
     * @param {@link MetaUsina}
     */
    public void alteraMetaUsina(MetaUsina usina) throws ErroSistemicoException {
        try {
            super.atualizar(usina);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Remove o objeto usina da entidade
     *
     * @param {@link MetaUsina}
     */
    public void removeMetaUsina(MetaUsina usina) throws ErroSistemicoException {
        try {
            super.deletar(usina);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca usina da entidade MetaUsina por exemplo
     *
     * @param {@link MetaUsina}
     * @return link List<MetaUsina>}
     */
    public List<MetaUsina> buscaPorExemploMetaUsina(MetaUsina usina) throws ErroSistemicoException {
        try {
            List<MetaUsina> listaPesquisada = super.buscarListaDeObjetos(usina);
//            super.encerrarSessao();
            return listaPesquisada;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }
}
