package com.etz.gh.amard.controller;

import com.etz.gh.amard.dao.AmardDAO;
import com.etz.gh.amard.entities.EcardQueryResult;
import com.etz.gh.amard.entities.FundGateMerchants;
import com.etz.gh.amard.entities.FundGateResponse;
import com.etz.gh.amard.entities.Graph;
import com.etz.gh.amard.entities.GraphQueryResult;
import com.etz.gh.amard.entities.MobileMoney;
import com.etz.gh.amard.entities.Log;
import com.etz.gh.amard.entities.VasGateBillers;
import com.etz.gh.amard.model.ChartJsData;
import com.etz.gh.amard.model.ChartJsDatasets;
import com.etz.gh.amard.model.ChartJsWrapper;
import com.etz.gh.amard.utilities.GeneralUtils;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import static java.util.stream.Collectors.toList;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONArray;

/**
 *
 * @author seth.sebeh
 */
public class AmardController {

    int i = 1;
    static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(AmardController.class);

    static {
        PropertyConfigurator.configure("cfg\\log4j.config");
    }

    List<Graph> graphTableList = null;

    public AmardController() {
        //graphTableList = new AmardDAO().loadGraphTable();
    }

    public List<Log> getLogRecordsByType(String type) {
        return new AmardDAO().getLogRecordsByType(type);
    }

    public List<Log> getBoaLogRecordsByType(String type) {
        return new AmardDAO().getBoaLogRecordsByType(type);
    }

    public List<Log> getGCBLogRecordsByType(String type) {
        return new AmardDAO().getGCBLogRecordsByType(type);
    }

    public void refreshGraphTableList() {
        this.graphTableList = new AmardDAO().loadGraphTable();
    }

    public List<Graph> loadGraphData() {
        return new AmardDAO().loadGraphTable();
    }

