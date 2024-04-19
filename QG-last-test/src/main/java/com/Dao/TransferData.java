package com.Dao;

public interface TransferData {
    //对密码进行加密
    public String encryptProcess(String password);

    public String selectPaymentPassword(String username);
}
