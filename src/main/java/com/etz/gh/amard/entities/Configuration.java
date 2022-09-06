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
@Table(name = "configuration")
public class Configuration implements Serializable {

    private static final long serialVersionUID = 8925572934546049068L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    private String k;
    private String v;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    @Override
    public String toString() {
        return "Configuration{" + "id=" + id + ", k=" + k + ", v=" + v + '}';
    }
    
}
