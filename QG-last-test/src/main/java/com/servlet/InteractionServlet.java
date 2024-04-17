package com.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface InteractionServlet {
    //添加申请
    public void applyToJoin(HttpServletRequest req, HttpServletResponse resp);

    //判断是否属于该企业
    public void judgmentJoin(HttpServletRequest req, HttpServletResponse resp);
    //退出企业
    public void deleteRelation(HttpServletRequest req, HttpServletResponse resp);
    //申请成为负责人
    public void applyToLeader(HttpServletRequest req, HttpServletResponse resp);
    //判断是否未企业负责人
    public void judgmentLeader(HttpServletRequest req, HttpServletResponse resp);

    //展示申请表
    public void displayApplication(HttpServletRequest req, HttpServletResponse resp);
    //展示已经审理的申请表
    public void displayHistoryApplication(HttpServletRequest req, HttpServletResponse resp);
    //同意申请
    public void agreeApplication(HttpServletRequest req, HttpServletResponse resp);

    //同意加入企业
    public void agreeJoinEnterprise(HttpServletRequest req, HttpServletResponse resp);

    //同意成为企业负责人
    public void updateEnterpriseLeader(HttpServletRequest req, HttpServletResponse resp);

    //注销企业
    public void deregisterEnterprise(HttpServletRequest req, HttpServletResponse resp);
}
