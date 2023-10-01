package pub.carzy.validation.impl;

import pub.carzy.validation.adapter.ArrayProcessorAdapter;
import pub.carzy.validation.adapter.ValueProcessorAdapter;
import pub.carzy.validation.annotations.Print;
import pub.carzy.validation.interfaces.ValueProcessor;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author admin
 */
public class PrintValidator implements ValueProcessor {
    @Override
    public void process(ValueProcessorAdapter adapter) {
        Print print = (Print) adapter.getAnnotation();
        if (!"".equals(print.format().trim())){

        }else{
            System.out.println(adapter.gainValue().toString());
        }
    }

    @Override
    public List<Class<? extends Annotation>> processType() {
        return Collections.singletonList(Print.class);
    }
}
