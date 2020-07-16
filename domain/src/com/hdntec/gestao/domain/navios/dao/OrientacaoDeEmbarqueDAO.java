package com.hdntec.gestao.domain.navios.dao;

import java.util.List;

import org.hibernate.HibernateException;

import com.hdntec.gestao.domain.navios.entity.status.OrientacaoDeEmbarque;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;


/**
 * Classe DAO para persistir a entidade OrientacaoDeEmbarque
 * 
 * @author hdn.luchetta -> Rodrigo Luchetta
 * 
 */
public class OrientacaoDeEmbarqueDAO extends AbstractGenericDAO<OrientacaoDeEmbarque> {

    /**
     * Salva objeto orientacaoEmbarque na entidade
     * @param {@link OrientacaoDeEmbarque}
     */
    public OrientacaoDeEmbarque salvaOrientacaoDeEmbarque(OrientacaoDeEmbarque orientacaoEmbarque) throws ErroSistemicoException {
        try {
            OrientacaoDeEmbarque oeSalva = super.salvar(orientacaoEmbarque);
//            super.encerrarSessao();
            return oeSalva;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Altera o objeto orientacaoEmbarque na entidade
     * @param {@link OrientacaoDeEmbarque}
     */
    public void alteraOrientacaoDeEmbarque(OrientacaoDeEmbarque orientacaoEmbarque) throws ErroSistemicoException {
        try {
            super.atualizar(orientacaoEmbarque);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Remove o objeto orientacaoEmbarque da entidade
     * @param {@link OrientacaoDeEmbarque}
     */
    public void removeOrientacaoDeEmbarque(OrientacaoDeEmbarque orientacaoEmbarque) throws ErroSistemicoException {
        try {
            super.deletar(orientacaoEmbarque);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca orientacaoEmbarque da entidade OrientacaoDeEmbarque por exemplo
     * @param {@link OrientacaoDeEmbarque}
     * @return link List<OrientacaoDeEmbarque>}
     */
    public List<OrientacaoDeEmbarque> buscaPorExemploOrientacaoDeEmbarque(OrientacaoDeEmbarque orientacaoEmbarque) throws ErroSistemicoException {
        try {
            List<OrientacaoDeEmbarque> listaPesquisada = super.buscarListaDeObjetos(orientacaoEmbarque);
            acessaListasOrientacaoEmbarque(listaPesquisada);
//            super.encerrarSessao();
            return listaPesquisada;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

     /**
     * Metodo auxiliar que acessa as listas do objeto OrientacaoEmbarque e suas sublistas
     * para evitar EAGERInicializationException na interface
     * @param filaDeNavios
     */
    private void acessaListasOrientacaoEmbarque(List<OrientacaoDeEmbarque> listaOE) {
        for (OrientacaoDeEmbarque oe : listaOE) {
            oe.getTipoProduto();
            oe.getListaItemDeControle().size();
        }
    }

}
