package com.etz.gh.amard.jobs;

import com.etz.gh.amard.dao.AmardDAO;
import com.etz.gh.amard.entities.FundGateTransaction;
import com.etz.gh.amard.entities.MobileMoney;
import com.etz.gh.amard.entities.Monitor;
import static com.etz.gh.amard.jobs.TransactionsFailingJob.logger;
import com.etz.gh.amard.utilities.DBUtils;
import java.util.Date;
import java.util.List;
import static java.util.stream.Collectors.joining;
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
public class AmbiguousErrorJob implements Job {

    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(AmbiguousErrorJob.class);

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

            if (monitor.getGroup().equals("MOBILE MONEY") || monitor.getGroup().equals("FUNDGATE")) {
                //get mm db connection
                Session session = DBUtils.getMobileMoneySession();

                Date dt = new java.util.Date();
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                String request_time = sdf.format(dt);
                long start = System.currentTimeMillis();

                String sql = monitor.getQuery().replace("<INTERVAL>", monitor.getQuery_interval()).replace("<LIMIT>", monitor.getCount());

                List<MobileMoney> mmList = null;
                List<FundGateTransaction> fgList = null;
                int listSize = 0;
                String message = "";
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
                logger.info(Thread.currentThread().getName() + " " + monitor.getType() + "::" + monitor.getName() + " :: TRANSACTIONS RETRIEVED " + listSize);
                double tat = System.currentTimeMillis() - start;
                logger.info(Thread.currentThread().getName() + " " + monitor.getType() + "::" + monitor.getName() + ":: TAT [SQL RUN] >> " + tat + " ms");

                Date ddt2 = new java.util.Date();
                String response_time = sdf.format(ddt2);

                int err = 9;
                if (listSize > 0) {
                    logger.info(Thread.currentThread().getName() + " " + monitor.getType() + "::" + monitor.getName() + ":: TEST FAILED ");
                    String references = mmList.stream().map(f -> "'" + f.getReference() + "'").collect(joining(","));
                    String clientCodes = mmList.stream().map(f -> "'" + f.getClientCode() + "'").collect(joining(","));
                    String etzErrorCodes = mmList.stream().map(f -> "'" + f.getRespCode()+ "'").collect(joining(","));
                    message = "Transaction status unknown. Found " + listSize + " transactions";
                    message += "<br>Transaction References " + references;
                    message += "<br>Client Error Codes " + clientCodes;
                    message += "<br>ETZ Error Codes " + etzErrorCodes;
                    message += "<br>Confirm real status of transactions from Service Provider Report";
                    logger.info(Thread.currentThread().getName() + " " + monitor.getType() + "::" + monitor.getName() + " Message : " + message);
                } else {
                    err = 0;
                    logger.info(Thread.currentThread().getName() + " " + monitor.getType() + "::" + monitor.getName() + ":: TEST SUCCESSFUL ");
                    message = "No " + monitor.getName() + " transaction found in the last " + monitor.getQuery_interval() + " minutes";
                }
                int i = AmardDAO.log(monitor, request_time, response_time, tat, err, "", message, "");
                logger.info(Thread.currentThread().getName() + " " + monitor.getType() + "::" + monitor.getName() + " Record Inserted ID :: " + i);
            }
        } catch (Exception e) {
            logger.error(Thread.currentThread().getName() + " " + "Sorry an error occured", e);
        }
    }
}
