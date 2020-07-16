/**
 * 
 */
package com.hdntec.gestao.domain.planta.dao;

import java.util.List;

import org.hibernate.HibernateException;

import com.hdntec.gestao.domain.planta.entity.status.ManutencaoMaquina;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;


/**
 * Classe para persistir as manutencoes
 * @author guilherme
 * 
 */
public class ManutencaoMaquinaDAO extends AbstractGenericDAO<ManutencaoMaquina> {

    /**
     * Salva objeto Manutencao
     *
     * @param {@link Manutencao}
     */
    public List<ManutencaoMaquina> salvaOuAtualizarListaManutencao(List<ManutencaoMaquina> listaManutencao) throws ErroSistemicoException {
        try {
        	List<ManutencaoMaquina> listaManutencaoSalva = super.salvar(listaManutencao);
//            super.encerrarSessao();
            return listaManutencaoSalva;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Remove o objeto Manutencao
     *
     * @param {@link Manutencao}
     */
    public void removeManutencao(ManutencaoMaquina manutencao) throws ErroSistemicoException {
        try {
            super.deletar(manutencao);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca Manutencao de uma maquina por exemplo
     *
     * @param {@link Manutencao}
     * @return link List<Manutencao>}
     */
    public ManutencaoMaquina buscarManutencao(ManutencaoMaquina manutencao) throws ErroSistemicoException {
        try {
            ManutencaoMaquina manutencaoPesquisada = super.buscarPorObjeto(manutencao);
//            super.encerrarSessao();
            return manutencaoPesquisada;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca lista da entidade Manutencao
     * @return
     * @throws ErroSistemicoException
     */
    public List<ManutencaoMaquina> buscaListaDeManutencoes() throws ErroSistemicoException {
        try {
            List<ManutencaoMaquina> listaManutencao = super.buscarListaDeObjetos(new ManutencaoMaquina());
//            super.encerrarSessao();
            return listaManutencao;
        } catch (HibernateException hbex) {
            hbex.printStackTrace();
            throw new ErroSistemicoException(hbex.getMessage());
        }
    }
    

    

}
