package com.etz.gh.amard.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Seth
 */
@Entity
public class FundGateTransaction implements Serializable {

    private static final long serialVersionUID = -6067886018182254342L;
    @Id
    private String etzRef;
    private String destination;
    private String clientRef;
    private BigDecimal amount; 
//    private String created;
    private String created2;
    private String respMessage;
    private String lineType;
    private String action;
    private String RESPCODE;
//    private String counter;
//    private String terminal; 

    public String getEtzRef() {
        return etzRef;
    }

    public void setEtzRef(String etzRef) {
        this.etzRef = etzRef;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getClientRef() {
        return clientRef;
    }

    public void setClientRef(String clientRef) {
        this.clientRef = clientRef;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getRespMessage() {
        return respMessage;
    }

    public void setRespMessage(String respMessage) {
        this.respMessage = respMessage;
    }

    public String getLineType() {
        return lineType;
    }

    public void setLineType(String lineType) {
        this.lineType = lineType;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

//    public String getCounter() {
//        return counter;
//    }
//
//    public void setCounter(String counter) {
//        this.counter = counter;
//    }
//
//    public String getBiller() {
//        return biller;
//    }
//
//    public void setBiller(String biller) {
//        this.biller = biller;
//    }

//    public String getTerminal() {
//        return terminal;
//    }
//
//    public void setTerminal(String terminal) {
//        this.terminal = terminal;
//    }

//    public String getCreated() {
//        return created;
//    }
//
//    public void setCreated(String created) {
//        this.created = created;
//    }

    public String getCreated2() {
        return created2;
    }

    public void setCreated2(String created2) {
        this.created2 = created2;
    }

    public String getRESPCODE() {
        return RESPCODE;
    }

    public void setRESPCODE(String RESPCODE) {
        this.RESPCODE = RESPCODE;
    }
     
}
