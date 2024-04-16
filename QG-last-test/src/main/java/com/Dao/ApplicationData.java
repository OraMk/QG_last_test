package com.Dao;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ApplicationData {

    public int addApplicationJoin(HttpServletRequest req, HttpServletResponse resp);

    public ResultSet judgmentJoin(HttpServletRequest req, HttpServletResponse resp);

    public boolean judgmentApplyJoin(HttpServletRequest req, HttpServletResponse resp) throws SQLException;

    public int deleteRelation(HttpServletRequest req, HttpServletResponse resp);

    public int addApplicationLeader(HttpServletRequest req, HttpServletResponse resp);

    public boolean judgmentApplyLeader(HttpServletRequest req, HttpServletResponse resp) throws SQLException;



}
