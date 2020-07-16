package com.hdntec.gestao.batch.integracaoSAP;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hdntec.gestao.batch.conexaoJDBC.ConexaoOracle;
import com.hdntec.gestao.batch.conexaoJDBC.ConexaoOracleException;
import com.hdntec.gestao.batch.conexaoJDBC.ConexaoPostgresException;
import com.hdntec.gestao.batch.controlador.ControladorIntegracaoSistemasExternos;
import com.hdntec.gestao.batch.controlador.IntegracaoParametrosVO;
import com.hdntec.gestao.batch.utilitarios.arquivo.FuncoesArquivo;


public class IntegracaoSistemaSAP {

    /** Conexao com o banco de dados MES  */
    private Connection conexaoSAP = null;

    /** Conexao com banco de dados de integracao */
    private Connection conexaoIntegracao = null;

    public IntegracaoSistemaSAP() {
        try {
            conexaoSAP = ConexaoOracle.conectaSAP(FuncoesArquivo.lePropriedade("usuario.sistema.sap"), FuncoesArquivo.lePropriedade("senha.usuario.sistema.sap"));
            conexaoIntegracao = ConexaoOracle.conecta(FuncoesArquivo.lePropriedade("usuario.banco.integracao"), FuncoesArquivo.lePropriedade("senha.usuario.banco.integracao"));
        } catch (ConexaoPostgresException cnpEx) {
            cnpEx.printStackTrace();
        } catch (ConexaoOracleException cnoEx) {
            cnoEx.printStackTrace();
        }
    }

    /** Metodo que sera o acesso a inicializacao da integracao com o sistema MES */
    public void inicializaIntegracaoSAP(Date dataInicioExecucao) {

       try {
          List<IntegracaoSAP> listaDadosSAP = buscarDadosIntegracao();
           if (listaDadosSAP.isEmpty()) {
               atualizaParametroIntegracaoSAP(dataInicioExecucao, false);
           } else {
              inserirDadosIntegracaoSAP(listaDadosSAP);
              atualizaParametroIntegracaoSAP(dataInicioExecucao, true);
           }
       }
       catch (SQLException sqlex)
       {
          throw new RuntimeException(sqlex.getMessage());
       }
    }

    /**
     * Metodo que atualiza o parametro da integracao do sistema SAP
     * com a data e hora da integracao que acabou de ser realizada
     * @param dataInicioIntegracao
     */
    private void atualizaParametroIntegracaoSAP(Date dataInicioIntegracao, Boolean integracaoRealizada) throws SQLException {

        PreparedStatement stm = null;
        String sql = "";

        try {

            sql =  "UPDATE integracaoparametros SET \n";
            sql += "dataultimaintegracao = ? \n";
            if (integracaoRealizada)
            {
               sql += ", atualizacaocampointegracao = ? \n";
            }
            sql += "WHERE idsistema = " + ControladorIntegracaoSistemasExternos.SISTEMA_SAP;

            stm = conexaoIntegracao.prepareStatement(sql);
            stm.setTimestamp(1, new java.sql.Timestamp(dataInicioIntegracao.getTime()));
            if (integracaoRealizada)
            {
               stm.setString(2, "TRUE");
            }

            stm.execute();
            stm.close();

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            conexaoSAP.close();
            conexaoIntegracao.close();
            throw new RuntimeException(sqlEx.getMessage());
        }
    }


    /**
     * Metodo que insere os dados do SAP no banco de integracao
     * @param listaDadosSAP
     */
    private void inserirDadosIntegracaoSAP(List<IntegracaoSAP> listaDadosSAP) throws SQLException {

        PreparedStatement stm = null;
        String sql = "";

        try {

            conexaoIntegracao.setAutoCommit(false);

            sql =  "INSERT INTO INTEGRACAOSAP \n";
            sql += "(idIntegracaoSAP, cd_SAP_Dado,cd_SAP_Grupo,titulo, \n";
            sql += "dataLeitura,valorEstoque,processado,codigotipoproduto) \n";
            sql += "VALUES (seqintegracaosap.nextval, ?, ?, ?, ?, ?, ?, ?)";

            stm = conexaoIntegracao.prepareStatement(sql);

            for (IntegracaoSAP integraSAP : listaDadosSAP) {

                stm.clearParameters();

                stm.setLong(1, integraSAP.getCd_SAP_Dado());
                stm.setLong(2, integraSAP.getCd_SAP_Grupo());
                stm.setString(3, integraSAP.getTitulo());
                stm.setTimestamp(4, new java.sql.Timestamp(integraSAP.getDataLeitura().getTime()));
                stm.setDouble(5, integraSAP.getValorEstoque());
                stm.setBoolean(6, integraSAP.getProcessado());
                stm.setString(7, integraSAP.getCdProduto());

                stm.execute();
            }

            conexaoIntegracao.commit();
            conexaoIntegracao.setAutoCommit(true);

            stm.close();

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            conexaoSAP.close();
            conexaoIntegracao.close();
            throw new RuntimeException(sqlEx.getMessage());
        }
    }


