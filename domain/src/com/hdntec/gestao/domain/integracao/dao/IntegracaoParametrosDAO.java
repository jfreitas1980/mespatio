package com.hdntec.gestao.domain.integracao.dao;

import java.util.List;

import org.hibernate.HibernateException;

import com.hdntec.gestao.domain.integracao.IntegracaoParametros;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;


/**
 * Classe DAO para a entidade forte integracaoParametros
 */
public class IntegracaoParametrosDAO extends AbstractGenericDAO<IntegracaoParametros> {

    /**
     * salva entidade IntegracaoParametros
     *
     * @param IntegracaoParametros
     */
    public void salvaIntegracaoMES(IntegracaoParametros integracaoParametros) throws ErroSistemicoException {
        super.salvar(integracaoParametros);
    }

    /**
     * salva entidade IntegracaoParametros
     *
     * @param IntegracaoParametros
     */
    public void salvaIntegracaoParametroSistema(IntegracaoParametros integracaoParametros) throws ErroSistemicoException {
        super.salvar(integracaoParametros);
    }

    /**
     * altera entidade IntegracaoParametros
     *
     * @param IntegracaoParametros
     */
    public void alteraIntegracaoMES(IntegracaoParametros integracaoParametros) throws ErroSistemicoException {
        super.atualizar(integracaoParametros);
    }

    /**
     * remove entidade IntegracaoParametros
     *
     * @param IntegracaoParametros
     */
    public void removeIntegracaoMES(IntegracaoParametros integracaoParametros) throws ErroSistemicoException {
        super.deletar(integracaoParametros);
    }

    /**
     * busca entidade IntegracaoParametros por exemplo
     *
     * @param IntegracaoParametros
     */
    public List<IntegracaoParametros> buscaPorExemploIntegracaoMES(IntegracaoParametros integracaoParametros) throws ErroSistemicoException {
        return super.buscarListaDeObjetos(integracaoParametros);
    }

    /**
     * Busca informações parametrizadas de um sistema
     */
    public IntegracaoParametros buscarParametroSistema(Long idSistema) throws ErroSistemicoException {
        IntegracaoParametros paramIntegracao = null;
        List<IntegracaoParametros> listaPesquisada = super.buscarListaDeObjetos(new IntegracaoParametros());
        for (IntegracaoParametros integracaoParametros : listaPesquisada) {
            if (integracaoParametros.getIdSistema().equals(idSistema)) {
                paramIntegracao = integracaoParametros;
                break;
            }
        }
        return paramIntegracao;
    }

    /**
     * Metodo que atualiza os parametros de tempo uma integracao
     * @param paramIntegracao
     * @throws com.hdntec.gestao.exceptions.ErroSistemicoException
     */
    public void atualizaParametroIntegracao(IntegracaoParametros paramIntegracao) throws ErroSistemicoException {
        try {
            super.atualizar(paramIntegracao);
//            super.encerrarSessao();
        } catch (HibernateException hbEx) {
            hbEx.printStackTrace();
            throw new ErroSistemicoException(hbEx.getMessage());
        }
    }


}
