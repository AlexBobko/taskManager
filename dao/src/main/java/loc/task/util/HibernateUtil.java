/*
 * Copyright (c) 2012 by VeriFone, Inc.
 * All Rights Reserved.
 *
 * THIS FILE CONTAINS PROPRIETARY AND CONFIDENTIAL INFORMATION
 * AND REMAINS THE UNPUBLISHED PROPERTY OF VERIFONE, INC.
 *
 * Use, disclosure, or reproduction is prohibited
 * without prior written approval from VeriFone, Inc.
 */
package loc.task.util;

//import net.sf.ehcache.config.Configuration;

import lombok.extern.log4j.Log4j;
import org.hibernate.CacheMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.stat.Statistics;

@Log4j
public class HibernateUtil {
    private static HibernateUtil util = null;
    private SessionFactory sessionFactory = null;
    private Statistics stats = null;

    private final ThreadLocal sessions = new ThreadLocal();

    private HibernateUtil() {
        try {
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml").setNamingStrategy(new CustomNamingStrategy());
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            System.out.println("Hibernate sessionFactory created");
            stats = sessionFactory.getStatistics();
            System.out.println("Stats enabled=" + stats.isStatisticsEnabled());
            stats.setStatisticsEnabled(true);
            System.out.println("Stats enabled=" + stats.isStatisticsEnabled());

        } catch (Throwable ex) {
            log.error("Initial SessionFactory creation failed." + ex);
            System.exit(0);
        }
    }

    public Session getSession() {
        Session session = (Session) sessions.get();
        if (session == null) {
            session = sessionFactory.openSession();
            //TODO ?? CacheMode.GET где прописать по умолчанию? конфиг
//            session.setCacheMode(CacheMode.GET);
            session.setCacheMode(CacheMode.NORMAL);
            sessions.set(session);
            System.out.println("*****NEW Session OPEN****");
            printStats(1);
        }
        return session;
    }

    public static synchronized HibernateUtil getHibernateUtil() {
        if (util == null) {
            util = new HibernateUtil();
        }
        return util;
    }

    public void removeSession() {
        sessions.remove();
    }

    public void printStats(int i) {
        System.out.println("***** " + i + " *****");
        System.out.println("Fetch Count=" + stats.getEntityFetchCount());
        System.out.println("Second Level Hit Count=" + stats.getSecondLevelCacheHitCount());
        System.out.println("Second Level Miss Count=" + stats.getSecondLevelCacheMissCount());
        System.out.println("Second Level Cache Put Count=" + stats.getSecondLevelCachePutCount());
    }
}
