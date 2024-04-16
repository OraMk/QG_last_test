package com.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface InteractionServlet {
    //添加申请
    public void applyToJoin(HttpServletRequest req, HttpServletResponse resp);

    //判断是否属于该企业
    public void judgmentJoin(HttpServletRequest req, HttpServletResponse resp);
    //退出企业
    public void deleteRelation(HttpServletRequest req, HttpServletResponse resp);

    public void applyToLeader(HttpServletRequest req, HttpServletResponse resp);

    public void judgmentLeader(HttpServletRequest req, HttpServletResponse resp);
}
