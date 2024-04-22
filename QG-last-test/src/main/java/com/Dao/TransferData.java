package com.Dao;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface TransferData {
    //对密码进行加密
    public String encryptProcess(String password);
    //查找该用户对应的支付密码
    public String selectPaymentPassword(String username);
    //增添转账请求
    public int addTransfer(String userPayer, String enterprisePayer, String userPayee, String enterprisePayee, double amount,String description);
    //减少用户/企业分配资金
    public int reduceFunds(String userPayer, String enterprisePayer, double amount);
    //减少企业总资金
    public int reduceEnterpriseFunds(String enterprisePayer, double amount);
    //通过用户名查找未提示的转账申请
    public ResultSet selectTransferInNOTipByUsername(String username);
    //通过申请id设置提示信息未yes
    public int setTransferTipByTid(String tid);
    //更改转账状态（即重新发起待受理的转账信息）
    public int editTransferStatusByTid(String tid);

    //查询所有流水根据用户名
    public ResultSet selectAllTransfer(String username);

    //根据转账单号查询转账单
    public ResultSet selectTransferByTid(String tid);
    //查找尚未处理的收款信息通过用户名
    public ResultSet selectPayoutInPendingByUsername(String username);
    //通过申请id查询转账信息

    //入账
    public int recordedForUser(String username, double amount) throws SQLException;

    //通过转账id设置是否入账成功，并且设置开启提示
    public int setIsAcceptByTid(String tid,String isAccept);

    //退账给用户
    public int chargebacksToUser(String userPayer, double amount);

    //退账给企业
    public int chargebacksToEnterprise(String userPayer, String enterprisePayer, double amount);

    public ResultSet selectPayoutInPendingByEnterprise(String enterprisePayee);

    //设置事务
    public void setAffair() throws SQLException;

    //提交事务
    public void commit() throws SQLException;
    //回滚事务
    public void rollback() throws SQLException;

    int recordedForEnterprise(String enterprisePayee, double amount);
}
