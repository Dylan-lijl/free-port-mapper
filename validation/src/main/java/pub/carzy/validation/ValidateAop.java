package pub.carzy.validation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pub.carzy.validation.adapter.ArrayProcessorAdapter;
import pub.carzy.validation.interfaces.ValueProcessor;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * @author admin
 */
@Aspect
@Component
@Order(Integer.MIN_VALUE / 2)
public class ValidateAop implements ApplicationRunner {

    @Resource
    @Lazy
    private ValueProcessorLoader valueProcessorLoader;

    /**
     * 注解对应的处理器
     */
    private final Map<Class<? extends Annotation>, List<ValueProcessor>> validateAfterProcessors = new HashMap<>();
    private final Map<Class<? extends Annotation>, List<ValueProcessor>> webResponseBeforeProcessors = new HashMap<>();

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)||@annotation(org.springframework.stereotype.Controller)||@annotation(org.springframework.web.bind.annotation.RestController)")
    public void pointCut() {

    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        //前置处理
        if (point.getSignature() instanceof MethodSignature) {
            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();
            Parameter[] parameters = method.getParameters();
            Object[] args = point.getArgs();
            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];
                for (Annotation annotation : parameter.getAnnotations()) {
                    List<ValueProcessor> processors = validateAfterProcessors.get(annotation.annotationType());
                    if (processors == null || processors.isEmpty()) {
                        continue;
                    }
                    ArrayProcessorAdapter adapter = new ArrayProcessorAdapter(annotation, args, i);
                    for (ValueProcessor processor : processors) {
                        processor.process(adapter);
                    }
                }
            }
            //遍历对象和数组集合map
            for (Object arg : args) {
                for (Class<? extends Annotation> key : validateAfterProcessors.keySet()) {
                    ValueProcessorUtils.process(arg, key, validateAfterProcessors);
                }
            }
        }
        //执行目标方法
        Object proceed = point.proceed(point.getArgs());
        //后置处理
        for (Class<? extends Annotation> key : webResponseBeforeProcessors.keySet()) {
            ValueProcessorUtils.process(proceed, key, webResponseBeforeProcessors);
        }
        return proceed;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        webResponseBeforeProcessors.putAll(valueProcessorLoader.filterProcessors((key, process) -> process.webResponseBefore()));
        validateAfterProcessors.putAll(valueProcessorLoader.filterProcessors((key, process) -> process.validateAfter()));
    }
}
