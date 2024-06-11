package pub.carzy.free_port_mapper.server.modules.dto.inner;

import lombok.Data;

import java.util.Date;

/**
 * @author admin
 */
@Data
public class PortMappingByProcessor {
    private Long id;
    /**
     * 协议,tcp:1,udp:2
     */
    private Integer protocol;
    /**
     * 服务端主机名
     */
    private String serverHost;
    /**
     * 服务端端口
     */
    private Integer serverPort;
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
    /**
     * 状态
     */
    private Integer state;
}
