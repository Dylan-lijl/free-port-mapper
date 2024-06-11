package pub.carzy.free_port_mapper.protocol.entity;

import lombok.Data;


/**
 * @author admin
 */
@Data
public class ConnectEntity {
    /**
     * 协议
     */
    private Integer protocol;
    /**
     * 客户端主机名
     */
    private String clientHost;
    /**
     * 客户端端口
     */
    private Integer clientPort;
    /**
     * 客户端代理
     */
    private String clientProxy;
}
