package com.higgschain.trust.contract.rhino.function;

import org.mozilla.javascript.BaseFunction;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Math funcs.
 *
 * @author duhongming
 * @date 2018 /7/17
 */
public class MathFuncs {

    private static MathFuncs instance = new MathFuncs();

    private static Map<String, BaseFunction> funcMapping = new HashMap<>();

    static {
        funcMapping.put("add", MathNativeFunction.ADD_FUNCTION);
        funcMapping.put("subtract", MathNativeFunction.SUBTRACT_FUNCTION);
        funcMapping.put("multiply", MathNativeFunction.MULTIPLY_FUNCTION);
        funcMapping.put("divide", MathNativeFunction.DIVIDE_FUNCTION);
        funcMapping.put("eq", MathNativeFunction.EQUALS_FUNCTION);
        funcMapping.put("compare", MathNativeFunction.COMPARE_FUNCTION);
    }

    /**
     * Gets func by name.
     *
     * @param funcName the func name
     * @return the func by name
     */
    public static BaseFunction getFuncByName(String funcName) {
        BaseFunction func = funcMapping.get(funcName);
        return func;
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static MathFuncs getInstance() {
        return instance;
    }

    /**
     * The constant ZERO.
     */
    public static final BigDecimal ZERO = BigDecimal.ZERO;
    /**
     * The constant ONE.
     */
    public static final BigDecimal ONE = BigDecimal.ONE;
    /**
     * The constant TEN.
     */
    public static final BigDecimal TEN = BigDecimal.TEN;

    /**
     * The Add.
     */
    public BaseFunction add = MathNativeFunction.ADD_FUNCTION;
    /**
     * The Subtract.
     */
    public BaseFunction subtract = MathNativeFunction.SUBTRACT_FUNCTION;
    /**
     * The Multiply.
     */
    public BaseFunction multiply = MathNativeFunction.MULTIPLY_FUNCTION;
    /**
     * The Divide.
     */
    public BaseFunction divide = MathNativeFunction.DIVIDE_FUNCTION;
    /**
     * The Eq.
     */
    public BaseFunction eq = MathNativeFunction.EQUALS_FUNCTION;
    /**
     * The Compare.
     */
    public BaseFunction compare = MathNativeFunction.COMPARE_FUNCTION;
}
