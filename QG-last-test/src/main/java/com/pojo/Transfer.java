package com.pojo;

import java.util.Date;

public class Transfer {
    private long tid;
    private String userPayer;
    private String enterprisePayer;
    private String userPayee;
    private String enterprisePayee;
    private String date;
    private double amount;
    private String description;
    private String isTip;
    private String isAccept;

    public Transfer() {
    }

    public Transfer(long tid, String userPayer, String enterprisePayer, String userPayee, String enterprisePayee, String date, double amount, String description, String isTip, String isAccept) {
        this.tid = tid;
        this.userPayer = userPayer;
        this.enterprisePayer = enterprisePayer;
        this.userPayee = userPayee;
        this.enterprisePayee = enterprisePayee;
        this.date = date;
        this.amount = amount;
        this.description = description;
        this.isTip = isTip;
        this.isAccept = isAccept;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTid() {
        return tid;
    }

    public void setTid(long tid) {
        this.tid = tid;
    }

    public String getUserPayer() {
        return userPayer;
    }

    public void setUserPayer(String userPayer) {
        this.userPayer = userPayer;
    }

    public String getEnterprisePayer() {
        return enterprisePayer;
    }

    public void setEnterprisePayer(String enterprisePayer) {
        this.enterprisePayer = enterprisePayer;
    }

    public String getUserPayee() {
        return userPayee;
    }

    public void setUserPayee(String userPayee) {
        this.userPayee = userPayee;
    }

    public String getEnterprisePayee() {
        return enterprisePayee;
    }

    public void setEnterprisePayee(String enterprisePayee) {
        this.enterprisePayee = enterprisePayee;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getIsTip() {
        return isTip;
    }

    public void setIsTip(String isTip) {
        this.isTip = isTip;
    }

    public String getIsAccept() {
        return isAccept;
    }

    public void setIsAccept(String isAccept) {
        this.isAccept = isAccept;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "tid=" + tid +
                ", userPayer='" + userPayer + '\'' +
                ", enterprisePayer='" + enterprisePayer + '\'' +
                ", userPayee='" + userPayee + '\'' +
                ", enterprisePayee='" + enterprisePayee + '\'' +
                ", date='" + date + '\'' +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", isTip='" + isTip + '\'' +
                ", isAccept='" + isAccept + '\'' +
                '}';
    }
}
