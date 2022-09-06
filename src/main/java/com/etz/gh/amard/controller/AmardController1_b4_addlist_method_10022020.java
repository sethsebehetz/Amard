//package com.etz.gh.amard.controller;
//
//import com.etz.gh.amard.dao.AmardDAO;
//import com.etz.gh.amard.entities.AppLog;
//import com.etz.gh.amard.entities.FundGateMerchants;
//import com.etz.gh.amard.entities.FundGateTransaction;
//import com.etz.gh.amard.entities.FundGateResponse;
//import com.etz.gh.amard.entities.Graph;
//import com.etz.gh.amard.entities.GraphQueryResult;
//import com.etz.gh.amard.entities.MobileMoney;
//import com.etz.gh.amard.entities.ResponseTable;
//import com.etz.gh.amard.entities.VasGateBillers;
//import com.etz.gh.amard.httpserver.AmardServer;
//import com.etz.gh.amard.model.ChartJsData;
//import com.etz.gh.amard.model.ChartJsDatasets;
//import com.etz.gh.amard.model.ChartJsWrapper;
//import com.etz.gh.amard.utilities.Config;
//import com.etz.gh.amard.utilities.GeneralUtils;
//import java.math.BigInteger;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import static java.util.stream.Collectors.toList;
//import org.apache.log4j.PropertyConfigurator;
//import org.json.JSONArray;
//
///**
// *
// * @author seth.sebeh
// */
//public class AmardController1_b4_addlist_method_10022020 {
//
//    int i = 1;
//    static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(AmardController1_b4_addlist_method_10022020.class);
//
//    static {
//        PropertyConfigurator.configure("cfg\\log4j.config");
//    }
//
//    List<Graph> graphTableList = null;
//
//    public AmardController1_b4_addlist_method_10022020() {
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
//    public String getMomoLiveGraphData(String group, String startDate, String endDate , String ext1) {
//        List<ChartJsWrapper> chartJsWrapperList = new ArrayList<>();
//
//        this.graphTableList = loadGraphData();
//        List<Graph> momoLiveGraphTableList = this.graphTableList.stream()
//                .filter(m -> {
//                    if (m.getGroup().equals(group) && m.getEnabled() == 1) {
//                        return true;
//                    }
//                    return false;
//                }).collect(toList());
//        i = 1;
//        momoLiveGraphTableList.forEach(g -> {
//            //String query = formQuery(g.getQuery(), g, startDate, endDate);
//            //String query2 = null;
//            //List<GraphQueryResult> graphQueryResult = new AmardDAO().runDynamicGraphQuery(query, g.getDatabase());
//            List<GraphQueryResult> graphQueryResult = getQueryData(g.getQuery(), g, startDate, endDate, ext1);
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
//            chartJsData.setTitle(g.getTitle());
//
//            List<ChartJsDatasets> chartJsDatasetsList = new ArrayList<>();
//
//            ChartJsDatasets chartJsDatasets = new ChartJsDatasets();
//
//            chartJsDatasets.setLabel(g.getTitle());
//            chartJsDatasets.setBorderWidth(2);
//            if (g.getQuery2() != null) {
//                chartJsDatasets.setBorderWidth(1);
//                chartJsDatasets.setType(g.getType());
//                chartJsDatasets.setLabel(g.getLegends().split("#")[0]);
//            }
//            chartJsDatasets.setData(valuesArray);
//            chartJsDatasets.setFill(false);
//
//            if (g.getType().equalsIgnoreCase("line")) {
//                chartJsDatasets.setBorderColor(GeneralUtils.getColor(i));
//                chartJsDatasets.setBackgroundColor(GeneralUtils.getColor(i));
//            } else {
//                chartJsDatasets.setBorderColor(colorsArray);
//                chartJsDatasets.setBackgroundColor(colorsArray);
//            }
//            //chartJsDatasets.setBorderWidth(1);
//            chartJsDatasets.setyAxisID(GeneralUtils.generateRandomNumber(10));
//            chartJsDatasetsList.add(chartJsDatasets);
//
//            if (g.getQuery2() != null) {
//                //query2 = formQuery(g.getQuery2(), g, startDate, endDate);
//                //List<GraphQueryResult> graphQueryResult2 = new AmardDAO().runDynamicGraphQuery(query2, g.getDatabase());
//                List<GraphQueryResult> graphQueryResult2 = getQueryData(g.getQuery2(), g, startDate, endDate,ext1);
//                String labelsArray2 = GeneralUtils.getLabelsArray(graphQueryResult2);
//                String valuesArray2 = GeneralUtils.getValuesArray(graphQueryResult2);
//                ChartJsDatasets chartJsDatasets2 = new ChartJsDatasets();
//                chartJsDatasets2.setType(g.getType());
//                chartJsDatasets2.setLabel(g.getLegends().split("#")[1]);
//                chartJsDatasets2.setData(valuesArray2);
//                chartJsDatasets2.setFill(false);
//                chartJsDatasets2.setBorderColor(GeneralUtils.getColor(i + 1));
//                chartJsDatasets2.setBackgroundColor(GeneralUtils.getColor(i + 1));
//                chartJsDatasets2.setBorderWidth(1);
//                chartJsDatasets2.setyAxisID(GeneralUtils.generateRandomNumber(10));
//                chartJsDatasetsList.add(chartJsDatasets2);
//            }
//
//            ChartJsWrapper chartJsWrapper = new ChartJsWrapper();
//            chartJsWrapper.setChartJsData(chartJsData);
//            chartJsWrapper.setChartJsDataset(chartJsDatasetsList);
//            //chartJsWrapper.setChartJsDataset(new JSONArray(chartJsDatasetsList).toString());
//
//            chartJsWrapperList.add(chartJsWrapper);
//            i++;
//        });
//        return new JSONArray(chartJsWrapperList).toString();
//    }
//
//    private static String formQuery(String query, Graph g, String startDate, String endDate, String ext1) {
//        //startDate = "2019-11-01 00:00";
//        //String query = g.getQuery();
//        logger.info("QUERY RECEIVED >>" + query);
//        if (g.getLimit() != null) {
//            query = query + " limit " + g.getLimit();
//        }
//
//        if (startDate != null && endDate != null) {
//            query = query.toUpperCase();
//            String date_clause = "";
//            if (g.getTable().equals("MOBILE MONEY")) {
//                date_clause = "AND TRNXDATE BETWEEN '" + startDate + "' AND '" + endDate + "'";
//                if (!query.contains("WHERE")) {
//                    date_clause = "WHERE TRNXDATE BETWEEN '" + startDate + "' AND '" + endDate + "'";
//                }
//            }
//            if (g.getTable().equals("FUNDGATE")) {
//                date_clause = "AND B.CREATED BETWEEN '" + startDate + "' AND '" + endDate + "'";
//                if (!query.contains("WHERE")) {
//                    date_clause = "WHERE B.CREATED BETWEEN '" + startDate + "' AND '" + endDate + "'";
//                }
//            }
//            if (g.getTable().equals("EVAS TRANS")) {
//                date_clause = "AND TRAN_DT BETWEEN '" + startDate + "' AND '" + endDate + "'";
//                if (!query.contains("WHERE")) {
//                    date_clause = "WHERE TRAN_DT BETWEEN '" + startDate + "' AND '" + endDate + "'";
//                }
//            }
//
//            if (query.contains("GROUP BY")) {
//                String[] qa = query.split("GROUP BY");
//                query = qa[0] + " " + date_clause + " GROUP BY " + qa[1];
//            } else if (query.contains("ORDER BY")) {
//                String[] qa = query.split("ORDER BY");
//                query = qa[0] + " " + date_clause + " ORDER BY " + qa[1];
//            } else if (query.contains("LIMIT")) {
//                String[] qa = query.split("LIMIT");
//                query = qa[0] + " " + date_clause + " LIMIT " + qa[1];
//            } else {
//                query = query + date_clause;
//            }
//        }
//
//        if (query.contains("<DATE_FORMAT>") && g.getTable().equals("MOBILE MONEY")) {
//            if (GeneralUtils.getDateDiffDays(startDate, endDate) > 1) {
//                query = query.replace("<DATE_FORMAT>", "DATE_FORMAT(TRNXDATE,'%D')");
//            } else {
//                query = query.replace("<DATE_FORMAT>", "DATE_FORMAT(TRNXDATE,'%H')");
//            }
//        }
//
//        if (query.contains("<DATE_FORMAT>") && g.getTable().equals("FUNDGATE")) {
//            if (GeneralUtils.getDateDiffDays(startDate, endDate) > 1) {
//                query = query.replace("<DATE_FORMAT>", "DATE_FORMAT(B.CREATED,'%D')");
//            } else {
//                query = query.replace("<DATE_FORMAT>", "DATE_FORMAT(B.CREATED,'%H')");
//            }
//        }
//
//        if (query.contains("<DATE_FORMAT>") && g.getTable().equals("EVAS TRANS")) {
//            if (GeneralUtils.getDateDiffDays(startDate, endDate) > 1) {
//                query = query.replace("<DATE_FORMAT>", "DATE_FORMAT(TRAN_DT,'%D')");
//            } else {
//                query = query.replace("<DATE_FORMAT>", "DATE_FORMAT(TRAN_DT,'%H')");
//            }
//        }
//
//        if (query.contains("<TERMINAL>")) {
//            query = query.replace("<TERMINAL>", ext1);
//        }
//        
//        if (query.contains("<ALIAS>")) {
//            query = query.replace("<ALIAS>", ext1);
//        }
//
//        logger.info("QUERY TO EXECUTE >> " + query);
//        return query;
//    }
//
//    //method checks if date range is > 15 then it splits the query into two and runs the query and merge them
//    //issues when running queries longer than 30 days
//    public List<GraphQueryResult> getQueryData(String query, Graph graph, String startDate, String endDate, String ext1) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
//        Calendar c = Calendar.getInstance();
//        int dateDiff = GeneralUtils.getDateDiffDays(startDate, endDate);
//        String midDate = null;
//        if (dateDiff > 31) {
//            //date range exceeded. no sql query run spanning a date difference of more than 31 days. too much headache for the database
//            logger.info("date range exceeded");
//            List<GraphQueryResult> r = new ArrayList<>();
//            GraphQueryResult g = new GraphQueryResult();
//            r.add(g);
//            return r;
//        } else {
//            if (dateDiff > 15) {
//                try {
//                    Date start_date = sdf.parse(startDate);
//                    c.setTime(start_date);
//                    c.add(Calendar.DAY_OF_MONTH, 15);
//                    midDate = sdf.format(c.getTime());
//                    logger.info("start date " + startDate);
//                    logger.info("mid date " + midDate);
//                } catch (ParseException ex) {
//                    logger.error("sorry an error occured", ex);
//                }
//            }
//        }
//
//        List<GraphQueryResult> graphQueryResult = null;
//        List<GraphQueryResult> graphQueryResult2 = null;
//        if (midDate == null) {
//            query = formQuery(query, graph, startDate, endDate, ext1);
//            graphQueryResult = new AmardDAO().runDynamicGraphQuery(query, graph.getDatabase());
//        } else {
//            query = formQuery(graph.getQuery(), graph, startDate, midDate, ext1);
//            graphQueryResult = new AmardDAO().runDynamicGraphQuery(query, graph.getDatabase());
//            query = formQuery(graph.getQuery(), graph, midDate, endDate, ext1);
//            graphQueryResult2 = new AmardDAO().runDynamicGraphQuery(query, graph.getDatabase());
//            graphQueryResult.addAll(graphQueryResult2);
//        }
//        return graphQueryResult;
//    }
//
//    public String getMomoCreditDroppedTxn(String startDate, String endDate) {
//        List<MobileMoney> lm = AmardDAO.getMomoCreditDroppedTransactions(startDate, endDate);
//        String response = new JSONArray(lm).toString();
//        return response;
//    }
//
//    public long countMomoCreditDroppedTxn(String startDate, String endDate) {
//        return AmardDAO.countMomoCreditDroppedTransactions(startDate, endDate);
//    }
//
//    public double sumMomoCreditDroppedTxn(String startDate, String endDate) {
//        return AmardDAO.sumMomoCreditDroppedTransactions(startDate, endDate);
//    }
//
//    public String getFundgateServerError99Transactions(String startDate, String endDate) {
//        List<FundGateResponse> lm = AmardDAO.getFundgateServerError99Transactions(startDate, endDate);
//        String response = new JSONArray(lm).toString();
//        return response;
//    }
//
//    public BigInteger countFundgateServerError99Transactions(String startDate, String endDate) {
//        return AmardDAO.countFundgateServerError99Transactions(startDate, endDate);
//    }
//
//    public double sumFundgateServerError99Transactions(String startDate, String endDate) {
//        return AmardDAO.sumFundgateServerError99Transactions(startDate, endDate);
//    }
//
//    public String getApplogs() {
//        List<AppLog> list = AmardDAO.getApplogs();
//        String response = new JSONArray(list).toString();
//        return response;
//    }
//
//    public String getFgMerchants() {
//        List<FundGateMerchants> list = AmardDAO.getFgMerchants();
//        String response = new JSONArray(list).toString();
//        return response;
//    }
//    
//    public String getVasGateBillers() {
//        List<VasGateBillers> list = AmardDAO.getVasGateBillers();
//        String response = new JSONArray(list).toString();
//        return response;
//    }
//    
//
//    public static void main(String[] args) {
//        //System.out.println(new AmardController().getMomoLiveGraphData("MTN", "C", 5));
//        System.out.println(new AmardController1_b4_addlist_method_10022020().getMomoLiveGraphData("MOMO LIVE TAT", "2019-10-18 00:00", "2019-10-18 17:00", null));
//    }
//}
