package com.servlet.impl;

import com.Dao.TransferData;
import com.Dao.impl.TransferDataImpl;
import com.servlet.BaseServlet;
import com.servlet.TransferServlet;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(value = "/transferServlet")
public class TransferServletImpl extends BaseServlet implements TransferServlet {
    TransferData transferData = new TransferDataImpl();

    public TransferServletImpl() throws SQLException, IOException, ClassNotFoundException {
    }

    @Override
    public void judgementTransferPassword(HttpServletRequest req, HttpServletResponse resp) {
        String payment_password = req.getParameter("payment_password");
        //进行哈希算法处理
        String inputPaymentPassword =  transferData.encryptProcess(payment_password);
        //获取用户id
        Cookie[] cookies = req.getCookies();
        //获取cookie
        String username = null;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("username".equals(c.getName())) {
                    username = c.getValue();
                }
            }
        }
        String realPassword =  transferData.selectPaymentPassword(username);
        if (realPassword != null){
            if (realPassword.equals(payment_password)){
                resp.setStatus(HttpServletResponse.SC_OK);

            }else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

            }
        }else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

        }
    }
}