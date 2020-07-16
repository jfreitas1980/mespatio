package com.hdntec.gestao.batch.integracaoCRM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import com.hdntec.gestao.batch.conexaoJDBC.ConexaoOracle;
import com.hdntec.gestao.batch.conexaoJDBC.ConexaoOracleException;
import com.hdntec.gestao.batch.conexaoJDBC.ConexaoPostgresException;
import com.hdntec.gestao.batch.controlador.ControladorIntegracaoSistemasExternos;
import com.hdntec.gestao.batch.controlador.IntegracaoParametrosVO;
import com.hdntec.gestao.batch.utilitarios.arquivo.FuncoesArquivo;
import com.hdntec.gestao.batch.utilitarios.arquivo.PropertiesUtil;
import com.hdntec.gestao.exceptions.IntegracaoNaoRealizadaException;


public class IntegracaoSistemaCRM {

    /** Conexao com o banco de dados CRM em UBU  */
    private Connection conexaoCRM_UBU = null;

    /** Conexao com banco de dados de integracao */
    private Connection conexaoIntegracao = null;
    private static Logger logger = Logger.getLogger("IntegracaoSistemaCRM");

    public IntegracaoSistemaCRM() {
        try {

        	conexaoCRM_UBU = ConexaoOracle.conectaCRM(FuncoesArquivo.lePropriedade("usuario.sistema.crm.ubu"), FuncoesArquivo.lePropriedade("senha.usuario.sistema.crm.ubu"));
            conexaoIntegracao = ConexaoOracle.conecta(FuncoesArquivo.lePropriedade("usuario.banco.integracao"), FuncoesArquivo.lePropriedade("senha.usuario.banco.integracao"));
        } catch (ConexaoPostgresException cnpEx) {
            cnpEx.printStackTrace();
        } catch (ConexaoOracleException cnoEx) {
            cnoEx.printStackTrace();
        }
    }


    /** Metodo que sera o acesso a inicializacao da integracao com o sistema CRM */
    public IntegracaoNavioCRM atualizarDadosNavio(String chaveSAP) throws IntegracaoNaoRealizadaException, SQLException {
        logger.info("Inicio integracao CRM " +  new Date(System.currentTimeMillis()));
        System.out.println("Inicio integracao CRM " +  new Date(System.currentTimeMillis()));
        // ... busca os novos navios na fila para integracao
        List<IntegracaoNavioCRM> listaNaviosCRM = new ArrayList<IntegracaoNavioCRM>();
        IntegracaoNavioCRM integraNavio = buscarNavioIntegracaoCRM(chaveSAP);      
        // ... se nao exitir novos navios
            if (integraNavio != null) {
                // carrega as informacoes de carga e orientacao de embarque para cada navio novo
                carregarCargasNavio(integraNavio);
            }
            listaNaviosCRM.add(integraNavio);
            // insere os novos navios na fila e retorna verdadeiro para a atualizacao do parametro
            boolean integracaoCRMExecutada = inserirDadosIntegracaoCRM(listaNaviosCRM);
                    
        return integraNavio;
    }

    
    
    
    /** Metodo que sera o acesso a inicializacao da integracao com o sistema CRM */
    public void inicializaIntegracaoCRM(Date dataInicioExecucao) throws IntegracaoNaoRealizadaException, SQLException {
    	logger.info("Inicio integracao CRM " +  new Date(System.currentTimeMillis()));
        System.out.println("Inicio integracao CRM " +  new Date(System.currentTimeMillis()));
    	// ... busca os novos navios na fila para integracao
    	List<IntegracaoNavioCRM> listaNaviosCRM = buscarNaviosIntegracaoCRM();    	
        // ... se nao exitir novos navios
            // se a lista de navios contiver algum navio novo
            for (IntegracaoNavioCRM integraNavio : listaNaviosCRM) {
                // carrega as informacoes de carga e orientacao de embarque para cada navio novo
                carregarCargasNavio(integraNavio);
            }
            // insere os novos navios na fila e retorna verdadeiro para a atualizacao do parametro
            boolean integracaoCRMExecutada = inserirDadosIntegracaoCRM(listaNaviosCRM);
            if (integracaoCRMExecutada)
            {
            	logger.info("Atualizando parametro CRM " +  new Date(System.currentTimeMillis()));
            	System.out.println("Atualizando parametro CRM " +  new Date(System.currentTimeMillis()));
            	atualizaParametroIntegracaoCRM(dataInicioExecucao, true);
            }        
        logger.info("FIM CRM " +  new Date(System.currentTimeMillis()));
        System.out.println("FIM CRM " +  new Date(System.currentTimeMillis()));
    }


    /** Metodo que sera o acesso a inicializacao da integracao com o sistema CRM */
    public void inicializaIntegracaoCRM(String chaveSap) throws IntegracaoNaoRealizadaException, SQLException {
        logger.info("Inicio integracao CRM " +  new Date(System.currentTimeMillis()));
        System.out.println("Inicio integracao CRM " +  new Date(System.currentTimeMillis()));
        // ... busca os novos navios na fila para integracao
        List<IntegracaoNavioCRM> listaNaviosCRM = buscarNaviosIntegracaoCRM(chaveSap);      
        // ... se nao exitir novos navios
            // se a lista de navios contiver algum navio novo
            for (IntegracaoNavioCRM integraNavio : listaNaviosCRM) {
                // carrega as informacoes de carga e orientacao de embarque para cada navio novo
                carregarCargasNavio(integraNavio);
            }
            // insere os novos navios na fila e retorna verdadeiro para a atualizacao do parametro
           // boolean integracaoCRMExecutada = inserirDadosIntegracaoCRM(listaNaviosCRM);
                    
        logger.info("FIM CRM " +  new Date(System.currentTimeMillis()));
        System.out.println("FIM CRM " +  new Date(System.currentTimeMillis()));
    }

    
    
    /**
     * Metodo que carrega todas as cargas de um determinado navio
     * @param integraNavio
     */
    private void carregarCargasNavio(IntegracaoNavioCRM integraNavio) throws SQLException {

        List<IntegracaoCargaCRM> listaCargas = new ArrayList<IntegracaoCargaCRM>();

        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql = "";

        try {

            sql =  "SELECT progEmbProd.cd_produto, progEmbProd.qt_progr_emb_produto, \n";
            sql += "progEmb.sap_vbap_vbeln, prod.ds_produto_port \n";
            sql += "FROM pro_progr_embarque progEmb \n";
            sql += "INNER JOIN pro_progr_emb_produto progEmbProd ON progEmbProd.sap_vbap_vbeln = progEmb.sap_vbap_vbeln \n";
            sql += "INNER JOIN pro_produto prod ON prod.cd_produto = progembprod.cd_produto \n";
            sql += "WHERE progEmb.id_status_emb NOT IN ('E') \n";
            sql += "AND progEmb.nm_navio NOT IN ('TBN') \n";
            sql += "AND progEmb.nr_navio = ? \n";
            sql += "AND progEmb.sap_vbap_vbeln = ? \n";
            sql += "ORDER BY progEmb.nr_navio \n";

            stm = conexaoCRM_UBU.prepareStatement(sql);
            stm.setLong(1, integraNavio.getCdNavio());
            stm.setString(2, integraNavio.getSap_vbap_vbeln());

            rs = stm.executeQuery();
            while (rs.next()) {
                IntegracaoCargaCRM integraCarga = new IntegracaoCargaCRM();

                integraCarga.setCodigoProduto(rs.getString("cd_produto"));
                integraCarga.setDescricaoCarga(rs.getString("ds_produto_port") + " - " + rs.getString("sap_vbap_vbeln"));
                integraCarga.setQuantidadeCarga(rs.getDouble("qt_progr_emb_produto"));
                integraCarga.setSap_vbap_vbeln(rs.getString("sap_vbap_vbeln"));

                integraCarga.setOrientacaoEmbarque(carregaOrientacaoEmbarqueCarga(integraCarga, integraNavio));

                listaCargas.add(integraCarga);
            }

            integraNavio.setListaCargasNavio(listaCargas);

        } catch (SQLException sqlEx) {
            conexaoCRM_UBU.close();
            conexaoIntegracao.close();
            sqlEx.printStackTrace();
            throw new RuntimeException(sqlEx.getMessage());
        }
    }

