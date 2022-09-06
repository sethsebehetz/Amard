package com.etz.gh.amard.model;

import java.io.Serializable;

/**
 *
 * @author seth.sebeh
 */
public class ChartJsData implements Serializable{

    private static final long serialVersionUID = 7032379161265894322L;

    private String chartType;
    private String labels;
    //private String y1Values;
    //private String y2Values;
    private String xValues;
    private String yValues;
    //private String title;
    //private String color;
    private String y1Label;
    private String y2Label;
    private String x1Label;
    private boolean showXdata;
    private String title;

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getxValues() {
        return xValues;
    }

    public void setxValues(String xValues) {
        this.xValues = xValues;
    }

    public String getyValues() {
        return yValues;
    }

    public void setyValues(String yValues) {
        this.yValues = yValues;
    }

//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getColor() {
//        return color;
//    }
//
//    public void setColor(String color) {
//        this.color = color;
//    }

    public String getChartType() {
        return chartType;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

//    public String getY1Values() {
//        return y1Values;
//    }
//
//    public void setY1Values(String y1Values) {
//        this.y1Values = y1Values;
//    }
//
//    public String getY2Values() {
//        return y2Values;
//    }
//
//    public void setY2Values(String y2Values) {
//        this.y2Values = y2Values;
//    }

    public String getY1Label() {
        return y1Label;
    }

    public void setY1Label(String y1Label) {
        this.y1Label = y1Label;
    }

    public String getY2Label() {
        return y2Label;
    }

    public void setY2Label(String y2Label) {
        this.y2Label = y2Label;
    }

    public String getX1Label() {
        return x1Label;
    }

    public void setX1Label(String x1Label) {
        this.x1Label = x1Label;
    }

    public boolean isShowXdata() {
        return showXdata;
    }

    public void setShowXdata(boolean showXdata) {
        this.showXdata = showXdata;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
      
    
}
