package pub.carzy.validation.validators;

/**
 * @author admin
 */

import pub.carzy.validation.constraints.InNumberRange;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class InNumberRangeValidator implements ConstraintValidator<InNumberRange, Number> {

    private double min;
    private double max;
    private boolean contain;

    @Override
    public void initialize(InNumberRange constraintAnnotation) {
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
        contain = constraintAnnotation.contain();
    }

    @Override
    public boolean isValid(Number value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        double d = value.doubleValue();
        return contain ? min <= d && d <= max : min < d && d < max;
    }
}

