package com.etz.gh.amard.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class EcardQueryResult implements Serializable {

    private static final long serialVersionUID = -93336157738L;

    public EcardQueryResult() {}
//A.ID,A.UNIQUE_TRANSID,A.DE37,B.DE37 AS B37,A.DE3,A.CREATED,A.EXT_1   

    @Id
    private long ID;
    private String UNIQUE_TRANSID;
    private String DE37;
    private String DE3;
    private String DE43;
    private int FLEX_TAT;
    private int DB_TAT;
    private String EXT_1;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED", insertable = false)
    private Date CREATED;

    public long getId() {
        return ID;
    }

    public void setId(long ID) {
        this.ID = ID;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getUNIQUE_TRANSID() {
        return UNIQUE_TRANSID;
    }

    public void setUNIQUE_TRANSID(String UNIQUE_TRANSID) {
        this.UNIQUE_TRANSID = UNIQUE_TRANSID;
    }

    public String getDE37() {
        return DE37;
    }

    public void setDE37(String DE37) {
        this.DE37 = DE37;
    }

    public String getDE3() {
        return DE3;
    }

    public void setDE3(String DE3) {
        this.DE3 = DE3;
    }

    public String getDE43() {
        return DE43;
    }

    public void setDE43(String DE43) {
        this.DE43 = DE43;
    }

    public Date getCREATED() {
        return CREATED;
    }

    public void setCREATED(Date CREATED) {
        this.CREATED = CREATED;
    }
    
    

    public int getFLEX_TAT() {
        return FLEX_TAT;
    }

    public void setFLEX_TAT(int FLEX_TAT) {
        this.FLEX_TAT = FLEX_TAT;
    }

    public int getDB_TAT() {
        return DB_TAT;
    }

    public void setDB_TAT(int DB_TAT) {
        this.DB_TAT = DB_TAT;
    }

    public String getEXT_1() {
        return EXT_1;
    }

    public void setEXT_1(String EXT_1) {
        this.EXT_1 = EXT_1;
    }

    public Date getCreated() {
        return CREATED;
    }

    public void setCreated(Date CREATED) {
        this.CREATED = CREATED;
    }

    @Override
    public String toString() {
        return "EcardQueryResult{" + "ID=" + ID + ", UNIQUE_TRANSID=" + UNIQUE_TRANSID + ", DE37=" + DE37 + ", DE3=" + DE3 + ", FLEX_TAT=" + FLEX_TAT + ", DB_TAT=" + DB_TAT + ", EXT_1=" + EXT_1 + ", CREATED=" + CREATED + '}';
    }
    
}
