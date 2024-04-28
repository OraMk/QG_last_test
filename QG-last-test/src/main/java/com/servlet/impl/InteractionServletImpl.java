package com.servlet.impl;

import com.Dao.ApplicationData;
import com.Dao.UserData;
import com.Dao.impl.ApplicationDataImpl;
import com.Dao.impl.EnterpriseDataImpl;
import com.Dao.impl.UserDataImpl;
import com.pojo.Application;
import com.pojo.BlockingApplication;
import com.pojo.Enterprise;
import com.pojo.Relation;
import com.servlet.BaseServlet;
import com.servlet.InteractionServlet;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 86178
 */
@WebServlet(value = "/InteractionServlet")
public class InteractionServletImpl extends BaseServlet implements InteractionServlet {
//    ResultSet ResultSet resultSet = null;
    ApplicationData applicationData = new ApplicationDataImpl();
    Application application = null;
    EnterpriseDataImpl enterpriseData = new EnterpriseDataImpl();
    Relation relation = null;
    UserData userData = new UserDataImpl();



    public InteractionServletImpl() throws SQLException, IOException, ClassNotFoundException {
    }

    @Override
    public void applyToJoin(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        boolean isApply = false;
        try {//先查询是否有重复的未受理的请求，以防产生多次请求
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
    public void judgmentJoin(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        //调用dao层去实现查询
        ResultSet resultSet = applicationData.judgmentJoin(req,resp);
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
    public void deleteRelation(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        int n = applicationData.deleteRelation(req, resp);
        if (n != 0){
            //则退出成功

            resp.setStatus(HttpServletResponse.SC_OK);
        }else {

            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    public void applyToLeader(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        String isApply = null;
        String username = null;
        String eid = null;
        ResultSet resultSet = null;
        try {//先查询是否有重复的未受理的请求，以防产生多次请求
            resultSet = applicationData.judgmentApplyLeader(req,resp);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (resultSet.next()){
            isApply = resultSet.getString("is_accept");
            username = resultSet.getString("username");
            eid = resultSet.getString("eid");
        }
        if (!"pending".equals(isApply)){
            int n =  applicationData.addApplicationLeader(req,resp);
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
        ResultSet resultSet = applicationData.judgmentJoin(req,resp);
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
        ResultSet resultSet = applicationData.displayApplication(req,resp);
        long aid = 0;
        String username = null;
        int eid = 0;
        String isAccept = null;
        String description = null;
        //获取查询结果
        List<Application> applicationList = new ArrayList<Application>();
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
        ResultSet resultSet = applicationData.displayHistoryApplication(req,resp);
        long aid = 0;
        String username = null;
        int eid = 0;
        String isAccept = null;
        String description = null;
        //获取查询结果
        List<Application> applicationList = new ArrayList<Application>();
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
    public void agreeApplication(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        applicationData.setAffair();
        ResultSet resultSet = applicationData.selectApplicationById(req,resp);
        String description = null;
//        long aid = 0;
        String eid = null;
        String username = null;
        try {
            if (resultSet.next()){
                description = resultSet.getString("description");
//                aid = resultSet.getLong("aid");
                eid = resultSet.getString("eid");
                username = resultSet.getString("username");
            }
        } catch (SQLException e) {
            applicationData.rollback();
            throw new RuntimeException(e);
        }
        if ("pending".equals(resultSet.getString("is_accept")))
        {
            if ("申请加入企业".equals(description)){
                agreeJoinEnterprise(req,resp,username,eid);

            } else if ("申请成为负责人".equals(description)) {
                updateEnterpriseLeader(req,resp,username,eid);
            }
            int n = applicationData.agreeApplication(req,resp);
            if (n == 1){
                applicationData.commit();
                resp.setStatus(HttpServletResponse.SC_OK);
            }else {
                applicationData.rollback();
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

            }
        }else {
            applicationData.rollback();

            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

        }


    }

    @Override
    public void refuseApplication(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        int n = applicationData.refuseApplication(req, resp);
        if (n == 1){

            resp.setStatus(HttpServletResponse.SC_OK);

        }else {

            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

        }
    }

    @Override
    public void agreeJoinEnterprise(HttpServletRequest req, HttpServletResponse resp,String username,String eid) throws SQLException {
        int n = enterpriseData.joinEnterprise(username,eid);
        if (n == 1){

            resp.setStatus(HttpServletResponse.SC_OK);

        }else {

            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    public void updateEnterpriseLeader(HttpServletRequest req, HttpServletResponse resp,String username,String eid) throws SQLException {
        int n = enterpriseData.joinLeader(username,eid);
        if (n == 1){

            resp.setStatus(HttpServletResponse.SC_OK);

        }else {

            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }



    @Override
    public void inviteUsername(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        int n = enterpriseData.inviteUsername(req,resp);
        if (n == 1){

            resp.setStatus(HttpServletResponse.SC_OK);

        }else {

            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

        }
    }

    @Override
    public void judgmentJoinForInvite(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        ResultSet resultSet = enterpriseData.judgmentJoinForInvite(req,resp);
        int n = 0 ;
        try {
            if (resultSet.next()){
                n++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (n == 1){

            resp.setStatus(HttpServletResponse.SC_OK);
        }else {

            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

        }
    }

    @Override
    public void displayAllocationFunds(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        //获取企业id
        Cookie[] cookies = req.getCookies();
        //获取cookie
        int eid = 0;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("eid".equals(c.getName())) {
                    eid = Integer.parseInt(c.getValue());
                }
            }
        }
        long rid = 0;
        String username = null;
        String isLeader = null;
        double allocationFunds = 0;
        if (eid != 0){
            //根据企业id查找分配资金
            List<Relation> relationList = new ArrayList<Relation>();
            ResultSet resultSet = enterpriseData.selectAllocationFundsByEid(eid);

            try {
                while (resultSet.next()){
                    rid = resultSet.getLong("rid");
                    username = resultSet.getString("username");
                    isLeader = resultSet.getString("isLeader");
                    allocationFunds = resultSet.getDouble("Allocation_funds");
                    relation = new Relation(rid,username,eid,isLeader,allocationFunds);
                    relationList.add(relation);
                }


                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(resp.getWriter(),relationList);
            } catch (SQLException | IOException e) {

                throw new RuntimeException(e);
            }
        }else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }


    }

    @Override
    public void selectRelationById(HttpServletRequest req, HttpServletResponse resp) {
        long rid = Long.parseLong(req.getParameter("rid"));
        ResultSet resultSet = enterpriseData.selectRelationById(rid);
        int eid = 0;
        String username = null;
        String isLeader =null;
        double allocationFunds = 0;
        Map<String,String> map = new HashMap<String,String>();
        try {
            if (resultSet.next()){
                eid = resultSet.getInt("eid");
                username = resultSet.getString("username");
                isLeader = resultSet.getString("isLeader");
                allocationFunds = resultSet.getDouble("Allocation_funds");

            }
            map.put("rid", String.valueOf(rid));
            map.put("username",username);
            map.put("eid", String.valueOf(eid));
            map.put("isLeader",isLeader);
            map.put("Allocation_funds", String.valueOf(allocationFunds));
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(resp.getWriter(),map);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void compareFunds(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        //获取企业id
        Cookie[] cookies = req.getCookies();
        //获取cookie
        int eid = 0;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("eid".equals(c.getName())) {
                    eid = Integer.parseInt(c.getValue());
                }
            }
        }
        long rid = Long.parseLong(req.getParameter("rid"));
        double fund = Double.parseDouble(req.getParameter("allocation_funds"));
        double totalFunds = 0;
        ResultSet resultSet = enterpriseData.selectEnterpriseByEid(eid);
        try {
            if (resultSet.next()){
                totalFunds = resultSet.getDouble("Total_fund");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        double sumFunds = 0;
        resultSet = enterpriseData.selectAllocationFundsByEid(eid);
        try {
            while (resultSet.next()){
                if (resultSet.getLong("rid") != rid){
                    //判断更改关系编号为哪一个
                    sumFunds += resultSet.getDouble("Allocation_funds");

                }
                else//找到了
                {
                    sumFunds +=fund;//加上更改后的资金
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (sumFunds <= totalFunds){
            //若资金总和小于等于总资金则合理

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"remaining_funds\":\""+(totalFunds-sumFunds)+"\"");

        }else {
            //若资金总和大于总资金则该次更改不合理

            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

        }

    }

    @Override
    public void toAllocateFunds(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        long rid = Long.parseLong(req.getParameter("rid"));
        double fund = Double.parseDouble(req.getParameter("allocation_funds"));
        int n = enterpriseData.updateAllocateFunds(rid,fund);
        //提交事务
        if (n == 1){
            resp.setStatus(HttpServletResponse.SC_OK);

        }else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

        }
    }

    @Override
    public void applyUnblockingByUsername(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        Cookie[] cookies = req.getCookies();
        String username = null;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("username".equals(c.getName())) {
                    username = c.getValue();

                }
            }
        }
        //判断是否发送过一个待定审理的申请
        ResultSet resultSet = applicationData.selectUnblockingApplicationByUsername(username);
        int judgment = 0 ;
        try {
            while (resultSet.next()){
                if ("pending".equals(resultSet.getString("is_accept"))){
                    //存在待定的申请，避免重复申请
                    judgment = 1;
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (judgment == 0){
            int n = applicationData.applyUnblockingForUsername(username);
            if (n == 1){

                resp.setStatus(HttpServletResponse.SC_OK);

            }else {

                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

            }
        }

    }

    @Override
    public void enterpriseAllocateFund(HttpServletRequest req, HttpServletResponse resp) {
        int eid = Integer.parseInt(req.getParameter("eid"));
        Cookie[] cookies = req.getCookies();
        String username = null;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("username".equals(c.getName())) {
                    username = c.getValue();

                }
            }
        }
        long rid = 0;
        String isleader = null;
        double allocationFunds = 0;
        ResultSet resultSet = enterpriseData.selectRelationByUsernameAndEid(eid,username);
        Map<String,String> map = new HashMap<String,String>();
        try {
            if (resultSet.next()){
                rid = resultSet.getLong("rid");
                isleader = resultSet.getString("isLeader");
                allocationFunds = resultSet.getDouble("Allocation_funds");
                map.put("rid", String.valueOf(rid));
                map.put("username",username);
                map.put("eid", String.valueOf(eid));
                map.put("isleader",isleader);
                map.put("Allocation_funds", String.valueOf(allocationFunds));
                ObjectMapper mapper = new ObjectMapper();
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
    public void judgmentEnterpriseBan(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        int eid = Integer.parseInt(req.getParameter("eid"));
        ResultSet resultSet =  enterpriseData.selectEnterpriseByEid(eid);
        try {
            if (resultSet.next()){
                String e_banned = resultSet.getString("e_banned");
                if ("no".equals(e_banned)){
                    //没有被封禁
                    Map<String,String> map = new HashMap<String,String>();
                    map.put("ename", resultSet.getString("ename"));
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.writeValue(resp.getWriter(),map);
                }else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
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
    public void checkRemainingFunds(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        //获取企业id
        Cookie[] cookies = req.getCookies();
        //获取cookie
        int eid = 0;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("eid".equals(c.getName())) {
                    eid = Integer.parseInt(c.getValue());
                }
            }
        }
        double totalFunds = 0;
        ResultSet resultSet = enterpriseData.selectEnterpriseByEid(eid);
            if (resultSet.next()){
                totalFunds = resultSet.getDouble("Total_fund");
            }
        double sumFunds = 0;
        resultSet = enterpriseData.selectSumAllocationFundsByEid(eid);
            if (resultSet.next()) {
                sumFunds = resultSet.getDouble("sumAllocation_funds");
            }
            //若资金总和小于等于总资金则合理
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(String.valueOf(totalFunds-sumFunds));

    }

    @Override
    public void selectBlockingApplication(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {

        ResultSet resultSet =  applicationData.selectBlockingApplication();
        BlockingApplication blockingApplication = null;
        long id = 0 ;
        String username = null;
        String enterprise = null;
        String is_accept = null;
        String processor = null;
        List<BlockingApplication> blockingApplications = new ArrayList<BlockingApplication>();
        while (resultSet.next()){
            id = resultSet.getLong("id");
            username = resultSet.getString("username");
            enterprise = resultSet.getString("enterprise");
            is_accept = resultSet.getString("is_accept");
            processor = resultSet.getString("processor");
            blockingApplication = new BlockingApplication(id,username,enterprise,is_accept,processor);
            blockingApplications.add(blockingApplication);
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(),blockingApplications);
    }

    @Override
    public void agreeUnblockingApplication(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        Cookie[] cookies = req.getCookies();
        //获取cookie
        String processor = null;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("username".equals(c.getName())) {
                    processor = c.getValue();
                }
            }
        }
        String id = req.getParameter("id");
        //设置事务
        applicationData.setAffair();
        //查找该解封id对应的用户企业信息
        ResultSet resultSet = applicationData.selectBlockingApplicationById(id);
        String username = null;
        String enterprise = null;
        if (resultSet.next()){
            username = resultSet.getString("username");
            enterprise = resultSet.getString("enterprise");
        }
        int n = 0;
        if (enterprise == null || "".equals(enterprise)){
            //请求解封为用户
                //解封用户
            n = applicationData.unblockForUser(username);
        }else {
            //请求解封为企业
                //解封企业
            n = applicationData.unblockForEnterprise(enterprise);

        }
        if (n == 1){
            //解封成功
                //更改解封信息
            n = applicationData.changeUnblockingApplication(id,"yes",processor);
            if (n == 1){
                //更改成功
                    //提交事务
                applicationData.commit();
                resp.setStatus(HttpServletResponse.SC_OK);

            }else {
                applicationData.rollback();
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);


            }
        }else {
            applicationData.rollback();
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);


        }
    }

    @Override
    public void refuseUnblockingApplication(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        Cookie[] cookies = req.getCookies();
        //获取cookie
        String processor = null;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("username".equals(c.getName())) {
                    processor = c.getValue();
                }
            }
        }
        String id = req.getParameter("id");
        int n = 0;
        n = applicationData.changeUnblockingApplication(id,"no",processor);
        if (n == 1){
            resp.setStatus(HttpServletResponse.SC_OK);

        }else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

        }

    }

    @Override
    public void blockUser(HttpServletRequest req, HttpServletResponse resp) {
            String uid = req.getParameter("uid");
            int n = userData.setBlockUser(uid,"yes");
            if (n == 1){
                resp.setStatus(HttpServletResponse.SC_OK);

            }else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

            }
    }

    @Override
    public void blockEnterprise(HttpServletRequest req, HttpServletResponse resp) {
        Cookie[] cookies = req.getCookies();
        //获取cookie
        int eid = 0;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("eid".equals(c.getName())) {
                    eid = Integer.parseInt(c.getValue());
                }
            }
        }
        int n = enterpriseData.setBlockEnterprise(eid,"yes");
        if (n == 1){
            resp.setStatus(HttpServletResponse.SC_OK);

        }else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

        }
    }

    @Override
    public void unblockUser(HttpServletRequest req, HttpServletResponse resp) {
        String uid = req.getParameter("uid");
        int n = userData.setBlockUser(uid,"no");
        if (n == 1){
            resp.setStatus(HttpServletResponse.SC_OK);

        }else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

        }
    }

    @Override
    public void unblockEnterprise(HttpServletRequest req, HttpServletResponse resp) {
        Cookie[] cookies = req.getCookies();
        //获取cookie
        int eid = 0;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("eid".equals(c.getName())) {
                    eid = Integer.parseInt(c.getValue());
                }
            }
        }
        int n = enterpriseData.setBlockEnterprise(eid,"no");
        if (n == 1){
            resp.setStatus(HttpServletResponse.SC_OK);

        }else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

        }
    }

    @Override
    public void applyUnblockingByEnterprise(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        Cookie[] cookies = req.getCookies();
        //获取cookie
        int eid = 0;
        String username = null;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("eid".equals(c.getName())) {
                    eid = Integer.parseInt(c.getValue());
                }else if ("username".equals(c.getName())){
                    username = c.getValue();
                }
            }
        }
        String enterprise = null;
        ResultSet resultSet = enterpriseData.selectEnterpriseByEid(eid);
        if (resultSet.next()){
            enterprise = resultSet.getString("ename");
        }
        resultSet = applicationData.selectUnblockingApplicationByEnterprise(enterprise);
        int judgment = 0;
        int n = 0;
        if(resultSet.next()){
            if ("pending".equals(resultSet.getString("is_accept"))){
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);


            }else {
                 n = applicationData.applyUnblockingForEnterprise(username,enterprise);
            }
        }else {
            n = applicationData.applyUnblockingForEnterprise(username,enterprise);
        }
        if (n == 1){
            resp.setStatus(HttpServletResponse.SC_OK);

        }else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

        }
    }

    @Override
    public void judgmentEnterpriseBanAlways(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        Cookie[] cookies = req.getCookies();
        //获取cookie
        int eid = 0;
        String username = null;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("eid".equals(c.getName())) {
                    eid = Integer.parseInt(c.getValue());
                }
            }
        }
        ResultSet resultSet =  enterpriseData.selectEnterpriseByEid(eid);
        if (resultSet.next()){
            String e_banned = resultSet.getString("e_banned");
            if ("no".equals(e_banned)){
                //没有被封禁
                Map<String,String> map = new HashMap<String,String>();
                map.put("ename", resultSet.getString("ename"));
                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(resp.getWriter(),map);
            }else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }
}