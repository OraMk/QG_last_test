package com.pojo;

/**
 * @author 86178
 */
public class Application {
    private int aId;
    private String username;
    private int eid;
    private String isAccept;

    public Application() {
    }

    public Application(int aId, String username, int eid, String isAccept) {
        this.aId = aId;
        this.username = username;
        this.eid = eid;
        this.isAccept = isAccept;
    }

    public int getaId() {
        return aId;
    }

    public void setaId(int aId) {
        this.aId = aId;
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

    public String getIsAccept() {
        return isAccept;
    }

    public void setIsAccept(String isAccept) {
        this.isAccept = isAccept;
    }
}
