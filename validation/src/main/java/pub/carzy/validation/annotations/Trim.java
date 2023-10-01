package pub.carzy.validation.annotations;

import pub.carzy.validation.Order;

import java.lang.annotation.*;

/**
 * 去除空格注解
 * @author admin
 */
@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Order(-100)
public @interface Trim {

}
