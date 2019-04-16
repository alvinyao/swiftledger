package com.higgschain.trust.contract.rhino;

import com.higgschain.trust.contract.ExecuteConfig;
import com.higgschain.trust.contract.QuotaExceededException;
import org.mozilla.javascript.Callable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Scriptable;

/**
 * The type Trust context factory.
 *
 * @author duhongming
 * @date 2018 /6/7
 */
public class TrustContextFactory extends ContextFactory {

    private ExecuteConfig executeConfig;

    /**
     * Install.
     */
    public static void install() {
        ContextFactory.getGlobalSetter().setContextFactoryGlobal(new TrustContextFactory());
        //ContextFactory.initGlobal(new TrustContextFactory());
    }

    /**
     * Instantiates a new Trust context factory.
     */
    public TrustContextFactory() {
//        System.out.println(" contractor TrustContextFactory");
//        System.out.println(this);
    }

    /**
     * Instantiates a new Trust context factory.
     *
     * @param executeConfig the execute config
     */
    public TrustContextFactory(ExecuteConfig executeConfig) {
        this.executeConfig = executeConfig;
    }

    @Override
    protected Context makeContext() {
        TrustContext cx = new TrustContext(this);
        cx.quota = executeConfig.getInstructionCountQuota();
        cx.setInstructionObserverThreshold(cx.quota / 2);
        return cx;
    }

    @Override
    protected void observeInstructionCount(Context cx, int instructionCount) {
        TrustContext tcx = (TrustContext) cx;
        tcx.quota -= instructionCount;
        if (tcx.quota <= 0) {
            throw new QuotaExceededException("instructionCount exceeded");
        }
    }

    @Override
    protected Object doTopCall(Callable callable, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        assert cx instanceof TrustContext;
        return super.doTopCall(callable, cx, scope, thisObj, args);
    }
}
