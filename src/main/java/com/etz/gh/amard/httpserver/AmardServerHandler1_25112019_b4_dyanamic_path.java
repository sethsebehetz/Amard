//package com.etz.gh.amard.httpserver;
//
//import com.etz.gh.amard.controller.AmardController;
//import com.etz.gh.amard.entities.ResponseTable;
//import com.sun.net.httpserver.Headers;
//import com.sun.net.httpserver.HttpExchange;
//import com.sun.net.httpserver.HttpHandler;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.io.UnsupportedEncodingException;
//import java.nio.charset.StandardCharsets;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//import org.apache.log4j.PropertyConfigurator;
//import org.json.JSONArray;
//
///**
// *
// * @author seth.sebeh
// */
//public class AmardServerHandler1_25112019_b4_dyanamic_path implements HttpHandler{
//    
//    static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(AmardServerHandler1_25112019_b4_dyanamic_path.class);
//
//    static {
//        PropertyConfigurator.configure("cfg\\log4j.config");
//    }
//
//    AmardController ctrl = new AmardController();
//    private Map<String, String> query_pairs = new LinkedHashMap<>();
//
//    public void handle(HttpExchange exchange) throws IOException {
//        long start = System.currentTimeMillis();
//        logger.info(Thread.currentThread().getName() + " NEW REQUEST IN <<<<<<<<< <<<<<<<<< <<<<<<<<< ");
//        String requestorIP = exchange.getRemoteAddress().getAddress().getHostAddress();
//        logger.info(Thread.currentThread().getName() + " REQUESTOR IP " + requestorIP);
//        //printRequestHeaders(exchange);
//        router(exchange.getRequestURI().getPath(), exchange);
//        logger.info(Thread.currentThread().getName() + " Time to process request " + (System.currentTimeMillis() - start) + "ms");
//        logger.info(Thread.currentThread().getName() + " <<<<<<<<< <<<<<<<<< <<<<<<<<< END OF REQUEST");
//        exchange.close();
//    }
//
//    public void printRequestHeaders(HttpExchange exchange) {
//        logger.info(Thread.currentThread().getName() + " -- headers --");
//        exchange.getRequestHeaders().entrySet().forEach(logger::info);
//        //logger.info("Request received from IP: " + exchange.getRemoteAddress().getAddress());
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
//            logger.info(Thread.currentThread().getName() + " Query string received " + query);
//            //query = URLDecoder.decode(query, "UTF-8");
//            logger.info(Thread.currentThread().getName() + " Decoded query string received " + query);
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
//    //http://localhost:8496/amard/graph/momolivetat
//    // graph is the function
//    public void router(String path, HttpExchange exchange) {
//        try {
//            String requestBody = getRequestBody(exchange.getRequestBody());
//            logger.info(Thread.currentThread().getName() + " Request body received " + requestBody);
//            setQueryPairs(exchange.getRequestURI().getQuery());
//            setHeaders(exchange);
//            logger.info(Thread.currentThread().getName() + " Request path received => " + path);
//            String s[] = path.split("/");
//            String contextPath = s[1];
//            String function = s[2];
//            String filter = "";
//            logger.info(Thread.currentThread().getName() + " Function to process " + function);
//
//            switch (function) {
//                case "monitorlogs":
//                    filter = s[3];
//                    monitorLogs(exchange, filter);
//                    break;
//                case "graph":
//                    filter = s[3];
//                    switch (filter) {
//                        case "momolivetat":
//                            momoliveDynamicGraphDataGenerator("MOMO LIVE TAT", exchange);
//                            break;
//                        case "momoliveerrors":
//                            momoliveDynamicGraphDataGenerator("MOMO ERRORS", exchange);
//                            break;
//                        case "momolivetable":
//                            momoliveDynamicGraphDataGenerator("MOMO DATABASE", exchange);
//                            break;
//                        case "momoliveleague":
//                            momoliveDynamicGraphDataGenerator("MOMO LEAGUE", exchange);
//                            break;
//                        case "momo24hrseagraph":
//                            momo24hrseagraphDynamicGraphDataGenerator(exchange);
//                            break;
//                        default:
//                            logger.info(Thread.currentThread().getName() + " No processor set up for path " + filter);
//                            exchange.sendResponseHeaders(404, 0);
//                    }
//                    break;
//                default:
//                    logger.info(Thread.currentThread().getName() + " No processor set up for path " + function);
//                    exchange.sendResponseHeaders(404, 0);
//            }
//        } catch (ArrayIndexOutOfBoundsException ex) {
//            String message = "No route found for path " + path;
//            logger.error(Thread.currentThread().getName() + message, ex);
//            try (OutputStream os = exchange.getResponseBody()) {
//                exchange.sendResponseHeaders(404, 0);
//                os.write(message.getBytes());
//                os.flush();
//            } catch (IOException ex1) {
//                logger.error(Thread.currentThread().getName() + " Something wrong ", ex1);
//            }
//        } catch (IOException ex) {
//            String message = "An error occured reading body content";
//            logger.error(Thread.currentThread().getName() + message, ex);
//            try (OutputStream os = exchange.getResponseBody()) {
//                exchange.sendResponseHeaders(500, message.getBytes().length);
//                os.write(message.getBytes());
//                os.flush();
//            } catch (IOException ex1) {
//
//            }
//        } catch (Exception ex) {
//            String message = "Internal server error ocurred";
//            logger.error(Thread.currentThread().getName() + " " + message, ex);
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
//    private void momoliveDynamicGraphDataGenerator(String group, HttpExchange exchange) throws IOException {
//        setHeaders(exchange);
//        String response = ctrl.getMomoLiveGraphData(group, getQueryParam("start"), getQueryParam("end"));
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
//    private void momo24hrseagraphDynamicGraphDataGenerator(HttpExchange exchange) throws IOException {
//        setHeaders(exchange);
//        String response = "";
//        //String response = ctrl.getMomo24hrseaGraphData();
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
