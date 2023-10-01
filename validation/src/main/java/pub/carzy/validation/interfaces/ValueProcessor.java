package pub.carzy.validation.interfaces;

import pub.carzy.validation.adapter.ValueProcessorAdapter;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @author admin
 */
public interface ValueProcessor {
    /**
     * 处理
     * @param adapter 参数
     */
    void process (ValueProcessorAdapter adapter);

    /**
     * 能够处理的类型
     * @return 能够处理的类型
     */
    List<Class<? extends Annotation>> processType();

    /**
     * 排序(值越小就越前面)
     * @return 默认返回最大值
     */
    default int order(){
        return Integer.MAX_VALUE;
    }

    /**
     * 校验前是否可以处理
     * @return 是否可以处理
     */
    default boolean validateBefore(){
        return true;
    }

    /**
     * 检验后是否可以处理
     * @return 是否可以处理
     */
    default boolean validateAfter(){
        return true;
    }

    /**
     * 返回时是否可以处理
     * @return 是否可以处理
     */
    default boolean webResponseBefore(){
        return true;
    }
}
