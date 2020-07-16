package com.hdntec.gestao.integracao.produto;

import java.util.List;

import javax.ejb.Local;

import com.hdntec.gestao.domain.produto.entity.Produto;
import com.hdntec.gestao.domain.produto.entity.TipoItemDeControle;
import com.hdntec.gestao.domain.produto.entity.TipoProduto;
import com.hdntec.gestao.exceptions.ErroSistemicoException;


/**
 * Interface de acesso às operações do subsistema de produtos.
 * 
 * @author andre
 * 
 */
@Local
public interface IControladorProduto {

    /**
     * Persiste o objeto produto no banco de dados
     * 
     * @param produto
     */
    public Produto salvaProduto(Produto produto) throws ErroSistemicoException;

    /**
     * Persiste o objeto tipo de produto no banco de dados
     * 
     * @param tipoProduto
     */
    public TipoProduto salvaTipoProduto(TipoProduto tipoProduto) throws ErroSistemicoException;

    /**
	 * Remove da base o tipo de produto
     * removeTipoProduto
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 08/07/2009
     * @see
     * @param tipoProduto
     * @throws ErroSistemicoException
     * @return Returns the void.
     */
    void removeTipoProduto(TipoProduto tipoProduto) throws ErroSistemicoException;
    
    /**
     * Busca uma lista com o tipo de produto cadastrado
     * @return List<TipoProduto>
     */
    public List<TipoProduto> buscarTiposProduto(TipoProduto tipoProduto)throws ErroSistemicoException;
    
    /**
     * 
     * buscarTipoProduto
     * @author <a href="mailto:hdn.darley@cflex.com.br">Darley</a>
     * @since 14/07/2009
     * @see
     * @param tipoProduto
     * @return
     * @throws ErroSistemicoException
     * @return Returns the TipoProduto.
     */
    public TipoProduto buscarTipoProduto(TipoProduto tipoProduto) throws ErroSistemicoException;

    /**
     * Salva uma lista de tipo de item de controle
     * @return List<TipoProduto>
     */
    public void salvarTiposItemControle(List<TipoItemDeControle> listaItensControle) throws ErroSistemicoException;

    /**
     * Busca uma lista com o tipo de item de controle cadastrado
     * @return List<TipoProduto>
     */
    public List<TipoItemDeControle> buscarTiposItemControle() throws ErroSistemicoException;


    /**
     * ca um tipo de item de controle especifico
     * @param itemControle
     * @return TipoItemDeControle
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     */
    public TipoItemDeControle buscarTipoItemControle(TipoItemDeControle itemControle) throws ErroSistemicoException;

    /**
     * Salva uma lista de tipos de produto na base 
     * @param listaTiposProduto
     * @throws com.hdntec.gestao.domain.exceptions.ErroSistemicoException
     */
    public void salvaListaTiposProduto(List<TipoProduto> listaTiposProduto) throws ErroSistemicoException;
    
    
    public void encerrarSessao();

}