    /** Metodo que realiza a busca dos dados no sistema SAP e transfere
     * para o banco de integracao
     */
    private List<IntegracaoSAP> buscarDadosIntegracao() throws SQLException{

        IntegracaoParametrosVO integracaoParametro = obterParametroSAP();

        PreparedStatement stm = null;
        PreparedStatement stmTipoProduto = null;
        ResultSet rs = null;
        ResultSet rsTipoProduto = null;
        String sql = "";
        List<IntegracaoSAP> listaDadosIntegracao = new ArrayList<IntegracaoSAP>();
        try {

            sql = "SELECT CDPRODUTO FROM tipoproduto \n";
            sql += "WHERE cdproduto <> '-' \n";
            sql += "ORDER BY cdproduto";

            stmTipoProduto = conexaoIntegracao.prepareStatement(sql);
            rsTipoProduto = stmTipoProduto.executeQuery();

            while (rsTipoProduto.next())
            {
               String codigoTipoProduto = rsTipoProduto.getString("CDPRODUTO");

               sql = "select R.CD_SAP_DADO, R.CD_SAP_GRUPO, R.NM_TITULO, R.DT_DADO, R.VL_DADO \n";
               sql += "from rov_sap_dado r, \n";
               sql += "(select trim(p.ds_produto_port)PROD, length(trim(p.ds_produto_port)) pos \n";
               sql += "   from pro_produto p \n";
               sql += "   where p.cd_produto = ?) p \n";
               sql += "where r.dt_dado >= ? \n";
               sql += " and trim(substr(r.nm_titulo, 1,pos)) = p.prod \n";
               sql += " order by r.dt_dado";

               stm = conexaoSAP.prepareStatement(sql);
               stm.setString(1, codigoTipoProduto);
               stm.setTimestamp(2, new java.sql.Timestamp(integracaoParametro.getDataUltimaIntegracao().getTime()));

               rs = stm.executeQuery();
               while (rs.next()) {
                   IntegracaoSAP integracaoSAP = new IntegracaoSAP();

                   integracaoSAP.setCd_SAP_Dado(rs.getLong("CD_SAP_DADO"));
                   integracaoSAP.setCd_SAP_Grupo(rs.getLong("CD_SAP_GRUPO"));
                   integracaoSAP.setTitulo(rs.getString("NM_TITULO"));
                   integracaoSAP.setDataLeitura(rs.getTimestamp("DT_DADO"));
                   integracaoSAP.setValorEstoque(rs.getDouble("VL_DADO"));
                   integracaoSAP.setProcessado(Boolean.FALSE);
                   integracaoSAP.setCdProduto(codigoTipoProduto);

                   listaDadosIntegracao.add(integracaoSAP);
               }

               rs.close();
               stm.close();
            }

            rsTipoProduto.close();
            stmTipoProduto.close();

            return listaDadosIntegracao;
            
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            conexaoSAP.close();
            conexaoIntegracao.close();
            throw new RuntimeException(sqlEx.getMessage());
        }
    }

    /**
     * Metodo que busca o parametro do sistema SAP para saber qual foi a
     * ultima data/hora de integracao
     *
     * @return IntegracaoPametrosVO
     */
    private IntegracaoParametrosVO obterParametroSAP() throws SQLException {

        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql = "";
        IntegracaoParametrosVO paramIntegracao = null;

        try {
            sql =  "SELECT idSistema, dataultimaintegracao, dataUltimaLeitura \n";
            sql += "FROM integracaoParametros \n";
            sql += "WHERE idSistema = ?";

            stm = conexaoIntegracao.prepareStatement(sql);
            stm.setInt(1, ControladorIntegracaoSistemasExternos.SISTEMA_SAP);

            rs = stm.executeQuery();
            if (rs.next()) {
                paramIntegracao = new IntegracaoParametrosVO();
                paramIntegracao.setIdSistema(rs.getInt("idsistema"));
                paramIntegracao.setDataUltimaIntegracao(rs.getTimestamp("dataultimaintegracao"));
                paramIntegracao.setDataUltimaLeitura(rs.getTimestamp("dataultimaleitura"));
            }

            rs.close();
            stm.close();

            return paramIntegracao;

        } catch (SQLException sqlEx) {
            conexaoSAP.close();
            conexaoIntegracao.close();
            sqlEx.printStackTrace();
            throw new RuntimeException(sqlEx.getMessage());
        }
    }

     public void fechaConexao(){
        try {
            conexaoIntegracao.close();
            conexaoSAP.close();
        } catch (SQLException ex) {
             ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        }
    }
}
