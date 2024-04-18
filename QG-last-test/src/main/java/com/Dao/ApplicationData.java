package com.Dao;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ApplicationData {
    //添加加入企业的申请
    public int addApplicationJoin(HttpServletRequest req, HttpServletResponse resp);
    //判断是否加入企业
    public ResultSet judgmentJoin(HttpServletRequest req, HttpServletResponse resp);
    //判断是否已经申请过加入企业
    public boolean judgmentApplyJoin(HttpServletRequest req, HttpServletResponse resp) throws SQLException;
    //退出企业
    public int deleteRelation(HttpServletRequest req, HttpServletResponse resp);
    //添加成为企业负责人的申请
    public int addApplicationLeader(HttpServletRequest req, HttpServletResponse resp);
    //判断是否已经申请过成为企业负责人
    public boolean judgmentApplyLeader(HttpServletRequest req, HttpServletResponse resp) throws SQLException;

    //查询属于该企业的请求
    public ResultSet displayApplication(HttpServletRequest req, HttpServletResponse resp);
    //通过请求id查找申请
    public ResultSet selectApplicationById(HttpServletRequest req, HttpServletResponse resp);
    //同意请求
    public int agreeApplication(HttpServletRequest req, HttpServletResponse resp);
    //拒绝请求
    public int refuseApplication(HttpServletRequest req, HttpServletResponse resp);
    //展示已经受理过的请求
    public ResultSet displayHistoryApplication(HttpServletRequest req, HttpServletResponse resp);

    public ResultSet selectUnblockingApplicationByUsername(String username);

    public int applyUnblockingForUsername(String username);
}
