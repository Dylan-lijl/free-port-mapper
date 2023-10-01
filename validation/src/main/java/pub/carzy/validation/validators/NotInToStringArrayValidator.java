package pub.carzy.validation.validators;

import pub.carzy.validation.constraints.NotInToStringArray;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author admin
 */
public class NotInToStringArrayValidator implements ConstraintValidator<NotInToStringArray, Object> {
    private String[] array;

    @Override
    public void initialize(NotInToStringArray constraintAnnotation) {
        array = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value != null) {
            String v = value.toString();
            for (String item : array) {
                if (item.equals(v)) {
                    return false;
                }
            }
        }
        return true;
    }
}
