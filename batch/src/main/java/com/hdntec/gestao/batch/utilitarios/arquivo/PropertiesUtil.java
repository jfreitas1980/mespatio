package com.hdntec.gestao.batch.utilitarios.arquivo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {

    /** Constante do nome do arquivo de properties */
    private static final String RESOURCE_BUNDLE_FILE = "IntegracaoSistemaBatch.properties";

    /** o arquivo de propriedades */
    private static Properties resources;

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
          pathArquivo += File.separator + RESOURCE_BUNDLE_FILE;
          is = new FileInputStream(pathArquivo);
          resources.load(is);
          conteudoDaChave = resources.getProperty(chave);
          is.close();
       } catch (IOException e)
       {
          conteudoDaChave = null;
          System.err.println("IOException " + e.getMessage());
       }
       return conteudoDaChave;
    }

}
