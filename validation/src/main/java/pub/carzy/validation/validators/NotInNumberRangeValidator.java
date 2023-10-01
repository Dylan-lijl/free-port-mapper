package pub.carzy.validation.validators;

/**
 * @author admin
 */

import pub.carzy.validation.constraints.InNumberRange;
import pub.carzy.validation.constraints.NotInNumberRange;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotInNumberRangeValidator implements ConstraintValidator<NotInNumberRange, Number> {

    private double min;
    private double max;

    @Override
    public void initialize(NotInNumberRange constraintAnnotation) {
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(Number value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        double d = value.doubleValue();
        return !(min < d && d < max);
    }
}

