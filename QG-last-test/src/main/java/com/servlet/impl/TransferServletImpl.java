package com.servlet.impl;

import com.Dao.EnterpriseData;
import com.Dao.TransferData;
import com.Dao.impl.EnterpriseDataImpl;
import com.Dao.impl.TransferDataImpl;
import com.pojo.Transfer;
import com.servlet.BaseServlet;
import com.servlet.TransferServlet;
import com.untils.JDBC;
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
    EnterpriseData enterpriseData = new EnterpriseDataImpl();
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
    public void addTransfer(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
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

        double amount = 0;
        try {
            amount = Double.parseDouble(req.getParameter("fund"));
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            throw new RuntimeException(e);
        }

        try {
            //设置事务
            //将转账申请添加到数据库中
            transferData.setAffair();
            int n = transferData.addTransfer(user_payer,enterprise_payer,user_payee,enterprise_payee,amount,description);
            if (n == 1 ){
                int count = 0;
                if (!(enterprise_payer == null || "".equals(enterprise_payer))){
                    //减少企业总资金
                    count = transferData.reduceEnterpriseFunds(enterprise_payer,amount);
                }else {
                    //将用户的资金减少相应的金额
                    count = transferData.reduceFunds(user_payer,enterprise_payer,amount);
                }
                if (count == 1){
                    resp.setStatus(HttpServletResponse.SC_OK);
                    transferData.commit();

                }else{


                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

                }

            }else {


                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

            }
            //提交事务
        } catch (Exception e) {
            //回滚事务

            throw new RuntimeException(e);
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
    public void setTransferTipByTid(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        String tid = req.getParameter("tid");
        int n = transferData.setTransferTipByTid(tid);
        if (n == 1){
            transferData.commit();
            resp.setStatus(HttpServletResponse.SC_OK);
        }else {

            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    public void editTransferStatus(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        String tid = req.getParameter("tid");
        int n = transferData.editTransferStatusByTid(tid);
        if (n == 1){
            transferData.commit();
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
    public void selectAllTransferByEnterprise(HttpServletRequest req, HttpServletResponse resp) {
        String enterprise = req.getParameter("enterprise");
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
        resultSet = transferData.selectAllTransferByEnterprise(enterprise);
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
    public void reduceAmountByTid(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
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
        int count = 0;
        if (!(enterprise_payer == null || "".equals(enterprise_payer))){
            //减少企业总资金
            count = transferData.reduceEnterpriseFunds(enterprise_payer,amount);
        }else {
            //将用户的资金减少相应的金额
            count = transferData.reduceFunds(user_payer,enterprise_payer,amount);
        }
        if (count == 1){

            resp.setStatus(HttpServletResponse.SC_OK);

        }else {

            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);


        }
    }

    @Override
    public void selectPayoutInPending(HttpServletRequest req, HttpServletResponse resp) {
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
        resultSet =  transferData.selectPayoutInPendingByUsername(username);
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
    public void agreeTransferByUser(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        String tid = req.getParameter("tid");
        transferData.setAffair();
        resultSet = transferData.selectTransferByTid(tid);
        double amount = 0 ;
        String user_payee = null;
        String isAccept = null;
        try {
            if (resultSet.next()){
                amount = resultSet.getDouble("amount");
                user_payee = resultSet.getString("user_payee");
                isAccept = resultSet.getString("is_accept");
            }
        } catch (SQLException e) {
            transferData.rollback();
            throw new RuntimeException(e);
        }
        if (!"pending".equals(isAccept)){
            transferData.rollback();
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        try {
            int n = transferData.recordedForUser(user_payee,amount);
            if (n == 1){
                //入账成功，更改转账信息为yes
                int count = transferData.setIsAcceptByTid(tid,"yes");
                if (count == 1 ){
                    //更改信息完成
                    transferData.commit();
                    resp.setStatus(HttpServletResponse.SC_OK);
                }else {
                    transferData.rollback();
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

                }
            }
            else {
                transferData.rollback();
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);


            }

        } catch (SQLException e) {

            throw new RuntimeException(e);
        }

    }

    @Override
    public void refuseTransferByUser(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        String tid = req.getParameter("tid");
        transferData.setAffair();
        resultSet = transferData.selectTransferByTid(tid);
        double amount = 0 ;
        String user_payer = null;
        String enterprise_payer = null;
//        String user_payee = null;
        String isAccept = null;
        int n = 0;
        try {
            if (resultSet.next()){
                amount = resultSet.getDouble("amount");
                user_payer = resultSet.getString("user_payer");
                enterprise_payer = resultSet.getString("enterprise_payer");
                isAccept = resultSet.getString("is_accept");

//                user_payee = resultSet.getString("user_payee");
            }
        } catch (SQLException e) {
            transferData.rollback();
            throw new RuntimeException(e);
        }
        if (!"pending".equals(isAccept)){
            transferData.rollback();
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        if (enterprise_payer == null || "".equals(enterprise_payer)){
            //转账人为用户
            //退账给用户
            n = transferData.chargebacksToUser(user_payer,amount);

        }else {
            //转账主体为企业
            n = transferData.chargebacksToEnterprise(user_payer,enterprise_payer,amount);
        }
        if (n == 1){
            //退账成功，设置is_accept 为no
            int count = transferData.setIsAcceptByTid(tid,"no");
            if (count == 1 ){
                //更改信息完成
                transferData.commit();
                resp.setStatus(HttpServletResponse.SC_OK);
            }else {
                transferData.rollback();
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }else {
            transferData.rollback();
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    public void selectPayoutInPendingByEnterprise(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
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
//        enterpriseData.setAffairs();
        resultSet = enterpriseData.selectEnterpriseByEid(Integer.parseInt(eid));
        String enterprise_payee = null;
        try {
            if (resultSet.next()){
                enterprise_payee = resultSet.getString("ename");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
//        transferData.setAffair();
        resultSet = transferData.selectPayoutInPendingByEnterprise(enterprise_payee);
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
    public void agreeTransferByEnterprise(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        String tid = req.getParameter("tid");
        transferData.setAffair();
        resultSet = transferData.selectTransferByTid(tid);
        double amount = 0 ;
        String isAccept = null;
//        String user_payee = null;
        String enterprise_payee = null;
        try {
            if (resultSet.next()){
                amount = resultSet.getDouble("amount");
                enterprise_payee = resultSet.getString("enterprise_payee");
                isAccept =resultSet.getString("is_accept");
            }
        } catch (SQLException e) {
            transferData.rollback();
            throw new RuntimeException(e);
        }
        if (!"pending".equals(isAccept)){
            transferData.rollback();
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        int n = transferData.recordedForEnterprise(enterprise_payee,amount);
        if (n == 1){
            //入账成功，更改转账信息为yes
            int count = transferData.setIsAcceptByTid(tid,"yes");
            if (count == 1 ){
                //更改信息完成
                transferData.commit();
                resp.setStatus(HttpServletResponse.SC_OK);

            }else {
                transferData.rollback();
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

            }
        }
        else {
                transferData.rollback();
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

        }

    }

    @Override
    public void commitAffair(HttpServletRequest req, HttpServletResponse resp) throws SQLException {

    }


}