    private IntegracaoOrientEmbarqueCRM carregaOrientacaoEmbarqueCarga(IntegracaoCargaCRM integraCarga, IntegracaoNavioCRM integraNavio) throws SQLException {

        IntegracaoOrientEmbarqueCRM orientacaoEmbarque = null;

        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql = "";

        try {

            sql =  "SELECT itemcontrato.cd_produto, itemcontrato.cd_contract_cargo, produto.ds_produto_port \n";
            sql += "FROM pro_web_contract@bh contrato \n";
            sql += "INNER JOIN pro_web_contract_cargo@bh itemcontrato ON itemcontrato.cd_contract = contrato.cd_contract \n";
            sql += "INNER JOIN pro_produto produto ON produto.cd_produto = itemcontrato.cd_produto \n";
            sql += "WHERE contrato.cd_cliente = ? \n";
            sql += "AND itemcontrato.cd_produto = ? \n";
            sql += "AND itemcontrato.sap_vbap_vbeln = ?";

            stm = conexaoCRM_UBU.prepareStatement(sql);
            stm.setString(1, integraNavio.getCodigoCliente());
            stm.setString(2, integraCarga.getCodigoProduto());
            stm.setString(3, integraCarga.getSap_vbap_vbeln());

            rs = stm.executeQuery();
            if (rs.next()) {
                orientacaoEmbarque = new IntegracaoOrientEmbarqueCRM();
                orientacaoEmbarque.setDescricaoOrientacaoEmbarque("Orientacao Produto Contrato " + rs.getString("cd_contract_cargo"));
                orientacaoEmbarque.setCdContractCargo(rs.getLong("cd_contract_cargo"));
                orientacaoEmbarque.setCodigoTipoProduto(rs.getString("cd_produto"));
                orientacaoEmbarque.setDescricaoTipoProduto(rs.getString("ds_produto_port"));
                orientacaoEmbarque.setListaIntegracaoItensControle(carregaListaItensControleOrientacaoEmbarque(orientacaoEmbarque.getCdContractCargo()));
            }

            return orientacaoEmbarque;
            
        } catch (SQLException sqlEx) {
            conexaoCRM_UBU.close();
            conexaoIntegracao.close();
            sqlEx.printStackTrace();
            throw new RuntimeException(sqlEx.getMessage());
        }
    }

    private List<IntegracaoItemControleCRM> carregaListaItensControleOrientacaoEmbarque(Long cdContractCargo) throws SQLException {

        List<IntegracaoItemControleCRM> listaItensControle = new ArrayList<IntegracaoItemControleCRM>();

        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql = "";

        try {

            sql =  "SELECT itemcontrole.VL_TYPICAL_MIN, itemcontrole.VL_TYPICAL_MAX, itemcontrole.VL_GUARANTEED_MIN, \n";
            sql += "itemcontrole.VL_GUARANTEED_MAX, itemcontrole.ID_PENALTY, proitem.ds_item_controle, proitem.cd_unidade_prod_port, \n";
            sql += "proitem.cd_item_controle, faseitem.cd_tipo_processo, faseitem.cd_area_resp_ed \n";
            sql += "FROM pro_web_specification_item@bh itemcontrole \n";
            sql += "INNER JOIN pro_web_contract_cargo@bh itemcontrato ON itemcontrato.cd_contract_cargo = itemcontrole.cd_contract_cargo \n";
            sql += "INNER JOIN pro_item_controle proitem ON proitem.cd_item_controle = itemcontrole.cd_item_controle \n";
            sql += "INNER JOIN (SELECT DISTINCT cd_item_controle, cd_area_resp_ed, cd_tipo_processo FROM pro_fase_item \n";
            sql += "            WHERE id_exibe_tela = 'S' \n";
            sql += "            AND cd_fase_processo in (?, ?, ?)) faseitem \n";
            sql += "    ON faseitem.cd_item_controle = proitem.cd_item_controle \n";
            sql += "WHERE itemcontrato.cd_contract_cargo = ? \n";
            sql += "ORDER BY proitem.cd_item_controle";
            
            stm = conexaoCRM_UBU.prepareStatement(sql);
            stm.setLong(1, new Long(PropertiesUtil.buscarPropriedade("producao.fase.pelota.usina1")));
            stm.setLong(2, new Long(PropertiesUtil.buscarPropriedade("producao.fase.pelota.usina2")));
            stm.setLong(3, new Long(PropertiesUtil.buscarPropriedade("producao.fase.pelota.usina3")));
            stm.setLong(4, cdContractCargo);

            rs = stm.executeQuery();
            while (rs.next()) {
                Long idPenalizacao = rs.getLong("ID_PENALTY");

                IntegracaoItemControleCRM item = new IntegracaoItemControleCRM();
                item.setCdTipoItemControle(rs.getLong("cd_item_controle"));
                item.setAreaRespED(rs.getString("cd_area_resp_ed"));
                item.setCoeficiente(new Double(0));
                item.setDescricaoTipoItemControle(rs.getString("ds_item_controle"));
                item.setFimEscala(new Integer(0));
                item.setInicioEscala(new Integer(100));
                item.setMultiplicidadeEscala(new Integer(10));
                if (idPenalizacao.equals(new Long(1))) {
                    item.setRelevante(Boolean.TRUE);
                } else {
                    item.setRelevante(Boolean.FALSE);
                }
                item.setTipoProcesso(rs.getString("cd_tipo_processo"));
                item.setUnidade(rs.getString("cd_unidade_prod_port"));
                item.setValorGarantidoMaximo(null);
                item.setValorGarantidoMinimo(null);
                item.setValorTipicoMaximo(null);
                item.setValorTipicoMinimo(null);

                String valorGarantidoMaximo = rs.getString("VL_GUARANTEED_MAX");
                String valorGarantidoMinimo = rs.getString("VL_GUARANTEED_MIN");
                String valorTipicoMaximo = rs.getString("VL_TYPICAL_MAX");
                String valorTipicoMinimo = rs.getString("VL_TYPICAL_MIN");

                if (valorGarantidoMaximo != null)
                {
                    item.setValorGarantidoMaximo(rs.getDouble("VL_GUARANTEED_MAX"));
                }
                if (valorGarantidoMinimo != null)
                {
                    item.setValorGarantidoMinimo(rs.getDouble("VL_GUARANTEED_MIN"));
                }
                if (valorTipicoMaximo != null)
                {
                    item.setValorTipicoMaximo(rs.getDouble("VL_TYPICAL_MAX"));
                }
                if (valorTipicoMinimo != null)
                {
                    item.setValorTipicoMinimo(rs.getDouble("VL_TYPICAL_MIN"));
                }

                listaItensControle.add(item);
            }

            rs.close();
            stm.close();

            return listaItensControle;

        } catch (SQLException sqlEx) {
            conexaoCRM_UBU.close();
            conexaoIntegracao.close();
            sqlEx.printStackTrace();
            throw new RuntimeException(sqlEx.getMessage());
        }
    }

