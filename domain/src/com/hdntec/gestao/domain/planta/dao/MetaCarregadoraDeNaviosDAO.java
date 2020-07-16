package com.hdntec.gestao.domain.planta.dao;

import java.util.List;

import org.hibernate.HibernateException;

import com.hdntec.gestao.domain.planta.entity.MetaCarregadoraDeNavios;
import com.hdntec.gestao.domain.planta.entity.MetaMaquinaDoPatio;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;


/**
 * Classe DAO para persistir a entidade CarregadoraDeNavios
 * 
 * @author hdn.luchetta -> Rodrigo Luchetta
 * 
 */
public class MetaCarregadoraDeNaviosDAO extends AbstractGenericDAO<MetaCarregadoraDeNavios> {

    private CarregadoraDeNaviosDAO dao = new CarregadoraDeNaviosDAO();
	/**
     * Salva carregadoraNavios berco na entidade
     *
     * @param {@link MetaCarregadoraDeNavios}
     */
    public MetaCarregadoraDeNavios salvaMetaCarregadoraDeNavios(MetaCarregadoraDeNavios carregadoraNavios) throws ErroSistemicoException {
        try {
            MetaCarregadoraDeNavios carregadoraSalva = super.salvar(carregadoraNavios);
            dao.salvaCarregadoraDeNavios(carregadoraNavios.getListaStatus());
//            super.encerrarSessao();
            return carregadoraSalva;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    public List<MetaCarregadoraDeNavios> salvaMetaCarregadoraDeNavios(List<MetaCarregadoraDeNavios> carregadoraNavios) throws ErroSistemicoException {
        try {
        	List<MetaCarregadoraDeNavios> carregadoraSalva = super.salvar(carregadoraNavios);
            for (MetaCarregadoraDeNavios mMaquina : carregadoraNavios) {
             	dao.salvar(mMaquina.getListaStatus());
             }            
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
     * @param {@link MetaCarregadoraDeNavios}
     */
    public void alteraMetaCarregadoraDeNavios(MetaCarregadoraDeNavios carregadoraNavios) throws ErroSistemicoException {
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
     * @param {@link MetaCarregadoraDeNavios}
     */
    public void removeMetaCarregadoraDeNavios(MetaCarregadoraDeNavios carregadoraNavios) throws ErroSistemicoException {
        try {
            super.deletar(carregadoraNavios);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca carregadoraNavios da entidade MetaCarregadoraDeNavios por exemplo
     *
     * @param {@link MetaCarregadoraDeNavios}
     * @return link List<MetaCarregadoraDeNavios>}
     */
    public List<MetaCarregadoraDeNavios> buscaListaMetaCarregadoraDeNavios() throws ErroSistemicoException {
        try {
            List<MetaCarregadoraDeNavios> listaPesquisada = super.buscarListaDeObjetos(new MetaCarregadoraDeNavios());
//            super.encerrarSessao();
            return listaPesquisada;
        } catch (HibernateException hbex) {
            hbex.printStackTrace();
            throw new ErroSistemicoException(hbex.getMessage());
        }
    }
}
