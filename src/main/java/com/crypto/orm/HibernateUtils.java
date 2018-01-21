package com.crypto.orm;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class HibernateUtils {

    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(HibernateUtils.class);

    /**
     * Hibernate connection session factory builder
     */
    private static final SessionFactory sessionFactory = buildSessionFactory();

    /**
     * Build SessionFactory using hibernate.properties file
     * @return
     */
    private static SessionFactory buildSessionFactory() {
        try {
            Properties props = new Properties();
            props.load(HibernateUtils.class.getClassLoader().getResourceAsStream("hibernate.properties"));

            return new Configuration().mergeProperties(props).configure().buildSessionFactory();
        } catch (Throwable ex) {
            logger.error("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Close caches and connection pools
     */
    public static void shutdown() {
        getSessionFactory().close();
    }
}
