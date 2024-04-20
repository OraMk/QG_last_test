package com.Dao;

import java.sql.ResultSet;

public interface TransferData {
    //对密码进行加密
    public String encryptProcess(String password);

    public String selectPaymentPassword(String username);

    public int addTransfer(String userPayer, String enterprisePayer, String userPayee, String enterprisePayee, double amount,String description);

    public int reduceFunds(String userPayer, String enterprisePayer, double amount);

    public int reduceEnterpriseFunds(String enterprisePayer, double amount);

    public ResultSet selectTransferInNOTipByUsername(String username);

    public int setTransferTipByTid(String tid);

    public int editTransferStatusByTid(String tid);

    //查询所有流水根据用户名
    public ResultSet selectAllTransfer(String username);

    //根据转账单号查询转账单
    public ResultSet selectTransferByTid(String tid);
}
