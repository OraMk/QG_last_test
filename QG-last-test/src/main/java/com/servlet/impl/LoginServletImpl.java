package com.servlet.impl;

import com.Dao.UserData;
import com.Dao.impl.UserDataImpl;
import com.alibaba.fastjson.JSONObject;
import com.pojo.User;
import com.servlet.BaseServlet;
import com.servlet.LoginServlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 86178
 */
@WebServlet(value = "/loginServlet")
public class LoginServletImpl extends BaseServlet implements LoginServlet{
    UserData userData = new UserDataImpl();
//    ResultSet ResultSet resultSet = null;
    public LoginServletImpl() throws SQLException, IOException, ClassNotFoundException {
    }

    @Override
    public void selectUser(HttpServletRequest req, HttpServletResponse resp) {

        try {
            if (req.getParameter("username") == null)
            {
                Cookie cookie = new Cookie("username", "");
                resp.addCookie(cookie);
//                if (cookies != null)
//                {
//                    for (Cookie c: cookies)
//                    {
//                        //销毁已存在的cookie保存的用户信息
//                        if ("username".equals(c.getName())){
//                            //更改生命周期销毁cookie
//                            c.setMaxAge(0);
//                            resp.setStatus(HttpServletResponse.SC_OK);
//
//                        }
//                    }
//                }else {
//                    resp.setStatus(HttpServletResponse.SC_OK);
//                }
            }
            else
            {
                ResultSet resultSet = userData.selectUser(req,resp);

                int count = 0;
                if (resultSet.next())
                {
                    count++;
                }
                if (count == 1)
                {
                    //通过设置cookie记录用户信息
                    Cookie cookie = new Cookie("username", req.getParameter("username"));
//                cookie.setMaxAge(3600); // 设置Cookie的过期时间为1小时
                    resp.addCookie(cookie);

                    resp.setStatus(HttpServletResponse.SC_OK);

                }else{

                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        try{
            int n = userData.add(req,resp);
            if (n == 1)
            {

                resp.setStatus(HttpServletResponse.SC_OK);
            }else{

                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }catch (Exception e)
        {

            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            throw new RuntimeException(e);
        }

    }

    @Override
    public void selectUsername(HttpServletRequest req, HttpServletResponse resp) {
        try {
            ResultSet resultSet = userData.selectUsername(req,resp);
            int count = 0;
            if (resultSet.next())
            {
                count++;
            }
            if (count == 1)
            {
                resp.setStatus(HttpServletResponse.SC_OK);
            }else{
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void judgment(HttpServletRequest req, HttpServletResponse resp) {
            ResultSet resultSet = userData.selectUsername(req,resp);
        try {
            if (resultSet.next())
            {
                String isAdministrator = resultSet.getString("is_administrator");
                //是网站管理员
                if ("yes".equals(isAdministrator))
                {
                    resp.setStatus(HttpServletResponse.SC_OK);

                }else{
                    //不是网站管理员
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
            }else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void selectByUsername(HttpServletRequest req, HttpServletResponse resp) {

        ResultSet resultSet = userData.selectUserByName(req,resp);
        Map<String,String> map = new HashMap<String,String>();
        try {
            if (resultSet.next())
            {
                String uid = resultSet.getString("uid");
                String userName = resultSet.getString("username");
                String name = resultSet.getString("name");
                String avatar = resultSet.getString("avatar");
                String pNumber = resultSet.getString("pnumber");
                String fund = String.valueOf(resultSet.getDouble("fund"));
                String isAdministrator = resultSet.getString("is_administrator");
                map.put("uid",uid);
                map.put("username",userName);
                map.put("name",name);
                map.put("avatar",avatar);
                map.put("pnumber",pNumber);
                map.put("fund",fund);
                map.put("is_administrator",isAdministrator);
                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(resp.getWriter(),map);
                }
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void changeInformationSimple(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        String username = req.getParameter("username");
        ResultSet resultSet = userData.selectUserByUsername(username);
        if (resultSet.next()){
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        int n = userData.changeInformationSimple(req,resp);
        if (n == 1)
        {//则更改成功

            resp.setStatus(HttpServletResponse.SC_OK);
        }else{

            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    public void avatarChange(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        int n = userData.changeAvatar(req,resp);
        if (n == 1 )
        {//更改成功

            resp.setStatus(HttpServletResponse.SC_OK);
        }else{

            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    public void judgementBanned(HttpServletRequest req, HttpServletResponse resp) {

        ResultSet resultSet = userData.selectUserByName(req, resp);
        String banned = null;
        try {
            if (resultSet.next()){
                banned = resultSet.getString("u_banned");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if ("yes".equals(banned)){
            //用户被封禁
            resp.setStatus(HttpServletResponse.SC_OK);

        }else {
            //用户没有被封禁
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

        }
    }

    @Override
    public void selectAllUserIsNoAdministrator(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, ClassNotFoundException {
        UserData userData = new UserDataImpl();
        ResultSet resultSet = userData.selectAllUserIsNoAdministrator();
        User user = null;
        List<User> userList = new ArrayList<User>();
        int uid = 0;
        String username = null;
        String pnumber = null;
        String u_banned = null;

        while (resultSet.next()){
            uid = resultSet.getInt("uid");
            username = resultSet.getString("username");
            pnumber = resultSet.getString("pnumber");
            u_banned = resultSet.getString("u_banned");
            user = new User(uid,username,pnumber,u_banned);
            userList.add(user);
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(),userList);
    }

    @Override
    public void selectUserByUsername(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        String username = req.getParameter("username");
        ResultSet resultSet = userData.selectUserByUsername(username);
        Map<String,String> userMap = new HashMap<String,String>();
        String uid = null;
        String pnumber = null;
        String u_banned = null;
        if (resultSet.next()){
            uid = resultSet.getString("uid");
            pnumber = resultSet.getString("pnumber");
            u_banned = resultSet.getString("u_banned");
            userMap.put("uid",uid);
            userMap.put("pnumber",pnumber);
            userMap.put("banned",u_banned);
            resp.setStatus(HttpServletResponse.SC_OK);
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(resp.getWriter(),userMap);
        }else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

        }

    }

    @Override
    public void judgementBannedForUser(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        String username = req.getParameter("username");
        ResultSet resultSet = userData.selectUserByUsername(username);
        String u_banned = null;
        if (resultSet.next()){
            u_banned = resultSet.getString("u_banned");
        }

        if ("yes".equals(u_banned)){
            //被封禁了
            resp.setStatus(HttpServletResponse.SC_OK);

        }else {
            // 设置响应内容类型为JSON
            resp.setContentType("application/json");

        }

    }


}