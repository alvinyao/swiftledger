package com.higgschain.trust.tester.dbunit;


import com.alibaba.fastjson.JSONObject;

/**
 * The type Json to sql.
 *
 * @author shenqingyan
 * @create 2018 /4/17 15:47
 * @desc translate Json to sql sentence
 */
public class JsonToSql {
    /**
     * Json to query sql string.
     *
     * @param json JSON
     * @return the string
     * @desc json转查询语句
     */
    public String jsonToQuerySql(JSONObject json){
        String sql = "select " + json.get("listname") + " from " + json.get("tablename") + " where " + json.get("condition") +";";
        return sql;
    }

    /**
     * Json to delete sql string.
     *
     * @param json the json
     * @return the string
     * @desc json转删除语句
     */
    public String jsonToDeleteSql(JSONObject json){
        String sql = "delete from " + json.get("tablename")  +  " where " + json.get("condition" +";");
        return sql;
    }

    /**
     * Json to insert sql string.
     *
     * @param json the json
     * @return the string
     * @desc json转insert语句
     */
    public String jsonToInsertSql(JSONObject json){
        String sql = "insert into " + json.get("tablename") + "(" + json.get("key") + ")" + " values " + json.get("value")+";";
        return sql;
    }

}
