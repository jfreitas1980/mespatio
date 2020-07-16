package com.hdntec.gestao.domain.planta.dao;


import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.hdntec.gestao.domain.planta.entity.MetaCorreia;
import com.hdntec.gestao.domain.planta.entity.MetaMaquinaDoPatio;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;
import com.hdntec.gestao.hibernate.HibernateUtil;


/**
 * Classe DAO para persistir a entidade Correia
 * 
 * @author hdn.luchetta -> Rodrigo Luchetta
 * 
 */
public class MetaCorreiaDAO extends AbstractGenericDAO<MetaCorreia> {

    private CorreiaDAO dao = new CorreiaDAO();
	/**
     * Salva correia berco na entidade
     *
     * @param {@link MetaCorreia}
     */
    public MetaCorreia salvaMetaCorreia(MetaCorreia correia) throws ErroSistemicoException {
        try {
            MetaCorreia correiaSalva = super.salvar(correia);
            dao.salvar(correia.getListaStatus());
//            super.encerrarSessao();
            return correiaSalva;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    public List<MetaCorreia> salvaMetaCorreia(List<MetaCorreia> correia) throws ErroSistemicoException {
        try {
        	List<MetaCorreia> correiaSalva = super.salvar(correia);
        	  for (MetaCorreia mCorreia : correia) {
               	dao.salvar(mCorreia.getListaStatus());
               }             
//            super.encerrarSessao();
            return correiaSalva;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }
    
    /**
     * Altera o objeto correia na entidade
     *
     * @param {@link MetaCorreia}
     */
    public void alteraMetaCorreia(MetaCorreia correia) throws ErroSistemicoException {
        try {
            super.atualizar(correia);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Remove o objeto correia da entidade
     *
     * @param {@link MetaCorreia}
     */
    public void removeMetaCorreia(MetaCorreia correia) throws ErroSistemicoException {
        try {
            super.deletar(correia);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca correia da entidade MetaCorreia por exemplo
     *
     * @param {@link MetaCorreia}
     * @return
     */
    public MetaCorreia buscarMetaCorreia(MetaCorreia correia) throws ErroSistemicoException {
        try {
            MetaCorreia correiaRecuperada = super.buscarPorObjeto(correia);
//            super.encerrarSessao();
            return correiaRecuperada;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }
    
    /**
     * Busca correia da entidade MetaCorreia por exemplo
     *
     * @param {@link MetaCorreia}
     * @return link List<MetaCorreia>}
     */
    public List<MetaCorreia> buscaPorExemploMetaCorreia(MetaCorreia correia) throws ErroSistemicoException {
        try {
            List<MetaCorreia> listaMetaCorreia = super.buscarListaDeObjetos(correia);
            //Collections.sort(listaMetaCorreia, new ComparadorMetaCorreias());
//            super.encerrarSessao();
            return listaMetaCorreia;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }
    
    /**
     * Retorna uma lista de correias com suas manutencoes dentro do periodo informado
     * @param dataInicial
     * @param dataFinal
     * @return lista MetaCorreias
     */
    @SuppressWarnings("unchecked")
	public List<MetaCorreia> buscaListaMetaCorreiaPorManutencao(
			MetaCorreia correia, Date dataInicial, Date dataFinal) {
    	Session session = HibernateUtil.getSession();
    	StringBuilder sSql = new StringBuilder();
    	sSql.append("select distinct(c) from "+MetaCorreia.class.getName());
    	sSql.append(" c join fetch c.listaManutencao as manutencoes ");
    	sSql.append(" where manutencoes.dataInicial >= :dataInicial ");
    	sSql.append(" and manutencoes.dataFinal <= :dataFinal");
    	if (correia != null) {
    		sSql.append(" and c.nomeMetaCorreia = :nomeMetaCorreia");
    	}
    	Query q = session.createQuery(sSql.toString());
    	q.setParameter("dataInicial", dataInicial);
    	q.setParameter("dataFinal", dataFinal);
    	if (correia != null) {
    		q.setParameter("nomeMetaCorreia", correia.getNomeCorreia());
    	}
    	List<MetaCorreia> result = q.list();

    	return result;
    }
}
