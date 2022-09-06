package com.etz.gh.amard.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class GraphQueryResult1 implements Serializable {

    private static final long serialVersionUID = -913736157738L;

    @Id
    private int id;
    private String value;
    private String label;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
