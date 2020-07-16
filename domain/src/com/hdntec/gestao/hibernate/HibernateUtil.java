package com.hdntec.gestao.hibernate;

import java.io.File;

import javax.persistence.FlushModeType;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

import com.hdntec.gestao.util.PropertiesUtil;


public class HibernateUtil {

    private static final SessionFactory factory;

//    private static final ThreadLocal<Session> sessionThread = new ThreadLocal<Session>();
    private static Session sessionThread = null;

    private static final ThreadLocal<Transaction> transactionThread = new ThreadLocal<Transaction>();
    
    


    static {
        // Bloco estático que inicializa o Hibernate, escreve o stack trace
        // se houver algum problema e relança a exceção
        try {
        	Boolean clienteBD = new Boolean(PropertiesUtil.buscarPropriedade("conectar.banco.cliente"));
        	Configuration configuration = new AnnotationConfiguration();
        	File file = null;
        	//if (clienteBD) {
        		 String who = PropertiesUtil.buscarPropriedade("hibernate.path");
                 file = new File(who);                                  
                 //configuration.configure(file);
                 factory = configuration.configure(file).buildSessionFactory();        		
        	//} else {            
        		//factory = configuration.configure().buildSessionFactory();
        	//}		
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
    }
/*
    public static Session getSession() {
        if (sessionThread.get() == null) {
            Session session = factory.openSession();
            sessionThread.set(session);
        }
        return (Session) sessionThread.get();
    }

    
    public static void closeSession() {
        Session session = (Session) sessionThread.get();
        if (session != null && session.isOpen()) {
            sessionThread.set(null);
            session.close();
        }
    }
*/
    public static Session getSession() {
    	if (sessionThread == null) {
    		sessionThread = factory.openSession();
    		sessionThread.setFlushMode(FlushMode.ALWAYS);    		
    	}
    	return sessionThread;
    }

    public static void closeSession() {
    	if (sessionThread != null && sessionThread.isOpen()) {
    		sessionThread.close();
    	}
    	sessionThread = null;
    }


    
    public static void beginTransaction() {
        Transaction transaction = transactionThread.get();
        if (transaction == null) {
            transaction = getSession().beginTransaction();
            transactionThread.set(transaction);
        }
    }

    public static void commitTransaction() {
        Transaction transaction = transactionThread.get();
        if (transaction != null && !transaction.wasCommitted() && !transaction.wasRolledBack()) {
            transaction.commit();
            //getSession().clear();
            transactionThread.set(null);
        }
    }

    public static void rollbackTransaction() {
        Transaction transaction = transactionThread.get();
        if (transaction != null && !transaction.wasCommitted() && !transaction.wasRolledBack()) {
            transaction.rollback();
            transactionThread.set(null);
        }
    }
    
    public static SessionFactory getSessionFactory() {
    	return factory;
	}
}
