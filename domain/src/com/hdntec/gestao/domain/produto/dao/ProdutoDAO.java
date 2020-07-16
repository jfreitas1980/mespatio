package com.hdntec.gestao.domain.produto.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.hdntec.gestao.domain.plano.entity.SituacaoPatio;
import com.hdntec.gestao.domain.produto.entity.Produto;
import com.hdntec.gestao.domain.produto.entity.Qualidade;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;
import com.hdntec.gestao.hibernate.HibernateUtil;


/**
 * Classe DAO para persistir a entidade Produto
 * 
 * @author hdn.luchetta -> Rodrigo Luchetta
 * 
 */
public class ProdutoDAO extends AbstractGenericDAO<Produto> {

    private QualidadeDAO qualidadeDAO = new QualidadeDAO();
    private RastreabilidadeDAO rastreabilidadeDAO = new RastreabilidadeDAO();
	/**
     * Salva objeto produto na entidade
     * @param {@link ProdutoDAO}
     */
    public Produto salvarProduto(Produto produto) throws ErroSistemicoException {
        try {       
            qualidadeDAO.salvaQualidade(produto.getQualidade());
            rastreabilidadeDAO.salvar(produto.getListaDeRastreabilidades());
            Produto produtoSalvo = super.salvar(produto);            
            //produtoSalvo.setQualidade(qualidade);
            //produtoSalvo.setQualidade(qualidade);
//            super.encerrarSessao();
            return produtoSalvo;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Altera o objeto produto na entidade
     * @param {@link ProdutoDAO}
     */
    public void alterarProduto(Produto produto) throws ErroSistemicoException {
        try {
            super.atualizar(produto);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Remove o objeto produto da entidade
     * @param {@link ProdutoDAO}
     */
    public void removerProduto(Produto produto) throws ErroSistemicoException {
        try {
        	
        	Qualidade qualidade = produto.getQualidade();
        	produto.setQualidade(null);
        	super.deletar(produto);        	
        	if (qualidade != null) {
        		List<Produto> listProdutosQualidade = getByQualidade(qualidade);
        	
        		if (listProdutosQualidade != null && listProdutosQualidade.size() == 0) {        		
        			qualidadeDAO.removeQualidade(qualidade);		        		
        		}
        	}	
            //rastreabilidadeDAO.removerRastreabilidade(produto.getListaDeRastreabilidades());        	
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca produto da entidade Produto por exemplo
     *
     * @param {@link ProdutoDAO}
     * @return link List<Produto>}
     */
    public List<Produto> buscaPorExemploProduto(Produto produto) throws ErroSistemicoException {
        try {
            List<Produto> listaPesquisada = super.buscarListaDeObjetos(produto);
//            super.encerrarSessao();
            return listaPesquisada;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }
    
    
    public List<Produto> getByQualidade(Qualidade qualidade) {
        Session session = HibernateUtil.getSession();
        //Montando a consulta
        StringBuilder sSql = new StringBuilder();
        sSql.append("SELECT DISTINCT p FROM Produto p WHERE p.qualidade =  :qualidade) order by idProduto");
        
        
        //Criando a query e passando os parametros
        Query q = session.createQuery(sSql.toString());
        q.setParameter("qualidade",qualidade);                
        
        
        //Executando a consulta
        List<Produto> produtosLista = q.list();
        //		super.encerrarSessao();
        return produtosLista;
    }
}
