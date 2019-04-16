package com.higgschain.trust.slave.common.util.beanvalidator;

import com.google.common.base.Preconditions;
import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * The type Bean validator.
 *
 * @author ShenTeng
 */
public class BeanValidator {

    // validator is thread-safe
    private static Validator validator;

    static {
        validator = Validation.byProvider(HibernateValidator.class).configure().failFast(true).buildValidatorFactory()
            .getValidator();
    }

    private BeanValidator() {
    }

    /**
     * Gets validator.
     *
     * @return the validator
     */
    public static Validator getValidator() {
        return validator;
    }

    /**
     * Validate bean validate result.
     *
     * @param <T>    the type parameter
     * @param object the object
     * @return the bean validate result
     */
    public static <T> BeanValidateResult<T> validate(T object) {
        Preconditions.checkNotNull(object, "validate object is null");
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(object);
        return new BeanValidateResult<>(constraintViolations);
    }

    /**
     * Validate bean validate result.
     *
     * @param <T>    the type parameter
     * @param object the object
     * @param groups the groups
     * @return the bean validate result
     */
    public static <T> BeanValidateResult<T> validate(T object, Class<?>... groups) {
        Preconditions.checkNotNull(object, "validate object is null");
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(object, groups);
        return new BeanValidateResult<>(constraintViolations);
    }
}
