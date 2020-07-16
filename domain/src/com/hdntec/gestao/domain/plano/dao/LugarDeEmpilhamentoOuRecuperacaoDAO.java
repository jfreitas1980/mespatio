/**
 * 
 */
package com.hdntec.gestao.domain.plano.dao;

import java.util.List;

import org.hibernate.HibernateException;

import com.hdntec.gestao.domain.plano.entity.LugarEmpilhamentoRecuperacao;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;


/**
 * Classe DAO para persistir a entidade LugarDeEmpilhamentoOuRecuperacao
 * @author andre
 *
 */
public class LugarDeEmpilhamentoOuRecuperacaoDAO extends AbstractGenericDAO<LugarEmpilhamentoRecuperacao> {

    /**
     * Salva objeto LugarDeEmpilhamentoOuRecuperacao na entidade
     *
     * @param {@link LugarDeEmpilhamentoOuRecuperacao}
     */
    public LugarEmpilhamentoRecuperacao salvaLugarEmpRec(LugarEmpilhamentoRecuperacao lugarDeEmpilhamentoOuRecuperacao) throws ErroSistemicoException {
        try {
            LugarEmpilhamentoRecuperacao locEmpRecSalva = super.salvar(lugarDeEmpilhamentoOuRecuperacao);
//            super.encerrarSessao();
            return locEmpRecSalva;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Altera o objeto lugarDeEmpilhamentoOuRecuperacao na entidade
     * @param {@link LugarDeEmpilhamentoOuRecuperacao}
     */
    public void alteraLugarEmpilhamentoRecuperacao(LugarEmpilhamentoRecuperacao lugarDeEmpilhamentoOuRecuperacao) throws ErroSistemicoException {
        try {
            super.atualizar(lugarDeEmpilhamentoOuRecuperacao);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Remove o objeto pilhaVirtual da entidade
     * @param {@link LugarDeEmpilhamentoOuRecuperacao}
     */
    public void removeLugarEmpilhamentoRecuperacao(LugarEmpilhamentoRecuperacao lugarDeEmpilhamentoOuRecuperacao) throws ErroSistemicoException {
        try {
            super.deletar(lugarDeEmpilhamentoOuRecuperacao);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }

    /**
     * Busca baliza da entidade LugarDeEmpilhamentoOuRecuperacao por exemplo
     * @param {@link LugarDeEmpilhamentoOuRecuperacao}
     * @return link List<LugarDeEmpilhamentoOuRecuperacao>}
     */
    public List<LugarEmpilhamentoRecuperacao> buscaPorExemploLugarEmpilhamentoRecuperacao(LugarEmpilhamentoRecuperacao lugarDeEmpilhamentoOuRecuperacao) throws ErroSistemicoException {
        try {
            List<LugarEmpilhamentoRecuperacao> listaPesquisada = super.buscarListaDeObjetos(lugarDeEmpilhamentoOuRecuperacao);
//            super.encerrarSessao();
            return listaPesquisada;
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }
}
