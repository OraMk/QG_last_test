package com.pojo;

public class EnterpriseApplication {
    private long id;
    private String applicant;
    private String ename;
    private long number;
    private String direction;
    private String size;
    private String publicMode;
    private String introduce;
    private String isAccept;
    private String processor;

    public EnterpriseApplication(long id, String applicant, String ename, long number, String direction, String size, String publicMode, String introduce, String isAccept, String processor) {
        this.id = id;
        this.applicant = applicant;
        this.ename = ename;
        this.number = number;
        this.direction = direction;
        this.size = size;
        this.publicMode = publicMode;
        this.introduce = introduce;
        this.isAccept = isAccept;
        this.processor = processor;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPublicMode() {
        return publicMode;
    }

    public void setPublicMode(String publicMode) {
        this.publicMode = publicMode;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getIsAccept() {
        return isAccept;
    }

    public void setIsAccept(String isAccept) {
        this.isAccept = isAccept;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    @Override
    public String toString() {
        return "EnterpriseApplication{" +
                "id=" + id +
                ", applicant='" + applicant + '\'' +
                ", ename='" + ename + '\'' +
                ", number=" + number +
                ", direction='" + direction + '\'' +
                ", size='" + size + '\'' +
                ", publicMode='" + publicMode + '\'' +
                ", introduce='" + introduce + '\'' +
                ", isAccept='" + isAccept + '\'' +
                ", processor='" + processor + '\'' +
                '}';
    }
}
