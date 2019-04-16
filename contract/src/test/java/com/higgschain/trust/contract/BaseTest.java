package com.higgschain.trust.contract;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * The type Base test.
 */
public abstract class BaseTest {

    /**
     * Before class.
     */
    @BeforeClass public static void beforeClass() {

    }

    /**
     * Run before.
     */
    @Before public void runBefore() {
        return;
    }

    /**
     * Run after.
     */
    @After public void runAfter() {
        runLast();
    }

    /**
     * Run last.
     */
    protected void runLast() {
    }

    /**
     * Load code from resource file string.
     *
     * @param fileName the file name
     * @return the string
     */
    protected String loadCodeFromResourceFile(String fileName) {
        try {
            try {
                return IOUtils.toString(new URI(fileName), "UTF-8");
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}