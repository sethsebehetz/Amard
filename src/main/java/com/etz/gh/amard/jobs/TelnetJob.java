package com.etz.gh.amard.jobs;

import com.etz.gh.amard.dao.AmardDAO;
import com.etz.gh.amard.entities.Monitor;
import static com.etz.gh.amard.jobs.TransactionsFailingJob.logger;
import com.etz.gh.amard.utilities.TelnetClient;
import java.util.Date;
import org.apache.log4j.PropertyConfigurator;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author seth.sebeh
 */
public class TelnetJob implements Job {

    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(TelnetJob.class);

    static {
        PropertyConfigurator.configure("cfg\\log4j.config");
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            JobDataMap dataMap = context.getJobDetail().getJobDataMap();
            Monitor monitor = (Monitor) dataMap.get("NAME");
            logger.info(Thread.currentThread().getName() + " " + monitor.getType() + "::" + monitor.getName() + " :: monitor started");
//        logger.info(Thread.currentThread().getName() + " " + monitor);

            Date dt = new java.util.Date();
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
            String request_time = sdf.format(dt);

            String rsp = new TelnetClient().telnet(monitor, monitor.getIp_address(), Integer.valueOf(monitor.getIp_port()), monitor.getConnect_timeout());

            int err = 9;
            double tat = 0;

            if (rsp != null) {
                err = Integer.valueOf(rsp.split("#")[0]);
                tat = Double.valueOf(rsp.split("#")[1]);
            }

            Date ddt2 = new java.util.Date();
            String response_time = sdf.format(ddt2);
            String message = "";
            if (err == 0) {
                logger.info(Thread.currentThread().getName() + " " + monitor.getType() + "::" + monitor.getName() + ":: TEST SUCCESSFUL ");
            } else {
                message = "Telnet to ip " + monitor.getIp_address() + " ON PORT " + monitor.getIp_port() + " FAILED";
                logger.info(Thread.currentThread().getName() + " " + monitor.getType() + "::" + monitor.getName() + " Message : " + message);
                logger.info(Thread.currentThread().getName() + " " + monitor.getType() + "::" + monitor.getName() + ":: TEST FAILED ");
            }

            //int i = AmardDAO.log(request_time, response_time, tat, monitor.getName(), err,"", monitor.getType(), monitor.getGroup(), monitor.getDescription());
            int i = AmardDAO.log(monitor, request_time, response_time, tat, err, "", message, "");
            logger.info(Thread.currentThread().getName() + " " + monitor.getType() + "::" + monitor.getName() + " Record Inserted ID :: " + i);
        } catch (Exception e) {
            logger.error(Thread.currentThread().getName() + " " + "Sorry an error occured", e);
        }
    }
}
