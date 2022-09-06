/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etz.gh.amard.entities;

/**
 *
 * @author seth.sebeh
 */
public class VasGateBillers {
    String alias;
    String name;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "VasGateBillers{" + "alias=" + alias + ", name=" + name + '}';
    }
    
}
