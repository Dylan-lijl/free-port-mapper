package pub.carzy.validation.adapter;

import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 字段适配器
 *
 * @author admin
 */
public class FieldProcessAdapter extends ValueProcessorAdapter {

    protected Field field;
    protected Class<?> clazz;
    protected Method setMethod;
    protected Method getMethod;
    protected boolean isFinal;
    /**
     * 方法缓存
     */
    public static final Map<String, Method> METHOD_CACHE = new ConcurrentHashMap<>();

    public FieldProcessAdapter(Annotation annotation, Object target, Field field, Class<?> clazz) {
        super(annotation, target);
        this.field = field;
        this.clazz = clazz;
        initClass();
    }

    private void initClass() {
        isFinal = Modifier.isFinal(field.getModifiers());
        String key = clazz.getSimpleName() + "#" + field.getName();
        //查找的方法缓存起来
        if (!METHOD_CACHE.containsKey("set_" + key)) {
            synchronized (METHOD_CACHE) {
                if (!METHOD_CACHE.containsKey("set_" + key)) {
                    METHOD_CACHE.put(key, ReflectionUtils.findMethod(clazz, "set" + StringUtils.capitalize(field.getName()), field.getType()));
                }
            }
        }
        if (!METHOD_CACHE.containsKey("get_" + key)) {
            synchronized (METHOD_CACHE) {
                if (!METHOD_CACHE.containsKey("get_" + key)) {
                    METHOD_CACHE.put(key, ReflectionUtils.findMethod(clazz, "get" + StringUtils.capitalize(field.getName())));
                }
            }
        }
        //缓存里面获取
        setMethod = METHOD_CACHE.get("set_" + key);
        getMethod = METHOD_CACHE.get("get_" + key);
        if (setMethod == null || getMethod == null) {
            ReflectionUtils.makeAccessible(field);
        }
    }

    @Override
    public boolean changeValue(Object value) {
        //跳过final修饰字段
        if (!isFinal) {
            if (setMethod != null) {
                ReflectionUtils.invokeMethod(setMethod, target, value);
            } else {
                ReflectionUtils.setField(field, target, value);
            }
            return true;
        }
        return false;
    }

    @Override
    public Object gainValue() {
        if (getMethod != null) {
            return ReflectionUtils.invokeMethod(getMethod, target);
        }
        return ReflectionUtils.getField(field, target);
    }

    public Field getField() {
        return field;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public Method getSetMethod() {
        return setMethod;
    }

    public Method getGetMethod() {
        return getMethod;
    }

    public boolean isFinal() {
        return isFinal;
    }

}
