package com.etz.gh.amard.momo.mtn;

public class MTNRequestResponse {

    private String statusCode;

    private String statusDescr;

    private String momTransId;

    private String processNum;

    private String senderId;

    private String thirdPartyAcctRef;

    private String msisdn;

    public MTNRequestResponse() {
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusDescr() {
        return statusDescr;
    }

    public void setStatusDescr(String statusDescr) {
        this.statusDescr = statusDescr;
    }

    public String getMomTransId() {
        return momTransId;
    }

    public void setMomTransId(String momTransId) {
        this.momTransId = momTransId;
    }

    public String getProcessNum() {
        return processNum;
    }

    public void setProcessNum(String processNum) {
        this.processNum = processNum;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getThirdPartyAcctRef() {
        return thirdPartyAcctRef;
    }

    public void setThirdPartyAcctRef(String thirdPartyAcctRef) {
        this.thirdPartyAcctRef = thirdPartyAcctRef;
    }

    @Override
    public String toString() {
        return "MTNRequestResponse{" + "statusCode=" + statusCode + ", statusDescr=" + statusDescr + ", momTransId=" + momTransId + ", processNum=" + processNum + ", senderId=" + senderId + ", thirdPartyAcctRef=" + thirdPartyAcctRef + ", msisdn=" + msisdn + '}';
    }  
}
