package com.higgschain.trust.tester.jsonutil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.higgschain.trust.tester.jsonutil.JsonFileUtil;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * The type Json file util test.
 */
public class JsonFileUtilTest {

    /**
     * Test read json file.
     *
     * @throws Exception the exception
     */
    @Test
    public void testReadJsonFile() throws Exception {
    }

    /**
     * Test buffered reader.
     *
     * @throws Exception the exception
     */
    @Test
    public void testBufferedReader() throws Exception {
    }

    /**
     * Test string to json object.
     *
     * @throws Exception the exception
     */
    @Test
    public void testStringToJsonObject() throws Exception {
    }

    /**
     * Test list string to json.
     *
     * @throws Exception the exception
     */
    @Test
    public void testListStringToJson() throws Exception {
    }

    /**
     * Test result set to json.
     *
     * @throws Exception the exception
     */
    @Test
    public void testResultSetToJson() throws Exception {
    }

    /**
     * Test json to map.
     *
     * @throws Exception the exception
     */
    @Test
    public void testJsonToMap() throws Exception {
        String AA = "{\"query\":{\"condition\":\"id = 1 && policy_name = 'test-policy'\",\"tablename\":\"policy\",\"listname\":\"id,policy_name\"},\"delete\":{\"condition\":\"id = 1 && policy_name = 'test-policy'\",\"tablename\":\"policy\"},\"insert\":{\"value\":\"('11', '934489785', 'test-policy', '[\\\"test1\\\",\\\"test2\\\",\\\"test3\\\"]', '2018-04-03 17:26:57.000')\",\"key\":\"`id`, `policy_id`, `policy_name`, `rs_ids`, `create_time`\",\"tablename\":\"policy\"}}";
        String BB = "{\"query\":{\"condition\":\"id = 1 && policy_name = 'test-policy'\",\"tablename\":\"policy\",\"listname\":\"id,policy_name\"},\"insert\":{\"value\":\"('11', '934489785', 'test-policy', '[\\\"test1\\\",\\\"test2\\\",\\\"test3\\\"]', '2018-04-03 17:26:57.000')\",\"key\":\"`id`, `policy_id`, `policy_name`, `rs_ids`, `create_time`\",\"tablename\":\"policy\"},\"delete\":{\"condition\":\"id = 1 && policy_name = 'test-policy'\",\"tablename\":\"policy\"}}";
        String CC = "{{\"query\":{\"condition\":\"id = 1 && policy_name = 'test-policy'\",\"tablename1\":\"policy\",\"listname\":\"id,policy_name\"}}";
        JSONObject a = JSON.parseObject(AA);
        JSONObject B = JSON.parseObject(BB);
        HashMap<String,Object> map1 = new HashMap<>();
        HashMap<String,Object> map = JsonFileUtil.jsonToMap(a, map1);
        for (String key: map.keySet()){
            System.out.println(key+":"+map.get(key));
        }
    }

    /**
     * Test json file to arry.
     *
     * @throws Exception the exception
     */
    @Test
    public void testJsonFileToArry() throws Exception {
    }
}