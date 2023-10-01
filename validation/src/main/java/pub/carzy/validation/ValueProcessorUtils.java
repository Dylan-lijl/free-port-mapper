package pub.carzy.validation;

import org.springframework.util.ReflectionUtils;
import pub.carzy.validation.adapter.FieldProcessAdapter;
import pub.carzy.validation.interfaces.ValueProcessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author admin
 */
public class ValueProcessorUtils {

    /**
     * key:类名|注解类名
     * 值: 含有该注解的字段
     */
    public static final Map<String, List<Field>> clazzAndAnnotationCache = new ConcurrentHashMap<>();

    public static void process(Object a, Class<? extends Annotation> key,Map<Class<? extends Annotation>, List<ValueProcessor>> listMap) {
        if (a == null) {
            return;
        }
        if (a instanceof Object[]) {
            for (Object item : (Object[]) a) {
                process(item, key,listMap);
            }
        } else if (a instanceof Iterable) {
            for (Object o : (Iterable<?>) a) {
                process(o, key,listMap);
            }
        } else if (a instanceof Map) {
            for (Object value : ((Map<?, ?>) a).values()) {
                process(value, key,listMap);
            }
        } else {
            //对象处理
            if (skip(a)) {
                return;
            }
            processObject(a, key,listMap);
        }
    }

    public static void processObject(Object o, Class<? extends Annotation> key,Map<Class<? extends Annotation>, List<ValueProcessor>> listMap) {
        //对象class
        Class<?> clazz = o.getClass();
        List<ValueProcessor> processors = listMap.get(key);
        String cacheKey = clazz.getName() + "|" + key.getName();
        //并发访问需要加锁操作
        if (!clazzAndAnnotationCache.containsKey(cacheKey)){
            synchronized (clazzAndAnnotationCache) {
                if (!clazzAndAnnotationCache.containsKey(cacheKey)) {
                    List<Field> fields = new ArrayList<>();
                    //遍历含有注解的key
                    ReflectionUtils.doWithFields(clazz, fields::add, field -> field.getAnnotation(key) != null);
                    //绑定
                    clazzAndAnnotationCache.put(cacheKey, fields);
                }
            }
        }
        List<Field> fields = clazzAndAnnotationCache.get(cacheKey);
        for (Field field : fields) {
            FieldProcessAdapter adapter = new FieldProcessAdapter(field.getAnnotation(key), o, field, clazz);
            for (ValueProcessor processor : processors) {
                processor.process(adapter);
            }
            //将值传递进去递归处理
            process(adapter.gainValue(), key,listMap);
        }
    }

    /**
     * 忽略那些没意义的class
     */
    public static final Class<?>[] CLASSES = {byte.class, char.class, short.class, int.class, float.class, long.class, double.class, boolean.class,
            Byte.class, Character.class, Short.class, Integer.class, Float.class, Long.class, Double.class, Boolean.class, BigDecimal.class, BigInteger.class,
            Object.class};
    public static final String[] packages = {"java.lang"};

    public static boolean skip(Object a) {
        Class<?> aClass = a.getClass();
        //跳过包前缀
        for (String packageStr : packages) {
            if (aClass.getSimpleName().startsWith(packageStr)) {
                return true;
            }
        }
        //跳过基础类型
        for (Class<?> c : CLASSES) {
            if (aClass == c) {
                return true;
            }
        }
        return false;
    }
}
