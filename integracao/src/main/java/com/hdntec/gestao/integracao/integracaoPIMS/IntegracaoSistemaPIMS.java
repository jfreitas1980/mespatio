package com.hdntec.gestao.integracao.integracaoPIMS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.hdntec.gestao.exceptions.ErroSistemicoException;
import com.hdntec.gestao.util.PropertiesUtil;

public class IntegracaoSistemaPIMS {

    private Connection conexaoIntegracao;    
    private IntegracaoSistemaPIMS instance;
    /**
     * 
     * */
    private Boolean offLine = Boolean.FALSE;
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("IntegracaoPIMS");
    
    public IntegracaoSistemaPIMS() {
    	//instance = new IntegracaoSistemaPIMS();  	
    	this.offLine = new Boolean(PropertiesUtil.buscarPropriedade("conexao.pims.offline"));    
    }
    
    
    @Deprecated
    private IntegracaoSistemaPIMS getInstance() {
    	instance = new IntegracaoSistemaPIMS();  	
    	instance.offLine = new Boolean(PropertiesUtil.buscarPropriedade("conexao.pims.offline"));
    	/*instance = null;
        	if (instance == null) {*
        		instance = new IntegracaoSistemaPIMS();
        		instance.offLine = new Boolean(PropertiesUtil.buscarPropriedade("conexao.pims.offline"));  
        	}*/
        return instance;
    }
    
    public static void main(String[] args) {
    	try {
    		IntegracaoSistemaPIMS obj= new IntegracaoSistemaPIMS();
    		obj.recuperarQuantidadePIMS(new Date("16/10/2009 12:00"), new Date("16/10/2009 14:30"),"WIT_798A-OE");
		} catch (ErroSistemicoException e) {
			// TODO Auto-generated catch block
			logger.error(e);
		}
    }
    
    private void conectaPIMS() throws RuntimeException {
        try
        {
        //offLine = Boolean.TRUE;
        if (!offLine) {
        	// Este é um dos meios para registrar um driver
          Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");

          // Registrado o driver, vamos estabelecer uma conexão
          conexaoIntegracao = DriverManager.getConnection(PropertiesUtil.buscarPropriedade("conexao.pims.url"),PropertiesUtil.buscarPropriedade("conexao.pims.user"),PropertiesUtil.buscarPropriedade("conexao.pims.password"));
        } else {
        	Class.forName("oracle.jdbc.driver.OracleDriver");
            // Registrado o driver, vamos estabelecer uma conexão
            conexaoIntegracao = DriverManager.getConnection(PropertiesUtil.buscarPropriedade("conexao.pims.offline.url"),PropertiesUtil.buscarPropriedade("conexao.pims.offline.user"),PropertiesUtil.buscarPropriedade("conexao.pims.offline.passwd"));
        	//conexaoIntegracao = DriverManager.getConnection("jdbc:oracle:thin:@192.168.20.125:1521:xe","MESPATIO_BRUNO","cflex");       
        }
        }
        catch (ClassNotFoundException e)
        {
        	logger.error(e);
        	throw new RuntimeException(e);
        }
        catch(SQLException e)
        {
        	logger.error(e);
        	throw new RuntimeException(e);
        }
    }

    private void desconectaPIMS() throws ErroSistemicoException{ 
        try
        {        
          if (conexaoIntegracao != null) {
        	  conexaoIntegracao.close();
        	  conexaoIntegracao = null;
          }
        }
        catch (Exception e)
        {
        	logger.error(e);
        	throw new ErroSistemicoException(e);
        }        
    }
    
