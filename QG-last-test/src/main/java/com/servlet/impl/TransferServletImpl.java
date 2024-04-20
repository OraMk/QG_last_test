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
            if (realPassword.equals(inputPaymentPassword)){
                resp.setStatus(HttpServletResponse.SC_OK);

            }else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

            }
        }else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

        }
    }

    @Override
    public void addTransfer(HttpServletRequest req, HttpServletResponse resp) {
        Cookie[] cookies = req.getCookies();
        //获取cookie
        String user_payer = null;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("username".equals(c.getName())) {
                    user_payer = c.getValue();
                }
            }
        }
        String enterprise_payer = req.getParameter("enterprise_payer");
        String user_payee = req.getParameter("user_payee");
        String enterprise_payee = req.getParameter("enterprise_payee");
        double amount = Double.parseDouble(req.getParameter("fund"));
        //将转账申请添加到数据库中
        int n = transferData.addTransfer(user_payer,enterprise_payer,user_payee,enterprise_payee,amount,"测试代码");
        if (n == 1 ){
            //将用户的资金减少相应的金额
            transferData.reduceFunds(user_payer,enterprise_payer,amount);
            if (!(enterprise_payer == null || "".equals(enterprise_payer))){
                //减少企业总资金
                transferData.reduceEnterpriseFunds(enterprise_payer,amount);
            }
            resp.setStatus(HttpServletResponse.SC_OK);

        }else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

        }
    }
}