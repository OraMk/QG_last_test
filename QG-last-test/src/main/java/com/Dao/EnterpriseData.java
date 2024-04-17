package com.Dao;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.ResultSet;

public interface EnterpriseData {
    public ResultSet selectAll(HttpServletRequest req, HttpServletResponse resp);

    public ResultSet displayIntroductionById(HttpServletRequest req, HttpServletResponse resp);

    public int changeInformationSimple(HttpServletRequest req, HttpServletResponse resp);

    public int joinEnterprise(HttpServletRequest req, HttpServletResponse resp);
}
