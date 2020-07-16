/*
 *  ConexaoPostgres.java
 *  Autor: Rodrigo Luchetta 
 *  Data: 22/05/2005
 *  Versao: 1.0.0
 */

package com.hdntec.gestao.batch.conexaoJDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.hdntec.gestao.batch.utilitarios.arquivo.FuncoesArquivo;


public class ConexaoOracle {

    public static final String BASE_UHU = "UBU";

    public static final String BASE_BH = "BH";

    public static Connection conecta(String usuario, String senha) throws ConexaoOracleException {

        Connection con = null;
        String sRet = "";
        
        try {
            
            Class.forName("oracle.jdbc.driver.OracleDriver");
            DriverManager.setLoginTimeout(5);
            
            String urlConexao = "jdbc:oracle:thin:@" +
                    FuncoesArquivo.lePropriedade("conexao.oracle.ip.servidor") + ":" +
                    FuncoesArquivo.lePropriedade("conexao.oracle.porta") + ":" +
                    FuncoesArquivo.lePropriedade("conexao.oracle.basedados");

            con = DriverManager.getConnection(urlConexao, usuario, senha);
            con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            
            return con;
            
        } catch(ClassNotFoundException ex) {
            sRet = ex.getMessage() + "\nDriver JDBC não encontrado para MESPATIO.\nFONTE:ConexaoOracle.conecta()";
        } catch (SQLException ex) {
            sRet = ex.getMessage() + "\nErro ao Conectar com Oracle - MESPATIO.\nFONTE:ConexaoOracle.conecta()";
        } catch (Exception ex) {
            sRet = ex.getMessage() + "\nFONTE:ConexaoOracle.conecta() - MESPATIO";
        } 
        
        throw new ConexaoOracleException(sRet);
    }

    public static Connection conectaMES(String usuario, String senha) throws ConexaoOracleException {

        Connection con = null;
        String sRet = "";

        try {

            Class.forName("oracle.jdbc.driver.OracleDriver");
            DriverManager.setLoginTimeout(5);

            String urlConexao = "jdbc:oracle:thin:@" +
                    FuncoesArquivo.lePropriedade("conexao.oracle.mes.ip.servidor") + ":" +
                    FuncoesArquivo.lePropriedade("conexao.oracle.mes.porta") + ":" +
                    FuncoesArquivo.lePropriedade("conexao.oracle.mes.basedados");

            con = DriverManager.getConnection(urlConexao, usuario, senha);
            con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            return con;

        } catch(ClassNotFoundException ex) {
            sRet = ex.getMessage() + "\nDriver JDBC não encontrado - MES.\nFONTE:ConexaoOracle.conecta()";
        } catch (SQLException ex) {
            sRet = ex.getMessage() + "\nErro ao Conectar com Oracle MES.\nFONTE:ConexaoOracle.conecta()";
        } catch (Exception ex) {
            sRet = ex.getMessage() + "\nFONTE:ConexaoOracle.conecta() - MES";
        }

        throw new ConexaoOracleException(sRet);
    }

    public static Connection conectaCRM(String usuario, String senha) throws ConexaoOracleException {

        Connection con = null;
        String sRet = "";

        try {

            Class.forName("oracle.jdbc.driver.OracleDriver");
            DriverManager.setLoginTimeout(5);

            String urlConexao = "";

            urlConexao = "jdbc:oracle:thin:@" +
                    FuncoesArquivo.lePropriedade("conexao.oracle.crm.ubu.ip.servidor") + ":" +
                    FuncoesArquivo.lePropriedade("conexao.oracle.crm.ubu.porta") + ":" +
                    FuncoesArquivo.lePropriedade("conexao.oracle.crm.ubu.basedados");

            con = DriverManager.getConnection(urlConexao, usuario, senha);
            con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            return con;

        } catch(ClassNotFoundException ex) {
            sRet = ex.getMessage() + "\nDriver JDBC não encontrado - CRM.\nFONTE:ConexaoOracle.conecta()";
        } catch (SQLException ex) {
            sRet = ex.getMessage() + "\nErro ao Conectar com Oracle CRM.\nFONTE:ConexaoOracle.conecta()";
        } catch (Exception ex) {
            sRet = ex.getMessage() + "\nFONTE:ConexaoOracle.conecta() - CRM";
        }

        throw new ConexaoOracleException(sRet);
    }

    public static Connection conectaSAP(String usuario, String senha) throws ConexaoOracleException {

        Connection con = null;
        String sRet = "";

        try {

            Class.forName("oracle.jdbc.driver.OracleDriver");
            DriverManager.setLoginTimeout(5);

            String urlConexao = "";

            urlConexao = "jdbc:oracle:thin:@" +
                    FuncoesArquivo.lePropriedade("conexao.oracle.sap.ip.servidor") + ":" +
                    FuncoesArquivo.lePropriedade("conexao.oracle.sap.porta") + ":" +
                    FuncoesArquivo.lePropriedade("conexao.oracle.sap.basedados");

            con = DriverManager.getConnection(urlConexao, usuario, senha);
            con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            return con;

        } catch(ClassNotFoundException ex) {
            sRet = ex.getMessage() + "\nDriver JDBC não encontrado - SAP.\nFONTE:ConexaoOracle.conecta()";
        } catch (SQLException ex) {
            sRet = ex.getMessage() + "\nErro ao Conectar com Oracle SAP.\nFONTE:ConexaoOracle.conecta()";
        } catch (Exception ex) {
            sRet = ex.getMessage() + "\nFONTE:ConexaoOracle.conecta() - SAP";
        }

        throw new ConexaoOracleException(sRet);
    }


}
