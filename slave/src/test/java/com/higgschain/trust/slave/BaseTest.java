package com.higgschain.trust.slave;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.higgschain.trust.slave.core.service.snapshot.SnapshotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

/**
 * The type Base test.
 */
@SpringBootTest
public abstract class BaseTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private SnapshotService snapshotService;

    /**
     * Before class.
     *
     * @throws Exception the exception
     */
    @BeforeSuite
    public void beforeClass() throws Exception {
        System.setProperty("spring.config.location", "classpath:test-application.json");

    }

    /**
     * Run before.
     */
    @BeforeClass
    public void runBefore() {
        System.setProperty("spring.config.location", "classpath:test-application.json");
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
    }

    /**
     * Run after.
     */
    @AfterClass
    public void runAfter() {
        runLast();
    }

    /**
     * Run last.
     */
    protected void runLast() {
    }
}