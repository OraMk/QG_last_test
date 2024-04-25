package com.Dao;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface UserData {
    //登录时候查找
    public ResultSet selectUser(String username,String password);

    //注册用户
    public int add(String username,String name,String password,String pNumber);
    //对用户密码进行加密
    public String encryptProcess(String password);

    public ResultSet selectUsername(HttpServletRequest req, HttpServletResponse resp);

    public ResultSet selectUserByName(HttpServletRequest req, HttpServletResponse resp);

    //更改普通信息
    public int changeInformationSimple(String formerly, String username, String password, String pNumber, String name) throws SQLException;
    ;
    //更改头像
    public int changeAvatar(HttpServletRequest req, HttpServletResponse resp);
    public void close();

    //查找全部的用户
    public ResultSet selectAllUserIsNoAdministrator();

    public int setBlockUser(String uid, String status);
    //通过传入的用户名查找用户信息
    public ResultSet selectUserByUsername(String username);
    //查找所有用户总资金
    public ResultSet selectAllFund();

    //设置事务
    public void setAffair() throws SQLException;
    //提交事务
    public void commit() throws SQLException;
    //回滚事务
    public void rollback() throws SQLException;
    //初始化用户的支付密码
    public int initialPayment(String username);

    public ResultSet selectUserByUsernameAndPhoneNumber(String username, String phoneNumber) throws SQLException;

    public int changePassword(String username, String password);
}
