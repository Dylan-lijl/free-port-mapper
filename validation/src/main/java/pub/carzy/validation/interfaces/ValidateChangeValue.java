package pub.carzy.validation.interfaces;

import org.springframework.validation.Errors;

/**
 * 可校验可修改
 * @author admin
 */
public interface ValidateChangeValue {
    /**
     * 修改值,只能修改对象类型,因为方法参数没有携带注解信息
     * @param target 值
     * @param errors 错误
     */
    void change(Object target, Errors errors);
}