    public Double  recuperarQuantidadePIMS(Date inicio,Date fim, String tag) throws ErroSistemicoException{        
        Double result = null;
        Double iniValue= null;
        Double fimValue = null;
    	Date iniAtual = new Date(inicio.getTime());
    	Date fimAtual = new Date(fim.getTime());;
    	try {
          conectaPIMS();    	  
          /**
           * TODO RECUPERANDO DE 4 EM 4 HORAS EM 1 ANO
           *  4^04^14^2  
           * 
           * */
          //int fator = 1;
          //int horas = 4;
          //while (iniValue == null && fator < 8 ) { 
        	   iniValue = loadPIMSUniqueResult(tag,null,inicio,48);
        	   /*if (iniValue == null) {
        		   fator++;
        		   Double mult = Math.pow(4,fator);
        		   horas =  mult.intValue();
        		   iniAtual = new Date(inicio.getTime() - horas * 2);        		   
        	   }*/
         // }	
         // fator = 1;
          //horas = 4;
         // while (fimValue == null  && fator < 8) {
        	    fimValue =  loadPIMSUniqueResult(tag,null,fim,48);
        	    /*if (fimValue == null) {
        	    	fator++;
         		    Double mult = Math.pow(4,fator);
         		    horas =  mult.intValue();
         		    fimAtual = new Date(inicio.getTime() - horas);    
         		    fator++;
         	   }
          } */         
          if (fimValue != null && iniValue != null) { 
        		  if ( fimValue < iniValue) {
        			  Double intermidiario = calculatePIMSValue(tag, new Date (iniAtual.getTime() - (1000*60*60*48)), fimAtual, 48);
        			  if (intermidiario != null) {
        				  result = new Double(intermidiario - iniValue + fimValue);
        			  }	  
        		  } else {
        			  result = new Double(fimValue - iniValue);
        		  }	  
         } 
    	}catch (Exception ex)
        {
    		logger.error(ex);
    		throw new ErroSistemicoException(ex);
        } finally {
        	desconectaPIMS();
        	instance = null;
        }
		return result;
    }
    
    private Double loadPIMSUniqueResult(String tagName,Date initTime,Date endTime,int qtdHours) throws ErroSistemicoException{ 
    	Double value = null;
    	ResultSet rs  = null;
    	try {    		
    		if (initTime != null) {
    			System.out.println("data inicio -loadPIMSUniqueResult" + initTime.toString());
    			logger.info("data inicio" + initTime.toString());
    		}	
    		if (endTime != null) {
    			System.out.println("data fim - loadPIMSUniqueResult" + endTime.toString());
    			logger.info("data fim" + endTime.toString());
    		}	
    		rs = loadPIMSData(tagName, initTime, endTime, qtdHours,true);
    		if (rs.next())
    		{
    			System.out.println(tagName);
    			System.out.println("Trend Time-" + rs.getString("IP_trend_Time"));                    
    			System.out.println("Trend Value-" + rs.getString("IP_trend_Value"));
            
    			logger.info(tagName);
    			logger.info("Trend Time-" + rs.getString("IP_trend_Time"));                    
    			logger.info("Trend Value-" + rs.getString("IP_trend_Value"));
                
                value = new Double(rs.getString("IP_trend_Value"));
          }
        
    	} 
        catch (Exception ex)
        {
        	throw new ErroSistemicoException(ex);
        } finally {
        	
        	if (rs != null) {
        		try {
        				rs.close();
        				rs.getStatement().close();        		
        				rs = null;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println(e);
					logger.error(e);
					//throw new ErroSistemicoException(e);
				}
        	}        	
        }
		return value;
    }     
    private Double calculatePIMSValue(String tagName,Date initTime,Date endTime,int qtdHours) throws ErroSistemicoException{ 
    	Double value = null;
    	ResultSet rs  = null;
    	try {
    		rs = loadPIMSData(tagName, initTime, endTime, qtdHours,false);
    		 
    		System.out.println("Procurando valor intermediario");
    		System.out.println(tagName);
    		while (rs.next())
    		{    			
    			
    			System.out.println(tagName);
    			System.out.println("Trend Time Intermediario-" + rs.getString("IP_trend_Time"));                    
    			System.out.println("Trend Value Intermediario-" + rs.getString("IP_trend_Value"));
    			logger.info(tagName);
    			logger.info("Trend Time Intermediario-" + rs.getString("IP_trend_Time"));                    
    			logger.info("Trend Value Intermediario-" + rs.getString("IP_trend_Value"));    			
                Double currValue =  new Double(rs.getString("IP_trend_Value"));                
                if (value == null) {
                	value = currValue;
                }
                if 	(currValue < value ) {                	
                	System.out.println("Valor Intermediario encontrado-" + value);
                	logger.info("Valor Intermediario-" + value);
                	logger.info("Valor Intermediario-" + value);                      	
                	break;	
                }
               value = currValue; 
            }
        
    	} 
        catch (Exception ex)
        {
        	logger.error(ex);
        	throw new ErroSistemicoException(ex);
        } finally {
        	if (rs != null) {
        		try {
    				rs.close();
    				rs.getStatement().close();        		
    				rs = null;        			
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					logger.error(e);					
					//throw new ErroSistemicoException(e);
				}
        	}        	
        }
		return value;
    }


