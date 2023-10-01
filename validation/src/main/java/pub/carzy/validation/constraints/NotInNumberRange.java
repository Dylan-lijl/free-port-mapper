package pub.carzy.validation.constraints;

import pub.carzy.validation.validators.InNumberArrayValidator;
import pub.carzy.validation.validators.NotInNumberRangeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 校验数字在指定范围内
 * @author admin
 */
@Documented
@Constraint(validatedBy = {NotInNumberRangeValidator.class})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotInNumberRange {

    String message() default "value must be one of {value}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    double min() default Double.NEGATIVE_INFINITY;
    double max() default Double.POSITIVE_INFINITY;
}

