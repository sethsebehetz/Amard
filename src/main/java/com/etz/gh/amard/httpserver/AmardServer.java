package com.etz.gh.amard.httpserver;

import com.etz.gh.amard.utilities.Config;
import com.etz.gh.amard.utilities.NetworkInterfaces;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import org.apache.log4j.PropertyConfigurator;

/**
 * server class to serve amard clients requests
 *
 * @author seth.sebeh
 */
public class AmardServer {

    static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(AmardServer.class);

    static {
        PropertyConfigurator.configure("cfg\\log4j.config");
    }

    public static void main(String[] args) {
        start();
    }

    public static void start() {
        String[] home_servers = Config.getKey("APP_HOME_SERVER").split("#");
        for (String ip : home_servers) {
            if (NetworkInterfaces.searchIPAddress(ip)) {
                try {
                    long start = System.currentTimeMillis();
                    logger.info(Thread.currentThread().getName() + " Starting Amard Server...");
                    int port = Integer.parseInt(Config.getKey("PORT"));
                    InetSocketAddress addr = new InetSocketAddress(port);
                    HttpServer server = HttpServer.create(addr, 0);
                    server.createContext(Config.getKey("REQUEST_PATH"), new AmardServerHandler());
                    server.setExecutor(Executors.newCachedThreadPool());
                    server.start();
                    long end = System.currentTimeMillis() - start;
                    logger.info(Thread.currentThread().getName() + " Amard Server started and listening on port " + port);
                    logger.info(Thread.currentThread().getName() + " Amard Server started in " + end + " ms");
                    System.out.println(Thread.currentThread().getName() + " Amard Server started in " + end + " ms");
                } catch (IOException ex) {
                    logger.error(Thread.currentThread().getName() + " An error occured starting Amard Server ", ex);
                }
            }
        }
    }
}
