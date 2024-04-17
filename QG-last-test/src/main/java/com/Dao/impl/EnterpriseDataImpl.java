package com.Dao.impl;

import com.Dao.ApplicationData;
import com.Dao.EnterpriseData;
import com.untils.JDBC;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 86178
 */
public class EnterpriseDataImpl implements EnterpriseData {
    JDBC jdbc = new JDBC();
    ResultSet resultSet= null;
    Connection connection=jdbc.getConnection();
    ApplicationData applicationData = new ApplicationDataImpl();

    public EnterpriseDataImpl() throws SQLException, IOException, ClassNotFoundException {
    }

    @Override
    public ResultSet selectAllInPublic(HttpServletRequest req, HttpServletResponse resp) {
        String sql = "select * from enterprise where public_mode = 'public'";
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

    @Override
    public int joinEnterprise(HttpServletRequest req, HttpServletResponse resp) {
        resultSet =  applicationData.selectApplicationById(req,resp);
        String username = null;
        int eid = 0;
        try {
            if (resultSet.next()){
                username = resultSet.getString("username");
                eid = resultSet.getInt("eid");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String sql = "insert relation(username,eid,isleader,Allocation_funds) values('"+username+"',"+eid+",'no',0)";
        return jdbc.Edit(sql);
    }

    @Override
    public int joinLeader(HttpServletRequest req, HttpServletResponse resp) {
        resultSet =  applicationData.selectApplicationById(req,resp);
        String username = null;
        int eid = 0;
        try {
            if (resultSet.next()){
                username = resultSet.getString("username");
                eid = resultSet.getInt("eid");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String sql = "update relation set isLeader = 'yes' where eid = " + eid +" and username = '" + username + "'";
        return jdbc.Edit(sql);
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
            connection.setAutoCommit(false);
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
                    connection.commit();
                }
            }
        } catch (Exception e) {
            try {
                //出现异常回滚事务
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }

        return n;
    }

    @Override
    public ResultSet selectByEnterpriseName(HttpServletRequest req, HttpServletResponse resp) {
        String ename = req.getParameter("ename");
        String sql = "select * from enterprise where public_mode = 'public'";
        if (!(ename == null || "".equals(ename)))
        {//判断用户是否有输入
            sql += "and ename like '%" + ename + "%'";
        }

        return jdbc.Select(sql);
    }

    @Override
    public ResultSet selectAllByUsername(HttpServletRequest req, HttpServletResponse resp) {
        String username = null;
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("username".equals(c.getName())) {
                    username = c.getValue();

                }
            }
        }
        String sql = "select * from relation where username = '" + username + "'";
        resultSet = jdbc.Select(sql);
        int[] eid = new int[100];
        int i = 0;
        try {
            while (resultSet.next()){
                eid[i++] = resultSet.getInt("eid");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (i != 0){
            StringBuffer SQL = new StringBuffer("select * from enterprise where eid in (");
            int j = 0 ;
            SQL.append(eid[j++]);
            for (; j < i ; j++ ){
                SQL.append("," + eid[j]);
            }
            SQL.append(")");
            return jdbc.Select(String.valueOf(SQL));

        }else {
            return  null;
        }
    }


}
