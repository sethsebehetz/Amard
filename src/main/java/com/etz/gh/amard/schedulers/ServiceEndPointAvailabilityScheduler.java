package com.etz.gh.amard.schedulers;

import com.etz.gh.amard.entities.Monitor;
import com.etz.gh.amard.jobs.ServiceEndpointAvailabilityJob;
import java.util.List;
import java.util.Properties;
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
public class ServiceEndPointAvailabilityScheduler extends SchedulerFactory implements Runnable {

    Scheduler scheduler;
    List<Monitor> serviceMonitors;

    public ServiceEndPointAvailabilityScheduler(List<Monitor> serviceMonitors, String name) throws SchedulerException {
        this.serviceMonitors = serviceMonitors;
        Properties config = new Properties();
        config.setProperty("org.quartz.scheduler.instanceName", name);
        config.setProperty("org.quartz.threadPool.threadCount", String.valueOf(this.serviceMonitors.size()));
        this.scheduler = getScheduler(config);
        System.out.println(name + " RECORDS SIZE :: " + this.serviceMonitors.size());
    }

    @Override
    public void run() {
        try {
            System.out.println("STARTING SERVICE ENDPOINT AVAILABILITY JOB PROCESSOR");
            for (Monitor service : this.serviceMonitors) {
                JobDetail jobDetail = JobBuilder.newJob(ServiceEndpointAvailabilityJob.class).withIdentity(service.getType() + "::" + service.getName(), service.getGroup()).build();
                Trigger trigger = TriggerBuilder.newTrigger().withIdentity(service.getType() + "::" + service.getName(), service.getGroup()).startNow().
                        withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(service.getScheduler_interval()).repeatForever()).build();
                System.out.println("Service class details >> " + service);
                jobDetail.getJobDataMap().put("NAME", service);

                this.scheduler.scheduleJob(jobDetail, trigger);
            }
            this.scheduler.start();
        } catch (SchedulerException ex) {
            System.out.println("EXCEPTION IN SERVICE ENDPOINT PROCESSOR >> " + ex);
        }
    }
}
