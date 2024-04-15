package com.Dao;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.ResultSet;

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
}
