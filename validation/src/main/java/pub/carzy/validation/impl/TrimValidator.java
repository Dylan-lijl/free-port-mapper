package pub.carzy.validation.impl;

import pub.carzy.validation.adapter.ValueProcessorAdapter;
import pub.carzy.validation.annotations.Trim;
import pub.carzy.validation.interfaces.ValueProcessor;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;


/**
 * @author admin
 */
public class TrimValidator implements ValueProcessor {

    @Override
    public void process(ValueProcessorAdapter adapter) {
        Object value = adapter.gainValue();
        if (value instanceof String) {
            adapter.changeValue(((String) value).trim());
        }
    }

    @Override
    public List<Class<? extends Annotation>> processType() {
        return Collections.singletonList(Trim.class);
    }

    @Override
    public boolean validateAfter() {
        return false;
    }

    @Override
    public boolean webResponseBefore() {
        return false;
    }
}
