package com.hdntec.gestao.batch.integracaoRPUSINAS;

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
import com.hdntec.gestao.batch.controlador.IntegracaoParametrosVO;
import com.hdntec.gestao.batch.utilitarios.arquivo.FuncoesArquivo;
import com.hdntec.gestao.batch.utilitarios.arquivo.PropertiesUtil;
import com.hdntec.gestao.exceptions.IntegracaoNaoRealizadaException;


public class IntegracaoSistemaRPUSINAS
{

   /** Conexao com o banco de dados MES  */
   private Connection conexaoMes = null;

   /** Conexao com banco de dados de integracao */
   private Connection conexaoIntegracao = null;

   public IntegracaoSistemaRPUSINAS()
   {
      try
      {
         conexaoMes = ConexaoOracle.conecta(FuncoesArquivo.lePropriedade("usuario.sistema.mes"), FuncoesArquivo.lePropriedade("senha.usuario.sistema.mes"));
         conexaoIntegracao = ConexaoOracle.conecta(FuncoesArquivo.lePropriedade("usuario.banco.integracao"), FuncoesArquivo.lePropriedade("senha.usuario.banco.integracao"));
      }
      catch (ConexaoPostgresException cnpEx)
      {
         cnpEx.printStackTrace();
      }
      catch (ConexaoOracleException cnoEx)
      {
         cnoEx.printStackTrace();
      }
   }

   /** Metodo que sera o acesso a inicializacao da integracao com o sistema MES para o ritimo de producao das usinas*/
   public void inicializaIntegracaoRPUsinas(Date dataInicioExecucao) throws IntegracaoNaoRealizadaException
   {

      List<IntegracaoDadosRPUSINAS> listaDadosRPUSINAS = buscarDadosIntegracao();
      if (listaDadosRPUSINAS.isEmpty())
      {
         atualizaParametroIntegracaoRPUSINAS(dataInicioExecucao);
         throw new IntegracaoNaoRealizadaException("Não existe novas informações sobre o ritimo de produção das usinas para importação.");
      }
      inserirDadosIntegracaoRPUsinas(listaDadosRPUSINAS);
      atualizaParametroIntegracaoRPUSINAS(dataInicioExecucao);
   }

   /**
    * Metodo que atualiza o parametro da integracao do sistema mes
    * para o ritimo de produção das usinas
    * com a data e hora da integracao que acabou de ser realizada
    * @param dataInicioIntegracao
    */
   private void atualizaParametroIntegracaoRPUSINAS(Date dataInicioIntegracao)
   {

      PreparedStatement stm = null;
      String sql = "";

      try
      {

         sql = "UPDATE integracaoparametros SET \n";
         sql += "dataultimaintegracao = ? ";
         sql += ", atualizacaocampointegracao = ? ";
       //  sql += "WHERE idsistema = " + ControladorIntegracaoSistemasExternos.SISTEMA_RPUSINAS;

         stm = conexaoIntegracao.prepareStatement(sql);
         stm.setTimestamp(1, new java.sql.Timestamp(dataInicioIntegracao.getTime()));
         stm.setString(2, "FALSE");

         stm.execute();

         stm.close();

      }
      catch (SQLException sqlEx)
      {
         sqlEx.printStackTrace();
         throw new RuntimeException(sqlEx.getMessage());
      }
   }

   /**
    * Metodo que insere os dados do MES para o ritimo de producao das usinas no banco de integracao
    * @param listaDadosRPUSINAS
    */
   private void inserirDadosIntegracaoRPUsinas(List<IntegracaoDadosRPUSINAS> listaDadosRPUSINAS)
   {

      PreparedStatement stm = null;
      String sql = "";

      try
      {

         conexaoIntegracao.setAutoCommit(false);

         sql = "INSERT INTO INTEGRACAORPUSINAS \n";
         sql += "(IDINTEGRACAORPUSINAS, CDFASEPROCESSO,CDITEMCONTROLE,CDTIPOPROCESSO, \n";
         sql += "CDAREARESPED,PROCESSADO,DATALEITURA,VALORLEITURA) \n";
         sql += "VALUES (seqintegracaorpusinas.nextval, ?, ?, ?, ?, ?, ?, ?)";

         stm = conexaoIntegracao.prepareStatement(sql);

         for (IntegracaoDadosRPUSINAS integraRPUSINAS : listaDadosRPUSINAS)
         {

            stm.clearParameters();

            stm.setLong(1, integraRPUSINAS.getCdFaseProcesso());
            stm.setLong(2, integraRPUSINAS.getCdItemControle());
            stm.setString(3, integraRPUSINAS.getCdTipoProcesso());
            stm.setString(4, integraRPUSINAS.getCdAreaRespEd());
            stm.setString(5, integraRPUSINAS.getProcessado());
            stm.setTimestamp(6, new java.sql.Timestamp(integraRPUSINAS.getDataMedicao().getTime()));
            stm.setDouble(7, integraRPUSINAS.getValorMedido());

            stm.execute();
         }

         conexaoIntegracao.commit();
         conexaoIntegracao.setAutoCommit(true);

         stm.close();

      }
      catch (SQLException sqlEx)
      {
         sqlEx.printStackTrace();
         throw new RuntimeException(sqlEx.getMessage());
      }
   }