    /**
     * Metodo que atualiza o parametro da integracao do sistema mes
     * com a data e hora da integracao que acabou de ser realizada
     * @param dataInicioIntegracao
     */
    private void atualizaParametroIntegracaoCRM(Date dataInicioIntegracao, boolean atualizaCampoIntegracao) throws SQLException {

        PreparedStatement stm = null;
        String sql = "";

        try {

            sql =  "UPDATE integracaoparametros SET \n";
            sql += "dataultimaintegracao = ? ";
            sql += ", atualizacaocampointegracao = ? ";
            sql += "WHERE idsistema = " + ControladorIntegracaoSistemasExternos.SISTEMA_CRM;

            stm = conexaoIntegracao.prepareStatement(sql);
            stm.setTimestamp(1, new java.sql.Timestamp(dataInicioIntegracao.getTime()));
            if (atualizaCampoIntegracao)
            {
                stm.setString(2, "TRUE");
            }
            else
            {
                stm.setString(2, "FALSE");
            }

            stm.execute();
            stm.close();

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            conexaoCRM_UBU.close();
            conexaoIntegracao.close();
            throw new RuntimeException(sqlEx.getMessage());
        }
    }


    /**
     * Metodo que insere os dados do MES no banco de integracao
     * @param listaDadosMES
     */
    private boolean inserirDadosIntegracaoCRM(List<IntegracaoNavioCRM> listaDadosCRM) throws SQLException {

        PreparedStatement stm = null;
        String sql = "";

        Statement stmConsulta = null;
        ResultSet rs = null;

        boolean dadosEncontradosParaAtualizacao = false;
        try {

            conexaoIntegracao.setAutoCommit(false);

            for (IntegracaoNavioCRM navio : listaDadosCRM) {

                dadosEncontradosParaAtualizacao = true;

                stmConsulta = conexaoIntegracao.createStatement();

                sql =  "INSERT INTO integracaonaviocrm (idintegracaonaviocrm, processado, capacidadenavio, cdnavio, datachegadanavio, \n";
                sql += "datasaidanavio, nomenavio, statusembarque, dataembarquenavio, sap_vbap_vbeln, codigocliente, dataeta, nomecliente) \n";
                sql += "VALUES (seqintegracaonaviocrm.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) \n";

                stm = conexaoIntegracao.prepareStatement(sql);
                stm.setBoolean(1, navio.getProcessado());
                stm.setDouble(2, navio.getCapacidadeNavio());
                stm.setLong(3, navio.getCdNavio());
                stm.setTimestamp(4, new java.sql.Timestamp(navio.getDataChegadaNavio().getTime()));
                stm.setTimestamp(5, new java.sql.Timestamp(navio.getDataSaidaNavio().getTime()));
                stm.setString(6, navio.getNomeNavio());
                stm.setString(7, navio.getStatusEmbarque());
                stm.setTimestamp(8, new java.sql.Timestamp(navio.getDataEmbarqueNavio().getTime()));
                stm.setString(9, navio.getSap_vbap_vbeln());
                stm.setString(10, navio.getCodigoCliente());
                if (navio.getDataETA() != null) {
                    stm.setTimestamp(11, new java.sql.Timestamp(navio.getDataETA().getTime()));
                } else {
                    stm.setTimestamp(11, new java.sql.Timestamp(new Date().getTime()));
                }
                stm.setString(12, navio.getNomeCliente());

                stm.execute();
                stm.close();

                for (IntegracaoCargaCRM carga : navio.getListaCargasNavio()) {
                    
                    sql =  "INSERT INTO integracaocargacrm (idintegracaocargacrm, sap_vbap_vbeln, codigoproduto, \n";
                    sql += "descricaocarga, quantidadecarga) VALUES ( \n";
                    sql += "seqintegracaocargacrm.nextval, ?, ?, ?, ?)";
                    
                    stm = conexaoIntegracao.prepareStatement(sql);
                    stm.setString(1, carga.getSap_vbap_vbeln());
                    stm.setString(2, carga.getCodigoProduto());
                    stm.setString(3, carga.getDescricaoCarga());
                    stm.setDouble(4, carga.getQuantidadeCarga());
                    
                    stm.execute();
                    stm.close();

                    Long sequenciaIntegracaoCargaCRM = 0L;
                    sql = "SELECT seqintegracaocargacrm.currval as sequenciaIntegracaoCargaCRM FROM integracaocargacrm";
                    rs = stmConsulta.executeQuery(sql);
                    if (rs.next()) {
                        sequenciaIntegracaoCargaCRM = rs.getLong("sequenciaIntegracaoCargaCRM");
                    }

                    sql =  "INSERT INTO integranavio_carga (idintegracaonaviocrm, idintegracaocargacrm) \n";
                    sql += "VALUES (seqintegracaonaviocrm.currval, seqintegracaocargacrm.currval)";

                    stm = conexaoIntegracao.prepareStatement(sql);
                    stm.execute();
                    stm.close();

                    if (carga.getOrientacaoEmbarque() != null) {
                        sql =  "INSERT INTO integracaoorientembarquecrm \n";
                        sql += "(idintegracaoorientembarquecrm, descricaoorientacaoembarque, \n";
                        sql += "codigotipoproduto, descricaoTipoProduto) \n";
                        sql += "VALUES (seqintegracaooecrm.nextval, ?, ?, ?)";

                        stm = conexaoIntegracao.prepareStatement(sql);
                        stm.setString(1, carga.getOrientacaoEmbarque().getDescricaoOrientacaoEmbarque());
                        stm.setString(2, carga.getOrientacaoEmbarque().getCodigoTipoProduto());
                        stm.setString(3, carga.getOrientacaoEmbarque().getDescricaoTipoProduto());

                        stm.execute();
                        stm.close();

                        Long sequenciaIntegracaoOECRM = 0L;
                        sql = "SELECT seqintegracaooecrm.currval as sequenciaIntegracaoOECRM FROM integracaoorientembarquecrm";
                        rs = stmConsulta.executeQuery(sql);
                        if (rs.next()) {
                            sequenciaIntegracaoOECRM = rs.getLong("sequenciaIntegracaoOECRM");
                        }
                        
                        sql =  "UPDATE integracaocargacrm SET \n";
                        sql += "idintegracaoorientembarquecrm = ? \n";
                        sql += "WHERE idintegracaocargacrm = ?";

                        stm = conexaoIntegracao.prepareStatement(sql);
                        stm.setLong(1, sequenciaIntegracaoOECRM);
                        stm.setLong(2, sequenciaIntegracaoCargaCRM);

                        stm.execute();
                        stm.close();

                        for (IntegracaoItemControleCRM itemControle : carga.getOrientacaoEmbarque().getListaIntegracaoItensControle()) {

                            sql =  "INSERT INTO integracaoitemcontrolecrm(idintegracaoiccrm,arearespedpelota,cdtipoitemcontrolepelota,coeficiente, \n";
                            sql += "descricaotipoitemcontrole,fimescala,inicioescala,multiplicidadeescala,relevante,tipoprocessopelota,unidade, \n";
                            sql += "valorgarantidomaximo,valorgarantidominimo,valortipicomaximo,valortipicominimo) VALUES ( \n";
                            sql += "seqintegracaoiccrm.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                            stm = conexaoIntegracao.prepareStatement(sql);
                            stm.setString(1, itemControle.getAreaRespED());
                            stm.setLong(2, itemControle.getCdTipoItemControle());
                            stm.setDouble(3, itemControle.getCoeficiente());
                            stm.setString(4, itemControle.getDescricaoTipoItemControle());
                            stm.setInt(5, itemControle.getFimEscala());
                            stm.setInt(6, itemControle.getInicioEscala());
                            stm.setInt(7, itemControle.getMultiplicidadeEscala());
                            stm.setBoolean(8, itemControle.getRelevante());
                            stm.setString(9, itemControle.getTipoProcesso());
                            stm.setString(10, itemControle.getUnidade());
                            if (itemControle.getValorGarantidoMaximo() != null)
                            {
                                stm.setDouble(11, itemControle.getValorGarantidoMaximo());
                            }
                            else
                            {
                                stm.setNull(11, java.sql.Types.DOUBLE);
                            }
                            if (itemControle.getValorGarantidoMinimo() != null)
                            {
                                stm.setDouble(12, itemControle.getValorGarantidoMinimo());
                            }
                            else
                            {
                                stm.setNull(12, java.sql.Types.DOUBLE);
                            }
                            if (itemControle.getValorTipicoMaximo() != null)
                            {
                                stm.setDouble(13, itemControle.getValorTipicoMaximo());
                            }
                            else
                            {
                                stm.setNull(13, java.sql.Types.DOUBLE);
                            }
                            if (itemControle.getValorTipicoMinimo() != null)
                            {
                                stm.setDouble(14, itemControle.getValorTipicoMinimo());
                            }
                            else
                            {
                                stm.setNull(14, java.sql.Types.DOUBLE);
                            }

                            stm.execute();
                            stm.close();

                            sql =  "INSERT INTO integracaooe_itemcontrole (idintegracaoorientembarquecrm, \n";
                            sql += "idintegracaoiccrm) VALUES (seqintegracaooecrm.currval, seqintegracaoiccrm.currval)";

                            stm = conexaoIntegracao.prepareStatement(sql);
                            stm.execute();
                            stm.close();

                        }
                    }
                }
                stmConsulta.close();
                rs.close();
            }

            conexaoIntegracao.commit();
            conexaoIntegracao.setAutoCommit(true);
  
            return dadosEncontradosParaAtualizacao;
            
        } catch (SQLException sqlEx) {
            conexaoCRM_UBU.close();
            conexaoIntegracao.close();
            sqlEx.printStackTrace();
            throw new RuntimeException(sqlEx.getMessage());
        } finally {
            if (stm != null)
                stm.close();
        }
    }


