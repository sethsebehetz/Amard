package com.etz.gh.amard.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "GRAPH")
public class Graph implements Serializable {

    private static final long serialVersionUID = -91373457738L;

    @Id
    private int id;
    private String type;
    private String title;
    private String legends;
    private String x_values;
    private String y_values;
    private String labels;
    private String query;
    private String query2;
    private String limit;
    private String color;
    private String database;
    private String group;
    private int enabled;
    private String y1_label;
    private String y2_label;
    private String x_label;
    private boolean show_xdata;
    private Date created;
    private Date updated;
    private String table;
    private String ext1;
    private String ext2;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getX_values() {
        return x_values;
    }

    public void setX_values(String x_values) {
        this.x_values = x_values;
    }

    public String getY_values() {
        return y_values;
    }

    public void setY_values(String y_values) {
        this.y_values = y_values;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getY1_label() {
        return y1_label;
    }

    public void setY1_label(String y1_label) {
        this.y1_label = y1_label;
    }

    public String getY2_label() {
        return y2_label;
    }

    public void setY2_label(String y2_label) {
        this.y2_label = y2_label;
    }

    public String getX_label() {
        return x_label;
    }

    public void setX_label(String x_label) {
        this.x_label = x_label;
    }

    public boolean isShow_xdata() {
        return show_xdata;
    }

    public void setShow_xdata(boolean show_xdata) {
        this.show_xdata = show_xdata;
    }

    public String getLegends() {
        return legends;
    }

    public void setLegends(String legends) {
        this.legends = legends;
    }

    public String getQuery2() {
        return query2;
    }

    public void setQuery2(String query2) {
        this.query2 = query2;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    public String getExt2() {
        return ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2;
    }
    
    @Override
    public String toString() {
        return "Graph{" + "id=" + id + ", type=" + type + ", title=" + title + ", x_values=" + x_values + ", y_values=" + y_values + ", labels=" + labels + ", query=" + query + ", limit=" + limit + ", color=" + color + ", database=" + database + ", group=" + group + ", status=" + enabled + ", y1_label=" + y1_label + ", y2_label=" + y2_label + ", x_label=" + x_label + ", show_xdata=" + show_xdata + ", created=" + created + ", updated=" + updated + '}';
    }

    

}
