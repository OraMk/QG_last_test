package com.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface LoginServlet {
    //    查找是否存在该用户
    public void selectUser(HttpServletRequest req, HttpServletResponse resp);

    //    添加用户数据
    public void  add(HttpServletRequest req, HttpServletResponse resp);
    //查找用户名字是否存在
    public void selectUsername(HttpServletRequest req, HttpServletResponse resp);
//    判断是否为网站管理员
    public void judgment(HttpServletRequest req, HttpServletResponse resp);

    //查找用户信息通过用户名
    public void selectByUsername(HttpServletRequest req, HttpServletResponse resp);

    //更换普通信息
    public void changeInformationSimple(HttpServletRequest req, HttpServletResponse resp);

    //更改头像
    public void avatarChange(HttpServletRequest req, HttpServletResponse resp);

    //判断用户是否被封禁
    public void judgementBanned(HttpServletRequest req, HttpServletResponse resp);

}

