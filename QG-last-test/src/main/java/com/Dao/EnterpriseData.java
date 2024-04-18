package com.Dao;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.ResultSet;

public interface EnterpriseData {
    //查找所有公开企业
    public ResultSet selectAllInPublic(HttpServletRequest req, HttpServletResponse resp);
    //查找企业信息
    public ResultSet displayIntroductionById(HttpServletRequest req, HttpServletResponse resp);
    //更改企业信息
    public int changeInformationSimple(HttpServletRequest req, HttpServletResponse resp);
    //加入企业
    public int joinEnterprise(HttpServletRequest req, HttpServletResponse resp);
    //成为负责人
    public int joinLeader(HttpServletRequest req, HttpServletResponse resp);
    //注销企业
    public int deleteEnterprise(HttpServletRequest req, HttpServletResponse resp);
    //通过企业名称查找企业
    public ResultSet selectByEnterpriseName(HttpServletRequest req, HttpServletResponse resp);
    //查找该用户所属的企业
    public ResultSet selectAllByUsername(HttpServletRequest req, HttpServletResponse resp);
    //邀请用户加入企业
    public int inviteUsername(HttpServletRequest req, HttpServletResponse resp);

    //判断邀请对象是否为企业成员
    public ResultSet judgmentJoinForInvite(HttpServletRequest req, HttpServletResponse resp);
    //根据企业id查找分配资金
    public ResultSet selectAllocationFundsByEid(int eid);
    //通过关系id查找关系
    public ResultSet selectRelationById(int rid);

    //根据企业id查找企业
    public ResultSet selectEnterpriseByEid(int eid);
    //更改分配资金根据关系id
    public int  updateAllocateFunds(int rid, double fund);
    //增添企业
    public int addEnterprise(HttpServletRequest req, HttpServletResponse resp);

    //添加企业并成为企业负责人
    public int addEnterpriseLeader(String eid,String username);
}
