package com.servlet.impl;

import com.Dao.UserData;
import com.Dao.impl.UserDataImpl;
import com.alibaba.fastjson.JSONObject;
import com.pojo.User;
import com.servlet.BaseServlet;
import com.servlet.LoginServlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.codehaus.jackson.map.ObjectMapper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

/**
 * @author 86178
 */
@WebServlet(value = "/loginServlet")
public class LoginServletImpl extends BaseServlet implements LoginServlet{
    UserData userData = new UserDataImpl();
//    ResultSet ResultSet resultSet = null;
    public LoginServletImpl() throws SQLException, IOException, ClassNotFoundException {
    }

    @Override
    public void selectUser(HttpServletRequest req, HttpServletResponse resp) {

        try {
            if (req.getParameter("username") == null)
            {
                Cookie cookie = new Cookie("username", "");
                resp.addCookie(cookie);
            }
            else
            {
                String username = req.getParameter("username");
                String password = req.getParameter("password");
                String hashPassword = userData.encryptProcess(password);
                ResultSet resultSet = userData.selectUser(username,hashPassword);
                if (resultSet.next())
                {
                    //通过设置cookie记录用户信息
                    Cookie cookie = new Cookie("username", req.getParameter("username"));
//                cookie.setMaxAge(3600); // 设置Cookie的过期时间为1小时
                    resp.addCookie(cookie);

                    resp.setStatus(HttpServletResponse.SC_OK);
                }else{

                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        String username =  req.getParameter("username");
        String name = req.getParameter("name");
        String password =  req.getParameter("password");
        String pNumber = req.getParameter("phone_number");
        //对密码进行哈希算法加密
        String hashPassword = userData.encryptProcess(password);
        try{
            userData.setAffair();
            int n = userData.add(username,name,hashPassword,pNumber);
            if (n == 1)
            {
                //增添支付密码
                int count = userData.initialPayment(username);
                if (count == 1){
                    userData.commit();
                    resp.setStatus(HttpServletResponse.SC_OK);
                }else{
                    userData.rollback();
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

                }
            }else{
                userData.rollback();

                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }catch (Exception e)
        {
            userData.rollback();
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            throw new RuntimeException(e);
        }

    }

    @Override
    public void selectUsername(HttpServletRequest req, HttpServletResponse resp) {
        try {
            ResultSet resultSet = userData.selectUsername(req,resp);
            int count = 0;
            if (resultSet.next())
            {
                count++;
            }
            if (count == 1)
            {
                resp.setStatus(HttpServletResponse.SC_OK);
            }else{
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void judgment(HttpServletRequest req, HttpServletResponse resp) {
            ResultSet resultSet = userData.selectUsernameForUser(req,resp);
        try {
            if (resultSet.next())
            {
                String isAdministrator = resultSet.getString("is_administrator");
                //是网站管理员
                if ("yes".equals(isAdministrator))
                {
                    resp.setStatus(HttpServletResponse.SC_OK);

                }else{
                    //不是网站管理员
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
            }else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void selectByUsername(HttpServletRequest req, HttpServletResponse resp) {

        ResultSet resultSet = userData.selectUserByName(req,resp);
        Map<String,String> map = new HashMap<String,String>();
        try {
            if (resultSet.next())
            {
                String uid = resultSet.getString("uid");
                String userName = resultSet.getString("username");
                String name = resultSet.getString("name");
                String avatar = resultSet.getString("avatar");
                String pNumber = resultSet.getString("pnumber");
                String fund = String.valueOf(resultSet.getDouble("fund"));
                String isAdministrator = resultSet.getString("is_administrator");
                map.put("uid",uid);
                map.put("username",userName);
                map.put("name",name);
                map.put("avatar",avatar);
                map.put("pnumber",pNumber);
                map.put("fund",fund);
                map.put("is_administrator",isAdministrator);
                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(resp.getWriter(),map);
                }
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void changeInformationSimple(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        String username = req.getParameter("username");
        ResultSet resultSet = userData.selectUserByUsername(username);
        if (resultSet.next()){
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        Cookie[] cookies = req.getCookies();
        String formerly = null;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("username".equals(c.getName())) {
                    formerly = c.getValue();

                }
            }
        }
        String password = req.getParameter("password");
        String pNumber = req.getParameter("phone_number");
        String name = req.getParameter("name");
        int n = userData.changeInformationSimple(formerly,username,password,pNumber,name);
        if (n == 1)
        {//则更改成功
            resp.setStatus(HttpServletResponse.SC_OK);
        }else{

            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    public void avatarChange(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        int n = userData.changeAvatar(req,resp);
        if (n == 1 )
        {//更改成功

            resp.setStatus(HttpServletResponse.SC_OK);
        }else{

            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    public void judgementBanned(HttpServletRequest req, HttpServletResponse resp) {

        ResultSet resultSet = userData.selectUserByName(req, resp);
        String banned = null;
        try {
            if (resultSet.next()){
                banned = resultSet.getString("u_banned");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if ("yes".equals(banned)){
            //用户被封禁
            resp.setStatus(HttpServletResponse.SC_OK);

        }else {
            //用户没有被封禁
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

        }
    }

    @Override
    public void selectAllUserIsNoAdministrator(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, ClassNotFoundException {
        UserData userData = new UserDataImpl();
        ResultSet resultSet = userData.selectAllUserIsNoAdministrator();
        User user = null;
        List<User> userList = new ArrayList<User>();
        int uid = 0;
        String username = null;
        String pnumber = null;
        String u_banned = null;

        while (resultSet.next()){
            uid = resultSet.getInt("uid");
            username = resultSet.getString("username");
            pnumber = resultSet.getString("pnumber");
            u_banned = resultSet.getString("u_banned");
            user = new User(uid,username,pnumber,u_banned);
            userList.add(user);
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(),userList);
    }

    @Override
    public void selectUserByUsername(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        String username = req.getParameter("username");
        ResultSet resultSet = userData.selectUserByUsername(username);
        Map<String,String> userMap = new HashMap<String,String>();
        String uid = null;
        String pnumber = null;
        String u_banned = null;
        if (resultSet.next()){
            uid = resultSet.getString("uid");
            pnumber = resultSet.getString("pnumber");
            u_banned = resultSet.getString("u_banned");
            userMap.put("uid",uid);
            userMap.put("pnumber",pnumber);
            userMap.put("banned",u_banned);
            resp.setStatus(HttpServletResponse.SC_OK);
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(resp.getWriter(),userMap);
        }else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

        }

    }

    @Override
    public void judgementBannedForUser(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        String username = req.getParameter("username");
        ResultSet resultSet = userData.selectUserByUsername(username);
        String u_banned = null;
        if (resultSet.next()){
            u_banned = resultSet.getString("u_banned");
        }

        if ("yes".equals(u_banned)){
            //被封禁了
            resp.setStatus(HttpServletResponse.SC_OK);

        }else {
            // 设置响应内容类型为JSON
            resp.setContentType("application/json");

        }

    }

    @Override
    public void selectUserByPhoneAndUsername(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        String username = req.getParameter("username");
        String phoneNumber = req.getParameter("phoneNumber");
        ResultSet resultSet = userData.selectUserByUsernameAndPhoneNumber(username,phoneNumber);
        if (resultSet.next()){
            resp.setStatus(HttpServletResponse.SC_OK);

        }else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

        }
    }

    @Override
    public void changePassword(HttpServletRequest req, HttpServletResponse resp) {

        String username = req.getParameter("username");

        String password = req.getParameter("password");
        String hashPassword = userData.encryptProcess(password);
        int n = userData.changePassword(username,hashPassword);
        if (n == 1){
            resp.setStatus(HttpServletResponse.SC_OK);

        }else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

        }
    }

    @Override
    public void verificationCode(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int width = 100;
        int height = 50;
        //创建对象在内存中图片（验证码图片对象）
        BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        //画笔对象
        Graphics g = image.getGraphics();
        g.setColor(Color.PINK);//设置画笔颜色
        g.fillRect(0,0,width,height);
        //画边框
        g.setColor(Color.BLUE);
        g.drawRect(0,0,width-1,height-1);

        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        //生成随机角标
        Random ran = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 4 ; i++){
            int index = ran.nextInt(str.length());
            //获取字符
            char ch = str.charAt(index);//随机字符
            sb.append(ch);

            //写验证码
            g.drawString(ch+"",width/5*i,height/2);
        }
        String checkCode_session = sb.toString();
        //将验证码存入session
        req.getSession().setAttribute("checkCode_session",checkCode_session);

        //画干扰线
        g.setColor(Color.GREEN);

        //随机生成坐标
        for (int i = 0 ; i < 10 ; i++){
            int x1 = ran.nextInt(width);
            int x2 = ran.nextInt(width);

            int y1 = ran.nextInt(height);
            int y2 = ran.nextInt(height);
            g.drawLine(x1,y1,x2,y2);
        }

        //将图片输出到页面展示
        ImageIO.write(image,"jpg",resp.getOutputStream());
    }

    @Override
    public void checkCode(HttpServletRequest req, HttpServletResponse resp) {
        String checkCode = req.getParameter("checkCode");
        HttpSession session = req.getSession();
        String checkCode_session = (String) session.getAttribute("checkCode_session");
        if (checkCode_session.equalsIgnoreCase(checkCode)){
            resp.setStatus(HttpServletResponse.SC_OK);

        }else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

        }
    }

    @Override
    public void selectPhoneNumber(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        String phoneNumber = req.getParameter("phoneNumber");
        ResultSet resultSet = userData.selectPhoneNumber(phoneNumber);
        if (resultSet.next()){
            resp.setStatus(HttpServletResponse.SC_OK);
        }else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

        }
    }


}