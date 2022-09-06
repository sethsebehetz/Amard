package com.etz.gh.amard.jobs;

import com.etz.gh.amard.dao.AmardDAO;
import org.apache.log4j.PropertyConfigurator;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author seth.sebeh
 */
public class DataCleanerJob implements Job {
    
    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(DataCleanerJob.class);

    static {
        PropertyConfigurator.configure("cfg\\log4j.config");
    }
    
     public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            AmardDAO.cleanLogData();
        } catch (Exception e) {
            logger.error(Thread.currentThread().getName() + " " + "Sorry an error occured", e);
        }
     }
}
