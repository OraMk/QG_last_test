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
    public void selectByUsername(HttpServletRequest req, HttpServletResponse resp);


}

