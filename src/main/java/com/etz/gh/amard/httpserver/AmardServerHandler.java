package com.etz.gh.amard.httpserver;

import com.etz.gh.amard.controller.AmardController;
import com.etz.gh.amard.entities.Log;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONArray;

/**
 *
 * @author seth.sebeh
 */
public class AmardServerHandler implements HttpHandler {

    static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(AmardServerHandler.class);

    static {
        PropertyConfigurator.configure("cfg\\log4j.config");
    }

    AmardController ctrl = new AmardController();
    private Map<String, String> query_pairs = new LinkedHashMap<>();

    public void handle(HttpExchange exchange) throws IOException {
        long start = System.currentTimeMillis();
        logger.info(Thread.currentThread().getName() + " NEW REQUEST IN <<<<<<<<< <<<<<<<<< <<<<<<<<< ");
        String requestorIP = exchange.getRemoteAddress().getAddress().getHostAddress();
        logger.info(Thread.currentThread().getName() + " REQUESTOR IP " + requestorIP);
        //printRequestHeaders(exchange);
        router(exchange.getRequestURI().getPath(), exchange);
        logger.info(Thread.currentThread().getName() + " Time to process request " + (System.currentTimeMillis() - start) + "ms");
        logger.info(Thread.currentThread().getName() + " <<<<<<<<< <<<<<<<<< <<<<<<<<< END OF REQUEST");
        exchange.close();
    }

    public void printRequestHeaders(HttpExchange exchange) {
        logger.info(Thread.currentThread().getName() + " -- headers --");
        exchange.getRequestHeaders().entrySet().forEach(logger::info);
        //logger.info("Request received from IP: " + exchange.getRemoteAddress().getAddress());
    }

