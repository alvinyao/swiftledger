package com.higgschain.trust.contract.rhino.types;

import com.higgschain.trust.contract.AccessDeniedException;
import org.mozilla.javascript.ScriptableObject;

/**
 * @author duhongming
 * @date 2018/6/11
 */
public class JsDate extends ScriptableObject {
    @Override
    public String getClassName() {
        return "Date";
    }

    public void jsConstructor() {
        throw new AccessDeniedException("Date");
    }

    private static double now() {
        return (double)System.currentTimeMillis();
    }
}
