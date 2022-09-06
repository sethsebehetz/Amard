package com.etz.gh.amard.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author seth.sebeh
 */
@Entity
@Table(name = "MONITOR")
public class Monitor implements Serializable {

    private static final long serialVersionUID = 3654041047970204950L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    private String name;
    private String type;
    private String group;
    private String http_request_method;
    private String ip_address;
    private String ip_port;
    private String url;
    private String truststore_location;
    private String truststore_password;
    private String keystore_location;
    private String keystore_password;
    private String ssl_type;
    private Integer scheduler_interval;
    private Integer enabled;
    private String description;
    private String count;
    private Double threshold;
    private String query_interval;
    private String query;
    private Integer connect_timeout;
    private String server_ip;
    private String request_xml;
    private String database;
    private String alarm;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

   
    public String getHttp_request_method() {
        return http_request_method;
    }

    public void setHttp_request_method(String http_request_method) {
        this.http_request_method = http_request_method;
    }

    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }

    public String getIp_port() {
        return ip_port;
    }

    public void setIp_port(String ip_port) {
        this.ip_port = ip_port;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTruststore_location() {
        return truststore_location;
    }

    public void setTruststore_location(String truststore_location) {
        this.truststore_location = truststore_location;
    }

    public String getTruststore_password() {
        return truststore_password;
    }

    public void setTruststore_password(String truststore_password) {
        this.truststore_password = truststore_password;
    }

    public String getKeystore_location() {
        return keystore_location;
    }

    public void setKeystore_location(String keystore_location) {
        this.keystore_location = keystore_location;
    }

    public String getKeystore_password() {
        return keystore_password;
    }

    public void setKeystore_password(String keystore_password) {
        this.keystore_password = keystore_password;
    }

    public String getSsl_type() {
        return ssl_type;
    }

    public void setSsl_type(String ssl_type) {
        this.ssl_type = ssl_type;
    }

    public Integer getScheduler_interval() {
        return scheduler_interval;
    }

    public void setScheduler_interval(Integer scheduler_interval) {
        this.scheduler_interval = scheduler_interval;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public Double getThreshold() {
        return threshold;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    public String getQuery_interval() {
        return query_interval;
    }

    public void setQuery_interval(String query_interval) {
        this.query_interval = query_interval;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Integer getConnect_timeout() {
        return connect_timeout;
    }

    public void setConnect_timeout(Integer connect_timeout) {
        this.connect_timeout = connect_timeout;
    }

    public String getServer_ip() {
        return server_ip;
    }

    public void setServer_ip(String server_ip) {
        this.server_ip = server_ip;
    }

    public String getRequest_xml() {
        return request_xml;
    }

    public void setRequest_xml(String request_xml) {
        this.request_xml = request_xml;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }
}
