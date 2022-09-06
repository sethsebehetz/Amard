package com.etz.gh.amard.jobs;

import com.etz.gh.MGClient;
import com.etz.gh.amard.dao.AmardDAO;
import com.etz.gh.amard.entities.Monitor;
import com.etz.gh.amard.momo.mtn.MTNClient;
import com.etz.gh.amard.utilities.Config;
import com.etz.gh.amard.utilities.GeneralUtils;
import com.etz.gh.amard.utilities.SuperHttpClient;
import com.etz.gh.as.AutoSwitch;
import com.etz.vasgate.dao.impl.VasgateDAOImpl;
import org.apache.log4j.PropertyConfigurator;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author seth.sebeh
 *
 */
public class MobileGateJob implements Job {

    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(MobileGateJob.class);

    static {
        PropertyConfigurator.configure("cfg\\log4j.config");
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            JobDataMap dataMap = context.getJobDetail().getJobDataMap();
            Monitor monitor = (Monitor) dataMap.get("NAME");
            logger.info(Thread.currentThread().getName() + " " + monitor.getType() + "::" + monitor.getName() + " :: monitor started");
            //logger.info(Thread.currentThread().getName() + " " + monitor); 

            double tat = 0;
            String rsp = "";
            String message = "";
            String request_time = "";
            String response_time = "";
            int err = 9;
            long start = System.currentTimeMillis();
            request_time = GeneralUtils.getRequestTime();
            String mg_data[] = Config.getPropertyEager("MG_BALANCE_DATA").split(";");
            rsp = new MGClient().doBalance(mg_data[0], mg_data[1], mg_data[2], mg_data[3]);
            logger.info("MGRESPONSE " + rsp);
            if (rsp != null) {
                if (rsp.contains("balance")) {
                    message = "Mobile Gate balance check successful. Mobile Gate service is up and processing";
                    err = 0;
                } else {
                    err = 9;
                    message = "Mobile Gate balance check failed. Service may down";
                }
            }
            logger.info(message);
            response_time = GeneralUtils.getResponseTime();
            tat = System.currentTimeMillis() - start;
            //int i = AmardDAO.log(request_time, response_time, tat, monitor.getName(), err, http_code, monitor.getType(), monitor.getGroup(), monitor.getDescription());
            int i = AmardDAO.log(monitor, request_time, response_time, tat, err, "", message, mg_data[1]);
            logger.info(Thread.currentThread().getName() + " " + monitor.getType() + "::" + monitor.getName() + " Record Inserted ID :: " + i);

        } catch (Exception e) {
             logger.error(Thread.currentThread().getName() + " " + "Sorry an error occured", e);
        }
    }
}
