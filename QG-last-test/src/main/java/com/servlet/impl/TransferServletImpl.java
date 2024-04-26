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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(value = "/transferServlet")
@MultipartConfig
public class TransferServletImpl extends BaseServlet implements TransferServlet {

    TransferData transferData = new TransferDataImpl();
//    ResultSet ResultSet resultSet = null;
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
                    //减少企业分配资金
                    int number = transferData.reduceEnterpriseAllocatedFunds(user_payer,enterprise_payer,amount);
                    if (number == 1){
                        //减少企业总资金
                        count = transferData.reduceEnterpriseFunds(enterprise_payer,amount);

                    }else {
                        transferData.rollback();
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
                }else {
                    //将用户的资金减少相应的金额
                    count = transferData.reduceFunds(user_payer,enterprise_payer,amount);
                }
                if (count == 1){
                    resp.setStatus(HttpServletResponse.SC_OK);
                    transferData.commit();

                }else{

                    transferData.rollback();

                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

                }

            }else {

                transferData.rollback();

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
        ResultSet resultSet = transferData.selectTransferInNOTipByUsername(username);
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

            resp.setStatus(HttpServletResponse.SC_OK);
        }else {

            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    public void editTransferStatus(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        String tid = req.getParameter("tid");
        transferData.setAffair();
        int n = transferData.editTransferStatusByTid(tid);
        if (n == 1){
            tid = req.getParameter("tid");
            ResultSet resultSet = transferData.selectTransferByTid(tid);
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
                //减少企业分配资金
                int number = transferData.reduceEnterpriseAllocatedFunds(user_payer,enterprise_payer,amount);
                if (number == 1){
                    //减少企业总资金
                    count = transferData.reduceEnterpriseFunds(enterprise_payer,amount);

                }else {
                    transferData.rollback();
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
            }else {
                //将用户的资金减少相应的金额
                count = transferData.reduceFunds(user_payer,enterprise_payer,amount);
            }
            if (count == 1){
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
    public void selectAllTransferByUser(HttpServletRequest req, HttpServletResponse resp) {
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
        ResultSet resultSet = transferData.selectAllTransferByUser(username);
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
        ResultSet resultSet = transferData.selectAllTransferByEnterprise(enterprise);
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
        ResultSet resultSet = transferData.selectTransferByTid(tid);
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
        ResultSet resultSet =  transferData.selectPayoutInPendingByUsername(username);
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
        ResultSet resultSet = transferData.selectTransferByTid(tid);
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
        ResultSet resultSet = transferData.selectTransferByTid(tid);
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
        ResultSet resultSet = enterpriseData.selectEnterpriseByEid(Integer.parseInt(eid));
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
        ResultSet resultSet = transferData.selectTransferByTid(tid);
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

    @Override
    public void deregisterEnterprise(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        String eid = null;
        Cookie[] cookies = req.getCookies();
        //获取cookie
        String username = null;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("eid".equals(c.getName())) {
                    eid = c.getValue();
                }
            }
        }
        transferData.setAffair();
        int n = transferData.distributeFundAfterDeregisterEnterprise(eid);

        if (n != 0){
            //分配企业资金
            int count = transferData.deleteEnterprise(req,resp);
            if (count != 0){
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
    public void selectAllTransfer(HttpServletRequest req, HttpServletResponse resp) {
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
        ResultSet resultSet = transferData.selectAllTransfer();

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
    public void rechargeForEnterprise(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        double fund = Double.parseDouble(req.getParameter("fund"));
        Cookie[] cookies = req.getCookies();
        //获取cookie
        String eid = null;
        String username =  null;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("eid".equals(c.getName())) {
                    eid = c.getValue();
                }else if ("username".equals(c.getName())){
                    username = c.getValue();
                }
            }
        }
        transferData.setAffair();
        int n = transferData.rechargeForEnterprises(fund,eid);
        if (n == 1){
            int count = transferData.reduceUser(fund,username);
            if (count == 1)
            {
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
    public void changePayment(HttpServletRequest req, HttpServletResponse resp) {
        String password = req.getParameter("payment");
        Cookie[] cookies = req.getCookies();
        String username =  null;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("username".equals(c.getName())){
                    username = c.getValue();
                }
            }
        }
        String hashPassword = transferData.encryptProcess(password);
        int n = transferData.changePayPassword(username,hashPassword);
        if (n == 1)
        {
            resp.setStatus(HttpServletResponse.SC_OK);

        }else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

        }
    }

    @Override
    public void upload(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            Part filePart = req.getPart("file");
            String enterprise = req.getParameter("enterprise");
            String fund = req.getParameter("fund");
            Cookie[] cookies = req.getCookies();
            String username =  null;
            if (cookies != null) {
                for (Cookie c : cookies) {
                    if ("username".equals(c.getName())){
                        username = c.getValue();
                    }
                }
            }
            //判断上传文件是否为空
            if (filePart != null){
                String fileName = filePart.getSubmittedFileName();
                if (fileName == null){
                    return;
                }
                // 设置保存文件的目录
                String uploadDir = "D:\\GitHub\\QG_last_test\\QG-last-test\\upload\\";
                System.out.println(uploadDir);
                File uploadFile = new File(uploadDir+ fileName);
                try (InputStream fileContent = filePart.getInputStream();
                     FileOutputStream outputStream = new FileOutputStream(uploadFile)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = fileContent.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
                enterpriseData.addFileUpload(username,enterprise,fund,uploadDir + fileName);


                resp.getWriter().write("文件上传成功");
            }else {
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("文件上传失败");
        }
    }



}