    //build graph data from graph table in database. returns json array of graph data
    public String getMomoLiveGraphData(String group, String startDate, String endDate, String ext1) {
        List<ChartJsWrapper> chartJsWrapperList = new ArrayList<>();

        this.graphTableList = loadGraphData();
        List<Graph> momoLiveGraphTableList = this.graphTableList.stream()
                .filter(m -> {
                    if (m.getGroup().equals(group) && m.getEnabled() == 1) {
                        return true;
                    }
                    return false;
                }).collect(toList());
        i = 1;

        momoLiveGraphTableList.forEach(g -> {
            //String query = formQuery(g.getQuery(), g, startDate, endDate);
            //String query2 = null;
            //List<GraphQueryResult> graphQueryResult = new AmardDAO().runDynamicGraphQuery(query, g.getDatabase());
            List<GraphQueryResult> graphQueryResult = getQueryData(g.getQuery(), g, startDate, endDate, ext1);
            String labelsArray = GeneralUtils.getLabelsArray(graphQueryResult);
            String valuesArray = "";
            String colorsArray = "";
            if (g.getExt1() != null && g.getExt1().equals("GREEN ONLY")) {
                valuesArray = GeneralUtils.getValuesArray(graphQueryResult);
                colorsArray = GeneralUtils.getColorsArrayGreen(graphQueryResult);
            } else if (g.getGroup().equals("TMC NODES STATUS")) {
                valuesArray = GeneralUtils.getValuesArrayCustom(graphQueryResult);
                colorsArray = GeneralUtils.getColorsArrayCustom(graphQueryResult);
            } else if (g.getGroup().equals("TMC AVERAGE RESPONSE TIME") || g.getGroup().equals("TMC AVERAGE RESPONSE TIME LAST 10 MINUTES")) {
                valuesArray = GeneralUtils.getValuesArray(graphQueryResult);
                colorsArray = GeneralUtils.getColorsArrayCustom2(graphQueryResult);
            } else if (g.getGroup().equals("GCB TMC AVERAGE RESPONSE TIME") || g.getGroup().equals("GCB TMC AVERAGE RESPONSE TIME LAST 5 MINUTES")) {
                valuesArray = GeneralUtils.getValuesArray(graphQueryResult);
                colorsArray = GeneralUtils.getColorsArrayCustom2(graphQueryResult);
            } else {
                valuesArray = GeneralUtils.getValuesArray(graphQueryResult);
                colorsArray = GeneralUtils.getColorsArray(valuesArray.split(",").length);
            }
            logger.info("colorsArray Length " + valuesArray.split(",").length);
            ChartJsData chartJsData = new ChartJsData();
            //chartJsData.setY1Values(valuesArray);
            chartJsData.setLabels(labelsArray);
            //chartJsData.setTitle(g.getTitle());
            chartJsData.setChartType(g.getType());
            //chartJsData.setColor(g.getColor());
            chartJsData.setY1Label(g.getY1_label());
            chartJsData.setY2Label(g.getY2_label());
            chartJsData.setX1Label(g.getX_label());
            chartJsData.setShowXdata(g.isShow_xdata());
            chartJsData.setTitle(g.getTitle());

            List<ChartJsDatasets> chartJsDatasetsList = new ArrayList<>();

            ChartJsDatasets chartJsDatasets = new ChartJsDatasets();

            chartJsDatasets.setLabel(g.getTitle());
            chartJsDatasets.setBorderWidth(1);
            if (g.getQuery2() != null) {
                chartJsDatasets.setBorderWidth(1);
                chartJsDatasets.setType(g.getType());
                chartJsDatasets.setLabel(g.getLegends().split("#")[0]);
            }
            chartJsDatasets.setData(valuesArray);
            chartJsDatasets.setFill(false);

            if (g.getType().equalsIgnoreCase("line")) {
                chartJsDatasets.setBorderColor(GeneralUtils.getColor(i));
                chartJsDatasets.setBackgroundColor(GeneralUtils.getColor(i));
            } else if (g.getQuery2() != null && g.getColor() != null) {
                chartJsDatasets.setBorderColor(g.getColor().split("~")[0]);
                chartJsDatasets.setBackgroundColor(g.getColor().split("~")[0]);
            } else {
                chartJsDatasets.setBorderWidth(1);
                //chartJsDatasets.setBorderColor(colorsArray);
                chartJsDatasets.setBackgroundColor(colorsArray);
            }
            //chartJsDatasets.setBorderWidth(1);
            chartJsDatasets.setyAxisID(GeneralUtils.generateRandomNumber(10));
            chartJsDatasetsList.add(chartJsDatasets);

            if (g.getQuery2() != null) {
                //query2 = formQuery(g.getQuery2(), g, startDate, endDate);
                //List<GraphQueryResult> graphQueryResult2 = new AmardDAO().runDynamicGraphQuery(query2, g.getDatabase());
                List<GraphQueryResult> graphQueryResult2 = getQueryData(g.getQuery2(), g, startDate, endDate, ext1);
                String labelsArray2 = GeneralUtils.getLabelsArray(graphQueryResult2);
                String valuesArray2 = GeneralUtils.getValuesArray(graphQueryResult2);
                ChartJsDatasets chartJsDatasets2 = new ChartJsDatasets();
                chartJsDatasets2.setType(g.getType());
                chartJsDatasets2.setLabel(g.getLegends().split("#")[1]);
                chartJsDatasets2.setData(valuesArray2);
                chartJsDatasets2.setFill(false);
                chartJsDatasets2.setBorderColor(GeneralUtils.getColor(i + 1));
                chartJsDatasets2.setBackgroundColor(GeneralUtils.getColor(i + 1));
                if (g.getColor() != null) {
                    chartJsDatasets2.setBorderColor(g.getColor().split("~")[1]);
                    chartJsDatasets2.setBackgroundColor(g.getColor().split("~")[1]);
                }
                chartJsDatasets2.setBorderWidth(1);
                chartJsDatasets2.setyAxisID(GeneralUtils.generateRandomNumber(10));
                chartJsDatasetsList.add(chartJsDatasets2);
            }

            ChartJsWrapper chartJsWrapper = new ChartJsWrapper();
            chartJsWrapper.setChartJsData(chartJsData);
            chartJsWrapper.setChartJsDataset(chartJsDatasetsList);
            //chartJsWrapper.setChartJsDataset(new JSONArray(chartJsDatasetsList).toString());

            chartJsWrapperList.add(chartJsWrapper);
            i++;
        });
        return new JSONArray(chartJsWrapperList).toString();
    }

