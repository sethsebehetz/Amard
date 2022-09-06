package com.etz.gh.amard.utilities;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import javax.net.ssl.HostnameVerifier;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import org.apache.log4j.Logger;

public class DoRequest {

    static final Logger l = Logger.getLogger(DoRequest.class);
    //private static String tloc = "D:\\GCB\\G-MONEY\\huawei requests\\server_keystore.ks";
    private static String tloc = "cfg\\gmoney_ws_server_keystore.ks";
    private static String url = "https://172.16.30.9:8550/receiver/gcbmomomw";

    //static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(DoRequest.class);
    public static void main(String[] args) throws Exception {
        String url = "http://localhost:8866/vodabill?action=process&account=test03";
        String data = "dataToSend";

        DoRequest http = new DoRequest();

        l.info("Testing 1 - Send Http GET request");
       // http.sendGet(url);

        l.info("\nTesting 2 - Send Http POST request");
        //l.info(DoRequest.sendGet(url));
        
//        String xml = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"http://ws.fundgate.etranzact.com/\">\n" +
//"   <soapenv:Header/>\n" +
//"   <soapenv:Body>\n" +
//"      <ws:process>\n" +
//"         <request>\n" +
//"            <action>BE</action>\n" +
//"            <id>1</id>\n" +
//"            <terminalId>0060000244</terminalId>\n" +
//"            <transaction>\n" +
//"               <pin>Ew81GF1im1w/lnK0/wc5Ow==</pin>\n" +
//"               <reference>TESTACCESdSPINSETH_11</reference>\n" +
//"            </transaction>\n" +
//"         </request>\n" +
//"      </ws:process>\n" +
//"   </soapenv:Body>\n" +
//"</soapenv:Envelope>";
        
        String xml = "<?xml version='1.0' encoding='UTF-8'?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"http://ws.fundgate.etranzact.com/\"> <soapenv:Header/><soapenv:Body><ws:process><request><action>BE</action><id>1</id><terminalId>0060000244</terminalId><transaction><pin>wkHBKcduFasNdYqD0TGG3Q==</pin><reference>AMD26480485423</reference></transaction></request></ws:process></soapenv:Body></soapenv:Envelope>";
        
        System.out.println(sendPostSSL("https://webpay.etranzactgh.com/FundGate3.0/ws", xml, "cfg\\fg_keystore.ks", "123456"));

    }

    // HTTP GET request
    public static String sendGet(String url) {
        StringBuffer response = null;
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            //con.setRequestProperty("User-Agent", USER_AGENT);
            int responseCode = con.getResponseCode();
            l.info("\nSending 'GET' request to URL : " + url);
            l.info("Response Code : " + responseCode);

            BufferedReader in = null;
            if (responseCode < HttpURLConnection.HTTP_BAD_REQUEST) {
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

        } catch (Exception ex) {
            l.error("Sorry something wrong, ex");
        }

        return response.toString();
    }

    // HTTP POST request
    public String sendPost(String url, String data) throws Exception {

        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add reqest header
        con.setRequestMethod("POST");
        //con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Content-Type", "application/json");
        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(data);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        l.info("\nSending 'POST' request to URL : " + url);
        l.info("Post data : " + data);
        l.info("Response Code : " + responseCode);

        BufferedReader in = null;
        if (responseCode < HttpURLConnection.HTTP_BAD_REQUEST) {
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }

        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        return response.toString();

    }

    public static String sendPostSSL(String url, String payload, String trustStoreLoc, String trustStorePass) throws Exception {
        long start = System.currentTimeMillis();
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add reqest header
        con.setRequestMethod("POST");
        //con.setRequestProperty("User-Agent", USER_AGENT);
        //con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        //con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Content-Type", "text/xml");
        // Send post request
        con.setDoOutput(true);
        con.setSSLSocketFactory(getSocketFactory(trustStoreLoc, trustStorePass));
        con.setHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String host, SSLSession sess) {
                return true;
            }
        });
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(payload);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        l.info("\nSending 'POST' request to URL : " + url);
        l.info("Post data : " + payload);
        l.info("Response Code : " + responseCode);

        BufferedReader in = null;
        if (responseCode < HttpURLConnection.HTTP_BAD_REQUEST) {
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }

        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        String r = response.toString();
        l.info("RESPONSE BODY >>" + r);
        l.info("TAT [CONNECT+DATA] >> " + (System.currentTimeMillis() - start));
        return r;
    }

    //one way ssl set up
    private static SSLSocketFactory getSocketFactory(String trustStoreLoc, String trustStorePass) {
        try {
            final KeyStore trustStore = KeyStore.getInstance("JKS");
            try (final InputStream is = new FileInputStream(trustStoreLoc)) {
                trustStore.load(is, trustStorePass.toCharArray());
            }
            final TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(trustStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), new java.security.SecureRandom());

            //SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new String[]{"TLSv1", "TLSv1.1", "TLSv1.2"}, null, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            return sslContext.getSocketFactory();
        } catch (java.security.KeyStoreException e) {
            l.info("Error while creating SSL Factory.");
            return null;
        } catch (Exception e) {
            l.info("Error while creating SSL Factory.");
            e.printStackTrace(System.out);
        }
        return null;
    }

}
