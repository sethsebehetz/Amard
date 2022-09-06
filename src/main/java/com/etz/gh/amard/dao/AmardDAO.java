package com.etz.gh.amard.dao;

import com.etz.gh.amard.entities.AppLog;
import com.etz.gh.amard.entities.Configuration;
import com.etz.gh.amard.entities.EcardQueryResult;
import com.etz.gh.amard.entities.FundGateMerchants;
import com.etz.gh.amard.entities.FundGateTransaction;
import com.etz.gh.amard.entities.FundGateResponse;
import com.etz.gh.amard.entities.Graph;
import com.etz.gh.amard.entities.GraphQueryResult;
import com.etz.gh.amard.entities.Monitor;
import com.etz.gh.amard.entities.Log;
import com.etz.gh.amard.entities.MobileMoney;
import com.etz.gh.amard.entities.Log;
import com.etz.gh.amard.entities.VasGateBillers;
import com.etz.gh.amard.model.TMCNodesTAT;
import com.etz.gh.amard.utilities.Config;
import com.etz.gh.amard.utilities.DBUtils;
import com.etz.gh.amard.utilities.GeneralUtils;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.joining;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.apache.log4j.PropertyConfigurator;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author seth.sebeh
 */
//com.etz.gh.amard.dao.AmardDAO
public class AmardDAO {

    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(AmardDAO.class);

    static {
        PropertyConfigurator.configure("cfg\\log4j.config");
    }

    public List<Monitor> getServiceEndPointAvailabilityMonitors() {
        Session session = DBUtils.getMonitorSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();

        //Create Criteria against a particular persistent class
        CriteriaQuery<Monitor> criteria = cb.createQuery(Monitor.class);

        //Query roots always reference entities
        Root<Monitor> root = criteria.from(Monitor.class);
        //criteria.select(root);
        criteria.select(root).where(cb.equal(root.get("type"), "SEI"));
        List<Monitor> SEIMonitors = session.createQuery(criteria).getResultList();

        logger.info(Thread.currentThread().getName() + " " + "SERVICE ENDPOINT AVAILABILITY ::RECORDS SIZE:: " + SEIMonitors.size());
        session.close();
        return SEIMonitors;
    }

    public List<Monitor> getRequestNotComing() {
        Session session = DBUtils.getMonitorSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();

        //Create Criteria against a particular persistent class
        CriteriaQuery<Monitor> criteria = cb.createQuery(Monitor.class);

        //Query roots always reference entities
        Root<Monitor> root = criteria.from(Monitor.class);
        //criteria.select(root);
        criteria.select(root).where(cb.equal(root.get("type"), "RNC"));

        List<Monitor> SEIMonitors = session.createQuery(criteria).getResultList();

        logger.info(Thread.currentThread().getName() + " " + "REQUEST NOT COMING IN ::RECORDS SIZE:: " + SEIMonitors.size());
        session.close();
        return SEIMonitors;
    }

    public List<Monitor> getMonitors() {
        logger.info(Thread.currentThread().getName() + " " + "Reading all Monitors from database");
        Session session = DBUtils.getMonitorSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();

        //Create Criteria against a particular persistent class
        CriteriaQuery<Monitor> criteria = cb.createQuery(Monitor.class);

        //Query roots always reference entities
        Root<Monitor> root = criteria.from(Monitor.class);
        //criteria.select(root);
        criteria.select(root).where(cb.equal(root.get("enabled"), "1"));

        List<Monitor> monitors = session.createQuery(criteria).getResultList();
        logger.info(Thread.currentThread().getName() + " " + "Done loading monitors");
        logger.info(Thread.currentThread().getName() + " " + "Total monitors retrieved :: " + monitors.size());
        session.close();
        return monitors;
    }

    public static List<Configuration> getConfigData() {
        Session session = DBUtils.getMonitorSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();

        //Create Criteria against a particular persistent class
        CriteriaQuery<Configuration> criteria = cb.createQuery(Configuration.class);

        //Query roots always reference entities
        Root<Configuration> root = criteria.from(Configuration.class);
        criteria.select(root);
        logger.info(Thread.currentThread().getName() + " " + "Loading configuration data");
        List<Configuration> configs = session.createQuery(criteria).getResultList();
        logger.info(Thread.currentThread().getName() + " " + "App Configuration loading done");
        logger.info(Thread.currentThread().getName() + " " + configs);
        session.close();
        return configs;
    }

