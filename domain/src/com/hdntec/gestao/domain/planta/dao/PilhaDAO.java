/**
 * 
 */
package com.hdntec.gestao.domain.planta.dao;

import java.util.List;

import org.hibernate.HibernateException;

import com.hdntec.gestao.domain.planta.entity.status.Pilha;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;


/**
 * Classe DAO para persistir a entidade Pilha
 * 
 * @author hdn.luchetta -> Rodrigo Luchetta
 * 
 */
public class PilhaDAO extends AbstractGenericDAO<Pilha> {

    /**
     * Salva objeto pilha na entidade
     * @param {@link Pilha}
     */
    public Pilha salvaPilha(Pilha pilha) throws ErroSistemicoException {
        try {
            Pilha pilhaSalva = super.salvar(pilha);
//            super.encerrarSessao();
            return pilhaSalva;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Altera o objeto pilha na entidade
     * @param {@link Pilha}
     */
    public void alteraPilha(Pilha pilha) throws ErroSistemicoException {
        try {
            super.atualizar(pilha);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Remove o objeto pilha da entidade
     * @param {@link Pilha}
     */
    public void removePilha(Pilha pilha) throws ErroSistemicoException {
        try {
            super.deletar(pilha);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

      
    
    /**
     * Busca baliza da entidade Pilha por exemplo
     * @param {@link Pilha}
     * @return link List<Pilha>}
     */
    public List<Pilha> buscaPorExemploPilha(Pilha pilha) throws ErroSistemicoException {
        try {
            List<Pilha> listaPesquisada = super.buscarListaDeObjetos(pilha);
//            super.encerrarSessao();
            return listaPesquisada;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }
}
