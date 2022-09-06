/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etz.gh.amard.entities;

/**
 *
 * @author seth.sebeh
 */
public class FundGateMerchants {
    private String terminal_id;
    private String merchant_name;

    public String getTerminal_id() {
        return terminal_id;
    }

    public void setTerminal_id(String terminal_id) {
        this.terminal_id = terminal_id;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    @Override
    public String toString() {
        return "FundgateMerchants{" + "terminal_id=" + terminal_id + ", merchant_name=" + merchant_name + '}';
    }
    
}
