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
import java.util.List;

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

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void selectAll(HttpServletRequest req, HttpServletResponse resp) {
        resultSet = enterpriseData.selectAll(req,resp);
        enterpriseList = new ArrayList<Enterprise>();
        ObjectMapper mapper = new ObjectMapper();
            try {
                while (resultSet.next()){
//                    enterprise = new Enterprise();
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
}