    private static String formQuery(String query, Graph g, String startDate, String endDate, String ext1) {
        //startDate = "2019-11-01 00:00";
        //String query = g.getQuery();
        logger.info("QUERY RECEIVED >>" + query);
        if (g.getLimit() != null) {
            query = query + " limit " + g.getLimit();
        }

        if (startDate != null && endDate != null) {
            query = query.toUpperCase();
            String date_clause = "";
            if (g.getTable().equals("MOBILE MONEY")) {
                date_clause = "AND TRNXDATE BETWEEN '" + startDate + "' AND '" + endDate + "'";
                if (!query.contains("WHERE")) {
                    date_clause = "WHERE TRNXDATE BETWEEN '" + startDate + "' AND '" + endDate + "'";
                }
            }
            if (g.getTable().equals("FUNDGATE")) {
                date_clause = "AND B.CREATED BETWEEN '" + startDate + "' AND '" + endDate + "'";
                if (!query.contains("WHERE")) {
                    date_clause = "WHERE B.CREATED BETWEEN '" + startDate + "' AND '" + endDate + "'";
                }
            }
            if (g.getTable().equals("EVAS TRANS")) {
                date_clause = "AND TRAN_DT BETWEEN '" + startDate + "' AND '" + endDate + "'";
                if (!query.contains("WHERE")) {
                    date_clause = "WHERE TRAN_DT BETWEEN '" + startDate + "' AND '" + endDate + "'";
                }
            }

            if (g.getTable().equals("USSD")) {
                date_clause = "AND CREATED BETWEEN '" + startDate + "' AND '" + endDate + "'";
                if (!query.contains("WHERE")) {
                    date_clause = "WHERE CREATED BETWEEN '" + startDate + "' AND '" + endDate + "'";
                }
            }

//            if (g.getTable().equals("TMC NODES")) {
//                date_clause = "AND CREATED BETWEEN '" + startDate + "' AND '" + endDate + "'";
//                if (!query.contains("WHERE")) {
//                    date_clause = "WHERE CREATED BETWEEN '" + startDate + "' AND '" + endDate + "'";
//                }
//            }
            if (g.getTable().equals("TMC REQUEST")) {
                date_clause = "AND TRANS_DATE BETWEEN '" + startDate + "' AND '" + endDate + "'";
                if (!query.contains("WHERE")) {
                    date_clause = "WHERE TRANS_DATE BETWEEN '" + startDate + "' AND '" + endDate + "'";
                }
            }

            if (g.getTable().startsWith("BANK ECARDDB")) {
                date_clause = "AND A.CREATED BETWEEN '" + startDate + "' AND '" + endDate + "'";
                if (!query.contains("WHERE")) {
                    date_clause = "WHERE A.CREATED BETWEEN '" + startDate + "' AND '" + endDate + "'";
                }
            }

            if (g.getTable().equals("G-MONEY")) {
                date_clause = "AND DT BETWEEN '" + startDate + "' AND '" + endDate + "'";
                if (!query.contains("WHERE")) {
                    date_clause = "WHERE DT BETWEEN '" + startDate + "' AND '" + endDate + "'";
                }
            }

            if (g.getTable().equals("MOBILE DB")) {
                date_clause = "AND CREATED BETWEEN '" + startDate + "' AND '" + endDate + "'";
                if (!query.contains("WHERE")) {
                    date_clause = "WHERE CREATED BETWEEN '" + startDate + "' AND '" + endDate + "'";
                }
            }

            if (query.contains("GROUP BY")) {
                String[] qa = query.split("GROUP BY");
                query = qa[0] + " " + date_clause + " GROUP BY " + qa[1];
            } else if (query.contains("ORDER BY")) {
                String[] qa = query.split("ORDER BY");
                query = qa[0] + " " + date_clause + " ORDER BY " + qa[1];
            } else if (query.contains("LIMIT")) {
                String[] qa = query.split("LIMIT");
                query = qa[0] + " " + date_clause + " LIMIT " + qa[1];
            } else {
                query = query + date_clause;
            }
        }

        if (query.contains("<DATE_FORMAT>") && g.getTable().equals("MOBILE MONEY")) {
            if (GeneralUtils.getDateDiffDays(startDate, endDate) > 1) {
                query = query.replace("<DATE_FORMAT>", "DATE_FORMAT(TRNXDATE,'%D')");
            } else {
                query = query.replace("<DATE_FORMAT>", "CONCAT(DATE_FORMAT(TRNXDATE,'%H'),'Hrs')");
            }
        }

        if (query.contains("<DATE_FORMAT>") && g.getTable().equals("FUNDGATE")) {
            if (GeneralUtils.getDateDiffDays(startDate, endDate) > 1) {
                query = query.replace("<DATE_FORMAT>", "DATE_FORMAT(B.CREATED,'%D')");
            } else {
                query = query.replace("<DATE_FORMAT>", "CONCAT(DATE_FORMAT(B.CREATED,'%H'),'Hrs')");
            }
        }

        if (query.contains("<DATE_FORMAT>") && g.getTable().equals("EVAS TRANS")) {
            if (GeneralUtils.getDateDiffDays(startDate, endDate) > 1) {
                query = query.replace("<DATE_FORMAT>", "DATE_FORMAT(TRAN_DT,'%D')");
            } else {
                query = query.replace("<DATE_FORMAT>", "CONCAT(DATE_FORMAT(TRAN_DT,'%H'),'Hrs')");
            }
        }

        if (query.contains("<DATE_FORMAT>") && g.getTable().equals("USSD")) {
            if (GeneralUtils.getDateDiffDays(startDate, endDate) > 1) {
                query = query.replace("<DATE_FORMAT>", "DATE_FORMAT(CREATED,'%D')");
            } else {
                query = query.replace("<DATE_FORMAT>", "CONCAT(DATE_FORMAT(CREATED,'%H'),'Hrs')");
            }
        }

        if (query.contains("<DATE_FORMAT>") && g.getTable().equals("TMC NODES")) {
            if (GeneralUtils.getDateDiffDays(startDate, endDate) > 1) {
                query = query.replace("<DATE_FORMAT>", "DATE_FORMAT(CREATED,'%D')");
            } else {
                query = query.replace("<DATE_FORMAT>", "CONCAT(DATE_FORMAT(CREATED,'%H'),'Hrs')");
            }
        }

        if (query.contains("<DATE_FORMAT>") && g.getTable().equals("TMC REQUEST")) {
            if (GeneralUtils.getDateDiffDays(startDate, endDate) > 1) {
                query = query.replace("<DATE_FORMAT>", "DATE_FORMAT(TRANS_DATE,'%D')");
            } else {
                query = query.replace("<DATE_FORMAT>", "CONCAT(DATE_FORMAT(TRANS_DATE,'%H'),'Hrs')");
            }
        }

        if (query.contains("<DATE_FORMAT>") && g.getTable().equals("G-MONEY")) {
            if (GeneralUtils.getDateDiffDays(startDate, endDate) > 1) {
                query = query.replace("<DATE_FORMAT>", "DATE_FORMAT(DT,'%D')");
            } else {
                query = query.replace("<DATE_FORMAT>", "CONCAT(DATE_FORMAT(DT,'%H'),'Hrs')");
            }
        }

        if (query.contains("<:REFERENCES_MREQUEST>")) {
            //for GCB deposit mobilization select references            
            query = query.replace("<:REFERENCES_MREQUEST>", AmardDAO.getMRequestReferences(startDate, endDate));
        }

        if (query.contains("<TERMINAL>")) {
            query = query.replace("<TERMINAL>", ext1);
        }

        if (query.contains("<ALIAS>")) {
            query = query.replace("<ALIAS>", ext1);
        }

        logger.info("QUERY TO EXECUTE >> " + query);
        return query;
    }

