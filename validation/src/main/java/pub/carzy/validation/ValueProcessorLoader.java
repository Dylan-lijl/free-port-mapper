package pub.carzy.validation;

import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pub.carzy.validation.interfaces.ValueProcessor;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * @author admin
 */
@Component
@Order(Integer.MIN_VALUE)
public class ValueProcessorLoader implements ApplicationRunner, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private final Map<Class<? extends Annotation>, List<ValueProcessor>> processors = new HashMap<>();

    /**
     * 排序
     */
    private void sortBeans() {
        for (List<ValueProcessor> processors : processors.values()) {
            processors.sort(Comparator.comparingInt(ValueProcessor::order));
        }
    }

    /**
     * 根据spi进行注入
     */
    private void addBeansForSPI() {
        ServiceLoader<ValueProcessor> load = ServiceLoader.load(ValueProcessor.class);
        Iterator<ValueProcessor> iterator = load.iterator();
        if (iterator.hasNext()) {
            List<ValueProcessor> list = new ArrayList<>();
            while (iterator.hasNext()) {
                list.add(iterator.next());
            }
            addBeans(list);
        }
    }

    /**
     * 将容器里的bean添加到集合中
     */
    private void addBeansForContext() {
        Map<String, ValueProcessor> beans = applicationContext.getBeansOfType(ValueProcessor.class);
        if (!beans.isEmpty()) {
            //遍历容器对象
            addBeans(beans.values());
        }
    }

    private void addBeans(Collection<ValueProcessor> beans) {
        for (ValueProcessor processor : beans) {
            //查看可支持的注解
            List<Class<? extends Annotation>> list = processor.processType();
            //注解跟对象进行绑定
            for (Class<? extends Annotation> clazz : list) {
                List<ValueProcessor> l = processors.computeIfAbsent(clazz, key -> new ArrayList<>());
                l.add(processor);
            }
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        addBeansForContext();
        addBeansForSPI();
        //进行排序
        sortBeans();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 过滤处理器
     * @param filter 过滤器
     * @return 过滤的数据
     */
    public Map<Class<? extends Annotation>, List<ValueProcessor>> filterProcessors(Filter filter) {
        Map<Class<? extends Annotation>, List<ValueProcessor>> map = new HashMap<>();
        for (Map.Entry<Class<? extends Annotation>, List<ValueProcessor>> entry : processors.entrySet()) {
            List<ValueProcessor> list = entry.getValue();
            List<ValueProcessor> newList = new ArrayList<>();
            for (ValueProcessor p : list) {
                if (filter.accept(entry.getKey(),p)){
                    newList.add(p);
                }
            }
            if (!newList.isEmpty()){
                map.put(entry.getKey(), newList);
            }
        }
        return map;
    }

    /**
     * 过滤接口
     */
    interface Filter{
        /**
         * 是否添加
         * @param clazz 注解
         * @param processor 处理器
         * @return true则添加
         */
         boolean accept(Class<? extends Annotation> clazz,ValueProcessor processor);
    }
}
