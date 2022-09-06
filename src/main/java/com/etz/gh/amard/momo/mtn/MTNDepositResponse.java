package com.etz.gh.amard.momo.mtn;

public class MTNDepositResponse {

    private String statusCode;

    private String statusDescr;

    private String momTransId;

    private String processNum;

    private String senderId;

    private String opCoId;

    private String msisdn;

    public MTNDepositResponse() {
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

        this.statusDescr = statusDescr.replace("'", "");
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

    public String getOpCoId() {
        return opCoId;
    }

    public void setOpCoId(String opCoId) {
        this.opCoId = opCoId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    @Override
    public String toString() {
        return "MTNDepositResponse{" + "statusCode=" + statusCode + ", statusDescr=" + statusDescr + ", momTransId=" + momTransId + ", processNum=" + processNum + ", senderId=" + senderId + ", opCoId=" + opCoId + ", msisdn=" + msisdn + '}';
    } 
}