    //method checks if date range is > 15 then it splits the query into two and runs the query and merge them
    //issues when running queries longer than 30 days
    public List<GraphQueryResult> getQueryData(String query, Graph graph, String startDate, String endDate, String ext1) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Calendar c = Calendar.getInstance();
        int dateDiff = GeneralUtils.getDateDiffDays(startDate, endDate);
        String midDate = null;
        if (dateDiff > 31) {
            //date range exceeded. no sql query run spanning a date difference of more than 31 days. too much headache for the database
            logger.info("date range exceeded");
            List<GraphQueryResult> r = new ArrayList<>();
            GraphQueryResult g = new GraphQueryResult();
            r.add(g);
            return r;
        } else {
            if (dateDiff > 15) {
                try {
                    Date start_date = sdf.parse(startDate);
                    c.setTime(start_date);
                    c.add(Calendar.DAY_OF_MONTH, 15);
                    midDate = sdf.format(c.getTime());
                    logger.info("start date " + startDate);
                    logger.info("mid date " + midDate);
                } catch (ParseException ex) {
                    logger.error("sorry an error occured", ex);
                }
            }
        }

        List<GraphQueryResult> graphQueryResult = null;
        List<GraphQueryResult> graphQueryResult2 = null;
        if (midDate == null) {
            query = formQuery(query, graph, startDate, endDate, ext1);
            graphQueryResult = new AmardDAO().runDynamicGraphQuery(query, graph.getDatabase());
        } else {
            query = formQuery(graph.getQuery(), graph, startDate, midDate, ext1);
            graphQueryResult = new AmardDAO().runDynamicGraphQuery(query, graph.getDatabase());
            query = formQuery(graph.getQuery(), graph, midDate, endDate, ext1);
            graphQueryResult2 = new AmardDAO().runDynamicGraphQuery(query, graph.getDatabase());

            //addall duplicates list - not in use anymore
            //graphQueryResult.addAll(graphQueryResult2);
            //10022020 we use custom method addList
            graphQueryResult = addList(graphQueryResult, graphQueryResult2);
        }

