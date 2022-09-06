//package com.etz.gh.amard.controller;
//
//import com.etz.gh.amard.dao.AmardDAO;
//import com.etz.gh.amard.entities.Graph;
//import com.etz.gh.amard.entities.GraphQueryResult;
//import com.etz.gh.amard.entities.ResponseTable;
//import com.etz.gh.amard.httpserver.AmardServer;
//import com.etz.gh.amard.model.ChartJsData;
//import com.etz.gh.amard.model.ChartJsDatasets;
//import com.etz.gh.amard.model.ChartJsWrapper;
//import com.etz.gh.amard.utilities.Config;
//import com.etz.gh.amard.utilities.GeneralUtils;
//import java.util.ArrayList;
//import java.util.List;
//import static java.util.stream.Collectors.toList;
//import org.apache.log4j.PropertyConfigurator;
//import org.json.JSONObject;
//import org.json.JSONArray;
//
///**
// *
// * @author seth.sebeh
// */
//public class AmardController_27112019 {
//
//    static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(AmardController_27112019.class);
//
//    static {
//        PropertyConfigurator.configure("cfg\\log4j.config");
//    }
//
//    List<Graph> graphTableList = null;
//
//    public AmardController_27112019() {
//        //graphTableList = new AmardDAO().loadGraphTable();
//    }
//
//    public List<ResponseTable> getLogRecordsByType(String type) {
//        return new AmardDAO().getLogRecordsByType(type);
//    }
//
//    public void refreshGraphTableList() {
//        this.graphTableList = new AmardDAO().loadGraphTable();
//    }
//
//    public List<Graph> loadGraphData() {
//        return new AmardDAO().loadGraphTable();
//    }
//
//    //build graph data from graph table in database. returns json array of graph data
//    public String getMomoLiveGraphData(String group, String startDate, String endDate) {
//        List<ChartJsWrapper> chartJsWrapperList = new ArrayList<>();
//
//        this.graphTableList = loadGraphData();
//        List<Graph> momoLiveGraphTableList = this.graphTableList.stream()
//                .filter(m -> {
//                    if (m.getGroup().equals(group)) {
//                        return true;
//                    }
//                    return false;
//                }).collect(toList());
//
//        momoLiveGraphTableList.forEach(g -> {
//            String query = g.getQuery();
//            logger.info("QUERY RECEIVED >>" + query);
//            if (g.getLimit() != null) {
//                query = query + " limit " + g.getLimit();
//            }
//
//            if (startDate != null && endDate != null) {
//                query = query.toUpperCase();
//                String date_clause = "";
//                date_clause = " AND TRNXDATE BETWEEN '" + startDate + "' AND '" + endDate + "'";
//                if (!query.contains("WHERE")) {
//                    date_clause = " WHERE TRNXDATE BETWEEN '" + startDate + "' AND '" + endDate + "'";
//                }
//
//                if (query.contains("GROUP BY")) {
//                    String[] qa = query.split("GROUP BY");
//                    query = qa[0] + " " + date_clause + " GROUP BY " + qa[1];
//                }
//                else if (query.contains("ORDER BY")) {
//                    String[] qa = query.split("ORDER BY");
//                    query = qa[0] + " " + date_clause + " ORDER BY " + qa[1];
//                } else if (query.contains("LIMIT")) {
//                    String[] qa = query.split("LIMIT");
//                    query = qa[0] + " " + date_clause + " LIMIT " + qa[1];
//                } else {
//                    query = query + date_clause;
//                }
//            }
//         
//            logger.info("QUERY TO EXECUTE >> " + query);
//
//            List<GraphQueryResult> graphQueryResult = new AmardDAO().runDynamicGraphQuery(query, g.getDatabase());
//            String labelsArray = GeneralUtils.getLabelsArray(graphQueryResult);
//            String valuesArray = GeneralUtils.getValuesArray(graphQueryResult);
//            String colorsArray = GeneralUtils.getColorsArray(valuesArray.split(",").length);
//            logger.info("colorsArray Length " + valuesArray.split(",").length);
//            ChartJsData chartJsData = new ChartJsData();
//            //chartJsData.setY1Values(valuesArray);
//            chartJsData.setLabels(labelsArray);
//            //chartJsData.setTitle(g.getTitle());
//            chartJsData.setChartType(g.getType());
//            //chartJsData.setColor(g.getColor());
//            chartJsData.setY1Label(g.getY1_label());
//            chartJsData.setY2Label(g.getY2_label());
//            chartJsData.setX1Label(g.getX_label());
//            chartJsData.setShowXdata(g.isShow_xdata());
//
//            List<ChartJsDatasets> chartJsDatasetsList = new ArrayList<>();
//            ChartJsDatasets chartJsDatasets = new ChartJsDatasets();
//            chartJsDatasets.setType(g.getType());
//            chartJsDatasets.setLabel(g.getTitle());
//            chartJsDatasets.setData(valuesArray);
//            chartJsDatasets.setFill(false);           
//            chartJsDatasets.setBorderColor(colorsArray);
//            chartJsDatasets.setBackgroundColor(colorsArray);
//            chartJsDatasets.setBorderWidth(1);
//            chartJsDatasets.setyAxisID(GeneralUtils.generateRandomNumber(10));
//            chartJsDatasetsList.add(chartJsDatasets);
//
//            ChartJsWrapper chartJsWrapper = new ChartJsWrapper();
//            chartJsWrapper.setChartJsData(chartJsData);
//            chartJsWrapper.setChartJsDataset(chartJsDatasetsList);
//            //chartJsWrapper.setChartJsDataset(new JSONArray(chartJsDatasetsList).toString());
//
//            chartJsWrapperList.add(chartJsWrapper);
//        });
//        return new JSONArray(chartJsWrapperList).toString();
//    }
//
//    public static void main(String[] args) {
//        //System.out.println(new AmardController().getMomoLiveGraphData("MTN", "C", 5));
//        System.out.println(new AmardController_27112019().getMomoLiveGraphData("MOMO LIVE TAT", "2019-10-18 00:00", "2019-10-18 17:00"));
//    }
//}
