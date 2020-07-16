package com.hdntec.gestao.hibernate;


public class GeraBanco {

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            // Lê o hibernate.cfg.xml
        	HibernateUtil.rollbackTransaction();

        	//File file = new File("C:\\trabalho\\Projetos\\CFlex\\SAMARCO\\versaoC\\dssStockYardDomain\\resource\\hibernate.cfg.xml");
           // Configuration configuration = new AnnotationConfiguration();
           // configuration.configure(file);
            // Cria as DDLs
            //SchemaExport se = new SchemaExport(configuration);
            // se.drop(true, true);
            //se.create(true, true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
