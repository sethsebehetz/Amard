package com.etz.gh.amard.jobs;

import com.etz.gh.amard.dao.AmardDAO;
import com.etz.gh.amard.entities.Monitor;
import static com.etz.gh.amard.jobs.TransactionsFailingJob.logger;
import com.etz.gh.amard.momo.mtn.MTNClient;
import com.etz.gh.amard.utilities.Config;
import com.etz.gh.amard.utilities.GeneralUtils;
import com.etz.gh.amard.utilities.SuperHttpClient;
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
public class ServiceEndpointAvailabilityJob implements Job {

    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ServiceEndpointAvailabilityJob.class);

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
            String rsp = "";
            String payload = monitor.getRequest_xml();
            if (payload == null) {
                payload = "";
            }

            payload = payload.replace("<:MSISDN>", "233" + GeneralUtils.generateRandomNumber(11))
                    .replace("<:REFERENCE>", "AMD" + GeneralUtils.generateRandomNumber(11))
                    .replace("<:TIMESTAMP>", GeneralUtils.getTimestamp())
                    .replace("<:TERMINALID>", Config.FG_TERMINAL_ID)
                    .replace("<:PIN>", Config.FG_PIN);

//           logger.info(Thread.currentThread().getName() + " " + payload);
            if (monitor.getHttp_request_method().equalsIgnoreCase("post")) {
                if (monitor.getSsl_type().equals("0")) {
                    //no ssl
//                if (monitor.getName().contains("MTN MOMO DEBIT")) {
//                    rsp = MTNClient.processRequestPayment(monitor, MTNClient.getMTNDebitSample(), monitor.getUrl());
//                } else if (monitor.getName().contains("MTN MOMO CREDIT")) {
//                    rsp = MTNClient.processDepositRequest(monitor, MTNClient.getMTNCreditSample(), monitor.getUrl());
//                } else {
//                    rsp = SuperHttpClient.doPost(monitor, monitor.getUrl(), payload);
//                }
                    rsp = SuperHttpClient.doPost(monitor, monitor.getUrl(), payload);
                } else if (monitor.getSsl_type().equals("1")) {
                    //one way ssl
                    rsp = SuperHttpClient.doPostSSL(monitor, monitor.getUrl(), payload, monitor.getTruststore_location(), monitor.getTruststore_password());
                } else if (monitor.getSsl_type().equals("2")) {
                    //two way ssl
                    rsp = SuperHttpClient.doPostSSL(monitor, monitor.getUrl(), payload, monitor.getTruststore_location(), monitor.getTruststore_password(), monitor.getKeystore_location(), monitor.getKeystore_password());
                }
            } else if (monitor.getHttp_request_method().equalsIgnoreCase("get")) {
                if (monitor.getSsl_type().equals("0")) {
                    rsp = SuperHttpClient.doGet(monitor, monitor.getUrl());
                } else if (monitor.getSsl_type().equals("1")) {

                } else if (monitor.getSsl_type().equals("2")) {

                }
            }

            logger.info(Thread.currentThread().getName() + " " + monitor.getType() + "::" + monitor.getName() + " :: RESPONSE " + rsp);

            if (rsp != null) {
                //requestTime#httpstatuscode#tat#responseTime
                String s[] = rsp.split("#");
                String request_time = s[0];
                String http_code = s[1];
                double tat = Double.valueOf(s[2]);
                String response_time = s[3];
                int response_length = Integer.valueOf(s[4]);
                String message = s[5];
                logger.info(Thread.currentThread().getName() + " " + monitor.getType() + "::" + monitor.getName() + " ::SEA MESSAGE: " + message);
                int err = Integer.valueOf(http_code);
                if (err == 200) {
                    err = 0;
                } else if (err == 600) {
                    err = 8;
                    //we will return http_status code of 600 when ssl handshake and assing err = 8 
                } else if (response_length > 0) {
                    err = 0;
                } else {
                    err = 9;
                }

                //int i = AmardDAO.log(request_time, response_time, tat, monitor.getName(), err, http_code, monitor.getType(), monitor.getGroup(), monitor.getDescription());
                int i = AmardDAO.log(monitor, request_time, response_time, tat, err, http_code, message, "");
                logger.info(Thread.currentThread().getName() + " " + monitor.getType() + "::" + monitor.getName() + " Record Inserted ID :: " + i);
            }
        } catch (Exception e) {
            logger.error(Thread.currentThread().getName() + " " + "Sorry an error occured", e);
        }
    }
}
