package com.higgschain.trust.consensus.p2pvalid;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

/**
 * The type P 2 p base test.
 */
@SpringBootTest(classes = P2pTestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class p2pBaseTest
        extends AbstractTestNGSpringContextTests {

    /**
     * Before class.
     */
    @BeforeSuite
    public void beforeClass() {
        System.setProperty("spring.config.location", "classpath:test-application.json");
    }

    /**
     * Run before.
     */
    @BeforeTest
    public void runBefore() {
        //JSON auto detect class type
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        //JSON不做循环引用检测
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.DisableCircularReferenceDetect.getMask();
        //JSON输出NULL属性
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.WriteMapNullValue.getMask();
        //toJSONString的时候对一级key进行按照字母排序
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.SortField.getMask();
        //toJSONString的时候对嵌套结果进行按照字母排序
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.MapSortField.getMask();
        //toJSONString的时候记录Class的name
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.WriteClassName.getMask();
        return;
    }

    /**
     * Run after.
     */
    @BeforeTest
    public void runAfter() {
        runLast();
    }

    /**
     * Run last.
     */
    protected void runLast() {
    }
}