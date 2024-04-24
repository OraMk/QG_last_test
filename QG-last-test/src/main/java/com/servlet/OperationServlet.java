package com.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    public void changeEnterpriseInformation(HttpServletRequest req, HttpServletResponse resp) throws SQLException;
    //根据企业名称查找企业
    public void selectByEnterpriseName(HttpServletRequest req, HttpServletResponse resp);

    //根据名称查找所属企业
    public void selectByUsername(HttpServletRequest req, HttpServletResponse resp);


    //判断企业名是否存在
    public void judgementEnterpriseName(HttpServletRequest req, HttpServletResponse resp);

    //判断企业是否被封禁
    public void judgmentEnterpriseBanned(HttpServletRequest req, HttpServletResponse resp) throws SQLException;

    //创建企业申请
    public void createEnterpriseApplication(HttpServletRequest req, HttpServletResponse resp) throws SQLException;

    //查看所有企业申请
    public void selectAllEnterpriseApplication(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException;
    //同意企业申请
    public void agreeEnterpriseApplication(HttpServletRequest req, HttpServletResponse resp) throws SQLException;
    //拒绝企业申请
    public void refuseEnterpriseApplication(HttpServletRequest req, HttpServletResponse resp) throws SQLException;

    //查看用户自己的申请
    public void selectEnterpriseApplicationByUser(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException;

    //查看站内所有资金
    public  void  selectAllFundInWebsite(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException;

    //查看所有企业包括公开不公开的
    public void selectAllForAdministrator(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException;

    //为网络管理员查找企业
    public void selectByEnterpriseNameForAdministrator(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException;

}
