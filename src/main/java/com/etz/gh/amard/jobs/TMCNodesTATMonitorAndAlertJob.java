package com.etz.gh.amard.jobs;

import com.etz.gh.Config;
import com.etz.gh.amard.model.TMCNodesTAT;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.PropertyConfigurator;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author seth.sebeh
 */
public class TMCNodesTATMonitorAndAlertJob implements Job {
    private int TMC_MINIMUM_ACCEPTABLE_TAT = Integer.parseInt(Config.getProperty("TMC_MINIMUM_ACCEPTABLE_TAT"));

    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(MobileGateJob.class);

    static {
        PropertyConfigurator.configure("cfg\\log4j.config");
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //SELECT nodes TAT 2.for each node if TAT is > 5 alert
        List<TMCNodesTAT> nodes = new ArrayList<>();
        for(TMCNodesTAT node : nodes){
            if(node.getTat() > TMC_MINIMUM_ACCEPTABLE_TAT){
                if(node.getNode().equals("GCBCMS")  || node.getNode().equals("GCBCBA")){
                    //send mail, sms to gcb
                }
                else if(node.getNode().equals("VBANKCMS")  || node.getNode().equals("VBANKCBA")){
                    //send mail, sms to etz
                }
                else if(node.getNode().equals("MTNMOBILEMONEY")  || node.getNode().equals("GIPXchange")){
                    //send mail, sms to etz
                }
                else if(node.getNode().equals("BOACMS")  || node.getNode().equals("BOACBA")){
                    //send mail, sms to boa
                }
            }
        }
    }

}