    public static int log(Monitor monitor, String request_time, String response_time, double tat, int error, String http_code, String message, String other_name) {
        logger.info(Thread.currentThread().getName() + " " + "logging record...");
        Session session = DBUtils.getMonitorSession();
        Transaction tx = null;
        Integer seiID = null;

        try {
            tx = session.beginTransaction();
            Log sl = new Log();
            sl.setName(monitor.getName());
            sl.setDescription(monitor.getDescription());
            sl.setType(monitor.getType());
            sl.setGroup(monitor.getGroup());
            sl.setServer_ip(monitor.getServer_ip());
            sl.setUrl(monitor.getUrl());
            sl.setScheduler_interval(monitor.getScheduler_interval() != null ? String.valueOf(monitor.getScheduler_interval()) : "");
            sl.setIp_address(monitor.getIp_address());
            sl.setIp_port(monitor.getIp_port());
            sl.setHttp_request_method(monitor.getHttp_request_method());
            sl.setMonitor_id(monitor.getId());
            sl.setRequest_time(request_time);
            sl.setResponse_time(response_time);
            sl.setTat(tat);
            sl.setError(error);
            sl.setHttp_code(http_code);
            sl.setMessage(message);
            sl.setOther_name(other_name);
            seiID = (Integer) session.save(sl);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            logger.error(Thread.currentThread().getName() + " " + "An error occured inserting record ", e);
        } finally {
            session.close();
        }
        logger.info(Thread.currentThread().getName() + " " + "finished logging record");
        return seiID;
    }

    //thank u Eugene for the query here
    public static List<Log> getLogRecordsByType(String type) {
        logger.info(Thread.currentThread().getName() + " " + "AmardDAO:getLogRecordsByType => " + type);
        Session session = DBUtils.getMonitorSession();
        Query query = null;
        if (type.equals("ALL")) {
            query = session.createNativeQuery("SELECT * FROM amard.log WHERE id IN (SELECT max(id) FROM amard.log where TIMESTAMPDIFF(MINUTE,created,NOW()) < 61 GROUP BY name) order by request_time desc", Log.class);
        } else if (type.equals("FAILING")) {
            query = session.createNativeQuery("SELECT * FROM amard.log WHERE id IN (SELECT max(id) FROM amard.log where error != '0' AND TIMESTAMPDIFF(MINUTE,created,NOW()) < 61 GROUP BY name) order by request_time desc", Log.class);
        } else if (type.equals("VASGATE")) {
            query = session.createNativeQuery("SELECT * FROM amard.log WHERE id IN (SELECT max(id) FROM amard.log where type = 'VASGATE' AND TIMESTAMPDIFF(MINUTE,created,NOW()) < 61 GROUP BY name) order by request_time desc", Log.class);
        } else if (type.equals("GIP")) {
            query = session.createNativeQuery("SELECT * FROM amard.log WHERE id IN (SELECT max(id) FROM amard.log where type = 'GIP' AND TIMESTAMPDIFF(MINUTE,created,NOW()) < 61 GROUP BY name) order by request_time desc", Log.class);
        } else {
            query = session.createNativeQuery("SELECT * FROM amard.log WHERE id IN (SELECT max(id) FROM amard.log where type = :TYPE AND TIMESTAMPDIFF(MINUTE,created,NOW()) < 61 GROUP BY name) order by request_time desc", Log.class);
            query.setParameter("TYPE", type);
        }

        List<Log> list = query.getResultList();
        System.out.println(list);
        logger.info(Thread.currentThread().getName() + " " + "AmardDAO:getLogRecordsByType => " + type + " SIZE " + list.size());
        session.close();
        return list;
    }

