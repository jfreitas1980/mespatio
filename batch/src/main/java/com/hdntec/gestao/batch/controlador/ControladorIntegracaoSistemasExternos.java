package com.hdntec.gestao.batch.controlador;

import java.util.Date;

import com.hdntec.gestao.batch.integracaoCRM.IntegracaoSistemaCRM;
import com.hdntec.gestao.batch.integracaoMES.IntegracaoSistemaMES;
import com.hdntec.gestao.batch.integracaoRPUSINAS.IntegracaoSistemaRPUSINAS;
import com.hdntec.gestao.batch.integracaoSAP.IntegracaoSistemaSAP;
import com.hdntec.gestao.batch.utilitarios.arquivo.FuncoesArquivo;
import com.hdntec.gestao.exceptions.IntegracaoNaoRealizadaException;


public class ControladorIntegracaoSistemasExternos {
    
        
    /** Identificador do sistema MES */
    public static final Integer SISTEMA_MES_USINAS = new Integer(FuncoesArquivo.lePropriedade("codigo.sistema.externo.MES_USINAS"));
    
    /** Identificador do sistema MES */
    public static final Integer SISTEMA_MES_FILTRAGEM = new Integer(FuncoesArquivo.lePropriedade("codigo.sistema.externo.MES_FILTRAGEM"));
    
    /** Identificador do sistema MES */
    public static final Integer SISTEMA_MES_FILTRAGEM_QUIMICO = new Integer(FuncoesArquivo.lePropriedade("codigo.sistema.externo.MES_FILTRAGEM_QUIMICO"));
    
    /** Identificador do sistema MES */
    public static final Integer SISTEMA_MES_FILTRAGEM_FISICO = new Integer(FuncoesArquivo.lePropriedade("codigo.sistema.externo.MES_FILTRAGEM_FISICO"));
    
    /** Identificador do sistema CRM */
    public static final Integer SISTEMA_CRM = new Integer(FuncoesArquivo.lePropriedade("codigo.sistema.externo.CRM"));

    /** Identificador do sistema SAP */
    public static final Integer SISTEMA_SAP = new Integer(FuncoesArquivo.lePropriedade("codigo.sistema.externo.SAP"));

    
    /**
     * Executa a pesquisa no sistema MES e realiza a importacao 
     * para o banco de dados do MESPATIO
     */
    public void executaIntegracaoSistemaMES(Date dataInicioExecucao) throws IntegracaoNaoRealizadaException {
        IntegracaoSistemaMES integraMES = new IntegracaoSistemaMES();
        integraMES.inicializaIntegracaoMES(dataInicioExecucao);
        integraMES.fechaConexao();
    }

    /**
     * Executa a pesquisa no sistema CRM e realiza a importacao
     * para o banco de dados do MESPATIO
     */
    public void executaIntegracaoSistemaCRM(Date dataInicioExecucao) throws IntegracaoNaoRealizadaException {
        IntegracaoSistemaCRM integraCRM = new IntegracaoSistemaCRM();
        try {
            integraCRM.inicializaIntegracaoCRM(dataInicioExecucao);
            integraCRM.fechaConexao();
        } catch (Exception ex) {
            throw new IntegracaoNaoRealizadaException(ex);
	  }
    }

    /**
     * Executa a pesquisa no sistema CRM e realiza a importacao
     * para o banco de dados do MESPATIO
     */
    public void executaIntegracaoSistemaSAP(Date dataInicioExecucao) throws IntegracaoNaoRealizadaException {
        IntegracaoSistemaSAP integraSAP = new IntegracaoSistemaSAP();
        integraSAP.inicializaIntegracaoSAP(dataInicioExecucao);
        integraSAP.fechaConexao();
    }

   /* *//**
     * Executa a pesquisa no sistema CRM e realiza a importacao
     * para o banco de dados do MESPATIO
     *//*
    public void executaIntegracaoSistemaPIMS(Date dataInicioExecucao) throws IntegracaoNaoRealizadaException {
        IntegracaoSistemaPIMS integraPIMS = new IntegracaoSistemaPIMS();
        integraPIMS.inicializaIntegracaoPIMS(dataInicioExecucao);
        integraPIMS.fechaConexao();
    }*/

    /**
     * Executa a pesquisa no sistema MES para o ritimo de producao das usinas e realiza a importacao
     * para o banco de dados do MESPATIO
     */
    public void executaIntegracaoSistemaRPUSINAS(Date dataInicioExecucao) throws IntegracaoNaoRealizadaException {
        IntegracaoSistemaRPUSINAS integraRPUSINAS = new IntegracaoSistemaRPUSINAS();
        integraRPUSINAS.inicializaIntegracaoRPUsinas(dataInicioExecucao);
        integraRPUSINAS.fechaConexao();
    }


}
