/**
 * 
 */
package com.hdntec.gestao.domain.planta.dao;

import java.util.List;

import org.hibernate.HibernateException;

import com.hdntec.gestao.domain.navios.dao.NavioDAO;
import com.hdntec.gestao.domain.navios.entity.MetaNavio;
import com.hdntec.gestao.domain.navios.entity.status.Navio;
import com.hdntec.gestao.domain.plano.entity.MovimentacaoNavio;
import com.hdntec.gestao.domain.planta.entity.MetaBerco;
import com.hdntec.gestao.domain.planta.entity.status.Berco;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;


/**
 * Classe para persistir as MovimentacaoNavio
 * @author guilherme
 * 
 */
public class MovimentacaoNavioDAO extends AbstractGenericDAO<MovimentacaoNavio> {

    private NavioDAO navioDAO = new NavioDAO();
    private BercoDAO bercoDAO = new BercoDAO();
	/**
     * Salva objeto MovimentacaoNavio
     *
     * @param {@link MovimentacaoNavio}
     */
    public List<MovimentacaoNavio> salvaOuAtualizarListaMovimentacaoNavio(List<MovimentacaoNavio> listaMovimentacaoNavio) throws ErroSistemicoException {
        try {
        	List<MovimentacaoNavio> listaMovimentacaoNavioSalva = super.salvar(listaMovimentacaoNavio);
//            super.encerrarSessao();
            return listaMovimentacaoNavioSalva;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    public MovimentacaoNavio salvaMovimentacaoNavio(MovimentacaoNavio movimentacaoNavio) throws ErroSistemicoException {
        try {
        	if (movimentacaoNavio.getBercoDestino() != null) {
        		bercoDAO.salvaBerco(movimentacaoNavio.getBercoDestino());
        	}	
        	if (movimentacaoNavio.getNavio() != null) {
        		navioDAO.salvaNavio(movimentacaoNavio.getNavio());
        	}	
        	MovimentacaoNavio movimentacaoNavioSalva = super.salvar(movimentacaoNavio);        	
//            super.encerrarSessao();
            return movimentacaoNavioSalva;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }
    
    /**
     * Remove o objeto MovimentacaoNavio
     *
     * @param {@link MovimentacaoNavio}
     */
    public void removeMovimentacaoNavio(MovimentacaoNavio movimentacaoNavio) throws ErroSistemicoException {
        try {
        	Berco berco = null;
        	Navio navio = null;
        	super.deletar(movimentacaoNavio);
        	
        	if (movimentacaoNavio.getBercoDestino() != null) {        		
        		berco = movimentacaoNavio.getBercoDestino();
        		berco.setNavioAtracado(null);        		        		
        	}	
        	if (movimentacaoNavio.getNavio() != null) {
        		navio = movimentacaoNavio.getNavio();
        		navio.setBercoDeAtracacao(null);        		
        	}
        	if (navio != null) {        		    		
    			MetaNavio meta = navio.getMetaNavio();
    			navioDAO.removeNavio(navio);	        	    
    			meta.removerStatus(navio);
    		}
        	        	
        	if (berco != null) {
        		MetaBerco meta = berco.getMetaBerco();    				        	        			
        		bercoDAO.removeBerco(berco);	
        		meta.removerStatus(berco);
        	}
        	
        	
        	
        	
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca MovimentacaoNavio de uma maquina por exemplo
     *
     * @param {@link MovimentacaoNavio}
     * @return link List<MovimentacaoNavio>}
     */
    public MovimentacaoNavio buscarMovimentacaoNavio(MovimentacaoNavio MovimentacaoNavio) throws ErroSistemicoException {
        try {
            MovimentacaoNavio MovimentacaoNavioPesquisada = super.buscarPorObjeto(MovimentacaoNavio);
//            super.encerrarSessao();
            return MovimentacaoNavioPesquisada;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca lista da entidade MovimentacaoNavio
     * @return
     * @throws ErroSistemicoException
     */
    public List<MovimentacaoNavio> buscaListaDeManutencoes() throws ErroSistemicoException {
        try {
            List<MovimentacaoNavio> listaMovimentacaoNavio = super.buscarListaDeObjetos(new MovimentacaoNavio());
//            super.encerrarSessao();
            return listaMovimentacaoNavio;
        } catch (HibernateException hbex) {
            hbex.printStackTrace();
            throw new ErroSistemicoException(hbex.getMessage());
        }
    }
    

    

}
