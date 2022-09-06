package com.etz.gh.amard.schedulers;

import java.util.Properties;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author seth.sebeh
 */
public class SchedulerFactory {
    
   public Scheduler getScheduler(Properties schedulerInstanceProperties) throws SchedulerException{     
        return new StdSchedulerFactory(schedulerInstanceProperties).getScheduler();
   }   
   
}
