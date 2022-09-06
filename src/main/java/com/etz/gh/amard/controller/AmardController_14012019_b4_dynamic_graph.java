//package com.etz.gh.amard.controller;
//
//import com.etz.gh.amard.dao.AmardDAO;
//import com.etz.gh.amard.entities.GraphQueryResult;
//import com.etz.gh.amard.entities.ResponseTable;
//import com.etz.gh.amard.model.ChartJsData;
//import com.etz.gh.amard.utilities.Config;
//import com.etz.gh.amard.utilities.GeneralUtils;
//import java.util.List;
//import org.json.JSONObject;
//
///**
// *
// * @author seth.sebeh
// */
//public class AmardController_14012019_b4_dynamic_graph {
//
//    public List<ResponseTable> getLogRecordsByType(String type) {
//        return new AmardDAO().getLogRecordsByType(type);
//    }
//
//    //works on successful txns
//    public String getMomoLiveGraphData(String network, String txn_type, int interval) {
//        ChartJsData g = new ChartJsData();
//        String type = "";
//        if (!(network != null && txn_type != null)) {
//            return null;
//        }
//
//        if (!(network.equalsIgnoreCase("MTN") || network.equalsIgnoreCase("TIGO") || network.equalsIgnoreCase("VODA"))) {
//            return null;
//        }
//
//        if (txn_type.equalsIgnoreCase("C") || txn_type.equalsIgnoreCase("D")) {
//            type = txn_type;
//        } else {
//            return null;
//        }
//
//        //String sql = "Select id, reference as label,txn_tat as value from telcodb.mobilemoney where client = :client and respcode = '00' and paymenttype = :paymenttype and txn_tat IS NOT NULL and TRNXDATE <= DATE_SUB(now(), INTERVAL :interval MINUTE) order by trnxdate desc limit 50";
//        String sql = "Select id, reference as label,TIMESTAMPDIFF(SECOND,TRNXDATE,PROCESSDATE) as value from telcodb.mobilemoney where client = :client and respcode = '00' and paymenttype = :paymenttype and txn_tat IS NOT NULL and TRNXDATE <= DATE_SUB(now(), INTERVAL :interval MINUTE) order by trnxdate desc limit 50";
//        List<GraphQueryResult> graph = new AmardDAO().getMomoLiveGraphData(sql, network, txn_type, interval);
//        String labelsArray = GeneralUtils.getLabelsArray(graph);
//        String valuesArray = GeneralUtils.getValuesArray(graph);
//
//        String type_full_text = "";
//        if (txn_type.equals("C")) {
//            type_full_text = "Credit";
//        }
//        if (txn_type.equals("D")) {
//            type_full_text = "Debit";
//        }
//        String title = network + " " + type_full_text + " TAT (Successful txn throughput in seconds)";
//        g.setColor(Config.getKey("COLOR.ETZ"));
//        g.setColor(Config.getKey("COLOR.ETZ"));
//        if (network.equalsIgnoreCase("MTN")) {
//            g.setColor(Config.getKey("COLOR.MTN"));
//            g.setColor(Config.getKey("COLOR.MTN"));
//        }
//        if (network.equalsIgnoreCase("VODA")) {
//            g.setColor(Config.getKey("COLOR.VODA"));
//            g.setColor(Config.getKey("COLOR.VODA"));
//        }
//        if (network.equalsIgnoreCase("TIGO")) {
//            g.setColor(Config.getKey("COLOR.TIGO"));
//            g.setColor(Config.getKey("COLOR.TIGO"));
//        }
//
//        g.setChartType("line");
//        g.setTitle(title);
//        g.setLabels(labelsArray);
//        g.setxValues(valuesArray);
//        String rsp = new JSONObject(g).toString();
//        return rsp;
//    }
//
//    public static void main(String[] args) {
//        System.out.println(new AmardController_14012019_b4_dynamic_graph().getMomoLiveGraphData("MTN", "C", 5));
//    }
//}
