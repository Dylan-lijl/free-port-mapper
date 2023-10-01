package pub.carzy.validation.constraints;

import pub.carzy.validation.validators.InToStringArrayValidator;
import pub.carzy.validation.validators.NotInToStringArrayValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author admin
 */
@Documented
@Constraint(validatedBy = {NotInToStringArrayValidator.class})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotInToStringArray {
    String message() default "value must be one of {value}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] value();
}