    /** Metodo que realiza a busca dos dados no sistema MES e transfere
     * para o banco de integracao
     */
    private List<IntegracaoNavioCRM> buscarNaviosIntegracaoCRM() throws SQLException {

        IntegracaoParametrosVO integracaoParametro = obterParametroCRM();

        PreparedStatement stm = null;
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer();
        List<IntegracaoNavioCRM> listaNavios = new ArrayList<IntegracaoNavioCRM>();

        // criando calendario para data da ultima execucao
        Calendar dataFinalIntegracao = Calendar.getInstance();
        dataFinalIntegracao.setTime(integracaoParametro.getDataUltimaIntegracao());

        // somando o numero de dias parametrizado para a data final
        Integer nroDias = new Integer(PropertiesUtil.buscarPropriedade("quantidade.dias.a.frente.busca.navios.fila"));
        dataFinalIntegracao.add(Calendar.DAY_OF_MONTH, nroDias);

        HashMap<Long, IntegracaoNavioCRM> mapaNovosNavios = new HashMap<Long, IntegracaoNavioCRM>();
        try {

            
        	sql.append("SELECT progEmb.ID_STATUS_EMB,");
        	sql.append("progEmb.nr_navio,");
        	sql.append("progEmb.nm_navio,");
        	sql.append("progEmb.dt_progr_embarque,");
        	sql.append("progEmb.dt_inicio_eta,");
        	sql.append("progEmb.dt_inicio_layday,");
        	sql.append("progEmb.dt_fim_layday,");
        	sql.append("progEmb.sap_vbap_vbeln,");
        	sql.append("progEmb.cd_cliente,");
        	sql.append("cliente.nm_cliente,");
        	sql.append("dadosgerais.dt_chegada_barra,");
        	sql.append("dadosgerais.dt_atracacao,");
        	sql.append("dadosgerais.dt_desatracacao,");
        	sql.append("navio.vl_capacidade_navio");
        	sql.append(" FROM pro_navio navio,");
        	sql.append("pro_progr_embarque progEmb,");
        	sql.append("pro_cliente cliente,");
        	sql.append("pro_dados_gerais_emb dadosgerais");

        	sql.append(" WHERE progEmb.id_status_emb IN ('A','B','C')");
        	sql.append(" AND navio.nr_navio = progEmb.nr_navio");
        	sql.append(" AND cliente.cd_cliente = progemb.cd_cliente");
        	sql.append(" AND dadosgerais.sap_vbap_vbeln(+) = progEmb.sap_vbap_vbeln");
        	sql.append(" AND navio.nm_navio NOT IN ('TBN') ");
        	sql.append(" AND progEmb.dt_inicio_eta between ? and ?");
        	
        	/*
        	sql =  "SELECT navio.nr_navio, navio.nm_navio, navio.vl_capacidade_navio, progEmb.dt_progr_embarque, \n";
            sql += "progEmb.dt_inicio_eta, progEmb.dt_inicio_layday, progEmb.dt_fim_layday, progEmb.id_status_emb, \n";
            sql += "progEmb.sap_vbap_vbeln, progEmb.cd_cliente, cliente.nm_cliente, dadosgerais.dt_chegada_barra, \n";
            sql += "dadosgerais.dt_atracacao, dadosgerais.dt_desatracacao \n";
            sql += "FROM pro_navio navio \n";
            sql += "INNER JOIN pro_progr_embarque progEmb ON progEmb.nr_navio = navio.nr_navio \n";
            sql += "INNER JOIN pro_cliente cliente ON cliente.cd_cliente = progemb.cd_cliente \n";
            sql += "INNER JOIN pro_dados_gerais_emb dadosgerais ON dadosgerais.sap_vbap_vbeln = progEmb.sap_vbap_vbeln \n";
            sql += "WHERE progEmb.id_status_emb NOT IN ('E') \n";
            sql += "AND navio.nm_navio NOT IN ('TBN') \n";
            sql += "AND progEmb.dt_inicio_eta between ? and ?";*/

            stm = conexaoCRM_UBU.prepareStatement(sql.toString());
            logger.info("Range Busca Navios Sistema externo : "  + new java.sql.Timestamp(integracaoParametro.getDataUltimaIntegracao().getTime()) + " - " + dataFinalIntegracao.getTimeInMillis());
            System.out.println("Range Busca Navios Sistema externo : "  + new java.sql.Timestamp(integracaoParametro.getDataUltimaIntegracao().getTime()) + " - " + new java.sql.Timestamp(dataFinalIntegracao.getTimeInMillis()));
            stm.setTimestamp(1, new java.sql.Timestamp(integracaoParametro.getDataUltimaIntegracao().getTime()));
            
            stm.setTimestamp(2, new java.sql.Timestamp(dataFinalIntegracao.getTimeInMillis()));

            rs = stm.executeQuery();            
            while (rs.next()) {
                if (mapaNovosNavios.get(new Long(rs.getString("sap_vbap_vbeln"))) == null) 
                {
                    if (!verificarNavioIntegrado(rs.getString("sap_vbap_vbeln")))
                    {

                        IntegracaoNavioCRM integraNavio = new IntegracaoNavioCRM();

                        integraNavio.setCdNavio(rs.getLong("nr_navio"));
                        integraNavio.setNomeNavio(rs.getString("nm_navio"));
                        integraNavio.setCapacidadeNavio(rs.getDouble("vl_capacidade_navio"));
                        integraNavio.setDataEmbarqueNavio(rs.getTimestamp("dt_progr_embarque"));
                        integraNavio.setDataETA(rs.getTimestamp("dt_inicio_eta"));
                        integraNavio.setProcessado(Boolean.FALSE);
                        integraNavio.setDataChegadaNavio(rs.getTimestamp("dt_inicio_layday"));
                        integraNavio.setDataSaidaNavio(rs.getTimestamp("dt_fim_layday"));
                        integraNavio.setStatusEmbarque(rs.getString("id_status_emb"));
                        integraNavio.setSap_vbap_vbeln(rs.getString("sap_vbap_vbeln"));
                        integraNavio.setCodigoCliente(rs.getString("cd_cliente"));
                        integraNavio.setNomeCliente(rs.getString("nm_cliente"));
                        integraNavio.setDataAtracacao(rs.getTimestamp("dt_atracacao"));
                        integraNavio.setDataChegadaBarra(rs.getTimestamp("dt_chegada_barra"));
                        integraNavio.setDataDesatracacao(rs.getTimestamp("dt_desatracacao"));
                        integraNavio.setNavioAtualizado(Boolean.TRUE);
                        logger.info("Navio entrando na fila : "  + integraNavio); 
                        System.out.println("Navio entrando na fila : "  + integraNavio);
                        mapaNovosNavios.put(new Long(rs.getString("sap_vbap_vbeln")), integraNavio);
                    }
                }
            }

            listaNavios.addAll(mapaNovosNavios.values());
            
            rs.close();
            stm.close();

            return listaNavios;
            
        } catch (SQLException sqlEx) {
            conexaoCRM_UBU.close();
            conexaoIntegracao.close();
            sqlEx.printStackTrace();
            throw new RuntimeException(sqlEx.getMessage());
        }
    }


