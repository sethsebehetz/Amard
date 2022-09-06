package com.etz.gh.amard.jobs;

import com.etz.gh.amard.dao.AmardDAO;
import com.etz.gh.amard.entities.MobileMoney;
import com.etz.gh.amard.entities.Monitor;
import com.etz.gh.amard.utilities.DBUtils;
import com.etz.gh.amard.utilities.SuperHttpClient;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author seth.sebeh
 */
public class JobTemplate implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        Monitor service = (Monitor) dataMap.get("NAME");
        System.out.println(service.getType() + "::" + service.getName() + " :: service started");
        //System.out.println("service started >> THREAD NAME >>" + Thread.currentThread().getName() );
        System.out.println(service);

        Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        String request_time = sdf.format(dt);
        long start = System.currentTimeMillis();

        /**
         * job logic goes here
        */
        
        double tat = System.currentTimeMillis() - start;
        System.out.println(service.getType() + "::" + service.getName() + ":: TAT >> " + tat + " ms");

        Date ddt2 = new java.util.Date();
        String response_time = sdf.format(ddt2);

    }
}
