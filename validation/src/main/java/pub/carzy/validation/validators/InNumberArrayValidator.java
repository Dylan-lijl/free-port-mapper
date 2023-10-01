package pub.carzy.validation.validators;

/**
 * @author admin
 */
import pub.carzy.validation.constraints.InNumberArray;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class InNumberArrayValidator implements ConstraintValidator<InNumberArray, Number> {

    private double[] array;

    @Override
    public void initialize(InNumberArray constraintAnnotation) {
        array = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Number value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        double d = value.doubleValue();
        for (double item : array) {
            if (d == item) {
                return true;
            }
        }
        return false;
    }
}

