package com.Dao.impl;

import com.Dao.ApplicationData;
import com.Dao.EnterpriseData;
import com.untils.JDBC;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 86178
 */
public class EnterpriseDataImpl implements EnterpriseData {
    JDBC jdbc = new JDBC();
    ResultSet resultSet= null;
    Connection connection=jdbc.getConnection();
    ApplicationData applicationData = new ApplicationDataImpl();

    public EnterpriseDataImpl() throws SQLException, IOException, ClassNotFoundException {
    }

    @Override
    public ResultSet selectAllInPublic(HttpServletRequest req, HttpServletResponse resp) {
        String sql = "select * from enterprise where public_mode = 'public'";
        resultSet = jdbc.Select(sql);
        return resultSet;
    }

    @Override
    public ResultSet displayIntroductionById(HttpServletRequest req, HttpServletResponse resp) {
        String id = null;
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("eid".equals(c.getName())) {
                    id = c.getValue();

                }
            }
        }
        String sql = "select * from enterprise where eid = " + id;
        resultSet = jdbc.Select(sql);
        return  resultSet;
    }

    @Override
    public int changeInformationSimple(HttpServletRequest req, HttpServletResponse resp) {
        Cookie[] cookies = req.getCookies();
        String eid = null;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("eid".equals(c.getName())) {
                    eid = c.getValue();

                }
            }
        }
        String ename = req.getParameter("ename");
        String number = req.getParameter("number");
        String size = req.getParameter("size");
        String direction = req.getParameter("direction");
        String public_mode = req.getParameter("public_mode");
        String introduce = req.getParameter("introduce");

        int n = 0;
        if (ename != null || number != null || size != null || direction != null || public_mode != null || introduce != null){
            StringBuffer sql = new StringBuffer("update enterprise set eid = '" + eid +"'");
            if (!(ename == null || ename == "")){
                sql.append(", ename= '" + ename + "'");
            }
            if (!(number ==null || number =="" ))
            {
                sql.append(", number= '"+ number + "'");
            }
            if (!(size == null || size =="")){
                sql.append(", size= '"+ size + "'");
            }
            if (!(direction == null || direction == "")){
                sql.append(", direction = '"+ direction +"'");
            }
            if (!(public_mode == null || public_mode == "")){
                sql.append(", public_mode = '"+ public_mode +"'");
            }if (!(introduce == null || introduce == "")){
                sql.append(", introduce = '"+ introduce +"'");
            }
            sql.append("where eid = '" + eid +"'");
            n = jdbc.Edit(String.valueOf(sql));
        }
        return n;
    }

    @Override
    public int joinEnterprise(HttpServletRequest req, HttpServletResponse resp) {
        resultSet =  applicationData.selectApplicationById(req,resp);
        String username = null;
        int eid = 0;
        try {
            if (resultSet.next()){
                username = resultSet.getString("username");
                eid = resultSet.getInt("eid");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String sql = "insert into relation(username,eid,isleader,Allocation_funds) values('"+username+"',"+eid+",'no',0)";
        return jdbc.Edit(sql);
    }

    @Override
    public int joinLeader(HttpServletRequest req, HttpServletResponse resp) {
        resultSet =  applicationData.selectApplicationById(req,resp);
        String username = null;
        int eid = 0;
        try {
            if (resultSet.next()){
                username = resultSet.getString("username");
                eid = resultSet.getInt("eid");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String sql = "update relation set isLeader = 'yes' where eid = " + eid +" and username = '" + username + "'";
        return jdbc.Edit(sql);
    }



    @Override
    public ResultSet selectByEnterpriseName(HttpServletRequest req, HttpServletResponse resp) {
        String ename = req.getParameter("ename");
        String sql = "select * from enterprise where public_mode = 'public'";
        if (!(ename == null || "".equals(ename)))
        {//判断用户是否有输入
            sql += "and ename like '%" + ename + "%'";
        }

        return jdbc.Select(sql);
    }

    @Override
    public ResultSet selectAllByUsername(HttpServletRequest req, HttpServletResponse resp) {
        String username = null;
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("username".equals(c.getName())) {
                    username = c.getValue();

                }
            }
        }
        String sql = "select * from relation where username = '" + username + "'";
        resultSet = jdbc.Select(sql);
        int[] eid = new int[100];
        int i = 0;
        try {
            while (resultSet.next()){
                eid[i++] = resultSet.getInt("eid");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (i != 0){
            StringBuffer SQL = new StringBuffer("select * from enterprise where eid in (");
            int j = 0 ;
            SQL.append(eid[j++]);
            for (; j < i ; j++ ){
                SQL.append("," + eid[j]);
            }
            SQL.append(")");
            return jdbc.Select(String.valueOf(SQL));

        }else {
            return  null;
        }
    }

    @Override
    public int inviteUsername(HttpServletRequest req, HttpServletResponse resp) {
        String username = null;
        int eid = 0;
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("eid".equals(c.getName())) {
                    eid = Integer.parseInt(c.getValue());
                }
            }
        }
        username = req.getParameter("username");
        String sql = "insert into relation(username,eid,isleader,Allocation_funds) values('"+username+"',"+eid+",'no',0)";
        return jdbc.Edit(sql);

    }
    @Override
    public ResultSet judgmentJoinForInvite(HttpServletRequest req, HttpServletResponse resp) {
        String username = req.getParameter("username");
        String eid = null;
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("eid".equals(c.getName())) {
                    eid = c.getValue();
                }
            }
        }

        String sql = "select * from relation where username ='"+ username +"'  and eid = "+ eid ;
        resultSet =  jdbc.Select(sql);
        return resultSet;
    }

    @Override
    public ResultSet selectAllocationFundsByEid(int eid) {
        String sql = "select * from relation where eid = " + eid + " for update";
        return jdbc.Select(sql);
    }

    @Override
    public ResultSet selectRelationById(long rid) {
        String sql = "select * from relation where rid = " + rid;
        return jdbc.Select(sql);
    }

    @Override
    public ResultSet selectEnterpriseByEid(int eid) throws SQLException {

        String sql = "select * from enterprise where eid = " + eid + " for update";
        return jdbc.Select(sql);
    }

    @Override
    public int updateAllocateFunds(long rid, double fund) {
        String sql = "update relation set Allocation_funds = " + fund + " where rid =" + rid;
        return jdbc.Edit(sql);
    }

    @Override
    public int addEnterprise(String ename, String number, String size, String direction, String publicMode, String introduce) {
        String sql = "insert into enterprise(ename,number,size,direction,public_mode,introduction) values('"+ename+"',"+number+",'"+size+"','"+direction+"','"+publicMode+"','"+introduce+"')";
        return jdbc.Edit(sql);
    }


    @Override
    public int addEnterpriseLeader(String eid, String username) {
        String sql ="insert into relation(username,eid,isleader,allocation_funds) values('"+username+"',"+eid+",'yes',0)";
        return jdbc.Edit(sql);
    }

    @Override
    public ResultSet selectEnterpriseByEnterpriseName(String ename) {
        String sql = "select * from enterprise where ename = '" + ename + "'";
        return jdbc.Select(sql);
    }

    @Override
    public ResultSet selectRelationByUsernameAndEid(int eid, String username) {
        String sql = "select * from relation where eid = "+eid+" and username = '"+username+"'";
        return jdbc.Select(sql);
    }

    @Override
    public void setAffairs() throws SQLException {
        connection.setAutoCommit(false);
    }

    @Override
    public void commit() throws SQLException {
        connection.commit();
        connection.setAutoCommit(true);
    }
    @Override
    public void rollback() throws SQLException {
        connection.rollback();
        connection.setAutoCommit(true);
    }

    @Override
    public int setBlockEnterprise(int eid, String status) {
        String sql = "update enterprise set e_banned = '"+status+"' where eid = " + eid;
        return jdbc.Edit(sql);
    }

    @Override
    public int addEnterpriseApplication(String applicant, String ename, String number, String size, String direction, String publicMode, String introduction) {
        String sql = "insert into enterprise_application(applicant,ename,number,direction,size,public_mode,introduce) values('"+applicant+"','"+ename+"',"+number+",'"+direction+"','"+size+"','"+publicMode+"','"+introduction+"')";
        return jdbc.Edit(sql);
    }

    @Override
    public ResultSet selectEnterpriseApplicationByUser(String applicant) {
        String sql = "select * from enterprise_application where applicant = '" +applicant+ "'";
        return jdbc.Select(sql);
    }

    @Override
    public ResultSet selectEnterpriseApplicationByEname(String ename) {
        String sql = "select * from enterprise_application where ename = '" +ename+ "'";
        return jdbc.Select(sql);
    }

    @Override
    public ResultSet selectAllEnterpriseApplication() {
        String sql = "select * from enterprise_application order by case when is_accept = 'pending' then 0 else 1 end";
        return jdbc.Select(sql);
    }

    @Override
    public ResultSet selectEnterpriseApplicationById(int id) {
        String sql = "select * from enterprise_application where id = '" + id + "' for update";
        return jdbc.Select(sql);
    }

    @Override
    public int updateEnterpriseApplication(int id, String status, String username) {
        String sql = "update enterprise_application set is_accept = '"+status+"' , processor = '"+username+"' where id =" + id;
        return jdbc.Edit(sql);
    }

    @Override
    public ResultSet selectAllFund() {
        String sql = "select sum(Total_fund) as enterpriseFund from enterprise";
        return jdbc.Select(sql);
    }

    @Override
    public ResultSet selectAllEnterprise() {
        String sql = "select * from enterprise";
        return jdbc.Select(sql);
    }

    @Override
    public ResultSet selectAllEnterpriseByEnterpriseName(String ename) {
        String sql = "select * from enterprise where '1' = '1' ";
        if (!(ename == null || "".equals(ename))){
            sql += "and ename like '%" +ename + "%'";
        }
        return jdbc.Select(sql);
    }

    @Override
    public int updatetransfer(String preEnterpriseName,String ename) {
        String sql = "update transfer set enterprise_payer = '"+ename+"' where enterprise_payer = '" +preEnterpriseName+"'";
        jdbc.Edit(sql);
        String sql1 = " update transfer set enterprise_payee = '"+ename+"' where enterprise_payee = '" +preEnterpriseName+"'";
        jdbc.Edit(sql1);
        return 1;
    }

    @Override
    public int updateBlockingApplicationForEname(String preEnterpriseName, String ename) {
        String sql = "update blocking_application set enterprise = '" +ename+"' where enterprise = '" +preEnterpriseName+ "'";
        return jdbc.Edit(sql);
    }

    @Override
    public int updateEnterpriseApplicationForEname(String preEnterpriseName, String ename) {
        String sql = "update enterprise_application set ename = '" +ename+ "' where ename = '" +preEnterpriseName+ "'";
        return jdbc.Edit(sql);
    }


    @Override
    public ResultSet selectSumAllocationFundsByEid(int eid) {
        String sql = "select sum(allocation_funds) as sumAllocation_funds from relation where eid = " + eid;
        return jdbc.Select(sql);
    }

}
