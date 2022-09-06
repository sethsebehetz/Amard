package com.etz.gh.amard.momo.mtn;

import com.etz.gh.amard.entities.Monitor;
import com.etz.gh.amard.utilities.GeneralUtils;
import com.etz.gh.amard.utilities.SuperHttpClient;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class MTNClient {

    final static Logger logger = Logger.getLogger(MTNClient.class);

    static {
        PropertyConfigurator.configure("cfg\\log4j.config");
    }

    public static void main(String[] arg) {
    }

    public static MTN getMTNDebitSample() {
        MTN mtn = new MTN();
        
        mtn.setTimeStamp(GeneralUtils.getTimestamp());
        String ETZSPID_ = "2330110002770#bmeB500";
        String spidInfo[];
        spidInfo = ETZSPID_.split("#");
        mtn.setSpId(spidInfo[0]);
        mtn.setPwd(spidInfo[1]);
        mtn.setProcessingNumber(GeneralUtils.generateRandomNumber(10));
        mtn.setServiceId("etranpush.sp");
        mtn.setMsisdnNum("233" + GeneralUtils.generateRandomNumber(11) );
        String narration = "BILL PAYMENT";
        mtn.setNarration(narration.trim());
        mtn.setDueAmount("1");
        mtn.setAcctRef("112233");
        mtn.setFromMessage("BILL PAYMENT");

        return mtn;
    }

    public static MTN getMTNCreditSample() {
        MTN mtn = new MTN();       
        mtn.setTimeStamp(GeneralUtils.getTimestamp());
        String ETZSPID_ = "2330110002770#bmeB500";
        String spidInfo[];
        spidInfo = ETZSPID_.split("#");
        mtn.setSpId(spidInfo[0]);
        mtn.setPwd(spidInfo[1]);
        mtn.setProcessingNumber(GeneralUtils.generateRandomNumber(10));
        mtn.setServiceId("etranpush.sp");
        mtn.setMsisdnNum("233" + GeneralUtils.generateRandomNumber(11) );
        String narration = "BILL PAYMENT";
        mtn.setNarration(narration.trim());
        mtn.setAmount("1");
        mtn.setFromMessage("BILL PAYMENT");
        return mtn;
    }

    public static String processRequestPayment(Monitor monitor, MTN mtn, String url) {
        String responseXML = "";
        mtn.setSpPassword(GeneralUtils.getMD5(mtn.getSpId() + mtn.getPwd() + mtn.getTimeStamp()));
        MTNRequestResponse requestResponse = new MTNRequestResponse();
        try {
            String xml = "<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">\n"
                    + "   <S:Header>\n"
                    + "      <ns1:RequestSOAPHeader xmlns:ns1=\"http://www.huawei.com.cn/schema/common/v2_1\">\n"
                    + "         <spId>" + mtn.getSpId() + "</spId>\n"
                    + "         <spPassword>" + mtn.getSpPassword() + "</spPassword>\n"
                    + "         <timeStamp>" + mtn.getTimeStamp() + "</timeStamp>\n"
                    + "      </ns1:RequestSOAPHeader>\n"
                    + "   </S:Header>\n"
                    + "   <S:Body>\n"
                    + "      <ns2:processRequest xmlns:ns2=\"http://b2b.mobilemoney.mtn.zm_v1.0\">\n"
                    + "         <serviceId>100</serviceId>\n"
                    + "         <parameter>\n"
                    + "            <name>AcctBalance</name>\n"
                    + "            <value>0</value>\n"
                    + "         </parameter>\n"
                    + "         <parameter>\n"
                    + "            <name>PrefLang</name>\n"
                    + "            <value>EN</value>\n"
                    + "         </parameter>\n"
                    + "         <parameter>\n"
                    + "            <name>ProcessingNumber</name>\n"
                    + "            <value>" + mtn.getProcessingNumber() + "</value>\n"
                    + "         </parameter>\n"
                    + "         <parameter>\n"
                    + "            <name>serviceId</name>\n"
                    + "            <value>" + mtn.getServiceId() + "</value>\n"
                    + "         </parameter>\n"
                    + "         <parameter>\n"
                    + "            <name>MinDueAmount</name>\n"
                    + "            <value>0</value>\n"
                    + "         </parameter>\n"
                    + "         <parameter>\n"
                    + "            <name>OpCoID</name>\n"
                    + "            <value>gh</value>\n"
                    + "         </parameter>\n"
                    + "         <parameter>\n"
                    + "            <name>DueAmount</name>\n"
                    + "            <value>" + mtn.getDueAmount() + "</value>\n"
                    + "         </parameter>\n"
                    + "         <parameter>\n"
                    + "            <name>Narration</name>\n"
                    + "            <value>" + mtn.getNarration() + "</value>\n"
                    + "         </parameter>\n"
                    + "         <parameter>\n"
                    + "            <name>AcctRef</name>\n"
                    + "            <value>112233</value>\n"
                    + "         </parameter>\n"
                    + "         <parameter>\n"
                    + "            <name>frommessage</name>\n"
                    + "            <value></value>\n"
                    + "         </parameter>\n"
                    + "         <parameter>\n"
                    + "            <name>MSISDNNum</name>\n"
                    + "            <value>" + mtn.getMsisdnNum() + "</value>\n"
                    + "         </parameter>\n"
                    + "      </ns2:processRequest>\n"
                    + "   </S:Body>\n"
                    + "</S:Envelope>";
            
            logger.info(Thread.currentThread().getName() + " " + monitor.getName() + " REQUEST XML \n" + xml );
            responseXML = SuperHttpClient.doPost(monitor, url, xml);
            logger.info(Thread.currentThread().getName() + " " + responseXML);
            //String responseXML = SuperHttpClient.doPost(url, xml, 60, mtn.getProcessingNumber());
            if (responseXML == null) {
                return null;
            }
            //requestResponse = MTNResponseParser.parseMTNDebit(responseXML, mtn.getProcessingNumber());
        } catch (Exception e) {
            logger.error("sorry, something wrong!", e);
        }
        //return requestResponse;
        return responseXML;
    }

    public static String processDepositRequest(Monitor monitor, MTN mtn, String url) {
        String responseXML = "";
        mtn.setSpPassword(GeneralUtils.getMD5(mtn.getSpId() + mtn.getPwd() + mtn.getTimeStamp()));
        MTNDepositResponse depositResponse = new MTNDepositResponse();
        try {
            String xml = "<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">\n"
                    + "   <S:Header>\n"
                    + "      <ns1:RequestSOAPHeader xmlns:ns1=\"http://www.huawei.com.cn/schema/common/v2_1\">\n"
                    + "         <spId>" + mtn.getSpId() + "</spId>\n"
                    + "         <spPassword>" + mtn.getSpPassword() + "</spPassword>\n"
                    + "         <timeStamp>" + mtn.getTimeStamp() + "</timeStamp>\n"
                    + "      </ns1:RequestSOAPHeader>\n"
                    + "   </S:Header>\n"
                    + "   <S:Body>\n"
                    + "      <ns2:processRequest xmlns:ns2=\"http://b2b.mobilemoney.mtn.zm_v1.0/\">\n"
                    + "         <serviceId>1001</serviceId>\n"
                    + "         <parameter>\n"
                    + "            <name>Amount</name>\n"
                    + "            <value>" + mtn.getAmount() + "</value>\n"
                    + "         </parameter>\n"
                    + "         <parameter>\n"
                    + "            <name>PrefLang</name>\n"
                    + "            <value>EN</value>\n"
                    + "         </parameter>\n"
                    + "         <parameter>\n"
                    + "            <name>serviceId</name>\n"
                    + "            <value>" + mtn.getServiceId() + "</value>\n"
                    + "         </parameter>\n"
                    + "         <parameter>\n"
                    + "            <name>ProcessingNumber</name>\n"
                    + "            <value>" + mtn.getProcessingNumber() + "</value>\n"
                    + "         </parameter>\n"
                    + "         <parameter>\n"
                    + "            <name>CurrCode</name>\n"
                    + "            <value>GHS</value>\n"
                    + "         </parameter>\n"
                    + "         <parameter>\n"
                    + "            <name>OpCoID</name>\n"
                    + "            <value>gh</value>\n"
                    + "         </parameter>\n"
                    + "         <parameter>\n"
                    + "            <name>Narration</name>\n"
                    + "            <value>" + mtn.getNarration() + "</value>\n"
                    + "         </parameter>\n"
                    + "         <parameter>\n"
                    + "            <name>SenderID</name>\n"
                    + "            <value>MOM</value>\n"
                    + "         </parameter>\n"
                    + "         <parameter>\n"
                    + "            <name>MSISDNNum</name>\n"
                    + "            <value>" + mtn.getMsisdnNum() + "</value>\n"
                    + "         </parameter>\n"
                    + "      </ns2:processRequest>\n"
                    + "   </S:Body>\n"
                    + "</S:Envelope>";
            
            logger.info(Thread.currentThread().getName() + " " + monitor.getName() + " REQUEST XML \n" + xml );
            responseXML = SuperHttpClient.doPost(monitor, url, xml);
            logger.info(Thread.currentThread().getName() + " " + responseXML);
            // String responseXML = SuperHttpClient.doPost(url, xml, 60, mtn.getProcessingNumber());
            if (responseXML == null) {
                return null;
            }
            //depositResponse = MTNResponseParser.parseMTNCredit(responseXML, mtn.getProcessingNumber());
        } catch (Exception e) {
            logger.error("sorry, something wrong!", e);
        }
        // return depositResponse;
        return responseXML;
    }

    
}