    /** Metodo que realiza a busca dos dados no sistema MES e transfere
     * para o banco de integracao
     */
    private IntegracaoNavioCRM buscarNavioIntegracaoCRM(String chaveSap) throws SQLException {

        IntegracaoParametrosVO integracaoParametro = obterParametroCRM();

        PreparedStatement stm = null;
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer();
        IntegracaoNavioCRM integraNavio = null;

        // criando calendario para data da ultima execucao
        Calendar dataFinalIntegracao = Calendar.getInstance();
        dataFinalIntegracao.setTime(integracaoParametro.getDataUltimaIntegracao());

        // somando o numero de dias parametrizado para a data final
        Integer nroDias = new Integer(PropertiesUtil.buscarPropriedade("quantidade.dias.a.frente.busca.navios.fila"));
        dataFinalIntegracao.add(Calendar.DAY_OF_MONTH, nroDias);

        HashMap<Long, IntegracaoNavioCRM> mapaNovosNavios = new HashMap<Long, IntegracaoNavioCRM>();
        try {

            
            sql.append("SELECT progEmb.ID_STATUS_EMB,");
            sql.append("progEmb.nr_navio,");
            sql.append("progEmb.nm_navio,");
            sql.append("progEmb.dt_progr_embarque,");
            sql.append("progEmb.dt_inicio_eta,");
            sql.append("progEmb.dt_inicio_layday,");
            sql.append("progEmb.dt_fim_layday,");
            sql.append("progEmb.sap_vbap_vbeln,");
            sql.append("progEmb.cd_cliente,");
            sql.append("cliente.nm_cliente,");
            sql.append("dadosgerais.dt_chegada_barra,");
            sql.append("dadosgerais.dt_atracacao,");
            sql.append("dadosgerais.dt_desatracacao,");
            sql.append("navio.vl_capacidade_navio");
            sql.append(" FROM pro_navio navio,");
            sql.append("pro_progr_embarque progEmb,");
            sql.append("pro_cliente cliente,");
            sql.append("pro_dados_gerais_emb dadosgerais");

            sql.append(" WHERE progEmb.id_status_emb IN ('A','B','C')");
            sql.append(" AND navio.nr_navio = progEmb.nr_navio");
            sql.append(" AND cliente.cd_cliente = progemb.cd_cliente");
            sql.append(" AND dadosgerais.sap_vbap_vbeln(+) = progEmb.sap_vbap_vbeln");
            sql.append(" AND navio.nm_navio NOT IN ('TBN') ");
            sql.append(" AND progEmb.sap_vbap_vbeln = ? ");

            stm = conexaoCRM_UBU.prepareStatement(sql.toString());
            logger.info("Atualizando dados do navio : "  + chaveSap);
            System.out.println("Atualizando dados do navio : "  + chaveSap);
            stm.setString(1, chaveSap);
            
            rs = stm.executeQuery();            
            if (rs.next()) {
                        integraNavio = new IntegracaoNavioCRM();

                        integraNavio.setCdNavio(rs.getLong("nr_navio"));
                        integraNavio.setNomeNavio(rs.getString("nm_navio"));
                        integraNavio.setCapacidadeNavio(rs.getDouble("vl_capacidade_navio"));
                        integraNavio.setDataEmbarqueNavio(rs.getTimestamp("dt_progr_embarque"));
                        integraNavio.setDataETA(rs.getTimestamp("dt_inicio_eta"));
                        integraNavio.setProcessado(Boolean.FALSE);
                        integraNavio.setDataChegadaNavio(rs.getTimestamp("dt_inicio_layday"));
                        integraNavio.setDataSaidaNavio(rs.getTimestamp("dt_fim_layday"));
                        integraNavio.setStatusEmbarque(rs.getString("id_status_emb"));
                        integraNavio.setSap_vbap_vbeln(rs.getString("sap_vbap_vbeln"));
                        integraNavio.setCodigoCliente(rs.getString("cd_cliente"));
                        integraNavio.setNomeCliente(rs.getString("nm_cliente"));
                        integraNavio.setDataAtracacao(rs.getTimestamp("dt_atracacao"));
                        integraNavio.setDataChegadaBarra(rs.getTimestamp("dt_chegada_barra"));
                        integraNavio.setDataDesatracacao(rs.getTimestamp("dt_desatracacao"));
                        integraNavio.setNavioAtualizado(Boolean.TRUE);
                        logger.info("Navio entrando na fila : "  + integraNavio); 
                        System.out.println("Navio entrando na fila : "  + integraNavio);
                    
            }

            
            rs.close();
            stm.close();

            return integraNavio;
            
        } catch (SQLException sqlEx) {
            conexaoCRM_UBU.close();
            conexaoIntegracao.close();
            sqlEx.printStackTrace();
            throw new RuntimeException(sqlEx.getMessage());
        }
    }

    
    
