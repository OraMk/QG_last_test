package com.Dao.impl;

import com.Dao.EnterpriseData;
import com.untils.JDBC;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EnterpriseDataImpl implements EnterpriseData {
    JDBC jdbc = new JDBC();
    ResultSet resultSet= null;
    Connection connection=jdbc.getConnection();

    public EnterpriseDataImpl() throws SQLException, IOException, ClassNotFoundException {
    }

    @Override
    public ResultSet selectAll(HttpServletRequest req, HttpServletResponse resp) {
        String sql = "select * from enterprise";
        resultSet = jdbc.Select(sql);
        return resultSet;
    }

    @Override
    public ResultSet displayIntroductionById(HttpServletRequest req, HttpServletResponse resp) {
        String id = null;
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("eid".equals(c.getName())) {
                    id = c.getValue();

                }
            }
        }
        String sql = "select * from enterprise where eid = " + id;
        resultSet = jdbc.Select(sql);
        return  resultSet;
    }


}
