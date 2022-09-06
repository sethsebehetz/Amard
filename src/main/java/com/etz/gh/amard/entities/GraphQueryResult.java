package com.etz.gh.amard.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class GraphQueryResult implements Serializable {

    private static final long serialVersionUID = -913736157738L;

    public GraphQueryResult() {
    }

    public GraphQueryResult(String id, String value, String label) {
        this.id = id;
        this.value = value;
        this.label = label;
    }

    @Id
    private String id;
    private String value;
    private String label;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "Graph{" + "id=" + id + ", value=" + value + ", label=" + label + '}';
    }   
       
}
