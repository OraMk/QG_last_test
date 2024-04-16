package com.pojo;

public class Enterprise {
    private int eid;
    private String eName;
    private long number;
    private String size;
    private String direction;
    private String publicMode;
    private String totalFund;
    private String introduction;
    private String eBanned;

    public Enterprise() {

    }

    public Enterprise(int eid, String eName, long number, String size, String direction, String publicMode, String totalFund, String introduction, String eBanned) {
        this.eid = eid;
        this.eName = eName;
        this.number = number;
        this.size = size;
        this.direction = direction;
        this.publicMode = publicMode;
        this.totalFund = totalFund;
        this.introduction = introduction;
        this.eBanned = eBanned;
    }

    public int getEid() {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public String geteName() {
        return eName;
    }

    public void seteName(String eName) {
        this.eName = eName;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getPublicMode() {
        return publicMode;
    }

    public void setPublicMode(String publicMode) {
        this.publicMode = publicMode;
    }

    public String getTotalFund() {
        return totalFund;
    }

    public void setTotalFund(String totalFund) {
        this.totalFund = totalFund;
    }

    public String geteBanned() {
        return eBanned;
    }

    public void seteBanned(String eBanned) {
        this.eBanned = eBanned;
    }

    @Override
    public String toString() {
        return "Enterprise{" +
                "eid=" + eid +
                ", eName='" + eName + '\'' +
                ", number='" + number + '\'' +
                ", size='" + size + '\'' +
                ", direction='" + direction + '\'' +
                ", publicMode='" + publicMode + '\'' +
                ", totalFund='" + totalFund + '\'' +
                ", eBanned='" + eBanned + '\'' +
                '}';
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}