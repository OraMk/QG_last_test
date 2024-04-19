package com.Dao.impl;

import com.Dao.TransferData;
import com.untils.JDBC;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class TransferDataImpl implements TransferData {


    MessageDigest md ;
    ResultSet resultSet = null;
    JDBC jdbc = new JDBC();

    public TransferDataImpl() throws SQLException, IOException, ClassNotFoundException {
    }

    @Override
    public String encryptProcess(String password) {
        try {
            //指定使用SHA-256哈希算法
            md = MessageDigest.getInstance("SHA-256");

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        //将密码转化为字符数组
        byte[] bytePassword = password.getBytes();
        //进行哈希处理
        byte[] hashBytePassword = md.digest(bytePassword);

        // 将字节数组转换为十六进制字符串表示
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytePassword) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        //返回哈希处理后的字符数组
        return hexString.toString();

    }

    @Override
    public String selectPaymentPassword(String username) {
        String sql = "select * from payment_information where username = ‘" + username + "'";
        resultSet = jdbc.Select(sql);
        try {
            if (resultSet.next()){
                return resultSet.getString("payment_password");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
