package pub.carzy.free_port_mapper.server.modules.event;

import lombok.Data;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import pub.carzy.free_port_mapper.server.modules.model.PortMapping;

/**
 * @author admin
 */
@Getter
public class UpdatePortMappingEvent extends ApplicationEvent {
    private PortMapping old;
    private PortMapping newObj;

    public UpdatePortMappingEvent(Object source, PortMapping old, PortMapping newObj) {
        super(source);
        this.old = old;
        this.newObj = newObj;
    }
}
