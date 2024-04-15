package com.Dao.impl;

import com.Dao.UserData;
import com.pojo.User;
import com.untils.JDBC;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDataImpl implements UserData {
    JDBC jdbc = new JDBC();
    ResultSet resultSet= null;
    public UserDataImpl() throws SQLException, IOException, ClassNotFoundException {
    }

    @Override
    public ResultSet selectUser(HttpServletRequest req, HttpServletResponse resp) {
        Connection connection=jdbc.getConnection();
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
        String pNumber = req.getParameter("pNumber");
        int fund = 0;
        String isAdminstaraor = "no";
        String banned = "no";
        Connection connection=jdbc.getConnection();
        String sql = "insert into user(username,name,password,avatar,pnumber,fund,is_administrator,u_banned) " +
                "values('" +username + "','" + name +  "','" + password + "','" +avatar+ "',"+
                pNumber + ","+ fund + ",'" +isAdminstaraor + "','" +banned+ "')";
        return jdbc.Edit(sql);

    }

    @Override
    public ResultSet selectUsername(HttpServletRequest req, HttpServletResponse resp) {
        Connection connection=jdbc.getConnection();
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
}