    public static List<Log> getBoaLogRecordsByType(String type) {
        logger.info(Thread.currentThread().getName() + " " + "AmardDAO:getBOALogRecordsByType => " + type);
        Session session = DBUtils.getMonitorSession();
        Query query = null;
        if (type.equals("ALL")) {
            query = session.createNativeQuery("SELECT * FROM amard.log WHERE id IN (SELECT max(id) FROM amard.log where TIMESTAMPDIFF(MINUTE,created,NOW()) < 61 GROUP BY name)  AND name like '%BOA%' order by request_time desc", Log.class);
        } else if (type.equals("FAILING")) {
            query = session.createNativeQuery("SELECT * FROM amard.log WHERE id IN (SELECT max(id) FROM amard.log where error != '0' AND TIMESTAMPDIFF(MINUTE,created,NOW()) < 61 GROUP BY name)  AND name like '%BOA%' order by request_time desc", Log.class);
        } else {
            query = session.createNativeQuery("SELECT * FROM amard.log WHERE id IN (SELECT max(id) FROM amard.log where type = :TYPE AND TIMESTAMPDIFF(MINUTE,created,NOW()) < 61 GROUP BY name)  AND name like '%BOA%' order by request_time desc", Log.class);
            query.setParameter("TYPE", type);
        }

        List<Log> list = query.getResultList();
        System.out.println(list);
        logger.info(Thread.currentThread().getName() + " " + "AmardDAO:getBoaLogRecordsByType => " + type + " SIZE " + list.size());
        session.close();
        return list;
    }

    public static List<Log> getGCBLogRecordsByType(String type) {
        logger.info(Thread.currentThread().getName() + " " + "AmardDAO:getGCBLogRecordsByType => " + type);
        Session session = DBUtils.getMonitorSession();
        Query query = null;
        if (type.equals("ALL")) {
            query = session.createNativeQuery("SELECT * FROM amard.log WHERE id IN (SELECT max(id) FROM amard.log where TIMESTAMPDIFF(MINUTE,created,NOW()) < 61 GROUP BY name) AND name like '%GCB%' order by request_time desc", Log.class);
        } else if (type.equals("FAILING")) {
            query = session.createNativeQuery("SELECT * FROM amard.log WHERE id IN (SELECT max(id) FROM amard.log where error != '0' AND TIMESTAMPDIFF(MINUTE,created,NOW()) < 61 GROUP BY name)  AND name like '%GCB%'  order by request_time desc", Log.class);
        } else {
            query = session.createNativeQuery("SELECT * FROM amard.log WHERE id IN (SELECT max(id) FROM amard.log where type = :TYPE AND TIMESTAMPDIFF(MINUTE,created,NOW()) < 61 GROUP BY name)  AND name like '%GCB%' order by request_time desc", Log.class);
            query.setParameter("TYPE", type);
        }

        List<Log> list = query.getResultList();
        System.out.println(list);
        logger.info(Thread.currentThread().getName() + " " + "AmardDAO:getGCBLogRecordsByType => " + type + " SIZE " + list.size());
        session.close();
        return list;
    }

    public static List<Log> getLogRecordsByTypeOld(String type) {
        logger.info(Thread.currentThread().getName() + " " + "AmardDAO:getLogRecordsByType => " + type);
        Session session = DBUtils.getMonitorSession();
        Query query = null;
        if (type.equals("ALL")) {
            query = session.createNativeQuery("SELECT a.* FROM amard.log a where a.id = b.monitor_id group by a.id order by b.request_time desc", Log.class);
        } else if (type.equals("FAILING")) {
            query = session.createNativeQuery("SELECT a.* FROM amard.log a, amard.log b where a.id = b.monitor_id and b.error != '0' group by a.id order by b.request_time desc", Log.class);
        } else if (type.equals("VASGATE")) {
            query = session.createNativeQuery("SELECT a.* FROM amard.log a, amard.log b where a.id = b.monitor_id and a.type = 'VASGATE' group by b.other_name order by b.request_time desc", Log.class);
        } else {
            query = session.createNativeQuery("SELECT a.* FROM amard.log a, amard.log b where a.id = b.monitor_id and a.type = :TYPE group by a.name order by b.request_time desc", Log.class);
            query.setParameter("TYPE", type);
        }

        List<Log> list = query.getResultList();
        logger.info(Thread.currentThread().getName() + " " + "AmardDAO:getLogRecordsByType => " + type + " SIZE " + list.size());
        session.close();
        return list;
    }

