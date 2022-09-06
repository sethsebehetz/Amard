package com.etz.gh.amard.jobs;

import com.etz.gh.amard.dao.AmardDAO;
import com.etz.gh.amard.entities.Monitor;
import static com.etz.gh.amard.jobs.TransactionsFailingJob.logger;
import com.etz.gh.amard.momo.mtn.MTNClient;
import com.etz.gh.amard.utilities.Config;
import com.etz.gh.amard.utilities.GeneralUtils;
import com.etz.gh.amard.utilities.SuperHttpClient;
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
public class VasGateJob implements Job {

    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(VasGateJob.class);
    VasgateDAOImpl vasImpl = new VasgateDAOImpl();

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
            String payload = monitor.getRequest_xml();
            if (payload == null) {
                payload = "";
            }

            payload = payload.replace("<:MSISDN>", "233" + GeneralUtils.generateRandomNumber(11))
                    .replace("<:REFERENCE>", "AMD" + GeneralUtils.generateRandomNumber(11))
                    .replace("<:TIMESTAMP>", GeneralUtils.getTimestamp())
                    .replace("<:TERMINALID>", Config.FG_TERMINAL_ID)
                    .replace("<:PIN>", Config.FG_PIN);

            //verifyBiller(String vastype, String account, String otherInfo, String reference) {
            String[] billers = Config.getPropertyEager("BILL_ALIAS").split(";");

            for (String billerInfo : billers) {
                double tat = 0;
                String rsp = "";
                String message = "";
                String request_time = "";
                String response_time = "";
                int err = 9;
                long start = System.currentTimeMillis();
                request_time = GeneralUtils.getRequestTime();
                rsp = vasImpl.verifyBiller(billerInfo.split("-")[0], billerInfo.split("-")[1], billerInfo.split("-").length > 2 ? billerInfo.split("-")[2] : "", GeneralUtils.generateRandomNumber(8));
                if (rsp != null) {
                    if (rsp.split("#")[0].equals("00") || rsp.split("#")[0].equals("0")) {
                        message = billerInfo.split("-")[0] + " Verification successful. " + rsp.split("#")[1];
                        err = 0;
                    } else {
                        err = 9;
                        message = billerInfo.split("-")[0] + " Verification failed. Service may down";
                        if (rsp.split("#").length > 1) {
                            message += "<br> " + rsp.split("#")[1];
                        }
                    }
                }
                //update monitor name by adding the vas product. this will help in the group by query for applogs so each biller 
                //report can be seen
                monitor.setName(billerInfo.split("-")[0] + " " + "Verification");
                response_time = GeneralUtils.getResponseTime();
                tat = System.currentTimeMillis() - start;
                //int i = AmardDAO.log(request_time, response_time, tat, monitor.getName(), err, http_code, monitor.getType(), monitor.getGroup(), monitor.getDescription());
                int i = AmardDAO.log(monitor, request_time, response_time, tat, err, "", message, billerInfo.split("-")[0]);
                logger.info(Thread.currentThread().getName() + " " + monitor.getType() + "::" + monitor.getName() + " Record Inserted ID :: " + i);
            }
        } catch (Exception e) {
            logger.error(Thread.currentThread().getName() + " " + "Sorry an error occured", e);
        }
    }
}
