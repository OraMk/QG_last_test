package com.Dao.impl;

import com.Dao.UserData;
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

public class UserDataImpl implements UserData {
    JDBC jdbc = new JDBC();
    Connection connection=jdbc.getConnection();
    ResultSet resultSet= null;
    public UserDataImpl() throws SQLException, IOException, ClassNotFoundException {
    }

    @Override
    public ResultSet selectUser(String username,String password) {

        try {
            String sql = "select * from user where username = ? and password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);
            resultSet = preparedStatement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int add(String username,String name,String password,String pNumber) {

        String avatar = "https://img.zcool.cn/community/01a3865ab91314a8012062e3c38ff6.png@1280w_1l_2o_100sh.png" ;
        int fund = 0;
        String isAdminstaraor = "no";
        String banned = "no";
        String sql = "insert into user(username,name,password,avatar,pnumber,fund,is_administrator,u_banned) " +
                "values('" +username + "','" + name +  "','" + password + "','" +avatar+ "','"+
                pNumber + "',"+ fund + ",'" +isAdminstaraor + "','" +banned+ "')";
        return jdbc.Edit(sql);

    }

    @Override
    public String encryptProcess(String password) {
        MessageDigest md ;
        try {
            //指定使用SHA-256哈希算法
            md = MessageDigest.getInstance("SHA-512");

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
    public ResultSet selectUsernameForUser(HttpServletRequest req, HttpServletResponse resp){
        Cookie[] cookies = req.getCookies();
        String username = null;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("username".equals(c.getName())) {
                    username = c.getValue();
                }
            }
        }
        try {
            String sql = "select * from user where username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,username);
            resultSet = preparedStatement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResultSet selectPhoneNumber(String phoneNumber) {
        String sql = "select * from user where pnumber = '"+phoneNumber+"'";
//        String sql = "select * from phoneNumber";
        return jdbc.Select(sql);
    }

    @Override
    public ResultSet selectUsername(HttpServletRequest req, HttpServletResponse resp) {
//        Cookie[] cookies = req.getCookies();
//        String username = null;
//        if (cookies != null) {
//            for (Cookie c : cookies) {
//                if ("username".equals(c.getName())) {
//                    username = c.getValue();
//
//                }
//            }
//        }
        String username = req.getParameter("username");
        try {
            String sql = "select * from user where username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,username);
            resultSet = preparedStatement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResultSet selectUserByName(HttpServletRequest req, HttpServletResponse resp) {
        Cookie[] cookies = req.getCookies();
        String username = null;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("username".equals(c.getName())) {
                    username = c.getValue();

                }
            }
        }
        try {
            String sql = "select * from user where username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int changeInformationSimple(String formerly, String username, String password, String pNumber, String name) throws SQLException {
        int n = 0;
        int pass = 0;
        String hashPassword = null;
        if (username != null || password != null || pNumber != null || name != null){
            StringBuffer sql = new StringBuffer("update user set username = '" + formerly +"'");
            if (!(username == null || username == "")){
                sql.append(", username= '" + username + "'");
            }
            if (!(password ==null || password =="" ))
            {
                pass = 1;
                hashPassword = encryptProcess(password);
                sql.append(", password= '?'");
            }
            if (!(name == null || name =="")){
                sql.append(", name= '"+ name + "'");
            }
            if (!(pNumber == null || pNumber == "")){
                sql.append(", pnumber = '"+ pNumber +"'");
            }
            sql.append("where username = '" + formerly +"'");
            if (pass == 1){
                PreparedStatement preparedStatement = connection.prepareStatement(String.valueOf(sql));
                preparedStatement.setString(1,hashPassword);
                n = preparedStatement.executeUpdate();
            }else {
                n = jdbc.Edit(String.valueOf(sql));

            }

        }
        return n;
    }

    @Override
    public int changeAvatar(HttpServletRequest req, HttpServletResponse resp) {
        Cookie[] cookies = req.getCookies();
        String formerly = null;
        int n = 0;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("username".equals(c.getName())) {
                    formerly = c.getValue();

                }
            }
        }
        String avatar = req.getParameter("avatar");
        String sql = "update user set avatar = '" + avatar +"' where username = '"+ formerly +"'";
        n = jdbc.Edit(sql);
        return  n;
    }

    @Override
    public void close() {
        jdbc.close();
    }

    @Override
    public ResultSet selectAllUserIsNoAdministrator() {
        String sql = "select * from user Where is_administrator != 'yes'";
        return jdbc.Select(sql);
    }

    @Override
    public int setBlockUser(String uid, String status) {
        String sql = "update user set u_banned = '" +status+"' where uid = " + uid;
        return jdbc.Edit(sql);
    }

    @Override
    public ResultSet selectUserByUsername(String username) {
        String sql = "select * from user where username = '" + username + "'";
        return jdbc.Select(sql);
    }

    @Override
    public ResultSet selectAllFund() {
        String sql = "select sum(fund) as userFund from user";
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
    public int initialPayment(String username) {
        String sql = "insert into payment_information(username) values('"+username+"') ";
        return jdbc.Edit(sql);
    }

    @Override
    public ResultSet selectUserByUsernameAndPhoneNumber(String username, String phoneNumber) throws SQLException {
        String sql = "select * from user where username = ? and pnumber = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,username);
        preparedStatement.setString(2,phoneNumber);
        return preparedStatement.executeQuery();
    }

    @Override
    public int changePassword(String username, String password) {
        String sql = "update user set password = '" +password+ "' where username = '" +username+ "'";
        return jdbc.Edit(sql);
    }
}
