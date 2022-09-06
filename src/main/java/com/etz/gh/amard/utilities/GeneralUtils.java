/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etz.gh.amard.utilities;

import com.etz.gh.amard.entities.GraphQueryResult;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import static java.util.stream.Collectors.joining;
import org.json.JSONArray;

/**
 *
 * @author seth.sebeh
 */
public class GeneralUtils {

    private static java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

    public static void main(String[] args) {
        //System.out.println(getColorsArray(25));
        System.out.println(getColor(1));
    }

    public static String generateRandomNumber(int size) {
        String value = "AMRD";
        for (int t = 0; t < size; t++) {
            value = value + new Random().nextInt(9);
        }
        return value;
    }

    public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getLabelsArray(List<GraphQueryResult> graph) {
        String labelsArray = graph.stream().map(g -> "\"" + merchantsMapper(g.getLabel()) + "\"").collect(joining(","));
        //JSONArray json = new JSONArray("[" + labelsArray + "]");
        return "[" + labelsArray + "]";
        //return json.toString();
    }

    public static String getValuesArray(List<GraphQueryResult> graph) {
        String valuesArray = graph.stream().map(g -> g.getValue()).collect(joining(","));
        //JSONArray json = new JSONArray("[" + valuesArray + "]");
        return "[" + valuesArray + "]";
        //return json.toString();
    }

    public static String getValuesArrayCustom(List<GraphQueryResult> graph) {
        String valuesArray = graph.stream().map(g -> "10").collect(joining(","));
        return "[" + valuesArray + "]";
    }

