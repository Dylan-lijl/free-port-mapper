package pub.carzy.validation.validators;

import pub.carzy.validation.constraints.NotInNumberArray;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author admin
 */
public class NotInNumberArrayValidator implements ConstraintValidator<NotInNumberArray, Number> {
    private double[] array;

    @Override
    public void initialize(NotInNumberArray constraintAnnotation) {
        array = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Number value, ConstraintValidatorContext context) {
        if (value != null) {
            double d = value.doubleValue();
            for (double item : array) {
                if (d == item) {
                    return false;
                }
            }
        }
        return true;
    }
}
