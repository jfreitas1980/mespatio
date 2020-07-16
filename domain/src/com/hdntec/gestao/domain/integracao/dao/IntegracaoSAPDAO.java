package com.hdntec.gestao.domain.integracao.dao;

import java.util.Date;
import java.util.List;

import com.hdntec.gestao.domain.integracao.IntegracaoSAP;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.hibernate.AbstractGenericDAO;


/**
 * Classe DAO para a entidade forte IntegracaoSAP
 */
public class IntegracaoSAPDAO extends AbstractGenericDAO<IntegracaoSAP> {

    /**
     * salva entidade IntegracaoSAP
     *
     * @param IntegracaoSAP
     */
    public void salvaIntegracaoSAP(IntegracaoSAP integracaoSAP) throws ErroSistemicoException {
        super.salvar(integracaoSAP);
    }

    /**
     * altera entidade IntegracaoSAP
     *
     * @param IntegracaoSAP
     */
    public void alteraIntegracaoSAP(IntegracaoSAP integracaoSAP) throws ErroSistemicoException {
        super.atualizar(integracaoSAP);
    }

    /**
     * remove entidade IntegracaoSAP
     *
     * @param IntegracaoSAP
     */
    public void removeIntegracaoSAP(IntegracaoSAP integracaoSAP) throws ErroSistemicoException {
        super.deletar(integracaoSAP);
    }

    /**
     * busca entidade carga por exemplo
     *
     * @param IntegracaoSAP
     */
    public List<IntegracaoSAP> buscaPorExemploIntegracaoSAP(IntegracaoSAP integracaoSAP) throws ErroSistemicoException {
        return super.buscarListaDeObjetos(integracaoSAP);
    }

    /**
     * Metodo que busca os dados lidos do mes
     * @param IntegracaoSAP
     * @param dataLeituraInicial
     * @param dataLeituraFinal
     * @return
     * @throws com.hdntec.gestao.exceptions.ErroSistemicoException
     */
    public List<IntegracaoSAP> buscarDadosLidosPorData(IntegracaoSAP integracaoSAP, Date dataLeituraInicial, Date dataLeituraFinal) throws ErroSistemicoException {
        return super.buscarListaDeObjetosPorDatas(integracaoSAP, "dataLeitura", dataLeituraInicial, dataLeituraFinal);
    }
}
