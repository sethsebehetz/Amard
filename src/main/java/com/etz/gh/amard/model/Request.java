package com.etz.gh.amard.model;

import java.io.Serializable;

/**
 *
 * @author seth.sebeh
 */
public class Request implements Serializable{

    private static final long serialVersionUID = 702373255220294322L;
    String labels;
    String values;

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }
    
    
}
