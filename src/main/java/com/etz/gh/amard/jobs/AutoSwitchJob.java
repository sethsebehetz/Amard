package com.etz.gh.amard.jobs;

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
public class AutoSwitchJob implements Job {

    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(AutoSwitchJob.class);

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
            String accounts[] = Config.getPropertyEager("GIP_VERIFY_ACCOUNT").split(";");

            for (String acct : accounts) {
                rsp = AutoSwitch.GIPAccountsEnquiry(acct);
                String bankName = GeneralUtils.getBankNameByBankCode(acct.split("~")[1]);
                if (rsp != null) {
                    if (rsp.equals("00")) {
                        message = bankName +  " GIP Verification successful.";
                        //message = billerInfo.split("-")[0] + " Verification successful. " + rsp.split("#")[1];
                        err = 0;
                    } else {
                        err = 9;
                        message = bankName +  " GIP Verification failed.";
//                        message = "GIP MTN Mobile Money Verification failed. Service may down";
                    }
                }
                logger.info(message);
                monitor.setName(bankName + " GIP" + " Verification");
                response_time = GeneralUtils.getResponseTime();
                tat = System.currentTimeMillis() - start;
                //int i = AmardDAO.log(request_time, response_time, tat, monitor.getName(), err, http_code, monitor.getType(), monitor.getGroup(), monitor.getDescription());
                int i = AmardDAO.log(monitor, request_time, response_time, tat, err, "", message, acct);
                logger.info(Thread.currentThread().getName() + " " + monitor.getType() + "::" + monitor.getName() + " Record Inserted ID :: " + i);
            }

        } catch (Exception e) {
            logger.error("An error occurred ", e);
        }
    }
}
