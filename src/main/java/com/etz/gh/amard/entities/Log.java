package com.etz.gh.amard.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "log")
public class Log implements Serializable {

    private static final long serialVersionUID = -5301132391355157738L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    private String name;
    private String description;
    private String type;
    @Column(name = "monitor_group")
    private String group;
    private String url;
    private String server_ip;
    private String scheduler_interval;
    private String ip_address;
    private String ip_port;
    private String http_request_method;
    private int error;
    private String http_code;
    private String message;
    private String request_time;
    private String response_header;
    private String response_time;
    private String sgroup;
    private double tat;
    private int monitor_id;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", insertable = false)
    private Date created;
    private String other_name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getServer_ip() {
        return server_ip;
    }

    public void setServer_ip(String server_ip) {
        this.server_ip = server_ip;
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

    public String getHttp_request_method() {
        return http_request_method;
    }

    public void setHttp_request_method(String http_request_method) {
        this.http_request_method = http_request_method;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getHttp_code() {
        return http_code;
    }

    public void setHttp_code(String http_code) {
        this.http_code = http_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequest_time() {
        return request_time;
    }

    public void setRequest_time(String request_time) {
        this.request_time = request_time;
    }

    public String getResponse_header() {
        return response_header;
    }

    public void setResponse_header(String response_header) {
        this.response_header = response_header;
    }

    public String getResponse_time() {
        return response_time;
    }

    public void setResponse_time(String response_time) {
        this.response_time = response_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getSgroup() {
        return sgroup;
    }

    public void setSgroup(String sgroup) {
        this.sgroup = sgroup;
    }

    

    public double getTat() {
        return tat;
    }

    public void setTat(double tat) {
        this.tat = tat;
    }

    public int getMonitor_id() {
        return monitor_id;
    }

    public void setMonitor_id(int monitor_id) {
        this.monitor_id = monitor_id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getOther_name() {
        return other_name;
    }

    public void setOther_name(String other_name) {
        this.other_name = other_name;
    }

    public String getScheduler_interval() {
        return scheduler_interval;
    }

    public void setScheduler_interval(String scheduler_interval) {
        this.scheduler_interval = scheduler_interval;
    }
    
    @Override
    public String toString() {
        return "Log{" + "id=" + id + ", name=" + name + ", description=" + description + ", type=" + type + ", monitor_group=" + group + ", url=" + url + ", server_ip=" + server_ip + ", scheduler_interval=" + scheduler_interval + ", ip_address=" + ip_address + ", ip_port=" + ip_port + ", http_request_method=" + http_request_method + ", error=" + error + ", http_code=" + http_code + ", message=" + message + ", request_time=" + request_time + ", response_header=" + response_header + ", response_time=" + response_time + ", sgroup=" + sgroup + ", tat=" + tat + ", monitor_id=" + monitor_id + ", created=" + created + ", other_name=" + other_name + '}';
    }
}
