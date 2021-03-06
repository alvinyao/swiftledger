package com.higgschain.trust.contract.rhino.function;

import com.higgschain.trust.contract.rhino.SafeNativeJavaObject;
import com.higgschain.trust.contract.rhino.types.BigDecimalWrap;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * The type Math native function.
 *
 * @author duhongming
 * @date 2018 /9/10
 */
public class MathNativeFunction {

    /**
     * The constant ADD_FUNCTION.
     */
    protected static AddFunction ADD_FUNCTION = new AddFunction();
    /**
     * The constant SUBTRACT_FUNCTION.
     */
    protected static SubtractFunction SUBTRACT_FUNCTION = new SubtractFunction();
    /**
     * The constant MULTIPLY_FUNCTION.
     */
    protected static MultiplyFunction MULTIPLY_FUNCTION = new MultiplyFunction();
    /**
     * The constant DIVIDE_FUNCTION.
     */
    protected static DivideFunction DIVIDE_FUNCTION = new DivideFunction();
    /**
     * The constant EQUALS_FUNCTION.
     */
    protected static EqualsFunction EQUALS_FUNCTION = new EqualsFunction();
    /**
     * The constant COMPARE_FUNCTION.
     */
    protected static CompareFunction COMPARE_FUNCTION = new CompareFunction();

    /**
     * To big decimal big decimal.
     *
     * @param obj the obj
     * @return the big decimal
     */
    public static BigDecimal toBigDecimal(Object obj) {
        if (obj instanceof SafeNativeJavaObject) {
            obj = ((SafeNativeJavaObject) obj).unwrap();
        }
        if (obj instanceof BigDecimalWrap) {
            return ((BigDecimalWrap) obj).getRawBigDecimal();
        }
        if (obj instanceof BigDecimal) {
            return (BigDecimal) obj;
        }
        if (obj instanceof Double) {
            return new BigDecimal((Double) obj);
        }
        if (obj instanceof BigInteger) {
            return new BigDecimal((BigInteger) obj);
        }
        if (obj instanceof Integer) {
            return new BigDecimal((Integer) obj);
        }

        return new BigDecimal(obj.toString());
    }

    private static class AddFunction extends BaseFunction {
        @Override
        public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
            if (thisObj instanceof BigDecimalWrap) {
                return new BigDecimalWrap(((BigDecimalWrap) thisObj).add(args[0]));
            }
            BigDecimal result = toBigDecimal(args[0]).add(toBigDecimal(args[1]));
            if (args.length > 2) {

                for(int i = 2; i < args.length; i++) {
                    result = result.add(toBigDecimal(args[i]));
                }
            }
            return result;
        }
    }

    private static class SubtractFunction extends BaseFunction {
        @Override
        public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
            if (thisObj instanceof BigDecimalWrap) {
                return new BigDecimalWrap(((BigDecimalWrap) thisObj).subtract(args[0]));
            }
            BigDecimal result = toBigDecimal(args[0]).subtract(toBigDecimal(args[1]));
            if (args.length > 2) {
                for(int i = 2; i < args.length; i++) {
                    result = result.subtract(toBigDecimal(args[i]));
                }
            }
            return result;
        }
    }

    private static class MultiplyFunction extends BaseFunction {
        @Override
        public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
            if (thisObj instanceof BigDecimalWrap) {
                return new BigDecimalWrap(((BigDecimalWrap) thisObj).multiply(args[0]));
            }
            BigDecimal result = toBigDecimal(args[0]).multiply(toBigDecimal(args[1]));
            if (args.length > 2) {
                for(int i = 2; i < args.length; i++) {
                    result = result.multiply(toBigDecimal(args[i]));
                }
            }
            return result;
        }
    }

    private static class DivideFunction extends BaseFunction {
        @Override
        public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
            if (thisObj instanceof BigDecimalWrap) {
                return new BigDecimalWrap(((BigDecimalWrap) thisObj).divide(args[0]));
            }

            BigDecimal result = toBigDecimal(args[0]).divide(toBigDecimal(args[1]));
            if (args.length > 2) {
                for (int i = 2; i < args.length; i++) {
                    result = result.divide(toBigDecimal(args[i]));
                }
            }
            return result;
        }
    }

    private static class EqualsFunction extends BaseFunction {
        @Override
        public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
            if (thisObj instanceof BigDecimalWrap) {
                return ((BigDecimalWrap) thisObj).eq(args[0]);
            }

            return toBigDecimal(args[0]).compareTo(toBigDecimal(args[1])) == 0;
        }
    }

    private static class CompareFunction extends BaseFunction {
        @Override
        public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
            if (thisObj instanceof BigDecimalWrap) {
                return toBigDecimal(thisObj).compareTo(toBigDecimal(args[0]));
            }

            return toBigDecimal(args[0]).compareTo(toBigDecimal(args[1]));
        }
    }

}
