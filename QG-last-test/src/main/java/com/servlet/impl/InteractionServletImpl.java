package com.servlet.impl;

import com.Dao.ApplicationData;
import com.Dao.impl.ApplicationDataImpl;
import com.Dao.impl.EnterpriseDataImpl;
import com.pojo.Application;
import com.pojo.Enterprise;
import com.servlet.BaseServlet;
import com.servlet.InteractionServlet;
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

/**
 * @author 86178
 */
@WebServlet(value = "/InteractionServlet")
public class InteractionServletImpl extends BaseServlet implements InteractionServlet {
    ResultSet resultSet = null;
    ApplicationData applicationData = new ApplicationDataImpl();
    List<Application> applicationList = null;
    Application application = null;
    EnterpriseDataImpl enterpriseData = new EnterpriseDataImpl();

    public InteractionServletImpl() throws SQLException, IOException, ClassNotFoundException {
    }

    @Override
    public void applyToJoin(HttpServletRequest req, HttpServletResponse resp) {
        boolean isApply = false;
        try {//先查询是否有重复的未受理的请求，以防多次请求
            isApply = applicationData.judgmentApplyJoin(req,resp);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (isApply == false){
            int n =  applicationData.addApplicationJoin(req, resp);
            if (n == 1){
                //数据添加成功
                resp.setStatus(HttpServletResponse.SC_OK);
            }else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }



    }

    @Override
    public void judgmentJoin(HttpServletRequest req, HttpServletResponse resp) {
        //调用dao层去实现查询
        resultSet = applicationData.judgmentJoin(req,resp);
        int n = 0;
        while(true)
        {
            try {
                if (!resultSet.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            n++ ;
        }

        if (n != 0){
            //则说明已经在在该企业了
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }else{
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }

    @Override
    public void deleteRelation(HttpServletRequest req, HttpServletResponse resp) {
        int n = applicationData.deleteRelation(req, resp);
        if (n != 0){
            //则退出成功
            resp.setStatus(HttpServletResponse.SC_OK);
        }else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    public void applyToLeader(HttpServletRequest req, HttpServletResponse resp) {
        boolean isApply = false;
        try {//先查询是否有重复的未受理的请求，以防多次请求
            isApply = applicationData.judgmentApplyLeader(req,resp);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (isApply == false){
            int n =  applicationData.addApplicationLeader(req, resp);
            if (n == 1){
                //数据添加成功
                resp.setStatus(HttpServletResponse.SC_OK);
            }else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

    }

    @Override
    public void judgmentLeader(HttpServletRequest req, HttpServletResponse resp) {
        resultSet = applicationData.judgmentJoin(req,resp);
        int n = 0;


            try {//判断是不是负责人
                if (resultSet.next()){
                    if ("yes".equals(resultSet.getString("isleader")))
                    {//是负责人
                        resp.setStatus(HttpServletResponse.SC_OK);

                    }
                    else {//不是负责人
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

                    }
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

    }

    @Override
    public void displayApplication(HttpServletRequest req, HttpServletResponse resp) {
        //调用dao层
        resultSet = applicationData.displayApplication(req,resp);
        long aid = 0;
        String username = null;
        int eid = 0;
        String isAccept = null;
        String description = null;
        //获取查询结果
        applicationList = new ArrayList<Application>();
        try {
            while (resultSet.next()){
                aid = resultSet.getLong("aid");
                username = resultSet.getString("username");
                eid = resultSet.getInt("eid");
                isAccept = resultSet.getString("is_accept");
                description = resultSet.getString("description");
                application = new Application(aid,username,eid,isAccept,description);
                applicationList.add(application);
            }
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(resp.getWriter(),applicationList);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void displayHistoryApplication(HttpServletRequest req, HttpServletResponse resp) {
        //调用dao层
        resultSet = applicationData.displayHistoryApplication(req,resp);
        long aid = 0;
        String username = null;
        int eid = 0;
        String isAccept = null;
        String description = null;
        //获取查询结果
        applicationList = new ArrayList<Application>();
        try {
            while (resultSet.next()){
                aid = resultSet.getLong("aid");
                username = resultSet.getString("username");
                eid = resultSet.getInt("eid");
                isAccept = resultSet.getString("is_accept");
                description = resultSet.getString("description");
                application = new Application(aid,username,eid,isAccept,description);
                applicationList.add(application);
            }
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(resp.getWriter(),applicationList);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void agreeApplication(HttpServletRequest req, HttpServletResponse resp) {
        resultSet = applicationData.selectApplicationById(req,resp);
        String description = null;
//        long aid = 0;
        try {
            if (resultSet.next()){
                description = resultSet.getString("description");
//                aid = resultSet.getLong("aid");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if ("申请加入企业".equals(description)){
            agreeJoinEnterprise(req,resp);

        } else if ("申请成为负责人".equals(description)) {
            updateEnterpriseLeader(req,resp);
        }
        int n = applicationData.agreeApplication(req,resp);
        if (n == 1){
            resp.setStatus(HttpServletResponse.SC_OK);
        }

    }

    @Override
    public void agreeJoinEnterprise(HttpServletRequest req, HttpServletResponse resp) {
        int n = enterpriseData.joinEnterprise(req, resp);
        if (n == 1){
            resp.setStatus(HttpServletResponse.SC_OK);

        }else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    public void updateEnterpriseLeader(HttpServletRequest req, HttpServletResponse resp) {
        int n = enterpriseData.joinLeader(req,resp);
        if (n == 1){
            resp.setStatus(HttpServletResponse.SC_OK);

        }else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}