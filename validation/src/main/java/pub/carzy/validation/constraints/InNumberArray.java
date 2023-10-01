package pub.carzy.validation.constraints;

import pub.carzy.validation.validators.InNumberArrayValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 校验数字在指定数组内
 * @author admin
 */
@Documented
@Constraint(validatedBy = {InNumberArrayValidator.class})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface InNumberArray {

    String message() default "value must be one of {value}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    double[] value();
}

