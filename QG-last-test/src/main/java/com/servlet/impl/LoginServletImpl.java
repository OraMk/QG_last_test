package com.servlet.impl;

import com.Dao.UserData;
import com.Dao.impl.UserDataImpl;
import com.servlet.BaseServlet;
import com.servlet.LoginServlet;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 86178
 */
@WebServlet(value = "/loginServlet")
public class LoginServletImpl extends BaseServlet implements LoginServlet {
    UserData userData = new UserDataImpl();
    ResultSet resultSet = null;
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
                resultSet = userData.selectUser(req,resp);

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
    public void add(HttpServletRequest req, HttpServletResponse resp) {
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
            resultSet = userData.selectUsername(req,resp);
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
}