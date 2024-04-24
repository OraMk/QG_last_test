package com.Dao.impl;

import com.Dao.UserData;
import com.pojo.User;
import com.untils.JDBC;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
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
    public ResultSet selectUser(HttpServletRequest req, HttpServletResponse resp) {

        String username = req.getParameter("username");
        String password = req.getParameter("password");
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
    public int add(HttpServletRequest req, HttpServletResponse resp) {
        String username =  req.getParameter("username");
        String name = req.getParameter("name");
        String password =  req.getParameter("password");
        String avatar = req.getParameter("avatar");
        //设置默认头像
        avatar = "https://img.zcool.cn/community/01a3865ab91314a8012062e3c38ff6.png@1280w_1l_2o_100sh.png" ;
        String pNumber = req.getParameter("phone_number");
        int fund = 0;
        String isAdminstaraor = "no";
        String banned = "no";
        Connection connection=jdbc.getConnection();
        String sql = "insert into user(username,name,password,avatar,pnumber,fund,is_administrator,u_banned) " +
                "values('" +username + "','" + name +  "','" + password + "','" +avatar+ "','"+
                pNumber + "',"+ fund + ",'" +isAdminstaraor + "','" +banned+ "')";
        return jdbc.Edit(sql);

    }

    @Override
    public ResultSet selectUsername(HttpServletRequest req, HttpServletResponse resp) {
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
    public int changeInformationSimple(HttpServletRequest req, HttpServletResponse resp) {
        Cookie[] cookies = req.getCookies();
        String formerly = null;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("username".equals(c.getName())) {
                    formerly = c.getValue();

                }
            }
        }
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String pNumber = req.getParameter("phone_number");
        String name = req.getParameter("name");

        int n = 0;
        if (username != null || password != null || pNumber != null || name != null){
            StringBuffer sql = new StringBuffer("update user set username = '" + formerly +"'");
            if (!(username == null || username == "")){
                sql.append(", username= '" + username + "'");
            }
            if (!(password ==null || password =="" ))
            {
                sql.append(", password= '"+ password + "'");
            }
            if (!(name == null || name =="")){
                sql.append(", name= '"+ name + "'");
            }
            if (!(pNumber == null || pNumber == "")){
                sql.append(", pnumber = '"+ pNumber +"'");
            }
            sql.append("where username = '" + formerly +"'");
            n = jdbc.Edit(String.valueOf(sql));
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
    public ResultSet selectAllUser() {
        String sql = "select * from user";
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
}
