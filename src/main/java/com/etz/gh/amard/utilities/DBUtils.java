package com.etz.gh.amard.utilities;

import org.apache.log4j.PropertyConfigurator;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author seth.sebeh
 */
public class DBUtils {

    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(DBUtils.class);

    static {
        PropertyConfigurator.configure("cfg\\log4j.config");
    }

    private final static SessionFactory monitorSessionFactory;
    private static SessionFactory mobileMoneySessionFactory;
    private static SessionFactory mobileMoneyLiveSessionFactory;
    private static SessionFactory tmcSessionFactory;
    private static SessionFactory tmcLiveSessionFactory;
    private static SessionFactory gmoneySessionFactory;
    private static SessionFactory mobileDBSessionFactory;
    private static SessionFactory gcbecarddbSessionFactory;

    public static void main(String[] args) {
        System.out.println("MONITOR SESSION:: " + getMonitorSession());
    }

    static {
        try {
            monitorSessionFactory = new Configuration().configure("monitor.cfg.xml").buildSessionFactory();
            if (NetworkInterfaces.searchIPAddress("172.16.30.10")) {
                mobileDBSessionFactory = new Configuration().configure("mobiledb.cfg.xml").buildSessionFactory();
                tmcSessionFactory = new Configuration().configure("tmc.cfg.xml").buildSessionFactory();
                tmcLiveSessionFactory = new Configuration().configure("tmc_live.cfg.xml").buildSessionFactory();
                gmoneySessionFactory = new Configuration().configure("gmoney.cfg.xml").buildSessionFactory();
                mobileMoneySessionFactory = new Configuration().configure("mobilemoney.cfg.xml").buildSessionFactory();
                mobileMoneyLiveSessionFactory = new Configuration().configure("mobilemoney_live.cfg.xml").buildSessionFactory();
            } else if (NetworkInterfaces.searchIPAddress("172.16.30.9") || NetworkInterfaces.searchIPAddress("10.0.1.42") || NetworkInterfaces.searchIPAddress("172.16.30.14") || NetworkInterfaces.searchIPAddress("172.16.30.4") ) {
               gcbecarddbSessionFactory = new Configuration().configure("gcbecarddb.cfg.xml").buildSessionFactory();
               mobileDBSessionFactory = new Configuration().configure("mobiledb.cfg.xml").buildSessionFactory();
            }
        } catch (HibernateException ex) {
            logger.error("Failed to create sessionFactory object.", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getMonitorSession() {
        return monitorSessionFactory.openSession();
    }

    public static Session getMobileMoneySession() {
        return mobileMoneySessionFactory.openSession();
    }

    public static Session getMobileMoneyLiveSession() {
        return mobileMoneyLiveSessionFactory.openSession();
    }

    public static Session getTmcLiveSession() {
        return tmcLiveSessionFactory.openSession();
    }

    public static Session getTmcSession() {
        return tmcSessionFactory.openSession();
    }

    public static Session get40dot9Session() {
        return mobileMoneySessionFactory.openSession();
    }

    public static Session getGmoneySession() {
        return gmoneySessionFactory.openSession();
    }

    public static Session getMobileDBSession() {
        return mobileDBSessionFactory.openSession();
    }

    public static Session getGcbecarddbSessionFactory() {
        return gcbecarddbSessionFactory.openSession();
    }

}
