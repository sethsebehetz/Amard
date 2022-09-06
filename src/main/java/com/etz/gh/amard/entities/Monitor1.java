//package com.etz.gh.amard.entities;
//
//import java.io.Serializable;
//import java.util.Date;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.Table;
//
///**
// *
// * @author seth.sebeh
// */
//@Entity
//@Table(name = "MONITOR")
//public class Monitor1 implements Serializable {
//
//    private static final long serialVersionUID = 3654041047970204950L;
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private int id;
//
//    private String name;
//    private String type;
//    private String group;
//    private String http_request_type;
//    private String http_request_method;
//    private String ip_address;
//    private String ip_port;
//    private String url;
//    private Date request_time;
//    private Date response_time;
//    private Double tat;
//    private String truststore_location;
//    private String truststore_password;
//    private String keystore_location;
//    private String keystore_password;
//    private String ssl_type;
//    private Integer bridge;
//    private Integer interval;
//    private String priority;
//    private Integer enabled;
//    private String description;
//    private String bridge_ip_address;
//    private String bridge_port;
//    private String bridge_url;
//    private String count;
//    private Double threshold;
//    private String period;
//    private String query;
//    private Integer connect_timeout;
//    private String server_ip;
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//
//    public String getGroup() {
//        return group;
//    }
//
//    public void setGroup(String group) {
//        this.group = group;
//    }
//    
//    public String getHttp_request_type() {
//        return http_request_type;
//    }
//
//    public void setHttp_request_type(String http_request_type) {
//        this.http_request_type = http_request_type;
//    }
//
//    public String getHttp_request_method() {
//        return http_request_method;
//    }
//
//    public void setHttp_request_method(String http_request_method) {
//        this.http_request_method = http_request_method;
//    }
//
//    public String getIp_address() {
//        return ip_address;
//    }
//
//    public void setIp_address(String ip_address) {
//        this.ip_address = ip_address;
//    }
//
//    public String getIp_port() {
//        return ip_port;
//    }
//
//    public void setIp_port(String ip_port) {
//        this.ip_port = ip_port;
//    }
//
//    public String getUrl() {
//        return url;
//    }
//
//    public void setUrl(String url) {
//        this.url = url;
//    }
//
//    public java.util.Date getRequest_time() {
//        return request_time;
//    }
//
//    public void setRequest_time(Date request_time) {
//        this.request_time = request_time;
//    }
//
//    public java.util.Date getResponse_time() {
//        return response_time;
//    }
//
//    public void setResponse_time(Date response_time) {
//        this.response_time = response_time;
//    }
//
//    public Double getTat() {
//        return tat;
//    }
//
//    public void setTat(Double tat) {
//        this.tat = tat;
//    }
//
//    public String getTruststore_location() {
//        return truststore_location;
//    }
//
//    public void setTruststore_location(String truststore_location) {
//        this.truststore_location = truststore_location;
//    }
//
//    public String getTruststore_password() {
//        return truststore_password;
//    }
//
//    public void setTruststore_password(String truststore_password) {
//        this.truststore_password = truststore_password;
//    }
//
//    public String getKeystore_location() {
//        return keystore_location;
//    }
//
//    public void setKeystore_location(String keystore_location) {
//        this.keystore_location = keystore_location;
//    }
//
//    public String getKeystore_password() {
//        return keystore_password;
//    }
//
//    public void setKeystore_password(String keystore_password) {
//        this.keystore_password = keystore_password;
//    }
//
//    public String getSsl_type() {
//        return ssl_type;
//    }
//
//    public void setSsl_type(String ssl_type) {
//        this.ssl_type = ssl_type;
//    }
//
//    public Integer getBridge() {
//        return bridge;
//    }
//
//    public void setBridge(Integer bridge) {
//        this.bridge = bridge;
//    }
//
//    public Integer getInterval() {
//        return interval;
//    }
//
//    public void setInterval(Integer interval) {
//        this.interval = interval;
//    }
//
//    public String getPriority() {
//        return priority;
//    }
//
//    public void setPriority(String priority) {
//        this.priority = priority;
//    }
//
//    public Integer getEnabled() {
//        return enabled;
//    }
//
//    public void setEnabled(Integer enabled) {
//        this.enabled = enabled;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public String getBridge_ip_address() {
//        return bridge_ip_address;
//    }
//
//    public void setBridge_ip_address(String bridge_ip_address) {
//        this.bridge_ip_address = bridge_ip_address;
//    }
//
//    public String getBridge_port() {
//        return bridge_port;
//    }
//
//    public void setBridge_port(String bridge_port) {
//        this.bridge_port = bridge_port;
//    }
//
//    public String getBridge_url() {
//        return bridge_url;
//    }
//
//    public void setBridge_url(String bridge_url) {
//        this.bridge_url = bridge_url;
//    }
//
//    public String getCount() {
//        return count;
//    }
//
//    public void setCount(String count) {
//        this.count = count;
//    }
//
//    public Double getThreshold() {
//        return threshold;
//    }
//
//    public void setThreshold(Double threshold) {
//        this.threshold = threshold;
//    }
//
//    public String getPeriod() {
//        return period;
//    }
//
//    public void setPeriod(String period) {
//        this.period = period;
//    }
//
//    public String getQuery() {
//        return query;
//    }
//
//    public void setQuery(String query) {
//        this.query = query;
//    }
//
//    public Integer getConnect_timeout() {
//        return connect_timeout;
//    }
//
//    public void setConnect_timeout(Integer connect_timeout) {
//        this.connect_timeout = connect_timeout;
//    }
//
//    public String getServer_ip() {
//        return server_ip;
//    }
//
//    public void setServer_ip(String server_ip) {
//        this.server_ip = server_ip;
//    }
//
//    @Override
//    public String toString() {
//        return "Service{" + "id=" + id + ", name=" + name + ", type=" + type + ", group=" + group + ", http_request_type=" + http_request_type + ", http_request_method=" + http_request_method + ", ip_address=" + ip_address + ", ip_port=" + ip_port + ", url=" + url + ", request_time=" + request_time + ", response_time=" + response_time + ", tat=" + tat + ", truststore_location=" + truststore_location + ", truststore_password=" + truststore_password + ", keystore_location=" + keystore_location + ", keystore_password=" + keystore_password + ", ssl_type=" + ssl_type + ", bridge=" + bridge + ", interval=" + interval + ", priority=" + priority + ", enabled=" + enabled + ", description=" + description + ", bridge_ip_address=" + bridge_ip_address + ", bridge_port=" + bridge_port + ", bridge_url=" + bridge_url + ", count=" + count + ", threshold=" + threshold + ", period=" + period + ", query=" + query + ", connect_timeout=" + connect_timeout + '}';
//    }
//    
//    
//}
