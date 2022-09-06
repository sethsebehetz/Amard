package com.etz.gh.amard.utilities;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.log4j.PropertyConfigurator;

public class NetworkInterfaces {

    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(NetworkInterfaces.class);
    private static Map<String, Boolean> ipMatches = new HashMap<>();

    static {
        PropertyConfigurator.configure("cfg\\log4j.config");
    }

    public static void main(String[] args) throws Exception {
        logger.info(Thread.currentThread().getName() + " " + searchIPAddress("10.0.1.21"));
    }

    public void getIPAddress() throws Exception {
        InetAddress ip = null;
        String hostname = "";
        try {
            ip = InetAddress.getLocalHost();
            hostname = ip.getHostName();
            logger.info(Thread.currentThread().getName() + " " + "Your current IP address : " + ip);
            logger.info(Thread.currentThread().getName() + " " + "Your current Hostname : " + hostname);

        } catch (UnknownHostException e) {
            logger.error("Sorry something wrong!", e);
        }
    }

    public static void interfaces() throws Exception {
        logger.info(Thread.currentThread().getName() + " " + "Your Host addr: " + InetAddress.getLocalHost().getHostAddress());  // often returns "127.0.0.1"
        Enumeration<NetworkInterface> n = NetworkInterface.getNetworkInterfaces();
        for (; n.hasMoreElements();) {
            NetworkInterface e = n.nextElement();
            Enumeration<InetAddress> a = e.getInetAddresses();
            for (; a.hasMoreElements();) {
                InetAddress addr = a.nextElement();
                //logger.info(Thread.currentThread().getName() + " " + "IP Address  " + addr.getHostAddress());
            }
        }
    }

    public static boolean searchIPAddress(String ipAddress) {
        try {
            if(ipMatches.get(ipAddress) != null){
                //logger.info(Thread.currentThread().getName() + " " + ipAddress + " match true from amard ipAddress cache registry");
                return true;
            }
            //logger.info(Thread.currentThread().getName() + " " + "checking network interfaces for IP address: " + ipAddress);
            //logger.info(Thread.currentThread().getName() + " " + "Your Host addr: " + InetAddress.getLocalHost().getHostAddress());  // often returns "127.0.0.1"
            Enumeration<NetworkInterface> n = NetworkInterface.getNetworkInterfaces();
            int i;
            for (i = 0; n.hasMoreElements();) {
                NetworkInterface e = n.nextElement();
                Enumeration<InetAddress> a = e.getInetAddresses();
                for (; a.hasMoreElements();) {
                    i++;
                    InetAddress addr = a.nextElement();
                    //logger.info(Thread.currentThread().getName() + " " + "IP Address  " + i + " : " + addr.getHostAddress());
                    if (ipAddress.equals(addr.getHostAddress())) {
                        ipMatches.put(ipAddress, Boolean.TRUE);
                      //  logger.info(Thread.currentThread().getName() + " " + ipAddress + " address found on this server. IPAddress match true");
                        return true;
                    }
                }
            }
        } catch (SocketException ex) {
            Logger.getLogger(NetworkInterfaces.class.getName()).log(Level.SEVERE, null, ex);
        }
        //logger.info(Thread.currentThread().getName() + " " + ipAddress + " not found on any of the network interfaces on this server");
        return false;
    }
}
