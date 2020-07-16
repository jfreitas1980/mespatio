package com.hdntec.gestao.domain.produto.dao;

import java.util.List;

import org.hibernate.HibernateException;

import com.hdntec.gestao.domain.produto.entity.ItemDeControle;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;


/**
 * Classe DAO para persistir a entidade ItemDeControle
 * 
 * @author hdn.luchetta -> Rodrigo Luchetta
 * 
 */
public class ItemDeControleDAO extends AbstractGenericDAO<ItemDeControle> {

    /**
     * Salva objeto itemControle na entidade
     *
     * @param {@link ItemDeControleDAO}
     */
    public ItemDeControle salvaItemDeControle(ItemDeControle itemControle) throws ErroSistemicoException {
        try {
            ItemDeControle itemSalvo = super.salvar(itemControle);
//            super.encerrarSessao();
            return itemSalvo;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Altera o objeto itemControle na entidade
     *
     * @param {@link ItemDeControleDAO}
     */
    public void alteraItemDeControle(ItemDeControle itemControle) throws ErroSistemicoException {
        try {
            super.atualizar(itemControle);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Remove o objeto itemControle da entidade
     *
     * @param {@link ItemDeControleDAO}
     */
    public void removeItemDeControle(ItemDeControle itemControle) throws ErroSistemicoException {
        try {
            super.deletar(itemControle);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca itemControle da entidade ItemDeControle por exemplo
     *
     * @param {@link ItemDeControleDAO}
     * @return link List<ItemDeControle>}
     */
    public List<ItemDeControle> buscaPorExemploItemDeControle(ItemDeControle itemControle) throws ErroSistemicoException {
        try {
            List<ItemDeControle> listaPesquisada = super.buscarListaDeObjetos(itemControle);
              super.encerrarSessao();
            return listaPesquisada;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    
}
