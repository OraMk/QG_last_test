package com.Dao;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.ResultSet;

public interface EnterpriseData {
    public ResultSet selectAllInPublic(HttpServletRequest req, HttpServletResponse resp);

    public ResultSet displayIntroductionById(HttpServletRequest req, HttpServletResponse resp);

    public int changeInformationSimple(HttpServletRequest req, HttpServletResponse resp);

    public int joinEnterprise(HttpServletRequest req, HttpServletResponse resp);

    public int joinLeader(HttpServletRequest req, HttpServletResponse resp);

    public int deleteEnterprise(HttpServletRequest req, HttpServletResponse resp);

    public ResultSet selectByEnterpriseName(HttpServletRequest req, HttpServletResponse resp);

    public ResultSet selectAllByUsername(HttpServletRequest req, HttpServletResponse resp);
}
