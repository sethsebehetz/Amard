package com.etz.gh.amard.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author seth.sebeh
 */
@Entity
public class EvasNode implements Serializable {

    private static final long serialVersionUID = -1160624334251137107L;
    
    @Id
    private String VAS_ID;
    private String ACCT_NO;
    private String ALIAS;
    private double AMOUNT;
    private String CLIENT_REF;
    private String RESPONSE_CODE;
    private String NARRATION;
    private String NODE_REF;
    private String NODE_RESPONSE;
    private String PROD_ID;
    private String VAS_REF;
    private String SUBSCRIBER;
    private String THIRD_PARTY_REF;
    private String CHANNEL_ID;
    private String TERMINAL_ID;
    private String BANK_CODE;
    private String LINE_TYPE;
    private String LINE_MODE;
    private String CLIENT;
    private String OTHER_INFO;

    public String getVAS_ID() {
        return VAS_ID;
    }

    public void setVAS_ID(String VAS_ID) {
        this.VAS_ID = VAS_ID;
    }

    public String getACCT_NO() {
        return ACCT_NO;
    }

    public void setACCT_NO(String ACCT_NO) {
        this.ACCT_NO = ACCT_NO;
    }

    public String getALIAS() {
        return ALIAS;
    }

    public void setALIAS(String ALIAS) {
        this.ALIAS = ALIAS;
    }

    public double getAMOUNT() {
        return AMOUNT;
    }

    public void setAMOUNT(double AMOUNT) {
        this.AMOUNT = AMOUNT;
    }

    public String getCLIENT_REF() {
        return CLIENT_REF;
    }

    public void setCLIENT_REF(String CLIENT_REF) {
        this.CLIENT_REF = CLIENT_REF;
    }

    public String getRESPONSE_CODE() {
        return RESPONSE_CODE;
    }

    public void setRESPONSE_CODE(String RESPONSE_CODE) {
        this.RESPONSE_CODE = RESPONSE_CODE;
    }

    public String getNARRATION() {
        return NARRATION;
    }

    public void setNARRATION(String NARRATION) {
        this.NARRATION = NARRATION;
    }

    public String getNODE_REF() {
        return NODE_REF;
    }

    public void setNODE_REF(String NODE_REF) {
        this.NODE_REF = NODE_REF;
    }

    public String getNODE_RESPONSE() {
        return NODE_RESPONSE;
    }

    public void setNODE_RESPONSE(String NODE_RESPONSE) {
        this.NODE_RESPONSE = NODE_RESPONSE;
    }

    public String getPROD_ID() {
        return PROD_ID;
    }

    public void setPROD_ID(String PROD_ID) {
        this.PROD_ID = PROD_ID;
    }

    public String getVAS_REF() {
        return VAS_REF;
    }

    public void setVAS_REF(String VAS_REF) {
        this.VAS_REF = VAS_REF;
    }

    public String getSUBSCRIBER() {
        return SUBSCRIBER;
    }

    public void setSUBSCRIBER(String SUBSCRIBER) {
        this.SUBSCRIBER = SUBSCRIBER;
    }

    public String getTHIRD_PARTY_REF() {
        return THIRD_PARTY_REF;
    }

    public void setTHIRD_PARTY_REF(String THIRD_PARTY_REF) {
        this.THIRD_PARTY_REF = THIRD_PARTY_REF;
    }

    public String getCHANNEL_ID() {
        return CHANNEL_ID;
    }

    public void setCHANNEL_ID(String CHANNEL_ID) {
        this.CHANNEL_ID = CHANNEL_ID;
    }

    public String getTERMINAL_ID() {
        return TERMINAL_ID;
    }

    public void setTERMINAL_ID(String TERMINAL_ID) {
        this.TERMINAL_ID = TERMINAL_ID;
    }

    public String getBANK_CODE() {
        return BANK_CODE;
    }

    public void setBANK_CODE(String BANK_CODE) {
        this.BANK_CODE = BANK_CODE;
    }

    public String getLINE_TYPE() {
        return LINE_TYPE;
    }

    public void setLINE_TYPE(String LINE_TYPE) {
        this.LINE_TYPE = LINE_TYPE;
    }

    public String getLINE_MODE() {
        return LINE_MODE;
    }

    public void setLINE_MODE(String LINE_MODE) {
        this.LINE_MODE = LINE_MODE;
    }

    public String getCLIENT() {
        return CLIENT;
    }

    public void setCLIENT(String CLIENT) {
        this.CLIENT = CLIENT;
    }

    public String getOTHER_INFO() {
        return OTHER_INFO;
    }

    public void setOTHER_INFO(String OTHER_INFO) {
        this.OTHER_INFO = OTHER_INFO;
    }
    
    
    

}
