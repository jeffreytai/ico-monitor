package com.crypto.utils;

import com.crypto.orm.HibernateUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Query;
import java.util.List;
import java.util.Map;

public class DbUtils {

    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(DbUtils.class);

    /**
     * Given a list of entities, save them to the database
     * @param entities
     */
    public static void saveEntities(List<? extends Object> entities) {
        Session session = HibernateUtils.getSessionFactory().getCurrentSession();

        try {
            session.getTransaction().begin();
            entities.forEach(entity -> session.save(entity));
            session.getTransaction().commit();
        } catch (Exception ex) {
            session.getTransaction().rollback();
            ex.printStackTrace();
        }

        session.close();
    }

    /**
     * Given an entity, save it to the database
     * @param entity
     */
    public static void saveEntity(Object entity) {
        Session session = HibernateUtils.getSessionFactory().getCurrentSession();

        try {
            session.getTransaction().begin();
            session.save(entity);
            session.getTransaction().commit();
        } catch (Exception ex) {
            session.getTransaction().rollback();
            ex.printStackTrace();
        }

        session.close();
    }

    /**
     * Run a query that expects to only have one result
     * @param query
     * @return
     */
    public static Object runSingularResultQuery(String query) {
        Object result = null;
        Session session = HibernateUtils.getSessionFactory().getCurrentSession();

        try {
            session.getTransaction().begin();
            Query q = session.createQuery(query);
            result = q.getSingleResult();
            session.getTransaction().commit();
        } catch (Exception ex) {
            session.getTransaction().rollback();
            logger.debug("No result found for query {}", query);
        }

        session.close();
        return result;
    }

    /**
     * Run a query that expects to only have one result
     * @param query
     * @return
     */
    public static Object runSingularResultQuery(String query, Map<Object, Object> bindedParameters) {
        Object result = null;
        Session session = HibernateUtils.getSessionFactory().getCurrentSession();

        try {
            session.getTransaction().begin();

            Query bindedQuery = session.createQuery(query);
            for (Map.Entry<Object, Object> entry : bindedParameters.entrySet()) {
                bindedQuery.setParameter(entry.getKey().toString(), entry.getValue());
            }

            result = bindedQuery.getSingleResult();
            session.getTransaction().commit();
        } catch (Exception ex) {
            session.getTransaction().rollback();
            logger.debug("No result found for query {}", query);
        }

        session.close();
        return result;
    }

    /**
     * Run a query that expects to have multiple results
     * @param query
     * @return
     */
    public static List<? extends Object> runMultipleResultQuery(String query) {
        List<? extends Object> result = null;
        Session session = HibernateUtils.getSessionFactory().getCurrentSession();

        try {
            session.getTransaction().begin();

            Query q = session.createQuery(query);

            result = q.getResultList();
            session.getTransaction().commit();
        } catch (Exception ex) {
            session.getTransaction().rollback();
            ex.printStackTrace();
        }

        session.close();
        return result;
    }

    /**
     * Run a query that expects to have multiple results and
     * substitute binded parameters in the query
     * @param query
     * @param bindedParameters
     * @return
     */
    public static List<? extends Object> runMultipleResultQuery(String query, Map<Object, Object> bindedParameters) {
        List<? extends Object> result = null;
        Session session = HibernateUtils.getSessionFactory().getCurrentSession();

        try {
            session.getTransaction().begin();

            Query bindedQuery = session.createQuery(query);
            for (Map.Entry<Object, Object> entry : bindedParameters.entrySet()) {
                bindedQuery.setParameter(entry.getKey().toString(), entry.getValue());
            }

            result = bindedQuery.getResultList();
            session.getTransaction().commit();
        } catch (Exception ex) {
            session.getTransaction().rollback();
            ex.printStackTrace();
        }

        session.close();
        return result;
    }

}
