package com.servlet.impl;

import com.Dao.ApplicationData;
import com.Dao.impl.ApplicationDataImpl;
import com.servlet.BaseServlet;
import com.servlet.InteractionServlet;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 86178
 */
@WebServlet(value = "/InteractionServlet")
public class InteractionServletImpl extends BaseServlet implements InteractionServlet {
    ResultSet resultSet = null;
    ApplicationData applicationData = new ApplicationDataImpl();

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
            try {//判断是不是负责人
                if ("yes".equals(resultSet.getString("isleader")))
                {//是负责人
                    resp.setStatus(HttpServletResponse.SC_OK);

                }
                else {//不是负责人
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else{
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

        }
    }
}