package com.pojo;

public class Relation {
    private int rid;
    private String username;
    private int eid;
    private String isleader;
    private double allocationFunds;

    public Relation() {
    }

    public Relation(int rid, String username, int eid, String isleader, double allocationFunds) {
        this.rid = rid;
        this.username = username;
        this.eid = eid;
        this.isleader = isleader;
        this.allocationFunds = allocationFunds;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getEid() {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public String getIsleader() {
        return isleader;
    }

    public void setIsleader(String isleader) {
        this.isleader = isleader;
    }

    public double getAllocationFunds() {
        return allocationFunds;
    }

    public void setAllocationFunds(double allocationFunds) {
        this.allocationFunds = allocationFunds;
    }

    @Override
    public String toString() {
        return "Relation{" +
                "rid=" + rid +
                ", username='" + username + '\'' +
                ", eid=" + eid +
                ", isleader='" + isleader + '\'' +
                ", allocationFunds=" + allocationFunds +
                '}';
    }
}
