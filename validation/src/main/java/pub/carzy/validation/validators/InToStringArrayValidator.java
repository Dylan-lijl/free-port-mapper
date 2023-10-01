package pub.carzy.validation.validators;

import pub.carzy.validation.constraints.InToStringArray;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author admin
 */
public class InToStringArrayValidator implements ConstraintValidator<InToStringArray, Object> {
    private String[] array;

    @Override
    public void initialize(InToStringArray constraintAnnotation) {
        array = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        String v = value.toString();
        for (String item : array) {
            if (item.equals(v)) {
                return true;
            }
        }
        return false;
    }
}
