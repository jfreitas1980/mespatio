
package com.hdntec.gestao.integracao.integracaoCRM;

import java.util.Date;

import javax.ejb.Local;

import com.hdntec.gestao.domain.navios.entity.MetaNavio;
import com.hdntec.gestao.domain.plano.entity.PlanoEmpilhamentoRecuperacao;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.IntegracaoNaoRealizadaException;




/**
 * Acesso às operações do subsistema de integração com o sistema externo CRM.
 * 
 * @author andre
 * 
 */
@Local
public interface IControladorCRM {

    /**
     * Atualiza os dados do sistema externo CRM, a partir da ultimaAtualizacao.
     *
     * @return a lista de atividades que aconteceram desde a última atualização
     */
    public PlanoEmpilhamentoRecuperacao atualizarDados(Date dataExecucaoAtualizacao, PlanoEmpilhamentoRecuperacao planoEmpilhamentoRecuperacao,Date dataSitucao) throws IntegracaoNaoRealizadaException, ErroSistemicoException;
    
    void atualizarDados(MetaNavio metaNavio, long timeDefault) throws IntegracaoNaoRealizadaException,
                    ErroSistemicoException;
}