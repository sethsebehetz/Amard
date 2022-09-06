package com.etz.gh.amard.jobs;

import com.etz.gh.amard.dao.AmardDAO;
import com.etz.gh.amard.entities.Monitor;
import com.etz.gh.amard.entities.TestDatabase;
import com.etz.gh.amard.utilities.DBUtils;
import com.etz.gh.amard.utilities.GeneralUtils;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author seth.sebeh
 *
 */
public class TestDatabaseJob implements Job {

    final Logger logger = Logger.getLogger(getClass().getName());

    static {
        PropertyConfigurator.configure("cfg\\log4j.config");
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            JobDataMap dataMap = context.getJobDetail().getJobDataMap();
            Monitor monitor = (Monitor) dataMap.get("NAME");
            logger.info(Thread.currentThread().getName() + " " + monitor.getType() + "::" + monitor.getName() + " :: monitor started");

            double tat = 0;
            Date dt = new java.util.Date();
            String request_time = GeneralUtils.getRequestTime();
            long start = System.currentTimeMillis();
            String message = "An error occured inserting record - database IP " + monitor.getDatabase();
            int err = 9;

            Callable<Integer> dbCallable = () -> {
                Transaction tx = null;
                Integer recordId = null;
                //TestDatabase t = new TestDatabase();

                Session session = null;
                try {
                    if (monitor.getDatabase().equals("172.16.40.9")) {
                        session = DBUtils.get40dot9Session();
                    } else if (monitor.getDatabase().equals("172.16.40.14")) {
                       // session = DBUtils.get40dot14Session();
                    } else if (monitor.getDatabase().equals("172.16.40.15")) {
                       // session = DBUtils.get40dot15Session();
                    } else if (monitor.getDatabase().equals("172.16.40.17")) {
                       // session = DBUtils.get40dot17Session();
                    }
                    //t.setC1("test data_" + GeneralUtils.generateRandomNumber(8));
                    //recordId = (Integer) session.save(t);
                    tx = session.beginTransaction();
                    String sql = "insert into amard_monitor.test(c1) values ('test data_" + GeneralUtils.generateRandomNumber(8) + "')";
                    Query query = session.createNativeQuery(sql);
                    recordId = query.executeUpdate();
                    tx.commit();
                } catch (HibernateException e) {
                    if (tx != null) {
                        tx.rollback();
                    }
                    logger.error("An error occured inserting record ", e);
                    recordId = -1;
                } finally {
                    session.close();
                }
                return recordId;
            };

            Future<Integer> future = Executors.newSingleThreadExecutor().submit(dbCallable);

            Integer recId = -1;
            try {
                recId = future.get(Long.valueOf(monitor.getConnect_timeout()), TimeUnit.MILLISECONDS);
                message = "db insertion " + monitor.getDatabase() + " successful. Took " + (System.currentTimeMillis() - start) + " ms";
            } catch (TimeoutException ex) {
                message = "Database insertion timeout. Took more than " + monitor.getConnect_timeout() + " ms";
                logger.error("Timeout exception");
            } catch (InterruptedException ex) {
                logger.error("Sorry, something wrong!", ex);
            } catch (ExecutionException ex) {
                logger.error("Sorry, something wrong!", ex);
            }

            String response_time = GeneralUtils.getResponseTime();
            tat = System.currentTimeMillis() - start;

            if (recId > 0) {
                err = 0;
            }
            int i = AmardDAO.log(monitor, request_time, response_time, tat, err, "", message, "");
            logger.info(Thread.currentThread().getName() + " " + monitor.getType() + "::" + monitor.getName() + " Record Inserted ID :: " + i);
        } catch (Exception e) {
            logger.error(Thread.currentThread().getName() + " " + "Sorry an error occured", e);
        }
    }
}
