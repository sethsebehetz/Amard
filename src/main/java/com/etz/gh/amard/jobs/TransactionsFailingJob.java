package com.etz.gh.amard.jobs;

import com.etz.gh.amard.dao.AmardDAO;
import com.etz.gh.amard.entities.EvasNode;
import com.etz.gh.amard.entities.FundGateTransaction;
import com.etz.gh.amard.entities.MobileMoney;
import com.etz.gh.amard.entities.Monitor;
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
public class TransactionsFailingJob implements Job {

    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(TransactionsFailingJob.class);

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

            if (monitor.getGroup().equals("MOBILE MONEY") || monitor.getGroup().equals("FUNDGATE") || monitor.getGroup().equals("VASGATE")) {
                //get mm db connection
                Session session = DBUtils.getMobileMoneyLiveSession();

                Date dt = new java.util.Date();
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                String request_time = sdf.format(dt);
                long start = System.currentTimeMillis();

                String sql = monitor.getQuery().replace("<INTERVAL>", monitor.getQuery_interval()).replace("<LIMIT>", monitor.getCount());

                List<MobileMoney> mmList = null;
                List<FundGateTransaction> fgList = null;
                List<EvasNode> vasList = null;
                boolean isSuccessful = true;
                int listSize = 0;
                String message = "";
                logger.info(Thread.currentThread().getName() + " " + monitor.getType() + "::" + monitor.getName() + " :: QUERY TO RUN >> " + sql);
                if (monitor.getGroup().equals("MOBILE MONEY")) {
                    Query query = session.createNativeQuery(sql, MobileMoney.class);
                    mmList = query.getResultList();
                    listSize = mmList.size();
                    //for TF if all failed we raise an errror. test is successful even if any of them was successful
                    //so we search if any of them was successful else we mark test as failed
                    MobileMoney anySuccessful = mmList.stream().filter(t -> t.getRespCode().equals("00")).findFirst().orElse(null);
                    if (anySuccessful == null && listSize > 3) {
                        isSuccessful = false;
                        String references = mmList.stream().map(f -> "'" + f.getReference() + "'").collect(joining(","));
                        String clientCodes = mmList.stream().map(f -> "'" + f.getClientCode() + "'").collect(joining(","));
                        String etzErrorCodes = mmList.stream().map(f -> "'" + f.getRespCode() + "'").collect(joining(","));
                        message = "No successful mobile money transaction found from last " + listSize + " transactions";
                        message += "<br>Transaction References " + references;
                        message += "<br>Client Error Codes " + clientCodes;
                        message += "<br>ETZ Error Codes " + etzErrorCodes;
                        message += "<br>Check if transactions are still failing and take appropriate action";
                        logger.info(Thread.currentThread().getName() + " " + monitor.getType() + "::" + monitor.getName() + " Message : " + message);
                    }
                } else if (monitor.getGroup().equals("FUNDGATE")) {
                    Query query = session.createNativeQuery(sql, FundGateTransaction.class);
                    fgList = query.getResultList();
                    listSize = fgList.size();
                    FundGateTransaction anySuccessful = fgList.stream().filter(t -> t.getRespMessage().contains("0::")).findFirst().orElse(null);
                    if (anySuccessful == null && listSize > 3) {
                        isSuccessful = false;
                        String references = fgList.stream().map(f -> "'" + f.getEtzRef() + "'").collect(joining(","));
                        String errorCodes = fgList.stream().map(f -> "'" + f.getRESPCODE() + "'").collect(joining(","));
                        message = "No successful fundgate transaction found from last " + listSize + " transactions";
                        message += "<br>Transaction References " + references;
                        message += "<br>FG Error Codes " + errorCodes;
                        message += "<br>Check if transactions are still failing and take appropriate action";
                    }
                } else if (monitor.getGroup().equals("VASGATE")) {
                    Query query = session.createNativeQuery(sql, EvasNode.class);
                    vasList = query.getResultList();
                    listSize = vasList.size();
                    EvasNode anySuccessful = vasList.stream().filter(t -> t.getRESPONSE_CODE().equals("0") || t.getRESPONSE_CODE().equals("00")).findFirst().orElse(null);
                    if (anySuccessful == null && listSize > 3) {
                        isSuccessful = false;
                        String references = vasList.stream().map(f -> "'" + f.getCLIENT_REF() + "'").collect(joining(","));
                        String errorCodes = vasList.stream().map(f -> "'" + f.getRESPONSE_CODE() + "'").collect(joining(","));
                        message = "No successful " + monitor.getName() + "transaction found from last " + listSize + " transactions";
                        message += "<br>Transaction References " + references;
                        message += "<br>FG Error Codes " + errorCodes;
                        message += "<br>Check if transactions are still failing and take appropriate action";
                    }
                }
                logger.info(Thread.currentThread().getName() + " " + monitor.getType() + "::" + monitor.getName() + " :: TRANSACTIONS RETRIEVED " + listSize);
                double tat = System.currentTimeMillis() - start;
                logger.info(Thread.currentThread().getName() + " " + monitor.getType() + "::" + monitor.getName() + ":: TAT [SQL RUN] >> " + tat + " ms");

                Date ddt2 = new java.util.Date();
                String response_time = sdf.format(ddt2);

                int err = 9;
                if (isSuccessful) {
                    //TF test succeeded. ie at least one of the txn was successful
                    logger.info(Thread.currentThread().getName() + " " + monitor.getType() + "::" + monitor.getName() + ":: TEST SUCCESSFUL ");
                    err = 0;
                } else {

                    //TF test failed. ie all transactions selected failed
                    logger.info(Thread.currentThread().getName() + " " + monitor.getType() + "::" + monitor.getName() + ":: TEST FAILED ");
                }
                if (listSize > 0) {
                    int i = AmardDAO.log(monitor, request_time, response_time, tat, err, "", message, "");
                    logger.info(Thread.currentThread().getName() + " " + monitor.getType() + "::" + monitor.getName() + " Record Inserted ID :: " + i);
                }
            }
        } catch (Exception e) {
            logger.error(Thread.currentThread().getName() + " " + "Sorry an error occured", e);
        }
    }
}
