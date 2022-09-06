package com.etz.gh.amard.schedulers;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author seth.sebeh
 */
public final class SchedulerFactorySingleton {

    private static Scheduler instance;

    private SchedulerFactorySingleton() {

    }

    public static Scheduler getScheduler() throws SchedulerException {
        if (instance == null) {
            instance = (new StdSchedulerFactory()).getScheduler();
        }
        return instance;    
    }

}
