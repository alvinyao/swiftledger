package com.higgschain.trust.tester.dbunit;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.higgschain.trust.tester.jsonutil.JsonFileUtil;
import lombok.extern.java.Log;

import java.sql.*;

/**
 * The type Data base manager.
 *
 * @author shenqingyan
 * @create 2018 /4/16 20:23
 * @desc dealing with DB's connection
 */
@Log
public class DataBaseManager {

    /**
     * Gets mysql connection.
     *
     * @param dburl 使用指定的连接信息来连接到数据库，其中dbUrl是数据库的连接字符串，如： jdbc:mysql://localhost:3306/;DatabaseName=A_AID;user=sa;password=sa
     * @return the mysql connection
     * @desc 创建mysql连接
     */
    public Connection getMysqlConnection(String dburl) {
        String driverName = "com.mysql.jdbc.Driver";
        try {
            Class.forName(driverName);
            Connection conn = DriverManager.getConnection(dburl);
            log.info("connection success");
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("connection failed");
        }
        return null;
    }

    /**
     * Close connection.
     *
     * @param conn Connection对象
     * @desc 关闭db连接
     */
    public void closeConnection(Connection conn) {
        if (null != conn) {
            try {
                conn.close();
                log.info("close db connection");
            } catch (SQLException ex) {
                ex.printStackTrace();
                log.info("close failed");
            }
            conn = null;
        }
    }

    /**
     * Execute query json array.
     *
     * @param sql  string
     * @param conn Connection
     * @return the json array
     * @desc 执行查询sql
     */
    public JSONArray executeQuery(String sql,Connection conn) {
        ResultSet result;
        PreparedStatement pst = null;
        JSONArray jsonArray = new JSONArray();
        try {
            pst = conn.prepareStatement(sql);
            result = pst.executeQuery();
            try {
                try {
                    jsonArray = JsonFileUtil.resultSetToJson(result);
                }catch (SQLException e){
                    e.getErrorCode();
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
            log.info("excute query success");
        } catch (SQLException e) {
            log.info("execute query failed");
            e.printStackTrace();
        }
        return jsonArray;
    }

    /**
     * Execute single query json array.
     *
     * @param sql   String
     * @param dburl String
     * @return the json array
     * @desc 创建连接并执行查询sql, 并关闭数据库连接
     */
    public JSONArray executeSingleQuery(String sql, String dburl){
        DataBaseManager dataBaseManager = new DataBaseManager();
        Connection conn = dataBaseManager.getMysqlConnection(dburl);
        JSONArray jsonArray = executeQuery(sql, conn);
        dataBaseManager.closeConnection(conn);
        return jsonArray;
    }

    /**
     * Execute update.
     *
     * @param sql  string
     * @param conn connection
     * @desc 执行更新sql
     */
    public void executeUpdate(String sql,Connection conn) {
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(sql);
            pst.executeUpdate();
            log.info("execute update");
        } catch (SQLException e) {
            log.info("execute update");
            e.printStackTrace();
        }
    }

    /**
     * Execute single update.
     *
     * @param sql   String
     * @param dburl String
     * @desc 创建数据库连接 ，执行更新sql，并关闭连接
     */
    public void executeSingleUpdate(String sql, String dburl){
        DataBaseManager dataBaseManager = new DataBaseManager();
        Connection conn = dataBaseManager.getMysqlConnection(dburl);
        dataBaseManager.executeUpdate(sql,conn);
        dataBaseManager.closeConnection(conn);

    }

    /**
     * Execute delete.
     *
     * @param sql  string
     * @param conn connection
     * @desc 执行删除sql
     */
    public void executeDelete(String sql,Connection conn) {
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(sql);
            pst.executeUpdate();
            log.info("execute delete");
        } catch (SQLException e) {
            log.info("execute delete");
            e.printStackTrace();
        }
    }

    /**
     * Execute single delete.
     *
     * @param sql   String
     * @param dburl String
     * @desc 创建连接并执行删除sql ，再关闭数据库连接
     */
    public void executeSingleDelete(String sql, String dburl){
        DataBaseManager dataBaseManager = new DataBaseManager();
        Connection conn = dataBaseManager.getMysqlConnection(dburl);
        dataBaseManager.executeDelete(sql,conn);
        dataBaseManager.closeConnection(conn);
    }

    /**
     * Execute insert.
     *
     * @param sql  string
     * @param conn connection
     * @desc 执行插入语句
     */
    public void executeInsert(String sql,Connection conn) {
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(sql);
            pst.executeUpdate();
            log.info("execute insert success");
        } catch (SQLException e) {
            log.info("execute insert failed");
            e.printStackTrace();
        }
    }

    /**
     * Execute single insert.
     *
     * @param sql   String
     * @param dburl String
     * @desc 创建连接并执行insert语句 ，再关闭连接
     */
    public void executeSingleInsert(String sql, String dburl){
        DataBaseManager dataBaseManager = new DataBaseManager();
        Connection conn = dataBaseManager.getMysqlConnection(dburl);
        dataBaseManager.executeInsert(sql,conn);
        dataBaseManager.closeConnection(conn);
    }
}
