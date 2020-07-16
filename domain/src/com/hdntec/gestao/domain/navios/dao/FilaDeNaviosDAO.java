/**
 *
 */
package com.hdntec.gestao.domain.navios.dao;

import java.util.List;

import org.hibernate.HibernateException;

import com.hdntec.gestao.domain.navios.entity.status.FilaDeNavios;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;


/**
 * Classe DAO para persistir a entidade FilaDeNavios
 *
 * @author hdn.luchetta -> Rodrigo Luchetta
 *
 */
public class FilaDeNaviosDAO extends AbstractGenericDAO<FilaDeNavios> {

    /**
     * Salva objeto fileDeNavios na entidade
     * @param {@link FilaDeNavios}
     */
    public FilaDeNavios salvaFilaDeNavios(FilaDeNavios filaDeNavios) throws ErroSistemicoException {
        try {
            FilaDeNavios filaSalva = super.salvar(filaDeNavios);
//            super.encerrarSessao();
            return filaSalva;
        } catch (HibernateException hbex) {
            hbex.printStackTrace();
            throw new ErroSistemicoException(hbex.getMessage());
        }
    }

    /**
     * Altera o objeto filaDeNavios na entidade
     * @param {@link FilaDeNavios}
     */
    public void alteraFilaDeNavios(FilaDeNavios filaDeNavios) throws ErroSistemicoException {
        try {
            super.atualizar(filaDeNavios);
//            super.encerrarSessao();
        } catch (HibernateException hbex) {
            hbex.printStackTrace();
            throw new ErroSistemicoException(hbex.getMessage());
        }
    }

    /**
     * Remove o objeto filaDeNavios da entidade
     * @param {@link FilaDeNavios}
     */
    public void removeFilaDeNavios(FilaDeNavios filaDeNavios) throws ErroSistemicoException {
        try {
            super.deletar(filaDeNavios);
//            super.encerrarSessao();
        } catch (HibernateException hbex) {
            hbex.printStackTrace();
            throw new ErroSistemicoException(hbex.getMessage());
        }
    }

    /**
     * Busca filaDeNavios da entidade FilaDeNavios por exemplo
     *
     * @param {@link FilaDeNavios}
     * @return link List<FilaDeNavios>}
     */
    public List<FilaDeNavios> buscaPorExemploFilaDeNavios(FilaDeNavios filaDeNavios) throws ErroSistemicoException {
        try {
            List<FilaDeNavios> listaPesquisada = super.buscarListaDeObjetos(filaDeNavios);
//            super.encerrarSessao();
            return listaPesquisada;
        } catch (HibernateException hbex) {
            hbex.printStackTrace();
            throw new ErroSistemicoException(hbex.getMessage());
        }
    }

    /**
     * Busca filaDeNavios da entidade FilaDeNavios por exemplo
     *
     * @param {@link FilaDeNavios}
     * @return link FilaDeNavios
     */
    public FilaDeNavios buscarFilaDeNavios(FilaDeNavios filaDeNavios) throws ErroSistemicoException {
        try {
            FilaDeNavios filaPesquisada = super.buscarPorObjeto(filaDeNavios);
//            super.encerrarSessao();
            return filaPesquisada;
        } catch (HibernateException hbex) {
            hbex.printStackTrace();
            throw new ErroSistemicoException(hbex.getMessage());
        }
    }

}
