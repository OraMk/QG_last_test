package com.pojo;

public class User {

    private int uid;
    //系统分配id
    private String userName;
    //用户名唯一
    private String name;
    //用户昵称
    private String passWord;
    //密码
    private String avatar;
    //用户头像url
    private String pNumber;
//    用户手机号码
    private long fund;
//    用户资金
    private String isAdministrator;
    //用户是否为网站管理员
    private String banned;
    //用户是否被封禁


    public User(int uid, String userName, String name, String passWord, String avatar, String pNumber, long fund, String isAdministrator, String banned) {
        this.uid = uid;
        this.userName = userName;
        this.name = name;
        this.passWord = passWord;
        this.avatar = avatar;
        this.pNumber = pNumber;
        this.fund = fund;
        this.isAdministrator = isAdministrator;
        this.banned = banned;
    }
    public User() {
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getpNumber() {
        return pNumber;
    }

    public void setpNumber(String pNumber) {
        this.pNumber = pNumber;
    }

    public long getFund() {
        return fund;
    }

    public void setFund(long fund) {
        this.fund = fund;
    }

    public String getIsAdministrator() {
        return isAdministrator;
    }

    public void setIsAdministrator(String isAdministrator) {
        this.isAdministrator = isAdministrator;
    }

    public String getBanned() {
        return banned;
    }

    public void setBanned(String banned) {
        this.banned = banned;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", userName='" + userName + '\'' +
                ", passWord='" + passWord + '\'' +
                ", avatar='" + avatar + '\'' +
                ", pNumber='" + pNumber + '\'' +
                ", fund=" + fund +
                ", isAdministrator='" + isAdministrator + '\'' +
                ", banned='" + banned + '\'' +
                '}';
    }
}
