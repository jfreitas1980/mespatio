package com.hdntec.gestao.domain.planta.dao;

import java.util.List;

import org.hibernate.HibernateException;

import com.hdntec.gestao.domain.planta.entity.MetaBerco;
import com.hdntec.gestao.domain.planta.entity.MetaCarregadoraDeNavios;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;


/**
 * Classe DAO para persistir a entidade Berco
 * 
 * @author hdn.luchetta -> Rodrigo Luchetta
 * 
 */
public class MetaBercoDAO extends AbstractGenericDAO<MetaBerco> {

	private BercoDAO dao = new BercoDAO();
    /**
     * Salva objeto berco na entidade
     * @param {@link MetaBerco}
     */
    public MetaBerco salvaMetaBerco(MetaBerco berco) throws ErroSistemicoException {
        try {
            MetaBerco bercoSalvo = super.salvar(berco);
            dao.salvar(berco.getListaStatus());
//            super.encerrarSessao();
            return bercoSalvo;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    public List<MetaBerco> salvaMetaBerco(List<MetaBerco> berco) throws ErroSistemicoException {
        try {
        	List<MetaBerco> bercoSalvo = super.salvar(berco);
            for (MetaBerco mMaquina : berco) {
             	dao.salvar(mMaquina.getListaStatus());
             }               
//            super.encerrarSessao();
            return bercoSalvo;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }
    
    /**
     * Altera o objeto berco na entidade
     * @param {@link MetaBerco}
     */
    public void alteraMetaBerco(MetaBerco berco) throws ErroSistemicoException {
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
     * @param {@link MetaBerco}
     */
    public void removeMetaBerco(MetaBerco berco) throws ErroSistemicoException {
        try {
            super.deletar(berco);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca berco da entidade MetaBerco por exemplo
     * @param {@link MetaBerco}
     * @return link List<MetaBerco>}
     */
    public List<MetaBerco> buscaPorExemploMetaBerco(MetaBerco berco) throws ErroSistemicoException {
        try {
            List<MetaBerco> listaPesquisada = super.buscarListaDeObjetos(berco);
//            super.encerrarSessao();
            return listaPesquisada;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }
}
