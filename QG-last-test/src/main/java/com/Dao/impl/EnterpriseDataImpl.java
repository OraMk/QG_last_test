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

    @Override
    public int changeInformationSimple(HttpServletRequest req, HttpServletResponse resp) {
        Cookie[] cookies = req.getCookies();
        String eid = null;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("eid".equals(c.getName())) {
                    eid = c.getValue();

                }
            }
        }
        String ename = req.getParameter("ename");
        String number = req.getParameter("number");
        String size = req.getParameter("size");
        String direction = req.getParameter("direction");
        String public_mode = req.getParameter("public_mode");
        String introduce = req.getParameter("introduce");

        int n = 0;
        if (ename != null || number != null || size != null || direction != null || public_mode != null || introduce != null){
            StringBuffer sql = new StringBuffer("update enterprise set eid = '" + eid +"'");
            if (!(ename == null || ename == "")){
                sql.append(", ename= '" + ename + "'");
            }
            if (!(number ==null || number =="" ))
            {
                sql.append(", number= '"+ number + "'");
            }
            if (!(size == null || size =="")){
                sql.append(", size= '"+ size + "'");
            }
            if (!(direction == null || direction == "")){
                sql.append(", direction = '"+ direction +"'");
            }
            if (!(public_mode == null || public_mode == "")){
                sql.append(", public_mode = '"+ public_mode +"'");
            }if (!(introduce == null || introduce == "")){
                sql.append(", introduce = '"+ introduce +"'");
            }
            sql.append("where eid = '" + eid +"'");
            n = jdbc.Edit(String.valueOf(sql));
        }
        return n;
    }


}
