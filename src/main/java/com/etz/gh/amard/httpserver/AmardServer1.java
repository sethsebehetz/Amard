//package com.etz.gh.amard.httpserver;
//
//import com.etz.gh.amard.controller.AmardController;
//import com.etz.gh.amard.dao.AmardDAO;
//import com.etz.gh.amard.entities.ResponseTable;
//import com.etz.gh.amard.utilities.Config;
//import com.etz.gh.amard.utilities.NetworkInterfaces;
//import com.sun.net.httpserver.Headers;
//import com.sun.net.httpserver.HttpExchange;
//import com.sun.net.httpserver.HttpHandler;
//import com.sun.net.httpserver.HttpServer;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.io.UnsupportedEncodingException;
//import java.net.InetSocketAddress;
//import java.nio.charset.StandardCharsets;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.Executors;
//import org.apache.log4j.PropertyConfigurator;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
///**
// * server class to serve amard clients requests
// *
// * @author seth.sebeh
// */
//public class AmardServer1 {
//
//    static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(AmardServer1.class);
//
//    static {
//        PropertyConfigurator.configure("cfg\\log4j.config");
//    }
//
//    public static void main(String[] args) {
//        start();
//    }
//
//    public static void start() {
//        String[] home_servers = Config.getKey("APP_HOME_SERVER").split("#");
//        for (String ip : home_servers) {
//            if (NetworkInterfaces.searchIPAddress(ip)) {
//                try {
//                    long start = System.currentTimeMillis();
//                    logger.info("Starting Amard Server...");
//                    int port = Integer.parseInt(Config.getKey("PORT"));
//                    InetSocketAddress addr = new InetSocketAddress(port);
//                    HttpServer server = HttpServer.create(addr, 0);
//                    server.createContext(Config.getKey("REQUEST_PATH"), new ServerProcessor1());
//                    server.setExecutor(Executors.newCachedThreadPool());
//                    server.start();
//                    long end = System.currentTimeMillis() - start;
//                    logger.info("Amard Server started and listening on port " + port);
//                    logger.info("Amard Server started in " + end + " ms");
//                } catch (IOException ex) {
//                    logger.error("An error occured starting Amard Server ", ex);
//                }
//            }
//        }
//    }
//}
//
//class ServerProcessor1 implements HttpHandler {
//
//    private Map<String, String> query_pairs = new LinkedHashMap<>();
//
//    public void handle(HttpExchange exchange) throws IOException {
//        long start = System.currentTimeMillis();
//        AmardServer1.logger.info("NEW REQUEST IN <<<<<<<<< <<<<<<<<< <<<<<<<<< ");
//        String requestorIP = exchange.getRemoteAddress().getAddress().getHostAddress();
//        AmardServer1.logger.info("REQUESTOR IP " + requestorIP);
//        printRequestHeaders(exchange);
//        urlRouter(exchange.getRequestURI().getPath(), exchange);
//        AmardServer1.logger.info("Time to process request " + (System.currentTimeMillis() - start) + "ms");
//        AmardServer1.logger.info("<<<<<<<<< <<<<<<<<< <<<<<<<<< END OF REQUEST");
//    }
//
//    public void printRequestHeaders(HttpExchange exchange) {
//        AmardServer1.logger.info("-- headers --");
//        exchange.getRequestHeaders().entrySet().forEach(AmardServer1.logger::info);
//        AmardServer1.logger.info("Request received from IP: " + exchange.getRemoteAddress().getAddress());
//    }
//
//    public String getRequestBody(InputStream inputStream) throws IOException {
//
//        StringBuilder stringBuilder = new StringBuilder();
//        String line = null;
//
//        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8.name()))) {
//            while ((line = bufferedReader.readLine()) != null) {
//                stringBuilder.append(line);
//            }
//        }
//
//        return stringBuilder.toString();
//    }
//
//    public void urlRouter(String path, HttpExchange exchange) {
//        try {
//            AmardServer1.logger.info("Request path received => " + path);
//            String s[] = path.split("/");
//            String contextPath = s[1];
//            String actionPath = s[2];
//            String filter = s[3];
//            AmardServer1.logger.info("CONTEXT PATH => " + contextPath);
//            AmardServer1.logger.info("ACTION PATH => " + actionPath);
//            AmardServer1.logger.info("FILTER => " + filter);
//            actionPathProcessor(actionPath, filter, exchange);
//        } catch (ArrayIndexOutOfBoundsException ex) {
//            String message = "No route found for path " + path;
//            AmardServer1.logger.error(message, ex);
//            try (OutputStream os = exchange.getResponseBody()) {
//                exchange.sendResponseHeaders(404, 0);
//                os.write(message.getBytes());
//                os.flush();
//            } catch (IOException ex1) {
//                AmardServer1.logger.error("Something wrong ", ex1);
//            }
//        }
//    }
//
//    public void actionPathProcessor(String actionPath, String filter, HttpExchange exchange) {
//        AmardController ctrl = new AmardController();
//        try {
//            String requestBody = getRequestBody(exchange.getRequestBody());
//            AmardServer1.logger.info("Request body received " + requestBody);
//            setQueryPairs(exchange.getRequestURI().getQuery());
//            Headers responseHeaders = exchange.getResponseHeaders();
//            responseHeaders.set("Content-Type", "application/json");
//            responseHeaders.set("Access-Control-Allow-Origin", "*");
//            String response = "";
//            switch (actionPath) {
//                case "monitorlogs":
////                    List<ResponseTable> list = new AmardDAO().getLogRecordsByType(filter);
//                    List<ResponseTable> list = ctrl.getLogRecordsByType(filter);
//                    response = new JSONArray(list).toString();
//                    try (OutputStream os = exchange.getResponseBody()) {
//                        exchange.sendResponseHeaders(200, response.getBytes().length);
//                        os.write(response.getBytes());
//                        os.flush();
//                    }
//                    break;
//                case "graph":
//                    String network = getQueryParam("network");
//                    String txn_type = getQueryParam("txn_type");
//                    int interval = Integer.parseInt(getQueryParam("interval"));
//                    response = ctrl.getMomoLiveGraphData(network, txn_type, interval);
//                    try (OutputStream os = exchange.getResponseBody()) {
//                        exchange.sendResponseHeaders(200, response.getBytes().length);
//                    }
//                    break;
//                default:
//                    AmardServer1.logger.info("No processor set up for path " + actionPath);
//                    try {
//                        exchange.sendResponseHeaders(404, 0);
//                    } catch (IOException ex1) {
//
//                    }
//            }
//        } catch (IOException ex) {
//            String message = "An error occured reading body content";
//            AmardServer1.logger.error(message, ex);
//            try (OutputStream os = exchange.getResponseBody()) {
//                exchange.sendResponseHeaders(500, message.getBytes().length);
//                os.write(message.getBytes());
//                os.flush();
//            } catch (IOException ex1) {
//
//            }
//        } catch (Exception ex) {
//            String message = "Internal server error ocurred";
//            AmardServer1.logger.error(message, ex);
//            try (OutputStream os = exchange.getResponseBody()) {
//                exchange.sendResponseHeaders(500, message.getBytes().length);
//                os.write(message.getBytes());
//                os.flush();
//            } catch (IOException ex1) {
//
//            }
//        }
//    }
//
//    public void setQueryPairs(String query) throws UnsupportedEncodingException {
//        if (query != null) {
//            AmardServer1.logger.info("Query string received " + query);
//            //query = URLDecoder.decode(query, "UTF-8");
//            AmardServer1.logger.info("Decoded query string received " + query);
//            String[] pairs = query.split("&");
//            for (String pair : pairs) {
//                int idx = pair.indexOf("=");
//                final String value = idx > 0 && pair.length() > idx + 1 ? pair.substring(idx + 1) : "";
//                this.query_pairs.put(pair.substring(0, idx), value);
//            }
//        }
//    }
//
//    public String getQueryParam(String key) {
//        return this.query_pairs.get(key);
//    }
//
//}
