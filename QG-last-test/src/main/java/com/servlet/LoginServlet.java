package com.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

public interface LoginServlet {
    //    查找是否存在该用户
    public void selectUser(HttpServletRequest req, HttpServletResponse resp);

    //    添加用户数据
    public void  add(HttpServletRequest req, HttpServletResponse resp) throws SQLException;
    //查找用户名字是否存在
    public void selectUsername(HttpServletRequest req, HttpServletResponse resp);
//    判断是否为网站管理员
    public void judgment(HttpServletRequest req, HttpServletResponse resp);

    //查找用户信息通过用户名
    public void selectByUsername(HttpServletRequest req, HttpServletResponse resp);

    //更换普通信息
    public void changeInformationSimple(HttpServletRequest req, HttpServletResponse resp) throws SQLException;

    //更改头像
    public void avatarChange(HttpServletRequest req, HttpServletResponse resp) throws SQLException;

    //判断用户是否被封禁
    public void judgementBanned(HttpServletRequest req, HttpServletResponse resp);

    //查找全部用户数据
    public void selectAllUserIsNoAdministrator(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, ClassNotFoundException;

    //通过传入的用户名查找用户
    public void selectUserByUsername(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException;

    //判断指定用户是否被封禁
    public void judgementBannedForUser(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException;

    //根据手机号和用户名查询用户
    public void selectUserByPhoneAndUsername(HttpServletRequest req, HttpServletResponse resp) throws SQLException;

    //更改用户密码
    public void changePassword(HttpServletRequest req, HttpServletResponse resp);
    //生产验证码
    public void verificationCode(HttpServletRequest req, HttpServletResponse resp) throws IOException;
    //核对验证码
    public void checkCode(HttpServletRequest req, HttpServletResponse resp);
    //查找手机号码是否存在
    public void selectPhoneNumber(HttpServletRequest req, HttpServletResponse resp) throws SQLException;
}

