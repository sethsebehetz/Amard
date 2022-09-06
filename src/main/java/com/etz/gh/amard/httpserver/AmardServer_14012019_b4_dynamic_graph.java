//package com.etz.gh.amard.httpserver;
//
//import com.etz.gh.amard.controller.AmardController;
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
//public class AmardServer_14012019_b4_dynamic_graph {
//
//    static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(AmardServer_14012019_b4_dynamic_graph.class);
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
//                    logger.info(Thread.currentThread().getName() + " Starting Amard Server...");
//                    int port = Integer.parseInt(Config.getKey("PORT"));
//                    InetSocketAddress addr = new InetSocketAddress(port);
//                    HttpServer server = HttpServer.create(addr, 0);
//                    server.createContext(Config.getKey("REQUEST_PATH"), new ServerProcessor_14012019_b4_dynamic_graph());
//                    server.setExecutor(Executors.newCachedThreadPool());
//                    server.start();
//                    long end = System.currentTimeMillis() - start;
//                    logger.info(Thread.currentThread().getName() + " Amard Server started and listening on port " + port);
//                    logger.info(Thread.currentThread().getName() + " Amard Server started in " + end + " ms");
//                } catch (IOException ex) {
//                    logger.error(Thread.currentThread().getName() + " An error occured starting Amard Server ", ex);
//                }
//            }
//        }
//    }
//}
//
//class ServerProcessor_14012019_b4_dynamic_graph implements HttpHandler {
//
//    AmardController ctrl = new AmardController();
//    private Map<String, String> query_pairs = new LinkedHashMap<>();
//
//    public void handle(HttpExchange exchange) throws IOException {
//        long start = System.currentTimeMillis();
//        AmardServer_14012019_b4_dynamic_graph.logger.info(Thread.currentThread().getName() + " NEW REQUEST IN <<<<<<<<< <<<<<<<<< <<<<<<<<< ");
//        String requestorIP = exchange.getRemoteAddress().getAddress().getHostAddress();
//        AmardServer_14012019_b4_dynamic_graph.logger.info(Thread.currentThread().getName() + " REQUESTOR IP " + requestorIP);
//        //printRequestHeaders(exchange);
//        router(exchange.getRequestURI().getPath(), exchange);
//        AmardServer_14012019_b4_dynamic_graph.logger.info(Thread.currentThread().getName() + " Time to process request " + (System.currentTimeMillis() - start) + "ms");
//        AmardServer_14012019_b4_dynamic_graph.logger.info(Thread.currentThread().getName() + " <<<<<<<<< <<<<<<<<< <<<<<<<<< END OF REQUEST");
//        exchange.close();
//    }
//
//    public void printRequestHeaders(HttpExchange exchange) {
//        AmardServer_14012019_b4_dynamic_graph.logger.info(Thread.currentThread().getName() + " -- headers --");
//        exchange.getRequestHeaders().entrySet().forEach(AmardServer_14012019_b4_dynamic_graph.logger::info);
//        //AmardServer.logger.info("Request received from IP: " + exchange.getRemoteAddress().getAddress());
//    }
//
//    public String getRequestBody(InputStream inputStream) throws IOException {
//        StringBuilder stringBuilder = new StringBuilder();
//        String line = null;
//        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8.name()))) {
//            while ((line = bufferedReader.readLine()) != null) {
//                stringBuilder.append(line);
//            }
//        }
//        return stringBuilder.toString();
//    }
//
//    public void setQueryPairs(String query) throws UnsupportedEncodingException {
//        if (query != null) {
//            AmardServer_14012019_b4_dynamic_graph.logger.info(Thread.currentThread().getName() + " Query string received " + query);
//            //query = URLDecoder.decode(query, "UTF-8");
//            AmardServer_14012019_b4_dynamic_graph.logger.info(Thread.currentThread().getName() + " Decoded query string received " + query);
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
//    public void setHeaders(HttpExchange exchange) {
//        Headers responseHeaders = exchange.getResponseHeaders();
//        responseHeaders.set("Content-Type", "application/json");
//        responseHeaders.set("Access-Control-Allow-Origin", "*");
//    }
//
//    //http://localhost:8496/amard/graph/momo?network=MTN&txn_type=C&interval=10
//    // graph is the function
//    public void router(String path, HttpExchange exchange) {
//        try {
//            String requestBody = getRequestBody(exchange.getRequestBody());
//            AmardServer_14012019_b4_dynamic_graph.logger.info(Thread.currentThread().getName() + " Request body received " + requestBody);
//            setQueryPairs(exchange.getRequestURI().getQuery());
//            setHeaders(exchange);
//            AmardServer_14012019_b4_dynamic_graph.logger.info(Thread.currentThread().getName() + " Request path received => " + path);
//            String s[] = path.split("/");
//            String contextPath = s[1];
//            String function = s[2];
//            String filter = "";
//            AmardServer_14012019_b4_dynamic_graph.logger.info(Thread.currentThread().getName() + " Function to process " + function);
//
//            switch (function) {
//                case "monitorlogs":
//                    filter = s[3];
//                    monitorLogs(exchange, filter);
//                    break;
//                case "graph":
//                    filter = s[3];
//                    switch (filter) {
//                        case "momo":
//                            momoGraphProcessor(exchange);
//                            break;
//                        default:
//                            AmardServer_14012019_b4_dynamic_graph.logger.info(Thread.currentThread().getName() + " No processor set up for path " + filter);
//                            exchange.sendResponseHeaders(404, 0);
//                    }
//                    break;
//                default:
//                    AmardServer_14012019_b4_dynamic_graph.logger.info(Thread.currentThread().getName() + " No processor set up for path " + function);
//                    exchange.sendResponseHeaders(404, 0);
//
//            }
//        } catch (ArrayIndexOutOfBoundsException ex) {
//            String message = "No route found for path " + path;
//            AmardServer_14012019_b4_dynamic_graph.logger.error(Thread.currentThread().getName() + message, ex);
//            try (OutputStream os = exchange.getResponseBody()) {
//                exchange.sendResponseHeaders(404, 0);
//                os.write(message.getBytes());
//                os.flush();
//            } catch (IOException ex1) {
//                AmardServer_14012019_b4_dynamic_graph.logger.error(Thread.currentThread().getName() + " Something wrong ", ex1);
//            }
//        } catch (IOException ex) {
//            String message = "An error occured reading body content";
//            AmardServer_14012019_b4_dynamic_graph.logger.error(Thread.currentThread().getName() + message, ex);
//            try (OutputStream os = exchange.getResponseBody()) {
//                exchange.sendResponseHeaders(500, message.getBytes().length);
//                os.write(message.getBytes());
//                os.flush();
//            } catch (IOException ex1) {
//
//            }
//        } catch (Exception ex) {
//            String message = "Internal server error ocurred";
//            AmardServer_14012019_b4_dynamic_graph.logger.error(Thread.currentThread().getName() + message, ex);
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
//    public void monitorLogs(HttpExchange exchange, String filter) throws IOException {
//        setHeaders(exchange);
//        String response = "";
//        List<ResponseTable> list = ctrl.getLogRecordsByType(filter);
//        response = new JSONArray(list).toString();
//        try (OutputStream os = exchange.getResponseBody()) {
//            exchange.sendResponseHeaders(200, response.getBytes().length);
//            os.write(response.getBytes());
//            os.flush();
//        }
//    }
//
//    private void momoGraphProcessor(HttpExchange exchange) throws IOException {
//        setHeaders(exchange);
//        String action = getQueryParam("action");
//        switch (action) {
//            case "tat":
//                momoLiveTAT(exchange);
//                break;
//            default:
//                AmardServer_14012019_b4_dynamic_graph.logger.info(Thread.currentThread().getName() + " No processor set up for path " + action);
//                exchange.sendResponseHeaders(404, 0);
//                exchange.getResponseBody().flush();
//        }
//    }
//
//    private void momoLiveTAT(HttpExchange exchange) throws IOException {
//        setHeaders(exchange);
//        String network = getQueryParam("network");
//        String txn_type = getQueryParam("txn_type");
//        int interval = Integer.parseInt(getQueryParam("interval"));
//        String response = ctrl.getMomoLiveGraphData(network, txn_type, interval);
//        try (OutputStream os = exchange.getResponseBody()) {
//            exchange.sendResponseHeaders(200, response.getBytes().length);
//            os.write(response.getBytes());
//            os.flush();
//        } catch (NullPointerException e) {
//            response = "Invalid request received";
//            exchange.sendResponseHeaders(400, response.getBytes().length);
//            exchange.getResponseBody().write(response.getBytes());
//            exchange.getResponseBody().flush();
//        }
//    }
//
//}
