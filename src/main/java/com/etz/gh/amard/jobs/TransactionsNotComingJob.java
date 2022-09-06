package com.etz.gh.amard.jobs;

import com.etz.gh.amard.dao.AmardDAO;
import com.etz.gh.amard.entities.FundGateTransaction;
import com.etz.gh.amard.entities.MobileMoney;
import com.etz.gh.amard.entities.Monitor;
import com.etz.gh.amard.utilities.DBUtils;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author seth.sebeh todos decision for bridge should we set payload in table
 * or empty in the class? log request header sent check http_responsecode error
 * values for each difference monitor name to determine/set vaues for error
 */
public class TransactionsNotComingJob implements Job {

    final static Logger logger = Logger.getLogger(TransactionsNotComingJob.class);

    static {
        PropertyConfigurator.configure("cfg\\log4j.config");
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            JobDataMap dataMap = context.getJobDetail().getJobDataMap();
            Monitor monitor = (Monitor) dataMap.get("NAME");
            logger.info(Thread.currentThread().getName() + " " + monitor.getType() + "::" + monitor.getName() + " :: monitor started");
            //logger.info(Thread.currentThread().getName() + " " + "monitor started >> THREAD NAME >>" + Thread.currentThread().getName() );
            //logger.info(Thread.currentThread().getName() + " " + monitor);

            int listSize = 0;
            String message = "";
            Date dt = new java.util.Date();
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
            String request_time = sdf.format(dt);
            long start = System.currentTimeMillis();

            String sql = monitor.getQuery().replace("<INTERVAL>", monitor.getQuery_interval()).replace("<LIMIT>", monitor.getCount());
            String sql2 = monitor.getQuery().replace("<INTERVAL>", monitor.getQuery_interval()).replace("<LIMIT>", monitor.getCount());

            if (monitor.getGroup().equals("MOBILE MONEY") || monitor.getGroup().equals("FUNDGATE") || monitor.getGroup().equals("VASGATE")) {
                //get mm db connection
                Session session = DBUtils.getMobileMoneyLiveSession();

                List<MobileMoney> mmList = null;
                List<FundGateTransaction> fgList = null;
//                List<VasGateJob> = null;

                logger.info(Thread.currentThread().getName() + " " + monitor.getType() + "::" + monitor.getName() + " :: QUERY TO RUN >> " + sql);
                if (monitor.getGroup().equals("MOBILE MONEY")) {
                    Query query = session.createNativeQuery(sql, MobileMoney.class);
                    mmList = query.getResultList();
                    listSize = mmList.size();
                } else if (monitor.getGroup().equals("FUNDGATE")) {
                    Query query = session.createNativeQuery(sql, FundGateTransaction.class);
                    fgList = query.getResultList();
                    listSize = fgList.size();
                }

            } else if (monitor.getGroup().equals("G-MONEY")) {
                //get gmoney db connection
                Session session = DBUtils.getGmoneySession();
                Query query = session.createNativeQuery(sql);
                List<Object[]> result = query.getResultList();
                listSize = result.size();
            }

            logger.info(Thread.currentThread().getName() + " " + monitor.getType() + "::" + monitor.getName() + " :: TRANSACTIONS RETRIEVED " + listSize);
            double tat = System.currentTimeMillis() - start;
            logger.info(Thread.currentThread().getName() + " " + monitor.getType() + "::" + monitor.getName() + ":: TAT [SQL RUN] >> " + tat + " ms");

            Date ddt2 = new java.util.Date();
            String response_time = sdf.format(ddt2);

            int err = 9;
            if (listSize > 0) {
                err = 0;
                logger.info(Thread.currentThread().getName() + " " + monitor.getType() + "::" + monitor.getName() + ":: TEST SUCCESSFUL ");
            } else {
                logger.info(Thread.currentThread().getName() + " " + monitor.getType() + "::" + monitor.getName() + ":: TEST FAILED ");
                message = "No " + monitor.getName() + " transaction found in the last " + monitor.getQuery_interval() + " minutes";
                logger.info(Thread.currentThread().getName() + " " + monitor.getType() + "::" + monitor.getName() + " Message : " + message);
            }

            int i = AmardDAO.log(monitor, request_time, response_time, tat, err, "", message, "");
            logger.info(Thread.currentThread().getName() + " " + monitor.getType() + "::" + monitor.getName() + " Record Inserted ID :: " + i);

        } catch (Exception e) {
            logger.error(Thread.currentThread().getName() + " " + "Sorry an error occured", e);
        }
    }
}
