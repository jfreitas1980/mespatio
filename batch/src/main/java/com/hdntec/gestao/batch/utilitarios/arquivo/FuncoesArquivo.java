package com.hdntec.gestao.batch.utilitarios.arquivo;

import java.io.FileInputStream;
import java.util.Properties;

public class FuncoesArquivo {

    private static final String ARQUIVO_PROPERTIES = "IntegracaoSistemaBatch.properties";

    public static String lePropriedade(String chave) {
        try {

            String dir = System.getProperty("user.dir");

            Properties applicationProps = new Properties();
            String path = dir + "/" + ARQUIVO_PROPERTIES;

            FileInputStream in = new FileInputStream(path);
            applicationProps.load(in);
            in.close();

            return applicationProps.getProperty(chave);

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
}