        return graphQueryResult;

        //for DATE_FORMAT arrays issue of missing rows can be solved using java
//        if (query.contains("'%H'")) {
//            return fillMissingHours(graphQueryResult);
//        } else if (query.contains("'%D'")) {
//            return fillMissingDays(graphQueryResult);
//        } else {
//            return graphQueryResult;
//        }
    }

    //fill missing hours
    public static List<GraphQueryResult> fillMissingHours(List<GraphQueryResult> inList) {
        logger.info("BEFORE : " + inList);
        List<GraphQueryResult> outList = new ArrayList<>();
        if (inList.size() < 1) {
            return outList;
        }
        for (int i = 0; i < 24; i++) {
            GraphQueryResult newElement = new GraphQueryResult(String.valueOf(i), "0", (i < 10 ? "0" + i : "" + i) + "Hrs");
            outList.add(newElement);
        }

        for (int i = 0; i < outList.size(); i++) {
            for (int y = 0; y < inList.size(); y++) {
                if (inList.get(y).getLabel().equals(outList.get(i).getLabel())) {
                    outList.set(i, inList.get(y));
                }
            }
        }
        logger.info("AFTER : " + outList);

        //exclude extreme ends. outlist populated from 0-23 but we only need the range from inlist start to end after we have filled the missing hours
        //1. get first element from inlist
        GraphQueryResult firstElementInList = inList.get(0);
        //2. get last element from inlist
        GraphQueryResult lastElementList = inList.get(inList.size() - 1);

        int outListStartIndex = 0, outListEndIndex = 0;
        //look for index of firstElementInList in outList
        for (int i = 0; i < outList.size(); i++) {
            if (outList.get(i).getLabel().equals(firstElementInList.getLabel())) {
                outListStartIndex = i;
            }
            if (outList.get(i).getLabel().equals(lastElementList.getLabel())) {
                outListEndIndex = i;
            }
        }
        //List<GraphQueryResult> returnedList = outList.subList(outListStartIndex, outListEndIndex + 1);
        List<GraphQueryResult> returnedList = outList.subList(outListStartIndex, outListStartIndex + inList.size());
        logger.info("FINAL RETURNED LIST: " + returnedList);
        return returnedList;
    }

    //fill missing days
    public static List<GraphQueryResult> fillMissingDays(List<GraphQueryResult> inList) {
        logger.info("BEFORE : " + inList);

        List<GraphQueryResult> outList = new ArrayList<>();
        if (inList.size() < 1) {
            return outList;
        }
        for (int i = 1; i < 32; i++) {
            String postFix = "";
            switch (i) {
                case 1:
                case 21:
                case 31:
                    postFix = "st";
                    break;
                case 2:
                case 22:
                    postFix = "nd";
                    break;
                case 3:
                case 23:
                    postFix = "rd";
                    break;
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                case 19:
                case 20:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                    postFix = "th";
                    break;
            }
            GraphQueryResult newElement = new GraphQueryResult(String.valueOf(i), "0", i + postFix);
            outList.add(newElement);
        }

        for (int i = 0; i < outList.size(); i++) {
            for (int y = 0; y < inList.size(); y++) {
                if (inList.get(y).getLabel().equals(outList.get(i).getLabel())) {
                    outList.set(i, inList.get(y));
                }
            }
        }

        logger.info("AFTER : " + outList);

        //exclude extreme ends. outlist populated from 0-23 but we only need the range from inlist start to end after we have filled the missing hours
        //1. get first element from inlist
        GraphQueryResult firstElementInList = inList.get(0);
        //2. get last element from inlist
        GraphQueryResult lastElementList = inList.get(inList.size() - 1);

        int outListStartIndex = 0, outListEndIndex = 0;
        //look for index of firstElementInList in outList
        for (int i = 0; i < outList.size(); i++) {
            if (outList.get(i).getLabel().equals(firstElementInList.getLabel())) {
                outListStartIndex = i;
            }
            if (outList.get(i).getLabel().equals(lastElementList.getLabel())) {
                outListEndIndex = i;
            }
        }

        //List<GraphQueryResult> returnedList = outList.subList(outListStartIndex, outListEndIndex + 1);        
        //List<GraphQueryResult> returnedList = outList.subList(outListStartIndex, outListStartIndex + inList.size());
        //logger.info("FINAL RETURNED LIST: " + returnedList);
        //return returnedList;
        //logic change: previous code does not work in all cases esp when date range for inlist is from say 3rd - 27th of previous month
        //new change to just loop outlist and if element(label) is not in inlist then we remove from out list
        //so we return outlist to caller
//        for (int i = 0; i < outList.size(); i++) {
//            if (!inList.contains(outList.get(i))) {
//                outList.remove(i);
//            }
//        }
        for (int i = 0; i < outList.size(); i++) {
            if (!inList.contains(outList.get(i))) {
                outList.remove(i);
            }
        }

//        for (int i = 0; i < outList.size(); i++) {
//            if (!inList.contains(outList.get(i))) {
//                outList.remove(i);
//            }
//        }
        logger.info("FINAL RETURNED LIST: " + outList);
        return outList;
    }

    //incomplete will continue
    //for now want to disable one month query
    public static List<GraphQueryResult> addList(List<GraphQueryResult> list1, List<GraphQueryResult> list2) {
        for (int i = 0; i < list2.size(); i++) {
            for (int y = 0; y < list1.size(); y++) {
                if (list1.get(y).getLabel().equals(list2.get(i).getLabel())) {
                    GraphQueryResult newModifiedElement = new GraphQueryResult(String.valueOf(i), String.valueOf(Double.parseDouble(list2.get(i).getValue()) + Double.parseDouble(list1.get(y).getValue())), list1.get(y).getLabel());
                    list1.set(y, newModifiedElement);
                }
            }
        }
        return list1;
    }

    //method merges duplicate labels and add their value
    //not working . need to implement equals,hashcode before indexof will work perfectly
    //another bug method only adding when labels r equal
    public static List<GraphQueryResult> addList2(List<GraphQueryResult> list1, List<GraphQueryResult> list2) {
        for (GraphQueryResult l2 : list2) {
            for (GraphQueryResult l1 : list1) {
                if (l1.getLabel().equals(l2.getLabel())) {
                    GraphQueryResult newModifiedElement = new GraphQueryResult(l1.getId(), String.valueOf(Double.parseDouble(l2.getValue()) + Double.parseDouble(l1.getValue())), l1.getLabel());
                    list1.set(list1.indexOf(l1), newModifiedElement);
                }
            }
        }
        return list1;
    }

    public String getMomoCreditDroppedTxn(String startDate, String endDate) {
        List<MobileMoney> lm = AmardDAO.getMomoCreditDroppedTransactions(startDate, endDate);
        String response = new JSONArray(lm).toString();
        return response;
    }

    public long countMomoCreditDroppedTxn(String startDate, String endDate) {
        return AmardDAO.countMomoCreditDroppedTransactions(startDate, endDate);
    }

    public double sumMomoCreditDroppedTxn(String startDate, String endDate) {
        return AmardDAO.sumMomoCreditDroppedTransactions(startDate, endDate);
    }

    public String getFundgateServerError99Transactions(String startDate, String endDate) {
        List<FundGateResponse> lm = AmardDAO.getFundgateServerError99Transactions(startDate, endDate);
        String response = new JSONArray(lm).toString();
        return response;
    }

    public BigInteger countFundgateServerError99Transactions(String startDate, String endDate) {
        return AmardDAO.countFundgateServerError99Transactions(startDate, endDate);
    }

    public double sumFundgateServerError99Transactions(String startDate, String endDate) {
        return AmardDAO.sumFundgateServerError99Transactions(startDate, endDate);
    }

    public String getApplogs() {
        List<Log> list = AmardDAO.getApplogs();
        String response = new JSONArray(list).toString();
        return response;
    }

    public String getBOAApplogs() {
        List<Log> list = AmardDAO.getBOAApplogs();
        String response = new JSONArray(list).toString();
        return response;
    }

    public String getGCBApplogs() {
        List<Log> list = AmardDAO.getGCBApplogs();
        String response = new JSONArray(list).toString();
        return response;
    }

    public String getFgMerchants() {
        List<FundGateMerchants> list = AmardDAO.getFgMerchants();
        String response = new JSONArray(list).toString();
        return response;
    }

    public String getVasGateBillers() {
        List<VasGateBillers> list = AmardDAO.getVasGateBillers();
        String response = new JSONArray(list).toString();
        return response;
    }

    public String getGCBSocketErrors(String filter, String startDate, String endDate) {
        List<EcardQueryResult> list = AmardDAO.getEcardQueryResult(filter, startDate, endDate);
        String response = new JSONArray(list).toString();
        return response;
    }
    
     public String getAlarmLogs() {
        List<Log> list = AmardDAO.getAlarmLogs();
        String response = new JSONArray(list).toString();
        return response;
    }

    public static void main(String[] args) {
        //System.out.println(new AmardController().getMomoLiveGraphData("MTN", "C", 5));
        System.out.println(new AmardController().getMomoLiveGraphData("MOMO LIVE TAT", "2019-10-18 00:00", "2019-10-18 17:00", null));
    }
}
