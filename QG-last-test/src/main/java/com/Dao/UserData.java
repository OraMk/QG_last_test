package com.Dao;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface UserData {
    //登录时候查找
    public ResultSet selectUser(HttpServletRequest req, HttpServletResponse resp);

    //注册用户
    public int add(HttpServletRequest req, HttpServletResponse resp);

    public ResultSet selectUsername(HttpServletRequest req, HttpServletResponse resp);

    public ResultSet selectUserByName(HttpServletRequest req, HttpServletResponse resp);

    //更改普通信息
    public int changeInformationSimple(HttpServletRequest req, HttpServletResponse resp);
    //更改头像
    public int changeAvatar(HttpServletRequest req, HttpServletResponse resp);
    public void close();

    //查找全部的用户
    public ResultSet selectAllUser();

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
}
