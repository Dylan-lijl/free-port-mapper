package pub.carzy.free_port_mapper.protocol.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author admin
 */
@Data
public class UpdateClientProxy implements Serializable {
    private ConnectEntity oldObj;
    private ConnectEntity newObj;
}
