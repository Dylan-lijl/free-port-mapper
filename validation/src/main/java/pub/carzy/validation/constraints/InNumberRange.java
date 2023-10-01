package pub.carzy.validation.constraints;

import pub.carzy.validation.validators.InNumberArrayValidator;
import pub.carzy.validation.validators.InNumberRangeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 校验数字在指定范围内
 * @author admin
 */
@Documented
@Constraint(validatedBy = {InNumberRangeValidator.class})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface InNumberRange {

    String message() default "value must be one of {value}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean contain() default true;

    double min() default Double.NEGATIVE_INFINITY;
    double max() default Double.POSITIVE_INFINITY;
}

