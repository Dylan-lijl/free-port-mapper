package pub.carzy.validation.adapter;

import java.lang.annotation.Annotation;

/**
 * 适配器
 * @author admin
 */
public abstract class ValueProcessorAdapter {

    protected Annotation annotation;

    public ValueProcessorAdapter(Annotation annotation, Object target) {
        this.annotation = annotation;
        this.target = target;
    }

    protected Object target;

    public abstract boolean changeValue(Object value);

    public abstract Object gainValue();

    public Object getTarget() {
        return target;
    }

    public Annotation getAnnotation() {
        return annotation;
    }
}
