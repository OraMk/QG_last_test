package com.pojo;

/**
 * @author 86178
 */
public class Application {
    private long aId;
    private String username;
    private int eid;
    private String isAccept;
    private String description;

    public Application() {
    }

    public Application(long aId, String username, int eid, String isAccept, String description) {
        this.aId = aId;
        this.username = username;
        this.eid = eid;
        this.isAccept = isAccept;
        this.description = description;
    }

    public long getaId() {
        return aId;
    }

    public void setaId(long aId) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Application{" +
                "aId=" + aId +
                ", username='" + username + '\'' +
                ", eid=" + eid +
                ", isAccept='" + isAccept + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
