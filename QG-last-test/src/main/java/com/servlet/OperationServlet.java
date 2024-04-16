package com.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.ResultSet;

public interface OperationServlet {
    public void loginUser(HttpServletRequest req, HttpServletResponse resp);

    public void selectAll(HttpServletRequest req, HttpServletResponse resp);

    public void remainEnterpriseId(HttpServletRequest req, HttpServletResponse resp);

    public void displayIntroductionById(HttpServletRequest req, HttpServletResponse resp);
}
