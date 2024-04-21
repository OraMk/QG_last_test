package com.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface TransferServlet {
    //判断支付密码是否正确
    public void judgementTransferPassword(HttpServletRequest req, HttpServletResponse resp);

    //发起转账申请
    public void addTransfer(HttpServletRequest req, HttpServletResponse resp);

    //查找尚未提示的转账提示
    public void selectTransferInNoTip(HttpServletRequest req, HttpServletResponse resp);

    //通过tid 将transfer特定的转账订单的is_tip更改为yes
    public void setTransferTipByTid(HttpServletRequest req, HttpServletResponse resp);

    //修改申请的状态(即通过流水重新发送申请给用户)
    public void editTransferStatus(HttpServletRequest req, HttpServletResponse resp);

    //查找流水
    public void selectAllTransfer(HttpServletRequest req, HttpServletResponse resp);

    //重新减少资金
    public void reduceAmountByTid(HttpServletRequest req, HttpServletResponse resp);

    //查看尚未处理的收款信息
    public void selectPayoutInPending(HttpServletRequest req, HttpServletResponse resp);
    //同意入账
    public void agreeTransferByUser(HttpServletRequest req, HttpServletResponse resp);

    //拒绝入账
    public void refuseTransferByUser(HttpServletRequest req, HttpServletResponse resp);

}
