package com.hdntec.gestao.domain.produto.dao;

import java.util.List;

import org.hibernate.HibernateException;

import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;


/**
 * Classe DAO para persistir a entidade TipoProduto
 * 
 * @author hdn.luchetta -> Rodrigo Luchetta
 * 
 */
public class TipoProdutoDAO extends AbstractGenericDAO<TipoProduto> {

    /**
     * Salva objeto tipoProduto na entidade
     *
     * @param {@link TipoProdutoDAO}
     */
		
    public TipoProduto salvaTipoProduto(TipoProduto tipoProduto) throws ErroSistemicoException {
        try {
            TipoProduto tipoProdutoSalvo = super.salvar(tipoProduto);
//            super.encerrarSessao();
            return tipoProdutoSalvo;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Salva objeto tipoProduto na entidade
     *
     * @param {@link TipoProdutoDAO}
     */
    public void salvaListaTiposProduto(List<TipoProduto> listaTiposProduto) throws ErroSistemicoException {
        try {
            super.salvar(listaTiposProduto);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Altera o objeto tipoProduto na entidade
     *
     * @param {@link TipoProdutoDAO}
     */
    public void alteraTipoProduto(TipoProduto tipoProduto) throws ErroSistemicoException {
        try {
            super.atualizar(tipoProduto);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Remove o objeto tipoProduto da entidade
     *
     * @param {@link TipoProdutoDAO}
     */
    public void removeTipoProduto(TipoProduto tipoProduto) throws ErroSistemicoException {
        try {
            super.deletar(tipoProduto);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca tipoProduto da entidade TipoProduto por exemplo
     *
     * @param {@link TipoProdutoDAO}
     * @return link List<TipoProduto>}
     */
    public List<TipoProduto> buscaPorExemploTipoProduto(TipoProduto tipoProduto) throws ErroSistemicoException {
        try {
            List<TipoProduto> listaPesquisada = super.buscarListaDeObjetos(tipoProduto);
//            super.encerrarSessao();
            return listaPesquisada;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca por TipoProduto por exemplo
     * @param tipoProduto
     * @return
     */
    public TipoProduto buscaPorTipoProduto(TipoProduto tipoProduto) throws ErroSistemicoException {
        try {
            TipoProduto tipoProdutoPesquisado = super.buscarPorObjeto(tipoProduto);
//            super.encerrarSessao();
            return tipoProdutoPesquisado;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }
}