    public static List<GraphQueryResult> getMomoLiveGraphData(String sql, String network, String paymenttype, int interval) {
        Session session = DBUtils.getMobileMoneySession();
        Query query = session.createNativeQuery(sql, GraphQueryResult.class);
        query.setParameter("client", network);
        query.setParameter("paymenttype", paymenttype);
        query.setParameter("interval", interval);
        List<GraphQueryResult> list = query.getResultList();
        session.close();
        return list;
    }

    public static List<Graph> loadGraphTable() {
        Session session = DBUtils.getMonitorSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();

        //Create Criteria against a particular persistent class
        CriteriaQuery<Graph> criteria = cb.createQuery(Graph.class);

        //Query roots always reference entities
        Root<Graph> root = criteria.from(Graph.class);
        //criteria.select(root);
        criteria.select(root).where(cb.equal(root.get("enabled"), "1"));
        logger.info(Thread.currentThread().getName() + " " + "Loading graph data");
        List<Graph> graphList = session.createQuery(criteria).getResultList();
        logger.info(Thread.currentThread().getName() + " " + "Graph data loading done");
        //logger.info(graphList);
        session.close();
        return graphList;
    }

    public static List<GraphQueryResult> runDynamicGraphQuery(String sql_query, String database) {
        Session session = null;
        Query query;
        switch (database) {
            case "10.0.1.230":
                session = DBUtils.getMonitorSession();
                break;
            case "172.16.40.9":
                session = DBUtils.getMobileMoneySession();
                break;
            case "172.16.40.12":
                session = DBUtils.getTmcSession();
                break;
            case "172.16.40.15":
                session = DBUtils.getTmcLiveSession();
                break;
            case "172.16.30.12":
                session = DBUtils.getGmoneySession();
                break;
            case "192.168.150.100":
                session = DBUtils.getGcbecarddbSessionFactory();
                break;
            case "172.16.40.17":
                session = DBUtils.getMobileDBSession();
                break;
            default:
                break;
        }
        query = session.createNativeQuery(sql_query, GraphQueryResult.class);
        List<GraphQueryResult> list = query.getResultList();
        session.close();
        return list;
    }

    public static List<MobileMoney> getMomoCreditDroppedTransactions(String startDate, String endDate) {
        Session session = DBUtils.getMobileMoneySession();
        String sql = "Select * from telcodb.mobilemoney where paymenttype = 'C' and ( CLIENTCODE  is null or RESPCODE = '') and TRNXDATE between '" + startDate + "' AND '" + endDate + "' order by TRNXDATE desc limit 100";
        logger.info(Thread.currentThread().getName() + " " + "Momo credit sql :: " + sql);
        Query query = session.createNativeQuery(sql, MobileMoney.class);
        List<MobileMoney> mmList = query.getResultList();
        session.close();
        return mmList;
    }

    public static long countMomoCreditDroppedTransactions(String startDate, String endDate) {
        Session session = DBUtils.getMobileMoneySession();
        String sql = "Select count(id) from MobileMoney where paymenttype = 'C' and ( CLIENTCODE  is null or RESPCODE = '') and TRNXDATE between '" + startDate + "' AND '" + endDate + "' ";
        logger.info(Thread.currentThread().getName() + " " + "Momo credit count sql :: " + sql);
        Query query = session.createQuery(sql);
        Long count = (Long) query.uniqueResult();
        session.close();
        return count;
    }

    public static double sumMomoCreditDroppedTransactions(String startDate, String endDate) {
        Session session = DBUtils.getMobileMoneySession();
        String sql = "Select ROUND(sum(amount),2) from MobileMoney where paymenttype = 'C' and ( CLIENTCODE  is null or RESPCODE = '') and TRNXDATE between '" + startDate + "' AND '" + endDate + "'";
        logger.info(Thread.currentThread().getName() + " " + "Momo credit sum sql :: " + sql);
        Query query = session.createQuery(sql);
        double sum = (Double) query.uniqueResult();
        session.close();
        return sum;
    }

