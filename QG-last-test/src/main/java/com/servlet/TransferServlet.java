package com.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface TransferServlet {
    //判断支付密码是否正确
    public void judgementTransferPassword(HttpServletRequest req, HttpServletResponse resp);

    //发起转账申请
    public void addTransfer(HttpServletRequest req, HttpServletResponse resp);
}
