
package com.etz.gh.amard.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ResponseTable implements Serializable {

    private static final long serialVersionUID = -5301132391355157738L;

    @Id
    private int id;
    private int monitor_id;
    private String request_time;
    private String response_time;
    private double tat;
    private String name;
    private String response_header;
    private int error;
    private String http_code;
    private String type;
    private String monitor_group;
    private String message;
    private String description;
    private String url;
    private String scheduler_interval;
    private String server_ip;
    private String ip_address;
    private String ip_port;
    private String http_request_method;
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMonitor_id() {
        return monitor_id;
    }

    public void setMonitor_id(int monitor_id) {
        this.monitor_id = monitor_id;
    }
    
    public String getRequest_time() {
        return request_time;
    }

    public void setRequest_time(String request_time) {
        this.request_time = request_time;
    }

    public String getResponse_time() {
        return response_time;
    }

    public void setResponse_time(String response_time) {
        this.response_time = response_time;
    }

    public double getTat() {
        return tat;
    }

    public void setTat(double tat) {
        this.tat = tat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResponse_header() {
        return response_header;
    }

    public void setResponse_header(String response_header) {
        this.response_header = response_header;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMonitor_group() {
        return monitor_group;
    }

    public void setMonitor_group(String monitor_group) {
        this.monitor_group = monitor_group;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getScheduler_interval() {
        return scheduler_interval;
    }

    public void setScheduler_interval(String scheduler_interval) {
        this.scheduler_interval = scheduler_interval;
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
    
    
   
}