    public static List<FundGateResponse> getFundgateServerError99Transactions(String startDate, String endDate) {
        Session session = DBUtils.getMobileMoneySession();
        //String sql = "Select * from telcodb.mobilemoney where paymenttype = 'C' and ( CLIENTCODE  is null or RESPCODE = '') and TRNXDATE between '" + startDate + "' AND '" + endDate + "' order by TRNXDATE desc limit 100";
        //String sql = "SELECT * FROM FGDB.fundgate_response B where B.respcode = '99' and B.action='FT' and B.CREATED BETWEEN '" + startDate + "' AND '" + endDate + "' order by B.CREATED desc limit 100";
        String sql = "SELECT B.*,a.merchant_name FROM FGDB.fundgate_response B inner join FGDB.cop_fundgate_info A on B.terminal = a.terminal_id where B.respcode = '99' and B.action IN ('FT','PB','VT') and B.CREATED BETWEEN '" + startDate + "' AND '" + endDate + "' order by B.CREATED desc limit 100";
        logger.info(Thread.currentThread().getName() + " " + "FundgateServerError99 sql :: " + sql);
        Query query = session.createNativeQuery(sql, FundGateResponse.class);
        List<FundGateResponse> fgList = query.getResultList();
        session.close();
        return fgList;
    }

    public static BigInteger countFundgateServerError99Transactions(String startDate, String endDate) {
        Session session = DBUtils.getMobileMoneySession();
        String sql = "SELECT count(B.respId) FROM FGDB.fundgate_response B inner join FGDB.cop_fundgate_info A on B.terminal = a.terminal_id where B.respcode = '99' and B.action IN ('FT','PB','VT') and B.CREATED BETWEEN '" + startDate + "' AND '" + endDate + "'";
        logger.info(Thread.currentThread().getName() + " " + "FundgateServerErro99 count sql :: " + sql);
        Query query = session.createNativeQuery(sql);
        BigInteger count = (BigInteger) query.uniqueResult();
        session.close();
        return count;
    }

    public static double sumFundgateServerError99Transactions(String startDate, String endDate) {
        Session session = DBUtils.getMobileMoneySession();
        String sql = "select ROUND(SUM(a.amount),2) from fgdb.cop_fundgate_info c, fgdb.fundgate_request A inner join fgdb.fundgate_response B on B.terminal = A.terminal where B.respcode = '99' and B.action IN ('FT','PB','VT') AND a.clientref = b.clientref and b.terminal = c.terminal_id AND B.CREATED BETWEEN '" + startDate + "' AND '" + endDate + "'";
        logger.info(Thread.currentThread().getName() + " " + "FundgateServerErro99 sum sql :: " + sql);
        Query query = session.createNativeQuery(sql);
        double sum = (Double) query.uniqueResult();
        session.close();
        return sum;
    }

    public static void getApplogs2() {
        Session session = DBUtils.getMonitorSession();
        String sql = "SELECT m.name,m.description,m.type,m.group,l.id,l.error,l.message,l.request_time,l.created FROM amard.log l inner join amard.monitor m on l.monitor_id = m.id where l.error != '0' order by l.created desc limit " + Config.getPropertyEager("NOTIFICATION_RECORDS_SIZE");
        Query query = session.createNativeQuery(sql);
        List<Object[]> l = query.getResultList();
        for (Object[] a : l) {
            System.out.println(a[0] + " " + a[1]);
        }
        session.close();
    }

    public static String getMRequestReferences(String startDate, String endDate) {
        Session session = DBUtils.getMobileDBSession();
        String sql = "SELECT unique_transid FROM mobiledb.m_requests where appid = 'GCBMobile' and token = 'TAM' AND CREATED BETWEEN '" + startDate + "' AND '" + endDate + "'";
        Query query = session.createNativeQuery(sql);
        List<String> l = query.getResultList();
        logger.info(Thread.currentThread().getName() + " " + "M_REQUEST GCB App Deposit Mobilization Size :: " + l.size());
        String references = l.stream().map(g -> "'" + g + "'").collect(joining(","));

        session.close();
        return references;
    }

