package com.higgschain.trust.slave.common.util.beanvalidator;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * The type Bean validate result.
 *
 * @param <T> the type parameter
 * @author ShenTeng
 */
public class BeanValidateResult<T> {

    private boolean isSuccess;

    private Set<ConstraintViolation<T>> constraintViolations;

    /**
     * Instantiates a new Bean validate result.
     *
     * @param constraintViolations the constraint violations
     */
    public BeanValidateResult(Set<ConstraintViolation<T>> constraintViolations) {
        this.isSuccess = CollectionUtils.isEmpty(constraintViolations);
        this.constraintViolations = constraintViolations;
    }

    /**
     * Is success boolean.
     *
     * @return the boolean
     */
    public boolean isSuccess() {
        return isSuccess;
    }

    /**
     * Gets constraint violations.
     *
     * @return the constraint violations
     */
    public Set<ConstraintViolation<T>> getConstraintViolations() {
        return constraintViolations;
    }

    /**
     * Gets first msg.
     *
     * @return the first msg
     */
    public String getFirstMsg() {
        if (CollectionUtils.isEmpty(constraintViolations)) {
            return "";
        } else {
            ConstraintViolation<T> next = constraintViolations.iterator().next();
            return getMsg(next);
        }
    }

    /**
     * Fail throw.
     */
    public void failThrow() {
        if (!isSuccess()) {
            throw new IllegalArgumentException(getFirstMsg());
        }
    }

    /**
     * Fail throw.
     *
     * @param extMsg the ext msg
     */
    public void failThrow(String extMsg) {
        if (!isSuccess()) {
            throw new IllegalArgumentException(getFirstMsg() + ". " + extMsg);
        }
    }

    private String getMsg(ConstraintViolation<T> violation) {
        return violation.getPropertyPath() + " " + violation.getMessage();
    }

    @Override public String toString() {
        return new ToStringBuilder(this).append("isSuccess", isSuccess)
            .append("constraintViolations", constraintViolations).toString();
    }
}
