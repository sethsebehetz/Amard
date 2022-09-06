package com.etz.gh.amard.schedulers;

import com.etz.gh.amard.jobs.DataCleanerJob;
import com.etz.gh.amard.utilities.Config;
import org.apache.log4j.PropertyConfigurator;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author seth.sebeh
 */
public class DataLogCleanerScheduler {

    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(DataLogCleanerScheduler.class);  

    static {
        PropertyConfigurator.configure("cfg\\log4j.config");
    }

    public static void startScheduler() {
        try {
            logger.info("Starting data cleaner scheduler");
            JobDetail jobDetail = JobBuilder.newJob(DataCleanerJob.class).withIdentity("DATACLEANER SCHEDULER", "DATACLEANER SERVICES").build();
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity("DATACLEANER SCHEDULER", "DATACLEANER SERVICES").startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule(Config.getProperty("DATA_CLEANER_CRON_JOB_TIME"))).build();

            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
            scheduler.scheduleJob(jobDetail, trigger);           
        } catch (SchedulerException ex) {
            logger.error("Scheduler Error. Sorry, something wrong!", ex);
        }
    }
}