    /** Metodo que realiza a busca dos dados no sistema MES e transfere
     * para o banco de integracao
     */
    public List<IntegracaoNavioCRM> buscarNaviosIntegracaoCRM(String sap_vbap_vbeln) throws SQLException {

        IntegracaoParametrosVO integracaoParametro = obterParametroCRM();

        PreparedStatement stm = null;
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer();
        List<IntegracaoNavioCRM> listaNavios = new ArrayList<IntegracaoNavioCRM>();

        // criando calendario para data da ultima execucao
        Calendar dataFinalIntegracao = Calendar.getInstance();
        dataFinalIntegracao.setTime(integracaoParametro.getDataUltimaIntegracao());

        // somando o numero de dias parametrizado para a data final
        Integer nroDias = new Integer(PropertiesUtil.buscarPropriedade("quantidade.dias.a.frente.busca.navios.fila"));
        dataFinalIntegracao.add(Calendar.DAY_OF_MONTH, nroDias);

        HashMap<Long, IntegracaoNavioCRM> mapaNovosNavios = new HashMap<Long, IntegracaoNavioCRM>();
        try {

            
            sql.append("SELECT progEmb.ID_STATUS_EMB,");
            sql.append("progEmb.nr_navio,");
            sql.append("progEmb.nm_navio,");
            sql.append("progEmb.dt_progr_embarque,");
            sql.append("progEmb.dt_inicio_eta,");
            sql.append("progEmb.dt_inicio_layday,");
            sql.append("progEmb.dt_fim_layday,");
            sql.append("progEmb.sap_vbap_vbeln,");
            sql.append("progEmb.cd_cliente,");
            sql.append("cliente.nm_cliente,");
            sql.append("dadosgerais.dt_chegada_barra,");
            sql.append("dadosgerais.dt_atracacao,");
            sql.append("dadosgerais.dt_desatracacao,");
            sql.append("navio.vl_capacidade_navio");
            sql.append(" FROM pro_navio navio,");
            sql.append("pro_progr_embarque progEmb,");
            sql.append("pro_cliente cliente,");
            sql.append("pro_dados_gerais_emb dadosgerais");

            sql.append(" WHERE progEmb.id_status_emb IN ('A','B','C')");
            sql.append(" AND navio.nr_navio = progEmb.nr_navio");
            sql.append(" AND cliente.cd_cliente = progemb.cd_cliente");
            sql.append(" AND dadosgerais.sap_vbap_vbeln(+) = progEmb.sap_vbap_vbeln");
            sql.append(" AND navio.nm_navio NOT IN ('TBN') ");
            sql.append(" AND progEmb.sap_vbap_vbeln ? ");
            
         
            stm = conexaoCRM_UBU.prepareStatement(sql.toString());
            stm.setString(1, sap_vbap_vbeln);
            
            rs = stm.executeQuery();            
            if (rs.next()) {
                    if (!verificarNavioIntegrado(rs.getString("sap_vbap_vbeln")))
                    {

                        IntegracaoNavioCRM integraNavio = new IntegracaoNavioCRM();

                        integraNavio.setCdNavio(rs.getLong("nr_navio"));
                        integraNavio.setNomeNavio(rs.getString("nm_navio"));
                        integraNavio.setCapacidadeNavio(rs.getDouble("vl_capacidade_navio"));
                        integraNavio.setDataEmbarqueNavio(rs.getTimestamp("dt_progr_embarque"));
                        integraNavio.setDataETA(rs.getTimestamp("dt_inicio_eta"));
                        integraNavio.setProcessado(Boolean.FALSE);
                        integraNavio.setDataChegadaNavio(rs.getTimestamp("dt_inicio_layday"));
                        integraNavio.setDataSaidaNavio(rs.getTimestamp("dt_fim_layday"));
                        integraNavio.setStatusEmbarque(rs.getString("id_status_emb"));
                        integraNavio.setSap_vbap_vbeln(rs.getString("sap_vbap_vbeln"));
                        integraNavio.setCodigoCliente(rs.getString("cd_cliente"));
                        integraNavio.setNomeCliente(rs.getString("nm_cliente"));
                        integraNavio.setDataAtracacao(rs.getTimestamp("dt_atracacao"));
                        integraNavio.setDataChegadaBarra(rs.getTimestamp("dt_chegada_barra"));
                        integraNavio.setDataDesatracacao(rs.getTimestamp("dt_desatracacao"));
                        integraNavio.setNavioAtualizado(Boolean.TRUE);
                    }             
            }

            listaNavios.addAll(mapaNovosNavios.values());
            
            rs.close();
            stm.close();

            return listaNavios;
            
        } catch (SQLException sqlEx) {
            conexaoCRM_UBU.close();
            conexaoIntegracao.close();
            sqlEx.printStackTrace();
            throw new RuntimeException(sqlEx.getMessage());
        }
    }

    
    /**
     * Verifica se existe na tabela de integracao dos navios no mespatio o navio
     * referente ao codigo do sap passado por parametro.
     * @param sap_vbap_vbeln
     * @return
     * @throws java.sql.SQLException
     */
    private boolean verificarNavioIntegrado(String sap_vbap_vbeln) throws SQLException
    {

        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql = "";

        try
        {
            boolean navioExistenteMesPatio = false;
            
            sql =  "SELECT * FROM integracaonaviocrm \n";
            sql += "WHERE sap_vbap_vbeln = ?";

            stm = conexaoIntegracao.prepareStatement(sql);
            stm.setString(1, sap_vbap_vbeln);

            rs = stm.executeQuery();
            if (rs.next())
            {
                navioExistenteMesPatio = true;
            }

            rs.close();
            stm.close();

            return navioExistenteMesPatio;
        } 
        catch (SQLException sqlEx) 
        {
            conexaoCRM_UBU.close();
            conexaoIntegracao.close();
            sqlEx.printStackTrace();
            throw new RuntimeException(sqlEx.getMessage());
        }
    }


    /** Metodo que realiza a busca dos dados no sistema MES e faz update
     *  do status no banco de integracao
     */
    @Deprecated
    private boolean  atualizarStatusNaviosCRM() throws SQLException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        StringBuilder sql = new StringBuilder();
        boolean dadosEncontradosParaAtualizacao = false;
        
