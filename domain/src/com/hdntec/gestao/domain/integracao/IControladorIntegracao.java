package com.hdntec.gestao.domain.integracao;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import com.hdntec.gestao.exceptions.ErroSistemicoException;


/**
 * Inteface que gerencia os metodos que realizara a integracao 
 * com os sistemas externos
 * 
 * @author Rodrigo Luchetta
 */
@Local
public interface IControladorIntegracao {

    /** 
     * Metodo que busca uma lista com os dados a serem integrados utilizando o obejto
     * integracaoMES como filtro.
     * 
     * @param integracaoMES
     * @return List<IntegracaoMES> - lista com os dados a serem integrados
     */
    public List<IntegracaoMES> obterDadosIntegracao(IntegracaoMES integracaoMES) throws ErroSistemicoException;

    /**
     * Metodo que atualizará a flag processado dos registros integrados.
     * @param listaDadosIntegrados
     */
    public void atualizarDadosIntegrados(List<IntegracaoMES> listaDadosIntegrados) throws ErroSistemicoException;

    /**
     * Metodo que busca os dados lidos do mes
     * @param integracaoMES
     * @param dataLeituraInicial
     * @param dataLeituraFinal
     * @return
     * @throws com.hdntec.gestao.exceptions.ErroSistemicoException
     */
    public List<IntegracaoMES> buscarDadosLidosPorData(IntegracaoMES integracaoMES, Date dataLeituraInicial, Date dataLeituraFinal) throws ErroSistemicoException;

    public Date buscarUltimaDataAntesDaDataInicial(IntegracaoMES integracaoMES, Date dataLeituraInicial) throws ErroSistemicoException;
    
    /**
     * Busca as informações parametrizadas por sistema
     * @param idSistema
     * @return
     * @throws com.hdntec.gestao.exceptions.ErroSistemicoException
     */
    public IntegracaoParametros buscarParametroSistema(Long idSistema) throws ErroSistemicoException;


    /**
     * Metodo que atualiza os parametros de um determinado sistema
     * @param paramIntegracao
     * @throws com.hdntec.gestao.exceptions.ErroSistemicoException
     */
    public void atualizarParametroSistema(IntegracaoParametros paramIntegracao) throws ErroSistemicoException;

    /**
     * Metodo que salva um objeto integracaoParametro na tabela com mesmo nome
     * @param integracaoParametros
     * @throws com.hdntec.gestao.exceptions.ErroSistemicoException
     */
    public void salvarParametroSistema(IntegracaoParametros integracaoParametros) throws ErroSistemicoException;

    /**
     * Metodo que busca nos dados importados os navios que ainda não foram carregados para a fila de navios
     * @param dataLeituraEmbarqueInicial
     * @param dataLeituraEmbarqueFinal
     * @return List<IntegracaoNavioCRM>
     * @throws com.hdntec.gestao.exceptions.ErroSistemicoException
     */
    public List<IntegracaoNavioCRM> buscarNovosNaviosParaFila(Date dataLeituraEmbarqueInicial, Date dataLeituraEmbarqueFinal) throws ErroSistemicoException;

    /**
     * Metodo que atualizará a flag processado dos registros integrados do CRM.
     * @param listaDadosIntegrados
     */
    public void atualizarNaviosIntegrados(List<IntegracaoNavioCRM> listaNaviosIntegrados) throws ErroSistemicoException;

    /**
     * Metodo que busca todos os totalizadores lidos do SAP a partir da data da ultima atualizacao
     * @param dataUltimaIntegracao
     * @return
     */
    public List<IntegracaoSAP> buscarListaDadosTotalizadosSAP(Date dataInicial, Date dataFinal) throws ErroSistemicoException;
    
    
    /**
     * Metodo que busca na tabela de ritimo de producao um novo valor lido do mes para que a taxa
     * de operacao da usina seja atualizada para novas atividades
     * @param dataHoraSituacao
     * @return List<IntegracaoRPUsinas>
     * @throws ErroSistemicoException
     */
    public List<IntegracaoRPUSINAS> buscarNovoRitimoProducaoUsinas(Date dataHoraSituacao) throws ErroSistemicoException;
 
    /**
     * Metodo que busca os dados lidos do mes
     * @param integracaoMES
     * @param dataLeituraFinal
     * @return
     * @throws com.hdntec.gestao.exceptions.ErroSistemicoException
     */
    public List<IntegracaoMES> buscarDadosLidosPorData(IntegracaoMES integracaoMES, Date dataLeituraFinal) throws ErroSistemicoException;

    List<IntegracaoNavioCRM> buscarAtualizacaoNavio(String chaveSAP) throws ErroSistemicoException;
}