package pub.carzy.validation.annotations;

import pub.carzy.validation.Order;

import java.lang.annotation.*;

/**
 * 打印
 *
 * @author admin
 */
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Order(Integer.MAX_VALUE)
public @interface Print {
    /**
     * 格式化
     *
     * @return 格式化
     */
    String format() default "";

}
