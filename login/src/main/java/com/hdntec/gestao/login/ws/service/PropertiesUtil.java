package com.hdntec.gestao.login.ws.service;

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
	
    private static Properties resourcesMessages;

    private static ResourceBundle bundleMSG;
	  
    private static ResourceBundle bundlePROP;
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

		            	bundlePROP = ResourceBundle.getBundle("CFlexStockyardConfig");

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
    	String conteudoDaChave;
        FileInputStream is=null;
        try
        {
           if (resources == null)
           {
              resources = new Properties();
           }
           String pathArquivo = System.getProperty("user.dir");
           pathArquivo += File.separator;
           pathArquivo += "resources" + File.separator;
           pathArquivo += RESOURCE_BUNDLE_FILE;
           is = new FileInputStream(pathArquivo);
           resources.load(is);
           conteudoDaChave = resources.getProperty(chave);
           is.close();
        }
        catch (IOException e)
        {
           conteudoDaChave = null;
           System.err.println("IOException " + e.getMessage());
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
    	String conteudoDaChave;
        FileInputStream is=null;
        try
        {
           if (resources == null)
           {
              resources = new Properties();
           }
           String pathArquivo = System.getProperty("user.dir");
           pathArquivo += File.separator;
           pathArquivo += "resources" + File.separator;
           pathArquivo += "messages_pt.properties";
           is = new FileInputStream(pathArquivo);
           resources.load(is);
           conteudoDaChave = resources.getProperty(chave);
           is.close();
        }
        catch (IOException e)
        {
           conteudoDaChave = null;
           System.err.println("IOException " + e.getMessage());
        }
        return conteudoDaChave;
    }


}
