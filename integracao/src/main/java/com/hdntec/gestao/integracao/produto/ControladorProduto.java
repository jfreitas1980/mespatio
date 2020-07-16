package com.hdntec.gestao.integracao.produto;

import java.util.List;

import javax.ejb.Stateless;

import com.hdntec.gestao.domain.produto.dao.ProdutoDAO;
import com.hdntec.gestao.domain.produto.dao.TipoItemDeControleDAO;
import com.hdntec.gestao.domain.produto.dao.TipoProdutoDAO;
import com.hdntec.gestao.domain.produto.entity.Produto;
import com.hdntec.gestao.domain.produto.entity.TipoItemDeControle;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.HibernateUtil;


/**
 * Controlador das operações do subsistema de produto
 * 
 * @author andre
 * 
 */
@Stateless(name = "bs/ControladorProduto", mappedName = "bs/ControladorProduto")
public class ControladorProduto implements IControladorProduto {

   /**
    * Construtor padrão
    */
   public ControladorProduto()
   {
   }

   /** o produto controlador*/
   private Produto produto;
   
    /**
    * @param produto
    */
   public ControladorProduto(Produto produto)
   {
      this.produto = produto;
   }

   @Override
    public Produto salvaProduto(Produto produto) throws ErroSistemicoException {
        ProdutoDAO produtoDAO = new ProdutoDAO();
        Produto produtoSalvo = produtoDAO.salvarProduto(produto);
        return produtoSalvo;
    }

    @Override
    public TipoProduto salvaTipoProduto(TipoProduto tipoProduto) throws ErroSistemicoException {
        TipoProdutoDAO tipoProdutoDAO = new TipoProdutoDAO();
        return tipoProdutoDAO.salvaTipoProduto(tipoProduto);
    }
    
    @Override
    public void removeTipoProduto(TipoProduto tipoProduto) throws ErroSistemicoException {
        TipoProdutoDAO tipoProdutoDAO = new TipoProdutoDAO();
        tipoProdutoDAO.removeTipoProduto(tipoProduto);
    }

    @Override
    public List<TipoProduto> buscarTiposProduto(TipoProduto tipoProduto) throws ErroSistemicoException {
        TipoProdutoDAO tipoProdutoDAO = new TipoProdutoDAO();
        return tipoProdutoDAO.buscaPorExemploTipoProduto(tipoProduto);
    }

    @Override
    public TipoProduto buscarTipoProduto(TipoProduto tipoProduto) throws ErroSistemicoException {
    	TipoProdutoDAO tipoProdutoDAO = new TipoProdutoDAO();
        return tipoProdutoDAO.buscaPorTipoProduto(tipoProduto);
    }

    @Override
    public void salvaListaTiposProduto(List<TipoProduto> listaTiposProduto) throws ErroSistemicoException {
        TipoProdutoDAO tipoProdutoDAO = new TipoProdutoDAO();
        tipoProdutoDAO.salvaListaTiposProduto(listaTiposProduto);
    }

    @Override
    public void salvarTiposItemControle(List<TipoItemDeControle> listaItensControle) throws ErroSistemicoException {
        TipoItemDeControleDAO tipoItemDAO = new TipoItemDeControleDAO();
        tipoItemDAO.salvaListaTiposItemDeControle(listaItensControle);
    }

    @Override
    public List<TipoItemDeControle> buscarTiposItemControle() throws ErroSistemicoException {
        TipoItemDeControleDAO tipoItemDAO = new TipoItemDeControleDAO();
//		List<TipoItemDeControle> listaRetorno = tipoItemDAO.findAll(TipoItemDeControle.class);
        //Collections.sort(listaRetorno, new ComparadorTipoItemControle());
        return tipoItemDAO.findAll(TipoItemDeControle.class);
    }

    @Override
    public TipoItemDeControle buscarTipoItemControle(TipoItemDeControle itemControle) throws ErroSistemicoException {
        TipoItemDeControleDAO tipoItemDAO = new TipoItemDeControleDAO();
        TipoItemDeControle tipoItemPesquisado = tipoItemDAO.buscarTipoItemControle(itemControle);
        return tipoItemPesquisado;
    }

    @Override
    public void encerrarSessao() {
    	HibernateUtil.closeSession();
    }
}
