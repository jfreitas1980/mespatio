package com.hdntec.gestao.domain.planta.dao;

import java.util.Collections;
import java.util.List;

import org.hibernate.HibernateException;

import com.hdntec.gestao.domain.planta.comparadores.ComparadorMetaBalizas;
import com.hdntec.gestao.domain.planta.entity.MetaBaliza;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;


/**
 * Classe DAO para persistir a entidade MetaBaliza
 * 
 * @author hdn.luchetta -> Rodrigo Luchetta
 * 
 */
public class MetaBalizaDAO extends AbstractGenericDAO<MetaBaliza> {

	private BalizaDAO dao = new BalizaDAO();
	/**
     * Salva objeto baliza na entidade
     * @param {@link MetaBaliza}
     */
    public MetaBaliza salvaMetaBaliza(MetaBaliza baliza) throws ErroSistemicoException {
        try {                    	
        	MetaBaliza balizaSalva = super.salvar(baliza);
        	dao.salvaListaBalizas(baliza.getListaStatus());
//            super.encerrarSessao();
            return balizaSalva;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Salva objeto baliza na entidade
     * @param {@link MetaBaliza}
     */
    public void salvaListaMetaBalizas(List<MetaBaliza> listaMetaBaliza) throws ErroSistemicoException {
        try {                    	
        	super.salvar(listaMetaBaliza);
        	for (MetaBaliza mBaliza : listaMetaBaliza) {
            	dao.salvaListaBalizas(mBaliza.getListaStatus());
            }
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Altera o objeto baliza na entidade
     * @param {@link MetaBaliza}
     */
    public void alteraMetaBaliza(MetaBaliza baliza) throws ErroSistemicoException {
        try {        	            
        	super.atualizar(baliza);
        	dao.salvaListaBalizas(baliza.getListaStatus());            
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Remove o objeto baliza da entidade
     * @param {@link MetaBaliza}
     */
    public void removeMetaBaliza(MetaBaliza baliza) throws ErroSistemicoException {
        try {        	
        	super.deletar(baliza);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca baliza da entidade MetaBaliza por exemplo
     * @param {@link MetaBaliza}
     * @return link List<MetaBaliza>}
     */
    public List<MetaBaliza> buscaPorExemploMetaBaliza(MetaBaliza baliza) throws ErroSistemicoException {
        try {
            List<MetaBaliza> listaMetaBalizas = super.buscarListaDeObjetos(baliza);
            Collections.sort(listaMetaBalizas, new ComparadorMetaBalizas());
            return listaMetaBalizas;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }
}
