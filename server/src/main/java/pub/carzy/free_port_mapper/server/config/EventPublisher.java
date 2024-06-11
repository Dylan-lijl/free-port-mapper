package pub.carzy.free_port_mapper.server.config;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author admin
 */
@Component
public class EventPublisher {
    @Resource
    private ApplicationEventPublisher eventPublisher;

    public void publishEvent(Object obj) {
        eventPublisher.publishEvent(obj);
    }
}