    //method to test app mobilization
    public static void appMobilization() {
        String references = "'09FG05160135536424043','09FG05160135536424043','justx-el-1141888-i41i-284'";
        Session session = DBUtils.getMonitorSession();
        String sql = "select trans_seq as id, count(*) as value , CONCAT(DATE_FORMAT(TRANS_DATE,'%H'),'Hrs') as label from ecarddb.e_tmcrequest where response_code = '00' and TRANS_DATA in (<:REFERENCES_MREQUEST>)";
        if (sql.contains("<:REFERENCES_MREQUEST>")) {
            //for GCB deposit mobilization select references 
            sql = sql.replace("<:REFERENCES_MREQUEST>", references);
        }
        Query query = session.createNativeQuery(sql);
        List<Object[]> l = query.getResultList();
        for (Object[] a : l) {
            System.out.println(a[0] + " " + a[1]);
            System.out.println("SIZE " + l.size());
        }
        session.close();
    }

    public static List<FundGateMerchants> getFgMerchants() {
        Session session = DBUtils.getMobileMoneySession();
        String sql = "Select terminal_id, merchant_name from fgdb.cop_fundgate_info order by merchant_name";
        Query query = session.createNativeQuery(sql);
        List<Object[]> result = query.getResultList();
        List<FundGateMerchants> fgMerchantsList = new ArrayList<>();
        for (Object[] a : result) {
            FundGateMerchants fm = new FundGateMerchants();
            fm.setTerminal_id((String) a[0]);
            fm.setMerchant_name((String) a[1]);
            fgMerchantsList.add(fm);
        }
        session.close();
        //System.out.println(fgMerchantsList);
        return fgMerchantsList;
    }

    public static List<VasGateBillers> getVasGateBillers() {
        Session session = DBUtils.getMobileMoneySession();
        String sql = "SELECT distinct(alias),name  FROM vasdb2.e_vas_node order by name";
        Query query = session.createNativeQuery(sql);
        List<Object[]> result = query.getResultList();
        List<VasGateBillers> list = new ArrayList<>();
        for (Object[] a : result) {
            VasGateBillers fm = new VasGateBillers();
            fm.setAlias((String) a[0]);
            fm.setName((String) a[1]);
            list.add(fm);
        }
        System.out.println(list);
        session.close();
        return list;
    }

    public static List<Log> getApplogs() {
        Session session = DBUtils.getMonitorSession();
        String sql = "SELECT * FROM amard.log WHERE error != '0' and id IN (SELECT max(id) FROM amard.log where TIMESTAMPDIFF(MINUTE,created,NOW()) < 61 GROUP BY name) order by request_time desc";
        //String sql = "SELECT * FROM amard.log where error != '0' AND TIMESTAMPDIFF(MINUTE,created,NOW()) < 61 order by request_time desc limit " + Config.getPropertyEager("NOTIFICATION_RECORDS_SIZE");
        //String sql = "SELECT m.name,m.description,m.type,m.monitor_group,l.id,l.error,l.message,l.request_time,l.response_time,l.created FROM amard.log l inner join monitor.monitor m on l.monitor_id = m.id where l.error != '0' order by l.created desc limit " + Config.getPropertyEager("NOTIFICATION_RECORDS_SIZE");
        Query query = session.createNativeQuery(sql, Log.class);
        List<Log> list = query.getResultList();
        session.close();
        return list;
    }

    public static List<Log> getBOAApplogs() {
        Session session = DBUtils.getMonitorSession();
        String sql = "SELECT * FROM amard.log WHERE error != '0' and id IN (SELECT max(id) FROM amard.log where TIMESTAMPDIFF(MINUTE,created,NOW()) < 61 GROUP BY name) AND name like '%BOA%' order by request_time desc";
        //String sql = "SELECT * FROM amard.log where error != '0' AND TIMESTAMPDIFF(MINUTE,created,NOW()) < 61 order by request_time desc limit " + Config.getPropertyEager("NOTIFICATION_RECORDS_SIZE");
        //String sql = "SELECT m.name,m.description,m.type,m.monitor_group,l.id,l.error,l.message,l.request_time,l.response_time,l.created FROM amard.log l inner join monitor.monitor m on l.monitor_id = m.id where l.error != '0' order by l.created desc limit " + Config.getPropertyEager("NOTIFICATION_RECORDS_SIZE");
        Query query = session.createNativeQuery(sql, Log.class);
        List<Log> list = query.getResultList();
        session.close();
        return list;
    }

