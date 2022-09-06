package com.etz.gh.amard.schedulers;


import com.etz.gh.Config;
import com.etz.gh.amard.jobs.TMCNodesTATMonitorAndAlertJob;
import org.apache.log4j.PropertyConfigurator;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author seth.sebeh edwom dont want transactional items in the ewallet. they
 * want affiliates to see their earnings calculated weekly
 *
 */
public class TMCNodesTATMonitorAndAlertScheduler {

    public static void main(String[] args) {
        startSheduler();
    }

    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(TMCNodesTATMonitorAndAlertScheduler.class);

    static {
        PropertyConfigurator.configure("cfg\\log4j.config");
    }

    String name = "";

    public static void startSheduler() {
        try {
            logger.info(Thread.currentThread().getName() + " " + "Starting TMC Nodes TAT Alert Monitor Job sheduler");
            JobDetail jobDetail = JobBuilder.newJob(TMCNodesTATMonitorAndAlertJob.class).withIdentity("TMC Nodes ", "TMC TSERVICES").build();
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity("TMC Nodes", "TMC SERVICES").startNow()
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(Integer.parseInt(Config.getProperty("TMC_NODES_ALERT_SCHEDULER_INTERVAL"))).repeatForever()).build();

            //scheduler
            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException ex) {
            logger.error("Sorry, something wrong!", ex);
        }
    }
}
