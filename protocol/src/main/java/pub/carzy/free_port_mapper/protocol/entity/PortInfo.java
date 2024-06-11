package pub.carzy.free_port_mapper.protocol.entity;

import lombok.Data;

/**
 * @author admin
 */
@Data
public class PortInfo {
    private Integer protocol;
    private String host;
    private Integer port;

    public PortInfo(Integer protocol, String host, Integer port) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
    }

    public PortInfo() {
    }
}
