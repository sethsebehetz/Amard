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
@Table(name = "log_")
public class Log1_ implements Serializable {

    private static final long serialVersionUID = -5301132391355157738L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    private int monitor_id;
    private String request_time;
    private String response_time;
    private double tat;
    private String response_header;
    private int error;
    private String http_code;
    private String sgroup;
    private String message;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", insertable = false)
    private Date created;
    private String other_name;

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

    public String getSgroup() {
        return sgroup;
    }

    public void setSgroup(String sgroup) {
        this.sgroup = sgroup;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
}
