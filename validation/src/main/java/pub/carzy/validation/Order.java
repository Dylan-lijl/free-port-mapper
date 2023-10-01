package pub.carzy.validation;

import java.lang.annotation.*;

/**
 *
 * @author admin
 */
@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Order {
    /**
     * 值越小排的越前面
     * @return 默认0
     */
    int value() default 0;
}
