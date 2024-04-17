package com.Dao.impl;

import com.Dao.ApplicationData;
import com.Dao.UserData;
import com.untils.JDBC;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ApplicationDataImpl implements ApplicationData {
    Cookie[] cookies = null;
    JDBC jdbc = new JDBC();
    UserData userData = new UserDataImpl();
    ResultSet resultSet = null;
    Connection connection = jdbc.getConnection();

    public ApplicationDataImpl() throws SQLException, IOException, ClassNotFoundException {
    }

    @Override
    public int addApplicationJoin(HttpServletRequest req, HttpServletResponse resp) {
        Cookie[] cookies = req.getCookies();
        String username = null;
        String eid = null;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("username".equals(c.getName())) {
                    username = c.getValue();

                } else if ("eid".equals(c.getName())) {
                    eid = c.getValue();
                }
            }
        }
        if (username == null || username == "")
        {
            return 0;
        }
        String isAccept = "pending";
        String description = "申请加入企业";
        String sql = "insert into application(username,eid,is_accept,description) values( '"+username +"' , " +eid + " ,'" + isAccept + "', '"+description +"')";
        return jdbc.Edit(sql);
    }

    @Override
    public ResultSet judgmentJoin(HttpServletRequest req, HttpServletResponse resp) {
        String username = null;
        String eid = null;
        cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("eid".equals(c.getName())) {
                    eid = c.getValue();
                }
                if ("username".equals(c.getName())){
                    username = c.getValue();
                }
            }
        }

        String sql = "select * from relation where username ='"+ username +"'  and eid = "+ eid ;
        resultSet =  jdbc.Select(sql);
        return resultSet;
    }

    @Override
    public boolean judgmentApplyJoin(HttpServletRequest req, HttpServletResponse resp) throws SQLException {

        String username = null;
        String eid = null;
        cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("eid".equals(c.getName())) {
                    eid = c.getValue();
                }else if ("username".equals(c.getName()))
                {
                    username = c.getValue();
                }
                    }
                }
        String sql = "select * from application where username ='"+ username +"'  and eid = "+ eid +" and description ='申请加入企业'";
        resultSet = jdbc.Select(sql);
        while (resultSet.next()){
            String isAccept = resultSet.getString("is_accept");
            if ("pending".equals(isAccept))return true;
        }
        return false;
    }

    @Override
    public int deleteRelation(HttpServletRequest req, HttpServletResponse resp) {
        resultSet = userData.selectUserByName(req, resp);
        String username = null;
        String eid = null;

        cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("eid".equals(c.getName())) {
                    eid = c.getValue();
                }
                if ("username".equals(c.getName())){
                    username = c.getValue();
                }
            }
        }

        String sql = "delete from relation where username ="+ username +"  and eid = "+ eid ;
        return jdbc.Edit(sql);

    }

    @Override
    public int addApplicationLeader(HttpServletRequest req, HttpServletResponse resp) {

            Cookie[] cookies = req.getCookies();
            //获取cookie
            String username = null;
            String eid = null;
            if (cookies != null) {
                for (Cookie c : cookies) {
                    if ("username".equals(c.getName())) {
                        username = c.getValue();

                    } else if ("eid".equals(c.getName())) {
                        eid = c.getValue();
                    }
                }
            }
            String isAccept = "pending";
            String description = "申请成为负责人";
            String sql = "insert into application(username,eid,is_accept,description) values( '"+username +"' , " +eid + " ,'" + isAccept + "', '"+description +"')";
            return jdbc.Edit(sql);

    }

    @Override
    public boolean judgmentApplyLeader(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        String username = null;
        String eid = null;
        cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("eid".equals(c.getName())) {
                    eid = c.getValue();
                }else if ("username".equals(c.getName()))
                {
                    username = c.getValue();
                }
            }
        }
        String sql = "select * from application where username ='"+ username +"'  and eid = "+ eid +" and description ='申请成为负责人'";
        resultSet = jdbc.Select(sql);
        while (resultSet.next()){
            String isAccept = resultSet.getString("is_accept");
            if ("pending".equals(isAccept))return true;
        }
        return false;
    }

    @Override
    public ResultSet displayApplication(HttpServletRequest req, HttpServletResponse resp) {
        Cookie[] cookies = req.getCookies();
        //获取cookie
        String eid = null;
        if (cookies != null) {
            for (Cookie c : cookies) {
                 if ("eid".equals(c.getName())) {
                    eid = c.getValue();
                }
            }
        }
        //查询属于该企业的请求
        String sql = "select * from application where is_accept = 'pending' and eid = " + eid;
        return jdbc.Select(sql);
    }

    @Override
    public ResultSet selectApplicationById(HttpServletRequest req, HttpServletResponse resp) {
        long aId = Integer.parseInt(req.getParameter("aid"));
        //通过id查找目标请求
        String sql = "select * from application where aid =" + aId;
        return jdbc.Select(sql);
    }

    @Override
    public int agreeApplication(HttpServletRequest req, HttpServletResponse resp) {
        long aId = Integer.parseInt(req.getParameter("aid"));
        String sql = "update application set is_accept = 'yes' where aid = " + aId;
        return jdbc.Edit(sql);
    }

    @Override
    public int refuseApplication(HttpServletRequest req, HttpServletResponse resp) {
        long aId = Integer.parseInt(req.getParameter("aid"));
        String sql = "update application set is_accept = 'no' where aid = " + aId;
        return jdbc.Edit(sql);
    }

    @Override
    public ResultSet displayHistoryApplication(HttpServletRequest req, HttpServletResponse resp) {
        Cookie[] cookies = req.getCookies();
        //获取cookie
        String eid = null;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("eid".equals(c.getName())) {
                    eid = c.getValue();
                }
            }
        }
        //查询属于该企业的请求
        String sql = "select * from application where is_accept != 'pending' and eid = " + eid;
        return jdbc.Select(sql);
    }
}