        try {
                    	        	
        	sql.append("SELECT statusembarque, sap_vbap_vbeln from integracaonaviocrm ");
            sql.append(" where processado = 1");
            stm = conexaoIntegracao.prepareStatement(sql.toString());
            
            rs = stm.executeQuery();
            
            while (rs.next()) {
                sql = new StringBuilder();
        	    sql.append("SELECT progEmb.id_status_emb, progEmb.sap_vbap_vbeln from pro_progr_embarque progEmb");
        	    sql.append(" where progEmb.sap_vbap_vbeln = ? and progEmb.id_status_emb <> ?");
                
                stm = conexaoCRM_UBU.prepareStatement(sql.toString());
                stm.setString(1, rs.getString("sap_vbap_vbeln"));
                stm.setString(2, rs.getString("statusembarque"));
                rs1 = stm.executeQuery();                
                while (rs1.next()) {
                	  dadosEncontradosParaAtualizacao = true;

                    sql = new StringBuilder();
                	  sql.append("update integracaonaviocrm set statusembarque = ?,processado = ?, navioatualizado = ? where sap_vbap_vbeln = ?");
                    stm = conexaoIntegracao.prepareStatement(sql.toString());
                    
                    stm.setString(1, rs1.getString("id_status_emb"));
                    stm.setBoolean(2, Boolean.FALSE);
                    stm.setBoolean(3, Boolean.TRUE);
                    stm.setString(4, rs1.getString("sap_vbap_vbeln"));
                    
                    stm.execute();
                }
               rs1.close();  
            }

            rs.close();
            stm.close();

            return dadosEncontradosParaAtualizacao;
            
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            conexaoCRM_UBU.close();
            conexaoIntegracao.close();
            throw new RuntimeException(sqlEx.getMessage());
        }finally {
        	rs = null;
        	rs1 = null;
        	stm = null;
        }
    }
    
     /* Metodo que busca o parametro do sistema MES para saber qual foi a
     * ultima data/hora de integracao
     *
     * @return IntegracaoPametrosVO
     */
    private IntegracaoParametrosVO obterParametroCRM() {

        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql = "";
        IntegracaoParametrosVO paramIntegracao = null;

        try {
            sql =  "SELECT idSistema, dataultimaintegracao, dataUltimaLeitura \n";
            sql += "FROM integracaoParametros \n";
            sql += "WHERE idSistema = ?";

            stm = conexaoIntegracao.prepareStatement(sql);
            stm.setInt(1, ControladorIntegracaoSistemasExternos.SISTEMA_CRM);

            rs = stm.executeQuery();
            if (rs.next()) {
                paramIntegracao = new IntegracaoParametrosVO();
                paramIntegracao.setIdSistema(rs.getInt("idsistema"));
                paramIntegracao.setDataUltimaIntegracao(rs.getDate("dataultimaintegracao"));
                paramIntegracao.setDataUltimaLeitura(rs.getDate("dataultimaleitura"));
            }

            rs.close();
            stm.close();

            return paramIntegracao;
            
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            throw new RuntimeException(sqlEx.getMessage());
        }
    }

    public Connection getConexaoCRM_UBU() {
        return conexaoCRM_UBU;
    }

    public Connection getConexaoIntegracao() {
        return conexaoIntegracao;
    }

     public void fechaConexao(){
        try {
            conexaoIntegracao.close();
            conexaoCRM_UBU.close();
        } catch (SQLException ex) {
             ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        }
    }

     /**
      * atualiza os dados(identificadorBerco) do navio, para saber em que berco o navio deve atracar
      * @throws java.sql.SQLException
      */
    public boolean atualizaDadosNavioCRM(List<IntegracaoNavioCRM> listaNaviosCRM) throws SQLException {
        PreparedStatement stm = null;
        ResultSet rs1 = null;
        StringBuilder sql = new StringBuilder();
        boolean dadosEncontradosParaAtualizacao = false;
        try {
            if (!listaNaviosCRM.isEmpty())
            {
                for (IntegracaoNavioCRM navio : listaNaviosCRM) {
                     sql = new StringBuilder();
                 	/*sql.append("SELECT progEmb.ID_STATUS_EMB,");
                	sql.append("progEmb.nr_navio,");
                	sql.append("progEmb.nm_navio,");
                	sql.append("progEmb.dt_progr_embarque,");
                	sql.append("progEmb.dt_inicio_eta,");
                	sql.append("progEmb.dt_inicio_layday,");
                	sql.append("progEmb.dt_fim_layday,");
                	sql.append("progEmb.sap_vbap_vbeln,");
                	sql.append("progEmb.cd_cliente,");
                	sql.append("cliente.nm_cliente,");
                	sql.append("dadosgerais.dt_chegada_barra,");
                	sql.append("dadosgerais.dt_atracacao,");
                	sql.append("dadosgerais.dt_desatracacao,");
                	sql.append("navio.vl_capacidade_navio");
                	sql.append(" FROM pro_navio navio,");
                	sql.append("pro_progr_embarque progEmb,");
                	sql.append("pro_cliente cliente,");
                	sql.append("pro_dados_gerais_emb dadosgerais");
                	sql.append(" WHERE progEmb.sap_vbap_vbeln = ? ");*/
                	
                     
                     
                     sql.append("SELECT dadosgerais.id_berco_atracacao, dadosgerais.sap_vbap_vbeln, dadosgerais.DT_CHEGADA_BARRA,");
                     sql.append(" dadosgerais.DT_ATRACACAO, dadosgerais.DT_DESATRACACAO, progEmb.dt_progr_embarque, progEmb.dt_inicio_eta,");
                     sql.append(" progEmb.dt_inicio_layday, progEmb.dt_fim_layday, progEmb.nm_navio");
                     sql.append(" FROM pro_dados_gerais_emb dadosgerais");
                     sql.append(" INNER JOIN PRO_PROGR_EMBARQUE progEmb ON progEmb.sap_vbap_vbeln = dadosgerais.sap_vbap_vbeln");
                     sql.append(" WHERE dadosgerais.sap_vbap_vbeln = ?");

                     stm = conexaoCRM_UBU.prepareStatement(sql.toString());
                     stm.setString(1, navio.getSap_vbap_vbeln());

                     //guarda os dados do select executado, retornando o id_Berco para poder atualizar o campo da IntegracaoNavioCRM
                     rs1 = stm.executeQuery();
                     while (rs1.next()) {
                         dadosEncontradosParaAtualizacao = true;

                         //atualizando tabela IntegracaoNavioCRM
                         sql = new StringBuilder();
                         sql.append("UPDATE integracaonaviocrm SET");
                         sql.append(" IDENTIFICADORBERCO = ? ");
                         sql.append(",datachegadabarra = ? ");
                         sql.append(",dataatracacao = ? ");
                         sql.append(",datadesatracacao = ? ");
                         sql.append(",dataETA = ? ");
                         sql.append(",dataChegadaNavio = ? ");
                         sql.append(",dataSaidaNavio = ? ");
                         sql.append(",dataEmbarqueNavio = ? ");
                         sql.append(" WHERE sap_vbap_vbeln = ?");

                         stm = conexaoIntegracao.prepareStatement(sql.toString());
                         stm.setString(1, rs1.getString("id_berco_atracacao"));
                         stm.setTimestamp(2, rs1.getTimestamp("DT_CHEGADA_BARRA"));
                         stm.setTimestamp(3, rs1.getTimestamp("DT_ATRACACAO"));
                         stm.setTimestamp(4, rs1.getTimestamp("DT_DESATRACACAO"));
                         stm.setTimestamp(5, rs1.getTimestamp("dt_inicio_eta"));
                         stm.setTimestamp(6, rs1.getTimestamp("dt_inicio_layday"));
                         stm.setTimestamp(7, rs1.getTimestamp("dt_fim_layday"));
                         stm.setTimestamp(8, rs1.getTimestamp("dt_progr_embarque"));
                         stm.setString(9, rs1.getString("sap_vbap_vbeln"));

                         stm.execute();

                         // atualizando os dados do navio
                         sql = new StringBuilder();
                         sql.append("UPDATE navio SET ");
                         sql.append("DATACHEGADABARRA = ? ");
                         sql.append(",DATAATRACACAO = ? ");
                         sql.append(",DATADESATRACACAO = ? ");
                         sql.append(",ETA = ? ");
                         sql.append(",DIADECHEGADA = ? ");
                         sql.append(",DIADESAIDA = ? ");
                         sql.append(",DATAEMBARQUE = ? ");
                         sql.append(" WHERE sap_vbap_vbeln = ?");

                         stm = conexaoIntegracao.prepareStatement(sql.toString());
                         stm.setTimestamp(1, rs1.getTimestamp("DT_CHEGADA_BARRA"));
                         stm.setTimestamp(2, rs1.getTimestamp("DT_ATRACACAO"));
                         stm.setTimestamp(3, rs1.getTimestamp("DT_DESATRACACAO"));
                         stm.setTimestamp(4, rs1.getTimestamp("dt_inicio_eta"));
                         stm.setTimestamp(5, rs1.getTimestamp("dt_inicio_layday"));
                         stm.setTimestamp(6, rs1.getTimestamp("dt_fim_layday"));
                         stm.setTimestamp(7, rs1.getTimestamp("dt_progr_embarque"));
                         stm.setString(8, rs1.getString("sap_vbap_vbeln"));

                         stm.execute();

                     }
                     stm.close();
                     rs1.close();
                }
            }
            return dadosEncontradosParaAtualizacao;

        } catch (SQLException ex) {
            ex.printStackTrace();
            conexaoCRM_UBU.close();
            conexaoIntegracao.close();
            throw new RuntimeException(ex.getMessage());
        } finally {
            rs1 = null;
            stm = null;
        }
    }
    
    /**
     * Verifica e atualiza os dados do Navio caso necesssario
     * @return true se existir atualização ha ser feita<br>
     * false caso contrario;
     * @throws SQLException
     * @author author <a href="mailto:bgomes@cflex.com.br"> Bruno Gomes </a> 
     */
    private boolean atualizaDadosNavioCRM() throws SQLException{
    	 PreparedStatement stm = null;
         ResultSet rs = null;
         ResultSet rs1 = null;
         StringBuilder sql = new StringBuilder();
         boolean dadosEncontradosParaAtualizacao = false;
         
         try {
        	 //busca todos os navios da tabela integracaoNavio para ver se algum deve ser atualizado
         	sql.append("SELECT statusembarque, sap_vbap_vbeln, NOMENAVIO, DATAATRACACAO, DATACHEGADABARRA, DATACHEGADANAVIO, ");
         	sql.append("DATADESATRACACAO, DATAETA, DATAEMBARQUENAVIO, DATASAIDANAVIO ");
         	sql.append(" FROM integracaonaviocrm ");
            sql.append(" where statusembarque <> 'E'");
            stm = conexaoIntegracao.prepareStatement(sql.toString());
            rs = stm.executeQuery();
            
            while(rs.next()){
            	
        	    sql = new StringBuilder();
                sql.append("SELECT dadosgerais.id_berco_atracacao, dadosgerais.sap_vbap_vbeln, dadosgerais.DT_CHEGADA_BARRA,");
                sql.append(" dadosgerais.DT_ATRACACAO, dadosgerais.DT_DESATRACACAO, progEmb.dt_progr_embarque, progEmb.dt_inicio_eta,");
                sql.append(" progEmb.dt_inicio_layday, progEmb.dt_fim_layday, progEmb.nm_navio, progEmb.id_status_emb");
                sql.append(" FROM pro_dados_gerais_emb dadosgerais");
                sql.append(" INNER JOIN PRO_PROGR_EMBARQUE progEmb ON progEmb.sap_vbap_vbeln = dadosgerais.sap_vbap_vbeln");
                sql.append(" WHERE dadosgerais.sap_vbap_vbeln = ?");
        	    
        	    stm = conexaoCRM_UBU.prepareStatement(sql.toString());
                stm.setString(1, rs.getString("sap_vbap_vbeln"));
                rs1 = stm.executeQuery(); 
                
                while (rs1.next()) 
                {//busca o navio (MESPATIO) equivalente no CRM
                	if(existeAtualizacaoParaFazer(rs, rs1))
                	{//verifica necessidade de atualizaçõa, caso contrario nada é feito
                		dadosEncontradosParaAtualizacao = true;
                		
                		//atualizando tabela IntegracaoNavioCRM
                        sql = new StringBuilder();
                        sql.append("UPDATE integracaonaviocrm SET");
                        sql.append(" IDENTIFICADORBERCO = ? ");
                        sql.append(",datachegadabarra = ? ");
                        sql.append(",dataatracacao = ? ");
                        sql.append(",datadesatracacao = ? ");
                        sql.append(",dataETA = ? ");
                        sql.append(",dataChegadaNavio = ? ");
                        sql.append(",dataSaidaNavio = ? ");
                        sql.append(",dataEmbarqueNavio = ? ");
                        sql.append(",STATUSEMBARQUE = ? ");
                        sql.append(",NOMENAVIO = ? ");
                        sql.append(",navioatualizado = ? ");
                        sql.append(" WHERE sap_vbap_vbeln = ?");

                        stm = conexaoIntegracao.prepareStatement(sql.toString());
                        stm.setString(1, rs1.getString("id_berco_atracacao"));
                        stm.setTimestamp(2, rs1.getTimestamp("DT_CHEGADA_BARRA"));
                        stm.setTimestamp(3, rs1.getTimestamp("DT_ATRACACAO"));
                        stm.setTimestamp(4, rs1.getTimestamp("DT_DESATRACACAO"));
                        stm.setTimestamp(5, rs1.getTimestamp("dt_inicio_eta"));
                        stm.setTimestamp(6, rs1.getTimestamp("dt_inicio_layday"));
                        stm.setTimestamp(7, rs1.getTimestamp("dt_fim_layday"));
                        stm.setTimestamp(8, rs1.getTimestamp("dt_progr_embarque"));
                        stm.setString(9, rs1.getString("id_status_emb"));
                        stm.setString(10, rs1.getString("nm_navio"));
                        stm.setBoolean(11, Boolean.TRUE);
                        stm.setString(12, rs1.getString("sap_vbap_vbeln"));
                        stm.execute();
                	}
                }                
            }            
            return dadosEncontradosParaAtualizacao;
            
         }catch (SQLException ex) {
             ex.printStackTrace();
             conexaoCRM_UBU.close();
             conexaoIntegracao.close();
             throw new RuntimeException(ex.getMessage());
         } finally {
             rs1 = null;
             stm = null;
         }
    }
    
    /**
     * Compara os atributos do navio do CRM com a tabela IntegracaoNavioCRM se algum for diferente deve-se atualizar os dados
     * @param rs1 - IntegracaoNavioCRM
     * @param rs2 - pro_dados_gerais_emb INNERJOIN PRO_PROGR_EMBARQUE
     * @return
     * @throws SQLException
     */
    private boolean existeAtualizacaoParaFazer(ResultSet rs1, ResultSet rs2) throws SQLException{
    	boolean atualizacaoNecessaria = Boolean.FALSE;
    	
    	if(!rs1.getString("NOMENAVIO").equalsIgnoreCase(rs2.getString("nm_navio"))){
    		atualizacaoNecessaria = true;
    	}else if( !rs1.getString("STATUSEMBARQUE").equalsIgnoreCase(rs2.getString("id_status_emb"))){
    		atualizacaoNecessaria = true;
    	} else if ((rs1.getTimestamp("DATAATRACACAO") == null && rs2.getTimestamp("DT_ATRACACAO") != null)
				|| (rs1.getTimestamp("DATAATRACACAO") != null && rs2.getTimestamp("DT_ATRACACAO") != null && !rs1.getTimestamp("DATAATRACACAO").equals(rs2.getTimestamp("DT_ATRACACAO")))) {
    		atualizacaoNecessaria = true;
    	} else if ((rs1.getTimestamp("DATACHEGADABARRA") == null && rs2.getTimestamp("DT_CHEGADA_BARRA") != null)
				|| (rs1.getTimestamp("DATACHEGADABARRA") != null && rs2.getTimestamp("DT_CHEGADA_BARRA") != null && !rs1.getTimestamp("DATACHEGADABARRA").equals(rs2.getTimestamp("DT_CHEGADA_BARRA")))) {
    		atualizacaoNecessaria = true;
    	} else if ((rs1.getTimestamp("DATACHEGADANAVIO") == null && rs2.getTimestamp("dt_inicio_layday") != null)
				|| (rs1.getTimestamp("DATACHEGADANAVIO") != null && rs2.getTimestamp("dt_inicio_layday") != null && !rs1.getTimestamp("DATACHEGADANAVIO").equals(rs2.getTimestamp("dt_inicio_layday")))) {
    		atualizacaoNecessaria = true;
    	}else if ((rs1.getTimestamp("DATADESATRACACAO") == null && rs2.getTimestamp("DT_DESATRACACAO") != null)
				|| (rs1.getTimestamp("DATADESATRACACAO") != null && rs2.getTimestamp("DT_DESATRACACAO") != null && !rs1.getTimestamp("DATADESATRACACAO").equals(rs2.getTimestamp("DT_DESATRACACAO")))) {
    		atualizacaoNecessaria = true;
    	}else if ((rs1.getTimestamp("DATAETA") == null && rs2.getTimestamp("dt_inicio_eta") != null)
				|| (rs1.getTimestamp("DATAETA") != null && rs2.getTimestamp("dt_inicio_eta") != null && !rs1.getTimestamp("DATAETA").equals(rs2.getTimestamp("dt_inicio_eta")))) {
    		atualizacaoNecessaria = true;
    	}else if ((rs1.getTimestamp("DATAEMBARQUENAVIO") == null && rs2.getTimestamp("dt_progr_embarque") != null)
				|| (rs1.getTimestamp("DATAEMBARQUENAVIO") != null && rs2.getTimestamp("dt_progr_embarque") != null && !rs1.getTimestamp("DATAEMBARQUENAVIO").equals(rs2.getTimestamp("dt_progr_embarque")))) {
    		atualizacaoNecessaria = true;
    	}else if ((rs1.getTimestamp("DATASAIDANAVIO") == null && rs2.getTimestamp("dt_fim_layday") != null)
				|| (rs1.getTimestamp("DATASAIDANAVIO") != null && rs2.getTimestamp("dt_fim_layday") != null && !rs1.getTimestamp("DATASAIDANAVIO").equals(rs2.getTimestamp("dt_fim_layday")))) {
    		atualizacaoNecessaria = true;
    	}
    	
    	return atualizacaoNecessaria;    	
    }


}
