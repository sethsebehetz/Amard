//package com.etz.gh.amard.essential;
//
//import com.etz.gh.amard.dao.AmardDAO;
//import com.etz.gh.amard.entities.Configuration;
//import com.etz.gh.amard.entities.Monitor;
//import com.etz.gh.amard.jobs.*;
//import com.etz.gh.amard.schedulers.SchedulerTemplate;
//import com.etz.gh.amard.utilities.Config;
//import com.etz.gh.amard.utilities.NetworkInterfaces;
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import static java.util.stream.Collectors.toList;
//import org.quartz.SchedulerException;
//
///**
// *
// * @author seth.sebeh
// */
//public class Starter_22082019 {
//
//    final private String appHomeServer;
//
//    public Starter_22082019() {
//        System.out.println("Starting A.M.A.R.D 1.0 ...");
//        //load set up data
//        List<Configuration> configs = loadSetUpData();
//        appHomeServer = Config.getKey(configs, "APP_HOME");
//        System.out.println("App Home Server configured is " + appHomeServer);
//        System.out.println("Finished Loading set up data.");
//    }
//
////    List<Service> serviceEnpointAvailabilityMonitors;
////    List<Service> transactionsNotComingMonitors;
//    //load record from service table
//    //each record as a Monitor class
//    //dispatcher
//    //Each monitor class passed to ShedulerFactory
//    public void start() {
//        try {
//
//            List<Monitor> monitors = getMonitors();
//
//            List<Monitor> serviceEndpointAvailabilityMonitors = getServiceEndPointAvailabilityMonitors(monitors);
//
//            //this.serviceEnpointAvailabilityMonitors = getServiceEndPointAvailabilityMonitors();
////           
////            List<Monitor> serviceEnpointAvailabilityMonitors = monitors.stream().filter(s -> s.getType().equals("SEI")).collect(toList());
//            //List<Monitor> transactionsNotComingMonitors = monitors.stream().filter(s -> s.getType().equals("TNC")).collect(toList());
//            //List<Monitor> transactionsFailingMonitors = monitors.stream().filter(s -> s.getType().equals("TF")).collect(toList());
//            //List<Monitor> pingMonitors = monitors.stream().filter(s -> s.getType().equals("PING")).collect(toList());
//            //List<Monitor> telnetMonitors = monitors.stream().filter(s -> s.getType().equals("TELNET")).collect(toList());
//            ExecutorService executor = Executors.newFixedThreadPool(6);
//
//            //start Monitor Enpoint Availability Processing
//            if (serviceEndpointAvailabilityMonitors.size() > 0) {
//                executor.submit(new SchedulerTemplate(serviceEndpointAvailabilityMonitors, ServiceEndpointAvailabilityJob.class, "SEI"));
//            }
//
//            //start Transactions Not Coming Processing
//            // executor.submit(new SchedulerTemplate(transactionsNotComingMonitors, TransactionsNotComingJob.class, "TNC"));
//            //start Transactions Failing Processing
//            //executor.submit(new SchedulerTemplate(transactionsFailingMonitors, TransactionsFailingJob.class, "TF"));           
//            //start Ping Processing
//            //executor.submit(new SchedulerTemplate(pingMonitors, PingJob.class, "PING"));
//            //start Telnet Processing
////            executor.submit(new SchedulerTemplate(telnetMonitors, TelnetJob.class, "TELNET"));
//        } catch (SchedulerException ex) {
//            Logger.getLogger(Starter_22082019.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    public static void main(String[] args) {
//        new Starter_22082019().start();
//    }
//
//    public List<Monitor> getServiceEndPointAvailabilityMonitors(List<Monitor> monitors) {
//        System.out.println("Loading ServiceEndPointAvailabilityMonitors ...");
//        List<Monitor> SEAMonitors = monitors.stream()
//                .filter(s -> {
//                    if (s.getType().equals("SEA")) {
//                        System.out.println(s.getName() + " Bridge Value " + s.getBridge());
//                        if (s.getBridge() == 0) {
//                            try {
//                                if (NetworkInterfaces.searchIPAddress(appHomeServer)) {
//                                    System.out.println("AppHomeServer True");
//                                    System.out.println("SEA:" + s.getName() + " running from AppHomeServer " + appHomeServer);
//                                    return true;
//                                }
//                            } catch (Exception ex) {
//                                Logger.getLogger(Starter_22082019.class.getName()).log(Level.SEVERE, null, ex);
//                            }
//                            return false;
//                        } else {
//                            System.out.println(s.getName() + " BridgeServerIP " + s.getBridge_ip_address());
//                            try {
//                                if (NetworkInterfaces.searchIPAddress(s.getBridge_ip_address())) {
//                                    System.out.println("BridgeServerIP True");
//                                    System.out.println("SEA:" + s.getName() + " running from BridgeServer " + appHomeServer);
//                                    return true;
//                                }
//                            } catch (Exception ex) {
//                                Logger.getLogger(Starter_22082019.class.getName()).log(Level.SEVERE, null, ex);
//                            }
//                            return false;
//                        }
//                    }
//                    return false;
//                }
//                ).collect(toList());
//
//        System.out.println("Done loading ServiceEndPointAvailabilityMonitors");
//
//        if (SEAMonitors.size() == 0) {
//            System.out.println("No service set up for monitor SEA");
//        }
//
//        return SEAMonitors;
//    }
//
//    public List<Monitor> getServices(String moniter_type) {
//        List<Monitor> monitors = new AmardDAO().getRequestNotComing();
//        System.out.println("Loading " + moniter_type + " monitors ...");
//        List<Monitor> services = monitors.stream()
//                .filter(s -> {
//                    if (s.getType().equals(moniter_type)) {
//                        System.out.println(s.getName() + " Bridge Value " + s.getBridge());
//                        if (s.getBridge() == 0) {
//                            try {
//                                if (NetworkInterfaces.searchIPAddress(appHomeServer)) {
//                                    System.out.println("AppHomeServer True");
//                                    System.out.println(moniter_type + ":" + s.getName() + " running from AppHomeServer " + appHomeServer);
//                                    return true;
//                                }
//                            } catch (Exception ex) {
//                                Logger.getLogger(Starter_22082019.class.getName()).log(Level.SEVERE, null, ex);
//                            }
//                            return false;
//                        } else {
//                            System.out.println(s.getName() + " BridgeServerIP " + s.getBridge_ip_address());
//                            try {
//                                if (NetworkInterfaces.searchIPAddress(s.getBridge_ip_address())) {
//                                    System.out.println("BridgeServerIP True");
//                                    System.out.println(moniter_type + ":" + s.getName() + " running from BridgeServer " + appHomeServer);
//                                    return true;
//                                }
//                            } catch (Exception ex) {
//                                Logger.getLogger(Starter_22082019.class.getName()).log(Level.SEVERE, null, ex);
//                            }
//                            return false;
//                        }
//                    }
//                    return false;
//                }
//                ).collect(toList());
//
//        System.out.println("Done loading " + moniter_type + " services");
//
//        if (services.size() == 0) {
//            System.out.println("No service set up for monitor " + moniter_type);
//        }
//
//        return services;
//    }
//
//    public List<Monitor> getRequestNotComingMonitors() {
//        return new AmardDAO().getRequestNotComing();
//    }
//
//    public List<Monitor> getMonitors() {
//        return new AmardDAO().getMonitors();
//    }
//
//    public List<Configuration> loadSetUpData() {
//        return new AmardDAO().getConfigData();
//    }
//}