    public static List<Log> getGCBApplogs() {
        Session session = DBUtils.getMonitorSession();
        String sql = "SELECT * FROM amard.log WHERE error != '0' and id IN (SELECT max(id) FROM amard.log where TIMESTAMPDIFF(MINUTE,created,NOW()) < 61 GROUP BY name) AND name like '%GCB%' order by request_time desc";
        //String sql = "SELECT * FROM amard.log where error != '0' AND TIMESTAMPDIFF(MINUTE,created,NOW()) < 61 order by request_time desc limit " + Config.getPropertyEager("NOTIFICATION_RECORDS_SIZE");
        //String sql = "SELECT m.name,m.description,m.type,m.monitor_group,l.id,l.error,l.message,l.request_time,l.response_time,l.created FROM amard.log l inner join monitor.monitor m on l.monitor_id = m.id where l.error != '0' order by l.created desc limit " + Config.getPropertyEager("NOTIFICATION_RECORDS_SIZE");
        Query query = session.createNativeQuery(sql, Log.class);
        List<Log> list = query.getResultList();
        session.close();
        return list;
    }

    public static List<TMCNodesTAT> getTMCNodesTAT() {
        Session session = DBUtils.getTmcLiveSession();
        String sql = "select target , round(avg(TIME_TO_SEC(TIMEDIFF(RESPONSE_TIME, TRANS_DATE))), 0) as tat from ecarddb.e_tmcrequest where trans_date > DATE_SUB(now(), INTERVAL 5 MINUTE) and target != '' group by target";
        Query query = session.createNativeQuery(sql);
        List<Object[]> result = query.getResultList();
        List<TMCNodesTAT> list = new ArrayList<>();
        for (Object[] a : result) {
            TMCNodesTAT tt = new TMCNodesTAT();
            tt.setNode((String) a[0]);
            tt.setTat(Integer.parseInt((String) a[1]));
            list.add(tt);
        }
        System.out.println(list);
        session.close();
        return list;
    }

    public static List<TMCNodesTAT> getGCBTMCNodesTAT() {
        Session session = DBUtils.getTmcLiveSession();
        String sql = "select target , round(avg(TIME_TO_SEC(TIMEDIFF(RESPONSE_TIME, TRANS_DATE))), 0) as tat from ecarddb.e_tmcrequest where trans_date > DATE_SUB(now(), INTERVAL 5 MINUTE) and target != '' and target like '%GCB%' group by target";
        Query query = session.createNativeQuery(sql);
        List<Object[]> result = query.getResultList();
        List<TMCNodesTAT> list = new ArrayList<>();
        for (Object[] a : result) {
            TMCNodesTAT tt = new TMCNodesTAT();
            tt.setNode((String) a[0]);
            tt.setTat(Integer.parseInt((String) a[1]));
            list.add(tt);
        }
        System.out.println(list);
        session.close();
        return list;
    }

    public static void testGmoneyTable() {
        Session session = DBUtils.getGmoneySession();
        Query query = session.createNativeQuery("select * from gcbmm.rslog limit 10");
        List<Object[]> result = query.getResultList();
        int listSize = result.size();
        System.out.println("SIZE " + listSize);
    }

    public static void cleanLogData() {
        Transaction tx = null;
        Session session = DBUtils.getMonitorSession();
        tx = session.beginTransaction();
        //String hql = "delete from Log";
        String hql = Config.getProperty("DATA.CLEANER.QUERY");
        Query query = session.createNativeQuery(hql);
//        Query query = session.createQuery(hql);
        if (query.executeUpdate() > 0) {
            logger.info(Thread.currentThread().getName() + " Table Log cleared");
        } else {
            logger.error(Thread.currentThread().getName() + " Table Log failed to clear");
        }
        tx.commit();
    }

