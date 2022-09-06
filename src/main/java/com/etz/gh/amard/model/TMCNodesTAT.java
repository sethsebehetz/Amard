package com.etz.gh.amard.model;

/**
 *
 * @author seth.sebeh
 */
public class TMCNodesTAT{
  private int id;
  private String node;
  private int tat;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public int getTat() {
        return tat;
    }

    public void setTat(int tat) {
        this.tat = tat;
    }

    @Override
    public String toString() {
        return "TMCNodesTAT{" + "id=" + id + ", node=" + node + ", tat=" + tat + '}';
    }
  
    
}
