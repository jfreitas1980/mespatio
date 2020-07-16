package com.hdntec.gestao.integracao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import com.hdntec.gestao.domain.integracao.IntegracaoMES;
import com.hdntec.gestao.domain.integracao.IntegracaoRPUSINAS;
import com.hdntec.gestao.domain.navios.entity.MetaNavio;
import com.hdntec.gestao.domain.plano.entity.PlanoEmpilhamentoRecuperacao;
import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.exceptions.IntegracaoNaoRealizadaException;

/**
 * Interface de acesso às operações do subsistema de integração com sistemas externos
 * <p>
 * Requisitos especiais: - Distribuída
 *
 * @author andre
 *
 */
@Remote
public interface IControladorIntegracaoSistemasExternos {

    /**
     * Chavear a atualização automática de dados dos sistemas externos. Caso esteja ativada, é desativada, e vice-versa.
     *
     * @param atualizacaoAtivada o status da atualização
     */
    public void chavearAtualizacaoAutomatica(Boolean atualizacaoAtivada);

    /**
     * Atualiza o tempo da última atualização de dados de sistemas externos.
     * Esse método deve atualizar o atributo ultimaAtualizacao.
     * Deve-se tomar cuidado para que o valor atualizado represente o momento exato da última atualização,
     * para não correr o risco de perder dados na próxima leitura.
     */
    public void atualizarTempoUltimaAtualizacao();

    /**
     * Envia mensagem para os clientes, no tópico de erro de leitura de sistemas externos.
     */
    public void enviaMensagemClienteErroDeLeituraDeSistemasExternos();

    /**
     * Ativa a atualização automática.
     */
    public void ativarAtualizacaoAutomatica();

    /**
     * Desativa a atualização automática
     */
    public void desativarAtualizacaoAutomatica();

    /**
     * Retorna o status da atualizacao automatica
     */
    public Boolean getAtualizacaoAtivada();


    /**
     * Executa a atualizacao dos dados dos sistemas externos CRM
     */
    public PlanoEmpilhamentoRecuperacao atualizarDadosCRM(Date dataExecucaoAtualizacao, PlanoEmpilhamentoRecuperacao planoEmpilhamentoRecuperacao,Date dataSitucao) throws IntegracaoNaoRealizadaException, ErroSistemicoException;

    /**
     * Executa a atualizacao dos dados dos sistemas externos MES
     */
    public PlanoEmpilhamentoRecuperacao atualizarDadosMES(Date dataExecucaoAtualizacao, PlanoEmpilhamentoRecuperacao planoEmpilhamentoRecuperacao,Map<String,List<IntegracaoMES>> map) throws IntegracaoNaoRealizadaException, ErroSistemicoException;

    /**
     * Executa a atualizacao dos dados dos sistemas externos PIMS
     */
    public PlanoEmpilhamentoRecuperacao atualizarDadosPIMS(Date dataExecucaoAtualizacao, PlanoEmpilhamentoRecuperacao planoEmpilhamentoRecuperacao,Map<String,List<IntegracaoMES>> map) throws IntegracaoNaoRealizadaException, ErroSistemicoException;


    /**
     * Metodo que busca na tabela de ritimo de producao um novo valor lido do mes para que a taxa
     * de operacao da usina seja atualizada para novas atividades
     * @param dataHoraSituacao
     * @return List<IntegracaoRPUsinas>
     * @throws ErroSistemicoException
     */
    public List<IntegracaoRPUSINAS> buscarNovoRitimoProducaoUsinas(Date dataHoraSituacao) throws ErroSistemicoException;
    /**
     * Executa a atualizacao dos dados da usina no sistemas externos CRM
     */
    public List<IntegracaoMES> atualizarDadosUsina(IntegracaoMES integracaoMES, Date dataLeituraInicial ) throws IntegracaoNaoRealizadaException, ErroSistemicoException;

    void atualizarDadosNavioCRM(MetaNavio metaNavio, Date dataSituacao) throws IntegracaoNaoRealizadaException,
                    ErroSistemicoException;


}