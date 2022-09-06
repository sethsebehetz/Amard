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
//public class AmardController1 {
//
//    static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(AmardController1.class);
//
//    static {
//        PropertyConfigurator.configure("cfg\\log4j.config");
//    }
//
//    List<Graph> graphTableList = null;
//
//    public AmardController1() {
//        graphTableList = new AmardDAO().loadGraphTable();
//    }
//
//    public List<ResponseTable> getLogRecordsByType(String type) {
//        return new AmardDAO().getLogRecordsByType(type);
//    }
//
////    //build graph data per each request from web service call
////    public String getMomoLiveGraphData(String network, String txn_type, int interval) {
////
////        ChartJsData g = new ChartJsData();
////        String type = "";
////        if (!(network != null && txn_type != null)) {
////            return null;
////        }
////
////        if (!(network.equalsIgnoreCase("MTN") || network.equalsIgnoreCase("TIGO") || network.equalsIgnoreCase("VODA"))) {
////            return null;
////        }
////
////        if (txn_type.equalsIgnoreCase("C") || txn_type.equalsIgnoreCase("D")) {
////            type = txn_type;
////        } else {
////            return null;
////        }
////
////        //String sql = "Select id, reference as label,txn_tat as value from telcodb.mobilemoney where client = :client and respcode = '00' and paymenttype = :paymenttype and txn_tat IS NOT NULL and TRNXDATE <= DATE_SUB(now(), INTERVAL :interval MINUTE) order by trnxdate desc limit 50";
////        String sql = "Select id, reference as label,TIMESTAMPDIFF(SECOND,TRNXDATE,PROCESSDATE) as value from telcodb.mobilemoney where client = :client and respcode = '00' and paymenttype = :paymenttype and txn_tat IS NOT NULL and TRNXDATE <= DATE_SUB(now(), INTERVAL :interval MINUTE) order by trnxdate desc limit 50";
////        List<GraphQueryResult> graph = new AmardDAO().getMomoLiveGraphData(sql, network, txn_type, interval);
////        String labelsArray = GeneralUtils.getLabelsArray(graph);
////        String valuesArray = GeneralUtils.getValuesArray(graph);
////
////        String type_full_text = "";
////        if (txn_type.equals("C")) {
////            type_full_text = "Credit";
////        }
////        if (txn_type.equals("D")) {
////            type_full_text = "Debit";
////        }
////        String title = network + " " + type_full_text + " TAT (Successful txn throughput in seconds)";
////        g.setColor(Config.getKey("COLOR.ETZ"));
////        if (network.equalsIgnoreCase("MTN")) {
////            g.setColor(Config.getKey("COLOR.MTN"));
////        }
////        if (network.equalsIgnoreCase("VODA")) {
////            g.setColor(Config.getKey("COLOR.VODA"));
////        }
////        if (network.equalsIgnoreCase("TIGO")) {
////            g.setColor(Config.getKey("COLOR.TIGO"));
////        }
////
////        g.setChartType("line");
////        g.setTitle(title);
////        g.setLabels(labelsArray);
////        g.setxValues(valuesArray);
////        String rsp = new JSONObject(g).toString();
////        return rsp;
////    }
//
//    public void refreshGraphTableList() {
//        this.graphTableList = new AmardDAO().loadGraphTable();
//    }
//
//    //build graph data from graph table in database. returns json array of graph data
//    public String getMomoLiveGraphData(String group, String startDate, String endDate) {
//        List<ChartJsWrapper> chartJsWrapperList = new ArrayList<>();
//
//        //remove        
//        List<ChartJsData> chartJsDataList = new ArrayList<>();
//
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
//            if (g.getLimit() != null) {
//                query = query + " limit " + g.getLimit();
//            }
//
//            if (startDate != null && endDate != null) {
//                String date_clause = "AND TRNXDATE BETWEEN '" + startDate + "' AND '" + endDate + "'";
//                String q = query.toUpperCase();
//                if (q.contains("ORDER BY")) {
//                    String[] qa = q.split("ORDER BY");
//                    query = qa[0] + " " + date_clause + " ORDER BY " + qa[1];
//                } else if (q.contains("LIMIT")) {
//                    String[] qa = q.split("LIMIT");
//                    query = qa[0] + " " + date_clause + " LIMIT " + qa[1];
//                }
//            }
//            logger.info("QUERY TO EXECUTE >> " + query);
//
//            List<GraphQueryResult> graphQueryResult = new AmardDAO().runDynamicGraphQuery(query, g.getDatabase());
//            String labelsArray = GeneralUtils.getLabelsArray(graphQueryResult);
//            String valuesArray = GeneralUtils.getValuesArray(graphQueryResult);
//
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
//            chartJsDatasets.setBorderColor(g.getColor());
//            chartJsDatasets.setBackgroundColor(g.getColor());
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
//
//            //remove
//            chartJsDataList.add(chartJsData);
//        });
//        logger.info(chartJsDataList);
//        logger.info(chartJsWrapperList);
//        return new JSONArray(chartJsWrapperList).toString();
//    }
//
////    public String getMomo24hrseaGraphData() {
////        List<ChartJsData> chartJsDataList = new ArrayList<>();
////        List<Graph> momo24hrseaGraphTableList = this.graphTableList.stream()
////                .filter(m -> {
////                    if (m.getGroup().equals("24HR CHECK")) {
////                        return true;
////                    }
////                    return false;
////                }).collect(toList());
////
////        momo24hrseaGraphTableList.forEach(g -> {
////            String query = g.getQuery();
////            List<GraphQueryResult> graphQueryResult = new AmardDAO().runDynamicGraphQuery(query, g.getDatabase());
////            String labelsArray = GeneralUtils.getLabelsArray(graphQueryResult);
////            String valuesArray = GeneralUtils.getValuesArray(graphQueryResult);
////            ChartJsData chartJsData = new ChartJsData();
////            chartJsData.setY1Values(valuesArray);
////            chartJsData.setLabels(labelsArray);
////            chartJsData.setTitle(g.getTitle());
////            chartJsData.setChartType(g.getType());
////            chartJsData.setColor(g.getColor());
////            chartJsData.setY1Label(g.getY1_label());
////            chartJsData.setY2Label(g.getY2_label());
////            chartJsData.setX1Label(g.getX_label());
////            chartJsData.setShowXdata(g.isShow_xdata());
////
////            chartJsDataList.add(chartJsData);
////        });
////
////        return new JSONArray(chartJsDataList).toString();
////    }
//
//    public static void main(String[] args) {
//        //System.out.println(new AmardController().getMomoLiveGraphData("MTN", "C", 5));
//        System.out.println(new AmardController1().getMomoLiveGraphData("MOMO LIVE TAT","2019-10-18 00:00", "2019-10-18 17:00"));
//    }
//}
