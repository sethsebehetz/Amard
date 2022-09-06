package com.etz.gh.amard.utilities;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import com.etz.gh.amard.entities.Monitor;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author seth.sebeh
 */
public class TelnetClient {

    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(TelnetClient.class);

    static {
        PropertyConfigurator.configure("cfg\\log4j.config");
    }

    Socket client = null;

    public String telnet(Monitor monitor, String host, int port, int timeout) {
        long start = System.currentTimeMillis();
        int err = 99;
        try {
            //socket = new Socket(host, port);       
            client = new Socket();
            client.connect(new InetSocketAddress(host, port), timeout);
            logger.info(Thread.currentThread().getName() + " " + "Connected to host :: " + host + " :: PORT :: " + port);
            err = 0;
        } catch (SocketTimeoutException ex) {
            logger.error("Connection Timeout :: " + host + " :: PORT :: " + port, ex);
            err = 8;
        } catch (IOException ex) {
            logger.error("Connection refused :: " + host + " :: PORT :: " + port, ex);
            err = 9;
        } finally {
            try {
                if (client != null) {
                    client.close();
                }
            } catch (IOException ex) {
                logger.error("could not close socket", ex);
            }
        }
        long tat = System.currentTimeMillis() - start;
        logger.info(Thread.currentThread().getName() + " " + monitor.getName() + " :: " + err + "#" + tat);
        return err + "#" + tat;
    }

    public static void main(String[] args) {
        TelnetClient tc = new TelnetClient();
        Monitor m = new Monitor();
        m.setName("Test");
        String s = tc.telnet(m, "localhost", 3306, 60000);
        logger.info(Thread.currentThread().getName() + " " + s);
    }

}
