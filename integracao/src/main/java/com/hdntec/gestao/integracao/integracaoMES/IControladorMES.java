package com.hdntec.gestao.integracao.integracaoMES;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import com.hdntec.gestao.domain.integracao.IntegracaoMES;
import com.hdntec.gestao.domain.plano.entity.PlanoEmpilhamentoRecuperacao;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.IntegracaoNaoRealizadaException;



/**
 * Interface de acesso às operações do subsistema de integração com o sistema externo MES.
 * 
 * @author andre
 * 
 */
@Local
public interface IControladorMES {

    /**
     * Atualiza os dados do sistema externo MES, a partir da ultimaAtualizacao.
     *
     * @return a lista de atividades que aconteceram desde a última atualização
     */
    public PlanoEmpilhamentoRecuperacao atualizarDados(Date dataExecucaoAtualizacao, PlanoEmpilhamentoRecuperacao planoEmpilhamentoRecuperacao,Map<String,List<IntegracaoMES>> map) throws IntegracaoNaoRealizadaException, ErroSistemicoException;
    public List<IntegracaoMES> atualizarDadosUsina(IntegracaoMES integracaoMES, Date dataLeituraInicial) throws ErroSistemicoException;
}