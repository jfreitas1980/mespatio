/**
 * 
 */
package com.hdntec.gestao.domain.planta.dao;

import java.util.List;

import org.hibernate.HibernateException;

import com.hdntec.gestao.domain.planta.entity.status.Manutencao;
import com.hdntec.gestao.domain.planta.entity.status.ManutencaoCorreia;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;


/**
 * Classe para persistir as manutencoes
 * @author guilherme
 * 
 */
public class ManutencaoCorreiaDAO extends AbstractGenericDAO<ManutencaoCorreia> {

    /**
     * Salva objeto Manutencao
     *
     * @param {@link Manutencao}
     */
    public List<ManutencaoCorreia> salvaOuAtualizarListaManutencaoCorreia(List<ManutencaoCorreia> listaManutencaoCorreia) throws ErroSistemicoException {
        try {
        	List<ManutencaoCorreia> listaManutencaoCorreiaSalva = super.salvar(listaManutencaoCorreia);
//            super.encerrarSessao();
            return listaManutencaoCorreiaSalva;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Remove o objeto ManutencaoCorreia
     *
     * @param {@link ManutencaoCorreia}
     */
    public void removeManutencaoCorreia(ManutencaoCorreia ManutencaoCorreia) throws ErroSistemicoException {
        try {
            super.deletar(ManutencaoCorreia);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca ManutencaoCorreia de uma maquina por exemplo
     *
     * @param {@link ManutencaoCorreia}
     * @return link List<ManutencaoCorreia>}
     */
    public ManutencaoCorreia buscarManutencaoCorreia(ManutencaoCorreia manutencao) throws ErroSistemicoException {
        try {
            ManutencaoCorreia ManutencaoCorreiaPesquisada = super.buscarPorObjeto(manutencao);
//            super.encerrarSessao();
            return ManutencaoCorreiaPesquisada;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca lista da entidade ManutencaoCorreia
     * @return
     * @throws ErroSistemicoException
     */
    public List<ManutencaoCorreia> buscaListaDeManutencoes() throws ErroSistemicoException {
        try {
            List<ManutencaoCorreia> listaManutencaoCorreia = super.buscarListaDeObjetos(new ManutencaoCorreia());
//            super.encerrarSessao();
            return listaManutencaoCorreia;
        } catch (HibernateException hbex) {
            hbex.printStackTrace();
            throw new ErroSistemicoException(hbex.getMessage());
        }
    }
    

    

}
