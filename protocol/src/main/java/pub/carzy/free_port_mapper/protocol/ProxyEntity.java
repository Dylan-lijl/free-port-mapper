package pub.carzy.free_port_mapper.protocol;

import lombok.Data;


/**
 * 代理客户端与代理服务器消息交换协议
 *
 * @author admin
 *
 */
@Data
public class ProxyEntity {

    /** 消息类型 */
    private int type;

    /** 消息流水号 */
    private int serialNumber;

    /** 消息命令请求信息 */
    private String uri;

    /** 消息传输数据 */
    private byte[] data;

}
