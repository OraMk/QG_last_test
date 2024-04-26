package com.Dao.impl;

import com.Dao.TransferData;
import com.untils.JDBC;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransferDataImpl implements TransferData {


    JDBC jdbc = new JDBC();
    Connection connection = jdbc.getConnection();

    public TransferDataImpl() throws SQLException, IOException, ClassNotFoundException {
    }

    @Override
    public String encryptProcess(String password) {
        MessageDigest md ;
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
        ResultSet resultSet = jdbc.Select(sql);
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
           ResultSet resultSet = jdbc.Select(sql);
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
    public ResultSet selectAllTransferByUser(String username) {
        String sql = "select * from transfer  where user_payer = '" + username + "' or user_payee = '"+username+"' order by date desc";
        return jdbc.Select(sql);
    }

    @Override
    public ResultSet selectAllTransferByEnterprise(String enterprise) {
        String sql = "select * from transfer where enterprise_payer = '" +enterprise+ "' or enterprise_payee = '" +enterprise+ "' order by date desc";
        return jdbc.Select(sql);

    }

    @Override
    public ResultSet selectTransferByTid(String tid) {
        String sql = "select * from transfer where tid = " +tid  + " for update";
        return jdbc.Select(sql);
    }

    @Override
    public ResultSet selectPayoutInPendingByUsername(String username) {
        String sql = "select * from transfer where user_payee = '" + username +"' and is_Accept = 'pending'" ;
        return jdbc.Select(sql);
    }
    @Override
    public int recordedForUser(String username, double amount)  {
        String sql  = null;
        double fund = 0;
        //设置事务
        try {

            //设置悲观锁
             sql = "select * from user where username = '" + username + "' for update ";
            ResultSet resultSet = jdbc.Select(sql);
            fund = 0;
            if (resultSet.next()){
                fund = resultSet.getDouble("fund");
            }
            fund += amount;
            //更新资金
            sql = "update user set fund = " + fund + " where username = '" +username + "' ";
            if (jdbc.Edit(sql) == 1)
            {
                //提交事务

                return 1;
            }else {

                return 0;
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }


    }

    @Override
    public int setIsAcceptByTid(String tid,String isAccept) {
        String sql = "update transfer set is_accept = '"+isAccept+"' , is_tip = 'no' where tid = " + tid;
        return jdbc.Edit(sql);
    }

    @Override
    public int chargebacksToUser(String userPayer, double amount) {
        String sql  = null;
        double fund = 0;
        //设置事务
        try {
            //设置悲观锁
            sql = "select * from user where username = '" + userPayer + "' for update ";
            ResultSet resultSet = jdbc.Select(sql);
            if (resultSet.next()){
                fund = resultSet.getDouble("fund");
            }
            fund += amount;
            //更新资金
            sql = "update user set fund = " + fund + " where username = '" + userPayer + "' ";
            if (jdbc.Edit(sql) == 1)
            {
                //提交事务


                return 1;
            }else {
                //回滚事务


                return 0;
            }
        } catch (SQLException e) {

            throw new RuntimeException(e);
        }


    }

    @Override
    public int chargebacksToEnterprise(String userPayer, String enterprisePayer, double amount) {
        String sql = null;
        double total_fund = 0;
        double allocation_funds = 0;
        int eid = 0;
        try {
            //设置事务

            //设置悲观锁
            sql = "select * from enterprise where ename = '" +enterprisePayer+ "' for update";
            ResultSet resultSet = jdbc.Select(sql);
            if (resultSet.next()){
                //获取企业总资金
                total_fund = resultSet.getDouble("Total_fund");
                eid = resultSet.getInt("eid");
            }
            sql = "select * from relation where username = '" +userPayer+ "' and eid = " + eid + " for update";
            resultSet = jdbc.Select(sql);
            if (resultSet.next()){
                allocation_funds = resultSet.getDouble("Allocation_funds");
            }
            total_fund += amount;
            allocation_funds += amount;
            sql = "update relation set Allocation_funds = " +allocation_funds + " where username = '" + userPayer + "' and eid = " + eid;
            if (jdbc.Edit(sql) == 1){
                sql = "update enterprise set Total_fund = " + total_fund + " where ename = '" + enterprisePayer + "'";
                if (jdbc.Edit(sql) == 1){
                    //当更改同时成功时
                    //提交事务


                    return 1;
                }else {


                    return 0;
                }
            }else {


                return 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResultSet selectPayoutInPendingByEnterprise(String enterprisePayee) {
        String sql = "select * from transfer where is_accept = 'pending' and enterprise_payee = '" + enterprisePayee +"' for update";
        return jdbc.Select(sql);
    }

    @Override
    public void setAffair() throws SQLException {
        connection.setAutoCommit(false);
    }

    @Override
    public void commit() throws SQLException {
        connection.commit();
        connection.setAutoCommit(true);

    }

    @Override
    public void rollback() throws SQLException {
        connection.rollback();
        connection.setAutoCommit(true);
    }

    @Override
    public int recordedForEnterprise(String enterprisePayee, double amount) {
        String sql  = null;
        double fund = 0;
        try {

            //设置悲观锁
            sql = "select * from enterprise where ename = '" + enterprisePayee + "' for update ";
            ResultSet resultSet = jdbc.Select(sql);
            fund = 0;
            if (resultSet.next()){
                fund = resultSet.getDouble("Total_fund");
            }
            fund += amount;
            //更新资金
            sql = "update enterprise set Total_fund = " + fund + " where ename = '" +enterprisePayee + "' ";
            if (jdbc.Edit(sql) == 1)
            {
                //提交事务

                return 1;
            }else {

                return 0;
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }

    }

    @Override
    public int deleteEnterprise(HttpServletRequest req, HttpServletResponse resp) {
        Cookie[] cookies = req.getCookies();
        String eid = null;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("eid".equals(c.getName())) {
                    eid = c.getValue();

                }
            }
        }
        int n = 0;
        //删除企业信息表
        try {
            //设置事务
//            connection.setAutoCommit(false);
            String sql = "delete from enterprise where eid =" + eid;
            n = jdbc.Edit(sql);
            if (n == 1)
            {
                //删除企业员工关系
                sql = "delete from relation where eid =" + eid;
                n = jdbc.Edit(sql);
                if (n != 0){
                    //删除与该企业相关的申请表
                    sql = "delete from application where eid =" + eid;
                    n = jdbc.Edit(sql);
                    if (n==0)n++;
                    //提交事务
//                    connection.commit();
                }
            }
        } catch (Exception e) {
            //出现异常回滚事务
//                connection.rollback();
            throw new RuntimeException(e);
        }

        return n;
    }

    @Override
    public int distributeFundAfterDeregisterEnterprise(String eid) throws SQLException {
        String sql = "select * from relation where eid =  " + eid + " for update";
        ResultSet resultSet = jdbc.Select(sql);
        String username = null;
        double allocation_funds = 0;
        int n = 0;
        while (resultSet.next()){
            username = resultSet.getString("username");
            allocation_funds = resultSet.getDouble("Allocation_funds");
            n += recordedForUser(username,allocation_funds);
        }
        return n;
    }

    @Override
    public ResultSet selectAllTransfer() {
        String sql = "select * from transfer order by date desc";
        return jdbc.Select(sql);
    }
    @Override
    public int rechargeForEnterprises(double fund, String eid) {
        String sql = "update enterprise set Total_fund = Total_fund + " + fund + " where eid = " + eid;
        return jdbc.Edit(sql);
    }

    @Override
    public int reduceUser(double fund, String username) {
        String sql = "update user set fund = fund - " + fund + " where username = '" + username + "'";
        return jdbc.Edit(sql);
    }

    @Override
    public int changePayPassword(String username, String hashPassword) {
        String sql = "update payment_information set payment_password = '" +hashPassword+ "' where username = '" +username+ "'";
        return jdbc.Edit(sql);
    }

    @Override
    public int reduceEnterpriseAllocatedFunds(String userPayer, String enterprisePayer, double amount) throws SQLException {
        String sql = "select eid from enterprise where ename = '" +enterprisePayer+"' for update";
        ResultSet resultSet = jdbc.Select(sql);
        String eid = null;
        if (resultSet.next()){
            eid = resultSet.getString("eid");
        }
        String SQL = "update relation set Allocation_funds = Allocation_funds -" +amount + "where eid =" + eid + " and username = '" +userPayer+ "'";
        return jdbc.Edit(SQL);
    }

    @Override
    public ResultSet selectFileUploadByEid(String eid) {
        String sql = "select * from file_upload where eid = " +eid;
        return jdbc.Select(sql);
    }


}
