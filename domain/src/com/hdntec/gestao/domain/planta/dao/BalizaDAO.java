package com.hdntec.gestao.domain.planta.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.HibernateException;

import com.hdntec.gestao.domain.planta.comparadores.ComparadorBalizas;
import com.hdntec.gestao.domain.planta.entity.status.Baliza;
import com.hdntec.gestao.domain.planta.entity.status.Pilha;
import com.hdntec.gestao.domain.produto.dao.ProdutoDAO;
import com.hdntec.gestao.domain.produto.entity.Produto;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;


/**
 * Classe DAO para persistir a entidade Baliza
 * 
 * @author hdn.luchetta -> Rodrigo Luchetta
 * 
 */
public class BalizaDAO extends AbstractGenericDAO<Baliza> {

    private ProdutoDAO produtoDAO = new ProdutoDAO();
    private PilhaDAO pilhaDAO = new PilhaDAO();
    public Baliza salvaBaliza(Baliza baliza) throws ErroSistemicoException {
         
    try {        	
        	if (baliza.getProduto() != null) {        		
        		 Produto produto = produtoDAO.salvarProduto(baliza.getProduto());
        	}	
        	//baliza.setProduto(produto);
        	List<Pilha> pilhas = new ArrayList<Pilha>();
        	if (baliza.getPilhas() != null) {            
                //pilhas.addAll(baliza.getPilhas());
        		//baliza.getPilhas().clear();
                for(Pilha p : baliza.getPilhas()) {
                	if (baliza.getProduto() == null) {
                		p.removeBaliza(baliza);                		
                		//baliza.getPilhas().remove(p);
                		baliza.setPilhas(null);
                	}
                	if (p.getListaDeBalizas() == null || p.getListaDeBalizas().size() == 0) {
                		
                		p.setListaDeBalizas(null);   
                		try {                			
                			pilhaDAO.removePilha(p);
                		} catch (Exception e) {
							// TODO: handle exception
                			System.out.println("Removendo pilha !!");
						}	
                	} else {
                		pilhas.add(pilhaDAO.salvar(p));
                	}
                }        		
                //baliza.setPilhas(new ArrayList<Pilha>());               
                //baliza.setPilhas(pilhas);
            }
        	Baliza balizaSalva = super.salvar(baliza);            	
//            super.encerrarSessao();
            return balizaSalva;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Salva objeto baliza na entidade
     * @param {@link Baliza}
     */
    public void salvaListaBalizas(List<Baliza> listaBaliza) throws ErroSistemicoException {
        try {
            for (Baliza b : listaBaliza) {
              salvaBaliza(b);
            }
        	
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Altera o objeto baliza na entidade
     * @param {@link Baliza}
     */
    public void alteraBaliza(Baliza baliza) throws ErroSistemicoException {
        try {
            super.atualizar(baliza);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Remove o objeto baliza da entidade
     * @param {@link Baliza}
     */
    public void removeBaliza(Baliza baliza) throws ErroSistemicoException {
        try {
       	List<Pilha> pilhas = new ArrayList<Pilha>();
       	if (baliza.getPilhas() != null) {            
               //pilhas.addAll(baliza.getPilhas());
       		//baliza.getPilhas().clear();
               for(Pilha p : baliza.getPilhas()) {
               		p.removeBaliza(baliza);
               		baliza.setPilhas(null);
                
               	if (p.getListaDeBalizas() == null || p.getListaDeBalizas().size() == 0) {
               		   pilhaDAO.removePilha(p);
               	} else {
               		pilhas.add(pilhaDAO.salvar(p));
               	}
               }
           }
       		
        	super.deletar(baliza);
        	
        	if (baliza.getProduto() != null) {        		
        		   produtoDAO.removerProduto(baliza.getProduto());
          	}	
        	
        	
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca baliza da entidade Baliza por exemplo
     * @param {@link Baliza}
     * @return link List<Baliza>}
     */
    public List<Baliza> buscaPorExemploBaliza(Baliza baliza) throws ErroSistemicoException {
        try {
            List<Baliza> listaBalizas = super.buscarListaDeObjetos(baliza);
            Collections.sort(listaBalizas, new ComparadorBalizas());
            return listaBalizas;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }
}
