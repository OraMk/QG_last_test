package com.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface OperationServlet {
    public void loginUser(HttpServletRequest req, HttpServletResponse resp);

}
