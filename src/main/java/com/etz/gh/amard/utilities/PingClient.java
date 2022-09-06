package com.etz.gh.amard.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import com.etz.gh.amard.entities.Monitor;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author seth.sebeh
 *
 */
public class PingClient {

    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(PingClient.class);

    static {
        PropertyConfigurator.configure("cfg\\log4j.config");
    }

    String message = "";
    String pingResult = "";

    public String ping(Monitor monitor, String host) {
        long start = System.currentTimeMillis();
        int err = 9;
        String os = System.getProperty("os.name");
        ProcessBuilder processBuilder = new ProcessBuilder();

        if (os.startsWith("Windows")) {
            //Run this on Windows, cmd, /c = terminate after this run
            processBuilder.command("cmd.exe", "/c", "ping -n 1 " + host);
            //processBuilder.command("cmd.exe", "/c", "ping -n 2 google.com");
        } else {
            processBuilder.command("cmd.exe", "/c", "ping -c 1 " + host);
        }

        try {
            Process process = processBuilder.start();

            // blocked :(
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String b;
            StringBuffer buffer = new StringBuffer();

            while ((b = reader.readLine()) != null) {
                buffer.append(b);
                logger.info(Thread.currentThread().getName() + " " + b);
                if (b.contains("Request timed out") || message.contains("unreacheable")) {
                    reader.close();
                }
            }

            reader.close();
            message = buffer.toString();

            int exitCode = process.waitFor();
            logger.info(Thread.currentThread().getName() + " " + "\nExited with error code : " + exitCode);

            if (message.contains("0%") && !message.contains("unreacheable")) {
                logger.info(Thread.currentThread().getName() + " " + "PING to host successful :: " + host);
                err = 0;
            } else {
                logger.info(Thread.currentThread().getName() + " " + "PING to host failed :: " + host);
            }

        } catch (IOException e) {
            logger.error("IOException connecting to " + monitor.getIp_address(), e);
        } catch (InterruptedException e) {
            logger.error("Exception pinging " + monitor.getIp_address(), e);
        }

        double tat = System.currentTimeMillis() - start;
        logger.info(Thread.currentThread().getName() + " " + monitor.getName() + " :: " + err + "#" + tat);
        return err + "#" + tat;
    }

    public static void main(String[] args) {
        //logger.info(Thread.currentThread().getName() + " " + "OS NAME :: " +  System.getProperty("os.name"));
        PingClient p = new PingClient();
        Monitor m = new Monitor();
        m.setName("Test");
        String s = p.ping(m, "google.com");
        logger.info(Thread.currentThread().getName() + " " + s);
       
        System.out.println( s);
    }
}
