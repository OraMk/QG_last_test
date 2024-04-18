package com.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.ResultSet;

public interface OperationServlet {
    //获取登录账户
    public void loginUser(HttpServletRequest req, HttpServletResponse resp);
    //查询所有企业
    public void selectAll(HttpServletRequest req, HttpServletResponse resp);
    //保存企业id
    public void remainEnterpriseId(HttpServletRequest req, HttpServletResponse resp);

    //展示企业详情通过id
    public void displayIntroductionById(HttpServletRequest req, HttpServletResponse resp);
    //更改企业信息
    public void changeEnterpriseInformation(HttpServletRequest req, HttpServletResponse resp);
    //根据企业名称查找企业
    public void selectByEnterpriseName(HttpServletRequest req, HttpServletResponse resp);

    //根据名称查找所属企业
    public void selectByUsername(HttpServletRequest req, HttpServletResponse resp);
}
