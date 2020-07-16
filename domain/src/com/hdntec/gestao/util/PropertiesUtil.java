package com.hdntec.gestao.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

public class PropertiesUtil {
    
    /** Constante do nome do arquivo de properties */
    private static final String RESOURCE_BUNDLE_FILE = "CFlexStockyardConfig.properties";

    /** o arquivo de propriedades */
    private static Properties resources;	
	
    /** o arquivo de propriedades */
    private static Properties resourcesMessage;	
	
    private static ResourceBundle bundleMSG;

    private static ResourceBundle bundlePROP;

    static {
        StringBuffer pathArquivo = new StringBuffer();
        String os = (System.getProperty("os.name"));
        
        FileInputStream is = null;
        try {
    	if (resources == null)
        {
           resources = new Properties();
        }
        
        pathArquivo.append(System.getProperty("user.dir"));
        pathArquivo.append(File.separator);
        
        if (os != null && os.startsWith("Windows")) {
        	pathArquivo.append("\\resources").append(File.separator);
        } else {
        	pathArquivo.append("resources").append(File.separator);
        }
        pathArquivo.append(RESOURCE_BUNDLE_FILE);
        
			is = new FileInputStream(pathArquivo.toString());
			resources.load(is);
			is.close();
		
        }
        catch (IOException e)
        {
            pathArquivo = new StringBuffer(); 
            pathArquivo.append(System.getProperty("user.dir"));
            pathArquivo.append(File.separator);
            if (os != null && os.startsWith("Windows")) {
            	pathArquivo.append("\\src\\main\\resources").append(File.separator);
            } else {
            	pathArquivo.append("src/main/resources").append(File.separator);
            }
            	pathArquivo.append(RESOURCE_BUNDLE_FILE);
            try {
                is = new FileInputStream(pathArquivo.toString());
                resources.load(is);           
                is.close();
                pathArquivo = null;
                is = null;
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            
        }         	
			
			
          if (resourcesMessage == null)
           {
        	   resourcesMessage = new Properties();
           }
          try {

          pathArquivo = new StringBuffer(); 
           pathArquivo.append(System.getProperty("user.dir"));
           pathArquivo.append(File.separator);
            
           if (os != null && os.startsWith("Windows")) {
           	pathArquivo.append("\\resources").append(File.separator);
           } else {
           	pathArquivo.append("resources").append(File.separator);
           }
           
           pathArquivo.append("messages_pt.properties");
           is = new FileInputStream(pathArquivo.toString());
           resourcesMessage.load(is);           
           is.close();
           pathArquivo = null;
           is = null;
        }
        catch (IOException e)
        {
            pathArquivo = new StringBuffer(); 
            pathArquivo.append(System.getProperty("user.dir"));
            pathArquivo.append(File.separator);
            if (os != null && os.startsWith("Windows")) {
            	pathArquivo.append("\\src\\main\\resources").append(File.separator);
            } else {
            	pathArquivo.append("src/main/resources").append(File.separator);
            }            
            pathArquivo.append("messages_pt.properties");
            try {
                is = new FileInputStream(pathArquivo.toString());
                resourcesMessage.load(is);           
                is.close();
                pathArquivo = null;
                is = null;
            } catch (IOException e1 ) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            
            
            
            
            System.err.println("IOException " + e.getMessage());
        }
 }
	
    
    /**
	    * getBundleMessage
	    * @author <a href="mailto:hdn.jesse@cflex.com.br">Jessé</a>
	    * @since 13/02/2009
	    * @see
	    * @param 
	    * @return ResourceBundle
	    */
	    public static ResourceBundle getBundleMessage() {
	    
	        if (bundleMSG == null) {
	            try {

	                bundleMSG = ResourceBundle.getBundle("messages", Locale.getDefault(), Thread.currentThread()
	                                .getContextClassLoader());

	            } catch (MissingResourceException e) {

	                throw new RuntimeException("The 'messages.properties' file was " + "not found in classpath.");

	            }

	        }

	        return bundleMSG;

	    }

	    /**
		    * getBundleMessage
		    * @author <a href="mailto:hdn.jesse@cflex.com.br">Jessé</a>
		    * @since 13/02/2009
		    * @see
		    * @param 
		    * @return ResourceBundle
		    */
		    public static ResourceBundle getBundlePROP() {
		    
		        if (bundlePROP == null) {
		            try {

		            	bundlePROP = ResourceBundle.getBundle("resources/CFlexStockyardConfig");

		            } catch (MissingResourceException e) {

		                throw new RuntimeException("The 'CFlexStockyardConfig.properties' file was " + "not found in classpath.");

		            }

		        }

		        return bundlePROP;

		    }
	    			    
		 /**
	     * getMessage 
	     * @author <a href="mailto:hdn.jesse@cflex.com.br">Jessé</a>
	     * @since 13/02/2009
	     * @see
	     * @param 
	     * @return String
	     */
	    public static String getMessage(String key) {
	        //return getBundleMessage().getString(key);
            return buscarMensagem(key);
	    }
	    
	    /**
	     * getAppMessage 
	     * @author <a href="mailto:hdn.jesse@cflex.com.br">Jessé</a>
	     * @since 13/02/2009
	     * @see
	     * @param 
	     * @return String
	     */
	    public static String getAppMessage(String key) {
	        return getBundlePROP().getString(key);
	    }
	    /**
	     * Metodo que retorna a mensagem com parametros adicionados.
	     * 
	     * @param key
	     * @param listaParam
	     * @return message
	     */
	    public static String getMessage(String key, List<String> listaParam) {
	        String message = "";
	        String mensagemProperties = PropertiesUtil.getMessage(key);

	        String[] arrayMensages = mensagemProperties.split("#P");

	        for (int i = 0; i < arrayMensages.length; i++) {
	            message += arrayMensages[i];
	            if (i < arrayMensages.length - 1) {
	                message += listaParam.get(i);
	            }
	        }

	        return message;
	    }


    /**
     * Busca o conteudo de uma chave do arquivo de propriedades
     * 
     * @param chave
     * @return conteudo da chave
     */
    public static String buscarPropriedade(String chave)
    {
    	String conteudoDaChave = "";        
           if (resources != null) {
        	conteudoDaChave = resources.getProperty(chave);
           }
        return conteudoDaChave;
    }

        /**
     * Busca o conteudo de uma chave do arquivo de propriedades
     *
     * @param chave
     * @return conteudo da chave
     */
    public static String buscarMensagem(String chave)
    {
    	String conteudoDaChave = "";        
        if (resourcesMessage != null) {
     	conteudoDaChave = resourcesMessage.getProperty(chave);
        }
 
    	return conteudoDaChave;    
    }


}
