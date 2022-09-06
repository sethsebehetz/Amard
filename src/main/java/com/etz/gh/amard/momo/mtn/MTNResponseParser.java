package com.etz.gh.amard.momo.mtn;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author seth.sebeh
 */
public class MTNResponseParser {

    final static Logger logger = Logger.getLogger(MTNResponseParser.class);

    static {
        PropertyConfigurator.configure("cfg\\log4j.config");
    }

    public static MTNRequestResponse parseMTNDebit(String xml, String reference) {
        MTNRequestResponse requestResp = new MTNRequestResponse();
        try {
            NodeList nList = new SuperDomParser(xml).getDocument().getElementsByTagName("return");
            logger.info(reference + " :: (0200)DEBIT TRANSACTION DETAILS ");
            logger.info("====================================================");

            String name = "";
            String value = "";
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node node = nList.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    name = eElement.getElementsByTagName("name").item(0).getTextContent();
                    value = eElement.getElementsByTagName("value").item(0).getTextContent();
                    logger.info(reference + " ::" + name + ":" + value);
                    if (name.equalsIgnoreCase("ProcessingNumber")) {
                        requestResp.setProcessNum(value);
                    } else if (name.equalsIgnoreCase("StatusCode")) {
                        requestResp.setStatusCode(value);
                    } else if (name.equalsIgnoreCase("StatusDesc")) {
                        requestResp.setStatusDescr(value);
                    } else if (name.equalsIgnoreCase("MSISDNNum")) {
                        requestResp.setMsisdn(value);
                    }
                }
            }
            logger.info("====================================================");
        } catch (Exception e) {
            logger.error("sorry, something wrong!", e);
        }
        return requestResp;
    }

    public static MTNDepositResponse parseMTNCredit(String xml, String reference) {
        MTNDepositResponse depositResp = new MTNDepositResponse();
        try {
            NodeList nList = new SuperDomParser(xml).getDocument().getElementsByTagName("return");
            logger.info(reference + " :: (0900)CREDIT TRANSACTION DETAILS ");
            logger.info("====================================================");

            String name = "";
            String value = "";
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node node = nList.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    name = eElement.getElementsByTagName("name").item(0).getTextContent();
                    value = eElement.getElementsByTagName("value").item(0).getTextContent();
                    logger.info(reference + " ::" + name + ":" + value);
                    if (name.equalsIgnoreCase("ProcessingNumber")) {
                        depositResp.setProcessNum(value);
                    } else if (name.equalsIgnoreCase("StatusCode")) {
                        depositResp.setStatusCode(value);
                    } else if (name.equalsIgnoreCase("StatusDesc")) {
                        depositResp.setStatusDescr(value);
                    } else if (name.equalsIgnoreCase("MOMTransactionID")) {
                        depositResp.setMomTransId(value);
                    }
                }
            }
            logger.info("====================================================");
        } catch (Exception e) {
            logger.error("sorry, something wrong!", e);
        }
        return depositResp;
    }
}
