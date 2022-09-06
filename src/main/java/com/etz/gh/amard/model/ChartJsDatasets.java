package com.etz.gh.amard.model;

import java.io.Serializable;

/**
 *
 * @author seth.sebeh
 */
public class ChartJsDatasets implements Serializable{

    private static final long serialVersionUID = 703237322122894322L;
    private String type;
    private String label;
    private String data;
    private boolean fill = false;
    private String borderColor;
    private String backgroundColor;
    private int borderWidth;
    private String yAxisID;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isFill() {
        return fill;
    }

    public void setFill(boolean fill) {
        this.fill = fill;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
    }

    public String getyAxisID() {
        return yAxisID;
    }

    public void setyAxisID(String yAxisID) {
        this.yAxisID = yAxisID;
    }
    
}
