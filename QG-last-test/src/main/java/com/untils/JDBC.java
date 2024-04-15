package com.untils;

import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class JDBC {
    private String driver;
    private String url;
    private String user;
    private  String  password;
    private  Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private Properties properties = new Properties();

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

    public void setPreparedStatement(PreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public JDBC() throws ClassNotFoundException, SQLException, IOException {
        //载入配置文件
        InputStream reader = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
        properties.load(reader);
        //获取配置文件中的数据
        this.driver = properties.getProperty("driver");
        this.user = properties.getProperty("user");
        this.url = properties.getProperty("url");
        this.password = properties.getProperty("password");
        //注册驱动
        Class.forName(driver);
        //获取数据库连接
        this.connection = DriverManager.getConnection(url,user,password);
    }

    public void InputSql(String sql)//create update delete select
    {
        if (sql.substring(0,6).equals("update") || sql.substring(0, 6).equals("delete") ||sql.substring(0,6).equals("insert"))
        {
            Edit(sql);

        }

        else if (sql.substring(0,6).equals("select")) {
            Select(sql);
        }
        else {
            System.out.println("sql语句输入错误");

        }
    }
    public int Edit(String sql)  {

        try {
            //注册数据库操作对象
            preparedStatement = connection.prepareStatement(sql);
            //执行sql语句
            int n = preparedStatement.executeUpdate();
            return n;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



    }
    public ResultSet Select(String sql)
    {
        ResultSetMetaData metaData = null;
        try {
            //注册数据库操作对象
            preparedStatement = connection.prepareStatement(sql);
            //执行sql语句并获取查询结构集
            resultSet = preparedStatement.executeQuery();
            return resultSet;
//            metaData = resultSet.getMetaData();
//            int columnCount = metaData.getColumnCount();
//            boolean title =false;
//            while(resultSet.next())
//            {
//                for (int i = 1 ; i <=columnCount ; i++)
//                {
//                    if (title==false)
//                    {
//                        for (int j = 1 ; j <=columnCount ; j++){
//                        System.out.print("\t\t" + metaData.getColumnName(i)  );}
//                        System.out.println();
//                    }
//                    System.out.print("\t\t" + resultSet.getString(i) );
//                    title = true;
//                }
//                System.out.println();
//            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }




    }

    public ResultSet UserSelect(HttpServletRequest req)
    {
        ResultSetMetaData metaData = null;
        try {
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            String sql = "select * from User where username = ? and password = ?";
            //注册数据库操作对象
            preparedStatement = connection.prepareStatement(sql);
            //执行sql语句并获取查询结构集
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);
            resultSet = preparedStatement.executeQuery();
            return resultSet;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }




    }
    public void close()
    {
        if (resultSet == null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (preparedStatement == null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }if (connection == null) {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    }

}
