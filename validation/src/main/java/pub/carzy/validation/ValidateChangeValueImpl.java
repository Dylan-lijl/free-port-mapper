package pub.carzy.validation;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import pub.carzy.validation.interfaces.ValidateChangeValue;
import pub.carzy.validation.interfaces.ValueProcessor;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author admin
 */
@Component
@Order(Integer.MIN_VALUE / 2)
public class ValidateChangeValueImpl implements ValidateChangeValue, ApplicationRunner {

    @Resource
    @Lazy
    private ValueProcessorLoader valueProcessorLoader;

    private final Map<Class<? extends Annotation>, List<ValueProcessor>> processors = new HashMap<>();

    @Override
    public void change(Object target, Errors errors) {
        //后置处理
        for (Class<? extends Annotation> key : processors.keySet()) {
            ValueProcessorUtils.process(target, key, processors);
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        processors.putAll(valueProcessorLoader.filterProcessors((key, process) -> process.validateBefore()));
    }
}
