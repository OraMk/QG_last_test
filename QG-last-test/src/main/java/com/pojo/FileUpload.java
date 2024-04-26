package com.pojo;

public class FileUpload {
    private long id;
    private String username;
    private String eid;
    private double fund;
    private String date;
    private String file;

    public FileUpload(long id, String username, String eid, double fund, String date, String file) {
        this.id = id;
        this.username = username;
        this.eid = eid;
        this.fund = fund;
        this.date = date;
        this.file = file;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public double getFund() {
        return fund;
    }

    public void setFund(double fund) {
        this.fund = fund;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "FileUpload{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", eid='" + eid + '\'' +
                ", fund='" + fund + '\'' +
                ", date='" + date + '\'' +
                ", file='" + file + '\'' +
                '}';
    }
}
