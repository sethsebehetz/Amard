//package com.etz.gh.amard.jobs;
//
//import com.etz.gh.amard.dao.AmardDAO;
//import com.etz.gh.amard.entities.MobileMoney;
//import com.etz.gh.amard.entities.Monitor;
//import com.etz.gh.amard.utilities.DBUtils;
//import com.etz.gh.amard.utilities.SuperHttpClient;
//import java.util.Date;
//import java.util.List;
//import org.apache.log4j.PropertyConfigurator;
//import org.hibernate.Session;
//import org.hibernate.query.Query;
//import org.quartz.Job;
//import org.quartz.JobDataMap;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//
///**
// *
// * @author seth.sebeh todos decision for bridge should we set payload in table
// * or empty in the class? log request header sent check http_responsecode error
// * values for each difference monitor name to determine/set vaues for error
// */
//public class TransactionsFailingJob1 implements Job {
//    
//    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(TransactionsFailingJob1.class);
//
//    static {
//        PropertyConfigurator.configure("cfg\\log4j.config");
//    }
//
//    @Override
//    public void execute(JobExecutionContext context) throws JobExecutionException {
//        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
//        Monitor monitor = (Monitor) dataMap.get("NAME");
//        logger.info(Thread.currentThread().getName() + " " + monitor.getType() + "::" + monitor.getName() + " :: monitor started");
//        //logger.info(Thread.currentThread().getName() + " " + "monitor started >> THREAD NAME >>" + Thread.currentThread().getName() );
//        logger.info(Thread.currentThread().getName() + " " + monitor);
//
//        if (monitor.getGroup().equals("MOBILE MONEY")) {
//            //get mm db connection
//            Session session = DBUtils.getMobileMoneySession();
//
//            Date dt = new java.util.Date();
//            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
//            String request_time = sdf.format(dt);
//            long start = System.currentTimeMillis();
//
//            String sql = monitor.getQuery().replace("<INTERVAL>", monitor.getQuery_interval()).replace("<LIMIT>", monitor.getCount());
//
//            logger.info(Thread.currentThread().getName() + " " + monitor.getType() + "::" + monitor.getName() + " :: QUERY TO RUN >> " + sql);
//            Query query = session.createNativeQuery(sql, MobileMoney.class);
//            List<MobileMoney> mmList = query.getResultList();
//            logger.info(Thread.currentThread().getName() + " " + monitor.getType() + "::" + monitor.getName() + " :: TRANSACTIONS RETRIEVED " + mmList.size());
//            double tat = System.currentTimeMillis() - start;
//            logger.info(Thread.currentThread().getName() + " " + monitor.getType() + "::" + monitor.getName() + ":: TAT [SQL RUN] >> " + tat + " ms");
//
//            Date ddt2 = new java.util.Date();
//            String response_time = sdf.format(ddt2);
//
//            //for TF if all failed we raise an errror. test is successful even if any of them was successful
//            //so we search if any of them was successful else we mark test as failed
//            MobileMoney anySuccessful = mmList.stream().filter(t -> t.getRespCode().equals("00")).findFirst().orElse(null);
//
//            int err = 9;
//            if (anySuccessful == null) {
//                //TF test failed. ie all transactions selected failed
//                logger.info(Thread.currentThread().getName() + " " + monitor.getType() + "::" + monitor.getName() + ":: TEST FAILED ");
//                //int i = AmardDAO.log(request_time, response_time, tat, monitor.getName(), err, "", monitor.getType(), monitor.getGroup(), monitor.getDescription());               
//            } else {
//                //TF test succeeded. ie at least one of the txn was successful
//                logger.info(Thread.currentThread().getName() + " " + monitor.getType() + "::" + monitor.getName() + ":: TEST SUCCESSFUL ");
//                err = 0;
//                //int i = AmardDAO.log(request_time, response_time, tat, monitor.getName(), err,"", monitor.getType(), monitor.getGroup(), monitor.getDescription());
//                //int i = AmardDAO.log(monitor.getId(), request_time, response_time, tat, err, "");               
//            }
//            int i = AmardDAO.log(monitor, request_time, response_time, tat, err, "","");
//            logger.info(Thread.currentThread().getName() + " " + monitor.getType() + "::" + monitor.getName() + " Record Inserted ID :: " + i);
//        }
//
//    }
//}
