package com.hdntec.gestao.domain.navios.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.hdntec.gestao.domain.navios.entity.status.Carga;
import com.hdntec.gestao.domain.navios.entity.status.OrientacaoDeEmbarque;
import com.hdntec.gestao.domain.produto.dao.ProdutoDAO;
import com.hdntec.gestao.domain.produto.entity.Produto;
import com.hdntec.gestao.domain.produto.entity.Qualidade;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;
import com.hdntec.gestao.hibernate.HibernateUtil;


/**
 * Classe DAO para a entidade forte carga
 * 
 * @author Leonel
 */
public class CargaDAO extends AbstractGenericDAO<Carga> {

    private ProdutoDAO produtoDAO = new ProdutoDAO();
    private OrientacaoDeEmbarqueDAO orientacaoDeEmbarqueDAO = new OrientacaoDeEmbarqueDAO();
	/**
     * salva entidade carga
     * @param carga
     */
    public Carga salvaCarga(Carga carga) throws ErroSistemicoException {
        try {
           if (carga.getProduto() != null) {
        	   produtoDAO.salvarProduto(carga.getProduto());
           }
          
           if (carga.getOrientacaoDeEmbarque() != null) {
               orientacaoDeEmbarqueDAO.salvaOrientacaoDeEmbarque(carga.getOrientacaoDeEmbarque());
            }
            
           Carga cargaSalva = super.salvar(carga);
        
           // super.encerrarSessao();
            return cargaSalva;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    
    /**
     * salva entidade carga
     * @param carga
     */
    public void salvaCarga(List<Carga> cargas) throws ErroSistemicoException {
        try {
           for(Carga c : cargas) {
        	   salvaCarga(c);
           }
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }
    /**
     * altera entidade carga
     * @param carga
     */
    public void alteraCarga(Carga carga) throws ErroSistemicoException {
        try {
            super.atualizar(carga);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * remove entidade carga
     * @param carga
     */
    public void removeCarga(Carga carga) throws ErroSistemicoException {
        try {
        	
    		OrientacaoDeEmbarque oE = carga.getOrientacaoDeEmbarque();
    	    Produto produto = carga.getProduto();
    		carga.setOrientacaoDeEmbarque(null);
        	carga.setProduto(null);
    	    super.deletar(carga);        	
        	
        	List<Carga> listOrientacaoCarga = getByOrientacaoDeEmbarque(oE);
        	
        	if (listOrientacaoCarga != null && listOrientacaoCarga.size() == 0 && oE != null) {        		
        		orientacaoDeEmbarqueDAO.removeOrientacaoDeEmbarque(oE);		        		
        	}	    
        	
        	if (produto != null) {
         	   produtoDAO.removerProduto(produto);
            }
            
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    public void removeCarga(List<Carga> cargas) throws ErroSistemicoException {
        try {
            for (Carga c : cargas) {
            	removeCarga(c);
            }	
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    
    /**
     * busca entidade carga por exemplo
     * @param carga
     */
    public List<Carga> buscaListaDeCagas(Carga carga) throws ErroSistemicoException {
        try {
            List<Carga> listaPesquisada = super.buscarListaDeObjetos(carga);
//            super.encerrarSessao();
            return listaPesquisada;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }
    
    public List<Carga> getByOrientacaoDeEmbarque(OrientacaoDeEmbarque oE) {
        Session session = HibernateUtil.getSession();
        //Montando a consulta
        StringBuilder sSql = new StringBuilder();
        sSql.append("SELECT DISTINCT c FROM Carga c WHERE c.orientacaoDeEmbarque =  :orientacaoDeEmbarque) order by idCarga");
        
        
        //Criando a query e passando os parametros
        Query q = session.createQuery(sSql.toString());
        q.setParameter("orientacaoDeEmbarque",oE);                
        
        
        //Executando a consulta
        List<Carga> cargaLista = q.list();
        //		super.encerrarSessao();
        return cargaLista;
    }
}