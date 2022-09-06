package com.etz.gh.amard.essential;

import com.etz.gh.amard.dao.AmardDAO;
import com.etz.gh.amard.entities.Configuration;
import com.etz.gh.amard.entities.Monitor;
import com.etz.gh.amard.httpserver.AmardServer;
import com.etz.gh.amard.jobs.*;
import com.etz.gh.amard.schedulers.DataLogCleanerScheduler;
import com.etz.gh.amard.schedulers.SchedulerTemplate;
import com.etz.gh.amard.utilities.Config;
import com.etz.gh.amard.utilities.NetworkInterfaces;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.function.Predicate;
import static java.util.stream.Collectors.toList;
import org.apache.log4j.PropertyConfigurator;
import org.quartz.SchedulerException;

/**
 *
 * @author seth.sebeh
 */
public class Starter {

    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Starter.class);
    List<Monitor> monitorList = null;

    static {
        PropertyConfigurator.configure("cfg\\log4j.config");
    }

    public Starter() {
        logger.info("Starting A.M.A.R.D 1.0 ...");
        logger.info("Loading set up data ...");
        //List<Configuration> configs = loadSetUpData();
        List<Configuration> configs = Config.CONFIG_DATA;
        Config.printKV(configs);
        logger.info("Finished Loading set up data.");
        logger.info("Retrieving all list of monitors ...");
        this.monitorList = getAllMonitorList();
        logger.info("Finished retrieving list of monitors.");
        logger.info("COUNT DISTINCT MONITORS " + getTotalMonitors());
        logger.info("AMARD Initialisation done successfully");
        AmardServer.start();
        if (NetworkInterfaces.searchIPAddress(Config.getProperty("DATA.CLEANER.SERVER"))) {
            DataLogCleanerScheduler.startScheduler();
        }
    }

    public void start() {
        try {

            List<Monitor> serviceEndpointAvailabilityMonitors = getAllMonitorListByType(this.monitorList, "SEA");
            List<Monitor> pingMonitors = getAllMonitorListByType(this.monitorList, "PING");
            List<Monitor> telnetMonitors = getAllMonitorListByType(this.monitorList, "TELNET");
            List<Monitor> transactionsNotComingMonitors = getAllMonitorListByType(this.monitorList, "TNC");
            List<Monitor> transactionsFailing = getAllMonitorListByType(this.monitorList, "TF");
            List<Monitor> ambiguousTransactions = getAllMonitorListByType(this.monitorList, "AE");
            List<Monitor> vasgateNodesMonitors = getAllMonitorListByType(this.monitorList, "VASGATE");
            List<Monitor> dbMonitors = getAllMonitorListByType(this.monitorList, "DBMONITOR");
            List<Monitor> gipNodesMonitors = getAllMonitorListByType(this.monitorList, "GIP");
            List<Monitor> mobileGateNodesMonitors = getAllMonitorListByType(this.monitorList, "MOBILE GATE");

            ExecutorService executor = Executors.newFixedThreadPool(Integer.valueOf(Config.getProperty("MONITOR_GROUP_POOL")));

            if (Config.getProperty("RUN_MONITORS") != null && Config.getProperty("RUN_MONITORS").equals("1")) {
                logger.info("Monitors runnable is set to ON");
//            //start Service Endpoint Availability[SEA] Processing
                if (!serviceEndpointAvailabilityMonitors.isEmpty()) {
                    executor.submit(new SchedulerTemplate(serviceEndpointAvailabilityMonitors, ServiceEndpointAvailabilityJob.class, "SEA"));
                }

//            //start Service PING Processing
                if (!pingMonitors.isEmpty()) {
                    executor.submit(new SchedulerTemplate(pingMonitors, PingJob.class, "PING"));
                }
//
                //start Service TELNET Processing
                if (!telnetMonitors.isEmpty()) {
                    executor.submit(new SchedulerTemplate(telnetMonitors, TelnetJob.class, "TELNET"));
                }

//            //start Transactions Not Coming[TNC] Processing 
                if (!transactionsNotComingMonitors.isEmpty()) {
                    executor.submit(new SchedulerTemplate(transactionsNotComingMonitors, TransactionsNotComingJob.class, "TNC"));
                }

                //start Transactions Failing [TF] Processing 
                if (!transactionsFailing.isEmpty()) {
                    executor.submit(new SchedulerTemplate(transactionsFailing, TransactionsFailingJob.class, "TF"));
                }
//            
//            //Disabled
//            //start Ambiguous Transactions Processing 
//            if (!ambiguousTransactions.isEmpty()) {
//                executor.submit(new SchedulerTemplate(ambiguousTransactions, AmbiguousErrorJob.class, "TF"));
//            }
//            
//            //start vasgate monitor pool txns 
                if (!vasgateNodesMonitors.isEmpty()) {
                    executor.submit(new SchedulerTemplate(vasgateNodesMonitors, VasGateJob.class, "VASGATE"));
                }
//            
//            //start dbmonitor monitor pool txns 
                if (!dbMonitors.isEmpty()) {
                    //executor.submit(new SchedulerTemplate(dbMonitors, TestDatabaseJob.class, "DBMONITOR"));
                }
//            
                //disabled
//            //start GIP monitor txns 
                if (!gipNodesMonitors.isEmpty()) {
                    executor.submit(new SchedulerTemplate(gipNodesMonitors, AutoSwitchJob.class, "GIP"));
                }

                //start mobilegate monitor
                if (!mobileGateNodesMonitors.isEmpty()) {
                    executor.submit(new SchedulerTemplate(mobileGateNodesMonitors, MobileGateJob.class, "MOBILE GATE"));
                }
            }else{
                logger.info("Monitors runnable is set to OFF");
            }

        } catch (Exception ex) {
            //catch (SchedulerException ex) {
            logger.error("Sorry something went wrong", ex);
        }
    }

    public static void main(String[] args) {
        new Starter().start();
    }

    public List<Monitor> getAllMonitorList() {
        return new AmardDAO().getMonitors();
    }

    public List<Monitor> getAllMonitorListByType(List<Monitor> monitors, String moniter_type) {
        logger.info("Loading " + moniter_type + " monitors ...");
        List<Monitor> services = monitors.stream()
                .filter(s -> {
                    if (s.getType().equals(moniter_type)) {
                        try {
                            if (NetworkInterfaces.searchIPAddress(s.getServer_ip()) || NetworkInterfaces.searchIPAddress(("10.0.1.42"))) {
                                logger.info(moniter_type + ":" + s.getName() + " running on " + s.getServer_ip());
                                return true;
                            }
                        } catch (Exception ex) {
                            logger.error("Sorry, something wrong!", ex);
                        }
                        return false;
                    }
                    return false;
                }).collect(toList());

        logger.info("Done loading " + moniter_type + " monitors");

        if (services.isEmpty()) {
            logger.info("No service set up for monitor " + moniter_type);
        }

        return services;
    }

    public List<Configuration> loadSetUpData() {
        return AmardDAO.getConfigData();
    }

    public int getTotalMonitors() {
        return (int) this.monitorList.stream().filter(distinctByKey(Monitor::getType)).count();
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

}
