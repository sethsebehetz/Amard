package com.etz.gh.amard.schedulers;

import com.etz.gh.amard.entities.Monitor;
import com.etz.gh.amard.jobs.ServiceEndpointAvailabilityJob;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

/**
 *
 * @author seth.sebeh
 */
public class SchedulerTemplate extends SchedulerFactory implements Runnable {

    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SchedulerTemplate.class);

    static {
        PropertyConfigurator.configure("cfg\\log4j.config");
    }

    Scheduler scheduler;
    List<Monitor> serviceMonitors;
    Class jobClass = null;
    String name = "";

    public SchedulerTemplate(List<Monitor> serviceMonitors, Class jobClass, String name) throws SchedulerException {
        this.serviceMonitors = serviceMonitors;
        this.jobClass = jobClass;
        this.name = name;
        Properties config = new Properties();
        config.setProperty("org.quartz.scheduler.instanceName", name);
        config.setProperty("org.quartz.threadPool.threadCount", String.valueOf(this.serviceMonitors.size()));
        this.scheduler = getScheduler(config);
        logger.info(Thread.currentThread().getName() + " " + name + " RECORDS SIZE :: " + this.serviceMonitors.size());
    }

    @Override
    public void run() {
        try {
            logger.info(Thread.currentThread().getName() + " " + "STARTING " + this.name + " JOB PROCESSOR");
            for (Monitor service : this.serviceMonitors) {
                JobDetail jobDetail = JobBuilder.newJob(this.jobClass).withIdentity(service.getType() + "::" + service.getName(), service.getGroup()).build();
                Trigger trigger = TriggerBuilder.newTrigger().withIdentity(service.getType() + "::" + service.getName(), service.getGroup()).startNow().
                        withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(service.getScheduler_interval()).repeatForever()).build();
                //logger.info(Thread.currentThread().getName() + " " + "Service class details >> " + service);
                jobDetail.getJobDataMap().put("NAME", service);
                this.scheduler.scheduleJob(jobDetail, trigger);
            }
            this.scheduler.start();
        } catch (SchedulerException ex) {
            logger.error("Sorry, something wrong!", ex);
        }
    }
}
