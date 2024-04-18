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

    //拒绝申请
    public void refuseApplication(HttpServletRequest req, HttpServletResponse resp);

    //同意加入企业
    public void agreeJoinEnterprise(HttpServletRequest req, HttpServletResponse resp);

    //同意成为企业负责人
    public void updateEnterpriseLeader(HttpServletRequest req, HttpServletResponse resp);

    //注销企业
    public void deregisterEnterprise(HttpServletRequest req, HttpServletResponse resp);

    //拉用户进入企业
    public void inviteUsername(HttpServletRequest req, HttpServletResponse resp);

    //判断邀请用户是否为企业成员
    public void judgmentJoinForInvite(HttpServletRequest req, HttpServletResponse resp);

    //展示分配的资金
    public void displayAllocationFunds(HttpServletRequest req, HttpServletResponse resp);

    //根据关系id查找关系
    public void selectRelationById(HttpServletRequest req, HttpServletResponse resp);

    //比较资金
    public void compareFunds(HttpServletRequest req, HttpServletResponse resp);

    //实现分配资金功能
    public void toAllocateFunds(HttpServletRequest req, HttpServletResponse resp);

    //申请用户解封
    public void applyUnblockingByUsername(HttpServletRequest req, HttpServletResponse resp);
}
