package com.Dao.impl;

import com.Dao.TransferData;
import com.untils.JDBC;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransferDataImpl implements TransferData {


    MessageDigest md ;
    ResultSet resultSet = null;
    JDBC jdbc = new JDBC();
    Connection connection = jdbc.getConnection();

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
        String sql = "select * from payment_information where username = '" + username + "'";
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

    @Override
    public int addTransfer(String userPayer, String enterprisePayer, String userPayee, String enterprisePayee, double amount,String description) {
        String sql = "insert into transfer(user_payer,enterprise_payer,user_payee,enterprise_payee,amount,description,date) values(?,?,?,?,?,?,NOW())";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,userPayer);
            preparedStatement.setString(2,enterprisePayer);
            preparedStatement.setString(3,userPayee);
            preparedStatement.setString(4,enterprisePayee);
            preparedStatement.setDouble(5,amount);
            preparedStatement.setString(6,description);
            if (enterprisePayer == null || enterprisePayer.equals("")){
                preparedStatement.setNull(2,java.sql.Types.NULL);
                if (enterprisePayee == null || enterprisePayee == ""){
                    preparedStatement.setNull(4,java.sql.Types.NULL);
                }
            }else {
                if (userPayee == null || userPayee.equals("")){
                    preparedStatement.setNull(3,java.sql.Types.NULL);
                }
            }
            if (description == null || "".equals(description)){
                preparedStatement.setNull(6,java.sql.Types.NULL);

            }
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public int reduceFunds(String userPayer, String enterprisePayer, double amount) {
        String sql = null;
        if (enterprisePayer == null || "".equals(enterprisePayer)){
             sql = "update user set fund = fund- " + amount  +" where username = '" + userPayer +"'";
        }else {
            //查询企业id
             sql = "select * from enterprise where ename = '" + enterprisePayer + "'";
            int eid = 0;
            resultSet = jdbc.Select(sql);
            try {
                if (resultSet.next()){
                    eid = resultSet.getInt("eid");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            sql = "update relation set Allocation_funds = allocation_funds - "+amount+" where eid = " + eid + " and username = '" +userPayer + "'";
        }
        return jdbc.Edit(sql);
    }

    @Override
    public int reduceEnterpriseFunds(String enterprisePayer, double amount) {
        String sql = "update enterprise set Total_fund = Total_fund - " + amount + " where ename = '" + enterprisePayer + "'";
        return jdbc.Edit(sql);
    }

    @Override
    public ResultSet selectTransferInNOTipByUsername(String username) {
        String sql = "select * from transfer  where is_tip = 'no' and  user_payer = '" + username + "' order by date desc";
        return jdbc.Select(sql);
    }

    @Override
    public int setTransferTipByTid(String tid) {
        String sql = "update transfer set is_tip = 'yes' where tid = " + tid;
        return jdbc.Edit(sql);
    }

    @Override
    public int editTransferStatusByTid(String tid) {
        String sql = "update transfer set is_tip = 'no' , is_accept = 'pending' where tid = "+ tid;
        return jdbc.Edit(sql);
    }

    @Override
    public ResultSet selectAllTransfer(String username) {
        String sql = "select * from transfer  where user_payer = '" + username + "' order by date desc";
        return jdbc.Select(sql);
    }

    @Override
    public ResultSet selectTransferByTid(String tid) {
        String sql = "select * from transfer where tid = " +tid;
        return jdbc.Select(sql);
    }
}
