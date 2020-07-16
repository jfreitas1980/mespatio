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


public class ConexaoPostgres {
    
    public static Connection conecta() throws ConexaoPostgresException {

        Connection con = null;
        String sRet = "";
        
        try {
            
            Class.forName("org.postgresql.Driver");
            DriverManager.setLoginTimeout(5);

            String urlConexao = "jdbc:postgresql://" +
                    FuncoesArquivo.lePropriedade("conexao.postgres.ip.servidor") + ":" +
                    FuncoesArquivo.lePropriedade("conexao.postgres.porta") + "/" +
                    FuncoesArquivo.lePropriedade("conexao.postgres.basedados");

            con = DriverManager.getConnection(urlConexao, FuncoesArquivo.lePropriedade("conexao.postgres.usuario"), FuncoesArquivo.lePropriedade("conexao.postgres.senha"));
            con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            
            return con;
            
        } catch(ClassNotFoundException ex) {
            sRet = ex.getMessage() + "\nDriver JDBC não encontrado.\nFONTE:ConexaoPostgres.conecta()";
        } catch (SQLException ex) {
            sRet = ex.getMessage() + "\nErro ao Conectar com Postgres.\nFONTE:ConexaoPostgres.conecta()";
        } catch (Exception ex) {
            sRet = ex.getMessage() + "\nFONTE:ConexaoPostgres.conecta()";
        } 
        
        throw new ConexaoPostgresException(sRet);
    }
}
