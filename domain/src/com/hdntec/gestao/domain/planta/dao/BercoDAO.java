package com.hdntec.gestao.domain.planta.dao;

import java.util.List;

import org.hibernate.HibernateException;

import com.hdntec.gestao.domain.planta.entity.status.Berco;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;


/**
 * Classe DAO para persistir a entidade Berco
 * 
 * @author hdn.luchetta -> Rodrigo Luchetta
 * 
 */
public class BercoDAO extends AbstractGenericDAO<Berco> {

    /**
     * Salva objeto berco na entidade
     * @param {@link Berco}
     */
    public Berco salvaBerco(Berco berco) throws ErroSistemicoException {
        try {
            Berco bercoSalvo = super.salvar(berco);
//            super.encerrarSessao();
            return bercoSalvo;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Altera o objeto berco na entidade
     * @param {@link Berco}
     */
    public void alteraBerco(Berco berco) throws ErroSistemicoException {
        try {
            super.atualizar(berco);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Remove o objeto berco da entidade
     * @param {@link Berco}
     */
    public void removeBerco(Berco berco) throws ErroSistemicoException {
        try {        	
        	
        	super.deletar(berco);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca berco da entidade Berco por exemplo
     * @param {@link Berco}
     * @return link List<Berco>}
     */
    public List<Berco> buscaPorExemploBerco(Berco berco) throws ErroSistemicoException {
        try {
            List<Berco> listaPesquisada = super.buscarListaDeObjetos(berco);
//            super.encerrarSessao();
            return listaPesquisada;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }
}