    public static String getColorsArray(int size) {
        String colors = "#3e95cd,#ff9f40,#870000,#8e5ea2,#ff6384,#005005,#3cba9f,#e8c3b9,#c45850,#11f324,#ecec3b,#928000,#607d8b,#000000,#003300,#ba000d,#6a0080,#002984,#00675b,#5a9216,#c8b900,#c41c00#707070";
        String a[] = colors.split(",");
        String colorsArray = "";
        try {
            for (int i = 0; i < size; i++) {
                if (i == 0) {
                    colorsArray = "\"" + a[i] + "\"";
                } else if (i > 0) {
                    colorsArray = colorsArray + "," + "\"" + a[i] + "\"";
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {

        }
        return "[" + colorsArray + "]";
    }

    public static String getColorsArrayCustom(List<GraphQueryResult> graph) {
        String red = "#ff0000";
        String green = "#3fd820";
        String gray = "#777777";
        String colorsArray = graph.stream().map(g -> "\"" + (g.getValue().equals("1") ? green : g.getValue().equals("2") ? gray : red) + "\"").collect(joining(","));
        return "[" + colorsArray + "]";
    }

    public static String getColorsArrayCustom2(List<GraphQueryResult> graph) {
        String red = "#ff0000";
        String green = "#3fd820";
        String gray = "#777777";
        String colorsArray = graph.stream().map(g -> "\"" + (Integer.parseInt(g.getValue()) > 5 ? red : green) + "\"").collect(joining(","));
        return "[" + colorsArray + "]";
    }

    public static String getColorsArrayGreen(List<GraphQueryResult> graph) {
        String green = "#3fd820";
        String colorsArray = graph.stream().map(g -> "\"" + green + "\"").collect(joining(","));
        return "[" + colorsArray + "]";
    }

    public static String getColor(int i) {
        String colors = "#3e95cd,#ff9f40,#870000,#8e5ea2,#ff6384,#005005,#3cba9f,#e8c3b9,#c45850,#883997,#810095,#928000,#607d8b,#000000,#003300,#ba000d,#6a0080,#002984,#00675b,#5a9216,#c8b900,#c41c00#707070";
        String a[] = colors.split(",");
        return a[i - 1];
    }

    public static String getTimestamp() {
        SimpleDateFormat mtnTimestampFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = mtnTimestampFormat.format(new Date());
        return timestamp;
    }

    public static String merchantsMapper(String code) {
        if (code != null) {
            switch (code) {
                case "017":
                    return "FIRST ATLANTIC";
                case "004":
                    return "GCB";
                case "905":
                    return "BEST POINT ";
                case "029":
                    return "ENERGY BANK ";
                case "012":
                    return "ZENITH";
                case "003":
                    return "BARCLAYS";
                case "021":
                    return "BOA";
                case "0069900706083291":
                    return "OMANYE";
                case "0067510000010000":
                    return "JUSTPAY";
                case "0069900706081063":
                    return "EXPRESSPAYGH";
                case "0060020087":
                    return "WEBCONNECT";
                case "0060010112":
                    return "WEBCONNECT";
                case "0069900706089108":
                    return "DStv SYDNEY";
                case "0069900805230306":
                    return "GHANAMART";
                case "0238801407090690":
                    return "GT BANK";
                case "0178801407118097":
                    return "FIRST ATLANTIC BANK";
                case "0179911405075790":
                    return "MYGHANALIVE";
                case "0069900706080180":
                    return "NSANO";
                case "0069900706088258":
                    return "MONEY EXPRESS";
                case "0179971409091391":
                    return "RMART GHANA LTD";
                case "0039980000010000":
                    return "NFORTICS";
                case "0069900706089652":
                    return "AYA-TECH";
                case "0179951406270248":
                    return "SOCHITEL";
                case "0069900706084786":
                    return "TRANSFERWISE";
                case "0069900706080834":
                    return "FOSSIL TRADE";
                case "0069900594850009":
                    return "DIGITAL DREAMS";
                case "0069900594870005":
                    return "OPANET GHANA LIMITED";
                case "0069900594910007":
                    return "MONI TECHNOLOGIES";
                case "0257780000010000":
                    return "ACCESS BANK";
                case "0069900706089629":
                    return "AFRO INTERNATIONAL";
                case "0250240006680005":
                    return "INTERPAY";
                case "0337780706080784":
                    return "FNB";
                case "0179951406270008":
                    return "Ghana Post";
                case "0036670000010001":
                    return "BARCLAYS UBP";
                case "0296670000010001":
                    return "ENERGY BANK";
                case "0060011511261040":
                    return "ePESEWA";
                case "0067780000010003":
                    return "PING EXPRESS";
                case "1907780000010009":
                    return "STANBIC BANK";
                case "0068891012072477":
                    return "SMSGH";
                case "0039901301116178":
                    return "TEST CARD";
                case "0067790000010001":
                    return "XOOM GLOBAL";
                case "0069901108108876":
                    return "TRANSFERTO";
                case "0067710000080003XXX":
                    return "Power-Soft";
                case "0067710000090001":
                    return "DMS - FLEX TRANSFER";
                case "0067500000010002":
                    return "VODAFONE";
                case "0067710000110007":
                    return "UK4REX";
                case "0067530000010006":
                    return "CLICKPAY";
                case "0067550000010001":
                    return "JUSTPAY";
                case "0067570000010007":
                    return "PRIMEAIRTIME";
                case "0067580000010005":
                    return "CYST";
                case "0067710000020006":
                    return "CRAFT SILICON";
                case "0257790000010008":
                    return "ACCESS BANK PAYWITHCAPTURE";
                case "0067710000030004":
                    return "NALO";
                case "0067710000040002":
                    return "REFITOK";
                case "0067710000050009":
                    return "FUNDGATE-TESTER";
                case "0067590000010003":
                    return "CRAFT-SILICON_NEW";
                case "0067590000020001":
                    return "TELESOL";
                case "0067600000020009":
                    return "USA CONCIERGE";
                case "0067600000010001":
                    return "CASH REMIT";
                case "0067610000010009":
                    return "ADOM SAVINGS AND LOAN ";
                case "0067620000020005":
                    return "INFORTICS ";
                case "0097620000010004":
                    return "SGSSB ";
                case "0067620000310004":
                    return "PAY HUB";
                case "0067520000010008":
                    return "TERA PAY";
                case "0027520000020005":
                    return "CRAFT SILICON MOMO";
                case "0027530000010000":
                    return "CRAFT SILICON MOMO 2";
                case "0067620000300006":
                    return "BITPESA";
                case "0067520000010050":
                    return "SEGOVIA";
                case "0067620000290008":
                    return "WORLD REMIT";
                case "0067620000000005":
                    return "LIQUID";
                case "0067620000000008":
                    return "ECOACH";
                case "0067620000000006":
                    return "OROBO";
                case "0067620000000009":
                    return "KORBA";
                case "0067630000000002":
                    return "PRIKANY";
                case "0067710000000001":
                    return "ETZ GLOBAL";
                case "0237620000000001":
                    return "GTBANK";
                case "0067510000000001":
                    return "GCBHUAWEIMW";
                case "0049990000001056":
                    return "FUNDGATE-TEST GCB";
                case "0049900000000001":
                    return "GCBHUAWEIMWTEST";
                case "0067620000000010":
                    return "8D GHANA";
                case "006":
                    return "ETRANZACT";
                default:
                    return code;
            }
        }
        return null;
    }

    public static int getDateDiffDays(String startDate, String endDate) {
        try {
            Date d1 = new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(startDate);
            Date d2 = new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(endDate);

            long diff = d2.getTime() - d1.getTime();
            return (int) (diff / (24 * 60 * 60 * 1000));
        } catch (ParseException ex) {

        }
        return -1;
    }

    public static String getRequestTime() {
        Date dt = new java.util.Date();
        return sdf.format(dt);
    }

    public static String getResponseTime() {
        Date dt = new java.util.Date();
        return sdf.format(dt);
    }

    public static String getBankNameByBankCode(String code) {
        switch (code) {
            case "017":
                return "FAB";
            case "004":
                return "GCB";
            case "905":
                return "BEST POINT ";
            case "029":
                return "ENERGY BANK ";
            case "012":
                return "ZENITH";
            case "003":
                return "ABSA";
            case "021":
                return "BOA";
            case "002":
                return "STANDART CHARTERED";
            case "006":
                return "UBA";
            case "008":
                return "ADB";
            case "009":
                return "SOCIETE GENERAL";
            case "010":
                return "UMB";
            case "011":
                return "REPUBLIC BANK";
            case "013":
                return "ECOBANK";
            case "014":
                return "CAL BANK";
            case "018":
                return "PRUDENTIAL BANK";
            case "019":
                return "STANBIC BANK";
            case "020":
                return "FBN";
            case "023":
                return "GT BANK";
            case "024":
                return "FIDELITY";
            case "025":
                return "ACCESS BANK";
            case "033":
                return "FNB";
            case "686":
                return "MTN MOBILE MONEY";
            case "863":
                return "VODACASH";
            case "844":
                return "AIRTELTIGO MONEY";

        }
        return "BANK CODE " + code;
    }
}
