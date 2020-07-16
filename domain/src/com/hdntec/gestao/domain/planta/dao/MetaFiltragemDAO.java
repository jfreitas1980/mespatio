package com.hdntec.gestao.domain.planta.dao;

import java.util.List;

import org.hibernate.HibernateException;

import com.hdntec.gestao.domain.planta.entity.MetaBaliza;
import com.hdntec.gestao.domain.planta.entity.MetaFiltragem;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;


/**
 * Classe DAO para persistir a entidade Usina
 * 
 * @author hdn.luchetta -> Rodrigo Luchetta
 * 
 */
public class MetaFiltragemDAO extends AbstractGenericDAO<MetaFiltragem> {

    private FiltragemDAO dao = new FiltragemDAO();
	/**
     * Salva objeto filtragem na entidade
     *
     * @param {@link MetaFiltragem}
     */
    public MetaFiltragem salvaMetaFiltragem(MetaFiltragem filtragem) throws ErroSistemicoException {
        try {
            MetaFiltragem usinaSalva = super.salvar(filtragem);
            dao.salvar(filtragem.getListaStatus());
//            super.encerrarSessao();
            return usinaSalva;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    public List<MetaFiltragem> salvaMetaFiltragem(List<MetaFiltragem> filtragem) throws ErroSistemicoException {
        try {
        	List<MetaFiltragem>  usinaSalva = super.salvar(filtragem);
            for (MetaFiltragem mBaliza : filtragem) {
            	dao.salvar(mBaliza.getListaStatus());
            }
//            super.encerrarSessao();
            return usinaSalva;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    
    /**
     * Altera o objeto filtragem na entidade
     *
     * @param {@link MetaFiltragem}
     */
    public void alteraMetaFiltragem(MetaFiltragem filtragem) throws ErroSistemicoException {
        try {
            super.atualizar(filtragem);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Remove o objeto filtragem da entidade
     *
     * @param {@link MetaFiltragem}
     */
    public void removeMetaFiltragem(MetaFiltragem filtragem) throws ErroSistemicoException {
        try {
            super.deletar(filtragem);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca filtragem da entidade MetaFiltragem por exemplo
     *
     * @param {@link MetaFiltragem}
     * @return link List<MetaFiltragem>}
     */
    public List<MetaFiltragem> buscaPorExemploMetaFiltragem(MetaFiltragem usina) throws ErroSistemicoException {
        try {
            List<MetaFiltragem> listaPesquisada = super.buscarListaDeObjetos(usina);
//            super.encerrarSessao();
            return listaPesquisada;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }
}