    public static List<EcardQueryResult> getEcardQueryResult(String filter, String startDate, String endDate) {
        Session session = DBUtils.getGcbecarddbSessionFactory();
        String sql = "SELECT A.ID,A.UNIQUE_TRANSID,A.DE37,B.DE37 AS B37,A.DE3,A.DE43,A.CREATED,A.EXT_1,A.DB_TAT,A.DB_TAT AS FLEX_TAT FROM ecarddb.e_hostrequestlog A LEFT JOIN ecarddb.e_hostresponselog B on A.DE37 = B.DE37 where A.created between  '" + startDate + "' AND '" + endDate + "' AND B.DE37 IS NULL AND A.EXT_1 IS NOT NULL order by A.created DESC limit 700";
        if (filter.equals("transfer")) {
            sql = "SELECT A.ID,A.UNIQUE_TRANSID,A.DE37,B.DE37 AS B37,A.DE3,A.DE43,A.CREATED,A.EXT_1,A.DB_TAT,A.DB_TAT AS FLEX_TAT FROM ecarddb.e_hostrequestlog A LEFT JOIN ecarddb.e_hostresponselog B on A.DE37 = B.DE37 where A.created between  '" + startDate + "' AND '" + endDate + "' AND A.DE3='402000' AND B.DE37 IS NULL AND A.EXT_1 IS NOT NULL order by A.created DESC limit 700";
        } else if (filter.equals("balance")) {
            sql = "SELECT A.ID,A.UNIQUE_TRANSID,A.DE37,B.DE37 AS B37,A.DE3,A.DE43,A.CREATED,A.EXT_1,A.DB_TAT,A.DB_TAT AS FLEX_TAT FROM ecarddb.e_hostrequestlog A LEFT JOIN ecarddb.e_hostresponselog B on A.DE37 = B.DE37 where A.created between  '" + startDate + "' AND '" + endDate + "' AND A.DE3='312000' AND B.DE37 IS NULL AND A.EXT_1 IS NOT NULL order by A.created DESC limit 700";
        } else if (filter.equals("statement")) {
            sql = "SELECT A.ID,A.UNIQUE_TRANSID,A.DE37,B.DE37 AS B37,A.DE3,A.DE43,A.CREATED,A.EXT_1,A.DB_TAT,A.DB_TAT AS FLEX_TAT FROM ecarddb.e_hostrequestlog A LEFT JOIN ecarddb.e_hostresponselog B on A.DE37 = B.DE37 where A.created between  '" + startDate + "' AND '" + endDate + "' AND A.DE3='382000' AND B.DE37 IS NULL AND A.EXT_1 IS NOT NULL order by A.created DESC limit 700";
        }
        System.out.println("query to run " + sql);
        Query query = session.createNativeQuery(sql, EcardQueryResult.class);
        List<EcardQueryResult> list = query.getResultList();
        session.close();
        return list;
    }

    public static void main(String[] args) {
        System.out.println(getEcardQueryResult("transfer", "2022-01-16 00:00", "2022-01-19 00:00"));
        //List<Log> list = new AmardDAO().getLogRecordsByType("SEA");
        //JSONArray jsArray = new JSONArray(list);
        //System.out.println(jsArray);
        //logger.info(new AmardDAO().getServiceEndPointAvailabilityMonitors());

        // List<Graph> graph = new AmardDAO().getMomoLiveGraphData();
//        System.out.println(GeneralUtils.getValuesArray(graph));
//        System.out.println(GeneralUtils.getLabelsArray(graph));
//        GraphQueryResult g = list.get(5);
//        JSONArray jsArray = new JSONArray(list);
//        System.out.println(jsArray);
//        System.out.println(g);
        // loadGraphTable();
        //System.out.println(sumFundgateServerError99Transactions("2019-11-05 00:00", "2019-12-29 23:56"));
        //System.out.println(countFundgateServerError99Transactions("2019-11-05 00:00", "2019-12-29 23:56"));
        //getApplogs();
        //getFgMerchants();
//        getVasGateBillers();
//        getLogRecordsByType("ALL");
//        testGmoneyTable();
        // getMRequestReferences("2018-08-03 10:41:14", "2018-09-03 10:41:14");
        //appMobilization();
//        System.out.println(new AmardDAO().getMonitors());
        AmardDAO.cleanLogData();
    }
}
