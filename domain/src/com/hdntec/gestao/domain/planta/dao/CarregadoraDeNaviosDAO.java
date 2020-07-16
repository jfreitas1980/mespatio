package com.hdntec.gestao.domain.planta.dao;

import java.util.List;

import org.hibernate.HibernateException;

import com.hdntec.gestao.domain.planta.entity.status.CarregadoraDeNavios;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;


/**
 * Classe DAO para persistir a entidade CarregadoraDeNavios
 * 
 * @author hdn.luchetta -> Rodrigo Luchetta
 * 
 */
public class CarregadoraDeNaviosDAO extends AbstractGenericDAO<CarregadoraDeNavios> {

    /**
     * Salva carregadoraNavios berco na entidade
     *
     * @param {@link CarregadoraDeNavios}
     */
    public CarregadoraDeNavios salvaCarregadoraDeNavios(CarregadoraDeNavios carregadoraNavios) throws ErroSistemicoException {
        try {
            CarregadoraDeNavios carregadoraSalva = super.salvar(carregadoraNavios);
//            super.encerrarSessao();
            return carregadoraSalva;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Salva carregadoraNavios berco na entidade
     *
     * @param {@link CarregadoraDeNavios}
     */
    public List<CarregadoraDeNavios> salvaCarregadoraDeNavios(List<CarregadoraDeNavios> carregadoraNavios) throws ErroSistemicoException {
        try {
        	List<CarregadoraDeNavios> carregadoraSalva = super.salvar(carregadoraNavios);
//            super.encerrarSessao();
            return carregadoraSalva;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }
    
    /**
     * Altera o objeto carregadoraNavios na entidade
     *
     * @param {@link CarregadoraDeNavios}
     */
    public void alteraCarregadoraDeNavios(CarregadoraDeNavios carregadoraNavios) throws ErroSistemicoException {
        try {
            super.atualizar(carregadoraNavios);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Remove o objeto carregadoraNavios da entidade
     *
     * @param {@link CarregadoraDeNavios}
     */
    public void removeCarregadoraDeNavios(CarregadoraDeNavios carregadoraNavios) throws ErroSistemicoException {
        try {
            super.deletar(carregadoraNavios);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca carregadoraNavios da entidade CarregadoraDeNavios por exemplo
     *
     * @param {@link CarregadoraDeNavios}
     * @return link List<CarregadoraDeNavios>}
     */
    public List<CarregadoraDeNavios> buscaListaCarregadoraDeNavios() throws ErroSistemicoException {
        try {
            List<CarregadoraDeNavios> listaPesquisada = super.buscarListaDeObjetos(new CarregadoraDeNavios());
//            super.encerrarSessao();
            return listaPesquisada;
        } catch (HibernateException hbex) {
            hbex.printStackTrace();
            throw new ErroSistemicoException(hbex.getMessage());
        }
    }
}