    public String getRequestBody(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8.name()))) {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        return stringBuilder.toString();
    }

    public void setQueryPairs(String query) throws UnsupportedEncodingException {
        if (query != null) {
            logger.info(Thread.currentThread().getName() + " Query string received " + query);
            //query = URLDecoder.decode(query, "UTF-8");
            logger.info(Thread.currentThread().getName() + " Decoded query string received " + query);
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                final String value = idx > 0 && pair.length() > idx + 1 ? pair.substring(idx + 1) : "";
                this.query_pairs.put(pair.substring(0, idx), value);
            }
        }
    }

    public String getQueryParam(String key) {
        return this.query_pairs.get(key);
    }

    public void setHeaders(HttpExchange exchange) {
        Headers responseHeaders = exchange.getResponseHeaders();
        responseHeaders.set("Content-Type", "application/json");
        responseHeaders.set("Access-Control-Allow-Origin", "*");
    }

    //http://localhost:8496/amard/graph/momolivetat
    // graph is the function
    public void router(String path, HttpExchange exchange) {
        try {
            String requestBody = getRequestBody(exchange.getRequestBody());
            logger.info(Thread.currentThread().getName() + " Request body received " + requestBody);
            setQueryPairs(exchange.getRequestURI().getQuery());
            setHeaders(exchange);
            logger.info(Thread.currentThread().getName() + " Request path received => " + path);
            String s[] = path.split("/");
//            String contextPath = s[1];
            String function = s[2];
            String filter = "";
            logger.info(Thread.currentThread().getName() + " Function to process " + function);

            switch (function) {
                case "monitorlogs":
                    filter = s[3];
                    monitorLogs(exchange, filter);
                    break;
                case "boamonitorlogs":
                    filter = s[3];
                    boamonitorLogs(exchange, filter);
                    break;
                case "gcbmonitorlogs":
                    filter = s[3];
                    gcbmonitorLogs(exchange, filter);
                    break;
                case "graph":
                    filter = s[3];
                    switch (filter) {
                        case "momolivetat":
                            momoliveDynamicGraphDataGenerator("MOMO LIVE TAT", exchange);
                            break;                        
                        case "momoliveerrors":
                            momoliveDynamicGraphDataGenerator("MOMO ERRORS", exchange);
                            break;
                        case "momolivetable":
                            momoliveDynamicGraphDataGenerator("MOMO DATABASE", exchange);
                            break;
                        case "gcblivetat":
                            momoliveDynamicGraphDataGenerator("GCB LIVE TAT", exchange);
                            break;
                        case "gcbliveerrors":
                            momoliveDynamicGraphDataGenerator("GCB ERRORS", exchange);
                            break;
                        case "gcblivetatdelayed":
                            momoliveDynamicGraphDataGenerator("GCB LIVE TAT DELAYED", exchange);
                            break;
                        case "gcbtmclivetat":
                            momoliveDynamicGraphDataGenerator("GCB TMC Transactions Live TAT", exchange);
                            break;
                        case "gcbtmclivetable":
                            momoliveDynamicGraphDataGenerator("GCB TMC  DATABASE", exchange);
                            break;
                        case "b2ww2b":
                            momoliveDynamicGraphDataGenerator("B2W W2B", exchange);
                            break;
                        case "B2WW2BBOA":
                            momoliveDynamicGraphDataGenerator("B2W W2B BOA", exchange);
                            break;
                        case "B2WW2BBARCLAYS":
                            momoliveDynamicGraphDataGenerator("B2W W2B BARCLAYS", exchange);
                            break;
                        case "fglivetat":
                            momoliveDynamicGraphDataGenerator("FUNDGATE LIVE TAT", exchange);
                            break;
                        case "fgliveerrors":
                            momoliveDynamicGraphDataGenerator("FUNDGATE ERRORS", exchange);
                            break;
                        case "fgliveleague":
                            momoliveDynamicGraphDataGenerator("FUNDGATE LEAGUE", exchange);
                            break;
                        case "momo24hrseagraph":
                            momo24hrseagraphDynamicGraphDataGenerator(exchange);
                            break;
                        case "GCBReportMobileMoneyAll":
                            momoliveDynamicGraphDataGenerator("GCB MOBILE MONEY REPORT ALL", exchange);
                            break;
                        case "GCBReportB2WVsW2B":
                            momoliveDynamicGraphDataGenerator("GCB B2W Vs W2B REPORT", exchange);
                            break;
                        case "GCBReportMobileMoneyB2W":
                            momoliveDynamicGraphDataGenerator("GCB MOBILE MONEY REPORT B2W", exchange);
                            break;
                        case "GCBReportMobileMoneyW2B":
                            momoliveDynamicGraphDataGenerator("GCB MOBILE MONEY REPORT W2B", exchange);
                            break;
                        case "GCBReportAirtime":
                            momoliveDynamicGraphDataGenerator("GCB AIRTIME REPORT", exchange);
                            break;
                        case "GCBReportAirtimeByNetwork":
                            momoliveDynamicGraphDataGenerator("GCB AIRTIME REPORT BY NETWORK", exchange);
                            break;
                        case "GCBReportBillPayment":
                            momoliveDynamicGraphDataGenerator("GCB BILL PAYMENT REPORT", exchange);
                            break;
                        case "GCBReportBillPaymentByBiller":
                            momoliveDynamicGraphDataGenerator("GCB BILL PAYMENT REPORT BY BILLER", exchange);
                            break;
                        case "GCBReportMobileMoneyByNetworkAll":
                            momoliveDynamicGraphDataGenerator("GCB MOBILE MONEY REPORT BY NETWORK ALL", exchange);
                            break;
                        case "GCBReportMobileMoneyByNetworkB2W":
                            momoliveDynamicGraphDataGenerator("GCB MOBILE MONEY REPORT BY NETWORK B2W", exchange);
                            break;
                        case "GCBReportMobileMoneyByNetworkW2B":
                            momoliveDynamicGraphDataGenerator("GCB MOBILE MONEY REPORT BY NETWORK W2B", exchange);
                            break;
                        case "GCBDepositMobilization":
                            momoliveDynamicGraphDataGenerator("GCB DEPOSIT MOBILIZATION", exchange);
                            break;
                        case "FundGateReportAll":
                            momoliveDynamicGraphDataGenerator("FUNDGATE REPORT ALL", exchange);
                            break;
                        case "FundGateReportByMerchantAll":
                            momoliveDynamicGraphDataGenerator("FUNDGATE REPORT BY MERCHANT ALL", exchange);
                            break;
                        case "FundGateReportByTransactionType":
                            momoliveDynamicGraphDataGenerator("FUNDGATE REPORT BY TRANSACTION TYPE", exchange);
                            break;
                        case "TerraPayReport":
                            momoliveDynamicGraphDataGenerator("TERRA PAY REPORT", exchange);
                            break;
                        case "GMoneyReport":
                            momoliveDynamicGraphDataGenerator("G-MONEY REPORT", exchange);
                            break;
                        case "GMoneyUSSDTrafficReport":
                            momoliveDynamicGraphDataGenerator("G-MONEY USSD TRAFFIC", exchange);
                            break;
                        case "FundGateMerchantReport":
                            momoliveDynamicGraphDataGenerator("FUNDGATE MERCHANT REPORT", exchange);
                            break;
                        case "MobileMoneyReport":
                            momoliveDynamicGraphDataGenerator("MOBILE MONEY REPORT", exchange);
                            break;
                        case "MobileMoneyReportCredit":
                            momoliveDynamicGraphDataGenerator("MOBILE MONEY REPORT CREDIT", exchange);
                            break;
                        case "MobileMoneyReportDebit":
                            momoliveDynamicGraphDataGenerator("MOBILE MONEY REPORT DEBIT", exchange);
                            break;
                        case "MobileMoneyReportCreditVsDebit":
                            momoliveDynamicGraphDataGenerator("MOBILE MONEY REPORT CREDIT VS DEBIT", exchange);
                            break;
                        case "MobileMoneyReportByNetwork":
                            momoliveDynamicGraphDataGenerator("MOBILE MONEY REPORT BY NETWORK", exchange);
                            break;
                        case "MobileMoneyReportByMerchant":
                            momoliveDynamicGraphDataGenerator("MOBILE MONEY REPORT BY MERCHANT", exchange);
                            break;
                        case "BillPaymentReport":
                            momoliveDynamicGraphDataGenerator("BILL PAYMENT REPORT", exchange);
                            break;
                        case "BillPaymentReportByBiller":
                            momoliveDynamicGraphDataGenerator("BILL PAYMENT REPORT BY BILLER", exchange);
                            break;
                        case "BillPaymentBillerReport":
                            momoliveDynamicGraphDataGenerator("BILL PAYMENT BILLER REPORT", exchange);
                            break;
                        case "AirtimeReport":
                            momoliveDynamicGraphDataGenerator("AIRTIME REPORT", exchange);
                            break;
                        case "AirtimeReportByNetwork":
                            momoliveDynamicGraphDataGenerator("AIRTIME REPORT BY NETWORK", exchange);
                            break;
                        case "USSDReportByNetwork":
                            momoliveDynamicGraphDataGenerator("USSD REPORT BY NETWORK", exchange);
                            break;
                        case "TMCNodesConnectionStatus":
                            momoliveDynamicGraphDataGenerator("TMC NODES STATUS", exchange);
                            break;
                        case "TMCAverageResponseTimeLast10minutes":
                            momoliveDynamicGraphDataGenerator("TMC AVERAGE RESPONSE TIME LAST 10 MINUTES", exchange);
                            break;
                        case "GCBTMCAverageResponseTimeLast5minutes":
                            momoliveDynamicGraphDataGenerator("GCB TMC AVERAGE RESPONSE TIME LAST 5 MINUTES", exchange);
                            break;
                        case "GCBTransactionCount":
                            momoliveDynamicGraphDataGenerator("GCB TRANSACTION COUNT", exchange);
                            break;
                        case "TMCAverageResponseTime":
                            momoliveDynamicGraphDataGenerator("TMC AVERAGE RESPONSE TIME", exchange);
                            break;
                        case "GCBTMCAverageResponseTime":
                            momoliveDynamicGraphDataGenerator("GCB TMC AVERAGE RESPONSE TIME", exchange);
                            break;
                        case "BOAReportMobileMoneyAll":
                            momoliveDynamicGraphDataGenerator("BOA MOBILE MONEY REPORT ALL", exchange);
                            break;
                        case "BOAReportB2WVsW2B":
                            momoliveDynamicGraphDataGenerator("BOA B2W Vs W2B REPORT", exchange);
                            break;
                        case "BOAReportMobileMoneyB2W":
                            momoliveDynamicGraphDataGenerator("BOA MOBILE MONEY REPORT B2W", exchange);
                            break;
                        case "BOAReportMobileMoneyW2B":
                            momoliveDynamicGraphDataGenerator("BOA MOBILE MONEY REPORT W2B", exchange);
                            break;
                        case "BOAReportAirtime":
                            momoliveDynamicGraphDataGenerator("BOA AIRTIME REPORT", exchange);
                            break;
                        case "BOAReportAirtimeByNetwork":
                            momoliveDynamicGraphDataGenerator("BOA AIRTIME REPORT BY NETWORK", exchange);
                            break;
                        case "BOAReportBillPayment":
                            momoliveDynamicGraphDataGenerator("BOA BILL PAYMENT REPORT", exchange);
                            break;
                        case "BOAReportBillPaymentByBiller":
                            momoliveDynamicGraphDataGenerator("BOA BILL PAYMENT REPORT BY BILLER", exchange);
                            break;
                        case "BOAReportMobileMoneyByNetworkAll":
                            momoliveDynamicGraphDataGenerator("BOA MOBILE MONEY REPORT BY NETWORK ALL", exchange);
                            break;
                        case "BOAReportMobileMoneyByNetworkB2W":
                            momoliveDynamicGraphDataGenerator("BOA MOBILE MONEY REPORT BY NETWORK B2W", exchange);
                            break;
                        case "BOAReportMobileMoneyByNetworkW2B":
                            momoliveDynamicGraphDataGenerator("BOA MOBILE MONEY REPORT BY NETWORK W2B", exchange);
                            break;
                        default:
                            logger.info(Thread.currentThread().getName() + " No processor set up for path " + filter);
                            exchange.sendResponseHeaders(404, 0);
                    }
                case "dropped":
                    filter = s[3];
                    switch (filter) {
                        case "momocredit":
                            getMomoCreditDroppedTxn(exchange, filter);
                            break;
                        case "momocreditcount":
                            countMomoCreditDroppedTxn(exchange, filter);
                            break;
                        case "momocreditsum":
                            sumMomoCreditDroppedTxn(exchange, filter);
                            break;
                        case "fgservererror99":
                            getFundgateServerError99Transactions(exchange, path);
                            break;
                        case "fgservererror99count":
                            countFundgateServerError99Transactions(exchange, path);
                            break;
                        case "fgservererror99sum":
                            sumFundgateServerError99Transactions(exchange, path);
                            break;
                    }
                    break;
                case "applogs":
                    getAppLogs(exchange, filter);
                    break;
                case "boaapplogs":
                    getBOAAppLogs(exchange, filter);
                    break;
                case "gcbapplogs":
                    getGCBAppLogs(exchange, filter);
                    break;
                case "fgmerchants":
                    getFgMerchants(exchange, filter);
                    break;
                case "vasbillers":
                    getVasGateBillers(exchange, filter);
                    break;
                case "gcbsocketerrors":
                    filter = s[3];
                    getGCBSocketErrors(exchange, filter);
                    break;
                case "alarmlogs":
                    getAlarmLogs(exchange);
                    break;
                default:
                    logger.info(Thread.currentThread().getName() + " No processor set up for path " + function);
                    exchange.sendResponseHeaders(404, 0);
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            String message = "No route found for path " + path;
            logger.error(Thread.currentThread().getName() + message, ex);
            try (OutputStream os = exchange.getResponseBody()) {
                exchange.sendResponseHeaders(404, 0);
                os.write(message.getBytes());
                os.flush();
            } catch (IOException ex1) {
                logger.error(Thread.currentThread().getName() + " Something wrong ", ex1);
            }
        } catch (IOException ex) {
            String message = "An error occured reading body content";
            logger.error(Thread.currentThread().getName() + message, ex);
            try (OutputStream os = exchange.getResponseBody()) {
                exchange.sendResponseHeaders(500, message.getBytes().length);
                os.write(message.getBytes());
                os.flush();
            } catch (IOException ex1) {

            }
        } catch (Exception ex) {
            String message = "Internal server error ocurred";
            logger.error(Thread.currentThread().getName() + " " + message, ex);
            try (OutputStream os = exchange.getResponseBody()) {
                exchange.sendResponseHeaders(500, message.getBytes().length);
                os.write(message.getBytes());
                os.flush();
            } catch (IOException ex1) {

            }
        }
    }

    private void monitorLogs(HttpExchange exchange, String filter) throws IOException {
        setHeaders(exchange);
        String response = "";
        List<Log> list = ctrl.getLogRecordsByType(filter);
        response = new JSONArray(list).toString();
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(200, response.getBytes().length);
            os.write(response.getBytes());
            os.flush();
        }
    }

    private void boamonitorLogs(HttpExchange exchange, String filter) throws IOException {
        setHeaders(exchange);
        String response = "";
        List<Log> list = ctrl.getBoaLogRecordsByType(filter);
        response = new JSONArray(list).toString();
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(200, response.getBytes().length);
            os.write(response.getBytes());
            os.flush();
        }
    }

    private void gcbmonitorLogs(HttpExchange exchange, String filter) throws IOException {
        setHeaders(exchange);
        String response = "";
        List<Log> list = ctrl.getGCBLogRecordsByType(filter);
        response = new JSONArray(list).toString();
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(200, response.getBytes().length);
            os.write(response.getBytes());
            os.flush();
        }
    }

    private void momoliveDynamicGraphDataGenerator(String group, HttpExchange exchange) throws IOException {
        setHeaders(exchange);
        String response = ctrl.getMomoLiveGraphData(group, getQueryParam("start"), getQueryParam("end"), getQueryParam("ext1"));
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(200, response.getBytes().length);
            os.write(response.getBytes());
            os.flush();
        } catch (NullPointerException e) {
            response = "Invalid request received";
            exchange.sendResponseHeaders(400, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().flush();
        }
    }

    private void momo24hrseagraphDynamicGraphDataGenerator(HttpExchange exchange) throws IOException {
        setHeaders(exchange);
        String response = "";
        //String response = ctrl.getMomo24hrseaGraphData();
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(200, response.getBytes().length);
            os.write(response.getBytes());
            os.flush();
        } catch (NullPointerException e) {
            response = "Invalid request received";
            exchange.sendResponseHeaders(400, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().flush();
        }
    }

    private void getMomoCreditDroppedTxn(HttpExchange exchange, String filter) throws IOException {
        setHeaders(exchange);
        String response = ctrl.getMomoCreditDroppedTxn(getQueryParam("start"), getQueryParam("end"));
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(200, response.getBytes().length);
            os.write(response.getBytes());
            os.flush();
        }
    }

    private void countMomoCreditDroppedTxn(HttpExchange exchange, String filter) throws IOException {
        setHeaders(exchange);
        String response = String.valueOf(ctrl.countMomoCreditDroppedTxn(getQueryParam("start"), getQueryParam("end")));
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(200, response.getBytes().length);
            os.write(response.getBytes());
            os.flush();
        }
    }

    private void sumMomoCreditDroppedTxn(HttpExchange exchange, String filter) throws IOException {
        setHeaders(exchange);
        String response = String.valueOf(ctrl.sumMomoCreditDroppedTxn(getQueryParam("start"), getQueryParam("end")));
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(200, response.getBytes().length);
            os.write(response.getBytes());
            os.flush();
        }
    }

    private void getFundgateServerError99Transactions(HttpExchange exchange, String filter) throws IOException {
        setHeaders(exchange);
        String response = ctrl.getFundgateServerError99Transactions(getQueryParam("start"), getQueryParam("end"));
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(200, response.getBytes().length);
            os.write(response.getBytes());
            os.flush();
        }
    }

    private void countFundgateServerError99Transactions(HttpExchange exchange, String filter) throws IOException {
        setHeaders(exchange);
        String response = String.valueOf(ctrl.countFundgateServerError99Transactions(getQueryParam("start"), getQueryParam("end")));
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(200, response.getBytes().length);
            os.write(response.getBytes());
            os.flush();
        }
    }

    private void sumFundgateServerError99Transactions(HttpExchange exchange, String filter) throws IOException {
        setHeaders(exchange);
        String response = String.valueOf(ctrl.sumFundgateServerError99Transactions(getQueryParam("start"), getQueryParam("end")));
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(200, response.getBytes().length);
            os.write(response.getBytes());
            os.flush();
        }
    }

    private void getAppLogs(HttpExchange exchange, String filter) throws IOException {
        setHeaders(exchange);
        String response = ctrl.getApplogs();
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(200, response.getBytes().length);
            os.write(response.getBytes());
            os.flush();
        }
    }

    private void getBOAAppLogs(HttpExchange exchange, String filter) throws IOException {
        setHeaders(exchange);
        String response = ctrl.getBOAApplogs();
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(200, response.getBytes().length);
            os.write(response.getBytes());
            os.flush();
        }
    }

    private void getGCBAppLogs(HttpExchange exchange, String filter) throws IOException {
        setHeaders(exchange);
        String response = ctrl.getGCBApplogs();
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(200, response.getBytes().length);
            os.write(response.getBytes());
            os.flush();
        }
    }

    private void getFgMerchants(HttpExchange exchange, String filter) throws IOException {
        setHeaders(exchange);
        String response = ctrl.getFgMerchants();
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(200, response.getBytes().length);
            os.write(response.getBytes());
            os.flush();
        }
    }

    private void getVasGateBillers(HttpExchange exchange, String filter) throws IOException {
        setHeaders(exchange);
        String response = ctrl.getVasGateBillers();
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(200, response.getBytes().length);
            os.write(response.getBytes());
            os.flush();
        }
    }
    
    private void getGCBSocketErrors(HttpExchange exchange, String filter) throws IOException {
        setHeaders(exchange);
        String response = ctrl.getGCBSocketErrors(filter, getQueryParam("start"), getQueryParam("end"));
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(200, response.getBytes().length);
            os.write(response.getBytes());
            os.flush();
        }
    }
    
    private void getAlarmLogs(HttpExchange exchange) throws IOException {
       setHeaders(exchange);
        String response = ctrl.getAlarmLogs();
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(200, response.getBytes().length);
            os.write(response.getBytes());
            os.flush();
        }
    }

}
