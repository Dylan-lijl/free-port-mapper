package pub.carzy.validation.adapter;

import java.lang.annotation.Annotation;

/**
 * @author admin
 */
public class ArrayProcessorAdapter extends ValueProcessorAdapter {

    private final int index;

    public ArrayProcessorAdapter(Annotation annotation, Object target, int index) throws IllegalAccessException {
        super(annotation, target);
        if (!(target instanceof Object[])) {
            throw new IllegalAccessException();
        }
        this.index = index;
    }

    @Override
    public Object[] getTarget() {
        return (Object[]) super.getTarget();
    }

    @Override
    public boolean changeValue(Object value) {
        getTarget()[index] = value;
        return true;
    }

    @Override
    public Object gainValue() {
        return getTarget()[index];
    }
}
