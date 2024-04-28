package com.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

public interface InteractionServlet {
    //添加申请
    public void applyToJoin(HttpServletRequest req, HttpServletResponse resp) throws SQLException;

    //判断是否属于该企业
    public void judgmentJoin(HttpServletRequest req, HttpServletResponse resp) throws SQLException;
    //退出企业
    public void deleteRelation(HttpServletRequest req, HttpServletResponse resp) throws SQLException;
    //申请成为负责人
    public void applyToLeader(HttpServletRequest req, HttpServletResponse resp) throws SQLException;
    //判断是否未企业负责人
    public void judgmentLeader(HttpServletRequest req, HttpServletResponse resp);

    //展示申请表
    public void displayApplication(HttpServletRequest req, HttpServletResponse resp);
    //展示已经审理的申请表
    public void displayHistoryApplication(HttpServletRequest req, HttpServletResponse resp);
    //同意申请
    public void agreeApplication(HttpServletRequest req, HttpServletResponse resp) throws SQLException;

    //拒绝申请
    public void refuseApplication(HttpServletRequest req, HttpServletResponse resp) throws SQLException;

    //同意加入企业
    public void agreeJoinEnterprise(HttpServletRequest req, HttpServletResponse resp,String username,String eid) throws SQLException;

    //同意成为企业负责人
    public void updateEnterpriseLeader(HttpServletRequest req, HttpServletResponse resp,String username,String eid) throws SQLException;


    //拉用户进入企业
    public void inviteUsername(HttpServletRequest req, HttpServletResponse resp) throws SQLException;

    //判断邀请用户是否为企业成员
    public void judgmentJoinForInvite(HttpServletRequest req, HttpServletResponse resp) throws SQLException;

    //展示分配的资金
    public void displayAllocationFunds(HttpServletRequest req, HttpServletResponse resp) throws SQLException;

    //根据关系id查找关系
    public void selectRelationById(HttpServletRequest req, HttpServletResponse resp);

    //比较资金
    public void compareFunds(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException;

    //实现分配资金功能
    public void toAllocateFunds(HttpServletRequest req, HttpServletResponse resp) throws SQLException;

    //申请用户解封
    public void applyUnblockingByUsername(HttpServletRequest req, HttpServletResponse resp) throws SQLException;

    //查找企业为用户分配的资金
    public void enterpriseAllocateFund(HttpServletRequest req, HttpServletResponse resp);

    //判断企业是否被封禁
    public void judgmentEnterpriseBan(HttpServletRequest req, HttpServletResponse resp) throws SQLException;

    //查看剩余资金
    public void checkRemainingFunds(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException;

    //查看解封申请
    public void selectBlockingApplication(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException;

    //同意解封申请
    public void agreeUnblockingApplication(HttpServletRequest req, HttpServletResponse resp) throws SQLException;

    //拒绝解封申请
    public void refuseUnblockingApplication(HttpServletRequest req, HttpServletResponse resp) throws SQLException;

    //禁用用户
    public void blockUser(HttpServletRequest req, HttpServletResponse resp);
    //封禁企业
    public void blockEnterprise(HttpServletRequest req, HttpServletResponse resp);
    //解封用户
    public void unblockUser(HttpServletRequest req, HttpServletResponse resp);
    //解封企业
    public void unblockEnterprise(HttpServletRequest req, HttpServletResponse resp);

    //申请企业解封
    public void applyUnblockingByEnterprise(HttpServletRequest req, HttpServletResponse resp) throws SQLException;

    //实时查看企业是否被封禁
    public void judgmentEnterpriseBanAlways(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException;
}