   /** Metodo que realiza a busca dos dados no sistema MES para o ritimo de
    * produção das usinas e transfere para o banco de integracao
    */
   private List<IntegracaoDadosRPUSINAS> buscarDadosIntegracao()
   {

      IntegracaoParametrosVO integracaoParametro = obterParametroRPUSINAS();
      PreparedStatement stm = null;
      ResultSet rs = null;
      String sql = "";
      try
      {

         List<IntegracaoDadosRPUSINAS> listaDadosIntegracao = new ArrayList<IntegracaoDadosRPUSINAS>();

         int quantidadeUsinas = Integer.parseInt(PropertiesUtil.buscarPropriedade("ritimo.quantidade.usinas"));
         for (int i = 1; i <= quantidadeUsinas; i++)
         {
            // chave do ritimo de producao da tabela do mes para a usina i
            long codigoFaseProcesso = Long.parseLong(PropertiesUtil.buscarPropriedade("ritimo.fase.processo.usina"+i));
            long codigoItemControle = Long.parseLong(PropertiesUtil.buscarPropriedade("ritimo.cd.item.controle.usina"+i));
            String codigoTipoProcesso = PropertiesUtil.buscarPropriedade("ritimo.cd.tipo.processo.usina"+i);
            String codigoAreaResp = PropertiesUtil.buscarPropriedade("ritimo.cd.area.resp.ed.usina"+i);

            sql = "SELECT * FROM mes_pro_ingredientinput \n";
            sql += "WHERE timestamp >= ? \n";
            sql += "AND CD_FASE_PROCESSO = ? \n";
            sql += "AND CD_ITEM_CONTROLE = ? \n";
            sql += "AND CD_TIPO_PROCESSO = ? \n";
            sql += "AND CD_AREA_RESP_ED = ? \n";
            sql += "AND rownum = ?";

            stm = conexaoMes.prepareStatement(sql);
            stm.setDate(1, new java.sql.Date(integracaoParametro.getDataUltimaIntegracao().getTime()));
            stm.setLong(2, codigoFaseProcesso);
            stm.setLong(3, codigoItemControle);
            stm.setString(4, codigoTipoProcesso);
            stm.setString(5, codigoAreaResp);
            stm.setInt(6, 1);

            rs = stm.executeQuery();
            while (rs.next())
            {
               IntegracaoDadosRPUSINAS integraRPUsinas = new IntegracaoDadosRPUSINAS();

               integraRPUsinas.setCdFaseProcesso(rs.getLong("cd_fase_processo"));
               integraRPUsinas.setCdItemControle(rs.getLong("cd_item_controle"));
               integraRPUsinas.setCdTipoProcesso(rs.getString("cd_tipo_processo"));
               integraRPUsinas.setCdAreaRespEd(rs.getString("cd_area_resp_ed"));
               integraRPUsinas.setDataMedicao(rs.getTimestamp("timestamp"));
               integraRPUsinas.setProcessado("N");
               integraRPUsinas.setValorMedido(rs.getDouble("inputvalue"));

               listaDadosIntegracao.add(integraRPUsinas);
            }
         }

         rs.close();
         stm.close();

         return listaDadosIntegracao;

      }
      catch (SQLException sqlEx)
      {
         sqlEx.printStackTrace();
         throw new RuntimeException(sqlEx.getMessage());
      }
   }

   /**
    * Metodo que busca o parametro do sistema RPUSINAS para saber qual foi a
    * ultima data/hora de integracao
    *
    * @return IntegracaoPametrosVO
    */
   private IntegracaoParametrosVO obterParametroRPUSINAS()
   {

      PreparedStatement stm = null;
      ResultSet rs = null;
      String sql = "";
      IntegracaoParametrosVO paramIntegracao = null;

      try
      {
         sql = "SELECT idSistema, dataultimaintegracao,dataultimaleitura FROM integracaoParametros \n";
         sql += "WHERE idSistema = ?";

         stm = conexaoIntegracao.prepareStatement(sql);
       //  stm.setInt(1, ControladorIntegracaoSistemasExternos.SISTEMA_RPUSINAS);

         rs = stm.executeQuery();
         if (rs.next())
         {
            paramIntegracao = new IntegracaoParametrosVO();
            paramIntegracao.setIdSistema(rs.getInt("idsistema"));
            paramIntegracao.setDataUltimaIntegracao(rs.getDate("dataultimaintegracao"));
            paramIntegracao.setDataUltimaLeitura(rs.getDate("dataUltimaLeitura"));
         }

         rs.close();
         stm.close();

         return paramIntegracao;

      }
      catch (SQLException sqlEx)
      {
         sqlEx.printStackTrace();
         throw new RuntimeException(sqlEx.getMessage());
      }

   }

    public void fechaConexao(){
        try {
            conexaoIntegracao.close();
            conexaoMes.close();
        } catch (SQLException ex) {
             ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        }
    }
}