package com.hdntec.gestao.hibernate;

import java.io.File;

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class ApagaBanco {

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            // Lê o hibernate.cfg.xml
            File file = new File("./hibernate.cfg.xml");
            Configuration configuration = new AnnotationConfiguration();
            configuration.configure(file);
            // Cria as DDLs
            SchemaExport se = new SchemaExport(configuration);
            se.drop(true, true);
            // se.create(true, true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
