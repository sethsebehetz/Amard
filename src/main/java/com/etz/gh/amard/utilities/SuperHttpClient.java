/*
 * ETZ.Dev.Team 2019
 */
package com.etz.gh.amard.utilities;

import com.etz.gh.amard.entities.Monitor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.security.KeyStore;
import java.util.Base64;
import java.util.Date;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManagerFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class SuperHttpClient {

    final static Logger logger = Logger.getLogger(SuperHttpClient.class);

    static {
        PropertyConfigurator.configure("cfg\\log4j.config");
    }

    static final int CONNECT_TIMEOUT = 60;
    static final int SOCKET_TIMEOUT = 60;
    static final boolean LOGGING_ENABLED = true;
    final static boolean ENABLE_DEBUGGING = false; //turn off in production environment.

    public static void main(String[] args) {
        SuperHttpClient client = new SuperHttpClient();
        client.doGet(new Monitor(), "https://google.com", CONNECT_TIMEOUT, SOCKET_TIMEOUT);
    }

    //default timeout 30secs
    public static String doGet(Monitor service, String url) {
        return doGet(service, url, CONNECT_TIMEOUT, SOCKET_TIMEOUT);
    }

    public static String doGet(Monitor service, String url, int connectTimeout, int socketTimeout) {
        //CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
        RequestConfig config = RequestConfig.custom().setConnectTimeout(connectTimeout * 1000).setSocketTimeout(socketTimeout * 1000).build();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(config);

        if (service.getName().equalsIgnoreCase("PAYFLUID SERVICE")) {
            httpGet.addHeader("payReference", "11881");
        }
        //httpGet.addHeader("Content-type", "application/json");
        logger.info(service.getType() + "::" + service.getName() + ":: connecting to url >>" + url);
        return send(service, httpClient, httpGet);
    }

    //default timeout 30secs
    public static String doPost(Monitor service, String url, String payload) {
        logger.info("Payload " + payload);
        return doPost(service, url, payload, CONNECT_TIMEOUT, SOCKET_TIMEOUT);
    }

    public static String doPost(Monitor service, String url, String payload, int connectTimeout, int socketTimeout) {
        logger.info("Payload " + payload);
        //CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
        RequestConfig config = RequestConfig.custom().setConnectTimeout(connectTimeout * 1000).setSocketTimeout(socketTimeout * 1000).build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(config);
        StringEntity st = new StringEntity(payload, "UTF-8");
        st.setChunked(true);
        httpPost.setEntity(st);
        //httpPost.addHeader("Accept", "application/json");
        if (service.getName().equalsIgnoreCase("GCB NAME ENQUIRY") || service.getName().equalsIgnoreCase("G-MONEY NAME ENQUIRY")) {
            httpPost.addHeader("Content-type", "application/json; charset=utf8");
        }
         //httpPost.addHeader("Content-type", "application/json; charset=utf8");
        //httpPost.addHeader("Content-type", "text/xml");
        logger.info(service.getType() + "::" + service.getName() + ":: connecting to url >>" + url);
        return send(service, httpClient, httpPost);
    }

    //one way SSL authentication
    public static String doPostSSL(Monitor service, String url, String payload, int connectTimeout, int socketTimeout, String trustStoreLoc, String trustStorePass) {
        logger.info("Payload " + payload);
        return doPostSSL(service, url, payload, connectTimeout, socketTimeout, trustStoreLoc, trustStorePass, null, null);
    }

    //one way SSL authentication
    //default timeout of 30 seconds
    public static String doPostSSL(Monitor service, String url, String payload, String trustStoreLoc, String trustStorePass) {
        logger.info("Payload " + payload);
        return doPostSSL(service, url, payload, CONNECT_TIMEOUT, SOCKET_TIMEOUT, trustStoreLoc, trustStorePass, null, null);
    }

    //main ssl method - handles both oneway or two way authentication
    public static String doPostSSL(Monitor service, String url, String payload, int connectTimeout, int socketTimeout, String trustStoreLoc, String trustStorePass, String keystoreloc, String keyStorePass) {
        logger.info("Payload " + payload);
        SSLConnectionSocketFactory sslsf = null;
        if (keystoreloc == null) {
            //1 way
            sslsf = getSocketFactory(service, trustStoreLoc, trustStorePass);
        } else {
            //2 way
            sslsf = getSocketFactory(service, trustStoreLoc, trustStorePass, keystoreloc, keyStorePass);
        }
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
//      CloseableHttpClient httpClient = HttpClientBuilder.create().setSSLSocketFactory(sslsf).setRedirectStrategy(new LaxRedirectStrategy()).build();
        RequestConfig config = RequestConfig.custom().setConnectTimeout(connectTimeout * 1000).setSocketTimeout(socketTimeout * 1000).build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(config);
        StringEntity st = new StringEntity(payload, "UTF-8");
        st.setChunked(true);
        httpPost.setEntity(st);
        //httpPost.addHeader("Accept", "application/json");
        //httpPost.addHeader("Content-type", "application/json");
        //httpPost.addHeader("Content-type", "text/xml;charset=UTF-8");
        logger.info(service.getName());
        if (service.getGroup().equals("FUNDGATE")) {
            httpPost.addHeader("Content-Type", "text/xml;charset=UTF-8");
        }

        logger.info(service.getType() + "::" + service.getName() + ":: connecting to url >>" + url);
        return send(service, httpClient, httpPost);
    }

    //mutual authentication
    //default timeout of 30 seconds
    public static String doPostSSL(Monitor service, String url, String payload, String trustStoreLoc, String trustStorePass, String keystoreloc, String keyStorePass) {
        logger.info("Payload " + payload);
        return doPostSSL(service, url, payload, CONNECT_TIMEOUT, SOCKET_TIMEOUT, trustStoreLoc, trustStorePass, keystoreloc, keyStorePass);
    }

    public static String send(Monitor service, CloseableHttpClient httpClient, HttpRequestBase httpRequest) {
        Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

        String message = "";
        String requestTime = sdf.format(dt);
        CloseableHttpResponse response = null;
        int http_status = 0;
        long len = 0;

//      httpRequest.addHeader("Authorization", "Basic " + getBascicAuthStr("ETRANZACT", "ETRANZACT"));
        long start = System.currentTimeMillis();
        try {
            response = httpClient.execute(httpRequest);
            logger.info(service.getType() + "::" + service.getName() + ":: RESPONSE HEADERS >> " + response.toString());
            http_status = response.getStatusLine().getStatusCode();
            System.out.println("");
            //if (http_status >= 200 || http_status < 400) {
            //message = "CONNECTED  " + http_status;
            //}
            message = "CONNECTED. HTTP CODE " + http_status;
            HttpEntity entity = response.getEntity();
            String rspBody = EntityUtils.toString(entity);
            len = rspBody.length();
            logger.info(rspBody);
//          logger.info(service.getType()+ "::" +service.getName() + "::RESPONSE BODY >> " + rspBody);
        } catch (ConnectTimeoutException e) {
            message = "ERROR " + e.getMessage();
            logger.info(service.getType() + "::" + service.getName() + ":: CONNECT TIMEOUT >> Couldn't establish connection to the host server");
            logger.error("sorry, something wrong!", e);
        } catch (SocketTimeoutException e) {
            message = "ERROR " + e.getMessage();
            logger.info(service.getType() + "::" + service.getName() + ":: READ TIMEOUT >> Couldn't read data from the host server");
            logger.error("sorry, something wrong!", e);
        } catch (SSLHandshakeException e) {
            message = e.getMessage();
            //set httpstatus = 600 for all ssl handshake related issues
            http_status = 600;
            logger.info(service.getType() + "::" + service.getName() + ":: SSL HANDSHAKE EXCEPTION");
            logger.error("sorry, something wrong!", e);
        } catch (IOException e) {
            message = "ERROR " + e.getMessage();
            logger.info(service.getType() + "::" + service.getName() + ":: IOException Calling HTTPS server. Possibly server is down or not accepting the request");
            logger.error("sorry, something wrong!", e);
        } catch (Exception e) {
            message = "ERROR " + e.getMessage();
            logger.info(service.getType() + "::" + service.getName() + ":: Exception Calling HTTPS server. Possibly server is down or not accepting the request");
            logger.error("sorry, something wrong!", e);
        }
        long tat = System.currentTimeMillis() - start;
        logger.info(service.getType() + "::" + service.getName() + ":: TAT [CONNECT+DATA] >> " + tat + " ms");

        Date ddt2 = new java.util.Date();
        String responseTime = sdf.format(ddt2);
        //return rspBody;
        //requestTime#httpstatuscode#tat#responseTime
        return requestTime + "#" + http_status + "#" + tat + "#" + responseTime + "#" + len + "#" + message;
    }

    //two way ssl set up
    public static SSLConnectionSocketFactory getSocketFactory(Monitor service, String trustStoreLoc, String trustStorePass, String keystoreloc, String keyStorePass) {
        try {
            final KeyStore keyStore = KeyStore.getInstance("JKS");
            try (final InputStream is = new FileInputStream(keystoreloc)) {
                keyStore.load(is, keyStorePass.toCharArray());
            }
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, keyStorePass.toCharArray());

            final KeyStore trustStore = KeyStore.getInstance("JKS");
            try (final InputStream is = new FileInputStream(trustStoreLoc)) {
                trustStore.load(is, trustStorePass.toCharArray());
            }
            final TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(trustStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new java.security.SecureRandom());

            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new String[]{"TLSv1", "TLSv1.1", "TLSv1.2"}, null, new NoopHostnameVerifier());
            return sslsf;
        } catch (java.security.KeyStoreException e) {
            logger.info(service.getType() + "::" + service.getName() + ":: Error while creating SSL Factory.");
            logger.error("sorry, something wrong!", e);
        } catch (Exception e) {
            logger.info(service.getType() + "::" + service.getName() + ":: Error while creating SSL Factory.");
            logger.error("sorry, something wrong!", e);
        }
        return null;
    }

    //one way ssl set up
    public static SSLConnectionSocketFactory getSocketFactory(Monitor service, String trustStoreLoc, String trustStorePass) {
        try {
            final KeyStore trustStore = KeyStore.getInstance("JKS");
            try (final InputStream is = new FileInputStream(trustStoreLoc)) {
                trustStore.load(is, trustStorePass.toCharArray());
            }
            final TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(trustStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), new java.security.SecureRandom());

            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new String[]{"TLSv1", "TLSv1.1", "TLSv1.2"}, null, new NoopHostnameVerifier());
            return sslsf;
        } catch (java.security.KeyStoreException e) {
            logger.info(service.getType() + "::" + service.getName() + ":: Error while creating SSL Factory.");
            logger.error("sorry, something wrong!", e);
        } catch (Exception e) {
            logger.info(service.getType() + "::" + service.getName() + ":: Error while creating SSL Factory.");
            logger.error("sorry, something wrong!", e);
        }
        return null;
    }

    public static String getBascicAuthStr(String username, String password) {
        String authStr = username + ":" + password;
        authStr = Base64.getEncoder().encodeToString(authStr.getBytes());
        return authStr;
    }
}
