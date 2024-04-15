package com.servlet.impl;

import com.Dao.UserData;
import com.Dao.impl.UserDataImpl;
import com.servlet.BaseServlet;
import com.servlet.LoginServlet;
import com.servlet.OperationServlet;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 86178
 */
@WebServlet(value = "/operationServlet")
public class OperationServletImpl extends BaseServlet implements OperationServlet {
    UserData userData = new UserDataImpl();
    ResultSet resultSet = null;

    public OperationServletImpl() throws SQLException, IOException, ClassNotFoundException {
    }

    @Override
    public void loginUser(HttpServletRequest req, HttpServletResponse resp) {


        try {
            Cookie[] cookies = req.getCookies();
            if (cookies != null)
            {
                for (Cookie c: cookies)
                {
                    if ("username".equals(c.getName())){
                        String username = c.getValue();
                        if ("".equals(c.getValue())){
                            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        }
                        else {
                            resp.setStatus(HttpServletResponse.SC_OK);
                            resp.getWriter().write(username);
                        }

                    }
                }
            }else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}