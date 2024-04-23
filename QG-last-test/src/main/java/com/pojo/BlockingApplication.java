package com.pojo;

public class BlockingApplication {
    private long id;
    private String username;
    private String enterprise;
    private String isAccept;
    private String processor;

    public BlockingApplication() {
    }

    public BlockingApplication(long id, String username, String enterprise, String isAccept, String processor) {
        this.id = id;
        this.username = username;
        this.enterprise = enterprise;
        this.isAccept = isAccept;
        this.processor = processor;
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

    public String getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(String enterprise) {
        this.enterprise = enterprise;
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
        return "BlockingApplication{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", enterprise='" + enterprise + '\'' +
                ", isAccept='" + isAccept + '\'' +
                ", processor='" + processor + '\'' +
                '}';
    }
}
