package com.servlet.impl;

import com.Dao.EnterpriseData;
import com.Dao.UserData;
import com.Dao.impl.EnterpriseDataImpl;
import com.Dao.impl.UserDataImpl;
import com.pojo.Enterprise;
import com.pojo.EnterpriseApplication;
import com.pojo.Relation;
import com.servlet.BaseServlet;
import com.servlet.LoginServlet;
import com.servlet.OperationServlet;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 86178
 */
@WebServlet(value = "/operationServlet")
public class OperationServletImpl extends BaseServlet implements OperationServlet {
    UserData userData = new UserDataImpl();
//    ResultSet ResultSet resultSet = null;

    EnterpriseData enterpriseData = new EnterpriseDataImpl();

    public OperationServletImpl() throws SQLException, IOException, ClassNotFoundException {
    }

    @Override
    public void loginUser(HttpServletRequest req, HttpServletResponse resp) {

            int ture = 0;
        try {
            Cookie[] cookies = req.getCookies();
            if (cookies != null)
            {
                for (Cookie c: cookies)
                {
                    if ("username".equals(c.getName())){
                        ture = 1 ;
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
        if (ture == 0){
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

        }


    }

    @Override
    public void selectAll(HttpServletRequest req, HttpServletResponse resp) {
        ResultSet resultSet = enterpriseData.selectAllInPublic(req,resp);
        List<Enterprise> enterpriseList = new ArrayList<Enterprise>();
        Enterprise enterprise = null;
        ObjectMapper mapper = new ObjectMapper();
            try {
                while (resultSet.next()){
                    int eid = resultSet.getInt("eid");
                    String ename = resultSet.getString("ename");
                    long number = resultSet.getLong("number");
                    String size = resultSet.getString("size");
                    String direction = resultSet.getString("direction");
                    String publicMode = resultSet.getString("public_mode");
                    String totalFund = resultSet.getString("total_fund");
                    String ebanned = resultSet.getString("e_banned");
                    String introduction = resultSet.getString("introduction");
                    enterprise = new Enterprise(eid,ename,number,size,direction,publicMode,totalFund,introduction,ebanned);
                    enterpriseList.add(enterprise);
                }
                mapper.writeValue(resp.getWriter(),enterpriseList);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (JsonMappingException e) {
                throw new RuntimeException(e);
            } catch (JsonGenerationException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


    }

    @Override
    public void remainEnterpriseId(HttpServletRequest req, HttpServletResponse resp) {

        Cookie cookie = new Cookie("eid", req.getParameter("eid"));
//                cookie.setMaxAge(3600); // 设置Cookie的过期时间为1小时
        resp.addCookie(cookie);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    public void displayIntroductionById(HttpServletRequest req, HttpServletResponse resp) {
        ResultSet resultSet = enterpriseData.displayIntroductionById(req,resp);
        ObjectMapper mapper = new ObjectMapper();
        Map<String,String> map = new HashMap<String,String>();
        try {
            //获取查询结果集中的数据
            if (resultSet.next()){
                String eid = String.valueOf(resultSet.getInt("eid"));
                String ename = resultSet.getString("ename");
                String number = String.valueOf(resultSet.getLong("number"));
                String size = resultSet.getString("size");
                String direction = resultSet.getString("direction");
                String publicMode = resultSet.getString("public_mode");
                String totalFund = resultSet.getString("total_fund");
                String ebanned = resultSet.getString("e_banned");
                String introduction = resultSet.getString("introduction");
                //包装数据为json数据
                map.put("eid",eid);
                map.put("ename",ename);
                map.put("number",number);
                map.put("size",size);
                map.put("direction",direction);
                map.put("public_mode",publicMode);
                map.put("total_fund",totalFund);
                map.put("e_banned",ebanned);
                map.put("introduction",introduction);

                //发送数据到前端
                mapper.writeValue(resp.getWriter(),map);

            }




        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonGenerationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void changeEnterpriseInformation(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        enterpriseData.setAffairs();
        //更改企业账户信息
        Cookie[] cookies = req.getCookies();
        String eid = null;
        for (Cookie c :cookies){
            if ("eid".equals(c.getName())){
                eid = c.getValue();
            }
        }
        ResultSet resultSet = enterpriseData.selectEnterpriseByEid(Integer.parseInt(eid));
        String preEnterpriseName = null;
        if (resultSet.next()){
            preEnterpriseName = resultSet.getString("ename");
        }
        int n = enterpriseData.changeInformationSimple(req,resp);
        String ename = req.getParameter("ename");
        if (n == 1)
        {//则更改成功

            try {
                enterpriseData.updatetransfer(preEnterpriseName,ename);
                enterpriseData.updateBlockingApplicationForEname(preEnterpriseName,ename);
                enterpriseData.updateEnterpriseApplicationForEname(preEnterpriseName,ename);
            } catch (Exception e) {
                enterpriseData.rollback();
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                throw new RuntimeException(e);
                }
            enterpriseData.commit();
            resp.setStatus(HttpServletResponse.SC_OK);
        }else{

            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    public void selectByEnterpriseName(HttpServletRequest req, HttpServletResponse resp) {
        ResultSet resultSet = enterpriseData.selectByEnterpriseName(req, resp);
        List<Enterprise> enterpriseList = new ArrayList<Enterprise>();
        Enterprise enterprise = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            while (resultSet.next()){
                int eid = resultSet.getInt("eid");
                String ename = resultSet.getString("ename");
                long number = resultSet.getLong("number");
                String size = resultSet.getString("size");
                String direction = resultSet.getString("direction");
                String publicMode = resultSet.getString("public_mode");
                String totalFund = resultSet.getString("total_fund");
                String ebanned = resultSet.getString("e_banned");
                String introduction = resultSet.getString("introduction");
                enterprise = new Enterprise(eid,ename,number,size,direction,publicMode,totalFund,introduction,ebanned);
                enterpriseList.add(enterprise);
            }
            mapper.writeValue(resp.getWriter(),enterpriseList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonGenerationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void selectByUsername(HttpServletRequest req, HttpServletResponse resp) {
        ResultSet resultSet = enterpriseData.selectAllByUsername(req,resp);
        if (resultSet == null){
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        List<Enterprise> enterpriseList = new ArrayList<Enterprise>();
        Enterprise enterprise = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            while (resultSet.next()){
                int eid = resultSet.getInt("eid");
                String ename = resultSet.getString("ename");
                long number = resultSet.getLong("number");
                String size = resultSet.getString("size");
                String direction = resultSet.getString("direction");
                String publicMode = resultSet.getString("public_mode");
                String totalFund = resultSet.getString("total_fund");
                String ebanned = resultSet.getString("e_banned");
                String introduction = resultSet.getString("introduction");
                enterprise = new Enterprise(eid,ename,number,size,direction,publicMode,totalFund,introduction,ebanned);
                enterpriseList.add(enterprise);
            }
            resp.setStatus(HttpServletResponse.SC_OK);
            mapper.writeValue(resp.getWriter(),enterpriseList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonGenerationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    @Override
//    public void createEnterprise(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
//        enterpriseData.setAffairs();
//        int n = enterpriseData.addEnterprise(req,resp);
//        Cookie[] cookies = req.getCookies();
//        String username = null;
//        for (Cookie c :cookies){
//            if ("username".equals(c.getName())){
//                username = c.getValue();
//            }
//        }
//        int eid = 0;
//        if (n == 1)
//        {//企业添加成功
//            ResultSet resultSet = enterpriseData.selectByEnterpriseName(req,resp);
//            try {
//                if (resultSet.next()){
//                     eid = resultSet.getInt("eid");
//                }
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//            int success = enterpriseData.addEnterpriseLeader(String.valueOf(eid),username);
//            if (success == 1){
//                Cookie cookie = new Cookie("eid", String.valueOf(eid));
//                //添加cookie或者更改cookie
//                enterpriseData.commit();
//                resp.addCookie(cookie);
//                resp.setStatus(HttpServletResponse.SC_OK);
//            }
//            else {
//
//                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
//            }
//        }else {
//            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
//
//        }
//
//    }

    @Override
    public void judgementEnterpriseName(HttpServletRequest req, HttpServletResponse resp) {
        String ename = req.getParameter("ename");
        ResultSet resultSet = enterpriseData.selectEnterpriseByEnterpriseName(ename);
        try {
            if (resultSet.next()){
                resp.setStatus(HttpServletResponse.SC_OK);

            }else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void judgmentEnterpriseBanned(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        Cookie[] cookies = req.getCookies();
        String eid = null;
        for (Cookie c :cookies){
            if ("eid".equals(c.getName())){
                eid = c.getValue();
            }
        }
        ResultSet resultSet = enterpriseData.selectEnterpriseByEid(Integer.parseInt(eid));
        if (resultSet.next()){
            String banned = resultSet.getString("e_banned");
            if ("yes".equals(banned)){
                //企业被封禁
                resp.setStatus(HttpServletResponse.SC_OK);

            }else {
                //企业未被封禁
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

            }
        }
    }

    @Override
    public void createEnterpriseApplication(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        //判断该用户是否存在待定确认的企业申请，防止用户申请过多，导致数据库过载
        Cookie[] cookies = req.getCookies();
        String username = null;
        for (Cookie c :cookies){
            if ("username".equals(c.getName())){
                username = c.getValue();
            }
        }
        ResultSet resultSet =  enterpriseData.selectEnterpriseApplicationByUser(username);
        while (resultSet.next()){
            if ("pending".equals(resultSet.getString("is_accept"))){
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
        }
        //倘若不存在则下一步
        String ename = req.getParameter("ename");
        //查询是否存在申请企业中为该企业名称的且是尚未处理的申请;
        ResultSet resultSet1 = enterpriseData.selectEnterpriseApplicationByEname(ename);
        while (resultSet.next()){
            if ("pending".equals(resultSet1.getString("is_accept"))){
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
        }
        String number = req.getParameter("number");
        String size = req.getParameter("size");
        String direction = req.getParameter("direction");
        String public_mode = req.getParameter("public_mode");
        String introduction = req.getParameter("introduce");

        int n = enterpriseData.addEnterpriseApplication(username,ename,number,size,direction,public_mode,introduction);
        if (n == 1){
            //成功
            resp.setStatus(HttpServletResponse.SC_OK);

        }else {
            //失败
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

        }
    }

    @Override
    public void selectAllEnterpriseApplication(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        ResultSet resultSet = enterpriseData.selectAllEnterpriseApplication();
        EnterpriseApplication enterpriseApplication = null;
        long id = 0;
        String applicant = null;
        String ename = null;
        long number = 0;
        String direction = null;
        String size = null;
        String publicMode = null;
        String introduce = null;
        String is_accept = null;
        String processor = null;
        List<EnterpriseApplication> enterpriseApplicationList = new ArrayList<EnterpriseApplication>();
        while (resultSet.next()){
            id = Long.parseLong(resultSet.getString("id"));
            applicant = resultSet.getString("applicant");
            ename = resultSet.getString("ename");
            number = Long.parseLong(resultSet.getString("number"));
            direction = resultSet.getString("direction");
            size = resultSet.getString("size");
            publicMode = resultSet.getString("public_mode");
            introduce = resultSet.getString("introduce");
            is_accept = resultSet.getString("is_accept");
            processor = resultSet.getString("processor");
            enterpriseApplication = new EnterpriseApplication(id,applicant,ename,number,direction,size,publicMode,introduce,is_accept,processor);
            enterpriseApplicationList.add(enterpriseApplication);

        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(),enterpriseApplicationList);


    }

    @Override
    public void agreeEnterpriseApplication(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        Cookie[] cookies = req.getCookies();
        String username = null;
        for (Cookie c :cookies){
            if ("username".equals(c.getName())){
                username = c.getValue();
            }
        }
        int id = Integer.parseInt(req.getParameter("id"));
        enterpriseData.setAffairs();
        ResultSet resultSet = enterpriseData.selectEnterpriseApplicationById(id);
        String ename = null;
        String number = null;
        String size = null;
        String direction = null;
        String publicMode = null;
        String introduce = null;
        String applicant = null;
        String isAccept = null;
        if (resultSet.next()){
            applicant = resultSet.getString("applicant");
            ename = resultSet.getString("ename");
            number = resultSet.getString("number");
            size = resultSet.getString("size");
            direction = resultSet.getString("direction");
            publicMode = resultSet.getString("public_mode");
            introduce = resultSet.getString("introduce");
            isAccept = resultSet.getString("is_accept");

        }
        //判断该申请是否已经被处理了
        if (!"pending".equals(isAccept)){
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        int n = enterpriseData.addEnterprise(ename,number,size,direction,publicMode,introduce);
        String eid = null;
        if (n == 1){
            //企业添加成功
            //增添那个关系
            ResultSet resultSet1 = enterpriseData.selectEnterpriseByEnterpriseName(ename);
            if (resultSet1.next()){
                eid = resultSet1.getString("eid");
            }else {
                enterpriseData.rollback();
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            n = enterpriseData.addEnterpriseLeader(String.valueOf(eid),applicant);
            if (n == 1){
                //增添企业负责人成功
                //更改表格
                n = enterpriseData.updateEnterpriseApplication(id,"yes",username);
                if (n == 1){
                    enterpriseData.commit();
                    resp.setStatus(HttpServletResponse.SC_OK);

                }else {
                    enterpriseData.rollback();
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

                }

            }else {
                enterpriseData.rollback();
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

            }
        }
        else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

        }
    }

    @Override
    public void refuseEnterpriseApplication(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        Cookie[] cookies = req.getCookies();
        String username = null;
        for (Cookie c :cookies){
            if ("username".equals(c.getName())){
                username = c.getValue();
            }
        }
        int id = Integer.parseInt(req.getParameter("id"));
        String isAccept = null;
        ResultSet resultSet = enterpriseData.selectEnterpriseApplicationById(id);
        if (resultSet.next()) {
            isAccept = resultSet.getString("is_accept");
        }
        //判断该申请是否已经被处理了
        if (!"pending".equals(isAccept)){
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        int n = enterpriseData.updateEnterpriseApplication(id,"no",username);
        if (n == 1){
            resp.setStatus(HttpServletResponse.SC_OK);

        }else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

        }
    }

    @Override
    public void selectEnterpriseApplicationByUser(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        Cookie[] cookies = req.getCookies();
        String username = null;
        for (Cookie c :cookies){
            if ("username".equals(c.getName())){
                username = c.getValue();
            }
        }
        ResultSet resultSet = enterpriseData.selectEnterpriseApplicationByUser(username);
        long id = 0;
        String applicant = null;
        String ename = null;
        long number = 0;
        String direction = null;
        String size = null;
        String publicMode = null;
        String introduce = null;
        String is_accept = null;
        String processor = null;
        EnterpriseApplication enterpriseApplication = null;
        List<EnterpriseApplication> enterpriseApplicationList = new ArrayList<EnterpriseApplication>();
        while (resultSet.next()){
            id = Long.parseLong(resultSet.getString("id"));
            applicant = resultSet.getString("applicant");
            ename = resultSet.getString("ename");
            number = Long.parseLong(resultSet.getString("number"));
            direction = resultSet.getString("direction");
            size = resultSet.getString("size");
            publicMode = resultSet.getString("public_mode");
            introduce = resultSet.getString("introduce");
            is_accept = resultSet.getString("is_accept");
            processor = resultSet.getString("processor");
            enterpriseApplication = new EnterpriseApplication(id,applicant,ename,number,direction,size,publicMode,introduce,is_accept,processor);
            enterpriseApplicationList.add(enterpriseApplication);

        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(),enterpriseApplicationList);

    }

    @Override
    public synchronized void  selectAllFundInWebsite(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        double userFund = 0;
        double enterpriseFund = 0;
        ResultSet resultSet = userData.selectAllFund();
        if (resultSet.next()){
            userFund = resultSet.getDouble("userFund");
        }else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        ResultSet resultSet1 = enterpriseData.selectAllFund();
        if (resultSet1.next()){
            enterpriseFund = resultSet1.getDouble("enterpriseFund");
        }else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        double TotalFund = userFund + enterpriseFund;
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(String.valueOf(TotalFund));


    }

    @Override
    public void selectAllForAdministrator(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        ResultSet resultSet = enterpriseData.selectAllEnterprise();
        List<Enterprise> enterpriseList = new ArrayList<Enterprise>();
        Enterprise enterprise = null;
        int eid = 0;
        String ename = null;
        long number = 0;
        String size = null;
        String direction = null;
        String publicMode = null;
        String totalFund = null;
        String ebanned = null;
        String introduction = null;
        while (resultSet.next()){
            eid = resultSet.getInt("eid");
            ename = resultSet.getString("ename");
            number = resultSet.getLong("number");
            size = resultSet.getString("size");
            direction = resultSet.getString("direction");
            publicMode = resultSet.getString("public_mode");
            totalFund = resultSet.getString("total_fund");
            ebanned = resultSet.getString("e_banned");
            introduction = resultSet.getString("introduction");
            enterprise = new Enterprise(eid,ename,number,size,direction,publicMode,totalFund,introduction,ebanned);
            enterpriseList.add(enterprise);
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(),enterpriseList);


    }

    @Override
    public void selectByEnterpriseNameForAdministrator(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        String ename = req.getParameter("ename");
        ResultSet resultSet = enterpriseData.selectAllEnterpriseByEnterpriseName(ename);
        List<Enterprise> enterpriseList = new ArrayList<Enterprise>();
        Enterprise enterprise = null;
        int eid = 0;
        long number = 0;
        String size = null;
        String direction = null;
        String publicMode = null;
        String totalFund = null;
        String ebanned = null;
        String introduction = null;
        while (resultSet.next()){
            eid = resultSet.getInt("eid");
            ename = resultSet.getString("ename");
            number = resultSet.getLong("number");
            size = resultSet.getString("size");
            direction = resultSet.getString("direction");
            publicMode = resultSet.getString("public_mode");
            totalFund = resultSet.getString("total_fund");
            ebanned = resultSet.getString("e_banned");
            introduction = resultSet.getString("introduction");
            enterprise = new Enterprise(eid,ename,number,size,direction,publicMode,totalFund,introduction,ebanned);
            enterpriseList.add(enterprise);
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(),enterpriseList);
    }
}