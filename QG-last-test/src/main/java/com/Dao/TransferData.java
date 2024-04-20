package com.Dao;

public interface TransferData {
    //对密码进行加密
    public String encryptProcess(String password);

    public String selectPaymentPassword(String username);

    public int addTransfer(String userPayer, String enterprisePayer, String userPayee, String enterprisePayee, double amount,String description);

    public int reduceFunds(String userPayer, String enterprisePayer, double amount);

    public int reduceEnterpriseFunds(String enterprisePayer, double amount);
}