    private  ResultSet loadPIMSData(String tagName,Date initTime,Date endTime,int qtdHours,Boolean orderDesc) throws ErroSistemicoException{        
    	PreparedStatement stm = null;
        ResultSet rs = null;
        StringBuilder sql = new StringBuilder();
        String formato ="dd-MMM-yy HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(formato,Locale.US);
        System.out.println(sdf.format(new Date(endTime.getTime())));
        try {            
            
        	if (initTime != null){
        		System.out.println("data param inicio" + initTime.toString());
        		logger.info("data param inicio" + initTime.toString());
        	}	
        	if (endTime != null){
                System.out.println("data param fim" + endTime.toString());       	
        		logger.info("data param fim" + endTime.toString());
        	}	
        	
        	if (!offLine) {
	        	sql.append("Select IP_trend_Time, cast( IP_trend_Value as Char Using 'F15.3') as IP_trend_Value");
	            sql.append(" From  IP_analogDef ");
	            sql.append(" Where name = ? ");
	            sql.append(" and IP_trend_Time > ?  and IP_trend_Time <= ? ");
	            if (orderDesc) {
	            	sql.append(" order by IP_trend_Time desc");
	            } else {
	            	sql.append(" order by IP_trend_Time");
	            }
	            stm = conexaoIntegracao.prepareStatement(sql.toString());
	            stm.setString(1, tagName);            
	            if (initTime == null) {
	            	String param = sdf.format(new Date(endTime.getTime() - (1000*60*60*qtdHours))).toLowerCase();
	            	stm.setString(2,param);
	            } else {
	            	stm.setString(2,sdf.format(new Date(initTime.getTime())).toLowerCase());
	            }
	            stm.setString(3,sdf.format(endTime).toLowerCase());
	            rs = stm.executeQuery();
            }
            else {
	            sql.append("Select IP_trend_Time, IP_trend_Value ");
	            sql.append(" From  IP_analogDef ");
	            sql.append(" Where name = ? ");
	            sql.append(" and IP_trend_Time > ?  and IP_trend_Time <= ? ");
	            if (orderDesc) {
	            	sql.append(" order by IP_trend_Time desc");
	            } else {
	            	sql.append(" order by IP_trend_Time");
	            }
	            stm = conexaoIntegracao.prepareStatement(sql.toString());
	            stm.setString(1, tagName);            
	            if (initTime == null) {
	            	Date tmp = new Date(endTime.getTime() - (1000*60*60*qtdHours));
	            	java.sql.Timestamp param = new java.sql.Timestamp(tmp.getTime());
	            	stm.setTimestamp(2,param);	            		            	
	            	logger.info("data param tratado inicio" + param.toString());                    	    			
	            } else {
	            	stm.setTimestamp(2,new java.sql.Timestamp(initTime.getTime()));
	            	logger.info("data param tratado inicio" + initTime.toString());
	            }
	            stm.setTimestamp(3,new java.sql.Timestamp(endTime.getTime()));	            
	            logger.info("data param tratado fim" + endTime.toString());
	            rs = stm.executeQuery();
            }    
        } 
        catch (Exception ex)
        {
        	logger.error(ex);
        	throw new ErroSistemicoException(ex);
        }   
		return rs;
    }
}
