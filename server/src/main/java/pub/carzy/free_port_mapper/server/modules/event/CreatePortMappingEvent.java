package pub.carzy.free_port_mapper.server.modules.event;

import org.springframework.context.ApplicationEvent;
import pub.carzy.free_port_mapper.server.modules.model.PortMapping;

/**
 * @author admin
 */
public class CreatePortMappingEvent extends ApplicationEvent {
    private PortMapping portMapping;

    public CreatePortMappingEvent(Object source, PortMapping portMapping) {
        super(source);
        this.portMapping = portMapping;
    }
}
