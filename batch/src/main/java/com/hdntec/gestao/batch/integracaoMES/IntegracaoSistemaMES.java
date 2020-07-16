package com.hdntec.gestao.batch.integracaoMES;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.hdntec.gestao.batch.conexaoJDBC.ConexaoOracle;
import com.hdntec.gestao.batch.conexaoJDBC.ConexaoOracleException;
import com.hdntec.gestao.batch.conexaoJDBC.ConexaoPostgresException;
import com.hdntec.gestao.batch.controlador.ControladorIntegracaoSistemasExternos;
import com.hdntec.gestao.batch.controlador.IntegracaoParametrosVO;
import com.hdntec.gestao.batch.utilitarios.arquivo.FuncoesArquivo;
import com.hdntec.gestao.batch.utilitarios.arquivo.PropertiesUtil;
import com.hdntec.gestao.exceptions.IntegracaoNaoRealizadaException;


public class IntegracaoSistemaMES
{

   /** Conexao com o banco de dados MES  */
   private Connection conexaoMes = null;

   /** Conexao com banco de dados de integracao */
   private Connection conexaoIntegracao = null;

   public IntegracaoSistemaMES()
   {
      try
      {
         conexaoMes = ConexaoOracle.conectaMES(FuncoesArquivo.lePropriedade("usuario.sistema.mes"), FuncoesArquivo.lePropriedade("senha.usuario.sistema.mes"));
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

   /** Metodo que sera o acesso a inicializacao da integracao com o sistema MES */
   public void inicializaIntegracaoMES(Date dataInicioExecucao) throws IntegracaoNaoRealizadaException
   {

      // dados referentes a qualidade
      //boolean atualizou = false;
	   /**
	    *  producao/qualidade usina 1,2,3 
	    * */

	   
       List<IntegracaoDadosMES> listaDadosMESProducao = buscarDadosIntegracaoUsina(dataInicioExecucao,1);
        listaDadosMESProducao.addAll(buscarDadosIntegracaoUsina(dataInicioExecucao,2));
        listaDadosMESProducao.addAll(buscarDadosIntegracaoUsina(dataInicioExecucao,3));
       
	   if (!listaDadosMESProducao.isEmpty())
      {
         //atualizou = true;
         inserirDadosIntegracaoMES(listaDadosMESProducao);
         atualizaParametroIntegracaoMES(dataInicioExecucao, true, ControladorIntegracaoSistemasExternos.SISTEMA_MES_USINAS);
      }
      
	   /**
        *  producao filtragem 1,2 
        * */
	   
      List<IntegracaoDadosMES> listaDadosMESRitmo = buscarDadosIntegracaoFiltragem(dataInicioExecucao, 1);
      listaDadosMESRitmo.addAll(buscarDadosIntegracaoFiltragem(dataInicioExecucao, 2));
      
      if (!listaDadosMESRitmo.isEmpty())
      {

         inserirDadosIntegracaoMES(listaDadosMESRitmo);         
         atualizaParametroIntegracaoMES(dataInicioExecucao, true, ControladorIntegracaoSistemasExternos.SISTEMA_MES_FILTRAGEM);
      }

     
      /**
       *  ritmo preparacao qualidade quimico
       * */
      
      List<IntegracaoDadosMES> listaDadosMESPreparacao = buscarDadosIntegracaoFiltragemQuimico(dataInicioExecucao,1);
      listaDadosMESPreparacao.addAll(buscarDadosIntegracaoFiltragemQuimico(dataInicioExecucao,2));
      if (!listaDadosMESPreparacao.isEmpty())
      {

         inserirDadosIntegracaoMES(listaDadosMESPreparacao);
         atualizaParametroIntegracaoMES(dataInicioExecucao, true, ControladorIntegracaoSistemasExternos.SISTEMA_MES_FILTRAGEM_QUIMICO);
      }

      
      /**
       *  ritmo preparacao qualidade fisico
       * */

      
      List<IntegracaoDadosMES> listaDadosMESPreparacaoF = buscarDadosIntegracaoFiltragemFisico(dataInicioExecucao,1);
      listaDadosMESPreparacaoF.addAll(buscarDadosIntegracaoFiltragemFisico(dataInicioExecucao,2));
      if (!listaDadosMESPreparacao.isEmpty())
      {

         inserirDadosIntegracaoMES(listaDadosMESPreparacao);
         atualizaParametroIntegracaoMES(dataInicioExecucao, true, ControladorIntegracaoSistemasExternos.SISTEMA_MES_FILTRAGEM_FISICO);
      }
           
   }

   /**
    * Metodo que atualiza o parametro da integracao do sistema mes
    * com a data e hora da integracao que acabou de ser realizada
    * @param dataInicioIntegracao
    */
   private void atualizaParametroIntegracaoMES(Date dataInicioIntegracao, boolean atualizaCampoIntegracao, Integer idSistema)
   {

      PreparedStatement stm = null;
      String sql = "";

      try
      {

         sql = "UPDATE integracaoparametros SET \n";
         sql += "dataultimaintegracao = ? ";
         sql += ", atualizacaocampointegracao = ? ";
         sql += "WHERE idsistema = " + idSistema;

         stm = conexaoIntegracao.prepareStatement(sql);
         stm.setTimestamp(1, new java.sql.Timestamp(dataInicioIntegracao.getTime()));
         if (atualizaCampoIntegracao)
         {
            stm.setString(2, "TRUE");
         }

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
    * Metodo que insere os dados do MES no banco de integracao
    * @param listaDadosMES
    */
   private void inserirDadosIntegracaoMES(List<IntegracaoDadosMES> listaDadosMES)
   {

      PreparedStatement stm = null;
      String sql = "";

      try
      {

         conexaoIntegracao.setAutoCommit(false);

         sql = "INSERT INTO INTEGRACAOMES \n";
         sql += "(IDINTEGRACAOMES, CDFASEPROCESSO,CDITEMCONTROLE,CDTIPOPROCESSO, \n";
         sql += "CDAREARESPED,DATALEITURA,VALORLEITURA,DATALEITURAINICIO) \n";
         sql += "VALUES (seqintegracaomes.nextval, ?, ?, ?,  ?, ?, ?, ?)";
         conexaoIntegracao.setAutoCommit(true);
         stm = conexaoIntegracao.prepareStatement(sql);

         for (IntegracaoDadosMES integraMES : listaDadosMES)
         {

            stm.clearParameters();

            stm.setLong(1, integraMES.getCdFaseProcesso());
            stm.setLong(2, integraMES.getCdItemControle());
            stm.setString(3, integraMES.getCdTipoProcesso());
            stm.setString(4, integraMES.getCdAreaRespEd());
            stm.setTimestamp(5, new java.sql.Timestamp(integraMES.getDataMedicao().getTime()));
            stm.setDouble(6, integraMES.getValorMedido());
            if (integraMES.getDataMedicaoAnterior() != null)
            {
               stm.setTimestamp(7, new java.sql.Timestamp(integraMES.getDataMedicaoAnterior().getTime()));
            }
            else
            {
               stm.setTimestamp(7, null);
            }
            try {  
              stm.execute();
              conexaoIntegracao.commit();              
            } catch (SQLException sqlEx)
            {               
                //sqlEx.printStackTrace();             
            }
         }
         
         stm.close();

      }
      catch (SQLException sqlEx)
      {
         sqlEx.printStackTrace();
         throw new RuntimeException(sqlEx.getMessage());
      }
   }

   /** Metodo que realiza a busca dos dados no sistema MES e transfere
    * para o banco de integracao MAX SIZE = 3
    */
   private List<IntegracaoDadosMES> buscarDadosIntegracaoProducao(Date dataInicioExcucao, IntegracaoDadosMES parametros, Integer idSistema)
   {

      IntegracaoParametrosVO integracaoParametro = obterParametroMES(idSistema);

      PreparedStatement stm = null;
      ResultSet rs = null;
      StringBuilder sql = new StringBuilder();
      List<IntegracaoDadosMES> listaDadosIntegracao = new ArrayList<IntegracaoDadosMES>();
      try
      {

         sql.append("SELECT * FROM mes_pro_ingredientinput ");
         sql.append(" WHERE inputdate >= ? and inputdate < ? ");
         sql.append(" AND cd_fase_processo  = ? ");
         sql.append(" order by cd_fase_processo,cd_item_controle,cd_tipo_processo,cd_area_resp_ed,timestamp ");
        /*and cd_item_controle = ? and cd_tipo_processo =  ? and cd_area_resp_ed = ? */
         stm = conexaoMes.prepareStatement(sql.toString());
         
         if (idSistema == ControladorIntegracaoSistemasExternos.SISTEMA_MES_FILTRAGEM) {
        	 GregorianCalendar c = new GregorianCalendar(); 
        	 c.setTime(integracaoParametro.getDataUltimaIntegracao());
        	 c.add(Calendar.DAY_OF_MONTH, -1); 
        	 integracaoParametro.setDataUltimaIntegracao(new Date(c.getTimeInMillis()));
         }
         stm.setTimestamp(1, new java.sql.Timestamp(integracaoParametro.getDataUltimaIntegracao().getTime()));        
         stm.setTimestamp(2, new java.sql.Timestamp(dataInicioExcucao.getTime()));
         
         if (parametros.getCdFaseProcesso() != null) {
             stm.setLong(3, parametros.getCdFaseProcesso());    
         } else {
             stm.setNull(3, java.sql.Types.INTEGER); 
         }
         
        /* if (parametros.getCdItemControle() != null) {
             stm.setLong(4, parametros.getCdItemControle());    
         } else {
             stm.setNull(4, java.sql.Types.NUMERIC); 
         }
         
         if (parametros.getCdTipoProcesso() != null) {
             stm.setString(5, parametros.getCdTipoProcesso());    
         } else {
             stm.setNull(5, java.sql.Types.VARCHAR); 
         }
         
         if (parametros.getCdAreaRespEd() != null) {
             stm.setString(6, parametros.getCdAreaRespEd());    
         } else {
             stm.setNull(6, java.sql.Types.VARCHAR); 
         }*/
         
         rs = stm.executeQuery();
         Date lastDate = null;
         String key = null;
         while (rs.next())
         {
            if (!verificarImportacaoItem(rs.getLong("cd_fase_processo"),rs.getLong("cd_item_controle"),rs.getString("cd_tipo_processo"),rs.getString("cd_area_resp_ed"),new Date(rs.getTimestamp("timestamp").getTime()),rs.getDouble("inputvalue") )) 
            {
               IntegracaoDadosMES integraMES = new IntegracaoDadosMES();
               integraMES.setCdFaseProcesso(rs.getLong("cd_fase_processo"));
               integraMES.setCdItemControle(rs.getLong("cd_item_controle"));
               integraMES.setCdTipoProcesso(rs.getString("cd_tipo_processo"));
               integraMES.setCdAreaRespEd(rs.getString("cd_area_resp_ed"));
               integraMES.setDataMedicao(new Date(rs.getTimestamp("timestamp").getTime()));               
               integraMES.setValorMedido(rs.getDouble("inputvalue"));

               //valida se mudou a chave
               StringBuilder currKey = new StringBuilder();
               currKey.append(integraMES.getCdFaseProcesso().toString().trim());
               currKey.append(integraMES.getCdItemControle().toString().trim());
               currKey.append(integraMES.getCdTipoProcesso().trim());
               currKey.append(integraMES.getCdAreaRespEd().trim());

               if (key != null && !key.equals(currKey.toString()))
               {
                  key = null;
               }
               // verifica mudanca de chave
               if (key == null)
               {
                  key = new String(currKey.toString());
                  lastDate = getDataInicial(integraMES);
               }
               //lastDate = validarChave(key, integraMES);
               integraMES.setDataMedicaoAnterior(lastDate);
               lastDate = new Date(integraMES.getDataMedicao().getTime());
               listaDadosIntegracao.add(integraMES);
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

   
   
   private boolean verificarImportacaoItem(Long cdFaseProcesso, Long cdItemControle, String cdTipoProcesso, String cdAreaRespEd, Date dataLeitura,Double valor) throws SQLException
   {
      boolean dadoJaImportado = false;
      
      PreparedStatement stm = null;
      ResultSet rs = null;
      StringBuilder sql = new StringBuilder();

      sql.append("SELECT * FROM integracaomes ");
      sql.append("WHERE cdfaseprocesso = ? ");
      sql.append("AND cditemcontrole = ? ");
      sql.append("AND cdtipoprocesso = ? ");
      sql.append("AND cdarearesped = ?");
      sql.append("AND dataleitura = ?");
      sql.append("AND valorLeitura = ?");

      stm = conexaoIntegracao.prepareStatement(sql.toString());
      stm.setLong(1, cdFaseProcesso);
      stm.setLong(2, cdItemControle);
      stm.setString(3, cdTipoProcesso);
      stm.setString(4, cdAreaRespEd);
      stm.setTimestamp(5, new java.sql.Timestamp(dataLeitura.getTime()));
      stm.setDouble(6, valor);

      rs = stm.executeQuery();
      if (rs.next())
      {
         dadoJaImportado = true;
      }

      rs.close();
      stm.close();
      
      return dadoJaImportado;
   }

   /** Metodo que realiza a busca dos dados no sistema MES e transfere
    * para o banco de integracao MAX SIZE = 3
    */
   private Date getDataInicial(IntegracaoDadosMES integraMES)
   {

      PreparedStatement stm = null;
      ResultSet rs = null;
      StringBuilder sql = new StringBuilder();
      try
      {

         sql.append("SELECT max(dataleitura) as maxdate FROM INTEGRACAOMES ");
         sql.append(" WHERE CDFASEPROCESSO = ? and CDITEMCONTROLE = ? and CDTIPOPROCESSO = ? and CDAREARESPED = ? ");
         sql.append(" group by CDFASEPROCESSO,CDITEMCONTROLE,CDTIPOPROCESSO,CDAREARESPED");

         stm = conexaoIntegracao.prepareStatement(sql.toString());
         stm.setLong(1, integraMES.getCdFaseProcesso());
         stm.setLong(2, integraMES.getCdItemControle());
         stm.setString(3, integraMES.getCdTipoProcesso());
         stm.setString(4, integraMES.getCdAreaRespEd());
         rs = stm.executeQuery();
         Date lastDate = null;
         if (rs.next())
         {
            lastDate = rs.getTimestamp("maxdate");
         }
         rs.close();
         stm.close();
         return lastDate;

      }
      catch (SQLException sqlEx)
      {
         sqlEx.printStackTrace();
         throw new RuntimeException(sqlEx.getMessage());
      }
   }



   /** Metodo que realiza a busca dos dados no sistema MES Referentes a qualidade e transfere
    * para o banco de integracao
    */
   private List<IntegracaoDadosMES> buscarDadosIntegracaoUsina(Date dataInicioExcucao, int index)
   {
      /*mAX SIZE = 3 **/
      IntegracaoDadosMES filtro = new IntegracaoDadosMES();
           
      filtro.setCdFaseProcesso(new Long(PropertiesUtil.buscarPropriedade("producao.fase.pelota.usina" + index)));
      filtro.setCdItemControle(new Long(PropertiesUtil.buscarPropriedade("producao.item_controle.pelota.usina" + index)));
      filtro.setCdTipoProcesso(PropertiesUtil.buscarPropriedade("producao.tipo_processo.pelota.usina" + index));  
      filtro.setCdAreaRespEd(PropertiesUtil.buscarPropriedade("producao.area_resp.ed.pelota.usina" + index));
      return buscarDadosIntegracaoProducao(dataInicioExcucao, filtro, ControladorIntegracaoSistemasExternos.SISTEMA_MES_USINAS);
   }


      
   /** Metodo que realiza a busca dos dados no sistema MES Referentes as quantidades das usinas e transfere
    * para o banco de integracao
    */
   private List<IntegracaoDadosMES> buscarDadosIntegracaoFiltragem(Date dataInicioExcucao,int index)
   {
	   /* mAX SIZE = 3 */
       IntegracaoDadosMES filtro = new IntegracaoDadosMES();
        
       filtro.setCdFaseProcesso(new Long(PropertiesUtil.buscarPropriedade("producao.fase.pellet.filtragem1")));
      
      return buscarDadosIntegracaoProducao(dataInicioExcucao, filtro, ControladorIntegracaoSistemasExternos.SISTEMA_MES_FILTRAGEM);
      
   }
   
   /** Metodo que realiza a busca dos dados no sistema MES Referentes as quantidades das usinas e transfere
    * para o banco de integracao
    */
   private List<IntegracaoDadosMES> buscarDadosIntegracaoFiltragemQuimico(Date dataInicioExcucao,int index)
   {
       /* mAX SIZE = 3 */
       IntegracaoDadosMES filtro = new IntegracaoDadosMES();
        
       filtro.setCdFaseProcesso(new Long(PropertiesUtil.buscarPropriedade("qualidade.fase.pellet.quimico.filtragem" + index)));
      
      return buscarDadosIntegracaoProducao(dataInicioExcucao, filtro, ControladorIntegracaoSistemasExternos.SISTEMA_MES_FILTRAGEM_QUIMICO);
      
   }
   
   /** Metodo que realiza a busca dos dados no sistema MES Referentes as quantidades das usinas e transfere
    * para o banco de integracao
    */
   private List<IntegracaoDadosMES> buscarDadosIntegracaoFiltragemFisico(Date dataInicioExcucao,int index)
   {
       /* mAX SIZE = 3 */
       IntegracaoDadosMES filtro = new IntegracaoDadosMES();
        
       filtro.setCdFaseProcesso(new Long(PropertiesUtil.buscarPropriedade("qualidade.fase.pellet.fisico.filtragem" + index)));
      
      return buscarDadosIntegracaoProducao(dataInicioExcucao, filtro, ControladorIntegracaoSistemasExternos.SISTEMA_MES_FILTRAGEM_QUIMICO);
      
   }
   
   
   
   /**
    * Metodo que busca o parametro do sistema MES para saber qual foi a
    * ultima data/hora de integracao
    *
    * @return IntegracaoPametrosVO
    */
   private IntegracaoParametrosVO obterParametroMES(Integer idSistema)
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
         stm.setInt(1, idSistema);

         rs = stm.executeQuery();
         if (rs.next())
         {
            paramIntegracao = new IntegracaoParametrosVO();
            paramIntegracao.setIdSistema(rs.getInt("idsistema"));
            paramIntegracao.setDataUltimaIntegracao(rs.getTimestamp("dataultimaintegracao"));
            paramIntegracao.setDataUltimaLeitura(rs.getTimestamp("dataUltimaLeitura"));
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

   public Connection getConexaoIntegracao()
   {
      return conexaoIntegracao;
   }

   public Connection getConexaoMes()
   {
      return conexaoMes;
   }

   public void fechaConexao()
   {
      try
      {
         conexaoIntegracao.close();
         conexaoMes.close();
      }
      catch (SQLException ex)
      {
         ex.printStackTrace();
         throw new RuntimeException(ex.getMessage());
      }
   }
}
