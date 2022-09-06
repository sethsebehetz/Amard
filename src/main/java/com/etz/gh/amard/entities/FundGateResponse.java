/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etz.gh.amard.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author seth.sebeh
 */
@Entity
public class FundGateResponse implements Serializable{

    private static final long serialVersionUID = 1737688872412337495L;
    
    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int respId;
    private String action;
    private String terminal;
    private String etzRef;
    private String respMessage;
    private String clientRef;
    private Date created;
    private String respcode;
    private String merchant_name;

    public int getRespId() {
        return respId;
    }

    public void setRespId(int respId) {
        this.respId = respId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getEtzRef() {
        return etzRef;
    }

    public void setEtzRef(String etzRef) {
        this.etzRef = etzRef;
    }

    public String getRespMessage() {
        return respMessage;
    }

    public void setRespMessage(String respMessage) {
        this.respMessage = respMessage;
    }

    public String getClientRef() {
        return clientRef;
    }

    public void setClientRef(String clientRef) {
        this.clientRef = clientRef;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getRespcode() {
        return respcode;
    }

    public void setRespcode(String respcode) {
        this.respcode = respcode;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }
    
    
    
    
}
