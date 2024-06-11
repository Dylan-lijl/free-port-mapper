package pub.carzy.free_port_mapper.server.modules.dto.inner;

import lombok.Data;

import java.util.List;

/**
 * @author admin
 */
@Data
public class ClientDetailByProcessor {
    private Long id;
    private Integer status;
    private List<PortMappingByProcessor> mappings;
}
