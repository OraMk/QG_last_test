package com.servlet.impl;

import com.Dao.TransferData;
import com.Dao.impl.TransferDataImpl;
import com.pojo.Transfer;
import com.servlet.BaseServlet;
import com.servlet.TransferServlet;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(value = "/transferServlet")
public class TransferServletImpl extends BaseServlet implements TransferServlet {
    TransferData transferData = new TransferDataImpl();
    ResultSet resultSet = null;
    Transfer transfer = null;
    List<Transfer> transferList = null;

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
        String description = req.getParameter("description");
        String enterprise_payer = req.getParameter("enterprise_payer");
        String user_payee = req.getParameter("user_payee");
        String enterprise_payee = req.getParameter("enterprise_payee");
        double amount = Double.parseDouble(req.getParameter("fund"));
        //将转账申请添加到数据库中
        int n = transferData.addTransfer(user_payer,enterprise_payer,user_payee,enterprise_payee,amount,description);
        if (n == 1 ){

            if (!(enterprise_payer == null || "".equals(enterprise_payer))){
                //减少企业总资金
                transferData.reduceEnterpriseFunds(enterprise_payer,amount);
            }else {
                //将用户的资金减少相应的金额
                transferData.reduceFunds(user_payer,enterprise_payer,amount);
            }
            resp.setStatus(HttpServletResponse.SC_OK);

        }else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

        }
    }

    @Override
    public void selectTransferInNoTip(HttpServletRequest req, HttpServletResponse resp) {
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
        long tid = 0;
        String userPayer = null;
        String enterprisePayer = null;
        String userPayee = null;
        String enterprisePayee= null;
        String date = null;
        double amount = 0;
        String description = null;
        String isTip = null;
        String isAccept = null;
        resultSet = transferData.selectTransferInNOTipByUsername(username);
        try {
            transferList = new ArrayList<Transfer>();
            while (resultSet.next()){
                tid = resultSet.getLong("tid");
                userPayer = resultSet.getString("user_payer");
                enterprisePayer = resultSet.getString("enterprise_payer");
                userPayee = resultSet.getString("user_payee");
                enterprisePayee = resultSet.getString("enterprise_payee");
                date = resultSet.getString("Date");
                amount = resultSet.getDouble("amount");
                description = resultSet.getString("description");
                isTip = resultSet.getString("is_tip");
                isAccept = resultSet.getString("is_accept");
                transfer = new Transfer(tid,userPayer,enterprisePayer,userPayee,enterprisePayee,date,amount,description,isTip,isAccept);
                transferList.add(transfer);
            }
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(resp.getWriter(),transferList);
        } catch (SQLException e) {

        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonGenerationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setTransferTipByTid(HttpServletRequest req, HttpServletResponse resp) {
        String tid = req.getParameter("tid");
        int n = transferData.setTransferTipByTid(tid);
        if (n == 1){
            resp.setStatus(HttpServletResponse.SC_OK);
        }else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    public void editTransferStatus(HttpServletRequest req, HttpServletResponse resp) {
        String tid = req.getParameter("tid");
        int n = transferData.editTransferStatusByTid(tid);
        if (n == 1){
            resp.setStatus(HttpServletResponse.SC_OK);

        }else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

        }
    }

    @Override
    public void selectAllTransfer(HttpServletRequest req, HttpServletResponse resp) {
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
        long tid = 0;
        String userPayer = null;
        String enterprisePayer = null;
        String userPayee = null;
        String enterprisePayee= null;
        String date = null;
        double amount = 0;
        String description = null;
        String isTip = null;
        String isAccept = null;
        resultSet = transferData.selectAllTransfer(username);
        try {
            transferList = new ArrayList<Transfer>();
            while (resultSet.next()){
                tid = resultSet.getLong("tid");
                userPayer = resultSet.getString("user_payer");
                enterprisePayer = resultSet.getString("enterprise_payer");
                userPayee = resultSet.getString("user_payee");
                enterprisePayee = resultSet.getString("enterprise_payee");
                date = resultSet.getString("Date");
                amount = resultSet.getDouble("amount");
                description = resultSet.getString("description");
                isTip = resultSet.getString("is_tip");
                isAccept = resultSet.getString("is_accept");
                transfer = new Transfer(tid,userPayer,enterprisePayer,userPayee,enterprisePayee,date,amount,description,isTip,isAccept);
                transferList.add(transfer);
            }
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(resp.getWriter(),transferList);
        } catch (SQLException e) {

        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonGenerationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void reduceAmountByTid(HttpServletRequest req, HttpServletResponse resp) {
        String tid = req.getParameter("tid");
        resultSet = transferData.selectTransferByTid(tid);
        String user_payer = null;
        String enterprise_payer = null;

        double amount = 0;
        try {
            if (resultSet.next()){
                user_payer = resultSet.getString("user_payer");
                enterprise_payer = resultSet.getString("enterprise_payer");
                amount = resultSet.getDouble("amount");

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (!(enterprise_payer == null || "".equals(enterprise_payer))){
            //减少企业总资金
            transferData.reduceEnterpriseFunds(enterprise_payer,amount);
        }else {
            //将用户的资金减少相应的金额
            transferData.reduceFunds(user_payer,enterprise_payer,amount);
        }
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}