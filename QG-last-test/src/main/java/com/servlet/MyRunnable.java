package com.servlet;

import com.servlet.impl.LoginServletImpl;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(value = "/myRunnable")
public class MyRunnable extends BaseServlet implements Runnable {
    LoginServlet loginServlet = new LoginServletImpl();

    public MyRunnable() throws SQLException, IOException, ClassNotFoundException {
    }

    @Override
    public void run() {

    }
}