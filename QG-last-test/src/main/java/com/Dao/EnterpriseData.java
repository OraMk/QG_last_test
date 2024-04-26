package com.Dao;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.ResultSet;
import java.sql.SQLException;

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
    public ResultSet selectRelationById(long rid);

    //根据企业id查找企业
    public ResultSet selectEnterpriseByEid(int eid) throws SQLException;
    //更改分配资金根据关系id
    public int  updateAllocateFunds(long rid, double fund);
    //增添企业
    int addEnterprise(String ename, String number, String size, String direction, String publicMode, String introduce);


    //添加企业并成为企业负责人
    public int addEnterpriseLeader(String eid,String username);
    //根据企业名字查询企业
    public ResultSet selectEnterpriseByEnterpriseName(String ename);

    public ResultSet selectRelationByUsernameAndEid(int eid, String username);

    //设置事务
    public void setAffairs() throws SQLException;

    //提交事务
    public void commit() throws SQLException;
    //查询企业总分配资金
    public ResultSet selectSumAllocationFundsByEid(int eid);

    //回滚事务
    public void rollback() throws SQLException;

    public int setBlockEnterprise(int eid, String status);

    int addEnterpriseApplication(String applicant, String ename, String number, String size, String direction, String publicMode, String introduction);
    //根据申请人用户名查询用户申请企业订单
    public ResultSet selectEnterpriseApplicationByUser(String applicant);

    public ResultSet selectEnterpriseApplicationByEname(String ename);

    public ResultSet selectAllEnterpriseApplication();

    public ResultSet selectEnterpriseApplicationById(int id);

    public int updateEnterpriseApplication(int id, String status, String username);

    public ResultSet selectAllFund();

    public ResultSet selectAllEnterprise();


    public ResultSet selectAllEnterpriseByEnterpriseName(String ename);

    public int updatetransfer(String preEnterpriseName,String ename);

    public int updateBlockingApplicationForEname(String preEnterpriseName, String ename);

    public int updateEnterpriseApplicationForEname(String preEnterpriseName, String ename);
    //增添上传文件的信息
    public int addFileUpload(String username, String enterprise, String fund, String s) throws SQLException;
}
