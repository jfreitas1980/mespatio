package com.hdntec.gestao.domain.produto.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.hdntec.gestao.domain.produto.entity.TipoItemDeControle;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;
import com.hdntec.gestao.hibernate.HibernateUtil;


/**
 * Classe DAO para persistir a entidade TipoItemDeControle
 * 
 * @author hdn.luchetta -> Rodrigo Luchetta
 * 
 */
public class TipoItemDeControleDAO extends AbstractGenericDAO<TipoItemDeControle> {

    /**
     * Salva objeto tipoItemControle na entidade
     * @param {@link TipoItemDeControleDAO}
     */
    public TipoItemDeControle salvaTipoItemDeControle(TipoItemDeControle tipoItemControle) throws ErroSistemicoException {
        try {
        	TipoItemDeControle tipoItemDeControleSalvo = super.salvar(tipoItemControle);            
            return tipoItemDeControleSalvo;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }
    
    /**
     * Salva lista objeto tipoItemControle na entidade
     * @param {@link List<TipoItemDeControleDAO>}
     */
    public void salvaListaTiposItemDeControle(List<TipoItemDeControle> listaTiposItemControle) throws ErroSistemicoException {
        try {
            super.salvar(listaTiposItemControle);            
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Altera o objeto tipoItemControle na entidade
     * @param {@link TipoItemDeControleDAO}
     */
    public void alteraTipoItemDeControle(TipoItemDeControle tipoItemControle)  throws ErroSistemicoException {
        try {
            super.atualizar(tipoItemControle);            
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Remove o objeto tipoItemControle da entidade
     * @param {@link TipoItemDeControleDAO}
     */
    public void removeTipoItemDeControle(TipoItemDeControle tipoItemControle) throws ErroSistemicoException {
        try {
            super.deletar(tipoItemControle);            
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca tipoItemControle da entidade TipoItemDeControle por exemplo
     * @param {@link TipoItemDeControleDAO}
     * @return link List<TipoItemDeControle>}
     */
    public List<TipoItemDeControle> buscaPorExemploTipoItemDeControle(TipoItemDeControle tipoItemControle) throws ErroSistemicoException {
        try {
            List<TipoItemDeControle> listaPesquisada = super.buscarListaDeObjetos(tipoItemControle);                      
            return listaPesquisada;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca um tipo de item de controle especifico
     * @param itemControle
     * @return
     * @throws com.hdntec.gestao.exceptions.ErroSistemicoException
     */
    public TipoItemDeControle buscarTipoItemControle(TipoItemDeControle itemControle) throws ErroSistemicoException {
        try {
            TipoItemDeControle tipoPesquisado = super.buscarPorObjeto(itemControle);
            if (tipoPesquisado != null) {
                tipoPesquisado.getListaDeMetasInternas().size();
            }            
            return tipoPesquisado;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    public List<TipoItemDeControle> buscarTodos() throws ErroSistemicoException {
    	List<TipoItemDeControle> result = null;
    	try {
        	Session session =  HibernateUtil.getSession();
        	//Montando a consulta
    		StringBuilder sSql = new StringBuilder();
    		sSql.append("select distinct(t) from TipoItemDeControle t order by descricaoTipoItemControle desc");     	    		
    		//Criando a query e passando os parametros
    		Query q = session.createQuery(sSql.toString());    		
    		//Executando a consulta
    		result = q.list();    		
//    		super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
        return result;
    }
    
    
    
   /* *//**
     * Metodo auxiliar que acessa as listas de um objeto TipoItemDeControle
     * para que as listas EAGER sejam carregadas e retornadas para interface jah preenchidas
     * evitando o erro EAGERInitializationException.
     *
     * @param List<TipoItemDeControle>
     *//*
    private void acessarListaTipoItemDeControle(List<TipoItemDeControle> listaTipoItemControle) {
        for (TipoItemDeControle tipoItemControle : listaTipoItemControle) {
            tipoItemControle.getListaDeMetasInternas().size();
        }
    }*/

}
