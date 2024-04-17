package com.servlet.impl;

import com.Dao.EnterpriseData;
import com.Dao.UserData;
import com.Dao.impl.EnterpriseDataImpl;
import com.Dao.impl.UserDataImpl;
import com.pojo.Enterprise;
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
    ResultSet resultSet = null;
    Enterprise enterprise =null;
    EnterpriseData enterpriseData = new EnterpriseDataImpl();
    List<Enterprise> enterpriseList = null;

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
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void selectAll(HttpServletRequest req, HttpServletResponse resp) {
        resultSet = enterpriseData.selectAllInPublic(req,resp);
        enterpriseList = new ArrayList<Enterprise>();
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
        resultSet = enterpriseData.displayIntroductionById(req,resp);
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
    public void changeEnterpriseInformation(HttpServletRequest req, HttpServletResponse resp) {
        int n = enterpriseData.changeInformationSimple(req,resp);
        if (n == 1)
        {//则更改成功
            resp.setStatus(HttpServletResponse.SC_OK);
        }else{
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    public void selectByEnterpriseName(HttpServletRequest req, HttpServletResponse resp) {
        resultSet = enterpriseData.selectByEnterpriseName(req, resp);
        enterpriseList = new ArrayList<Enterprise>();
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
        resultSet = enterpriseData.selectAllByUsername(req,resp);
        if (resultSet == null){
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        enterpriseList = new ArrayList<Enterprise>();
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
}